package com.glide.springcloud.service;

import com.glide.springcloud.model.CloudUser;
import com.glide.springcloud.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    ReactiveAuthenticationManager authenticationManager;

    @Autowired
    RedisTemplate redisTemplate;

//    @Autowired
//    JwtUtil jwtUtil;

    public Mono<Object> login(CloudUser cloudUser) {


        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(cloudUser.getUsername(), cloudUser.getPasswd());
        Mono<Authentication> authentication = authenticationManager.authenticate(authenticationRequest);

        return authentication.map(auth -> {
            if (auth.isAuthenticated()) {
                // reached controller, means no more filters to meet, thereby no need to set security context
//                SecurityContextHolder.getContext().setAuthentication(auth);
                Map<String, Object> claims = new HashMap<>();
                claims.put("principals", auth.getPrincipal());
                claims.put("credentials", auth.getCredentials());
                claims.put("roles", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
                String jwtToken = JwtUtil.generateToken(claims, auth.getName());
//                redisTemplate.opsForValue().set(jwtToken, auth);
                return Map.of("principal", auth.getPrincipal(), "token", jwtToken);
//                return jwtToken;
            }
            return Map.of("principal", auth.getPrincipal(),
                    "code", 200, "message", "authentication failure");
        });
    }
}
