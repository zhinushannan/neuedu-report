package club.kwcoder.weather.util;

import org.apache.hadoop.conf.Configuration;

/**
 * Hadoop 工具类，用于获取hadoop配置对象
 *
 * @author zhinushannan
 */
public class HadoopUtils {

    /**
     * hadoop配置
     */
    private static final Configuration conf;

    static {
        conf = new Configuration();
    }

    public static Configuration getConf() {
        return conf;
    }


}
