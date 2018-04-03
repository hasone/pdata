package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.SmsTemplateMapper;
import com.cmcc.vrp.province.model.SmsTemplate;
import com.cmcc.vrp.province.service.SmsTemplateService;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * 企业管理service单元测试
 *
 * @author kok
 */
@RunWith(EasyMockRunner.class)
public class SmsTemplateServiceTest {

    @TestSubject
    SmsTemplateService eService = new SmsTemplateServiceImpl();

    @Mock
    SmsTemplateMapper mapper;

    SmsTemplate sTemplate = new SmsTemplate();

    @Test
    public void deleteByPrimaryKeyTest() {
        assertEquals(eService.deleteByPrimaryKey(0L), false);
        
        // 录制预期的行为
        expect(mapper.deleteByPrimaryKey(1l)).andReturn(1);
        replay(mapper);
        assertEquals(eService.deleteByPrimaryKey(1l), true);
        verify(mapper);
    }    
    @Test
    public void deleteByPrimaryKeyTest2() {
        // 录制预期的行为
        expect(mapper.deleteByPrimaryKey(1l)).andReturn(0);
        replay(mapper);
        assertEquals(eService.deleteByPrimaryKey(1l), false);
        verify(mapper);
    }

    @Test
    public void insertSelectiveTest1() {
        SmsTemplate record = new SmsTemplate();
        assertEquals(eService.insertSelective(null), false);
        assertEquals(eService.insertSelective(record), false);
        record.setName("");
        assertEquals(eService.insertSelective(record), false);
        
        sTemplate.setContent("余额不足");
        sTemplate.setName("欠费提醒");

        // 录制预期的行为
        expect(mapper.insertSelective(sTemplate)).andReturn(1);
        replay(mapper);
        assertEquals(eService.insertSelective(sTemplate), true);
        verify(mapper);
    }
    
    @Test
    public void insertSelectiveTest2() {
        sTemplate.setContent("余额不足");
        sTemplate.setName("欠费提醒");

        // 录制预期的行为
        expect(mapper.insertSelective(sTemplate)).andReturn(0);
        replay(mapper);
        assertEquals(eService.insertSelective(sTemplate), false);
        verify(mapper);
    }


    @Test
    public void selectByPrimaryKeyTest() {

        sTemplate.setName("欠费提醒");
        // 录制预期的行为
        expect(mapper.selectByPrimaryKey(1l)).andReturn(sTemplate);
        replay(mapper);
        assertEquals(eService.selectByPrimaryKey(1l).getName(), "欠费提醒");
        verify(mapper);
    }

    @Test
    public void updateByPrimaryKeySelectiveTest1() {
        SmsTemplate record = new SmsTemplate();
        assertEquals(eService.updateByPrimaryKeySelective(null), false);
        record.setId(0L);
        assertEquals(eService.updateByPrimaryKeySelective(record), false);
        
        sTemplate.setName("CJ290");
        sTemplate.setId(1l);
        // 录制预期的行为
        expect(mapper.updateByPrimaryKeySelective(sTemplate)).andReturn(1);
        replay(mapper);
        assertEquals(eService.updateByPrimaryKeySelective(sTemplate), true);
        verify(mapper);
    }
    
    @Test
    public void updateByPrimaryKeySelectiveTest2() {

        sTemplate.setName("CJ290");
        sTemplate.setId(1l);
        // 录制预期的行为
        expect(mapper.updateByPrimaryKeySelective(sTemplate)).andReturn(0);
        replay(mapper);
        assertEquals(eService.updateByPrimaryKeySelective(sTemplate), false);
        verify(mapper);
    }

    @Test
    public void showsTemplateForPageResultTest() {

        List<SmsTemplate> list = new ArrayList<SmsTemplate>();
        sTemplate.setName("CJ290");
        list.add(sTemplate);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("index", "1");
        map.put("pageSize", "10");

        // 录制预期的行为
        expect(mapper.showSmsTemplate(map)).andReturn(list);
        replay(mapper);

        assertEquals(eService.showSmsTemplate(map).size(), 1);
        verify(mapper);
    }
    
    @Test
    @Ignore
    public void testCountSmsTemplate() {
        // 录制预期的行为
        expect(mapper.countSmsTemplate(Mockito.anyMap())).andReturn(1);
        replay(mapper);
        assertEquals(eService.countSmsTemplate(new HashMap()),1);
        verify(mapper);
    }
    
    @Test
    @Ignore
    public void testShowSmsTemplate() {
        // 录制预期的行为
        expect(mapper.showSmsTemplate(Mockito.anyMap())).andReturn(new ArrayList());
        replay(mapper);
        assertNotNull(eService.showSmsTemplate(new HashMap()));
        verify(mapper);
    }
    
    // savagechen11 delete 20170116
    @Test
    @Ignore
    public void testCheckSms() {
        // 录制预期的行为
        expect(mapper.checkSms(Mockito.anyString())).andReturn(new ArrayList());
        replay(mapper);
        assertNotNull(eService.checkSms(Mockito.anyString()));
        verify(mapper);
    }
    
    @Test
    public void testGet() {
        assertNull(eService.get(""));
        // 录制预期的行为
        expect(mapper.get("11")).andReturn(new SmsTemplate());
        replay(mapper);
        assertNotNull(eService.get("11"));
        verify(mapper);
    }
}
