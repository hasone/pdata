/**
 * @Title: InterfaceRecordService.java
 * @Package com.cmcc.vrp.province.service
 * @author: qihang
 * @date: 2015年11月4日 下午5:03:51
 * @version V1.0
 */
package com.cmcc.vrp.province.service;

import com.cmcc.vrp.enums.ActivityWinRecordStatus;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.InterfaceRecord;

import java.util.List;

/**
 * @ClassName: InterfaceRecordService
 * @Description: TODO
 * @author: qihang
 * @date: 2015年11月4日 下午5:03:51
 */
public interface InterfaceRecordService {

    /**
     * 插入记录
     * @param record  记录
     * @return  结果
     */
    boolean insert(InterfaceRecord record);

    /**
     * 获取记录
     * @param id   主键
     * @return    结果
     */
    InterfaceRecord get(Long id);

    /**
     * 获取记录
     * @param enterCode enterCode
     * @param serialNum serialNum
     * @param phoneNum phoneNum
     * @return  结果
     */
    InterfaceRecord get(String enterCode, String serialNum, String phoneNum);

    /**
     * 更新状态
     * @param id id
     * @param status status
     * @param errMsg errMsg
     * @return 结果
     */
    boolean updateChargeStatus(Long id, ChargeRecordStatus status, String errMsg);

    boolean updateActivityStatus(Long id, ActivityWinRecordStatus status, String errMsg);
    
    /**
     * 更新状态
     * @param id id
     * @param statusCode statusCode
     * @return 结果
     */
    boolean updateStatusCode(Long id, String statusCode);

    /**
     * 更新记录
     * @param records records
     * @return 结果
     */
    boolean batchUpdateStatus(List<InterfaceRecord> records);
    
    /** 
     * @Title: selectBySerialNum 
     */
    List<InterfaceRecord> selectBySerialNum(String serialNum);

}
