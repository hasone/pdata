package com.cmcc.vrp.boss.ecinvoker.ecservice;

import com.cmcc.vrp.ec.bean.AuthResp;

/**
 * Created by leelyn on 2016/7/14.
 */
public interface EcAuthService {

    /**
     * 接口信息鉴权
     * @param appKey  appKey
     * @param appSecrect  appSecrect
     * @param url  路径
     * @return   鉴权类信息
     */
    AuthResp auth(String appKey, String appSecrect, String url);
}
