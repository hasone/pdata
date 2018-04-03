package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.SupplierProdSuccessUsePerDay;
/**
 * 统计供应商产品每天成功充值mapper（以充值成功统计）
 * 跑脚本进行统计，插入到相应表中
 * @author qinqinyan
 * */
public interface SupplierProdSuccessUsePerDayMapper {
    /**
     * @title: selectByMap
     * */
    List<SupplierProdSuccessUsePerDay> selectByMap(Map map);
}