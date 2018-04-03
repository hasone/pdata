package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.charge.ChargeService;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ActivityWinRecordStatus;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.EnterpriseStatus;
import com.cmcc.vrp.enums.FinanceStatus;
import com.cmcc.vrp.enums.IspType;
import com.cmcc.vrp.enums.ProductStatus;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.CallbackPojo;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityTemplateService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.CallbackService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ActivitiesWinPojo;
import com.cmcc.vrp.util.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Formatter;

/**
 * Created by sunyiwei on 16-3-29.
 * edit by qinqinyan on 16-11-25
 */
@Service
public class CallbackServiceImpl implements CallbackService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackServiceImpl.class);

    @Autowired
    ChargeService chargeService;
    @Autowired
    ChargeRecordService chargeRecordService;
    @Autowired
    ProductService productService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    TaskProducer producer;
    @Autowired
    SerialNumService serialNumService;
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    AdministerService administerService;
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    @Autowired
    IndividualAccountService individualAccountService;
    @Autowired
    AccountService accountService;
    @Autowired
    EntProductService entProductService;
    @Autowired
    ActivityInfoService activityInfoService;
    @Autowired
    ActivityTemplateService activityTemplateService;

    @Override
    public boolean callback(CallbackPojo pojo, String serialNum) {
        LOGGER.info("callbackPojo= " + pojo.toString());

        //校验手机号是否符合要求
        if(activityTemplateService.invalidMobile(pojo.getMobile())){
            LOGGER.error("mobile = {} 不满足奖品配置的要求", pojo.getMobile());
            return false;
        }

        //0.1、校验企业状态
        Long entId = NumberUtils.toLong(pojo.getEnterId(), -1);
        Enterprise enter = enterprisesService.selectById(entId);
        if (!(enter.getStatus().toString().equals("3") 
                && enter.getDeleteFlag().toString().equals(EnterpriseStatus.NORMAL.getCode().toString()))) {
            LOGGER.error("企业异常，EntId = {}.", pojo.getEnterId());
            return false;
        }

        //0.2、获取产品
        Long prdId = NumberUtils.toLong(pojo.getPrizeId(), -1);
        Product product = productService.get(prdId);
        if (product == null) {
            LOGGER.error("无法根据产品ID获取产品信息, PrdCode = {}.", pojo.getPrizeId());
            return false;
        }

        //0.3 校验企业与产品关系
        if (!validateEntProduct(prdId, entId)) {
            LOGGER.error("无法查询企业与产品的关联关系, entId = {}, PrdId = {}.", entId, prdId);
            return false;
        }
        
        
        Activities activities = activitiesService.selectByActivityId(pojo.getActiveId());
        ActivityType activityType = ActivityType.fromValue(activities.getType());
        /**
         * 拼手气红包要在这里虚拟一个产品出来替换掉pojo中prizeId.
         * 在这里替换的原因是：已经校验过原生产品与企业关联关系，所以可以愉快地虚拟一个出来了
         * */
        //实际使用的产品id
        Long realUseProdId = prdId;
        if(activities.getType().toString().equals(ActivityType.LUCKY_REDPACKET.getCode().toString())){
            realUseProdId = getVirtualProductForLuckyRedpacket(pojo, product);
            LOGGER.info("拼手气红包，将中奖返回的pojo中的Prizeid = {} 替换成实际使用的 productId = {}", pojo.getPrizeId(), realUseProdId);
            pojo.setPrizeId(realUseProdId.toString());
        }
        
        //1. 扣钱
        if (!minusCount(entId, realUseProdId, pojo.getMobile(), serialNum)) {
            LOGGER.error("扣减账户余额失败. EntId = {}, PrdId = {}, Mobile = {}, serialNum = {}.", pojo.getEnterId(),
                pojo.getPrizeId(), pojo.getMobile(), serialNum);
            return false;
        }

        //2.生成中奖记录
        if (insertInitialRecords(pojo, serialNum)) {
            ChargeRecord cr = chargeRecordService.getRecordBySN(serialNum);
            //2.加入充值队列
            if (!charge(pojo, serialNum)) {//加入充值队列失败
                LOGGER.info("加入充值队列失败。。。。。。。");  
                Date updateChargeTime = new Date();
                Integer financeStatus = null;
                
                //退款
                //ActivityType activityType = ActivityType.fromValue(activities.getType());
                if (!accountService.returnFunds(serialNum, activityType, Long.parseLong(String.valueOf(pojo.getPrizeId())),
                        1)) {
                    LOGGER.error("退款失败");
                }else{
                    financeStatus = FinanceStatus.IN.getCode();
                }
                
                //3.失败，更新充值记录状态
                changeActivityWinStatus(serialNum, ChargeResult.ChargeMsgCode.refuseToEnterQueue.getCode(),
                    ActivityWinRecordStatus.FALURE.getCode());
                if (!chargeRecordService.updateStatusAndStatusCode(cr.getId(),
                        ChargeResult.ChargeMsgCode.refuseToEnterQueue.getCode(), ChargeRecordStatus.FAILED.getCode(),
                        ChargeRecordStatus.FAILED.getMessage(), financeStatus, updateChargeTime)) {
                    LOGGER.error("更新充值记录失败, id = {}.", cr.getId());
                }
                return false;
            } else {//加入充值队列成功
                LOGGER.info("加入充值队列成功。。。。。。。");
                //3.成功，更新充值记录状态
                if (changeActivityWinStatus(serialNum, ChargeResult.ChargeMsgCode.businessQueue.getCode(),
                    ActivityWinRecordStatus.WAIT.getCode())
                    && chargeRecordService.updateStatusCode(cr.getId(),
                    ChargeResult.ChargeMsgCode.businessQueue.getCode())) {
                    LOGGER.info("加入业务队列成功, 状态码={}.", ChargeResult.ChargeMsgCode.businessQueue.getCode());

                    //如果是拼手气红包，更新分发数量和流量大小
                    if(activities.getType().toString().equals(ActivityType.LUCKY_REDPACKET.getCode().toString())){
                        ActivityInfo activityInfo = activityInfoService.selectByActivityId(pojo.getActiveId());
                        ActivityInfo updateActivityInfo = new ActivityInfo();
                        updateActivityInfo.setActivityId(pojo.getActiveId());

                        //数据库采用累加方式增加，与账户一样
                        updateActivityInfo.setGivedUserCount(1L);
                        updateActivityInfo.setUsedProductSize(pojo.getPrizeCount().longValue()*1024);
                        updateActivityInfo.setUpdateTime(new Date());
                        if(!activityInfoService.updateForRendomPacket(updateActivityInfo)){
                            LOGGER.info("更新随机红包 = {} 的分发用户数={}, 增加分发产品大小 = {}KB失败", pojo.getActiveId(), activityInfo.getGivedUserCount().longValue()+1,
                                pojo.getPrizeCount());
                        }
                    }
                    return true;
                }
                return false;
            }
        } else {
            //Activities activities = activitiesService.selectByActivityId(pojo.getActiveId());

            if (!accountService.returnFunds(serialNum, activityType, Long.parseLong(String.valueOf(pojo.getPrizeId())),
                1)) {
                LOGGER.error("退款失败");
                return false;
            }
            return false;
        }
    }
    
    /**
     * 对随机红包中奖纪录替换成符合其中奖大小的的虚拟产品
     * */
    private Long getVirtualProductForLuckyRedpacket(CallbackPojo pojo, Product product){
        
        Long productSize = pojo.getPrizeCount().longValue()*1024;
        
        Product virtualProduct = productService.getPrdBySizeAndId(productSize, product.getFlowAccountProductId());
        if(virtualProduct!=null){
            //已经存在该虚拟化产品，不需要创建了
            return virtualProduct.getId();
        }else{
            Product realProduct = productService.get(product.getFlowAccountProductId());
            Product newVirtualProduct = new Product();
            newVirtualProduct.setProductCode(realProduct.getProductCode()+"-"+productSize.toString());
            newVirtualProduct.setType(1);
            newVirtualProduct.setName(realProduct.getName()+"-"+pojo.getPrizeCount().toString()+"M");
            newVirtualProduct.setStatus(ProductStatus.NORMAL.getCode());
            newVirtualProduct.setCreateTime(new Date());
            newVirtualProduct.setUpdateTime(new Date());
            newVirtualProduct.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
            newVirtualProduct.setPrice(realProduct.getPrice().intValue()*pojo.getPrizeCount().intValue());
            newVirtualProduct.setDefaultvalue(0);
            newVirtualProduct.setIsp(IspType.CMCC.getValue());
            newVirtualProduct.setOwnershipRegion(realProduct.getOwnershipRegion());
            newVirtualProduct.setRoamingRegion(realProduct.getRoamingRegion());
            newVirtualProduct.setProductSize(productSize);
            newVirtualProduct.setFlowAccountFlag(product.getFlowAccountFlag());
            newVirtualProduct.setFlowAccountProductId(realProduct.getId());
            newVirtualProduct.setConfigurableNumFlag(1);
            if(!productService.insertProduct(newVirtualProduct)){
                LOGGER.info("构造虚拟产品失败");
            }
            return newVirtualProduct.getId();
        }        
    }
    
    //虚拟产品和真实产品转化
    private Long getRealProductId(Product product){
        Long parentPrdId = null;
        if (com.cmcc.vrp.ec.bean.Constants.ProductType.FLOW_ACCOUNT.getValue() == product.getType() //流量池
            || com.cmcc.vrp.ec.bean.Constants.ProductType.MOBILE_FEE.getValue() == product.getType()
            || com.cmcc.vrp.ec.bean.Constants.ProductType.VIRTUAL_COIN.getValue() == product.getType()) { 
            parentPrdId = product.getFlowAccountProductId(); //转化成真实的产品ID
        } else {
            parentPrdId = product.getId();  //其它的为真实产品，父产品为它本身
        }
        return parentPrdId;
    }

    //校验企业与产品的关联关系
    private boolean validateEntProduct(Long prdId, Long entId) {
        if (prdId == null || entId == null) {
            return false;
        }

        Product product = productService.get(prdId);
        if (product == null) {
            return false;
        }

        //判断是否为虚拟产品
        Long parentPrdId = getRealProductId(product);
        /*if (com.cmcc.vrp.ec.bean.Constants.ProductType.FLOW_ACCOUNT.getValue() == product.getType() //流量池
            || com.cmcc.vrp.ec.bean.Constants.ProductType.MOBILE_FEE.getValue() == product.getType()) { //话费
            parentPrdId = product.getFlowAccountProductId(); //转化成真实的产品ID
        } else {
            parentPrdId = prdId;  //其它的为真实产品，父产品为它本身
        }
*/
        return parentPrdId != null && entProductService.selectByProductIDAndEnterprizeID(parentPrdId, entId) != null;
    }

    @Transactional
    private boolean insertInitialRecords(CallbackPojo pojo, String serialNum) {
        return StringUtils.isNotBlank(serialNum) && insertRecord(pojo, serialNum);
    }

    private boolean changeActivityWinStatus(String serialNum, String statusCode, Integer status) {
        LOGGER.info("start to change activity_win_record fail. recordId-{}, statusCode-{}," + "status-{}", serialNum,
            statusCode, status);
        if (StringUtils.isNotBlank(serialNum) && StringUtils.isNotBlank(statusCode) && status != null) {
            ActivityWinRecord activityWinRecord = new ActivityWinRecord();
            activityWinRecord.setRecordId(serialNum);
            activityWinRecord.setStatus(status);
            activityWinRecord.setStatusCode(statusCode);
            if (activityWinRecordService.updateByPrimaryKeySelective(activityWinRecord)) {
                LOGGER.info("succeed to change activity_win_record status");
                return true;
            }
        }
        LOGGER.info("fail to change activity_win_record status");
        return false;
    }

    /**
     * 插入中奖记录
     *
     * @param pojo
     * @param serialNum
     * @return
     * @author qinqinyan
     */
    private boolean insertRecord(CallbackPojo pojo, String serialNum) {

        //1. 插入活动记录，平台序列号
        ActivityWinRecord record = convertToActivityWinRecord(pojo, serialNum);
        LOGGER.info("start to insert activity_win_record ……");
        if (!activityWinRecordService.insertSelective(record) || !serialNumService.insert(initSerailNum(serialNum))) {
            LOGGER.info("insert activity_win_record fail, recordId-{}", serialNum);
            pojo.setRecordId(record.getId());
            return false;
        }

        //2. 插入充值记录
        ChargeRecord cr = convertToChargeRecord(record);
        LOGGER.info("start to insert charge_record ……");
        if (!chargeRecordService.create(cr)) {
            LOGGER.info("insert charge_record fail, recordId-{}", serialNum);
            return false;
        }
        
        return true;
    }

    /**
     * 构造充值记录
     *
     * @param pojo
     * @return
     * @author xujue
     **/
    private ChargeRecord convertToChargeRecord(ActivityWinRecord pojo) {

        Activities activities = activitiesService.selectByActivityId(pojo.getActivityId());
        ActivityType activityType = ActivityType.fromValue(activities.getType());

        ChargeRecord cr = new ChargeRecord();
        cr.setPrdId(pojo.getPrizeId());
        cr.setEnterId(activities.getEntId());
        cr.setTypeCode(activities.getType());
        cr.setRecordId(pojo.getId());
        cr.setStatus(ChargeRecordStatus.WAIT.getCode());
        cr.setType(activityType.getname());
        cr.setPhone(pojo.getOwnMobile());
        cr.setaName(activities.getName());
        cr.setSystemNum(pojo.getRecordId());
        cr.setChargeTime(new Date());
        return cr;
    }

    /**
     * 构造中奖记录
     *
     * @param pojo
     * @return
     * @author qinqinyan
     **/
    private ActivityWinRecord convertToActivityWinRecord(CallbackPojo pojo, String serialNum) {
        ActivityWinRecord record = new ActivityWinRecord();

        Product product = productService.selectProductById(Long.parseLong(pojo.getPrizeId()));
        if (product == null || StringUtils.isBlank(product.getIsp())) {
            return record;
        }
        record.setActivityId(pojo.getActiveId());
        record.setOwnMobile(pojo.getMobile());
        record.setChargeMobile(pojo.getMobile());
        
        //如果是虚拟产品，这个是虚拟产品的产品id
        record.setPrizeId(Long.parseLong(pojo.getPrizeId()));
        
        record.setIsp(product.getIsp());
        record.setWinTime(pojo.getCreateTime());
        record.setChargeTime(new Date());
        record.setStatus(ActivityWinRecordStatus.WAIT.getCode());
        record.setReason(ActivityWinRecordStatus.WAIT.getname());
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        record.setRecordId(serialNum);
        
        //中奖大小,可以考虑记录一下下
        record.setSize((Integer.valueOf(pojo.getPrizeCount().intValue()*1024)).toString());
        return record;
    }

    private SerialNum initSerailNum(String platformSerialNum) {
        SerialNum serialNum = new SerialNum();
        serialNum.setPlatformSerialNum(platformSerialNum);
        serialNum.setUpdateTime(new Date());
        serialNum.setCreateTime(new Date());
        serialNum.setDeleteFlag(0);
        return serialNum;
    }

    private boolean charge(CallbackPojo callbackPojo, String serialNum) {
        LOGGER.info("Start to add to activity task queue.");
        ActivitiesWinPojo taskPojo = convertToActivitiesWinPojo(callbackPojo, serialNum);
        Boolean bFlag = producer.produceActivityWinMsg(taskPojo);
        LOGGER.info("Adding to activity task returns {}.", bFlag);
        return bFlag;
    }
    /**
     * 营销服务活动类型定义：
     * 0, "红包"
     * 1, "转盘"
     * 2, "砸金蛋"
     * 3, "个人红包"
     * 4, "随机红包"
     * */
    /*private ActivityType convertToPlatFormActivityType(Integer type){
        if(type == null){
            return null;
        }
        switch (type) {
        case 0:
            return ActivityType.REDPACKET;
        case 1:
            return ActivityType.LOTTERY;
        case 2:
            return ActivityType.GOLDENBALL;
        case 3:
            //这个原始给集中化平台，现在已经不使用
            return ActivityType.COMMON_REDPACKET;
        case 4:
            return ActivityType.LUCKY_REDPACKET;
        default:
            return null;
        }
    }*/

    private ActivitiesWinPojo convertToActivitiesWinPojo(CallbackPojo callbackPojo, String serialNum) {
        ActivitiesWinPojo taskPojo = new ActivitiesWinPojo();
        Activities activities = activitiesService.selectByActivityId(callbackPojo.getActiveId());
        taskPojo.setActivities(activities);
        taskPojo.setActivitiesWinRecordId(serialNum);
        return taskPojo;
    }

    private boolean minusCount(Long entId, Long prdId, String mobile, String serialNum) {
        try {
            AccountType accountType = AccountType.ENTERPRISE;

            Product product = productService.get(prdId);
            if (product == null) {
                LOGGER.error("无法根据产品ID获取产品信息, PrdId = {}.", prdId);
                return false;
            }

            //账户余额扣减,扣减的都是企业账户。现在没有活动账户了
            if (!tryMinusCount(entId, prdId, accountType, 1, serialNum, "充值")) {
                LOGGER.error("扣款失败, SerialNum = {}, Mobile = {}.", serialNum, mobile);
                return false;
            }

            return true;
        } catch (Exception e) {
            LOGGER.error("扣款失败, SerialNum = {}, Mobile = {}.", serialNum, mobile);
        }

        return false;
    }

    private boolean tryMinusCount(Long activityId, Long prdId, AccountType accountType, double delta, String serialNum,
                                  String desc) {
        try {
            if (accountService.minusCount(activityId, prdId, accountType, delta, serialNum, desc)) {
                LOGGER.info(buildMsg("成功", activityId, prdId, delta, serialNum));
                return true;
            } else {
                LOGGER.error(buildMsg("失败", activityId, prdId, delta, serialNum));
            }
        } catch (RuntimeException e) {
            LOGGER.error(buildMsg("失败", activityId, prdId, delta, serialNum));
        }

        LOGGER.error("扣减帐户失败，返回false");
        return false;
    }

    private String buildMsg(String statusMessage, Long activityId, Long prdId, double delta, String serialNum) {
        StringBuilder stringBuilder = new StringBuilder();
        Formatter formatter = new Formatter(stringBuilder);
        formatter.format("扣减帐户余额时%s，充值失败. ActivityId = %s, PrdId = %s, Delta = %f, SerialNum = %s", statusMessage,
            activityId, prdId, delta, serialNum);

        return stringBuilder.toString();
    }
}
