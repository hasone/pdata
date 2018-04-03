package com.cmcc.vrp.boss.jiangxi.impl;


import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.jiangxi.JxReconcileService;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ChargeQueryPojo;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * @author lgk8023
 *
 */
@Service
public class JxReconcileServiceImpl implements JxReconcileService{
    private static Logger logger = LoggerFactory.getLogger(JxReconcileServiceImpl.class);
    @Autowired
    ChargeRecordService chargeRecordService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    TaskProducer taskProducer;

    @Override
    public boolean doDailyJob() {
        logger.info("江西查找充值结果定时任务开始:" + new Date().toString());
        Date yesterday = DateUtil.getDateBefore(new Date(), 1);
        Date start = DateUtil.getBeginOfDay(yesterday);
        Date end = DateUtil.getEndTimeOfDate(yesterday);
        Long supplierId = Long.valueOf(getSupplierId());
        logger.info("查询参数start{},end{},supplierid{}", start.toString(), end.toString(), supplierId);
        List<ChargeRecord>  chargeRecords = chargeRecordService.getJxChargeRecords(start, end, supplierId);
        if (chargeRecords == null
                || chargeRecords.isEmpty()) {
            logger.error("查询结果为空");
            return false;
        }
        for (ChargeRecord chargeRecord:chargeRecords) {
            ChargeQueryPojo pojo = new ChargeQueryPojo();
            pojo.setSystemNum(chargeRecord.getSystemNum());
            pojo.setFingerPrint("jiangxiquery");
            pojo.setEntId(chargeRecord.getEnterId());
            taskProducer.produceAsynChargeQueryMsg(pojo);
        }

        return true;
    }

    private String getSupplierId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_JIANGXI_SUPPLIERID.getKey());
    }
}
