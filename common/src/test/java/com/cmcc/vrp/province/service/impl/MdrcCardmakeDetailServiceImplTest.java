package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.MdrcCardmakeDetailMapper;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.mdrc.service.MdrcCardmakerService;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.model.MdrcCardmakeDetail;
import com.cmcc.vrp.province.model.MdrcCardmaker;
import com.cmcc.vrp.province.model.MdrcMakecardRequestConfig;
import com.cmcc.vrp.province.quartz.service.ScheduleService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.CardNumAndPwdService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.MdrcMakecardRequestConfigService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.SmsPojo;

/**
 * Created by qinqinyan on 2016/11/30.
 * 营销卡制卡服务测试类
 */
@RunWith(MockitoJUnitRunner.class)
public class MdrcCardmakeDetailServiceImplTest {

    @InjectMocks
    MdrcCardmakeDetailServiceImpl mdrcCardmakeDetailService = new MdrcCardmakeDetailServiceImpl();
    @Mock
    MdrcCardmakeDetailMapper mdrcCardmakeDetailMapper;

    @Mock
    TaskProducer taskProducer;

    @Mock
    MdrcCardmakerService mdrcCardmakerService;

    @Mock
    ManagerService managerService;

    @Mock
    MdrcBatchConfigService mdrcBatchConfigService;

    @Mock
    MdrcCardInfoService mdrcCardInfoService;

    @Mock
    ScheduleService scheduleService;

    @Mock
    MdrcMakecardRequestConfigService mdrcMakecardRequestConfigService;

    @Mock
    GlobalConfigService globalConfigService;

    @Mock
    CardNumAndPwdService mdrcCardNumAndPwdService;

    @Mock
    AdministerService administerService;

    private MdrcCardmaker createMdrcCardmaker(){
        MdrcCardmaker mdrcCardmaker = new MdrcCardmaker();
        mdrcCardmaker.setId(1L);
        return mdrcCardmaker;
    }
    
    private Manager createManager(){
        Manager manager = new Manager();
        manager.setId(1L);
        return manager;
    }
    
    private MdrcBatchConfig createMdrcBatchConfig(){
        MdrcBatchConfig config = new MdrcBatchConfig();
        config.setId(1L);
        return config;
    }
    
