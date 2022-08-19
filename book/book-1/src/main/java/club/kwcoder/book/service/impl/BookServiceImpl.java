package club.kwcoder.book.service.impl;

import club.kwcoder.book.dataobject.Book;
import club.kwcoder.book.dataobject.BookClassify;
import club.kwcoder.book.dto.PageDTO;
import club.kwcoder.book.dto.ResultDTO;
import club.kwcoder.book.repository.BookClassifyRepository;
import club.kwcoder.book.repository.BookRepository;
import club.kwcoder.book.repository.BorrowLogRepository;
import club.kwcoder.book.service.BookService;
import club.kwcoder.book.util.CriteriaUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 书籍相关的服务接口实现类
 *
 * @author zhinushannan
 */
@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookClassifyRepository bookClassifyRepository;
    @Autowired
    private BorrowLogRepository borrowLogRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 创建书籍的方法
     *
     * @param book 书籍信息对象
     * @return 返回响应结果对象
     */
    @Override
    public ResultDTO<String> save(Book book) {
        /*
        检验合法性：
        1、年份大于本年、年份小于等于0，非法
        2、月份大于12、小于等于0，非法
        3、分类编号不存在，非法
         */
        if (
                (book.getPublishYear() >= LocalDate.now().getYear() || book.getPublishYear() <= 0) &&
                        (book.getPublishMonth() > 12 || book.getPublishMonth() <= 0) &&
                        bookClassifyRepository.existsById(book.getClassifyId())
        ) {
            return ResultDTO.forbidden("参数错误！");
        }

        String message = "添加成功！";

        if (StringUtils.isBlank(book.getUuid())) {
            boolean flag = true;
            String uuid = "";
            while (flag) {
                uuid = UUID.randomUUID().toString();
                flag = bookRepository.existsById(uuid);
            }
            book.setUuid(uuid);
            book.setRemain(book.getTotal());
        } else
        // 修改的需求
        {
            Book byUuid = bookRepository.findByUuid(book.getUuid());
            if (byUuid == null) {
                return ResultDTO.forbidden("书籍不存在！");
            }
            // 当修改后的总数小于未归还的数量时
            long borrow = borrowLogRepository.countByBookUuidAndReturnDate(book.getUuid(), null);
            if (book.getTotal() < borrow) {
                return ResultDTO.forbidden("参数错误！");
            }
            // 当剩余数量大于修改后的总数时
            book.setRemain((int) (book.getTotal() - borrow));
            message = "修改成功！";
        }
        bookRepository.save(book);
        return ResultDTO.ok(message, null);
    }

    /**
     * 分页查询
     *
     * @param page       当前页数
     * @param size       每页数量
     * @param conditions 查询条件
     * @return 返回分页数据集合
     */
    @Override
    public ResultDTO<PageDTO<Book>> list(Integer page, Integer size, Map<String, Object> conditions) {
        if (PageDTO.isIllegal(page, size)) {
            return ResultDTO.forbidden("参数不正确！");
        }

        Criteria criteria = CriteriaUtils.getLikeCriteria(conditions);
        Object publishYear = conditions.get("publishYear");
        if (publishYear != null) {
            criteria.and("publishYear").is(conditions.get("publishYear"));
        }
        Object publishYearStart = conditions.get("publishYearStart");
        Object publishYearEnd = conditions.get("publishYearEnd");
        if (publishYearStart != null && publishYearEnd != null) {
            criteria.and("publishYear").gte(publishYearStart);
            criteria.and("publishYear").lte(publishYearEnd);
        }
        Query query = Query.query(criteria);
        long total = mongoTemplate.count(query, Book.class, "book");
        List<Book> data = mongoTemplate.find(query.skip((long) (page - 1) * size).limit(size), Book.class, "book");


        return ResultDTO.ok(
                "查询成功！",
                PageDTO.<Book>builder()
                        .page(page)
                        .size(size)
                        .data(data)
                        .total(total).build());
    }

    /**
     * 获取分类列表
     *
     * @return 返回分类列表
     */
    @Override
    public ResultDTO<List<BookClassify>> classifyList() {
        return ResultDTO.ok(
                "查询成功！",
                bookClassifyRepository.findAll()
        );
    }

    /**
     * 删除书籍
     *
     * @param uuid 书籍的uuid
     * @return 返回统一结果对象
     */
    @Override
    public ResultDTO<String> delete(String uuid) {
        Book book = bookRepository.findByUuid(uuid);
        if (null == book) {
            return ResultDTO.notFound("书籍不存在！");
        }
        if (book.getTotal().intValue() != book.getRemain().intValue()) {
            return ResultDTO.forbidden("该书籍尚未完全归还，请完全归还后再删除！");
        }
        bookRepository.deleteById(uuid);
        return ResultDTO.ok("删除成功！", null);
    }
}
