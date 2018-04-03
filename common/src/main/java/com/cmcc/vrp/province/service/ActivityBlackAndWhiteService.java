package com.cmcc.vrp.province.service;

import java.util.List;
import java.util.Map;

/**
 * Created by qinqinyan on 2016/8/25.
 */
public interface ActivityBlackAndWhiteService {

    /**
     * @param map
     * @return
     */
    List<String> selectPhonesByMap(Map map);

    /**
     * batchInsert
     *
     * @param activityId
     * @param isWhite    1白名单，2黑名单
     * @param phones     手机号
     * @author qinqinyan
     */
    boolean batchInsert(String activityId, Integer isWhite, String phones);

    /**
     * 根据activityId删除黑白名单
     *
     * @param activityId
     * @author qinqinyan
     */
    boolean deleteByActivityId(String activityId);

    /**
     * 更新黑白名单属性
     *
     * @param activityId
     * @param isWhite
     * @author qinqinyan
     */
    boolean updateIsWhiteByActivityId(String activityId, Integer isWhite);
    
    /**
     * 判断是否在活动名单列表内
     * @param mobile
     * @author activityId
     * @author qinqinyan
     * */
    boolean weatherInPhoneList(String mobile, String activityId);
}
