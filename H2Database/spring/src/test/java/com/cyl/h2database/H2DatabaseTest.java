package com.cyl.h2database;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootTest
public class H2DatabaseTest {

  @Resource
  private DataSource dataSource;

  @Test
  public void testInsert() {
    try (Statement statement = dataSource.getConnection().createStatement()) {
      long ret = statement.executeLargeUpdate("INSERT INTO `demo` (NAME) VALUES ('测试03')");
      Assertions.assertEquals(1, ret, "插入数据失败");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testQuery() {
    try (Statement statement = dataSource.getConnection().createStatement();
         ResultSet resultSet = statement.executeQuery("select * from demo")) {
      resultSet.last();
      Assertions.assertEquals(2, resultSet.getRow(), "读取数据失败");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
