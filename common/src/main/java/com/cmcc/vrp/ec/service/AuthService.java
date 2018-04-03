package com.cmcc.vrp.ec.service;

import com.cmcc.vrp.ec.bean.AuthReq;
import com.cmcc.vrp.ec.bean.AuthResp;
import com.cmcc.vrp.ec.exception.ForbiddenException;
import com.cmcc.vrp.ec.exception.ParaErrorException;

/**
 * Created by leelyn on 2016/5/18.
 */
public interface AuthService {

    public AuthResp getToken(AuthReq authReq, String ipAddress) throws ParaErrorException, ForbiddenException;
}
