package com.glide.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

/**
 * @auther zzyy
 * @create 2020-02-17 21:13
 */
@SpringBootApplication
@EnableCircuitBreaker
//@EnableDiscoveryClient
public class ProviderHystrix8003 {
    public static void main(String[] args) {
        SpringApplication.run(ProviderHystrix8003.class, args);
    }
}
