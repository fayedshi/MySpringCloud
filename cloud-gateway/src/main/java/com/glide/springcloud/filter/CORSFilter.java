package com.glide.springcloud.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

//@Component
public class CORSFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpResponse res = exchange.getResponse();
        HttpHeaders headers = res.getHeaders();
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        headers.add("Access-Control-Allow-Headers", "Content-Type,Authorization,sessionToken,X-TOKEN");
        if (exchange.getRequest().getMethod().equals("OPTIONS")) {
            exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(204));
            return Mono.empty();
        }
        return chain.filter(exchange);
    }
}