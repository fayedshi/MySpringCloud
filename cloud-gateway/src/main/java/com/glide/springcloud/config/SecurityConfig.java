package com.glide.springcloud.config;

import com.glide.springcloud.filter.CustomJWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

import java.util.Map;


@Configuration(proxyBeanMethods = false)
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    CustomAuthEntrypoint customAuthEntrypoint;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        http.authorizeExchange((authorize) -> authorize // 这里只是应用在AuthorizationWebFilter，然后通过DelegatingReactiveAuthorizationManager去查权限，
                        // 跟新加的filter无关，所有的filter都会经过， 不管path是否permitAll
                        .pathMatchers("/dept/list").hasRole("ADMIN")
                        .pathMatchers("/dept/get/**").hasRole("USER")
                        .pathMatchers("/api/test").hasRole("USER")
                        .pathMatchers("/api/user/login").permitAll()
                        .pathMatchers("/api/user/register").permitAll()
                        //剩下的所有请求都需要认证
                        .anyExchange().authenticated()
                ).securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasicSpec -> httpBasicSpec.disable())
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(customAuthEntrypoint);
                    exception.accessDeniedHandler((exchange, deniedException) -> {
                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
                        Map<String, String> resMap = Map.of("Message", deniedException.getMessage(),
                                "Code", HttpStatus.FORBIDDEN.toString());
                        DataBuffer dataBuffer = dataBufferFactory.wrap(resMap.toString().getBytes());
                        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
                    });
                })
                .authenticationManager(authenticationManager())
                .addFilterBefore(new CustomJWTFilter(), SecurityWebFiltersOrder.SECURITY_CONTEXT_SERVER_WEB_EXCHANGE);
        return http.build();
    }

//    @Bean
//    public SecurityWebFilterChain securedFilterChain(ServerHttpSecurity http) throws Exception {
//        http
////                .securityMatcher("/secured/**")
//                .authorizeExchange(authorize -> authorize
//                        .pathMatchers("/secured/user").hasRole("USER")
//                        .pathMatchers("/secured/admin").hasRole("ADMIN")
//                        .anyExchange().authenticated()
//                )
//                .httpBasic(Customizer.withDefaults())
//                .formLogin(Customizer.withDefaults());
//        return http.build();
//    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(customUserDetailsService);
        authenticationManager.setPasswordEncoder(bcEncoder());
        return authenticationManager;
    }

    @Bean
    public BCryptPasswordEncoder bcEncoder() {
        return new BCryptPasswordEncoder();
    }
}