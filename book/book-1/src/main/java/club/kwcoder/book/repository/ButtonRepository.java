package club.kwcoder.book.repository;

import club.kwcoder.book.dataobject.Button;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 按钮的数据访问接口
 *
 * @author zhinushannan
 */
public interface ButtonRepository extends MongoRepository<Button, String> {
}
