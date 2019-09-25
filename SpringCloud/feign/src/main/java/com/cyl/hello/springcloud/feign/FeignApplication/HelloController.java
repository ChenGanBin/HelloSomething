package com.cyl.hello.springcloud.feign.FeignApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    //编译器报错，无视。 因为这个Bean是在程序启动的时候注入的，编译器感知不到，所以报错。
    @Autowired
    HelloService helloService;

    @GetMapping("/hi")
    public String sayHi(String name) {
        return helloService.sayHiFromClientOne(name);
    }

}
