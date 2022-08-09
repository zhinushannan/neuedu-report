package club.kwcoder.book.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Map;

public abstract class CriteriaUtils {

    public static Criteria getLikeCriteria(Map<String, Object> conditions) {
        Criteria criteria = new Criteria();
        conditions.forEach((key, value) -> {
            if (value instanceof String && StringUtils.isNotBlank((String) value)) {
                criteria.and(key).regex("^.*" + value + ".*$");
            }
        });
        return criteria;
    }

}
