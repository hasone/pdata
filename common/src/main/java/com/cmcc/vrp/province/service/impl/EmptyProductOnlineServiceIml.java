package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.service.ProductOnlineService;

/**
 * 不需要在线同步BOSS产品，空实现
 * Created by lilin on 2016/9/11.
 */
public class EmptyProductOnlineServiceIml implements ProductOnlineService {

    @Override
    public boolean isOnlineProduct(Long entId) {
        return true;
    }
}
