package com.cmcc.vrp.province.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.cmcc.vrp.province.dao.SupplierProdSuccessUsePerDayMapper;
import com.cmcc.vrp.province.model.SupplierProdSuccessUsePerDay;
import com.cmcc.vrp.province.service.SupplierProdSuccessUsePerDayService;

/**
 * 统计供应商产品每天成功充值服务类（以充值成功统计）
 * 跑脚本进行统计，插入到相应表中
 * @author qinqinyan
 * */
public class SupplierProdSuccessUsePerDayServiceImpl implements SupplierProdSuccessUsePerDayService{

    @Autowired
    SupplierProdSuccessUsePerDayMapper mapper;
    
    @Override
    public List<SupplierProdSuccessUsePerDay> selectByMap(Map map) {
        // TODO Auto-generated method stub
        return mapper.selectByMap(map);
    }

}
