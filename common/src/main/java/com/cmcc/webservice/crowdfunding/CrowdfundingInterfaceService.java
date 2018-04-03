package com.cmcc.webservice.crowdfunding;

import java.util.Map;

import com.cmcc.webservice.crowdfunding.pojo.ActivityResultResp;
import com.cmcc.webservice.crowdfunding.pojo.CFChargePojo;
import com.cmcc.webservice.crowdfunding.pojo.CFChargeReq;
import com.cmcc.webservice.crowdfunding.pojo.CFChargeResultResp;
import com.cmcc.webservice.crowdfunding.pojo.JoinActivityReq;
import com.cmcc.webservice.crowdfunding.pojo.JoinActivityResp;
import com.cmcc.webservice.crowdfunding.pojo.PaymentPojo;
import com.cmcc.webservice.crowdfunding.pojo.PaymentReq;
import com.cmcc.webservice.crowdfunding.pojo.QueryActResPojo;
import com.cmcc.webservice.crowdfunding.pojo.QueryActResReq;
import com.cmcc.webservice.crowdfunding.pojo.QueryActivityReq;
import com.cmcc.webservice.crowdfunding.pojo.QueryActivityResp;
import com.cmcc.webservice.crowdfunding.pojo.QueryJoinResReq;
import com.cmcc.webservice.crowdfunding.pojo.QueryJoinResResp;

/**
 * CrowdfundingInterfaceService.java
 * @author wujiamin
 * @date 2017年2月8日
 */
public interface CrowdfundingInterfaceService {
    /** 
     * 校验查询活动参数
     * @Title: validateQueryActivityRequest 
     */
    boolean validateQueryActivityRequest(QueryActivityReq request);
    
    /** 
     * 查询活动
     * @Title: queryActivity 
     */
    QueryActivityResp queryActivity(QueryActivityReq request);

    /** 
     * 众筹活动报名
     * @Title: joinActivity 
     */
    JoinActivityResp joinActivity(JoinActivityReq request);
    
    /**
     * 校验充值接口的请求参数是否为空
     * @param req 请求对象
     * @author qinqinyan
     * */
    boolean validateCFChargeReq(CFChargeReq req);
    
    /**
     * 校验是否满足充值条件
     * @param pojo
     * @author qinqinyan
     * */
    Map<String, String> validateChargeRequirement(CFChargePojo pojo);
    
    /**
     * 接口充值
     * @param pojo
     * @author qinqinyan
     * */
    boolean chargeForInterface(CFChargePojo pojo);
    
    /**
     * 校验通知支付接口
     * @param req
     * @author qinqinyan
     * */
    boolean validatePaymentReq(PaymentReq req);
    
    /**
     * 支付完成通知流量平台接口
     * @param pojo
     * @param systemSerial 系统支付流水号
     * @author qinqinyan
     * */
    Map<String, String> notifyPayment(PaymentPojo pojo, String systemSerial);
    
    /**
     * 众筹结果查询接口参数校验
     * @param req
     * @author qinqinyan
     * */
    boolean validateQueryActResReq(QueryActResReq req);
    
    /**
     * 众筹结果查询接口
     * @param pojo
     * @author qinqinyan
     * */
    ActivityResultResp queryActivityResult(QueryActResPojo pojo);
    
    /**
     * 众筹充值结果查询
     * @param recordId 充值ID
     * @author qinqinyan
     * */
    CFChargeResultResp queryCFChargeResult(String recordId);

    /** 
     * 校验众筹报名结果查询
     * @Title: validateQueryJoinResultRequest 
     */
    boolean validateQueryJoinResultRequest(QueryJoinResReq req);

    /** 
     * 众筹报名结果查询
     * @Title: queryJoinResult 
     */
    QueryJoinResResp queryJoinResult(QueryJoinResReq req);
}
