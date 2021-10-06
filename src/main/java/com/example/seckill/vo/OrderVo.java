package com.example.seckill.vo;

import com.example.seckill.pojo.Goods;
import com.example.seckill.pojo.Order;
import lombok.Data;

/**
 * @author zhongyikang
 * @create 2021-10-06 17:35
 */
@Data
public class OrderVo {
    private Order order;
    private Goods goods;
}
