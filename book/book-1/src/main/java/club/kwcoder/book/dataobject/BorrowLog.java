package club.kwcoder.book.dataobject;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.util.DigestUtils;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "borrowlog")
public class BorrowLog {

    /**
     * 日志的_id，由用户的邮箱、书籍的uuid和借阅日期共同求md5摘要得出
     */
    @MongoId
    private String _id;
    /**
     * 用户的邮箱
     */
    private String email;
    /**
     * 书籍的id
     */
    private String bookUuid;
    /**
     * 借阅日期
     */
    private Date borrowDate;
    /**
     * 归还日期
     */
    private Date returnDate;

    /**
     * 获取借阅日志建造者对象
     *
     * @return 返回建造者对象
     */
    public static BorrowLogBuilder builder() {
        return new BorrowLogBuilder();
    }

    /**
     * 实例化对象
     *
     * @param email      用户邮箱
     * @param bookUuid   书籍uuid
     * @param borrowDate 借阅日期
     * @param returnDate 归还日期
     */
    private BorrowLog(String email, String bookUuid, Date borrowDate, Date returnDate) {
        this._id = DigestUtils.md5DigestAsHex((email + bookUuid + borrowDate).getBytes());
        this.email = email;
        this.bookUuid = bookUuid;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public static class BorrowLogBuilder {
        /**
         * 用户的邮箱
         */
        private String email;
        /**
         * 书籍的id
         */
        private String bookUuid;
        /**
         * 借阅日期
         */
        private Date borrowDate;
        /**
         * 归还日期
         */
        private Date returnDate;

        /**
         * 建造者模式：为email赋值
         *
         * @param email email
         * @return 返回建造者对象
         */
        public BorrowLogBuilder email(String email) {
            this.email = email;
            return this;
        }

        /**
         * 建造者模式：为bookUuid赋值
         *
         * @param bookUuid bookUuid
         * @return 返回建造者对象
         */
        public BorrowLogBuilder bookUuid(String bookUuid) {
            this.bookUuid = bookUuid;
            return this;
        }

        /**
         * 建造者模式：为borrowDate赋值
         *
         * @param borrowDate borrowDate
         * @return 返回建造者对象
         */
        public BorrowLogBuilder borrowDate(Date borrowDate) {
            this.borrowDate = borrowDate;
            return this;
        }

        /**
         * 建造者模式：为returnDate赋值
         *
         * @param returnDate returnDate
         * @return 返回建造者对象
         */
        public BorrowLogBuilder returnDate(Date returnDate) {
            this.returnDate = returnDate;
            return this;
        }

        /**
         * 通过建造者对象获取日志对象
         *
         * @return 日志对象
         */
        public BorrowLog build() {
            return new BorrowLog(email, bookUuid, borrowDate, returnDate);
        }
    }

}
