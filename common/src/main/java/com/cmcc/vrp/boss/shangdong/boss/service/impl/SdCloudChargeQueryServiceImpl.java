package com.cmcc.vrp.boss.shangdong.boss.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.shangdong.boss.service.SdCloudChargeQueryService;
import com.cmcc.vrp.boss.shangdong.boss.service.SdCloudWebserviceImpl;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.cache.AbstractCacheSupport;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.vrp.util.DateUtil;

/**
 * 山东云平台查询充值结果服务类
 *
 */
@Service("sdCloudChargeQueryService")
public class SdCloudChargeQueryServiceImpl extends AbstractCacheSupport
    implements SdCloudChargeQueryService {
    
    @Autowired
    ChargeRecordService chargeRecordService;
    
    @Autowired
    SupplierService supplierService;
    
    @Autowired
    SupplierProductService supplierProductService;
    
    @Autowired
    SdCloudWebserviceImpl sdCloudWebserviceImpl;
    
    @Autowired
    SerialNumService serialNumService;
    
    //正在查询BOSS的key值flag
    protected final static String FLAG_KEY_SYSTEMNUN = "flag.systemNun.";

    //查询最小时间
    protected final static int MINIMUM_MINUTE_TIME =30; 
    
    /**
     * 通过系统流水号查询
     */
    @Override
    public ChargeRecord queryStatusBySystemNun(String systemNun) {
        ChargeRecord record = chargeRecordService.getRecordBySN(systemNun); 
        updateResultFromBoss(record);
        return record;
    }

    /**
    * 通过企业的Id和流水号查询
    */
    @Override
    public List<ChargeRecord> queryStatusBySerialNum(Long enterId,
            String serialNum) {
        List<ChargeRecord> listRecords = chargeRecordService.selectRecordByEnterIdAndSerialNum(enterId, serialNum);
        for(ChargeRecord chargeRecord : listRecords){
            updateResultFromBoss(chargeRecord);
        }
        return listRecords;
    }

    @Override
    protected String getPrefix() {    
        return "sdCloudChargeQuery.";
    }
    
    /**
     * 查询boss并更新数据库，如果充值成功，也将ChargeRecord的状态更新
     * 注：采用缓存来实现同一时段只有一笔查询记录
     */
    protected boolean updateResultFromBoss(ChargeRecord record){
        if(record == null || record.getStatus() == null || record.getBossChargeTime() == null ||
                ChargeRecordStatus.COMPLETE.getCode().equals(record.getStatus()) || 
                ChargeRecordStatus.FAILED.getCode().equals(record.getStatus())) {
            return true;//数据库不存在或状态已为确定状态
        }else if(DateUtil.getDateAfterMinutes(record.getBossChargeTime(),MINIMUM_MINUTE_TIME).after(new Date())){
            return true;//充值时间+30分钟  早于  当前时间
        }else{//充值30分钟后，从boss更新结果

            /**
             * 加上分布式的并发控制标签，得到结果失败直接返回,得到结果成功更新数据库并返回
             * 使用redis的setNx进行处理
             * 为防止获取到标签后宕机和操作完成后删除标签失败，设置超时时间
             * 
             */
            if(!cacheService.setNxAndExpireTime(FLAG_KEY_SYSTEMNUN + record.getSystemNum(), "1", 60)){
                return false;
            }
            
            //如果从boss查询到充值结果成功，更新ChargeRecord并更新数据库
            if(getChargeSuccessFromBoss(record)){
                record.setStatus(ChargeRecordStatus.COMPLETE.getCode());
                record.setErrorMessage(ChargeRecordStatus.COMPLETE.getMessage());
                updateRecordStatus(record);
            }
    
            /**
             * 去除掉并发控制的标签
             */
            cacheService.delete(FLAG_KEY_SYSTEMNUN + record.getSystemNum());
            return true;
        }
    }
    
    /**
     * 解析数据库结构，得到需要发送boss数据，然后发送并收到返回结果
     */
    protected boolean getChargeSuccessFromBoss(ChargeRecord chargeRecord){
        if(chargeRecord == null || chargeRecord.getSupplierProductId() ==null){
            return false;
        }
        
        /**
         * 通过系统流水号查询boss流水号
         */
        SerialNum serialNum = serialNumService.getByPltSerialNum(chargeRecord.getSystemNum());
        if(serialNum ==null || StringUtils.isBlank(serialNum.getBossReqSerialNum())){
            return false;
        }
        
        /**
         * 查询供应商产品，得到查询boss需要的参数productId & enterpriseId &userId
         * 
         */
        SupplierProduct supplierProduct = supplierProductService.selectById(chargeRecord.getSupplierProductId());
        if(supplierProduct ==null || StringUtils.isEmpty(supplierProduct.getName()) || StringUtils.isEmpty(supplierProduct.getFeature())){
            return false;
        }
        
        String productId = null;
        String enterpriseId = null;
        try{
            /**
             * 山东数据库supplier_product表feature结构为
             * {"opr":"order","type":"UPDATE","seq":"531BIPCP00220160725171811422108","productID":"1087","customerType":"01",
             * "customerID":"5318069143431","oprCode":"03","customerName":"信息化开发及运营中心","enterpriseId":"39352","discount":"10",
             * "userID":"5318068907147","oldBizId":"","oldSpId":"","bizId":"108707","spId":"472528","chargingType":"01"}
             * 
             * name为userId
             * 
             */
            JSONObject jo = JSONObject.parseObject(supplierProduct.getFeature());
            if(jo == null){
                return false;
            }
            productId = jo.getString("productID");
            enterpriseId = jo.getString("enterpriseId");
          
            return sdCloudWebserviceImpl.getQueryChargeSuccess(productId, enterpriseId, 
                    serialNum.getBossReqSerialNum(), supplierProduct.getName());
            
        }catch (JSONException e) {
            return false;
        }
    }
    

    /**
     * 更新充值状态到数据库
     */
    private boolean updateRecordStatus(ChargeRecord record){
        return chargeRecordService.updateStatus(record.getId(), ChargeRecordStatus.fromValue(record.getStatus()), record.getErrorMessage());
    }

}
