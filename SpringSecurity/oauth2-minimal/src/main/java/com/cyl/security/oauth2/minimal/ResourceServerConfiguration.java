package com.cyl.security.oauth2.minimal;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author cyl
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

  @Override
  public void configure(HttpSecurity http) throws Exception {
    //设置资源的访问权限，允许匿名访问公共资源（“public”）,需要登陆后才能访问被保护资源（“protected”）
    http

      .authorizeRequests()
      .antMatchers("/public/**").permitAll().anyRequest().authenticated();
  }
}
