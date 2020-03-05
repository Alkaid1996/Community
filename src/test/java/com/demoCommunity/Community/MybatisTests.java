package com.demoCommunity.Community;

import com.demoCommunity.Community.dao.DiscussPostMapper;
import com.demoCommunity.Community.dao.LoginTicketMapper;
import com.demoCommunity.Community.dao.UserMapper;
import com.demoCommunity.Community.entity.DiscussPost;
import com.demoCommunity.Community.entity.LoginTicket;
import com.demoCommunity.Community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MybatisTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void testSelectUser() {
        User user = userMapper.selectById(149);
        System.out.println(user);
    }

    @Test
    public void testSelectDiscussPost() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149, 0, 10,0);
        for (DiscussPost discussPost : list) {
            System.out.println(discussPost);
        }
    }

    @Test
    public void testInsertLoginTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));

        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void testSelectLoginTicket() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("abc", 1);
        loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
    }
//    @Test
//    public void testSumDiscussPosts(){
//        System.out.println(discussPostMapper.sumDiscussPosts(0));
//    }
}
