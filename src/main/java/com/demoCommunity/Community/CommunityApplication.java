package com.demoCommunity.Community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration
public class CommunityApplication {

    public static void main(String[] args) {

        SpringApplication.run(CommunityApplication.class, args);
    }

}
