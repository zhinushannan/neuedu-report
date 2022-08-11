package club.kwcoder.book.repository;

import club.kwcoder.book.dataobject.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * 角色角色菜单的数据访问接口
 *
 * @author zhinushannan
 */
public interface UserRoleRepository extends MongoRepository<UserRole, String> {

    /**
     * 根据用户邮箱查询查询用户角色列表
     *
     * @param email 用户邮箱
     * @return 返回用户角色列表
     */
    List<UserRole> findByEmail(String email);

}
