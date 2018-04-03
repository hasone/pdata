package com.cmcc.vrp.province.dao;

import java.util.List;

import com.cmcc.vrp.province.model.AdminRole;

/**
 * AdminRoleMapper.java
 */
public interface AdminRoleMapper {

    /** 
     * @Title: insert 
     */
    int insert(AdminRole record);

    /** 
     * @Title: selectAdminRoleByAdminId 
     */
    List<AdminRole> selectAdminRoleByAdminId(Long adminId);

    /**
     * @param adminId
     * @return List<Long>
     * @Title: selectRoleIdsByAdminId
     * @Description: 由用户ID得到角色ID，角色和用户是多对多的关系
     */
    Long selectRoleIdByAdminId(Long adminId);

    /** 
     * @Title: selectAdminRoleByRoleId 
     */
    List<AdminRole> selectAdminRoleByRoleId(Long roleId);

    /**
     * 寻找符合条件的AdminRole个数
     * @param record
     * @return
     */
    int selectCountByQuery(AdminRole record);

    /**
     * @param adminId
     * @return
     * @Title:deleteByAdminId
     * @Description: 根据用户ID删除其所有的adminRole
     */
    int deleteByAdminId(Long adminId);

    /**
     * 根据角色ID删除其所有的adminRole
     *
     * @param roleId 角色ID
     * @return
     */
    int deleteByRoleId(Long roleId);

    /**
     * 删除指定的AdminRole
     *
     * @param record 用户角色对象
     * @return
     */
    int deleteAdminRole(AdminRole record);

    /**
     * @param adminId
     * @return
     * @throws
     * @Title:getRoleNameByAdminId
     * @Description: 根据用户ID得到期角色名称
     * @author: sunyiwei
     */
    String getRoleNameByAdminId(Long adminId);
}