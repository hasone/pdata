package com.cmcc.vrp.pay.service;

import com.cmcc.vrp.pay.enums.PayMethodType;
import com.cmcc.vrp.pay.enums.RefundType;
import com.cmcc.vrp.pay.model.Result;
import com.cmcc.vrp.province.model.YqxPayRecord;

/**
 * 
 * PayPlatformService
 *
 */
public interface PayPlatformService {
    /**
     * 通过支付流水号，订单流水号，金额，支付类型生成支付地址
     */
    String generatePayUrl(String transactionId,String orderId,String amount,PayMethodType type,String payInfo);
    
    /**
     * 分析支付返回xml数据
     */
    YqxPayRecord analyseCallBackXml(String xml);
    
    /**
     * 通过支付流水号,支付平台返回的流水号，订单流水号，退款流水号，退款原因，退款金额，退款类型
     * transactionId:支付流水号,同支付transactionId，非空
     * doneCode：支付平台返回的流水号，非空。
     * orderId：订单流水号，非空。
     */
    String generateRefundXml(String transactionId,String doneCode,String orderId,
            String refundReason,String amount,RefundType refundType);

    
    /**
     * 
     * 云企信退款全流程，通过yqx_pay_record的主键进行退款
     * id：yqx_pay_record主键
     * refundReason：退款原因，非空
     * amount:退款金额。单位为元必需带两位小数。比如一元请传 1.00   该字段可为空，为空时将会退款支付金额 
     * refundType:退款类型，默认请传BUSINESS
     */
    Result yqxRefundProcess(Long id,String refundReason,String amount,RefundType refundType, Long operatorId);
    
    /**
     * 
     * 云企信退款全流程，通过yqx_pay_record的主键进行退款
     * doneCode:支付返回流水号
     * refundReason：退款原因，非空
     * amount:退款金额。单位为元必需带两位小数。比如一元请传 1.00   该字段可为空，为空时将会退款支付金额 
     * refundType:退款类型，默认请传BUSINESS
     */
    Result yqxRefundProcess(String doneCode,String refundReason,String amount,RefundType refundType, Long operatorId);
    
    /**
     * 退款接口收到xml，yqx相关逻辑处理
     */
    boolean yqxRefundAsyncXml(String xml);
}
