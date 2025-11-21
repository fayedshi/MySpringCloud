package com.glide.springcloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.glide.springcloud.model.CloudUser;
import com.glide.springcloud.model.Roles;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RolesMapper extends BaseMapper<Roles> {
}
