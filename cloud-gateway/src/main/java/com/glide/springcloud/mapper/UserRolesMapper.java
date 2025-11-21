package com.glide.springcloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.glide.springcloud.model.CloudUser;
import com.glide.springcloud.model.UserRoles;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRolesMapper extends BaseMapper<UserRoles> {
}
