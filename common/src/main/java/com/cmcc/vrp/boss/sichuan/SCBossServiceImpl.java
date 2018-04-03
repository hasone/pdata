package com.cmcc.vrp.boss.sichuan;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.SyncAccountResult;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceRequest;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceResponse;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceResponseOutData;
import com.cmcc.vrp.boss.sichuan.model.SCChargeRequest;
import com.cmcc.vrp.boss.sichuan.model.SCChargeResponse;
import com.cmcc.vrp.boss.sichuan.service.SCAddMemberService;
import com.cmcc.vrp.boss.sichuan.service.ScMemberInquiryService;
import com.cmcc.vrp.enums.EnterpriseStatus;
import com.cmcc.vrp.enums.InterfaceStatus;
import com.cmcc.vrp.enums.MessageType;
import com.cmcc.vrp.province.model.EntECRecord;
import com.cmcc.vrp.province.model.EntStatusRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseSmsTemplate;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.EntECRecordService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EntStatusRecordService;
import com.cmcc.vrp.province.service.EnterpriseSmsTemplateService;
import com.cmcc.vrp.province.service.EnterpriseUserIdService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.SizeUnits;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.webservice.sichuan.pojo.ReturnCode;

/**
 * 实现BOSS的接口，并将它转化成平台定义的BOSS接口实现 <p> Created by sunyiwei on 2016/4/8.
 */
@Component
public class SCBossServiceImpl implements BossService {
    private final static Logger LOGGER = LoggerFactory.getLogger(SCBossServiceImpl.class);

    @Autowired
    SCAddMemberService scAddMemberService;

    @Autowired
    EnterpriseUserIdService enterpriseUserIdService;

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    SupplierProductService supplierProductService;

    @Autowired
    EnterpriseSmsTemplateService enterpriseSmsTemplateService;

    @Autowired
    com.cmcc.vrp.boss.sichuan.service.SCBalanceService SCBalanceService;

    @Autowired
    GlobalConfigService globalConfigService;

    @Autowired
    EntProductService entProductService;

    @Autowired
    AccountService accountService;
    
    @Autowired
    EntStatusRecordService entStatusRecordService;
    
    @Autowired
    EntECRecordService entECRecordService;
    
    @Autowired
    ScMemberInquiryService scMemberInquiryService;
    public static void main(String[] args) throws ParseException {      
        System.out.println(DateUtil.getDateRange("20160201000000")[1]);
    }

    @Autowired
    TaskProducer taskProducer;

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("四川charge method start!");
        Enterprise enterprise = null;
        SupplierProduct supplierProduct = null;

        if (entId == null
                || (enterprise = enterprisesService.selectByPrimaryKey(entId)) == null
                || splPid == null
                || (supplierProduct = supplierProductService.selectByPrimaryKey(splPid)) == null
                || !StringUtils.isValidMobile(mobile)) {
            return new SCBossOperationResultImpl(ReturnCode.parameter_error);
        }
        
        Integer preInterfaceFlag = enterprise.getInterfaceFlag();

        if ("v".equals(supplierProduct.getFeature())) {
            Integer dateRanage = scMemberInquiryService.getdayRange(mobile);
            if (dateRanage == null) {
                LOGGER.info("用户网龄查询失败!" + mobile);
                return new SCBossOperationResultImpl(ReturnCode.no_date_range);
            }

            if (dateRanage < 1) {
                return new SCBossOperationResultImpl(ReturnCode.over_date_range);
            }
           
        }

        // 检查是否在对账日
        if (duringAccountCheckDate(getCheckAccountDateType(),
                getCheckAccountStartTime(), getCheckAccountStartDay(),
                getCheckAccountEndTime(), getCheckAccountEndDay())) {
            LOGGER.info("当前时间是出账日，无法充值");
            return new SCBossOperationResultImpl(ReturnCode.ACCOUNT_CHECK_DATE_ERROR);
        }

        SCChargeRequest request = new SCChargeRequest();
        
        String userId = enterpriseUserIdService.getUserIdByEnterpriseCode(enterprise.getCode());
        request.setPhone_no(mobile);
        request.setUserId(userId);
        request.setPrcId(supplierProduct.getCode());

