package club.kwcoder.book.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 统一返回结果
 *
 * @param <T> 数据类型
 * @author zhinushannan
 */
@Data
@Builder
public class ResultDTO<T> {

    /**
     * 是否成功
     */
    private Boolean flag;
    /**
     * 业务响应码
     */
    private Integer code;
    /**
     * 业务响应消息
     */
    private String message;
    /**
     * 业务响应数据
     */
    private T data;

    /**
     * 返回成功的返回对象
     *
     * @param message 业务响应消息
     * @param data    业务响应数据
     * @param <T>     数据类型
     * @return 成功的返回对象
     */
    public static <T> ResultDTO<T> ok(String message, T data) {
        return ResultDTO.<T>builder()
                .flag(true)
                .code(200)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 返回被禁止的返回对象
     *
     * @param message 业务响应消息
     * @param <T>     数据类型
     * @return 成功的返回对象
     */
    public static <T> ResultDTO<T> forbidden(String message) {
        return ResultDTO.<T>builder()
                .flag(false)
                .code(403)
                .message(message)
                .build();
    }

    /**
     * 请求要求用户的身份认证
     *
     * @param <T> 数据类型
     * @return 返回401信息， 您尚未登录，请登录后再进行操作
     */
    public static <T> ResultDTO<T> unauthorized() {
        return ResultDTO.<T>builder()
                .flag(false)
                .code(401)
                .message("您尚未登录，请登录后再进行操作")
                .build();
    }

    /**
     * 请求未找到
     *
     * @param message 业务响应消息
     * @param <T>     数据类型
     * @return 成功的返回对象
     */
    public static <T> ResultDTO<T> notFound(String message) {
        return ResultDTO.<T>builder()
                .flag(false)
                .code(404)
                .message(message)
                .build();
    }

}
