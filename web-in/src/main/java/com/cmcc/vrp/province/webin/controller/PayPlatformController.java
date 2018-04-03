package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.service.ScMemberInquiryService;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.YqxOrderTradeStatus;
import com.cmcc.vrp.pay.enums.PayMethodType;
import com.cmcc.vrp.pay.enums.PayReturnType;
import com.cmcc.vrp.pay.enums.RefundType;
import com.cmcc.vrp.pay.enums.YqxRefundReturnType;
import com.cmcc.vrp.pay.model.Result;
import com.cmcc.vrp.pay.service.PayPlatformService;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.YqxOrderRecord;
import com.cmcc.vrp.province.model.YqxPayRecord;
import com.cmcc.vrp.province.model.YqxRefundRecord;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.YqxOrderRecordService;
import com.cmcc.vrp.province.service.YqxPayRecordService;
import com.cmcc.vrp.province.service.YqxRefundRecordService;
import com.cmcc.vrp.province.service.YqxVpmnDiscountService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.YqxChargePojo;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;

/**
 * 支付相关的所有controller
 *
 */
@Controller
@RequestMapping("/manage/payplatform")
public class PayPlatformController extends BaseController {
    
    private Logger logger = LoggerFactory.getLogger(PayPlatformController.class);
    
    @Autowired
    PayPlatformService payPlatformService;
    
    @Autowired
    YqxPayRecordService yqxPayRecordService;
    
    @Autowired
    YqxOrderRecordService yqxOrderRecordService;
    
    @Autowired
    private TaskProducer taskProducer;
    
    @Autowired
    YqxRefundRecordService yqxRefundRecordService;
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    @Autowired
    IndividualProductService individualProductService;
    
    @Autowired
    ScMemberInquiryService scMemberInquiryService;
    
    @Autowired
    YqxVpmnDiscountService yqxVpmnDiscountService;
    
