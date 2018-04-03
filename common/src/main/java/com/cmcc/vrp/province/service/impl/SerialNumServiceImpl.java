package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.SerialNumMapper;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.SerialNumService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sunyiwei on 2016/8/9.
 */
@Service
public class SerialNumServiceImpl implements SerialNumService {
    @Autowired
    private SerialNumMapper serialNumMapper;

    @Override
    public boolean insert(SerialNum serialNum) {
        return validate(serialNum) && serialNumMapper.insert(serialNum) == 1;
    }

    @Override
    public boolean batchInsert(List<SerialNum> serialNums) {
        if (serialNums == null || serialNums.isEmpty()) {
            return false;
        }

        for (SerialNum serialNum : serialNums) {
            if (!validate(serialNum)) {
                return false;
            }
        }

        return serialNumMapper.batchInsert(serialNums) == serialNums.size();
    }

    @Override
    public SerialNum getByPltSerialNum(String pltSerialNum) {
        return StringUtils.isBlank(pltSerialNum) ? null : serialNumMapper.getByPltSerialNum(pltSerialNum);
    }

    @Override
    public SerialNum getByBossReqSerialNum(String bossReqSerialNum) {
        return StringUtils.isBlank(bossReqSerialNum) ? null : serialNumMapper.getByBossReqSerialNum(bossReqSerialNum);
    }

    @Override
    public SerialNum getByBossRespSerialNum(String bossRespSerialNum) {
        return StringUtils.isBlank(bossRespSerialNum) ? null : serialNumMapper.getByBossRespSerialNum(bossRespSerialNum);
    }

    @Override
    public boolean updateSerial(SerialNum serialNum) {
        return serialNum != null && serialNumMapper.update(serialNum) == 1;
    }

    private boolean validate(SerialNum serialNum) {
        return serialNum != null && StringUtils.isNotBlank(serialNum.getPlatformSerialNum());
    }

    @Override
    public boolean batchUpdate(List<SerialNum> serialNums) {
        return serialNums != null && serialNumMapper.batchUpdate(serialNums) == serialNums.size();
    }

    @Override
    public boolean deleteByPlatformSerialNum(String platformSerialNum) {        
        return serialNumMapper.deleteByPlatformSerialNum(platformSerialNum) == 1;
    }
}
