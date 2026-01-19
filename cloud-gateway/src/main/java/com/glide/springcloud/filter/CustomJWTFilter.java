package com.glide.springcloud.filter;

import com.glide.springcloud.util.JwtUtil;
import com.glide.springcloud.util.ResponseWriter;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

@Component
public class CustomJWTFilter implements WebFilter {
    @Autowired
    RedisTemplate redisTemplate;
    final String authHeader = "Authorization";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String jwtToken = exchange.getRequest().getHeaders().getFirst(authHeader);
        if (jwtToken == null || jwtToken.isEmpty()) {
            // let it pass to next filter to process
            return chain.filter(exchange);
        }
        Claims claims;
        try {
            claims = JwtUtil.validateToken(jwtToken);
            if (claims == null) {
                return chain.filter(exchange);
            }
        } catch (Exception e) {
            return exchange.getResponse().writeWith(Mono.just(ResponseWriter.write(exchange, HttpStatus.INTERNAL_SERVER_ERROR, "Token validation failed: " + e.getMessage())));
        }
        List<SimpleGrantedAuthority> authorities = ((List<String>) claims.get("roles")).stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();
        Authentication auth = UsernamePasswordAuthenticationToken.authenticated(claims.get("principals"),
                claims.get("credentials"), authorities); // get null passwords as no pass in token generation which is safe
        // security context will be used by rest filters
        SecurityContextHolder.getContext().setAuthentication(auth); // for @EnableMethodSecurity

//        exchange = exchange.mutate().principal(Mono.just(auth)).build();  // not needed for reactive model
        return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
    }
}