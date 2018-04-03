package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.ActivityRecordMapper;
import com.cmcc.vrp.province.model.ActivityRecord;
import com.cmcc.vrp.province.service.ActivityRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * ActivityRecordServiceImpl
 * */
@Service("activityRecordService")
public class ActivityRecordServiceImpl implements ActivityRecordService {

    @Autowired
    ActivityRecordMapper activityRecordMapper;

    @Override
    public boolean insert(ActivityRecord activityRecord) {
        activityRecord.setCreateTime(new Date());
        activityRecord.setUpdateTime(new Date());
        if (activityRecordMapper.insert(activityRecord) > 0) {
            return true;
        } else {
            throw new RuntimeException();
        }
    }
}
