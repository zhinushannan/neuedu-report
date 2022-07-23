package hadoop.club.kwcoder.weather.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 校验工具类，统一返回方式，针对本项目，合法返回false，非法返回true
 *
 * @author zhinushannan
 */
public class ValidateUtils {

    /**
     * 校验输入文本数据
     *
     * @param line 文本数据
     * @return 空返回true
     */
    public static boolean validate(String line) {
        return StringUtils.isBlank(line);
    }

    /**
     * 校验字符串数组长度
     *
     * @param items  字符串数组
     * @param length 长度
     * @return 不相同返回true
     */
    public static boolean validate(String[] items, int length) {
        return items.length != length;
    }


}
