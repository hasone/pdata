package com.cmcc.vrp.province.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.DiscountRecord;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.ShBossProduct;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductMap;
import com.cmcc.vrp.util.QueryObject;

/**
 * @author wujiamin
 */
public interface SupplierProductService {
    /**
     * 
     * @Title: insert 
     * @Description: TODO
     * @param product
     * @return
     * @return: boolean
     */
    boolean insert(SupplierProduct product);

    /**
     * 
     * @Title: deleteSupplierProduct 
     * @Description: 逻辑删除：删除产品的同时会删除平台产品和供应商产品的关系
     * @param productId
     * @return
     * @return: boolean
     */
    boolean deleteSupplierProduct(Long productId);

    /**
     * 
     * @Title: updateByPrimaryKey 
     * @Description: TODO
     * @param product
     * @return
     * @return: boolean
     */
    boolean updateByPrimaryKey(SupplierProduct product);

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
     * @Title: selectBySupplierId 
     * @Description: TODO
     * @param supplierId
     * @return
     * @return: List<SupplierProduct>
     */
    List<SupplierProduct> selectBySupplierId(Long supplierId);

    /**
     * 
     * @Title: selectByName 
     * @Description: TODO
     * @param name
     * @return
     * @return: List<SupplierProduct>
     */
    List<SupplierProduct> selectByName(String name);

    /**
     * 
     * @Title: selectByCode 
     * @Description: TODO
     * @param code
     * @return
     * @return: SupplierProduct
     */
    SupplierProduct selectByCode(String code);

    /**
     * 
     * @Title: selectByCodeAndSupplierId 
     * @Description: TODO
     * @param code
     * @param supplierId
     * @return
     * @return: SupplierProduct
     */
    SupplierProduct selectByCodeAndSupplierId(String code, Long supplierId);

    /**
     * 
     * @Title: queryPaginationSupplierProduct 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: List<SupplierProduct>
     */
    List<SupplierProduct> queryPaginationSupplierProduct(QueryObject queryObject);

    /** 
     * @Title: queryPaginationSupplierProductCount 
    */
    int queryPaginationSupplierProductCount(QueryObject queryObject);

    /**
     * 查找平台产品已关联的BOSS产品
     *
     * @param queryObject
     * @return
     * @date 2016年6月17日
     * @author wujiamin
     */
    List<SupplierProduct> queryPaginationSupplierProduct2PltProduct(QueryObject queryObject);

    /**
     * 
     * @Title: queryPaginationSupplierProduct2PltProductCount 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: int
     */
    int queryPaginationSupplierProduct2PltProductCount(QueryObject queryObject);

    /**
     * 查找目前可以对某平台产品进行关联的BOSS产品
     *
     * @param queryObject
     * @return
     * @date 2016年6月17日
     * @author wujiamin
     */
    int querySupplierProductAvailableCount(QueryObject queryObject);

    /**
     * 
     * @Title: querySupplierProductAvailable 
     * @Description: TODO
     * @param queryObject
     * @return
     * @return: List<SupplierProduct>
     */
    List<SupplierProduct> querySupplierProductAvailable(QueryObject queryObject);

    /**
     * @param feature
     * @return
     * @Title: selectByFeature
     * @Description: 根据供应商产品特征查询
     * @return: SupplierProduct
     */
    SupplierProduct selectByFeature(String feature);

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    boolean batchInsertSupplierProduct(List<SupplierProduct> list);

    /**
     * 通过创建时间查询
     *
     * @param createTime
     * @return
     */
    List<SupplierProduct> selectSupplierByCreateTime(Date createTime);

    /**
     * 
     * @Title: updateSupplierProduct 
     * @Description: 更新供应商产品信息
     * @param epList
     * @param spmList
     * @param pList
     * @param spList
     * @param records
     * @return
     * @return: boolean
     */
    boolean updateSupplierProduct(List<EntProduct> epList, List<SupplierProductMap> spmList, List<Product> pList,
            List<SupplierProduct> spList, List<DiscountRecord> records);

    /**
     * 
     * @Title: createOrUpdateSupplierProduct 
     * @Description: 产品更新操作
     * @param enterpriseCode
     * @param newProducts
     * @param updateProducts
     * @param operation
     * @return
     * @return: boolean
     */
    boolean createOrUpdateSupplierProduct(String enterpriseCode, List<SupplierProduct> newProducts,
            List<SupplierProduct> updateProducts);

    /**
     * 
     * @Title: batchUpdate 
     * @Description: 批量更新信息
     * @param list
     * @return
     * @return: boolean
     */
    boolean batchUpdate(List<SupplierProduct> list);

    /**
     * 
     * @Title: selectByNameAndCodeOrStatusOrDeleteFlag 
     * @Description: 通过产品名称、产品编码筛选供应商产品
     * @param name
     * @param code
     * @param status
     * @param deleteFlag
     * @return
     * @return: List<SupplierProduct>
     */
    List<SupplierProduct> selectByNameAndCodeOrStatusOrDeleteFlag(String name, String code, Integer status,
            Integer deleteFlag);

    /**
     * 
     * @Title: createNewSupplierProduct 
     * @Description: TODO
     * @param entId
     * @param newProduct
     * @param product
     * @return
     * @return: boolean
     */
    boolean createNewSupplierProduct(Long entId, SupplierProduct newProduct, Product product);

    /**
     * 
     * @Title: selectByNameWithOutDeleteFlag 
     * @Description: 通过名称查询
     * @param name
     * @return
     * @return: List<SupplierProduct>
     */
    List<SupplierProduct> selectByNameWithOutDeleteFlag(String name);

    /**
     * 
     * @Title: updateSulliperProduct 
     * @Description: 批量更新
     * @param entId
     * @param updateProducts
     * @return
     * @return: boolean
     */
    boolean updateSulliperProduct(Long entId, List<SupplierProduct> updateProducts);

    /**
     * 
     * @Title: createNewSupplierProduct 
     * @Description: 批量新增
     * @param entId
     * @param newProducts
     * @return
     * @return: boolean
     */
    boolean createNewSupplierProduct(Long entId, List<SupplierProduct> newProducts);

    /**
     * @param id
     * @return
     */
    SupplierProduct selectById(Long id);
    
    /**
     * 根据供应商ID删除供应商产品及供应商产品与平台产品的关系
     * @param supplierId
     * @author qinqinyan
     * */
    boolean deleteBysupplierId(Long supplierId);
    
    /**
     * 获取所有供应商产品
     * @author qinqinyan
     * */
    List<SupplierProduct> selectAllSupplierProducts();

    /**
     * 查找上架状态的供应商产品
     * @author qinqinyan
     * */
    SupplierProduct selectOnshelfByPrimaryKey(Long id);
    
    /**
     * 根据map查找
     * @author qinqinyan
     * */
    List<SupplierProduct> selectByMap(Map map);
    
    /**
     * 设置供应商产品限额
     * @author qinqinyan
     * */
    boolean setProductSupplierLimit(SupplierProduct supplierProduct, Long adminId);
    
    /**
     * 
     * @Title: getByNameOrCodeOrOpStatus 
     * @Description: 根据名称或编码或订单状态查询
     * @param name
     * @param opStatus
     * @return
     * @return: List<SupplierProduct>
     */
    public List<SupplierProduct> getByNameOrCodeOrOpStatus(String name, String code, Integer opStatus);
    
    public List<ShBossProduct> getShBossProducts();

    List<ShBossProduct> getShBossProductsByOrderType(String orderType);
}
