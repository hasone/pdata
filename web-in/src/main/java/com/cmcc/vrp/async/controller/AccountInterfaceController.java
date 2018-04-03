package com.cmcc.vrp.async.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ApprovalRequestStatus;
import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.enums.SdAccountRecordStatus;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.AccountChangeDetail;
import com.cmcc.vrp.province.model.AccountChangeOperator;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SdAccountChargeRecord;
import com.cmcc.vrp.province.service.AccountChangeOperatorService;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SdAccountChargeRecordService;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.google.gson.Gson;

/**
 * 
 * @ClassName: AccountInterfaceController 
 * @Description: 山东流量平台预付费账户充值
 * @author: Rowe
 * @date: 2017年9月1日 下午5:14:25
 */
@Controller
@RequestMapping(value = "/accounts")
public class AccountInterfaceController {

    private static final Logger logger = LoggerFactory.getLogger(AccountInterfaceController.class);

    private static final int MAX_LENGTH_255 = 255;
    private static final int MAX_LENGTH_64 = 64;

    private static final String SD_CLOUD_CODE = "99999";//山东云平台

    @Autowired
    private AdministerService administerService;

    @Autowired
    private EnterprisesService enterprisesService;

    @Autowired
    private SdAccountChargeRecordService sdAccountChargeRecordService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ProductService productService;

    @Autowired
    ApprovalRequestService approvalRequestService;

    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;

    @Autowired
    AccountChangeOperatorService accountChangeOperatorService;

    /**
     * 
     * @Title: accountOperation 
     * @Description: TODO
     * @param request
     * @param response
     * @throws IOException
     * @return: void
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void accountOperation(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {

        String appKey = null;//企业APPKEY
        String systemSerialNum = null;//系统流水号

        //校验认证返回的参数,返回appKey和 systemSerialNum,否则认为认证失败，返回403
        if (StringUtils.isBlank(appKey = (String) request.getAttribute(Constants.APP_KEY_ATTR))
                || StringUtils.isBlank(systemSerialNum = (String) request.getAttribute(Constants.SYSTEM_NUM_ATTR))) {
            logger.error("认证未通过, AppKey = {}, SystemSerialNum = {}.", appKey, systemSerialNum);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        //校验是否是山东云平台发送的请求
        Enterprise enterprise = enterprisesService.selectByAppKey(appKey);
        if (enterprise == null || DELETE_FLAG.UNDELETED.getValue() != enterprise.getDeleteFlag().intValue()
                || !SD_CLOUD_CODE.equalsIgnoreCase(enterprise.getCode())) {
            logger.error("非山东云平台推送消息,约定只能使用山东云平台【code = " + SD_CLOUD_CODE + "】推送消息.");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String json = (String) request.getAttribute(Constants.BODY_XML_ATTR);//请求报文参数获取，JSON格式字符串
        logger.info("预付费账户接口收到报文：" + json);
        int httpCode = HttpServletResponse.SC_BAD_REQUEST;//默认操作失败，参数缺失或者非法，返回400
        try {
            //预付费账户逻辑处理
            if (StringUtils.isNotBlank(json) && handleAccount(json)) {//操作成功，返回http状态码200
                httpCode = HttpServletResponse.SC_OK;//http状态码200
            }
        } catch (Exception e) {
            logger.error("参数错误或操作失败:" + json);
            e.printStackTrace();
        }
        response.setStatus(httpCode);//设置http状态码
        logger.info("预付费账户接口响应状态码：" + httpCode);
        return;
    }

    /**
     * 
     * @Title: jsonToEnterpise 
     * @Description: 接口报文转为Model类型
     * @param json
     * @return
     * @return: Enterprise
     */
    private SdAccountChargeRecord jsonToSdAccount(String json) {
        //约定的报文格式
        //        { 
        //         “opr”:”pay”,  //固定值
        //         “type”：”NEW” 必填
        //            “info” : {
        //         "PkgSeq":[发起方交易包流水号], 必填
        //         "ECID":[集团客户ECID], 必填
        //         "AcctID:[账户编码],
        //         "UserID":[产品实例], 必填
        //         "ProductID":[主产品ID ],
        //         "Pay":[充值金额 ],必填
        //         "OprEffTime":[充值时间], 必填
        //         "AcctSeq":[充值流水号], 
        //         "AcctNo":[充值序列号],
        //         "Reserved":[{
        //         "ParamName"：[预留参数名],
        //         "ParamValue"：[预留参数值]
        //         },
        //         {
        //         "ParamName"：[预留参数名],
        //         "ParamValue"：[预留参数值]
        //         }
        //         ]
        //         }
        //         }

        JSONObject jo = JSONObject.parseObject(json);
        String accountInfo = jo.getString("info");
        SdAccountChargeRecord sdAccountChargeRecord = JSON.parseObject(accountInfo, SdAccountChargeRecord.class);
        sdAccountChargeRecord.setCreateTime(new Date());
        sdAccountChargeRecord.setUpdateTime(new Date());
        sdAccountChargeRecord.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        sdAccountChargeRecord.setRequestBody(json);
        sdAccountChargeRecord.setOpr(jo.getString("opr"));
        sdAccountChargeRecord.setType(jo.getString("type"));
        return sdAccountChargeRecord;
    }

