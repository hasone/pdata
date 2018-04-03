package com.cmcc.vrp.province.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.dao.SupplierProductAccountMapper;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SupplierProductAccount;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.SupplierProductAccountService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.Constants;
/**
 * @author lgk8023
 *
 */
@Service("supplierProductAccountService")
public class SupplierProductAccountServiceImpl implements SupplierProductAccountService {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierProductAccountServiceImpl.class);
	 
    @Autowired
	SupplierProductService supplierProductService;
	
    @Autowired
	SupplierProductAccountMapper supplierProductAccountMapper;
	
    @Autowired
	AccountService accountService;
	
    @Autowired
	SupplierProductMapService supplierProductMapService;
	
    @Autowired
	EnterprisesService enterpriseService;

    @Override
	public boolean createSupplierProductAccount(Long supplierProductId, Double count, Long entSyncListId) {
        if (supplierProductId == null
				|| count == null
				|| entSyncListId == null) {
            LOGGER.error("无效的参数, 创建运营商产品账户失败");
            return false;
        }
		
        if (supplierProductService.selectByPrimaryKey(supplierProductId) == null) {
            LOGGER.error("运营商产品ID不存在，创建运营商产品帐户信息失败. supplierProductId = {}. ", supplierProductId);
            return false;
        }
        SupplierProductAccount supplierProductAccount = new SupplierProductAccount();
        supplierProductAccount.setSupplierProductId(supplierProductId);
        supplierProductAccount.setEntSyncListId(entSyncListId);
        supplierProductAccount.setCount(count);
        supplierProductAccount.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        return supplierProductAccountMapper.createSupplierProductAccount(supplierProductAccount) > 0;       
        
    }
    
    @Transactional
    @Override
	public boolean updateSupplierProductAccount(Long supplierProductId, Double count, Long entId) {
    	if (supplierProductId == null
				|| count == null
				|| entId == null) {
    	    LOGGER.error("无效的参数, 更新运营商产品账户失败");
    	    return false;
    	}

        if (supplierProductService.selectByPrimaryKey(supplierProductId) == null) {
            LOGGER.error("运营商产品ID不存在，更新运营商产品帐户信息失败. supplierProductId = {}. ", supplierProductId);
            return false;
        }
        Double before = supplierProductAccountMapper.getInfoBySupplierProductId(supplierProductId).getCount();
        Double delta = count - before;
        if (!(supplierProductAccountMapper.updateSupplierProductAccount(supplierProductId, count) > 0)) {
            throw new TransactionException("更新失败");
        } 
        
        //更新account
        List<Product> products = supplierProductMapService.getBySplPid(supplierProductId);
        for (Product product:products) {
            Long productId = product.getId();
            if (delta > 0) {
            	String serialNum = SerialNumGenerator.buildSerialNum();
            	if (!accountService.addCount(entId, productId, AccountType.ENTERPRISE, delta, serialNum, "同步BOSS侧余额, 增加平台账户余额.")) {
            	    LOGGER.error("增加平台侧账户余额失败. enterId = {}; productId = {}; serialNum = {}", entId, productId, serialNum);
            	    throw new TransactionException("增加平台侧账户余额失败");
            	}
            } else {
            	String serialNum = SerialNumGenerator.buildSerialNum();
            	if (!accountService.minusCount(entId, productId, AccountType.ENTERPRISE, -delta, serialNum, "同步BOSS侧余额, 扣减平台账户余额.")) {
            	    LOGGER.error("扣减平台侧账户余额失败. enterId = {}; productId = {}; serialNum = {}", entId, productId, serialNum);
            	    throw new TransactionException("扣减平台侧账户余额失败");
            	}
            }
        }
        
        return true;
    }

    @Override
	public SupplierProductAccount getInfoBySupplierProductId(Long supplierProductId) {
        if (supplierProductId == null) {
            LOGGER.error("无效的参数, 获取产品余额失败");
            return null;
        }
		
        if (supplierProductService.selectById(supplierProductId) == null) {
            LOGGER.error("运营商产品ID不存在，获取产品余额失败. supplierProductId = {}. ", supplierProductId);
            return null;
        }
        
        return supplierProductAccountMapper.getInfoBySupplierProductId(supplierProductId);
    }

    @Override
	public List<SupplierProductAccount> getInfoByEntSyncListId(Long entSyncListId) {
    	if (entSyncListId == null) {
    	    LOGGER.error("无效的参数, 获取产品余额信息失败");
    	    return null;
    	}
    	return supplierProductAccountMapper.getInfoByEntSyncListId(entSyncListId);
    }

    @Override
	public SupplierProductAccount getById(Long id) {
    	return supplierProductAccountMapper.getById(id);
    }

    @Override
	public boolean updateById(Long id, Double count) {
    	return supplierProductAccountMapper.updateById(id, count);
    }
}
