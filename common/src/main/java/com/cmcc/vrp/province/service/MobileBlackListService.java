package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.MobileBlackList;

/**
 * @author lgk8023
 *
 */
public interface MobileBlackListService {

    /**
     * @param mobileBlackList
     * @return
     */
    boolean insert(MobileBlackList mobileBlackList);
    
    /**
     * @param id
     * @return
     */
    boolean deleteById(Long id);
    
    List<MobileBlackList> getByMobile(String mobile);
    MobileBlackList getByMobileAndType(String mobile, String type);
}
