/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.EnterpriseSmsTemplateMapper;
import com.cmcc.vrp.province.model.EnterpriseSmsTemplate;
import com.cmcc.vrp.province.model.SmsTemplate;
import com.cmcc.vrp.province.service.EnterpriseSmsTemplateService;
import com.cmcc.vrp.province.service.SmsTemplateService;
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
import static org.mockito.Mockito.when;

/**
 * <p>Title:EnterpriseSmsTemplateServiceImplTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年10月31日
 */
@RunWith(MockitoJUnitRunner.class)
public class EnterpriseSmsTemplateServiceImplTest {

    @InjectMocks
    EnterpriseSmsTemplateService enterpriseSmsTemplateService = new EnterpriseSmsTemplateServiceImpl();

    @Mock
    EnterpriseSmsTemplateMapper enterpriseSmsTemplateMapper;

    @Mock
    SmsTemplateService smsTemplateService;

    @Test
    public void testInsert() {

        EnterpriseSmsTemplate record = new EnterpriseSmsTemplate();

        when(enterpriseSmsTemplateMapper.insert(record)).thenReturn(0).thenReturn(1);

        assertFalse(enterpriseSmsTemplateService.insert(record));
        assertTrue(enterpriseSmsTemplateService.insert(record));

        Mockito.verify(enterpriseSmsTemplateMapper, Mockito.times(2)).insert(record);
    }

    @Test
    public void testSelectByEnterId() {

        Long enterId = 1L;
        List<Long> list = new ArrayList();
        when(enterpriseSmsTemplateMapper.selectByEnterId(enterId)).thenReturn(list);
        assertSame(list, enterpriseSmsTemplateService.selectByEnterId(enterId));
        Mockito.verify(enterpriseSmsTemplateMapper, Mockito.times(1)).selectByEnterId(enterId);
    }

    @Test
    public void testSelectSmsTemplateByEnterId() {

        Long enterId = 1L;
        List<EnterpriseSmsTemplate> list = new ArrayList();
        when(enterpriseSmsTemplateMapper.selectSmsTemplateByEnterId(enterId)).thenReturn(list);
        assertSame(list, enterpriseSmsTemplateService.selectSmsTemplateByEnterId(enterId));
        Mockito.verify(enterpriseSmsTemplateMapper, Mockito.times(1)).selectSmsTemplateByEnterId(enterId);
    }

    @Test
    public void testUpdateEnterpriseSmsTemplate() {

        Long enterId = 1L;
        List<Long> smsTemplatesList = new ArrayList();
        enterpriseSmsTemplateService.updateEnterpriseSmsTemplate(enterId, null);
        enterpriseSmsTemplateService.updateEnterpriseSmsTemplate(enterId, smsTemplatesList);
        smsTemplatesList.add(1L);
        smsTemplatesList.add(2L);
        when(enterpriseSmsTemplateMapper.deleteByEnterId(enterId)).thenReturn(1);
        when(enterpriseSmsTemplateMapper.insert(Mockito.any(EnterpriseSmsTemplate.class))).thenReturn(1);
        enterpriseSmsTemplateService.updateEnterpriseSmsTemplate(enterId, smsTemplatesList);

    }

    @Test
    public void testSetSmsTemplateForEnterprise() {

        Long enterId = 1L;
        Long smsTemplateId = 1L;
        List<EnterpriseSmsTemplate> list = new ArrayList();


        when(enterpriseSmsTemplateMapper.selectSmsTemplateByEnterId(enterId)).thenReturn(null).thenReturn(list);
        when(enterpriseSmsTemplateMapper.updateStatus(enterId, smsTemplateId, 1)).thenReturn(1);
        when(enterpriseSmsTemplateMapper.updateStatus(enterId, smsTemplateId, 0)).thenReturn(1);

        enterpriseSmsTemplateService.setSmsTemplateForEnterprise(enterId, smsTemplateId);
        enterpriseSmsTemplateService.setSmsTemplateForEnterprise(enterId, smsTemplateId);
        EnterpriseSmsTemplate enterpriseSmsTemplate = new EnterpriseSmsTemplate();
        enterpriseSmsTemplate.setStatus(1);
        list.add(enterpriseSmsTemplate);
        EnterpriseSmsTemplate enterpriseSmsTemplate1 = new EnterpriseSmsTemplate();
        enterpriseSmsTemplate1.setStatus(0);
        list.add(enterpriseSmsTemplate1);
        enterpriseSmsTemplateService.setSmsTemplateForEnterprise(enterId, 0L);
    }

    @Test
    public void testGetChoosedSmsTemplate() {

        Long enterId = 1L;
        EnterpriseSmsTemplate est = new EnterpriseSmsTemplate();
        List<EnterpriseSmsTemplate> list = new ArrayList();

        when(enterpriseSmsTemplateMapper.getChoosedSmsTemplate(enterId)).thenReturn(null).thenReturn(list);
        assertNull(enterpriseSmsTemplateService.getChoosedSmsTemplate(enterId));
        assertNull(enterpriseSmsTemplateService.getChoosedSmsTemplate(enterId));

        list.add(est);
        assertSame(est, enterpriseSmsTemplateService.getChoosedSmsTemplate(enterId));

        Mockito.verify(enterpriseSmsTemplateMapper, Mockito.times(3)).getChoosedSmsTemplate(enterId);
    }

    @Test
    public void testInsertDefaultSmsTemplate() {
        Long id = 1L;
        List<SmsTemplate> templates = new ArrayList();

        when(smsTemplateService.showSmsTemplate(Mockito.anyMap())).thenReturn(null).thenReturn(templates);
        enterpriseSmsTemplateService.insertDefaultSmsTemplate(id);
        enterpriseSmsTemplateService.insertDefaultSmsTemplate(id);

        SmsTemplate st1 = new SmsTemplate();
        SmsTemplate st2 = new SmsTemplate();
        st1.setId(1L);
        st1.setDefaultUse(1);
        st1.setDefaultHave(1);
        st2.setId(2L);
        st2.setDefaultHave(2);
        templates.add(st1);
        templates.add(st2);
        when(enterpriseSmsTemplateMapper.insert(Mockito.any(EnterpriseSmsTemplate.class))).thenReturn(-1).thenReturn(1);
        try {
            enterpriseSmsTemplateService.insertDefaultSmsTemplate(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        enterpriseSmsTemplateService.insertDefaultSmsTemplate(id);
    }
}
