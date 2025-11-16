package com.glide.springcloud.config;

import com.glide.springcloud.filter.CustomJWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;


@Configuration(proxyBeanMethods = false)
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        http.authorizeExchange((authorize) -> authorize // 这里只是应用在AuthorizationWebFilter，然后通过DelegatingReactiveAuthorizationManager去查权限，
                                // 跟新加的filter无关，所有的filter都会经过， 不管path是否permitAll
                                .pathMatchers("/dept/list").hasRole("ADMIN")
                                .pathMatchers("/dept/get/**").hasRole("USER")
//                        .pathMatchers("/api/test").authenticated()
                                .pathMatchers("/api/user/login").permitAll()
                                .pathMatchers("/api/user/register").permitAll()
                                //剩下的所有请求都需要认证
                                .anyExchange().authenticated()
                )
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable
//                                .authenticationSuccessHandler(new CustomAuthenticationSuccessHandler())
                )
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasicSpec -> httpBasicSpec.disable())
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(null);
                    exception.accessDeniedHandler((request, response) -> {
                        return Mono.create(resp -> {
                            resp.success();
                        });
                    });
                })
                .authenticationManager(authenticationManager())
                // what is the authentication filter?
                .addFilterBefore(new CustomJWTFilter(), SecurityWebFiltersOrder.AUTHORIZATION);
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
