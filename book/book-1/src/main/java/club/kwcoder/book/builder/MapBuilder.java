package club.kwcoder.book.builder;

import java.util.HashMap;
import java.util.Map;

/**
 * Map集合建造者模式类
 *
 * @param <K> Map的key类型
 * @param <V> Map的value类型
 * @author zhinushannan
 */
public class MapBuilder<K, V> {

    /**
     * 建造者建造的map集合
     */
    private final Map<K, V> conditions = new HashMap<>();

    /**
     * 获取建造者的对象
     *
     * @param <K> Map的key类型
     * @param <V> Map的value类型
     * @return 返回建造者对象
     */
    public static <K, V> MapBuilder<K, V> builder() {
        return new MapBuilder<>();
    }

    /**
     * 向map中添加数据
     *
     * @param key   键数据
     * @param value 值数据
     * @return 返回建造者对象
     */
    public MapBuilder<K, V> put(K key, V value) {
        conditions.put(key, value);
        return this;
    }

    /**
     * 建造完成，获取map集合
     *
     * @return 返回map集合
     */
    public Map<K, V> build() {
        return conditions;
    }

}
