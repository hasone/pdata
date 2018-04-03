package com.cmcc.vrp.queue.consumer.provinces;

import com.cmcc.vrp.queue.enums.Provinces;
import org.springframework.stereotype.Component;

/**
 * Created by leelyn on 2016/11/15.
 */
@Component
public class SxConsumer extends ProvinceConsumer {

    @Override
    protected String getPackageKey() {
        return Provinces.SHANXI1.getCode();
    }

    @Override
    protected String getConsumeQueueName() {
        return Provinces.SHANXI1.getQueueName();
    }

    @Override
    protected int getWorkerCount() {
        return super.getWorkerCount();
    }
}
