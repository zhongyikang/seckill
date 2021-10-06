package com.example.seckill.controller;


import com.example.seckill.pojo.Order;
import com.example.seckill.pojo.User;
import com.example.seckill.service.IOrderService;
import com.example.seckill.vo.GoodsVo;
import com.example.seckill.vo.OrderVo;
import com.example.seckill.vo.RespBody;
import com.example.seckill.vo.RespEnum;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zyk
 * @since 2021-10-04
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @GetMapping("/details/{orderId}")
    @ApiOperation("通过orderId获取订单详情")
    public RespBody details(User user, @PathVariable Long orderId) {
        OrderVo orderVo = orderService.details(orderId);
        return RespBody.success(RespEnum.SUCCESS, orderVo);

    }

}
