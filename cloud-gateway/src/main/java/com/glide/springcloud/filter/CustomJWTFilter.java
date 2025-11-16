package com.glide.springcloud.filter;

import com.glide.springcloud.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Component
public class CustomJWTFilter implements WebFilter {
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (exchange.getRequest().getPath().value().equals("/api/user/login")) {
            return chain.filter(exchange);
        }
        final String authHeader = "Authorization";
        String jwtToken = exchange.getRequest().getHeaders().getFirst(authHeader);
        if (jwtToken == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().add("error", "Not authenticated");
            return chain.filter(exchange);
        }
        Claims claims = JwtUtil.validateToken(jwtToken);
        if (claims == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        }
        // todo: get authorities from claims
//                        claims.get("roles");
        Authentication auth = UsernamePasswordAuthenticationToken.authenticated(claims.get("principals"),
                claims.get("credentials"), (Collection<? extends GrantedAuthority>) claims.get("roles"));
        // security context will be used by rest filters
        return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
    }
}