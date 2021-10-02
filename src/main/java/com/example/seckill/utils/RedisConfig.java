package com.example.seckill.utils;

import io.lettuce.core.dynamic.RedisCommandFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author zhongyikang
 * @create 2021-10-02 16:56
 */

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        /**
         * 整个操作的的结果就是：
         * 序列化时，插入的结构式String,Object(动态映射)。
         * 在JSON中，可以看到插入的value结构中有类信息，在反序列化的时候通过反射构建对象。
         */
        //key序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //value序列化
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        //hash
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setConnectionFactory(redisConnectionFactory);

        return redisTemplate;
    }
}
