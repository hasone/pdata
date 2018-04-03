package com.cmcc.vrp.queue.task.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.jiangsu.JsBossServiceImpl;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年4月5日 上午10:25:04
*/
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JsChannelWorker extends AbstractChannelWorker {

    @Autowired
    JsBossServiceImpl bossService;

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
