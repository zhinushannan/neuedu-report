package club.kwcoder.book.dto;

import club.kwcoder.book.dataobject.Book;
import club.kwcoder.book.dataobject.BorrowLog;
import club.kwcoder.book.dataobject.User;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class LogsDTO {

    private BorrowLog borrowLog;
    private Book book;
    private User user;

}
