package com.cmcc.vrp.province.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.ChargeRecordMapper;
import com.cmcc.vrp.province.model.EntBillRecord;
import com.cmcc.vrp.province.service.EntBillService;
import com.cmcc.vrp.util.QueryObject;

/**
 * EntBillServiceImpl.java
 * @author wujiamin
 * @date 2016年12月29日
 */
@Service("entBillService")
public class EntBillServiceImpl implements EntBillService{
    @Autowired
    ChargeRecordMapper chargeRecordMapper;

    @Override
    public List<EntBillRecord> showPageList(QueryObject queryObject) {
        return chargeRecordMapper.selectEntBillRecordByMap(queryObject.toMap());
    }

    @Override
    public int showPageCount(QueryObject queryObject) {
        return chargeRecordMapper.countEntBillRecordByMap(queryObject.toMap());
    }

    @Override
    public Double sumEntBillPriceByMap(Map map) {
        Long price = chargeRecordMapper.sumEntBillPriceByMap(map);
        if(price != null){
            return price/100.0;
        }
        return 0d;
    }

}
