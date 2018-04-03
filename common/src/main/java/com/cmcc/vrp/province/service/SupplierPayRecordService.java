package com.cmcc.vrp.province.service;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.SupplierPayRecord;

/**
 * 供应商付款记录服务类
 * @author qinqinyan
 * */
public interface SupplierPayRecordService {
    /**
     * @title:deleteByPrimaryKey
     * */
    boolean deleteByPrimaryKey(Long id);

    /**
     * @title:insert
     * */
    boolean insert(SupplierPayRecord record);

    /**
     * @title:insertSelective
     * */
    boolean insertSelective(SupplierPayRecord record);

    /**
     * @title:selectByPrimaryKey
     * */
    SupplierPayRecord selectByPrimaryKey(Long id);

    /**
     * @title:updateByPrimaryKeySelective
     * */
    boolean updateByPrimaryKeySelective(SupplierPayRecord record);

    /**
     * @title:updateByPrimaryKey
     * */
    boolean updateByPrimaryKey(SupplierPayRecord record);
    
    /**
     * 获取供应商的付款记录
     * @param map
     * */
    int countSupplierPayRecords(Map map);
    /**
     * 获取供应商的付款记录
     * @param map
     * */
    List<SupplierPayRecord> querySupplierPayRecords(Map map);
  
    /**
     * 新建供应商付款记录
     * @param supplierPayRecord
     * @author qinqinyan
     * */
    boolean saveSupplierPayRecord(SupplierPayRecord supplierPayRecord);
    
    /**
     * @title:deleteBysupplierId
     * */
    boolean deleteBysupplierId(Long supplierId);
    
    /**
     * @title:deletePayRecord
     * */
    boolean deleteSupplierPayRecord(Long id);
}
