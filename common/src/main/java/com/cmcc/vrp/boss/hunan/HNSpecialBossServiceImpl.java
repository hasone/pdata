package com.cmcc.vrp.boss.hunan;

import com.google.gson.Gson;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.hunan.model.HNFreeOfChargeBossOperationResult;
import com.cmcc.vrp.boss.hunan.model.HNFreeOfChargeReq;
import com.cmcc.vrp.boss.hunan.model.HNFreeOfChargeResp;
import com.cmcc.vrp.boss.hunan.model.HNOperation;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 湖南0元订购接口
 * 湖南0元订购接口上游平台由BOSS切换至能力开发平台，接口规范发生变更，故该接口实现类已废弃不用。废弃时间：2017.5.25
 * <p>
 * Created by sunyiwei on 2016/9/19.

 */
@Service
@Component("huNanSpecialBossService")
public class HNSpecialBossServiceImpl extends HNBossServcieImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(HNSpecialBossServiceImpl.class);

    @Autowired
    private SupplierProductService supplierProductService;

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        SupplierProduct sp = null;
        if ((sp = supplierProductService.selectByPrimaryKey(splPid)) == null) {
            return build("12345", "无法找到相应的供应商产品");
        }

        String code = sp.getCode();
        HNFreeOfChargeReq hcq = buildReq(mobile, code);

        String url = getBossUrl();
        String reqStr = buildReqStr(hcq); //已经带上?了，这个渠道只能从url的queryString里获取参数...我也没办法
        LOGGER.info("向湖南BOSS[URL = {}]电渠POST请求报文内容为: {}.", url, reqStr);

        String resp = HttpUtils.post(url + reqStr, "", "application/json");
        LOGGER.info("湖南BOSS电渠响应报文内容为：{}.", resp);

        return buildResult(resp);
    }

    //构建充值请求包体
    private String buildReqStr(HNFreeOfChargeReq hcq) {
        TreeMap<String, String> map = new TreeMap<String, String>();

        //通用参数
        map.put("appid", getBossAppid());
        map.put("timestamp", new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
        map.put("format", getBossFormat());
        map.put("method", getFreeOfChargeMethod());
        map.put("sign_method", "MD5");
        map.put("version", getBossVersion());
        map.put("locale", getBossLocale());
        map.put("status", getBossStatus());

        //方法特有的参数
        map.put("mobileNum", hcq.getMobileNum());
        map.put("packages", hcq.getPackages());
        map.put("operation", hcq.getOperation());

        //签名
        map.put("sign", buildSign(map, getBossSecret()));

        return convertToString(map);
    }

    //转化成字符串
    private String convertToString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder("?");

        for (String key : map.keySet()) {
            sb.append(key).append("=").append(map.get(key)).append("&");
        }

        String reqString = sb.toString();
        return reqString.substring(0, reqString.length() - 1); //排除最后一个&
    }

    //根据map值构建sign值
    private String buildSign(TreeMap<String, String> map, String bossSecret) {
        StringBuilder sb = new StringBuilder(bossSecret);

        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            sb.append(key).append(map.get(key));
        }

        sb.append(bossSecret);
        LOGGER.info("加密前的报文为{}.", sb.toString());

        String result = DigestUtils.md5Hex(sb.toString()).toUpperCase();
        LOGGER.info("加密后的报文为{}.", result);
        return result;
    }

    private BossOperationResult buildResult(String resp) {
        if (StringUtils.isBlank(resp)) {
            return build("99999", "BOSS侧响应内容为空");
        }

        try {
            HNFreeOfChargeResp hcr = new Gson().fromJson(resp, HNFreeOfChargeResp.class);
            return build(hcr.getRes_code(), hcr.getRest_desc());
        } catch (Exception e) {
            LOGGER.error("解析BOSS侧响应内容时出错.");
            return build("88888", "解析BOSS侧响应内容时出错");
        }
    }

    private HNFreeOfChargeReq buildReq(String mobile, String code) {
        HNFreeOfChargeReq hcq = new HNFreeOfChargeReq();
        hcq.setMobileNum(mobile);
        hcq.setPackages(code);
        hcq.setOperation(HNOperation.ORDER.getCode());

        return hcq;
    }

    private BossOperationResult build(String resultCode, String resultDesc) {
        HNFreeOfChargeBossOperationResult hcbor = new HNFreeOfChargeBossOperationResult();
        hcbor.setResultDesc(resultDesc);
        hcbor.setResultCode(resultCode);

        return hcbor;
    }

    @Override
    public String getFingerPrint() {
        return "hunanFreeOfChargeOld";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }

    private String getFreeOfChargeMethod() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_FREE_OF_CHARGE_METHOD.getKey());
    }
}
