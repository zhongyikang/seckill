package com.example.seckill.exception;

import com.example.seckill.vo.RespEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhongyikang
 * @create 2021-10-01 21:25
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalException extends RuntimeException {
    private RespEnum respEnum;
}
