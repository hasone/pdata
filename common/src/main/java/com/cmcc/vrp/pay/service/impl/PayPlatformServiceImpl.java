package com.cmcc.vrp.pay.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.gansu.HttpUtil;
import com.cmcc.vrp.pay.enums.PayMethodType;
import com.cmcc.vrp.pay.enums.PayReturnType;
import com.cmcc.vrp.pay.enums.RefundType;
import com.cmcc.vrp.pay.enums.YqxRefundReturnType;
import com.cmcc.vrp.pay.enums.YqxRefundStatus;
import com.cmcc.vrp.pay.model.PayCallbackBody;
import com.cmcc.vrp.pay.model.PayCallbackResponse;
import com.cmcc.vrp.pay.model.PayHeader;
import com.cmcc.vrp.pay.model.PayReqBasicBody;
import com.cmcc.vrp.pay.model.PayReqBody;
import com.cmcc.vrp.pay.model.PayRequest;
import com.cmcc.vrp.pay.model.RefundAsyncResponse;
import com.cmcc.vrp.pay.model.RefundRequest;
import com.cmcc.vrp.pay.model.RefundResponse;
import com.cmcc.vrp.pay.model.Result;
import com.cmcc.vrp.pay.service.PayPlatformService;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.YqxOrderRecord;
import com.cmcc.vrp.province.model.YqxPayRecord;
import com.cmcc.vrp.province.model.YqxRefundRecord;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.YqxOrderRecordService;
import com.cmcc.vrp.province.service.YqxPayRecordService;
import com.cmcc.vrp.province.service.YqxRefundRecordService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.thoughtworks.xstream.XStream;

/**
 * 支付服务类
 *
 */
@Service("payPlatformService")
public class PayPlatformServiceImpl implements PayPlatformService {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(PayPlatformServiceImpl.class);
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    @Autowired
    YqxPayRecordService yqxPayRecordService;
    
    @Autowired
    YqxRefundRecordService yqxRefundRecordService;
    
    @Autowired
    AdministerService administerService;
    
    @Autowired
    YqxOrderRecordService yqxOrderRecordService;
    
    /**
     * 生成支付url
     */
    @Override
    public String generatePayUrl(String transactionId, String orderId,
            String amount, PayMethodType type, String payInfo) {
        //生成header
        PayHeader header = new PayHeader(getOriginId(), transactionId);
         
        //生成body
        PayReqBody body = new PayReqBody(getAccountCode(),getMerchantId(),getProductId(),getPayPeriod(),
                getPayNotifyIntURL(),getPayNotifyPageURL(),"");
        body.setOrderId(orderId);
        body.setPayAmount(amount);
        body.setPayType(type.getCode());
        body.setPayInfo(payInfo);
       
        //合并成request并加密
        PayRequest request = new PayRequest();
        request.setPayHeader(header);
        request.setPayBody(body); 
        request.encode(getEncodeSecret());
      
        //XStream生成xml和地址
        return getPayPlatformUrl() + generatePayXml(request);
    }
    
