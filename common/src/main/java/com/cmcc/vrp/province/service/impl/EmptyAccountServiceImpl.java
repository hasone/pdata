package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.boss.SyncAccountResult;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.MinusCountReturnType;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.AccountPrizeMap;
import com.cmcc.vrp.province.model.Product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 帐户服务的默认空实现 <p> Created by sunyiwei on 2016/4/14.
 */

/**
 * modified: 会创建账户，但是不会去做账户余额变更及校验
 *
 * @author wujiamin
 * @date 2016年11月18日
 */
public class EmptyAccountServiceImpl extends AccountServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmptyAccountServiceImpl.class);

    @Override
    public boolean addCount(Long ownerId, Long prdId, AccountType accountType, Double delta, String serialNum, String desc) {
        return true;
    }

    @Override
    public boolean minusCount(Long ownerId, Long prdId, AccountType accountType, Double delta, String serialNum, String desc) {
        return true;
    }

    @Override
    public MinusCountReturnType minusCount(Long ownerId, Long prdId, AccountType accountType,
                                           Double delta, String serialNum, String desc, boolean isCheckSolde) {
        return MinusCountReturnType.OK;
    }

    @Override
    public boolean returnFunds(String serialNum, ActivityType activityType, Long dstPrdId, Integer count) {
        return true;
    }

    @Override
    public boolean returnFunds(String serialNum) {
        return true;
    }

    @Override
    public boolean isDebt2Account(List<AccountPrizeMap> maps, Long enterId) {
        return false;
    }

    @Override
    public boolean checkAccountByEntIdAndProductId(Long entId, Long productId) {
        return true;
    }

    //空实现， 直接返回成功
    @Override
    public SyncAccountResult syncFromBoss(Long entId, Long prdId) {
        return SyncAccountResult.SUCCESS;
    }

    @Override
    public boolean updateAlertSelective(Account account) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void recoverAlert(Long ownerId, Long prdId) {
        // TODO Auto-generated method stub

    }

    @Override
    public String checkAlertStopValue(Long entId, Double totalPrice) {
        LOGGER.error("EmptyAccountServiceImpl 不需要检验余额，和暂停值预警值,entId:{},prize:{}", entId, totalPrice);
        return null;
    }

    @Override
    public boolean isEnough2Debt(Account currency, Double debt) {
        return true;
    }

    @Override
    public boolean isEnoughInAccount(Long productId, Long entId) {
        return true;
    }
    
    @Override
    public boolean isEnoughInAccount(List<Product> prds, Long entId){
        return true;
    }

    @Override
    public boolean isEmptyAccount() {
        // TODO Auto-generated method stub
        return true;
    }

    /**
     * 校验随机红包余额
     * @param entId
     * @param totalProductSize
     * @author qinqinyan
     * */
    @Override
    public boolean verifyAccountForRendomPacket(Long entId, Long totalProductSize, Long productId){
        return true;
    }


}
