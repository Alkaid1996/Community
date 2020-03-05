package com.demoCommunity.Community.dao;

import com.demoCommunity.Community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);

    //根据id查询一个comment

    Comment selectCommentById(int id);
}
