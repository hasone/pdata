package com.cmcc.vrp.queue.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.enums.ActivityWinRecordStatus;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.ChargeType;
import com.cmcc.vrp.enums.FinanceStatus;
import com.cmcc.vrp.enums.SupplierLimitStatus;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.Supplier;
import com.cmcc.vrp.province.model.SupplierProdReqUsePerDay;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductAccount;
import com.cmcc.vrp.province.model.SupplierReqUsePerDay;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.InterfaceRecordService;
import com.cmcc.vrp.province.service.MonthlyPresentRecordService;
import com.cmcc.vrp.province.service.MonthlyPresentRuleService;
import com.cmcc.vrp.province.service.PresentRecordService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProdReqUsePerDayService;
import com.cmcc.vrp.province.service.SupplierProductAccountService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.SupplierReqUsePerDayService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.vrp.queue.QueueRegistryCenter;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.CallbackPojo;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.queue.AbstractQueue;
import com.cmcc.vrp.queue.rule.DeliverRule;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.Constants.FLOW_ACCOUNT_FLAG;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.webservice.crowdfunding.CrowdfundingCallbackService;
import com.google.gson.Gson;

/**
 * 分发工作类
 * <p>
 * Created by sunyiwei on 2016/11/9.
 */
public abstract class AbstractDeliverWorker extends Worker {
    private static final Logger logger = LoggerFactory.getLogger(AbstractDeliverWorker.class);

    @Autowired
    protected QueueRegistryCenter queueRegistryCenter;

    @Autowired
    ChargeRecordService chargeRecordService;

    @Autowired
    PresentRecordService presentRecordService;
    
    @Autowired
    MonthlyPresentRecordService monthlyPresentRecordService;

    @Autowired
    ActivityWinRecordService activityWinRecordService;

    @Autowired
    InterfaceRecordService interfaceRecordService;

    @Autowired
    AccountService accountService;

    @Autowired
    ProductService productService;

    @Autowired
    EntProductService entProductService;

    @Autowired
    SupplierService supplierService;
    @Autowired
    SupplierProductService supplierProductService;
    @Autowired
    SupplierReqUsePerDayService supplierReqUsePerDayService;
    @Autowired
    SupplierProdReqUsePerDayService supplierProdReqUsePerDayService;
    
    @Autowired
    GlobalConfigService globalConfigService;
    
    @Autowired
    SupplierProductAccountService supplierProductAccountService;
    
    @Autowired
    TaskProducer taskProducer;
    
    @Autowired
    CrowdfundingCallbackService crowdfundingCallbackService;
    
    @Autowired
    MonthlyPresentRuleService monthlyPresentRuleService;

