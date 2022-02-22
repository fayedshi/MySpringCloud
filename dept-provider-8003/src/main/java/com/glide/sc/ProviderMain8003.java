package com.glide.sc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @auther zzyy
 * @create 2020-02-17 21:13
 */
@SpringBootApplication
@EnableEurekaClient
//@EnableDiscoveryClient
public class ProviderMain8003 {
    public static void main(String[] args) {
        SpringApplication.run(ProviderMain8003.class, args);
    }
}
