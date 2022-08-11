package club.kwcoder;

import club.kwcoder.runner.DataCleaning;
import club.kwcoder.runner.Forecast;
import club.kwcoder.runner.YearSummary;
import club.kwcoder.writable.ForecastWritable;
import club.kwcoder.writable.WeatherWritable;
import club.kwcoder.writable.WeatherSummaryWritable;
import io.github.zhinushannan.util.hbase.HBaseUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class WeatherStart {

    public static final byte[] family_global = Bytes.toBytes("data");

    public static void main(String[] args) {
        DataCleaning.exec();
        run(Forecast.class, WeatherWritable.class, ForecastWritable.class);
        run(YearSummary.class, WeatherWritable.class, WeatherSummaryWritable.class);
    }

    public static void run(Class<?> runnerCls, Class<? extends Writable> inputCls, Class<? extends Writable> outputCls) {
        TableName inputTable = TableName.valueOf(Bytes.toBytes(inputCls.getSimpleName().toLowerCase().replace("writable", "")));
        TableName outputTable = TableName.valueOf(Bytes.toBytes(outputCls.getSimpleName().toLowerCase().replace("writable", "")));

        try {
            if (!HBaseUtils.getAdmin().tableExists(inputTable)) {
                return;
            }
            HBaseUtils.deleteTaleIfExist(outputTable);
            HBaseUtils.getConf().set(TableOutputFormat.OUTPUT_TABLE, outputTable.getNameAsString());
            HBaseUtils.getAdmin().createTable(TableDescriptorBuilder.newBuilder(outputTable).setColumnFamily(ColumnFamilyDescriptorBuilder.of(family_global)).build());

            Job job = Job.getInstance(HBaseUtils.getConf(), runnerCls.getSimpleName());
            runnerCls.getMethod("exec", Job.class, TableName.class, TableName.class).invoke(null, job, inputTable, outputTable);

            Scan scan = new Scan();
            Table table = HBaseUtils.getConnection().getTable(outputTable);
            ResultScanner results = table.getScanner(scan);
            HBaseUtils.getInstances(outputCls, results, Bytes.toBytes("data"));

        } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                 InstantiationException e) {
            e.printStackTrace();
        } finally {
            HBaseUtils.close();
        }


    }

}
