package club.kwcoder.book.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultDTO<T> {

    private Boolean flag;
    private Integer code;
    private String message;
    private T data;

    public static <T> ResultDTO<T> ok(String message, T data) {
        return ResultDTO.<T>builder()
                .flag(true)
                .code(200)
                .message(message)
                .data(data)
                .build();
    }

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
     * @param <T> 返回值范型
     * @return 返回401信息， 您尚未登录，请登录后再进行操作
     */
    public static <T> ResultDTO<T> unauthorized() {
        return ResultDTO.<T>builder()
                .flag(false)
                .code(401)
                .message("您尚未登录，请登录后再进行操作")
                .build();
    }

    public static <T> ResultDTO<T> notFound(String message) {
        return ResultDTO.<T>builder()
                .flag(false)
                .code(404)
                .message(message)
                .build();
    }

    public static <T> ResultDTO<T> error(String message) {
        return ResultDTO.<T>builder()
                .flag(false)
                .code(500)
                .message(message)
                .build();
    }

}
