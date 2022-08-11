package club.kwcoder.book.repository;

import club.kwcoder.book.dataobject.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 书籍的数据访问接口
 *
 * @author zhinushannan
 */
public interface BookRepository extends MongoRepository<Book, String> {
    /**
     * 根据uuid查找Book
     *
     * @param uuid 书籍的uuid
     * @return 返回书籍对象
     */
    Book findByUuid(String uuid);

    /**
     * 根据分类id统计书籍
     *
     * @param classifyId 分类id
     * @return 返回统计的数量
     */
    long countByClassifyId(String classifyId);


}
