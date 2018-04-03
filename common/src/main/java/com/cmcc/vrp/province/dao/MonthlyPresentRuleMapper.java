package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.MonthlyPresentRule;

/**
 * 
 * @ClassName: MonthlyPresentRuleMapper 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年7月18日 下午4:15:12
 */
public interface MonthlyPresentRuleMapper {
    /**
     * 
     * @Title: deleteByPrimaryKey 
     * @Description: TODO
     * @param id
     * @return
     * @return: int
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 
     * @Title: insert 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int insert(MonthlyPresentRule record);

    /**
     * 
     * @Title: insertSelective 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int insertSelective(MonthlyPresentRule record);

    /**
     * 
     * @Title: selectByPrimaryKey 
     * @Description: TODO
     * @param id
     * @return
     * @return: MonthlyPresentRule
     */
    MonthlyPresentRule selectByPrimaryKey(Long id);

    /**
     * 
     * @Title: updateByPrimaryKeySelective 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int updateByPrimaryKeySelective(MonthlyPresentRule record);

    /**
     * 
     * @Title: updateByPrimaryKey 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int updateByPrimaryKey(MonthlyPresentRule record);

    /**
     * 
     * @Title: getRules 
     * @Description: TODO
     * @param map
     * @return
     * @return: List<MonthlyPresentRule>
     */
    List<MonthlyPresentRule> getRules(Map map);

    /**
     * 
     * @Title: countRules 
     * @Description: TODO
     * @param map
     * @return
     * @return: Long
     */
    Long countRules(Map map);

    /**
     * 
     * @Title: getDetailByRuleId 
     * @Description: TODO
     * @param ruleId
     * @return
     * @return: MonthlyPresentRule
     */
    MonthlyPresentRule getDetailByRuleId(Long ruleId);
    
    /**
     * 更新状态为完成
     * @return
     */
    int updateRuleStatusFini();
}