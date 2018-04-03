package com.cmcc.vrp.province.service;

import java.security.InvalidParameterException;
import java.util.List;

import com.cmcc.vrp.province.model.ProductConverter;
import com.cmcc.vrp.util.QueryObject;

/**
 * 产品禁止转换关系服务类
 * @author qihang
 *
 */
public interface ProductConverterService {
    /**
     * 检测合法
     */
    boolean validate(ProductConverter converter) throws InvalidParameterException;
    
    /**
     * 批量检测合法
     */
    boolean batchValidate(List<ProductConverter> converters) throws InvalidParameterException;
    
    /**
     * 插入
     */
    boolean insert(ProductConverter converter);
    
    /**
     * 批量插入
     */
    boolean insertBatch(List<ProductConverter> list);
    
    /**
     * 根据id查询
     */
    ProductConverter get(Long id);
    
    /**
     * 根据id删除
     */
    boolean delete(Long id);
    
    /**
     * 批量删除，根据id
     */
    boolean deleteBatch(List<ProductConverter> list);
    
    /**
     * 批量插入且删除，有事务
     */
    boolean insertDeleteBatch(List<ProductConverter> addList,List<ProductConverter> deleteList);
    
    /**
     * 保存页面
     */
    boolean save(List<ProductConverter> pageList , List<ProductConverter> dbList);
    
    /**
     * 得到所有关系
     */
    List<ProductConverter> get();
    
    /**
     * 得到指定关系，按srcPrdId -> destPrdId,先缓存
     */
    ProductConverter get(Long srcPrdId , Long destPrdId);
    
    /**
     * 是否禁止
     */
    boolean isInterdictConvert(Long srcPrdId , Long destPrdId);
    
    /**
     * 分页，总个数
     */
    int count(QueryObject queryObject);
    
    /**
     * 分页
     */
    List<ProductConverter> page(QueryObject queryObject);
}
