package com.glide.springcloud.config;

import com.alibaba.nacos.shaded.com.google.gson.JsonObject;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
//import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {
//    @Override
//    public void onAuthenticationSuccess(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Authentication authentication) throws IOException {
//        response.setContentType("application/json, charset=UTF-8");
//        authentication.getPrincipal();
//
//        response.getWriter().println("logged in successfully \n"+ authentication);
//    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatusCode.valueOf(200));
        return Mono.defer(() -> Mono.just(webFilterExchange.getExchange().getResponse()).flatMap(response -> {
            DataBufferFactory dataBufferFactory = response.bufferFactory();
            // 生成JWT token
            Map<String, Object> map = new HashMap<>(2);
//            SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
            UserDetails principal = (UserDetails) authentication.getDetails();
//        map.put("userId", principal.get);
            map.put("username", principal.getUsername());
            map.put("roles", principal.getAuthorities());
//            String token = JwtTokenUtil.generateToken(map, userDetails.getUsername(), jwtTokenExpired);
//            String refreshToken = JwtTokenUtil.generateToken(map, userDetails.getUsername(), jwtTokenRefreshExpired);
            Map<String, Object> tokenMap = new HashMap<>(2);
//            tokenMap.put("token", token);
//            tokenMap.put("refreshToken", refreshToken);
//            DataBuffer dataBuffer = dataBufferFactory.wrap(JSONObject.toJSONString(ResultVoUtil.success(tokenMap)).getBytes());
            DataBuffer dataBuffer = dataBufferFactory.wrap(map.toString().getBytes());
            return response.writeWith(Mono.just(dataBuffer));
        }));
    }
}
