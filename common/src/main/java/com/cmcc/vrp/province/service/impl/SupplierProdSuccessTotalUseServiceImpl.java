package com.cmcc.vrp.province.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.cmcc.vrp.province.dao.SupplierProdSuccessTotalUseMapper;
import com.cmcc.vrp.province.model.SupplierProdSuccessTotalUse;
import com.cmcc.vrp.province.service.SupplierProdSuccessTotalUseService;

/**
 * 供应商产品成功充值总额度服务类（以成功充值为统计依据）
 * 跑脚本进行统计，插入到相应表中
 * @author qinqinyan
 * */
public class SupplierProdSuccessTotalUseServiceImpl implements SupplierProdSuccessTotalUseService{

    @Autowired
    SupplierProdSuccessTotalUseMapper mapper;
    
    @Override
    public List<SupplierProdSuccessTotalUse> selectByMap(Map map) {
        // TODO Auto-generated method stub
        return mapper.selectByMap(map);
    }

}
