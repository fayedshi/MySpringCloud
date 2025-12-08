package com.glide.springcloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @auther yuan
 * @create 2020-02-17 21:13
 */
@SpringBootApplication
@EnableDiscoveryClient
//@MapperScan("com.glide.springcloud.mapper")
public class GatewayMain {
    public static void main(String[] args) {
        SpringApplication.run(GatewayMain.class, args);
    }
}
