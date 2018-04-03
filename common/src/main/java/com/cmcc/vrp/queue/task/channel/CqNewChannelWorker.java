package com.cmcc.vrp.queue.task.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.chongqing.CqNewBossServiceImpl;


/**
 * @author lgk8023
 *
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CqNewChannelWorker extends AbstractChannelWorker {

    @Autowired
    CqNewBossServiceImpl bossService;

    @Override
    protected boolean isContinueDistribute() {
        return false;
    }

    @Override
    protected BossService getBossService() {
        return bossService;
    }
}
