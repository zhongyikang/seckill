package com.example.seckill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhongyikang
 * @create 2021-10-07 20:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillMsg {
    private User user;
    private Long goodsId;
}
