package com.cmcc.vrp.province.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.ActivityWinRecord;

/**
 * <p>Title:ActivityWinRecordMapper </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月8日
 */
public interface ActivityWinRecordMapper {
    /**
     * @Title: deleteByPrimaryKey
     * @Description: TODO
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @Title: insert
     * @Description: TODO
     */
    int insert(ActivityWinRecord record);

    /**
     * @Title: insertSelective
     * @Description: TODO
     */
    int insertSelective(ActivityWinRecord record);

    /**
     * @Title: selectByPrimaryKey
     * @Description: TODO
     */
    ActivityWinRecord selectByPrimaryKey(Long id);

    /**
     * @Title: updateByPrimaryKeySelective
     * @Description: TODO
     */
    int updateByPrimaryKeySelective(ActivityWinRecord record);

    /**
     * @Title: updateByPrimaryKey
     * @Description: TODO
     */
    int updateByPrimaryKey(ActivityWinRecord record);

    /**
     * @Title: batchUpdateStatus
     * @Description: TODO
     */
    int batchUpdateStatus(@Param("recordIds") List<String> recordIds,
                          @Param("chargeMobile") String chargeMobile,
                          @Param("newStatus") int newStatus,
                          @Param("oldStatus") int oldStatus,
                          @Param("winTime") Date winTime);
    
    /**
     * @Title: batchUpdateForFlowcard
     * @Description: TODO
     */
    int batchUpdateForFlowcard(@Param("recordIds") List<String> recordIds, 
            @Param("chargeMobile") String chargePhone, 
            @Param("newStatus") int newStatus, 
            @Param("oldStatus") int oldStatus,
            @Param("winTime") Date winTime, 
            @Param("channel") String channel);

    //更新状态

    /**
     * @Title: updateStatus
     * @Description: TODO
     */
    int updateStatus(@Param("id") Long id, @Param("status") int status, @Param("errorMsg") String errorMsg, @Param("chargeMobile") String mobile);
    
    /**
     * @param id
     * @param status
     * @param statusCode
     * @param errorMsg
     * @param mobile
     * @return
     */
    int updateStatusAndStatusCode(@Param("id") Long id, @Param("status") int status, @Param("statusCode") String statusCode, 
	    @Param("errorMsg") String errorMsg, @Param("chargeMobile") String mobile);
    
    /**
     * @param activityId
     * @return
     * @Title: getCurrentActivityInfo
     * @Description: 根据activityId获取当前中奖记录，返回的对象带有补充信息（金额，流量包大小）
     * @Author: wujiamin
     * @date 2016年8月18日下午3:03:16
     */
    List<ActivityWinRecord> getCurrentActivityInfo(String activityId);

    /**
     * @Title: showForPageResultCount
     * @Description: TODO
     */
    int showForPageResultCount(Map<String, Object> map);
    
    /**
     * @Title: countWinRecords
     * @Description: TODO
     * 用于广东众筹，prizeId是activity_prize的id，不是product的id
     * @author qinqinyan
     */
    int countWinRecords(Map<String, Object> map);

    /**
     * @Title: showForPageResult
     * @Description: TODO
     */
    List<ActivityWinRecord> showForPageResult(Map<String, Object> map);
    
    
    /**
     * @Title: showWinRecords
     * 用于广东众筹，prizeId是activity_prize的id，不是product的id
     * @param map
     * @author qinqinyan
     * */
    List<ActivityWinRecord> showWinRecords(Map<String, Object> map);
    
    /**
     * @Title: selectAllWinRecords
     * 用于广东众筹,下载报表功能
     * @param map
     * @author qinqinyan
     * */
    List<ActivityWinRecord> selectAllWinRecords(Map<String, Object> map);

    /**
     * @Title: selectByMap
     * @Description: TODO
     */
    List<ActivityWinRecord> selectByMap(Map map);

    /**
     * @Title: batchInsert
     * @Description: TODO
     */
    int batchInsert(@Param("records") List<ActivityWinRecord> records);

    /**
     * @Title: selectByActivityId
     * @Description: TODO
     */
    List<ActivityWinRecord> selectByActivityId(@Param("activityId") String activityId);

    /**
     * @Title: selectByActivityIdAndIsp
     * @Description: TODO
     */
    List<ActivityWinRecord> selectByActivityIdAndIsp(@Param("activityId") String activityId, @Param("isp") String isp);

    /**
     * @Title: deleteByActivityId
     * @Description: TODO
     */
    int deleteByActivityId(@Param("activityId") String activityId);

    /**
     * @Title: selectByRecordId
     * @Description: TODO
     */
    ActivityWinRecord selectByRecordId(String recordId);

    /**
     * 根据条件计算不同的充值号码的个数
     *
     * @param map
     * @return
     * @Title: countChargeMobileByMap
     * @Author: wujiamin
     * @date 2016年9月8日下午4:08:40
     */
    int countChargeMobileByMap(Map map);

    /**
     * @Title: selectByMapForRedpacket
     * @Description: TODO
     */
    List<ActivityWinRecord> selectByMapForRedpacket(Map map);

    /**
     * @Title: countByMapForRedpacket
     * @Description: TODO
     */
    Long countByMapForRedpacket(Map map);

    /**
     * @Title: showForPageResultCountIndividualPresent
     * @Description: TODO
     */
    int showForPageResultCountIndividualPresent(Map<String, Object> map);

    /**
     * @Title: showForPageResultIndividualPresent
     * @Description: TODO
     */
    List<ActivityWinRecord> showForPageResultIndividualPresent(Map<String, Object> map);

    /**
     * @Title: batchUpdate
     * @Description: TODO
     */
    int batchUpdate(@Param("records") List<ActivityWinRecord> records);

    /**
     * @Title: updateStatusCodeByRecordId
     * @Description: TODO
     */
    int updateStatusCodeByRecordId(@Param("recordId") String recordId, @Param("statusCode") String statusCode);
    /**
     * @Title: batchUpdateStatusCodeByRecordId
     * @Description: TODO
     */
    int batchUpdateStatusCodeByRecordId(@Param("sns") List<String> sns, @Param("statusCode") String statusCode);
    
    /** 
     * @Title: countChargeMobileByActivityId 
     */
    int countChargeMobileByActivityId(String activityId);

    /**
     * @Title: selectByActivityIdAndMobile
     * @Description: TODO
     */
    ActivityWinRecord selectByActivityIdAndMobile(@Param("activityId")String activityId, 
            @Param("mobile")String mobile);
    
    /**
     * @Title: getWinRecordsForCrowdFunding
     * @Description: TODO
     */
    List<ActivityWinRecord> getWinRecordsForCrowdFunding(Map<String, String> map);
    
    /** 
     * @Title: selectIndividualFlowRedpacketList 
     */
    List<ActivityWinRecord> selectIndividualFlowRedpacketList(Map map);

    /** 
     * @Title: countIndividualFlowRedpacketList 
     */
    Integer countIndividualFlowRedpacketList(Map map);
    
    /** 
     * @Title: getWinRecordsForCrowdFundingByMap 
     */
    List<ActivityWinRecord> getWinRecordsForCrowdFundingByMap(Map map);

    /** 
     * @Title: selectActivityIdByMobile 
     */
    List<String> selectActivityIdByMobile(String mobile);
}