    @Test
    public void testMakecard() {
        List<MdrcBatchConfig> configList = new ArrayList<MdrcBatchConfig>();
        configList.add(createMdrcBatchConfig());
        
        Mockito.when(mdrcCardmakeDetailMapper.selectByRequestId(Mockito.anyLong())).thenReturn(createMdrcCardmakeDetail());
        Mockito.when(mdrcCardmakerService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(createMdrcCardmaker());
        Mockito.when(managerService.getManagerByAdminId(Mockito.anyLong())).thenReturn(createManager());
        Mockito.when(mdrcBatchConfigService.selectByYearAndProvinceCode(Mockito.anyString(), Mockito.anyString())).thenReturn(configList);
        
        Mockito.when(mdrcBatchConfigService.insertSelective(Mockito.any(MdrcBatchConfig.class))).thenReturn(0l);
        assertFalse(mdrcCardmakeDetailService.makecard(1L, 1L));
    }

    @Test
    public void testDeleteByPrimaryKey() {
        assertFalse(mdrcCardmakeDetailService.deleteByPrimaryKey(null));
        Mockito.when(mdrcCardmakeDetailMapper.deleteByPrimaryKey(anyLong())).thenReturn(1);
        assertTrue(mdrcCardmakeDetailService.deleteByPrimaryKey(1L));
        Mockito.verify(mdrcCardmakeDetailMapper).deleteByPrimaryKey(anyLong());
    }

    @Test
    public void testInsert() {
        assertFalse(mdrcCardmakeDetailService.insert(null));
        Mockito.when(mdrcCardmakeDetailMapper.insert(any(MdrcCardmakeDetail.class))).thenReturn(1);
        assertTrue(mdrcCardmakeDetailService.insert(createMdrcCardmakeDetail()));
        Mockito.verify(mdrcCardmakeDetailMapper).insert(any(MdrcCardmakeDetail.class));
    }

    @Test
    public void testInsertSelective() {
        assertFalse(mdrcCardmakeDetailService.insertSelective(null));
        Mockito.when(mdrcCardmakeDetailMapper.insertSelective(any(MdrcCardmakeDetail.class))).thenReturn(1);
        assertTrue(mdrcCardmakeDetailService.insertSelective(createMdrcCardmakeDetail()));
        Mockito.verify(mdrcCardmakeDetailMapper).insertSelective(any(MdrcCardmakeDetail.class));
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        assertFalse(mdrcCardmakeDetailService.updateByPrimaryKeySelective(null));
        Mockito.when(mdrcCardmakeDetailMapper.updateByPrimaryKeySelective(any(MdrcCardmakeDetail.class))).thenReturn(1);
        assertTrue(mdrcCardmakeDetailService.updateByPrimaryKeySelective(createMdrcCardmakeDetail()));
        Mockito.verify(mdrcCardmakeDetailMapper).updateByPrimaryKeySelective(any(MdrcCardmakeDetail.class));
    }

    @Test
    public void testUpdateByPrimaryKey() {
        assertFalse(mdrcCardmakeDetailService.updateByPrimaryKey(null));
        Mockito.when(mdrcCardmakeDetailMapper.updateByPrimaryKey(any(MdrcCardmakeDetail.class))).thenReturn(1);
        assertTrue(mdrcCardmakeDetailService.updateByPrimaryKey(createMdrcCardmakeDetail()));
        Mockito.verify(mdrcCardmakeDetailMapper).updateByPrimaryKey(any(MdrcCardmakeDetail.class));
    }

    @Test
    public void testSelectByPrimaryKey() {
        assertNull(mdrcCardmakeDetailService.selectByPrimaryKey(null));
        Mockito.when(mdrcCardmakeDetailMapper.selectByPrimaryKey(anyLong())).thenReturn(createMdrcCardmakeDetail());
        assertNotNull(mdrcCardmakeDetailService.selectByPrimaryKey(1L));
        Mockito.verify(mdrcCardmakeDetailMapper).selectByPrimaryKey(anyLong());
    }

    @Test
    public void testSelectByRequestId() {
        assertNull(mdrcCardmakeDetailService.selectByRequestId(null));
        Mockito.when(mdrcCardmakeDetailMapper.selectByRequestId(anyLong())).thenReturn(createMdrcCardmakeDetail());
        assertNotNull(mdrcCardmakeDetailService.selectByRequestId(1L));
        Mockito.verify(mdrcCardmakeDetailMapper).selectByRequestId(anyLong());
    }

    @Test
    @Ignore
    public void testMakcard() {
        Mockito.when(mdrcCardmakeDetailMapper.selectByRequestId(anyLong())).thenReturn(createMdrcCardmakeDetail());
        Mockito.when(mdrcCardmakerService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(new MdrcCardmaker());
        Mockito.when(globalConfigService.get(Mockito.anyString())).thenReturn("12");
        Mockito.when(mdrcBatchConfigService.selectByYearAndProvinceCode(Mockito.anyString(), Mockito.anyString())).thenReturn(null);


        List<String> cardNumberList = new ArrayList<String>();
        cardNumberList.add("11");
        cardNumberList.add("11");
        cardNumberList.add("11");
        cardNumberList.add("11");
        Mockito.when(mdrcCardNumAndPwdService.generatCardNums(Mockito.any(MdrcBatchConfig.class))).thenReturn(cardNumberList);

        MdrcBatchConfig config = new MdrcBatchConfig();
        config.setAmount(1L);
        Mockito.when(managerService.getManagerByAdminId(Mockito.anyLong())).thenReturn(new Manager());

        Mockito.when(mdrcCardInfoService.batchInsert(Mockito.anyInt(), Mockito.anyList())).thenReturn(1);

        Mockito.when(scheduleService.createScheduleJob(Mockito.any(Class.class), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), Mockito.any(Date.class))).thenReturn("success");

        Mockito.when(administerService.queryAllUsersByAuthName(Mockito.anyString())).thenReturn(new ArrayList<Administer>());

        Mockito.when(mdrcMakecardRequestConfigService.insertSelective(Mockito.any(MdrcMakecardRequestConfig.class))).thenReturn(true);

        assertTrue(mdrcCardmakeDetailService.makecard(1L, 1L));


        List<Administer> administers = new ArrayList<Administer>();
        administers.add(new Administer());
        Mockito.when(administerService.queryAllUsersByAuthName(Mockito.anyString())).thenReturn(administers);
        Mockito.when(taskProducer.produceDeliverNoticeSmsMsg(Mockito.any(SmsPojo.class))).thenReturn(true);
        assertTrue(mdrcCardmakeDetailService.makecard(1L, 1L));

    }

    private MdrcCardmakeDetail createMdrcCardmakeDetail() {
        MdrcCardmakeDetail record = new MdrcCardmakeDetail();
        record.setId(1L);
        record.setCardmakerId(2L);
        record.setAmount(1L);
        record.setAmount(2l);
        return record;
    }

    private List<MdrcBatchConfig> buildConfig(int length) {
        List<MdrcBatchConfig> list = new ArrayList<MdrcBatchConfig>();
        for (int i = 0; i < length; i++) {
            MdrcBatchConfig config = new MdrcBatchConfig();
            config.setThisYear("5000");
            config.setProvinceCode("code");
            config.setSerialNumber(i);
            list.add(config);
        }
        return list;
    }

    private List<String> buildNums(int length) {
        List<String> nums = new ArrayList<String>();
        for (int i = 0; i < length; i++) {
            nums.add(String.valueOf(i));
        }
        return nums;
    }


}

