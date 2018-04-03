package com.cmcc.vrp.queue.task;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.ec.bean.CallBackReq;
import com.cmcc.vrp.ec.bean.CallBackReqData;
import com.cmcc.vrp.ec.bean.CallbackResp;
import com.cmcc.vrp.ec.utils.CallbackResult;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.EntCallbackAddr;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EntCallbackAddrService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.CallbackPojo;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;

import com.cmcc.vrp.util.HttpUtils;
import com.thoughtworks.xstream.XStream;

/**
 * Created by leelyn on 2016/8/11.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CallbackEcPlatformWorker extends Worker {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackEcPlatformWorker.class);
    private static XStream xStream;
    
//    //可以重试的次数
//    private final Integer canRetryTimes = new Integer(3);
// 
//    //重试中间隔的时间种子(second)
//    private final Integer retrySeconds = new Integer(5);
    

    static {
        xStream = new XStream();
        xStream.alias("Request", CallBackReq.class);
        xStream.alias("Response", CallbackResp.class);

        xStream.autodetectAnnotations(true);
    }

    @Autowired
    private EntCallbackAddrService entCallbackAddrService;
    @Autowired
    private ChargeRecordService recordService;
    @Autowired
    private Gson gson;
    @Autowired
    TaskProducer taskProducer;
    @Autowired
    ProductService productService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    EnterprisesService enterprisesService;

    @Override
    public void exec() {
        //获取消息
        String msg = getTaskString();
        LOGGER.info("从回调EC平台消息队列中获取消息:{}", msg);
        CallbackPojo pojo = gson.fromJson(msg, CallbackPojo.class);

        Long entId = pojo.getEntId();
        String serialNum = pojo.getSerialNum();

        if (entId == null
            || StringUtils.isBlank(serialNum)) {
            LOGGER.error("消息队列中的消息为空");
            return;
        }

        ChargeRecord chargeRecord;

        if ((chargeRecord = recordService.getRecordBySN(serialNum)) != null
            && callback(entId, chargeRecord)) {
            LOGGER.info("回调EC平台成功,serialNum:{}", serialNum);
        } else {
            LOGGER.info("回调EC平台失败,serialNum:{}", serialNum);
        }        
    }


    //回调方法
    private boolean callback(Long entId, ChargeRecord chargeRecord) {
        EntCallbackAddr entCallbackAddr = entCallbackAddrService.get(entId);
        Enterprise enterprise = enterprisesService.selectById(entId);
        if (enterprise == null
                || entCallbackAddr == null
                || StringUtils.isBlank(entCallbackAddr.getCallbackAddr())
                || entCallbackAddr.getDeleteFlag() == Constants.DELETE_FLAG.DELETED.getValue()) {
            LOGGER.info("企业的回调信息配置错误，回调失败. 具体信息为{}.", entCallbackAddr == null ? "空" : new Gson().toJson(entCallbackAddr));
            return false;
        }

        //回调
        String callbackUrl = entCallbackAddr.getCallbackAddr();
        String serialNum = chargeRecord.getSerialNum();
        if ("chongqing".equals(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()))
                && getEnterCode().equals(enterprise.getCode())
                && serialNum.startsWith("cq")
                && serialNum.endsWith("yqx")) {
            callbackUrl = getYqxCallBackUrl();
        }
        String reqStr = buildCallbackStr(chargeRecord);
        /*String respStr = HttpUtil.doPost(callbackUrl, reqStr, "application/xml", "utf-8", true);
        CallbackResp callbackResp = parseCallbackResp(respStr);
        if (callbackResp == null
            || CallbackResult.fromCode(callbackResp.getCode()) != CallbackResult.SUCCESS) {
            LOGGER.error("回调异步充值结果时返回失败，EC侧返回的错误信息为{}.", callbackResp == null ? "响应结果无法解析." : callbackResp.getMessage());
            return false;
        }

        return true;*/
        return tryCallBack(callbackUrl, reqStr);
    }
    
    /**
     * 需求如下
     * 平台在有BOSS侧明确返回的结果下，将隔5s、10s、15s进行最多三次的重发，该改造将在大版本进行，所有平台适用。
     */
    private boolean tryCallBack(String callbackUrl,String reqStr){
        Integer canRetryTimes = getCanRetryTimes();
        Integer retrySeconds = getRetrySeconds();
        CallbackResp callbackResp = null;
        RateLimiter rateLimiter = RateLimiter.create(1.0);//Max 1 call per sec
        for(int i=1;i<= canRetryTimes+1 ;i++){//总共尝试的次数为1+canRetryTimes
            rateLimiter.acquire(i*retrySeconds);
            
            String respStr = HttpUtils.post(callbackUrl, reqStr, "application/xml");
            callbackResp = parseCallbackResp(respStr);
            if (callbackResp == null
                || CallbackResult.fromCode(callbackResp.getCode()) != CallbackResult.SUCCESS) {
                LOGGER.error("回调异步充值结果时返回失败，EC侧返回的错误信息为{},"
                        + "回调内容为{},"
                        + "回调地址为{},"
                        + "已发送次数{}.", 
                        callbackResp == null ? "响应结果无法解析." : callbackResp.getMessage(),reqStr,callbackUrl,i);
            }else{
                return true;
            } 
        }
        return false;
    }

    private String buildCallbackStr(ChargeRecord chargeRecord) {
        CallBackReq callBackReq = buildCallback(chargeRecord);

        OutputStream os = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(os, Charsets.UTF_8);
        xStream.toXML(callBackReq, writer);

        return os.toString();
    }

    private CallbackResp parseCallbackResp(String respStr) {
        try {
            return (CallbackResp) xStream.fromXML(respStr);
        } catch (Exception e) {
            LOGGER.error("解析回调响应结果时出错，回调响应结果为{}.", respStr);
            return null;
        }
    }

    private CallBackReq buildCallback(ChargeRecord chargeRecord) {
        CallBackReq callBackReq = new CallBackReq();

        ChargeRecordStatus chargeRecordStatus = ChargeRecordStatus.fromValue(chargeRecord.getStatus());
        CallBackReqData callBackReqData = new CallBackReqData();
        callBackReqData.setEcSerialNum(chargeRecord.getSerialNum());
        callBackReqData.setSystemNum(chargeRecord.getSystemNum());
        callBackReqData.setMobile(chargeRecord.getPhone());
        callBackReqData.setStatus(chargeRecord.getStatus());
        callBackReqData.setChargeTime(new DateTime(chargeRecord.getChargeTime()).toString());
        callBackReqData.setDescription(chargeRecordStatus == null ? "" : chargeRecordStatus.getMessage());

        callBackReq.setCallBackReqData(callBackReqData);
        callBackReq.setDateTime(new DateTime().toString());

        return callBackReq;
    }


    private Integer getCanRetryTimes() {
        String canRetryTimes = globalConfigService.get(GlobalConfigKeyEnum.CALLBACK_RETRY_TIMES.getKey());
        return NumberUtils.toInt(canRetryTimes);
    }
    private Integer getRetrySeconds() {
        String retrySeconds = globalConfigService.get(GlobalConfigKeyEnum.CALLBACK_RETRY_SECONDS.getKey());
        return NumberUtils.toInt(retrySeconds);
    }
    
    public String getEnterCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_ORDERID_ENTER_CODE.getKey());
    }
    
    public String getYqxCallBackUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.YQX_CQ_CALLBACK_URL.getKey());
    }
}
