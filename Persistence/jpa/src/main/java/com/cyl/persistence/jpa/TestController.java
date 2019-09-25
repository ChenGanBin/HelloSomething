package com.cyl.persistence.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class TestController {

  @Autowired
  private TestRepository userRepository;

  @GetMapping("/")
  public List<Test> test() {
    Iterable<Test> iterable = userRepository.findAll();
    List<Test> list = CollectionUtils.arrayToList(iterable);
    return list;
  }

  @GetMapping("/save")
  public String save() {
    Test test = new Test();
    test.setName("测试999");
    userRepository.save(test);
    return "保存用户："+test.getName()+" 主键："+test.getId();
  }

  @GetMapping("get")
  public String get(Long id) {
    Optional<Test> optional = userRepository.findById(id);
    return optional.get().toString();
  }
}
