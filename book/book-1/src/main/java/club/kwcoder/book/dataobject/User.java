package club.kwcoder.book.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

/**
 * 用户实体类对象
 * 对应mongodb中的collection user
 *
 * @author zhinushannan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
public class User {

    /**
     * 用户的邮箱
     */
    @MongoId
    private String email;
    /**
     * 用户的密码
     */
    private String password;
    /**
     * 用户的姓名
     */
    private String name;
    /**
     * 用户剩余可借阅量
     */
    private Integer remain;
    /**
     * 注册时间
     */
    private Date register;
    /**
     * 上次登录时间
     */
    private Date lastLogin;

}
