package com.cmcc.vrp.province.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.cmcc.vrp.boss.SyncAccountResult;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceRequest;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceResponse;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceResponseOutData;
import com.cmcc.vrp.boss.sichuan.service.SCBalanceService;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.EnterpriseStatus;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.EntECRecord;
import com.cmcc.vrp.province.model.EntStatusRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.EntECRecordService;
import com.cmcc.vrp.province.service.EntStatusRecordService;
import com.cmcc.vrp.province.service.EnterpriseUserIdService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * SichuanAccountServiceImpl.java
 * @author wujiamin
 * @date 2016年11月22日
 */
public class ScAccountServiceImpl extends AccountServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScAccountServiceImpl.class);

    @Autowired
    SCBalanceService scBalanceService;
    
    @Autowired
    EnterpriseUserIdService enterpriseUserIdService;
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    @Autowired
    EntStatusRecordService entStatusRecordService;
    
    @Autowired
    EntECRecordService entECRecordService;

    @Override
    public SyncAccountResult syncFromBoss(Long entId, Long prdId) {
        //开始查询之前，先做各种校验
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        if (enterprise == null) {
            return buildResult("企业不存在", false);
        }

        if(notSynchronized(enterprise.getCode())){
            return buildResult("该企业不进行BOSS余额同步", false);
        }

        Account platAccount = getCurrencyAccount(entId);
        if (platAccount == null) {
            return buildResult("平台现金账户不存在", false);
        }
        SCBalanceRequest banlanceRequest = new SCBalanceRequest();
        
        String userId;
        if(isMockBoss()){
            userId = "0000000";
        }else{
            userId = enterpriseUserIdService.getUserIdByEnterpriseCode(enterprise.getCode());    
        }
        if(StringUtils.isEmpty(userId)){
            return buildResult("该企业无法查询BOSS余额", false);
        }
        banlanceRequest.setPhoneNo(userId);
        
        SCBalanceResponse balanceResponse;  
        if(isMockBoss()){
            balanceResponse = new SCBalanceResponse();
            balanceResponse.setResCode("0000000");
            SCBalanceResponseOutData outData = new SCBalanceResponseOutData();
            outData.setPREPAY_FEE(getMockAccountBalance());
            balanceResponse.setOutData(outData);
        }else{
            balanceResponse = scBalanceService.sendBalanceRequest(banlanceRequest);  
        }
        
        
        if (!"0000000".equals(balanceResponse.getResCode())){
            return buildResult("查询boss资金账户失败", false);
        }
        
        //boss处余额，单位（分）
        double bossAccount = Double.parseDouble(balanceResponse.getOutData().getPREPAY_FEE());                
        
        //万事俱备，开始查询
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);

        //查询到了，开始更新
        if (!update(platAccount, bossAccount)) {
            final String returnMsg = "同步boss资金账户失败,BOSS侧资金余额为：" + nf.format(bossAccount/ 100.0) + " 元";
            return buildResult(returnMsg, false);
        }
        
        //恢复企业状态和Ec状态
        if(!restoreEnter(enterprise,balanceResponse)){
            final String returnMsg = "变更企业状态和Ec状态失败";
            return buildResult(returnMsg, false);
        }
        
        return buildResult("同步boss资金账户成功，平台侧资金余额为：" + nf.format(bossAccount/ 100.0) + " 元", true);
    }

    //将平台账户余额更新成新的值
    private boolean update(Account platAccount, double newCount) {
        double delta = Math.abs(platAccount.getCount() - newCount);
        if (delta == 0) {
            return true;
        }

        final String serialNum = SerialNumGenerator.buildSerialNum();
        final String desc = "BOSS侧同步余额";

        final Long ownerId = platAccount.getOwnerId();
        final Long enterId = platAccount.getEnterId();
        final Long accountId = platAccount.getId();
        final Long prdId = platAccount.getProductId();

        //当前余额比目标余额多，要扣减， 注意这里的扣减是不考虑其它因素的(如账户转换等)，直接扣减
        if (platAccount.getCount() > newCount) {
            LOGGER.info("当前余额比BOSS侧余额多，从账户上扣减.");
            if (accountMapper.forceUpdateCount(accountId, -delta) == 1
                && accountRecordService.create(buildAccountRecord(enterId, ownerId, accountId, delta, AccountRecordType.OUTGO, serialNum, desc))) {
                LOGGER.info("扣减帐户余额信息成功, AccountId = {}, ownerId = {}, prdId = {}, delta = {}, serialNum = {}, desc = {}.",
                    accountId, ownerId, prdId, delta, serialNum, desc);
                return true;
            } else {
                LOGGER.error("扣减帐户余额信息失败, AccountId = {}, ownerId = {}, prdId = {}, delta = {}, serialNum = {}, desc = {}.",
                    accountId, ownerId, prdId, delta, serialNum, desc);
                return false;
            }
        } else { //反之要增加
            LOGGER.info("当前余额比BOSS侧余额少，往账户里增加.");
            if (accountMapper.forceUpdateCount(accountId, delta) == 1
                && accountRecordService.create(buildAccountRecord(enterId, ownerId, accountId, delta, AccountRecordType.INCOME, serialNum, desc))) {
                LOGGER.info("增加帐户余额信息成功, AccountId = {}, ownerId = {}, prdId = {}, delta = {}, serialNum = {}, desc = {}.",
                    accountId, ownerId, prdId, delta, serialNum, desc);
                return true;
            } else {
                LOGGER.error("增加帐户余额信息失败, AccountId = {}, ownerId = {}, prdId = {}, delta = {}, serialNum = {}, desc = {}.",
                    accountId, ownerId, prdId, delta, serialNum, desc);
                return false;
            }
        }
    }

    private SyncAccountResult buildResult(String msg, Boolean success) {
        return new SyncAccountResult(msg, success);
    }
    
    //咪咕公司不需要同步
    private boolean notSynchronized(String code) {
        String codeString = globalConfigService.get(GlobalConfigKeyEnum.SC_MIGU_CODE.getKey());
        if(StringUtils.isEmpty(codeString)){
            return false;
        }
        String[] codes = codeString.split(",");
        for(int i = 0; i< codes.length; i++){
            if(codes[i].equals(code)){
                return true;
            }            
        }
        return false;
    }
    
    /**
     * PDATA-1920 [四川]四川余额不足暂停后，企业及EC状态的恢复问题
     */
    public boolean restoreEnter(Enterprise enterprise,SCBalanceResponse balanceResponse){
        boolean isEnterNotStop = enterprise.getDeleteFlag().equals(0);
        if(isEnterNotStop){
            LOGGER.info("企业{}状态为0，不需要恢复状态",enterprise.getId());
            return true;
        }
        
        String money = globalConfigService.get(GlobalConfigKeyEnum.SC_ACCOUNT_BALANCE.getKey());
        if(!NumberUtils.isDigits(money)){
            LOGGER.error("globalConfig中，SC_ACCOUNT_BALANCE取值不合法为" + money);
            return true;
        }
        
        
        if(Integer.parseInt(balanceResponse.getOutData().getPREPAY_FEE()) <= Integer.parseInt(money) * 100){
            LOGGER.error("企业Id:{},查询余额为:{},少于规定阈值:{},无法恢复.",enterprise.getId(),
                    balanceResponse.getOutData().getPREPAY_FEE(),money);
            return true;
        }
        
        //找出最新一次的暂停记录
        EntECRecord preEcRecord = getLatestEcpauseRecord(enterprise.getId());
        if(preEcRecord == null){
            LOGGER.error("在EntECRecord表中没有找到相关自动关停的记录，entId = " + enterprise.getId());
            return true;
        }
        
        enterprise.setUpdateTime(new Date());
        enterprise.setDeleteFlag(0);
        enterprise.setInterfaceFlag(preEcRecord.getPreStatus());
        
        return enterprisesService.updateByPrimaryKeySelective(enterprise) && 
                insertEnterStatus(enterprise.getId()) && insertEcStatus(enterprise.getId(),preEcRecord);
    }
    
    /**
     * PDATA-1920 [四川]四川余额不足暂停后，企业及EC状态的恢复问题
     */
    protected boolean insertEnterStatus(Long entId){
        //生成企业状态变更记录
        EntStatusRecord entStatusRecord = new EntStatusRecord();

        entStatusRecord.setEntId(entId);//企业
        entStatusRecord.setCreateTime(new Date());
        entStatusRecord.setUpdateTime(new Date());
        entStatusRecord.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        entStatusRecord.setPreStatus(2);//企业先前状态，注：enterprise中deleteFlag表企业状态
        entStatusRecord.setNowStatus(0);
        entStatusRecord.setOpType(EnterpriseStatus.NORMAL.getCode());
        entStatusRecord.setOpDesc("企业缴费后恢复");
        entStatusRecord.setReason("自动恢复");
        return entStatusRecordService.insert(entStatusRecord);
    }
    
    /**
     * PDATA-1920 [四川]四川余额不足暂停后，企业及EC状态的恢复问题
     */
    protected boolean insertEcStatus(Long entId,EntECRecord preEcRecord){
        
        //生成企业EC接口变更记录
        EntECRecord entECRecord = new EntECRecord();

        entECRecord.setEntId(entId);
        entECRecord.setPreStatus(0);
        entECRecord.setCreateTime(new Date());
        entECRecord.setUpdateTime(new Date());
        entECRecord.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        
        entECRecord.setOpDesc("企业缴费后恢复");
        entECRecord.setNowStatus(preEcRecord.getPreStatus());//这里需要取出最近一次自动关停前的状态
        entECRecord.setOpType(preEcRecord.getPreStatus());//先设置为和status一致，如果有变化再更改
        return entECRecordService.insert(entECRecord);
    }

    /**
     * 
     * @param entId
     */
    protected EntECRecord getLatestEcpauseRecord(Long entId){
        List<EntECRecord> listECrecords = entECRecordService.getLatestEntEcRecords(entId);
        for(EntECRecord record : listECrecords){
            if(record.getOpDesc().equals("BOSS余额查询不足，平台自动关停")){
                return record;
            }
        }
        
        return null;
    }
    
    /**
     * 
     * @return
     */
    private boolean isMockBoss(){   
        return "true".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.SC_MOCK_BOSS.getKey()));
    }

    /**
     * 
     * @return
     */
    private String getMockAccountBalance(){
        return globalConfigService.get(GlobalConfigKeyEnum.SC_MOCK_ACCOUNT_BALANCE.getKey());
    }
}
