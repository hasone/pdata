package com.cmcc.vrp.queue.consumer.provinces;

import com.cmcc.vrp.queue.enums.Provinces;
import org.springframework.stereotype.Component;

/**
 * Created by leelyn on 2016/11/15.
 */
@Component
public class GxConsumer extends ProvinceConsumer {

    @Override
    protected String getPackageKey() {
        return Provinces.GUANGXI.getCode();
    }

    @Override
    protected String getConsumeQueueName() {
        return Provinces.GUANGXI.getQueueName();
    }

    @Override
    protected int getWorkerCount() {
        return super.getWorkerCount();
    }
}
