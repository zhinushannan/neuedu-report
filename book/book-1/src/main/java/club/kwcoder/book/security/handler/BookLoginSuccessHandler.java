package club.kwcoder.book.security.handler;

import club.kwcoder.book.builder.MapBuilder;
import club.kwcoder.book.dataobject.Menu;
import club.kwcoder.book.dto.MenuDTO;
import club.kwcoder.book.dto.ResultDTO;
import club.kwcoder.book.repository.MenuRepository;
import club.kwcoder.book.repository.RoleButtonRepository;
import club.kwcoder.book.repository.RoleMenuRepository;
import club.kwcoder.book.repository.UserRepository;
import club.kwcoder.book.util.JwtUtils;
import club.kwcoder.book.util.RedisUtils;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 登录成功的处理类
 * <p>
 * 1、将jwt作为键、用户信息作为值存入redis中
 * 2、向前端传输用户的菜单、按钮、邮箱，网站信息、网站书籍的比例
 *
 * @author zhinushannan
 */
@Component
public class BookLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Autowired
    private RoleButtonRepository roleButtonRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserRepository userRepository;

    /**
     * 1、将jwt作为键、用户信息作为值存入redis中
     * 2、向前端传输用户的菜单、按钮、邮箱，网站信息、网站书籍的比例
     *
     * @param httpServletRequest  请求对象
     * @param httpServletResponse 响应对象
     * @param authentication      用户认证信息
     * @throws IOException      IO异常
     * @throws ServletException servlet异常
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();


        User user = (User) authentication.getPrincipal();
        List<String> roleNames = new ArrayList<>();
        user.getAuthorities().forEach(auth -> roleNames.add(auth.getAuthority().replace("ROLE_", "")));

        // 生成jwt返回
        String jwt = jwtUtils.generateToken(authentication.getName(), roleNames);
        httpServletResponse.setHeader(jwtUtils.getHeader(), jwt);

        Map<String, Object> data = new HashMap<>();
        data.put("items", getItems(roleNames));
        data.put("button", getButton(roleNames));
        data.put("websiteInfo", websiteInfo());
        data.put("websiteRate", redisUtils.get("info:rate"));
        data.put("email", user.getUsername());

        // 更新user信息
        club.kwcoder.book.dataobject.User userDb = userRepository.findByEmail(user.getUsername());
        userDb.setLastLogin(new Date());
        userRepository.save(userDb);

        ResultDTO<Map<String, Object>> result = ResultDTO.ok("登录成功！", data);

        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();

    }


    /**
     * 根据权限名称获取用户菜单列表
     *
     * @param roleName 权限名称
     * @return 返回用户菜单列表
     */
    public List<MenuDTO> getItems(List<String> roleName) {

        List<String> indexes = new ArrayList<>();
        roleMenuRepository.findByRoleNameIn(roleName).forEach(roleMenu -> indexes.add(roleMenu.getMenuIndex()));

        List<Menu> menus = menuRepository.findByIndexIn(indexes, Sort.by(Sort.DEFAULT_DIRECTION, "sort"));

        List<MenuDTO> data = new ArrayList<>();
        menus.forEach(menu -> {
            if (StringUtils.isNotBlank(menu.getIcon())) {
                MenuDTO menuDTO = MenuDTO.builder().icon(menu.getIcon()).index(menu.getIndex()).title(menu.getTitle()).build();
                List<MenuDTO> subs = new ArrayList<>();

                menus.forEach(menu1 -> {
                    if (StringUtils.isNotBlank(menu1.getParentIndex()) && menu1.getParentIndex().equals(menu.getIndex())) {
                        subs.add(MenuDTO.builder().icon(menu1.getIcon()).index(menu1.getIndex()).title(menu1.getTitle()).build());
                    }
                });

                menuDTO.setSubs(subs);
                data.add(menuDTO);
            }
        });

        return data;
    }

    /**
     * 网站信息：
     * 1、书籍的数量
     * 2、书籍的总数
     * 3、在馆数量
     * 4、总借阅量
     *
     * @return 返回如上四条信息的map集合
     */
    public Map<String, Long> websiteInfo() {
        long bookNum = (long) redisUtils.get("info:bookNum");
        long total = (long) redisUtils.get("info:total");
        long remain = (long) redisUtils.get("info:remain");
        long borrowCount = (long) redisUtils.get("info:borrowCount");
        return MapBuilder.<String, Long>builder()
                .put("bookNum", bookNum)
                .put("total", total)
                .put("remain", remain)
                .put("borrowCount", borrowCount)
                .build();
    }

    /**
     * 根据权限列表查询所属的按钮列表
     *
     * @param roleName 权限列表
     * @return 返回按钮列表
     */
    public List<String> getButton(List<String> roleName) {
        List<String> result = new ArrayList<>();
        roleButtonRepository.findByRoleNameIn(roleName).forEach(roleButton -> {
            result.add(roleButton.getButtonId());
        });
        return result;
    }

}
