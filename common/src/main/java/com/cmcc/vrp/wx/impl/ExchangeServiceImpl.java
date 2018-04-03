package com.cmcc.vrp.wx.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.enums.WxTemplateMsgType;
import com.cmcc.vrp.province.cache.AbstractCacheSupport;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.WxAdminister;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.WxAdministerService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.wx.ExchangeService;
import com.cmcc.vrp.wx.WxExchangeRecordService;
import com.cmcc.vrp.wx.beans.ExchangeChargePojo;
import com.cmcc.vrp.wx.beans.TemplateMsgPojo;
import com.cmcc.vrp.wx.model.WxExchangeRecord;
import com.google.gson.Gson;

/**
 * ExchangeServiceImpl.java
 * @author wujiamin
 * @date 2017年3月15日
 */
@Service
public class ExchangeServiceImpl extends AbstractCacheSupport implements ExchangeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeServiceImpl.class);
    @Autowired
    IndividualAccountService individualAccountService;
    @Autowired
    IndividualProductService individualProductService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    TaskProducer producer;
    @Autowired
    WxExchangeRecordService wxExchangeRecordService;
    @Autowired
    WxAdministerService wxAdministerService;
    
    @Override
    public boolean operateExchange(String mobile, Long adminId, String prdCode, JSONObject json) {        
        //1、判断是都是自然月最后两天
        if(new Date().after(getStartDate())
                && new Date().before(getEndDate())){
            LOGGER.info("每个自然月配置时间内兑换流量，系统升级不能兑换");
            json.put("result", "系统升级不能兑换");
            return false;
        }
        //2、判断用户当天是否已经兑换过
//        if(!StringUtils.isEmpty(cacheService.get(adminId.toString()))){
//            LOGGER.info("今天已经兑换过流量请明天再来,adminId={}", adminId);
//            json.put("result", "今天已经兑换过流量请明天再来");
//            return false;            
//        }        
        
        //判断当天该用户兑换的总额度
        Calendar ca = Calendar.getInstance();
        Integer totalDaySize = wxExchangeRecordService.sumDayProductSize(DateUtil.getBeginOfDay(new Date()), 
                DateUtil.getEndTimeOfDate(new Date()), adminId);
        IndividualProduct product = individualProductService.selectByProductCode(prdCode);
        Integer limitDaySize = Integer.parseInt(globalConfigService.get(GlobalConfigKeyEnum.WEIXIN_EXCHANGE_DAY_LIMIT.getKey()));
        if(limitDaySize != null
                && limitDaySize != -1
                && totalDaySize + product.getProductSize().intValue() > limitDaySize * 1024){
            LOGGER.info("当天兑换流量额度不能大于{}MB，当前加上本次兑换后总额为{}KB", limitDaySize, totalDaySize + product.getProductSize().intValue());
            json.put("result", "当天兑换流量额度不能大于" + limitDaySize + "MB");
            return false;
        }
        //2、判断当月该用户兑换的总额度
        //得到当月用户兑换的流量总数
        Integer totalSize = wxExchangeRecordService.sumProductSize(ca.get(Calendar.MONTH)+1, adminId);
        Integer limitSize = Integer.parseInt(globalConfigService.get(GlobalConfigKeyEnum.WEIXIN_EXCHANGE_MOTHLY_LIMIT.getKey()));
        if(limitSize != null
                && limitSize != -1
                && totalSize + product.getProductSize().intValue() > limitSize * 1024){
            LOGGER.info("当月兑换流量额度不能大于{}MB，当前加上本次兑换后总额为{}KB", limitSize, totalSize + product.getProductSize().intValue());
            json.put("result", "当月兑换流量额度不能大于" + limitSize + "MB");
            return false;
        }
        //3、判断产品当月的兑换总额度
        Integer totalNum = wxExchangeRecordService.sumProductNum(ca.get(Calendar.MONTH)+1, product.getId());
        String limitProductNum = globalConfigService.get(GlobalConfigKeyEnum.WEIXIN_EXCHANGE_PRODUCT_NUM_LIMIT.getKey());
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(limitProductNum);
        try {
            if (!com.cmcc.vrp.util.StringUtils.isEmpty(jsonObject.getString(product.getProductSize().toString()))) {
                Integer limitNum = Integer.valueOf(jsonObject.getString(product.getProductSize().toString()));
                if(limitNum!=null 
                        && limitNum != -1
                        && totalNum >=limitNum){
                    LOGGER.info("流量包库存不足请下个月再来，productId={}，当月已兑换个数{}", product.getId(), totalNum);
                    json.put("result", "流量包库存不足请下个月再来");
                    return false;
                }
            }            
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
      //4、判断当月的兑换总额度
        Integer totalAllNum = wxExchangeRecordService.sumAllProductSize(ca.get(Calendar.MONTH)+1);
        Integer limitAllSize = Integer.parseInt(globalConfigService.get(GlobalConfigKeyEnum.WEIXIN_EXCHANGE_MOTHLY_ALL_LIMIT.getKey()));
        
        if(limitAllSize !=null 
                && limitAllSize != -1
                && totalAllNum + product.getProductSize().intValue() > limitAllSize * 1024){
            LOGGER.info("当月兑换流量额度不能大于{}MB，当前加上本次兑换后总额为{}KB", limitAllSize, totalAllNum + product.getProductSize().intValue());
            json.put("result", "当月兑换流量额度不能大于" + limitAllSize + "MB");
            return false;
        }
        //5生成序列号 判断当天兑换次数是否达到限制
        String systemNum = SerialNumGenerator.buildSerialNum();
        Long exchangeNum = cacheService.getIncrOrUpdate(adminId.toString(), DateUtil.secondsToEnd(new DateTime()));
        Integer limitExchangeNum = Integer.parseInt(globalConfigService.get(GlobalConfigKeyEnum.WEIXIN_EXCHANGE_NUM_LIMIT.getKey()));
        if (limitExchangeNum != null
                && limitExchangeNum != -1
                && exchangeNum > limitExchangeNum) {
            LOGGER.info("每天只可以赠送不超过{}次，exchangeNum={}", limitExchangeNum, exchangeNum);
            json.put("result", "每天只可以兑换不超过" + limitExchangeNum +"次");
            return false;
        }
        try{
            if(insertAllExchangeRecord(mobile, adminId, prdCode, systemNum)){
                
                //缓存中标记用户当天已兑换
                //cacheService.getIncrOrUpdate(adminId.toString(), DateUtil.secondsToEnd(new DateTime()));
                
                //1、塞入充值队列
                ExchangeChargePojo pojo = new ExchangeChargePojo();
                pojo.setMobile(mobile);
                pojo.setPrdCode(prdCode);
                pojo.setSystemNum(systemNum);
                if(!producer.produceWxScoreExchangeMsg(pojo)){
                    LOGGER.error("进入积分兑换充值队列失败！pojo={}", new Gson().toJson(pojo));
                    //进入队列失败，退款并修改状态
                    //兑换失败，退积分，并发送兑换失败模板消息
                    WxExchangeRecord wxExchangeRecord = wxExchangeRecordService.selectBySystemNum(systemNum);
                    IndividualProduct individualPointProduct = individualProductService.getIndivialPointProduct();
                    if (individualPointProduct == null) {
                        LOGGER.error("获取个人积分产品时返回空值,请确认是否配置了个人积分产品!");
                    }else{    
                        IndividualAccount account = individualAccountService.getAccountByOwnerIdAndProductId(wxExchangeRecord.getAdminId(),
                                individualPointProduct.getId(), IndividualAccountType.INDIVIDUAL_BOSS.getValue());

                        String desc = "兑换失败--" + wxExchangeRecord.getMobile() + "--" + product.getProductSize().intValue()/1024 + "MB";;
                        if(individualAccountService.changeAccount(account, new BigDecimal(wxExchangeRecord.getCount()), wxExchangeRecord.getSystemNum(), 
                                (int)AccountRecordType.INCOME.getValue(), desc, ActivityType.POINTS_EXCHANGR.getCode(), 1)){   
                            //发送兑换失败模板消息
                            TemplateMsgPojo msgPojo = new TemplateMsgPojo();
                            msgPojo.setType(WxTemplateMsgType.EXCHANGE_FAIL);
                            WxAdminister admin = wxAdministerService.selectWxAdministerById(wxExchangeRecord.getAdminId());
                            msgPojo.setMobile(admin.getMobilePhone());
                            msgPojo.setExchangeSystemNum(wxExchangeRecord.getSystemNum());            
                            if(!producer.produceWxSendTemplateMsg(msgPojo)){
                                LOGGER.error("进入消息模板队列失败！msgPojo={}", new Gson().toJson(msgPojo));                
                            }
                        }else{
                            LOGGER.error("流量币退回变更账户失败");
                        }
                    }

                }
                return true;
            }
        }catch(Exception e){
            LOGGER.error("插入所有积分兑换相关的记录时出现异常，error={}", e.getMessage());
            json.put("result", "fail");
            return false;
        }
        json.put("result", "fail");
        return false;
    }
    
    /** 
     * @Title: insertAllExchangeRecord 
     * 插入兑换记录相关的所有记录
     */
    @Transactional    
    private boolean insertAllExchangeRecord(String mobile, Long adminId, String prdCode, String systemNum) {
        //0、校验
        //获取积分产品
        IndividualProduct individualPointProduct = individualProductService.getIndivialPointProduct();
        if (individualPointProduct == null) {
            LOGGER.error("获取个人积分产品时返回空值,请确认是否配置了个人积分产品!");
            return false;
        }
        //用户积分是否足够兑换流量
        IndividualAccount pointsAccount = individualAccountService.getAccountByOwnerIdAndProductId(adminId, individualPointProduct.getId(), 
                IndividualAccountType.INDIVIDUAL_BOSS.getValue());
        IndividualProduct product = individualProductService.selectByProductCode(prdCode);
        if(product == null){
            LOGGER.error("获取个人产品时返回空值,productCode={}", prdCode);
            return false;
        }
        String exchangeRule = globalConfigService.get(GlobalConfigKeyEnum.WEIXIN_SCORE_EXCHANGE_RULE.getKey());
        if(StringUtils.isEmpty(exchangeRule)){
            LOGGER.error("积分兑换规则未配置，请确认globalConfig是否有配置WEIXIN_SCORE_EXCHANGE_RULE");
            return false;
        }
        Integer points = product.getProductSize().intValue()/1024/Integer.parseInt(exchangeRule);
        if(pointsAccount.getCount().intValue()<points){
            LOGGER.error("积分兑换时账户余额不足，当前账户accountId={}，count={}，兑换需要{}", pointsAccount.getId(), pointsAccount.getCount(), points);
            return false;
        }
        
       
        //1、变更账户
        String desc = "兑换--" + mobile + "--" + product.getProductSize().intValue()/1024 + "MB";
        if(individualAccountService.changeAccount(pointsAccount, new BigDecimal(points), systemNum, (int)AccountRecordType.OUTGO.getValue(), 
                desc, ActivityType.POINTS_EXCHANGR.getCode(), 0)){
            //2、插入兑换充值记录
            WxExchangeRecord exchangeRecord = createExchangeRecord(mobile, adminId, prdCode, systemNum, points);
            if(!wxExchangeRecordService.insert(exchangeRecord)){
                LOGGER.error("插入exchangeRecord失败！exchangeRecord={}", new Gson().toJson(exchangeRecord));
                throw new RuntimeException("插入exchangeRecord失败");
            }           
        }

        return true;
    }
    

    private WxExchangeRecord createExchangeRecord(String mobile, Long adminId, String prdCode, String systemNum, Integer points){
        WxExchangeRecord exchangeRecord = new WxExchangeRecord();
        exchangeRecord.setAdminId(adminId);
        exchangeRecord.setCreateTime(new Date());
        exchangeRecord.setDeleteFlag(0);
        IndividualProduct product = individualProductService.selectByProductCode(prdCode);
        if(product == null){
            return null;
        }
        exchangeRecord.setIndividualProductId(product.getId());
        exchangeRecord.setMobile(mobile);
        exchangeRecord.setStatus(ChargeRecordStatus.WAIT.getCode());
        exchangeRecord.setSystemNum(systemNum);
        exchangeRecord.setUpdateTime(new Date());
        exchangeRecord.setCount(points);
        
        return exchangeRecord;
    }

    @Override
    protected String getPrefix() {        
        return "wx.exchange.";
    }
    
    
    /** 
     * 获取当月的倒数第二天0:0:0
     * @Title: getLastTwoDate 
     */
    private Date getLastTwoDate(){
        Calendar ca = Calendar.getInstance();    
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH)); 
        Date end2Date = DateUtil.getDateBefore(ca.getTime(), 1);
        Date date = DateUtil.getBeginOfDay(end2Date);
        return date;
    }
    
    private Date getStartDate() {
        Calendar ca = Calendar.getInstance();    
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH)); 
        Date end2Date = DateUtil.getDateBefore(ca.getTime(), 
                Integer.valueOf(globalConfigService.get(GlobalConfigKeyEnum.WEIXIN_EXCHANGE_START_DATE.getKey())));
        Date date = DateUtil.getBeginOfDay(end2Date);
        return date;
    }
    private Date getEndDate() {
        Calendar ca = Calendar.getInstance();    
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH)); 
        Date end2Date = DateUtil.getDateBefore(ca.getTime(), 
                Integer.valueOf(globalConfigService.get(GlobalConfigKeyEnum.WEIXIN_EXCHANGE_END_DATE.getKey())));
        Date date = DateUtil.getEndTimeOfDate(end2Date);
        return date;
    }

}
