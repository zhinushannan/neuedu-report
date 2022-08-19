package club.kwcoder.book.config;

import club.kwcoder.book.security.entrypoint.JwtAuthenticationEntryPoint;
import club.kwcoder.book.security.filter.JWTAuthenticationFilter;
import club.kwcoder.book.security.handler.BookLoginFailureHandler;
import club.kwcoder.book.security.handler.BookLoginSuccessHandler;
import club.kwcoder.book.security.handler.BookLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security配置类
 *
 * @author zhinushannan
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BookLoginSuccessHandler loginSuccessHandler;

    @Autowired
    private BookLoginFailureHandler loginFailureHandler;

    @Autowired
    private BookLogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and().csrf().disable()
                // 登录
                .formLogin()
                .loginProcessingUrl("/login")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                // 登出
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies("JSESSIONID")
                // 设置放行
                .and()
                .authorizeRequests()
                .antMatchers("/book/list", "/book/classify/list").hasAnyRole("ADMIN", "USER")
                .antMatchers("/book/save", "/book/delete", "/user/**").hasRole("ADMIN")
                .antMatchers("/borrow/**").hasRole("USER")
                .antMatchers("/login", "/logout").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler((httpServletRequest, httpServletResponse, e) -> {
                    System.out.println(e.getLocalizedMessage());
                    System.out.println(e.getMessage());
                })
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                // 关闭session
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 设置过滤器
                .and()
                .addFilter(jwtAuthenticationFilter());

    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JWTAuthenticationFilter(authenticationManager());
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
