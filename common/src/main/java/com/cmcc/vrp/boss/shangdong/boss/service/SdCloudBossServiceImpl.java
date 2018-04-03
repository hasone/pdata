package com.cmcc.vrp.boss.shangdong.boss.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.shangdong.boss.enums.ShandongPrdEnum;
import com.cmcc.vrp.boss.shangdong.boss.model.ProductMemberFlowSystemPay;
import com.cmcc.vrp.boss.shangdong.boss.model.SdSerialNumGenerator;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * Created by qihang on 2016/11/09.
 * <p>
 * 该BossService是对接山东云平台，FingerPrint为shandongcloud
 */
@Service
public class SdCloudBossServiceImpl implements BossService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SdCloudBossServiceImpl.class);

    private static final String FINGERPRINT = "shandongcloud";

    /**
     * 云平台默认的充值账期为1
     */
    private static final String DEFAULT_CYCLE = "1";
    
    /**
     * global_config中设置value为异步的值
     */
    private static final String IS_ASYC_VALUE = "on";

    @Autowired
    private SupplierProductService sProductService;

    @Autowired
    private SdCloudWebserviceImpl sdCloudWebservice;

    @Autowired
    private GlobalConfigService globalConfigService;
    
    @Autowired
    private SerialNumService serialNumService;
    
    @Autowired
    private SdSerialNumGenerator serialGenerator;
    
    @Autowired
    private ChargeRecordService chargeRecordService;

    /**
     * ShandongCloudDeliverRuleByBoss 更改，将splPid替换为了prdId
     */
    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile,
                                      String serialNum, Long prdSize) {
        LOGGER.info("连接山东云平台");

        SupplierProduct sPrdouct;
        
        ChargeRecord chargeRecord;//为了填写充值账期，包月赠送使用
        
        //是否异步接口
        boolean isAsyc = isAsyc();
          

        if (entId == null
                || splPid == null
                || !com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)
                || StringUtils.isBlank(serialNum)
                || (sPrdouct = sProductService.selectById(splPid)) == null
                || (chargeRecord = chargeRecordService.getRecordBySN(serialNum)) == null) {
            LOGGER.error("Shandong charge requset Para is error");
            return SdCloudOperationResultImpl.initParamErrResult(isAsyc);
        }

        //取出SupplierProduct，进行处理，设置ProductMemberFlowSystemPay对象
        ProductMemberFlowSystemPay pm = fillingPm(sPrdouct, mobile, prdSize,chargeRecord.getCount());

        if (pm == null) {
            LOGGER.error("Shandong charge requset Para is error");
            return SdCloudOperationResultImpl.initParamErrResult(isAsyc);
        }
        
        //山东异步回调增加的信息，1.16版本使用,
        if(isAsyc()){
            pm.setPkgSeq(serialGenerator.getSdSerialNum());
            if(!updateSerialNum(serialNum, null, pm.getPkgSeq())){
                LOGGER.error("系列号更新到数据库失败，serialNum={},reqSeq={}",serialNum,pm.getPkgSeq());
                return SdCloudOperationResultImpl.initParamErrResult(isAsyc);
            }
        }
        
        return sdCloudWebservice.updateProductMemberFlowSystemPay(pm,isAsyc);
    }

    /**
     * 填充充值类
     */
    private ProductMemberFlowSystemPay fillingPm(SupplierProduct sPrdouct, String mobile, Long prdSize,Integer count){
        ProductMemberFlowSystemPay pm = new ProductMemberFlowSystemPay();
        String jsonStr = sPrdouct.getFeature();
        
        try {
            JSONObject jo = JSONObject.parseObject(jsonStr);

            //企业订购产品后产生的一个用户
            pm.setUserID(jo.get("userID").toString());

            pm.setPhone(mobile);

            //产品标识
            pm.setProductID(jo.get("productID").toString());

            //企业id
            pm.setEnterpriseID(jo.get("enterpriseId").toString());

            //增值产品id（规格id）
            pm.setBizId(jo.get("bizId").toString());

            pm.setFeeType(getFeeType());

            pm.setFactFee(getFactFee());
            
            if(ShandongPrdEnum.needUseFlowSize(sPrdouct.getCode())){
                pm.setLimitFlow(String.valueOf(prdSize / 1024)); //当前boss以MB为单位传递 
            } else { //设置的默认值，当前为0
                pm.setLimitFlow(getLimitFlow());
            }

            pm.setItemId(getItemId());

            pm.setStatus(getStatus());

            // pm.setCycle(DEFAULT_CYCLE);//账期,当前默认1,之后有流量池会传大小
            
            pm.setCycle((count == null || count<=0) ? DEFAULT_CYCLE:Integer.toString(count));//账期,当前默认1,之后有流量池会传大小

        } catch (JSONException e) {
            LOGGER.error("JSONException e:" + e.getMessage());
            return null;

        } catch (NullPointerException e) {
            LOGGER.error("NullPointerException e:" + e.getMessage());
            return null;
        }

        return pm;
    }
 
    /**
     * 得到fingerprint
     */
    @Override
    public String getFingerPrint() {
        return FINGERPRINT;
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }
    
    private boolean updateSerialNum(String serialNum, String bossRespNum, String bossReqNum) {
        if (updateRecord(serialNum, bossRespNum, bossReqNum)) {
            LOGGER.info("记录平台流水和BOSS返回流水号的关系成功");
            return true;
        } else {
            LOGGER.error("记录平台流水和BOSS返回流水号的关系失败");
            return false;
        }
    }
    
    private boolean updateRecord(String systemNum, String bossRespNum, String bossReqNum) {
        if (StringUtils.isBlank(systemNum)
                || serialNumService.getByPltSerialNum(systemNum) == null) {
            return false;
        }
        SerialNum serialNum = new SerialNum();
        serialNum.setBossReqSerialNum(bossReqNum);
        serialNum.setBossRespSerialNum(bossRespNum);
        serialNum.setPlatformSerialNum(systemNum);
        return serialNumService.updateSerial(serialNum);
    }
    

    /**
     * 从数据库得到默认参数
     *
     * @return
     */
    public String getFeeType() {
        return globalConfigService.get(GlobalConfigKeyEnum.SD_BOSS_FEETYPE.getKey());
    }

    /**
     * 从数据库得到默认参数
     *
     * @return
     */
    public String getFactFee() {
        return globalConfigService.get(GlobalConfigKeyEnum.SD_BOSS_FACTFEE.getKey());
    }

    /**
     * 从数据库得到默认参数
     *
     * @return
     */
    public String getLimitFlow() {
        return globalConfigService.get(GlobalConfigKeyEnum.SD_BOSS_LIMITFLOW.getKey());
    }

    /**
     * 从数据库得到默认参数
     *
     * @return
     */
    public String getItemId() {
        return globalConfigService.get(GlobalConfigKeyEnum.SD_BOSS_ITEMID.getKey());
    }

    /**
     * 从数据库得到默认参数
     *
     * @return
     */
    public String getStatus() {
        return globalConfigService.get(GlobalConfigKeyEnum.SD_BOSS_STATUS.getKey());
    }

    /**
     * 从数据库得到山东默认是同步还是异步，当前默认是同步
     */
    public boolean isAsyc(){
        return globalConfigService.get(GlobalConfigKeyEnum.SD_CHARGE_OPEN_ASYC.getKey()).equalsIgnoreCase(IS_ASYC_VALUE);
    }
}
