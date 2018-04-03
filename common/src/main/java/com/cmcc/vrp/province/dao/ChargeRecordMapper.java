package com.cmcc.vrp.province.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.boss.shangdong.boss.model.ServiceModel;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.EntBillRecord;
import com.cmcc.vrp.province.model.statement.FingerprintStat;
import com.cmcc.vrp.province.module.ChargeStatisticLineModule;
import com.cmcc.vrp.province.module.ChargeStatisticListModule;
import com.cmcc.vrp.province.reconcile.model.DBModel;
import com.cmcc.vrp.province.reconcile.model.JSModel;

/**
 * <p>Title:ChargeRecordMapper </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月9日
 */
public interface ChargeRecordMapper {

    /**
     * @Title: insert
     * @Description: TODO
     */
    int insert(ChargeRecord chargeRecord);

    /**
     * @Title: batchInsert
     * @Description: TODO
     */
    int batchInsert(@Param("list") List<ChargeRecord> records);

    /**
     * @Title: insertSelective
     * @Description: TODO
     */
    int insertSelective(ChargeRecord chargeRecord);

    /**
     * @Title: updateByPrimaryKeySelective
     * @Description: TODO
     */
    int updateByPrimaryKeySelective(ChargeRecord chargeRecord);

    /**
     * @Title: updateStatus
     * @Description: TODO
     */
    int updateStatus(@Param("id") Long id, @Param("status") int status, @Param("errorMsg") String errorMsg);

    /**
     * @param financeStatus 
     * @param updateChargeTime 
     * @Title: batchUpdateStatus
     * @Description: TODO
     */
    int batchUpdateStatus(@Param("records") List<ChargeRecord> records, 
            @Param("updateChargeTime")Date updateChargeTime, @Param("financeStatus")Integer financeStatus);

    /**
     * 统计充值记录列表
     */
    List<ChargeStatisticListModule> statisticChargeList(Map map);
    
    /**
     * 山东统计充值记录列表,原因，sql里面的fullDistrictName ,结构和省公司不同
     */
    List<ChargeStatisticListModule> sdstatisticChargeList(Map map);

    /**
     * @Title: statisticChargeListCount
     * @Description: TODO
     */
    int statisticChargeListCount(Map map);

    /**
     * 充值记录的列表统计
     *
     * @param map
     * @return
     */
    List<ChargeStatisticLineModule> statistictByChargeDay(Map map);

    /**
     * 充值记录状态更新
     */
    int updateByTypeCodeAndRecordId(Map map);

    /**
     * @Title: updateByRecordId
     * @Description: TODO
     */
    int updateByRecordId(@Param("recordId") Long recordId, @Param("status") Integer status, @Param("errorMessage") String errMsg);

    /**
     * @param updateChargeTime 
     * @param financeStatus 
     * @Title: updateBySystemNum
     * @Description: TODO
     */
    int updateBySystemNum(@Param("systemNum") String systemNum, @Param("status") Integer status, @Param("errorMessage") String errMsg,
            @Param("financeStatus") Integer financeStatus, @Param("updateChargeTime") Date updateChargeTime);

    /**
     * @Title: selectRecordBySN
     * @Description: TODO
     */
    ChargeRecord selectRecordBySN(@Param("systemNum") String systemNum);

    /**
     * @Title: get
     * @Description: TODO
     */
    ChargeRecord get(Long id);

    /**
     * @Title: selectRecords
     * @Description: TODO
     */
    List<ChargeRecord> selectRecords(@Param("enterId") Long entId, @Param("pageOff") Integer pageOff, @Param("pageSize") Integer pageSize);

    /**
     * @Title: selectByTypeCodeAndRecordId
     * @Description: TODO
     */
    ChargeRecord selectByTypeCodeAndRecordId(Map map);

    /**
     * 统计充值记录
     *
     * @param map
     * @return
     */
    List<ChargeStatisticLineModule> selectByChargeDay(Map map);

    //查询某段时间内指定企业的充值总量

    /**
     * @Title: statement
     * @Description: TODO
     */
    List<FingerprintStat> statement(@Param("begin") Date beginTime, @Param("end") Date endTime, @Param("entId") Long entId);

    /**
     * 山东专用，得到对账时需要的数据
     * params 中必需有的参数 billStartTime，billEndTime，billStartTime精确到00:00:00，billEndTime精确到23:59:59
     *
     * @return
     */
    List<ServiceModel> sdGetReconcileDatas(Map<String, Object> params);
    
    /**
     * 山东专用，得到对账时需要的数据,带boss流水号
     * params 中必需有的参数 billStartTime，billEndTime，billStartTime精确到00:00:00，billEndTime精确到23:59:59
     *
     * @return
     */
    List<ServiceModel> sdGetReconcileSeqDatas(Map<String, Object> params);

    /**
     * 山东专用，得到对账时需要的数据
     * params 中必需有的参数 billStartTime，billEndTime，billStartTime精确到00:00:00，billEndTime精确到23:59:59
     *
     * @return
     */
    List<ServiceModel> sdGetFlowReconcileDatas(Map<String, Object> params);
    
