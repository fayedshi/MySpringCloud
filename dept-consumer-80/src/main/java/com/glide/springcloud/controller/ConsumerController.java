package com.glide.springcloud.controller;

import com.glide.springcloud.models.Dept;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

//import javax.annotation.Resource;
import java.util.List;

/**
 * @auther zzyy
 * @create 2020-02-18 10:43
 */
@RestController

// consumer doesn't need any service or doesn't care about how the service is provided.

// call consumer  http://localhost:81/consumer/dept/get/1

public class ConsumerController {
    // original http://localhost:8001
    private String serviceUrl = "http://cloud-dept-service";

    @Resource
    private DiscoveryClient discoveryClient;

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/consumer/dept/create")
    public Boolean add(Dept dept) {
        return restTemplate.postForEntity(serviceUrl + "/dept/create/", dept, Boolean.class).getBody();
//        log.info("*****插入结果："+result);

    }

    @GetMapping(value = "/consumer/dept/get/{id}")
    public ResponseEntity<Dept> get(@PathVariable("id") Long id) {
        return restTemplate.getForEntity(serviceUrl + "/dept/get/" + id, Dept.class);
    }

    @GetMapping(value = "/consumer/dept/list")
    public List<Dept> listDepts() {
        return restTemplate.getForEntity(serviceUrl + "/dept/list/", List.class).getBody();
    }

    @GetMapping(value = "/consumer/dept/discovery")
    public Object discovery() {
        return restTemplate.getForEntity(serviceUrl + "/dept/discovery/", Object.class).getBody();
    }
}