    /**
     * 执行操作
     */
    @Override
    public void exec() {
        //0. 获取队列消息
        String taskStr = getTaskString();
        logger.info("从充值队列中消费消息，消息内容为{}.", taskStr);

        //1. 解析充值参数
        ChargeDeliverPojo chargeDeliverPojo = null;
        if ((chargeDeliverPojo = convert(taskStr)) == null) {
            logger.error("无效的充值请求参数，充值失败.");
            return;
        }

        //2. 进行分发操作
        if (!process(chargeDeliverPojo)) {
            logger.error("分发失败， ChargeDeliverPojo = {}.", new Gson().toJson(chargeDeliverPojo));
            //分发失败，进行退款及修改记录状态的操作
            Integer financeStatus = null;
            Date updateChargeTime = new Date();
            
            //退款
            if (!accountService.returnFunds(chargeDeliverPojo.getSerialNum(), chargeDeliverPojo.getActivityType(), chargeDeliverPojo.getPrdId(), 1)) {
                logger.error("退款失败, 平台流水号:{}", chargeDeliverPojo.getSerialNum());
            }else{
                financeStatus = FinanceStatus.IN.getCode();
            }
            if ("chongqing".equals(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()))
                    && chargeDeliverPojo.getSplPrdId() != null
                    && !"FreeTimeFlow".equals(productService.get(chargeDeliverPojo.getPrdId()).getIllustration())) {
                Long splPid = chargeDeliverPojo.getSplPrdId();
                addCount(splPid, 1.0);
            }
            //更新chargeRecord状态
            if (!chargeRecordService.updateBySystemNum(chargeDeliverPojo.getSerialNum(),
                    ChargeRecordStatus.FAILED.getCode(), ChargeRecordStatus.FAILED.getMessage(),
                    financeStatus, updateChargeTime)) {
                logger.error("更新充值记录状态失败，充值记录流水号：{}", chargeDeliverPojo.getSerialNum());
            }
            if (chargeDeliverPojo.getType().equals(ChargeType.PRESENT_TASK.getCode())) {
                if (!presentRecordService.updatePresentStatus(chargeDeliverPojo.getRecordId(),
                        ChargeRecordStatus.FAILED, ChargeRecordStatus.FAILED.getMessage())) {
                    logger.error("更新赠送记录状态出错");
                }
            }else if (chargeDeliverPojo.getType().equals(ChargeType.MONTHLY_TASK.getCode())) {         
                if (!monthlyPresentRecordService.updatePresentStatus(chargeDeliverPojo.getRecordId(),
                        ChargeRecordStatus.FAILED, ChargeRecordStatus.FAILED.getMessage())) {
                    logger.error("更新包月赠送记录状态出错");
                }
                monthlyPresentRuleService.updateRuleStatusFini();
                
            } else if (chargeDeliverPojo.getType().equals(ChargeType.EC_TASK.getCode())) {
                //更新ec记录状态
                if (!interfaceRecordService.updateChargeStatus(chargeDeliverPojo.getRecordId(),
                    ChargeRecordStatus.FAILED, ChargeRecordStatus.FAILED.getMessage())) {
                    logger.error("更新EC记录状态出错");
                }
                //从EC侧来的业务请求需要进行回调
                CallbackPojo pojo = new CallbackPojo();
                pojo.setEntId(chargeDeliverPojo.getEntId());
                pojo.setSerialNum(chargeDeliverPojo.getSerialNum());
                taskProducer.productPlatformCallbackMsg(pojo);
            } else if (chargeDeliverPojo.getType().equals(ChargeType.REDPACKET_TASK.getCode())
                    || chargeDeliverPojo.getType().equals(ChargeType.FLOWCARD_TASK.getCode())
                    || chargeDeliverPojo.getType().equals(ChargeType.QRCODE_TASK.getCode())
                    || chargeDeliverPojo.getType().equals(ChargeType.LOTTERY_TASK.getCode())
                    || chargeDeliverPojo.getType().equals(ChargeType.GOLDENBALL_TASK.getCode())
                    || chargeDeliverPojo.getType().equals(ChargeType.CROWDFUNDING_TASK.getCode())) {
                String errorMsg = ActivityWinRecordStatus.FALURE.getname();
                //更新活动中奖记录状态
                if (!activityWinRecordService.updateByPrimaryKeySelective(buildActivityWinRecord(chargeDeliverPojo, errorMsg))) {
                    logger.error("更新活动中奖记录状态出错");
                }                
                //众筹的充值结果，回调给企业
                if (chargeDeliverPojo.getType().equals(ChargeType.CROWDFUNDING_TASK.getCode())) {
                    crowdfundingCallbackService.notifyChargeResult(chargeDeliverPojo.getSerialNum());
                }
            }
        }else{
            //分发成功
            //供应商id
            Long supplierProductId = chargeDeliverPojo.getSplPrdId();
            SupplierProduct supplierProduct = supplierProductService.selectById(supplierProductId);
            Double delta = supplierProduct.getPrice().doubleValue();
            
            Supplier supplier = supplierService.get(supplierProduct.getSupplierId());
            if(supplier.getLimitMoneyFlag().toString().equals(SupplierLimitStatus.ON.getCode().toString())){
                Long supplierReqUsePerDayId = null;
                
                SupplierReqUsePerDay supplierReqUsePerDay = 
                        supplierReqUsePerDayService.getTodayRecord(supplierProduct.getSupplierId());
                if(supplierReqUsePerDay == null){
                    //未插入
                    SupplierReqUsePerDay newRecord = createSupplierReqUsePerDay(supplier.getId());
                    if(!supplierReqUsePerDayService.insertSelective(newRecord)){
                        logger.info("插入供应商  = {} 限额统计记录失败。", supplier.getId());
                    }else{
                        logger.info("插入供应商  = {} 限额统计记录成功。", supplier.getId());
                        supplierReqUsePerDayId = newRecord.getId();
                    }
                }else{
                    supplierReqUsePerDayId = supplierReqUsePerDay.getId();
                }
                
                //更新供应商发送充值请求总金额
                if(!supplierReqUsePerDayService.updateUsedMoney(supplierReqUsePerDayId, delta)){
                    logger.info("更新供应商发送充值请求总金额失败, supplierProdId = {}, delta = {}",
                            supplierProduct.getSupplierId(), delta);
                }
            }
            if(supplierProduct.getLimitMoneyFlag().toString().equals(SupplierLimitStatus.ON.getCode().toString())){
                Long supplierProdReqUsePerDayId = null;
                SupplierProdReqUsePerDay supplierProdReqUsePerDay = 
                        supplierProdReqUsePerDayService.getTodayRecord(supplierProductId);
                if(supplierProdReqUsePerDay == null){
                    SupplierProdReqUsePerDay newRecord = createSupplierProdReqUsePerDay(supplierProductId);
                    if(!supplierProdReqUsePerDayService.insertSelective(newRecord)){
                        logger.info("插入供应商产品  = {} 限额统计记录失败。", supplierProduct.getId());
                    }else{
                        logger.info("插入供应商产品  = {} 限额统计记录成功。", supplierProduct.getId());
                        supplierProdReqUsePerDayId = newRecord.getId();
                    }
                }else{
                    supplierProdReqUsePerDayId = supplierProdReqUsePerDay.getId();
                }
                //更新供应商产品发送充值请求总金额
                if(!supplierProdReqUsePerDayService.updateUsedMoney(supplierProdReqUsePerDayId, delta)){
                    logger.info("更新供应商产品发送充值请求总金额失败, supplierProdId = {}, delta = {}",
                            supplierProduct.getSupplierId(), delta);
                }
            }
        }
    }
    
    private SupplierReqUsePerDay createSupplierReqUsePerDay(Long supplierId){
        SupplierReqUsePerDay record = new SupplierReqUsePerDay();
        record.setSupplierId(supplierId);
        record.setUsedMoney(0D);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        return record;
    }
    
    private SupplierProdReqUsePerDay createSupplierProdReqUsePerDay(Long supplierProductId){
        SupplierProdReqUsePerDay record = new SupplierProdReqUsePerDay();
        record.setSupplierProductId(supplierProductId);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setUseMoney(0D);
        record.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        return record;
    }
    
    //将消息内容转化成分发对象
    private ChargeDeliverPojo convert(String taskStr) {
        try {
            return new Gson().fromJson(taskStr, ChargeDeliverPojo.class);
        } catch (Exception e) {
            logger.error("将消息内容转化成分发对象时抛出异常，异常信息为{}, 异常堆栈为{}.", e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    private boolean process(ChargeDeliverPojo chargeDeliverPojo) {
        DeliverRule deliverRule = getDeliverRule();
        if (deliverRule == null) {           
            return false;
        }
        ChargeRecord cr = chargeRecordService.getRecordBySN(chargeDeliverPojo.getSerialNum());

        //根据消息内容找到相应的目标队列
        AbstractQueue target = deliverRule.deliver(chargeDeliverPojo);
        if (target == null) {
            logger.error("没有找到适用的分发渠道");            
            return false;
        }

        //回填charge record的价格
        Long price = 0L;
        Product product = productService.get(chargeDeliverPojo.getPrdId());
        if (product == null) {
            logger.error("产品不存在");
            return false;
        }
        EntProduct entProduct = null;
        //产品校验：如果是虚拟产品则查询其父产品
        if (FLOW_ACCOUNT_FLAG.VIRTUAL_PRODUCT.getCode().intValue() == product.getFlowAccountFlag().intValue()) {//虚拟产品则查询其父产品
            product = productService.get(product.getFlowAccountProductId());
        }

        if ((entProduct = entProductService.selectByProductIDAndEnterprizeID(product.getId(),
                chargeDeliverPojo.getEntId())) == null) {
            logger.error("企业-平台产品关联不存在，企业ID:{},平台产品ID:{}", chargeDeliverPojo.getEntId(), product.getId());
            return false;
        }
        if (entProduct.getDiscount() != null) {
            price = product.getPrice().longValue() * entProduct.getDiscount().longValue() / 100;
        } else {
            price = product.getPrice().longValue();
        }
        
        Long splPrdPrice = 0l;
        SupplierProduct supplierProduct = supplierProductService.selectByPrimaryKey(chargeDeliverPojo.getSplPrdId());
        if (supplierProduct != null) {
            splPrdPrice = supplierProduct.getPrice().longValue();
        }
        
        ChargeRecord record = new ChargeRecord();
        record.setId(cr.getId());
        record.setPrice(price);
        record.setSupplierProductId(chargeDeliverPojo.getSplPrdId());
        record.setSupplierProductPrice(splPrdPrice);

        if (!chargeRecordService.updateByPrimaryKeySelective(record)) {
            logger.error("无法更新chargeRecord的price:{}和supplierProductId:{}", price, chargeDeliverPojo.getSplPrdId());
            return false;
        }

        //如果塞队列失败，更新状态
        if (!target.publish(chargeDeliverPojo)) {
            logger.info("入渠道队列失败.");
            return false;
        } else {
            if (!chargeRecordService.updateStatusCode(cr.getId(), ChargeResult.ChargeMsgCode.supplierQueue.getCode())) {
                logger.error("入渠道队列成功，更新充值记录状态码出错");
            } else {
                logger.info("入渠道队列成功，更新充值记录 id = {}, 状态码 = {}", cr.getId(),
                        ChargeResult.ChargeMsgCode.supplierQueue.getCode());
            }

            if (chargeDeliverPojo.getType().equals(ChargeType.PRESENT_TASK.getCode())) {
                if (!presentRecordService.updateStatusCode(chargeDeliverPojo.getRecordId(),
                        ChargeResult.ChargeMsgCode.supplierQueue.getCode())) {
                    logger.error("入渠道队列成功，更新赠送记录状态码出错");
                } else {
                    logger.info("入渠道队列成功，更新赠送记录 id = {}, 状态码 = {}", chargeDeliverPojo.getRecordId(),
                            ChargeResult.ChargeMsgCode.supplierQueue.getCode());
                }
            } else if (chargeDeliverPojo.getType().equals(ChargeType.MONTHLY_TASK.getCode())) {
                if (!monthlyPresentRecordService.updateStatusCode(chargeDeliverPojo.getRecordId(),
                        ChargeResult.ChargeMsgCode.supplierQueue.getCode())) {
                    logger.error("入渠道队列成功，更新赠送记录状态码出错");
                } else {
                    logger.info("入渠道队列成功，更新赠送记录 id = {}, 状态码 = {}", chargeDeliverPojo.getRecordId(),
                            ChargeResult.ChargeMsgCode.supplierQueue.getCode());
                }
            } else if (chargeDeliverPojo.getType().equals(ChargeType.EC_TASK.getCode())) {

                if (!interfaceRecordService.updateStatusCode(chargeDeliverPojo.getRecordId(),
                        ChargeResult.ChargeMsgCode.supplierQueue.getCode())) {
                    logger.error("入渠道队列成功，更新ec记录状态码出错");
                } else {
                    logger.info("入渠道队列成功，更新ec记录 id = {}, 状态码 = {}", chargeDeliverPojo.getRecordId(),
                            ChargeResult.ChargeMsgCode.supplierQueue.getCode());
                }
            } else {
                //营销活动
                if (!activityWinRecordService.updateStatusCodeByRecordId(chargeDeliverPojo.getSerialNum(),
                        ChargeResult.ChargeMsgCode.supplierQueue.getCode())) {
                    logger.error("入渠道队列成功，更新活动记录状态码出错");
                } else {
                    logger.info("入渠道队列成功，更新活动记录 id = {}, 状态码 = {}", chargeDeliverPojo.getSerialNum(),
                            ChargeResult.ChargeMsgCode.supplierQueue.getCode());
                }
            }
            return true;
        }

    }

    protected abstract DeliverRule getDeliverRule();

    private ActivityWinRecord buildActivityWinRecord(ChargeDeliverPojo pojo, String errorMsg) {
        ActivityWinRecord awr = new ActivityWinRecord();
        awr.setId(pojo.getRecordId());
        awr.setRecordId(pojo.getSerialNum());
        awr.setStatus(ActivityWinRecordStatus.FALURE.getCode());
        awr.setReason(errorMsg);
        awr.setUpdateTime(new Date());
        awr.setChargeTime(new Date());
        return awr;
    }
    /**
     * 退款子账户余额
     * 
     * @param splPid
     * @param delta
     * @return
     */
    public boolean addCount(Long splPid, Double delta) {
        SupplierProductAccount supplierProductAccount = null;
        if ((supplierProductAccount = supplierProductAccountService.getInfoBySupplierProductId(splPid)) == null) {
            logger.info("退款子账户余额失败");
            return false;
        }
        Long supProAccountId = supplierProductAccount.getId();
        supplierProductAccountService.updateById(supProAccountId, delta);               
        logger.info("退款帐户余额信息成功.");
        return true;
    }
}
