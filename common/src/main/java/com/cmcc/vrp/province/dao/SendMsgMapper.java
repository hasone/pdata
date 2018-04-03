package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.SendMsg;

/**
 * @Title:SendMsgMapper
 * @Description:
 * */
public interface SendMsgMapper {
    /**
     * @Title:deleteByPrimaryKey
     * @Description:
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @Title:insert
     * @Description:
     * */
    int insert(SendMsg record);

    /**
     * @Title:insertSelective
     * @Description:
     * */
    int insertSelective(SendMsg record);

    /**
     * @Title:selectByPrimaryKey
     * @Description:
     * */
    SendMsg selectByPrimaryKey(Long id);

    /**
     * @Title:getVirifyCodeByMobile
     * @Description:
     * */
    SendMsg getVirifyCodeByMobile(String mobile);

    /**
     * @Title:updateByPrimaryKey
     * @Description:
     * */
    int updateByPrimaryKeySelective(SendMsg record);

    /**
     * @Title:updateByPrimaryKey
     * @Description:
     * */
    int updateByPrimaryKey(SendMsg record);
}