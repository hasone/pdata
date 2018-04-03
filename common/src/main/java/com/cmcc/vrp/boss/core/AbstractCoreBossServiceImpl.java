package com.cmcc.vrp.boss.core;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.core.model.OrderReq;
import com.cmcc.vrp.boss.core.model.OrderResp;
import com.cmcc.vrp.boss.core.model.UserData;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 用于网宿渠道与unionflow渠道的对接
 * <p>
 * Created by sunyiwei on 2016/10/11.
 */
public abstract class AbstractCoreBossServiceImpl implements BossService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCoreBossServiceImpl.class);

    private static XStream xStream;

    static {
        xStream = new XStream();
        xStream.alias("Request", OrderReq.class);
        xStream.alias("Response", OrderResp.class);

        xStream.autodetectAnnotations(true);
    }

    @Autowired
    protected GlobalConfigService globalConfigService;

    @Autowired
    SupplierProductService supplierProductService;

    @Autowired
    SerialNumService serialNumService;

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        SupplierProduct supplierProduct;
        String pCode = null;
        if (entId == null
                || splPid == null
                || !com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)
                || StringUtils.isBlank(serialNum)
                || (supplierProduct = supplierProductService.selectByPrimaryKey(splPid)) == null
                || StringUtils.isBlank(pCode = supplierProduct.getCode())) {
            LOGGER.error("充值请求参数错误, EntId = {}, SplPid = {}, Mobile = {}, SerialNum = {}, Pcode = {}.",
                    entId, splPid, mobile, serialNum, pCode);
            return new CoreBossOperationResultImpl(CoreResultCodeEnum.PARA_ILLEGALITY.getCode(), CoreResultCodeEnum.PARA_ILLEGALITY.getMsg());
        }

        String bossReqNum = SerialNumGenerator.buildNormalBossReqNum(getSerialNumPrefix(), 25);

        //生成请求消息体
        String requestBody = xStream.toXML(buildOrderReq(bossReqNum, buildUserData(mobile, pCode), getAppKey()));
        LOGGER.info("SerialNum = {}, EntId = {}, SplPid = {}, Mobile = {}, 请求的消息体为{}",
                serialNum, entId, splPid, mobile, requestBody);

        //获取响应消息体
        OrderResp resp = doPost(getOrderUrl(), requestBody, buildHeaders(requestBody, getAppSecret()),
                buildContentType(), "utf-8");
        LOGGER.info("响应报文内容为{}.", new Gson().toJson(resp));

        //判断响应结果
        if (resp != null && isSuccess(resp)) {
            //成功后更新流水记录表
            updateSerialNum(serialNum, resp.getOperSeq(), bossReqNum);
            return new CoreBossOperationResultImpl(CoreResultCodeEnum.SUCCESS.getCode(), CoreResultCodeEnum.SUCCESS.getMsg());
        } else {
            //失败后更新流水记录表
            updateSerialNum(serialNum, resp == null ? SerialNumGenerator.buildNullBossRespNum(getSerialNumPrefix()) : resp.getOperSeq(), bossReqNum);
            return new CoreBossOperationResultImpl(CoreResultCodeEnum.FAILD.getCode(),
                    resp == null ? CoreResultCodeEnum.FAILD.getMsg() : resp.getErrDesc());
        }
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        return true;
    }

    private String buildContentType() {
        return "application/xml";
    }

    private void setHeaders(HttpMethod method, Map<String, String> headers) {
        if (headers != null) {
            for (String s : headers.keySet()) {
                method.addRequestHeader(new Header(s, headers.get(s)));
            }
        }
    }

    private boolean updateSerialNum(String systemNum, String bossRespNum, String bossReqNum) {
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

    private OrderReq buildOrderReq(String serialNum, List<UserData> userDatas, String appKey) {
        OrderReq or = new OrderReq();

        or.setAppKey(appKey);
        or.setRequestTime(String.valueOf(System.currentTimeMillis()));
        or.setTransID(serialNum);
        or.setUserDataList(userDatas);

        return or;
    }

    private List<UserData> buildUserData(String mobile, String pCode) {
        List<UserData> list = new LinkedList<UserData>();
        UserData ud = new UserData();
        ud.setaPackage(pCode);
        ud.setMobNum(mobile);
        list.add(ud);
        return list;
    }

    /**
     * 执行一个HTTP POST请求，返回请求响应的HTML
     *
     * @param url     请求的URL地址
     * @param reqStr  请求的查询参数,可以为null
     * @param charset 字符集
     * @return 返回请求响应解析的对象
     */
    private OrderResp doPost(String url, String reqStr, Map<String, String> headers,
                             String contentType, String charset) {
        HttpClient client = new HttpClient();

        //set headers
        PostMethod method = new PostMethod(url);
        setHeaders(method, headers);

        try {
            method.setRequestEntity(new StringRequestEntity(reqStr, contentType, charset));
            client.executeMethod(method);

            InputStream is;
            if (method.getStatusCode() == HttpStatus.SC_OK
                    && (is = method.getResponseBodyAsStream()) != null) {
                String responseBody = StreamUtils.copyToString(is, Charsets.UTF_8);
                LOGGER.info("Response body: {}", responseBody);

                return (OrderResp) xStream.fromXML(responseBody);
            } else {
                LOGGER.error("Response响应码为{}.", method.getStatusCode());
            }
        } catch (UnsupportedEncodingException e1) {
            LOGGER.error(e1.getMessage());
        } catch (IOException e) {
            LOGGER.error("执行HTTP Post请求" + url + "时，发生异常！", e);
        } finally {
            method.releaseConnection();
        }

        return null;
    }

    protected abstract Map<String, String> buildHeaders(String requestBody, String appSecret);

    protected abstract String getSerialNumPrefix();

    protected abstract String getAppKey();

    protected abstract String getOrderUrl();

    protected abstract String getAppSecret();

    protected abstract boolean isSuccess(OrderResp orderResp);
}
