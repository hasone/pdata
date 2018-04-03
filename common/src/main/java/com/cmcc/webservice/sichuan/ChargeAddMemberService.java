package com.cmcc.webservice.sichuan;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.charge.ChargeResult;
//import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.dao.ChargeRecordMapper;
//import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.InterfaceRecord;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.InterfaceRecordService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.Constants;
//import com.cmcc.vrp.util.IpUtils;
import com.cmcc.vrp.util.MD5;
import com.cmcc.webservice.sichuan.pojo.ReturnCode;
import com.cmcc.webservice.sichuan.task.ChargeTask;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
//import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

//@Component
//@Path("addMember")
/**
 * @author
 */
public class ChargeAddMemberService {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ChargeAddMemberService.class);
    @Autowired
    EnterprisesService enterprisesService;

    @Context
    HttpServletRequest request;

    @Autowired
    ProductService productService;

    @Autowired
    ChargeRecordMapper chargeRecordMapper;

    @Autowired
    private InterfaceRecordService interfaceRecordService;

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 加成员
     * @param serialNumber 系列号
     * @param account 账号
     * @param bisCode bisCode
     * @param timeStamp 时间戳
     * @param sign 签名
     * @param mobile  手机号码
     * @param serviceCode  服务号
     * @param discntCode discntCode
     * @param effectiveWay effectiveWay
     * @param effectiveCycle effectiveCycle
     * @return 响应对象
     */
    @POST
    @Consumes("application/x-www-form-urlencoded")
    public Response addMemeber(@FormParam("serialNumber") String serialNumber, @FormParam("account") String account, @FormParam("bisCode") String bisCode,
            @FormParam("timeStamp") String timeStamp, @FormParam("sign") String sign, @FormParam("mobile") String mobile,
            @FormParam("serviceCode") String serviceCode, @FormParam("discntCode") String discntCode, @FormParam("effectiveWay") String effectiveWay,
            @FormParam("effectiveCycle") String effectiveCycle) {

        return Response.status(404).entity("无效地址").build();

        /*
         * logger.info("接收企业成员增加接口调用，接口参数为：" + "serialNumber：" + serialNumber + " ,account：" + account + " ,bisCode:" + bisCode + " ,timeStamp:" + timeStamp +
         * " ,sign:" + sign + " ,mobile:" + mobile + " ,serviceCode:" + serviceCode + " ,discntCode：" + discntCode + " ,effectiveWay:" + effectiveWay +
         * " ,effectiveCycle" + effectiveCycle); //1、校验参数(包含校验产品) Product product = productService.selectByCode(discntCode);
         * 
         * ReturnCode returnCode = validateIfLegalRequest(serialNumber, account, bisCode, timeStamp, sign, mobile, serviceCode, discntCode, effectiveWay,
         * effectiveCycle, product);
         * 
         * if (!returnCode.equals(ReturnCode.success)) { Map map = new HashMap(); map.put("resultCode", returnCode.getCode()); map.put("resultMsg",
         * returnCode.getMsg()); String resultStr = JSON.toJSONString(map); //response返回数据 return Response.status(200).entity(resultStr).build(); }
         * 
         * //获企业编码 Enterprise enterprise = enterprisesService.selectByAppKey(bisCode); String code = enterprise.getCode();
         * 
         * //2、存储接口调用记录 //初始化订单对象 InterfaceRecord record = initRecord(mobile, discntCode, code, serialNumber, IpUtils.getRemoteAddr(request));
         * 
         * //尝试插入数据库，如已有记录包含相同的（企业编码，序列号），则将插入失败 if (!interfaceRecordService.insert(record)) { //订单已存在,已有记录包含相同的（企业编码，序列号），则将插入失败 logger.info("enterpriseCode:"
         * + record.getEnterpriseCode() + "changePhoneNum:" + record.getPhoneNum() + "productCode:" + record.getProductCode() + "requestTime:" +
         * record.getSerialNum() + "requestIp" + record.getIpAddress() + "errorMsg:" + com.cmcc.webservice.constants.ChargeRecordStatus.ChargeMsg.order_exist);
         * Map map = new HashMap(); map.put("resultCode", ReturnCode.other_error.getCode()); map.put("resultMsg", "该序列号已存在"); String resultStr =
         * JSON.toJSONString(map); return Response.status(200).entity(resultStr).build(); }
         * 
         * //向充值表中插入数据 InterfaceRecord interFaceChargeRecord = interfaceRecordService.get(record.getId()); ChargeRecord chargeRecord = new ChargeRecord();
         * 
         * 
         * chargeRecord.setEnterId(enterprise.getId()); chargeRecord.setPrdId(product.getId());
         * chargeRecord.setStatus(com.cmcc.vrp.enums.ChargeRecordStatus.PROCESSING.getCode()); chargeRecord.setType(ActivityType.INTERFACE.getname());
         * chargeRecord.setAname("-"); chargeRecord.setTypeCode(ActivityType.INTERFACE.getCode()); chargeRecord.setPhone(mobile);
         * chargeRecord.setRecordId(interFaceChargeRecord.getId()); chargeRecord.setSerialNum(serialNumber); chargeRecordMapper.insertSelective(chargeRecord);
         * logger.info("向充值表中插入数据:" + record.getEnterpriseCode() + "phone:" + record.getPhoneNum() + "productCode:" + record.getProductCode() + "chargeStatus:"
         * + chargeRecord.getStatus());
         * 
         * //3、进行成员新增 //开始封装连接boss的数据交给线程，然后将线程提交到线程池中运行，等待运行结果完成并回填数据库，返回充值结果chargeResult ChargeResult chargeResult = chargeFromBoss(record,
         * chargeRecord.getId());
         * 
         * logger.info("企业接口BOSS充值返回serialNumber={},bisCode={},mobile={},chargeResult:{}", serialNumber, bisCode, mobile, JSON.toJSONString(chargeResult));
         * 
         * if (chargeResult.getCode().getCode().equals("100")) { chargeRecord.setStatus(com.cmcc.vrp.enums.ChargeRecordStatus.COMPLETE.getCode());
         * chargeRecord.setErrorMessage("充值成功"); } else { chargeRecord.setStatus(com.cmcc.vrp.enums.ChargeRecordStatus.FAILED.getCode());
         * chargeRecord.setErrorMessage(chargeResult.getFailureReason()); }
         * 
         * chargeRecord.setChargeTime(new Date()); chargeRecordMapper.updateByPrimaryKeySelective(chargeRecord);
         * 
         * //chargeResult组装报文，返回给接口 //returnXML= assembleReturnXML(chargeResult);
         * 
         * //response返回数据 //return Response.status(200).entity(returnXML).build();
         * 
         * String resultCode = ""; String resultMsg = ""; Map map = new HashMap(); if (chargeResult.getCode().getCode().equals("100")) { resultCode = "0000";
         * resultMsg = "成功"; } else if (chargeResult.getCode().getCode().equals("101")) { resultCode = "0099"; resultMsg = chargeResult.getFailureReason(); }
         * else { resultCode = "0098"; resultMsg = chargeResult.getFailureReason(); }
         * 
         * map.put("resultCode", resultCode); map.put("resultMsg", resultMsg); String resultStr = JSON.toJSONString(map);
         * 
         * logger.info("serialNumber：" + serialNumber + " ,bisCode:" + bisCode + " ,mobile:" + mobile + ",返回企业Response:" + resultStr); //response返回数据 return
         * Response.status(200).entity(resultStr).build();
         */
    }

    /**
     * 检查所有数据是否正确，返回相应enum中的结果
     *
     * @param chargeInfo
     * @return
     */
    public ReturnCode validateIfLegalRequest(String serialNumber, String account, String bisCode, String timeStamp, String sign, String mobile,
            String serviceCode, String discntCode, String effectiveWay, String effectiveCycle, Product product) {

        // 1、检查参数是否正确
        if (!checkIfEmpty(serialNumber, account, bisCode, timeStamp, sign, mobile, discntCode, effectiveWay, effectiveCycle)) {
            return ReturnCode.parameter_error;
        }

        // 2、检查账号是否存在,企业账号状态是否正常，企业EC接口是否开通
        Enterprise enterprise = enterprisesService.selectByAppKey(bisCode);
        if (enterprise == null || enterprise.getDeleteFlag() != 0 || enterprise.getInterfaceFlag() != 1) {
            return ReturnCode.invalid_account;
        }

        // 3、检查md5是否正确
        // String vefiryResult= busiData;//chargeInfo.getBusiInfo().getChargeVerifyXml();
        if (!virifyMD5(serialNumber, account, bisCode, timeStamp, enterprise.getAppSecret(), sign)) {
            return ReturnCode.invalid_sign;
        }

        // 4、检查是否手机号码
        if (!com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)) {
            return ReturnCode.invalid_moblie;
        }

        // 5、检查时间戳是否在5分钟之内
        long createTime = Long.parseLong(timeStamp);
        long currentTime = System.currentTimeMillis();
        if (currentTime - createTime > 50 * 60 * 1000) {
            return ReturnCode.request_more_than_5min;
        }

        // 6、校验产品
        if (product == null) {
            return ReturnCode.no_product;
        }

        return ReturnCode.success;
    }

    Boolean checkIfEmpty(String serialNumber, String account, String bisCode, String timeStamp, String sign, String mobile, String discntCode,
            String effectiveWay, String effectiveCycle) {
        if (StringUtils.isEmpty(serialNumber) || StringUtils.isEmpty(account) || StringUtils.isEmpty(bisCode) || StringUtils.isEmpty(timeStamp)
                || StringUtils.isEmpty(mobile) || StringUtils.isEmpty(discntCode) || StringUtils.isEmpty(effectiveWay) || StringUtils.isEmpty(effectiveCycle)) {
            return false;
        }
        return true;
    }

    Boolean virifyMD5(String serialNumber, String account, String bisCode, String timeStamp, String key, String sign) {
        String content = serialNumber + account + bisCode + timeStamp;
        String signStr = MD5.sign(content, key, "UTF-8");
        if (!signStr.equals(sign)) {
            logger.info("MD5签名验证错误，签名应为:" + signStr);
            return false;
        }
        return true;
    }

    /**
     * 初始化对象，用于插入
     */
    private InterfaceRecord initRecord(String phoneNum, String productCode, String enterpriseCode, String serialNum, String ipAddress) {
        InterfaceRecord record = new InterfaceRecord();
        record.setEnterpriseCode(enterpriseCode);
        record.setProductCode(productCode);
        record.setPhoneNum(phoneNum);
        record.setSerialNum(serialNum);
        record.setIpAddress(ipAddress);
        record.setStatus(com.cmcc.vrp.enums.ChargeRecordStatus.WAIT.getCode());
        record.setCreateTime(new Date());
        record.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());

        return record;
    }

    /**
     * 将数据组装到线程，然后提交到线程池运行，返回结果
     *
     * @param phoneNum
     * @param productCode
     * @param enterpriseCode
     * @return ChargeResult
     * @throws
     * @author: qihang
     */
    private ChargeResult chargeFromBoss(InterfaceRecord record, Long chargeRecordId) {
        logger.info("chargeFromBoss start！ " + JSON.toJSONString(record));
        String phoneNum = record.getPhoneNum();
        String productCode = record.getProductCode();
        String enterpriseCode = record.getEnterpriseCode();

        // 得到一个线程对象，prototype型
        ChargeTask task = (ChargeTask) beanFactory.getBean("SCchargeTask");
        task.setConnectParams(enterpriseCode, productCode, phoneNum, chargeRecordId);
        ChargeResult chargeResult = null;
        try {

            Future<ChargeResult> threadResult = threadPoolTaskExecutor.submit(task);
            logger.info("threadPoolTaskExecutor getActiveCount:" + threadPoolTaskExecutor.getActiveCount());
            chargeResult = threadResult.get();

            return chargeResult;

        } catch (InterruptedException e) {// 线程中断异常，认为是线程没有执行完成，此处是boss中断
            chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.CONNECT_FAILURE, "连接boss失败");
            return chargeResult;

        } catch (TaskRejectedException e) {// 任务被线程池拒绝异常
            com.cmcc.webservice.constants.ChargeRecordStatus.ChargeMsg msg = com.cmcc.webservice.constants.ChargeRecordStatus.ChargeMsg.system_exception;
            chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.SYSTEM_ERROR, msg.getMsg());
            logger.error("EC同步接口线程池TaskRejectedException异常:{},chargeRecordId={}", e.getMessage(), chargeRecordId);
            return chargeResult;

        } catch (ExecutionException e) {// 线程运行异常
            com.cmcc.webservice.constants.ChargeRecordStatus.ChargeMsg msg = com.cmcc.webservice.constants.ChargeRecordStatus.ChargeMsg.system_exception;
            chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.SYSTEM_ERROR, msg.getMsg());
            logger.error("EC同步接口线程池ExecutionException异常:{},chargeRecordId={}", e.getMessage(), chargeRecordId);
            return chargeResult;
        } finally {
            // 更新数据库中，该笔状态为充值成功或失败
            updateStatus(record.getId(), chargeResult);
        }
    }

    /**
     * 更新充值的状态
     */
    private void updateStatus(Long recordId, ChargeResult result) {
        if (recordId == null || result == null) {
            return;
        }
        if (result.getCode().equals(ChargeResult.ChargeResultCode.SUCCESS)) { // 充值成功
            interfaceRecordService.updateChargeStatus(recordId, ChargeRecordStatus.COMPLETE, result.getFailureReason());
        } else {
            interfaceRecordService.updateChargeStatus(recordId, ChargeRecordStatus.FAILED, result.getFailureReason());
        }
    }

}
