package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.UserInfo;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:33:10
*/
public interface UserInfoMapper {
    /**
     * 根据ID获取用户
     */
    UserInfo get(Long id);

    /**
     * 根据手机号获取用户
     */
    UserInfo getByMobile(String mobile);

    /**
     * 插入用户
     */
    int insert(UserInfo userInfo);

    /**
     * 删除用户，逻辑删除
     */
    int delete(Long id);

    /**
     * 删除用户，逻辑删除
     */
    int deleteByMobile(String mobile);
}