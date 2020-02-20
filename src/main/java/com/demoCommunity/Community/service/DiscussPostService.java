package com.demoCommunity.Community.service;

import com.demoCommunity.Community.dao.DiscussPostMapper;
import com.demoCommunity.Community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPost(int userId,int offset,int limit){
        return discussPostMapper.selectDiscussPosts(userId,offset,limit);
    }

    public int findDiscussPostSum(int userId){
        return discussPostMapper.sumDiscussPosts(userId);
        //我们想看到用户的真名，希望可以通过UserId查到User
        //UserId-->User
    }
}
