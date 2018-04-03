package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.RoleAuthority;

import java.util.List;

/**
 * RoleAuthorityService.java
 */
public interface RoleAuthorityService {
    /** 
     * @Title: insert 
     */
    boolean insert(RoleAuthority roleAuthority);

    /** 
     * @Title: insert 
     */
    boolean insert(Long roleId, Long authorityId);

    /** 
     * @Title: select 
     */
    RoleAuthority select(Long roleId, Long authorityId);

    /** 
     * @Title: selectByRoleId 
     */
    List<RoleAuthority> selectByRoleId(Long roleId);

    /** 
     * @Title: selectAuthsByRoleId 
     */
    List<Long> selectAuthsByRoleId(Long roleId);

    /** 
     * @Title: deleteByRoleId 
     */
    boolean deleteByRoleId(Long roleId);

    /** 
     * @Title: deleteRoleAuthority 
     */
    boolean deleteRoleAuthority(Long roleId, Long authorityId);

    /** 
     * @Title: selectExistingRoleAuthorityByAuthorityName 
     */
    List<RoleAuthority> selectExistingRoleAuthorityByAuthorityName(
        String authorityName);

}