    /**
     * 
     * XStream生成xml
     */
    private String generatePayXml(PayRequest request){
        XStream xStream = new XStream();
        xStream.alias("AdvPay",PayRequest.class);
        xStream.alias("BusiData",PayReqBasicBody.class);
        xStream.autodetectAnnotations(true);
        
        String xml = xStream.toXML(request);
        
        return xml.replace("\n","").replace(" ", "");
    }
    
    
    /**
     * 分析充值返回xml
     */
    @Override
    public YqxPayRecord analyseCallBackXml(String xml) { 
        XStream xStream = new XStream();
        xStream.alias("AdvPay",PayCallbackResponse.class);
        xStream.alias("BusiData",PayCallbackBody.class);
        xStream.autodetectAnnotations(true);
        
        YqxPayRecord record = new YqxPayRecord();
        try {        
            PayCallbackResponse response = (PayCallbackResponse)xStream.fromXML(xml);
            if(checkValid(response) && checkVerifyCodeValid(response,xml)){//1.检测返回是否正确 //2.校验数据是否合法
                record.setPayTransactionId(response.getPayHeader().getTransactionId());
                record.setPayOrderId(response.getPaybody().getOrderId());
                record.setStatus(NumberUtils.toInt(response.getPaybody().getStatus())); 
                
                //如果充值成功，则设置支付平台流水号
                if(PayReturnType.SUCCESS.getCode().equals(response.getPaybody().getStatus())){
                    record.setDoneCode(response.getPaybody().getDoneCode());
                }
                
            }else{
                record.setStatus(NumberUtils.toInt(PayReturnType.UNKNOWN.getCode())); 
            }
        }catch(Exception e){
            record.setStatus(NumberUtils.toInt(PayReturnType.UNKNOWN.getCode())); 
            LOGGER.error("解析xml时出现错误,xml={}"+xml);
        }
        return record;
        
    }
    
    /**
     * 检测解析结果是否正确
     */
    private boolean checkValid(PayCallbackResponse response){
        if(response ==null || response.getPayHeader() == null || response.getPaybody()==null
                || StringUtils.isBlank(response.getPayHeader().getTransactionId())  
                || StringUtils.isBlank(response.getPaybody().getOrderId())
                || StringUtils.isBlank(response.getPaybody().getStatus())){
            return false;
        }
        String status = response.getPaybody().getStatus();

        return "0".equals(status) || "1".equals(status) || "2".equals(status);      
    }
    
    /**
     * 检查verifyCode是否有效
     * 异步通知返回的待加密字符串拼接和请求的时候不一样，是用transactionId+busiData的xml格式字符串;
     */
    private boolean checkVerifyCodeValid(PayCallbackResponse response,String xml){
        
        int stratIndex = xml.indexOf("<BusiData>");
        int endIndex = xml.indexOf("</BusiData>");
        
        if(stratIndex == -1 || endIndex == -1){
            return false;
        }
        
        String busiData = xml.substring(stratIndex, endIndex) + "</BusiData>";
        
        String encodeStr = response.getPayHeader().getTransactionId() + busiData + getEncodeSecret();
      
        
        return DigestUtils.md5Hex(encodeStr).equals(response.getPayHeader().getVerifyCode());
        
        
    }
    
    /**
     * generateRefundUrl
     */
    @Override
    public String generateRefundXml(String transactionId, String doneCode,
            String orderId, String refundReason,
            String amount, RefundType refundType) {
        if(StringUtils.isBlank(transactionId) || 
                StringUtils.isBlank(doneCode) ||
                StringUtils.isBlank(orderId) ||
                StringUtils.isBlank(refundReason) ||
                refundType == null ){
            LOGGER.error("退款字段不合法");
            return "";
        }
        
        //生成header
        PayHeader header = new PayHeader(getOriginId(), transactionId);
         
        //生成body
        RefundRequest body = new RefundRequest(doneCode, orderId, getRefundCallbackUrl(), refundReason, 
                amount, refundType.getCode());
        
        
        //合并成request并加密
        PayRequest request = new PayRequest();
        request.setPayHeader(header);
        request.setPayBody(body); 
        request.encode(getEncodeSecret());
      
        //XStream生成xml和地址
        try{
            return getRefundRequestUrl() + URLEncoder.encode(generateRefundXml(request), "utf-8");
        }catch (UnsupportedEncodingException e) {
            return "";
        }
    }
    
    /**
     * 
     * XStream生成xml
     */
    private String generateRefundXml(PayRequest request){
        XStream xStream = new XStream();
        xStream.alias("AdvPay",PayRequest.class);
        xStream.alias("BusiData",PayReqBasicBody.class);
        xStream.autodetectAnnotations(true);
        
        String xml = xStream.toXML(request);
        
        return xml.replace("\n","").replace(" ", "");
    }
    
