package com.glide.springcloud.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.glide.springcloud.mapper.CloudUserMapper;
import com.glide.springcloud.model.CloudUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    private CloudUserMapper userMapper;

    @Override
    public Mono<UserDetails> findByUsername(String username) throws UsernameNotFoundException {
        //同时连接权限表取出用户的权限信息
        CloudUser cloudUser = userMapper.selectUserWithAuth(username);
        if (cloudUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        //todo: use SimpleGrantedAuthority to configure authorities with roles for user
//        return Mono.just(new CloudUser(cloudUser.getUsername(), cloudUser.getPasswd(), null));
        return Mono.just(cloudUser);
    }
}