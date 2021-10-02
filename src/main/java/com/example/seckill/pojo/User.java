package com.example.seckill.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime registerDate;

    /**
     * 最近登陆日期
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime lastLoginDate;

    /**
     * 登陆次数
     */
    private Integer loginCount;


}
