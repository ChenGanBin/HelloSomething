package com.cyl.security.userdetailservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author cyl
 */
@MapperScan("com.cyl.security.userdetailservice")
@SpringBootApplication
public class UserDetailsServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserDetailsServiceApplication.class, args);
  }
}
