
package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.PrizeInfo;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.util.QueryObject;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @version V1.0
 * @Title: ProductService.java
 * @Package com.cmcc.vrp.province.service
 * @author: sunyiwei
 * @date: 2015年4月27日 上午9:55:25
 */
public interface ProductService {
    /**
     * @param queryObject
     * @return
     * @throws
     * @Title:list
     * @Description: 获取产品列表
     * @author: sunyiwei
     */
    List<Product> list(QueryObject queryObject);

    /**
     * @param queryObject
     * @return
     * @throws
     * @Title:queryCount
     * @Description: 得到满足条件的记录条数
     * @author: sunyiwei
     */
    int getProductCount(QueryObject queryObject);

    /**
     * @param id
     * @return
     * @throws
     * @Title:delete
     * @Description: 删除产品
     * @author: sunyiwei
     */
    boolean delete(Long id);

    /**
     * @param id
     * @return
     * @throws
     * @Title:selectProductById
     * @Description: 根据产品Id查找产品
     * @author: xuwanlin
     */
    Product selectProductById(Long id);


    /**
     * @param code
     * @return
     * @throws
     * @Title:selectProductById
     * @Description: 根据产品编码查找产品
     * @author: qihang
     */
    Product selectByCode(String code);

    /**
     * @param product
     * @return
     * @throws
     * @Title:insertProduct
     * @Description: 插入产品
     * @author: xuwanlin
     */
    boolean insertProduct(Product product);

    /**
     * @param product
     * @return
     * @throws
     * @Title:updateProduct
     * @Description: 更新产品
     * @author: xuwanlin
     */
    boolean updateProduct(Product product);

    /**
     * @param product
     * @return
     * @throws
     * @Title:checkUnique
     * @Description: 检查产品唯一性
     * @author: xuwanlin
     */
    boolean checkUnique(Product product);

    /**
     * @param product
     * @return
     * @throws
     * @Title:checkNameUnique
     * @Description: 检查产品名称唯一性
     * @author: xuwanlin
     */
    boolean checkNameUnique(Product product);

    /**
     * @param product
     * @return
     * @throws
     * @Title:checkProductCodeUnique
     * @Description: 产品编码唯一性检查
     * @author: xuwanlin
     */
    boolean checkProductCodeUnique(Product product);

    /**
     * @return
     * @throws
     * @Title:selectAllProducts
     * @Description: TODO
     * @author: xuwanlin
     */
    List<Product> selectAllProducts();

    /**
     * @param productSize
     * @param entId
     * @return
     * @Title: getProListByProSizeEnterId
     * @Description: 根据产品大小查询特定企业管理的产品
     * @return: List<Product>
     */
    List<Product> getProListByProSizeEnterId(String productSize, Long entId);

    /**
     * @return
     * @throws
     * @Title:selectDistinctFlowSize
     * @Description: TODO
     * @author: caohaibing
     */
    List<Product> selectDistinctProSize();


    /**
     * 根据企业编码查询所有的产品信息
     *
     * @param enterpriseCode
     * @return
     */
    List<Product> selectAllProductsByEnterCode(String enterpriseCode);

    /**
     * 得到平台中定义的现金产品
     *
     * @return 返回平台定义的现金产品
     */
    Product getCurrencyProduct();

    /**
     * @return
     * @Title: getFlowProduct
     * @Description: TODO
     * @return: Product
     */
    Product getFlowProduct();

    /**
     * 根据enterpriseId查找相关联产品，这个方法查找出来的都是上架的平台产品
     * @param enterpriseId
     * @return
     * @Title: selectAllProductsByEnterId
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> selectAllProductsByEnterId(Long enterpriseId);

    /**
     * 根据enterpriseId查找相关联产品，这个方法查找出来的所有产品（包括上架和下架）
     * @param enterpriseId
     * @return
     * @Title: selectProductsByEnterId
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> selectProductsByEnterId(Long enterpriseId);

    /**
     * 根据产品模板id查找产品
     * @param productTemplateId
     * @author qinqinyan
     * */
    List<Product> selectByProductTemplateId(Long productTemplateId);

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
     * @Title: selectByProductCode
     * @Description: TODO
     * @return: Product
     */
    Product selectByProductCode(String productCode);

    /**
     * @param productCodes
     * @return
     * @Title: selectProIdsByProductCode
     * @Description: TODO
     * @return: List<Long>
     */
    List<Long> selectProIdsByProductCode(List<String> productCodes);

    /**
     * @param productId
     * @return
     * @throws
     * @Title:get
     * @Description: TODO
     * @author: xuwanlin
     */
    Product get(Long productId);

    /**
     * @param product
     * @return
     * @Title: changeProductStatus
     * @Description: TODO
     * @return: boolean
     */
    boolean changeProductStatus(Product product);

    /**
     * 查询所有上架的产品
     *
     * @return
     */
    List<Product> selectAllProductsON();

    /**
     * @param prdCodes
     * @return
     * @Title: batchSelectByCodes
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> batchSelectByCodes(Set<String> prdCodes);

    /**
     * @param queryObject
     * @return
     * @Title: getProductListForEnter
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> getProductListForEnter(QueryObject queryObject);

    /**
     * @param queryObject
     * @return
     * @Title: getProductCountForEnter
     * @Description: TODO
     * @return: int
     */
    int getProductCountForEnter(QueryObject queryObject);

