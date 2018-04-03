package com.cmcc.vrp.boss.bjym;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.FeatureBasedBossServiceImpl;
import com.cmcc.vrp.boss.bjym.enums.BjymChargeResponseCodeEnum;
import com.cmcc.vrp.boss.bjym.pojos.BjymBossOperationResult;
import com.cmcc.vrp.boss.bjym.pojos.BjymChargeRequest;
import com.cmcc.vrp.boss.bjym.pojos.BjymChargeResponse;
import com.cmcc.vrp.boss.bjym.pojos.BjymRequestBody;
import com.cmcc.vrp.boss.bjym.pojos.BjymRequestBodySerializer;
import com.cmcc.vrp.boss.bjym.pojos.BjymRequestHeader;
import com.cmcc.vrp.boss.bjym.pojos.BjymUserData;
import com.cmcc.vrp.boss.bjym.pojos.BjymUserDataListSerializer;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * 北京云漫渠道对接
 *
 * Created by sunyiwei on 2017/4/6.
 */
@Service
public class BjymBossServiceImpl extends FeatureBasedBossServiceImpl<BjymChargeRequest, BjymChargeResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BjymBossServiceImpl.class);

    //配置北京云漫请求对象的gson
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(BjymRequestBody.class, new BjymRequestBodySerializer())
            .registerTypeAdapter(BjymUserData.class, new BjymUserDataListSerializer())
            .create();

    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * 返回渠道的名称
     */
    @Override
    protected String getName() {
        return "北京云漫";
    }

    /**
     * 返回参数无效时的结果
     */
    @Override
    protected BossOperationResult getInvalidParamResult() {
        return new BjymBossOperationResult(null, BjymChargeResponseCodeEnum.INVALID_PARAM);
    }

    /**
     * @param responsePojo 响应对象
     * @return 返回充值成功时的结果
     */
    @Override
    protected BossOperationResult parseResult(BjymChargeResponse responsePojo) {
        if (responsePojo == null) {
            return getInvalidParamResult();
        }

        return new BjymBossOperationResult(responsePojo, responsePojo.getCode(), responsePojo.getDescription());
    }

    /**
     * 返回向上游请求时的流水号
     *
     * @param pltSn 平台流水号
     */
    @Override
    protected String getBossReqSerialNum(String pltSn) {
        return pltSn;
    }

    /**
     * 根据供应商产品,手机号以及平台流水号构建请求对象
     *
     * @param supplierProduct 供应商产品
     * @param mobile          手机号
     * @param pltSn           平台流水号
     * @return 请求对象
     */
    @Override
    protected BjymChargeRequest buildRequestPojo(SupplierProduct supplierProduct, String mobile, String pltSn) {
        BjymChargeRequest bjymChargeRequest = new BjymChargeRequest();

        String userPackage = getUserPackage(supplierProduct);
        if (StringUtils.isBlank(userPackage)) {
            return null;
        }

        BjymRequestBody body = buildBody(getBossReqSerialNum(pltSn), mobile, userPackage);
        BjymRequestHeader header = buildHeader(body);

        bjymChargeRequest.setRequestHeader(header);
        bjymChargeRequest.setRequestBody(body);
        return bjymChargeRequest;
    }

    //根据供应商产品获取userPackage参数
    private String getUserPackage(SupplierProduct supplierProduct) {
        try {
            String feature = supplierProduct.getFeature();
            FeatureInfo featureInfo = gson.fromJson(feature, FeatureInfo.class);
            return featureInfo != null ? featureInfo.getUserPackage() : null;
        } catch (Exception e) {
            LOGGER.error("获取供应商产品的userPackage属性时抛出异常,请确认供应商产品的feature字段,格式为{\"userPackage\": value}.");
            return null;
        }
    }

    private BjymRequestHeader buildHeader(BjymRequestBody body) {
        BjymRequestHeader header = new BjymRequestHeader();
        header.setPartyId(getPartyId());
        header.setSign(buildSign(body));
        header.setReportUrl(getCallbackUrl());

        return header;
    }

    //sign = MD5(body+json中整个body部分+key+ key+partyId+partyId +requestid+requestid)
    private String buildSign(BjymRequestBody body) {
        return DigestUtils.md5Hex("body" + gson.toJson(body) +
                "key" + getKey() +
                "partyId" + getPartyId() +
                "requestid" + body.getRequestId());
    }

    private BjymRequestBody buildBody(String requestId, String mobile, String userPackage) {
        BjymRequestBody bjymRequestBody = new BjymRequestBody();

        bjymRequestBody.setRequestId(requestId);
        bjymRequestBody.setSize(1);
        bjymRequestBody.setUserDataList(buildDataList(mobile, userPackage));

        return bjymRequestBody;
    }

    private List<BjymUserData> buildDataList(String mobile, String userPackage) {
        List<BjymUserData> result = new LinkedList<BjymUserData>();

        BjymUserData bjymUserData = new BjymUserData();
        bjymUserData.setMobile(mobile);
        bjymUserData.setUserPackage(userPackage);

        result.add(bjymUserData);
        return result;
    }

    /**
     * 获取请求对象到url的转化器
     *
     * 如果实现重写了send方法,则不需要提供这个方法,否则必须提供.
     *
     * 已经重写了send方法,不需要实现这个方法,直接返回null即可
     */
    @Override
    protected RequestPojoToChargeUrl<BjymChargeRequest> getRequestPojoToChargeUrlConverter() {
        return new RequestPojoToChargeUrl<BjymChargeRequest>() {
            @Override
            public String convert(BjymChargeRequest bjymChargeRequest) {
                try {
                    return getChargeUrl() + "?param=" + new URLCodec("utf-8").encode(gson.toJson(bjymChargeRequest));
                } catch (EncoderException e) {
                    LOGGER.error("对请求地址进行编码时抛出异常,异常信息为{}, 异常堆栈为{}.", e.getMessage(), e.getStackTrace());
                    return null;
                }
            }
        };
    }

    /**
     * 校验响应对象的有效性
     */
    @Override
    protected boolean validateResponsePojo(BjymChargeResponse responsePojo) {
        return responsePojo != null
                && StringUtils.isNotBlank(responsePojo.getCode())
                && StringUtils.isNotBlank(responsePojo.getDescription())
                && StringUtils.isNotBlank(responsePojo.getSendId())
                && StringUtils.isNotBlank(responsePojo.getRequestId());
    }

    /**
     * 解析响应内容
     *
     * @param respStr 响应内容
     * @return 响应对象
     */
    @Override
    protected BjymChargeResponse parseResponse(String respStr) {
        try {
            return gson.fromJson(respStr, BjymChargeResponse.class);
        } catch (Exception e) {
            LOGGER.error("解析响应对象内容时出错, 错误信息为{}, 错误堆栈为{}, 响应内容为{}.", e.getMessage(), e.getStackTrace(), respStr);
        }

        return null;
    }

    /**
     * 根据响应对象获取boss侧响应流水号
     *
     * @param responsePojo 响应对象
     * @return boss侧响应的流水号
     */
    @Override
    protected String getBossRespSn(BjymChargeResponse responsePojo) {
        return responsePojo == null ? null : responsePojo.getSendId();
    }

    /**
     * 提供该BossService的指纹标识，只有当这个指纹标识与supplier表中的fingerPrint字段的值匹配时，该bossService才能为该供应商提供的产品进行充值 <p>
     * 建议采用随机字符串的MD5码作为指纹
     *
     * @return BossService提供的指纹数据
     */
    @Override
    public String getFingerPrint() {
        return "beijingyunman";
    }

    /**
     * 从BOSS同步账户余额，并写入数据库相应的记录
     *
     * @param entId 企业的平台ID
     * @param prdId 产品的平台ID，这里是平台产品
     */
    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }

    private String getCallbackUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BJYM_CALLBACK_URL.getKey());
    }

    private int getPartyId() {
        return NumberUtils.toInt(globalConfigService.get(GlobalConfigKeyEnum.BJYM_PARTY_ID.getKey()), -1);
    }

    private String getKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BJYM_KEY.getKey());
    }

    private String getChargeUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BJYM_CHARGE_URL.getKey());
    }

    private class FeatureInfo {
        private String userPackage;

        public String getUserPackage() {
            return userPackage;
        }

        public void setUserPackage(String userPackage) {
            this.userPackage = userPackage;
        }
    }
}
