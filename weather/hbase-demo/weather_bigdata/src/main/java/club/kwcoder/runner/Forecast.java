package club.kwcoder.runner;

import club.kwcoder.WeatherStart;
import club.kwcoder.writable.ForecastWritable;
import club.kwcoder.writable.WeatherWritable;
import io.github.zhinushannan.util.hbase.HBaseUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Forecast {

    public static void exec(Job job, TableName inputTable, TableName outputTable) {

        try {
            Scan tableScan = new Scan();
            TableMapReduceUtil.initTableMapperJob(inputTable, tableScan, ForecastMapper.class, Text.class, ForecastWritable.class, job);
            TableMapReduceUtil.initTableReducerJob(outputTable.getNameAsString(), ForecastReducer.class, job);
            boolean flag = job.waitForCompletion(true);
            if (flag) {
                System.out.println(job.getJobName() + " process success");
            }
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static class ForecastMapper extends TableMapper<Text, ForecastWritable> {
        private final Text outKey = new Text();

        @Override
        protected void map(ImmutableBytesWritable key, Result value, Mapper<ImmutableBytesWritable, Result, Text, ForecastWritable>.Context context) throws IOException, InterruptedException {

            try {
                WeatherWritable weatherWritable = HBaseUtils.getInstance(WeatherWritable.class, value, WeatherStart.family_global);

                ForecastWritable outVal = ForecastWritable
                        .builder()
                        .code(weatherWritable.getCode())
                        .date(weatherWritable.getDate().substring(0, 5))
                        .maxTemperature(weatherWritable.getMaxTemperature())
                        .minTemperature(weatherWritable.getMinTemperature())
                        .avgTemperature(weatherWritable.getAvgTemperature())
                        .precipitation(weatherWritable.getPrecipitation())
                        .build();

                outKey.set(outVal.getCode() + "_" + outVal.getDate());

                context.write(outKey, outVal);

            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }

    private static class ForecastReducer extends TableReducer<Text, ForecastWritable, NullWritable> {

        @Override
        protected void reduce(Text key, Iterable<ForecastWritable> values, Reducer<Text, ForecastWritable, NullWritable, Mutation>.Context context) throws IOException, InterruptedException {
            String code = "", date = "";
            double maxTemp = 0.0, minTemp = 0.0, avgTemp = 0.0, precipitation = 0.0;
            int maxTempSum = 0, minTempSum = 0, avgTempSum = 0, precipitationSum = 0;
            for (ForecastWritable value : values) {

                if (StringUtils.isBlank(code) || StringUtils.isBlank(date)) {
                    code = value.getCode();
                    date = value.getDate();
                }

                maxTemp += value.getMaxTemperature() == 0.0 ? 0.0 : value.getMaxTemperature();
                maxTempSum += value.getMaxTemperature() == 0.0 ? 0 : 1;

                minTemp += value.getMinTemperature() == 0.0 ? 0.0 : value.getMinTemperature();
                minTempSum += value.getMinTemperature() == 0.0 ? 0 : 1;

                avgTemp += value.getAvgTemperature() == 0.0 ? 0.0 : value.getAvgTemperature();
                avgTempSum += value.getAvgTemperature() == 0.0 ? 0 : 1;

                precipitation += value.getPrecipitation() == 0.0 ? 0.0 : value.getPrecipitation();
                precipitationSum += value.getPrecipitation() == 0.0 ? 0 : 1;
            }

            ForecastWritable build = ForecastWritable
                    .builder()
                    .code(code)
                    .date(date)
                    .maxTemperature(Math.round(maxTemp / maxTempSum * 100) / 100.0)
                    .minTemperature(Math.round(minTemp / minTempSum * 100) / 100.0)
                    .avgTemperature(Math.round(avgTemp / avgTempSum * 100) / 100.0)
                    .precipitation(Math.round(precipitation / precipitationSum * 100) / 100.0)
                    .build();

            try {
                Put put = HBaseUtils.getPut(Bytes.toBytes(key.toString()), WeatherStart.family_global, build);
                context.write(NullWritable.get(), put);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }


        }

    }

}
