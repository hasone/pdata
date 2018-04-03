package com.cmcc.vrp.queue.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.cmcc.vrp.province.service.ProductAutoService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ProductOnlinePojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lilin on 2016/9/11.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProductOnlineWorker extends Worker implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductOnlineWorker.class);

    private ApplicationContext applicationContext;

    @Autowired
    private TaskProducer taskProducer;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void exec() {
        LOGGER.info("在线同步渠道侧产品从队列中取消息");
        ProductOnlinePojo pojo = new ProductOnlinePojo();
        String msg = getTaskString();
        LOGGER.info("从队列中取消息:{}", msg);
        try {
            pojo = JSON.parseObject(msg, ProductOnlinePojo.class);
        } catch (JSONException e) {
            LOGGER.error("在线同步渠道侧产品解析Json消息异常");
        }
        Long entId = pojo.getEntId();
        String fingePrint = pojo.getFingerPrint();
        ProductAutoService productAutoService = chooseAutoService(fingePrint);
        if (!productAutoService.autoCreateRelation(entId)) {
            LOGGER.error("在线同步渠道侧产品失败了");
            // 在线同步渠道侧产品失败，重新入队
            taskProducer.productProductOnlineMsg(pojo);
        }
    }

    /**
     * 根据指纹返回相应的同步服务
     *
     * @param fingerPrint
     * @return
     */
    private ProductAutoService chooseAutoService(String fingerPrint) {
        List<ProductAutoService> autoServices = new LinkedList<ProductAutoService>(applicationContext.getBeansOfType(ProductAutoService.class).values());
        for (ProductAutoService autoService : autoServices) {
            if (autoService.getFingerPrint().equals(fingerPrint)) {
                return autoService;
            }
        }
        return null;
    }
}
