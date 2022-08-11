package club.kwcoder.book.controller;

import club.kwcoder.book.builder.MapBuilder;
import club.kwcoder.book.dataobject.Book;
import club.kwcoder.book.dataobject.BookClassify;
import club.kwcoder.book.dto.PageDTO;
import club.kwcoder.book.dto.ResultDTO;
import club.kwcoder.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 图书相关的控制类
 *
 * @author zhinushannan
 */
@RestController
@RequestMapping("/book")
public class BookController {

    public static final String BUSINESS = "图书";
    public static final String save = "保存";
    public static final String delete = "删除";
    public static final String list = "查询";
    public static final String classifyList = "类别查询";

    @Autowired
    private BookService bookService;

    /**
     * 保存或更新书籍
     * 若更新，book对象中的uuid不为空
     * <p>
     * 仅ADMIN角色可以操作
     *
     * @param book 书籍对象
     * @return 返回统一结果对象
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultDTO<String> save(@RequestBody Book book) {
        return bookService.save(book);
    }

    /**
     * 根据书籍的uuid删除书籍
     * <p>
     * 仅ADMIN角色可以操作
     *
     * @param uuid 书籍的uuid
     * @return 返回统一结果对象
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ResultDTO<String> delete(@RequestParam("uuid") String uuid) {
        return bookService.delete(uuid);
    }

    /**
     * 根据条件分页查询书籍列表
     * <p>
     * 除了page和size外，参数可有可无
     * <p>
     * publishYear是针对书籍出版年份的精确查询，而publishYearStart和publishYearEnd是对书籍出版年份的范围查询的起始和结束，
     * 因此publishYearStart和publishYearEnd必须同时出现，且不能与publishYear共存
     * <p>
     * 允许USER和ADMIN角色操作
     *
     * @param page             第几页
     * @param size             每页多少条
     * @param name             书籍的名称
     * @param author           书籍的作者
     * @param publish          书籍的出版社
     * @param publishYear      书籍的出版年份
     * @param publishYearStart 书籍出版年份范围查询的起始
     * @param publishYearEnd   书籍出版年份范围查询的结束
     * @param keyWord          关键词
     * @param abstractOfBook   摘要
     * @param classifyId       分类ID
     * @return 返回统一结果对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultDTO<PageDTO<Book>> list(@RequestParam(name = "page") Integer page,
                                         @RequestParam(name = "size") Integer size,
                                         @RequestParam(name = "name", required = false) String name,
                                         @RequestParam(name = "author", required = false) String author,
                                         @RequestParam(name = "publish", required = false) String publish,
                                         @RequestParam(name = "publishYear", required = false) Integer publishYear,
                                         @RequestParam(name = "publishYearStart", required = false) Integer publishYearStart,
                                         @RequestParam(name = "publishYearEnd", required = false) Integer publishYearEnd,
                                         @RequestParam(name = "keyWord", required = false) String keyWord,
                                         @RequestParam(name = "abstractOfBook", required = false) String abstractOfBook,
                                         @RequestParam(name = "classifyId", required = false) String classifyId) {
        Map<String, Object> conditions = MapBuilder.<String, Object>builder()
                .put("name", name)
                .put("author", author)
                .put("publish", publish)
                .put("publishYear", publishYear)
                .put("publishYearStart", publishYearStart)
                .put("publishYearEnd", publishYearEnd)
                .put("keyWord", keyWord)
                .put("abstractOfBook", abstractOfBook)
                .put("classifyId", classifyId)
                .build();
        return bookService.list(page, size, conditions);
    }

    /**
     * 获取图书所有分类
     * <p>
     * 允许USER和ADMIN用户操作
     *
     * @return 返回统一结果对象
     */
    @RequestMapping(value = "/classify/list", method = RequestMethod.GET)
    public ResultDTO<List<BookClassify>> classifyList() {
        return bookService.classifyList();
    }

}