    /**
     * @param map
     * @return
     * @Title: createNewProduct
     * @Description: 新增BOSS端产品, 根据各个省公司定义的BOSS端产品信息不同，分别去实现
     * @return: boolean
     */
    boolean createNewProduct(Map<String, Object> map);

    /**
     * 获取默认与企业关联的产品
     *
     * @return
     */
    List<Product> selectDefaultProductByCustomerType(Long productCustomerType);

    /**
     * 删除平台产品，同时要删除平台产品和企业的关系，平台产品和BOSS产品的关系
     *
     * @param id
     * @return
     * @date 2016年6月16日
     * @author wujiamin
     */
    boolean deletePlatformProduct(Long id);

    /**
     * 查询企业订购信息
     */
    List<Product> getProductListFromEnterAccount(QueryObject queryObject);

    /**
     * @param queryObject
     * @return
     * @Title: getProductCountFromEnterAccount
     * @Description: TODO
     * @return: int
     */
    int getProductCountFromEnterAccount(QueryObject queryObject);

    /**
     * 根据奖品获取产品信息
     *
     * @param prizes
     * @return
     */
    List<Product> getProductsByPrizes(List<PrizeInfo> prizes);

    /**
     * 根据企业id和运营商类型查找上架可用产品
     *
     * @author qinqinyan
     * *
     */
    List<Product> getProductByEntIdAndIsp(QueryObject queryObject);

    /**
     * @param queryObject
     * @return
     * @Title: countProductByEntIdAndIsp
     * @Description: TODO
     * @return: int
     */
    Integer countProductByEntIdAndIsp(QueryObject queryObject);

    /**
     * @param records
     * @return
     * @Title: batchInsertProduct
     * @Description: TODO
     * @return: boolean
     */
    boolean batchInsertProduct(List<Product> records);

    /**
     * @param createTime
     * @return
     * @Title: selectProductByCreateTime
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> selectProductByCreateTime(Date createTime);

    /**
     * @param records
     * @return
     * @Title: batchUpdate
     * @Description: 批量更新操作
     * @return: boolean
     */
    boolean batchUpdate(List<Product> records);

    /**
     * @param productId
     * @return
     * @Title: getProductsByPidWithDeleteFlag
     * @Description: 根据产品ID查询产品
     * @return: List<Product>
     */
    List<Product> getProductsByPidWithDeleteFlag(Long productId);

    /**
     * 将流量池转化为具体的平台产品(新疆ProductService实现)
     *
     * @Title: getProductForFlowAccount
     */
    Product getProductForFlowAccount(String size, Long productId);

    /**
     * 根据产品大小获取产品
     *
     * @param size
     * @return
     */
    List<Product> selectByProSize(Long size);

    /**
     * 将供应商产品与平台产品做关联（重庆平台）
     *
     * @param enterCode
     * @param enterProCode
     * @return
     */
    boolean synPrdsWithSupplierPro(String enterCode, String enterProCode);
    
    /**
     * @param productCode
     * @param currentBossNumber
     * @param enterprise
     * @param enterProCode
     */
    void insertSupplierProduct (String productCode, Double currentBossNumber,
    		Enterprise enterprise, String enterProCode);


    /***
     * @Title getPrdBySizeAndId
     * @param productId
     * @param productSize
     * */
    Product getPrdBySizeAndId(Long productSize, Long productId);
    
    /**
     * 根据类型得到产品列表（原产品，非转化产品），
     * diffId为不在产品产品编码里的Id，可为空
     * entId为关联到的企业，为空时是去掉该筛选条件
     * 
     * @param diffId
     * @param types
     * @return
     */
    List<Product> getPrdsByType(Long diffId, List<Integer> types , Long entId);
    
    /**
     * 根据类型查询，分页
     */
    List<Product> getPrdsByTypePageList(QueryObject queryObject,List<Integer> types);
    
    /**
     * 根据类型查询，分页
     */
    int getPrdsByTypePageCount(QueryObject queryObject,List<Integer> types);
    
    /**
     * 根据供应商产品ID查找平台产品
     * @param supplierId
     * @author qinqinyan
     * */
    List<Product> getPlatFormBySupplierId(Long supplierId);
    
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
    List<Product> getProductListAvailable(QueryObject queryObject);
    /**
     * 根据模板id获取产品（分页显示）,不包括已经跟模板id关联的产品
     * @param map
     * @author qinqinyan
     * */
    int countProductListAvailable(QueryObject queryObject);
    
    /**
     * 
     * @Title: getBySplIdWithoutDel 
     * @Description: 根据供应商产品ID查询平台产品信息
     * @param spid
     * @return
     * @return: List<Product>
     */
    List<Product> getBySplIdWithoutDel(Long spid);
    
    /**
     * 根据订购组编号获取产品信息 上海
     * @param oriderListId
     * @return
     */
    public List<Product> getProductsByOrderListId(Long oriderListId);

    /**
     * @param queryObject
     * @return
     */
    int showForPageResultCount(QueryObject queryObject);

    /**
     * @param queryObject
     * @return
     */
    List<Product> showForPageResultList(QueryObject queryObject);
    
    List<Product> getOrderProductByMap(Map map);

    /**
     * 
     * @Title: selectByType 
     * @Description: TODO
     * @param type
     * @return
     * @return: List<Product>
     */
    List<Product> selectByType(Integer type);
    
    /**
     * 判断是否山东线上环境，如果是则按照相应规则排序
     * 
     * 规则先1099-1092-1087 再109901-109902-109903
     */
    boolean sdPrdsSort(List<Product> listPrds);

}
