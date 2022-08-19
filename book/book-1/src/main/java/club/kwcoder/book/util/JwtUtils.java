package club.kwcoder.book.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * jwt相关的工具类
 *
 * @author zhinushannan
 */
@Data
@Component
@ConfigurationProperties(prefix = "club.kwcoder.book")
public class JwtUtils {

    /**
     * 从配置文件中注入，值为604800
     */
    private long expire;
    /**
     * 从配置文件中注入，值为f4e2e52034348f86b67cde581c0f9eb5
     */
    private String secret;
    /**
     * 从配置文件中注入，值为Authorization
     */
    private String header;

    /**
     * 生成jwt
     *
     * @param username  用户名
     * @param authority 用户权限列表
     * @return 返回jwt
     */
    public String generateToken(String username, List<String> authority) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000 * expire);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setAudience(authority.toString())
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)// 7天過期
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 根据jwt获取用户对象
     *
     * @param jwt jwt
     * @return 用户对象
     */
    public User getUser(String jwt) {
        Claims claims = getClaimByToken(jwt);
        String authStr = claims.getAudience().substring(1, claims.getAudience().length() - 1);
        List<GrantedAuthority> authorities = Arrays.stream(authStr.split(", ")).map((Function<String, GrantedAuthority>) s -> new SimpleGrantedAuthority("ROLE_" + s)).collect(Collectors.toList());
        return new User(claims.getSubject(), "Password: [PROTECTED]; ", authorities);
    }

    /**
     * 解析jwt
     *
     * @param jwt jwt字符串
     * @return 返回解析的Claims对象
     */
    public Claims getClaimByToken(String jwt) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 判断jwt是否过期
     *
     * @param claims Claims对象
     * @return 返回是否过期
     */
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

}