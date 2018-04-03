package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.ProductTemplateEnterpriseMap;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: EntProductMapper 
 * @Description: 企业产品关联DAO
 * @author: Rowe
 * @date: 2016年11月17日 下午8:09:55
 */
public interface EntProductMapper {
    /**
     * 
     * @Title: deleteByPrimaryKey 
     * @Description: 删除
     * @param id
     * @return
     * @return: int
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 
     * @Title: insert 
     * @Description: 生成记录
     * @param record
     * @return
     * @return: int
     */
    int insert(EntProduct record);

    /**
     * 
     * @Title: insertSelective 
     * @Description: 生成
     * @param record
     * @return
     * @return: int
     */
    int insertSelective(EntProduct record);

    /**
     * 
     * @Title: selectByPrimaryKey 
     * @Description: 查询
     * @param id
     * @return
     * @return: EntProduct
     */
    EntProduct selectByPrimaryKey(Long id);

    /**
     * 
     * @Title: updateByPrimaryKeySelective 
     * @Description: 更新
     * @param record
     * @return
     * @return: int
     */
    int updateByPrimaryKeySelective(EntProduct record);

    /**
     * 
     * @Title: updateByPrimaryKey 
     * @Description: 更新
     * @param record
     * @return
     * @return: int
     */
    int updateByPrimaryKey(EntProduct record);

    /**
     * 
     * @Title: selectByEnterpriseID 
     * @Description: 根据企业ID查询
     * @param enterprizeId
     * @return
     * @return: List<EntProduct>
     */
    List<EntProduct> selectByEnterpriseID(Long enterprizeId);

    /**
     * 
     * @Title: selectByProductID 
     * @Description: 根据产品ID查询
     * @param productId
     * @return
     * @return: List<EntProduct>
     */
    List<EntProduct> selectByProductID(Long productId);

    /**
     * 
     * @Title: selectByProductIDAndEnterprizeID 
     * @Description: 根据企业ID和产品ID查询
     * @param paramMap
     * @return
     * @return: EntProduct
     */
    EntProduct selectByProductIDAndEnterprizeID(Map paramMap);

    /**
     * 
     * @Title: deleteByProductID 
     * @Description: 根据产品ID逻辑删除
     * @param productId
     * @return
     * @return: int
     */
    int deleteByProductID(Long productId);

    /**
     * 
     * @Title: deleteByEnterpriseID 
     * @Description: 根据企业ID逻辑删除
     * @param enterpriseId
     * @return
     * @return: int
     */
    int deleteByEnterpriseID(long enterpriseId);

    /**
     * 
     * @Title: deleteByProIdEnterId 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int deleteByProIdEnterId(EntProduct record);
    
    /**
     * 
     * @Title: updateByProIdEnterId 
     * @Description: 更新
     * @param record
     * @return
     * @return: int
     */
    int updateByProIdEnterId(EntProduct record);

    /**
     * 
     * @Title: batchInsert 
     * @Description: 批量生成
     * @param records
     * @return
     * @return: int
     */
    int batchInsert(@Param("records") List<EntProduct> records);

    /**
     * 根据企业id查询企业关联的上架产品
     * @Title: selectProductByEnterId 
     * @Description: 查询
     * @param enterId
     * @return
     * @return: List<Product>
     */
    List<Product> selectProductByEnterId(@Param("enterId") Long enterId);
    
    /**
     * 根据企业id查询企业关联的所有产品
     * @Title: selectProductByEnterId 
     * @Description: 查询
     * @param enterId
     * @return
     * @return: List<Product>
     */
    List<Product> selectAllProductByEnterId(@Param("enterId")Long enterId);
        
    /**
     * 
     * @Title: selectProductCodeByEnterId 
     * @Description: 查询
     * @param enterId
     * @return
     * @return: List<String>
     */
    List<String> selectProductCodeByEnterId(@Param("enterId") Long enterId);
    
    /**
     * 
     * @Title: updateDiscountByEnterId 
     * @Description: 查询
     * @param enterId
     * @param discount
     * @return
     * @return: int
     */
    int updateDiscountByEnterId(@Param("enterId") Long enterId, @Param("discount") Integer discount);

    /**
     * 
     * @Title: productAvailableByEnterId 
     * @Description: 查询
     * @param enterId
     * @return
     * @return: List<Product>
     */
    List<Product> productAvailableByEnterId(Long enterId);
    
    /**
     * 
     * @Title: batchUpdate 
     * @Description: 批量更新
     * @param list
     * @return
     * @return: int
     */
    int batchUpdate(@Param("list") List<EntProduct> list);
    
    /**
     * 
     * @Title: selectByProductIDWithoutDeleteFlag 
     * @Description: 根据产品ID查询
     * @param productId
     * @return
     * @return: List<EntProduct>
     */
    List<EntProduct> selectByProductIDWithoutDeleteFlag(Long productId);

    /**
     * batchDeleteByEnterIdAndProductId
     * @param entProducts
     * @author qinqinyan
     * */
    int batchDeleteByEnterIdAndProductId(@Param("entProducts")List<EntProduct> entProducts);
    
    /**
     * batchDeleteByEnterId
     * @param productTemplateEnterpriseMaps
     * @author qinqinyan
     * */
    int batchDeleteByEnterId(@Param("productTemplateEnterpriseMaps")List<ProductTemplateEnterpriseMap> productTemplateEnterpriseMaps);
}