package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.ActivityPaymentInfo;

/**
 * 广东流量众筹活动支付记录服务类
 * Created by qinqinyan on 2017/1/6.
 */
public interface ActivityPaymentInfoService {

    /**
     * @Description:根据主键id查找
     * @param id
     * */
    boolean deleteByPrimaryKey(Long id);

    /**
     * @Description:根据主键sysSerialNum查找
     * @param sysSerialNum
     * */
    boolean deleteBySysSerialNum(String sysSerialNum);

    /**
     * @Description:插入
     * @param record
     * */
    boolean insert(ActivityPaymentInfo record);

    /**
     * @Description:插入
     * @param record
     * */
    boolean insertSelective(ActivityPaymentInfo record);

    /**
     * @Description:根据id查找
     * @param id
     * */
    ActivityPaymentInfo selectByPrimaryKey(Long id);

    /**
     * @Description:根据sysSerialNum查找
     * @param sysSerialNum
     * */
    ActivityPaymentInfo selectBySysSerialNum(String sysSerialNum);

    /**
     * @Description:根据系统流水号更新记录
     * @param record
     * */
    boolean updateBySysSerialNumSelective(ActivityPaymentInfo record);

    /**
     * @Description:根据系统流水号更新记录
     * @param record
     * */
    boolean updateBySysSerialNum(ActivityPaymentInfo record);
    
    
    /**
     * @Description:根据WinRecordId查找
     * @param sysSerialNum
     * */
    List<ActivityPaymentInfo> selectByWinRecordId(String winRecordId);
    
    /**
     * @param returnSerialNum
     * @return
     */
    ActivityPaymentInfo selectByReturnSerialNum(String returnSerialNum);

    /** 
     * 向第三方平台请求充值
     * @Title: callForPay 
     */
    boolean callForPay(String activityWinRecordId);

    /** 
     * 根据activityRecordId和支付状态查询支付记录
     * @Title: selectByWinRecordIdAndStatus 
     */
    List<ActivityPaymentInfo> selectByWinRecordIdAndStatus(String winRecordId, Integer status);
}
