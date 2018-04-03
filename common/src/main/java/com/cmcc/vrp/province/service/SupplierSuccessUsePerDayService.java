package com.cmcc.vrp.province.service;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.SupplierSuccessUsePerDay;

/**
 * 统计供应商每天成功充值的金额服务类（以充值成功为统计依据）
 * 跑脚本进行统计，插入到相应表中
 * @author qinqinyan
 * */
public interface SupplierSuccessUsePerDayService {
    /**
     * @title: selectByMap
     * */
    List<SupplierSuccessUsePerDay> selectByMap(Map map);
}
