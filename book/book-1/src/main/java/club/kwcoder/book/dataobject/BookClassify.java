package club.kwcoder.book.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * 图书分类实体类对象
 * 对应mongodb中的collection book_classify
 *
 * @author zhinushannan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "book_classify")
public class BookClassify {

    /**
     * 图书分类的ID
     */
    @MongoId
    private String classifyId;
    /**
     * 图书分类的名称
     */
    private String classify;

}
