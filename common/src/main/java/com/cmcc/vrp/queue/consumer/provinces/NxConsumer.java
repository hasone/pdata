package com.cmcc.vrp.queue.consumer.provinces;

import com.cmcc.vrp.queue.enums.Provinces;
import org.springframework.stereotype.Component;

/**
 * Created by leelyn on 2016/11/15.
 */
@Component
public class NxConsumer extends ProvinceConsumer {

    @Override
    protected String getPackageKey() {
        return Provinces.NINGXIA.getCode();
    }

    @Override
    protected String getConsumeQueueName() {
        return Provinces.NINGXIA.getQueueName();
    }

    @Override
    protected int getWorkerCount() {
        return super.getWorkerCount();
    }
}
