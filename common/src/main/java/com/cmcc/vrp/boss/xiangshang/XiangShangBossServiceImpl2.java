package com.cmcc.vrp.boss.xiangshang;

import com.google.gson.Gson;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.FeatureBasedBossServiceImpl;
import com.cmcc.vrp.boss.xiangshang.pojo.ErrCode;
import com.cmcc.vrp.boss.xiangshang.pojo.RequestPojo;
import com.cmcc.vrp.boss.xiangshang.pojo.ResponsePojo;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Formatter;
import java.util.concurrent.TimeUnit;

/**
 * 向上渠道
 *
 * Created by sunyiwei on 2016/12/9.
 */
public class XiangShangBossServiceImpl2 extends FeatureBasedBossServiceImpl<RequestPojo, ResponsePojo> {
    private static XStream xStream = null;

    static {
        xStream = new XStream();
        xStream.autodetectAnnotations(true);
        xStream.alias("result", ResponsePojo.class);
    }

    private final Logger LOGGER = LoggerFactory.getLogger(XiangShangBossServiceImpl.class);
    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private XsSignService signService;

    /**
     * 返回渠道的名称
     */
    @Override
    protected String getName() {
        return "向上";
    }

    /**
     * 返回参数无效时的结果
     */
    @Override
    protected BossOperationResult getInvalidParamResult() {
        return new XiangShangBossOperationResult(null, ErrCode.InvalidParam);
    }

    /**
     * @param responsePojo 响应对象
     * @return 返回充值成功时的结果
     */
    @Override
    protected BossOperationResult parseResult(ResponsePojo responsePojo) {
        if(responsePojo==null){
            return getInvalidParamResult();
        }

        return new XiangShangBossOperationResult(responsePojo, parseErrCode(responsePojo));
    }

    //解析响应代码
    private ErrCode parseErrCode(ResponsePojo responsePojo) {
        return ErrCode.fromCode(responsePojo.getErrcode());
    }

    /**
     * 返回向上游请求时的流水号
     *
     * @param pltSn 平台流水号
     */
    @Override
    protected String getBossReqSerialNum(String pltSn) {
        return SerialNumGenerator.buildBossReqSerialNum(32);
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
    protected RequestPojo buildRequestPojo(SupplierProduct supplierProduct, String mobile, String pltSn) {
        RequestPojo requestPojo = new RequestPojo();

        Info info = buildInfo(supplierProduct);
        if (info == null) {
            return null;
        }

        requestPojo.setArsid(info.getArsid());
        requestPojo.setDeno((int) (supplierProduct.getSize() / 1024));
        requestPojo.setOrderId(pltSn);
        requestPojo.setPhone(mobile);
        requestPojo.setTime(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() + 8 * 3600 * 1000))); //时区
        requestPojo.setMacid(getMacId());

        return requestPojo;
    }

    private Info buildInfo(SupplierProduct supplierProduct) {
        Info info = null;
        try {
            info = new Gson().fromJson(supplierProduct.getFeature(), Info.class);
            return info;
        } catch (Exception e) {
            LOGGER.error("解析特定参数时抛出异常，错误信息为{}, 异常堆栈为{}.", e.getMessage(), e.getStackTrace());
        }

        return null;
    }

    /**
     * 获取请求对象到url的转化器
     *
     * 如果实现重写了send方法,则不需要提供这个方法,否则必须提供.
     */
    @Override
    protected RequestPojoToChargeUrl<RequestPojo> getRequestPojoToChargeUrlConverter() {
        return new RequestPojoToChargeUrl<RequestPojo>() {
            @Override
            public String convert(RequestPojo requestPojo) {
                //1. 获取签名内容
                String sign = signService.sign(requestPojo);
                if (StringUtils.isBlank(sign)) {
                    LOGGER.error("根据请求对象生成签名时返回空值. RequestPojo = {}.", new Gson().toJson(requestPojo));
                    return null;
                }

                //2. 设置签名值
                try {
                    requestPojo.setSign(URLEncoder.encode(sign, Charsets.UTF_8.name()));
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error("对签名参数进行URL编码时抛出异常，异常信息为{}, 异常堆栈为{}.", e.getMessage(), e.getStackTrace());
                    return null;
                }

                //3. 生成相应的充值地址
                StringBuilder stringBuilder = new StringBuilder();
                Formatter formatter = new Formatter(stringBuilder);
                formatter.format("%s?arsid=%s&deno=%d&macid=%s&orderid=%s&phone=%s&sign=%s&time=%s",
                        getChargeUrl(), requestPojo.getArsid(), requestPojo.getDeno(), requestPojo.getMacid(),
                        requestPojo.getOrderId(), requestPojo.getPhone(), requestPojo.getSign(), requestPojo.getTime());

                //返回
                return stringBuilder.toString();
            }
        };
    }

    /**
     * 校验响应对象的有效性
     */
    @Override
    protected boolean validateResponsePojo(ResponsePojo responsePojo) {
        //校验充值结果对象
        if (!signService.validate(responsePojo)) {
            LOGGER.error("充值响应内容校验不通过.");
            return false;
        }

        return true;
    }

    /**
     * 解析响应内容
     *
     * @param respStr 响应内容
     * @return 响应对象
     */
    @Override
    protected ResponsePojo parseResponse(String respStr) {
        if (StringUtils.isBlank(respStr)) {
            LOGGER.error("充值请求返回的响应报文内容为空.");
            return null;
        }

        return (ResponsePojo) xStream.fromXML(respStr);
    }

    /**
     * 根据响应对象获取boss侧响应流水号
     *
     * @param responsePojo 响应对象
     * @return boss侧响应的流水号
     */
    @Override
    protected String getBossRespSn(ResponsePojo responsePojo) {
        return responsePojo.getId();
    }

    /**
     * 提供该BossService的指纹标识，只有当这个指纹标识与supplier表中的fingerPrint字段的值匹配时，该bossService才能为该供应商提供的产品进行充值 <p>
     * 建议采用随机字符串的MD5码作为指纹
     *
     * @return BossService提供的指纹数据
     */
    @Override
    public String getFingerPrint() {
        return "xiangshang";
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

    private String getChargeUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_XS_CHARGE_URL.getKey());
    }

    //获取向上公司分配的账号
    private String getMacId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_XS_MAC_ID.getKey());
    }

    private class Info {
        private String arsid;

        public String getArsid() {
            return arsid;
        }

        public void setArsid(String arsid) {
            this.arsid = arsid;
        }
    }
}
