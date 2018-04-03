package com.cmcc.vrp.queue.task.channel;

import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.xiangshang.XiangShangBossServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 向上渠道的工作者
 * <p>
 * Created by leelyn on 2016/11/14.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class XsChannelWorker extends AbstractChannelWorker {
    @Autowired
    XiangShangBossServiceImpl xiangShangBossService;

    @Override
    protected boolean isContinueDistribute() {
        return false;
    }

    @Override
    protected BossService getBossService() {
        return xiangShangBossService;
    }
}
