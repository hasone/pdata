package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.WxAdminister;

/**
 * 
 * */
public interface WxAdministerMapper {
    
    /**
     * 
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * 
     * */
    int insert(WxAdminister record);

    /**
     * 
     * */
    int insertSelective(WxAdminister record);

    /**
     * 
     * */
    WxAdminister selectByPrimaryKey(Long id);
    
    /**
     * 根据手机号码查询用户 用户用手机登录 或者修改密码时需要用到
     *
     * @param phone
     * @return
     */
    WxAdminister selectByMobilePhone(String phone);

    /**
     * 
     * */
    int updateByPrimaryKeySelective(WxAdminister record);

    /**
     * 
     * */
    int updateByPrimaryKey(WxAdminister record);
}