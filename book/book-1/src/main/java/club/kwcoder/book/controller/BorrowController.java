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

@RestController
@RequestMapping("/borrow")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    @RequestMapping(value = "borrow", method = RequestMethod.GET)
    public ResultDTO<String> borrow(@RequestParam("bookUuid") String bookUuid) {
        String email = "123";
        return borrowService.borrow(email, bookUuid);
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ResultDTO<PageDTO<LogsDTO>> list(@RequestParam("page") Integer page,
                                            @RequestParam("size") Integer size,
                                            @RequestParam(value = "hasReturn", defaultValue = "true", required = false) boolean hasReturn) {
        String email = "123";
        return borrowService.list(email, page, size, hasReturn);
    }

    @RequestMapping(value = "/return", method = RequestMethod.GET)
    public ResultDTO<String> returnBook(@RequestParam("_id") String _id) {
        return borrowService.returnBook(_id);
    }

}
