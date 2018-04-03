package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.SerialNumMapper;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.util.Constants;

/**
 * Created by sunyiwei on 16/8/29.
 */
@RunWith(MockitoJUnitRunner.class)
public class SerialNumServiceImplTest {
    @InjectMocks
    SerialNumService service = new SerialNumServiceImpl();

    @Mock
    SerialNumMapper mapper;
    
    private static String randStr(int length) {
        StringBuilder sb = new StringBuilder();

        Random r = new Random();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + r.nextInt(26)));
        }

        return sb.toString();
    }
    
    @Test
    public void testInsert(){
        assertFalse(service.insert(null));
        SerialNum sn = new SerialNum();        
        assertFalse(service.insert(sn));
        
        sn.setPlatformSerialNum("11");
        when(mapper.insert(Mockito.any(SerialNum.class))).thenReturn(0);
        assertFalse(service.insert(sn));
        when(mapper.insert(Mockito.any(SerialNum.class))).thenReturn(1);
        assertTrue(service.insert(sn));
    
        verify(mapper,times(2)).insert(Mockito.any(SerialNum.class));
    }
    
    @Test
    public void testBatchInsert(){
        assertFalse(service.batchInsert(null));
        assertFalse(service.batchInsert(new ArrayList()));
        
        ArrayList list1 = new ArrayList();
        SerialNum sn = new SerialNum();        
        list1.add(sn);
        assertFalse(service.batchInsert(list1));
        
        when(mapper.batchInsert(Mockito.anyList())).thenReturn(build().size());
        assertTrue(service.batchInsert(build()));
        
        when(mapper.batchInsert(Mockito.anyList())).thenReturn(1);
        assertFalse(service.batchInsert(build()));
        
        verify(mapper,times(2)).batchInsert(Mockito.anyList());
    }
    
    @Test
    public void testGetByPltSerialNum(){
        assertNull(service.getByPltSerialNum(null));
        
        when(mapper.getByPltSerialNum("11")).thenReturn(new SerialNum());
        assertNotNull(service.getByPltSerialNum("11"));
        verify(mapper).getByPltSerialNum("11");
    }
    
    @Test
    public void testGetByBossReqSerialNum(){
        assertNull(service.getByBossReqSerialNum(null));
        
        when(mapper.getByBossReqSerialNum("11")).thenReturn(new SerialNum());
        assertNotNull(service.getByBossReqSerialNum("11"));
        verify(mapper).getByBossReqSerialNum("11");
    }
    
    @Test
    public void testGetByBossRespSerialNum(){
        assertNull(service.getByBossRespSerialNum(null));
        
        when(mapper.getByBossRespSerialNum("11")).thenReturn(new SerialNum());
        assertNotNull(service.getByBossRespSerialNum("11"));
        verify(mapper).getByBossRespSerialNum("11");
    }
    
    @Test
    public void testUpdateSerial(){
        assertFalse(service.updateSerial(null));
        
        when(mapper.update(Mockito.any(SerialNum.class))).thenReturn(1);
        assertTrue(service.updateSerial(buildSingle()));
        
        when(mapper.update(Mockito.any(SerialNum.class))).thenReturn(0);
        assertFalse(service.updateSerial(buildSingle()));
        
        verify(mapper,times(2)).update(Mockito.any(SerialNum.class));
    }

    private List<SerialNum> build() {
        List<SerialNum> sns = new LinkedList<SerialNum>();

        for (int i = 0; i < 10; i++) {
            SerialNum serialNum = new SerialNum();

            serialNum.setBossReqSerialNum(randStr(15));
            serialNum.setBossRespSerialNum(randStr(15));
            serialNum.setCreateTime(new Date());
            serialNum.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
            serialNum.setEcSerialNum(randStr(15));
            serialNum.setPlatformSerialNum(randStr(15));
            serialNum.setUpdateTime(new Date());

            sns.add(serialNum);
        }

        return sns;
    }

//    @Test
//    public void testUpdateSerial() throws Exception {
//        assertTrue(serialNumService.updateSerial(buildSingle()));
//    }

    private SerialNum buildSingle() {
        SerialNum serialNum = new SerialNum();

        serialNum.setBossReqSerialNum(randStr(15));
        serialNum.setBossRespSerialNum(randStr(15));
        serialNum.setCreateTime(new Date());
        serialNum.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        serialNum.setEcSerialNum(randStr(15));
        serialNum.setPlatformSerialNum("ba5cf1ed9629487fc880887993e1b869f8a51a1a");
        serialNum.setUpdateTime(new Date());

        return serialNum;
    }

    @Test
    public void testLength() throws Exception {
        System.out.println("zhejiang is not return response serailNum".length());
    }
}