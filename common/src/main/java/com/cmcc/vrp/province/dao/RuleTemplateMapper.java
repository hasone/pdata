package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.RuleTemplate;

import java.util.List;
import java.util.Map;
/**
 * @Title:RuleTemplateMapper
 * @Description:
 * */
public interface RuleTemplateMapper {
    /**
     * @Title:deleteByPrimaryKey
     * @Description:
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @Title:insert
     * @Description:
     * */
    int insert(RuleTemplate record);

    /**
     * @Title:insertSelective
     * @Description:
     * */
    int insertSelective(RuleTemplate record);

    /**
     * @Title:selectByPrimaryKey
     * @Description:
     * */
    RuleTemplate selectByPrimaryKey(Long id);

    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:
     * */
    int updateByPrimaryKeySelective(RuleTemplate record);

    /**
     * @Title:updateByPrimaryKey
     * @Description:
     * */
    int updateByPrimaryKey(RuleTemplate record);

    /**
     * 查询记录条数
     * @param queryCriteria
     * @return
     */
    Long count(Map<String, Object> queryCriteria);

    /**
     * 查询记录列表
     * <p>
     * @return
     */
    List<RuleTemplate> listRuleTemplate(Map<String, Object> queryCriteria);

    /**
     * @Title:getRuleTemplateByCreatorId
     * @Description:
     * */
    List<RuleTemplate> getRuleTemplateByCreatorId(Map<String, Object> queryCriteria);


}