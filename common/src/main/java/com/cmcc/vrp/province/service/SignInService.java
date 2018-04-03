package com.cmcc.vrp.province.service;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * 签到服务
 *
 * Created by sunyiwei on 2017/2/23.
 */
public interface SignInService {
    /**
     * 签到
     *
     * 实现要考虑的内容包括:
     *
     * 1. 一定时间间隔内不允许重复签到
     *
     * 2. 签到获得的积分大小可控制
     *
     * @param mobile    用户手机号
     * @param serialNum 操作流水号
     * @param randomPoint 随机积分
     */
    boolean sign(String mobile, String serialNum, int randomPoint);
    
    /**
     * 新签到
     * @param mobile 用户手机号
     * @param signTime 签到手机号
     * @author qinqinyan
     * 1、一个月内最多赠送21个流量币（及有效签到数是21）
     * 2、每天只能签到1次，1次赠送一个流量币，一个月内连续签到21天，第21天额外赠送10个流量币
     * 3、不能跨月份进行累加
     * */
    Map<String, String> newSign(String mobile, Date signTime);

    /**
     * 获取签到记录
     *
     * @param mobile 用户手机号
     * @return 获取指定用户所有的签到记录, 每个字符串代表一个日期, 格式为yyyy-MM-dd
     */
    Set<String> records(String mobile);
    
    /**
     * 获取今天获得的流量币
     * @author qinqinyan
     * */
    String getAchieveCoinCountToday(Long adminId);
    
    /**
     * 获取起床时间
     * @author qinqinyan
     * */
    String getSignTime(Long adminId);
    
    /**
     * 获取总签到人数
     * @author qinqinyan
     * */
    Long getTotalSignCount();
}
