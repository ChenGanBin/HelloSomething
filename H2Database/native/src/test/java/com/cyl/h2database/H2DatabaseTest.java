package com.cyl.h2database;

import org.h2.tools.RunScript;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;

public class H2DatabaseTest {

  private static Connection conn;

  @BeforeAll
  public static void init() {
    try {
      Class.forName("org.h2.Driver");
      conn = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
      H2DatabaseTest h2DatabaseTest = new H2DatabaseTest();
      RunScript.execute(conn, new FileReader(h2DatabaseTest.getClass().getResource("/test.sql").getFile()));
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  @AfterAll
  public static void destory() {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  @Test
  public void testInsert() {
    try (Statement statement = conn.createStatement()) {
      long ret = statement.executeLargeUpdate("INSERT INTO `demo` (NAME) VALUES ('测试03')");
      Assertions.assertEquals(1, ret, "插入数据失败");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testQuery() {
    try (Statement statement = conn.createStatement();
         ResultSet resultSet = statement.executeQuery("select * from demo")) {
      resultSet.last();
      Assertions.assertEquals(2, resultSet.getRow(), "读取数据失败");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
