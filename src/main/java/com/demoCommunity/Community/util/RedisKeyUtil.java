package com.demoCommunity.Community.util;

public class RedisKeyUtil {

    //需要为key的所有属性需要在构造key的时候传入，至于具体的value则是在统计真实数据的时候用户点击注入

    //定义前缀

    //用来声明真实的key

    private static final String SPLIT = ":";

    //点赞的key:value
    //key是like:entity:entityType:entityId

    //以实体为key

    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    //以用户为Key

    private static final String PREFIX_USER_LIKE = "like:user";

    //关注的目标，
    private static final String PREFIX_FOLLOWEE = "followee";
    //具体关注的人
    //follower和followee之间互有联系，这样非常易于统计，比如，从关注的人这方面来统计他关注的目标
    //从关注的目标那边也能很容易统计关注他的人

    private static final String PREFIX_FOLLOWER = "follower";

    //存验证码的时候的前缀
    private static final String PREFIX_KAPTCHA = "kaptcha";

    //存储登录凭证的前缀
    private static final String PREFIX_TICKET = "ticket";

    private static final String PREFIX_USER = "user";

    //统计UV
    private static final String PREFIX_UV = "uv";

    //统计DAU
    private static final String PREFIX_DAU = "dau";

    //存储改动的帖子
    private static final String PREFIX_POST = "post";

    //某个实体的赞
    //like:entity:entityType:entityId->set(userId)集合有统计总数的方法
    //拼key的方法

    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    //某个用户的赞
    //like:user:userId -> int

    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    //某个用户关注的实体（人，帖子等）
    //followee:user:entityType->zset(entityId,now) //需要进行排序，所以使用zset

    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    //某个实体拥有的所有粉丝
    //follower:entityType:entityId -> zset(userId,now)

    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    //拼验证码的key
    //登录验证码  :每个用户的验证码应该是不一样的，此时并没有userId

    public static String getKaptchaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    //拼登录凭证的key
    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    //用户
    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }

    //单日UV
    public static String getUVKey(String date) {
        return PREFIX_UV + SPLIT + date;
    }

    //区间UV
    public static String getUVKey(String startDate, String endDate) {
        return PREFIX_UV + SPLIT + startDate + SPLIT + endDate;
    }

    //单日DAU
    public static String getDAUKey(String date) {
        return PREFIX_DAU + SPLIT + date;
    }

    //区间DAU
    public static String getDAUKey(String startDate, String endDate) {
        return PREFIX_DAU + SPLIT + startDate + SPLIT + endDate;
    }

    //返回统计帖子分数的
    public static String getPostScoreKey() {
        return PREFIX_POST + SPLIT + "score";
    }
}