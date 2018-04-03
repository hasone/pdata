/**
 *
 */
package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.Discount;

import java.util.List;

/**
 * <p>Title:DiscountService </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年7月25日
 */
public interface DiscountService {

    /** 
     * @Title: selectAllDiscount 
     */
    List<Discount> selectAllDiscount();

    /** 
     * @Title: selectByPrimaryKey 
     */
    Discount selectByPrimaryKey(Long id);

}
