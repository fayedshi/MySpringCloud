package com.glide.sc.controller;

import com.glide.sc.models.Dept;
import com.glide.sc.service.DeptClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @auther zzyy
 * @create 2020-02-18 10:43
 */
@RestController

public class ConsumerController {
//    @Resource
//    private DiscoveryClient discoveryClient;

    @Autowired
    DeptClientService service;

    @RequestMapping(value = "consumer/dept/create")
    public Boolean add(Dept dept) {
        return service.add(dept);
    }

    @GetMapping(value = "consumer/dept/get/{id}")
    public Dept get(@PathVariable("id") Long id) {
        return service.get(id);
    }

    @GetMapping(value = "consumer/dept/list")
    public List<Dept> list() {
        return service.list();
    }


    @GetMapping(value = "consumer/dept/discovery")
    public Object discovery() {
        return service.discovery();
    }
}