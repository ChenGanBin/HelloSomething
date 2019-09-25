package com.cyl.security.oauth2.minimal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author cyl
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  /**
   * 暴露 {@link AuthenticationManager}
   *
   * @return
   * @throws Exception
   */
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //创建用户admin,必须最少有一个权限
    auth.inMemoryAuthentication()
      .withUser("admin").password("{noop}1234").authorities("ROLE_USER");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http

      //这里关闭csrf，否则必须带上必须csrf值和使用post请求才能退出登陆，会使程序变复杂
      .csrf().disable()

      .formLogin()
      //登陆拦截地址
      .loginProcessingUrl("/login/login")

      .and().logout()
      //登出拦截地址
      .logoutUrl("/login/logout");
  }
}
