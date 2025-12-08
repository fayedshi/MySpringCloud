package com.glide.springcloud.dao;

import com.glide.springcloud.models.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther zzyy
 * @create 2020-02-18 10:27
 */
@Mapper
public interface DeptDao
{
    Dept getDeptById(@Param("id") Long id);
    List<Dept> listDepts();
    void addDept(Dept dept);
}
