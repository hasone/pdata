package com.cmcc.vrp.queue.task.channel;

import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.bjym.BjymBossServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 北京云漫worker <p>
 *
 * @author sunyiwei
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BjymChannelWorker extends AbstractChannelWorker {
    @Autowired
    BjymBossServiceImpl bjymBossService;

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
        return bjymBossService;
    }
}
