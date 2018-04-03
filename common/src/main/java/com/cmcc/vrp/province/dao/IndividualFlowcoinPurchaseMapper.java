package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.IndividualFlowcoinPurchase;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Title:IndividualFlowcoinPurchaseMapper
 * @Description:
 * */
public interface IndividualFlowcoinPurchaseMapper {
    /**
     * @Title:deleteByPrimaryKey
     * @Description:
     * */
    int deleteByPrimaryKey(Long id);
    /**
     * @Title:insert
     * @Description:
     * */
    int insert(IndividualFlowcoinPurchase record);
    /**
     * @Title:insertSelective
     * @Description:
     * */
    int insertSelective(IndividualFlowcoinPurchase record);
    /**
     * @Title:selectByPrimaryKey
     * @Description:
     * */
    IndividualFlowcoinPurchase selectByPrimaryKey(Long id);
    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:
     * */
    int updateByPrimaryKeySelective(IndividualFlowcoinPurchase record);
    /**
     * @Title:updateByPrimaryKey
     * @Description:
     * */
    int updateByPrimaryKey(IndividualFlowcoinPurchase record);
    /**
     * @Title:selectByMap
     * @Description:
     * */
    List<IndividualFlowcoinPurchase> selectByMap(Map map);
    /**
     * @Title:countByMap
     * @Description:
     * */
    int countByMap(Map map);
    /**
     * @Title:selectBySystemSerial
     * @Description:
     * */
    IndividualFlowcoinPurchase selectBySystemSerial(String systemSerial);
    /**
     * @Title:updateStatus
     * @Description:
     * */
    int updateStatus(@Param("systemSerial") String syetemSerial, @Param("status") Integer status);
    /**
     * @Title:updateBySystemSerial
     * @Description:
     * */
    int updateBySystemSerial(IndividualFlowcoinPurchase record);
}