    /**
     * 生成Url
     * 注，当前没有退款流水号生成方法，直接将transactionId当做流水号使用
     * 
     */
    private String generateYqxRefundXml(String doneCode,String refundReason,String amount,
            RefundType refundType,String transactionId) {
                  
        //String transactionId = yqxPayRecordService.getNewTransactionId();
                
        return generateRefundXml(transactionId, doneCode,
                transactionId,refundReason,amount, refundType);
    }
    
    /**
     * 云企信退款同步全流程,包括插入和更新数据库
     */
    @Override
    public Result yqxRefundProcess(Long id,String refundReason,String amount,RefundType refundType, Long operatorId) {
        //通过Id在YqxPayRecord表寻找记录
        YqxPayRecord record = yqxPayRecordService.selectByPrimaryKey(id);
        String doneCode =null;
        if(record == null || StringUtils.isBlank((doneCode = record.getDoneCode()))){
            LOGGER.error("云企新支付记录不存在或流水号不存在，id={}",id);
            return new Result(YqxRefundReturnType.NOTEXIST);
        }
        
        //生成发送的url
        String transactionId = yqxPayRecordService.getNewTransactionId();
        String url = generateYqxRefundXml(doneCode,refundReason,amount,refundType,transactionId);
        if(StringUtils.isBlank(url)){
            LOGGER.error("生成退款地址失败");
            return new Result(YqxRefundReturnType.GENEURLFAILED);
        }
               
        //插入到YqxRefundRecord表，成功返回key,失败返回null
        Long refundPrimaryKey  = insertYqxRefundRecord(doneCode,refundReason,amount,transactionId,operatorId);
        if(refundPrimaryKey == null){
            return new Result(YqxRefundReturnType.DBERROR);
        }
        
        //发送给支付平台得到返回结果,根据refundPrimaryKey更新YqxRefundRecord表
        LOGGER.info("退款id:[{}],发送:Url[{}]",id,url);
        String httpResult = HttpUtil.doGet(url, "", "utf-8", false);
        LOGGER.info("退款id:[{}],收到结果:[{}]",id,httpResult);   
        if(StringUtils.isBlank(httpResult)){
            updateYqxRefundRecord(refundPrimaryKey, YqxRefundStatus.REFUSED.getStatus(), YqxRefundReturnType.HTTPERROR.getDesc());
            return new Result(YqxRefundReturnType.HTTPERROR);          
        }else{
            //解析返回xml
            Result result = analyseRefundXml(httpResult);
            if(YqxRefundReturnType.ACCEPTED.getCode().equals(result.getCode())){
                updateYqxRefundRecord(refundPrimaryKey, YqxRefundStatus.ACCETED.getStatus(), result.getMsg());    
            }else{
                updateYqxRefundRecord(refundPrimaryKey, YqxRefundStatus.REFUSED.getStatus(), result.getMsg());
            }
            return result;
        }    
    }
    
