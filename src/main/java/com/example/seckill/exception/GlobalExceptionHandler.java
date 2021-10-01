package com.example.seckill.exception;

import com.example.seckill.vo.RespBody;
import com.example.seckill.vo.RespEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author zhongyikang
 * @create 2021-10-01 21:20
 */

@RestControllerAdvice //表示这是一个异常处理类
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class) //异常处理
    public RespBody ExceptionHandler(Exception e) {
        //非校验
        if (e instanceof GlobalException) {
            GlobalException ex = (GlobalException) e;
            return RespBody.error(ex.getRespEnum());

            //校验
        } else if (e instanceof BindException) { //校验不匹配的时候会抛出该异常
            BindException ex = (BindException) e;
            RespBody respBody = RespBody.error(RespEnum.BIND_ERROR);
            respBody.setMessage(respBody.getMessage()/*"参数校验异常"*/ + ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()); //ex.getMessage就是在校验注解上面的message内容
            return respBody;
        }


        //如果不是以上，返回默认的error异常。
        return RespBody.error(RespEnum.ERROR);

    }
}
