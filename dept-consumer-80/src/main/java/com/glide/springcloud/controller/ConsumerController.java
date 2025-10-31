package com.glide.springcloud.controller;

import com.glide.springcloud.models.Dept;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @auther yuan
 * @create 2020-02-18 10:43
 */
@RequestMapping("/consumer")
@RestController

// consumer doesn't need any service or doesn't care about how the service is provided.

public class ConsumerController {
    private final String serviceUrl = "http://dept-provider";

    @Resource
    private DiscoveryClient discoveryClient;

    @Autowired
    RestTemplate restTemplate;

    @Value("${server.port}")
    String serverPort;

    @RequestMapping(value = "/dept/create")
    public Boolean add(Dept dept) {
        return restTemplate.postForEntity(serviceUrl + "/dept/create/", dept, Boolean.class).getBody();
//        log.info("*****插入结果："+result);

    }

    @GetMapping(value = "/dept/get/{id}")
    public ResponseEntity<Dept> get(@PathVariable("id") Long id) {
        return restTemplate.getForEntity(serviceUrl + "/dept/get/" + id, Dept.class);
    }

    @GetMapping(value = "/dept/list")
    public List<Dept> listDepts() {
        return restTemplate.getForEntity(serviceUrl + "/dept/list", List.class).getBody();
    }

    @GetMapping(value = "/dept/discovery")
    public List<ServiceInstance> discovery() {
        return discoveryClient.getInstances("dept-provider");
    }

    @GetMapping(value = "/dept/test")
    public String test() {
        return "From consumer " + serverPort;
    }
}
