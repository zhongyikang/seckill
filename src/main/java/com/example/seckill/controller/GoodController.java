package com.example.seckill.controller;

import com.example.seckill.pojo.User;
import com.example.seckill.vo.RespBody;
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

    @GetMapping("/list")
    public String list(HttpSession session, Model model, @CookieValue("userTicket") String ticket) {
        //这里的返回值String类型表示的是view的name。跳转到对应的页面。
        if (StringUtils.isEmpty(ticket)) {
            return "login"; //跳转到resource/templates/login.html页面
        } else {
            User user = (User) session.getAttribute(ticket);
            if (user == null) {
                return "login";
            }
            model.addAttribute("user", user);
            return "goodList";
        }
    }
}
