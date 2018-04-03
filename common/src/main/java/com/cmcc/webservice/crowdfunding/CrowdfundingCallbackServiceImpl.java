package com.cmcc.webservice.crowdfunding;

import com.cmcc.vrp.ec.bean.CallbackResp;
import com.cmcc.vrp.ec.utils.CallbackResult;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.CrowdfundingActivityDetail;
import com.cmcc.vrp.province.model.EntCallbackAddr;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.CrowdfundingActivityDetailService;
import com.cmcc.vrp.province.service.EntCallbackAddrService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.HttpUtils;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.webservice.crowdfunding.pojo.ActivityPojo;
import com.cmcc.webservice.crowdfunding.pojo.CallBackActSucReq;
import com.cmcc.webservice.crowdfunding.pojo.CallBackActSucReqData;
import com.cmcc.webservice.crowdfunding.pojo.CallBackActivityReq;
import com.cmcc.webservice.crowdfunding.pojo.CallBackChargeResultReq;
import com.cmcc.webservice.crowdfunding.pojo.CallBackChargeResultReqData;
import com.cmcc.webservice.crowdfunding.pojo.PrizePojo;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.io.Charsets;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

@Service("crowdfundingCallbackService")
public class CrowdfundingCallbackServiceImpl implements CrowdfundingCallbackService {
    private static final Logger logger =
            LoggerFactory.getLogger(CrowdfundingCallbackService.class);
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    ActivityPrizeService activityPrizeService;
    @Autowired
    CrowdfundingActivityDetailService crowdfundingActivityDetailService;
    @Autowired
    EntCallbackAddrService entCallbackAddrService;
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    private XStream xStream;

    /**
     * 通知EC平台创建活动成功
     *
     * @author qinqinyan
     */
    @Override
    public boolean notifyCreateActivity(String activityId) {
        // TODO Auto-generated method stub
        if (!StringUtils.isEmpty(activityId)) {
            Activities activities = activitiesService.selectByActivityId(activityId);
            if (activities == null ||
                    !(activities.getStatus().toString().equals(ActivityStatus.PROCESSING.getCode().toString())
                            || activities.getStatus().toString().equals(ActivityStatus.ON.getCode().toString()))) {
                logger.info("回调失败！");
                return false;
            }
            //new xStream
            xStream = new XStream();
            xStream.alias("Request", CallBackActivityReq.class);
            xStream.alias("Response", CallbackResp.class);

            xStream.autodetectAnnotations(true);

            String callbackStr = buildCallackActivityStr(activityId, xStream);
            //回调
            EntCallbackAddr entCallbackAddr = entCallbackAddrService.get(activities.getEntId());
            if (entCallbackAddr == null) {
                logger.info("通知EC活动创建成功，企业回调地址为空，entId={}", activities.getEntId());
                return false;
            }
            String callbackUrl = entCallbackAddr.getCallbackAddr();
            logger.info("通知EC活动创建成功，回调参数{}", callbackStr);
            String respStr = HttpUtils.post(callbackUrl, callbackStr, "application/xml");
            logger.info("返回参数-{}", respStr);
            if (StringUtils.isEmpty(respStr)) {
                logger.error("回调失败，返回参数为空。活动ID-{}", activityId);
                return false;
            }
            CallbackResp callbackResp = parseCallbackResp(respStr, xStream);
            if (callbackResp == null
                    || CallbackResult.fromCode(callbackResp.getCode()) != CallbackResult.SUCCESS) {
                logger.error("回调失败，返回参数为空。活动ID-{},返回参数-{}", activityId, respStr);
                return false;
            }
            logger.info("回调成功");
            return true;
        }
        return false;
    }

    private CallbackResp parseCallbackResp(String respStr, XStream xStream) {
        try {
            return (CallbackResp) xStream.fromXML(respStr);
        } catch (Exception e) {
            logger.error("解析回调响应结果时出错，回调响应结果为{}.", respStr);
            return null;
        }
    }

