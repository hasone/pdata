package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.RuleSmsTemplate;
import com.cmcc.vrp.util.QueryObject;

import java.util.List;

/**
 * RuleSmsTemplateService.java
 */
public interface RuleSmsTemplateService {

    /** 
     * @Title: deleteByPrimaryKey 
     */
    boolean deleteByPrimaryKey(Long id);

    /** 
     * @Title: insert 
     */
    boolean insert(RuleSmsTemplate record);

    /** 
     * @Title: selectByPrimaryKey 
     */
    RuleSmsTemplate selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKeySelective 
     */
    boolean updateByPrimaryKeySelective(RuleSmsTemplate record);

    /**
     * 查询记录条数
     * <p>
     *
     * @param queryObject
     * @return
     */
    Long count(QueryObject queryObject);

    /**
     * 查询记录列表
     * <p>
     *
     * @param queryObject
     * @return
     */
    List<RuleSmsTemplate> listRuleSmsTemplate(QueryObject queryObject);

    List<RuleSmsTemplate> getRuleSmsTemplateByCreator(Long creatorId, String type);

}
