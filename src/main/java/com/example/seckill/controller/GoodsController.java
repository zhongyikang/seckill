package com.example.seckill.controller;


import com.example.seckill.pojo.User;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IUserService;
import com.example.seckill.vo.GoodsVo;
import com.example.seckill.vo.RespBody;
import com.example.seckill.vo.RespEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation("查看后台所有商品，需要有前台")
    @GetMapping("/list")
    public String list(Model model, User user /*MVCConfig会对参数进行监听，在Controller入参之前，如果看到了User， 会自动到Cookie中查找插入进入User*/) {
        //这里的返回值String类型表示的是view的name。跳转到对应的页面。

        if (user == null) {
            return "login";
        }


        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
        return "goodList";

    }


    @ApiOperation("查看后台所有商品，需要有前台")
    @GetMapping("/goodsDetail/{goodsId}")
    public String goodsDetail(Model model, User user /*MVCConfig会对参数进行监听，在Controller入参之前，如果看到了User， 会自动到Cookie中查找插入进入User*/
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


        model.addAttribute("remainSecond", remainSecond);
        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("goodsDetail", goodsDetail);


        return "goodsDetail";

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
