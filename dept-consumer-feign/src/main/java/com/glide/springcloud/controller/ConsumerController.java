package com.glide.springcloud.controller;

import com.glide.springcloud.models.Dept;
import com.glide.springcloud.service.DeptClientService;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {
    @Resource
    private DiscoveryClient discoveryClient;

    @Autowired
    DeptClientService service;

    @RequestMapping(value = "/dept/create")
    public Boolean add(Dept dept) {
        return service.add(dept);
    }

    @GetMapping(value = "/dept/get/{id}")
    public Dept get(@PathVariable("id") Long id) {
        return service.get(id);
    }

    @GetMapping(value = "/dept/list")
    public List<Dept> list() {
        return service.list();
    }


    @GetMapping(value = "/dept/discovery")
    public Object discovery() {
        return service.discovery();
    }


    @GetMapping(value = "/dept/lb")
    public String getLB() {
        return service.getLB();
    }

//    private String fallback(RequestNotPermitted requestNotPermitted) {
//        return "Payment service does not permit further calls";
//    }
}