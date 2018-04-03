package com.cmcc.vrp.province.service;

/**
 * 1、在线获取供应商的产品
 * 2、即时建立平台产品、供应商产品、及两这的关联关系
 * 3、建立平台产品与企业的关联关系
 * Created by lilin on 2016/9/11.
 */
public interface ProductAutoService {

    /**
     * @param entId
     * @return
     */
    boolean autoCreateRelation(Long entId);

    /**
     * 指纹
     *
     * @return
     */
    String getFingerPrint();

}
