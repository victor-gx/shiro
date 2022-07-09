package com.gx.springboot.service;

import com.gx.springboot.pojo.User;

public interface UserService {
    public User queryUserByName(String name);
}
