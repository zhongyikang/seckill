package com.example.seckill.service;

import com.example.seckill.pojo.SeckillOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zyk
 * @since 2021-10-04
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    void addOrder(SeckillOrder seckillOrder);

}
