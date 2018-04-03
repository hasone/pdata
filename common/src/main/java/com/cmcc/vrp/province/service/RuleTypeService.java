package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.RuleType;

import java.util.List;

/**
 * RuleTypeService.java
 */
public interface RuleTypeService {


    /**
     * 查询记录列表
     * <p>
     *
     * @param queryObject
     * @return
     */
    List<RuleType> listRuleType();


}
