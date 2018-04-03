package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.GiveMoneyEnterMapper;
import com.cmcc.vrp.province.model.GiveMoneyEnter;
import com.cmcc.vrp.province.service.GiveMoneyEnterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 
 * @ClassName: GiveMoneyEnterServiceImpl
 * @Description: TODO
 */
@Service
public class GiveMoneyEnterServiceImpl implements GiveMoneyEnterService {
    @Autowired
    GiveMoneyEnterMapper giveMoneyEnterMapper;

    @Override
    public boolean insert(Long giveMoneyId, Long enterId) {
        if (giveMoneyId == null || enterId == null) {
            return false;
        }
        GiveMoneyEnter record = new GiveMoneyEnter();
        record.setEnterId(enterId);
        record.setGiveMoneyId(giveMoneyId);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        if (giveMoneyEnterMapper.insert(record) > 0) {
            return true;
        }
        throw new RuntimeException();
    }

    @Override
    public boolean updateByEnterId(Long giveMoneyId, Long enterId) {
        if (giveMoneyId == null || enterId == null) {
            return false;
        }
        GiveMoneyEnter record = new GiveMoneyEnter();
        record.setEnterId(enterId);
        record.setGiveMoneyId(giveMoneyId);
        record.setUpdateTime(new Date());

        if (selectByEnterId(enterId) == null) {
            if (insert(giveMoneyId, enterId)) {
                return true;
            }
        } else {
            if (giveMoneyEnterMapper.updateByEnterId(record) > 0) {
                return true;
            }
        }
        throw new RuntimeException();
    }

    @Override
    public GiveMoneyEnter selectByEnterId(Long enterId) {
        return giveMoneyEnterMapper.selectByEnterId(enterId);
    }

    @Override
    public boolean deleteGiveMoneyEnterByEnterId(Long enterId) {
        if (giveMoneyEnterMapper.selectByEnterId(enterId) != null) {
            return giveMoneyEnterMapper.deleteGiveMoneyEnterByEnterId(enterId) > 0;
        }

        return true;
    }

}
