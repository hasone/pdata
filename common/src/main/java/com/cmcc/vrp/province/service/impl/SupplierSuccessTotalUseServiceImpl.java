package com.cmcc.vrp.province.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.SupplierSuccessTotalUseMapper;
import com.cmcc.vrp.province.model.SupplierSuccessTotalUse;
import com.cmcc.vrp.province.service.SupplierSuccessTotalUseService;
/**
 * 统计供应商成功充值的金额服务类（以充值成功为统计依据）
 * 跑脚本进行统计，插入到相应表中
 * @author qinqinyan
 * */
@Service("supplierSuccessTotalUseService")
public class SupplierSuccessTotalUseServiceImpl implements SupplierSuccessTotalUseService{

    @Autowired
    SupplierSuccessTotalUseMapper mapper;
    @Override
    public List<SupplierSuccessTotalUse> selectByMap(Map map) {
        // TODO Auto-generated method stub
        return mapper.selectByMap(map);
    }
    @Override
    public List<SupplierSuccessTotalUse> selectBySupplierId(Long supplierId) {
        // TODO Auto-generated method stub
        return mapper.selectBySupplierId(supplierId);
    }
    @Override
    public List<SupplierSuccessTotalUse> getAllSupplierSuccessTotalUseRecords() {
        // TODO Auto-generated method stub
        return mapper.getAllSupplierSuccessTotalUseRecords();
    }
}
