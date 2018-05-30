package com.pinyougou.shop.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @version V1.0
 * @ClassName: ValidateActionJson
 * @Description: 运营商后台登陆控制器
 * @Auther: Jianxiong Yi
 * @Date: 2018/5/29 20:26
 */
@RequestMapping("/login")
@RestController
public class LoginController {

    /**
     * 获得用户名
     */
    @GetMapping("/getUsername")
    public Map<String,String> getUsername() {
        Map<String,String> map = new HashMap<>();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        map.put("username",username);
        return map;
    }
}
