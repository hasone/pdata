package com.cmcc.vrp.queue.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.cmcc.vrp.province.dao.SmsRecordMapper;
import com.cmcc.vrp.province.model.SmsRecord;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.sms.SendMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 短信下发任务work类
 * <p>
 * Created by sunyiwei on 2016/4/8.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeliverSmsWorker extends Worker {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeliverSmsWorker.class);

    @Autowired
    SendMessageService sendMessageService;

    @Autowired
    SmsRecordMapper smsRecordMapper;

    @Override
    public void exec() {
        SmsPojo pojo = null;
        String taskString = getTaskString();

        try {
            pojo = JSON.parseObject(taskString, SmsPojo.class);
            LOGGER.info("开始处理下行短信" + pojo.getMobile());
        } catch (JSONException e) {
            LOGGER.info("短信发送错误JSONException-" + e.getMessage());
            return;
        }

        //短信接口
        if (sendMessageService.send(pojo)) {
            SmsRecord record = new SmsRecord();
            record.setContent(pojo.getContent());
            record.setMobile(pojo.getMobile());
            record.setCreateTime(new Date());
            record.setDeleteFlag(0);
            smsRecordMapper.insert(record);
        }
    }
}
