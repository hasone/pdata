package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Qualifier;

import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.BossServiceProxyService;
import com.cmcc.vrp.province.dao.MdrcCardInfoMapper;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigStatusRecordService;
import com.cmcc.vrp.province.mdrc.service.impl.MdrcCardInfoServiceImpl;
import com.cmcc.vrp.province.model.MdrcCardInfo;
import com.cmcc.vrp.province.service.CardNumAndPwdService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterpriseSmsTemplateService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.queue.TaskProducer;

 /**
  * 
  * @ClassName: MdrcBatchConfigServiceImplTest 
  * @Description: TODO
  * @author: Rowe
  * @date: 2017年8月18日 上午10:28:35
  */
@RunWith(MockitoJUnitRunner.class)
public class MdrcCardInfoServiceImplTest {

    @InjectMocks
    MdrcCardInfoServiceImpl mdrcCardInfoService = new MdrcCardInfoServiceImpl();   
    @Mock
    MdrcCardInfoMapper mdrcCardInfoMapper;
    @Mock
    MdrcBatchConfigService mdrcBatchConfigService;
    @Mock
    TaskProducer taskProducer;
    @Mock
    private EnterprisesService enterprisesService;
    @Mock
    @Qualifier("haiNanBossService")
    private BossService haiNanBossService;
    @Mock
    private EntProductService entProductService;
    @Mock
    private ProductService productService;
    @Mock
    private EnterpriseSmsTemplateService enterpriseSmsTemplateService;
    @Mock
    private BossServiceProxyService bossServiceProxyService;

    @Mock
    private GlobalConfigService globalConfigService;

    @Mock
    private CardNumAndPwdService cardNumAndPwdService;

    @Mock
    private MdrcBatchConfigStatusRecordService mdrcBatchConfigStatusRecordService;
    
    
    @Test
    public void testBatchInsert() {
        List<MdrcCardInfo> records = new ArrayList<MdrcCardInfo>();
        records.add(new MdrcCardInfo());
        
        Mockito.when(mdrcCardInfoMapper.batchInsert(any(Integer.class), any(List.class))).thenReturn(1);
        assertTrue(mdrcCardInfoService.batchInsert(17, records) == 1);

        for(int i = 0 ; i <= 50000 ; i++){
            records.add(new MdrcCardInfo());
        }
        assertTrue(mdrcCardInfoService.batchInsert(17, records) == 50002);

    }
    
    @Test
    public void testSelectByPrimaryKey() {
        MdrcCardInfo record = new MdrcCardInfo();
        Mockito.when(mdrcCardInfoMapper.selectByPrimaryKey(any(Integer.class), any(Long.class))).thenReturn(record);
        assertTrue(mdrcCardInfoService.selectByPrimaryKey(13, 12L).equals(record));
    }
    
    @Test
    public void testQueryMdrcList() {
        List<MdrcCardInfo> records = new ArrayList<MdrcCardInfo>();
        Mockito.when(mdrcCardInfoMapper.queryMdrcList(any(Map.class))).thenReturn(records);
        assertTrue(mdrcCardInfoService.queryMdrcList(null).equals(records));
    }
    
    
    @Test
    public void testQueryMdrcCount() {
        Mockito.when(mdrcCardInfoMapper.queryMdrcCount(any(Map.class))).thenReturn(1L);
        assertTrue(mdrcCardInfoService.queryMdrcCount(null) == 1);
    }

  
    @Test
    public void testBatchExpire() {
        assertTrue(mdrcCardInfoService.batchExpire(14, null) == 0);

        Mockito.when(mdrcCardInfoMapper.batchExpire(any(Integer.class), any(List.class))).thenReturn(1);
        List<String> list =  new ArrayList<String>();
        list.add("123456");
        assertTrue(mdrcCardInfoService.batchExpire(14, list) == 1);

    }
    
    @Test
    public void testExpire() {
        assertFalse(mdrcCardInfoService.expire(null));
        MdrcCardInfo mdrcCardInfo = new MdrcCardInfo();
        Mockito.when(cardNumAndPwdService.get(any(String.class))).thenReturn(null);
        assertFalse(mdrcCardInfoService.expire("123456"));
        
        Mockito.when(cardNumAndPwdService.get(any(String.class))).thenReturn(mdrcCardInfo);
        Mockito.when(mdrcCardInfoMapper.expire(any(Integer.class), any(String.class))).thenReturn(1);
        assertTrue(mdrcCardInfoService.expire("123456"));

    }
    
    @Test
    public void testGet() {
        MdrcCardInfo mdrcCardInfo = new MdrcCardInfo();
        Mockito.when(cardNumAndPwdService.get(any(String.class))).thenReturn(mdrcCardInfo);        
        assertTrue(mdrcCardInfoService.get("123456").equals(mdrcCardInfo));

    }

}

