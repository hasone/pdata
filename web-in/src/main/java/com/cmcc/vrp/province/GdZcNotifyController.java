package com.cmcc.vrp.province;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.vrp.ec.bean.NotifyReq;
import com.cmcc.vrp.ec.bean.NotifyResp;
import com.cmcc.vrp.enums.WxTemplateMsgType;
import com.cmcc.vrp.province.model.ActivityPaymentInfo;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityPaymentInfoService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.CrowdFundingService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.wx.beans.TemplateMsgPojo;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

/**
 * <p>Title: </p> <p>Description: </p>
 *
 * @author lgk8023
 * @date 2017年1月6日 上午10:34:42
 */
@Controller
@RequestMapping(value = "gdzc")
public class GdZcNotifyController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsynCallbackController.class);
    private static XStream xstream;

    static {
        xstream = new XStream();
        xstream.alias("NotifyReq", NotifyReq.class);
        xstream.alias("NotifyResp", NotifyResp.class);
        xstream.autodetectAnnotations(true);
    }

    @Autowired
    ActivityPaymentInfoService activityPaymentInfoService;
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    ChargeRecordService chargeRecordService;
    @Autowired
    SerialNumService serialNumService;
    @Autowired
    TaskProducer taskProducer;
    @Autowired
    CrowdFundingService crowdFundingService;

    /**
     * @param request
     * @param response
     */
    @RequestMapping(value = "notify", method = RequestMethod.POST)
    @ResponseBody
    public void payNotify(final HttpServletRequest request, final HttpServletResponse response) {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {

                //获取平台的回调对象
                NotifyReq notifyReq = parse(request);
                
                if (notifyReq == null) {
                    return null;
                }

                ActivityPaymentInfo activityPaymentInfo = activityPaymentInfoService.selectBySysSerialNum(notifyReq.getOrderID());
                if (activityPaymentInfo == null) {
                    LOGGER.info("activityPaymentInfo为空.serialNum={}", notifyReq.getOrderID());
                    return null;
                }

                boolean chargeTag = false;//表示是否可以进入充值流程
                
                //发送支付模板消息
                TemplateMsgPojo msgPojo = new TemplateMsgPojo();
                ActivityWinRecord winRecord = activityWinRecordService.selectByRecordId(activityPaymentInfo.getWinRecordId());
                msgPojo.setActivityId(winRecord.getActivityId());
                msgPojo.setPaymentSerial(activityPaymentInfo.getSysSerialNum());               
                msgPojo.setMobile(winRecord.getOwnMobile());
                
                //支付
                //支付结果，2：支付成功； 3：支付失败；4，退款成功 5：退款失败：6未知异常,7等待付款, 8已取消, 9超时, 10关闭
                //ADC支付回调的结果1：等待付款,2：成功,3：已取消,4：超时,5：失败,6：部分退款
                if (notifyReq.getCategory().equals("1")) {
                    //判断是否是重复的支付回调，如果支付状态是成功、失败、未知异常、关闭订单，则不进行支付状态的更新
                    if(activityPaymentInfo.getStatus().equals(2) || activityPaymentInfo.getStatus().equals(3) 
                            || activityPaymentInfo.getStatus().equals(6) || activityPaymentInfo.getStatus().equals(10)){
                        LOGGER.info("支付记录状态为终结状态，不进行再次更新。支付序列号{}，支付记录状态{}，接收到回调的ADC支付状态码{}",
                                notifyReq.getOrderID(), activityPaymentInfo.getStatus(), notifyReq.getPaymentStatus());
                        return null;
                    }
       
                    activityPaymentInfo.setReturnSerialNum(notifyReq.getaDCOrderID());
                    activityPaymentInfo.setReturnPayNum(notifyReq.getPaymentOrderID());
                    activityPaymentInfo.setReturnCategory(NumberUtils.toInt(notifyReq.getCategory()));
                    activityPaymentInfo.setReturnPayAmount(NumberUtils.toLong(notifyReq.getAmount()));
                    activityPaymentInfo.setReturnPayStatus(NumberUtils.toInt(notifyReq.getPaymentStatus()));
                    activityPaymentInfo.setChargeUpdateTime(new Date());
                    //支付不成功
                    if (("5").equals(notifyReq.getPaymentStatus())) {
                        //修改支付记录的状态为：失败                        
                        activityPaymentInfo.setStatus(3);
                        winRecord.setPayResult(3);
                        //模板消息，支付失败
                        msgPojo.setType(WxTemplateMsgType.PAY_FAIL);
                    
                    } else if (("2").equals(notifyReq.getPaymentStatus())) { //支付成功
                        //修改支付记录的状态为：成功
                        activityPaymentInfo.setStatus(2);
                        winRecord.setPayResult(2);
                        //模板消息，支付成功
                        msgPojo.setType(WxTemplateMsgType.PAY_SUCCESS);
                        
                        chargeTag = true;//可以进入充值流程

                    } else if (("1").equals(notifyReq.getPaymentStatus())){
                        //修改支付记录的状态为：等待付款
                        activityPaymentInfo.setStatus(7);
                    } else if (("3").equals(notifyReq.getPaymentStatus())){
                        //修改支付记录的状态为：已取消
                        activityPaymentInfo.setStatus(8);
                    } else if (("4").equals(notifyReq.getPaymentStatus())){
                        //修改支付记录的状态为：超时
                        activityPaymentInfo.setStatus(9);                        
                    } else {//其他错误
                        //修改支付记录的状态为：未知错误
                        activityPaymentInfo.setStatus(6);
                        winRecord.setPayResult(6);
                    }                    
                }

                //退款
                if (notifyReq.getCategory().equals("2")) {
                    //判断是否是重复的退款回调，如果支付状态是退款成功、退款失败、未知异常，则不进行支付状态的更新
                    if(activityPaymentInfo.getStatus().equals(4) || activityPaymentInfo.getStatus().equals(5) 
                            || activityPaymentInfo.getStatus().equals(6)){
                        LOGGER.info("支付记录状态为终结状态，不进行再次退款状态的更新。支付序列号{}，支付记录状态{}，接收到回调的ADC支付状态码{}",
                                notifyReq.getOrderID(), activityPaymentInfo.getStatus(), notifyReq.getPaymentStatus());
                        return null;
                    }
       
                    activityPaymentInfo.setReturnRefundAmount(NumberUtils.toLong(notifyReq.getAmount()));
                    activityPaymentInfo.setRefundTime(new Date());
                    activityPaymentInfo.setReturnCategory(NumberUtils.toInt(notifyReq.getCategory()));
                    activityPaymentInfo.setReturnPayStatus(NumberUtils.toInt(notifyReq.getPaymentStatus()));

                    //支付不成功
                    if (("5").equals(notifyReq.getPaymentStatus())) {
                        //修改支付记录的状态为：退款失败
                        activityPaymentInfo.setStatus(5);
                        winRecord.setPayResult(5);//支付结果，4，退款成功 5：退款失败：6未知异常
                    } else if (("3").equals(notifyReq.getPaymentStatus())) { //ADC返回3表示已取消，说明退款成功
                        //修改支付记录的状态为：退款成功
                        activityPaymentInfo.setStatus(4);
                        winRecord.setPayResult(4);
                        //模板消息，退款成功
                        msgPojo.setType(WxTemplateMsgType.REFUND);
                        
                        LOGGER.info("众筹退款成功，删除serial_num表中platform_serial_num为{}的记录，目前activity_win_record记录的值为{}",
                                winRecord.getRecordId(), new Gson().toJson(winRecord));
                        if(!serialNumService.deleteByPlatformSerialNum(winRecord.getRecordId())){
                            LOGGER.info("删除 serial_num表中platform_serial_num为{}的记录失败", winRecord.getRecordId());
                        }

                    } else {//未知错误
                        //修改支付记录的状态为：未知错误
                        activityPaymentInfo.setStatus(6);
                        winRecord.setPayResult(6);
                    }

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
                    /*if(!charge(activityPaymentInfo.getWinRecordId())){
                        LOGGER.error("进入充值流程失败！activityWinRecordId={}", activityPaymentInfo.getWinRecordId());
                    }*/
                }

                if (("1").equals(notifyReq.getAckRequired())) {
                    //返回响应
                    NotifyResp resp = null;
                    resp = buildNotifyResp(notifyReq.getMessageID());
                    OutputStream os = new ByteArrayOutputStream();
                    Writer writer = new OutputStreamWriter(os, "UTF-8");
                    xstream.toXML(resp, writer);
                    return os.toString();

                }
                return null;
            }
        };
        try {
            String rep = callable.call();
            response.getWriter().write(rep);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "payBack", method = RequestMethod.GET)
    public String payBack(HttpServletRequest request, final HttpServletResponse response) {

        String req = request.getQueryString();
        Map<String, String> map = getQueryParams(req);
        JSONObject jsonObject = JSONObject.fromObject(map);

        LOGGER.info("众筹支付同步页面响应内容为{}.", jsonObject == null ? "空" : jsonObject.toString());
        
        return "redirect:/wx/zhongchou/list.html";

    }

    private Map<String, String> getQueryParams(String req) {
        Map<String, String> params = new HashMap<String, String>();
        try {
            for (String param : req.split("&")) {
                String[] pair = param.split("=");
                String key = URLDecoder.decode(pair[0], "UTF-8");
                String value = "";
                if (pair.length > 1) {
                    value = URLDecoder.decode(pair[1], "UTF-8");
                }
                params.put(key, value);
            }
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError(ex);
        }
        return params;
    }

    private NotifyResp buildNotifyResp(String messageID) {
        NotifyResp notifyResp = new NotifyResp();
        notifyResp.setMessageID(messageID);
        notifyResp.setResult("0");
        notifyResp.setDescription("已成功接收");
        return notifyResp;
    }

    private NotifyReq parse(HttpServletRequest request) {
        String respStr = null;
        try {
            respStr = StreamUtils.copyToString(request.getInputStream(), Charsets.UTF_8);
            LOGGER.info("从BOSS侧接收支付回调信息， 回调内容为{}.", respStr == null ? "空" : respStr);
        } catch (IOException e) {
            LOGGER.error("解析回调参数时出错.");
            return null;
        }

        return (NotifyReq) xstream.fromXML(respStr);
    }

}
