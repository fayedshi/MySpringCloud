package com.glide.sc.service.impl;

import com.glide.sc.dao.DeptDao;
import com.glide.sc.models.Dept;
import com.glide.sc.service.DeptService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @auther zzyy
 * @create 2020-02-18 10:40
 */
@Service
public class DeptServiceImpl implements DeptService
{
    @Resource
    private DeptDao deptDao;

    public void add(Dept dept)
    {
        deptDao.addDept(dept);
    }

    public Dept get(Long id)
    {
        return deptDao.getDeptById(id);
    }

    @Override
    public List<Dept> list() {
        return deptDao.listDepts();
    }
}
