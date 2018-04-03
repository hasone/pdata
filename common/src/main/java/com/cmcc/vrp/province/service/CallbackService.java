package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.CallbackPojo;

/**
 * Created by sunyiwei on 16-3-29.
 */
public interface CallbackService {
    /**
     * @param pojo
     * @param serialNum
     * @return
     */
    boolean callback(CallbackPojo pojo, String serialNum);
}
