/**
 *
 */
package com.cmcc.vrp.boss.sichuan.service.impl;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.sichuan.model.individual.ScAppQryRequest;
import com.cmcc.vrp.boss.sichuan.model.individual.ScAppQryResponse;
import com.cmcc.vrp.boss.sichuan.model.individual.ScFlowChgCfmRequest;
import com.cmcc.vrp.boss.sichuan.model.individual.ScFlowChgCfmResponse;
import com.cmcc.vrp.boss.sichuan.service.IndividualBossService;
import com.cmcc.vrp.boss.sichuan.service.ScAppQryService;
import com.cmcc.vrp.boss.sichuan.service.ScFlowChgCfmService;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.service.IndividualProductService;

/**
 * 个人业务BOSS服务类
 *
 * @author wujiamin
 * @date 2016年10月9日上午11:05:56
 */
@Service("individualBossService")
public class IndividualBossServiceImpl implements IndividualBossService {
    private static Logger logger = Logger.getLogger(IndividualBossServiceImpl.class);
    @Autowired
    ScAppQryService scAppQryService;
    @Autowired
    ScFlowChgCfmService scFlowChgCfmService;
    @Autowired
    IndividualProductService individualProductService;

    
    /** 
     * @Title: queryFlow 
     * @param mobile
     * @param result
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    @Override
    public Boolean queryFlow(String mobile, ScAppQryResponse result) {
        ScAppQryRequest request = new ScAppQryRequest();
        request.setPhoneNo(mobile);
        result = scAppQryService.sendRequest(request);
        if (result != null) {
            return true;
        }

        return false;
    }

    /** 
     * @Title: changeBossFlowcoin 
     * @param mobile
     * @param count
     * @param accountRecordType
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    @Override
    public Boolean changeBossFlowcoin(String mobile, Long count, Integer accountRecordType, String systemSerial) {
        /* Random random = new Random();
        Boolean result = random.nextBoolean();*/
        
        ScFlowChgCfmRequest request = new ScFlowChgCfmRequest();
        if(accountRecordType.equals((int)AccountRecordType.INCOME.getValue())){
            request.setOpType("1");//增加流量币，boss操作编码为1
        }
        if(accountRecordType.equals((int)AccountRecordType.OUTGO.getValue())){
            request.setOpType("2");//减少流量币，boss操作编码为1
        }
        request.setOpCode("1796");
        request.setUseFlow(count.toString());

        request.setUseCost("0");
        
        ScFlowChgCfmResponse response = scFlowChgCfmService.sendRequest(request);
        return "0000000".equals(response.getResCode());
        
        /*Boolean result = true;
        logger.info("变更boss流量币余额，boss返回" + result);
        return result;*/
        //return true;
    }

    /** 
     * @Title: changeBossPhoneFare 
     * @param mobile
     * @param price
     * @param accountRecordType
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    @Override
    public Boolean changeBossPhoneFare(String mobile, BigDecimal price, Integer accountRecordType, String systemSerial) {
      /*  Random random = new Random();
        Boolean result = random.nextBoolean();*/
        Boolean result = true;
        logger.info("变更boss话费余额，boss返回" + result);
        return result;
        //return true;
    }

    /** 
     * @Title: chargeFlow 
     * @param mobile
     * @param individualProductId
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    @Override
    public Boolean chargeFlow(String mobile, Long individualProductId, String systemSerial) {
       /* Random random = new Random();
        Boolean result = random.nextBoolean();*/
               
        ScFlowChgCfmRequest request = new ScFlowChgCfmRequest();
        request.setOpType("2");
        request.setOpCode("1798");
        IndividualProduct flowcoinProduct = individualProductService.getFlowcoinProduct();
        IndividualProduct exchangeProduct = individualProductService.selectByPrimaryId(individualProductId);
        Integer d = exchangeProduct.getPrice()/flowcoinProduct.getPrice();
        request.setUseFlow(d.toString());
        
        Long productSize = exchangeProduct.getProductSize()/1024;
        request.setUseCost(productSize.toString());
        
        ScFlowChgCfmResponse response = scFlowChgCfmService.sendRequest(request);
        return "0000000".equals(response.getResCode());
        
        
        /*Boolean result = true;
        logger.info("变更boss流量，boss返回" + result);
        return result;*/
        //return true;
    }

}
