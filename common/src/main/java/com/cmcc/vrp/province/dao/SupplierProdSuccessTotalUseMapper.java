package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.SupplierProdSuccessTotalUse;
/**
 * 供应商产品成功充值总额度mapper（以成功充值为统计依据）
 * 跑脚本进行统计，插入到相应表中
 * @author qinqinyan
 * */
public interface SupplierProdSuccessTotalUseMapper {
    /**
     * @title: selectByMap
     * */
    List<SupplierProdSuccessTotalUse> selectByMap(Map map);
}