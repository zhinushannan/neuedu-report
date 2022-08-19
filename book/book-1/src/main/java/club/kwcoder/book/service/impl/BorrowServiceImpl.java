package club.kwcoder.book.service.impl;


import club.kwcoder.book.dataobject.Book;
import club.kwcoder.book.dataobject.BorrowLog;
import club.kwcoder.book.dataobject.User;
import club.kwcoder.book.dto.LogsDTO;
import club.kwcoder.book.dto.PageDTO;
import club.kwcoder.book.dto.ResultDTO;
import club.kwcoder.book.repository.BookRepository;
import club.kwcoder.book.repository.BorrowLogRepository;
import club.kwcoder.book.repository.UserRepository;
import club.kwcoder.book.service.BorrowService;
import club.kwcoder.book.util.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 借阅相关的服务接口实现类
 *
 * @author zhinushannan
 */
@Service
public class BorrowServiceImpl implements BorrowService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowLogRepository borrowLogRepository;

    @Autowired
    private JwtUtils jwtUtils;


    /**
     * 借阅书籍
     *
     * @param jwt      用户的jwt
     * @param bookUuid 书籍的uuid
     * @return 返回统一对象
     */
    @Override
    @Transactional
    public ResultDTO<String> borrow(String jwt, String bookUuid) {
        org.springframework.security.core.userdetails.User userRedis = jwtUtils.getUser(jwt);
        if (null == userRedis) {
            return ResultDTO.forbidden("认证错误，请重新登录或联系管理员！");
        }
        String email = userRedis.getUsername();

        if (StringUtils.isBlank(bookUuid)) {
            // 书籍id为空
            return ResultDTO.forbidden("参数错误！");
        }

        User user = userRepository.findByEmail(email);
        if (null == user) {
            // 用户不存在
            return ResultDTO.notFound("您尚未注册，请联系管理员进行注册！");
        }
        if (user.getRemain() == 0) {
            // 该用户不能再借书了
            return ResultDTO.forbidden("您的账户已不能借阅，请归还书籍后再次操作！");
        }

        Book book = bookRepository.findByUuid(bookUuid);
        if (null == book) {
            // 书籍不存在
            return ResultDTO.notFound("书籍不存在！");
        }
        if (book.getRemain() == 0) {
            // 书被借完了
            return ResultDTO.forbidden("该书籍已没有库存！");
        }

        boolean flag = borrowLogRepository.existsByBookUuidAndEmailAndReturnDate(bookUuid, email, null);
        if (flag) {
            // 借过这个本书，但是还没归还
            return ResultDTO.forbidden("您已借过该书籍，请归还后再次借阅。");
        }

        // 借书流程
        // 用户可借阅量 -1
        user.setRemain(user.getRemain() - 1);
        userRepository.save(user);
        // 剩余书 - 1
        book.setRemain(book.getRemain() - 1);
        bookRepository.save(book);

        // 日志
        BorrowLog log = BorrowLog.builder().email(email).bookUuid(bookUuid).borrowDate(new Date()).returnDate(null).build();
        borrowLogRepository.save(log);

        return ResultDTO.ok("借阅成功！", null);
    }

    /**
     * 分页查看借阅历史
     *
     * @param jwt       用户的jwt
     * @param page      第几页
     * @param size      每页的条数
     * @param hasReturn 查询条件：是否已经归还，false未归还
     * @return 返回统一对象
     */
    @Override
    public ResultDTO<PageDTO<LogsDTO>> list(String jwt, Integer page, Integer size, boolean hasReturn) {
        org.springframework.security.core.userdetails.User userRedis = jwtUtils.getUser(jwt);
        if (null == userRedis) {
            return ResultDTO.forbidden("认证错误，请重新登录或联系管理员！");
        }
        String email = userRedis.getUsername();

        if (page <= 0 || size <= 0) {
            return ResultDTO.forbidden("参数不正确！");
        }

        List<LogsDTO> data = new ArrayList<>();

        User user = userRepository.findByEmail(email);

        List<BorrowLog> list;
        PageRequest pageable = PageRequest.of(page - 1, size);
        if (hasReturn) {
            list = borrowLogRepository.findByEmail(email, pageable).getContent();
        } else {
            list = borrowLogRepository.findByEmailAndReturnDate(email, null, pageable).getContent();
        }

        list.forEach(borrowLog -> {
            Book book = bookRepository.findByUuid(borrowLog.getBookUuid());
            LogsDTO build = LogsDTO.builder().user(user).borrowLog(borrowLog).book(book).build();
            data.add(build);
        });


        long total = borrowLogRepository.count();

        return ResultDTO.ok("查询成功！", PageDTO.<LogsDTO>builder().page(page).size(size).data(data).total(total).build());
    }

    /**
     * 归还书籍
     *
     * @param _id 借书日志的_id
     * @return 返回同一对象
     */
    @Override
    @Transactional
    public ResultDTO<String> returnBook(String jwt, String _id) {
        org.springframework.security.core.userdetails.User userRedis = jwtUtils.getUser(jwt);
        if (null == userRedis) {
            return ResultDTO.forbidden("认证错误，请重新登录或联系管理员！");
        }
        String email = userRedis.getUsername();

        BorrowLog log = borrowLogRepository.findBy_id(_id);

        if (null == log) {
            return ResultDTO.forbidden("参数错误！");
        }

        if (!StringUtils.equals(email, log.getEmail())) {
            return ResultDTO.forbidden("认证错误！");
        }

        if (log.getReturnDate() != null) {
            return ResultDTO.forbidden("您已经归还该图书！");
        }

        // 借书流程
        // 用户可借阅量 + 1
        User user = userRepository.findByEmail(log.getEmail());
        user.setRemain(user.getRemain() + 1);
        userRepository.save(user);

        // 剩余书 + 1
        Book book = bookRepository.findByUuid(log.getBookUuid());
        book.setRemain(book.getRemain() + 1);
        bookRepository.save(book);
        // 日志
        log.setReturnDate(new Date());
        borrowLogRepository.save(log);

        return ResultDTO.ok("归还成功！", null);
    }
}
