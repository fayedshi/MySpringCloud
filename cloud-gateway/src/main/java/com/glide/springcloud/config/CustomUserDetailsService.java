package com.glide.springcloud.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.glide.springcloud.mapper.UserMapper;
import com.glide.springcloud.model.CloudUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Mono<UserDetails> findByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<CloudUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        CloudUser cloudUser = userMapper.selectOne(wrapper);
        if (cloudUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return Mono.just(new User(cloudUser.getUsername(), cloudUser.getPasswd(), new ArrayList<>()));
    }
}