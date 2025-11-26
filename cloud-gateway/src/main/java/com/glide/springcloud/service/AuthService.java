package com.glide.springcloud.service;

import com.glide.springcloud.model.CloudUser;
import com.glide.springcloud.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    //    private static final Log log = LoggerFactory.getLogger(AuthService.class);
    private static final Log logger = LogFactory.getLog(AuthService.class);
    @Autowired
    ReactiveAuthenticationManager authenticationManager;

    @Autowired
    RedisTemplate redisTemplate;

    public Mono<Object> login(CloudUser cloudUser) {
        Authentication authToken = UsernamePasswordAuthenticationToken.unauthenticated(cloudUser.getUsername(), cloudUser.getPasswd());
        try {
            Mono<Authentication> authentication = authenticationManager.authenticate(authToken);
            return authentication.map(auth -> {
                // reached controller, means no more filters to meet, thereby no need to set security context
//                SecurityContextHolder.getContext().setAuthentication(auth);
                Map<String, Object> claims = new HashMap<>();
                claims.put("principals", auth.getPrincipal());
                claims.put("credentials", auth.getCredentials());
                claims.put("roles", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
                String jwtToken = JwtUtil.generateToken(claims, auth.getName());
//                redisTemplate.opsForValue().set(jwtToken, auth);
                return Map.of("username", auth.getName(), "authorities", auth.getAuthorities(), "token", jwtToken);
            });
        } catch (Exception e) {
            logger.error("User not found " + e.getMessage());
            return Mono.just(Map.of("Code", -1, "Message", e.getMessage()));
//            return Mono.error(e);
        }
    }
}