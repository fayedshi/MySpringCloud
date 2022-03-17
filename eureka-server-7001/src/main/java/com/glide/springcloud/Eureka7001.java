package com.glide.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @auther zzyy
 * @create 2020-02-17 21:13
 */
@SpringBootApplication
@EnableEurekaServer
//@EnableDiscoveryClient
public class Eureka7001 {
    public static void main(String[] args) {
        SpringApplication.run(Eureka7001.class, args);
    }
}
