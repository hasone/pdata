package com.cmcc.vrp.province.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.SupplierReqUsePerDayMapper;
import com.cmcc.vrp.province.model.SupplierReqUsePerDay;
import com.cmcc.vrp.province.service.SupplierReqUsePerDayService;
import com.cmcc.vrp.util.DateUtil;

/**
 * 统计供应商每天发送充值请求的金额服务类（以成功发送请求充值为统计依据）
 * @author qinqinyan
 * */
@Service("supplierReqUsePerDayService")
public class SupplierReqUsePerDayServiceImpl implements SupplierReqUsePerDayService{

    @Autowired
    SupplierReqUsePerDayMapper mapper;
    
    @Override
    public boolean insertSelective(SupplierReqUsePerDay record) {
        // TODO Auto-generated method stub
        return mapper.insertSelective(record)==1;
    }

    @Override
    public SupplierReqUsePerDay selectByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateByPrimaryKeySelective(SupplierReqUsePerDay record) {
        // TODO Auto-generated method stub
        return mapper.updateByPrimaryKeySelective(record)==1;
    }

    @Override
    public boolean deleteByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        return mapper.deleteByPrimaryKey(id)==1;
    }

    @Override
    public boolean batchDelete(Map map) {
        // TODO Auto-generated method stub
        return mapper.batchDelete(map)>=0;
    }

    @Override
    public boolean updateUsedMoney(Long id, Double delta) {
        // TODO Auto-generated method stub
        return mapper.updateUsedMoney(id, delta)==1;
    }

    @Override
    public SupplierReqUsePerDay getTodayRecord(Long supplierId) {
        // TODO Auto-generated method stub
        Map map = new HashMap<String, String>();
        map.put("supplierId", supplierId);
        Date startTime = DateUtil.getBeginOfDay(new Date());
        Date endTime = DateUtil.getEndTimeOfDate(new Date());
        map.put("startTime", DateUtil.dateToString(startTime, "yyyy-MM-dd HH:mm:ss"));
        map.put("endTime", DateUtil.dateToString(endTime, "yyyy-MM-dd HH:mm:ss"));
        List<SupplierReqUsePerDay> list = mapper.selectByMap(map);
        return list!=null&&list.size()>0?list.get(0):null;
    }

    @Override
    public List<SupplierReqUsePerDay> selectByMap(Map map) {
        // TODO Auto-generated method stub
        return mapper.selectByMap(map);
    }
    
}
