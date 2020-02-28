package com.demoCommunity.Community.util;

public class RedisKeyUtil {

    private static final String SPLIT = ":";

    //点赞的key:value
    //key是like:entity:entityType:entityId

    //以实体为key

    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    //以用户为Key

    private static final String PREFIX_USER_LIKE = "like:user";

    //某个实体的赞
    //like:entity:entityType:entityId->set(userId)集合有统计总数的方法

    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    //某个用户的赞

    public static String getUserLikeKey(int userId){
        return PREFIX_ENTITY_LIKE + SPLIT + userId;
    }
}
