package com.glide.springcloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.glide.springcloud.model.CloudUser;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper extends BaseMapper<CloudUser> {
}
