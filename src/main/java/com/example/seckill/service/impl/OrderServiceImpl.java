package com.example.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.seckill.exception.GlobalException;
import com.example.seckill.pojo.*;
import com.example.seckill.mapper.OrderMapper;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.service.ISeckillGoodsService;
import com.example.seckill.service.ISeckillOrderService;
import com.example.seckill.utils.MD5Utils;
import com.example.seckill.utils.UUIDUtils;
import com.example.seckill.vo.GoodsVo;
import com.example.seckill.vo.OrderVo;
import com.example.seckill.vo.RespEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zyk
 * @since 2021-10-04
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private IGoodsService goodsService;


    @Autowired
    private ISeckillGoodsService seckillGoodsService;


    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional //事务，出现异常的时候进行回滚操作
    @Override
    public Order seckill(User user, Long goodsId) {
        //到了这里，表示1当前用户未秒杀， 2当前秒杀库存充足
        //1查询秒杀库存，如果有， 秒杀库存量 -1，普通商品库存量 - 1， 创建order表。

        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);

        //更新秒杀商品库存,解决超卖问题。（如果库存小于0，不更新）
        SeckillGoods seckillGoods = seckillGoodsService.getByGoodsId(goods.getId());

        if (seckillGoods.getStockCount() < 1) {
            redisTemplate.opsForValue().set("isStockEmpty:" + goodsId, "0");
            throw new GlobalException(RespEnum.STOCK_EMPTY);
        }

        boolean hasUpdated = seckillGoodsService.update(
                new UpdateWrapper<SeckillGoods>()
                        .eq("goods_id", goods.getId())
                        .gt("stock_count", 0)
                        .setSql("stock_count = stock_count - 1"));


        if (hasUpdated == false) {//更新失败,因为没有库存了。
            throw new GlobalException(RespEnum.STOCK_EMPTY);
        }


        //更新普通商品库存
        goods.setGoodsStock(goods.getGoodsStock() - 1);
        goodsService.updateById(goods);


        //创建普通订单
        Order order = new Order();

        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setStatus(0);//0： 新建未支付


        long orderKey = baseMapper.insert(order); //返回值是刚才插入的主键值

        //创建秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(orderKey);
        seckillOrder.setUserId(user.getId());

        seckillOrderService.addOrder(seckillOrder);


        //在redis中添加库存信息，让前台判定是否重复购买。 不经过mysql数据库。
        redisTemplate.opsForValue().set("order:" + user.getId() + ":" + goods.getId(), seckillOrder);


        //返回订单
        return order;

    }

    @Override
    public OrderVo details(Long orderId) {


        Order order = baseMapper.selectById(orderId);
        if (order == null) {
            new GlobalException(RespEnum.ORDER_NO_EXIST);
        }

        Goods goods = goodsService.getById(order.getGoodsId());


        OrderVo orderVo = new OrderVo();
        orderVo.setGoods(goods);
        orderVo.setOrder(order);

        return orderVo;

    }

    @Override
    public String createPath(User user, Long goodsId) {
        String str = MD5Utils.passToMD5Password(UUIDUtils.getUUID());
        redisTemplate.opsForValue().set("seckillPath:" + user.getId() + ":" + goodsId, str);
        return str;
    }

    @Override
    public boolean checkPath(User user, Long goodsId, String path) {
        if (user == null || goodsId < 0 || StringUtils.isEmpty(path)) {
            throw new GlobalException(RespEnum.SUCCESS);
        }

        String key = "seckillPath:" + user.getId() + ":" + goodsId;
        String value = (String) redisTemplate.opsForValue().get(key);
        if (value != null && value.equals(path)) {
            return true;
        }
        return false;

    }

    @Override
    public boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if (user == null || goodsId < 0 || captcha == null) {
            throw new GlobalException(RespEnum.ERROR);
        }


        //这个redis值是在创建验证码api时存入到redis之中的。 （但是本项目没有实现...因为那个依赖在网上找不到）
        String redisCaptcha = (String) redisTemplate.opsForValue().get("seckillCaptcha:" + user.getId() + ":" + goodsId);
        if (captcha.equals(redisCaptcha)) {
            return true;
        }
        return false;


        //"seckillPath:" + user.getId() + ":" + goodsId


    }
}
