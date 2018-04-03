package com.cmcc.vrp.province.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.IndividualFlowOrder;

/**
 * IndividualFlowOrderMapper
 */
public interface IndividualFlowOrderMapper {
    /**
     * int deleteByPrimaryKey(Long id);
     */
    int deleteByPrimaryKey(Long id);

    /**
     * int insert(IndividualFlowOrder record);
     */
    int insert(IndividualFlowOrder record);

    /**
     * int insertSelective(IndividualFlowOrder record);
     */
    int insertSelective(IndividualFlowOrder record);

    /**
     * IndividualFlowOrder selectByPrimaryKey(Long id);
     */
    IndividualFlowOrder selectByPrimaryKey(Long id);

    /**
     * int updateByPrimaryKeySelective(IndividualFlowOrder record);
     */
    int updateByPrimaryKeySelective(IndividualFlowOrder record);

    /**
     * int updateByPrimaryKey(IndividualFlowOrder record);
     */
    int updateByPrimaryKey(IndividualFlowOrder record);

    /** 
     * @Title: selectBySystemNum 
     */
    IndividualFlowOrder selectBySystemNum(String systemNum);

    /** 
     * @Title: countByDate 
     */
    Integer countByDate(@Param("startTime")Date startTime, @Param("endTime")Date endTime);

    /** 
     * @Title: selectByMap 
     */
    List<IndividualFlowOrder> selectByMap(Map map);

    /** 
     * @Title: countByMap 
     */
    Integer countByMap(Map map);
}