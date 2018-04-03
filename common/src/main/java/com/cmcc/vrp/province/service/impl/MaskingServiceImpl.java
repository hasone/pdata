package com.cmcc.vrp.province.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.MaskingMapper;
import com.cmcc.vrp.province.model.Masking;
import com.cmcc.vrp.province.service.MaskingService;

/**
 * @author lgk8023
 *
 */
@Service
public class MaskingServiceImpl implements MaskingService{
    @Autowired
    MaskingMapper maskingMapper;

    @Override
    public boolean insert(Masking masking) {
        if (masking == null) {
            return false;
        }
        return maskingMapper.insert(masking) == 1;
    }

    @Override
    public Masking getByAdminId(Long adminId) {
        if (adminId == null) {
            return null;
        }
        return maskingMapper.getByAdminId(adminId);
    }

    @Override
    public boolean updateByPrimaryKeySelective(Masking masking) {
        if (masking == null) {
            return false;
        }
        return maskingMapper.updateByPrimaryKeySelective(masking) ==1;
    }

    @Override
    public Boolean insertOrUpdate(Masking masking) {

        if (masking == null) {
            return false;
        }
        Masking oldMasking = getByAdminId(masking.getAdminId());
        if (oldMasking != null) {
            masking.setUpdateTime(new Date());
            masking.setId(oldMasking.getId());
            return updateByPrimaryKeySelective(masking);
        }
        return insert(masking);
    }

    
}
