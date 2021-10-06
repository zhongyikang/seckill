package com.example.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.seckill.exception.GlobalException;
import com.example.seckill.pojo.Order;
import com.example.seckill.pojo.SeckillOrder;
import com.example.seckill.pojo.User;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IOrderService;
import com.example.seckill.service.ISeckillOrderService;
import com.example.seckill.service.IUserService;
import com.example.seckill.service.impl.GoodsServiceImpl;
import com.example.seckill.vo.GoodsVo;
import com.example.seckill.vo.RespBody;
import com.example.seckill.vo.RespEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhongyikang
 * @create 2021-10-04 14:42
 */
@RestController
@RequestMapping("/seckill")
@Api("具体秒杀操作")
public class SeckillController {


    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IUserService userService;

    @ApiOperation("秒杀商品")
    @PostMapping("/dosecKill/{goodsId}")
    public String dosecKill(Model model, User user, @PathVariable Long goodsId) {
        if (user == null)
            return "login";


        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);

        //判断库存、判断是否重复抢购，如果是，跳转到secFail页面。
        if (goodsVo.getStockCount() == 0) {
            model.addAttribute("errMsg", RespEnum.STOCK_EMPTY.getMessage());
            return "seckillFail";
        }

        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));

        if (seckillOrder != null) {
            model.addAttribute("errMsg", RespEnum.REPEATED_SECKILL.getMessage());
            return "seckillFail";
        }

        // 如果以上条件否符合，则秒杀。创建订单，并跳转到order页面
        Order order = orderService.seckill(user, goodsVo);

        model.addAttribute("order", order);
        model.addAttribute("goods", goodsVo);
        return "orderDetail";
    }

    @ApiOperation("秒杀商品测试")
    @GetMapping("/dosecKillTest/{goodsId}")
    public RespBody dosecKillTest(@PathVariable Long goodsId) {


        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);

        User user = userService.getById(17375067111L);

        //判断库存、判断是否重复抢购，如果是，跳转到secFail页面。
        if (goodsVo.getStockCount() == 0) {
            throw new GlobalException(RespEnum.STOCK_EMPTY);
        }

        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));

        if (seckillOrder != null) {
            throw new GlobalException(RespEnum.REPEATED_SECKILL);
        }


        Order order = orderService.seckill(user, goodsVo);


        return RespBody.success(RespEnum.SUCCESS, order);
    }


}
