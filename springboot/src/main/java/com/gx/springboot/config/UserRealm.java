package com.gx.springboot.config;

import com.gx.springboot.pojo.User;
import com.gx.springboot.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

//自定义Realm
public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

    //执行授权逻辑
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("执行了=>授权逻辑PrincipalCollection");

        //给资源进行授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //添加资源的授权字符串
        //info.addStringPermission("user:add");

        Subject subject = SecurityUtils.getSubject(); //获得当前对象
        User currentUser = (User) subject.getPrincipal(); //拿到User对象
        info.addStringPermission(currentUser.getPerms()); //设置权限

        return info;
    }

    //执行认证逻辑
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了=>认证逻辑AuthenticationToken");


        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        //真实连接数据库
        User user = userService.queryUserByName(usernamePasswordToken.getUsername());

        if (user==null) {
            //用户名不存在
            return null; //shiro底层就会抛出 UnknownAccountException
        }


        Subject currentsubject = SecurityUtils.getSubject();
        currentsubject.getSession().setAttribute("loginUser",user);

        //2. 验证密码,我们可以使用一个AuthenticationInfo实现类 SimpleAuthenticationInfo
        // shiro会自动帮我们验证！重点是第二个参数就是要验证的密码！
        return new SimpleAuthenticationInfo(user, user.getPwd(), "");
    }
}
