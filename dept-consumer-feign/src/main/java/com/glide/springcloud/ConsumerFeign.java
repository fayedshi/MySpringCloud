package com.glide.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @auther zzyy
 * @create 2020-02-17 21:13
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.glide.springcloud")
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.glide.springcloud")
public class ConsumerFeign {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerFeign.class, args);
    }
}
