package com.cmcc.vrp.boss.ecinvoker.ecservice.impl;

import com.cmcc.vrp.boss.ecinvoker.ecservice.EcChargeService;
import com.cmcc.vrp.ec.bean.ChargeReq;
import com.cmcc.vrp.ec.bean.ChargeResp;
import com.cmcc.vrp.ec.bean.QueryResp;
import com.cmcc.vrp.util.HttpUtils;
import com.thoughtworks.xstream.XStream;

import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by leelyn on 2016/7/15.
 */
@Service
public class EcChargeServiceImpl implements EcChargeService {


    private static Map<String, String> buildHeaders(String token, String signature) {
        Map<String, String> headers = new LinkedHashMap<String, String>();
        if (StringUtils.isNotBlank(token)) {
            headers.put("4GGOGO-Auth-Token", token);
        }

        if (StringUtils.isNotBlank(signature)) {
            headers.put("HTTP-X-4GGOGO-Signature", signature);
        }

        headers.put("Content-Type", "application/xml");
        return headers;
    }

    @Override
    public ChargeResp charge(ChargeReq chargeReq, String token, String signature, String url) {
        if (chargeReq == null
                || StringUtils.isBlank(token)
                || StringUtils.isBlank(signature)) {
            return null;
        }
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        xStream.alias("Request", ChargeReq.class);
        String req = xStream.toXML(chargeReq);
        String resp = HttpUtils.post(url, req, ContentType.TEXT_XML.getMimeType(), buildHeaders(token, signature));
        xStream.alias("Response", ChargeResp.class);
        return (ChargeResp) xStream.fromXML(resp);
    }

    @Override
    public QueryResp qureyChargeResult(String token, String signature, String url) {
        if (StringUtils.isBlank(token)
                || StringUtils.isBlank(signature)) {
            return null;
        }
        String resp = HttpUtils.get(url, null, buildHeaders(token, signature));
        XStream xStream = new XStream();
        xStream.alias("Response", QueryResp.class);
        xStream.autodetectAnnotations(true);

        return (QueryResp) xStream.fromXML(resp);
    }
}
