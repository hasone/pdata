package com.cmcc.vrp.boss.heilongjiang.fee;

import com.cmcc.vrp.province.service.GlobalConfigService;

/**
 * 
 * @ClassName: HLJFeeService 
 * @Description: 黑龙江话费接口
 * @author: Rowe
 * @date: 2017年10月17日 上午10:49:18
 */
public interface HLJFeeService {

    /**
     * 
     * @Title: balanceQuery 
     * @Description: 黑龙江话费信息查询
     * @param request
     * @return
     * @return: BalanceQueryResponse
     */
    BalanceQueryResponse balanceQuery(BalanceQueryRequest request);

    /**
     * 
     * @Title: eCoupon 
     * @Description: 黑龙江话费红包电子券业务冲正
     * @param request
     * @return
     * @return: ECouponResponse
     */
    ECouponResponse eCoupon(ECouponRequest request);

    /**
     * 
     * @Title: groupPerson 
     * @Description: 黑龙江话费集团产品为个人充值
     * @param request
     * @return
     * @return: GroupPersonResponse
     */
    GroupPersonResponse groupPerson(GroupPersonRequest request);

    /**
     * 
     * @Title: prepayQuery 
     * @Description: 黑龙江话费红包账户可转预存查询
     * @param request
     * @return
     * @return: PrepayQueryResponse
     */
    PrepayQueryResponse prepayQuery(PrepayQueryRequest request);

    /**
     * 
     * @Title: setGlobalConfigService 
     * @Description: TODO
     * @param globalConfigService
     * @return: void
     */
    void setGlobalConfigService(GlobalConfigService globalConfigService);
}