    /**
     * 
     * @Title: checkAccountRecord 
     * @Description: 企业账户报文校验
     * @param sdAccountChargeRecord
     * @return
     * @return: boolean
     */
    private boolean checkAccountRecord(SdAccountChargeRecord sdAccountChargeRecord) {
        if (sdAccountChargeRecord == null) {
            return false;
        } else if (StringUtils.isBlank(sdAccountChargeRecord.getOpr())
                || sdAccountChargeRecord.getOpr().length() > MAX_LENGTH_255) {
            logger.error("账户信息报文校验失败：opr非法，约定opr非空且最大长度为" + MAX_LENGTH_255 + ",实际opr="
                    + sdAccountChargeRecord.getEcid());
            return false;
        } else if (StringUtils.isBlank(sdAccountChargeRecord.getType())
                || sdAccountChargeRecord.getType().length() > MAX_LENGTH_64) {
            logger.error("账户信息报文校验失败：type非法，约定type非空且最大长度为" + MAX_LENGTH_64 + ",实际type="
                    + sdAccountChargeRecord.getType());
            return false;
        } else if (StringUtils.isBlank(sdAccountChargeRecord.getPkgSeq())
                || sdAccountChargeRecord.getPkgSeq().length() > MAX_LENGTH_64) {
            logger.error("账户信息报文校验失败：PkgSeq非法，约定PkgSeq非空且最大长度为" + MAX_LENGTH_64 + ",实际PkgSeq="
                    + sdAccountChargeRecord.getPkgSeq());
            return false;
        } else if (StringUtils.isBlank(sdAccountChargeRecord.getUserId())
                || sdAccountChargeRecord.getOpr().length() > MAX_LENGTH_255) {
            logger.error("账户信息报文校验失败：UserID非法，约定UserID非空且最大长度为" + MAX_LENGTH_255 + ",实际UserID="
                    + sdAccountChargeRecord.getUserId());
            return false;
        } else if (sdAccountChargeRecord.getPay() == null) {
            logger.error("账户信息报文校验失败：Pay非法，约定Pay非空,实际Pay=" + sdAccountChargeRecord.getUserId());
            return false;
        } else if (sdAccountChargeRecord.getOprEffTime() == null) {
            logger.error("账户信息报文校验失败：UserID非法，约定UserID非空,实际UserID=" + sdAccountChargeRecord.getUserId());
            return false;
        } else if (StringUtils.isBlank(sdAccountChargeRecord.getAcctSeq())
                || sdAccountChargeRecord.getAcctSeq().length() > MAX_LENGTH_255) {
            logger.error("账户信息报文校验失败：AcctSeq非法，约定AcctSeq非空且最大长度为" + MAX_LENGTH_255 + ",实际AcctSeq"
                    + sdAccountChargeRecord.getUserId());
            return false;
        } else if (StringUtils.isBlank(sdAccountChargeRecord.getAcctNo())
                || sdAccountChargeRecord.getAcctNo().length() > MAX_LENGTH_255) {
            logger.error("账户信息报文校验失败：AcctNo非法，约定AcctNo非空且最大长度为" + MAX_LENGTH_255 + ",实际AcctNo="
                    + sdAccountChargeRecord.getUserId());
            return false;
        }
        return true;
    }

