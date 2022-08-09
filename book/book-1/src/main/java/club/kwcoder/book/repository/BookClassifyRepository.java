package club.kwcoder.book.repository;

import club.kwcoder.book.dataobject.BookClassify;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookClassifyRepository extends MongoRepository<BookClassify, String> {
}
