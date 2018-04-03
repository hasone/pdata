package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.RuleType;

import java.util.List;
import java.util.Map;

/**
 * @Title:RuleTypeMapper
 * @Description:
 * */
public interface RuleTypeMapper {
    /**
     * @Title:deleteByPrimaryKey
     * @Description:
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @Title:insert
     * @Description:
     * */
    int insert(RuleType record);

    /**
     * @Title:insertSelective
     * @Description:
     * */
    int insertSelective(RuleType record);

    /**
     * @Title:selectByPrimaryKey
     * @Description:
     * */
    RuleType selectByPrimaryKey(Long id);

    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:
     * */
    int updateByPrimaryKeySelective(RuleType record);

    /**
     * @Title:updateByPrimaryKey
     * @Description:
     * */
    int updateByPrimaryKey(RuleType record);

    /**
     * 查询记录条数
     * @param queryCriteria
     * @return
     */
    Long count(Map<String, Object> queryCriteria);

    /**
     * 查询记录列表
     * @return
     */
    List<RuleType> listRuleType();
}