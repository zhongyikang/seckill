package com.example.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhongyikang
 * @create 2021-09-30 14:06
 */
@Getter
@AllArgsConstructor
public enum RespEnum {

    SUCCESS(200,"SUCCESS"),
    ERROR(500,"ERROR"),
    LOGIN_ERROR(401, "用户名或者密码错误or无效");


    long code;
    String message;


}
