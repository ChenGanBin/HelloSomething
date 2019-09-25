package com.cyl.security.userdetailservice;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author cyl
 */
@Data
@Accessors(chain = true)
@TableName("user")
public class CustomUser {

  private Long id;

  private String name;

  private String username;

  private String password;
}
