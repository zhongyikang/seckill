package com.example.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.seckill.exception.GlobalException;
import com.example.seckill.pojo.*;
import com.example.seckill.rabbitmq.MQSender;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IOrderService;
import com.example.seckill.service.ISeckillOrderService;
import com.example.seckill.service.IUserService;
import com.example.seckill.service.impl.GoodsServiceImpl;
import com.example.seckill.vo.GoodsVo;
import com.example.seckill.vo.OrderVo;
import com.example.seckill.vo.RespBody;
import com.example.seckill.vo.RespEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * @author zhongyikang
 * @create 2021-10-04 14:42
 */
@RestController
@RequestMapping("/seckill")
@Api("具体秒杀操作")
public class SeckillController implements InitializingBean {


    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MQSender mqSender;

    private HashMap<Long, Boolean> emptyStock = new HashMap<>();

    @ApiOperation("秒杀商品")
    @PostMapping("/{path}/dosecKill")
    public RespBody dosecKill(HttpServletResponse response, User user, Long goodsId, @PathVariable String path) {
        if (user == null) {
            try {
                response.sendRedirect("/login/loginByPhone");
                return RespBody.error(RespEnum.MOBILE_NO_REGISTER);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        boolean isPathTrue = orderService.checkPath(user, goodsId, path);
        if (!isPathTrue) {
            //
            throw new GlobalException(RespEnum.ERROR);
        }

        //如果库存为空， 直接退出。
        if (emptyStock.get(goodsId) == true) {
            throw new GlobalException(RespEnum.STOCK_EMPTY);
        }


        //1. 判断库存，必须大于0
        if ((Integer) redisTemplate.opsForValue().get("seckillGoods:" + goodsId) < 1) {
            emptyStock.put(goodsId, true);
            throw new GlobalException(RespEnum.STOCK_EMPTY);
        } else {
            //减库存操作
            redisTemplate.opsForValue().decrement("seckillGoods:" + goodsId);
        }

        //2. 判断是否重复购买
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            return RespBody.error(RespEnum.REPEATED_SECKILL);
        }

        //3.如果以上条件否符合，则秒杀。创建订单(使用rabbitMQ)
        SeckillMsg seckillMsg = new SeckillMsg(user, goodsId);
        mqSender.sendSeckillMsg(JSON.toJSONString(seckillMsg));


        //异步处理， 下单成功/失败的 信息需要通过这个来返回。
        return RespBody.success(RespEnum.SUCCESS, 0);
    }


    @ApiOperation("前台不断轮询查看秒杀结果")
    @GetMapping("/result")
    public RespBody getResult(User user, Long goodsId) {
        if (user == null) {
            throw new GlobalException(RespEnum.SESSION_ERROR);
        }
        Long orderId = seckillOrderService.getResult(user, goodsId);

        return RespBody.success(RespEnum.SUCCESS, orderId);
    }


    @ApiOperation("秒杀商品测试")
    @GetMapping("/dosecKillTest/{goodsId}")
    public RespBody dosecKillTest(@PathVariable Long goodsId) {


        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);

        User user = userService.getById(17375067111L);

        //判断库存、判断是否重复抢购，如果是，跳转到secFail页面。
        if ((Integer) redisTemplate.opsForValue().get("seckillGoods:" + goodsVo.getId()) < 1) {
            emptyStock.put(goodsId, true);
            throw new GlobalException(RespEnum.STOCK_EMPTY);
        }

        SeckillOrder seckillOrder = seckillOrderService.getOne(
                new QueryWrapper<SeckillOrder>()
                        .eq("user_id", user.getId())
                        .eq("goods_id", goodsId));

        if (seckillOrder != null) {
            throw new GlobalException(RespEnum.REPEATED_SECKILL);
        }


        Order order = orderService.seckill(user, goodsId);


        return RespBody.success(RespEnum.SUCCESS, order);
    }

    @ApiOperation("获取秒杀地址，因为秒杀地址已经被隐藏了") //这个api就是前台点击‘开始秒杀’后发送的请求你
    @GetMapping("/path")
    public RespBody getPath(User user, Long goodsId, String captcha) {
        if (user == null) {
            throw new GlobalException(RespEnum.ERROR);
        }


        //创建一个随机path
        String path = orderService.createPath(user, goodsId);

        boolean isCaptchaMatched = orderService.checkCaptcha(user, goodsId, captcha);


        return RespBody.success(RespEnum.SUCCESS, path);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化bean的时候， 如果实现了initialBean，会自动执行该方法。
        //注意哦： @Controller也是一个bean哦。
        //把秒杀商品加载到redis之中。
        List<GoodsVo> goodsVoList = goodsService.findGoodsVo(); //查询到的结果是秒杀的商品

        if (goodsVoList == null || goodsVoList.size() == 0) return;

        goodsVoList.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            emptyStock.put(goodsVo.getId(), false);
        });
    }
}
