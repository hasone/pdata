/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.dao.PresentRecordMapper;
import com.cmcc.vrp.province.dao.PresentRuleMapper;
import com.cmcc.vrp.province.dao.ProductMapper;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.PresentRecord;
import com.cmcc.vrp.province.model.PresentRule;
import com.cmcc.vrp.province.model.json.post.PresentRecordJson;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivityCreatorService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.PresentRecordService;
import com.cmcc.vrp.province.service.PresentRuleService;
import com.cmcc.vrp.province.service.PresentSerialNumService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.util.QueryObject;

/**
 * <p>Title:PresentRuleServiceImpTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月3日
 */
@RunWith(MockitoJUnitRunner.class)
public class PresentRuleServiceImpTest {

    @InjectMocks
    PresentRuleService prService = new PresentRuleServiceImp();

    @Mock
    PresentRuleMapper presentRuleMapper;

    @Mock
    PresentRecordMapper presentRecordMapper;

    @Mock
    PresentRecordService presentRecordService;

    @Mock
    TaskProducer taskProducer;

    @Mock
    EnterprisesService enterprisesService;

    @Mock
    ProductMapper productMapper;

    @Mock
    GlobalConfigService globalConfigService;
    
    @Mock
    PresentSerialNumService presentSerialNumService;
    
    @Mock
    AccountService accountService;
    
    @Mock
    SerialNumService serialNumService;
    
    @Mock
    ChargeRecordService chargeRecordService;
    
    @Mock
    ActivityCreatorService activityCreatorService;

    @Test
    public void testDeleteDraft() {
        long id = 1L;
        int result = 1;
        when(presentRuleMapper.deleteByPrimaryKey(id)).thenReturn(result);
        assertSame(result, prService.deleteDraft(id));
        Mockito.verify(presentRuleMapper, Mockito.times(1)).deleteByPrimaryKey(id);
    }

    @Test
    public void testQueryCount() {
        QueryObject queryObject = new QueryObject();
        int result = 1;
        when(presentRuleMapper.selectCount(Mockito.anyMap())).thenReturn(result);
        assertSame(result, prService.queryCount(queryObject));
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
        queryObject.getQueryCriterias().put("endTime", matter1.format(new Date()));
        assertSame(result, prService.queryCount(queryObject));
        Mockito.verify(presentRuleMapper, Mockito.times(2)).selectCount(Mockito.anyMap());
    }

    @Test
    public void testQueryPage() {
        QueryObject queryObject = new QueryObject();
        List<PresentRule> list = new ArrayList();
        when(presentRuleMapper.selectPageRule(Mockito.anyMap())).thenReturn(list);
        assertSame(list, prService.queryPage(queryObject));
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
        queryObject.getQueryCriterias().put("endTime", matter1.format(new Date()));
        assertSame(list, prService.queryPage(queryObject));
        Mockito.verify(presentRuleMapper, Mockito.times(2)).selectPageRule(Mockito.anyMap());
    }

    @Test
    public void testSelectByPrimaryKey() {
        Long id = 1L;
        PresentRule presentRule = new PresentRule();
        when(presentRuleMapper.selectByPrimaryKey(id)).thenReturn(presentRule);
        assertSame(presentRule, prService.selectByPrimaryKey(id));
        Mockito.verify(presentRuleMapper, Mockito.times(1)).selectByPrimaryKey(id);
    }

    @Test
    public void testSelectRuleDetails() {
        Long id = 1L;
        PresentRule presentRule = new PresentRule();
        when(presentRuleMapper.selectRuleDetails(id)).thenReturn(presentRule);
        assertSame(presentRule, prService.selectRuleDetails(id));
        Mockito.verify(presentRuleMapper, Mockito.times(1)).selectRuleDetails(id);
    }