    private boolean handleAccount(String json) {
        //报文转换
        SdAccountChargeRecord sdAccountChargeRecord = jsonToSdAccount(json);

        //参数校验
        if (!checkAccountRecord(sdAccountChargeRecord)) {
            return false;
        }
        //合法性校验
        //获取企业信息
        Enterprise enterprise = enterprisesService.selectByPhone(sdAccountChargeRecord.getEcid());
        if (enterprise == null) {
            logger.info("企业信息不存在ECID = {}, 请先推送企业信息。 ", sdAccountChargeRecord.getEcid());
            return false;
        }

        //获取产品信息
        List<Product> productList = productService.selectByType(Integer
                .valueOf(com.cmcc.vrp.ec.bean.Constants.ProductType.PRE_PAY_CURRENCY.getValue()));
        if (productList == null || productList.size() <= 0) {
            logger.info("预付产品不存在：type ={}，请先订购预付费产品。 ",
                    com.cmcc.vrp.ec.bean.Constants.ProductType.PRE_PAY_CURRENCY.getValue());
            return false;
        }

        //是否订购该产品
        String productCode = sdAccountChargeRecord.getUserId();//模糊匹配，山东产品编码：userId + productId
        if (StringUtils.isNotBlank(sdAccountChargeRecord.getProductId())) {//精确匹配
            productCode += sdAccountChargeRecord.getProductId();
        }

        boolean isOrder = false;//是否订购该产品
        List<Product> products = productService.selectAllProductsByEnterId(enterprise.getId());
        if (products != null && products.size() > 0) {
            for (Product p : products) {
                if (p.getProductCode().contains(productCode)) {//订购过该产品
                    isOrder = true;
                    break;
                }
            }
        }
        if (!isOrder) {
            logger.info("企业未订购该产品 ECID = {}, USERID = {}, PRODUCTID= {},请先订购。", sdAccountChargeRecord.getEcid(),
                    sdAccountChargeRecord.getUserId(), sdAccountChargeRecord.getProductId());
            return false;
        }

        //获取账户信息：预付费账户信息
        Account account = accountService.get(enterprise.getId(), productList.get(0).getId(),
                AccountType.ENTERPRISE.getValue());
        if (account == null) {
            logger.info("预付费账户信息不存在 entId = {}, prdId = {},请先订购预付费产品。", enterprise.getId(), productList.get(0).getId());
            return false;
        }

        //生成报文记录
        sdAccountChargeRecord.setPlatAccountId(account.getId());
        sdAccountChargeRecord.setStatus(SdAccountRecordStatus.PROCESSING.getCode());
        sdAccountChargeRecord.setMessage(SdAccountRecordStatus.PROCESSING.getMessage());
        if (!sdAccountChargeRecordService.insertSelective(sdAccountChargeRecord)) {
            logger.info("生成接口推送记录失败sdAccountChargeRecord = {}", new Gson().toJson(sdAccountChargeRecord));
            return false;
        }

        //生成提交记录,注意：此处需要单位做转换，报文中单位为分，平台单位为元
        AccountChangeOperator accountChangeOperator = createAccountChangeOperator(enterprise.getId(), productList
                .get(0).getId(), AccountType.ENTERPRISE, Double.valueOf(sdAccountChargeRecord.getPay() * 1.0 / 100),
                SerialNumGenerator.buildSerialNum());
        if (!accountChangeOperatorService.insert(accountChangeOperator)) {
            logger.info("生成提交记录失败");
        }

        //增加账户余额:无审批
        ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                .selectByType(ApprovalType.Account_Change_Approval.getCode());
        AccountChangeDetail accountChangeDetail = createAccountChangeDetail(accountChangeOperator.getId());
        if (approvalRequestService.submitWithoutApproval(
                createApprovalRequest(approvalProcessDefinition.getId(), enterprise.getId(), null), null,
                accountChangeDetail, null, null)) {
            sdAccountChargeRecord.setStatus(SdAccountRecordStatus.SUCCESS.getCode());
            sdAccountChargeRecord.setMessage(SdAccountRecordStatus.SUCCESS.getMessage());
        } else {
            sdAccountChargeRecord.setStatus(SdAccountRecordStatus.FAILURE.getCode());
            sdAccountChargeRecord.setMessage(SdAccountRecordStatus.FAILURE.getMessage());
        }

        //将山东单独的充值记录与平台充值记录关联起来
        sdAccountChargeRecord.setUpdateTime(new Date());
        sdAccountChargeRecord.setChangeDetailId(accountChangeDetail.getId());
        if (!sdAccountChargeRecordService.updateByPrimaryKeySelective(sdAccountChargeRecord)) {
            logger.info("更新账户记录状态失败");
        }
        return true;
    }

    private ApprovalRequest createApprovalRequest(Long processId, Long entId, Long currUserId) {
        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setProcessId(processId);
        approvalRequest.setEntId(entId);
        approvalRequest.setCreatorId(currUserId);
        approvalRequest.setStatus(0);
        approvalRequest.setCreateTime(new Date());
        approvalRequest.setUpdateTime(new Date());
        approvalRequest.setDeleteFlag(0);
        approvalRequest.setResult(ApprovalRequestStatus.APPROVING.getCode());
        return approvalRequest;
    }

    private AccountChangeDetail createAccountChangeDetail(Long id) {
        if (id == null) {
            return null;
        }
        AccountChangeDetail accountChangeDetail = new AccountChangeDetail();
        AccountChangeOperator record = accountChangeOperatorService.selectByPrimaryKey(id);
        accountChangeDetail.setAccountId(record.getAccountId());
        accountChangeDetail.setCount(record.getCount());
        accountChangeDetail.setDeleteFlag(0);
        accountChangeDetail.setProductId(record.getPrdId());
        accountChangeDetail.setSerialNum(record.getSerialNum());
        return accountChangeDetail;
    }

    private AccountChangeOperator createAccountChangeOperator(Long entId, Long prdId, AccountType accountType,
            Double deltaCount, String serialNum) {
        AccountChangeOperator accountChangeOperator = new AccountChangeOperator();
        Account account = accountService.get(entId, prdId, accountType.getValue());
        accountChangeOperator.setCount(deltaCount);
        accountChangeOperator.setEntId(entId);
        accountChangeOperator.setPrdId(prdId);
        accountChangeOperator.setAccountId(account.getId());
        accountChangeOperator.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        accountChangeOperator.setSerialNum(serialNum);
        accountChangeOperator.setCreateTime(new Date());
        accountChangeOperator.setUpdateTime(new Date());
        return accountChangeOperator;
    }
}
