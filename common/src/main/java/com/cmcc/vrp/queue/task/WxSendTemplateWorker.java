package com.cmcc.vrp.queue.task;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.WxTemplateMsgType;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityPaymentInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityPaymentInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.wx.WetchatService;
import com.cmcc.vrp.wx.WxExchangeRecordService;
import com.cmcc.vrp.wx.beans.TemplateData;
import com.cmcc.vrp.wx.beans.TemplateMsg;
import com.cmcc.vrp.wx.beans.TemplateMsgPojo;
import com.cmcc.vrp.wx.beans.WxUserInfo;
import com.cmcc.vrp.wx.model.WxExchangeRecord;
import com.google.gson.Gson;

/**
 * 个人积分流量兑换
 * WxScoreExchangeWorker.java
 * @author wujiamin
 * @date 2017年3月16日
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WxSendTemplateWorker extends Worker {
    private static final Logger logger = LoggerFactory.getLogger(WxSendTemplateWorker.class);
    @Autowired
    WetchatService wetchatService;
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    @Autowired
    ActivityPrizeService activityPrizeService;
    @Autowired
    ProductService productService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    ActivityPaymentInfoService paymentInfoService;
    @Autowired
    WxExchangeRecordService wxExchangeRecordService;
    @Autowired
    IndividualProductService individualProductService;
    
    @Override
    public void exec() {
        //0. 获取队列消息
        String taskStr = getTaskString();
        logger.info("从发送模板消息队列中消费消息，消息内容为{}.", taskStr);

        //1. 解析模板消息参数
        TemplateMsgPojo pojo;
        if ((pojo = parse(taskStr)) == null) {
            logger.error("无效的发送模板消息请求参数，充值失败.");
            return;
        }
        
        //向微信公众号发送请求
        String param = getTemplateContentString(pojo.getType(), pojo.getMobile(), pojo.getActivityId(), pojo.getActivityWinRecordId(), 
                pojo.getPaymentSerial(), pojo.getExchangeSystemNum());
        String resp = wetchatService.sendTemplateMag(param);
        
        logger.info("模板消息发送返回：" + resp);        
    }

    //解析充值对象
    private TemplateMsgPojo parse(String taskString) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(taskString, TemplateMsgPojo.class);
        } catch (Exception e) {
            logger.info("参数反序列化错误, 错误信息为{}， 具体的参数为{}", e.getMessage(), taskString);
            return null;
        }
    }
    
    private String getTemplateContentString(WxTemplateMsgType type, String mobile, String activityId, String activityWinRecordId, String paymentSerial, 
            String exchangeSystemNum){
        TemplateMsg msg = new TemplateMsg();
        WxUserInfo userInfo = wetchatService.getWxUserInfo(mobile);
        if(userInfo == null){
            return null;
        }
        
        if(WxTemplateMsgType.CROWDFUNDING_SUCCESS.equals(type)){
            getTemplateCrowdfundingSuccessData(userInfo, activityId, activityWinRecordId, msg);
        }
        if(WxTemplateMsgType.PAY_SUCCESS.equals(type)){
            getPaySuccessData(userInfo, activityId, paymentSerial, msg);
        }
        if(WxTemplateMsgType.PAY_FAIL.equals(type)){
            getPayFailData(userInfo, activityId, paymentSerial, msg);
        }
        if(WxTemplateMsgType.REFUND.equals(type)){
            getRefundData(userInfo, activityId, paymentSerial, msg);
        }
        if(WxTemplateMsgType.EXCHANGE_SUCCESS.equals(type)){
            getExchangeSuccessData(userInfo, exchangeSystemNum, msg);
        }
        if(WxTemplateMsgType.EXCHANGE_FAIL.equals(type)){
            getExchangeFailData(userInfo, exchangeSystemNum, msg);
        }
 
        msg.setTouser(userInfo.getOpenid());//向公众号获取openid
        msg.setTemplateId(type.getTemplateId());

        return JSONObject.toJSONString(msg);
    }
    
    //封装模板消息参数
    private void getTemplateCrowdfundingSuccessData(WxUserInfo userInfo, String activityId, String activityWinRecordId, TemplateMsg msg) {
        Map<String, TemplateData> data = new HashMap<String, TemplateData>();
        Activities activity = activitiesService.selectByActivityId(activityId);
        TemplateData first = new TemplateData();
        first.setValue("尊敬的用户您好！您参加的" + activity.getName() + "已达到目标人数，请点击详情完成支付");
        data.put("first", first);

        ActivityWinRecord record = activityWinRecordService.selectByRecordId(activityWinRecordId);
        ActivityPrize prize = activityPrizeService.selectByPrimaryKey(record.getPrizeId());
        Product product = productService.selectProductById(prize.getProductId());       
        double price = product.getPrice() /100d * prize.getDiscount()/100;
        TemplateData money = new TemplateData();
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        money.setValue(nf.format(price) + "元");//支付金额
        data.put("keyword1", money); 


        TemplateData activityName = new TemplateData();
        activityName.setValue(activity.getName());//活动主题（众筹活动名称）
        data.put("keyword2", activityName);
        
        TemplateData time = new TemplateData();
        String startTime = DateUtil.dateToString(activity.getStartTime(), "yyyy-MM-dd HH:mm:ss");
        String endTime = DateUtil.dateToString(activity.getEndTime(), "yyyy-MM-dd HH:mm:ss");
        time.setValue(startTime + " -- " + endTime);//活动时间
        data.put("keyword3", time);


        TemplateData remark = new TemplateData();
        remark.setValue("支付后流量将在7个工作日内充入手机，充值结果以充值短信为准");
        data.put("remark", remark); 

        String addr = globalConfigService.get(GlobalConfigKeyEnum.PLATFORM_URL.getKey());
        msg.setUrl(addr + "/web-in/wx/zhongchou/activityDetail.html?activityId="+ activityId);
        msg.setData(data);
    }
    
    private void getPaySuccessData(WxUserInfo userInfo, String activityId, String paymentSerial, TemplateMsg msg){
        Map<String, TemplateData> data = new HashMap<String, TemplateData>();
        Activities activity = activitiesService.selectByActivityId(activityId);
        TemplateData first = new TemplateData();
        first.setValue("尊敬的用户您好！您已支付成功");
        data.put("first", first);

        TemplateData nickName = new TemplateData();
        nickName.setValue(userInfo.getNickname());//昵称
        data.put("keyword1", nickName); 

        TemplateData seriaNum = new TemplateData();
        seriaNum.setValue(paymentSerial);//订单号
        data.put("keyword2", seriaNum);
        
        ActivityPaymentInfo paymentInfo = paymentInfoService.selectBySysSerialNum(paymentSerial);
        TemplateData money = new TemplateData();
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        money.setValue(nf.format(paymentInfo.getPayAmount()/100d) + "元");//订单金额
        data.put("keyword3", money);

        TemplateData activityName = new TemplateData();
        activityName.setValue(activity.getName());//商品信息（众筹活动名称）
        data.put("keyword4", activityName);

        TemplateData remark = new TemplateData();
        remark.setValue("支付后流量将在7个工作日内充入手机，充值结果以充值短信为准");
        data.put("remark", remark); 
        
        msg.setUrl("");
        msg.setData(data);
    }

    private void getPayFailData(WxUserInfo userInfo, String activityId, String paymentSerial, TemplateMsg msg) {
        Map<String, TemplateData> data = new HashMap<String, TemplateData>();
        Activities activity = activitiesService.selectByActivityId(activityId);
        TemplateData first = new TemplateData();
        first.setValue("尊敬的用户您好！非常抱歉 " + activity.getName() + "支付失败");
        data.put("first", first);

        TemplateData seriaNum = new TemplateData();
        seriaNum.setValue(paymentSerial);//订单号
        data.put("keyword1", seriaNum);
        
        ActivityPaymentInfo paymentInfo = paymentInfoService.selectBySysSerialNum(paymentSerial);
        TemplateData money = new TemplateData();
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        money.setValue(nf.format(paymentInfo.getPayAmount()/100d) + "元");//支付金额
        data.put("keyword2", money);
        
        TemplateData payWay = new TemplateData();
        payWay.setValue("微信支付");//支付方式（默认微信支付）
        data.put("keyword3", payWay);

        TemplateData remark = new TemplateData();
        remark.setValue("点击详情再次支付");
        data.put("remark", remark); 
        
        String addr = globalConfigService.get(GlobalConfigKeyEnum.PLATFORM_URL.getKey());
        msg.setUrl(addr + "/web-in/wx/zhongchou/activityDetail.html?activityId="+ activityId);
        msg.setData(data);
    }
        
    private void getRefundData(WxUserInfo userInfo, String activityId, String paymentSerial, TemplateMsg msg){
        Map<String, TemplateData> data = new HashMap<String, TemplateData>();
        Activities activity = activitiesService.selectByActivityId(activityId);
        TemplateData first = new TemplateData();
        first.setValue("尊敬的用户您好！" + activity.getName() + "充值失败退款，请留意");
        data.put("first", first);

        TemplateData seriaNum = new TemplateData();
        seriaNum.setValue(paymentSerial);//订单号
        data.put("keyword1", seriaNum);
        
        ActivityPaymentInfo paymentInfo = paymentInfoService.selectBySysSerialNum(paymentSerial);
        TemplateData money = new TemplateData();
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        money.setValue(nf.format(paymentInfo.getPayAmount()/100d) + "元");//退款金额
        data.put("keyword2", money);

        msg.setUrl("");
        msg.setData(data);
    }    
    
    private void getExchangeSuccessData(WxUserInfo userInfo, String exchangeSystemNum, TemplateMsg msg) {
        Map<String, TemplateData> data = new HashMap<String, TemplateData>();
        TemplateData first = new TemplateData();
        first.setValue("尊敬的用户您好！您已兑换成功");
        data.put("first", first);

        TemplateData productName = new TemplateData();
        WxExchangeRecord record = wxExchangeRecordService.selectBySystemNum(exchangeSystemNum);
        IndividualProduct product = individualProductService.selectByPrimaryId(record.getIndividualProductId());
        productName.setValue(product.getName());//兑换商品        
        data.put("keyword1", productName); 

        TemplateData amount = new TemplateData();
        //根据产品大小和流量币兑换规则获取消耗的流量币
        String num = globalConfigService.get(GlobalConfigKeyEnum.WEIXIN_SCORE_EXCHANGE_RULE.getKey());       
        amount.setValue(new Long((product.getProductSize()/1024)/Long.parseLong(num)).toString() + "流量币");//消耗流量币
        data.put("keyword2", amount);
        
        TemplateData time = new TemplateData();
        time.setValue(DateUtil.dateToString(record.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));//订单金额
        data.put("keyword3", time);


        TemplateData remark = new TemplateData();
        remark.setValue("感谢您的使用与支持");
        data.put("remark", remark); 
        
        msg.setUrl("");
        msg.setData(data);
    }

    private void getExchangeFailData(WxUserInfo userInfo, String exchangeSystemNum, TemplateMsg msg) {
        Map<String, TemplateData> data = new HashMap<String, TemplateData>();
        TemplateData first = new TemplateData();
        first.setValue("尊敬的用户您好！兑换失败，流量币已退回");
        data.put("first", first);

        TemplateData productName = new TemplateData();
        WxExchangeRecord record = wxExchangeRecordService.selectBySystemNum(exchangeSystemNum);
        IndividualProduct product = individualProductService.selectByPrimaryId(record.getIndividualProductId());
        productName.setValue(new Long(product.getProductSize()/1024).toString() + "MB");//充值金额       
        data.put("money", productName); 

        TemplateData amount = new TemplateData();
        //根据产品大小和流量币兑换规则获取消耗的流量币
        String num = globalConfigService.get(GlobalConfigKeyEnum.WEIXIN_SCORE_EXCHANGE_RULE.getKey());       
        amount.setValue(new Long((product.getProductSize()/1024)/Long.parseLong(num)).toString() + "流量币");//消耗流量币
        data.put("product", amount);

        TemplateData remark = new TemplateData();
        remark.setValue("感谢您的使用与支持");
        data.put("remark", remark); 
        
        msg.setUrl("");
        msg.setData(data);
    }

}
