package com.example.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.exception.GlobalException;
import com.example.seckill.mapper.UserMapper;
import com.example.seckill.pojo.User;
import com.example.seckill.service.IUserService;
import com.example.seckill.utils.CookieUtils;
import com.example.seckill.utils.MD5Utils;
import com.example.seckill.utils.PhoneFormatCheckUtils;
import com.example.seckill.utils.UUIDUtils;
import com.example.seckill.vo.RespBody;
import com.example.seckill.vo.RespEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public RespBody insertUser(User user, HttpServletRequest request, HttpServletResponse response) {
        //1.判断是否有问题。

//        if(StringUtils.isEmpty(user.getId()) || StringUtils.isEmpty(user.getPassword())) {
//            throw new GlobalException(RespEnum.LOGIN_ERROR);
//            //RespBody.error(RespEnum.LOGIN_ERROR,"user信息不匹配，可能user.id或者user.password为空");
//        }

        if (!PhoneFormatCheckUtils.isPhoneLegal(user.getId().toString())) {
            throw new GlobalException(RespEnum.MOBILE_ERROR);
        }

        //2.md5加密
        String DBpass = MD5Utils.formpassToDBpass(user.getPassword(), MD5Utils.SALT);
        user.setPassword(DBpass);
        user.setSalt(MD5Utils.SALT);

        User userBackGroud = baseMapper.selectById(user.getId());
        if (userBackGroud == null) {
            //注册功能
            user.setLoginCount(1);
            user.setNickname(user.getId().toString());
            baseMapper.insert(user);
        } else {
            //登录
            //1.在后台查看password
            if (userBackGroud.getPassword().equals(DBpass)) {
                user.setLoginCount(userBackGroud.getLoginCount() + 1);
                baseMapper.updateById(user);


                //cookie相关
                String uuid = UUIDUtils.getUUID(); //cookie值
                redisTemplate.opsForValue().set("user:" + uuid, user);
                //request.getSession().setAttribute(uuid, user); //在cookie中添加key-value结构， cookie、user二者映射
                CookieUtils.setCookie(request, response, "userTicket", uuid);


            } else { //密码错误
                throw new GlobalException(RespEnum.LOGIN_PASSWORD_ERROR);
            }


        }

        //登陆成功
        return RespBody.success(RespEnum.SUCCESS);


    }

    @Override
    public User getUserByCookie(String ticket) {
        User user = (User) redisTemplate.opsForValue().get("user:" + ticket);

        return user;
    }
}
