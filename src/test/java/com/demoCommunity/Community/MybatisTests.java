package com.demoCommunity.Community;

import com.demoCommunity.Community.dao.DiscussPostMapper;
import com.demoCommunity.Community.dao.UserMapper;
import com.demoCommunity.Community.entity.DiscussPost;
import com.demoCommunity.Community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MybatisTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testSelectUser(){
        User user=userMapper.selectById(11);
        System.out.println(user);
    }
    @Test
    public void testSelectDiscussPost(){
        List<DiscussPost> list=discussPostMapper.selectDiscussPosts(149,0,10);
        for(DiscussPost discussPost:list){
            System.out.println(discussPost);
        }
    }
//    @Test
//    public void testSumDiscussPosts(){
//        System.out.println(discussPostMapper.sumDiscussPosts(0));
//    }
}
