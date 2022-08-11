package club.kwcoder.book.controller;

import club.kwcoder.book.builder.MapBuilder;
import club.kwcoder.book.dataobject.User;
import club.kwcoder.book.dto.PageDTO;
import club.kwcoder.book.dto.ResultDTO;
import club.kwcoder.book.dto.UserDTO;
import club.kwcoder.book.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户相关的控制类
 * <p>
 * 仅ADMIN用户可操作
 *
 * @author zhinushannan
 */
@RestController
@RequestMapping("/user")
public class UserController {

    public static final String BUSINESS = "用户";
    public static final String save = "保存";
    public static final String update = "更新";
    public static final String delete = "删除";
    public static final String list = "查询";

    @Autowired
    private UserService userService;

    /**
     * 保存用户
     *
     * @param user 用户对象
     * @return 返回统一结果对象
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultDTO<String> save(@RequestBody User user) {
        return userService.save(user);
    }

    /**
     * 更新用户
     *
     * @param user 用户对象
     * @return 返回统一结果对象
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultDTO<String> update(@RequestBody User user) {
        return userService.update(user);
    }

    /**
     * 删除用户
     *
     * @param email 用户的邮箱
     * @return 返回统一结果对象
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ResultDTO<String> delete(@RequestParam("email") String email) {
        return userService.delete(email);
    }

    /**
     * 分页按条件查询用户列表
     *
     * @param page   第几页
     * @param size   每页多少条
     * @param email  用户的邮箱
     * @param name   用户的用户名
     * @param remain 用户剩余的可借阅量
     * @return 返回统一结果对象
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultDTO<PageDTO<UserDTO>> list(@RequestParam("page") Integer page,
                                            @RequestParam("size") Integer size,
                                            @RequestParam(value = "email", required = false) String email,
                                            @RequestParam(value = "name", required = false) String name,
                                            @RequestParam(value = "remain", required = false) Integer remain) {
        Map<String, Object> conditions = MapBuilder.<String, Object>builder()
                .put("email", email)
                .put("name", name)
                .put("remain", remain)
                .build();
        return userService.list(page, size, conditions);
    }

}
