package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.AdminChangeOperator;

/**
 * 已保存而未提交的用户修改类
 *
 */
public interface AdminChangeOperatorService {
    /**
     * 通过adminId得到保存的记录
     */
    List<AdminChangeOperator> getByAdminId(Long adminId);
    
    /**
     * 通过adminId删除保存的记录
     */
    boolean deleteByAdminId(Long adminId);
    
    /**
     * 插入记录
     */
    boolean insert(AdminChangeOperator adminChangeOperator);
  
    /**
     * 先删除旧的记录，再增加新的记录，都按adminId
     */
    boolean insertAndDelByAdminId(AdminChangeOperator adminChangeOperator);
}
