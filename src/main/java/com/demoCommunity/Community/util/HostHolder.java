package com.demoCommunity.Community.util;

import com.demoCommunity.Community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 持有用户信息，用于代替session对象
 */
@Component
public class HostHolder {

    //该HostHolder是用ThreadLocal来构建
    //每一个浏览器向服务器发送请求的时候，服务器都会构建一个线程
    //会将这个浏览器目前的状态放在ThreadLocal里

    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
