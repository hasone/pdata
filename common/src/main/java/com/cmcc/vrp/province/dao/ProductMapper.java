package com.cmcc.vrp.province.dao;


import com.cmcc.vrp.province.model.PrizeInfo;
import com.cmcc.vrp.province.model.Product;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: ProductMapper
 * @Description: TODO
 */
public interface ProductMapper {

    /**
     * @param id
     * @return
     * @Title: deleteByPrimaryKey
     * @Description: TODO
     * @return: int
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     * @Title: insert
     * @Description: TODO
     * @return: int
     */
    int insert(Product record);

    /**
     * @param record
     * @return
     * @Title: insertSelective
     * @Description: TODO
     * @return: int
     */
    int insertSelective(Product record);

    /**
     * @param id
     * @return
     * @Title: selectByPrimaryKey
     * @Description: TODO
     * @return: Product
     */
    Product selectByPrimaryKey(Long id);

    /**
     * @param code
     * @return
     * @Title: selectByCode
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> selectByCode(String code);

    /**
     * @param record
     * @return
     * @Title: updateByPrimaryKeySelective
     * @Description: TODO
     * @return: int
     */
    int updateByPrimaryKeySelective(Product record);

    /**
     * @param record
     * @return
     * @Title: updateByPrimaryKey
     * @Description: TODO
     * @return: int
     */
    int updateByPrimaryKey(Product record);


    /**
     * 查询所有产品
     *
     * @return
     * @throws
     * @Title:selectAll
     * @Description: TODO
     * @author: hexinxu
     */
    List<Product> selectAll();

    /**
     * @return
     * @Title: selectDistinctProSize
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> selectDistinctProSize();

    /**
     * @param params
     * @return
     * @throws
     * @Title:getProductList
     * @Description: 获取产品列表
     * @author: xuwanlin
     */
    List<Product> getProductList(Map<String, Object> params);

    /**
     * @return
     * @throws
     * @Title:getProductCount
     * @Description: 根据查询条件返回相应的记录数
     * @author: xuwanlin
     */
    int getProductCount(Map<String, Object> queryCriteria);

    /**
     * @return
     * @throws
     * @Title:selectAllProducts
     * @Description: TODO
     * @author: xuwanlin
     */
    List<Product> selectAllProducts();

    /**
     * @param map
     * @return
     * @Title: getProListByProSizeEnterId
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> getProListByProSizeEnterId(Map<String, Object> map);

    /**
     * 根据enterpriseId查找相关联产品，这个方法查找出来的都是上架的平台产品
     * @param enterId
     * @return
     * @Title: selectAllProductsByEnterId
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> selectAllProductsByEnterId(Long enterId);

    /**
     * 根据enterpriseId查找相关联产品，这个方法查找出来的所有产品（包括上架和下架）
     * @param enterpriseId
     * @return
     * @Title: selectProductsByEnterId
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> selectProductsByEnterId(@Param("enterId") Long enterpriseId);

    /**
     * 根据产品模板id获取所有产品（上架和下架）
     * @param productTemplateId
     * @return
     * @Title: selectByProductTemplateId
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> selectByProductTemplateId(@Param("productTemplateId") Long productTemplateId);

    /**
     * @param enterId
     * @return
     * @Title: selectAllProductsByMdrcEnterId
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> selectAllProductsByMdrcEnterId(Long enterId);

    /**
     * @param productName
     * @return
     * @throws
     * @Title:selectByProductName
     * @Description: TODO
     * @author: xuwanlin
     */
    Product selectByProductName(String productName);

    /**
     * @param productCode
     * @return
     * @throws
     * @Title:selectByProductCode
     * @Description: TODO
     * @author: qihang
     */
    Product selectByProductCode(String productCode);

    /**
     * @param id
     * @return
     * @throws
     * @Title:selectPrdSizeById
     * @Description: 根据产品的ID获取产品的大小
     * @author: xuwanlin
     */
    Long selectPrdSizeById(Long id);

    /**
     * 根据企业编码查询所有的产品信息
     *
     * @param enterpriseCode
     * @return
     */
    List<Product> selectAllProductsByEnterCode(String enterpriseCode);

    /**
     * @return
     * @Title: selectAllProductsON
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> selectAllProductsON();

    /**
     * @param prdCodes
     * @return
     * @Title: batchSelectProductsByProductCodes
     * @Description: 批量地根据产品编码获取相应的产品信息
     * @return: List<Product>
     */
    List<Product> batchSelectProductsByProductCodes(@Param("prdCodes") Set<String> prdCodes);

    /**
     * @return
     * @Title: getCurrencyProduct
     * @Description: 获取现金产品
     * @return: Product
     */
    Product getCurrencyProduct();

    /**
     * @return
     * @Title: getFlowProduct
     * @Description: 获取流量池产品
     * @return: Product
     */
    Product getFlowProduct();

    /**
     * @return
     * @Title: selectDefaultProduct
     * @Description: 获取企业创建时要默认添加的产品
     * @return: List<Product>
     */
    List<Product> selectDefaultProductByCustomerType(Long productCustomerType);

