package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.ChangeMobileRecord;

/**
 * 变更手机号记录mapper
 * */
public interface ChangeMobileRecordMapper {
    /**
     * @title:deleteByPrimaryKey
     * */
    int deleteByPrimaryKey(Long id);
    /**
     * @title:insert
     * */
    int insert(ChangeMobileRecord record);
    /**
     * @title:insertSelective
     * */
    int insertSelective(ChangeMobileRecord record);
    /**
     * @title:selectByPrimaryKey
     * */
    ChangeMobileRecord selectByPrimaryKey(Long id);
    /**
     * @title:updateByPrimaryKeySelective
     * */
    int updateByPrimaryKeySelective(ChangeMobileRecord record);
    /**
     * @title:updateByPrimaryKey
     * */
    int updateByPrimaryKey(ChangeMobileRecord record);
}