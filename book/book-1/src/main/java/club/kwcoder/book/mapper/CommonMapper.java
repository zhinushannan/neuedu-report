package club.kwcoder.book.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 通用查询
 */
@Component
public abstract class CommonMapper<T> {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 保存对象
     *
     * @param entity 实体类对象
     * @return 返回保存的对象
     */
    public T save(T entity) {
        return mongoTemplate.save(entity, entity.getClass().getSimpleName().toLowerCase());
    }

    /**
     * 根据条件查询
     *
     * @param entityClass 实体类对象的class
     * @param conditions  条件集合
     * @return 返回查询结果
     */
    public List<T> findByConditions(Class<T> entityClass, Map<String, Object> conditions) {
        List<T> list = mongoTemplate.find(getQueryByConditions(conditions), entityClass, entityClass.getSimpleName().toLowerCase());
        if (list.size() == 0) {
            return null;
        }
        return list;
    }

    /**
     * 分页查询
     *
     * @param entityClass 实体类对象的class
     * @param page        页码
     * @param size        页容量
     * @return 返回查询结果
     */
    public List<T> list(Class<T> entityClass, Integer page, Integer size) {
        return list(entityClass, page, size, null);
    }

    /**
     * 分页查询
     *
     * @param entityClass 实体类对象的class
     * @param page        页码
     * @param size        页容量
     * @param conditions  条件
     * @return 返回查询结果
     */
    public List<T> list(Class<T> entityClass, Integer page, Integer size, Map<String, Object> conditions) {
        Query query = getQueryByConditions(conditions).skip((long) (page - 1) * size).limit(size);
        return mongoTemplate.find(query, entityClass, entityClass.getSimpleName().toLowerCase());
    }


    /**
     * 获取数据总条数
     *
     * @param entityClass 实体类对象
     * @return 返回总条数
     */
    public Long total(Class<T> entityClass) {
        return totalByConditions(entityClass, null);
    }

    /**
     * 根据条件获取数据总条数
     *
     * @param entityClass 实体类对象
     * @param conditions  条件
     * @return 返回总条数
     */
    public Long totalByConditions(Class<T> entityClass, Map<String, Object> conditions) {
        return mongoTemplate.count(getQueryByConditions(conditions), entityClass.getSimpleName().toLowerCase());
    }

    /**
     * 更新数据
     *
     * @param conditions  条件
     * @param update      需要更新的键和值
     * @param entityClass 需要更新的实体类
     */
    public void update(Map<String, Object> conditions, Map<String, Object> update, Class<T> entityClass) {
        mongoTemplate.updateFirst(getQueryByConditions(conditions), getUpdate(update), entityClass, entityClass.getSimpleName().toLowerCase());
    }


    /**
     * 根据查询结果组装Query对象
     *
     * @param conditions 条件集合
     * @return 返回Query对象
     */
    private Query getQueryByConditions(Map<String, Object> conditions) {
        Query query = new Query();

        if (null != conditions && conditions.size() != 0) {
            Criteria criteria = new Criteria();
            Set<Map.Entry<String, Object>> entries = conditions.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                criteria.and(entry.getKey()).is(entry.getValue());
            }
            query.addCriteria(criteria);
        }

        return query;
    }

    private UpdateDefinition getUpdate(Map<String, Object> updateMap) {
        Update update = new Update();

        if (null != updateMap && updateMap.size() != 0) {
            Set<Map.Entry<String, Object>> entries = updateMap.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                update.set(entry.getKey(), entry.getValue());
            }
        }

        return update;
    }


}
