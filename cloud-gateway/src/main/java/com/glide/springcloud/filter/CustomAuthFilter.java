package com.glide.springcloud.filter;

import com.glide.springcloud.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


@Component
public class CustomAuthFilter implements GatewayFilter, Ordered {
    @Autowired
    RedisTemplate redisTemplate;


    @Autowired
    JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (token == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().add("error","Not authenticated");
//            String data = "{\"code\":" + 11429 + ",\"msg\": \"" + "request limit" + "\"}";
            return exchange.getResponse().setComplete();
        }
        return Mono.fromCallable(() -> isValidToken(token)).subscribeOn(Schedulers.parallel()).flatMap(isValid -> {
            if (!isValid) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        });
    }

    private boolean isValidToken(String token) {
        if (redisTemplate.opsForValue().get(token) == null) {
            return false;
        }
        Claims claims = jwtUtil.validateToken(token);
        return claims != null;
    }

    @Override
    public int getOrder() {
        return 0;
    }

}