    @Test
    public void testAddRule() {
        Administer administer = new Administer();
        PresentRule presentRule = new PresentRule();
        List<PresentRecordJson> prs = new ArrayList();
//	
        when(activityCreatorService.insert(Mockito.any(ActivityType.class), Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);
        assertFalse(prService.addRule(null, null, null));
        assertFalse(prService.addRule(null, null, prs));
        PresentRecordJson prj = new PresentRecordJson();
        prj.setGiveNum(10);
        prs.add(prj);
        assertFalse(prService.addRule(null, null, prs));
        assertFalse(prService.addRule(administer, presentRule, prs));
        presentRule.setEntId(1L);
        assertFalse(prService.addRule(null, presentRule, prs));

        List<Enterprise> enterprises = new ArrayList();
        when(enterprisesService.getEnterpriseListByAdminId(administer)).thenReturn(null).thenReturn(enterprises);
        assertFalse(prService.addRule(administer, presentRule, prs));
        assertFalse(prService.addRule(administer, presentRule, prs));

        Enterprise e = new Enterprise();
        Enterprise e1 = new Enterprise();
        e.setId(2L);
        enterprises.add(e);
        e1.setId(1L);
        enterprises.add(e1);
        administer.setId(1L);
        when(presentRuleMapper.insertSelective(Mockito.any(PresentRule.class))).thenReturn(0).thenReturn(1);
        assertFalse(prService.addRule(administer, presentRule, prs));

        when(presentRecordService.create(Mockito.any(PresentRule.class), Mockito.anyList())).thenReturn(false).thenReturn(true);
        try {
            prService.addRule(administer, presentRule, prs);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertTrue(prService.addRule(administer, presentRule, prs));
    }

    @Test
    public void testGive() {
        Administer administer = new Administer();
        administer.setId(1L);
        PresentRule presentRule = new PresentRule();
        presentRule.setId(1L);
        presentRule.setCreatorId(2L);
        presentRule.setEntId(1L);

        when(activityCreatorService.insert(Mockito.any(ActivityType.class), Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);
        when(presentRuleMapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(presentRule);
        assertFalse(prService.give(administer, presentRule,"123456"));

        presentRule.setCreatorId(1L);
        presentRule.setStatus((byte) 1);
        assertFalse(prService.give(administer, presentRule,"123456"));

        presentRule.setStatus((byte) 0);
        List<PresentRecord> list = new ArrayList();
        PresentRecord pr = new PresentRecord();
        list.add(pr);
        when(presentRecordMapper.selectByRuleId(Mockito.anyLong())).thenReturn(list);
        when(globalConfigService.get(Mockito.anyString())).thenReturn("1");
        when(presentSerialNumService.batchInsert(Mockito.anyString(),Mockito.anyList())).thenReturn(false).thenReturn(true);
        when(accountService.minusCount(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AccountType.class), 
        	Mockito.anyDouble(), Mockito.anyString(), Mockito.anyString())).thenReturn(false).thenReturn(true);
        when(taskProducer.produceBatchPresentMsg(Mockito.anyList())).thenReturn(false).thenReturn(true);
        assertFalse(prService.give(administer, presentRule,"123456"));
        assertFalse(prService.give(administer, presentRule,"123456"));
        assertFalse(prService.give(administer, presentRule,"123456"));
        when(presentRuleMapper.updateByPrimaryKeySelective(Mockito.any(PresentRule.class))).thenReturn(1);
        when(presentSerialNumService.batchInsert(Mockito.anyString(), Mockito.anyList())).thenReturn(false).thenReturn(true);
        when(serialNumService.batchInsert(Mockito.anyList())).thenReturn(false).thenReturn(true);
        when(chargeRecordService.batchInsert(Mockito.anyList())).thenReturn(false).thenReturn(true);
        when(accountService.minusCount(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AccountType.class),
        	Mockito.anyDouble(), Mockito.anyString(), Mockito.anyString())).thenReturn(false).thenReturn(true);
        when(taskProducer.produceBatchPresentMsg(Mockito.anyList())).thenReturn(false).thenReturn(true);
        assertFalse(prService.give(administer, presentRule,"123456"));
        assertFalse(prService.give(administer, presentRule,"123456"));
        assertFalse(prService.give(administer, presentRule,"123456"));
        assertFalse(prService.give(administer, presentRule,"123456"));
        assertTrue(prService.give(administer, presentRule,"123456"));
        assertFalse(prService.give(administer, presentRule,"123456"));
    }
    
    @Test
    public void testGive2() {
        Administer administer = new Administer();
        administer.setId(1L);
        PresentRule presentRule = new PresentRule();
        presentRule.setId(1L);
        presentRule.setCreatorId(2L);
        presentRule.setEntId(1L);

        when(presentRuleMapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(presentRule);
        assertFalse(prService.give(administer, presentRule,"123456"));

        presentRule.setCreatorId(1L);
        presentRule.setStatus((byte) 1);
        assertFalse(prService.give(administer, presentRule,"123456"));

        presentRule.setStatus((byte) 0);
        List<PresentRecord> list = new ArrayList();
        PresentRecord pr = new PresentRecord();
        list.add(pr);
        when(presentRecordMapper.selectByRuleId(Mockito.anyLong())).thenReturn(list);
        when(globalConfigService.get(Mockito.anyString())).thenReturn("1");
        when(presentSerialNumService.batchInsert(Mockito.anyString(),Mockito.anyList())).thenReturn(false).thenReturn(true);
        when(serialNumService.batchInsert(Mockito.anyList())).thenReturn(false).thenReturn(true);
        when(chargeRecordService.batchInsert(Mockito.anyList())).thenReturn(false).thenReturn(true);
        when(accountService.minusCount(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AccountType.class), 
        	Mockito.anyDouble(), Mockito.anyString(), Mockito.anyString())).thenReturn(false).thenReturn(true);
        when(taskProducer.produceBatchPresentMsg(Mockito.anyList())).thenReturn(false).thenReturn(true);
        assertFalse(prService.give(administer, presentRule,"123456"));
        assertFalse(prService.give(administer, presentRule,"123456"));
        assertFalse(prService.give(administer, presentRule,"123456"));
        assertFalse(prService.give(administer, presentRule,"123456"));
        assertTrue(prService.give(administer, presentRule,"123456"));
        when(presentRuleMapper.updateByPrimaryKeySelective(Mockito.any(PresentRule.class))).thenReturn(1);
        when(presentRecordService.batchUpdateStatusCode(Mockito.anyList(), Mockito.anyString())).thenReturn(false).thenReturn(true);
        assertFalse(prService.give(administer, presentRule,"123456"));
    }

    @Test
    public void testUpdateRule() {
        Administer administer = new Administer();
        PresentRule presentRule = new PresentRule();
        String[] phones = {"18867101111", "18867102222"};
        Integer type = 1;
        presentRule.setId(1L);
        when(presentRuleMapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(null).thenReturn(presentRule);
        assertFalse(prService.updateRule(administer, presentRule, phones, type));
        when(presentRuleMapper.updateByPrimaryKeySelective(presentRule)).thenReturn(0).thenReturn(1);
        assertFalse(prService.updateRule(administer, presentRule, phones, type));
        when(presentRecordMapper.deleteByRuleId(presentRule.getId())).thenReturn(0).thenReturn(1);
        assertFalse(prService.updateRule(administer, presentRule, phones, type));
        assertTrue(prService.updateRule(administer, presentRule, phones, type));
        Mockito.verify(presentRuleMapper, Mockito.times(4)).selectByPrimaryKey(Mockito.anyLong());
        Mockito.verify(presentRuleMapper, Mockito.times(3)).updateByPrimaryKeySelective(presentRule);
        Mockito.verify(presentRecordMapper, Mockito.times(2)).deleteByRuleId(presentRule.getId());
    }

    @Test
    public void testIsSameAdminCreated() {
        Administer administer = new Administer();
        Long ruleId = 1L;
        PresentRule rule = new PresentRule();
        when(presentRuleMapper.selectByPrimaryKey(ruleId)).thenReturn(null).thenReturn(rule);
        assertFalse(prService.isSameAdminCreated(administer, ruleId));
        administer.setId(1L);
        rule.setCreatorId(2L);
        assertFalse(prService.isSameAdminCreated(administer, ruleId));
        rule.setCreatorId(1L);
        assertTrue(prService.isSameAdminCreated(administer, ruleId));
        Mockito.verify(presentRuleMapper, Mockito.times(3)).selectByPrimaryKey(ruleId);
    }

    @Test
    public void testChargeAgain() {
        Long id = 1L;
        PresentRecord record = new PresentRecord();
        record.setRuleId(1L);
        record.setStatus((byte) 0);
        PresentRule presentRule = new PresentRule();
        when(presentRecordMapper.selectByPrimaryKey(id)).thenReturn(null).thenReturn(record);
        when(presentRuleMapper.selectByPrimaryKey(record.getRuleId())).thenReturn(null).thenReturn(presentRule);
        assertFalse(prService.chargeAgain(id));
        assertFalse(prService.chargeAgain(id));
        assertFalse(prService.chargeAgain(id));

        record.setStatus((byte) 4);
        when(presentRecordMapper.updateByPrimaryKeySelective(record)).thenReturn(0).thenReturn(1);
        when(taskProducer.produceBatchPresentMsg(Mockito.anyList())).thenReturn(true);
        assertFalse(prService.chargeAgain(id));
        record.setStatus((byte) 4);
        assertTrue(prService.chargeAgain(id));
    }
}
