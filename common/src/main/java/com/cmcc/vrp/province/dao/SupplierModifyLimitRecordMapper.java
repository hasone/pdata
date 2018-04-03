package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.SupplierModifyLimitRecord;

/**
 * 供应商修改限额历史记录mapper
 * @author qinqinyan
 * */
public interface SupplierModifyLimitRecordMapper {

    /**
     * @title:insertSelective
     * */
    int insertSelective(SupplierModifyLimitRecord record);

}