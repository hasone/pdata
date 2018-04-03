package com.cmcc.vrp.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.vrp.queue.pojo.SmsPojo;

/**
 * 用于测试
 * Created by sunyiwei on 2016/7/7.
 */
public class EmptySendMessageServiceImpl implements SendMessageService {
    private static Logger logger = LoggerFactory.getLogger(EmptySendMessageServiceImpl.class);
    @Override
    public boolean send(SmsPojo smsPojo) {
        logger.info("短信发送成功，发送对象：{},短信内容：{}",smsPojo.getMobile(), smsPojo.getContent());
        return true;
    }
}
