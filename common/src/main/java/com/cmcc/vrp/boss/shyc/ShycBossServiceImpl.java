package com.cmcc.vrp.boss.shyc;

import com.google.gson.Gson;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.FeatureBasedBossServiceImpl;
import com.cmcc.vrp.boss.shyc.enums.ErrorCode;
import com.cmcc.vrp.boss.shyc.pojos.ShycBossOperationResult;
import com.cmcc.vrp.boss.shyc.pojos.SingleChargeRequest;
import com.cmcc.vrp.boss.shyc.pojos.SingleChargeResponse;
import com.cmcc.vrp.boss.shyc.utils.QueryStringUtils;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 上海月呈充值渠道
 *
 * Created by sunyiwei on 2017/3/14.
 */
@Service
public class ShycBossServiceImpl extends FeatureBasedBossServiceImpl<SingleChargeRequest, SingleChargeResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShycBossServiceImpl.class);

    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * 返回渠道的名称
     */
    @Override
    protected String getName() {
        return "上海月呈";
    }

    /**
     * 返回参数无效时的结果
     */
    @Override
    protected BossOperationResult getInvalidParamResult() {
        return new ShycBossOperationResult(ErrorCode.INVALID_PARAM);
    }


    /**
     * 返回充值成功时的结果
     */
    @Override
    protected BossOperationResult parseResult(SingleChargeResponse response) {
        if (response == null) {
            return getInvalidParamResult();
        }

        return new ShycBossOperationResult(response.getCode(), response.getMessage());
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
     * 校验响应对象的有效性
     */
    @Override
    protected boolean validateResponsePojo(SingleChargeResponse responsePojo) {
        return true;
    }

    /**
     * 获取请求对象到url的转化器
     *
     * 如果实现重写了send方法,则不需要提供这个方法,否则必须提供.
     */
    @Override
    protected RequestPojoToChargeUrl<SingleChargeRequest> getRequestPojoToChargeUrlConverter() {
        return new RequestPojoToChargeUrl<SingleChargeRequest>() {
            @Override
            public String convert(SingleChargeRequest singleChargeRequest) {
                //1. 获取请求字符串内容
                String queryString = QueryStringUtils.queryString(singleChargeRequest, getApiKey());
                if (StringUtils.isBlank(queryString)) {
                    LOGGER.error("根据请求对象生成请求字符串时返回空值. RequestPojo = {}.", new Gson().toJson(singleChargeRequest));
                    return null;
                }

                //2. 生成相应的充值地址
                return getChargeUrl() + queryString;
            }
        };
    }

    /**
     * 根据响应对象获取boss侧响应流水号
     *
     * @param responsePojo 响应对象
     * @return boss侧响应的流水号
     */
    @Override
    protected String getBossRespSn(SingleChargeResponse responsePojo) {
        return responsePojo == null ? null : responsePojo.getTaskId();
    }

    //解析响应对象
    @Override
    protected SingleChargeResponse parseResponse(String responseStr) {
        if (StringUtils.isBlank(responseStr)) {
            LOGGER.error("充值请求返回的响应报文内容为空.");
            return null;
        }

        try {
            return new Gson().fromJson(responseStr, SingleChargeResponse.class);
        } catch (Exception e) {
            LOGGER.error("解析上海月呈侧返回的响应报文时出错,错误原因为{}, 错误堆栈为{}.", e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    //构建请求对象
    @Override
    protected SingleChargeRequest buildRequestPojo(SupplierProduct supplierProduct, String mobile, String sn) {
        SingleChargeRequest scr = new SingleChargeRequest();

        Feature feature = parse(supplierProduct);
        if (feature == null) {
            return null;
        }

        scr.setAccount(getSupplierCode());
        scr.setRange(feature.getRange());
        scr.setPackageName(String.valueOf(feature.getPackageSize()));
        scr.setMobile(mobile);
        scr.setOrderNo(sn);
        scr.setTimeStamp(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        return scr;
    }

    private Feature parse(SupplierProduct supplierProduct) {
        try {
            String featureStr = supplierProduct.getFeature();
            Feature feature = new Gson().fromJson(featureStr, Feature.class);
            return feature;
        } catch (Exception e) {
            LOGGER.error("解析上海月号供应商产品特征参数时出错,请按照{\"range\": range, \"packageSize\": packageSize}的格式设置特征参数. SupplierProduct = {}",
                    new Gson().toJson(supplierProduct));
        }

        return null;
    }

    /**
     * 提供该BossService的指纹标识，只有当这个指纹标识与supplier表中的fingerPrint字段的值匹配时，该bossService才能为该供应商提供的产品进行充值 <p>
     * 建议采用随机字符串的MD5码作为指纹
     *
     * @return BossService提供的指纹数据
     */
    @Override
    public String getFingerPrint() {
        return "shanghaiyuecheng";
    }


    /**
     * 从BOSS同步账户余额，并写入数据库相应的记录
     *
     * @param entId 企业的平台ID
     * @param prdId 产品的平台ID，这里是平台产品
     */
    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return false;
    }

    private String getApiKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.SHYC_API_KEY.getKey());
    }

    private String getSupplierCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.SHYC_SUPPLIER_CODE.getKey());
    }

    private String getChargeUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.SHYC_CHARGE_URL.getKey());
    }

    private class Feature {
        private int range;
        private int packageSize;

        public int getRange() {
            return range;
        }

        public void setRange(int range) {
            this.range = range;
        }

        public int getPackageSize() {
            return packageSize;
        }

        public void setPackageSize(int packageSize) {
            this.packageSize = packageSize;
        }
    }
}
