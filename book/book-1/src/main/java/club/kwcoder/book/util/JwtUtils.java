package club.kwcoder.book.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * jwt相关的工具类
 *
 * @author zhinushannan
 */
@Data
@Component
@ConfigurationProperties(prefix = "club.kwcoder.book")
public class JwtUtils {

    private long expire;
    private String secret;
    private String header;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 生成jwt
     *
     * @param username 用户名
     * @return 返回jwt
     */
    public String generateToken(String username) {

        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000 * expire);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
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
        Object o = redisUtils.get(jwt);
        if (o == null) {
            return null;
        }
        return (User) o;
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