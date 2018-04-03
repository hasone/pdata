package com.cmcc.vrp.province.service;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.SupplierReqUsePerDay;

/**
 * 统计供应商每天发送充值请求的金额服务类（以成功发送请求充值为统计依据）
 * @author qinqinyan
 * */
public interface SupplierReqUsePerDayService {
    /**
     * @title:insertSelective
     * */
    boolean insertSelective(SupplierReqUsePerDay record);

    /**
     * @title:selectByPrimaryKey
     * */
    SupplierReqUsePerDay selectByPrimaryKey(Long id);

    /**
     * @title:updateByPrimaryKeySelective
     * */
    boolean updateByPrimaryKeySelective(SupplierReqUsePerDay record);
    
    /**
     * @title:deleteByPrimaryKey
     * */
    boolean deleteByPrimaryKey(Long id);
    
    /**
     * 批量删除
     * */
    boolean batchDelete(Map map);
    
    /**
     * 更新使用金钱
     * @param id
     * @param delta
     * */
    boolean updateUsedMoney(Long id, Double delta);
    
    /**
     * 获取当天统计记录
     * @param supplierId
     * @author qinqinyan
     * */
    SupplierReqUsePerDay getTodayRecord(Long supplierId);
    
    /**
     * selectByMap
     * */
    List<SupplierReqUsePerDay> selectByMap(Map map);
}
