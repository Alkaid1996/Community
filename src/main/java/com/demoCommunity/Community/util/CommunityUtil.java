package com.demoCommunity.Community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtil {

    //生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    //MD5加密
    //只能加密，不能解密，如果只加密一次，因为黑客可能会有常用密码的库，
    // 可以根据你的加密密码是多少来推测原文密码
    //一般都是原文+一个salt随机字符串，再来MD5加密
    public static String md5(String key){
        //使用Spring的apache commons-lang工具
        if(StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}
