package com.demoCommunity.Community.dao;

import com.demoCommunity.Community.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    //返回的是用户的具体信息
    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    //返回插入数据的行数
    int insertUser(User user);

    int updateStatus(int id, int status);

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);
}
