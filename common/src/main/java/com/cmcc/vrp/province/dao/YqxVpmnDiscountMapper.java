package com.cmcc.vrp.province.dao;

import java.util.List;

import com.cmcc.vrp.province.model.YqxVpmnDiscount;

/**
 * YqxVpmnDiscountMapper.java
 * @author wujiamin
 * @date 2017年5月9日
 */
public interface YqxVpmnDiscountMapper {
    /** 
     * @Title: insert 
     */
    int insert(YqxVpmnDiscount record);

    /** 
     * @Title: insertSelective 
     */
    int insertSelective(YqxVpmnDiscount record);
    
    /** 
     * 获取所有数据库记录
     * @Title: getAllDiscount 
     */
    List<YqxVpmnDiscount> getAllDiscount();
}