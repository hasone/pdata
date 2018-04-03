package com.cmcc.vrp.queue.task;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.zhuowang.bean.OrderRequestResult;
import com.cmcc.vrp.boss.zhuowang.bean.UserData;
import com.cmcc.vrp.boss.zhuowang.service.FlowPackageOrderService;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.ChargeType;
import com.cmcc.vrp.enums.FinanceStatus;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.CallbackPojo;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.pojo.ZwBossPojo;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * edited by sunyiwei on 2016/10/18
 * edited by leelyn on 2016/11/16
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZwChargeWorker extends Worker {

    private static final Logger logger = LoggerFactory.getLogger(ZwChargeWorker.class);
    private static final String defaultFlag = "false";
    @Autowired
    SerialNumService serialNumService;
    @Autowired
    TaskProducer taskProducer;
    @Autowired
    SupplierProductService supplierProductService;
    @Autowired
    FlowPackageOrderService<UserData> flowPackageOrderService;
    @Autowired
    ChargeRecordService chargeRecordService;
    @Autowired
    AccountService accountService;
    @Autowired
    GlobalConfigService globalConfigService;

    //1. 解析充值参数
    //2. 更新记录状态为已发送充值请求
    //3. 发送充值请求
    //4. 更新记录状态为充值结果
    @Override
    public void exec() {
        //0. 获取队列消息
        Gson gson = new Gson();
        String taskStr = getTaskString();
        logger.info("从卓望充值队列中消费消息，消息内容为{}.", taskStr);

        //1. 解析充值参数
        ZwBossPojo zwBossPojo = parse(gson, taskStr);
        //to fix findbugs warning
        List<ChargeDeliverPojo> pojos;
        if (zwBossPojo == null
            || (pojos = zwBossPojo.getPojos()) == null) {
            return;
        }
        System.out.println("卓望BOSS渠道消费者拿到消息包的尺寸:" + pojos.size());
        logger.info("卓望BOSS渠道消费者拿到消息包的尺寸:{}", pojos.size());
        if (!validate(pojos)) {
            logger.error("无效的充值请求参数，充值失败.");
            //更新充值记录结果
            ChargeResult result = new ChargeResult(ChargeResult.ChargeResultCode.FAILURE, "卓望BOSS渠道中取到无效的充值请求参数");
            result.setStatusCode(ChargeResult.ChargeMsgCode.zwBossQueue.getCode());
            dealWhenFailure(pojos, result);
            return;
        }

        //校验通过了，开始发起充值
        String bossReqSerialNum = buildBossReqSerialNum();
        try {
            logger.info("开始向卓望BOSS侧发送请求");
  
            FlowPackageOrderService<UserData> orderService = dynamicProxy(flowPackageOrderService);
            OrderRequestResult orr = orderService.sendRequest(build(pojos), bossReqSerialNum);
            
            //批量更新记录的boss充值时间
            chargeRecordService.batchUpdateBossChargeTimeBySystemNum(buildSystemNums(pojos), new Date());

            //批量处理流水号信息并更新充值结果
            processSerialNum(pojos, bossReqSerialNum, orr);

            ChargeResult result = buildChargeResult(orr);
            
            if (!result.isSuccess()) {
                dealWhenFailure(pojos, result);
            
            } else {
                if (!batchUpdateStatus(pojos, result)) {
                    logger.error("卓望提交成功后更新充值记录失败");
                }
            }
        } catch (Exception e) {
            dealWhenFailure(pojos, buildFailChargeResult("连接卓望BOSS失败", ChargeResult.ChargeMsgCode.completed.getCode()));
            logger.error("连接BOSS失败,错误信息:{}", e);
        }
    }

    private void dealWhenFailure(List<ChargeDeliverPojo> pojos, ChargeResult result) {
        result.setUpdateChargeTime(new Date());
        result.setFinanceStatus(FinanceStatus.IN.getCode());
        // 1、退款
        for (ChargeDeliverPojo pojo : pojos) {
            accountService.returnFunds(pojo.getSerialNum());
        }
        // 2、更新充值状态
        if (!batchUpdateStatus(pojos, result)) {
            logger.error("卓望队列中处理失败后更新充值记录失败");
        }
        // 3、回调EC
        for (ChargeDeliverPojo pojo : pojos) {
            if (pojo.getType().equals(ChargeType.EC_TASK.getCode())) {
                callBackEC(pojo.getEntId(), pojo.getSerialNum());
            }
        }
    }

    /**
     * 回调EC
     *
     * @param entId
     * @param systemSerialNum
     */
    private void callBackEC(Long entId, String systemSerialNum) {
        CallbackPojo pojo = new CallbackPojo();
        pojo.setEntId(entId);
        pojo.setSerialNum(systemSerialNum);
        taskProducer.productPlatformCallbackMsg(pojo);
    }

    //根据充值结果构建相应的对象
    private ChargeResult buildChargeResult(OrderRequestResult orr) {
        //只有在处理结果为00时才是请求成功，其它状态都是充值失败.
        if (orr != null && OrderRequestResult.ResultCode.SUCCESS.getStatus().equals(orr.getStatus())) {
            return new ChargeResult(ChargeResult.ChargeResultCode.PROCESSING);
        } else {
            return new ChargeResult(ChargeResult.ChargeResultCode.FAILURE,
                (orr == null || StringUtils.isBlank(orr.getRspDesc())) ? ChargeRecordStatus.FAILED.getMessage() : orr.getRspDesc());
        }
    }

    //构建失败的充值结果
    private ChargeResult buildFailChargeResult(String errorMsg, String statusCode) {
        ChargeResult chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.FAILURE, errorMsg);
        chargeResult.setStatusCode(statusCode);
        return chargeResult;
    }

    /**
     * 批量更新充值状态
     *
     * @param pojos
     * @param chargeResult
     * @return
     */
    private boolean batchUpdateStatus(List<ChargeDeliverPojo> pojos, ChargeResult chargeResult) {
        if (pojos == null
            || chargeResult == null) {
            logger.error("无效参数，更新充值状态失败");
            return false;
        }
        List<ChargeRecord> records = chargeRecordService.batchSelectBySystemNum(buildSystemNums(pojos));

        if (records == null
            || records.isEmpty()) {
            logger.error("没有找到相应的充值记录,更新充值");
            return false;
        }

        String statusCode = chargeResult.getStatusCode();
        
        if(StringUtils.isEmpty(statusCode)){
            statusCode = ChargeResult.ChargeMsgCode.completed.getCode();       
        }
        chargeResult.setStatusCode(statusCode);
        
        if (!chargeRecordService.batchUpdateStatus(buildRecords(records, chargeResult,
            chargeResult.getStatusCode()))
            || !chargeRecordService.updateActivityRecords(records, chargeResult)) {
            logger.error("更新充值记录失败");
            return false;
        }

        logger.info("更新StatusCode为:{}", statusCode);
        return true;
    }

    private List<ChargeRecord> buildRecords(List<ChargeRecord> records, ChargeResult chargeResult, String statusCode) {
        ChargeRecordStatus status = null;
        if (chargeResult.getCode().equals(ChargeResult.ChargeResultCode.PROCESSING)) {
            status = ChargeRecordStatus.PROCESSING;
        } else if (chargeResult.getCode().equals(ChargeResult.ChargeResultCode.FAILURE)) {
            status = ChargeRecordStatus.FAILED;
        }
        List<ChargeRecord> list = new ArrayList<ChargeRecord>();
        for (ChargeRecord record : records) {
            record.setStatus(status.getCode());
            record.setErrorMessage(status.getMessage());
            record.setChargeTime(new Date());
            record.setStatusCode(statusCode);
            record.setFinanceStatus(chargeResult.getFinanceStatus());
            record.setUpdateChargeTime(chargeResult.getUpdateChargeTime());
            list.add(record);
        }
        return list;
    }

    private List<String> buildSystemNums(List<ChargeDeliverPojo> pojos) {
        List<String> list = new ArrayList<String>();
        for (ChargeDeliverPojo pojo : pojos) {
            list.add(pojo.getSerialNum());
        }
        return list;
    }

    private String buildBossReqSerialNum() {
        return "ZJHY" + SerialNumGenerator.buildBossReqSerialNum(26);
    }

    private List<UserData> build(List<ChargeDeliverPojo> pojos) {

        List<UserData> userDatas = new LinkedList<UserData>();
        for (ChargeDeliverPojo pojo : pojos) {
            String code;
            if (StringUtils.isBlank(code = get(pojo.getSplPrdId()))) {
                logger.error("供应产品ID{}对应的编码不存在", pojo.getSplPrdId());
                continue;
            }
            UserData userData = new UserData();
            userData.setMobNum(pojo.getMobile());
            userData.setUserPackage(code);
            userDatas.add(userData);
        }
        return userDatas;
    }

    //处理流水号相关信息
    private void processSerialNum(List<ChargeDeliverPojo> pojos, String bossReqSerialNum, OrderRequestResult orr) {
        //卓望平台有自己的流水号规则，因此这里要生成相应的BOSS请求流水号以及响应流水号
        List<SerialNum> serialNums = buildSerialNum(pojos, bossReqSerialNum, parseRespSerialNum(orr));
        if (!serialNumService.batchUpdate(serialNums)) {
            logger.error("更新卓望流水号记录失败. 具体信息为{}.", new Gson().toJson(serialNums));
        } else {
            logger.debug("更新卓望流水号记录成功. 具体信息为{}.", new Gson().toJson(serialNums));
        }
    }

    //解析响应的流水号
    private String parseRespSerialNum(OrderRequestResult orr) {
        List<String> operSeqList = orr.getOperSeqList();
        //响应的序列号超过一个，目前是不可能的！但为了保险起见，先记下来再说
        if (operSeqList == null || operSeqList.size() != 1) {
            return operSeqList == null ? null : StringUtils.join(operSeqList, ", ");
        }

        return operSeqList.get(0);
    }

    //根据供应商产品ID获取相应的产品编码
    private String get(Long splPid) {
        SupplierProduct sp = supplierProductService.selectByPrimaryKey(splPid);
        return sp == null ? null : sp.getCode();
    }

    //解析充值对象
    private ZwBossPojo parse(Gson gson, String taskString) {
        try {
            return gson.fromJson(taskString, ZwBossPojo.class);
        } catch (Exception e) {
            logger.info("充值参数反序列化错误, 错误信息为{}， 具体的参数为{}", e.getMessage(), taskString);
            return null;
        }
    }

    //校验充值对象的有效性
    private boolean validate(List<ChargeDeliverPojo> pojos) {
        for (ChargeDeliverPojo pojo : pojos) {
            if (pojo == null
                || pojo.getEntId() == null
                || pojo.getSplPrdId() == null
                || StringUtils.isBlank(pojo.getMobile())
                || StringUtils.isBlank(pojo.getSerialNum())) {
                logger.error("无效的充值请求参数，ZwChargePojo = {}.", JSONObject.toJSONString(pojo));
                return false;
            }
        }
        return true;
    }


    private List<SerialNum> buildSerialNum(List<ChargeDeliverPojo> pojos, String bossReqSerialNum, String bossRespSerialNum) {
        List<SerialNum> list = new ArrayList<SerialNum>();
        for (ChargeDeliverPojo pojo : pojos) {
            SerialNum serialNum = new SerialNum();
            serialNum.setPlatformSerialNum(pojo.getSerialNum());
            serialNum.setBossReqSerialNum(bossReqSerialNum);
            serialNum.setBossRespSerialNum(bossRespSerialNum);
            serialNum.setUpdateTime(new Date());
            list.add(serialNum);
        }
        return list;
    }

    private Boolean getDynamicProxyFlag() {
        String dynamicFlag = globalConfigService.get(GlobalConfigKeyEnum.DYNAMIC_PROXY_BOSS_FLAG.getKey());
        String finalFlag = StringUtils.isBlank(dynamicFlag) ? defaultFlag : dynamicFlag;
        return Boolean.parseBoolean(finalFlag);
    }

    private FlowPackageOrderService<UserData> dynamicProxy(final FlowPackageOrderService<UserData> flowPackageOrderService) {
        if (!getDynamicProxyFlag()) {
            return flowPackageOrderService;
        }
        return (FlowPackageOrderService<UserData>) Proxy.newProxyInstance(flowPackageOrderService.getClass().getClassLoader(), new Class[]{FlowPackageOrderService.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if ("sendRequest".equals(method.getName())) {
                    return getDynamicResult();
                }
                return method.invoke(flowPackageOrderService, args);
            }
        });
    }

    private OrderRequestResult getDynamicResult() {
        String[] results = {OrderRequestResult.ResultCode.SUCCESS.getStatus(), OrderRequestResult.ResultCode.OTHERS.getStatus()};
        int index = (int) (Math.random() * 2);
        OrderRequestResult requestResult = new OrderRequestResult();
        requestResult.setStatus(results[index]);
        return requestResult;
    }
}
