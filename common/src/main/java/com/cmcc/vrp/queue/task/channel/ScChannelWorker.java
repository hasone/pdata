package com.cmcc.vrp.queue.task.channel;

import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.sichuan.SCBossServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by leelyn on 2016/11/14.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScChannelWorker extends AbstractChannelWorker {

    @Autowired
    SCBossServiceImpl bossService;

    @Override
    protected boolean isContinueDistribute() {
        return false;
    }

    @Override
    protected BossService getBossService() {
        return bossService;
    }
}