    private String buildCallackActivityStr(String activityId, XStream xStream) {
        Activities activities = activitiesService.selectByActivityId(activityId);
        List<ActivityPrize> prizes = activityPrizeService.selectByActivityId(activityId);
        CrowdfundingActivityDetail crowdfundingActivityDetail =
                crowdfundingActivityDetailService.selectByActivityId(activityId);

        CallBackActivityReq req = new CallBackActivityReq();
        req.setCallBackType("2");

        ActivityPojo record = new ActivityPojo();
        //活动ID加密处理
        record.setActivityId(activitiesService.encryptActivityId(activities.getActivityId()));
        record.setActivityName(activities.getName());
        record.setStatus(activities.getStatus().toString());
        String type = "yyyy-MM-dd HH:mm:ss";
        record.setStartTime(DateUtil.dateToString(activities.getStartTime(), type));
        record.setEndTime(DateUtil.dateToString(activities.getEndTime(), type));
        record.setChargeType(crowdfundingActivityDetail.getChargeType().toString());
        record.setHasWhiteOrBlack(crowdfundingActivityDetail.getHasWhiteOrBlack().toString());
        record.setTargetCount(crowdfundingActivityDetail.getTargetCount().toString());
        record.setCurrentCount(crowdfundingActivityDetail.getCurrentCount().toString());
        record.setResult(crowdfundingActivityDetail.getResult().toString());
        record.setRules(crowdfundingActivityDetail.getRules());
        record.setJoinType(crowdfundingActivityDetail.getJoinType().toString());

        List<PrizePojo> pojoList = new ArrayList<PrizePojo>();
        for (ActivityPrize item : prizes) {
            PrizePojo pojo = new PrizePojo();
            pojo.setPrizeId(item.getId());
            pojo.setPrizeName(item.getPrizeName());
            Double discount = item.getDiscount() / 10.0;
            pojo.setDiscount(discount.toString() + "折");
            pojo.setProductCode(item.getProductCode());
            pojo.setProductName(pojo.getProductName());

            pojoList.add(pojo);
        }
        record.setPrizes(pojoList);

        req.setAcitityPojo(record);
        req.setDateTime(new DateTime().toString());

        OutputStream os = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(os, Charsets.UTF_8);
        xStream.toXML(req, writer);

        return os.toString();
    }

    @Override
    public boolean notifyCrowdFundingSucceed(String activityId) {
        if (!StringUtils.isEmpty(activityId)) {
            Activities activities = activitiesService.selectByActivityId(activityId);
            //new xStream
            xStream = new XStream();
            xStream.alias("Request", CallBackActSucReqData.class);
            xStream.alias("Response", CallbackResp.class);

            xStream.autodetectAnnotations(true);

            String callbackStr = buildCallackCrowdFundingSucceedStr(activityId, xStream);
            logger.info("通知用户众筹成功接口，返回：" + callbackStr);
            //回调
            EntCallbackAddr entCallbackAddr = entCallbackAddrService.get(activities.getEntId());
            if (entCallbackAddr == null) {
                logger.info("通知EC活动众筹人数已到目标，众筹成功，企业回调地址为空，entId={}", activities.getEntId());
                return false;
            }
            String callbackUrl = entCallbackAddr.getCallbackAddr();
            logger.info("通知EC活动众筹人数已到目标，众筹成功，回调参数{}", callbackStr);
            String respStr = HttpUtils.post(callbackUrl, callbackStr, "application/xml");
            logger.info("返回参数-{}", respStr);
            if (StringUtils.isEmpty(respStr)) {
                logger.error("回调失败，返回参数为空。活动ID-{}", activityId);
                return false;
            }
            CallbackResp callbackResp = parseCallbackResp(respStr, xStream);
            if (callbackResp == null
                    || CallbackResult.fromCode(callbackResp.getCode()) != CallbackResult.SUCCESS) {
                logger.error("回调失败，返回参数为空。活动ID-{},返回参数-{}", activityId, respStr);
                return false;
            }
            logger.info("回调成功");
            return true;
        }
        return false;
    }

    private String buildCallackCrowdFundingSucceedStr(String activityId, XStream xStream) {
        Activities activities = activitiesService.selectByActivityId(activityId);
        CrowdfundingActivityDetail detail =
                crowdfundingActivityDetailService.selectByActivityId(activityId);

        CallBackActSucReq req = new CallBackActSucReq();
        req.setCallBackType("3");

        CallBackActSucReqData record = new CallBackActSucReqData();
        //activityId加密
        record.setActivityId(activitiesService.encryptActivityId(activities.getActivityId()));
        record.setActivityName(activities.getName());
        record.setCurrentCount(detail.getCurrentCount());
        record.setTargetCount(detail.getTargetCount());
        record.setResult(detail.getResult().toString());

        req.setData(record);
        req.setDateTime(new DateTime().toString());

        OutputStream os = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(os, Charsets.UTF_8);
        xStream.toXML(req, writer);

        return os.toString();
    }

