package com.cmcc.vrp.province.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.province.dao.SupplierPayRecordMapper;
import com.cmcc.vrp.province.model.SupplierFinanceRecord;
import com.cmcc.vrp.province.model.SupplierPayRecord;
import com.cmcc.vrp.province.service.SupplierFinanceRecordService;
import com.cmcc.vrp.province.service.SupplierPayRecordService;

/**
 * 供应商付款记录服务类
 * @author qinqinyan
 * */
@Service("supplierPayRecordService")
public class SupplierPayRecordServiceImpl implements SupplierPayRecordService{
    private static final Logger logger = LoggerFactory.getLogger(SupplierPayRecordServiceImpl.class);

    @Autowired
    SupplierPayRecordMapper mapper;
    @Autowired
    SupplierFinanceRecordService supplierFinanceRecordService;
    
    /**
     * deleteByPrimaryKey
     * */
    @Override
    public boolean deleteByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        if(id!=null){
            return mapper.deleteByPrimaryKey(id)>=0;
        }
        return false;
    }

    /**
     * insert
     * */
    @Override
    public boolean insert(SupplierPayRecord record) {
        // TODO Auto-generated method stub
        if(record!=null){
            return mapper.insert(record)==1;
        }
        return false;
    }

    /**
     * insertSelective
     * */
    @Override
    public boolean insertSelective(SupplierPayRecord record) {
        // TODO Auto-generated method stub
        if(record!=null){
            return mapper.insertSelective(record)==1;
        }
        return false;
    }

    /**
     * selectByPrimaryKey
     * */
    @Override
    public SupplierPayRecord selectByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        if(id!=null){
            return mapper.selectByPrimaryKey(id);
        }
        return null;
    }

    /**
     * updateByPrimaryKeySelective
     * */
    @Override
    public boolean updateByPrimaryKeySelective(SupplierPayRecord record) {
        // TODO Auto-generated method stub
        if(record!=null){
            return mapper.updateByPrimaryKeySelective(record)==1;
        }
        return false;
    }

    /**
     * updateByPrimaryKey
     * */
    @Override
    public boolean updateByPrimaryKey(SupplierPayRecord record) {
        // TODO Auto-generated method stub
        if(record!=null){
            return mapper.updateByPrimaryKey(record)==1;
        }
        return false;
    }

    /**
     * countSupplierPayRecords
     * */
    @Override
    public int countSupplierPayRecords(Map map) {
        // TODO Auto-generated method stub
        return mapper.countSupplierPayRecords(map);
    }
    
    /**
     * querySupplierPayRecords
     * */
    @Override
    public List<SupplierPayRecord> querySupplierPayRecords(Map map) {
        // TODO Auto-generated method stub
        return mapper.querySupplierPayRecords(map);
    }

    /**
     * 保存供应商付款记录
     * */
    @Override
    @Transactional
    public boolean saveSupplierPayRecord(SupplierPayRecord supplierPayRecord) {
        // TODO Auto-generated method stub
        if(!insertSelective(supplierPayRecord)){
            logger.info("插入供应商付款记录失败。"+JSON.toJSONString(supplierPayRecord));
            return false;
        }
        
        SupplierFinanceRecord supplierFinanceRecord = supplierFinanceRecordService.selectBySupplierId(supplierPayRecord.getSupplierId());
        supplierFinanceRecord.setTotalMoney(supplierFinanceRecord.getTotalMoney().doubleValue()+supplierPayRecord.getPayMoney().doubleValue());
        supplierFinanceRecord.setBalance(supplierFinanceRecord.getBalance().doubleValue()+supplierPayRecord.getPayMoney().doubleValue());
        supplierFinanceRecord.setUpdateTime(new Date());
        supplierFinanceRecord.setOperateTime(new Date());
        
        if(!supplierFinanceRecordService.updateByPrimaryKeySelective(supplierFinanceRecord)){
            logger.info("更新供应商财务记录失败。"+JSON.toJSONString(supplierFinanceRecord));
            throw new RuntimeException();
        }
        logger.info("用户= {} 新增供应商 = {} 付款记录 = {} 成功。", supplierPayRecord.getOperatorId(),
                supplierPayRecord.getSupplierId(), supplierPayRecord.getId());
        return true;
    }

    /**
     * 更据供应商id删除
     * */
    @Override
    public boolean deleteBysupplierId(Long supplierId) {
        // TODO Auto-generated method stub
        if(supplierId!=null){
            return mapper.deleteBysupplierId(supplierId)>=0;
        }
        return false;
    }

    /**
     * 废弃供应商付款记录
     * 1、逻辑删除
     * 2、扣将供应商总额
     * */
    @Override
    @Transactional
    public boolean deleteSupplierPayRecord(Long id) {
        // TODO Auto-generated method stub
        if(id!=null){
            SupplierPayRecord supplierPayRecord = selectByPrimaryKey(id); 
            if(supplierPayRecord!=null){
                if(!deleteByPrimaryKey(id)){
                    logger.info("删除供应商付款记录失败,id = "+id);
                    return false;
                }
                
                SupplierFinanceRecord supplierFinanceRecord = 
                        supplierFinanceRecordService.selectBySupplierId(supplierPayRecord.getSupplierId());
                SupplierFinanceRecord updateRecord = new SupplierFinanceRecord();
                updateRecord.setId(supplierFinanceRecord.getId());
                updateRecord.setSupplierId(supplierFinanceRecord.getSupplierId());
                updateRecord.setTotalMoney(supplierFinanceRecord.getTotalMoney().doubleValue()-supplierPayRecord.getPayMoney().doubleValue());
                updateRecord.setBalance(supplierFinanceRecord.getBalance().doubleValue()-supplierPayRecord.getPayMoney().doubleValue());
                updateRecord.setUpdateTime(new Date());
                updateRecord.setOperateTime(new Date());
                if(!supplierFinanceRecordService.updateByPrimaryKeySelective(updateRecord)){
                    logger.info("扣减供应商总额和余额失败,供应商id = {}, 扣减额度 = {}", 
                            supplierFinanceRecord.getSupplierId(), supplierPayRecord.getPayMoney().doubleValue());
                    throw new RuntimeException();
                }
                
                return true;
            }
        }
        return false;
    }
    
    
    
}
