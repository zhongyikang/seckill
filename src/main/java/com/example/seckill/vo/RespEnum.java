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

    //通用
    SUCCESS(200, "SUCCESS"),
    ERROR(500, "ERROR"),

    //登录
    LOGIN_ERROR(500201, "用户名或者密码不能为空"),
    LOGIN_PASSWORD_ERROR(500201, "用户名或者密码错误"),
    MOBILE_ERROR(500202, "手机号码格式不正确"),
    BIND_ERROR(500203, "参数校验错误:");


    private final long code;
    private final String message;


}
