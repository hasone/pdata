package com.cmcc.vrp.boss.shangdong.boss.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossQueryResult;
import com.cmcc.vrp.boss.shangdong.boss.service.SdCloudWebserviceImpl;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.BaseBossQuery;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;

/**
 * 从云平台查询充值状态的接口
 * TODO 缺少云平台对中间状态的定义
 *
 */
@Service("sdCloudBossQueryServiceImpl")
public class SdCloudBossQueryServiceImpl implements BaseBossQuery {

    @Autowired
    SdCloudWebserviceImpl sdCloudBossService;
    
    @Autowired
    ChargeRecordService chargeRecordService;
    
    @Autowired
    SerialNumService serialNumService;
    
    @Autowired
    SupplierProductService supplierProductService;
    
    @Override
    public BossOperationResult queryStatusAndMsg(String systemNum) {
        ChargeRecord chargeRecord = chargeRecordService.getRecordBySN(systemNum);
        if(chargeRecord == null || chargeRecord.getSupplierProductId() ==null){
            return null;
        }
        
        /**
         * 通过系统流水号查询boss流水号
         */
        SerialNum serialNum = serialNumService.getByPltSerialNum(systemNum);
        if(serialNum ==null || serialNum.getBossReqSerialNum() ==null){
            return null;
        }
        
        /**
         * 查询供应商产品，得到查询boss需要的参数productId & enterpriseId
         */
        SupplierProduct supplierProduct = supplierProductService.selectById(chargeRecord.getSupplierProductId());
        if(supplierProduct ==null || StringUtils.isEmpty(supplierProduct.getName()) || StringUtils.isEmpty(supplierProduct.getFeature())){
            return null;
        }
        
        String productId = null;
        String enterpriseId = null;
        try{
            JSONObject jo = JSONObject.parseObject(supplierProduct.getFeature());
            productId = jo.get("productID").toString();
            enterpriseId = jo.get("enterpriseId").toString();
            
        }catch(NullPointerException e){
            return null;
        }
        
        BossOperationResult result = sdCloudBossService.getMemCurrentInstance(productId,
                enterpriseId,serialNum.getBossReqSerialNum(), supplierProduct.getName());
        //TODO 需要增加中间状态的判断
        
        return result;
    }
    
    
    @Override
    public BossQueryResult queryStatus(String systemNum) {
        return null;
    }

    @Override
    public String getFingerPrint() {
        return "shandongcloud";
    }

    

}
