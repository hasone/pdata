package com.cmcc.vrp.province.dao;

import java.util.List;

import com.cmcc.vrp.province.model.MdrcActiveDetail;

import org.apache.ibatis.annotations.Param;
/**
 * MdrcActiveDetailMapper
 * */
public interface MdrcActiveDetailMapper {
    /**
     * @Titile:deleteByPrimaryKey
     * @Description:todo
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @Titile:insert
     * @Description:todo
     * */
    int insert(MdrcActiveDetail record);

    /**
     * @Titile:insertSelective
     * @Description:todo
     * */
    int insertSelective(MdrcActiveDetail record);

    /**
     * @Titile:selectByPrimaryKey
     * @Description:todo
     * */
    MdrcActiveDetail selectByPrimaryKey(Long id);

    /**
     * @Titile:updateByPrimaryKeySelective
     * @Description:todo
     * */
    int updateByPrimaryKeySelective(MdrcActiveDetail record);

    /**
     * @Titile:updateByPrimaryKey
     * @Description:todo
     * */
    int updateByPrimaryKey(MdrcActiveDetail record);

    /**
     * @Titile:selectByRequestId
     * @Description:todo
     * */
    MdrcActiveDetail selectByRequestId(@Param("requestId") Long requestId);

    /**
     * @Titile:updateByRequestIdSelective
     * @Description:todo
     * */
    int updateByRequestIdSelective(MdrcActiveDetail record);
    
    /**
     * 
     * @Title: selectByconfigIdAndStatus 
     * @Description: 根据规则ID、审批状态查询激活申请信息
     * @param configId
     * @param status
     * @return
     * @return: List<MdrcActiveDetail>
     */
    List<MdrcActiveDetail> selectByconfigIdAndStatus(@Param("configId") Long configId,@Param("status") Integer status);

}