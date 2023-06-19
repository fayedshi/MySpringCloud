package com.glide.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @auther zzyy
 * @create 2020-02-17 21:13
 */
@SpringBootApplication
@EnableZuulProxy
public class ZuulMain {
    public static void main(String[] args) {
        SpringApplication.run(ZuulMain.class, args);
    }
}
