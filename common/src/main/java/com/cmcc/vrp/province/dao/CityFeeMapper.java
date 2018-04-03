package com.cmcc.vrp.province.dao;

import java.util.Map;

import com.cmcc.vrp.province.model.CityFee;

public interface CityFeeMapper {

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
    int insert(CityFee record);

    /**
     * 
     * @Title: insertSelective 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int insertSelective(CityFee record);

    /**
     * 
     * @Title: selectByPrimaryKey 
     * @Description: TODO
     * @param id
     * @return
     * @return: CityFee
     */
    CityFee selectByPrimaryKey(Long id);

    /**
     * 
     * @Title: updateByPrimaryKeySelective 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int updateByPrimaryKeySelective(CityFee record);

    /**
     * 
     * @Title: updateByPrimaryKey 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int updateByPrimaryKey(CityFee record);
    
    /**
     * 
     * @Title: getByCityCodeAndFeecode 
     * @Description: TODO
     * @param map
     * @return
     * @return: CityFee
     */
    CityFee getByCityCodeAndFeecode(Map<String, Object> map);

}