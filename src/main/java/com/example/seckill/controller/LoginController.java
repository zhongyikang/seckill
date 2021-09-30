package com.example.seckill.controller;

import com.example.seckill.pojo.User;
import com.example.seckill.service.IUserService;
import com.example.seckill.vo.LoginVo;
import com.example.seckill.vo.RespBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhongyikang
 * @create 2021-09-30 12:17
 */

@Api
@RestController
@RequestMapping("/login")
@Log4j
public class LoginController {

    @Autowired
    private IUserService userService;

    @PostMapping("/loginByPhone")
    @ApiOperation(value ="使用手机号进行登录")
    public RespBody login(@RequestBody @Validated LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        User user = new User();
        user.setId(loginVo.getMobile());
        user.setPassword(loginVo.getPassword());

        return userService.insertUser(user,request,response);
        //return RespBody.success();
    }
}
