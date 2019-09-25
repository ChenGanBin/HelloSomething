package com.cyl.hello.springcloud.feign.FeignApplication;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "eurka-client", fallback = HelloServiceImpl.class)
public interface HelloService {

    @RequestMapping("/hi")
    String sayHiFromClientOne(String name);
}
