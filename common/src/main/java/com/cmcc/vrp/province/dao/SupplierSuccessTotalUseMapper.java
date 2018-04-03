package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.SupplierSuccessTotalUse;
/**
 * 统计供应商成功充值的金额mapper（以充值成功为统计依据）
 * 跑脚本进行统计，插入到相应表中
 * @author qinqinyan
 * */
public interface SupplierSuccessTotalUseMapper {

    /**
     * @title: selectByMap
     * */
    List<SupplierSuccessTotalUse> selectByMap(Map map);
    
    /**
     * @title: selectBySupplierId
     * */
    List<SupplierSuccessTotalUse> selectBySupplierId(@Param("supplierId")Long supplierId);
    
    /**
     * @title: getAllSupplierSuccessTotalUseRecords
     * */
    List<SupplierSuccessTotalUse> getAllSupplierSuccessTotalUseRecords();

}