package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.Authority;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* <p>Title: </p>
* <p>Description: </p>
* @date 2017年4月19日 下午5:25:20
*/
public interface AuthorityService {

    /**
     * @return
     */
    List<Authority> selectAllAuthority();

    /**
     * @param userName
     * @return
     */
    Set<String> queryAuthCodesByUserName(String userName);

    /**
     * @param userName
     * @return
     */
    Set<String> queryAuthUrlsByUserName(String userName);

    /**
     * @param managerId
     * @param authorityName
     * @return
     */
    boolean ifHaveAuthority(Long managerId, String authorityName);

    /**
     * @param authority
     * @return
     */
    boolean insert(Authority authority);

    /**
     * @param id
     * @return
     */
    Authority get(Long id);

    /**
     * @param record
     * @return
     */
    boolean updateByPrimaryKeySelective(Authority record);

    /**
     * @param map
     * @return
     */
    int countAuthority(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    List<Authority> selectAuthorityPage(Map<String, Object> map);

    /**
     * @param authority
     * @return
     */
    boolean uniqueAuthority(Authority authority);

    /**
     * @param authorityId
     * @return
     */
    boolean deleteAuthorityById(Long authorityId);
}
