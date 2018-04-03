/**
 * 
 */
package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.sichuan.model.SCBalanceRequest;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceResponse;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceResponseOutData;
import com.cmcc.vrp.boss.sichuan.service.SCBalanceService;
import com.cmcc.vrp.province.dao.AccountMapper;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.AccountRecord;
import com.cmcc.vrp.province.model.EntECRecord;
import com.cmcc.vrp.province.model.EntStatusRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.AccountRecordService;
import com.cmcc.vrp.province.service.EntECRecordService;
import com.cmcc.vrp.province.service.EntStatusRecordService;
import com.cmcc.vrp.province.service.EnterpriseUserIdService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
/**
 * <p>Title:HuNanAccountServiceImplTest </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年11月8日
 */
@RunWith(MockitoJUnitRunner.class)
public class ScAccountServiceImplTest {

    @InjectMocks
    ScAccountServiceImpl aService = new ScAccountServiceImpl();
    
    @Mock
    EnterprisesService enterprisesService;

    @Mock
    EnterpriseUserIdService enterpriseUserIdService;
    
    @Mock
    AccountMapper accountMapper;
    
    @Mock
    AccountRecordService accountRecordService;
    
    @Mock
    SCBalanceService scBalanceService;
    
    @Mock
    GlobalConfigService globalConfigService;
    
    @Mock
    EntStatusRecordService entStatusRecordService;
    
    @Mock
    EntECRecordService entECRecordService;
    
