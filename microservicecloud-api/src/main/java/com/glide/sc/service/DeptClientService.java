package com.glide.sc.service;

import com.glide.sc.models.Dept;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @auther zzyy
 * @create 2020-02-18 10:40
 */
@FeignClient(value = "CLOUD-DEPT-SERVICE")
public interface DeptClientService {
    @RequestMapping(value = "/dept/create")
    public boolean add(Dept dept);

    @GetMapping(value = "/dept/get/{id}")
    public Dept get(@PathVariable("id") Long id);

    @GetMapping(value = "/dept/list")
    public List<Dept> list();

}
