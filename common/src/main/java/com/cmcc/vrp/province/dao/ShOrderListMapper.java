package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.ShOrderList;

/**
 * @author lgk8023
 *
 */
public interface ShOrderListMapper {

    /**
     * @param shOrderList
     * @return
     */
    public int insert(ShOrderList shOrderList);
    
    public List<ShOrderList> getByEnterId(@Param("enterId") Long enterId);
    
    public ShOrderList getByMainBillId(@Param("mainBillId") String mainBillId);

    public ShOrderList getById(@Param("id") Long id);

    /**
     * @param shOrderList
     * @return
     */
    public int updateAlertSelective(ShOrderList shOrderList);

    /**
     * @param map
     * @return
     */
    public int showForPageResultCount(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    public List<ShOrderList> showForPageResultList(Map<String, Object> map);

    /**
     * 更新订购组余额
     * @param shOrderList
     * @return
     */
    public int updateCount(ShOrderList shOrderList);

    public List<ShOrderList> getOrderListByMap(Map map);
  
}
