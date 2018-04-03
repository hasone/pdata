package com.cmcc.vrp.province.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.WxSerialSignRecordMapper;
import com.cmcc.vrp.province.model.WxSerialSignRecord;
import com.cmcc.vrp.province.service.WxSerialSignRecordService;
import com.cmcc.vrp.util.DateUtil;

@Service("wxSerialSignRecordService")
public class WxSerialSignRecordServiceImpl implements WxSerialSignRecordService{
    @Autowired
    WxSerialSignRecordMapper mapper;
    
    @Override
    public boolean insertSelective(WxSerialSignRecord record) {
        // TODO Auto-generated method stub
        return mapper.insertSelective(record)>=0;
    }

    @Override
    public boolean updateByPrimaryKeySelective(WxSerialSignRecord record) {
        // TODO Auto-generated method stub
        return mapper.updateByPrimaryKeySelective(record)>=0;
    }

    @Override
    public List<WxSerialSignRecord> selectByMap(Map map) {
        // TODO Auto-generated method stub
        return mapper.selectByMap(map);
    }

    @Override
    public int getTotalCountByAdminIdAndMonth(Long adminId, Date date) {
        // TODO Auto-generated method stub
        int totalCount = 0;
        Map map = new HashMap<String, String>();
        map.put("adminId", adminId.toString());
        String startTime = DateUtil.dateToString(DateUtil.getBeginMonthOfDate(DateUtil.getBeginOfDay(date)), "yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;//月
        int year = calendar.get(Calendar.YEAR);      //年
        String endTime = DateUtil.dateToString(DateUtil.getEndTimeOfMonth(year, month), "yyyy-MM-dd HH:mm:ss");
        
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        List<WxSerialSignRecord> list = selectByMap(map);
        
        if(list!=null && list.size()>0){
            for(WxSerialSignRecord item : list){
                totalCount += item.getCount().intValue();
            }
        }
        return totalCount;
    }

}
