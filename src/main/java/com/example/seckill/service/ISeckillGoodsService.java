package com.example.seckill.service;

import com.example.seckill.pojo.SeckillGoods;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zyk
 * @since 2021-10-04
 */
public interface ISeckillGoodsService extends IService<SeckillGoods> {

    SeckillGoods getByGoodsId(Long goodsId);
}
