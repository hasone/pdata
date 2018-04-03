package com.cmcc.vrp.queue.task.channel;

import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.virtualCharge.webService.VirtualChargeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by qinqinyan on 2017/5/9.
 * 虚拟充值
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VirtualChargeChannelWorker extends AbstractChannelWorker{
    @Autowired
    VirtualChargeServiceImpl bossService;

    /**
     * @title: isContinueDistribute
     * */
    @Override
    protected boolean isContinueDistribute() {
        return false;
    }

    /**
     * @title: getBossService
     * */
    @Override
    protected BossService getBossService() {
        return bossService;
    }
}
