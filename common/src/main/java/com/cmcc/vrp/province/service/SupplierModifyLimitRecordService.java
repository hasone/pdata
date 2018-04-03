package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.SupplierModifyLimitRecord;

/**
 * 供应商修改限额历史记录服务类
 * @author qinqinyan
 * */
public interface SupplierModifyLimitRecordService {

    /**
     * @title:insertSelective
     * */
    boolean insertSelective(SupplierModifyLimitRecord record);
}
