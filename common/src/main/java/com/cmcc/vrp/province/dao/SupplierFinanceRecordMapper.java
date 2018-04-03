package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.SupplierFinanceRecord;

/**
 * 供应商财务记录
 * @author qinqinyan
 * */
public interface SupplierFinanceRecordMapper {
    /**
     * @title:deleteByPrimaryKey
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @title:insert
     * */
    int insert(SupplierFinanceRecord record);

    /**
     * @title:insertSelective
     * */
    int insertSelective(SupplierFinanceRecord record);

    /**
     * @title:selectByPrimaryKey
     * */
    SupplierFinanceRecord selectByPrimaryKey(Long id);
    
    /**
     * @title:selectBySupplierId
     * */
    SupplierFinanceRecord selectBySupplierId(@Param("supplierId")Long supplierId);

    /**
     * @title:updateByPrimaryKeySelective
     * */
    int updateByPrimaryKeySelective(SupplierFinanceRecord record);

    /**
     * @title:updateByPrimaryKey
     * */
    int updateByPrimaryKey(SupplierFinanceRecord record);
    
    /**
     * @title:countSupplierFinanceRecords
     * */
    int countSupplierFinanceRecords(Map map);
    
    /**
     * @title:countSupplierFinanceRecords
     * 查询供应商记录
     * */
    List<SupplierFinanceRecord> querySupplierFinanceRecords(Map map);
    
    /**
     * @title:deleteBysupplierId
     * */
    int deleteBysupplierId(@Param("supplierId")Long supplierId);
    
    /**
     * @title:batchUpdate
     * */
    int batchUpdate(@Param("records")List<SupplierFinanceRecord> records);
    
    /**
     * @title: getAllSupplierFinanceRecords
     * */
    List<SupplierFinanceRecord> getAllSupplierFinanceRecords();
}