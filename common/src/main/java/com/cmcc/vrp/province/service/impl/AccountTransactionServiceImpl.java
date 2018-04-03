package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.AccountMapper;
import com.cmcc.vrp.province.model.AccountMinus;
import com.cmcc.vrp.province.model.AccountRecord;
import com.cmcc.vrp.province.service.AccountRecordService;
import com.cmcc.vrp.province.service.AccountTransactionService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by sunyiwei on 2016/9/7.
 */
@Service
public class AccountTransactionServiceImpl implements AccountTransactionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountTransactionServiceImpl.class);

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    AccountRecordService accountRecordService;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void minus(List<AccountMinus> accountMinusList) {
        for (AccountMinus accountMinus : accountMinusList) {
            Long accountId = accountMinus.getId();
            Double delta = accountMinus.getDelta();
            AccountRecord accountRecord = accountMinus.getAccountRecord();
            if (delta == 0) {
                continue;
            }

            if (accountMapper.updateCount(accountId, delta) != 1
                || !accountRecordService.create(accountRecord)) {
                LOGGER.error("扣减帐户余额信息失败, 具体信息为{}.", new Gson().toJson(accountMinusList));
                throw new RuntimeException("扣减账户余额失败.");
            }
        }

        LOGGER.info("扣减帐户余额信息成功, 具体信息为{}.", new Gson().toJson(accountMinusList));
    }
}
