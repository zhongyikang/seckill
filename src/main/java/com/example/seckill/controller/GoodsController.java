package com.example.seckill.controller;


import com.example.seckill.pojo.SeckillGoods;
import com.example.seckill.pojo.User;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IUserService;
import com.example.seckill.vo.GoodsVo;
import com.example.seckill.vo.RespBody;
import com.example.seckill.vo.RespEnum;
import com.example.seckill.vo.SeckillGoodsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zyk
 * @since 2021-10-04
 */
@RestController
@RequestMapping("/goods")
@Api
public class GoodsController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IGoodsService goodsService;

    @ApiOperation("查看后台所有商品，需要有前台(这里特指秒杀商品)")
    @GetMapping("/list")
    public RespBody list(User user /*MVCConfig会对参数进行监听，在Controller入参之前，如果看到了User， 会自动到Cookie中查找插入进入User*/,
                         HttpServletResponse response) {
        //这里的返回值String类型表示的是view的name。跳转到对应的页面。

        if (user == null) {
            //未登录， 重定向到登录页面
            try {
                response.sendRedirect("/login/loginByPhone");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 查看redis中有没有该缓存。
         * 1. 有，直接返回缓存。
         * 2. 没有， 从后台得到数据 + 创建redis缓存 + 返回缓存到前台。
         *
         * ps:缓存的页面需要设定过期时间。
         *
         * -------------------------------------
         * 如果是前后端分离， 前台已经有了模板，后台只需要传递json数据，
         * 这时候，缓存的对象就是数据库中的数据。更多的缓存是对象。
         *
         * more：
         *如何保证缓存数据和mysql数据库中的数据的一致性？
         *
         *
         *
         */
        List<GoodsVo> goodsVoList = goodsService.findGoodsVo();
        return RespBody.success(RespEnum.SUCCESS, goodsVoList);

    }


    @ApiOperation("查看后台单个秒杀商品详情，需要有前台")
    @GetMapping("/goodsDetail/{goodsId}")
    public RespBody goodsDetail(User user /*MVCConfig会对参数进行监听，在Controller入参之前，如果看到了User， 会自动到Cookie中查找插入进入User*/
            , @PathVariable Long goodsId) {

        GoodsVo goodsDetail = goodsService.findGoodsVoByGoodsId(goodsId);

        int seckillStatus; //秒杀状态； 0（未开始）、1（正在开始）、2（已经结束）
        int remainSecond = 0; //秒杀倒计时
        Date nowDate = new Date();
        Date startTime = Date.from(goodsDetail.getStartTime().atZone(ZoneId.systemDefault()).toInstant());
        Date endTime = Date.from(goodsDetail.getEndTime().atZone(ZoneId.systemDefault()).toInstant());


        if (nowDate.getTime() - startTime.getTime() < 0) {
            //秒杀未开始
            seckillStatus = 0;
            remainSecond = ((int) ((nowDate.getTime() - startTime.getTime()) / 1000));
        } else if (nowDate.getTime() < endTime.getTime()) {
            //秒杀中
            seckillStatus = 1;
        } else {
            //秒杀结束
            seckillStatus = 2;
        }

        SeckillGoodsVo seckillGoodsVo = new SeckillGoodsVo(user, remainSecond, seckillStatus, goodsDetail);

        return RespBody.success(RespEnum.SUCCESS, seckillGoodsVo);

    }


    @ApiOperation("测试查看所有后台的商品")
    @GetMapping("/test")
    public RespBody getAllGoods() {
        List<GoodsVo> goodsVo = goodsService.findGoodsVo();
        return RespBody.success(RespEnum.SUCCESS, goodsVo);
    }

    @ApiOperation("测试通过商品id查看后台的商品")
    @GetMapping("/test/{goodsId}")
    public RespBody getAllGoods1(@PathVariable Long goodsId) {
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        return RespBody.success(RespEnum.SUCCESS, goods);
    }


}
