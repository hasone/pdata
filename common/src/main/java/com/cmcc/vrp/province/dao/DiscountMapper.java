/**
 *
 */
package com.cmcc.vrp.province.dao;

import java.util.List;

import com.cmcc.vrp.province.model.Discount;

/**
 * @author JamieWu
 */
public interface DiscountMapper {
    /**
     * @return
     */
    List<Discount> selectAllDiscount();

    /**
     * @param id
     * @return
     */
    Discount selectByPrimaryKey(Long id);

}
