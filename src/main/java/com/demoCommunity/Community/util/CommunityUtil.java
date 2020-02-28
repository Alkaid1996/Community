package com.demoCommunity.Community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
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

    //Json数据包含的1.编号2.提示信息3.业务数据
    public static String getJsonString(int code, String msg, Map<String,Object> map){
        JSONObject json=new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        if(map!=null){
            for(String key:map.keySet()){
                json.put(key,map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String getJsonString(int code,String msg){
        return getJsonString(code,msg,null);
    }

    public static String getJsonString(int code){
        return getJsonString(code,null,null);
    }

    public static void main(String[] args) {
        Map<String,Object> map=new HashMap<>();
        map.put("name","zhangsan");
        map.put("age",25);
        System.out.println(getJsonString(0,"ok",map));
    }

}