    /**
     * @param map
     * @return
     * @Title: getProducts
     * @Description: 创建活动获取产品（包括账户产品数量）
     * @return: List<Product>
     */
    List<Product> getProducts(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @Title: getProductListForEnter
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> getProductListForEnter(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @Title: getProductCountForEnter
     * @Description: TODO
     * @return: int
     */
    int getProductCountForEnter(Map<String, Object> map);

    /**
     * @param entId
     * @return
     * @Title: getProductListInAccount
     * @Description: 获取企业账户产品列表
     * @return: List<Product>
     */
    List<Product> getProductListInAccount(Long entId);

    /**
     * @param products
     * @return
     * @Title: insertBatchProduct
     * @Description: TODO
     * @return: int
     */
    int insertBatchProduct(@Param("ProductList") List<Product> products);

    /**
     * @param productIds
     * @return
     * @Title: deleteBatchProduct
     * @Description: TODO
     * @return: int
     */
    int deleteBatchProduct(@Param("productIds") List<Long> productIds);

    /**
     * @param products
     * @return
     * @Title: udpateBatchProduct
     * @Description: TODO
     * @return: int
     */
    int udpateBatchProduct(List<Product> products);

    /**
     * @param productIds
     * @return
     * @Title: selectBatchByIds
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> selectBatchByIds(@Param("ProductIds") List<Long> productIds);

    /**
     * @param codes
     * @return
     * @Title: selectBatchByCodes
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> selectBatchByCodes(@Param("Codes") List<String> codes);

    /**
     * @param map
     * @return
     * @Title: getProductListFromEnterAccount
     * @Description: 查询企业订购信息
     * @return: List<Product>
     */
    List<Product> getProductListFromEnterAccount(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @Title: getProductCountFromEnterAccount
     * @Description: TODO
     * @return: int
     */
    int getProductCountFromEnterAccount(Map<String, Object> map);

    /**
     * @param prizes
     * @return
     * @Title: selectProductsByPrizes
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> selectProductsByPrizes(@Param("prizeList") List<PrizeInfo> prizes);

    /**
     * @param records
     * @return
     * @Title: batchInsertProduct
     * @Description: TODO
     * @return: int
     */
    int batchInsertProduct(@Param("list") List<Product> records);

    /**
     * @param createTime
     * @return
     * @Title: selectProductByCreateTime
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> selectProductByCreateTime(@Param("createTime") Date createTime);

    /**
     * @param list
     * @return
     * @Title: batchUpdate
     * @Description: TODO
     * @return: int
     */
    int batchUpdate(@Param("list") List<Product> list);

    /**
     * @param productId
     * @return
     * @Title: getProductsByPidWithDeleteFlag
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> getProductsByPidWithDeleteFlag(@Param("productId") Long productId);

    /**
     * @Title: selectProductByProductSize
     */
    List<Product> selectProductByProductSize(@Param("productSize") String productSize);

    /**
     * @Title: selectFlowAccountProductByProductSize
     */
    Product selectFlowAccountProductByProductSize(@Param("productSize") Long productSize, @Param("productId") Long productId);

    /**
     * @param productSize
     * @param productId
     * @return
     */
    Product selectPrdBySizeAndId(@Param("productSize") Long productSize, @Param("productId") Long productId);
    
    /**
     * 根据类型得到产品列表（原产品，非转化产品），diffId为不在产品产品编码里的Id，可为空
     * entId 为关联到的企业产品+现金产品，可为空
     * 现在是按type排序 desc,如果前台有要求，再进行修改
     * 
     * @param diffId
     * @param types
     * @return
     */
    List<Product> getPrdsByType(@Param("diffId") Long diffId , @Param("types") List<Integer> types , @Param("entId") Long entId);
    
    /**
     * 根据类型查询，分页
     */
    List<Product> getPrdsByTypePageList(Map<String, Object> map);
    
    /**
     * 根据类型查询，分页
     */
    int getPrdsByTypePageCount(Map<String, Object> map);
    
    /**
     * 营销活动：账户不为空的平台调用这个
     * */
    List<Product> getProductsWhenAccountIsNotEmpty(Map map);
    /**
     * 营销活动：账户不为空的平台调用这个
     * */
    int countProductsWhenAccountIsNotEmpty(Map map);
    
    /**
     * 根据supplierId获取平台产品
     * */
    List<Product> getPlatFormBySupplierId(@Param("supplierId")Long supplierId);
    
    /**
     * 根据模板id获取产品（分页显示）
     * @param map
     * @author qinqinyan
     * */
    List<Product> listProductsByTemplateId(Map map);
    
    /**
     * 根据模板id获取产品（分页显示）
     * @param map
     * @author qinqinyan
     * */
    int countProductsByTemplate(Map map);
    
    /**
     * 根据模板id获取产品（分页显示）,不包括已经跟模板id关联的产品
     * @param map
     * @author qinqinyan
     * */
    List<Product> getProductListAvailable(Map map);
    /**
     * 根据模板id获取产品（分页显示）,不包括已经跟模板id关联的产品
     * @param map
     * @author qinqinyan
     * */
    int countProductListAvailable(Map map);
    
    /**
     * 
     * @Title: getBySplIdWithoutDel 
     * @Description: TODO
     * @param spid
     * @return
     * @return: List<Product>
     */
    List<Product> getBySplIdWithoutDel(Long spid);

    /**
     * 获取上海产品信息
     * @param oriderListId
     * @return
     */
    List<Product> getProductsByOrderListId(Long oriderListId);

    /**
     * @param map
     * @return
     */
    int showForPageResultCount(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    List<Product> showForPageResultList(Map<String, Object> map);

    List<Product> getOrderProductByMap(Map map);
    
    /**
     * 
     * @Title: selectByType 
     * @Description: 根据产品类型搜索
     * @param type
     * @return
     * @return: List<Product>
     */
    List<Product> selectByType(Integer type);
}