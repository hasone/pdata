package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.Role;

import java.util.List;
import java.util.Map;

/**
 * RoleMapper.java
 */
public interface RoleMapper {
    /** 
     * @Title: deleteByPrimaryKey 
     */
    int deleteByPrimaryKey(Long roleId);

    /** 
     * @Title: insert 
     */
    int insert(Role record);

    /** 
     * @Title: insertSelective 
     */
    int insertSelective(Role record);

    /** 
     * @Title: selectByPrimaryKey 
     */
    Role selectByPrimaryKey(Long roleId);

    /** 
     * @Title: selectByCode 
     */
    Role selectByCode(String code);

    /** 
     * @Title: updateByPrimaryKeySelective 
     */
    int updateByPrimaryKeySelective(Role record);

    /** 
     * @Title: updateByPrimaryKey 
     */
    int updateByPrimaryKey(Role record);

    /**
     * @描述：角色分页查询（总记录数量）,按角色名称模糊查询
     */
    int queryPaginationRoleCount(Map<String, Object> map);

    /**
     * @描述：角色分页查询（查询分页结果 ），按角色名称模糊查询
     */
    List<Role> queryPaginationRoleList(Map<String, Object> map);

    /**
     * 获取所有可用的角色
     */
    List<Role> getAvailableRoles();

    List<Role> getCreateRoleByRoleId(Long roleId);

    /**
     * 获取所有角色
     */
    List<Role> getAllRoles();
}