package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.EntCallbackAddr;

/**
 * 企业异步充值回调地址服务
 * <p>
 * Created by sunyiwei on 2016/7/6.
 */
public interface EntCallbackAddrService {
    /**
     * 根据企业ID获取相应的回调信息
     *
     * @param entId 企业ID
     * @return
     */
    EntCallbackAddr get(Long entId);

    /**
     * 删除企业的回调信息
     *
     * @param entId 企业ID
     * @return
     */
    boolean delete(Long entId);

    /**
     * 更新企业回调信息
     *
     * @param entId           企业ID
     * @param newCallbackAddr 新的回调地址
     * @return
     */
    boolean update(Long entId, String newCallbackAddr);
    
    /**
    * @Title: insert
    * @Description: 插入记录
    */ 
    boolean insert(EntCallbackAddr entCallbackAddr);
}
