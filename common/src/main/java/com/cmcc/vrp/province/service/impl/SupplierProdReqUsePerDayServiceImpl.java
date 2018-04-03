package com.cmcc.vrp.province.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.SupplierProdReqUsePerDayMapper;
import com.cmcc.vrp.province.model.SupplierProdReqUsePerDay;
import com.cmcc.vrp.province.service.SupplierProdReqUsePerDayService;
import com.cmcc.vrp.util.DateUtil;

/**
 * 供应商产品每天充值额度服务类（以发送充值请求为统计依据）
 * @author qinqinyan
 * */
@Service("supplierProdReqUsePerDayService")
public class SupplierProdReqUsePerDayServiceImpl implements SupplierProdReqUsePerDayService{

    @Autowired
    SupplierProdReqUsePerDayMapper mapper;
    
    @Override
    public boolean insertSelective(SupplierProdReqUsePerDay record) {
        return mapper.insertSelective(record)==1;
    }

    @Override
    public SupplierProdReqUsePerDay selectByPrimaryKey(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateByPrimaryKeySelective(SupplierProdReqUsePerDay record) {
        record.setUpdateTime(new Date());
        return mapper.updateByPrimaryKeySelective(record)==1;
    }

    @Override
    public boolean deleteByPrimaryKey(Long id) {
        return mapper.deleteByPrimaryKey(id)==1;
    }

    @Override
    public boolean updateUsedMoney(Long id, Double delta) {
        return mapper.updateUsedMoney(id, delta)==1;
    }

    @Override
    public SupplierProdReqUsePerDay getTodayRecord(Long supplierProductId) {
        Map map = new HashMap<String, String>();
        map.put("supplierProductId", supplierProductId);
        Date startTime = DateUtil.getBeginOfDay(new Date());
        Date endTime = DateUtil.getEndTimeOfDate(new Date());
        map.put("startTime", DateUtil.dateToString(startTime, "yyyy-MM-dd HH:mm:ss"));
        map.put("endTime", DateUtil.dateToString(endTime, "yyyy-MM-dd HH:mm:ss"));
        List<SupplierProdReqUsePerDay> list = mapper.selectByMap(map);
        return list!=null&&list.size()>0?list.get(0):null;
    }

    @Override
    public List<SupplierProdReqUsePerDay> selectByMap(Map map) {
        return mapper.selectByMap(map);
    }
    
    
}
