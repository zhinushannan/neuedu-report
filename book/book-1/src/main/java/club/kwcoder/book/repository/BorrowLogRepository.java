package club.kwcoder.book.repository;

import club.kwcoder.book.dataobject.BorrowLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;

/**
 * 借阅日志的数据访问接口
 *
 * @author zhinushannan
 */
public interface BorrowLogRepository extends MongoRepository<BorrowLog, String> {

    /**
     * 根据书籍uuid、用户邮箱、归还时间判断是否存在该日志
     *
     * @param bookUuid   书籍uuid
     * @param email      用户邮箱
     * @param returnDate 归还时间
     * @return 返回日志对象
     */
    boolean existsByBookUuidAndEmailAndReturnDate(String bookUuid, String email, Date returnDate);

    /**
     * 根据用户邮箱、归还时间判断是否存在该日志
     *
     * @param email      用户邮箱
     * @param returnDate 归还时间
     * @return 返回日志对象
     */
    boolean existsByEmailAndReturnDate(String email, Date returnDate);

    /**
     * 根据用户邮箱分页查询
     *
     * @param email    用户邮箱
     * @param pageable 分页条件
     * @return 返回Page对象
     */
    Page<BorrowLog> findByEmail(String email, Pageable pageable);

    /**
     * 根据用户邮箱和归还日期分页查询
     *
     * @param email      用户邮箱
     * @param returnDate 归还日期
     * @param pageable   分页条件
     * @return 返回Page对象
     */
    Page<BorrowLog> findByEmailAndReturnDate(String email, Date returnDate, Pageable pageable);

    /**
     * 根据 _id 查询日志
     *
     * @param _id 日志的 _id
     * @return 日志对象
     */
    BorrowLog findBy_id(String _id);

    /**
     * 根据邮箱查询记录数量
     *
     * @param email 用户邮箱
     * @return 返回记录数量
     */
    long countByEmail(String email);

    /**
     * 根据书籍编号和归还日期计数
     *
     * @param bookUuid   书籍Uuid
     * @param returnDate 归还日期
     * @return 计数结果
     */
    long countByBookUuidAndReturnDate(String bookUuid, Date returnDate);

    /**
     * 根据用户邮箱删除日志
     *
     * @param email 用户邮箱
     */
    void deleteByEmail(String email);

}
