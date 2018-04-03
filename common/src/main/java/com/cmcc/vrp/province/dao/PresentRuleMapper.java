package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.FlowCountLineMap;
import com.cmcc.vrp.province.model.PresentRule;

import java.util.List;
import java.util.Map;

/**
 * PresentRuleMapper
 * */
public interface PresentRuleMapper {
    /**
     * @Title:deleteByPrimaryKey
     * @Description:
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @Title:insert
     * @Description:
     * */
    int insert(PresentRule record);

    /**
     * @Title:insertSelective
     * @Description:
     * */
    int insertSelective(PresentRule record);

    /**
     * @Title:selectByPrimaryKey
     * @Description:
     * */
    PresentRule selectByPrimaryKey(Long id);

    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:
     * */
    int updateByPrimaryKeySelective(PresentRule record);

    /**
     * @Title:updateByPrimaryKey
     * @Description:
     * */
    int updateByPrimaryKey(PresentRule record);

    /**
     * @Title:selectCount
     * @Description:
     * */
    Integer selectCount(Map map);

    /**
     * @Title:selectPageRule
     * @Description:
     * */
    List<PresentRule> selectPageRule(Map map);

    /**
     * @Title:selectRuleDetails
     * @Description:
     * */
    PresentRule selectRuleDetails(Long id);

    /**
     * @Title:selectRuleId
     * @Description:
     * */
    long selectRuleId(PresentRule presentRule);

    /**
     * @Title:selectRuleByMult
     * @Description:
     * */
    PresentRule selectRuleByMult(PresentRule presentRule);

    /**
     * @Title:getPresentCount
     * @Description:
     * */
    //转赠数饼图
    Integer getPresentCount(Map<String, Object> paramMap);

    /**
     * @Title:getPresentCountLine
     * @Description:
     * */
    //转赠数折线图
    List<FlowCountLineMap> getPresentCountLine(Map<String, Object> paramMap);

    /**
     * @Title:getMonthPresentCountLine
     * @Description:
     * */
    //月转赠送
    List<FlowCountLineMap> getMonthPresentCountLine(Map<String, Object> paramMap);
}