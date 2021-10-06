package com.example.seckill.service;

import com.example.seckill.pojo.Goods;
import com.example.seckill.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.pojo.User;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zyk
 * @since 2021-10-04
 */
public interface IOrderService extends IService<Order> {


    Order seckill(User user, Goods goods);
}
