package club.kwcoder.book.repository;

import club.kwcoder.book.dataobject.RoleButton;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

/**
 * 角色按钮的数据访问接口
 *
 * @author zhinushannan
 */
public interface RoleButtonRepository extends MongoRepository<RoleButton, String> {

    /**
     * 根据角色集合查询角色按钮对象列表
     *
     * @param roleName 角色名称列表
     * @return 返回角色=按钮列表
     */
    List<RoleButton> findByRoleNameIn(Collection<String> roleName);

}
