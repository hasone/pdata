package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.SerialNum;

import java.util.List;

/**
 * 序列号服务
 * Created by sunyiwei on 2016/8/9.
 */
public interface SerialNumService {
    /**
     * 插入新的序列号对象
     *
     * @param serialNum 序列号对象
     * @return
     */
    boolean insert(SerialNum serialNum);

    //批量插入
    /** 
     * @Title: batchInsert 
    */
    boolean batchInsert(List<SerialNum> serialNums);

    /**
     * 根据平台序列号查询记录
     *
     * @param pltSerialNum
     * @return
     */
    SerialNum getByPltSerialNum(String pltSerialNum);

    /**
     * 根据平台向BOSS侧请求的序列号查询
     *
     * @param bossReqSerialNum 平台向BOSS侧请求的序列号
     * @return 序列号对象
     */
    SerialNum getByBossReqSerialNum(String bossReqSerialNum);

    /**
     * 根据BOSS响应平台侧响应码进行查询
     *
     * @param bossRespSerialNum BOSS侧响应的序列号
     * @return 序列号对象
     */
    SerialNum getByBossRespSerialNum(String bossRespSerialNum);

    /**
     * 更新流水号
     *
     * @param serialNum
     * @return
     */
    boolean updateSerial(SerialNum serialNum);

    /**
     * 批量更新流水号
     *
     * @param serialNums
     * @return
     */
    boolean batchUpdate(List<SerialNum> serialNums);
    
    /** 
     * @Title: deleteByPlatformSerialNum 
     */
    boolean deleteByPlatformSerialNum(String platformSerialNum);
}
