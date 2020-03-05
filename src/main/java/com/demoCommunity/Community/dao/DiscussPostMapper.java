package com.demoCommunity.Community.dao;

import com.demoCommunity.Community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    //分页查询，所以每一页肯定有很多数据，所以List
    //因为有我的的功能，userId可以看到我的首页
    //为了支持分页，可以将首行+limit当参数注入，这样就可以在拼sql语句的时候将该参数拼入

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit,/*排序模式*/ int orderMode);
    //为了更方便的显示页码，我们可以将总数/limit数，就可以算出当前页码

    //@Param这个注解用于给参数取别名
    //如果只有一个参数，并且在<if>里使用，就必须加别名

   int sumDiscussPosts(@Param("userId") int userId);

   //发布帖子

   int insertDiscussPost(DiscussPost discussPost);

   //帖子详情

    DiscussPost selectDiscussPostById(int id);

    //评论数量

    int updateCommentCount(int id,int commentCount);


    int updateType(int id,int type);

    int updateStatus(int id,int status);

    int updateScore(int id,double score);
}
