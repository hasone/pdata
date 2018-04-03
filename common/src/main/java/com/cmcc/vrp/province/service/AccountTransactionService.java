package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.AccountMinus;

import java.util.List;

/**
 * 扣减余额时的事务处理
 * <p>
 * Created by sunyiwei on 2016/9/7.
 */
public interface AccountTransactionService {
    /** 
     * @Title: minus 
     * @param accountMinusList
     * @Author: wujiamin
     * @date 2016年10月17日下午5:27:32
    */
    void minus(List<AccountMinus> accountMinusList);
}
