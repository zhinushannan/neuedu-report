package club.kwcoder.book.service.impl;

import club.kwcoder.book.repository.UserRepository;
import club.kwcoder.book.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring security 的userdetailsservice的实现类
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    /**
     * 根据username获取用户信息
     *
     * @param username username
     * @return 返回用户信息
     * @throws UsernameNotFoundException 当用户不存在时抛出次异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        club.kwcoder.book.dataobject.User user = userRepository.findByEmail(username);

        List<GrantedAuthority> authorities = new ArrayList<>();
        userRoleRepository.findByEmail(user.getEmail()).forEach(userRole -> authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getRoleName())));

        return new User(user.getEmail(), user.getPassword(), authorities);
    }


}
