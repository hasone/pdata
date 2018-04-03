package com.cmcc.vrp.province.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.MobileBlackList;

/**
 * @author lgk8023
 *
 */
public interface MobileBlackListMapper {

    /**
     * @param mobileBlackList
     * @return
     */
    int insert(MobileBlackList mobileBlackList);
    
    /**
     * @param id
     * @return
     */
    int deleteById(@Param("id") Long id);
    
    List<MobileBlackList> getByMobile(@Param("mobile") String mobile);

    MobileBlackList getByMobileAndType(@Param("mobile") String mobile, @Param("type") String type);
}
