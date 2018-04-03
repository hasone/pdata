package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.boss.shangdong.boss.model.UserOrder;
import com.cmcc.vrp.province.model.SupplierProduct;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: SupplierProductMapper 
 * @Description: TODO
 */
public interface SupplierProductMapper {
    /**
     * 
     * @Title: deleteByPrimaryKey 
     * @Description: TODO
     * @param id
     * @return
     * @return: int
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 
     * @Title: insert 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int insert(SupplierProduct record);

    /**
     * 
     * @Title: insertSelective 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int insertSelective(SupplierProduct record);

    /**
     * 
     * @Title: selectByPrimaryKey 
     * @Description: TODO
     * @param id
     * @return
     * @return: SupplierProduct
     */
    SupplierProduct selectByPrimaryKey(Long id);

    /**
     * 
     * @Title: updateByPrimaryKeySelective 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int updateByPrimaryKeySelective(SupplierProduct record);

    /**
     * 
     * @Title: updateByPrimaryKey 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int updateByPrimaryKey(SupplierProduct record);

    /**
     * 
     * @Title: selectBySupplierId 
     * @Description: TODO
     * @param supplierId
     * @return
     * @return: List<SupplierProduct>
     */
    List<SupplierProduct> selectBySupplierId(@Param("supplierId") Long supplierId);

    /**
     * 
     * @Title: selectByName 
     * @Description: TODO
     * @param name
     * @return
     * @return: List<SupplierProduct>
     */
    List<SupplierProduct> selectByName(@Param("name") String name);

    /**
     * 
     * @Title: selectByCode 
     * @Description: TODO
     * @param code
     * @return
     * @return: SupplierProduct
     */
    SupplierProduct selectByCode(@Param("code") String code);

    /**
     * 
     * @Title: selectByCodeAndSupplierId 
     * @Description: TODO
     * @param code
     * @param supplierId
     * @return
     * @return: SupplierProduct
     */
    SupplierProduct selectByCodeAndSupplierId(@Param("code") String code, @Param("supplierId") Long supplierId);

    /**
     * 
     * @Title: queryPaginationSupplierProduct 
     * @Description: TODO
     * @param map
     * @return
     * @return: List<SupplierProduct>
     */
    List<SupplierProduct> queryPaginationSupplierProduct(Map<String, Object> map);

    /**
     * 
     * @Title: queryPaginationSupplierProductCount 
     * @Description: TODO
     * @param map
     * @return
     * @return: int
     */
    int queryPaginationSupplierProductCount(Map<String, Object> map);

    /**
     * 
     * @Title: deleteBySupplierId 
     * @Description: TODO
     * @param id
     * @return
     * @return: int
     */
    int deleteBySupplierId(Long id);

    /**
     * 
     * @Title: queryPaginationSupplierProduct2PltProduct 
     * @Description: TODO
     * @param map
     * @return
     * @return: List<SupplierProduct>
     */
    List<SupplierProduct> queryPaginationSupplierProduct2PltProduct(
            Map<String, Object> map);

    /**
     * 
     * @Title: queryPaginationSupplierProduct2PltProductCount 
     * @Description: TODO
     * @param map
     * @return
     * @return: int
     */
    int queryPaginationSupplierProduct2PltProductCount(Map<String, Object> map);

    /**
     * 
     * @Title: querySupplierProductAvailableCount 
     * @Description: TODO
     * @param map
     * @return
     * @return: int
     */
    int querySupplierProductAvailableCount(Map<String, Object> map);

    /**
     * 
     * @Title: querySupplierProductAvailable 
     * @Description: TODO
     * @param map
     * @return
     * @return: List<SupplierProduct>
     */
    List<SupplierProduct> querySupplierProductAvailable(Map<String, Object> map);

    /**
     * 
     * @Title: selectByFeature 
     * @Description: TODO
     * @param feature
     * @return
     * @return: SupplierProduct
     */
    SupplierProduct selectByFeature(@Param("feature") String feature);

    /**
     * 
     * @Title: batchInsertSupplierProduct 
     * @Description: TODO
     * @param records
     * @return
     * @return: int
     */
    int batchInsertSupplierProduct(@Param("list") List<SupplierProduct> records);

    /**
     * 
     * @Title: selectSupplierByCreateTime 
     * @Description: TODO
     * @param createTime
     * @return
     * @return: List<SupplierProduct>
     */
    List<SupplierProduct> selectSupplierByCreateTime(@Param("createTime") Date createTime);
    
    /**
     * 山东专用，得到所有的企业订单
     */
    List<UserOrder> sdGetAllUserOrders();
    
    /**
     * 
     * @Title: batchUpdate 
     * @Description: TODO
     * @param list
     * @return
     * @return: int
     */
    int batchUpdate(@Param("list") List<SupplierProduct> list);
    
    /**
     * 
     * @Title: selectByNameAndCodeOrStatusOrDeleteFlag 
     * @Description: TODO
     * @param name
     * @param code
     * @param status
     * @param deleteFlag
     * @return
     * @return: List<SupplierProduct>
     */
    List<SupplierProduct> selectByNameAndCodeOrStatusOrDeleteFlag(@Param("name") String name,
            @Param("code") String code, @Param("status") Integer status, @Param("deleteFlag") Integer deleteFlag);
    
    /**
     * 
     * @Title: selectByNameWithoutDeleteFlag 
     * @Description: TODO
     * @param name
     * @return
     * @return: List<SupplierProduct>
     */
    List<SupplierProduct> selectByNameWithoutDeleteFlag(@Param("name") String name);

	/**
	 * @param id
	 * @return
	 */
    SupplierProduct selectById(@Param("id") Long id);
    
    /**
     * @Title selectAllSupplierProducts
     * @return
     */
    List<SupplierProduct> selectAllSupplierProducts();

    /**
     * @Title selectOnshelfByPrimaryKey
     * @return
     */
    SupplierProduct selectOnshelfByPrimaryKey(Long id);
    
    /**
     * @Title selectByMap
     * @return
     */
    List<SupplierProduct> selectByMap(Map map);
    
    /**
     * 
     * @Title: getByNameOrCodeOrOpStatus 
     * @Description: TODO
     * @param map
     * @return
     * @return: List<SupplierProduct>
     */
    List<SupplierProduct> getByNameOrCodeOrOpStatus(Map map);
        
}