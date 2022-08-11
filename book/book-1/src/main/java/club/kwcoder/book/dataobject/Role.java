package club.kwcoder.book.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * 角色实体类对象
 * 对应mongodb中的collection role
 *
 * @author zhinushannan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "role")
public class Role {

    /**
     * 角色名称
     */
    @MongoId
    private String name;
    /**
     * 角色备注
     */
    private String remark;


}
