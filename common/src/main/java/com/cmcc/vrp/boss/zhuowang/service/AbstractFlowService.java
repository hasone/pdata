package com.cmcc.vrp.boss.zhuowang.service;


import com.cmcc.vrp.boss.zhuowang.bean.OrderRequestResult;
import com.cmcc.vrp.boss.zhuowang.utils.HttpUtils;
import com.cmcc.vrp.boss.zhuowang.utils.ParseXml;
import com.cmcc.vrp.boss.zhuowang.utils.ToolsUtils;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import sun.misc.BASE64Encoder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流量服务的抽象实现
 */
public abstract class AbstractFlowService<T> implements FlowPackageOrderService<T> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractFlowService.class);

    @Autowired
    GlobalConfigService globalConfigService;

    abstract protected String getXmlHead(String serialNum);

    abstract protected String getXmlBody(List<T> list);

    abstract protected boolean verify(List<T> list);

    /**
     * 构建请求参数，包括xmlhead、xmlbody
     */
    protected Map<String, String> getParams(List<T> list, String serialNum) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("xmlhead", getXmlHead(serialNum));
        paramMap.put("xmlbody", getXmlBody(list));
        return paramMap;
    }

    /**
     * 发送请求到卓望侧
     *
     * @param list      充值号码
     * @param serialNum 平台侧充值序列号
     * @return 请求结果
     */
    public OrderRequestResult sendRequest(List<T> list, String serialNum) {
        if (!verify(list)) {
            return null;
        }

        OrderRequestResult result = null;
        try {
            Map<String, String> headerMap = getAuthHead();
            Map<String, String> params = getParams(list, serialNum);

            logger.info("流量叠加包接口鉴权头信息：" + headerMap);
            logger.info("流量叠加包接口请求头报文：" + params.get("xmlhead"));
            logger.info("流量叠加包接口请求体报文：" + params.get("xmlbody"));

            String rspXml = HttpUtils.doPost(getUrl(), params,
                    "UTF-8", 3000, 190000, headerMap);

            logger.info("流量叠加包接口调用响应：" + rspXml);
            result = parseXml2Result(rspXml);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("流量叠加包接口调用失败：" + e.getMessage());
            result = new OrderRequestResult();
            result.setRspDesc("流量叠加包接口调用失败：" + e.getMessage());
        }
        return result;
    }

    public Map<String, String> getAuthHead() throws Exception {
        // appkey、secretkey
        String username = getAppKey();
        String appSecret = getAppSecret();

        // nonce
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String nonce = System.currentTimeMillis() + "";
        String nonceSecret = base64Encoder.encode(nonce.getBytes("UTF-8"));

        // created
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String created = df.format(new Date());

        // passwordDigest
        String passwordDigest = base64Encoder.encode(ToolsUtils.getSha256(
                nonce + created + appSecret).getBytes());

        String authorization = "WSSE realm=\"DOMS\", profile=\"UsernameToken\", type=\"AppKey\"";
        String wsse = "UsernameToken Username=\"" + username
                + "\",  PasswordDigest=\"" + passwordDigest + "\",  Nonce=\""
                + nonceSecret + "\",  Created=\"" + created + "\"";

        wsse = wsse.replaceAll("\n", "").replaceAll("\r", "");

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", authorization);
        headerMap.put("X-WSSE", wsse);

        //卓望侧接口变更,增加prodid及actcode头信息, 2017.02.28
        headerMap.put("PRODID", DigestUtils.md5Hex(getProductId()).toLowerCase());
        headerMap.put("ACTCODE", "BIP4B876");  //充值接口的业务代码BIPCode

        return headerMap;
    }

    /**
     * 解析响应消息
     */
    protected OrderRequestResult parseXml2Result(String xml) {
        OrderRequestResult result = new OrderRequestResult();
        try {
            Map map = ParseXml.xml2Map(xml);

            Map transInfo = (HashMap) map.get("TransInfo");
            if (transInfo != null) {
                String transIDO = (String) transInfo.get("TransIDO");
                result.setTransIDO(transIDO);
            }

            Map response = (HashMap) map.get("Response");
            if (response != null) {
                String rspType = (String) response.get("RspType");
                String rspCode = (String) response.get("RspCode");
                String rspDesc = (String) response.get("RspDesc");
                result.setRspCode(rspCode);
                result.setRspType(rspType);
                result.setRspDesc(rspDesc);
            }
            String svcCont = (String) map.get("SvcCont");
            if (StringUtils.isNotBlank(svcCont)) {
                Map svcCountMap = ParseXml.xml2Map(svcCont);
                String status = (String) svcCountMap.get("Status");
                Map operSeqList = (Map) svcCountMap.get("OperSeqList");
                if (operSeqList != null) {
                    if (operSeqList.get("OperSeq") instanceof String) {
                        List<String> list = new ArrayList<String>(1);
                        list.add((String) operSeqList.get("OperSeq"));
                        result.setOperSeqList(list);
                    } else if (operSeqList.get("OperSeq") instanceof List) {
                        List list = (ArrayList) operSeqList.get("OperSeq");
                        result.setOperSeqList(list);
                    }
                }
                result.setStatus(status);
            }
        } catch (DocumentException e) {
            logger.error("解析响应xml出错：" + e.getMessage());
        }
        return result;
    }

    protected String getAppKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_ZW_APP_KEY.getKey());
    }

    protected String getAppSecret() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_ZW_APP_SECRET.getKey());
    }

    protected String getUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_ZW_URL.getKey());
    }

    protected String getTestFlag() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_ZW_TEST_FLAG.getKey());
    }

    protected String getProductId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_ZW_PRODUCT_ID.getKey());
    }
}
