package com.example.seckill.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.seckill.exception.GlobalException;
import com.example.seckill.pojo.SeckillMsg;
import com.example.seckill.pojo.SeckillOrder;
import com.example.seckill.pojo.User;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IOrderService;
import com.example.seckill.vo.GoodsVo;
import com.example.seckill.vo.RespBody;
import com.example.seckill.vo.RespEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author zhongyikang
 * @create 2021-10-07 19:08
 */
@Service
@Slf4j
public class MQReciver {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;


    @RabbitListener(queues = "queue") //消费者监听队列
    public void receive(Object msg) {
        log.info("接收消息：" + msg);
    }

    @RabbitListener(queues = "seckillQueue")
    public void receiveSeckillMsg(String msg) {
        //1.得到seckill信息
        JSONObject jsonObject = JSONObject.parseObject(msg);
        SeckillMsg seckillMsg = JSON.toJavaObject(jsonObject, SeckillMsg.class);


        User user = seckillMsg.getUser();
        Long goodsId = seckillMsg.getGoodsId();


        //2.再次判断库存是否为0、是否重复购买
        GoodsVo goodsvo = goodsService.findGoodsVoByGoodsId(goodsId);
        if (goodsvo.getStockCount() < 1) {
            throw new GlobalException(RespEnum.STOCK_EMPTY);
        }

        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            throw new GlobalException(RespEnum.REPEATED_SECKILL);
        }


        //3.减库存操作
        orderService.seckill(user, goodsId);


    }

}
