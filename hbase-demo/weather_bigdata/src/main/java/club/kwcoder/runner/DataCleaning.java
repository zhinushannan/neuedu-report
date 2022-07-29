package club.kwcoder.runner;

import club.kwcoder.WeatherStart;
import club.kwcoder.writable.WeatherWritable;
import io.github.zhinushannan.util.hbase.HBaseUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class DataCleaning {

    public static void exec() {
        try {
            Admin admin = HBaseUtils.getAdmin();
            TableName weather = TableName.valueOf(WeatherWritable.tableName);
            HBaseUtils.deleteTaleIfExist(weather);
            TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(weather).setColumnFamily(ColumnFamilyDescriptorBuilder.of(WeatherStart.family_global)).build();
            admin.createTable(tableDescriptor);
            HBaseUtils.getConf().set(TableOutputFormat.OUTPUT_TABLE, weather.getNameAsString());

            Job job = Job.getInstance(HBaseUtils.getConf(), "data import");
            // 设置输入
            job.setInputFormatClass(TextInputFormat.class);
            FileInputFormat.setInputPaths(job, new Path("hdfs://master:9000/weather/*.csv"));
            // 设置Mapper
            job.setMapperClass(DataCleaningMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(WeatherWritable.class);
            // 设置Reducer
            job.setReducerClass(DataCleaningReducer.class);
            // 设置输出
            job.setOutputFormatClass(TableOutputFormat.class);
            // 运行
            boolean flag = job.waitForCompletion(true);
            if (flag) {
                System.out.println("Data Cleaning Process Success");
            }

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            HBaseUtils.close();
        }

    }

    private static class DataCleaningMapper extends Mapper<LongWritable, Text, Text, WeatherWritable> {
        private final Text outKey = new Text();

        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, WeatherWritable>.Context context) throws IOException, InterruptedException {
            String line = value.toString();
            if (StringUtils.isBlank(line)) {
                return;
            }
            if (key.get() == 0L && line.startsWith("Estacao")) {
                return;
            }
            if (!line.contains("83377")) {
                return;
            }
            String[] items = line.split(";", 19);
            if (items.length != 19) {
                return;
            }
            WeatherWritable outVal = WeatherWritable.builder()
                    .code(items[0])
                    .date(items[1])
                    .precipitation(StringUtils.isBlank(items[3]) ? 0.0 : Double.parseDouble(items[3]))
                    .maxTemperature(StringUtils.isBlank(items[6]) ? 0.0 : Double.parseDouble(items[6]))
                    .minTemperature(StringUtils.isBlank(items[7]) ? 0.0 : Double.parseDouble(items[7]))
                    .avgTemperature(StringUtils.isBlank(items[16]) ? 0.0 : Double.parseDouble(items[16]))
                    .build();

            outKey.set(items[0] + "_" + items[1]);
            context.write(outKey, outVal);
        }
    }

    private static class DataCleaningReducer extends TableReducer<Text, WeatherWritable, NullWritable> {
        @Override
        protected void reduce(Text key, Iterable<WeatherWritable> values, Reducer<Text, WeatherWritable, NullWritable, Mutation>.Context context) throws IOException, InterruptedException {
            WeatherWritable weather = new WeatherWritable();
            weather.setCode(key.toString().split("_")[0]);
            weather.setDate(key.toString().split("_")[1]);
            for (WeatherWritable value : values) {
                weather.setMaxTemperature(weather.getMaxTemperature() + value.getMaxTemperature());
                weather.setMinTemperature(weather.getMinTemperature() + value.getMinTemperature());
                weather.setAvgTemperature(weather.getAvgTemperature() + value.getAvgTemperature());
                weather.setPrecipitation(weather.getPrecipitation() + value.getPrecipitation());
            }

            if (weather.getMinTemperature() == 0.0 || weather.getMaxTemperature() == 0.0 || weather.getAvgTemperature() == 0.0) {
                return;
            }

            try {
                Put put = HBaseUtils.getPut(Bytes.toBytes(key.toString()), WeatherStart.family_global, weather);
                context.write(NullWritable.get(), put);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

}
