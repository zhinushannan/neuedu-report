package club.kwcoder.book.repository;

import club.kwcoder.book.dataobject.RoleMenu;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

/**
 * 角色菜单的数据访问接口
 *
 * @author zhinushannan
 */
public interface RoleMenuRepository extends MongoRepository<RoleMenu, String> {

    /**
     * 根据角色列表查询角色菜单列表
     *
     * @param roleName 角色列表
     * @return 返回角色菜单列表
     */
    List<RoleMenu> findByRoleNameIn(Collection<String> roleName);

}
