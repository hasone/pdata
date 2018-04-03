package com.cmcc.vrp.province.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.WxSerialSignRecord;

/**
 * 广东众筹连续签到服务类
 * @author qinqinyan
 * */
public interface WxSerialSignRecordService {
    
    /**
     * @title:insertSelective
     * */
    boolean insertSelective(WxSerialSignRecord record);
    
    /**
     * @title:updateByPrimaryKeySelective
     * */
    boolean updateByPrimaryKeySelective(WxSerialSignRecord record);
  
    /**
     * @title: selectByMap
     * */
    List<WxSerialSignRecord> selectByMap(Map map);
    
    /**
     * 由于要兼容线上版本
     * 所以这个方法用于获取所有指定月份的连续签到记录
     * 在根据这些记录累加总的签到次数
     * 
     * @author qinqinyan
     * @date 2017/09/06
     * */
    int getTotalCountByAdminIdAndMonth(Long amindId, Date date);

}