    /**
     * 云企信退款同步全流程,包括插入和更新数据库
     */
    @Override
    public Result yqxRefundProcess(String doneCode, String refundReason,
            String amount, RefundType refundType, Long operatorId) {
        //通过doneCode在YqxPayRecord表寻找记录
        YqxPayRecord record = yqxPayRecordService.selectByDoneCode(doneCode);
        
        if(record == null){
            LOGGER.error("云企新支付记录不存在或流水号不存在，doneCode={}",doneCode);
            return new Result(YqxRefundReturnType.NOTEXIST);
        }

        //根据payRecord找到强关联的OrderRecord
        YqxOrderRecord orderRecord = yqxOrderRecordService.selectBySerialNum(record.getOrderSerialNum());
        if(orderRecord == null || !record.getPayTransactionId().equals(orderRecord.getPayTransactionId())){
            LOGGER.info("当前支付记录没有对应订购记录（可能是订购记录对应的支付并不是该条支付记录）,支付记录doneCode={},订购记录SerialNum={}", doneCode, record.getOrderSerialNum());
            LOGGER.info("退款记录不需要更新到订购记录中");
            orderRecord = null;
        }else{//找到订购记录，把订购记录的退款状态设置成退款中
            LOGGER.info("后台管理员发起退款流程，订购记录原退款状态{}，doneCode={}，orderSerialNum={}", orderRecord.getRefundStatus(), doneCode, orderRecord.getSerialNum());
            orderRecord.setRefundStatus(1);//退款受理中
            if(!yqxOrderRecordService.updateByPrimaryKey(orderRecord)){
                LOGGER.error("更新订购记录到退款已提交请求状态失败，orderSerialNum={}", orderRecord.getSerialNum());
                return new Result(YqxRefundReturnType.DBERROR);
            }
        }
        return doYqxRefundProcess(doneCode, refundReason,
                amount, refundType, operatorId, orderRecord);
    }
    
    /**
     * yqx退款全流程
     */
    private Result doYqxRefundProcess(String doneCode, String refundReason,
            String amount, RefundType refundType, Long operatorId, YqxOrderRecord orderRecord){
        //生成发送的url
        String transactionId = yqxPayRecordService.getNewTransactionId();
        String url = generateYqxRefundXml(doneCode,refundReason,amount,refundType,transactionId);
        if(StringUtils.isBlank(url)){
            LOGGER.error("生成退款地址失败");
            return new Result(YqxRefundReturnType.GENEURLFAILED);
        }
               
        //插入到YqxRefundRecord表，成功返回key,失败返回null
        Long refundPrimaryKey  = insertYqxRefundRecord(doneCode,refundReason,amount,transactionId, operatorId);
        if(refundPrimaryKey == null){
            return new Result(YqxRefundReturnType.DBERROR);
        }
        
        //更新退款状态为处理中
        updateOrderRecord(orderRecord, 2);
        
        //发送给支付平台得到返回结果,根据refundPrimaryKey更新YqxRefundRecord表
        LOGGER.info("退款doneCode:[{}],发送:Url[{}]",doneCode,url);
        String httpResult = HttpUtil.doGet(url, "", "utf-8", false);
        LOGGER.info("退款doneCode:[{}],收到结果:[{}]",doneCode,httpResult);   
        if(StringUtils.isBlank(httpResult)){
            updateYqxRefundRecord(refundPrimaryKey, YqxRefundStatus.REFUSED.getStatus(), YqxRefundReturnType.HTTPERROR.getDesc());            
            //更新orderRecord退款记录为退款失败
            updateOrderRecord(orderRecord, 4);
            
            return new Result(YqxRefundReturnType.HTTPERROR);          
        }else{
            //解析返回xml
            Result result = analyseRefundXml(httpResult);
            if(YqxRefundReturnType.ACCEPTED.getCode().equals(result.getCode())){
                updateYqxRefundRecord(refundPrimaryKey, YqxRefundStatus.ACCETED.getStatus(), result.getMsg());
            }else{
                updateYqxRefundRecord(refundPrimaryKey, YqxRefundStatus.REFUSED.getStatus(), result.getMsg());
                //更新orderRecord退款记录为退款失败
                updateOrderRecord(orderRecord, 4);
            }
            return result;
        }    
    }
    

