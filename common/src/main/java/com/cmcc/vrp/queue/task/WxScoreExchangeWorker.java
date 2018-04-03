package com.cmcc.vrp.queue.task;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.enums.WxTemplateMsgType;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.IndividualAccountRecordService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.wx.WxExchangeRecordService;
import com.cmcc.vrp.wx.beans.ExchangeChargePojo;
import com.cmcc.vrp.wx.beans.TemplateMsgPojo;
import com.cmcc.vrp.wx.flowcoin.GdFlowCoinCharge;
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
public class WxScoreExchangeWorker extends Worker {
    private static final Logger logger = LoggerFactory.getLogger(WxScoreExchangeWorker.class);
    
    @Autowired
    GdFlowCoinCharge gdFlowCoinCharge;
    
    @Autowired
    TaskProducer producer;
    
    @Autowired
    AdministerService administerService;
    
    @Autowired
    WxExchangeRecordService wxExchangeRecordService;
    
    @Autowired
    IndividualAccountRecordService individualAccountRecordService;
    
    @Autowired
    IndividualAccountService individualAccountService;
    
    @Autowired
    IndividualProductService individualProductService;
    
    @Override
    public void exec() {
        //0. 获取队列消息
        String taskStr = getTaskString();
        logger.info("从充值队列中消费消息，消息内容为{}.", taskStr);

        //1. 解析充值参数
        ExchangeChargePojo pojo;
        if ((pojo = parse(taskStr)) == null) {
            logger.error("无效的充值请求参数，充值失败.");
            return;
        }
        WxExchangeRecord wxExchangeRecord = null;
        if (StringUtils.isBlank(pojo.getSystemNum())
                || (wxExchangeRecord=wxExchangeRecordService.selectBySystemNum(pojo.getSystemNum())) == null) {
            logger.error("系统序列号没有对应的兑换记录，systemNum={}", pojo.getSystemNum());
            return;
        }
        
        //向广东BOSS充值
        BossOperationResult result = gdFlowCoinCharge.chargeFlow(pojo.getMobile(), pojo.getPrdCode(), wxExchangeRecord);
        //兑换成功
        if(!result.isSuccess()){
            //兑换失败，退积分，并发送兑换失败模板消息
            IndividualProduct individualPointProduct = individualProductService.getIndivialPointProduct();
            if (individualPointProduct == null) {
                logger.error("获取个人积分产品时返回空值,请确认是否配置了个人积分产品!");
                return;
            }                    
            IndividualAccount account = individualAccountService.getAccountByOwnerIdAndProductId(wxExchangeRecord.getAdminId(), individualPointProduct.getId(), 
                    IndividualAccountType.INDIVIDUAL_BOSS.getValue());
            IndividualProduct product = individualProductService.selectByPrimaryId(wxExchangeRecord.getIndividualProductId());
            String desc = "兑换失败--" + wxExchangeRecord.getMobile() + "--" + product.getProductSize().intValue()/1024 + "MB";;
            if(individualAccountService.changeAccount(account, new BigDecimal(wxExchangeRecord.getCount()), wxExchangeRecord.getSystemNum(), 
                    (int)AccountRecordType.INCOME.getValue(), desc, ActivityType.POINTS_EXCHANGR.getCode(), 1)){   
                //发送兑换失败模板消息
                TemplateMsgPojo msgPojo = new TemplateMsgPojo();
                msgPojo.setType(WxTemplateMsgType.EXCHANGE_FAIL);
                Administer admin = administerService.selectAdministerById(wxExchangeRecord.getAdminId());
                msgPojo.setMobile(admin.getMobilePhone());
                msgPojo.setExchangeSystemNum(wxExchangeRecord.getSystemNum());            
                if(!producer.produceWxSendTemplateMsg(msgPojo)){
                    logger.error("进入消息模板队列失败！msgPojo={}", new Gson().toJson(msgPojo));                
                }
            }else{
                logger.error("流量币退回变更账户失败");
            }
        }

        logger.info("充值结果返回：" + new Gson().toJson(result));        
    }

    //解析充值对象
    private ExchangeChargePojo parse(String taskString) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(taskString, ExchangeChargePojo.class);
        } catch (Exception e) {
            logger.info("参数反序列化错误, 错误信息为{}， 具体的参数为{}", e.getMessage(), taskString);
            return null;
        }
    }
}
