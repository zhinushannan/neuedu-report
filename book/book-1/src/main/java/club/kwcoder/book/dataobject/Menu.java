package club.kwcoder.book.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * 菜单实体类对象
 * 用户控制前端的菜单展示
 * 对应mongodb中的collection menu
 *
 * @author zhinushannan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "menu")
public class Menu {

    /**
     * 菜单的图标
     */
    private String icon;
    /**
     * 菜单的index属性
     */
    @MongoId
    private String index;
    /**
     * 菜单对应页面的标题
     */
    private String title;
    /**
     * 菜单的父index
     */
    private String parentIndex;
    /**
     * 菜单的排序编号
     */
    private Integer sort;

}
