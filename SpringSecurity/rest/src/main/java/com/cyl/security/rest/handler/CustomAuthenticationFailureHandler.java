package com.cyl.security.rest.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录认证失败处理器
 *
 * @author cyl
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    response.setHeader("Content-Type", "application/json;charset=utf-8");

    //  这里可以根据异常的类型来判断登陆失败的原因
    //if(exception instanceof DisabledException) {
    //  response.getWriter().print("{\"errno\":10000,\"errmsg\":\"账号已被冻结，无法使用！\"}");
    //}else if (exception instanceof BadCredentialsException) {
    //  response.getWriter().print("{\"errno\":10000,\"errmsg\":\"账号或密码输入错误！\"}");
    //}

    response.getWriter().print("账号或密码输入错误！");
    response.getWriter().flush();
  }
}
