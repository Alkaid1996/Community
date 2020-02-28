package com.demoCommunity.Community.controller.interceptor;

import com.demoCommunity.Community.annotation.LoginRequired;
import com.demoCommunity.Community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    //需要判断是否登录，所以HostHolder获取用户，获取成功则是在登录
    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //只判定方法，SpringMVC,拦截到的是一个方法的话，就变成HandlerMethod
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod=(HandlerMethod) handler;
            Method method=handlerMethod.getMethod();
            LoginRequired loginRequired=method.getAnnotation(LoginRequired.class);
            //如果这个注解不是空的，判断有没有登录   ==null相当于没登录
            if(loginRequired!=null&&hostHolder.getUser()==null){
                //@Controller下的重定向return字符串底层也是这么写的
                response.sendRedirect(request.getContextPath()+"/login");
                return false;
            }
        }
        return true;
    }
}
