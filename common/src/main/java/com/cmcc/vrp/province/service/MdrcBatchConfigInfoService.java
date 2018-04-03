package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.MdrcBatchConfigInfo;

/**
 * create by qinqinyan on 2018/08/01
 * */
public interface MdrcBatchConfigInfoService {
    
    /**
     * @title:deleteByPrimaryKey
     * */
    boolean deleteByPrimaryKey(Long id);

    /**
     * @title:insertSelective
     * */
    boolean insertSelective(MdrcBatchConfigInfo record);

    /**
     * @title:selectByPrimaryKey
     * */
    MdrcBatchConfigInfo selectByPrimaryKey(Long id);

    /**
     * @title:updateByPrimaryKeySelective
     * */
    boolean updateByPrimaryKeySelective(MdrcBatchConfigInfo record);

    /**
     * @title:updateByPrimaryKey
     * */
    boolean updateByPrimaryKey(MdrcBatchConfigInfo record);

}
