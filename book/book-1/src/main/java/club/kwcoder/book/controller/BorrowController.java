package club.kwcoder.book.controller;

import club.kwcoder.book.dto.LogsDTO;
import club.kwcoder.book.dto.PageDTO;
import club.kwcoder.book.dto.ResultDTO;
import club.kwcoder.book.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 借阅图书相关的控制类
 * <p>
 * 该类仅允许USER角色操作
 *
 * @author zhinushannan
 */
@RestController
@RequestMapping("/borrow")
public class BorrowController {

    public static final String BUSINESS = "借阅";
    public static final String borrow = "借阅";
    public static final String list = "查询借阅历史";
    public static final String returnBook = "归还";

    @Autowired
    private BorrowService borrowService;

    /**
     * 根据书籍的uuid借阅书籍
     *
     * @param bookUuid 书籍uuid
     * @return 返回统一结果对象
     */
    @RequestMapping(value = "borrow", method = RequestMethod.GET)
    public ResultDTO<String> borrow(@RequestParam("bookUuid") String bookUuid,
                                    HttpServletRequest request) {
        String jwt = request.getHeader("authorization");
        return borrowService.borrow(jwt, bookUuid);
    }

    /**
     * 根据条件分页查询借阅记录
     *
     * @param page      第几页
     * @param size      一页有几条
     * @param hasReturn 条件：是否已经归还
     * @return 返回统一结果对象
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ResultDTO<PageDTO<LogsDTO>> list(@RequestParam("page") Integer page,
                                            @RequestParam("size") Integer size,
                                            @RequestParam(value = "hasReturn", defaultValue = "true", required = false) boolean hasReturn,
                                            HttpServletRequest request) {
        String jwt = request.getHeader("authorization");
        return borrowService.list(jwt, page, size, hasReturn);
    }

    /**
     * 归还书籍
     *
     * @param _id 借阅日志的_id
     * @return 返回统一结果对象
     */
    @RequestMapping(value = "/return", method = RequestMethod.GET)
    public ResultDTO<String> returnBook(@RequestParam("_id") String _id,
                                        HttpServletRequest request) {
        String jwt = request.getHeader("authorization");
        return borrowService.returnBook(jwt, _id);
    }

}
