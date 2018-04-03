package com.cmcc.vrp.boss.henan.service;

import com.cmcc.vrp.boss.henan.model.HaQueryBalanceResp;
import com.cmcc.vrp.boss.henan.model.HaQueryStatusResp;
import com.cmcc.vrp.boss.henan.model.MemberDeal;

/**
 * Created by leelyn on 2016/8/16.
 */
public interface HaQueryBossService {

    /**
     * @param phone
     * @return
     */
    MemberDeal queryMemberDeal(String phone);

    /**
     * @param phone
     * @param bossReqNum
     * @return
     */
    HaQueryStatusResp queryMemberStatus(String phone, String bossReqNum);

    /**
     * @param billId
     * @return
     */
    HaQueryBalanceResp queryGrpBalance(String billId);
}
