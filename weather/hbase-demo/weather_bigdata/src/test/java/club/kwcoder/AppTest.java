package club.kwcoder;



import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {

//        Method maxTemperature = HBaseUtils.getGetMethod(ForecastWritable.class, "maxTemperature");
//        System.out.println(maxTemperature);

//        Scan scan = new Scan();
//        Table table = HBaseUtils.getConnection().getTable(TableName.valueOf(WeatherWritable.tableName));
//        ResultScanner scanner = table.getScanner(scan);
//        HBaseUtils.show(WeatherWritable.class, scanner, WeatherWritable.family);

//        Scan scan = new Scan();
//        Table table = HBaseUtils.getConnection().getTable(TableName.valueOf(WeatherWritable.tableName));
//        ResultScanner results = table.getScanner(scan);
//
//        Map<byte[], Class<?>> privateFieldClassBytesKey = FieldClassUtils.getPrivateFieldClassBytesKey(WeatherWritable.class);
//
//        long start = System.currentTimeMillis();

        // 2572  20000
        // 284170  3832157
//         HBaseUtils.show(results, WeatherWritable.family, String.class, privateFieldClassBytesKey);

        // 1714  20000
        // 166979   3832157
//        int count = 0;
//        for (Result result : results) {
//            HBaseUtils.getInstance(WeatherWritable.class, result, WeatherWritable.family);
//            count++;
//        }
//        System.out.println(count);
//        long end = System.currentTimeMillis();
//
//        System.out.println(end - start);


    }


}


