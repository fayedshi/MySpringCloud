package com.glide.springcloud.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.glide.springcloud.mapper.UserMapper;
import com.glide.springcloud.mapper.UserRolesMapper;
import com.glide.springcloud.model.CloudUser;
import com.glide.springcloud.model.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRolesMapper userRolesMapper;
    @Autowired
    BCryptPasswordEncoder bcEncoder;

    @Transactional
    public Map<String, Object> register(CloudUser cloudUser) {
        try {
            CloudUser user = userMapper.selectOne(new QueryWrapper<CloudUser>().eq("username", cloudUser.getUsername()));
            if (user != null) {
                return Map.of("Code", 400, "Message", "Registration failed, Username exists.");
            }
            cloudUser.setPasswd(bcEncoder.encode((cloudUser.getPasswd())));
            userMapper.insert(cloudUser);
            userRolesMapper.insert(new UserRoles(cloudUser.getId(), 2));
            return Map.of("Code", 200, "Message", "Success");
        } catch (Exception e) {
            return Map.of("Code", -1, "Message", e.getMessage());
        }
    }
}