    @Test
    public void testSyncFromBoss() {
    	Long entId = 1L;
    	Long prdId = 1L;
    	
    	Enterprise enterprise = new Enterprise();
    	
    	when(enterprisesService.selectByPrimaryKey(entId)).thenReturn(null).thenReturn(enterprise);
    	assertSame("企业不存在", aService.syncFromBoss(entId, prdId).getResult());
    	
    	when(globalConfigService.get(GlobalConfigKeyEnum.SC_MIGU_CODE.getKey())).thenReturn("");
    	
    	Account platAccount = new Account();
    	platAccount.setCount(10000.0);
    	platAccount.setOwnerId(1L);
    	platAccount.setEnterId(1L);
    	platAccount.setProductId(1L);
    	when(accountMapper.selectCurrencyAccount(entId)).thenReturn(null).thenReturn(platAccount);
    	assertSame("平台现金账户不存在", aService.syncFromBoss(entId, prdId).getResult());	
    	
    	
    	when(globalConfigService.get(GlobalConfigKeyEnum.SC_MIGU_CODE.getKey())).thenReturn("11");
    	when(enterpriseUserIdService.getUserIdByEnterpriseCode(Mockito.anyString())).thenReturn("");
    	assertSame("该企业无法查询BOSS余额", aService.syncFromBoss(entId, prdId).getResult());    
    	
    	when(enterpriseUserIdService.getUserIdByEnterpriseCode(Mockito.anyString())).thenReturn("202");
    	SCBalanceResponse response = new SCBalanceResponse();
    	//response.setResCode("0000000");
    	when(scBalanceService.sendBalanceRequest(Mockito.any(SCBalanceRequest.class))).thenReturn(response); 
    	assertSame("查询boss资金账户失败", aService.syncFromBoss(entId, prdId).getResult());
    
        response.setResCode("0000000");
        SCBalanceResponseOutData outData = new SCBalanceResponseOutData();
        outData.setPREPAY_FEE("10000");
        response.setOutData(outData);
        when(scBalanceService.sendBalanceRequest(Mockito.any(SCBalanceRequest.class))).thenReturn(response); 
    
    	java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
            nf.setGroupingUsed(false);
    	//assertEquals("同步boss资金账户成功，平台侧资金余额为："+ nf.format((Double.parseDouble(outData.getPREPAY_FEE())/ 100.0)) + " 元", aService.syncFromBoss(entId, prdId).getResult());
    
    	outData.setPREPAY_FEE("5000");
    	response.setOutData(outData);
        when(scBalanceService.sendBalanceRequest(Mockito.any(SCBalanceRequest.class))).thenReturn(response); 
    	when(accountMapper.forceUpdateCount(Mockito.anyLong(), Mockito.anyDouble())).thenReturn(0).thenReturn(1);
    	when(accountRecordService.create(Mockito.any(AccountRecord.class))).thenReturn(false).thenReturn(true);
    	assertEquals("同步boss资金账户失败,BOSS侧资金余额为："+ nf.format((Double.parseDouble(outData.getPREPAY_FEE()) / 100.0)) + " 元", aService.syncFromBoss(entId, prdId).getResult());
    	assertEquals("同步boss资金账户失败,BOSS侧资金余额为："+ nf.format((Double.parseDouble(outData.getPREPAY_FEE()) / 100.0)) + " 元", aService.syncFromBoss(entId, prdId).getResult());
    	//assertEquals("同步boss资金账户成功，平台侧资金余额为："+ nf.format((Double.parseDouble(outData.getPREPAY_FEE()) / 100.0)) + " 元", aService.syncFromBoss(entId, prdId).getResult());
    	
    	outData.setPREPAY_FEE("15000");
        response.setOutData(outData);
        when(scBalanceService.sendBalanceRequest(Mockito.any(SCBalanceRequest.class))).thenReturn(response); 
    	//assertEquals("同步boss资金账户成功，平台侧资金余额为："+ nf.format((Double.parseDouble(outData.getPREPAY_FEE()) / 100.0)) + " 元", aService.syncFromBoss(entId, prdId).getResult());
    }
    
    
    @Test
    public void testSyncFromBoss1() {
        Long entId = 1L;
        Long prdId = 1L;
        
        Enterprise enterprise = new Enterprise();
        
        when(enterprisesService.selectByPrimaryKey(entId)).thenReturn(null).thenReturn(enterprise);
        assertSame("企业不存在", aService.syncFromBoss(entId, prdId).getResult());
        
        Account platAccount = new Account();
        platAccount.setCount(10000.0);
        platAccount.setOwnerId(1L);
        platAccount.setEnterId(1L);
        platAccount.setProductId(1L);
        when(accountMapper.selectCurrencyAccount(entId)).thenReturn(null).thenReturn(platAccount);
        assertSame("平台现金账户不存在", aService.syncFromBoss(entId, prdId).getResult());   
        
        when(enterpriseUserIdService.getUserIdByEnterpriseCode(Mockito.anyString())).thenReturn("");
        assertSame("该企业无法查询BOSS余额", aService.syncFromBoss(entId, prdId).getResult());    
        
        when(enterpriseUserIdService.getUserIdByEnterpriseCode(Mockito.anyString())).thenReturn("202");
        SCBalanceResponse response = new SCBalanceResponse();
        //response.setResCode("0000000");
        when(scBalanceService.sendBalanceRequest(Mockito.any(SCBalanceRequest.class))).thenReturn(response); 
        assertSame("查询boss资金账户失败", aService.syncFromBoss(entId, prdId).getResult());
    
        response.setResCode("0000000");
        SCBalanceResponseOutData outData = new SCBalanceResponseOutData();
        outData.setPREPAY_FEE("10000");
        response.setOutData(outData);
        when(scBalanceService.sendBalanceRequest(Mockito.any(SCBalanceRequest.class))).thenReturn(response); 
    
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
            nf.setGroupingUsed(false);
        //assertEquals("同步boss资金账户成功，平台侧资金余额为："+ nf.format((Double.parseDouble(outData.getPREPAY_FEE())/ 100.0)) + " 元", aService.syncFromBoss(entId, prdId).getResult());
    
        outData.setPREPAY_FEE("15000");
        response.setOutData(outData);
        when(scBalanceService.sendBalanceRequest(Mockito.any(SCBalanceRequest.class))).thenReturn(response); 
        when(accountMapper.forceUpdateCount(Mockito.anyLong(), Mockito.anyDouble())).thenReturn(0);
        when(accountRecordService.create(Mockito.any(AccountRecord.class))).thenReturn(false).thenReturn(true);
        assertEquals("同步boss资金账户失败,BOSS侧资金余额为："+ nf.format((Double.parseDouble(outData.getPREPAY_FEE()) / 100.0)) + " 元", aService.syncFromBoss(entId, prdId).getResult()); 
    }
    
