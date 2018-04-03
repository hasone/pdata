package com.cmcc.vrp.province.service;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.WxSignDetailRecord;
/**
 * 广东众筹签到详细记录服务类
 * @author qinqinyan
 * */
public interface WxSignDetailRecordService {
    
    /**
     * @title:insertSelective
     * */
    boolean insertSelective(WxSignDetailRecord record);
    
    /**
     * @title:selectByMap
     * */
    List<WxSignDetailRecord> selectByMap(Map map);
}
