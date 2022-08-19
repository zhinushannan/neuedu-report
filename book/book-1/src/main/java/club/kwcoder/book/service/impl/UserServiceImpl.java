package club.kwcoder.book.service.impl;

import club.kwcoder.book.dataobject.Book;
import club.kwcoder.book.dataobject.User;
import club.kwcoder.book.dataobject.UserRole;
import club.kwcoder.book.dto.PageDTO;
import club.kwcoder.book.dto.ResultDTO;
import club.kwcoder.book.dto.UserDTO;
import club.kwcoder.book.repository.BorrowLogRepository;
import club.kwcoder.book.repository.UserRepository;
import club.kwcoder.book.repository.UserRoleRepository;
import club.kwcoder.book.service.UserService;
import club.kwcoder.book.util.CriteriaUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 用户相关的服务接口实现类
 *
 * @author zhinushannan
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private BorrowLogRepository borrowLogRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 创建用户的方法
     *
     * @param user 用户信息对象
     * @return 返回响应结果对象
     */
    @Override
    public ResultDTO<String> save(UserDTO user) {
        user.setRegister(new Date());
        user.setLastLogin(user.getRegister());
        return insertOrUpdate(user, false);
    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息对象
     * @return 返回统一对象
     */
    @Override
    public ResultDTO<String> update(UserDTO user) {
        return insertOrUpdate(user, true);
    }

    @Transactional
    private ResultDTO<String> insertOrUpdate(UserDTO user, boolean isUpdate) {
        if (StringUtils.isAnyBlank(user.getName(), user.getEmail())) {
            return ResultDTO.forbidden("字段不正确！");
        }
        if (!isUpdate && userRepository.existsById(user.getEmail())) {
            return ResultDTO.forbidden("邮箱已被占用！");
        }
        if (isUpdate && !userRepository.existsById(user.getEmail())) {
            return ResultDTO.forbidden("用户未注册！");
        }
        // 插入
        if (!isUpdate) {
            if (StringUtils.isAnyBlank(user.getPassword())) {
                return ResultDTO.forbidden("字段不正确！");
            }
            userRoleRepository.save(UserRole.builder()._id(UUID.randomUUID().toString()).email(user.getEmail()).roleName(user.getRole()).build());
            user.setRemain(5);
        } else
        // 更新
        {
            User temp = userRepository.findByEmail(user.getEmail());
            user.setRemain(temp.getRemain());
            if (StringUtils.isAnyBlank(user.getPassword())) {
                user.setPassword(temp.getPassword());
            }
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .remain(user.getRemain())
                .register(user.getRegister())
                .lastLogin(user.getLastLogin())
                .build());
        return ResultDTO.ok(isUpdate ? "更新成功" : "插入成功", null);
    }

    /**
     * 分页查询用户列表
     *
     * @param page       第几页
     * @param size       每页的条数
     * @param conditions 查询条件
     * @return 返回统一对象
     */
    @Override
    public ResultDTO<PageDTO<UserDTO>> list(Integer page, Integer size, Map<String, Object> conditions) {
        if (PageDTO.isIllegal(page, size)) {
            return ResultDTO.forbidden("参数不正确！");
        }

        Criteria criteria = CriteriaUtils.getLikeCriteria(conditions);
        if (null != conditions.get("remain") && (int) conditions.get("remain") >= 0) {
            criteria.and("remain").is(conditions.get("remain"));
        }
        Query query = Query.query(criteria);
        long total = mongoTemplate.count(query, Book.class, "user");

        List<UserDTO> data = new ArrayList<>();
        mongoTemplate.find(query.skip((long) (page - 1) * size).limit(size), User.class, "user").forEach(
                user -> data.add(
                        UserDTO.builder()
                                .email(user.getEmail())
                                .name(user.getName())
                                .remain(user.getRemain())
                                .borrowTotal(borrowLogRepository.countByEmail(user.getEmail()))
                                .build()
                )
        );
        return ResultDTO.ok(
                "查询成功！",
                PageDTO.<UserDTO>builder()
                        .page(page)
                        .size(size)
                        .total(total)
                        .data(data)
                        .build()
        );
    }

    /**
     * 删除用户
     *
     * @param email 用户的邮箱
     * @return 返回统一对象
     */
    @Override
    @Transactional
    public ResultDTO<String> delete(String email) {
        User user = userRepository.findByEmail(email);
        if (null == user) {
            return ResultDTO.forbidden("用户不存在！");
        }
        if (borrowLogRepository.existsByEmailAndReturnDate(email, null)) {
            return ResultDTO.forbidden("该用户有书籍尚未归还，请归还后注销！");
        }
        userRepository.deleteById(email);
        borrowLogRepository.deleteByEmail(email);
        return ResultDTO.ok("删除成功！", null);
    }
}
