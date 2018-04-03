package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.UrlMap;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年2月14日 上午8:38:51
*/
public interface UrlMapMapper {
    /**
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int insert(UrlMap record);

    /**
     * @param record
     * @return
     */
    int insertSelective(UrlMap record);

    /**
     * @param id
     * @return
     */
    UrlMap selectByPrimaryKey(Long id);

    /**
     * @param uuid
     * @return
     */
    UrlMap selectByUUID(String uuid);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(UrlMap record);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKey(UrlMap record);
}