package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.SmsRecordMapper;
import com.cmcc.vrp.province.model.SmsRecord;
import com.cmcc.vrp.province.service.SmsRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @ClassName: SmsRecordServiceImplTest
 * @Description: 手机验证码记录
 * @author: qinqinyan
 * @date: 2016年5月26日
 */
@RunWith(MockitoJUnitRunner.class)
public class SmsRecordServiceImplTest {
    @InjectMocks
    SmsRecordService smsRecordService = new SmsRecordServiceImpl();
    @Mock
    SmsRecordMapper smsRecordMapper;

    /*
     * 获取短信记录
     */
    @Test
    public void testGet() {
        //invalid
        assertNull(smsRecordService.get(invalidSmsRecord().getMobile()));
        assertNull(smsRecordService.get(nullMobile().getMobile()));
        assertNull(smsRecordService.get(nullCreateTime().getMobile()));
        assertNull(smsRecordService.get(nullDeleteFlag().getMobile()));
        assertNull(smsRecordService.get(nullContent().getMobile()));

        //valid
        SmsRecord record = validSmsRecord();
        Mockito.when(smsRecordMapper.get(record.getMobile())).thenReturn(record);
        assertNotNull(smsRecordService.get(record.getMobile()));
        Mockito.verify(smsRecordMapper).get(record.getMobile());
    }

    /*
     * 删除短信记录
     */
    @Test
    public void testDelete() {
        //invalid
        assertFalse(smsRecordService.delete(invalidSmsRecord().getMobile()));
        assertFalse(smsRecordService.delete(nullMobile().getMobile()));

        //valid
        SmsRecord record = validSmsRecord();
        Mockito.when(smsRecordMapper.delete(record.getMobile())).thenReturn(1);
        assertTrue(smsRecordService.delete(record.getMobile()));
        Mockito.verify(smsRecordMapper).delete(record.getMobile());
    }

    /*
     * 插入短信记录
     */
    @Test
    public void testInsert() {
        //invalid
        assertFalse(smsRecordService.insert(invalidSmsRecord()));
        assertFalse(smsRecordService.insert(nullMobile()));
        assertFalse(smsRecordService.insert(nullContent()));
        assertFalse(smsRecordService.insert(nullCreateTime()));
        assertFalse(smsRecordService.insert(nullDeleteFlag()));

        //valid
        SmsRecord record = validSmsRecord();
        Mockito.when(smsRecordMapper.get(record.getMobile())).thenReturn(null);
        Mockito.when(smsRecordMapper.insert(record)).thenReturn(1);
        assertTrue(smsRecordService.insert(record));
//        Mockito.verify(smsRecordMapper).get(record.getMobile());
//        Mockito.verify(smsRecordMapper).insert(record);
    }

    @Test
    public void testUpdate() {
        //valid
        SmsRecord record = validSmsRecord();
        Mockito.when(smsRecordMapper.get(record.getMobile())).thenReturn(record);
        Mockito.when(smsRecordMapper.update(record)).thenReturn(1);
        assertFalse(smsRecordService.insert(record));
//        Mockito.verify(smsRecordMapper).get(record.getMobile());
//        Mockito.verify(smsRecordMapper).update(record);
    }

    private SmsRecord invalidSmsRecord() {
        SmsRecord record = new SmsRecord();
        record.setId(null);
        record.setMobile("18867545");
        record.setContent("测试短信内容");
        record.setCreateTime(new Date());
        record.setDeleteFlag(0);
        return record;
    }

    private SmsRecord nullMobile() {
        SmsRecord record = new SmsRecord();
        record.setMobile(null);
        record.setContent("测试短信内容");
        record.setCreateTime(new Date());
        record.setDeleteFlag(0);
        return record;
    }

    private SmsRecord nullContent() {
        SmsRecord record = new SmsRecord();
        record.setMobile("18867103717");
        record.setContent(null);
        record.setCreateTime(new Date());
        record.setDeleteFlag(0);
        return record;
    }

    private SmsRecord nullCreateTime() {
        SmsRecord record = new SmsRecord();
        record.setMobile("18867103717");
        record.setContent("测试短信内容");
        record.setCreateTime(null);
        record.setDeleteFlag(0);
        return record;
    }

    private SmsRecord nullDeleteFlag() {
        SmsRecord record = new SmsRecord();
        record.setMobile("18867103717");
        record.setContent("测试短信内容");
        record.setCreateTime(new Date());
        record.setDeleteFlag(null);
        return record;
    }

    private SmsRecord validSmsRecord() {
        SmsRecord record = new SmsRecord();
        record.setId(1L);
        record.setMobile("18867102222");
        record.setContent("测试短信内容！");
        record.setCreateTime(new Date());
        record.setDeleteFlag(0);
        record.setUpdateTime(new Date());
        return record;
    }
}
