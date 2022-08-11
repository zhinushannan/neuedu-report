package club.kwcoder.book.service;

import club.kwcoder.book.dto.LogsDTO;
import club.kwcoder.book.dto.PageDTO;
import club.kwcoder.book.dto.ResultDTO;

/**
 * 借阅相关的服务接口
 *
 * @author zhinushannan
 */
public interface BorrowService {


    /**
     * 根据书籍的uuid借阅书籍
     *
     * @param jwt      用户的jwt
     * @param bookUuid 书籍uuid
     * @return 返回统一结果对象
     */
    ResultDTO<String> borrow(String jwt, String bookUuid);

    /**
     * 根据条件分页查询借阅记录
     *
     * @param jwt       用户的jwt
     * @param page      第几页
     * @param size      一页有几条
     * @param hasReturn 条件：是否已经归还
     * @return 返回统一结果对象
     */
    ResultDTO<PageDTO<LogsDTO>> list(String jwt, Integer page, Integer size, boolean hasReturn);

    /**
     * 归还书籍
     *
     * @param _id 借阅日志的_id
     * @return 返回统一结果对象
     */
    ResultDTO<String> returnBook(String _id);
}
