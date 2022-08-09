package club.kwcoder.book.repository;

import club.kwcoder.book.dataobject.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String> {

    /**
     * 根据用户邮箱查询用户
     *
     * @param email 用户邮箱
     * @return 返回用户对象
     */
    User findByEmail(String email);



}
