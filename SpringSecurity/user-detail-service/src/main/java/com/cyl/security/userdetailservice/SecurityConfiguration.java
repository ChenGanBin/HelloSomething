package com.cyl.security.userdetailservice;

import org.springframework.context.annotation.Configuration;
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

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //允许匿名访问公共资源（“public”）,需要登陆后才能访问被保护资源（“protected”）
    http
      //这里先关闭csrf，否则必须带上必须csrf值和使用post请求才能退出登陆，会使示例程序变复杂
      .csrf().disable()
      //设置资源的访问权限
      .authorizeRequests()
      .antMatchers("/public/**").permitAll()
      .anyRequest().authenticated()
      //登陆拦截地址
      .and().formLogin().loginProcessingUrl("/login/login")
      //登出拦截地址
      .and().logout().logoutUrl("/login/logout");
  }
}
