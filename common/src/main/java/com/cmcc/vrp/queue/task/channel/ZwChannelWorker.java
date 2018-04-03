package com.cmcc.vrp.queue.task.channel;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.boss.BossService;

/**
 * Created by leelyn on 2016/11/14.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZwChannelWorker extends AbstractChannelWorker {
    @Override
    protected boolean isContinueDistribute() {
        return true;
    }

    @Override
    protected BossService getBossService() {
        return null;
    }
}
