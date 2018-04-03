package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.WxSignDetailRecord;

/**
 * 广东众筹签到详细记录表
 * @author qinqinyan
 * */
public interface WxSignDetailRecordMapper {
    /**
     * @title:deleteByPrimaryKey
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @title:insert
     * */
    int insert(WxSignDetailRecord record);

    /**
     * @title:insertSelective
     * */
    int insertSelective(WxSignDetailRecord record);

    /**
     * @title:selectByPrimaryKey
     * */
    WxSignDetailRecord selectByPrimaryKey(Long id);

    /**
     * @title:updateByPrimaryKeySelective
     * */
    int updateByPrimaryKeySelective(WxSignDetailRecord record);

    /**
     * @title:updateByPrimaryKey
     * */
    int updateByPrimaryKey(WxSignDetailRecord record);
    
    /**
     * @title:selectByMap
     * */
    List<WxSignDetailRecord> selectByMap(Map map);
}