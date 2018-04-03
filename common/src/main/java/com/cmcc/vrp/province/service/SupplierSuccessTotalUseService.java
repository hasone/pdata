package com.cmcc.vrp.province.service;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.SupplierSuccessTotalUse;

/**
 * 统计供应商成功充值的金额服务类（以充值成功为统计依据）
 * 跑脚本进行统计，插入到相应表中
 * @author qinqinyan
 * */
public interface SupplierSuccessTotalUseService {
    /**
     * @title: selectByMap
     * */
    List<SupplierSuccessTotalUse> selectByMap(Map map);
    
    /**
     * 根据供应商id查找
     * @param supplierId
     * */
    List<SupplierSuccessTotalUse> selectBySupplierId(Long supplierId);
    
    /**
     * 获取所有记录
     * */
    List<SupplierSuccessTotalUse> getAllSupplierSuccessTotalUseRecords();
}
