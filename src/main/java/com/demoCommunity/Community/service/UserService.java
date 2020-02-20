package com.demoCommunity.Community.service;

import com.demoCommunity.Community.dao.UserMapper;
import com.demoCommunity.Community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findUserById(int userId) {
         return userMapper.selectById(userId);
    }
}
