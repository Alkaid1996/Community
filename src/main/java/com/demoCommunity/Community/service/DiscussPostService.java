package com.demoCommunity.Community.service;

import com.demoCommunity.Community.dao.DiscussPostMapper;
import com.demoCommunity.Community.entity.DiscussPost;
import com.demoCommunity.Community.util.SensitiveFilter;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DiscussPostService {

    private static final Logger logger = LoggerFactory.getLogger(DiscussPostService.class);

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Value("${caffeine.posts.max-size}")
    private int maxSize;

    @Value("${caffeine.posts.expire-seconds}")
    private int expireSeconds;

    //咖啡因核心接口：Cache，LoadingCache同步缓存,AsyncLoadingCache异步缓存，并发的

    //帖子列表的缓存

    private LoadingCache<String, List<DiscussPost>> postListCache;

    //缓存都是按Key缓存value
    //帖子总数的缓存
    private LoadingCache<Integer, Integer> postRowsCache;

    //给当前类加一个初始化方法
    @PostConstruct
    public void init() {

        //初始化帖子列表缓存
        postListCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, List<DiscussPost>>() {
                    //当缓存没有的时候查询数据库初始化到缓存的方法
                    @Nullable
                    @Override
                    public List<DiscussPost> load(@NonNull String key) throws Exception {
                        if (key == null || key.length() == 0) {
                            throw new IllegalArgumentException("参数错误！");
                        }
                        String[] params = key.split(":");
                        if (params == null || params.length != 2) {
                            throw new IllegalArgumentException("参数错误！");
                        }
                        int offset = Integer.valueOf(params[0]);
                        int limit = Integer.valueOf(params[1]);

                        //先从二级缓存查，Redis

                        //再在DB查
                        logger.info("load post list from DB.");

                        return discussPostMapper.selectDiscussPosts(0, offset, limit, 1);
                    }
                });

        //初始化帖子总数缓存
        postRowsCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, Integer>() {
                    @Nullable
                    @Override
                    public Integer load(@NonNull Integer key) throws Exception {
                        logger.info("load post rows from DB.");
                        return discussPostMapper.sumDiscussPosts(key);
                    }
                });


    }


    public List<DiscussPost> findDiscussPost(int userId, int offset, int limit, int orderMode) {

        //只传热帖，也就是orderMode等于1的帖子，并且只缓存一页，所以需要offset和Limit，并不需要userId
        if (userId == 0 && orderMode == 1) {
            return postListCache.get(offset + ":" + limit);
        }
        logger.info("load post list from DB.");

        return discussPostMapper.selectDiscussPosts(userId, offset, limit, orderMode);
    }

    public int findDiscussPostSum(int userId) {
        if (userId == 0) {
            return postRowsCache.get(userId);
        }
        logger.info("load post rows from DB.");
        return discussPostMapper.sumDiscussPosts(userId);
        //我们想看到用户的真名，希望可以通过UserId查到User
        //UserId-->User
    }

    public int addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        //要对标题和内容进行敏感词过滤，另外还要过滤一些标签
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        //过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostMapper.insertDiscussPost(post);
    }

    public DiscussPost findDiscussPostById(int id) {
        return discussPostMapper.selectDiscussPostById(id);
    }

    public int updateCommentCount(int id, int commentCount) {
        return discussPostMapper.updateCommentCount(id, commentCount);
    }

    public int updateType(int id, int type) {
        return discussPostMapper.updateType(id, type);
    }

    public int updateStatus(int id, int status) {
        return discussPostMapper.updateStatus(id, status);
    }

    public int updateScore(int id, double score) {
        return discussPostMapper.updateScore(id, score);
    }
}
