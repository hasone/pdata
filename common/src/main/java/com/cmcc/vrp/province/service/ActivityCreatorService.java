package com.cmcc.vrp.province.service;

import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.model.ActivityCreator;

/**
 * 活动创建者服务
 * 
 */
public interface ActivityCreatorService {
    /**
     * 插入
     */
    boolean insertSelective(ActivityCreator record);
    
    /**
     * 插入
     */
    boolean insert(ActivityType actType,Long actId,Long adminId);
    
    /**
     * 根据活动类型和Id获取创建者
     */
    ActivityCreator getByActId(ActivityType actType ,Long actId );
    
}
