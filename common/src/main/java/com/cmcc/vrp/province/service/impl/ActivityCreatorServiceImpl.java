package com.cmcc.vrp.province.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.dao.ActivityCreatorMapper;
import com.cmcc.vrp.province.model.ActivityCreator;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.service.ActivityCreatorService;
import com.cmcc.vrp.province.service.AdministerService;

/**
 * 活动创建者服务
 *
 */
@Service
public class ActivityCreatorServiceImpl implements ActivityCreatorService{

    @Autowired
    AdministerService administerService;
    
    @Autowired
    ActivityCreatorMapper activityCreatorMapper;
    
    @Override
    public boolean insertSelective(ActivityCreator record) {
        return activityCreatorMapper.insertSelective(record)>0;
    }

    @Override
    public boolean insert(ActivityType actType, Long actId, Long adminId) {
        if(actType == null || actId == null){
            return false;
        }
        
        Administer admin = administerService.selectAdministerById(adminId);
        if(admin == null){
            return false;
        }
        
        ActivityCreator record = new ActivityCreator();
        record.setActivityId(actId);
        record.setActivityType(actType.getCode());
        record.setAdminId(adminId);
        record.setUserName(admin.getUserName());
        record.setMobilePhone(admin.getMobilePhone());
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setDeleteFlag(0);
        
        return insertSelective(record);
    }

    @Override
    public ActivityCreator getByActId(ActivityType actType, Long actId) {
        if(actType == null || actId == null){
            return null;
        }
        List<ActivityCreator> list = activityCreatorMapper.selectByActId(actId, actType.getCode());
        return list.isEmpty()?null:list.get(0);
    }
    

}
