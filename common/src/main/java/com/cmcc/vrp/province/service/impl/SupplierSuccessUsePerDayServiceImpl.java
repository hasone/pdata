package com.cmcc.vrp.province.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.cmcc.vrp.province.dao.SupplierSuccessUsePerDayMapper;
import com.cmcc.vrp.province.model.SupplierSuccessUsePerDay;
import com.cmcc.vrp.province.service.SupplierSuccessUsePerDayService;

/**
 * 统计供应商每天成功充值的金额服务类（以充值成功为统计依据）
 * 跑脚本进行统计，插入到相应表中
 * @author qinqinyan
 * */
public class SupplierSuccessUsePerDayServiceImpl implements SupplierSuccessUsePerDayService{

    @Autowired
    SupplierSuccessUsePerDayMapper mapper;
    
    @Override
    public List<SupplierSuccessUsePerDay> selectByMap(Map map) {
        // TODO Auto-generated method stub
        return mapper.selectByMap(map);
    }

}
