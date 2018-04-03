package com.cmcc.vrp.province.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.MobileBlackListMapper;
import com.cmcc.vrp.province.model.MobileBlackList;
import com.cmcc.vrp.province.service.MobileBlackListService;

/**
 * @author lgk8023
 *
 */
@Service
public class MobileBlackListServiceImpl implements MobileBlackListService {
    @Autowired
    MobileBlackListMapper mobileBlackListMapper;

    @Override
    public boolean insert(MobileBlackList mobileBlackList) {
        if (mobileBlackList == null) {
            return false;
        }
        return mobileBlackListMapper.insert(mobileBlackList) == 1;
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }
        return mobileBlackListMapper.deleteById(id) == 1;
    }

    @Override
    public List<MobileBlackList> getByMobile(String mobile) {
        return mobileBlackListMapper.getByMobile(mobile);
    }

    @Override
    public MobileBlackList getByMobileAndType(String mobile, String type) {
        return mobileBlackListMapper.getByMobileAndType(mobile, type);
    }

}
