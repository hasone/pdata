package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.service.ProductOnlineService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ProductOnlinePojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lilin on 2016/9/11.
 */
public class ProductOnlineServiceImpl implements ProductOnlineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductOnlineServiceImpl.class);

    @Autowired
    private TaskProducer producer;

    @Override
    public boolean isOnlineProduct(Long entId) {
        ProductOnlinePojo pojo = new ProductOnlinePojo();
        pojo.setEntId(entId);
        pojo.setFingerPrint("shanghainational123456789");
        Boolean isInto = producer.productProductOnlineMsg(pojo);
        LOGGER.info("在线同步渠道商消息已经入队列:", isInto);
        return isInto;
    }
}
