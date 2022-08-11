package club.kwcoder.book.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * 角色-按钮实体类对象
 * 用户角色和按钮的对应关系
 * 对应mongodb中的collection role_button
 *
 * @author zhinushannan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "role_button")
public class RoleButton {

    /**
     * _id，uuid
     */
    @MongoId
    private String _id;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 按钮的id
     */
    private String buttonId;

}
