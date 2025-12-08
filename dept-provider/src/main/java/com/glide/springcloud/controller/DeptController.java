package com.glide.springcloud.controller;

import com.glide.springcloud.models.Dept;
import com.glide.springcloud.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @auther zzyy
 * @create 2020-02-18 10:43
 */

@RestController
@RequestMapping("/dept")
@Slf4j
public class DeptController {
    @Resource
    private DeptService deptService;

    @Value("${server.port}")
    private String serverPort;

    @Value("${spring.application.name}")
    private String appName;

    @Resource
    private DiscoveryClient discoveryClient;

    @RequestMapping(value = "/create")
    public boolean create(@RequestBody Dept dept) {
       deptService.add(dept);
        log.info("*****插入结果：" + true);

        return true;
    }

    @GetMapping(value = "/get/{id}")
    public Dept getDeptById(@PathVariable("id") Long id) {
        Dept dept = deptService.get(id);
        log.info("get method of provider 8001");
        return dept;
    }

    @GetMapping(value = "/list")
    public List<Dept> listDepts() {
        return deptService.list();
    }

    @GetMapping(value = "/lb")
    public String getDeptLB() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        return address.getHostAddress();
    }

    @GetMapping(value = "/discovery")
    public List<ServiceInstance> discovery() {
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            log.info("*****element: "+element);
        }
        //        for (ServiceInstance instance : instances) {
//            log.info(instance.getServiceId()+"\t"+instance.getHost()+"\t"+instance.getPort());
//        }
        return discoveryClient.getInstances(appName);
    }
}
