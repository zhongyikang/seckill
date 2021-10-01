package com.example.seckill.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author zyk
 * @since 2021-09-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id，默认为手机号码
     */
    private Long id;

    /**
     * 名称
     */
    private String nickname;

    /**
     * 密码，经过两次md5加密
     */
    private String password;

    /**
     * md5加密的盐值
     */
    private String salt;

    /**
     * 头像
     */
    private String head;

    /**
     * 注册日期
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime registerDate;

    /**
     * 最近登陆日期
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime lastLoginDate;

    /**
     * 登陆次数
     */
    private Integer loginCount;


}
