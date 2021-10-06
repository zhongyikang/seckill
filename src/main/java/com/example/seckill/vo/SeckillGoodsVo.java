package com.example.seckill.vo;

import com.example.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zhongyikang
 * @create 2021-10-06 17:23
 */

@Data
@AllArgsConstructor
public class SeckillGoodsVo {
    private User user;
    private int remainSecond;
    private int seckillStatus;
    private GoodsVo goodsVo;
}
