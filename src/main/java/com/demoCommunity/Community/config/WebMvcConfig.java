package com.demoCommunity.Community.config;

import com.demoCommunity.Community.controller.interceptor.AlphaInterceptor;
import com.demoCommunity.Community.controller.interceptor.LoginRequiredInterceptor;
import com.demoCommunity.Community.controller.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    //这个配置类是用来配置到底需要拦截哪些，也就是静态资源完全不管，管真的需要拦截的就可以
    @Autowired
    private AlphaInterceptor alphaInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;
    //Spring在注册的时候将对象注入拦截器

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //把静态资源排除在外，不显示
        registry.addInterceptor(alphaInterceptor)
                .excludePathPatterns("/**/*.css","**/*.js","**/*.png","/**/*.jpg","/**/*.jpeg")
                .addPathPatterns("/register","/login");
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css","**/*.js","**/*.png","/**/*.jpg","/**/*.jpeg");

        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/**/*.css","**/*.js","**/*.png","/**/*.jpg","/**/*.jpeg");
    }
}
