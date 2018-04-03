/**
 *
 */
package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.CustomerType;

import java.util.List;

/**
 * @author JamieWu
 *         客户分类
 */
public interface CustomerTypeMapper {
    /** 
     * @Title: selectAll 
     */
    List<CustomerType> selectAll();

    /**
     *根据id查找
     * @param id
     * @author qinqinyan
     * */
    CustomerType selectById(Long id);
}
