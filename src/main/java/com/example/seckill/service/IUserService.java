package com.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.pojo.User;
import com.example.seckill.vo.RespBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyk
 * @since 2021-09-30
 */
public interface IUserService extends IService<User> {

    RespBody insertUser(User user, HttpServletRequest request, HttpServletResponse response);

    User getUserByCookie(String ticket);

}
