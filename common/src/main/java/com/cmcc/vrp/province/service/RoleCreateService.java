/**
 *
 */
package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.Role;

/**
 * <p>Title:RoleCreateService </p>
 * <p>Description: 创建角色Service</p>
 *
 * @author xujue
 * @date 2016年7月15日
 */
public interface RoleCreateService {

    /** 
     * @Title: selectRoleIdsCreateByRoleId 
     */
    List<Long> selectRoleIdsCreateByRoleId(Long roleId);

    /** 
     * @Title: updateRoleWithRoleIdsCreate 
     */
    boolean updateRoleWithRoleIdsCreate(Role role, List<Long> roleIdsList);

    List<Role> getCreateRolesByRoleId(Long roleId);

}
