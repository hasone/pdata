package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.SupplierProdModifyLimitRecord;

/**
 * 供应商产品变更限额记录服务类
 * @author qinqinyan
 * */
public interface SupplierProdModifyLimitRecordService {

    /**
     * @title: insertSelective
     * */
    boolean insertSelective(SupplierProdModifyLimitRecord record);
}
