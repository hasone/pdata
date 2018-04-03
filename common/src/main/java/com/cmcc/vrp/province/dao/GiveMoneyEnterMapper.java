package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.GiveMoneyEnter;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:21:30
*/
public interface GiveMoneyEnterMapper {
    /**
     * @param record
     * @return
     */
    int insert(GiveMoneyEnter record);

    /**
     * @param record
     * @return
     */
    int updateByEnterId(GiveMoneyEnter record);

    /**
     * @param enterId
     * @return
     */
    GiveMoneyEnter selectByEnterId(Long enterId);

    /**
     * @param enterId
     * @return
     */
    int deleteGiveMoneyEnterByEnterId(Long enterId);
}
