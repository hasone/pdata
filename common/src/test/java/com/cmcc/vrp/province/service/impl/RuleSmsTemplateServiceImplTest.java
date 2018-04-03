/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.RuleSmsTemplateMapper;
import com.cmcc.vrp.province.model.RuleSmsTemplate;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.RuleSmsTemplateService;
import com.cmcc.vrp.util.QueryObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>Title:RuleSmsTemplateServiceImplTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月4日
 */
@RunWith(MockitoJUnitRunner.class)
public class RuleSmsTemplateServiceImplTest {

    @InjectMocks
    RuleSmsTemplateService rstService = new RuleSmsTemplateServiceImpl();

    @Mock
    RuleSmsTemplateMapper mapper;

    @Mock
    AdminRoleService adminRoleService;

    @Test
    public void testDeleteByPrimaryKey() {
        Long id = 1L;
        RuleSmsTemplate ruleSmsTemplate = new RuleSmsTemplate();

        when(mapper.selectByPrimaryKey(id)).thenReturn(ruleSmsTemplate);
        when(mapper.updateByPrimaryKeySelective(ruleSmsTemplate)).thenReturn(0).thenReturn(1);

        assertFalse(rstService.deleteByPrimaryKey(null));
        assertFalse(rstService.deleteByPrimaryKey(id));
        assertTrue(rstService.deleteByPrimaryKey(id));
        verify(mapper, Mockito.times(2)).updateByPrimaryKeySelective(ruleSmsTemplate);
    }

    @Test
    public void testInsert() {
        RuleSmsTemplate record = new RuleSmsTemplate();
        record.setName("1 2 3");
        when(mapper.insert(record)).thenReturn(0).thenReturn(1);

        assertFalse(rstService.insert(null));
        assertFalse(rstService.insert(record));
        assertTrue(rstService.insert(record));
        verify(mapper, Mockito.times(2)).insert(record);
    }

    @Test
    public void testSelectByPrimaryKey() {
        Long id = 1L;
        RuleSmsTemplate ruleSmsTemplate = new RuleSmsTemplate();
        when(mapper.selectByPrimaryKey(id)).thenReturn(ruleSmsTemplate);
        assertNull(rstService.selectByPrimaryKey(null));
        assertSame(ruleSmsTemplate, rstService.selectByPrimaryKey(id));

        verify(mapper, Mockito.times(1)).selectByPrimaryKey(id);
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        RuleSmsTemplate record = new RuleSmsTemplate();
        record.setName("1 2 3");
        when(mapper.updateByPrimaryKeySelective(record)).thenReturn(0).thenReturn(1);
        assertFalse(rstService.updateByPrimaryKeySelective(null));
        assertFalse(rstService.updateByPrimaryKeySelective(record));
        assertTrue(rstService.updateByPrimaryKeySelective(record));

        verify(mapper, Mockito.times(2)).updateByPrimaryKeySelective(record);
    }

    @Test
    public void testCount() {
        QueryObject queryObject = new QueryObject();
        Long result = 1L;
        when(mapper.count(queryObject.toMap())).thenReturn(null).thenReturn(result);
        assertSame(0L, rstService.count(null));
        assertSame(0L, rstService.count(queryObject));
        assertSame(result, rstService.count(queryObject));
        verify(mapper, Mockito.times(2)).count(queryObject.toMap());
    }

    @Test
    public void testListRuleSmsTemplate() {
        QueryObject queryObject = new QueryObject();
        List<RuleSmsTemplate> list = new ArrayList();
        when(mapper.listRuleSmsTemplate(queryObject.toMap())).thenReturn(list);
        assertNull(rstService.listRuleSmsTemplate(null));
        assertSame(list, rstService.listRuleSmsTemplate(queryObject));
        verify(mapper, Mockito.times(1)).listRuleSmsTemplate(queryObject.toMap());
    }

    @Test
    public void testGetRuleSmsTemplateByCreator() {
        Long creatorId = 1L;
        String type = "1";
        Long roleId = 1L;
        List<RuleSmsTemplate> list = new ArrayList();
        when(adminRoleService.getRoleIdByAdminId(creatorId)).thenReturn(roleId);
        when(mapper.getRuleTemplateByCreatorId(Mockito.anyMap())).thenReturn(list);
        assertNull(rstService.getRuleSmsTemplateByCreator(null, null));
        assertNull(rstService.getRuleSmsTemplateByCreator(creatorId, null));
        assertSame(list, rstService.getRuleSmsTemplateByCreator(creatorId, type));

        roleId = 2L;
        when(adminRoleService.getRoleIdByAdminId(creatorId)).thenReturn(roleId);
        assertSame(list, rstService.getRuleSmsTemplateByCreator(creatorId, type));

        verify(mapper, Mockito.times(2)).getRuleTemplateByCreatorId(Mockito.anyMap());
    }
}
