package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.ProductTemplateEnterpriseMap;

/**
 * @ClassName: EntProductService
 * @Description: TODO
 */
public interface EntProductService {

    /**
     * @param enterprizeId
     * @return
     * @Title: selectByEnterprizeID
     * @Description: 根据企业ID查询
     * @return: List<EntProduct>
     */
    List<EntProduct> selectByEnterprizeID(Long enterprizeId);

    /**
     * @param enterprizeId
     * @return
     * @Title: selectProIdsByEnterprizeID
     * @Description: 根据企业ID查询产品Id
     * @return: List<Long>
     */
    List<Long> selectProIdsByEnterprizeID(Long enterprizeId);

    /**
     * @param productId
     * @param enterpriseId
     * @return
     * @Title: selectByProductIDAndEnterprizeID
     * @Description: 根据产品ID和企业ID查询
     * @return: EntProduct
     */
    EntProduct selectByProductIDAndEnterprizeID(Long productId, Long enterpriseId);

    /**
     * @param productID
     * @return
     * @Title: deleteByProductID
     * @Description: 根据产品ID逻辑删除
     * @return: boolean
     */
    boolean deleteByProductID(Long productID);

    /**
     * @param record
     * @return
     * @Title: insert
     * @Description: 存储操作
     * @return: boolean
     */
    boolean insert(EntProduct record);

    /**
     * @param record
     * @return
     * @Title: insertSelective
     * @Description: 存储操作
     * @return: boolean
     */
    boolean insertSelective(EntProduct record);

    /**
     * 更新企业产品信息
     */
    boolean updateEnterpriseProduct(List<String> productCodes, Long enterpriseId);

    /**
     * 根据企业查找其关联的上架产品
     * @param enterId
     * @return
     * @Title: selectProductByEnterId
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> selectProductByEnterId(Long enterId);
    
    /**
     * 根据企业查找其关联的所有产品
     * @param enterId
     * @return
     * @Title: selectProductByEnterId
     * @Description: TODO
     * @return: List<Product>
     * @author qinqinyan
     */
    List<Product> selectAllProductByEnterId(Long enterId);

//	boolean updateEnterProductByRecords(Long enterId, List<EntProduct> entProductAdds, List<EntProduct> entProductDeletes);

    /**
     * 根据企业ID更新企业产品关联折扣
     *
     * @param enterId
     * @param discount
     * @return
     * @date 2016年7月1日
     * @author wujiamin
     */
    boolean updateDiscountByEnterId(Long enterId, Integer discount);

    /**
     * @param enterId
     * @param entProductAdds
     * @param entProductDeletes
     * @param entProductUpdates
     * @return
     * @Title: updateEnterProductByRecords
     * @Description: TODO
     * @return: boolean
     */
    boolean updateEnterProductByRecords(Long enterId, List<EntProduct> entProductAdds, List<EntProduct> entProductDeletes, List<EntProduct> entProductUpdates);

    /**
     * @param enterId
     * @return
     * @Title: productAvailableByEnterId
     * @Description: TODO
     * @return: List<Product>
     */
    List<Product> productAvailableByEnterId(Long enterId);

    /**
     * @param records
     * @return
     * @Title: batchEntProduct
     * @Description: TODO
     * @return: boolean
     */
    boolean batchInsertEntProduct(List<EntProduct> records);

    /**
     * @param list
     * @return
     * @Title: batchUpdate
     * @Description: 批量更新
     * @return: boolean
     */
    boolean batchUpdate(List<EntProduct> list);

    /**
     * @param productId
     * @return
     * @Title: selectByProductIdWithoutDeleleFlag
     * @Description: 根据产品ID查询
     * @return: List<Product>
     */
    List<EntProduct> selectByProductIdWithoutDeleleFlag(Long productId);
    
    /**
     * 根据企业Id和产品id批量删除
     * @param entProducts
     * @author qinqinyan
     * */
    boolean batchDeleteByEnterIdAndProductId(List<EntProduct> entProducts);
    
    /**
     * 根据企业Id批量删除
     * @param productTemplateEnterpriseMaps
     * @author qinqinyan
     * */
    boolean batchDeleteByEnterId(List<ProductTemplateEnterpriseMap> productTemplateEnterpriseMaps);
    
    /**
     * 
     * @Title: validateEntAndPrd 
     * @Description: 企业产品校验
     * @param entId
     * @param productId
     * @return
     * @return: String
     */
    String validateEntAndPrd(Long entId, Long productId);
}
