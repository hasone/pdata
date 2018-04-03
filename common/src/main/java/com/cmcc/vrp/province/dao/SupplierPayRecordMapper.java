package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.SupplierPayRecord;
/**
 * 供应商付款记录
 * @author qinqinyan
 * */
public interface SupplierPayRecordMapper {
    /**
     * @title:deleteByPrimaryKey
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @title:insert
     * */
    int insert(SupplierPayRecord record);

    /**
     * @title:insertSelective
     * */
    int insertSelective(SupplierPayRecord record);

    /**
     * @title:selectByPrimaryKey
     * */
    SupplierPayRecord selectByPrimaryKey(Long id);

    /**
     * @title:updateByPrimaryKeySelective
     * */
    int updateByPrimaryKeySelective(SupplierPayRecord record);

    /**
     * @title:updateByPrimaryKey
     * */
    int updateByPrimaryKey(SupplierPayRecord record);
    
    /**
     * @title:countSupplierPayRecords
     * */
    int countSupplierPayRecords(Map map);
    
    /**
     * @title:querySupplierPayRecords
     * */
    List<SupplierPayRecord> querySupplierPayRecords(Map map);
    
    /**
     * @title:deleteBysupplierId
     * */
    int deleteBysupplierId(@Param("supplierId")Long supplierId);
}