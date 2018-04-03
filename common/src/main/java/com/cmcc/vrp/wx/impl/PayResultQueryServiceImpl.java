package com.cmcc.vrp.wx.impl;

import java.util.Date;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.gansu.HttpUtil;
import com.cmcc.vrp.enums.WxTemplateMsgType;
import com.cmcc.vrp.province.model.ActivityPaymentInfo;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.service.ActivityPaymentInfoService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.CrowdFundingService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.wx.PayResultQueryService;
import com.cmcc.vrp.wx.beans.TemplateMsgPojo;
import com.cmcc.vrp.wx.model.GetPaymentOrderReq;
import com.cmcc.vrp.wx.model.GetPaymentOrderResp;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月8日 下午3:14:48
*/
@Service("payResultQueryService")
public class PayResultQueryServiceImpl implements PayResultQueryService {
    private final static Logger LOGGER = LoggerFactory.getLogger(PayResultQueryServiceImpl.class);
    private static XStream xStream = null;
    private String cId = "21";
    static {
        xStream = new XStream();
        xStream.alias("GetPaymentOrderReq", GetPaymentOrderReq.class);
        xStream.alias("GetPaymentOrderResp", GetPaymentOrderResp.class);
        xStream.autodetectAnnotations(true);
    }
    @Autowired
    ActivityPaymentInfoService activityPaymentInfoService;
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    @Autowired
    ActivityWinRecordService activityWinRecordService;

    @Autowired
    TaskProducer taskProducer;
    
    @Autowired
    CrowdFundingService crowdFundingService;
    
    public static void main(String[] args) {
        PayResultQueryServiceImpl payResultQueryService = new PayResultQueryServiceImpl();
        payResultQueryService.payResultQuery("GZH200045600420170503094800050044431");
    }

