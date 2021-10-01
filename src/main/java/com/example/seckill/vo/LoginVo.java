package com.example.seckill.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhongyikang
 * @create 2021-09-30 14:32
 */
@Data
public class LoginVo {

    /**
     * 1.如果验证不通过，会抛出一个异常，进入BindException之中。
     * 2. GlobalExceptionHandler有一个@RestControllerAdvice注解， 可以进行异常的处理，
     * 结果是返回到前台一个RespBody对象。
     */
    @NotNull(message = "用户账号不能为空")
    private Long mobile;

    @NotBlank(message = "请输入密码：")
    private String password;
}
