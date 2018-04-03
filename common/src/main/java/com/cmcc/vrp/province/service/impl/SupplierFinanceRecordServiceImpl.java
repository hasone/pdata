package com.cmcc.vrp.province.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.SupplierFinanceRecordMapper;
import com.cmcc.vrp.province.model.SupplierFinanceRecord;
import com.cmcc.vrp.province.service.SupplierFinanceRecordService;
/**
 * 供应商财务记录类
 * @author qinqinyan
 * */
@Service("supplierFinanceRecordService")
public class SupplierFinanceRecordServiceImpl implements SupplierFinanceRecordService{

    @Autowired
    SupplierFinanceRecordMapper mapper;
    
    @Override
    public boolean deleteByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        if(id!=null){
            return mapper.deleteByPrimaryKey(id)>=0;
        }
        return false;
    }

    @Override
    public boolean insert(SupplierFinanceRecord record) {
        // TODO Auto-generated method stub
        if(record!=null){
            return mapper.insert(record)==1;
        }
        return false;
    }

    @Override
    public boolean insertSelective(SupplierFinanceRecord record) {
        // TODO Auto-generated method stub
        if(record!=null){
            return mapper.insertSelective(record)==1;
        }
        return false;
    }

    @Override
    public SupplierFinanceRecord selectByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        if(id!=null){
            return mapper.selectByPrimaryKey(id);
        }
        return null;
    }

    @Override
    public SupplierFinanceRecord selectBySupplierId(Long supplierId) {
        // TODO Auto-generated method stub
        if(supplierId!=null){
            return mapper.selectBySupplierId(supplierId);
        }
        return null;
    }

    @Override
    public boolean updateByPrimaryKeySelective(SupplierFinanceRecord record) {
        // TODO Auto-generated method stub
        if(record!=null){
            return mapper.updateByPrimaryKeySelective(record)>=0;
        }
        return false;
    }

    @Override
    public boolean updateByPrimaryKey(SupplierFinanceRecord record) {
        // TODO Auto-generated method stub
        if(record!=null){
            return mapper.updateByPrimaryKey(record)>=0;
        }
        return false;
    }

    @Override
    public int countSupplierFinanceRecords(Map map) {
        // TODO Auto-generated method stub
        return mapper.countSupplierFinanceRecords(map);
    }

    @Override
    public List<SupplierFinanceRecord> querySupplierFinanceRecords(Map map) {
        // TODO Auto-generated method stub
        return mapper.querySupplierFinanceRecords(map);
    }

    @Override
    public boolean deleteBysupplierId(Long supplierId) {
        // TODO Auto-generated method stub
        return mapper.deleteBysupplierId(supplierId)>=0;
    }

    @Override
    public boolean batchUpdate(List<SupplierFinanceRecord> records) {
        // TODO Auto-generated method stub
        return mapper.batchUpdate(records)>=0;
    }

    @Override
    public List<SupplierFinanceRecord> getAllSupplierFinanceRecords() {
        // TODO Auto-generated method stub
        return mapper.getAllSupplierFinanceRecords();
    }

    
}
