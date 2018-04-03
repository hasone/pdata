package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.RoleAuthority;

import java.util.List;
import java.util.Map;

/**
 * @Title:RoleAuthorityMapper
 * @Description:
 * */
public interface RoleAuthorityMapper {
    /**
     * @Title:insert
     * @Description:
     * */
    int insert(RoleAuthority record);

    /**
     * @Title:insertSelective
     * @Description:
     * */
    int insertSelective(RoleAuthority record);

    /**
     * @Title:deleteByRoleId
     * @Description:
     * */
    int deleteByRoleId(Long roleId);

    /**
     * @Title:deleteRoleAuthority
     * @Description:
     * */
    int deleteRoleAuthority(RoleAuthority roleAuthorrity);

    /**
     * @Title:select
     * @Description:
     * */
    RoleAuthority select(Map<String, Object> param);

    /**
     * @Title:selectByRoleId
     * @Description:
     * */
    List<RoleAuthority> selectByRoleId(Long roleId);

    /**
     * @Title:selectAuthsByRoleIds
     * @Description:
     * */
    List<Long> selectAuthsByRoleIds(Long roleIds);

    /**
     * @Title:selectExistingRoleAuthorityByAuthorityName
     * @Description:
     * */
    List<RoleAuthority> selectExistingRoleAuthorityByAuthorityName(
        String authorityName);

}