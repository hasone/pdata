package com.cmcc.vrp.province.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.ShBossProduct;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:31:23
*/
public interface ShBossProductMapper {
    /**
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int insert(ShBossProduct record);

    /**
     * @param record
     * @return
     */
    int insertSelective(ShBossProduct record);

    /**
     * @param id
     * @return
     */
    ShBossProduct selectByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ShBossProduct record);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKey(ShBossProduct record);
    List<ShBossProduct> getShBossProducts();

    List<ShBossProduct> getShBossProductsByOrderType(@Param("orderType") String orderType);
}