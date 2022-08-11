package club.kwcoder.book.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 菜单传输对象
 *
 * @author zhinushannan
 */
@Data
@Builder
public class MenuDTO {

    /**
     * 菜单的图标
     */
    private String icon;
    /**
     * 菜单的index属性
     */
    private String index;
    /**
     * 菜单对应页面的标题
     */
    private String title;
    /**
     * 菜单的子菜单
     */
    private List<MenuDTO> subs;
}
