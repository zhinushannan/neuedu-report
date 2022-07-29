package club.kwcoder.runner;


import club.kwcoder.WeatherStart;
import club.kwcoder.writable.WeatherWritable;
import club.kwcoder.writable.WeatherSummaryWritable;
import io.github.zhinushannan.util.hbase.HBaseUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class YearSummary {

    public static void exec(Job job, TableName inputTable, TableName outputTable) {

        try {
            Scan scanMapReduce = new Scan();
            TableMapReduceUtil.initTableMapperJob(inputTable.getNameAsString(), scanMapReduce, YearSummaryMapper.class, Text.class, WeatherSummaryWritable.class, job);
            TableMapReduceUtil.initTableReducerJob(outputTable.getNameAsString(), YearSummaryReducer.class, job);
            boolean flag = job.waitForCompletion(true);
            if (flag) {
                System.out.println("year summary process success");
            }
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static class YearSummaryMapper extends TableMapper<Text, WeatherSummaryWritable> {
        private final Text outKey = new Text();

        @Override
        protected void map(ImmutableBytesWritable key, Result value, Mapper<ImmutableBytesWritable, Result, Text, WeatherSummaryWritable>.Context context) throws IOException, InterruptedException {
            try {
                WeatherWritable weatherWritable = HBaseUtils.getInstance(WeatherWritable.class, value, WeatherStart.family_global);

                WeatherSummaryWritable outVal = WeatherSummaryWritable.builder()
                        .code(weatherWritable.getCode())
                        .year(weatherWritable.getDate().split("/")[2])
                        .precipitationTotal(weatherWritable.getPrecipitation())
                        .rainDays(weatherWritable.getPrecipitation() == 0.0 ? 0 : 1)
                        .maxTemperature(weatherWritable.getMaxTemperature())
                        .minTemperature(weatherWritable.getMinTemperature())
                        .avgTemperature(weatherWritable.getAvgTemperature()).build();

                outKey.set(outVal.getCode() + "_" + outVal.getYear());
                context.write(outKey, outVal);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                     InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private static class YearSummaryReducer extends TableReducer<Text, WeatherSummaryWritable, NullWritable> {
        @Override
        protected void reduce(Text key, Iterable<WeatherSummaryWritable> values, Reducer<Text, WeatherSummaryWritable, NullWritable, Mutation>.Context context) throws InterruptedException {
            WeatherSummaryWritable weatherSummary = new WeatherSummaryWritable();
            weatherSummary.setMaxTemperature(Double.MIN_VALUE);
            weatherSummary.setMinTemperature(Double.MAX_VALUE);

            int avgTempNum = 0;
            // 计算年降雨量、年最高气温、年最低气温、降雨天数
            for (WeatherSummaryWritable value : values) {
                if (StringUtils.isBlank(weatherSummary.getCode()) || StringUtils.isBlank(weatherSummary.getYear())) {
                    weatherSummary.setCode(value.getCode());
                    weatherSummary.setYear(value.getYear());
                }
                weatherSummary.setPrecipitationTotal(weatherSummary.getPrecipitationTotal() + value.getPrecipitationTotal());

                weatherSummary.setMaxTemperature(Math.max(weatherSummary.getMaxTemperature(), value.getMaxTemperature()));
                if (value.getMinTemperature() != 0.0) {
                    weatherSummary.setMinTemperature(Math.min(weatherSummary.getMinTemperature(), value.getMinTemperature()));
                }

                weatherSummary.setRainDays(weatherSummary.getRainDays() + value.getRainDays());
                if (value.getAvgTemperature() != 0.0) {
                    weatherSummary.setAvgTemperature(weatherSummary.getAvgTemperature() + value.getAvgTemperature());
                    avgTempNum++;
                }
            }

            weatherSummary.setAvgTemperature(weatherSummary.getAvgTemperature() / avgTempNum);

            try {
                Put put = HBaseUtils.getPut(Bytes.toBytes(weatherSummary.getYear()), WeatherStart.family_global, weatherSummary);
                context.write(NullWritable.get(), put);
            } catch (IOException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }


        }
    }

}
