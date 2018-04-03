package com.cmcc.vrp.boss.fujian;

import com.cmcc.api.fj.payflow.bean.LLTFMemberOrder;
import com.cmcc.api.fj.payflow.bean.ResultInfo;
import com.cmcc.api.fj.payflow.service.PayFlowService;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.fujian.model.FjReturnCode;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Hashtable;

/**
 * Created by leelyn on 2016/7/27.
 */
@Service
public class FjBossServiceImpl implements BossService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FjBossServiceImpl.class);

    @Autowired
    private SupplierProductService productService;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private SerialNumService serialNumService;

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("福建渠道开始充值");
        SupplierProduct sPrdouct;
        String pCode;
        if (entId == null
                || splPid == null
                || !com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)
                || StringUtils.isBlank(serialNum)
                || (sPrdouct = productService.selectByPrimaryKey(splPid)) == null
                || StringUtils.isBlank(pCode = sPrdouct.getCode())) {
            return new FjBossOperationResultImpl(FjReturnCode.PARA_ILLEGALITY);
        }

        String bossReqNum = SerialNumGenerator.buildNormalBossReqNum("fujian", 25);
        LLTFMemberOrder lltfMemberOrder = new LLTFMemberOrder();

        lltfMemberOrder.setECCode(getEccode());//集团编码：必填
        lltfMemberOrder.setNumber(mobile);
        lltfMemberOrder.setOprCode("01");//操作编码：必填,01=开通、02=退订、03=调整成员套餐或流量值
        lltfMemberOrder.setMode("3");//统付模式
        lltfMemberOrder.setDealID(pCode);//套餐编码,统付模式=定额模式必填
        //lltfMemberOrder.setMemberAmount("5"); //成员流量：统付模式=定量模式时必填，单位MB
        lltfMemberOrder.setEffType("1");//生效模式：必填，默认=2，1=立即生效、2=次月生效
        lltfMemberOrder.setEndTime(DateUtil.getFjBossEndTime()); //失效日期：必填，通用包订购的时候时间格式为YYYYMM，流量池订购的时候时间格式为 YYYYMMDD
        lltfMemberOrder.setSerialNumber(bossReqNum);
        lltfMemberOrder.setECMemberOrderID(bossReqNum);

        try {
            /*Hashtable respHashtable = new Hashtable();*/
            PayFlowService flowService = new PayFlowService(getAppId(), getAppKey(), getOrderUrl());
            //发送请求并接受响应内容
            LOGGER.info("福建渠道充值请求地址:{},请求包体：{}", getOrderUrl(), new Gson().toJson(lltfMemberOrder));
            Hashtable respHashtable = flowService.orderMemberProduct(lltfMemberOrder);
            //异常信息bean
            ResultInfo resultInfo = (ResultInfo) respHashtable.get("ResultInfo");
            //流量统付集团成员产品受理应答结果Bean
//            LLTFMemberOrderResult lltfMemberOrderResult = (LLTFMemberOrderResult) respHashtable.get("LLTFMemberOrderResult");
            //输出异常编码
            if (resultInfo != null) {
                LOGGER.info("福建渠道充值响应:{}", new Gson().toJson(resultInfo));
            }
            if (resultInfo != null
                    && resultInfo.getResultCode().equals(FjReturnCode.SUCCESS.getCode())) {
                // 充值成功了更新流水号
                if (!updateRecord(serialNum, SerialNumGenerator.buildNullBossRespNum("fujian"), bossReqNum)) {
                    LOGGER.error("更新流水失败");
                }
                return new FjBossOperationResultImpl(FjReturnCode.SUCCESS);
            }
            LOGGER.error("fujian boss channel charge faild,errCode:{}.errMsg:{}", resultInfo == null ? "" : resultInfo.getResultCode(),
                    resultInfo == null ? "" : resultInfo.getResultMsg());
        } catch (Exception e) {
            LOGGER.error("fujian boss channel charge faild,throw exception:{}", e.getMessage());
        }
        // 充值失败了更新流水号
        if (!updateRecord(serialNum, SerialNumGenerator.buildNullBossRespNum("fujian"), bossReqNum)) {
            LOGGER.error("更新流水失败");
        }
        return new FjBossOperationResultImpl(FjReturnCode.FAILD);
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

    @Override
    public String getFingerPrint() {
        return "fujian123456789";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }


    private String getAppId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_FUJIAN_APPID.getKey());
    }

    private String getAppKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_FUJIAN_APPKEY.getKey());
    }

    private String getEccode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_FUJIAN_ECCODE.getKey());
    }

    private String getOrderUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_FUJIAN_ORDER_URL.getKey());
    }
}
