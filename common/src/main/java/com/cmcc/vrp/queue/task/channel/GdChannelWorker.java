package com.cmcc.vrp.queue.task.channel;

import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.guangdong.GdBossServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年4月6日 下午2:36:44
*/
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GdChannelWorker extends AbstractChannelWorker {

    @Autowired
    GdBossServiceImpl bossService;

    @Override
    protected boolean isContinueDistribute() {
        return false;
    }

    @Override
    protected BossService getBossService() {
        return bossService;
    }
}
