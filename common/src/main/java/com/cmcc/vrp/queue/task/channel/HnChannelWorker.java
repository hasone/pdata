package com.cmcc.vrp.queue.task.channel;

import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.hunan.HNBossServcieImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by leelyn on 2016/11/14.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HnChannelWorker extends AbstractChannelWorker {

    @Autowired
    @Qualifier("huNanBossService")
    HNBossServcieImpl bossService;

    @Override
    protected boolean isContinueDistribute() {
        return false;
    }

    @Override
    protected BossService getBossService() {
        return bossService;
    }
}
