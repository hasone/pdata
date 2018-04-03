/**
 *
 */
package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.ProductChangeOperator;

import java.util.List;


/**
 * <p>Title:ProductChangeOperatorService </p>
 * <p>Description: 企业产品变更记录service</p>
 *
 * @author xujue
 * @date 2016年7月21日
 */
public interface ProductChangeOperatorService {

    /** 
     * 批量插入记录
     * @Title: batchInsert 
     */
    boolean batchInsert(List<ProductChangeOperator> pcoList);

    /** 
     * 读取企业产品变更记录
     * @Title: getProductChangeRecordByEntId 
     */
    List<ProductChangeOperator> getProductChangeRecordByEntId(Long entId);

    /** 
     * 删除企业产品变更记录
     * @Title: deleteProductChangeRecordByEntId 
     */
    boolean deleteProductChangeRecordByEntId(Long entId);

}
