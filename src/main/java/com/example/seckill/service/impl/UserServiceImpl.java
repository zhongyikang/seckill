package com.example.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.mapper.UserMapper;
import com.example.seckill.pojo.User;
import com.example.seckill.service.IUserService;
import com.example.seckill.utils.CookieUtils;
import com.example.seckill.utils.MD5Utils;
import com.example.seckill.utils.PhoneFormatCheckUtils;
import com.example.seckill.utils.UUIDUtils;
import com.example.seckill.vo.RespBody;
import com.example.seckill.vo.RespEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyk
 * @since 2021-09-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public RespBody insertUser(User user, HttpServletRequest request, HttpServletResponse response) {
        //1.判断是否有问题。

        if(StringUtils.isEmpty(user.getId()) || StringUtils.isEmpty(user.getPassword())) {
            return RespBody.error(RespEnum.LOGIN_ERROR,"user信息不匹配，可能user.id或者user.password为空");
        }
        if(PhoneFormatCheckUtils.isPhoneLegal(user.getId().toString()) == false) {
            return RespBody.error(RespEnum.LOGIN_ERROR,"登录手机号格式错误");
        }

        //2.md5加密
        String DBpass = MD5Utils.formpassToDBpass(user.getPassword(), MD5Utils.SALT);
        user.setPassword(DBpass);
        user.setSalt(MD5Utils.SALT);

        User userBackGroud = baseMapper.selectById(user.getId());
        if(userBackGroud == null) {
            //注册功能
            user.setLoginCount(1);
            user.setNickname(user.getId().toString());
            baseMapper.insert(user);
        } else {
            //登录
            //1.在后台查看password
            if(userBackGroud.getPassword().equals(DBpass)) {
                user.setLoginCount(userBackGroud.getLoginCount() + 1);
                baseMapper.updateById(user);


                String uuid = UUIDUtils.getUUID(); //cookie值
                request.getSession().setAttribute(uuid,user); //在cookie中添加key-value结构， cookie、user二者映射
                CookieUtils.setCookie(request, response, "userTicket", uuid);


            } else {
                return RespBody.error(RespEnum.LOGIN_ERROR,"密码错误");
            }




        }

        return RespBody.success();



    }
}
