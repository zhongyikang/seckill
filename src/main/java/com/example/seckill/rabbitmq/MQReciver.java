package com.example.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author zhongyikang
 * @create 2021-10-07 19:08
 */
@Service
@Slf4j
public class MQReciver {

    @RabbitListener(queues = "queue") //消费者监听队列
    public void receive(Object msg) {
        log.info("接收消息：" + msg);
    }

}
