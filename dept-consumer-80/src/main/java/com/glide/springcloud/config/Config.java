package com.glide.springcloud.config;

//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    @Bean
    @LoadBalanced
    // marked in client side, it decides which service instance to call dynamically
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
