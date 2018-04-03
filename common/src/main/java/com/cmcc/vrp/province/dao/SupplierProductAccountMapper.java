package com.cmcc.vrp.province.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.SupplierProductAccount;

/**
 * @author lgk8023
 * @date 2016年11月14日
 */
public interface SupplierProductAccountMapper {

	/**
	 * 创建供应商产品账户
	 * 
	 * @param supplierProductAccount
	 * @return
	 */
    int createSupplierProductAccount(SupplierProductAccount supplierProductAccount);

	/**
	 * 更新供应商产品账户
	 * 
	 * @param supplierProductId
	 * @param count
	 * @return
	 */
    int updateSupplierProductAccount(@Param("supplierProductId") Long supplierProductId, @Param("count") Double count);

    SupplierProductAccount getInfoBySupplierProductId(@Param("supplierProductId") Long supplierProductId);

    Double getCountBySupplierProductId(@Param("supplierProductId") Long supplierProductId);

    List<SupplierProductAccount> getInfoByEntSyncListId(@Param("entSyncListId")Long entSyncListId);
    
    SupplierProductAccount getById(@Param("id")Long id);

	/**
	 * @param id
	 * @param count
	 * @return
	 */
    boolean updateById(@Param("id") Long id, @Param("count") Double count);

}
