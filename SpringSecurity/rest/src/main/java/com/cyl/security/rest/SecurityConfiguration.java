package com.cyl.security.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * @author cyl
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  @Qualifier("customAuthenticationEntryPoint")
  private AuthenticationEntryPoint authenticationEntryPoint;

  @Autowired
  private AccessDeniedHandler accessDeniedHandler;

  @Autowired
  private AuthenticationFailureHandler authenticationFailureHandler;

  @Autowired
  private AuthenticationSuccessHandler authenticationSuccessHandler;

  @Autowired
  private LogoutSuccessHandler logoutSuccessHandler;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //创建用户admin,必须最小有一个权限
    auth.inMemoryAuthentication()
      .withUser("admin").password("{noop}1234").authorities("ROLE_USER");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //允许匿名访问公共资源（“public”）,需要登陆后才能访问被保护资源（“protected”）
    http

      //这里关闭csrf，否则必须带上必须csrf值和使用post请求才能退出登陆，会使程序变复杂
      .csrf().disable()

      //设置权限不足的处理器
      .exceptionHandling()
        //accessDeniedHandler代表的是登陆后的权限不足
        .accessDeniedHandler(accessDeniedHandler)
        //authenticationEntryPoint代表的是未登陆时的权限不足
        .authenticationEntryPoint(authenticationEntryPoint)

      //设置资源的访问权限
      .and().authorizeRequests()
        .antMatchers("/public/**").permitAll().anyRequest().authenticated()

      .and().formLogin()
        //登陆拦截地址
        .loginProcessingUrl("/login/login")
        //登陆成功的处理器
        .successHandler(authenticationSuccessHandler)
        //登陆失败处理器
        .failureHandler(authenticationFailureHandler)

      .and().logout()
        //登出拦截地址
        .logoutUrl("/login/logout")
        //登出成功的处理器
        .logoutSuccessHandler(logoutSuccessHandler);
  }
}
