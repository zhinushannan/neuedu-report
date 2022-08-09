package club.kwcoder.book.builder;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder {

    private final Map<String, Object> conditions = new HashMap<>();

    public static MapBuilder builder() {
        return new MapBuilder();
    }

    public MapBuilder put(String key, Object value) {
        conditions.put(key, value);
        return this;
    }

    public Map<String, Object> build() {
        return conditions;
    }

}
