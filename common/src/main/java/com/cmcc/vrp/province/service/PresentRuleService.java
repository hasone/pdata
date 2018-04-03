package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.PresentRule;
import com.cmcc.vrp.province.model.json.post.PresentRecordJson;
import com.cmcc.vrp.util.QueryObject;

import java.util.List;

/**
 * PresentRuleService.java
 */
public interface PresentRuleService {
    /** 
     * @Title: deleteDraft 
     */
    int deleteDraft(long id);

    /** 
     * @Title: queryCount 
     */
    int queryCount(QueryObject queryObject);

    /** 
     * @Title: queryPage 
     */
    List<PresentRule> queryPage(QueryObject queryObject);

    /** 
     * @Title: selectByPrimaryKey 
     */
    PresentRule selectByPrimaryKey(Long id);

    /** 
     * @Title: selectRuleDetails 
     */
    PresentRule selectRuleDetails(Long id);


    /**
     * 新增规则
     *
     * @param administer
     * @param presentRule
     * @param prs
     * @return
     * @throws
     * @Title:addRule
     * @Description: TODO
     * @author: hexinxu
     */
    boolean addRule(Administer administer, PresentRule presentRule, List<PresentRecordJson> prs);

    /**
     * 开始赠送
     *
     * @param administer
     * @param presentRule
     * @Title:give
     * @author: hexinxu
     */
    boolean give(Administer administer, PresentRule presentRule,String batchPresentSn);

    // Message insert(PresentDTO presentDTO, long adminId);


    /**
     * 修改规则
     *
     * @param administer
     * @param presentRule
     * @param phones
     * @param type
     * @Title:updateRule
     * @author: hexinxu
     */
    boolean updateRule(Administer administer, PresentRule presentRule,
                       String[] phones, Integer type);


    /**
     * @param administer
     * @param ruleId
     * @return
     * @throws
     * @Title:isSameAdminCreated
     * @Description: 检测某条规则的创建者是否和现在的登陆管理员为同一用户
     * @author: qihang
     */
    boolean isSameAdminCreated(Administer administer, Long ruleId);


    /** 
     * @Title: chargeAgain 
     */
    boolean chargeAgain(Long id);
}
