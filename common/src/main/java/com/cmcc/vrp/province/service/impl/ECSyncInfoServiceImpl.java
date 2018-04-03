package com.cmcc.vrp.province.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.vrp.province.dao.ECSyncInfoMapper;
import com.cmcc.vrp.province.model.ECSyncInfo;
import com.cmcc.vrp.province.service.ECSyncInfoService;
import com.cmcc.vrp.util.StringUtils;

/**
 * @author lgk8023
 *
 */
@Service
public class ECSyncInfoServiceImpl implements ECSyncInfoService{
    
    @Autowired
    ECSyncInfoMapper ecSyncInfoMapper;

    @Override
    public boolean insert(ECSyncInfo ecSyncInfo) {
        if (ecSyncInfo == null) {
            return false;
        }
        return ecSyncInfoMapper.insert(ecSyncInfo) == 1;
    }

    @Override
    public boolean updateByPrimaryKeySelective(ECSyncInfo ecSyncInfo) {
        if (ecSyncInfo == null) {
            return false;
        }
        return ecSyncInfoMapper.updateByPrimaryKeySelective(ecSyncInfo) == 1;
    }

    @Override
    public ECSyncInfo selectByECCode(String ecCode) {
        if (StringUtils.isEmpty(ecCode)) {
            return null;
        }
        return ecSyncInfoMapper.selectByECCode(ecCode);
    }

    @Override
    @Transactional
    public boolean updateOrInsert(ECSyncInfo newEcSyncInfo) {
        if (newEcSyncInfo == null) {
            return false;
        }
        String ecCode = newEcSyncInfo.getEcCode();
        ECSyncInfo ecSyncInfo = selectByECCode(ecCode);
        if (ecSyncInfo != null) {
            newEcSyncInfo.setId(ecSyncInfo.getId());
            newEcSyncInfo.setCreateTime(new Date());
            return updateByPrimaryKeySelective(newEcSyncInfo);
        } else {
            if (newEcSyncInfo.getDeleteFlag() == null) {
                newEcSyncInfo.setDeleteFlag(0);
            }
            return insert(newEcSyncInfo);
        }
    }
}
