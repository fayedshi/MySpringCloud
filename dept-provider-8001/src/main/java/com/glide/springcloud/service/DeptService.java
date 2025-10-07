package com.glide.springcloud.service;

import com.glide.springcloud.models.Dept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther zzyy
 * @create 2020-02-18 10:40
 */
public interface DeptService
{
    void add(Dept Dept);
    Dept get(@Param("id") Long id);
    List<Dept> list();
}
