package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.RuleTypeMapper;
import com.cmcc.vrp.province.model.RuleType;
import com.cmcc.vrp.province.service.RuleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ruleTypeService")
public class RuleTypeServiceImpl implements RuleTypeService {

    @Autowired
    RuleTypeMapper mapper;

    public List<RuleType> listRuleType() {
        return mapper.listRuleType();
    }


}
