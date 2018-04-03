package com.cmcc.vrp.queue.task.channel;

import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.wx.impl.GdZcBossServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by leelyn on 2017/1/9.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CrowdfundingChannelWorker extends AbstractChannelWorker {

    @Autowired
    GdZcBossServiceImpl bossService;

    @Override
    protected boolean isContinueDistribute() {
        return false;
    }

    @Override
    protected BossService getBossService() {
        return bossService;
    }
}
