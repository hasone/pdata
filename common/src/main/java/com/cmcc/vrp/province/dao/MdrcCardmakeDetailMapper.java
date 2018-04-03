package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.MdrcCardmakeDetail;
import org.apache.ibatis.annotations.Param;
/**
 * MdrcCardmakeDetailMapper
 * */
public interface MdrcCardmakeDetailMapper {
    /**
     * @Title:deleteByPrimaryKey
     * @Description:todo
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @Title:insert
     * @Description:todo
     * */
    int insert(MdrcCardmakeDetail record);

    /**
     * @Title:insertSelective
     * @Description:todo
     * */
    int insertSelective(MdrcCardmakeDetail record);

    /**
     * @Title:selectByPrimaryKey
     * @Description:todo
     * */
    MdrcCardmakeDetail selectByPrimaryKey(Long id);

    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:todo
     * */
    int updateByPrimaryKeySelective(MdrcCardmakeDetail record);

    /**
     * @Title:updateByPrimaryKey
     * @Description:todo
     * */
    int updateByPrimaryKey(MdrcCardmakeDetail record);

    /**
     * 根据requestId查找
     * @param requestId
     * */
    MdrcCardmakeDetail selectByRequestId(@Param("requestId") Long requestId);
}