package com.cyl.persistence.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MinimalApplication.class)
public class MinimalTest {

  @Resource
  private UserMapper userMapper;

  @Test
  public void aInsert() {
    User user = new User();
    user.setName("小羊");
    user.setAge(3);
    user.setEmail("abc@mp.com");
    Assert.assertTrue(userMapper.insert(user) > 0);
    // 成功直接拿会写的 ID
    System.err.println("\n插入成功 ID 为：" + user.getId());
  }


  @Test
  public void bDelete() {
    Assert.assertTrue(userMapper.deleteById(3L) > 0);
    Assert.assertTrue(userMapper.delete(new QueryWrapper<User>()
      .lambda().eq(User::getName, "Sandy")) > 0);
  }


  @Test
  public void cUpdate() {
    Assert.assertTrue(userMapper.updateById(new User().setId(1L).setEmail("ab@c.c")) > 0);
    Assert.assertTrue(userMapper.update(new User().setName("mp"),
      new UpdateWrapper<User>().lambda()
        .set(User::getAge, 3)
        .eq(User::getId, 2)) > 0);
  }


  @Test
  public void dSelect() {
    Assert.assertEquals("ab@c.c", userMapper.selectById(1L).getEmail());
    User user = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getId, 2));
    Assert.assertEquals("mp", user.getName());
    Assert.assertTrue(3 == user.getAge());
  }
}
