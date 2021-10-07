package com.example.seckill.controller;

import com.example.seckill.rabbitmq.MQSender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.applet.resources.MsgAppletViewer;

/**
 * @author zhongyikang
 * @create 2021-10-07 19:13
 */
@RestController
@Slf4j
@RequestMapping("/rabbitmq")
@Api("rabbitMQ的测试")
public class RabbitmqController {

    @Autowired
    private MQSender mqSender;

    @ApiOperation("使用生产者发送100条消息")
    @GetMapping("/sendTest")
    public void sendTest() {
        for (int i = 0; i < 100; i++) {
            mqSender.send(Integer.valueOf(i));
        }
    }

}
