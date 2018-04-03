package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.GiveMoney;

import java.util.List;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:22:00
*/
public interface GiveMoneyMapper {
    /**
     * @return
     */
    List<GiveMoney> selectNormalRecord();
}
