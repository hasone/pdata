package com.cmcc.vrp.province.service;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.ShOrderList;
import com.cmcc.vrp.util.QueryObject;

/**
 * @author lgk8023
 *
 */
public interface ShOrderListService {

    /**
     * 插入數據
     * @param shOrderList
     * @return
     */
    public boolean insert(ShOrderList shOrderList);
    
    
    /**
     * 获取企业订购组信息
     * @param enterId
     * @return
     */
    public List<ShOrderList> getByEnterId(Long enterId);
    
    
    /**
     * 根据计费号获取订购组信息
     * @param mainBillId
     * @return
     */
    public ShOrderList getByMainBillId(String mainBillId);
    
    /**
     * 根据主键获取订购组信息
     * @param mainBillId
     * @return
     */
    public ShOrderList getById(Long id);
    
    /**
     * 更新预警值，暂停值
     * @param shOrderList
     * @return
     */
    public boolean updateAlertSelective(ShOrderList shOrderList);
    
    
    /**
     * @param queryObject
     * @return
     */
    public int showForPageResultCount(QueryObject queryObject);
    
    /**
     * @param queryObject
     * @return
     */
    public List<ShOrderList> showForPageResultList(QueryObject queryObject);
    
    /**
     * 更新订购组余额
     * @param shOrderList
     * @return
     */
    public boolean updateCount(ShOrderList shOrderList);
    
    public List<ShOrderList> getOrderListByMap(Map map);
}
