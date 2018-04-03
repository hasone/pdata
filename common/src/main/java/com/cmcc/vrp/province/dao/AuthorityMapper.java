package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.Authority;

import java.util.List;
import java.util.Map;

/**
 * AuthorityMapper.java
 */
public interface AuthorityMapper {
    /** 
     * @Title: deleteByPrimaryKey 
     */
    int deleteByPrimaryKey(Long authorityId);

    /** 
     * @Title: insert 
     */
    int insert(Authority record);

    /** 
     * @Title: insertSelective 
     */
    int insertSelective(Authority record);

    /** 
     * @Title: selectByPrimaryKey 
     */
    Authority selectByPrimaryKey(Long authorityId);

    /** 
     * @Title: updateByPrimaryKeySelective 
     */
    int updateByPrimaryKeySelective(Authority record);

    /** 
     * @Title: updateByPrimaryKey 
     */
    int updateByPrimaryKey(Authority record);

    /** 
     * @Title: selectByQuery 
     */
    List<Authority> selectByQuery(Map<String, Object> param);

    /** 
     * @Title: queryUserAuthsByUserName 
     */
    List<Authority> queryUserAuthsByUserName(String userName);

    /** 
     * @Title: queryAuthNameByRoleId 
     */
    List<String> queryAuthNameByRoleId(Long roleId);

    /** 
     * @Title: countAuthority 
     */
    int countAuthority(Map<String, Object> map);

    /** 
     * @Title: selectAuthorityPage 
     */
    List<Authority> selectAuthorityPage(Map<String, Object> map);
}