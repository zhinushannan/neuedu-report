package club.kwcoder.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> {

    private Integer page = 1;
    private Integer size = 10;
    private Long total;
    private List<T> data = new ArrayList<>();

    /**
     * 检验分页条件是否合法
     *
     * @param page 第几页
     * @param size 页面大小
     * @return 若不合法，即page或size小于等于0，返回true
     */
    public static boolean isIllegal(int page, int size) {
        return page <= 0 || size <= 0;
    }

}
