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

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultDTO<String> save(@RequestBody Book book) {
        return bookService.save(book);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ResultDTO<String> delete(@RequestParam("uuid") String uuid) {
        return bookService.delete(uuid);
    }

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
        Map<String, Object> conditions = MapBuilder.builder()
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

    @RequestMapping(value = "/classify/list", method = RequestMethod.GET)
    public ResultDTO<List<BookClassify>> classifyList() {
        return bookService.classifyList();
    }

}