    /**
     * 支付地址页
     */
    @RequestMapping(value = "pay", method = RequestMethod.GET)
    public String pay(HttpServletRequest req,HttpServletResponse res,ModelMap modelMap,String orderSerialNum, Integer type, String payOrderId) throws IOException{

        YqxOrderRecord record = yqxOrderRecordService.selectBySerialNum(orderSerialNum);
        
        if(StringUtils.isBlank(orderSerialNum) || record == null || 
                record.getPayPrice() == null || record.getPayPrice()<=0L || type ==null
                || StringUtils.isEmpty(record.getMobile())){
            logger.info("云企信订购记录中有异常信息，orderSerialNum={}", orderSerialNum);
            return "yunqixin/failure.ftl";//订单确认受理失败页
        }
        

        //在支付时检查金额，wujiamin，20170704
        IndividualProduct product = individualProductService.selectByPrimaryId(record.getIndividualProductId());
        String cqOriginId = globalConfigService.get(GlobalConfigKeyEnum.YQX_CQ_ORIGIN_ID.getKey());
        String scOriginId = globalConfigService.get(GlobalConfigKeyEnum.YQX_SC_ORIGIN_ID.getKey());
        if(!StringUtils.isEmpty(cqOriginId) && cqOriginId.equals(getYqxAppTag())){//重庆云企信，无折扣
            if(!record.getPayPrice().equals(product.getPrice().longValue())){
                logger.info("云企信订购记录中的价格{}和数据库计算获得的价格{}不一致，异常订单无法支付，orderSerialNum={}", 
                        record.getPayPrice(), product.getPrice(), orderSerialNum);
                return "yunqixin/failure.ftl";//订单确认受理失败页
            }
        }else if(!StringUtils.isEmpty(scOriginId) && scOriginId.equals(getYqxAppTag())){//四川云企信，折扣要从V网网龄获取
            
            Integer date = scMemberInquiryService.getdayRange(record.getMobile());
            if(date != null){
                Integer factor = yqxVpmnDiscountService.getDiscountByDate(date);
                Long price = new BigDecimal(factor * product.getPrice()).divide(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
                if(!record.getPayPrice().equals(price)){
                    logger.info("云企信订购记录中的价格{}和数据库计算获得的价格factor{}*productPrice{}={}不一致，异常订单无法支付，orderSerialNum={}", 
                            record.getPayPrice(), factor, product.getPrice(), price, orderSerialNum);
                    return "yunqixin/failure.ftl";//订单确认受理失败页
                }
                
            }else{
                logger.info("四川云企信获取V网网龄失败，无法进行支付");
                return "yunqixin/failure.ftl";//订单确认受理失败页
            }

        }else{
            logger.info("session中的应用标识不存在或标识异常", getYqxAppTag());
            return "yunqixin/failure.ftl";//订单确认受理失败页
        }


        //产生YqxPayRecord记录插入数据库和生成支付url
        YqxPayRecord newRecord = new YqxPayRecord();      
        newRecord.setOrderSerialNum(orderSerialNum);//前端传递的流水号
        newRecord.setPayTransactionId(yqxPayRecordService.getNewTransactionId());//支付交易流水号
        newRecord.setPayOrderId(payOrderId);    //128位流水号由业务层传递
        newRecord.setCreateTime(new Date());
        newRecord.setUpdateTime(new Date());
        newRecord.setStatus(Integer.valueOf(PayReturnType.WAITRETURN.getCode())); //3.等待支付平台返回
        newRecord.setPayType(type);
        
        //映射支付类型
        PayMethodType payType = null;
        if(newRecord.getPayType().equals(1)){
            payType = PayMethodType.WECHAT;
        }else if(newRecord.getPayType().equals(2)){
            payType = PayMethodType.ALIPAY;
        }else{
            return "yunqixin/failure.ftl";//订单确认受理失败页
        }

        String payUrl = payPlatformService.generatePayUrl(newRecord.getPayTransactionId(), 
                newRecord.getPayOrderId(), formateMoney(record.getPayPrice()), payType, "支付");
        
        
        if(StringUtils.isBlank(payUrl) || !yqxPayRecordService.insert(newRecord)){
            return "yunqixin/failure.ftl";//订单确认受理失败页
        }
        
        logger.info("生成支付orderSerialNum={},url={}",orderSerialNum,payUrl);
        modelMap.addAttribute("redirectUrl", payUrl);
            
        return "pay/payFirst.ftl"; //这张页面的效果是直接跳转到支付平台
    }
    
    /**
     * 除以100，保留两位小数
     */
    private String formateMoney(Long payPrice){
        double d = payPrice.doubleValue();       
        return String.format("%.2f", d/100);
    }
    
    
    /**
     * 支付同步返回页
     */
    @RequestMapping(value = "payCallbackSync")
    public String payCallbackSync(HttpServletRequest req,HttpServletResponse res) throws IOException{

        String xml = req.getParameter("xml");     
        String decodeXml = StringEscapeUtils.unescapeHtml(xml);
        logger.error("从支付平台同步支付接口收到信息:" + decodeXml);


        
        //跳转到相应的支付结果页面
        YqxPayRecord result = payPlatformService.analyseCallBackXml(decodeXml);
        if(PayReturnType.UNKNOWN.getCode().equals(result.getStatus().toString())){
            logger.error("解析支付平台信息错误.xml=" + decodeXml);
            return "redirect:/yqx/order/payError.html";
        }else if (PayReturnType.SUCCESS.getCode().equals(result.getStatus().toString())){
            return "redirect:/yqx/order/paySuccess.html" ;
     
        }else if (PayReturnType.FAILD.getCode().equals(result.getStatus().toString())){
            return "redirect:/yqx/order/payFailure.html";
            
        }else{ //等待支付
            return "redirect:/yqx/order/payError.html";
        }
        
    }
    
   
    
    /**
     * 支付异步返回页
     */
    @RequestMapping(value = "payCallbackAsync")
    public void payCallbackAsync(HttpServletRequest req,HttpServletResponse res) throws IOException{
        //String xml = StreamUtils.copyToString(req.getInputStream(), Charsets.UTF_8); 
        String xml = req.getParameter("xml");     
        String decodeXml = StringEscapeUtils.unescapeHtml(xml);
        logger.error("从支付平台异步支付接口收到信息:" + decodeXml);
                
        //跳转到相应的支付结果页面
        YqxPayRecord result = payPlatformService.analyseCallBackXml(decodeXml);
        if(PayReturnType.UNKNOWN.getCode().equals(result.getStatus().toString())){
            logger.error("解析支付平台信息错误.xml=" + decodeXml);
        }else{
           //更新YqxPayRecord
            YqxPayRecord dataBaseRecord = yqxPayRecordService.
                    selectByPayIds(result.getPayOrderId(), result.getPayTransactionId());
            if(dataBaseRecord !=null){
                dataBaseRecord.setStatus(result.getStatus());
                dataBaseRecord.setResultReturnTime(new Date());
                dataBaseRecord.setUpdateTime(new Date());
                dataBaseRecord.setDoneCode(result.getDoneCode());
                yqxPayRecordService.updateByPrimaryKeySelective(dataBaseRecord);
            }else{
                logger.error("从数据库没有找到相关记录YqxPayRecord，tansactionId={},orderId={}" , result.getPayTransactionId(),result.getPayOrderId());
                res.getWriter().println("SUCCESS");
                return;
            }
            
            //更新YqxOrderRecord
            YqxOrderRecord orderRecord = yqxOrderRecordService.selectBySerialNum(dataBaseRecord.getOrderSerialNum());
            if(orderRecord.getPayTransactionId() == null){
                orderRecord.setPayTransactionId(dataBaseRecord.getPayTransactionId());
                if (PayReturnType.SUCCESS.getCode().equals(result.getStatus().toString())){
                    orderRecord.setPayStatus(2);
                    orderRecord.setTradeStatus(YqxOrderTradeStatus.SUCCESS.getCode());//支付成功，交易状态改为交易成功 
                    orderRecord.setUpdateTime(new Date());                
                    if(!yqxOrderRecordService.updateByPrimaryKey(orderRecord)){
                        logger.error("更新yqxOrderRecord失败，serialNum={}" , orderRecord.getSerialNum());
                    }
                    //更新支付记录为待充值
                    if(yqxPayRecordService.updateChargeStatus(ChargeRecordStatus.WAIT.getCode(), dataBaseRecord.getPayTransactionId(), null)){
                        //加入充值队列
                        if(!taskProducer.produceYqxChargeMsg(new YqxChargePojo(result.getPayOrderId(), result.getPayTransactionId()))){
                            logger.error("加入云企信充值队列失败，orderId={}, payTransactionId={}" , result.getPayOrderId(), result.getPayTransactionId());
                        }
                    }else{
                        logger.error("云企信支付记录更新充值状态为待充值时失败，payTransactionId={}", dataBaseRecord.getPayTransactionId());
                    }     
                }else if (PayReturnType.FAILD.getCode().equals(result.getStatus().toString())){
                    orderRecord.setPayStatus(3);
                    orderRecord.setTradeStatus(YqxOrderTradeStatus.FAIL.getCode());//支付失败，交易状态改为交易失败
                    orderRecord.setUpdateTime(new Date());
                    if(!yqxOrderRecordService.updateByPrimaryKey(orderRecord)){
                        logger.error("更新yqxOrderRecord失败，serialNum={}" , orderRecord.getSerialNum());
                    }
                    
                }else{ //等待支付状态不用更新
                    res.getWriter().println("SUCCESS");
                    logger.error("从数据库没有找到相关记录YqxOrderRecord，serialNum={}" , dataBaseRecord.getOrderSerialNum());
                    return;
                }
            }else{
                logger.info("云企信orderRecord已对应一条支付记录payTransactionId={}，不再更新订购记录中交易状态{}和支付状态{}",
                        dataBaseRecord.getPayTransactionId(), orderRecord.getTradeStatus(), orderRecord.getPayStatus());
            }
                  
            logger.error("从支付平台异步接口收到回调，更新完成，tansactionId={},orderId={}" , result.getPayTransactionId(),result.getPayOrderId());      
        }
        
        //返回支付平台SUCCESS
        res.getWriter().println("SUCCESS");
    }

    /**
     *  同步访问
     */
    /*@RequestMapping(value = "refundSync")
    public void refundSync(HttpServletRequest req,HttpServletResponse res) throws IOException{
        String id = req.getParameter("id");     
        String amount = req.getParameter("amount");
        payPlatformService.yqxRefundProcess(NumberUtils.toLong(id), "test", amount, RefundType.BUSINESS);
    }*/
    
    /**
     *  同步访问页面
     */
    @RequestMapping(value = "yqxRefundPage")
    public String refundPage(HttpServletRequest req,HttpServletResponse res) throws IOException{
        return "pay/refund.ftl"; //这张页面的效果是直接跳转到支付平台
    }
    
    /**
     * 获取用户列表
     */
    @RequestMapping("/yqxRefundIndex")
    public String yqxRefundIndex(ModelMap modelMap, QueryObject queryObject) {
        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }
        
        return "pay/refundIndex.ftl";
    }
    
