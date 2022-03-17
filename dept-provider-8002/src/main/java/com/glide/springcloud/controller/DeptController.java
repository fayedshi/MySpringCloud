package com.glide.springcloud.controller;

import com.glide.springcloud.models.Dept;
import com.glide.springcloud.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @auther zzyy
 * @create 2020-02-18 10:43
 */
@RestController
@Slf4j
public class DeptController {
    @Resource
    private DeptService deptService;

    @Value("${server.port}")
    private String serverPort;

//    @Resource
//    private DiscoveryClient discoveryClient;

    @RequestMapping(value = "/dept/create")
    public boolean create(@RequestBody Dept dept) {
       deptService.add(dept);
//        log.info("*****插入结果：" + result);

        return true;
    }

    @GetMapping(value = "/dept/get/{id}")
    public Dept getDeptById(@PathVariable("id") Long id) {
        Dept dept = deptService.get(id);
        log.info("get method of provider 8002");
        return dept;
    }

    @GetMapping(value = "/dept/list")
    public List<Dept> listDepts() {
        return deptService.list();
    }

    @GetMapping(value = "/dept/lb")
    public String getDeptLB() {
        return serverPort;
    }

//    @GetMapping(value = "/dept/discovery")
//    public Object discovery() {
//        List<String> services = discoveryClient.getServices();
//        for (String element : services) {
////            log.info("*****element: "+element);
//        }
//
//        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-Dept-SERVICE");
//        for (ServiceInstance instance : instances) {
//            log.info(instance.getServiceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
//        }
//
//        return this.discoveryClient;
//    }
//


//
//    @GetMapping(value = "/dept/feign/timeout")
//    public String deptFeignTimeout() {
//        // 业务逻辑处理正确，但是需要耗费3秒钟
//        try {
//            TimeUnit.SECONDS.sleep(3);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return serverPort;
//    }
//
//    @GetMapping("/dept/zipkin")
//    public String DeptZipkin() {
//        return "hi ,i'am Deptzipkin server fall back，welcome to atguigu，O(∩_∩)O哈哈~";
//    }
}
