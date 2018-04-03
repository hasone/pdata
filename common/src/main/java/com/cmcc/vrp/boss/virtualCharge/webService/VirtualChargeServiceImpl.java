package com.cmcc.vrp.boss.virtualCharge.webService;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.virtualCharge.model.VirtualChargeReturnCode;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.PresentRecord;
import com.cmcc.vrp.province.model.PresentRule;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.PresentRecordService;
import com.cmcc.vrp.province.service.PresentRuleService;
import com.cmcc.vrp.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by qinqinyan on 2017/5/9.
 * 流量币虚拟充值实现类
 */
@org.springframework.stereotype.Service
public class VirtualChargeServiceImpl implements BossService{

    private static final Logger LOGGER = LoggerFactory.getLogger(VirtualChargeServiceImpl.class);

    @Autowired
    IndividualProductService individualProductService;
    @Autowired
    IndividualAccountService individualAccountService;
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    PresentRecordService presentRecordService;
    @Autowired
    PresentRuleService presentRuleService;

    public VirtualChargeServiceImpl() {
        super();
    }
    
    /**
     * 兼容普通赠送和营销活动充值
     * 由于到这里时不携带充值原因，即哪个活动的充值
     * 所以只能挨个查
     * */
    //流量币虚拟充值，在业务上其实是个人积分账户的充值，在广东我们定义的个人产品是积分，流量币目前只有四川
    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("企业 endId = {}, 供应商 splPid = {}, 充值手机号 mobile = {}, 平台的充值序列号 = {}, 充值大小 prdSize = {}",
            entId, splPid, mobile, serialNum, prdSize);
        
        if(entId==null || splPid==null || !StringUtils.isValidMobile(mobile) 
                || StringUtils.isEmpty(serialNum) || prdSize==null){
            return new VirtualChargeBossOperationResultImpl(VirtualChargeReturnCode.PARA_ILLEGALITY);
        }
        
        ActivityWinRecord activityWinRecord = activityWinRecordService.selectByRecordId(serialNum);
        PresentRecord presentRecord = null;
        String description = "";
        ActivityType activityType = null;
        
        if(activityWinRecord==null){
            //不属于营销活动充值，需要查找是否是普通赠送充值
            presentRecord = presentRecordService.selectBySysSerialNum(serialNum);
            if(presentRecord == null){
                return new VirtualChargeBossOperationResultImpl(VirtualChargeReturnCode.PARA_ILLEGALITY);
            }else{
                PresentRule presentRule = presentRuleService.selectByPrimaryKey(presentRecord.getRuleId());
                if(presentRule==null){
                    return new VirtualChargeBossOperationResultImpl(VirtualChargeReturnCode.PARA_ILLEGALITY);
                }
                LOGGER.info("普通赠送,充值流水号 = {}, ruleId = {}", serialNum, presentRecord.getRuleId());
                description = "普通赠送-"+presentRule.getaName();
                activityType = ActivityType.GIVE;
            }
        }else{
            Activities activities = activitiesService.selectByActivityId(activityWinRecord.getActivityId());
            if(activities==null){
                LOGGER.info("获取活动信息为空,活动activtyId = {}", activityWinRecord.getActivityId());
                return new VirtualChargeBossOperationResultImpl(VirtualChargeReturnCode.PARA_ILLEGALITY);
            }
            
            LOGGER.info("营销活动,充值流水号 = {}, 活动id = {}", serialNum, activityWinRecord.getActivityId());
            description = ActivityType.fromValue(activities.getType()).getname() +"-"+activities.getName();
            activityType = ActivityType.fromValue(activities.getType());
        }

        IndividualProduct individualProduct = individualProductService.getIndivialPointProduct();
        if(individualProduct==null){
            LOGGER.info("获取积分产品为空,请查看individual_product这张表");
            return new VirtualChargeBossOperationResultImpl(VirtualChargeReturnCode.PARA_ILLEGALITY);
        }
        

        //开始给个人账户充值
        if(individualAccountService.addCountForcely(mobile, individualProduct.getId(), serialNum, BigDecimal.valueOf(prdSize),
                activityType, description)){
            LOGGER.info("成功给用户 mobile = {} 充值。充值金额 prdSize = {}, 充值流水 serialNum = {}, 企业 entId = {}, 活动  - {}",
                mobile, prdSize, serialNum, entId, description);
            return new VirtualChargeBossOperationResultImpl(VirtualChargeReturnCode.SUCCESS);
        }
        LOGGER.info("给用户 mobile = {} 充值失败。充值金额 prdSize = {}, 充值流水 serialNum = {}, 企业 entId = {}, 活动  - {}",
            mobile, prdSize, serialNum, entId, description);
        return new VirtualChargeBossOperationResultImpl(VirtualChargeReturnCode.FAILD);
    }

    @Override
    public String getFingerPrint() {
        return "virtualcharge";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }
}
