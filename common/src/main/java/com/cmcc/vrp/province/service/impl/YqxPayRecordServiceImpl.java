package com.cmcc.vrp.province.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.pay.enums.YqxRefundStatus;
import com.cmcc.vrp.pay.model.PayBillModel;
import com.cmcc.vrp.province.cache.AbstractCacheSupport;
import com.cmcc.vrp.province.dao.PayBillModelMapper;
import com.cmcc.vrp.province.dao.YqxPayRecordMapper;
import com.cmcc.vrp.province.model.YqxPayReconcileRecord;
import com.cmcc.vrp.province.model.YqxPayRecord;
import com.cmcc.vrp.province.model.YqxRefundRecord;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.YqxPayRecordService;
import com.cmcc.vrp.province.service.YqxRefundRecordService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * 
 * YqxPayRecordServiceImpl
 *
 */
@Service("YqxPayRecordService")
public class YqxPayRecordServiceImpl extends AbstractCacheSupport implements YqxPayRecordService {

    @Autowired
    YqxPayRecordMapper yqxPayRecordMapper;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    YqxRefundRecordService yqxRefundRecordService;
    @Autowired
    PayBillModelMapper payBillModelMapper;
    
    /**
     * 定义的转义类，int左边不足则补零
     */
    public static final DecimalFormat DECIMALFORMAT=new DecimalFormat("000000");
    
    @Override
    public boolean insert(YqxPayRecord record) {
        return yqxPayRecordMapper.insertSelective(record)>0;
    }

    @Override
    public YqxPayRecord selectByPrimaryKey(Long id) {
        return yqxPayRecordMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<YqxPayRecord> selectByOrderSerialNum(String orderSerialNum) {
        return yqxPayRecordMapper.selectByOrderSerialNum(orderSerialNum);
    }

    @Override
    public YqxPayRecord selectByPayIds(String payOrderId,
            String payTransactionId) {
        List<YqxPayRecord> list = yqxPayRecordMapper.selectByPayIds(payOrderId, payTransactionId);
        return list.isEmpty()?null:list.get(0);
    }

    @Override
    public boolean updateByPrimaryKeySelective(YqxPayRecord record) {
        return yqxPayRecordMapper.updateByPrimaryKeySelective(record)>0;
    }
    
    @Override
    public String getNewTransactionId() {
        Long hoursCount = cacheService.getIncrOrUpdate("transactionId", 3600);
        
        /**
         * 14位组包时间YYYYMMDDHH24MISS
         */
        SimpleDateFormat cloudDateFormate = new SimpleDateFormat("YYYYMMddHHmmss");
        
        StringBuffer buffer = new StringBuffer();
        if ("chongqing".equals(getProvinceFlag())) {
            buffer.append("cq");
        }        
        buffer.append(cloudDateFormate.format(new Date()));
        buffer.append("000000");
        buffer.append(DECIMALFORMAT.format(hoursCount % 1000000));
        if ("chongqing".equals(getProvinceFlag())) {
            buffer.append("yqx");
        }
        return buffer.toString();
    }

    @Override
    protected String getPrefix() {
        return "yqxPay";
    }

    @Override
    public YqxPayRecord selectNewestSuccessRecord(String orderSerialNum) {
        return yqxPayRecordMapper.selectNewestSuccessRecord(orderSerialNum);
    }

    @Override
    public YqxPayRecord selectByTransactionId(String payTransactionId) {
        return yqxPayRecordMapper.selectByTransactionId(payTransactionId);
    }
    
    @Override
    public YqxPayRecord selectByDoneCode(String doneCode) {
        List<YqxPayRecord> list = yqxPayRecordMapper.selectByDoneCode(doneCode);
        return list.isEmpty()?null:list.get(0);
    }
    

    private String getProvinceFlag() {
        return globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey());
    }

    @Override
    public Integer countRepeatPayByMap(Map<String, Object> map) {
        if(map.get("searchTime")!=null && !StringUtils.isEmpty((String) map.get("searchTime"))){
            String time = (String) map.get("searchTime");
            String[] times = time.split("~");
            String startTime = times[0];
            String endTime = times[1];
            startTime = DateUtil.dateToString(DateUtil.parse("yyyy-MM-ddHH:mm", startTime), "yyyy-MM-dd HH:mm");
            endTime = DateUtil.dateToString(DateUtil.parse("yyyy-MM-ddHH:mm", endTime), "yyyy-MM-dd HH:mm");
            map.put("startTime", startTime);
            map.put("endTime", endTime);
        }
        
        return yqxPayRecordMapper.countRepeatPayByMap(map);
    }

