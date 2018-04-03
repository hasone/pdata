package com.cmcc.vrp.queue.consumer.provinces;

import com.cmcc.vrp.queue.enums.Provinces;
import org.springframework.stereotype.Component;

/**
 * Created by leelyn on 2016/11/15.
 */
@Component
public class ScConsumer extends ProvinceConsumer {

    @Override
    protected String getPackageKey() {
        return Provinces.SICHUAN.getCode();
    }

    @Override
    protected String getConsumeQueueName() {
        return Provinces.SICHUAN.getQueueName();
    }

    @Override
    protected int getWorkerCount() {
        return super.getWorkerCount();
    }
}
