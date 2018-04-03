package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.AdminRole;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午4:23:24
*/
public interface AdminRoleService {

    /**
     * @param adminId
     * @param roleId
     * @return
     */
    boolean insertAdminRole(Long adminId, Long roleId);

    /**
     * @param adminRole
     * @return
     */
    boolean insertAdminRole(AdminRole adminRole);

    /**
     * @param adminRole
     * @return
     */
    int selectCountByQuery(AdminRole adminRole);

    Long getRoleIdByAdminId(Long adminId);

    /**
     * @param roleId
     * @return
     */
    boolean deleteByRoleId(Long roleId);

    /**
     * @param adminId
     * @return
     */
    boolean deleteByAdminId(Long adminId);

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
