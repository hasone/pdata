package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.Role;
import com.cmcc.vrp.util.QueryObject;

import java.util.List;

/**
 * RoleService.java
 */
public interface RoleService {
    Role getRoleById(Long id);

    Role getRoleByCode(String code);

    List<Role> getAvailableRoles();

    /** 
     * @Title: insertNewRoleWithAuthIds 
     */
    boolean insertNewRoleWithAuthIds(Role role, List<Long> authList);

    /** 
     * @Title: updateRoleWithAuthIds 
     */
    boolean updateRoleWithAuthIds(Role role, List<Long> entryAuthIds);

    /** 
     * @Title: deleteRoleById 
     */
    boolean deleteRoleById(Long id);

    /** 
     * @Title: queryPaginationRoleCount 
     */
    int queryPaginationRoleCount(QueryObject queryObject);

    /** 
     * @Title: queryPaginationRoleList 
     */
    List<Role> queryPaginationRoleList(QueryObject queryObject);

    List<Role> getCreateRoleByRoleId(Long roleId);

    List<Role> getAllRoles();

}
