package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.IndividualFlowcoinRecord;

import java.util.List;
import java.util.Map;

/**
 * @Title:IndividualFlowcoinRecordMapper
 * @Description:
 * */
public interface IndividualFlowcoinRecordMapper {

    /**
     * @Title:deleteByPrimaryKey
     * @Description:
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @Title:insert
     * @Description:
     * */
    int insert(IndividualFlowcoinRecord record);

    /**
     * @Title:insertSelective
     * @Description:
     * */
    int insertSelective(IndividualFlowcoinRecord record);

    /**
     * @Title:selectByPrimaryKey
     * @Description:
     * */
    IndividualFlowcoinRecord selectByPrimaryKey(Long id);

    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:
     * */
    int updateByPrimaryKeySelective(IndividualFlowcoinRecord record);

    /**
     * @Title:updateByPrimaryKey
     * @Description:
     * */
    int updateByPrimaryKey(IndividualFlowcoinRecord record);

    /**
     * @Title:selectByMap
     * @Description:
     * */
    List<IndividualFlowcoinRecord> selectByMap(Map map);

    /**
     * @Title:countByMap
     * @Description:
     * */
    int countByMap(Map map);
}