    /**
     * @param enterId
     * @param serialNum
     * @return
     * @Title: selectRecordByEnterIdAndSerialNum
     * @Description: 根据企业ID和请求序列号查询
     * @return: List<ChargeRecord>
     */
    List<ChargeRecord> selectRecordByEnterIdAndSerialNum(@Param("enterId") Long enterId, @Param("serialNum") String serialNum);

    /**
     * 根据流水号批量查找充值记录
     *
     * @param systemNums
     * @return
     */
    List<ChargeRecord> batchSelectBySystemNum(@Param("systemNums") List<String> systemNums);

    /**
     * @Title: updateStatusCode
     */
    int updateStatusCode(@Param("recordId") Long recordId, @Param("statusCode") String statusCode);

    /**
     * @Title: updateStatusAndStatusCode
     * @Description: TODO
     */
    int updateStatusAndStatusCode(@Param("recordId") Long recordId,
                                  @Param("statusCode") String statusCode,
                                  @Param("status") int status,
                                  @Param("errorMsg") String errorMsg, 
                                  @Param("financeStatus") Integer financeStatus, 
                                  @Param("updateChargeTime") Date updateChargeTime);
    /**
     * @Title: batchUpdateStatusCode
     * @Description: TODO
     */
    int batchUpdateStatusCode(@Param("statusCode") String statusCode,
                              @Param("systemNums") List<String> systemNums);
    
    /**
     * 根据类型和recordId查找
     *
     * @param systemNums
     * @return
     */
    List<ChargeRecord> selectByTypeRecordId(@Param("recordId") Long recordId, @Param("type") String type);

    /** 
     * @Title: updateStatusCodeBySystemNum 
     */
    int updateStatusCodeBySystemNum(@Param("systemNum")String systemNum, @Param("statusCode")String statusCode);
    
    /** 
     * 根据条件筛选企业账单记录
     * @Title: selectEntBillRecordByMap 
     */
    List<EntBillRecord> selectEntBillRecordByMap(Map map);
    
    /** 
     * 根据条件获取企业账单记录的数量
     * @Title: countEntBillRecordByMap 
     */
    int countEntBillRecordByMap(Map map);

    /** 
     * 根据条件获取企业账单记录金额和
     * @Title: sumEntBillPriceByMap 
     */
    Long sumEntBillPriceByMap(Map map);
    
    
    /** 
     * 更新boss充值时间
     * @Title: updateBossChargeTimeBySystemNum 
     */
    int updateBossChargeTimeBySystemNum(@Param("systemNum")String systemNum, @Param("date")Date date);

    /** 
     * @Title: batchUpdateBossChargeTimeBySystemNum 
     */
    int batchUpdateBossChargeTimeBySystemNum(@Param("systemNums")List<String> systemNums, @Param("date")Date date);

    /**
     * @Title:queryChargeRecord
     * @Description: 查询充值报表
     * @author: qinqinyan
     * */
    List<ChargeRecord> queryChargeRecord(Map map);

    /**
     * @Title:countChargeRecord
     * @Description: 查询充值报表
     * @author: qinqinyan
     * */
    Long countChargeRecord(Map map);
    
    /**
     * 得到江苏对账数据
     * qihang
     */
    List<DBModel> getJsReconcileDatas(Map<String, Object> params);
    
    /**
     * 得到江苏对账数据,带boss返回流水号的
     * qihang
     */
    List<DBModel> jsReconcileDatasWithRespSeq(Map<String, Object> params);


    /**
     * 江西渠道前一天充值记录
     * @param beginTime
     * @param endTime
     * @param supplierId
     * @return
     */
    List<ChargeRecord> getJxChargeRecords(@Param("begin") Date beginTime, @Param("end") Date endTime, @Param("supplierId") Long supplierId);

    /**
     * 
     * @Title: getMdrcChargeRecords 
     * @Description: TODO
     * @param map
     * @return
     * @return: List<ChargeRecord>
     */
    List<ChargeRecord> getMdrcChargeRecords(Map map);
    
    /**
     * 
     * @Title: countMdrcChargeRecords 
     * @Description: TODO
     * @param map
     * @return
     * @return: long
     */
    long countMdrcChargeRecords(Map map);

    /**
     * @param systemNum
     * @param date
     * @return
     */
    int updateQueryTime(@Param("systemNum")String systemNum, @Param("date")Date date);

    /**
     * 
     * */
    List<ChargeRecord> getRecordsByMobileAndPrd(@Param("mobile") String mobile, @Param("splPid") Long splPid, @Param("date") Date date);

    /**
     * 江苏专用，得到对账时需要的数据
     * params 中必需有的参数 billStartTime，billEndTime，billStartTime精确到秒，billEndTime精确到秒
     * 例如billStartTime 2017-09-05 23:55:00 billEndTime 2017-09-06 00:05:00
     *
     * @return
     */
    List<JSModel> jsGetReconcileDatas(Map<String, Object> params);
}