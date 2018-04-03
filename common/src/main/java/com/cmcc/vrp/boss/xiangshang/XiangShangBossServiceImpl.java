package com.cmcc.vrp.boss.xiangshang;

import com.google.gson.Gson;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.xiangshang.pojo.ErrCode;
import com.cmcc.vrp.boss.xiangshang.pojo.RequestPojo;
import com.cmcc.vrp.boss.xiangshang.pojo.ResponsePojo;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Formatter;
import java.util.concurrent.TimeUnit;

/**
 * 向上渠道 <p> Created by sunyiwei on 2016/12/9.
 */
@Service
public class XiangShangBossServiceImpl implements BossService {
    private static XStream xStream = null;

    static {
        xStream = new XStream();
        xStream.autodetectAnnotations(true);
        xStream.alias("result", ResponsePojo.class);
    }

    private final Logger LOGGER = LoggerFactory.getLogger(XiangShangBossServiceImpl.class);

    @Autowired
    SerialNumService serialNumService;

    @Autowired
    SupplierProductService supplierProductService;

    @Autowired
    private XsSignService signService;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        LOGGER.info("向上渠道充值开始");
        SupplierProduct sPrdouct;
        if (entId == null
                || splPid == null
                || !com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)
                || StringUtils.isBlank(serialNum)
                || (sPrdouct = supplierProductService.selectByPrimaryKey(splPid)) == null
                || StringUtils.isBlank(sPrdouct.getFeature())) {
            LOGGER.error("充值参数不正确，entId = {}, splPid = {}, Mobile = {}, serialNum = {}.", entId, splPid, mobile, serialNum);
            return new XiangShangBossOperationResult(null, ErrCode.InvalidParam);
        }

        //构建请求向上侧的流水号
        String bossReqSn = SerialNumGenerator.buildBossReqSerialNum(32);
        if (!serialNumService.updateSerial(buildSerialNum(serialNum, bossReqSn, null))) {
            LOGGER.error("插入流水号信息时出错， PltSn = {}, BossReqSn = {}.", serialNum, bossReqSn);
            return new XiangShangBossOperationResult(null, ErrCode.InvalidParam);
        }

        //构建请求参数
        RequestPojo requestPojo = buildRequestPojo(sPrdouct, mobile, bossReqSn);
        if (requestPojo == null) {
            LOGGER.error("构建充值请求参数时出错，请检查供应商产品的特征参数. SplPid = {}.", splPid);
            return new XiangShangBossOperationResult(null, ErrCode.InvalidParam);
        }

        //开始请求
        String url = buildChargeUrl(requestPojo);
        if (StringUtils.isBlank(url)) {
            LOGGER.error("构建充值请求地址时出错， RequestPojo = {}.", new Gson().toJson(requestPojo));
            return new XiangShangBossOperationResult(null, ErrCode.InvalidParam);
        }

        LOGGER.info("往向上侧的请求参数为{}.", url);

        //接收响应
        String responseStr = HttpUtils.get(url);
        LOGGER.info("向上BOSS返回响应内容为{}.", responseStr);

        //构建充值结果对象
        ResponsePojo responsePojo = parseResponse(responseStr);
        if (responsePojo == null) {
            LOGGER.error("响应内容解析后的对象为空. ResponseStr = {}.", responseStr);
            return new XiangShangBossOperationResult(null, ErrCode.InvalidParam);
        }

        //校验充值结果对象
        if (!signService.validate(responsePojo)) {
            LOGGER.error("充值响应内容校验不通过.");
            return new XiangShangBossOperationResult(null, ErrCode.ValidateFail);
        }

        //更新流水号信息
        if (StringUtils.isNotBlank(responsePojo.getId()) && !serialNumService.updateSerial(buildSerialNum(serialNum, bossReqSn, responsePojo.getId()))) {
            LOGGER.error("插入流水号信息时出错， PltSn = {}, BossReqSn = {}, BossRespSn = {}.", serialNum, bossReqSn, responsePojo.getId());
        }

        return new XiangShangBossOperationResult(responsePojo, parseErrCode(responsePojo));
    }

    @Override
    public String getFingerPrint() {
        return "xiangshang";
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return false;
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

    //解析响应代码
    private ErrCode parseErrCode(ResponsePojo responsePojo) {
        return ErrCode.fromCode(responsePojo.getErrcode());
    }

    //解析响应对象
    private ResponsePojo parseResponse(String responseStr) {
        if (StringUtils.isBlank(responseStr)) {
            LOGGER.error("充值请求返回的响应报文内容为空.");
            return null;
        }

        return (ResponsePojo) xStream.fromXML(responseStr);
    }

    //根据请求对象创建签名
    private String buildChargeUrl(RequestPojo requestPojo) {
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

    private RequestPojo buildRequestPojo(SupplierProduct supplierProduct, String mobile, String sn) {
        RequestPojo requestPojo = new RequestPojo();

        Info info = buildInfo(supplierProduct);
        if (info == null) {
            return null;
        }

        requestPojo.setArsid(info.getArsid());
        requestPojo.setDeno((int) (supplierProduct.getSize() / 1024));
        requestPojo.setOrderId(sn);
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
