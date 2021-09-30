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

    public static RespBody success() {
        return new RespBody(RespEnum.SUCCESS.getCode(),RespEnum.SUCCESS.getMessage(),null);
    }

    public static RespBody success(Object object) {
        return new RespBody(RespEnum.SUCCESS.getCode(),RespEnum.SUCCESS.getMessage(),object);
    }

    public static RespBody error() {
        return new RespBody(RespEnum.ERROR.getCode(),RespEnum.ERROR.getMessage(),null);
    }

    public static RespBody error(RespEnum respEnum,Object obj) {
        return new RespBody(respEnum.getCode(),respEnum.getMessage(),obj);
    }

}
