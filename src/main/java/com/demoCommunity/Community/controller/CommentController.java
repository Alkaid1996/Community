package com.demoCommunity.Community.controller;

import com.demoCommunity.Community.entity.Comment;
import com.demoCommunity.Community.entity.DiscussPost;
import com.demoCommunity.Community.entity.Event;
import com.demoCommunity.Community.event.EventProducer;
import com.demoCommunity.Community.service.CommentService;
import com.demoCommunity.Community.service.DiscussPostService;
import com.demoCommunity.Community.util.CommunityConstant;
import com.demoCommunity.Community.util.HostHolder;
import com.demoCommunity.Community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private RedisTemplate redisTemplate;

    //因为在评论的时候是在某个确定的帖子下评论，所以需要注入实时的帖子id

    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        //如果用户没登录，该处会报错，后面做全局处理

        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);
        //添加完评论以后，会触发评论事件
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId", discussPostId);
        //差一个entityUserId,所以需要去查
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        //吞吐量很大
        eventProducer.fireEvent(event);

        //评论也会触发发帖事件
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            event = new Event()
                    .setTopic(TOPIC_COMMENT)
                    .setUserId(comment.getUserId())
                    .setEntityType(ENTITY_TYPE_POST)
                    .setEntityId(discussPostId);
            eventProducer.fireEvent(event);

            String redisKey= RedisKeyUtil.getPostScoreKey();
            //放在什么数据结构呢？
            //队列：ABACA的话对队列的统计会有重复
            redisTemplate.opsForSet().add(redisKey,discussPostId);
        }
        return "redirect:/discuss/detail/" + discussPostId;
    }
}
