package com.cmcc.vrp.province.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.ecinvoker.EcBossOperationResultImpl;
import com.cmcc.vrp.boss.ecinvoker.impl.YqxEcBossServiceImpl;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.AsyncCallbackReq;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.YqxChargeInfo;
import com.cmcc.vrp.province.model.YqxOrderRecord;
import com.cmcc.vrp.province.model.YqxPayRecord;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.YqxChargeInfoService;
import com.cmcc.vrp.province.service.YqxChargeService;
import com.cmcc.vrp.province.service.YqxOrderRecordService;
import com.cmcc.vrp.province.service.YqxPayRecordService;

/**
 * 云企信的充值
 * YqxChargeServiceImpl.java
 * @author wujiamin
 * @date 2017年5月10日
 */
@Service
public class YqxChargeServiceImpl implements YqxChargeService{
    private final static Logger LOGGER = LoggerFactory.getLogger(YqxChargeServiceImpl.class); 
    @Autowired
    YqxEcBossServiceImpl bossService;
    @Autowired
    YqxOrderRecordService yqxOrderRecordService;
    @Autowired
    YqxPayRecordService yqxPayRecordService;
    @Autowired
    ProductService productService;
    @Autowired
    IndividualProductService individualProductService;
    @Autowired
    YqxChargeInfoService yqxChargeInfoService;
    
    @Override
    public boolean charge(String payTransactionId) {
        YqxPayRecord yqxPayRecord = yqxPayRecordService.selectByTransactionId(payTransactionId);
        if(yqxPayRecord == null){
            LOGGER.info("YqxPayRecord支付记录不存在，payTransactionId={}", payTransactionId);
            return false;
        }
        YqxOrderRecord orderRecord = yqxOrderRecordService.selectBySerialNum(yqxPayRecord.getOrderSerialNum());
        if(orderRecord == null){
            LOGGER.info("YqxOrderRecord订购记录不存在，serialNum={}", yqxPayRecord.getOrderSerialNum());
            return false;
        }
        //检查充值状态
        if(orderRecord.getChargeStatus() != null && orderRecord.getChargeStatus() != 1){
            LOGGER.info("YqxOrderRecord已存在充值，serialNum={}，当前充值状态chargeStatus={}，更新支付记录payTransactionId={}的充值状态为充值失败 ", 
                    yqxPayRecord.getOrderSerialNum(), orderRecord.getChargeStatus(), payTransactionId);
            if(!yqxPayRecordService.updateChargeStatus(ChargeRecordStatus.FAILED.getCode(), payTransactionId, new Date())){
                LOGGER.info("更新yqxPayRecord为充值失败时失败，payTransactionId={}", payTransactionId);
            }
            return false;
        }
        IndividualProduct product = individualProductService.selectByPrimaryId(orderRecord.getIndividualProductId());
        if(product == null){
            LOGGER.info("IndividualProduct不存在，productId={}", orderRecord.getIndividualProductId());
            return false;
        }
        Product platformProduct = productService.selectByCode(product.getProductCode()); 
        if(platformProduct == null){
            LOGGER.info("platformProduct不存在，productCode={}", product.getProductCode());
            return false;
        }
        
        //1、插入yqx_charge_info表
        YqxChargeInfo yqxChargeInfo = new YqxChargeInfo();
        yqxChargeInfo.setSerialNum(payTransactionId);
        if(!yqxChargeInfoService.insert(yqxChargeInfo)){
            LOGGER.info("YqxChargeInfo插入失败，serialNum={}", payTransactionId);
            return false;
        }
        
        //1、更新yqx_order_record表，charge_status为“已发送充值请求”,插入支付流水号
        Date now = new Date();
        orderRecord.setChargeTime(now);
        orderRecord.setChargeStatus(ChargeRecordStatus.PROCESSING.getCode());
        if(!yqxOrderRecordService.updateByPrimaryKey(orderRecord)){
            LOGGER.info("更新yqxOrderRecord为已发送充值请求失败，serialNum={}", orderRecord.getSerialNum());
            return false;
        } 

        BossOperationResult result = bossService.charge(null, platformProduct.getId(), orderRecord.getMobile(), payTransactionId, null);
        if(result!=null && result.getResultCode().equals(EcBossOperationResultImpl.SUCCESS.getResultCode())){
            //支付记录的充值状态变成已发送充值请求，修改状态前先判断该支付的充值状态是否是成功或失败
            if(!yqxPayRecordService.updateChargeStatus(ChargeRecordStatus.PROCESSING.getCode(), payTransactionId, now)){
                LOGGER.info("更新yqxPayRecord为已发送充值请求时失败，payTransactionId={}", payTransactionId);
            }
            return true;
        }else{
            LOGGER.info("云企信发送EC充值请求，平台直接返回失败，更新yqx_pay_record和yqx_order_record为充值失败");
            //1、更新yqx_order_record表，charge_status为“充值失败”
            orderRecord.setChargeReturnTime(new Date());
            orderRecord.setChargeMsg("充值失败");
            orderRecord.setChargeStatus(ChargeRecordStatus.FAILED.getCode());
            if(!yqxOrderRecordService.updateByPrimaryKey(orderRecord)){
                LOGGER.info("更新yqxOrderRecord为充值失败，失败，serialNum={}", orderRecord.getSerialNum());
            }
            
            //支付记录的充值状态变成充值失败，修改状态前先判断该支付的充值状态是否是成功或失败
            if(!yqxPayRecordService.updateChargeStatus(ChargeRecordStatus.FAILED.getCode(), payTransactionId, now)){
                LOGGER.info("更新yqxPayRecord为充值失败时失败，payTransactionId={}", payTransactionId);
            }
            return false;
        }        
    }

