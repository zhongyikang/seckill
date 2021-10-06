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
import com.example.seckill.vo.OrderVo;
import com.example.seckill.vo.RespBody;
import com.example.seckill.vo.RespEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    public RespBody dosecKill(HttpServletResponse response, User user, @PathVariable Long goodsId) {
        if (user == null) {
            try {
                response.sendRedirect("/login/loginByPhone");
                return RespBody.error(RespEnum.MOBILE_NO_REGISTER);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);

        //判断库存、判断是否重复抢购，如果是，跳转到secFail页面。
        if (goodsVo.getStockCount() == 0) {
            return RespBody.error(RespEnum.STOCK_EMPTY);
        }

        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));

        if (seckillOrder != null) {
            return RespBody.error(RespEnum.REPEATED_SECKILL);
        }


        // 如果以上条件否符合，则秒杀。创建订单，并跳转到order页面
        Order order = orderService.seckill(user, goodsVo);

        OrderVo orderVo = new OrderVo();

        orderVo.setOrder(order);
        orderVo.setGoods(goodsVo);


        return RespBody.success(RespEnum.SUCCESS, orderVo);
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
