package com.cmcc.vrp.boss.zhuowang;

import com.cmcc.vrp.boss.zhuowang.service.FlowPackageOrderService;
import com.cmcc.vrp.boss.zhuowang.service.impl.FlowPackageOrderServiceImpl;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午11:11:21
*/
public class FlowServiceFactory {

    public static FlowPackageOrderService getService() {
        return FlowServiceSingletonWrapper._flowService;
    }

    private static class FlowServiceSingletonWrapper {
        static final FlowPackageOrderService _flowService;

        static {
            _flowService = new FlowPackageOrderServiceImpl();
        }
    }
}
