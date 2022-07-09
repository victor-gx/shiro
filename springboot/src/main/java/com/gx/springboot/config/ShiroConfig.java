package com.gx.springboot.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

//声明为配置类
@Configuration
public class ShiroConfig {
    //创建 ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);

        /*
        添加Shiro内置过滤器，常用的有如下过滤器：
        anon： 无需认证就可以访问
        authc： 必须认证才可以访问
        user： 如果使用了记住我功能就可以直接访问
        perms: 拥有某个资源权限才可以访问
        role： 拥有某个角色权限才可以访问
        */
        // 拦截
        Map<String,String> filterMap = new LinkedHashMap<String, String>();

        //授权过滤器
        filterMap.put("/user/add","perms[user:add]");
        filterMap.put("/user/update","perms[user:update]");

        //授权过滤器
        filterMap.put("/user/add","perms[user:add]");

//        filterMap.put("/user/add","authc");
//        filterMap.put("/user/update","authc");
        filterMap.put("/user/*","authc");

        bean.setFilterChainDefinitionMap(filterMap);
        //设置toLogin请求
        bean.setLoginUrl("/toLogin");
        // 未授权
        bean.setUnauthorizedUrl("/noauth");
        return bean;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm")UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联Realm
        securityManager.setRealm(userRealm);
        return securityManager;
    }


    // 创建 realm 对象
    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }

    //配置ShiroDialect：方言，用于 thymeleaf 和 shiro 标签配合使用
    @Bean
    public ShiroDialect getShiroDialect() {
        return new ShiroDialect();
    }
}
