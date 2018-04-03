package com.cmcc.vrp.boss.zhejiang;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.zhejiang.model.ZjBossOperationResultImpl;
import com.cmcc.vrp.boss.zhejiang.model.ZjChargeReq;
import com.cmcc.vrp.boss.zhejiang.model.ZjChargeResp;
import com.cmcc.vrp.boss.zhejiang.model.ZjErrorCode;
import com.cmcc.vrp.boss.zhejiang.utils.TimestampUtil;
import com.cmcc.vrp.boss.zhejiang.utils.ZjHttpUtil;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by leelyn on 2016/7/28.
 */
@Service
public class ZjBossServiceImpl implements BossService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZjBossServiceImpl.class);

    @Autowired
    private SupplierProductService productService;

    @Autowired
    private GlobalConfigService configService;

    @Autowired
    private Gson gson;

    @Autowired
    private SerialNumService serialNumService;

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("浙江渠道充值开始");
        SupplierProduct sPrdouct;
        String pCode = null;
        if (entId == null
                || splPid == null
                || !com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)
                || StringUtils.isBlank(serialNum)
                || (sPrdouct = productService.selectByPrimaryKey(splPid)) == null
                || StringUtils.isBlank(pCode = sPrdouct.getCode())) {
            LOGGER.error("Zhejiang charge requset Para is null");
            return new ZjBossOperationResultImpl(ZjErrorCode.PARA_ERROR);
        }
        //生成交易日期 请求流水
        String tradeStr = TimestampUtil.getTradeDate();
        String random = SerialNumGenerator.genRandomNum(8);
        String bossReqNum = "LLSL" + tradeStr + getBizCode() + random;

        ZjChargeReq req = buildZJReq(mobile, bossReqNum, pCode, tradeStr);
        LOGGER.info("浙江渠道充值请求包体:{}", new Gson().toJson(req));

        String result = ZjHttpUtil.doPost(getChargeUrl(), gson.toJson(req), getAppkey(), getAppsecrect());
        if (StringUtils.isNotBlank(result)) {
            LOGGER.info("浙江渠道充值响应包体:{}", result);
        }

        ZjChargeResp resp = gson.fromJson(result, ZjChargeResp.class);

        if (resp != null
                && resp.getCode().equals(ZjErrorCode.SUCCESS.getCode())) {
            //充值成功更新流水关系
            if (updateRecord(serialNum, resp.getResult().getOsbSerialNo(), bossReqNum)) {
                LOGGER.info("记录平台流水和BOSS返回流水号的关系成功");
            } else {
                LOGGER.error("记录平台流水和BOSS返回流水号的关系失败");
            }
            return new ZjBossOperationResultImpl(ZjErrorCode.SUCCESS);
        }
        //充值失败更新流水关系
        if (updateRecord(serialNum, resp == null ? SerialNumGenerator.buildNullBossRespNum("zhejiang") : resp.getResult().getOsbSerialNo(), bossReqNum)) {
            LOGGER.info("记录平台流水和BOSS返回流水号的关系成功");
        } else {
            LOGGER.error("记录平台流水和BOSS返回流水号的关系失败");
        }
        return new ZjBossOperationResultImpl(ZjErrorCode.FAILD);
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
        return "zhejiang123456789";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    /**
     * 构建充值请求包体
     *
     * @param mobile
     * @param bossReqNum
     * @param pCode
     * @return
     */
    private ZjChargeReq buildZJReq(String mobile, String bossReqNum, String pCode, String tradeStr) {
        ZjChargeReq req = new ZjChargeReq();
        req.setTradeDate(tradeStr);
        req.setAccountDate(TimestampUtil.getAccountDate());
        req.setTradeSerialNo(bossReqNum);
        req.setOptCode("LLSL");
        req.setOfferId(pCode);
        req.setBillId(mobile);
        req.setBizCode(getBizCode());
        return req;
    }

    public String getChargeUrl() {
        return configService.get(GlobalConfigKeyEnum.BOSS_ZJ_CHARGE_URL.getKey());
    }

    public String getAppkey() {
        return configService.get(GlobalConfigKeyEnum.BOSS_ZJ_CHARGE_APPKEY.getKey());
    }

    public String getAppsecrect() {
        return configService.get(GlobalConfigKeyEnum.BOSS_ZJ_CHARGE_APPSECRET.getKey());
    }

    public String getBizCode() {
        return configService.get(GlobalConfigKeyEnum.BOSS_ZJ_CHARGE_BIZCODE.getKey());
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }
}
