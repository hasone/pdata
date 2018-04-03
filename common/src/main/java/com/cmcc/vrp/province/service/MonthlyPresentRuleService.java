package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.province.model.MonthlyPresentRule;
import com.cmcc.vrp.util.QueryObject;

/**
 * 
 * @ClassName: MonthlyPresentRuleService 
 * @Description: 包月赠送服务类
 * @author: luozuwu
 * @date: 2017年7月5日 上午9:15:58
 */
public interface MonthlyPresentRuleService {

    /**
     * 
     * @Title: selectByPrimaryKey 
     * @Description: 查询
     * @param id
     * @return
     * @return: MonthlyPresentRule
     */
    MonthlyPresentRule selectByPrimaryKey(Long id);

    /**
     * 
     * @Title: insert 
     * @Description: 生成记录
     * @param rule
     * @return
     * @return: boolean
     */
    boolean insert(MonthlyPresentRule rule);

    /**
     * 
     * @Title: getRules 
     * @Description: 查询
     * @param queryObject
     * @return
     * @return: List<MonthlyPresentRule>
     */
    List<MonthlyPresentRule> getRules(QueryObject queryObject);

    /**
     * 
     * @Title: countRules 
     * @Description: 统计
     * @param queryObject
     * @return
     * @return: Long
     */
    Long countRules(QueryObject queryObject);
   
    /**
     * 
     * @Title: updateByPrimaryKeySelective 
     * @Description: 统计
     * @param rule
     * @return
     * @return: boolean
     */
    boolean updateByPrimaryKeySelective(MonthlyPresentRule rule);
    
    /**
     * 适用于山东（赠送规则由boss维护）
     * @Title: create 
     * @Description: 创建
     * @param rule
     * @return
     * @return: String
     */
    String create(MonthlyPresentRule rule);
    
    /**
     * 
     * @Title: getDetailByRuleId 
     * @Description: 记录详情
     * @param ruleId
     * @return
     * @return: MonthlyPresentRule
     */
    MonthlyPresentRule getDetailByRuleId(Long ruleId);
    
    /**
     * 更新状态为成功
     */
    void updateRuleStatusFini();

    /**
     * 非山东（赠送规则本地维护）
     * @param initMonthlyPresentRule
     * @return
     */
    String unSdcreate(MonthlyPresentRule rule);
    
}
