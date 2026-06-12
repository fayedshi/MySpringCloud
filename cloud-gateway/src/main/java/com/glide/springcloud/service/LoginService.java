package com.glide.springcloud.service;

import com.glide.springcloud.config.RabbitConfig;
import com.glide.springcloud.model.CloudUser;
import com.glide.springcloud.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    private static final Log logger = LogFactory.getLog(LoginService.class);
    @Autowired
    ReactiveAuthenticationManager authenticationManager;

    //todo: redis version not compatible, fix later
    @Autowired
    RedisTemplate redisTemplate;

    @Value("${jwt.secret}")
    String secret;

    @Autowired
    RabbitProducer rabbitProducer;

    public Mono<Map<String, Object>> login(CloudUser cloudUser) {
        Authentication authToken = UsernamePasswordAuthenticationToken.unauthenticated(cloudUser.getUsername(), cloudUser.getPassword());
        try {
            Mono<Authentication> authentication = authenticationManager.authenticate(authToken); // go to retrieveUser()->CustomUserDetailService
            return authentication.map(auth -> {
                // reached controller, means no more filters to meet, thereby no need to set security context
                Map<String, Object> claims = new HashMap<>();
                claims.put("principals", auth.getPrincipal());
                claims.put("roles", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
                String jwtToken = JwtUtil.generateToken(claims, auth.getName(), this.secret);
                sendMessage(auth.getName());
//                redisTemplate.opsForValue().set(jwtToken, auth);
                return Map.of("code", 200,
                        "username", auth.getName(), "authorities", auth.getAuthorities(), "token", jwtToken);
            }).onErrorResume(throwable ->
                    Mono.just(Map.of("code", -1, "message", throwable.getMessage())));
        } catch (Exception e) {
            logger.error("User not found " + e.getMessage());
            return Mono.just(Map.of("code", -1, "message", e.getMessage()));
        }
    }

    private void sendMessage(String userName) {
        new Thread(() -> rabbitProducer.sendMessage(String.format("User %s logged in.", userName), RabbitConfig.ROUTING_KEY_LOGIN, 5000)).start();
    }
}