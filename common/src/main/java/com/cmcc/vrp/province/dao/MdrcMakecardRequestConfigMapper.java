package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.MdrcMakecardRequestConfig;
import org.apache.ibatis.annotations.Param;
/**
 * MdrcMakecardRequestConfigMapper
 * */
public interface MdrcMakecardRequestConfigMapper {
    /**
     * @title:deleteByPrimaryKey
     * @description:
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @title:insert
     * @description:
     * */
    int insert(MdrcMakecardRequestConfig record);

    /**
     * @title:insertSelective
     * @description:
     * */
    int insertSelective(MdrcMakecardRequestConfig record);

    /**
     * @title:selectByPrimaryKey
     * @description:
     * */
    MdrcMakecardRequestConfig selectByPrimaryKey(Long id);

    /**
     * @title:updateByPrimaryKeySelective
     * @description:
     * */
    int updateByPrimaryKeySelective(MdrcMakecardRequestConfig record);

    /**
     * @title:updateByPrimaryKey
     * @description:
     * */
    int updateByPrimaryKey(MdrcMakecardRequestConfig record);

    /**
     * @title:selectByRequestId
     * @description:
     * */
    MdrcMakecardRequestConfig selectByRequestId(@Param("requestId") Long requestId);
    
    /**
     * @title:selectByConfigId
     * @description:
     * */
    MdrcMakecardRequestConfig selectByConfigId(@Param("configId") Long configId);
}