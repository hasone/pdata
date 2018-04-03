package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.MdrcBatchConfigInfo;
/**
 * 卡批次详情扩展信息
 * @author qinqinyan
 * @date 2017/08/01
 * */
public interface MdrcBatchConfigInfoMapper {
    /**
     * @title:deleteByPrimaryKey
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @title:insert
     * */
    int insert(MdrcBatchConfigInfo record);

    /**
     * @title:insertSelective
     * */
    int insertSelective(MdrcBatchConfigInfo record);

    /**
     * @title:selectByPrimaryKey
     * */
    MdrcBatchConfigInfo selectByPrimaryKey(Long id);

    /**
     * @title:updateByPrimaryKeySelective
     * */
    int updateByPrimaryKeySelective(MdrcBatchConfigInfo record);

    /**
     * @title:updateByPrimaryKey
     * */
    int updateByPrimaryKey(MdrcBatchConfigInfo record);
}