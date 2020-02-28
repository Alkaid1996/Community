package com.demoCommunity.Community.controller;

import com.demoCommunity.Community.entity.Comment;
import com.demoCommunity.Community.service.CommentService;
import com.demoCommunity.Community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    //因为在评论的时候是在某个确定的帖子下评论，所以需要注入实时的帖子id

    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        //如果用户没登录，该处会报错，后面做全局处理
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);
        return "redirect:/discuss/detail/" + discussPostId;
    }
}
