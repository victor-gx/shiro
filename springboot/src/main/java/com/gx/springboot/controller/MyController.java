package com.gx.springboot.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyController {

    @RequestMapping({"/","/index"})
    public String toIndex(Model model) {
        model.addAttribute("msg","hello,Shiro");
        return "index";
    }

    @RequestMapping("/user/add")
    public String toAdd() {
        return "user/add";
    }

    @RequestMapping("/user/update")
    public String toUpdate() {
        return "user/update";
    }

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/login")
    public String login(String username,String password,Model model) {
        //1. 获取Subject
        Subject subject = SecurityUtils.getSubject();
        //2. 封装用户的数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        //3. 执行登录的方法，只要没有异常就代表登录成功！
        try{
            subject.login(token); //登录成功！返回首页
            return "index";
        } catch(UnknownAccountException e) { //用户名不存在
            model.addAttribute("msg","用户名不存在");
            return "login";
        }  catch (IncorrectCredentialsException e) { //密码错误
            model.addAttribute("msg","密码错误");
            return "login";
        }
    }

    @RequestMapping("/noauth")
    @ResponseBody
    public String noAuth() {
        return "未经授权不能访问此页面";
    }

}
