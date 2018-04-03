package com.cmcc.vrp.queue.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by leelyn on 2016/12/1.
 */
public class PorpertiesConfigurer extends PropertyPlaceholderConfigurer {

    private Map ctxPropertiesMap;

    /**
     * @title: processProperties
     * */
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);

        ctxPropertiesMap = new HashMap();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            ctxPropertiesMap.put(keyStr, value);
        }
    }
    /**
     * @title: getPropertiesValue
     * */
    public Object getPropertiesValue(String key) {
        return ctxPropertiesMap.get(key);
    }

}