    /**
     * 获取用户列表
     */
    @RequestMapping(value = "/yqxRefundIndexSearch")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        /**
         * 查询参数: 流水号
         */
        setQueryParameter("doneCode", queryObject);
        
        
        int count = yqxRefundRecordService.queryPaginationRefundCount(queryObject);
        List<YqxRefundRecord> list = yqxRefundRecordService.queryPaginationRefundList(queryObject);
        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());        
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     *  同步访问页面ajax
     */
    @RequestMapping(value = "yqxRefundPageAjax")
    public void refundPageAjax(HttpServletRequest req,HttpServletResponse resp,
            String doneCode,String reason) throws IOException{

        Map<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isBlank(reason) || StringUtils.isBlank(doneCode)){
            map.put("result", "failed");
            map.put("msg", "前端参数错误");
            resp.getWriter().write(JSON.toJSONString(map));
            return;
        }
        
        Result result = payPlatformService.yqxRefundProcess(doneCode, reason, null, RefundType.BUSINESS, getCurrentAdminID());
        if(YqxRefundReturnType.ACCEPTED.getCode().equals(result.getCode())){
            map.put("result", "success");
            map.put("msg", result.getMsg());
        }else{
            map.put("result", "failed");
            map.put("msg", result.getMsg());
        }
        resp.getWriter().write(JSON.toJSONString(map));
        return;
    }
    
    /**
     * 异步访问
     */
    @RequestMapping(value = "refundAsync")
    public void refundAsync(HttpServletRequest req,HttpServletResponse res) throws IOException{
        String xml = StreamUtils.copyToString(req.getInputStream(), Charsets.UTF_8); 

        /* String xml = "<AdvPay><PubInfo><Version>1.00</Version><TransactionId>20170516145955000000000004</TransactionId>"
                + "<OriginId>3003</OriginId>"
                + "<VerifyCode>87247ff60daf92a3cb8c8a8443e8eb19</VerifyCode><DigestAlg>MD5</DigestAlg>"
                + "</PubInfo><BusiData><ReturnCode>200</ReturnCode><ReturnMsg>退款成功</ReturnMsg>"
                + "<OrderId>OP2017051714431222023003</OrderId></BusiData></AdvPay>";*/
        
        String decodeXml = StringEscapeUtils.unescapeHtml(xml);
        logger.error("从支付平台异步退款接口收到信息:" + decodeXml);
        if(StringUtils.isNotBlank(decodeXml) && payPlatformService.yqxRefundAsyncXml(decodeXml)){
            res.getWriter().println("SUCCESS");
        }
    }
}
