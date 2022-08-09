package club.kwcoder.book.repository;

import club.kwcoder.book.dataobject.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String> {
    /**
     * 根据uuid查找Book
     *
     * @param uuid 书籍的uuid
     * @return 返回书籍对象
     */
    Book findByUuid(String uuid);


}
