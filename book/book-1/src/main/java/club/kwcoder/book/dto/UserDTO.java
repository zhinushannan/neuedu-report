package club.kwcoder.book.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 用户信息传输对象
 *
 * @author zhinushannan
 */
@Data
@Builder
public class UserDTO {

    /**
     * 用户的邮箱
     */
    private String email;
    /**
     * 用户的姓名
     */
    private String name;
    /**
     * 用户剩余的可借阅量
     */
    private Integer remain;
    /**
     * 用户的总借阅量
     */
    private Long borrowTotal;

    /**
     * 注册时间
     */
    private Date register;
    /**
     * 上次登录时间
     */
    private Date lastLogin;

}
