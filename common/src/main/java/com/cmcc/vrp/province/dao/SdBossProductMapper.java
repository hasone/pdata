package com.cmcc.vrp.province.dao;

import java.util.List;

import com.cmcc.vrp.province.model.SdBossProduct;

public interface SdBossProductMapper {
    /**
     * 
     * @Title: deleteByPrimaryKey 
     * @Description: TODO
     * @param id
     * @return
     * @return: int
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 
     * @Title: insert 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int insert(SdBossProduct record);

    /**
     * 
     * @Title: insertSelective 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int insertSelective(SdBossProduct record);

    /**
     * 
     * @Title: selectByPrimaryKey 
     * @Description: TODO
     * @param id
     * @return
     * @return: SdBossProduct
     */
    SdBossProduct selectByPrimaryKey(Long id);

    /**
     * 
     * @Title: updateByPrimaryKeySelective 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int updateByPrimaryKeySelective(SdBossProduct record);

    /**
     * 
     * @Title: updateByPrimaryKey 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int updateByPrimaryKey(SdBossProduct record);
    
    /**
     * 
     * @Title: selectByCode 
     * @Description: TODO
     * @param code
     * @return
     * @return: SdBossProduct
     */
    SdBossProduct selectByCode(String code);
    
    /**
     * 
     * @Title: getAllProducts 
     * @Description: TODO
     * @return
     * @return: List<SdBossProduct>
     */
    List<SdBossProduct> getAllProducts();
}