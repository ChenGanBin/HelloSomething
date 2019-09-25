package com.cyl.hello.springcloud.feign.FeignApplication;

import org.springframework.stereotype.Component;

@Component
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHiFromClientOne(String name) {
        return "sorry " + name;
    }
}