    @Override
    public GetPaymentOrderResp payResultQuery(String orderId) {
        LOGGER.info("广东众筹支付结果查询请求开始{}", orderId);
        ActivityPaymentInfo activityPaymentInfo = null;
        if (orderId == null
                || (activityPaymentInfo = activityPaymentInfoService.selectBySysSerialNum(orderId)) == null
                || !(activityPaymentInfo.getStatus() == 1)) {
            LOGGER.error("无效的支付结果查询请求参数orderid{}", orderId);
            return null;
        }

        GetPaymentOrderResp getPaymentOrderResp = null;
        GetPaymentOrderReq getPaymentOrderReq = buildGetPaymentOrderReq(orderId);      
        try {

            String req = xStream.toXML(getPaymentOrderReq);
            LOGGER.info("广东众筹支付结果查询请求：{}", getPaymentOrderReq);
            String url = getOrderRelationUrl("PayTx", "GetPaymentOrder");
            String response = HttpUtil.doPost(url, req, "application/xml", "utf-8", false);
            LOGGER.info("广东众筹支付结果查询返回：{}", response);
            if (StringUtils.isEmpty(response)) {
                return null;
            }
            getPaymentOrderResp = (GetPaymentOrderResp) xStream.fromXML(response);
            return getPaymentOrderResp;
        } catch (Exception e) {           
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    private GetPaymentOrderReq buildGetPaymentOrderReq(String orderId) {
        GetPaymentOrderReq getPaymentOrderReq = new GetPaymentOrderReq();
        getPaymentOrderReq.setChannelID(cId);
        getPaymentOrderReq.setOrderID(orderId);
        return getPaymentOrderReq;
    }
    private String getOrderRelationUrl(String svcCat, String svcCode) {
        return globalConfigService.get(GlobalConfigKeyEnum.GUANGDONG_ZHONGCHOU_GETPAYMENTORDER_URL.getKey()) + "?svc_cat=" + svcCat + "&svc_code=" + svcCode;
        //return "http://221.179.7.247/NGADCABInterface/TrafficZC/PmpServicesRec.aspx?svc_cat=" + svcCat + "&svc_code=" + svcCode;
    }
    
    /** 
     * 查询支付是否是支付中的状态，是-true，非支付中状态-false
     * @Title: checkPayingStatus 
     * @param orderId
     * @return
     * @Author: wujiamin
     * @date 2017年6月7日
    */
    @Override
    public Boolean checkPayingStatus(String orderId) {
        GetPaymentOrderResp result = payResultQuery(orderId);
        ActivityPaymentInfo activityPaymentInfo = activityPaymentInfoService.selectBySysSerialNum(orderId);  
        if("0".equals(result.getResult())){//查询成功
            String payStatus = result.getPaymentOrder().getStatus();//获取查询返回的支付状态编码
            LOGGER.info("查询支付结果orderId={}，查询到ADC返回的支付结果为{}", orderId, payStatus);
            //支付结果，2：支付成功； 3：支付失败；4，退款成功 5：退款失败：6未知异常,7等待付款, 8已取消, 9超时, 10关闭
            //ADC支付的结果0,实际测试中发现从支付平台中退出，查询会返回0,1：等待付款,2：成功,3：已取消,4：超时,5：失败,6：部分退款                      
            if (activityPaymentInfo != null) {
                ActivityWinRecord winRecord = activityWinRecordService.selectByRecordId(activityPaymentInfo.getWinRecordId());
                
                activityPaymentInfo.setReturnPayNum(result.getPaymentOrder().getPaymentOrderID());
                activityPaymentInfo.setReturnPayAmount(NumberUtils.toLong(result.getPaymentOrder().getAmount()));
                activityPaymentInfo.setReturnPayStatus(NumberUtils.toInt(payStatus));
                activityPaymentInfo.setChargeUpdateTime(new Date());
                
                //查询结果为：成功，失败，更新activity_payment_info/activity_win_record的支付结果为成功，失败；其他状态则不更新数据库支付状态
                if("2".equals(payStatus) || "5".equals(payStatus)){//ADC明确返回成功，失败，则说明支付状态已终结，返回false，表示不存在支付中的状态                    
                    //处理支付结果
                    processPayResult(payStatus, activityPaymentInfo, winRecord); 
                    return false;
                }else {                    
                    //处理支付结果
                    processPayResult(payStatus, activityPaymentInfo, winRecord); 
                    //等待付款、已取消、超时则显示恢复支付弹窗，返回true，表示存在支付中的状态 
                    return true;
                    
                }
            }else{
                LOGGER.info("activityPaymentInfo为空serialNum={}", orderId);
            }
        }else{//查询失败，表记录不做任何变更，返回true
            LOGGER.info("ADC查询支付结果失败，不用进行表记录状态变更", orderId);                   
            return true;
        }
        return false;        
    }
    
    /** 
     * 得到支付结果之后进行的处理（包括更新支付记录，更新活动参加记录，发送模板消息）
     * @Title: processPayResult 
     */
    private void processPayResult(String payStatus, ActivityPaymentInfo activityPaymentInfo, ActivityWinRecord winRecord){     
        //模板消息
        //发送支付模板消息
        TemplateMsgPojo msgPojo = new TemplateMsgPojo();
        msgPojo.setActivityId(winRecord.getActivityId());
        msgPojo.setPaymentSerial(activityPaymentInfo.getSysSerialNum());               
        msgPojo.setMobile(winRecord.getOwnMobile());
        
        boolean chargeTag = false;//表示是否可以进入充值流程
        if(!StringUtils.isEmpty(payStatus)){
            if (("5").equals(payStatus)) {
                //修改支付记录的状态为：失败                        
                activityPaymentInfo.setStatus(3);
                winRecord.setPayResult(3);
                //模板消息，支付失败
                msgPojo.setType(WxTemplateMsgType.PAY_FAIL);
            
            } else if (("2").equals(payStatus)) { //支付成功
                //修改支付记录的状态为：成功
                activityPaymentInfo.setStatus(2);
                winRecord.setPayResult(2);
                //模板消息，支付成功
                msgPojo.setType(WxTemplateMsgType.PAY_SUCCESS);
                
                chargeTag = true;//可以进入充值流程
    
            } else if (("1").equals(payStatus)){
                //修改支付记录的状态为：等待付款
                activityPaymentInfo.setStatus(7);
            } else if (("3").equals(payStatus)){
                //修改支付记录的状态为：已取消
                activityPaymentInfo.setStatus(8);
            } else if (("4").equals(payStatus)){
                //修改支付记录的状态为：超时
                activityPaymentInfo.setStatus(9);                        
            } else if(!("0").equals(payStatus)){//实际测试中，没有支付就退出支付平台会返回0，0的状态不去更新任何记录的支付状态
                //不为0的则表示其他错误
                //修改支付记录的状态为：未知错误
                activityPaymentInfo.setStatus(6);
                winRecord.setPayResult(6);
            }                    
        }else{//ADC查询结果没有具体的支付状态编码，直接修改为未知异常
            //修改支付记录的状态为：未知错误
            activityPaymentInfo.setStatus(6);
            winRecord.setPayResult(6);
        }
        
        //更新支付记录
        if (!activityPaymentInfoService.updateBySysSerialNumSelective(activityPaymentInfo)) {
            LOGGER.error("activityPaymentInfo更新时失败！记录SysSerialNum={}",activityPaymentInfo.getSysSerialNum());
        }
        
        //更新中奖记录activity_win_record
        winRecord.setUpdateTime(new Date());
        winRecord.setPaySerialNum(activityPaymentInfo.getSysSerialNum());
        if (!activityWinRecordService.updateByPrimaryKeySelective(winRecord)){
            LOGGER.error("activityWinRecord更新时失败！activityWinRecord_recordId={}", winRecord.getRecordId());
        }
        
        //发送支付模板消息
        if(msgPojo.getType()!=null){
            if(!taskProducer.produceWxSendTemplateMsg(msgPojo)){
                LOGGER.error("进入消息模板队列失败！msgPojo={}", new Gson().toJson(msgPojo));
            }
        }
        
        //可以进入充值流程
        if (chargeTag) {
            if (!crowdFundingService.charge(activityPaymentInfo.getWinRecordId())) {
                LOGGER.error("进入充值流程失败！activityWinRecordId={}", activityPaymentInfo.getWinRecordId());
            }
        }
    }

}
