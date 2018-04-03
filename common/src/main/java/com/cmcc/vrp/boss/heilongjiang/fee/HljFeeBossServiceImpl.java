package com.cmcc.vrp.boss.heilongjiang.fee;

import java.util.Date;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;

@Service
public class HljFeeBossServiceImpl implements BossService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HljFeeBossServiceImpl.class);

    @Autowired
    private HLJFeeService hLJFeeService;

    @Autowired
    private SupplierProductService productService;

    @Autowired
    private SerialNumService serialNumService;

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("entId:{}, splPid:{} ,mobile:{}, serialNum:{}", entId, splPid, mobile, serialNum);
        GroupPersonRequest request = null;
        HljFeeBossOperationResult result = null;
        SupplierProduct sPrdouct = null;
        Long size;
        if (entId == null
                || splPid == null
                || !com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)
                || StringUtils.isBlank(serialNum)
                || (sPrdouct = productService.selectByPrimaryKey(splPid)) == null
                || prdSize == null) {
            return new HljFeeBossOperationResult(HljFeeResultCode.PARA_ILLEGALITY);
        }
        String bossReqNum = SerialNumGenerator.buildNormalBossReqNum("hlj_fee", 32);
        try {
            Feature f = (Feature) JsonUtil.fromJson(sPrdouct.getFeature(), Feature.class);
            request = new GroupPersonRequest();
            request.setAccNbr(mobile);
            request.setAppKey(f.getAppKey());
            request.setGroupAccNo(f.getGroupAccNo());
            request.setGroupNo(f.getGroupNo());
            request.setOrderDate(DateUtil.formatDate(new Date(), HljConstants.ORDER_DATE_PATTERN));
            request.setOrderNo(bossReqNum);
            request.setTimestamp(new Date());
            double totalFee = (double) prdSize / 100;
            request.setTotalFee(String.valueOf(totalFee));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            result = new HljFeeBossOperationResult(HljFeeResultCode.CODE_PARAM_JSON_ERROR);
        }

        if (request != null) {  // request encapsulation is OK.
            GroupPersonResponse resp = hLJFeeService.groupPerson(request);
            if (resp != null) {
                updateSerialNum(serialNum, resp.getBossOrderNo(), bossReqNum);
                String respCode = resp.getResCode();
                if (respCode != null && respCode.equals(HljConstants.SUCCESS)) {
                    //充值成功更新流水关系
                    result = new HljFeeBossOperationResult(HljFeeResultCode.SUCCESS);
                    result.setFingerPrint(getFingerPrint());
                    result.setSystemNum(serialNum);
                    result.setEntId(entId);
                } else {
                    result = new HljFeeBossOperationResult(HljFeeResultCode.FAILD);
                    result.setFingerPrint(getFingerPrint());
                    result.setSystemNum(serialNum);
                    result.setEntId(entId);
                }
            } else {
                updateSerialNum(serialNum, SerialNumGenerator.buildNullBossRespNum("hlj_fee"), bossReqNum);
                result = new HljFeeBossOperationResult(HljFeeResultCode.FAILD);
                result.setFingerPrint(getFingerPrint());
                result.setSystemNum(serialNum);
                result.setEntId(entId);
            }
        }
        return result;
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

    private void updateSerialNum(String serialNum, String bossRespNum, String bossReqNum) {
        if (updateRecord(serialNum, bossRespNum, bossReqNum)) {
            LOGGER.info("记录平台流水和BOSS返回流水号的关系成功");
        } else {
            LOGGER.error("记录平台流水和BOSS返回流水号的关系失败");
        }
    }


    @Override
    public String getFingerPrint() {
        return "hlj_fee";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return false;
    }

    public static void main(String[] args) {
        Feature feature = new Feature();
        feature.setAppKey("xxxx");
        feature.setGroupNo("xxxxx");
        feature.setGroupAccNo("xxxx");
        String s = JsonUtil.getJson(feature);
        System.out.println(s);
    }
}
