package com.example.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @author zhongyikang
 * @create 2021-09-30 14:08
 */
@AllArgsConstructor
@Setter
@Getter
public class RespBody {
    private long code;
    private String message;
    private Object object;

    public static RespBody success(RespEnum respEnum) {
        return new RespBody(respEnum.getCode(), respEnum.getMessage(), null);
    }

    public static RespBody success(RespEnum respEnum, Object object) {
        return new RespBody(respEnum.getCode(), respEnum.getMessage(), object);
    }

    public static RespBody error(RespEnum respEnum) {
        return new RespBody(respEnum.getCode(), respEnum.getMessage(), null);
    }

    public static RespBody error(RespEnum respEnum, Object obj) {
        return new RespBody(respEnum.getCode(), respEnum.getMessage(), obj);
    }

}
