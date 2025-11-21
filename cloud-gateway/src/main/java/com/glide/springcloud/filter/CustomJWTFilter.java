package com.glide.springcloud.filter;

import com.glide.springcloud.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
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
            return chain.filter(exchange);
        }
        // todo: get authorities from claims
//                        claims.get("roles");
        List<SimpleGrantedAuthority> authorities = ((List<String>) claims.get("roles")).stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();
        Authentication auth = UsernamePasswordAuthenticationToken.authenticated(claims.get("principals"),
                claims.get("credentials"), authorities);
        // security context will be used by rest filters
        SecurityContextHolder.getContext().setAuthentication(auth); // for @EnableMethodSecurity

//        exchange = exchange.mutate().principal(Mono.just(auth)).build();  // not needed for reactive model
        return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
    }
}