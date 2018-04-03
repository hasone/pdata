package com.cmcc.vrp.province.service;

/**
 * Created by qinqinyan on 2016/8/28.
 */
public interface JudgeIspService {

    /**
     * 判断手机号所属运营商
     *
     * @param mobile
     * @author qinqinyan
     */
    String judgeIsp(String mobile);

    /**
     * 判断手机号归属运营商，对于多条手机搜索运营商，不用每次判断时去数据库读取号段信息，提高效率
     *
     * @param mobile
     * @param cmMobile
     * @param cuMobile
     * @param ctMobile
     * @return
     * @Title: judgeIsp
     * @Author: wujiamin
     * @date 2016年9月19日下午2:46:19
     */
    String judgeIsp(String mobile, String[] cmMobile, String[] cuMobile,
                    String[] ctMobile);
}