    @Override
    public boolean processingCallback(AsyncCallbackReq acr) {
        YqxPayRecord yqxPayRecord = yqxPayRecordService.selectByTransactionId(acr.getSystemSerialNum());
        if(yqxPayRecord == null){
            LOGGER.info("YqxPayRecord支付记录不存在，payTransactionId={}", acr.getSystemSerialNum());
            return false;
        }
        YqxOrderRecord orderRecord = yqxOrderRecordService.selectBySerialNum(yqxPayRecord.getOrderSerialNum());
        if(orderRecord == null){
            LOGGER.info("YqxOrderRecord订购记录不存在，serialNum={}", yqxPayRecord.getOrderSerialNum());
            return false;
        }
        if(!acr.getSystemSerialNum().equals(orderRecord.getPayTransactionId())){
            LOGGER.info("YqxOrderRecord订购记录中的payTransactionId和充值返回的不一致，serialNum={}，记录中的payTransactionId={}，"
                    + "充值返回的payTransactionId={}", yqxPayRecord.getOrderSerialNum(), yqxPayRecord.getPayTransactionId(), acr.getSystemSerialNum());
            return false;
        }
        if(ChargeRecordStatus.COMPLETE.getCode().equals(orderRecord.getChargeStatus()) 
                || ChargeRecordStatus.FAILED.getCode().equals(orderRecord.getChargeStatus())){
            LOGGER.info("订购记录的充值状态已经是失败或成功，不能重复修改，order_serialNum={}，AsyncCallbackReq={}", yqxPayRecord.getOrderSerialNum(),
                    JSONObject.toJSONString(acr));
            return false;
        }
        
        //支付记录的充值状态更新
        if(!yqxPayRecordService.updateChargeStatus(acr.getChargeRecordStatus(), acr.getSystemSerialNum(), null)){
            LOGGER.info("更新yqxPayRecord为{}时失败，payTransactionId={}", acr.getChargeRecordStatus(), acr.getSystemSerialNum());
        }
        
        //更新yqx_order_record表
        orderRecord.setChargeReturnTime(new Date());
        orderRecord.setChargeMsg(acr.getErrorMsg());
        orderRecord.setChargeStatus(acr.getChargeRecordStatus());
        if(!yqxOrderRecordService.updateByPrimaryKey(orderRecord)){
            LOGGER.info("更新yqxOrderRecord，失败，serialNum={}, AsyncCallbackReq={}", orderRecord.getSerialNum(), JSONObject.toJSONString(acr));
            return false;
        }
        
        return true;
    }

}
