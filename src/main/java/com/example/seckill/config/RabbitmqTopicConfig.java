package com.example.seckill.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.Topic;

/**
 * @author zhongyikang
 * @create 2021-10-07 20:19
 */
@Configuration
public class RabbitmqTopicConfig {
    private static final String QUEUE = "seckillQueue";
    private static final String EXCHANGE = "seckillExchange";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(topicExchange()).with("seckill.#");
    }
}
