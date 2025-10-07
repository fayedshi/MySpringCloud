package com.glide.springcloud.service;

import com.glide.springcloud.models.Dept;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyFallbackService implements DeptClientService {

    @Override
    public boolean add(Dept dept) {
        return false;
    }

    @Override
    public Dept get(Long id) {
        return new Dept().setDeptName("no dept exists");
    }

    @Override
    public List<Dept> list() {
        return List.of(new Dept().setDeptName("wrong invocation of list()"));
    }

    @Override
    public Object discovery() {
        return null;
    }

    @Override
    public String getLB() {
        return null;
    }
}
