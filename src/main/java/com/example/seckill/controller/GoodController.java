package com.example.seckill.controller;

import com.example.seckill.pojo.User;
import com.example.seckill.service.IUserService;
import com.example.seckill.vo.RespBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author zhongyikang
 * @create 2021-10-01 23:35
 */
@Controller("/good")
public class GoodController {

    @Autowired
    private IUserService userService;

    @GetMapping("/list")
    public String list(Model model, User user /*MVCConfig会对参数进行监听，在Controller入参之前，如果看到了User， 会自动到Cookie中查找插入进入User*/) {
        //这里的返回值String类型表示的是view的name。跳转到对应的页面。

        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);
        return "goodList";

    }
}
