package com.glide.springcloud.service.impl;

import com.glide.springcloud.dao.DeptDao;
import com.glide.springcloud.models.Dept;
import com.glide.springcloud.service.DeptService;
//import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @auther zzyy
 * @create 2020-02-18 10:40
 */
@Service
@Slf4j
public class DeptServiceImpl implements DeptService {
    @Resource
    private DeptDao deptDao;
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;
    final String redisKeyPrefix = "dept-";


    public void add(Dept dept) {
        deptDao.addDept(dept);
    }

    public Dept get(Long id) {
        return deptDao.getDeptById(id);
//        Object dept = redisTemplate.opsForValue().get(redisKeyPrefix + id);
//        if (dept == null) {
//            Dept dt = deptDao.getDeptById(id);
//            redisTemplate.opsForValue().set(redisKeyPrefix + id, dt);
//            log.debug("query from database " + redisKeyPrefix + id);
//            return dt;
//        }
//        log.debug("query from cache");
//        return (Dept) dept;
    }

    @Override
    public List<Dept> list() {
//        try {
//            // mock long waiting case
//            Thread.sleep(5000);
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }
        return deptDao.listDepts();
    }
}
