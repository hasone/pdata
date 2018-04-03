/**
 * @Title: WhiteListService.java
 * @Package com.cmcc.vrp.province.service
 * @author: sunyiwei
 * @date: 2015年6月10日 上午11:08:47
 * @version V1.0
 */
package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.WhiteList;
import com.cmcc.vrp.util.QueryObject;

import java.util.List;

/**
 * @ClassName: WhiteListService
 * @Description: 白名单服务
 * @author: sunyiwei
 * @date: 2015年6月10日 上午11:08:47
 *
 */
public interface WhiteListService {
    /*
     * 获取白名单对象
     */
    /** 
     * @Title: get 
    */
    WhiteList get(String mobile);

    /*
     * 删除对象， 逻辑删除
     */
    /** 
     * @Title: delete 
    */
    boolean delete(String mobile);

    /*
     * 根据ID删除对象，逻辑删除
     */
    /** 
     * @Title: delete 
    */
    boolean delete(Long id);

    /*
     * 插入对象
     */
    /** 
     * @Title: insert 
    */
    boolean insert(String mobile);

    /*
     * 查询符合条件的记录数
     */
    /** 
     * @Title: count 
    */
    Long count(QueryObject queryObject);

    /*
     * 查询符合条件的记录列表
     */
    /** 
     * @Title: query 
    */
    List<WhiteList> query(QueryObject queryObject);

    /*
     * 批量插入
     */
    /** 
     * @Title: insertBatch 
    */
    boolean insertBatch(List<String> list);
}
