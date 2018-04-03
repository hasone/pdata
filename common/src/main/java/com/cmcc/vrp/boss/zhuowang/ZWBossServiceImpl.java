package com.cmcc.vrp.boss.zhuowang;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.zhuowang.bean.OrderRequestResult;
import com.cmcc.vrp.boss.zhuowang.bean.UserData;
import com.cmcc.vrp.boss.zhuowang.service.FlowPackageOrderService;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ZwChargePojo;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sunyiwei on 2016/9/19.
 */
@Service
public class ZWBossServiceImpl implements BossService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZWBossServiceImpl.class);

    @Autowired
    FlowPackageOrderService<UserData> flowPackageOrderService;

    @Autowired
    SupplierProductService supplierProductService;

    @Autowired
    SerialNumService serialNumService;

    @Autowired
    TaskProducer taskProducer;

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        ZwChargePojo zwChargePojo = null;

        //由于卓望上游接口做了流速限制，这里只是简单地把它转投到相应的队列中去即认为是投递成功，后续的投递请求由后续的队列负责
        if ((zwChargePojo = build(entId, splPid, mobile, serialNum)) == null
                || !taskProducer.produceZwChargeMsg(zwChargePojo)) {
            LOGGER.error("无效的充值参数或者生产消息到队列时出错， ZwChargePojo = {}.", zwChargePojo == null ? "空" : new Gson().toJson(zwChargePojo));
            return new ZWBossOperationResultImpl(null);
        } else {
            OrderRequestResult orr = new OrderRequestResult();
            orr.setStatus(OrderRequestResult.ResultCode.SUCCESS.getStatus());
            orr.setRspDesc(ChargeRecordStatus.PROCESSING.getMessage());

            return new ZWBossOperationResultImpl(orr);
        }
    }

    @Override
    public String getFingerPrint() {
        return "zhuowang";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }

    private ZwChargePojo build(Long entId, Long splPid, String mobile, String serialNum) {
        if (entId == null || splPid == null || StringUtils.isBlank(mobile) || StringUtils.isBlank(serialNum)) {
            LOGGER.error("无效的充值请求参数, EntId = {}, SplPid = {}, Mobile = {}, SerialNum = {}.", entId, splPid, mobile, serialNum);
            return null;
        }

        ZwChargePojo chargePojo = new ZwChargePojo();
        chargePojo.setEntId(entId);
        chargePojo.setSplPid(splPid);
        chargePojo.setMobile(mobile);
        chargePojo.setSerialNum(serialNum);

        return chargePojo;
    }
}
