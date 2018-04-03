package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.ChangeMobileRecord;

/**
 * 变更手机号服务类
 * @author qinqinyan
 * */
public interface ChangeMobileRecordService {
    /**
     * @title:deleteByPrimaryKey
     * */
    boolean deleteByPrimaryKey(Long id);
    /**
     * @title:insert
     * */
    boolean insert(ChangeMobileRecord record);
    /**
     * @title:insertSelective
     * */
    boolean insertSelective(ChangeMobileRecord record);
    /**
     * @title:selectByPrimaryKey
     * */
    ChangeMobileRecord selectByPrimaryKey(Long id);
    /**
     * @title:updateByPrimaryKeySelective
     * */
    boolean updateByPrimaryKeySelective(ChangeMobileRecord record);
    /**
     * @title:updateByPrimaryKey
     * */
    boolean updateByPrimaryKey(ChangeMobileRecord record);
}
