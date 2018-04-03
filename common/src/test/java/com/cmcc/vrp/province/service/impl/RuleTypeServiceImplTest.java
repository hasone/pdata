package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.RuleTypeMapper;
import com.cmcc.vrp.province.model.RuleType;
import com.cmcc.vrp.province.service.RuleTypeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertSame;

/**
 * @author qinqinyan
 * @description 规则类型服务类
 */
@RunWith(MockitoJUnitRunner.class)
public class RuleTypeServiceImplTest {

    @InjectMocks
    RuleTypeService ruleTypeService = new RuleTypeServiceImpl();
    @Mock
    RuleTypeMapper ruleTypeMapper;

    @Test
    public void testListRuleType() {
        List<RuleType> list = new ArrayList();
        Mockito.when(ruleTypeMapper.listRuleType()).thenReturn(list);

        assertSame(list, ruleTypeService.listRuleType());

    }

}
