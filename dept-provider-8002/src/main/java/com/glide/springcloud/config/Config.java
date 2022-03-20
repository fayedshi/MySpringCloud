package com.glide.springcloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class Config {
    @Bean
    public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate();

        RedisSerializer stringSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer j2j = new Jackson2JsonRedisSerializer(Object.class);
        template.setDefaultSerializer(j2j);
        template.setValueSerializer(j2j);
        template.setConnectionFactory(factory);
        return template;
    }
}
