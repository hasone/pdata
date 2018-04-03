package com.cmcc.vrp.queue.task.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.shangdong.boss.service.SdCloudBossServiceImpl;

/**
 * Created by qihang on 2016/12/8.
 * <p>
 * 实现该类是为了更新charge_record 表里面的supplier_product_id
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SdPlatformWorker extends AbstractChannelWorker {

    @Autowired
    SdCloudBossServiceImpl bossService;

    @Override
    protected BossService getBossService() {
        return bossService;
    }

    @Override
    protected boolean isContinueDistribute() {
        return false;
    }
}
