package club.kwcoder.book.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * 角色-菜单实体类对象
 * 用户角色和菜单的对应关系
 * 对应mongodb中的collection role_menu
 *
 * @author zhinushannan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "role_menu")
public class RoleMenu {

    /**
     * 按钮的_id，uuid
     */
    @MongoId
    private String _id;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 菜单的index
     */
    private String menuIndex;

}
