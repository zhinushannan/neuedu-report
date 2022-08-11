package club.kwcoder.book.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * 用户-角色实体类对象
 * 用户用户和角色的对应
 * 对应mongodb中的collection user_role
 *
 * @author zhinushannan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "user_role")
public class UserRole {

    /**
     * _id，uuid
     */
    @MongoId
    private String _id;
    /**
     * 用户的邮箱
     */
    private String email;
    /**
     * 角色的名称
     */
    private String roleName;

}
