package com.cmcc.vrp.province.service;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.SupplierProdReqUsePerDay;

/**
 * 供应商产品每天充值额度服务类（以发送充值请求为统计依据）
 * @author qinqinyan
 * */
public interface SupplierProdReqUsePerDayService {
    /**
     * @title: insertSelective
     * */
    boolean insertSelective(SupplierProdReqUsePerDay record);

    /**
     * @title: selectByPrimaryKey
     * */
    SupplierProdReqUsePerDay selectByPrimaryKey(Long id);

    /**
     * @title: updateByPrimaryKeySelective
     * */
    boolean updateByPrimaryKeySelective(SupplierProdReqUsePerDay record);
    
    /**
     * @title: deleteByPrimaryKey
     * */
    boolean deleteByPrimaryKey(Long id);
    
    /**
     * @title: updateUsedMoney
     * */
    boolean updateUsedMoney(Long id, Double delta);
    
    /**
     * 获取当天统计记录
     * @param supplierId
     * @author qinqinyan
     * */
    SupplierProdReqUsePerDay getTodayRecord(Long supplierProductId);
    
    /**
     * selectByMap
     * */
    List<SupplierProdReqUsePerDay> selectByMap(Map map);
}
