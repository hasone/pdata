package com.cmcc.vrp.province.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.dao.AdminChangeOperatorMapper;
import com.cmcc.vrp.province.model.AdminChangeOperator;
import com.cmcc.vrp.province.service.AdminChangeOperatorService;

/**
 * adminChangeOperatorServiceImpl
 */
@Service("adminChangeOperatorServiceImpl")
public class AdminChangeOperatorServiceImpl implements
        AdminChangeOperatorService {

    @Autowired
    AdminChangeOperatorMapper adminChangeOperatorMapper;
    
    /**
     * 通过adminId得到保存的记录
     */
    @Override
    public List<AdminChangeOperator> getByAdminId(Long adminId) {
        return adminChangeOperatorMapper.getAdminChangeRecordByAdminId(adminId);
    }

    /**
     * 通过adminId删除保存的记录
     */
    @Override
    public boolean deleteByAdminId(Long adminId) {
        return adminChangeOperatorMapper.deleteAdminChangeRecordByAdminId(adminId)>0;
    }

    /**
     * 插入记录
     */
    @Override
    public boolean insert(AdminChangeOperator adminChangeOperator) {
        return adminChangeOperatorMapper.insertSelective(adminChangeOperator)>0;
    }

    /**
     * 先删除旧的记录，再增加新的记录，都按adminId
     */
    @Override
    @Transactional
    public boolean insertAndDelByAdminId(AdminChangeOperator adminChangeOperator) {
        if(adminChangeOperator.getAdminId() == null){
            return false;
        }
        deleteByAdminId(adminChangeOperator.getAdminId());
        if(!insert(adminChangeOperator)){
            throw new TransactionException();
        }
        return true;
    }

}
