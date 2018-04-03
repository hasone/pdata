package com.cmcc.vrp.province.webin.controller;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.henan.Util.HaAES256;
import com.cmcc.vrp.boss.henan.model.HaCallbackReq;
import com.cmcc.vrp.boss.henan.model.HaConstats;
import com.cmcc.vrp.boss.henan.model.HaResult;
import com.cmcc.vrp.boss.henan.model.JdJsonParas;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.enums.FinanceStatus;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.InterfaceRecordService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.CallbackPojo;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;

/**
 * Created by leelyn on 2016/6/27.
 */
@Controller
@RequestMapping(value = "henan/charge")
public class HaCallbackController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HaCallbackController.class);

    @Autowired
    private Gson gson;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private InterfaceRecordService interfaceRecordService;

    @Autowired
    private ChargeRecordService recordService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TaskProducer taskProducer;

    @Autowired
    private SerialNumService serialNumService;

    @RequestMapping(value = "callback", method = RequestMethod.POST)
    public void callback(@RequestBody HaCallbackReq req, HttpServletResponse response) throws Exception {
        LOGGER.info("河南BOSS回调来了");
        String method;
        String jdJsonParas;
        String provinceCode;
        String sign;
        if (req == null
            || StringUtils.isBlank(method = req.getMethod())
            || StringUtils.isBlank(jdJsonParas = req.getJd_json_paras())
            || StringUtils.isBlank(provinceCode = req.getProvince_code())
            || StringUtils.isBlank(sign = req.getSign())) {
            LOGGER.error("河南回调参数缺失");
            return;
        }
        LOGGER.info("河南BOSS回调包体:{}", jdJsonParas);
        //应河南BOSS的要求取消校验签名
//        if (!isLegal(method, provinceCode, jdJsonParas, sign)) {
//            LOGGER.error("河南BOSS回调签名错误");
//            return;
//        }
        JdJsonParas paras = gson.fromJson(jdJsonParas, JdJsonParas.class);
        String result;
        HaResult haResult;
        try {
            if (paras != null
                && (paras.getRespCode().equals(HaConstats.CallbackReq.success.getCode()))
                && (result = paras.getResult()) != null
                && StringUtils.isNotBlank(result = HaAES256.decryption(result, getAPPKEY()))
                && (haResult = gson.fromJson(result, HaResult.class)) != null
                && updateRecordStatus(haResult)) {
                LOGGER.info("平台开始响应BOSS侧回调");
                response.setCharacterEncoding("UTF-8");
                JSONObject object = new JSONObject();
                object.put("respCode", HaConstats.CallbackResp.success.getCode());
                object.put("respDesc", HaConstats.CallbackResp.success.getMsg());
                response.getWriter().write(object.toString());
                LOGGER.info("平台已经响应BOSS侧回调");
                return;
            }
        } catch (Exception e) {
            LOGGER.error("响应河南回调抛出异常:{}", e);
        }
    }

    //获取到充值结果后更新记录状态
    private boolean updateRecordStatus(HaResult haResult) {
        // 如果查询标志为true,表示已经通过查询接口更新了状态,无需再更新充值状态了
        if (Boolean.parseBoolean(getQueryFlag())) {
            return true;
        }
        String bossReqNum;
        String systemNum;
        SerialNum serialNum;
        if (StringUtils.isBlank(bossReqNum = haResult.getCUST_ORDER_ID())
            || (serialNum = serialNumService.getByBossReqSerialNum(bossReqNum)) == null
            || StringUtils.isBlank(systemNum = serialNum.getPlatformSerialNum())) {
            LOGGER.error("bossReqNum:{} is not exist,", bossReqNum);
            return false;
        }
        ChargeRecord record = recordService.getRecordBySN(systemNum);
        if (record == null) {
            LOGGER.error("ChargeRecord:{} is not exist,", systemNum);
            return false;
        }
        Long entId = record.getEnterId();
        ChargeResult chargeResult;
        
        Date updateChargeTime = new Date();
        Integer financeStatus = null;
        
        if (haResult.getDEAL_RESULT().equals("Y")) {
            chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.SUCCESS);
        } else {
            chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.FAILURE);
            chargeResult.setFailureReason(haResult.getDEAL_MESSAGE());
            if (!accountService.returnFunds(systemNum)) {
                LOGGER.error("充值serialNum{},entId{}失败时账户返还失败", systemNum, entId);
            }else{
                financeStatus = FinanceStatus.IN.getCode();
            }
        }

        chargeResult.setFinanceStatus(financeStatus);
        chargeResult.setUpdateChargeTime(updateChargeTime);
        
        boolean result = recordService.updateStatus(record.getId(), chargeResult);

        // 将回调EC消息放入消息队列中
        callbackEC(entId, systemNum);

        return result;
    }

    /**
     * 回掉EC平台
     *
     * @param entId
     * @param systemNum
     */
    private void callbackEC(Long entId, String systemNum) {
        // 将消息入列，实现回调EC平台
        CallbackPojo callbackPojo = new CallbackPojo();
        callbackPojo.setEntId(entId);
        callbackPojo.setSerialNum(systemNum);
        taskProducer.productPlatformCallbackMsg(callbackPojo);
    }

    public String getAPPKEY() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_DECODE_KEY.getKey());
    }

    private String getQueryFlag() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HENAN_QUERY_FLAG.getKey());
    }
}
