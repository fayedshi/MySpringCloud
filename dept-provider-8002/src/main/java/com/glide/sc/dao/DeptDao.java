package com.glide.sc.dao;

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
    public Dept getDeptById(@Param("id") Long id);
    public List<Dept> listDepts();
    public void addDept(Dept dept);
}
