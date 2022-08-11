package club.kwcoder.book.service;

import club.kwcoder.book.dataobject.User;
import club.kwcoder.book.dto.PageDTO;
import club.kwcoder.book.dto.ResultDTO;
import club.kwcoder.book.dto.UserDTO;

import java.util.Map;

/**
 * 用户相关的服务接口
 *
 * @author zhinushannan
 */
public interface UserService {

    /**
     * 创建用户的方法
     *
     * @param user 用户信息对象
     * @return 返回统一结果对象
     */
    ResultDTO<String> save(UserDTO user);

    /**
     * 更新用户信息
     *
     * @param user 用户信息对象
     * @return 返回统一结果对象
     */
    ResultDTO<String> update(UserDTO user);

    /**
     * 分页查询用户列表
     *
     * @param page       第几页
     * @param size       每页的条数
     * @param conditions 查询条件
     * @return 返回统一结果对象
     */
    ResultDTO<PageDTO<UserDTO>> list(Integer page, Integer size, Map<String, Object> conditions);

    /**
     * 删除用户
     *
     * @param email 用户的邮箱
     * @return 返回统一结果对象
     */
    ResultDTO<String> delete(String email);


}
