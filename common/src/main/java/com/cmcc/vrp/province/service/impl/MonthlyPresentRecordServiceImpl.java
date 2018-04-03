package com.cmcc.vrp.province.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.enums.ActivityWinRecordStatus;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.dao.MonthlyPresentRecordMapper;
import com.cmcc.vrp.province.model.MonthlyPresentRecord;
import com.cmcc.vrp.province.service.MonthlyPresentRecordService;
import com.cmcc.vrp.util.QueryObject;
/**
 * 
 * @ClassName: MonthlyPresentRecordServiceImpl 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年7月18日 下午4:16:24
 */
@Service
public class MonthlyPresentRecordServiceImpl implements MonthlyPresentRecordService{

    @Autowired
    MonthlyPresentRecordMapper monthlyPresentRecordMapper;
    
    /**
     * 插入
     */
    @Override
    public boolean insert(MonthlyPresentRecord record) {
        return monthlyPresentRecordMapper.insertSelective(record) == 1;
    }

    /**
     * 查询
     */
    @Override
    public List<MonthlyPresentRecord> getRecords(QueryObject queryObject) {
        return monthlyPresentRecordMapper.getRecords(queryObject.toMap());
    }

    /**
     * 统计
     */
    @Override
    public Long countRecords(QueryObject queryObject) {
        return monthlyPresentRecordMapper.countRecords(queryObject.toMap());
    }

    /**
     * 批量插入
     */
    @Override
    public boolean batchInsert(List<MonthlyPresentRecord> list) {
        return monthlyPresentRecordMapper.batchInsert(list) == list.size();
    }

    @Override
    public boolean updateActivityStatus(Long id,
            ActivityWinRecordStatus status, String errorMsg) {
        if (id == null 
                || status == null) {
            return false;
        }
        return monthlyPresentRecordMapper.updateStatusAndStatusCode(id, status.getCode(), status.getStatusCode(), errorMsg) == 1;
    }

    @Override
    public boolean batchUpdateChargeResult(List<MonthlyPresentRecord> records) {
        return records != null && !records.isEmpty()
                && monthlyPresentRecordMapper.batchUpdateChargeResult(records) == records.size();
    }

    @Override
    public boolean updatePresentStatus(Long id, ChargeRecordStatus status,
            String errorMsg) {
        if (id == null || status == null) {
            return false;
        }

        return monthlyPresentRecordMapper.updateStatus(id, status.getCode(), errorMsg) == 1;
    }

    @Override
    public boolean updateStatusCode(Long recordId, String statusCode) {
        return recordId != null && monthlyPresentRecordMapper.updateStatusCode(recordId, statusCode) == 1;
    }

    @Override
    public boolean batchUpdateStatusCode(List<Long> ids, String statusCode) {
        return ids != null && !ids.isEmpty()
                && monthlyPresentRecordMapper.batchUpdateStatusCode(ids, statusCode) == ids.size();
    }


}
