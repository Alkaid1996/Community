package com.demoCommunity.Community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableAutoConfiguration
public class CommunityApplication {

    @PostConstruct
    public void init(){
        //解决netty启动冲突的问题
        //处理方案在NettyUtils的setAvailableProcessors()
        System.setProperty("es.set.netty.runtime.available.processors","false");
    }

    public static void main(String[] args) {

        SpringApplication.run(CommunityApplication.class, args);
    }

}
