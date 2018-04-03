package com.cmcc.vrp.queue.task;

import com.cmcc.vrp.province.model.DeadLetterInfo;
import com.cmcc.vrp.province.service.DeadLetterInfoService;
import com.cmcc.vrp.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 死信队列的工作类,它的工作就是将死信队列中的消息存储到数据库中
 * <p>
 * <p>
 * Created by sunyiwei on 2016/6/17.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeadLetterWorker extends Worker {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeadLetterWorker.class);

    @Autowired
    DeadLetterInfoService deadLetterInfoService;

    @Override
    public void exec() {
        String taskString = getTaskString();

        if (!deadLetterInfoService.create(build(taskString))) {
            LOGGER.error("插入死信队列记录时出错.");
            throw new RuntimeException();
        }
    }

    private DeadLetterInfo build(String content) {
        DeadLetterInfo deadLetterInfo = new DeadLetterInfo();

        deadLetterInfo.setContent(content);
        deadLetterInfo.setCreateTime(new Date());
        deadLetterInfo.setUpdateTime(new Date());
        deadLetterInfo.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());

        return deadLetterInfo;
    }
}