    @Override
    public boolean notifyChargeResult(String activityWinRecordId) {
        if (!StringUtils.isEmpty(activityWinRecordId)) {
            ActivityWinRecord activityWinRecord = activityWinRecordService.selectByRecordId(activityWinRecordId);
            Activities activities = activitiesService.selectByActivityId(activityWinRecord.getActivityId());
            //new xStream
            xStream = new XStream();
            xStream.alias("Request", CallBackChargeResultReq.class);
            xStream.alias("Response", CallbackResp.class);

            xStream.autodetectAnnotations(true);

            String callbackStr = buildCallbackChargeResult(activityWinRecord, xStream);
            //回调
            EntCallbackAddr entCallbackAddr = entCallbackAddrService.get(activities.getEntId());
            if (entCallbackAddr == null) {
                logger.info("通知用户众筹充值结果接口，企业回调地址为空，entId={}", activities.getEntId());
                return false;
            }
            String callbackUrl = entCallbackAddr.getCallbackAddr();
            logger.info("通知用户众筹充值结果接口，回调参数{}", callbackStr);
            String respStr = HttpUtils.post(callbackUrl, callbackStr, "application/xml");
            logger.info("返回参数-{}", respStr);
            if (StringUtils.isEmpty(respStr)) {
                logger.error("通知用户众筹充值结果接口回调失败，返回参数为空。activityWinRecordId-{}", activityWinRecordId);
                return false;
            }
            CallbackResp callbackResp = parseCallbackResp(respStr, xStream);
            if (callbackResp == null
                    || CallbackResult.fromCode(callbackResp.getCode()) != CallbackResult.SUCCESS) {
                logger.error("通知用户众筹充值结果接口回调失败，返回参数为空。activityWinRecordId-{},返回参数-{}", activityWinRecordId, respStr);
                return false;
            }
            logger.info("通知用户众筹充值结果接口回调成功");
            return true;
        }
        return false;
    }

    private String buildCallbackChargeResult(ActivityWinRecord activityWinRecord, XStream xStream) {
        CallBackChargeResultReq req = new CallBackChargeResultReq();
        req.setCallBackType("4");
        req.setDateTime(new DateTime().toString());

        CallBackChargeResultReqData record = new CallBackChargeResultReqData();
        record.setSystemNum(activityWinRecord.getRecordId());
        record.setMobile(activityWinRecord.getChargeMobile());
        record.setStatus(activityWinRecord.getStatus());
        if (ChargeRecordStatus.COMPLETE.getCode().equals(activityWinRecord.getStatus())) {
            record.setDescription("充值成功");
        }
        if (ChargeRecordStatus.FAILED.getCode().equals(activityWinRecord.getStatus())) {
            record.setDescription("充值失败");
        }

        record.setChargeTime(DateUtil.dateToString(activityWinRecord.getChargeTime(), "yyyy-MM-dd HH:mm:ss"));

        req.setData(record);


        OutputStream os = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(os, Charsets.UTF_8);
        xStream.toXML(req, writer);

        return os.toString();
    }
    
    /*public static void  main(String args[]) {
        
        CallBackActivityReq req = new CallBackActivityReq();
        req.setCallBackType("2");
        //这里要进行加密，暂时未加，补充一个通用方法
        ActivityPojo record = new ActivityPojo();
        record.setActivityId("test");
        record.setActivityName("test");
        record.setStatus("test");
        String type = "yyyy-MM-dd HH:mm:ss";
        record.setStartTime(DateUtil.dateToString(new Date(), type));
        record.setEndTime(DateUtil.dateToString(new Date(), type));
        record.setChargeType("test");
        record.setHasWhiteOrBlack("test");
        record.setTargetCount("test");
        record.setCurrentCount("test");
        record.setResult("test");
        record.setRules("test");
        
        List<PrizePojo> pojoList= new ArrayList<PrizePojo>();
            PrizePojo pojo = new PrizePojo();
            pojo.setPrizeId(1L);
            pojo.setPrizeName("test");
            Double discount = 95/10.0;
            pojo.setDiscount(discount.toString()+"折");
            pojo.setProductCode("test");
            pojo.setProductName("test");
            pojoList.add(pojo);
            
            
            record.setPrizes(pojoList);
            req.setAcitityPojo(record);
        
        xStream = new XStream();
        xStream.alias("Request", CallBackActivityReq.class);
        //xStream.alias("Response", CallBackActivityReq.class);
        //xStream.
        xStream.autodetectAnnotations(true);
        
        OutputStream os = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(os, Charsets.UTF_8);
        xStream.toXML(req, writer);
        
        System.out.println(os.toString());
    }*/

}
