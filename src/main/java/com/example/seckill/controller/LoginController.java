package com.example.seckill.controller;

import com.example.seckill.pojo.User;
import com.example.seckill.service.IUserService;
import com.example.seckill.vo.LoginVo;
import com.example.seckill.vo.RespBody;
import com.example.seckill.vo.RespEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/loginByPhone")
    @ApiOperation(value = "使用手机号进行登录")
    public RespBody login(@RequestBody @Validated LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        User user = new User();
        user.setId(loginVo.getMobile());
        user.setPassword(loginVo.getPassword());

        return userService.insertUser(user, request, response);
        //return RespBody.success();
    }

    @GetMapping("/test")
    @ApiOperation(value = "测试")
    public RespBody test() {
        log.info("执行1");
        try {
            String value = (String) redisTemplate.opsForValue().get("key1");

            LoginVo loginVo = new LoginVo();
            loginVo.setMobile(1737507281L);
            loginVo.setPassword("asdf");

            redisTemplate.opsForValue().set("loginVo", loginVo);


            Object loginVo1 = redisTemplate.opsForValue().get("loginVo");

            System.out.println(loginVo1);


            log.info("执行2");
            System.out.println(value);
            log.info("执行3");
            return RespBody.success(RespEnum.SUCCESS, value);
        } catch (ClassCastException e) {
            System.out.println("无法转换啊啊啊");
        } catch (Exception e) {
            System.out.println("输出异常了。。。");
            e.printStackTrace();
        }

        return RespBody.success(RespEnum.SUCCESS);

    }


}
