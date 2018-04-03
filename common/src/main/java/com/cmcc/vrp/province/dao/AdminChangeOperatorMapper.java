package com.cmcc.vrp.province.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.AdminChangeOperator;

/**
 * AdminChangeOperatorMapper
 *
 */
public interface AdminChangeOperatorMapper {
    /**
     * deleteByPrimaryKey(Long id);
     */
    int deleteByPrimaryKey(Long id);

    /**
     * insert(AdminChangeOperator record);
     */
    int insert(AdminChangeOperator record);

    /**
     * insertSelective(AdminChangeOperator record);
     */
    int insertSelective(AdminChangeOperator record);

    /**
     * selectByPrimaryKey(Long id);
     */
    AdminChangeOperator selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective(AdminChangeOperator record);
     */
    int updateByPrimaryKeySelective(AdminChangeOperator record);

    /**
     * updateByPrimaryKey(AdminChangeOperator record);
     */
    int updateByPrimaryKey(AdminChangeOperator record);
    
    
    /**
     * 读取记录
     * getAdminChangeRecordByEntId
     * @Description:
     * */
    List<AdminChangeOperator> getAdminChangeRecordByAdminId(@Param("adminId") Long adminId);
    
    /**
     * 删除记录，按照adminId
     * @param adminId
     * @return
     */
    int deleteAdminChangeRecordByAdminId(@Param("adminId") Long adminId);
}