package com.cmcc.vrp.boss.henan.service;

import com.cmcc.vrp.boss.henan.model.HaAuthReq;
import com.cmcc.vrp.boss.henan.model.HaAuthResp;

/**
 * Created by leelyn on 2016/6/24.
 */
public interface HaAuthService {

    /**
     * @param authReq
     * @return
     */
    HaAuthResp auth(HaAuthReq authReq);

}
