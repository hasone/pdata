package com.cmcc.vrp.province.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.BossQueryResult;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.cache.AbstractCacheSupport;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Supplier;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.BaseBossQuery;
import com.cmcc.vrp.province.service.ChargeRecordQueryService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.SupplierService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 根据查询数据库充值状态，状态为不确定状态的，查询boss，并将结果更新数据库以及储存到缓存中
 * 
 * 本类boss查询实现查从boss查询结果并更新数据库及退款，boss实现接口为BaseBossQuery.java,通过查找供应商fingleprint找到各省实现类，如没有找到则直接返回数据库结果
 * 若出现某省已实现该类BaseBossQuery，却不想在这里使用，请在依赖注入使用类EmptyChargeRecordQueryServiceImpl
 * 
 * boss查询示例请见 SdCloudBossQueryServiceImpl
 *
 */
@Service("chargeRecordQueryService")
public class ChargeRecordQueryServiceImpl extends AbstractCacheSupport
        implements ChargeRecordQueryService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ChargeRecordQueryServiceImpl.class);
    
    @Autowired
    ChargeRecordService chargeRecordService;
    
    @Autowired
    SupplierService supplierService;
    
    @Autowired
    SupplierProductService supplierProductService;
    
    @Autowired
    AccountService accountService;
    
    //存在缓存的key值
    protected final static String CACHE_KEY_SYSTEMNUN = "cache.systemNun.";
    
    //存在缓存的key值
    protected final static String CACHE_KEY_SERIALNUM = "cache.serialNum.";
    
    //正在查询BOSS的key值flag
    protected final static String FLAG_KEY_SYSTEMNUN = "flag.systemNun.";

    
    /**
     * 为ChargeRecordController服务
     */
    @Override
    public ChargeRecord queryStatusBySystemNun(String systemNun) {

        String cacheValue = cacheService.get(CACHE_KEY_SYSTEMNUN + systemNun);
        ChargeRecord record = null;
       
        try{
            record = new Gson().fromJson(cacheValue, ChargeRecord.class);
        }catch(JsonSyntaxException e){
            LOGGER.error(e.getMessage());
        }
         
        if(record == null){//若缓存没有，1.则从数据库取记录，2.若状态不为3或4则查询boss
            record = chargeRecordService.getRecordBySN(systemNun);
            
            if(record != null && updateResultFromBoss(record)){//数据库存在且更新boss操作完成，才加入到缓存中
                cacheService.add(CACHE_KEY_SYSTEMNUN + systemNun, new Gson().toJson(record));
            }
            
        }
        
        return record;
    }
    
    /**
     * 为ChargeResultController服务
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ChargeRecord> queryStatusBySerialNum(
            Long enterId , String serialNum) {
        String cacheValue = cacheService.get(CACHE_KEY_SERIALNUM + serialNum);
        List<ChargeRecord> listRecords = null;
        
        try{
            listRecords = new Gson().fromJson(cacheValue, List.class);
        }catch(JsonSyntaxException e){
            LOGGER.error(e.getMessage());
        }
        
        if(listRecords == null){
            listRecords = chargeRecordService.selectRecordByEnterIdAndSerialNum(enterId, serialNum);
            
//            boolean canSaveToCache = true;
//            
//            for(ChargeRecord record : listRecords){
//                if(!updateResultFromBoss(record)){
//                    canSaveToCache = false;//有一条记录正在从boss更新，则不会将结果写入缓存
//                }
//            }
//            
//            if(!listRecords.isEmpty() && canSaveToCache){
//                cacheService.add(CACHE_KEY_SERIALNUM + serialNum, new Gson().toJson(listRecords));
//            }
        }
             
        return listRecords;
    }
    
    /**
     * 得到boss查询类，以及调用该类的queryStatus方法
     */
    protected BossQueryResult queryResultFromBoss(String systemNum,String fingerPrint){
        BaseBossQuery bossQuery = chooseBossQuery(fingerPrint);
        if(bossQuery == null){
            return null;
        }
        //return BossQueryResult.PROCESSING;
        return bossQuery.queryStatus(systemNum);
    }
    
    
    
    /**
     * 从boss查询结果并更新record对象的充值状态和返回信息，并更新到数据库,同时将入参record对象的状态更新
     * （不需要更新)或 (从boss查询到结果且更新完成) 返回true，其它返回false
     */
    protected boolean updateResultFromBoss(ChargeRecord record){
        BossQueryResult queryResult = null;       
        if(record == null || record.getStatus() == null || 
                ChargeRecordStatus.COMPLETE.getCode().equals(record.getStatus()) || 
                ChargeRecordStatus.FAILED.getCode().equals(record.getStatus())) {
            return true;//数据库不存在或状态已为确定状态
        }else{
            /**
             * 目标，获取到供应商的Fingerprint
             */
            SupplierProduct supplierproduct = supplierProductService.selectById(record.getSupplierProductId());
            if(supplierproduct == null){
                return false;
            }
            
            Supplier supplier = supplierService.get(supplierproduct.getSupplierId());
            if(supplier == null){
                return false;
            }
            
            /**
             * 加上分布式的并发控制标签，得到结果失败直接返回
             * 使用redis的setNx进行处理
             * 为防止获取到标签后宕机和操作完成后删除标签失败，设置超时时间
             * 
             */
            if(!cacheService.setNxAndExpireTime(FLAG_KEY_SYSTEMNUN + record.getSystemNum(), "1", 300)){
                return false;
            }
            
            queryResult = queryResultFromBoss(record.getSystemNum(),supplier.getFingerprint());
                        
            if(queryResult !=null){
                if (queryResult.equals(BossQueryResult.SUCCESS)) {
                    record.setStatus(ChargeRecordStatus.COMPLETE.getCode());
                } else if (queryResult.equals(BossQueryResult.FAILD)){
                    refund(record);//尝试退款，内部已判断是否需要退款
                    record.setStatus(ChargeRecordStatus.FAILED.getCode());
                } else {
                    cacheService.delete(FLAG_KEY_SYSTEMNUN + record.getSystemNum());
                    return false;
                }
                record.setErrorMessage(queryResult.getMsg());
                record.setChargeTime(new Date());    
                
                updateRecordStatus(record);    
            } else {
                cacheService.delete(FLAG_KEY_SYSTEMNUN + record.getSystemNum());
                return false;
            }
            
            /**
             * 去除掉并发控制的标签
             */
            cacheService.delete(FLAG_KEY_SYSTEMNUN + record.getSystemNum());
            return true;
        }
    }
    
    
    
    /**
     * 更新充值状态到数据库
     */
    private boolean updateRecordStatus(ChargeRecord record){
        return chargeRecordService.updateStatus(record.getId(), ChargeRecordStatus.fromValue(record.getStatus()), record.getErrorMessage());
    }
    
    /**
     * 根据指纹来查询相应的查询服务
     *
     * @param fingerPrint
     * @return
     */
    protected BaseBossQuery chooseBossQuery(String fingerPrint) {
        List<BaseBossQuery> bossQueries = new LinkedList<BaseBossQuery>(applicationContext.getBeansOfType(BaseBossQuery.class).values());
        for (BaseBossQuery bossQuery : bossQueries) {
            if (fingerPrint.equals(bossQuery.getFingerPrint())) {
                return bossQuery;
            }
        }
        return null;
    }
    
    /**
     * 该方法参考AsyncCallbackServiceImpl的proccess方法
     * @param chargeRecord
     */
    protected boolean refund(ChargeRecord chargeRecord){

        //2只有在当前的充值状态不为失败, 而回调状态为充值失败时，才需要进行退款！
        boolean needRefund = !chargeRecord.getStatus().equals(ChargeRecordStatus.FAILED.getCode());

        if (!needRefund) {
            LOGGER.info("当前的充值状态已经为充值失败，不会再次退款！SystemNum = {}.", chargeRecord.getSystemNum());
        }

        //3如果充值失败了,要给企业退钱
        if (needRefund
            && !accountService.returnFunds(chargeRecord.getSystemNum())) {
            LOGGER.error("充值失败给企业退款时失败. SystemNum = {}，EntId = {}.", chargeRecord.getSystemNum(), chargeRecord.getEnterId());
            return false;
        }
        
        return true;
    }
    

    @Override
    protected String getPrefix() {
        return "chargeRecord.";
    }
   
}
