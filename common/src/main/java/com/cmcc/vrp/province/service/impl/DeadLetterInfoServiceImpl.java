package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.DeadLetterInfoMapper;
import com.cmcc.vrp.province.model.DeadLetterInfo;
import com.cmcc.vrp.province.service.DeadLetterInfoService;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sunyiwei on 2016/6/17.
 */
@Service
public class DeadLetterInfoServiceImpl implements DeadLetterInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeadLetterInfoServiceImpl.class);

    @Autowired
    DeadLetterInfoMapper deadLetterInfoMapper;

    @Override
    public boolean create(DeadLetterInfo deadLetterInfo) {
        if (deadLetterInfo == null || StringUtils.isBlank(deadLetterInfo.getContent())) {
            LOGGER.error("无效的死信消息对象，创建记录失败. 内容为{}.", deadLetterInfo == null ? "" : new Gson().toJson(deadLetterInfo));
            return false;
        }

        return deadLetterInfoMapper.insert(deadLetterInfo) == 1;
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            LOGGER.error("无效的记录ID列表，批量删除记录失败.");
            return false;
        }

        deadLetterInfoMapper.batchDelete(ids);
        return true;
    }

    @Override
    public List<DeadLetterInfo> getAllUndeletedInfos() {
        return deadLetterInfoMapper.getAllUndeletedRecords();
    }
}
