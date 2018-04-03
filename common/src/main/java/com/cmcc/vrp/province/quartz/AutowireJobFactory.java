package com.cmcc.vrp.province.quartz;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * 自动装填quartz任务实例。
 * <p>
 *
 * @author qiaohao
 */
public class AutowireJobFactory extends SpringBeanJobFactory {

    @Autowired
    AutowireCapableBeanFactory beanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle)
        throws Exception {

        // 调用父类方法创建任务实例
        Object jobInstance = super.createJobInstance(bundle);

        // 调用beanFacotry注入依赖
        beanFactory.autowireBean(jobInstance);

        return jobInstance;
    }

}