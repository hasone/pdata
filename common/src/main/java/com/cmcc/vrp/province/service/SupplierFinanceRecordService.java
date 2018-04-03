package com.cmcc.vrp.province.service;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.SupplierFinanceRecord;

/**
 * 供应商财务记录
 * @author qinqinyan
 * */
public interface SupplierFinanceRecordService {
    /**
     * @title:deleteByPrimaryKey
     * */
    boolean deleteByPrimaryKey(Long id);

    /**
     * @title:insert
     * */
    boolean insert(SupplierFinanceRecord record);

    /**
     * @title:insertSelective
     * */
    boolean insertSelective(SupplierFinanceRecord record);

    /**
     * @title:selectByPrimaryKey
     * */
    SupplierFinanceRecord selectByPrimaryKey(Long id);
    
    /**
     * @title:selectBySupplierId
     * 根据供应商id查找
     * @param supplierId
     * */
    SupplierFinanceRecord selectBySupplierId(Long supplierId);

    /**
     * @title:updateByPrimaryKeySelective
     * */
    boolean updateByPrimaryKeySelective(SupplierFinanceRecord record);

    /**
     * @title:updateByPrimaryKey
     * */
    boolean updateByPrimaryKey(SupplierFinanceRecord record);
    
    /**
     * @title:countSupplierFinanceRecords
     * 查询供应商记录
     * */
    int countSupplierFinanceRecords(Map map);
    
    /**
     * @title:countSupplierFinanceRecords
     * 查询供应商记录
     * */
    List<SupplierFinanceRecord> querySupplierFinanceRecords(Map map);
    
    /**
     * 根据供应商id删除财务记录
     * @param supplierId
     * @author qinqinyan
     * */
    boolean deleteBysupplierId(Long supplierId);
    
    /**
     * 批量更新
     * @author qinqinyan
     * */
    boolean batchUpdate(List<SupplierFinanceRecord> records);
    
    /**
     * 获取所有供应商财务记录
     * @author qinqinyan
     * */
    List<SupplierFinanceRecord> getAllSupplierFinanceRecords();
    
}
