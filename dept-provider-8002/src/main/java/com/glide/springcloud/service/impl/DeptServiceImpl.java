package com.glide.springcloud.service.impl;

import com.glide.springcloud.dao.DeptDao;
import com.glide.springcloud.models.Dept;
import com.glide.springcloud.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    RedisTemplate<String, Object> redisTemplate;
    final String redisKeyPrefix = "dept-";

    public void add(Dept dept) {
        deptDao.addDept(dept);
    }

    public Object get(Long id) {
        Object dept = redisTemplate.opsForValue().get(redisKeyPrefix + id);
        if (Objects.isNull(dept)) {
            Dept dt = deptDao.getDeptById(id);
            redisTemplate.opsForValue().set(redisKeyPrefix + id, dt);
            log.debug("query from database " + redisKeyPrefix + id);
            return dt;
        }
        log.debug("query from cache");
        return  dept;
    }

    @Override
    public List<Dept> list() {
        return deptDao.listDepts();
    }
}
