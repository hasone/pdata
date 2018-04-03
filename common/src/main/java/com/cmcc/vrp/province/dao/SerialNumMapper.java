package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.SerialNum;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Title:SerialNumMapper
 * @Description:
 * */
public interface SerialNumMapper {
    /**
     * @Title:insert
     * @Description:
     * */
    int insert(SerialNum record);

    /**
     * @Title:batchInsert
     * @Description:
     * */
    int batchInsert(@Param("records") List<SerialNum> records);

    /**
     * @Title:getByPltSerialNum
     * @Description:
     * */
    SerialNum getByPltSerialNum(String pltSerialNum);

    /**
     * @Title:getByEcSerialNum
     * @Description:
     * */
    SerialNum getByEcSerialNum(String ecSerialNum);

    /**
     * @Title:getByBossReqSerialNum
     * @Description:
     * */
    SerialNum getByBossReqSerialNum(String bossReqSerialNum);

    /**
     * @Title:getByBossRespSerialNum
     * @Description:
     * */
    SerialNum getByBossRespSerialNum(String bossRespSerialNum);

    /**
     * @Title:update
     * @Description:
     * */
    int update(SerialNum record);

    /**
     * @Title:batchUpdate
     * @Description:
     * */
    int batchUpdate(@Param("records") List<SerialNum> records);
    
    /** 
     * @Title: deleteByPlatformSerialNum 
     */
    int deleteByPlatformSerialNum(@Param("platformSerialNum")String platformSerialNum);
}