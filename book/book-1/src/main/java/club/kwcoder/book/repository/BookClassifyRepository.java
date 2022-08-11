package club.kwcoder.book.repository;

import club.kwcoder.book.dataobject.BookClassify;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 书籍分类的数据访问接口
 *
 * @author zhinushannan
 */
public interface BookClassifyRepository extends MongoRepository<BookClassify, String> {
}
