package com.glide.springcloud.util;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

public class ResponseWriter {
    public static DataBuffer write(ServerWebExchange exchange, HttpStatus httpStatus, String message) {
        exchange.getResponse().setStatusCode(httpStatus);
        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        Map<String, Object> resMap = Map.of("message", message,
                "code", httpStatus.value());
        return dataBufferFactory.wrap(resMap.toString().getBytes());
    }
}
