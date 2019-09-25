package com.cyl.security.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cyl
 */
@RestController
public class TestController {

  @RequestMapping({"/", "/protected/res"})
  public String index() {
    return "这是主页，这是被保护资源，只有登陆了你才能看到该信息";
  }

  @RequestMapping("/public/res")
  public String publicRes() {
    return "这是公共资源";
  }

}
