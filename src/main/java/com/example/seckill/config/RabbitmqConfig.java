package com.example.seckill.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongyikang
 * @create 2021-10-07 19:00
 */
@Configuration
public class RabbitmqConfig {

    @Bean
    public Queue queue() {
        return new Queue("queue", true);
    }

}
