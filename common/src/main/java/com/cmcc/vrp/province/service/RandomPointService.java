package com.cmcc.vrp.province.service;

/**
 * 随机积分服务
 *
 * Created by sunyiwei on 2017/2/23.
 */
public interface RandomPointService {
    /**
     * 产生下一个随机积分
     *
     * @param mobile 积分将赋予的对象
     */
    int next(String mobile);
}
