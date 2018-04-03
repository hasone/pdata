package com.cmcc.vrp.province.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.SupplierModifyLimitRecordMapper;
import com.cmcc.vrp.province.model.SupplierModifyLimitRecord;
import com.cmcc.vrp.province.service.SupplierModifyLimitRecordService;

/**
 * 供应商修改限额历史记录服务类
 * @author qinqinyan
 * */
@Service("supplierModifyLimitRecordService")
public class SupplierModifyLimitRecordServiceImpl implements SupplierModifyLimitRecordService{

    @Autowired
    SupplierModifyLimitRecordMapper mapper;
    
    @Override
    public boolean insertSelective(SupplierModifyLimitRecord record) {
        // TODO Auto-generated method stub
        return mapper.insertSelective(record)==1;
    }

}
