package com.cmcc.vrp.province.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cmcc.vrp.province.dao.YqxRefundRecordMapper;
import com.cmcc.vrp.province.model.YqxRefundRecord;
import com.cmcc.vrp.province.service.YqxRefundRecordService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.QueryObject;

/**
 * 
 * YqxRefundRecordServiceImpl
 *
 */
@Service("YqxRefundRecordService")
public class YqxRefundRecordServiceImpl implements YqxRefundRecordService {

    @Autowired
    YqxRefundRecordMapper yqxRefundRecordMapper;
    
    /**
     * 插入,返回主键
     */
    @Override
    public boolean insert(YqxRefundRecord record) {
        return yqxRefundRecordMapper.insertSelective(record) > 0;
    }

    /**
     * selectByPrimaryKey(Long id)
     */
    @Override
    public YqxRefundRecord selectByPrimaryKey(Long id) {
        return yqxRefundRecordMapper.selectByPrimaryKey(id);
    }

    /**
     * updateByPrimaryKeySelective(YqxRefundRecord record)
     */
    @Override
    public boolean updateByPrimaryKeySelective(YqxRefundRecord record) {
        return yqxRefundRecordMapper.updateByPrimaryKeySelective(record)>0;
    }

    /**
     * updateByDoneCodeAcceptedRecord
     * 根据DoneCode和status为1的记录更新数据库,只有异步回调时使用
     * 
     */
    @Override
    public boolean updateByDoneCodeAcceptedRecord(String doneCode, int status,
            String msg) {
        return yqxRefundRecordMapper.updateByDoneCodeAcceptedRecord(doneCode, status, msg)>0;
    }

    /**
     * 分页
     */
    @Override
    public List<YqxRefundRecord> queryPaginationRefundList(QueryObject queryObject) {
        Map map = queryObject.toMap();
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
        return yqxRefundRecordMapper.queryPaginationRefundList(map);
    }

    /**
     * 分页
     */
    @Override
    public int queryPaginationRefundCount(QueryObject queryObject) {
        Map map = queryObject.toMap();
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
        return yqxRefundRecordMapper.queryPaginationRefundCount(map);
    }

    @Override
    public YqxRefundRecord selectByRefundSerialNum(String refundSerialNum) {
        return yqxRefundRecordMapper.selectByRefundSerialNum(refundSerialNum);
    }

    @Override
    public List<YqxRefundRecord> selectByDoneCodeAndStatus(String doneCode, String statusStr) {
        return yqxRefundRecordMapper.selectByDoneCodeAndStatus(doneCode, statusStr); 
    }
}
