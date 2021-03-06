package com.example.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.seckill.pojo.SeckillGoods;
import com.example.seckill.mapper.SeckillGoodsMapper;
import com.example.seckill.service.ISeckillGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsMapper, SeckillGoods> implements ISeckillGoodsService {

    @Override
    public SeckillGoods getByGoodsId(Long goodsId) {
        return baseMapper.selectOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goodsId));
    }
}
