package com.glide.springcloud.service;

import com.glide.springcloud.models.Dept;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DeptFallbackFactory implements FallbackFactory {
    @Override
    public DeptClientService create(Throwable throwable) {
        return new DeptClientService() {

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
                return Arrays.asList(new Dept().setDeptName("wrong invocation of list()"));
            }

            @Override
            public Object discovery() {
                return null;
            }

            @Override
            public String getLB() {
                return null;
            }
        };
    }
}
