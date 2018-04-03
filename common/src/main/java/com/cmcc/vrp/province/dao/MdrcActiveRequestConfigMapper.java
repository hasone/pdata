package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.MdrcActiveRequestConfig;

import java.util.List;
/**
 * MdrcActiveRequestConfigMapper
 * */
public interface MdrcActiveRequestConfigMapper {

    /**
     * @Title:deleteByPrimaryKey
     * @Description:
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @Title:insert
     * @Description:
     * */
    int insert(MdrcActiveRequestConfig record);

    /**
     * @Title:insertSelective
     * @Description:
     * */
    int insertSelective(MdrcActiveRequestConfig record);

    /**
     * @Title:selectByPrimaryKey
     * @Description:
     * */
    MdrcActiveRequestConfig selectByPrimaryKey(Long id);

    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:
     * */
    int updateByPrimaryKeySelective(MdrcActiveRequestConfig record);

    /**
     * @Title:updateByPrimaryKey
     * @Description:
     * */
    int updateByPrimaryKey(MdrcActiveRequestConfig record);

    /**
     * @Title:selectByRequestId
     * @Description:
     * */
    List<MdrcActiveRequestConfig> selectByRequestId(Long requestId);
}