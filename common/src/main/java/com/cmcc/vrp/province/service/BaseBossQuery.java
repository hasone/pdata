package com.cmcc.vrp.province.service;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossQueryResult;

/**
 * Created by leelyn on 2016/8/11.
 */
public interface BaseBossQuery {

    /**
     * 查询BOSS侧的充值状态
     *
     * @param systemNum
     * @return
     */
    BossQueryResult queryStatus(String systemNum);
    
    
    /**
     * 查询BOSS侧的充值状态，以及充值相关信息
     *
     * @param systemNum
     * @return
     */
    BossOperationResult queryStatusAndMsg(String systemNum);

    /**
     * 获取指纹
     *
     * @return
     */
    String getFingerPrint();
}
