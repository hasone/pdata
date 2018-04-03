package com.cmcc.vrp.boss;

import com.google.gson.Gson;

import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.HttpUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 基于供应商产品的feature字段的充值渠道实现
 *
 * Created by sunyiwei on 2017/3/14.
 */
public abstract class FeatureBasedBossServiceImpl<S, T> implements BossService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureBasedBossServiceImpl.class);

    @Autowired
    private SupplierProductService supplierProductService;

    @Autowired
    private SerialNumService serialNumService;

    /**
     * 返回渠道的名称
     */
    protected abstract String getName();

    /**
     * 返回参数无效时的结果
     */
    protected abstract BossOperationResult getInvalidParamResult();

    /**
     * @param responsePojo 响应对象
     * @return 返回充值成功时的结果
     */
    protected abstract BossOperationResult parseResult(T responsePojo);

    /**
     * 返回向上游请求时的流水号
     *
     * @param pltSn 平台流水号
     */
    protected abstract String getBossReqSerialNum(String pltSn);

    /**
     * 根据供应商产品,手机号以及平台流水号构建请求对象
     *
     * @param supplierProduct 供应商产品
     * @param mobile          手机号
     * @param pltSn           平台流水号
     * @return 请求对象
     */
    protected abstract S buildRequestPojo(SupplierProduct supplierProduct, String mobile, String pltSn);

    /**
     * 获取请求对象到url的转化器
     *
     * 如果实现重写了send方法,则不需要提供这个方法,否则必须提供.
     */
    protected abstract RequestPojoToChargeUrl<S> getRequestPojoToChargeUrlConverter();

    /**
     * 校验响应对象的有效性
     */
    protected abstract boolean validateResponsePojo(T responsePojo);

    /**
     * 将请求发往上游
     *
     * 默认实现为构建相应的请求URL, 并通过GET请求发送
     *
     * @param requestPojo 请求对象
     * @return 请求URL, 用于GET请求
     */
    protected String send(S requestPojo) {
        RequestPojoToChargeUrl<S> converter = getRequestPojoToChargeUrlConverter();
        if (converter == null) {
            LOGGER.error("无法根据请求对象构建充值URL, 当前实现的转化器为空, 请提供转化器或重写send()方法.");
            return null;
        }

        //开始请求
        String url = converter.convert(requestPojo);
        if (StringUtils.isBlank(url)) {
            LOGGER.error("构建充值请求地址时出错， RequestPojo = {}.", new Gson().toJson(requestPojo));
            return null;
        }

        LOGGER.info("往{}侧的请求参数为{}.", getName(), url);

        //接收响应
        String responseStr = HttpUtils.get(url);
        LOGGER.info("{}侧返回响应内容为{}.", getName(), responseStr);

        return responseStr;
    }

    /**
     * 解析响应内容
     *
     * @param respStr 响应内容
     * @return 响应对象
     */
    protected abstract T parseResponse(String respStr);

    /**
     * 根据响应对象获取boss侧响应流水号
     *
     * @param responsePojo 响应对象
     * @return boss侧响应的流水号
     */
    protected abstract String getBossRespSn(T responsePojo);

    /**
     * 向BOSS发起充值操作
     *
     * @param entId     企业ID
     * @param splPid    供应商产品ID
     * @param mobile    手机号码
     * @param serialNum 平台的充值序列号
     * @return 返回相应的充值结果
     */
    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info(getName() + "渠道充值开始");
        SupplierProduct sPrdouct;
        if (entId == null
                || splPid == null
                || !com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)
                || StringUtils.isBlank(serialNum)
                || (sPrdouct = supplierProductService.selectByPrimaryKey(splPid)) == null
                || StringUtils.isBlank(sPrdouct.getFeature())) {
            LOGGER.error("充值参数不正确，entId = {}, splPid = {}, Mobile = {}, serialNum = {}.", entId, splPid, mobile, serialNum);
            return getInvalidParamResult();
        }

        //构建请求流水号
        if (!serialNumService.updateSerial(buildSerialNum(serialNum, getBossReqSerialNum(serialNum), null))) {
            LOGGER.error("插入流水号信息时出错， PltSn = {}, BossReqSn = {}.", serialNum, getBossReqSerialNum(serialNum));
            return getInvalidParamResult();
        }

        //构建请求参数
        S requestPojo = buildRequestPojo(sPrdouct, mobile, serialNum);
        if (requestPojo == null) {
            LOGGER.error("构建充值请求参数时出错，请检查供应商产品的参数. SplPid = {}.", splPid);
            return getInvalidParamResult();
        }

        //开始请求, 并接收响应
        String responseStr = send(requestPojo);
//        LOGGER.info("{}侧返回响应内容为{}.", getName(), responseStr);

        //构建充值结果对象
        T responsePojo = parseResponse(responseStr);
        if (responsePojo == null) {
            LOGGER.error("响应内容解析后的对象为空. ResponseStr = {}.", responseStr);
            return getInvalidParamResult();
        }

        //校验充值结果对象
        if (!validateResponsePojo(responsePojo)) {
            LOGGER.error("充值响应内容校验不通过.");
            return getInvalidParamResult();
        }

        //更新流水号信息
        String bossRespSn = getBossRespSn(responsePojo);
        if (StringUtils.isNotBlank(bossRespSn)
                && !serialNumService.updateSerial(buildSerialNum(serialNum, serialNum, bossRespSn))) {
            LOGGER.error("插入流水号信息时出错， PltSn = {}, BossReqSn = {}, BossRespSn = {}.", serialNum, serialNum, bossRespSn);
        }

        return parseResult(responsePojo);
    }

    private SerialNum buildSerialNum(String pltSn, String bossReqSn, String bossRespSn) {
        SerialNum serialNum = new SerialNum();
        serialNum.setPlatformSerialNum(pltSn);
        serialNum.setBossReqSerialNum(bossReqSn);
        serialNum.setBossRespSerialNum(bossRespSn);
        serialNum.setCreateTime(new Date());
        serialNum.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());

        return serialNum;
    }

    /**
     * @Title: mdrcCharge
     * @Description: TODO
     * @return: BossOperationResult
     */
    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    protected interface RequestPojoToChargeUrl<S> {
        /**
         * 将请求对象转化成充值URL
         *
         * @param s 请求对象
         * @return 充值URL
         */
        String convert(S s);
    }
}
