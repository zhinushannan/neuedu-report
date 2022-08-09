package club.kwcoder.book.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "book")
public class Book {

    /**
     * 图书的uuid
     */
    @MongoId
    private String uuid;
    /**
     * 图书的书名
     */
    private String name;
    /**
     * 作者
     */
    private String author;
    /**
     * 出版社
     */
    private String publish;
    /**
     * 出版年份
     */
    private Integer publishYear;
    /**
     * 出版月
     */
    private Integer publishMonth;
    /**
     * 关键词
     */
    private String keyWord;
    /**
     * 摘要
     */
    private String abstractOfBook;
    /**
     * 分类号
     */
    private String classifyId;
    /**
     * 图书总数
     */
    private Integer total;
    /**
     * 剩余数量
     */
    private Integer remain;

}
