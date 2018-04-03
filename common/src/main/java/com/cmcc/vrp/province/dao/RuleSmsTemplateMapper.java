package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.RuleSmsTemplate;

import java.util.List;
import java.util.Map;

/**
 * RuleSmsTemplateMapper.java
 */
public interface RuleSmsTemplateMapper {
    /** 
     * @Title: deleteByPrimaryKey 
     */
    int deleteByPrimaryKey(Long id);

    /** 
     * @Title: insert 
     */
    int insert(RuleSmsTemplate record);

    /** 
     * @Title: insertSelective 
     */
    int insertSelective(RuleSmsTemplate record);

    /** 
     * @Title: selectByPrimaryKey 
     */
    RuleSmsTemplate selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKeySelective 
     */
    int updateByPrimaryKeySelective(RuleSmsTemplate record);

    /** 
     * @Title: updateByPrimaryKey 
     */
    int updateByPrimaryKey(RuleSmsTemplate record);

    /**
     * 查询记录条数
     * <p>
     *
     * @param queryCriteria
     * @return
     */
    Long count(Map<String, Object> queryCriteria);

    /**
     * 查询记录列表
     * <p>
     *
     * @param queryCriteria
     * @return
     */
    List<RuleSmsTemplate> listRuleSmsTemplate(Map<String, Object> queryCriteria);

    List<RuleSmsTemplate> getRuleTemplateByCreatorId(Map<String, Object> queryCriteria);
}