    /** 
     * 更新订购记录的退款状态
     * @Title: updateOrderRecord 
     */
    private void updateOrderRecord(YqxOrderRecord orderRecord, Integer refundStatus){
        //0-未申请退款；1-退款受理中；2-退款处理中（已发送退款申请）；3-退款成功；4-退款失败
        if(orderRecord != null){
            orderRecord.setRefundStatus(refundStatus);//退款失败
            if(!yqxOrderRecordService.updateByPrimaryKey(orderRecord)){
                LOGGER.error("更新订购记录退款状态失败，orderSerialNum={}，原refundStatus={}，待更新的refundStatus={}", 
                        orderRecord.getSerialNum(), orderRecord.getRefundStatus(), refundStatus);
            }
        }
    }

    
    /**
     * 发送到支付平台前，插入到YqxRefundRecord表
     * 成功返回主键，失败返回null
     * 
     */
    private Long insertYqxRefundRecord(String doneCode,String reason,String refundAmount,
            String serialNum, Long operatorId){
        YqxRefundRecord record = new YqxRefundRecord();
        record.setDoneCode(doneCode);
        record.setReason(reason);
        
        Double amount = NumberUtils.toDouble(refundAmount);
        if(amount != 0.0d){
            record.setRefundAmount(amount);
        }
        
        record.setRefundType(NumberUtils.toInt(RefundType.BUSINESS.getCode()));
        record.setRefundSerialNum(serialNum);
        record.setStatus(0);
        record.setMsg("待处理");
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        Administer admin = administerService.selectAdministerById(operatorId);
        record.setOperatorId(operatorId);
        record.setOperatorMobile(admin.getMobilePhone());
        record.setOperatorName(admin.getUserName());
        return yqxRefundRecordService.insert(record)?record.getId():null;           
    }
    
    /**
     * 根据id,更改YqxRefundRecord表status和msg
     */
    private void updateYqxRefundRecord(Long id,int status,String msg){
        YqxRefundRecord record = new YqxRefundRecord();
        record.setId(id);
        record.setStatus(status);
        record.setMsg(msg);
        record.setUpdateTime(new Date());
        System.out.println(yqxRefundRecordService.updateByPrimaryKeySelective(record));
    }
    
    /**
     * 分析退款返回的xml类型
     */
    private Result analyseRefundXml(String xml){
        /*xml ="<AdvPay><PubInfo/><BusiData><ReturnCode>202</ReturnCode>"
                + "<ReturnMsg>退款已受理</ReturnMsg><OrderId>OP2017051713562021623003</OrderId>"
                + "</BusiData></AdvPay>";*/
        
        //解析返回xml
        XStream xStream = new XStream();
        xStream.alias("AdvPay",RefundResponse.class);
        xStream.autodetectAnnotations(true);
        try { 
            RefundResponse response = (RefundResponse)xStream.fromXML(xml);
            RefundAsyncResponse body =  response.getBody();
            if(body == null || StringUtils.isBlank(body.getReturnCode())){
                return new Result(YqxRefundReturnType.ANALYSEFAILED);
            }
            
            return new Result(body.getReturnCode(),body.getReturnMsg()); 
           
                        
        }catch(Exception e){
            return new Result(YqxRefundReturnType.ANALYSEFAILED);
        }
    }
    
    
    /**
     * 退款接口收到xml，yqx相关逻辑处理
     * 1.分析xml
     * 2.找到并更新数据库
     * ps：同一个doneCode流水号的记录，只会处理一次成功
     * 
     */
    @Override
    public boolean yqxRefundAsyncXml(String xml) {
        if(StringUtils.isBlank(xml)){
            return false;
        }
        
        //1.分析xml
        XStream xStream = new XStream();
        xStream.alias("AdvPay",RefundResponse.class);
        xStream.autodetectAnnotations(true);
        RefundResponse response = null;
        RefundAsyncResponse body = null;
        try { 
            response = (RefundResponse)xStream.fromXML(xml);
            body =  response.getBody();
            if(body == null || StringUtils.isBlank(body.getReturnCode()) ||
                    StringUtils.isBlank(body.getOrderId()) ||
                    StringUtils.isBlank(body.getReturnMsg())){
                LOGGER.error("退款xml分析失败，xml="+xml);
                return false;
            }          
                        
        }catch(Exception e){
            LOGGER.error("退款xml分析失败，xml="+xml);
            return false;
        }
        
        String doneCode = body.getOrderId();//支付流水号
        int status = YqxRefundStatus.FAILED.getStatus();
        int refundStatus = 4;//订购记录中的退款状态，默认为失败
        if(YqxRefundReturnType.SUCCESS.getCode().equals(body.getReturnCode())){
            status = YqxRefundStatus.SUCCESS.getStatus();
            refundStatus = 3;
        }
        
        if(yqxRefundRecordService.
            updateByDoneCodeAcceptedRecord(doneCode, status, body.getReturnMsg())){
            LOGGER.info("退款处理结果成功,doneCode={},returnCode={},returnMsg={}",doneCode,
                    body.getReturnCode(),body.getReturnMsg());
            
            //将退款处理结果更新到orderRecord的退款状态中
            //通过doneCode在YqxPayRecord表寻找记录
            YqxPayRecord record = yqxPayRecordService.selectByDoneCode(doneCode);            
            if(record == null){
                LOGGER.error("云企新支付记录不存在或流水号不存在，doneCode={}",doneCode);
            }else{
                YqxOrderRecord orderRecord = yqxOrderRecordService.selectBySerialNum(record.getOrderSerialNum());
                if(orderRecord == null || !record.getPayTransactionId().equals(orderRecord.getPayTransactionId())){
                    LOGGER.info("当前支付记录没有对应订购记录（可能是订购记录对应的支付并不是该条支付记录）,支付记录doneCode={},订购记录SerialNum={}", doneCode, record.getOrderSerialNum());
                }else{//找到订购记录，把订购记录的退款状态设置成退款中
                    LOGGER.info("订购记录原退款状态{}，现要更新的退款状态{}，doneCode={}，orderSerialNum={}", orderRecord.getRefundStatus(), 
                            refundStatus, doneCode, orderRecord.getSerialNum());
                    updateOrderRecord(orderRecord, refundStatus);
                }
                
            }
                        
            return true;
        }else{
            LOGGER.error("退款处理结果失败,doneCode={},returnCode={},returnMsg={}",doneCode,
                    body.getReturnCode(),body.getReturnMsg());
            return false;
        }
        
    }
       

