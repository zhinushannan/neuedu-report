package club.kwcoder.book.repository;

import club.kwcoder.book.dataobject.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 角色的数据访问接口
 *
 * @author zhinushannan
 */
public interface RoleRepository extends MongoRepository<Role, String> {
}
