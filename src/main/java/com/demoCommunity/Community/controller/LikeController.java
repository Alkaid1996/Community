package com.demoCommunity.Community.controller;

import com.demoCommunity.Community.entity.Event;
import com.demoCommunity.Community.entity.User;
import com.demoCommunity.Community.event.EventProducer;
import com.demoCommunity.Community.service.LikeService;
import com.demoCommunity.Community.util.CommunityConstant;
import com.demoCommunity.Community.util.CommunityUtil;
import com.demoCommunity.Community.util.HostHolder;
import com.demoCommunity.Community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController implements CommunityConstant {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(path="/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType,int entityId,int entityUserId,int postId){
        User user=hostHolder.getUser();
        //实现点赞
        likeService.like(user.getId(),entityType,entityId,entityUserId);
        //数量
        long likeCount=likeService.findEntityLikeCount(entityType, entityId);
        //状态
        int likeStatus=likeService.findEntityLikeStatus(user.getId(),entityType,entityId);

        Map<String,Object> map= new HashMap<>();
        map.put("likeCount",likeCount);
        map.put("likeStatus",likeStatus);

        //触发点赞事件
        //取消赞就不用通知用户了，因为太恶心人了
        if(likeStatus==1){
            Event event=new Event()
                    .setTopic(TOPIC_LIKE)
                    .setUserId(hostHolder.getUser().getId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId",postId);
            eventProducer.fireEvent(event);

        }
        if(entityType==ENTITY_TYPE_POST){

            String redisKey= RedisKeyUtil.getPostScoreKey();
            //放在什么数据结构呢？
            //队列：ABACA的话对队列的统计会有重复
            redisTemplate.opsForSet().add(redisKey,postId);
        }

        return CommunityUtil.getJsonString(0,null,map);

    }
}
