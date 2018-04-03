package com.cmcc.vrp.province.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.SdDailystatisticMapper;
import com.cmcc.vrp.province.model.SdDailystatistic;
import com.cmcc.vrp.province.service.SdDailystatisticService;

/**
 * 
 * SdDailystatisticService 山东每日对账统计接口
 *
 */
@Service("sdDailystatisticService")
public class SdDailystatisticServiceImpl implements SdDailystatisticService{

    @Autowired
    private SdDailystatisticMapper sdDailystatisticMapper;
    
    
    @Override
    public boolean insert(SdDailystatistic dailystatistic) {
        if(dailystatistic == null){
            return false;
        }
        return sdDailystatisticMapper.insertSelective(dailystatistic)>0;
    }

    @Override
    public boolean update(SdDailystatistic dailystatistic) {
        if(dailystatistic == null || dailystatistic.getId() ==null){
            return false;
        }
        return sdDailystatisticMapper.updateByPrimaryKeySelective(dailystatistic)>0;
    }

    @Override
    public List<SdDailystatistic> selectByDate(String date) {
        return sdDailystatisticMapper.selectByDate(date);
    }

    @Override
    public SdDailystatistic seleceByKey(Long id) {    
        return sdDailystatisticMapper.selectByPrimaryKey(id);
    }

}
