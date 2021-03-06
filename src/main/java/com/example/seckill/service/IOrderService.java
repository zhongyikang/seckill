package com.example.seckill.service;

import com.example.seckill.pojo.Goods;
import com.example.seckill.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.pojo.User;
import com.example.seckill.vo.OrderVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zyk
 * @since 2021-10-04
 */
public interface IOrderService extends IService<Order> {


    Order seckill(User user, Long goodsId);

    OrderVo details(Long orderId);

    String createPath(User user, Long goodsId);

    boolean checkPath(User user, Long goodsId, String path);

    boolean checkCaptcha(User user, Long goodsId, String captcha);

}
