package club.kwcoder.book.service;

import club.kwcoder.book.dto.LogsDTO;
import club.kwcoder.book.dto.PageDTO;
import club.kwcoder.book.dto.ResultDTO;

public interface BorrowService {

    /**
     * 借阅书籍
     *
     * @param email    用户的邮箱
     * @param bookUuid 书籍的uuid
     * @return 返回统一对象
     */
    ResultDTO<String> borrow(String email, String bookUuid);

    /**
     * 分页查看借阅历史
     *
     * @param email     用户的邮箱
     * @param page      第几页
     * @param size      每页的条数
     * @param hasReturn 查询条件：是否已经归还，false未归还
     * @return 返回统一对象
     */
    ResultDTO<PageDTO<LogsDTO>> list(String email, Integer page, Integer size, boolean hasReturn);

    /**
     * 归还书籍
     *
     * @param _id 借书日志的_id
     * @return 返回同一对象
     */
    ResultDTO<String> returnBook(String _id);
}
