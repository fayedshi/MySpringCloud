package com.glide.springcloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.glide.springcloud.model.CloudUser;
import com.glide.springcloud.model.Permissions;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionsMapper extends BaseMapper<Permissions> {
}
