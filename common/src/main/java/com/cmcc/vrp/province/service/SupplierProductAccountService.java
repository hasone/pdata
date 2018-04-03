package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.SupplierProductAccount;

/**
 * @author lgk8023
 *
 */
public interface SupplierProductAccountService {
	
	/**
	 * 创建运营商产品账户信息
	 * 
	 * @param supplierProductId  运营商产品ID
	 * @param count   产品余额
	 * @return
	 */
    boolean createSupplierProductAccount(Long supplierProductId, Double count, Long entSyncListId);
		
	/**
	 * 更新运营商产品账户信息
	 * 
	 * @param supplierProductId  运营商产品ID
	 * @param count   产品余额
	 * @return
	 */
    boolean updateSupplierProductAccount(Long supplierProductId, Double count, Long entId);
	
	/**
	 * 根据运营商产品ID获取账户信息
	 * @param supplierProductId
	 * @return
	 */
    SupplierProductAccount getInfoBySupplierProductId(Long supplierProductId);
    
    List<SupplierProductAccount> getInfoByEntSyncListId(Long entSyncListId);
    
    SupplierProductAccount getById(Long id);
    
    /**
     * @param id
     * @param count
     * @return
     */
    boolean updateById(Long id, Double count);
    
	
}
