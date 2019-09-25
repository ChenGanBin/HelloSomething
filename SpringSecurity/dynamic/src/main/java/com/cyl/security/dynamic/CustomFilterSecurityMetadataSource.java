package com.cyl.security.dynamic;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * URL权限获取：如果没有标明所需权限则放行，如果标明权限则用户需要相应权限才能放行
 * @author cyl
 */
@Component
public class CustomFilterSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

  @Override
  public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
    FilterInvocation filterInvocation = (FilterInvocation) object;
    String totalUrl = filterInvocation.getRequestUrl();
    //分离出url中的参数
    String[] urls = totalUrl.split("\\?");

    // 可以获取URL的方法（GET、POST...）
    //String method = filterInvocation.getRequest().getMethod();

    //这里模拟从数据库中获取数据
    Set<ConfigAttribute> attributes = new HashSet<>();
    if ("/protected/res01".equals(urls[0])) {
      attributes.add(new SecurityConfig("ROLE_01"));
    } else if ("/protected/res02".equals(urls[0])) {
      attributes.add(new SecurityConfig("ROLE_01"));
    }
    return attributes;
  }

  @Override
  public Collection<ConfigAttribute> getAllConfigAttributes() {
    return null;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return FilterInvocation.class.isAssignableFrom(clazz);
  }
}
