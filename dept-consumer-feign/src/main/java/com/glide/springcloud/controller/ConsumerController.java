package com.glide.springcloud.controller;

import com.glide.springcloud.models.Dept;
import com.glide.springcloud.service.DeptClientService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${server.port}")
    private String serverPort;

    @GetMapping(value = "/dept/test")
    public String test() {
        return "From consumer "+serverPort;
    }

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
}