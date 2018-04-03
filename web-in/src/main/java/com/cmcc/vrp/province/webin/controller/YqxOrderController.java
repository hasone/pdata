package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.service.ScMemberInquiryService;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.YqxOrderPayStatus;
import com.cmcc.vrp.enums.YqxOrderTradeStatus;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.model.YqxOrderRecord;
import com.cmcc.vrp.province.model.YqxPayRecord;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.province.service.YqxOrderRecordService;
import com.cmcc.vrp.province.service.YqxPayRecordService;
import com.cmcc.vrp.province.service.YqxVpmnDiscountService;
import com.cmcc.vrp.util.AESdecry;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;


/**
 * YqxOrderController.java
 * @author wujiamin
 * @date 2017年5月5日
 */
@Controller
@RequestMapping("/yqx/order")
public class YqxOrderController extends BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(YqxOrderController.class); 
    @Autowired
    YqxOrderRecordService yqxOrderRecordService;
    @Autowired
    IndividualProductService individualProductService;
    @Autowired
    ScMemberInquiryService scMemberInquiryService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    YqxVpmnDiscountService yqxVpmnDiscountService;
    @Autowired
    YqxPayRecordService yqxPayRecordService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    AccountService accountService;
    @Autowired
    PhoneRegionService phoneRegionService;
    
    /**
     * VALID_ORIGINALID
     */
    private static final String VALID_ORIGINALID_SC = "zyscyqx";
    private static final String VALID_ORIGINALID_CQ = "zycqyqx";
    
    /** 
     * 订购首页
     * @Title: orderIndex 
     */
    @RequestMapping("index")
    public String orderIndex(ModelMap map){

        LOGGER.info("entry orderIndex");        
        
        String currentMobile = getYqxMobile();
        String currentOriginalId = getYqxAppTag();
        if(!StringUtils.isValidMobile(currentMobile) || StringUtils.isEmpty(currentOriginalId)){
            LOGGER.error("当前session中没有有效的云企信手机号和应用标识");
            return "yunqixin/404.ftl";
        }
        
        if(!getVpmnFlag()){//四川不需要，重庆需要
            //重庆增加虚拟订购数量
            Long cqNum = yqxOrderRecordService.getVirtualCqNum();
            map.put("cqNum", cqNum);
        }
        
        //获取云企信的产品（获取的产品中带有是否可用的标识）
        List<IndividualProduct> products = individualProductService.getYqxProduct(getYqxAppTag(), getCanOrderFlag());
        map.put("products", products);
        
        map.put("vpmn", getVpmnFlag());//是否需要显示V网网龄的标识
        //是否需要检测重庆手机号标识,只有YQX_CQ_PHONECHECK为ture且getYqxAppTag()为zycqyqx才校验
        map.put("checkPhoneCq", getCqCheckPhoneFlag());
        map.put("yqxOrderMobile", currentMobile);
        
        //客服电话
        map.put("text", globalConfigService.get(GlobalConfigKeyEnum.YQX_ORDER_PAGE_TEXT.getKey()));
        
        map.put("duringAccountCheckDate", yqxOrderRecordService.duringAccountCheckDate());//是否是对账日
        
        return "yunqixin/orderList.ftl";
    }
    
    /** 
     * 获取是否可以选中产品，true可点，false不可点
     * @Title: getCanOrderFlag 
     */
    private Boolean getCanOrderFlag(){
        String enterpriseCode = globalConfigService.get(GlobalConfigKeyEnum.YQX_ORDERID_ENTER_CODE.getKey());
        Enterprise enterprise = enterprisesService.selectByCode(enterpriseCode);
        if(enterprise == null || !new Integer(1).equals(enterprise.getInterfaceFlag()) ||
                !new Integer(0).equals(enterprise.getDeleteFlag()) ){
            LOGGER.error("企业状态非法，订购按钮不可点，code={}", enterpriseCode);
            return false;
        }
        
        //重庆云企信标识
        String cqOriginId = globalConfigService.get(GlobalConfigKeyEnum.YQX_CQ_ORIGIN_ID.getKey());
        //重庆云企信订购时不需要判断企业现金余额
        if(!getYqxAppTag().equals(cqOriginId)){
            //余额判断>10000元
            Account currencyAccount = accountService.getCurrencyAccount(enterprise.getId());
            if(currencyAccount == null || currencyAccount.getCount()<1000000.00){
                LOGGER.error("企业余额不足，订购按钮不可点，code={}", enterpriseCode);
                return false;
            }
        }
        
        return true;
    }

    /** 
     * @Title: getVpmnYear
     * 获取V网网龄
     */
    @RequestMapping("getVpmnYear")
    public void getVpmnYear(HttpServletResponse res){

        String mobile = getYqxMobile();
        if(!StringUtils.isValidMobile(mobile)){
            return;
        }
        JSONObject resultMap = new JSONObject();
                 
        resultMap.put("haveVpmnYear", false);
        if(getVpmnFlag()){                                
            Map vpmnInfoMap = scMemberInquiryService.getVpmnYearInfo(mobile);
            
            //TODO: for test
//            vpmnInfoMap = new HashMap();
//            vpmnInfoMap.put("year", 1);//到当前的入网年
//            vpmnInfoMap.put("month", 2);//到当前的入网月
//            vpmnInfoMap.put("day", 9);//到当前的入网日
//            vpmnInfoMap.put("vpmnYearStr", "20160307111210");//入网时间的字符串
//            vpmnInfoMap.put("date", 427);//到当前的入网天数    
            
            if(vpmnInfoMap != null){

                String vpmnYearStr = (String) vpmnInfoMap.get("vpmnYearStr");
                int year = (Integer) vpmnInfoMap.get("year");
                int month = (Integer) vpmnInfoMap.get("month");
                int day = (Integer) vpmnInfoMap.get("day");
                int date = (Integer) vpmnInfoMap.get("date");
                LOGGER.info("V网网龄，mobile={}，入网：{}年，{}月，{}日", mobile, year, month, day);
                //V网网龄
                //网龄转换成几天几月几日
                resultMap.put("vpmn", "四川移动V网网龄：" + year + "年" + month + "月" + day + "天");
                //获得折扣
                Integer factor = yqxVpmnDiscountService.getDiscountByDate(date);
                resultMap.put("factor", factor/100.0);
                //获得网龄转换成
                resultMap.put("haveVpmnYear", true);
                resultMap.put("vpmnDate", vpmnYearStr);                
            }else{
                LOGGER.info("V网网龄，mobile={}，无法获得网龄信息", mobile);
                resultMap.put("vpmn", "无法获得网龄信息");
            }
        }
        try {
            res.setCharacterEncoding("UTF-8"); 
            res.getWriter().write(resultMap.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /** 
     * 获取是否是V网标识，是-true，否-false，根据云企信专递的应用标识区分
     * @Title: getVpmnFlag 
     */
    private boolean getVpmnFlag(){  
        String originId = globalConfigService.get(GlobalConfigKeyEnum.YQX_SC_ORIGIN_ID.getKey());
        if(StringUtils.isEmpty(originId)){
            return false;
        }
        return originId.equals(getYqxAppTag());
    }
    
    
    /** 
     * @Title: checkPhoneRegionCq
     * 是否为重庆移动手机号
     */
    @RequestMapping("checkPhoneRegionCq")
    public void checkPhoneRegionCq(HttpServletResponse res){

        String mobile = getYqxMobile();
        if(!StringUtils.isValidMobile(mobile)){
            return;
        }
        
        JSONObject resultMap = new JSONObject();
        resultMap.put("phoneCqMobile", false);
        if(getCqCheckPhoneFlag()){
            PhoneRegion phoneRegion = phoneRegionService.query(mobile);
            if(phoneRegion !=null && "重庆".equals(phoneRegion.getProvince()) && 
                    "M".equals(phoneRegion.getSupplier())){
                resultMap.put("phoneCqMobile", true);
            }
        }
                
        try {
            res.setCharacterEncoding("UTF-8"); 
            res.getWriter().write(resultMap.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /** 
     * 获取是否是重庆检验手机号标识
     * 只有YQX_CQ_PHONECHECK为ture且getYqxAppTag()为zycqyqx才校验
     * @Title: getVpmnFlag 
     */
    private boolean getCqCheckPhoneFlag(){  
        String originId = globalConfigService.get(GlobalConfigKeyEnum.YQX_CQ_ORIGIN_ID.getKey());
        if(StringUtils.isEmpty(originId)){
            return false;
        }
        if(!"true".equals(globalConfigService.get(GlobalConfigKeyEnum.YQX_CQ_PHONECHECK.getKey()))){
            return false;
        }
        
        return originId.equals(getYqxAppTag());
    }
    
    /** 
     * 我的订购
     * @Title: queryRecord 
     */
    @RequestMapping("queryRecord")
    public void queryRecord(QueryObject queryObject, HttpServletRequest request, int pageSize, int pageNo, HttpServletResponse response){
        String mobile = getYqxMobile();
        List<YqxOrderRecord> records = new ArrayList<YqxOrderRecord>();
        if(StringUtils.isValidMobile(mobile)){
            if (queryObject == null) {
                queryObject = new QueryObject();
            }
           
            /**
             * 分页转换
             */
            queryObject.setPageNum(pageNo);
            queryObject.setPageSize(pageSize);
            
            queryObject.getQueryCriterias().put("mobile", mobile);
            records = yqxOrderRecordService.selectByMap(queryObject.toMap());
        }

        try {
            Map result = new HashMap();
            result.put("data", records);
            response.getWriter().write(JSONObject.toJSONString(result));            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /** 
     * 跳转到订购说明页
     * @Title: introduction 
     */
    @RequestMapping("introduction")
    public String introduction(ModelMap model){
        String startTime = globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_START_TIME.getKey());
        String startDay = globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_START_DAY.getKey());
        String endTime = globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_END_TIME.getKey());
        String endDay = globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_END_DAY.getKey());
        model.put("startTime", startTime);
        model.put("startDay", startDay);
        model.put("endTime", endTime);
        model.put("endDay", endDay);
        
        if(getVpmnFlag()){
            return "yunqixin/introduction_sc.ftl";
        }else {
            return "yunqixin/introduction_cq.ftl";
        }
    }
    
    /** 
     * 提交订购
     * @Title: submitOrder 
     */
    @RequestMapping("submitOrder")
    public void submitOrder(HttpServletResponse res, String productCode, Double factor, Double price, String vpmnDate){
        Map result = new HashMap();
        result.put("overnum", false);//是否超过限额
        result.put("success", false);
        
        String mobile = getYqxMobile();
        if(StringUtils.isValidMobile(mobile)){
            /*if(getVpmnFlag()){//四川V网用户
                //检查网龄是否一致（防止页面篡改）
                Map vpmnInfoMap = scMemberInquiryService.getVpmnYearInfo(mobile);
                if(vpmnInfoMap == null || !vpmnDate.equals((String) vpmnInfoMap.get("vpmnYearStr"))){
                    LOGGER.info("查出的V网网龄和页面传递的V网网龄不一致");
                    try {            
                        res.getWriter().write(JSONObject.toJSONString(result));            
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }*/

            //开始订购
            //检查是否超过限额
            if(yqxOrderRecordService.ifOverNum(mobile)){
                LOGGER.info("超过每月订购限额");
                result.put("overnum", true);
            }else{
                YqxOrderRecord yqxOrderRecord = createYqxOrderRecord(productCode, factor, price, vpmnDate);
                if(yqxOrderRecord != null && yqxOrderRecordService.create(yqxOrderRecord)){
                    result.put("success", true);
                    result.put("serialNum", yqxOrderRecord.getSerialNum());
                }
            }
        }
        
        try {            
            res.getWriter().write(JSONObject.toJSONString(result));            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private YqxOrderRecord createYqxOrderRecord(String productCode, Double factor, Double price, String vpmnDate) {
        String mobile = getYqxMobile();
        if(StringUtils.isEmpty(productCode) || (getVpmnFlag()&&StringUtils.isEmpty(vpmnDate)) || !StringUtils.isValidMobile(mobile)){
            return null;
        }
        YqxOrderRecord yqxOrderRecord = new YqxOrderRecord();
        yqxOrderRecord.setMobile(mobile);        
        yqxOrderRecord.setDiscount(new BigDecimal(String.valueOf(factor)).multiply(new BigDecimal(100)).intValue());        
        yqxOrderRecord.setVpmnTime(DateUtil.parse("yyyyMMddHHmmss", vpmnDate));
        yqxOrderRecord.setPayPrice(new BigDecimal(String.valueOf(price)).multiply(new BigDecimal(100)).longValue());
        
        IndividualProduct product = individualProductService.selectByProductCode(productCode);
        if(product == null){
            return null;
        }
        
        yqxOrderRecord.setIndividualProductId(product.getId());
        
        if(getVpmnFlag()){//四川V网用户
            Integer date = scMemberInquiryService.getdayRange(mobile);
            if(date != null){
                Integer acturalFactor = yqxVpmnDiscountService.getDiscountByDate(date);                
                Long acturalPrice = new BigDecimal(acturalFactor * product.getPrice()).divide(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
                if(!yqxOrderRecord.getPayPrice().equals(acturalPrice)){
                    LOGGER.info("四川云企信订购页面传递的价格{}和数据库计算获得的价格acturalFactor{}*productPrice{}={}不一致，无法订购", 
                            yqxOrderRecord.getPayPrice(), acturalFactor, product.getPrice(), acturalPrice);
                    return null;
                }
                if(!acturalFactor.equals(new BigDecimal(String.valueOf(factor)).multiply(new BigDecimal(100)).intValue())){
                    LOGGER.info("四川云企信订购页面传递的折扣{}*100和V网网龄获取的折扣{}不一致，无法订购", 
                            factor, acturalFactor);
                    return null;
                }
                
            }else{
                LOGGER.info("四川云企信获取V网网龄失败，无法进行订购");
                return null;
            }
        }else{
            if(!new Integer(100).equals(new BigDecimal(String.valueOf(factor)).multiply(new BigDecimal(100)).intValue())){
                LOGGER.info("重庆云企信订购页面传递的折扣{}*100的值不是100，无法订购", factor);
                return null;
            }
            //云企信价格没有折扣
            if(!yqxOrderRecord.getPayPrice().equals(product.getPrice().longValue())){
                LOGGER.info("重庆云企信订购记录中的价格{}和数据库计算获得的价格{}不一致，无法订购", yqxOrderRecord.getPayPrice(), product.getPrice());
                return null;
            }      
        }

        String serialNum = DateUtil.dateToString(new Date(), "yyyyMMddHHmmss") + SerialNumGenerator.genRandomNum(14);
        yqxOrderRecord.setSerialNum(serialNum);
        yqxOrderRecord.setTradeStatus(YqxOrderTradeStatus.PROCESSING.getCode());
        yqxOrderRecord.setPayStatus(YqxOrderPayStatus.WAIT.getCode());
        yqxOrderRecord.setRefundStatus(0);

        return yqxOrderRecord;
    }
    
    /** 
     * 订购确认及支付页
     * @Title: orderConfirm 
     */
    @RequestMapping("orderConfirm")
    public String orderConfirm(ModelMap map, String serialNum){
        YqxOrderRecord yqxOrderRecord = yqxOrderRecordService.selectBySerialNum(serialNum);
        if(yqxOrderRecord == null){
            LOGGER.info("订购记录不存在，serialNum={}", serialNum);
            return "yunqixin/failure.ftl";//订单确认受理失败页
        }
        if(!yqxOrderRecord.getMobile().equals(getYqxMobile())){
            LOGGER.info("订购记录中的手机号码和当前session中的用户手机号码不一致.session_mobile={},order_mobile={}, order_serialNum={}", 
                    getYqxMobile(), yqxOrderRecord.getMobile(), serialNum);
            return "yunqixin/failure.ftl";
        }
        map.put("yqxOrderRecord", yqxOrderRecord);
        DecimalFormat decimalFormat = new DecimalFormat("#####.##");
        IndividualProduct product = individualProductService.selectByPrimaryId(yqxOrderRecord.getIndividualProductId());
        if(product.getProductSize()>=1024L && product.getProductSize()<1024L*1024L){
            map.put("productName", decimalFormat.format(product.getProductSize()/1024.0)+"M");
        }else if(product.getProductSize()>=1024L*1024L){
            map.put("productName", decimalFormat.format(product.getProductSize()/1024.0/1024.0)+"G");
        }else{
            map.put("productName", decimalFormat.format(product.getProductSize())+"K");
        }

        map.put("vpmn", getVpmnFlag());//V网标识
        if(getVpmnFlag() && yqxOrderRecord.getVpmnTime()!=null){
            int[] dateRanage = null;//年月日的数组
            try {
                dateRanage = DateUtil.getDateRange(DateUtil.dateToString(yqxOrderRecord.getVpmnTime(), "yyyyMMddHHmmSS"));
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
            map.put("vpmnYear", dateRanage[0] + "年" + dateRanage[1] + "月" + dateRanage[2] + "天");
        }
                
        map.put("productPrice", decimalFormat.format(product.getPrice()/100.0));
        return "yunqixin/orderConfirm.ftl";
    }
    
    /** 
     * 交易中再次支付中转页
     * @Title: waitPay 
     */
    @RequestMapping("waitPay")
    public String waitPay(ModelMap map, String serialNum){
        YqxOrderRecord yqxOrderRecord = yqxOrderRecordService.selectBySerialNum(serialNum);
        if(yqxOrderRecord == null){
            LOGGER.info("订购记录不存在，serialNum={}", serialNum);
            return "yunqixin/failure.ftl";//订单确认受理失败页
        }
        if(!yqxOrderRecord.getMobile().equals(getYqxMobile())){
            LOGGER.info("订购记录中的手机号码和当前session中的用户手机号码不一致.session_mobile={},order_mobile={}, order_serialNum={}", 
                    getYqxMobile(), yqxOrderRecord.getMobile(), serialNum);
            return "yunqixin/failure.ftl";
        }
        map.put("yqxOrderRecord", yqxOrderRecord);
        DecimalFormat decimalFormat = new DecimalFormat("#####.##");
        IndividualProduct product = individualProductService.selectByPrimaryId(yqxOrderRecord.getIndividualProductId());
        if(product.getProductSize()>=1024L && product.getProductSize()<1024L*1024L){
            map.put("productName", decimalFormat.format(product.getProductSize()/1024.0)+"M");
        }else if(product.getProductSize()>=1024L*1024L){
            map.put("productName", decimalFormat.format(product.getProductSize()/1024.0/1024.0)+"G");
        }else{
            map.put("productName", decimalFormat.format(product.getProductSize())+"K");
        }

        map.put("productPrice", decimalFormat.format(product.getPrice()/100.0));
        return "yunqixin/waitPay.ftl";
    }
    
    /** 
     * 企业状态异常、EC状态异常、对账日充值限制日期内，无法进入支付流程，弹框提示“非常抱歉，目前暂不支持订购”
     * @Title: checkPay 
     */
    @RequestMapping("checkPay")
    public void checkPay(HttpServletResponse res){
        Map result = new HashMap();
        result.put("success", false);//是否可以支付，默认是不能支付
        
        //不在对账日且企业状态正常
        if(!yqxOrderRecordService.duringAccountCheckDate()){
            String enterpriseCode = globalConfigService.get(GlobalConfigKeyEnum.YQX_ORDERID_ENTER_CODE.getKey());
            Enterprise enterprise = enterprisesService.selectByCode(enterpriseCode);           
            if (enterprise != null && new Integer(1).equals(enterprise.getInterfaceFlag()) 
                    && new Integer(0).equals(enterprise.getDeleteFlag())){
                result.put("success", true);//是否可以支付，默认是不能支付
            }
            
        }
       
        try {            
            res.getWriter().write(JSONObject.toJSONString(result));            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /** 
     * 提交支付
     * @Title: submitPay 
     */
    @RequestMapping("submitPay")
    public String submitPay(String serialNum, String payType){
        YqxOrderRecord yqxOrderRecord = yqxOrderRecordService.selectBySerialNum(serialNum);
        if(StringUtils.isEmpty(payType) || yqxOrderRecord == null){
            LOGGER.info("支付类型为空或订购记录不存在，payType={}， serialNum={}", payType, serialNum);
            return "yunqixin/error.ftl";        
        }
        
        //检查订购记录中的支付状态
        if(YqxOrderPayStatus.SUCCESS.getCode() == yqxOrderRecord.getPayStatus().intValue()){
            LOGGER.info("该订购已支付成功，不能重新支付，订购serialNum{}", serialNum);
            return "yunqixin/error.ftl"; 
        }
        
        //进行支付
        return "redirect:/manage/payplatform/pay.html?orderSerialNum=" + serialNum + "&type=" + Integer.parseInt(payType) 
                + "&payOrderId=" + createPayOrderId(serialNum, yqxOrderRecord.getIndividualProductId());        
    }
    
    /** 
     * 构造128位支付订单号
     * @Title: createPayOrderId 
     */
    private String createPayOrderId(String serialNum, Long individualProductId) {
        
        String enterpriseCode = globalConfigService.get(GlobalConfigKeyEnum.YQX_ORDERID_ENTER_CODE.getKey());
        String provinceCode = globalConfigService.get(GlobalConfigKeyEnum.YQX_ORDERID_PROVINCE_CODE.getKey());
        String cityCode = globalConfigService.get(GlobalConfigKeyEnum.YQX_ORDERID_CITY_CODE.getKey());
        String receiveMoney = globalConfigService.get(GlobalConfigKeyEnum.YQX_ORDERID_RECEIVE_MONEY.getKey());
        String isp = globalConfigService.get(GlobalConfigKeyEnum.YQX_ORDERID_ISP_CODE.getKey());
        String supplier = globalConfigService.get(GlobalConfigKeyEnum.YQX_ORDERID_SUPPLIER_CODE.getKey());
        String app = globalConfigService.get(GlobalConfigKeyEnum.YQX_ORDERID_APP_CODE.getKey());
        
        String productCode = individualProductService.selectByPrimaryId(individualProductId).getProductCode();
        
        String orderId1 = changeLength(enterpriseCode, 10) + changeLength(provinceCode, 2) + changeLength(cityCode, 2) 
                + changeLength(receiveMoney, 18) + changeLength(isp, 1) + changeLength(supplier, 4) + changeLength(app, 6) 
                + changeLength(productCode, 6);
        int leftLength = 128 - serialNum.length();
        String orderId = changeLength(orderId1, leftLength) + serialNum;
        return orderId;
    }
    
    /** 
     * 变换长度
     * @Title: changeLength 
     */
    private String changeLength(String str, int totalLength) {
        int size = str.length();
        if(size >= totalLength){
            return str;
        }
        String temp = str;
        for (int i = 0; i < totalLength-size; i++) {
            temp +="0";
        }
        return temp;
    }

    /** 
     * 支付异常页
     * @Title: payErrpr 
     */
    @RequestMapping("payError")
    public String payError(){
        return "yunqixin/error.ftl";
    }
    
    /** 
     * 支付成功页
     * @Title: paySuccess 
     */
    @RequestMapping("paySuccess")
    public String paySuccess(){
        return "yunqixin/success.ftl";
    }
    
    /** 
     * 支付失败页
     * @Title: payFailure 
     */
    @RequestMapping("payFailure")
    public String payFailure(){
        return "yunqixin/failure.ftl";
    }
    
    
    /** 
     * 显示订购详情
     * @Title: showDetail 
     */
    @RequestMapping("showDetail")
    public String showDetail(String serialNum, ModelMap map){
        YqxOrderRecord yqxOrderRecord = yqxOrderRecordService.selectBySerialNum(serialNum);
        
        if(yqxOrderRecord == null){
            LOGGER.info("订购记录不存在，serialNum={}", serialNum);
            return "yunqixin/error.ftl";//订单确认受理失败页
        }
        if(!yqxOrderRecord.getMobile().equals(getYqxMobile())){
            LOGGER.info("订购记录中的手机号码和当前session中的用户手机号码不一致.session_mobile={},order_mobile={}, order_serialNum={}", 
                    getYqxMobile(), yqxOrderRecord.getMobile(), serialNum);
            return "yunqixin/error.ftl";
        }
        map.put("yqxOrderRecord", yqxOrderRecord);
      
        //根据订单号查找最新的一次支付成功记录，并传递到前台（前台需要获取支付时间）
        //YqxPayRecord yqxPayRecord = yqxPayRecordService.selectNewestSuccessRecord(serialNum);
        YqxPayRecord yqxPayRecord = yqxPayRecordService.selectByTransactionId(yqxOrderRecord.getPayTransactionId());
        if(yqxPayRecord == null){
            return "yunqixin/error.ftl";
        }
        map.put("yqxPayRecord", yqxPayRecord);

        DecimalFormat decimalFormat = new DecimalFormat("#####.##");
        IndividualProduct product = individualProductService.selectByPrimaryId(yqxOrderRecord.getIndividualProductId());
        if(product.getProductSize()>=1024L && product.getProductSize()<1024L*1024L){
            map.put("productName", decimalFormat.format(product.getProductSize()/1024.0)+"M");
        }else if(product.getProductSize()>=1024L*1024L){
            map.put("productName", decimalFormat.format(product.getProductSize()/1024.0/1024.0)+"G");
        }else{
            map.put("productName", decimalFormat.format(product.getProductSize())+"K");
        }
        if(YqxOrderPayStatus.FAIL.getCode() == yqxOrderRecord.getPayStatus().intValue()){//支付状态为“支付失败”
            return "yunqixin/payFailure.ftl";
        }else if(YqxOrderPayStatus.SUCCESS.getCode() == yqxOrderRecord.getPayStatus().intValue()){
            if(ChargeRecordStatus.FAILED.getCode().equals(yqxOrderRecord.getChargeStatus())
                    && (yqxOrderRecord.getRefundStatus().intValue() == 0)){//支付成功，充值失败，退款状态为未申请或退款失败时跳转到申请退款页
                return "yunqixin/refund.ftl";
            }
            if(ChargeRecordStatus.FAILED.getCode().equals(yqxOrderRecord.getChargeStatus())
                    && yqxOrderRecord.getRefundStatus().intValue() != 0){//支付成功，充值失败，退款状态为已申请退款跳转到申请退款处理中页
                return "yunqixin/refunding.ftl";
            }
            return "yunqixin/paySuccess.ftl";
        }else{
            return "yunqixin/payProcessing.ftl";
        }        
    }
    
    /** 
     * 提交退款申请
     * @Title: submitRefund 
     */
    @RequestMapping("submitRefund")
    public void submitRefund(HttpServletResponse res, String serialNum){
        Map result = new HashMap();
        YqxOrderRecord yqxOrderRecord = yqxOrderRecordService.selectBySerialNum(serialNum);
        if(yqxOrderRecord == null){
            LOGGER.info("订购记录不存在，serialNum={}", serialNum);
            result.put("success", false);
        }else{
            if(!yqxOrderRecord.getRefundStatus().equals(0)||!yqxOrderRecord.getApprovalRefund().equals(0)){
                LOGGER.info("用户mobile={}提交退款申请，订购记录serialNum={}，订购记录退款状态{}及提交退款标识{}异常，只记录申请退款的时间及申请退款标志位",
                        getYqxMobile(), yqxOrderRecord.getSerialNum(), yqxOrderRecord.getRefundStatus(), yqxOrderRecord.getApprovalRefund());
                yqxOrderRecord.setApprovalRefund(1);//1-标识用户提交了退款申请
                yqxOrderRecord.setRefundApprovalTime(new Date());
            }else{
                yqxOrderRecord.setRefundStatus(1);//1-退款申请处理中
                yqxOrderRecord.setApprovalRefund(1);//1-标识用户提交了退款申请
                yqxOrderRecord.setRefundApprovalTime(new Date());                
            }
            //更新订单的退款相关状态
            if(yqxOrderRecordService.updateByPrimaryKey(yqxOrderRecord)){
                LOGGER.info("用户mobile={}提交退款申请，订购记录serialNum={}", getYqxMobile(), yqxOrderRecord.getSerialNum());
                result.put("success", true);
            }else{
                result.put("success", false);
            }
        }
        try {           
            res.getWriter().write(JSONObject.toJSONString(result));            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /** 
     * 四川云企信的订购确认页,四川云企信的入口
     * @Title: notice 
     */
    @RequestMapping("notice")
    public String notice(String key, ModelMap model){
    //public String notice(String mobile, String originalId){
//        setYqxMobile("18867103685");//////////////TODO
//        setYqxAppTag("zyscyqx");//////////////TODO
        
        //将key解密并使用&进行split,如果合法，则parties[1]为手机号，parties[2]为originalId
        String[] parties = decryAndSplitKey(key);
        if(!verifyIfValidStrs(parties)){
            LOGGER.error("解析key失败，数据不合法,key="+key);
            return "yunqixin/404.ftl";
        }

        setYqxMobile(parties[1]);
        setYqxAppTag(parties[2]);

        //setYqxMobile("18867103685");//////////////TODO
        //setYqxAppTag("test");//////////////TODO
        
        String startTime = globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_START_TIME.getKey());
        String startDay = globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_START_DAY.getKey());
        String endTime = globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_END_TIME.getKey());
        String endDay = globalConfigService.get(GlobalConfigKeyEnum.CHECK_ACCOUNT_END_DAY.getKey());
        model.put("startTime", startTime);
        model.put("startDay", startDay);
        model.put("endTime", endTime);
        model.put("endDay", endDay);

        return "yunqixin/notice.ftl";
    }
    
    /**
     * 云企信重庆入口
     */
    @RequestMapping("cqYqxEntry")
    public String cqYqxEntry(ModelMap map,String key){

//      setYqxMobile("18867103685");//////////////TODO
//      setYqxAppTag("zycqyqx");//////////////TODO

        //将key解密并使用&进行split,如果合法，则parties[1]为手机号，parties[2]为originalId
        String[] parties = decryAndSplitKey(key);
        if(!verifyIfValidStrs(parties)){
            LOGGER.error("解析key失败，数据不合法,key="+key);
            return "yunqixin/404.ftl";
        }
        
        setYqxMobile(parties[1]);
        setYqxAppTag(parties[2]);
        return orderIndex(map);
    }
    
    /**
     * 将key 1.解密，2.用&分解，然后需要用verifyIfValidStrs验证是否合法
     */
    private String[] decryAndSplitKey(String key){
        if(org.apache.commons.lang.StringUtils.isBlank(key)){
            return new String[1];//返回一个非法数组
        }
        String decryStr = AESdecry.decrypt(key, getYqxEncryKey());
        return decryStr.split("&");
    }
    
    /**
     * 验证合法
     * 
     */
    private boolean verifyIfValidStrs(String[] decryStrs){
        if(decryStrs.length != 3){//1.个数必须为3
            return false;
        }
        
        if(!com.cmcc.vrp.util.StringUtils.isValidMobile(decryStrs[1])){//手机号合法
            return false;
        }
        
        if(!VALID_ORIGINALID_CQ.equals(decryStrs[2]) &&
                !VALID_ORIGINALID_SC.equals(decryStrs[2])){//必须是重庆或四川
            return false;
        }
        
        return true;
    }
    
        
    /**
     * 我们给云企信平台返回数据加密使用的key
     */
    private String getYqxEncryKey(){
        return "llpt_private_yqx";
    }
  
//    public static void main(String[] args){
//        System.out.print((new BigDecimal(String.valueOf(4.6)).multiply(new BigDecimal(100)).longValue()));
//        String mobile = "18867102087";
//        String flag = "zycqyqx";
//        String time="20170519135400";
//        String combine = time + "&" + mobile + "&" + flag;
//        
//        String deString = AESdecry.encrypt(combine, "llpt_private_yqx");
//        System.out.println(URLEncoder.encode(deString, "utf-8"));
//    }
}
