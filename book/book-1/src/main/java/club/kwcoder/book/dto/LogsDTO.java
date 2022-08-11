package club.kwcoder.book.dto;

import club.kwcoder.book.dataobject.Book;
import club.kwcoder.book.dataobject.BorrowLog;
import club.kwcoder.book.dataobject.User;
import lombok.Builder;
import lombok.Data;

/**
 * 借阅日志传输对象
 *
 * @author zhinushannan
 */
@Data
@Builder
public class LogsDTO {

    /**
     * 借阅日志实体类对象
     */
    private BorrowLog borrowLog;
    /**
     * 书籍实体类对象
     */
    private Book book;
    /**
     * 用户实体类对象
     */
    private User user;

}
