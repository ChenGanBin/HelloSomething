package com.cyl.security.dynamic;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cyl
 */
@RestController
public class TestController {

  @RequestMapping({"/protected/res01"})
  public String res01() {
    return "这是被保护资源01，只有拥有权限ROLE_01的人才能访问";
  }

  @RequestMapping({"/protected/res02"})
  public String res02() {
    return "这是被保护资源02，只有拥有权限ROLE_02的人才能访问";
  }

  @RequestMapping("/public/res")
  public String publicRes() {
    return "这是公共资源";
  }

}
