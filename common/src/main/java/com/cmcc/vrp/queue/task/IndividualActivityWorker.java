package com.cmcc.vrp.queue.task;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.model.flowredpacket.FlowRedPacketReq;
import com.cmcc.vrp.boss.sichuan.model.flowredpacket.FlowRedPacketResp;
import com.cmcc.vrp.boss.sichuan.model.flowredpacket.FlowRedPacketRespOutdata;
import com.cmcc.vrp.boss.sichuan.service.ScFlowRedPacketService;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.pojo.ActivitiesWinPojo;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * IndividualActivityWorker.java
 * @author wujiamin
 * @date 2017年1月13日
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IndividualActivityWorker extends Worker {

    private static final Logger logger = LoggerFactory.getLogger(IndividualActivityWorker.class);
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    ActivityPrizeService activityPrizeService;
    @Autowired
    ScFlowRedPacketService scFlowRedPacketService;
    @Autowired
    SerialNumService serialNumService;
    @Autowired
    IndividualAccountService individualAccountService;
    @Autowired
    IndividualProductService individualProductService;
    
    /*private static XStream xStream;

    static {
        xStream = new XStream();
        xStream.alias("Request", RedpacketCallBackReq.class);
        xStream.alias("Response", RedpacketCallbackResp.class);

        xStream.autodetectAnnotations(true);
    }*/

    //1. 解析充值参数
    //2. 更新记录状态为已发送充值请求
    //3. 发送充值请求
    //4. 更新记录状态为充值结果
    @Override
    public void exec() {
        //0. 获取队列消息
        String taskStr = getTaskString();
        logger.info("从充值队列中消费消息，消息内容为{}.", taskStr);

        //1. 解析充值参数
        ActivitiesWinPojo activityWinPojo;
        if (!validate(activityWinPojo = parse(taskStr))) {
            logger.error("无效的充值请求参数，充值失败.");
            return;
        }

        //获取参数
        Activities activities = activityWinPojo.getActivities();
        String activityWinRecordId = activityWinPojo.getActivitiesWinRecordId();
        ActivityWinRecord record;
        if (activities == null || activityWinRecordId == null
            || (record = activityWinRecordService.selectByRecordId(activityWinRecordId)) == null) {
            logger.error("活动对象或活动中奖记录id或中奖记录为空.");
            return;
        }
        
        //中奖记录状态必须是待充值
        if(!record.getStatus().equals(1)){
            logger.info("活动中奖记录状态非待充值1，status={}", record.getStatus());
            return;
        }

        ActivityWinRecord newRecord = new ActivityWinRecord();
        newRecord.setRecordId(activityWinRecordId);
        newRecord.setChargeTime(new Date());
        newRecord.setUpdateTime(new Date());
        
        // 向boss充值
        FlowRedPacketReq req = new FlowRedPacketReq();
        req.setPhoneNo(record.getChargeMobile());
        req.setOpType("2");//1、红包订购 2、红包落地
        String prcId = globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_FLOWREDPACKET_PRCID.getKey());
        req.setProdPrcid(prcId);
        req.setRedFlow(record.getSize());
        
        boolean back = true;//回退标识,true需要回退
        //是否是测试的boss
        String dynamicFlag = globalConfigService.get(GlobalConfigKeyEnum.DYNAMIC_PROXY_BOSS_FLAG.getKey());
        String finalFlag = StringUtils.isBlank(dynamicFlag) ? "false" : dynamicFlag;
        if(Boolean.parseBoolean(finalFlag)){
            newRecord.setStatus(ChargeRecordStatus.COMPLETE.getCode());
            newRecord.setReason("测试-充值成功");
            back = false;//测试，不需要回退
        }else{
            FlowRedPacketResp resp = scFlowRedPacketService.sendRequest(req);            
            if(resp !=null && "0000000".equals(resp.getResCode())){
                newRecord.setStatus(ChargeRecordStatus.COMPLETE.getCode());
                newRecord.setReason("充值成功");

                FlowRedPacketRespOutdata outData = resp.getOutData();
                if(outData!=null && !StringUtils.isEmpty(outData.getOrderId())){
                    SerialNum serialNum = new SerialNum();
                    serialNum.setPlatformSerialNum(activityWinRecordId);
                    serialNum.setBossRespSerialNum(outData.getOrderId());
                    serialNum.setUpdateTime(new Date());
                    serialNum.setCreateTime(new Date());
                    serialNum.setDeleteFlag(0);
                    serialNumService.insert(serialNum);
                }
                back = false;//充值成功，不需要回退
                
            }else{
                newRecord.setStatus(ChargeRecordStatus.FAILED.getCode());
                if(resp !=null){
                    newRecord.setReason(resp.getResMsg());
                }else{
                    newRecord.setReason("充值失败");
                }
            }
        }

        //4、更新充值记录
        if (!activityWinRecordService.updateByPrimaryKeySelective(newRecord)) {
            logger.error("fail to update charge flow status for individual repacket.");
        }

        //5、向活动账户回退余额（充值失败没有回退）
        /*if(back){
            logger.info("充值失败，开始回退活动账户,size={}, serialNum={}", record.getSize(), activityWinRecordId);
            IndividualProduct product = individualProductService.getDefaultFlowProduct();
            IndividualAccount account = individualAccountService.getAccountByOwnerIdAndProductId(activities.getId(), product.getId(), 
                    IndividualAccountType.INDIVIDUAL_ACTIVITIES.getValue());
            if(!individualAccountService.changeAccount(account, new BigDecimal(record.getSize()), activityWinRecordId,
                    (int)AccountRecordType.INCOME.getValue(), "中奖充值失败，退回活动账户", activities.getType(), 1)){
                logger.error("充值失败，回退活动账户失败,accountId={}, size={}, serialNum={}", account.getId(), record.getSize(), activityWinRecordId);
            }
        }*/
        
        //6、回调
        /*String callbackUrl = globalConfigService.get("JIZHONG_CALLBACK_ADDRESS");
        if(callback(callbackUrl, activityWinRecordId)){
            logger.info("回调EC平台成功，url={}，activityWinRecordId={}",callbackUrl, activityWinRecordId);
        }*/

    }

    //解析充值对象
    private ActivitiesWinPojo parse(String taskString) {
        try {
            return JSONObject.parseObject(taskString, ActivitiesWinPojo.class);
        } catch (Exception e) {
            logger.info("充值参数反序列化错误, 错误信息为{}， 具体的参数为{}", e.getMessage(), taskString);
            return null;
        }
    }

    //校验充值对象的有效性
    private boolean validate(ActivitiesWinPojo taskPojo) {
        if (taskPojo == null
            || taskPojo.getActivities() == null
            || taskPojo.getActivitiesWinRecordId() == null) {
            logger.error("无效的充值请求参数，ActivitiesWinPojo = {}.", JSONObject.toJSONString(taskPojo));
            return false;
        }

        return true;
    }
    
    /** 
     * 将中奖结果回调EC平台
     * @Title: callback 
     */
    /*private boolean callback(String callbackUrl, String activityWinRecordId){
        if (StringUtils.isBlank(callbackUrl)) {
            logger.info("企业的回调信息配置错误，回调地址为空.");
            return false;
        }
        
        ActivityWinRecord record = activityWinRecordService.selectByRecordId(activityWinRecordId);

        //回调
        String reqStr = buildCallbackStr(record);
        String respStr = HttpUtil.doPost(callbackUrl, reqStr, "application/xml", "utf-8", true);
        logger.info("回调企业返回：{}，serialNum={}", respStr, record.getRecordId());
        RedpacketCallbackResp callbackResp = parseCallbackResp(respStr);
        if (callbackResp == null
            || CallbackResult.fromCode(callbackResp.getCode()) != CallbackResult.SUCCESS) {
            logger.error("回调异步充值结果时返回失败，EC侧返回的错误信息为{}.", callbackResp == null ? "响应结果无法解析." : callbackResp.getMessage());
            return false;
        }
        return true;

    }*/
    
    /*private String buildCallbackStr(ActivityWinRecord record) {
        RedpacketCallBackReq callBackReq = buildCallback(record);

        OutputStream os = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(os, Charsets.UTF_8);
        xStream.toXML(callBackReq, writer);

        return os.toString();
    }
    
    private RedpacketCallBackReq buildCallback(ActivityWinRecord record) {
        RedpacketCallBackReq callBackReq = new RedpacketCallBackReq();

        RedpacketCallBackReqData callBackReqData = new RedpacketCallBackReqData();
        callBackReqData.setActivityId(record.getActivityId());
        callBackReqData.setMobile(record.getChargeMobile());
        Activities activity = activitiesService.selectByActivityId(record.getActivityId());
        //随机红包
        if(ActivityType.LUCKY_REDPACKET.getCode().equals(activity.getType())){
            callBackReqData.setSize(record.getSize());
        }
        //普通红包
        if(ActivityType.COMMON_REDPACKET.getCode().equals(activity.getType())){
            List<ActivityPrize> activityPrizes = activityPrizeService.selectByActivityIdForIndividual(activity.getActivityId());
            if(activityPrizes!=null && activityPrizes.size()==1){
                callBackReqData.setSize(activityPrizes.get(0).getSize().toString());
            }
        }
        
        callBackReqData.setWinTime(new DateTime(record.getWinTime()).toString());
        callBackReqData.setRecordId(record.getRecordId());
        callBackReqData.setChargeTime(new DateTime(record.getChargeTime()).toString());
        if(ChargeRecordStatus.COMPLETE.getCode().equals(record.getStatus())){
            callBackReqData.setChargeStatus(0);
            callBackReqData.setChargeMsg("成功");
        }
        if(ChargeRecordStatus.FAILED.getCode().equals(record.getStatus())){
            callBackReqData.setChargeStatus(1);
            callBackReqData.setChargeMsg(record.getReason());
        }
        
        callBackReq.setCallBackReqData(callBackReqData);
        
        return callBackReq;
    }

    private RedpacketCallbackResp parseCallbackResp(String respStr) {
        try {
            return (RedpacketCallbackResp) xStream.fromXML(respStr);
        } catch (Exception e) {
            logger.error("解析回调响应结果时出错，回调响应结果为{}.", respStr);
            return null;
        }
    }*/
}
