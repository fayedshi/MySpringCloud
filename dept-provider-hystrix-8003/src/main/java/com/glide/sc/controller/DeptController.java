package com.glide.sc.controller;

import com.glide.springcloud.models.Dept;
import com.glide.sc.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @auther zzyy
 * @create 2020-02-18 10:43
 */
@RestController
@Slf4j
public class DeptController {
    @Resource
    private DeptService deptService;

    @Resource
    private DiscoveryClient discoveryClient;

    @Value("${server.port}")
    private String serverPort;

    @RequestMapping(value = "/dept/get/{id}")
    public Dept getDeptById(@PathVariable("id") Long id) throws Exception {
        try {
            Dept dept = deptService.get(id);
            if(dept==null)
                throw new Exception("hell");
            return dept;
        } catch (Exception e) {
            throw e;
        }
    }

//    public Dept hysHandler(Long id) {
//        return new Dept().setDeptName("no dept exists");
//    }

    @GetMapping(value = "/dept/discovery")
    public Object discovery() {
        List<String> services = discoveryClient.getServices();
        // microservies list
        for (String element : services) {
            log.info("*****element: "+element);
        }

        // instances of some micro-service
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-Dept-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info(instance.getServiceId() + "\t" + instance.getHost() + "\t" + instance.getPort() + "\t" + instance.getUri());
        }

        return this.discoveryClient;
    }

    @GetMapping(value = "/dept/lb")
    public String getDeptLB() {
        return serverPort;
    }

    @GetMapping(value = "/dept/feign/timeout")
    public String deptFeignTimeout() {
        // 业务逻辑处理正确，但是需要耗费3秒钟
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return serverPort;
    }

    @GetMapping("/dept/zipkin")
    public String DeptZipkin() {
        return "hi ,I'm Deptzipkin server fall back，welcome to atguigu";
    }
}
