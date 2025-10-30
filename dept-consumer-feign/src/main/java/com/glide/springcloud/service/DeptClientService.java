package com.glide.springcloud.service;

import com.glide.springcloud.models.Dept;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @auther zzyy
 * @create 2020-02-18 10:40
 */
//@Component
@FeignClient(value = "dept-provider", fallback = MyFallbackService.class)
public interface DeptClientService {

    @RequestMapping(value = "/dept/create")
    boolean add(Dept dept);

    @RequestMapping(value = "/dept/get/{id}")
    @RateLimiter(name = "inst-ratelimiter", fallbackMethod = "getByIdFallback")
    Dept get(@PathVariable("id") Long id);

    default Dept getByIdFallback(Exception exception) {
        System.out.println("circuit breaker default method");
        exception.printStackTrace();
        return new Dept();
    }

    @RequestMapping(value = "/dept/list")
    List<Dept> list();

    @RequestMapping(value = "/dept/discovery")
    Object discovery();


    @RequestMapping(value = "/dept/lb")
    String getLB();
}

