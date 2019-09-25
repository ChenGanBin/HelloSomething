package com.cyl.security.dynamic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @author cyl
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private FilterInvocationSecurityMetadataSource customFilterSecurityMetadataSource;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //创建用户admin,必须最小有一个权限
    auth.inMemoryAuthentication()
      .withUser("admin").password("{noop}1234").authorities("ROLE_01");
  }



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
      .and().logout().logoutUrl("/login/logout")
      //配置自定义的CustomFilterSecurityMetadataSource
      .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
        public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
          fsi.setSecurityMetadataSource(customFilterSecurityMetadataSource);
          //fsi.setAccessDecisionManager(fsi.getAccessDecisionManager());
          return fsi;
        }
      });
  }
}