    @Test
    public void testSyncFromBoss2() {
        Long entId = 1L;
        Long prdId = 1L;
        
        Enterprise enterprise = new Enterprise();
        enterprise.setCode("2807797504");
        when(enterprisesService.selectByPrimaryKey(entId)).thenReturn(enterprise);

        when(globalConfigService.get(GlobalConfigKeyEnum.SC_MIGU_CODE.getKey())).thenReturn("2807797504");
                
        assertSame("该企业不进行BOSS余额同步", aService.syncFromBoss(entId, prdId).getResult());
    }
   
    
    /**
     * 
     */
    @Test
    public void testRestoreEnter(){
        Enterprise enterprise = new Enterprise();
        enterprise.setId(1L);
        enterprise.setDeleteFlag(1);
        
        ScAccountServiceImpl service = Mockito.spy(aService);
        when(globalConfigService.get(GlobalConfigKeyEnum.SC_ACCOUNT_BALANCE.getKey())).thenReturn("5000");
        SCBalanceResponse balanceResponse = new SCBalanceResponse();
        SCBalanceResponseOutData outData = new SCBalanceResponseOutData();
        outData.setPREPAY_FEE("50000");
        balanceResponse.setOutData(outData);
        
        EntECRecord preEcRecord = new EntECRecord();
        preEcRecord.setPreStatus(1);
        
        Mockito.doReturn(preEcRecord).when(service).getLatestEcpauseRecord(Mockito.anyLong());
        Mockito.when(enterprisesService.updateByPrimaryKeySelective(Mockito.any(Enterprise.class))).thenReturn(true);
        Mockito.doReturn(true).when(service).insertEnterStatus(Mockito.anyLong());
        Mockito.doReturn(true).when(service).insertEcStatus(Mockito.anyLong(), Mockito.any(EntECRecord.class));
        
        Assert.assertNotNull(service.restoreEnter(enterprise, balanceResponse));
        
        when(globalConfigService.get(GlobalConfigKeyEnum.SC_ACCOUNT_BALANCE.getKey())).thenReturn("1");
        Assert.assertTrue(service.restoreEnter(enterprise, balanceResponse));
        
        Mockito.doReturn(false).when(service).insertEcStatus(Mockito.anyLong(), Mockito.any(EntECRecord.class));
        Assert.assertTrue(service.restoreEnter(enterprise, balanceResponse));
        
        Mockito.doReturn(false).when(service).insertEnterStatus(Mockito.anyLong());
        Assert.assertTrue(service.restoreEnter(enterprise, balanceResponse));
        
        Mockito.when(enterprisesService.updateByPrimaryKeySelective(Mockito.any(Enterprise.class))).thenReturn(false);
        Assert.assertTrue(service.restoreEnter(enterprise, balanceResponse));
        
        Mockito.doReturn(null).when(service).getLatestEcpauseRecord(Mockito.anyLong());
        Assert.assertTrue(service.restoreEnter(enterprise, balanceResponse));
        
        when(globalConfigService.get(GlobalConfigKeyEnum.SC_ACCOUNT_BALANCE.getKey())).thenReturn("abc");
        Assert.assertTrue(service.restoreEnter(enterprise, balanceResponse));
    }
    
    /**
     * testInsertEnterStatus
     */
    @Test
    public void testInsertEnterStatus(){
        Mockito.when(entStatusRecordService.insert(Mockito.any(EntStatusRecord.class))).thenReturn(true);
        Assert.assertNotNull(aService.insertEnterStatus(1L));
        
        Mockito.when(entStatusRecordService.insert(Mockito.any(EntStatusRecord.class))).thenReturn(false);
        Assert.assertNotNull(aService.insertEnterStatus(1L));
    }
    
    /**
     * testInsertEnterStatus
     */
    @Test
    public void testInsertECStatus(){
        EntECRecord preEcRecord = new EntECRecord();
        preEcRecord.setPreStatus(1);
        
        Mockito.when(entECRecordService.insert(Mockito.any(EntECRecord.class))).thenReturn(true);
        Assert.assertNotNull(aService.insertEcStatus(1L,preEcRecord));
        
        Mockito.when(entECRecordService.insert(Mockito.any(EntECRecord.class))).thenReturn(false);
        Assert.assertNotNull(aService.insertEcStatus(1L,preEcRecord));
    }
    
    /**
     * testInsertEnterStatus
     */
    @Test
    public void testGetLatestEcpauseRecord(){
        EntECRecord preEcRecord1 = new EntECRecord();
        preEcRecord1.setPreStatus(1);
        preEcRecord1.setOpDesc("aaa");
        
        EntECRecord preEcRecord2 = new EntECRecord();
        preEcRecord2.setPreStatus(1);
        preEcRecord2.setOpDesc("BOSS余额查询不足，平台自动关停");
        List<EntECRecord> listECrecords = new ArrayList<EntECRecord>();
        listECrecords.add(preEcRecord1);
        listECrecords.add(preEcRecord2);
        
        Mockito.when(entECRecordService.getLatestEntEcRecords(1L)).thenReturn(listECrecords);
        Assert.assertNotNull(aService.getLatestEcpauseRecord(1L));
        
        Mockito.when(entECRecordService.getLatestEntEcRecords(1L)).thenReturn(new ArrayList<EntECRecord>());
        Assert.assertNull(aService.getLatestEcpauseRecord(1L));
        
    }
}
