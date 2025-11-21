package com.glide.springcloud.config;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class CustomAuthEntrypoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        Map<String, String> resMap = Map.of("Message", ex.getMessage(),
                "Code", HttpStatus.UNAUTHORIZED.toString());
        DataBuffer dataBuffer = dataBufferFactory.wrap(resMap.toString().getBytes());
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }
}
