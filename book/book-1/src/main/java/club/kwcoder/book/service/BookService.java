package club.kwcoder.book.service;

import club.kwcoder.book.dataobject.Book;
import club.kwcoder.book.dataobject.BookClassify;
import club.kwcoder.book.dto.PageDTO;
import club.kwcoder.book.dto.ResultDTO;

import java.util.List;
import java.util.Map;

/**
 * 书籍相关的服务接口
 *
 * @author zhinushannan
 */
public interface BookService {
    /**
     * 保存或更新书籍
     * 若更新，book对象中的uuid不为空
     *
     * @param book 书籍对象
     * @return 返回统一结果对象
     */
    ResultDTO<String> save(Book book);

    /**
     * 分页查询
     *
     * @param page       当前页数
     * @param size       每页数量
     * @param conditions 查询条件
     * @return 返回分页数据集合
     */
    ResultDTO<PageDTO<Book>> list(Integer page, Integer size, Map<String, Object> conditions);

    /**
     * 获取分类列表
     *
     * @return 返回分类列表
     */
    ResultDTO<List<BookClassify>> classifyList();

    /**
     * 根据书籍的uuid删除书籍
     *
     * @param uuid 书籍的uuid
     * @return 返回统一结果对象
     */
    ResultDTO<String> delete(String uuid);
}
