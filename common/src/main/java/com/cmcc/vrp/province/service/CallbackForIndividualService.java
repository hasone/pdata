package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.CallbackPojo;

/**
 * Created by qinqinyan on 2016/10/9.
 */
public interface CallbackForIndividualService {

    /**
     * @param pojo
     * @param serialNum
     * @return
     */
    boolean callback(CallbackPojo pojo, String serialNum);
}
