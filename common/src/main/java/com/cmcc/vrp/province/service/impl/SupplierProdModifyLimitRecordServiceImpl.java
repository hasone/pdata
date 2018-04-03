package com.cmcc.vrp.province.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.SupplierProdModifyLimitRecordMapper;
import com.cmcc.vrp.province.model.SupplierProdModifyLimitRecord;
import com.cmcc.vrp.province.service.SupplierProdModifyLimitRecordService;
/**
 * 供应商产品变更限额记录服务类
 * @author qinqinyan
 * */
@Service("supplierProdModifyLimitRecordService")
public class SupplierProdModifyLimitRecordServiceImpl implements SupplierProdModifyLimitRecordService{

    @Autowired
    SupplierProdModifyLimitRecordMapper mapper;
    
    @Override
    public boolean insertSelective(SupplierProdModifyLimitRecord record) {
        // TODO Auto-generated method stub
        return mapper.insertSelective(record)==1;
    }

}
