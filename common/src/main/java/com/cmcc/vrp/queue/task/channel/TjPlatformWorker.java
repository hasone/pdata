package com.cmcc.vrp.queue.task.channel;

import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.ecinvoker.impl.TjEcBossServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年2月7日 下午2:48:05
*/
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TjPlatformWorker extends AbstractChannelWorker {

    @Autowired
    TjEcBossServiceImpl bossService;

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
