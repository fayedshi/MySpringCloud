package com.glide.springcloud.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.glide.springcloud.mapper.RolePermissionsMapper;
import com.glide.springcloud.mapper.RolesMapper;
import com.glide.springcloud.mapper.UserMapper;
import com.glide.springcloud.mapper.UserRolesMapper;
import com.glide.springcloud.model.CloudUser;
import com.glide.springcloud.model.RolePermissions;
import com.glide.springcloud.model.Roles;
import com.glide.springcloud.model.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRolesMapper userRolesMapper;
    @Autowired
    RolesMapper rolesMapper;

    @Override
    public Mono<UserDetails> findByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<CloudUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        CloudUser cloudUser = userMapper.selectOne(wrapper);
        if (cloudUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
//        QueryWrapper<UserRoles> wrapper = new QueryWrapper<>();
//        wrapper.eq("username", username);
        List<UserRoles> userRoles = userRolesMapper.selectList(new
                QueryWrapper<UserRoles>().eq("user_Id", cloudUser.getId()));
        List<Integer> roleIdList = userRoles.stream().map(UserRoles::getRoleId).toList();
        List<Roles> roles = rolesMapper.selectBatchIds(roleIdList);
        List<SimpleGrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
//        List<RolePermissions> rolePermissions= rolePermissionsMapper.selectList(new
//                QueryWrapper<RolePermissions>().in("roleId",roleIdList));
//        rolePermissions.stream().map(rp->rp.get)
        //todo: use SimpleGrantedAuthority to configure authorities with roles for user
        return Mono.just(new User(cloudUser.getUsername(), cloudUser.getPasswd(), authorities));
    }
}