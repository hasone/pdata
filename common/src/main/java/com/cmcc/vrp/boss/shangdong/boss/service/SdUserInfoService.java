package com.cmcc.vrp.boss.shangdong.boss.service;

import com.cmcc.vrp.province.model.Administer;

/**
 * SdUserInfoService
 * @author panxin
 *
 */
public interface SdUserInfoService {
    public Administer getUserInfo(String userToken, Boolean isManager);

}
