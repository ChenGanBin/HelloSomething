package com.cyl.security.userdetailservice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link UserDetailsService}接口是spring security获取用户数据的顶级接口
 * {@link UserDetails}接口是代表用户信息的顶级接口
 * @author cyl
 */
@Configuration
public class CustomUserDetailsService implements UserDetailsService {

  @Resource
  private UserMapper userMapper;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //从各种途径获取用户信息，这里是模拟从数据库获取
    CustomUser customUser = userMapper.selectOne(new QueryWrapper<CustomUser>().eq("username", username));
    if(customUser == null) {
      //根据接口的说明找不到用户或者用户没有权限的都要抛出UsernameNotFoundException
      throw new UsernameNotFoundException("找不到账号"+username+"对应的用户");
    }

    //从各种途径获取用户的权限，用户最少要有一个权限，否则应该抛出异常UsernameNotFoundException
    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
    List<GrantedAuthority> grantedAuthorities = new ArrayList<>(1);
    grantedAuthorities.add(simpleGrantedAuthority);

    //最后返回一个实现了UserDetails接口的类
    User user = new User(customUser.getUsername(), customUser.getPassword(), grantedAuthorities);
    return user;
  }
}
