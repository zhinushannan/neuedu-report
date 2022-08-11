package club.kwcoder.book.security.handler;

import club.kwcoder.book.util.JwtUtils;
import club.kwcoder.book.util.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登出成功的处理类
 * <p>
 * 将用户信息从redis中移除
 */
@Component
public class BookLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 登出成功的处理方法，将用户信息从redis中移除
     *
     * @param httpServletRequest  请求对象
     * @param httpServletResponse 响应对象
     * @param authentication      用户认证信息
     * @throws IOException      IO异常
     * @throws ServletException servlet异常
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String jwt = httpServletRequest.getHeader(jwtUtils.getHeader());
        if (StringUtils.isBlank(jwt)) {
            return;
        }
        redisUtils.del(jwt);
    }
}
