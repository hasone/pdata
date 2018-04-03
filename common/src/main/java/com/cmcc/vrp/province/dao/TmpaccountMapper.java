package com.cmcc.vrp.province.dao;

import java.util.List;

import com.cmcc.vrp.wx.model.Tmpaccount;

/**
 * TmpaccountMapper.java
 * @author wujiamin
 * @date 2017年3月30日
 */
public interface TmpaccountMapper {
    /** 
     * @Title: insert 
     */
    int insert(Tmpaccount record);

    /** 
     * @Title: insertSelective 
     */
    int insertSelective(Tmpaccount record);
    
    /** 
     * @Title: selectByOpenid 
     */
    List<Tmpaccount> selectByOpenid(String openid);
}