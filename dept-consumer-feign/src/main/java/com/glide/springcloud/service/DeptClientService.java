package com.glide.springcloud.service;

import com.glide.springcloud.models.Dept;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @auther zzyy
 * @create 2020-02-18 10:40
 */
//@Component
@FeignClient(value = "cloud-dept-service", fallbackFactory = DeptFallbackFactory.class)
public interface DeptClientService {
    @RequestMapping(value = "/dept/create")
    public boolean add(Dept dept);

    @RequestMapping(value = "/dept/get/{id}")
    public Dept get(@PathVariable("id") Long id);

    @RequestMapping(value = "/dept/list")
    public List<Dept> list();

    @RequestMapping(value = "/dept/discovery")
    public Object discovery();


    @RequestMapping(value = "/dept/lb")
    public String getLB();
}
