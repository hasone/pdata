package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.WxSerialSignRecord;

/**
 * 广东众筹连续签到记录表
 * @author qinqinyan
 * */
public interface WxSerialSignRecordMapper {
    /**
     * @title:deleteByPrimaryKey
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @title:insert
     * */
    int insert(WxSerialSignRecord record);

    /**
     * @title:insertSelective
     * */
    int insertSelective(WxSerialSignRecord record);

    /**
     * @title:selectByPrimaryKey
     * */
    WxSerialSignRecord selectByPrimaryKey(Long id);

    /**
     * @title:updateByPrimaryKeySelective
     * */
    int updateByPrimaryKeySelective(WxSerialSignRecord record);

    /**
     * @title:updateByPrimaryKey
     * */
    int updateByPrimaryKey(WxSerialSignRecord record);
    
    /**
     * @title:selectByMap
     * */
    List<WxSerialSignRecord> selectByMap(Map map);
}