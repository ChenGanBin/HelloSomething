package com.cyl.security.rest.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限不足拒绝访问处理器（仅在用户未登录时触发）
 *
 * @author cyl
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

    //返回权限不足（401）的提示信息
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "权限不足,请先登陆");
  }
}
