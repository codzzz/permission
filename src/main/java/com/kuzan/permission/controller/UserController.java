package com.kuzan.permission.controller;

import com.kuzan.permission.entity.SysUser;
import com.kuzan.permission.service.SysUserService;
import com.kuzan.permission.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by sui on 2019/3/22.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private SysUserService userService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(String username, String password, String ret, HttpServletRequest request,HttpSession session){
        SysUser user = userService.findUserByKeyword(username);
        String errorMsg = "";
        if (StringUtils.isBlank(username)){
            errorMsg = "用户名不能为空";
        }else if (StringUtils.isBlank(password)){
            errorMsg = "密码不能为空";
        }else if (user == null){
            errorMsg = "查询不到用户";
        }else if (!StringUtils.equals(user.getPassword(), MD5Util.encrypt(password))){
            errorMsg = "用户名或密码错误";
        }else if (user.getStatus() != 1){
            errorMsg = "账户已被冻结，请联系管理员";
        }else {
            //login success
            session.setAttribute("user",user);
            if (StringUtils.isNotBlank(ret)){
                return "redirect:ret";
            }
            return "redirect:/admin/index";
        }
        request.setAttribute("username",username);
        request.setAttribute("error",errorMsg);
        request.setAttribute("ret",ret);
        return "signin";
    }

    @RequestMapping("signin")
    public String signin(){
        return "signin";
    }

    @RequestMapping("logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "signin";
    }
}
