package com.cyl.persistence.mybatisplus;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author cyl
 */
@Data
@Accessors(chain = true)
public class User {
  private Long id;
  private String name;
  private Integer age;
  private String email;
}
