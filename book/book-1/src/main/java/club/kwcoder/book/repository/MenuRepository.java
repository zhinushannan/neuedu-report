package club.kwcoder.book.repository;

import club.kwcoder.book.dataobject.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

/**
 * 菜单的数据访问接口
 *
 * @author zhinushannan
 */
public interface MenuRepository extends MongoRepository<Menu, String> {


    /**
     * 查询index在集合中的对象列表，并且根据一定的排序规则进行排序
     *
     * @param index index在集合
     * @param sort  排序规则
     * @return 返回菜单列表
     */
    List<Menu> findByIndexIn(Collection<String> index, Sort sort);

}
