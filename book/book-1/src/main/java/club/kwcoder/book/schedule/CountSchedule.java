package club.kwcoder.book.schedule;

import club.kwcoder.book.dataobject.Book;
import club.kwcoder.book.repository.BookClassifyRepository;
import club.kwcoder.book.repository.BookRepository;
import club.kwcoder.book.repository.BorrowLogRepository;
import club.kwcoder.book.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时任务，每小时进行一次统计：书籍总数、书籍数量、在馆书籍数量、累计借阅次数、各种类书籍的数量
 *
 * @author zhinushannan
 */
@Component
public class CountSchedule {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowLogRepository borrowLogRepository;

    @Autowired
    private BookClassifyRepository bookClassifyRepository;

    @Autowired
    private RedisUtils redisUtils;

    @Scheduled(fixedDelay = 3600000)
    public void count() {
        long bookNum;
        long total = 0L;
        long remain = 0L;
        long borrowCount;

        List<Book> books = bookRepository.findAll();
        bookNum = books.size();
        for (Book book : books) {
            total += book.getTotal();
            remain += book.getRemain();
        }

        borrowCount = borrowLogRepository.count();

        redisUtils.set("info:bookNum", bookNum);
        redisUtils.set("info:total", total);
        redisUtils.set("info:remain", remain);
        redisUtils.set("info:borrowCount", borrowCount);


        Map<String, Long> rate = new HashMap<>();

        bookClassifyRepository.findAll().forEach(bookClassify -> rate.put(bookClassify.getClassifyId(), bookRepository.countByClassifyId(bookClassify.getClassifyId())));

        redisUtils.set("info:rate", rate);
    }

}
