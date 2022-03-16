package com.glide.sc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @auther zzyy
 * @create 2020-02-17 21:13
 */
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
//@EnableDiscoveryClient
public class ProviderHystrix8003 {
    public static void main(String[] args) {
        SpringApplication.run(ProviderHystrix8003.class, args);
    }
}