    public String getOriginId() {
        return globalConfigService.get(GlobalConfigKeyEnum.PAY_ORIGINID.getKey());
    }

    public String getAccountCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.PAY_ACCOUNTCODE.getKey());
    }

    public String getMerchantId() {
        return globalConfigService.get(GlobalConfigKeyEnum.PAY_MERCHANTID.getKey());
    }

    public String getProductId() {
        return globalConfigService.get(GlobalConfigKeyEnum.PAY_PRODUCTID.getKey());
    }

    public String getPayNotifyIntURL() {
        return globalConfigService.get(GlobalConfigKeyEnum.PAY_PAYNOTIFYINTURL.getKey());
    }

    public String getPayNotifyPageURL() {
        return globalConfigService.get(GlobalConfigKeyEnum.PAY_PAYNOTIFYPAGEURL.getKey());
    }

    public String getPayPeriod() {
        return globalConfigService.get(GlobalConfigKeyEnum.PAY_PAYPERIOD.getKey());
    }

    public String getEncodeSecret() {
        return globalConfigService.get(GlobalConfigKeyEnum.PAY_ENCODESECRET.getKey());
    }

    public String getPayPlatformUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.PAY_PAYPLATFORMURL.getKey());
    }

    public String getRefundRequestUrl(){
        //return "https://paypre.4ggogo.com/admin/api/refundService.html?xml=";
        return globalConfigService.get(GlobalConfigKeyEnum.PAY_FORMREFUNDURL.getKey());
    }

    public String getRefundCallbackUrl(){
        //return "http://172.23.27.212:8080/web-in/manage/payplatform/refundAsync.html";
        return globalConfigService.get(GlobalConfigKeyEnum.PAY_FORMREFUNDCALLBACKURL.getKey());
    }
    
}
