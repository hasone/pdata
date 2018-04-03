package com.cmcc.vrp.province.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.ActivityPaymentInfo;

/**
 * 广东众筹活动支付记录mapper
 * Created by qinqinyan on 2017/1/6.
 * */
public interface ActivityPaymentInfoMapper {

    /**
     * @Description:根据主键id查找
     * @param id
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @Description:根据主键sysSerialNum查找
     * @param sysSerialNum
     * */
    int deleteBySysSerialNum(String sysSerialNum);

    /**
     * @Description:插入
     * @param record
     * */
    int insert(ActivityPaymentInfo record);

    /**
     * @Description:插入
     * @param record
     * */
    int insertSelective(ActivityPaymentInfo record);

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
    int updateBySysSerialNumSelective(ActivityPaymentInfo record);

    /**
     * @Description:根据系统流水号更新记录
     * @param record
     * */
    int updateBySysSerialNum(ActivityPaymentInfo record);

    /**
     * @param winRecordId
     * @return
     */
    List<ActivityPaymentInfo> selectByWinRecordId(String winRecordId);

    /**
     * @param returnSerialNum
     * @return
     */
    ActivityPaymentInfo selectByReturnSerialNum(String returnSerialNum);

    /** 
     * @Title: selectByWinRecordIdAndStatus 
     */
    List<ActivityPaymentInfo> selectByWinRecordIdAndStatus(@Param("winRecordId")String winRecordId, @Param("status")Integer status);
}