package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.SupplierProdModifyLimitRecord;
/**
 * 供应商产品变更限额记录对象
 * @author qinqinyan
 * */
public interface SupplierProdModifyLimitRecordMapper {

    /**
     * @title: insertSelective
     * */
    int insertSelective(SupplierProdModifyLimitRecord record);

}