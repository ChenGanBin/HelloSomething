package com.cyl.hello.springcloud.eurekaclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class EurekaClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }

    @Value("${server.port}")
    String port;

    @GetMapping("index")
    public String index() {
        return "hello, this is eureka client";
    }

    @RequestMapping("/hi")
    public String hi(@RequestParam(value = "name", defaultValue = "zkpk") String name) {
        return "hi " + name + " , i am from port: " + port;
    }
}
