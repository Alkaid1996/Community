package com.demoCommunity.Community.controller;

import com.demoCommunity.Community.entity.Comment;
import com.demoCommunity.Community.entity.DiscussPost;
import com.demoCommunity.Community.entity.Page;
import com.demoCommunity.Community.entity.User;
import com.demoCommunity.Community.service.CommentService;
import com.demoCommunity.Community.service.DiscussPostService;
import com.demoCommunity.Community.service.LikeService;
import com.demoCommunity.Community.service.UserService;
import com.demoCommunity.Community.util.CommunityConstant;
import com.demoCommunity.Community.util.CommunityUtil;
import com.demoCommunity.Community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private LikeService likeService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJsonString(403, "您还没有登录哦！");
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        //报错的情况最后全局处理
        return CommunityUtil.getJsonString(0, "发布成功！");
    }

    //返回模板，所以不加ResponseBody注解
    //因为路径中含有参数,要从discussPostId的路径中收到值，需要参数中增加@PathVariable注解

    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model,
                                 Page page) {
        //查询帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        //此时查到的用户信息是userId,我们可以采用1.mybatis的
        //关联查询查询到对应的账户信息
        //2.先查出来userId,再查到user
        //第一种方法虽然更快，但是很容易让干净的查询方法有一点
        //为了解决这种问题，我们最好采用第二种方法，为了解决第二种的慢的问题
        //我们可以采用Redis将数据缓存在内存里
        //作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);
        //点赞数量
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeCount", likeCount);
        //点赞状态
        int likeStatus = hostHolder.getUser() == null ? 0 :
                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeStatus", likeStatus);
        //评论的分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        //从帖子表里查出来，因为这个表里已经放进了评论数据
        page.setRows(post.getCommentCount());

        //评论：给帖子的评论叫评论
        //回复：给评论的评论叫回复
        //评论列表
        List<Comment> commentList = commentService.findCommentsByEntity(ENTITY_TYPE_POST,
                post.getId(), page.getOffset(), page.getLimit());

        //评论VO列表
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                //评论VO
                Map<String, Object> commentVo = new HashMap<>();
                //评论
                commentVo.put("comment", comment);
                //作者
                commentVo.put("user", userService.findUserById(comment.getUserId()));

                //点赞数量
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeCount", likeCount);
                //点赞状态
                likeStatus = hostHolder.getUser() == null ? 0 :
                        likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeStatus",likeStatus);
                //回复列表

                List<Comment> replyList = commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT,
                        comment.getId(), 0, Integer.MAX_VALUE);
                //回复的VO列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        //回复
                        replyVo.put("reply", reply);
                        //作者
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        //点赞数量
                        likeCount=likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,reply.getId());
                        replyVo.put("likeCount",likeCount);
                        //点赞状态
                        likeStatus=hostHolder.getUser()==null?0:
                                likeService.findEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_COMMENT,reply.getId());
                        replyVo.put("likeStatus",likeStatus);
                        //回复目标
                        User target = reply.getTargetId() == 9 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                //回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);
                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments", commentVoList);
        return "/site/discuss-detail";
    }
}
