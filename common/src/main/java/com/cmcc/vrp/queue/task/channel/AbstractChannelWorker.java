package com.cmcc.vrp.queue.task.channel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeType;
import com.cmcc.vrp.enums.FinanceStatus;
import com.cmcc.vrp.enums.MessageType;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseSmsTemplate;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EnterpriseSmsTemplateService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.InterfaceRecordService;
import com.cmcc.vrp.province.service.MonthlyPresentRecordService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.province.service.PresentRecordService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.queue.DistributeProducer;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.enums.Provinces;
import com.cmcc.vrp.queue.pojo.CallbackPojo;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.pojo.ChargeQueryPojo;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.SizeUnits;
import com.cmcc.webservice.crowdfunding.CrowdfundingCallbackService;
import com.google.gson.Gson;

/**
 * Created by leelyn on 2016/11/14.
 */
public abstract class AbstractChannelWorker extends Worker {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractChannelWorker.class);
    private static final String defaultFlag = "false";
    @Autowired
    Gson gson;
    @Autowired
    PhoneRegionService phoneRegionService;
    @Autowired
    TaskProducer taskProducer;
    @Autowired
    ChargeRecordService chargeRecordService;
    @Autowired
    AccountService accountService;
    @Autowired
    DistributeProducer distributeProducer;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    ProductService productService;
    @Autowired
    InterfaceRecordService interfaceRecordService;
    @Autowired
    PresentRecordService presentRecordService;
    @Autowired
    MonthlyPresentRecordService monthlyPresentRecordService;
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    @Autowired
    CrowdfundingCallbackService crowdfundingCallbackService;
    @Autowired
    private EnterprisesService enterprisesService;
    @Autowired
    EnterpriseSmsTemplateService enterpriseSmsTemplateService;
    

    protected abstract boolean isContinueDistribute();

    protected abstract BossService getBossService();

    /**
     * 执行方法
     */
    @Override
    public void exec() {
        // 获取队列消息
        String msg = getTaskString();
        LOGGER.info("开始消费消息：" + msg);
        if (StringUtils.isBlank(msg)) {
            LOGGER.error("获取消息为空");
            return;
        }
        ChargeDeliverPojo pojo = gson.fromJson(msg, ChargeDeliverPojo.class);

        if (pojo == null) {
            LOGGER.error("获取CommonPojo对象为空");
            return;
        }

        // 1、省渠道直连BOSS实现
        if (!isContinueDistribute()) {
            BossService bossService = getBossService();
            //向上游BOSS发起充值
            if (bossService == null) {
                LOGGER.error("没有获取相应BossService");
                return;
            }
            Product product;
            if ((product = productService.get(pojo.getPrdId())) == null) {
                return;
            }
            LOGGER.info("省渠道开始上游BOSS发送充值请求了");            
            try {
                BossService service = dynamicProxy(bossService);
                //System.out.println(service);
                //判断活动类型
                Long chargeSize = null;
                if(pojo.getActivityType().getCode().toString().equals(ActivityType.LUCKY_REDPACKET.getCode().toString())){
                    ActivityWinRecord activityWinRecord = activityWinRecordService.selectByRecordId(pojo.getSerialNum());
                    chargeSize = Long.valueOf(activityWinRecord.getSize());
                }else{
                    chargeSize = product.getProductSize();
                }
                BossOperationResult result = service.charge(pojo.getEntId(), pojo.getSplPrdId(), pojo.getMobile(), 
                        pojo.getSerialNum(), chargeSize);
                
                //更新charge_record的boss充值时间
                chargeRecordService.updateBossChargeTimeBySystemNum(pojo.getSerialNum(), new Date());
                
                //更新充值记录
                if (result.isSuccess()) {
                    //上游BOSS为异步接口
                    if (result.isAsync()) {
                        // 上游BOSS仅支持查询
                        if (result.isNeedQuery()) {
                            asynQuery(result);
                        }
                        //更新充值状态为已发充值请求
                        ChargeResult chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.PROCESSING);
                        if (!updateChargeRecord(pojo.getSerialNum(), chargeResult)) {
                            LOGGER.error("更新充值记录失败");
                        }
                    } else {
                        //更新充值状态为充值成功
                        ChargeResult chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.SUCCESS);
                        //boss为同步接口且充值成功，将charge_record的财务状态修改为
                        chargeResult.setFinanceStatus(FinanceStatus.OUT.getCode());
                        chargeResult.setUpdateChargeTime(new Date());
                        
                        if (!updateChargeRecord(pojo.getSerialNum(), chargeResult)) {
                            LOGGER.error("更新充值记录失败");
                        }
                        //从EC侧来的业务请求需要进行回调
                        if (pojo.getType().equals(ChargeType.EC_TASK.getCode())) {
                            callBackEC(pojo.getEntId(), pojo.getSerialNum());
                        }
                        
                        //众筹的充值结果，回调给企业
                        if (pojo.getType().equals(ChargeType.CROWDFUNDING_TASK.getCode())) {
                            crowdfundingCallbackService.notifyChargeResult(pojo.getSerialNum());
                        }
                        
                        if (needNotice()) {
                            ChargeRecord chargeRecord = chargeRecordService.getRecordBySN(pojo.getSerialNum());
                            LOGGER.info("充值成功，发送短信提醒{}", pojo.getSerialNum());
                            String mobile = chargeRecord.getPhone();
                            Integer effectType = chargeRecord.getEffectType();
                            String type = "本流量包含2G/3G/4G流量";
                            if ("2".equals(String.valueOf(effectType))) {
                                type = "本流量将于下月1日生效，包含2G/3G/4G流量";
                            }
                            Enterprise enterprise = enterprisesService.selectById(pojo.getEntId());
                            //Product product = productService.selectProductById(chargeRecord.getPrdId());
                            String productSize = String.valueOf(SizeUnits.KB.toMB(product.getProductSize()));
                            EnterpriseSmsTemplate smsmTemplate = enterpriseSmsTemplateService
                                        .getChoosedSmsTemplate(pojo.getEntId());
                            //EnterpriseSmsTemplate smsmTemplate = new EnterpriseSmsTemplate();
                            if (smsmTemplate != null) {
                                String template = smsmTemplate.getContent();
                                //String template = "尊敬的用户，{1}向您赠送了{0}MB国内流量，{2}，感谢您的支持！";
                                String content = MessageFormat.format(template,
                                            productSize, enterprise.getName(), type);
                                // 将短信扔到相应的队列中
                                if (!taskProducer.produceDeliverNoticeSmsMsg(new SmsPojo(mobile,
                                        content, productSize, enterprise.getEntName(), MessageType.CHARGE_NOTICE.getCode(),
                                        smsmTemplate.getName()))) {
                                    LOGGER.info("充值成功，入短信提醒队列失败{}", pojo.getSerialNum());
                                }
                            }
                              
                        }
                    }
                } else {
                    LOGGER.error("充值失败");
                    dealWhenFailure(pojo, result.getResultDesc(), ChargeResult.ChargeMsgCode.completed.getCode());
                }
            } catch (Exception e) {
                LOGGER.error("连接省渠道BOSS失败,异常信息:{}", e);
                dealWhenFailure(pojo, "连接省渠道BOSS失败,异常信息:" + e.getMessage(), ChargeResult.ChargeMsgCode.completed.getCode());
            }
        } else {
            // 2、卓望渠道将继续按省分发
            PhoneRegion phoneRegion = pojo.getPhoneRegion();
            String provinceName = null;
            if (phoneRegion == null
                    || StringUtils.isBlank(provinceName = phoneRegion.getProvince())) {
                LOGGER.error("无法获取手机号{}的归属地信息.", provinceName);
                return;
            }
            Provinces province = getProvinceByName(provinceName);
            if (province == null) {
                LOGGER.error("没找到相应的分省队列");
                return;
            }
            // 按省进行分发
            //分发失败，则退款及改状态
            if(!distributeByNetType(province, pojo)){
                dealWhenFailure(pojo, "卓望进入省渠道失败", ChargeResult.ChargeMsgCode.supplierQueue.getCode());
                return;
            }
                       
            //将status_code更新
            updateStatusCode(pojo, ChargeResult.ChargeMsgCode.zwProviceQueue.getCode());

            LOGGER.info("已入卓望分省队列中,省名:{},更新StatusCode状态:{}", provinceName, ChargeResult.ChargeMsgCode.zwProviceQueue.getCode());
        }
    }

    /**
     * @title: dealWhenFailure
     * */
    protected void dealWhenFailure(ChargeDeliverPojo pojo, String failureMsg, String statusCode) {
        ChargeResult chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.FAILURE);
        chargeResult.setFailureReason(failureMsg);
        chargeResult.setStatusCode(statusCode);
        
        //boss为同步接口且充值失败，需要更新charge_record的财务状态和充值时间
        chargeResult.setUpdateChargeTime(new Date());
        
        //退款
        if (!accountService.returnFunds(pojo.getSerialNum())) {
            LOGGER.error("退款失败");
        }else{
            chargeResult.setFinanceStatus(FinanceStatus.IN.getCode());
        }
        
        //更新充值状态为充值失败
        if (!updateChargeRecord(pojo.getSerialNum(), chargeResult)) {
            LOGGER.error("更新充值记录失败");
        }
        //从EC侧来的业务请求需要进行回调
        if (pojo.getType().equals(ChargeType.EC_TASK.getCode())) {
            callBackEC(pojo.getEntId(), pojo.getSerialNum());
        }
        
        //众筹的充值结果，回调给企业
        if (pojo.getType().equals(ChargeType.CROWDFUNDING_TASK.getCode())) {
            crowdfundingCallbackService.notifyChargeResult(pojo.getSerialNum());
        }
    }
    /**
     * @title: updateStatusCode
     * */
    private boolean updateStatusCode(ChargeDeliverPojo pojo, String statusCode) {
        ChargeRecord record;
        String systemNum;
        if (pojo == null
                || StringUtils.isBlank(systemNum = pojo.getSerialNum())
                || (record = chargeRecordService.getRecordBySN(systemNum)) == null) {
            return false;
        }
        //更新charge_record的status_code
        if(!chargeRecordService.updateStatusCode(record.getId(), statusCode)){
            LOGGER.error("charge_record更新statusCode失败,id={},statusCode={}", record.getId(), statusCode);
        }
        //更新分表的status_code
        if (pojo.getType().equals(ChargeType.PRESENT_TASK.getCode())) {
            if (!presentRecordService.updateStatusCode(pojo.getRecordId(), statusCode)) {
                LOGGER.error("更新赠送记录状态码出错,recordId={}, statusCode={}", pojo.getRecordId(), statusCode);
            } 
        } else if (pojo.getType().equals(ChargeType.MONTHLY_TASK.getCode())) {
            if (!monthlyPresentRecordService.updateStatusCode(pojo.getRecordId(), statusCode)) {
                LOGGER.error("更新包月赠送记录状态码出错,recordId={}, statusCode={}", pojo.getRecordId(), statusCode);
            } 
        } else if (pojo.getType().equals(ChargeType.EC_TASK.getCode())) {
            if (!interfaceRecordService.updateStatusCode(pojo.getRecordId(), statusCode)) {
                LOGGER.error("更新ec记录状态码出错,recordId={}, statusCode={}", pojo.getRecordId(), statusCode);
            }
        } else {
            //营销活动
            if (!activityWinRecordService.updateStatusCodeByRecordId(pojo.getSerialNum(), statusCode)) {
                LOGGER.error("更新活动记录状态码出错,recordId={}, statusCode={}", pojo.getSerialNum(), statusCode);
            }
        }
        
        return true;
    }


    /**
     * 更新充值状态
     *
     * @param systemNum
     * @param chargeResult
     * @return
     */
    protected boolean updateChargeRecord(String systemNum, ChargeResult chargeResult) {
        ChargeRecord record;
        if (StringUtils.isBlank(systemNum)
                || chargeResult == null
                || (record = chargeRecordService.getRecordBySN(systemNum)) == null) {
            return false;
        }
        String statusCode = null;
        if(StringUtils.isEmpty(chargeResult.getStatusCode())){
            statusCode = ChargeResult.ChargeMsgCode.completed.getCode();
            LOGGER.info("更新StatusCode为:{}", statusCode);
        }
       
        chargeResult.setStatusCode(statusCode);
        return chargeRecordService.updateStatus(record.getId(), chargeResult);
    }

    /**
     * 回调EC
     *
     * @param entId
     * @param systemSerialNum
     */
    protected void callBackEC(Long entId, String systemSerialNum) {
        CallbackPojo pojo = new CallbackPojo();
        pojo.setEntId(entId);
        pojo.setSerialNum(systemSerialNum);
        taskProducer.productPlatformCallbackMsg(pojo);
    }

    /**
     * 将查询队列中发送消息
     *
     * @param result
     */
    protected void asynQuery(BossOperationResult result) {
        ChargeQueryPojo pojo = new ChargeQueryPojo();
        pojo.setSystemNum(result.getSystemNum());
        pojo.setFingerPrint(result.getFingerPrint());
        pojo.setEntId(result.getEntId());
        pojo.setBeginTime(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        pojo.setTimes(0);
        taskProducer.produceAsynDeliverQueryMsg(pojo);
    }

    /**
     * 通过省名找对应枚举对象
     *
     * @param provinceName
     * @return
     */
    private Provinces getProvinceByName(String provinceName) {
        for (Provinces province : Provinces.values()) {
            if (province.getName().equals(provinceName)) {
                return province;
            }
        }
        return null;
    }

    /**
     * 按省进行分发
     *
     * @param provinces
     * @param pojo
     */
    private boolean distributeByNetType(Provinces provinces, ChargeDeliverPojo pojo) {
        boolean result = false;
        switch (provinces) {
            case BEIJING:
                result = distributeProducer.distibute(provinces.BEIJING.getQueueName(), pojo);
                break;
            case SHANGHAI:
                result = distributeProducer.distibute(provinces.SHANGHAI.getQueueName(), pojo);
                break;
            case TIANJIN:
                result = distributeProducer.distibute(provinces.TIANJIN.getQueueName(), pojo);
                break;
            case CHONGQING:
                result = distributeProducer.distibute(provinces.CHONGQING.getQueueName(), pojo);
                break;
            case HEBEI:
                result = distributeProducer.distibute(provinces.HEBEI.getQueueName(), pojo);
                break;
            case SHANXI1:
                result = distributeProducer.distibute(provinces.SHANXI1.getQueueName(), pojo);
                break;
            case NEIMENG:
                result = distributeProducer.distibute(provinces.NEIMENG.getQueueName(), pojo);
                break;
            case LIAONING:
                result = distributeProducer.distibute(provinces.LIAONING.getQueueName(), pojo);
                break;
            case JINLIN:
                result = distributeProducer.distibute(provinces.JINLIN.getQueueName(), pojo);
                break;
            case HEILONGJIANG:
                result = distributeProducer.distibute(provinces.HEILONGJIANG.getQueueName(), pojo);
                break;
            case JIANGSU:
                result = distributeProducer.distibute(provinces.JIANGSU.getQueueName(), pojo);
                break;
            case ZHEJIANG:
                result = distributeProducer.distibute(provinces.ZHEJIANG.getQueueName(), pojo);
                break;
            case ANHUI:
                result = distributeProducer.distibute(provinces.ANHUI.getQueueName(), pojo);
                break;
            case FUJIAN:
                result = distributeProducer.distibute(provinces.FUJIAN.getQueueName(), pojo);
                break;
            case JIANGXI:
                result = distributeProducer.distibute(provinces.JIANGXI.getQueueName(), pojo);
                break;
            case SHANDONG:
                result = distributeProducer.distibute(provinces.SHANDONG.getQueueName(), pojo);
                break;
            case HENAN:
                result = distributeProducer.distibute(provinces.HENAN.getQueueName(), pojo);
                break;
            case HUBEI:
                result = distributeProducer.distibute(provinces.HUBEI.getQueueName(), pojo);
                break;
            case HUNAN:
                result = distributeProducer.distibute(provinces.HUNAN.getQueueName(), pojo);
                break;
            case GUANGDONG:
                result = distributeProducer.distibute(provinces.GUANGDONG.getQueueName(), pojo);
                break;
            case GUANGXI:
                result = distributeProducer.distibute(provinces.GUANGXI.getQueueName(), pojo);
                break;
            case HAINAN:
                result = distributeProducer.distibute(provinces.HAINAN.getQueueName(), pojo);
                break;
            case SICHUAN:
                result = distributeProducer.distibute(provinces.SICHUAN.getQueueName(), pojo);
                break;
            case GUIZHOU:
                result = distributeProducer.distibute(provinces.GUIZHOU.getQueueName(), pojo);
                break;
            case YUNNAN:
                result = distributeProducer.distibute(provinces.YUNNAN.getQueueName(), pojo);
                break;
            case XIZANG:
                result = distributeProducer.distibute(provinces.XIZANG.getQueueName(), pojo);
                break;
            case SHANXI2:
                result = distributeProducer.distibute(provinces.SHANXI2.getQueueName(), pojo);
                break;
            case GANSU:
                result = distributeProducer.distibute(provinces.GANSU.getQueueName(), pojo);
                break;
            case QINGHAI:
                result = distributeProducer.distibute(provinces.QINGHAI.getQueueName(), pojo);
                break;
            case NINGXIA:
                result = distributeProducer.distibute(provinces.NINGXIA.getQueueName(), pojo);
                break;
            case XINJIANG:
                result = distributeProducer.distibute(provinces.XINJIANG.getQueueName(), pojo);
                break;
            default:
                LOGGER.error("卓望渠道消息没有找到相应的分省");
                break;
        }
        return result;
    }
    /**
     * @title: dynamicProxy
     * */
    private BossService dynamicProxy(final BossService bossService) {
        if (!getDynamicProxyFlag()) {
            return bossService;
        }
        return (BossService) Proxy.newProxyInstance(bossService.getClass().getClassLoader(), new Class[]{BossService.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if ("charge".equals(method.getName())) {
                    return new ProxyOperationResult();
                }
                return method.invoke(bossService, args);
            }
        });
    }
    /**
     * @title: getDynamicProxyFlag
     * */
    private Boolean getDynamicProxyFlag() {
        String dynamicFlag = globalConfigService.get(GlobalConfigKeyEnum.DYNAMIC_PROXY_BOSS_FLAG.getKey());
        String finalFlag = StringUtils.isBlank(dynamicFlag) ? defaultFlag : dynamicFlag;
        return Boolean.parseBoolean(finalFlag);
    }


    /**
     * 这个仅仅用于模拟BOSS实现随机返回成功或者失败的充值结果
     */
    private class ProxyOperationResult extends AbstractBossOperationResultImpl {
        Random random = new Random();

        @Override
        public boolean isSuccess() {
            if ("true".equals(globalConfigService.get(GlobalConfigKeyEnum.TEST_CHARGE_RESULT.getKey()))) {
                return true;
            } else if ("false".equals(globalConfigService.get(GlobalConfigKeyEnum.TEST_CHARGE_RESULT.getKey()))) {
                return false;
            }else{
                return random.nextBoolean();
            }
        }

        @Override
        public boolean isAsync() {
            return false;
        }

        @Override
        public Object getOperationResult() {
            return null;
        }

        @Override
        public boolean isNeedQuery() {
            return false;
        }
        
        @Override
        public void setResultDesc(String resultDesc) {
            if (globalConfigService.get(GlobalConfigKeyEnum.TEST_RESULT_DESC.getKey()).equals("true")) {
                this.resultDesc = "测试自定义充值失败";
            } else {
                this.resultDesc = "充值失败";
            }
            
        }
    }
    /**
     * @title: needNotice
     * */
    private boolean needNotice() {
        String noticeFlag = globalConfigService.get(GlobalConfigKeyEnum.CHARGE_SUCCESS_NOTICE.getKey());
        String finalFlag = org.apache.commons.lang.StringUtils.isBlank(noticeFlag) ? "false" : noticeFlag;
        return Boolean.parseBoolean(finalFlag);
    }
}