    @Override
    public List<YqxPayRecord> selectRepeatPayByMap(Map<String, Object> map) {
        if(map.get("searchTime")!=null && !StringUtils.isEmpty((String) map.get("searchTime"))){
            String time = (String) map.get("searchTime");
            String[] times = time.split("~");
            String startTime = times[0];
            String endTime = times[1];
            startTime = DateUtil.dateToString(DateUtil.parse("yyyy-MM-ddHH:mm", startTime), "yyyy-MM-dd HH:mm");
            endTime = DateUtil.dateToString(DateUtil.parse("yyyy-MM-ddHH:mm", endTime), "yyyy-MM-dd HH:mm");
            map.put("startTime", startTime);
            map.put("endTime", endTime);
        }
        
        return yqxPayRecordMapper.selectRepeatPayByMap(map);
    }

    @Override
    public Integer countByMap(Map<String, Object> map) {
        return yqxPayRecordMapper.countByMap(map);
    }

    @Override
    public List<YqxPayRecord> selectByMap(Map<String, Object> map) {
        List<YqxPayRecord> payRecords = yqxPayRecordMapper.selectByMap(map);
        for(YqxPayRecord payRecord : payRecords){//充值状态为失败，且退款记录不是待发送、受理成功、退款成功
            if(!StringUtils.isEmpty(payRecord.getDoneCode())&&ChargeRecordStatus.FAILED.getCode().equals(payRecord.getChargeStatus())){
                String status = YqxRefundStatus.ACCETED.getStatus() + "," + YqxRefundStatus.SUCCESS.getStatus()+ "," + YqxRefundStatus.WAIT.getStatus();
                List<YqxRefundRecord> refundRecords = yqxRefundRecordService.selectByDoneCodeAndStatus(payRecord.getDoneCode(), status);
                if(refundRecords == null || refundRecords.size() == 0){
                    payRecord.setCanRefund(true);
                }
                
            }
        }
        return payRecords;
    }

    @Override
    public Integer reconcileCountByMap(Map<String, Object> map) {
        if(map.get("searchTime")!=null && !StringUtils.isEmpty((String) map.get("searchTime"))){
            String time = (String) map.get("searchTime");
            String[] times = time.split("~");
            String startTime = times[0];
            String endTime = times[1];
            startTime = DateUtil.dateToString(DateUtil.parse("yyyy-MM-ddHH:mm", startTime), "yyyy-MM-dd HH:mm");
            endTime = DateUtil.dateToString(DateUtil.parse("yyyy-MM-ddHH:mm", endTime), "yyyy-MM-dd HH:mm");
            map.put("startTime", startTime);
            map.put("endTime", endTime);
        }
        
        return yqxPayRecordMapper.reconcileCount(map);
    }

    @Override
    public List<YqxPayReconcileRecord> reconcileSelectByMap(
            Map<String, Object> map) {
        return yqxPayRecordMapper.reconcileRecords(map);
    }
    
    @Override
    public Integer payBillCountByMap(Map<String, Object> map) {
        if(map.get("searchTime")!=null && !StringUtils.isEmpty((String) map.get("searchTime"))){
            String time = (String) map.get("searchTime");
            String[] times = time.split("~");
            String startTime = times[0];
            String endTime = times[1];
            startTime = DateUtil.dateToString(DateUtil.parse("yyyy-MM-ddHH:mm", startTime), "yyyy-MM-dd HH:mm");
            endTime = DateUtil.dateToString(DateUtil.parse("yyyy-MM-ddHH:mm", endTime), "yyyy-MM-dd HH:mm");
            map.put("startTime", startTime);
            map.put("endTime", endTime);
        }
        
        return payBillModelMapper.billCount(map);
    }

    @Override
    public List<YqxPayReconcileRecord> payBillSelectByMap(
            Map<String, Object> map) {
        return payBillModelMapper.billRecords(map);
    }

    @Override
    public List<YqxPayRecord> reconcileRangeTime(String date) {
        //date格式
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");  
        DateTime dateTime = DateTime.parse(date, format); 
        
        Date startTimeDate = dateTime.withTimeAtStartOfDay().toDate();//当天00:00:00
        Date endTimeDate= dateTime.millisOfDay().withMaximumValue().toDate();//当天23:59:59
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startTime", startTimeDate);
        params.put("endTime", endTimeDate);
        return yqxPayRecordMapper.reconcileRangeTime(params);
    }

    @Override
    public int updateReconcileInfo(YqxPayRecord record) {
        return yqxPayRecordMapper.updateReconcileInfo(record);
    }

    @Override
    public boolean insertYqxBill(PayBillModel model) {
        return payBillModelMapper.insertSelective(model)>0;
    }

    @Override
    public boolean updateChargeStatus(Integer chargeStatus, String payTransactionId, Date chargeTime) {
        return yqxPayRecordMapper.updateChargeStatus(chargeStatus, payTransactionId, chargeTime) == 1;
    }

    
}
