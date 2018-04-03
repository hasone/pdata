package com.cmcc.vrp.queue.consumer.provinces;

import com.cmcc.vrp.queue.enums.Provinces;
import org.springframework.stereotype.Component;

/**
 * Created by leelyn on 2016/11/15.
 */
@Component
public class GsConsumer extends ProvinceConsumer {

    @Override
    protected String getPackageKey() {
        return Provinces.GANSU.getCode();
    }

    @Override
    protected String getConsumeQueueName() {
        return Provinces.GANSU.getQueueName();
    }

    @Override
    protected int getWorkerCount() {
        return super.getWorkerCount();
    }
}
