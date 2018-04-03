package com.cmcc.vrp.province.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.cmcc.vrp.boss.SyncAccountResult;
import com.cmcc.vrp.boss.hunan.HNBossServcieImpl;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.Enterprise;
import com.google.gson.Gson;

public class HuNanAccountServiceImpl extends AccountServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(HuNanAccountServiceImpl.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public SyncAccountResult syncFromBoss(Long entId, Long prdId) {
        //开始查询之前，先做各种校验
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        if (enterprise == null) {
            return buildResult("企业不存在", false);
        }

        HNBossServcieImpl bossService = applicationContext.getBean("huNanBossService", HNBossServcieImpl.class);
        if (bossService == null) {
            return buildResult("无效的BOSS渠道", false);
        }

        Account platAccount = getCurrencyAccount(entId);
        if (platAccount == null) {
            return buildResult("平台现金账户不存在", false);
        }

        //万事俱备，开始查询
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);

        Account bossAccount = bossService.queryAccountByEntId(entId, platAccount.getProductId(), new SimpleDateFormat(
                "yyyyMMddHHmmss").format(new Date()));
        if (bossAccount == null) {
            return buildResult("查询boss资金账户失败", false);
        }

        //查询到了，开始更新
        if (!update(platAccount, bossAccount.getCount())) {
            final String returnMsg = "同步boss资金账户失败,BOSS侧资金余额为：" + nf.format((bossAccount.getCount() / 100.0)) + " 元";
            return buildResult(returnMsg, false);
        }

        return buildResult("同步boss资金账户成功，平台侧资金余额为：" + nf.format((bossAccount.getCount() / 100.0)) + " 元", true);
    }

    //将平台账户余额更新成新的值
    private boolean update(Account platAccount, double newCount) {
        LOGGER.info("平台账户信息：platAccount = {}, boss侧账户余额={}。" + new Gson().toJson(platAccount), newCount);
        double delta = Math.abs(platAccount.getCount() - newCount);
        LOGGER.info("平台账户余额与BOSS侧账户余额差额 = {}", delta);
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
                    && accountRecordService.create(buildAccountRecord(enterId, ownerId, accountId, delta,
                            AccountRecordType.OUTGO, serialNum, desc))) {
                LOGGER.info(
                        "扣减帐户余额信息成功, AccountId = {}, ownerId = {}, prdId = {}, delta = {}, serialNum = {}, desc = {}.",
                        accountId, ownerId, prdId, delta, serialNum, desc);
                return true;
            } else {
                LOGGER.error(
                        "扣减帐户余额信息失败, AccountId = {}, ownerId = {}, prdId = {}, delta = {}, serialNum = {}, desc = {}.",
                        accountId, ownerId, prdId, delta, serialNum, desc);
                return false;
            }
        } else { //反之要增加
            LOGGER.info("当前余额比BOSS侧余额少，往账户里增加.");
            if (accountMapper.forceUpdateCount(accountId, delta) == 1
                    && accountRecordService.create(buildAccountRecord(enterId, ownerId, accountId, delta,
                            AccountRecordType.INCOME, serialNum, desc))) {
                LOGGER.info(
                        "增加帐户余额信息成功, AccountId = {}, ownerId = {}, prdId = {}, delta = {}, serialNum = {}, desc = {}.",
                        accountId, ownerId, prdId, delta, serialNum, desc);
                return true;
            } else {
                LOGGER.error(
                        "增加帐户余额信息失败, AccountId = {}, ownerId = {}, prdId = {}, delta = {}, serialNum = {}, desc = {}.",
                        accountId, ownerId, prdId, delta, serialNum, desc);
                return false;
            }

        }
    }

    /**
     * 
     * @Title: buildResult 
     * @Description: TODO
     * @param msg
     * @param success
     * @return
     * @return: SyncAccountResult
     */
    private SyncAccountResult buildResult(String msg, Boolean success) {
        return new SyncAccountResult(msg, success);
    }
}
