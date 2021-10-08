package com.example.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.seckill.pojo.SeckillOrder;
import com.example.seckill.mapper.SeckillOrderMapper;
import com.example.seckill.pojo.User;
import com.example.seckill.service.ISeckillOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zyk
 * @since 2021-10-04
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void addOrder(SeckillOrder seckillOrder) {
        baseMapper.insert(seckillOrder);
    }

    @Override
    public Long getResult(User user, Long goodsId) {
        //返回的是订单信息。
        SeckillOrder seckillOrder = baseMapper.selectOne(new QueryWrapper<SeckillOrder>()
                .eq("user_id", user.getId())
                .eq("goods_id", goodsId));

        if (seckillOrder != null) {
            return seckillOrder.getOrderId();
        } else if (redisTemplate.hasKey("isStockEmpty:" + goodsId) == true) {
            return -1L; //没库存了
        } else {
            //秒杀失败
            return 0L;
        }

    }
}
