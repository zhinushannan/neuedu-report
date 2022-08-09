package club.kwcoder.book.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
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
