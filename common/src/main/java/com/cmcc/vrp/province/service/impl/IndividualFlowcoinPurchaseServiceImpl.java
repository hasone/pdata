package com.cmcc.vrp.province.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.FlowCoinPurchaseStatus;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.province.dao.IndividualFlowcoinPurchaseMapper;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualFlowcoinPurchase;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.IndividualProductMap;
import com.cmcc.vrp.province.service.IndividualAccountRecordService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualFlowcoinPurchaseService;
import com.cmcc.vrp.province.service.IndividualProductMapService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.util.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 
 * @ClassName: IndividualFlowcoinPurchaseServiceImpl 
 * @Description: TODO
 */
@Service("individualFlowcoinPurchaseService")
public class IndividualFlowcoinPurchaseServiceImpl implements IndividualFlowcoinPurchaseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndividualFlowcoinPurchaseServiceImpl.class);
    @Autowired
    IndividualFlowcoinPurchaseMapper mapper;

    @Autowired
    IndividualProductMapService individualProductMapService;
    @Autowired
    IndividualProductService individualProductService;
    @Autowired
    IndividualAccountRecordService individualAccountRecordService;
    @Autowired
    IndividualAccountService individualAccountService;

    @Override
    public boolean insert(IndividualFlowcoinPurchase record) {
        return mapper.insert(record) == 1;
    }

    /**
     * 保存流量币购买记录
     */
    @Override
    public boolean saveFlowcoinPurchase(IndividualFlowcoinPurchase record) {

        IndividualProduct product = individualProductService.getFlowcoinProduct();
        IndividualProductMap ipm = individualProductMapService.getByAdminIdAndProductId(record.getAdminId(),
                product.getId());
        if (ipm == null) {
            return false;
        }
        BigDecimal money = new BigDecimal(ipm.getPrice() * ipm.getDiscount() / 100d * record.getCount());

        record.setStatus(FlowCoinPurchaseStatus.WAITED.getCode());
        record.setCreateTime(new Date());
        record.setDeleteFlag(0);
        record.setPrice(money);
        record.setSystemSerial(generateSystemSerial(record.getAdminId()));
        record.setUpdateTime(new Date());
        record.setVersion(0L);
        //record.setExpireTime(DateUtil.getEndOfYear(record.getCreateTime()));

        if (!insert(record)) {
            return false;
        }

        return true;
    }

    @Override
    public List<IndividualFlowcoinPurchase> selectByMap(Map map) {
        if(map.get("status") != null){
            String status = (String) map.get("status");
            map.put("status", java.util.Arrays.asList(status.split(",")));   
        }
        return mapper.selectByMap(map);
    }

    @Override
    public int countByMap(Map map) {
        if(map.get("status") != null){
            String status = (String) map.get("status");
            map.put("status", java.util.Arrays.asList(status.split(",")));   
        }

        return mapper.countByMap(map);
    }

    @Override
    public IndividualFlowcoinPurchase selectByPrimaryKey(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public IndividualFlowcoinPurchase selectBySystemSerial(String systemSerial) {
        return mapper.selectBySystemSerial(systemSerial);
    }

    @Override
    public boolean updateStatus(String syetemSerial, Integer status) {
        return mapper.updateStatus(syetemSerial, status) == 1;
    }

    @Override
    public boolean updateBySystemSerial(IndividualFlowcoinPurchase record) {
        record.setUpdateTime(new Date());
        return mapper.updateBySystemSerial(record) == 1;
    }

    @Override
    @Transactional
    public boolean pay(Long adminId, String systemSerial) {

        IndividualFlowcoinPurchase purchaseRecord = selectBySystemSerial(systemSerial);
        IndividualProduct phoneFare = individualProductService.getPhoneFareProduct();
        IndividualProduct flowcoin = individualProductService.getFlowcoinProduct();
        if (purchaseRecord == null || phoneFare == null || flowcoin == null) {
            LOGGER.error("流量币购买记录未空，或话费产品为空，或流量币产品为空");
            return false;
        }
        //1、扣减话费
        //增加话费冻结账户、向boss发送扣减请求
        IndividualAccount account = initIndividualPhoneFareAccount(adminId, purchaseRecord.getId(),
                purchaseRecord.getPrice(), phoneFare);
        try {
            if (!individualAccountService.createFlowcoinExchangeAndPurchaseAccount(account,
                    purchaseRecord.getSystemSerial(), "流量币购买", ActivityType.INDIVIDUAL_FLOWCOIN_PURCHASE.getCode())) {
                throw new RuntimeException("创建冻结账户失败！");
            }
        } catch (Exception e) {
            throw new RuntimeException("创建冻结账户失败！创建时抛出异常" + e.getMessage());
        }

        //2、修改流量币购买订单状态
        if (!updateStatus(systemSerial, FlowCoinPurchaseStatus.PROCESSING.getCode())) {
            return false;
        }
        ;

        //3、扣减话费及创建话费冻结账户成功
        //增加流量币操作
        //3-1、boss处流量币增加成功
        if (individualAccountService.changeBossAccount(adminId, new BigDecimal(purchaseRecord.getCount()),
                flowcoin.getId(), systemSerial, (int) AccountRecordType.INCOME.getValue(), "流量币购买，请求boss增加流量币(3)",
                ActivityType.INDIVIDUAL_FLOWCOIN_PURCHASE.getCode(), 0)) {
            IndividualFlowcoinPurchase record = new IndividualFlowcoinPurchase();
            record.setStatus(FlowCoinPurchaseStatus.SUCCESS.getCode());
            record.setSystemSerial(systemSerial);
            record.setExpireTime(DateUtil.getEndOfNextYear(new Date()));
            if (!updateBySystemSerial(record)) {
                LOGGER.error("更新流量币购买记录失败！");
            }

            //扣除冻结账户
            if (!individualAccountService.changeFrozenAccount(account.getAdminId(), account.getOwnerId(),
                    account.getId(), account.getCount(), account.getIndividualProductId(), record.getSystemSerial(),
                    (int) AccountRecordType.OUTGO.getValue(), "流量币购买成功，扣除冻结账户(4)",
                    ActivityType.INDIVIDUAL_FLOWCOIN_PURCHASE.getCode(), 0)) {
                LOGGER.error("changeFrozenAccount失败，account={}" + JSONObject.toJSONString(account));
            }
        }else {
            //3-2boss处流量币增加失败，退回boss话费
            if (individualAccountService.changeBossAccount(adminId, purchaseRecord.getPrice(), phoneFare.getId(),
                    systemSerial, (int) AccountRecordType.INCOME.getValue(), "购买流量币失败，请求boss增加话费(3)",
                    ActivityType.INDIVIDUAL_FLOWCOIN_PURCHASE.getCode(), 1)) {
                updateStatus(systemSerial, FlowCoinPurchaseStatus.FAIL.getCode());

                //扣除冻结账户
                if (!individualAccountService.changeFrozenAccount(account.getAdminId(), account.getOwnerId(),
                        account.getId(), account.getCount(), account.getIndividualProductId(),
                        purchaseRecord.getSystemSerial(), (int) AccountRecordType.OUTGO.getValue(),
                        "购买流量币失败，BOSS话费增加成功，扣除冻结账户(4)", ActivityType.INDIVIDUAL_FLOWCOIN_PURCHASE.getCode(), 0)) {
                    LOGGER.error("changeFrozenAccount失败，account={}" + JSONObject.toJSONString(account));
                }
            }
        }

        return true;
    }

    /**
     * 生成订单号
     *
     * @param adminId
     * @return
     * @Title: generateSystemSerial
     * @Author: wujiamin
     * @date 2016年9月23日下午4:00:11
     */
    private String generateSystemSerial(Long adminId) {
        Long time = System.currentTimeMillis();
        Random random = new Random();
        Integer randomInt = Math.abs(random.nextInt());
        return time.toString() + adminId.toString() + randomInt.toString();
    }

    private IndividualAccount initIndividualPhoneFareAccount(Long adminId, Long flowcoinPurchaseId, BigDecimal count,
            IndividualProduct phoneFare) {
        IndividualAccount individualAccount = new IndividualAccount();
        individualAccount.setAdminId(adminId);
        individualAccount.setOwnerId(flowcoinPurchaseId);
        individualAccount.setIndividualProductId(phoneFare.getId());
        individualAccount.setType(IndividualAccountType.INDIVIDUAL_PURCHASE.getValue());
        individualAccount.setCount(count);
        individualAccount.setCreateTime(new Date());
        individualAccount.setUpdateTime(new Date());
        individualAccount.setDeleteFlag(0);
        individualAccount.setVersion(0);
        return individualAccount;
    }

}
