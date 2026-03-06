package com.glide.springcloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.glide.springcloud.model.CloudUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CloudUserMapper extends BaseMapper<CloudUser> {
    CloudUser selectUserWithAuth(@Param("username") String username);

}