        //判断是否进行余额检查，余额小于等于0则暂停企业
        String config = globalConfigService.get("SC_BOSSBALANCE_CHECK");
        String money = globalConfigService.get(GlobalConfigKeyEnum.SC_ACCOUNT_BALANCE.getKey());
        if (!StringUtils.isEmpty(config)
                && "OK".equals(config)
                && needCheckCount(enterprise.getCode())//临时加的，为了满足咪咕公司的需要
                && !StringUtils.isEmpty(money)) {
            SCBalanceRequest banlanceRequest = new SCBalanceRequest();
            banlanceRequest.setPhoneNo(userId);
            //SCBalanceResponse balanceResponse = SCBalanceService.sendBalanceRequest(banlanceRequest);
            SCBalanceResponse balanceResponse;
            System.out.println(globalConfigService.get(GlobalConfigKeyEnum.SC_MOCK_BOSS.getKey()));
            if(isMockBoss()){
                balanceResponse = new SCBalanceResponse();
                balanceResponse.setResCode("0000000");
                SCBalanceResponseOutData outData = new SCBalanceResponseOutData();
                outData.setPREPAY_FEE(getMockAccountBalance());
                balanceResponse.setOutData(outData);
            }else{
                balanceResponse = SCBalanceService.sendBalanceRequest(banlanceRequest);
            }
            
            if (balanceResponse.getResCode().equals("0000000")
                    && Integer.parseInt(balanceResponse.getOutData().getPREPAY_FEE()) <= Integer.parseInt(money) * 100) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                LOGGER.info("时间：" + sdf.format(new Date()) + "企业280:" + enterprise.getCode() + "，企业202:" + userId + "。企业boss处余额"
                        + balanceResponse.getOutData().getPREPAY_FEE() + "分，不足" + Integer.parseInt(money) * 100 + "！将暂停企业");
                //账户余额不足则返回错误，并且暂停企业
                enterprise.setDeleteFlag(2);
                enterprise.setInterfaceFlag(0);
                if (enterprisesService.updateByPrimaryKeySelective(enterprise)) {
                    LOGGER.info("企业Id:" + enterprise.getId() + "暂停企业、关闭EC接口成功");
                } else {
                    LOGGER.info("企业Id:" + enterprise.getId() + "暂停企业、关闭EC接口失败");
                }
                
                if(!insertEnterStatusRecord(enterprise.getId())){
                    LOGGER.info("企业Id:" + enterprise.getId() + "自动关停插入EnterStatus表失败");
                }
                if(!insertEnterEcStatusRecord(enterprise.getId(), preInterfaceFlag)){
                    LOGGER.info("企业Id:" + enterprise.getId() + "自动关停插入EnterEc表失败");
                }

                SCChargeResponse scChargeResponse = new SCChargeResponse();
                scChargeResponse.setResMsg("余额不足");
                return buildResult(scChargeResponse);
            }
        }

        //SCChargeResponse scChargeResponse = scAddMemberService.sendChargeRequest(request);
        SCChargeResponse scChargeResponse;
        if(isMockBoss()){
            scChargeResponse = new SCChargeResponse();
            scChargeResponse.setResCode("0000000");
            scChargeResponse.setResMsg("success");
        }else{
            scChargeResponse = scAddMemberService.sendChargeRequest(request);
        }

        // 充值成功，发个短信
        if (scChargeResponse != null && "0000000".equals(scChargeResponse.getResCode())) {
            String productSize = String.valueOf(SizeUnits.KB.toMB(prdSize));
            EnterpriseSmsTemplate smsmTemplate = enterpriseSmsTemplateService
                    .getChoosedSmsTemplate(entId);
            if (smsmTemplate != null) {
                String template = smsmTemplate.getContent();
                String content = MessageFormat.format(template,
                        productSize, enterprise.getEntName());
                // 将短信扔到相应的队列中
                taskProducer.produceDeliverNoticeSmsMsg(new SmsPojo(mobile,
                        content, productSize, enterprise.getEntName(), MessageType.CHARGE_NOTICE.getCode(),
                        smsmTemplate.getName()));
            }
        }

        return buildResult(scChargeResponse);
    }

    private boolean needCheckCount(String code) {
        String codeString = globalConfigService.get(GlobalConfigKeyEnum.SC_MIGU_CODE.getKey());
        if (StringUtils.isEmpty(codeString)) {
            return true;
        }
        String[] codes = codeString.split(",");
        for (int i = 0; i < codes.length; i++) {
            if (codes[i].equals(code)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        SyncAccountResult sar = accountService.syncFromBoss(entId, prdId);
        LOGGER.info("企业{}，产品{}，同步余额结果{}", entId, prdId, JSONObject.toJSONString(sar));
        return sar != null && SyncAccountResult.isSuccess(sar);
    }

    @Override
    public String getFingerPrint() {
        return "sc";
    }

    private BossOperationResult buildResult(SCChargeResponse response) {
        if (response == null) {
            return new SCBossOperationResultImpl(ReturnCode.no_response); 
        }
        SCBossOperationResultImpl sbori = new SCBossOperationResultImpl();

        sbori.setResultCode(response.getResCode());
        String returnMsg = response.getResMsg();
        if (StringUtils.isEmpty(returnMsg)) {
            returnMsg = "用户状态异常";
        }
        sbori.setResultDesc(returnMsg);


        return sbori;
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        // TODO Auto-generated method stub
        return null;
    }

    private boolean duringAccountCheckDate(String type, String startTime,
                                           String startDay, String endTime, String endDay) {
        if (org.springframework.util.StringUtils.isEmpty(type) || org.springframework.util.StringUtils.isEmpty(startTime)
                || org.springframework.util.StringUtils.isEmpty(startDay)
                || org.springframework.util.StringUtils.isEmpty(endTime) || org.springframework.util.StringUtils.isEmpty(endDay)) {
            return false;
        }
        Date now = new Date();
        Calendar a = Calendar.getInstance();

        int nowDay = now.getDate();
        int monthEndDay;
        int monthBeginDay;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if ("0".equals(type)) {
            return false;
        }
        if ("1".equals(type)) {
            a.set(Calendar.DATE, Integer.parseInt(endDay));// 把日期设置为当月第一天
            Date begin = a.getTime();

            Date beginDate = null;
            Date endDate = null;
            try {
                String beginStr = dateFormat.format(begin);
                beginDate = sdf.parse(beginStr + " " + endTime);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
            a.roll(Calendar.DATE, -1 * Integer.parseInt(startDay));// 日期回滚一天，也就是最后一天
            Date end = a.getTime();
            try {
                String endStr = dateFormat.format(end);
                endDate = sdf.parse(endStr + " " + startTime);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            if ((endDate != null && now.after(endDate))
                    || (beginDate != null && now.before(beginDate))) {
                return true;
            }
        }
        if ("2".equals(type)) {
            monthEndDay = Integer.parseInt(startDay);
            monthBeginDay = Integer.parseInt(endDay);
            if (monthEndDay > monthBeginDay) {
                if (nowDay > monthEndDay || nowDay < monthBeginDay) {
                    return true;
                }

                String str = dateFormat.format(now);
                Date date = null;
                try {
                    date = sdf.parse(str + " " + startTime);

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (nowDay == monthEndDay) {
                    if (now.after(date)) {
                        return true;
                    }
                }
                if (nowDay == monthBeginDay) {
                    if (now.before(date)) {
                        return true;
                    }
                }
            } else {
                a.set(Calendar.DATE, monthEndDay);
                Date begin = a.getTime();

                Date beginDate = null;
                Date endDate = null;
                try {
                    String beginStr = dateFormat.format(begin);
                    beginDate = sdf.parse(beginStr + " " + startTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                a.set(Calendar.DATE, monthBeginDay);
                Date end = a.getTime();
                try {
                    String endStr = dateFormat.format(end);
                    endDate = sdf.parse(endStr + " " + endTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (endDate != null && beginDate != null && now.before(endDate)
                        && now.after(beginDate)) {
                    return true;
                }
            }
        }

        return false;
    }

    public String getCheckAccountDateType() {
        return globalConfigService
                .get(GlobalConfigKeyEnum.CHECK_ACCOUNT_DATE_TYPE.getKey());
    }

    public String getCheckAccountStartDay() {
        return globalConfigService
                .get(GlobalConfigKeyEnum.CHECK_ACCOUNT_START_DAY.getKey());
    }

    public String getCheckAccountStartTime() {
        return globalConfigService
                .get(GlobalConfigKeyEnum.CHECK_ACCOUNT_START_TIME.getKey());
    }

    public String getCheckAccountEndDay() {
        return globalConfigService
                .get(GlobalConfigKeyEnum.CHECK_ACCOUNT_END_DAY.getKey());
    }

    public String getCheckAccountEndTime() {
        return globalConfigService
                .get(GlobalConfigKeyEnum.CHECK_ACCOUNT_END_TIME.getKey());
    }
    
    /**
     * 插入企业状态变更表
     */
    public boolean insertEnterStatusRecord(Long entId){
        //生成企业状态变更记录
        EntStatusRecord entStatusRecord = new EntStatusRecord();

        entStatusRecord.setEntId(entId);//企业
        entStatusRecord.setCreateTime(new Date());
        entStatusRecord.setUpdateTime(new Date());
        entStatusRecord.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        entStatusRecord.setPreStatus(0);//企业先前状态，注：enterprise中deleteFlag表企业状态
        entStatusRecord.setNowStatus(2);
        entStatusRecord.setOpType(EnterpriseStatus.PAUSE.getCode());
        entStatusRecord.setOpDesc("BOSS余额查询不足，平台自动关停");
        entStatusRecord.setReason("自动关停");
        return entStatusRecordService.insert(entStatusRecord);
    }
    
    /**
     * 插入企业状态变更表
     */
    public boolean insertEnterEcStatusRecord(Long entId,Integer preInterfaceFlag){
        //生成企业EC接口变更记录
        EntECRecord entECRecord = new EntECRecord();

        entECRecord.setEntId(entId);
        entECRecord.setPreStatus(preInterfaceFlag);
        entECRecord.setCreateTime(new Date());
        entECRecord.setUpdateTime(new Date());
        entECRecord.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        entECRecord.setOpType(InterfaceStatus.CLOSE.getCode());
        entECRecord.setOpDesc("BOSS余额查询不足，平台自动关停");
        entECRecord.setNowStatus(0);
        return entECRecordService.insert(entECRecord);
    }
    
    private boolean isMockBoss(){   
        return "true".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.SC_MOCK_BOSS.getKey()));
    }
    
    private String getMockAccountBalance(){
        return globalConfigService.get(GlobalConfigKeyEnum.SC_MOCK_ACCOUNT_BALANCE.getKey());
    }
}
