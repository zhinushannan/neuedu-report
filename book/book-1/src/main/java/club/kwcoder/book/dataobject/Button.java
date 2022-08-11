package club.kwcoder.book.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * 按钮实体类对象
 * 用户控制前端的按钮显示与否
 * 对应mongodb中的collection button
 *
 * @author zhinushannan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "button")
public class Button {

    /**
     * 按钮的id
     */
    @MongoId
    private Integer id;
    /**
     * 按钮的名称
     */
    private String button;

}
