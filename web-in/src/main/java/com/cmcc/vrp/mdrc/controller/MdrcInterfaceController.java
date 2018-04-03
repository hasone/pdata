package com.cmcc.vrp.mdrc.controller;

import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.exception.PreconditionRequiredException;
import com.cmcc.vrp.exception.UnprocessableEntityException;
import com.cmcc.vrp.province.mdrc.model.MdrcEcChargeRequest;
import com.cmcc.vrp.province.mdrc.model.MdrcEcChargeRequestData;
import com.cmcc.vrp.province.mdrc.model.MdrcEcChargeResponse;
import com.cmcc.vrp.province.mdrc.model.MdrcEcChargeResponseData;
import com.cmcc.vrp.province.mdrc.model.MdrcEcRequest;
import com.cmcc.vrp.province.mdrc.model.MdrcEcResponse;
import com.cmcc.vrp.province.mdrc.model.MdrcEcResponseData;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.mdrc.service.MdrcEcService;
import com.cmcc.vrp.province.mdrc.utils.MdrcChargeResult;
import com.cmcc.vrp.util.IpUtils;
import com.cmcc.vrp.util.StringUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.Charsets;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by sunyiwei on 2016/6/1.
 */
@Controller
@RequestMapping("mdrc")
public class MdrcInterfaceController {
    private final static Logger LOGGER = LoggerFactory.getLogger(MdrcInterfaceController.class);

    @Autowired
    MdrcEcService mdrcEcService;
    
    @Autowired
    MdrcCardInfoService mdrcCardInfoService;

    /**
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "operate", method = RequestMethod.PUT)
    @ResponseBody
    public void operate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        MdrcEcRequest mdrcEcRequest = null;
        try {
            mdrcEcRequest = buildRequest(request);
        } catch (ConversionException e) {
            LOGGER.error("无效请求时间，错误信息为{}.", e.toString());
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            return;
        }

        if (mdrcEcRequest == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //过期了
        if (outOfDate(mdrcEcRequest.getRequestTime())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        try {
            MdrcEcResponse mdrcEcResponse = new MdrcEcResponse();
            MdrcEcResponseData mdrcEcResponseData = mdrcEcService.operate(mdrcEcRequest.getMdrcEcRequestData());

            mdrcEcResponse.setDateTime(new Date());
            mdrcEcResponse.setMdrcEcResponseData(mdrcEcResponseData);


            StreamUtils.copy(buildResponse(mdrcEcResponse), Charsets.UTF_8, response.getOutputStream());
        } catch (UnprocessableEntityException e) {
            LOGGER.error("流量卡信息变更异常，错误信息为{}.", e.toString());
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        } catch (PreconditionRequiredException e) {
            LOGGER.error("流量卡状态变更异常，错误信息为{}.", e.toString());
            response.setStatus(HttpStatus.PRECONDITION_REQUIRED.value());
        } catch (IOException e) {
            LOGGER.error("流量卡操作响应抛出异常，错误信息为{}.", e.toString());
        }
    }

    /**
     * @param request
     * @param response
     */
    @RequestMapping(value = "use", method = RequestMethod.POST)
    public void use(HttpServletRequest request, HttpServletResponse response) {
        MdrcEcChargeRequest chargeRequest = buildChargeRequest(request);
        MdrcEcChargeRequestData mdrcEcChargeRequestData = null;
        if (chargeRequest == null
                || (mdrcEcChargeRequestData = chargeRequest.getMdrcEcChargeRequestData()) == null
                || org.apache.commons.lang.StringUtils.isBlank(mdrcEcChargeRequestData.getCardNum())
                || org.apache.commons.lang.StringUtils.isBlank(mdrcEcChargeRequestData.getPassword())
                || org.apache.commons.lang.StringUtils.isBlank(mdrcEcChargeRequestData.getMobile())
                || org.apache.commons.lang.StringUtils.isBlank(mdrcEcChargeRequestData.getSerialNum())) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!validate(mdrcEcChargeRequestData)) {
            LOGGER.error("字段位数错误或含非数字或非字母字符");
            response.setStatus(422);
            return;
        }
        String cardNum = mdrcEcChargeRequestData.getCardNum();
        String password = mdrcEcChargeRequestData.getPassword();
        String serialNum = mdrcEcChargeRequestData.getSerialNum();
        String mobile = mdrcEcChargeRequestData.getMobile();
        String systemSerialNum = (String) request.getAttribute(Constants.SYSTEM_NUM_ATTR);
        String remoteAddr = IpUtils.getRemoteAddr(request);
        if (mdrcCardInfoService.get(cardNum) == null) {
            LOGGER.error("该卡号不存在, cardNum{}", cardNum);
            response.setStatus(404);
            return;
        }
        
        if (!mdrcCardInfoService.validateBeforeUse(cardNum, password, mobile, remoteAddr)) {
            LOGGER.error("无效的请求参数，使用卡失败. 卡号为{}, 密码哈希值为{}, 手机号为{}, IP为{}.", cardNum, DigestUtils.md5Hex(password), mobile, remoteAddr);
            response.setStatus(404);
            return;
        }

        MdrcEcChargeResponse mdrcEcChargeResponse = null;
        if (mdrcEcService.useCard(cardNum, password, mobile, remoteAddr, systemSerialNum)) {
            mdrcEcChargeResponse = buildChargeResponse(MdrcChargeResult.SUCCESS, serialNum, systemSerialNum);
        } else {
            mdrcEcChargeResponse = buildChargeResponse(MdrcChargeResult.FAIL, serialNum, systemSerialNum);
        }

        try {
            StreamUtils.copy(buildResponse(mdrcEcChargeResponse), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("流量卡充值操作响应抛出异常，错误信息为{}.", e.toString());
        }
    }

    private boolean validate(MdrcEcChargeRequestData mdrcEcChargeRequestData) {
        return mdrcEcChargeRequestData != null
                && mdrcEcChargeRequestData.getCardNum().length() <= 32
                && mdrcEcChargeRequestData.getSerialNum().length() <= 32
                && mdrcEcChargeRequestData.getPassword().length() <= 32
                && !StringUtils.isHasSymble(mdrcEcChargeRequestData.getSerialNum())
                && StringUtils.isValidMobile(mdrcEcChargeRequestData.getMobile());
    }

    private MdrcEcChargeResponse buildChargeResponse(MdrcChargeResult mdrcChargeResult, String serialNum, String pltSerialNum) {
        MdrcEcChargeResponse mdrcEcChargeResponse = new MdrcEcChargeResponse();

        MdrcEcChargeResponseData mdrcEcChargeResponseData = new MdrcEcChargeResponseData();
        mdrcEcChargeResponseData.setPltSerialNum(pltSerialNum);
        mdrcEcChargeResponseData.setSerialNum(serialNum);
        mdrcEcChargeResponseData.setRspCode(mdrcChargeResult.getRetCode());
        mdrcEcChargeResponseData.setRspDesc(mdrcChargeResult.getRetMsg());

        mdrcEcChargeResponse.setMdrcEcResponseData(mdrcEcChargeResponseData);
        mdrcEcChargeResponse.setDateTime(new Date());
        return mdrcEcChargeResponse;
    }

    private MdrcEcChargeRequest buildChargeRequest(HttpServletRequest request) {
        XStream xstream = new XStream();
        xstream.alias("Request", MdrcEcChargeRequest.class);
        xstream.autodetectAnnotations(true);

        try {
            return (MdrcEcChargeRequest) xstream.fromXML((String) request.getAttribute(Constants.BODY_XML_ATTR));
        } catch (Exception e) {
            LOGGER.error("无效的请求参数,错误信息为{}.", e.toString());
            return null;
        }
    }

    private boolean outOfDate(Date date) {
        return new DateTime(date).plusSeconds(30).isBeforeNow();
    }

    private MdrcEcRequest buildRequest(HttpServletRequest request) {
        XStream xstream = new XStream();
        xstream.alias("Request", MdrcEcRequest.class);
        xstream.autodetectAnnotations(true);

        return (MdrcEcRequest) xstream.fromXML((String) request.getAttribute(Constants.BODY_XML_ATTR));
    }

    private String buildResponse(MdrcEcResponse response) {
        XStream xStream = new XStream();

        xStream.alias("Response", MdrcEcResponse.class);
        xStream.autodetectAnnotations(true);
        return xStream.toXML(response);
    }

    private String buildResponse(MdrcEcChargeResponse response) {
        XStream xStream = new XStream();

        xStream.alias("Response", MdrcEcChargeResponse.class);
        xStream.autodetectAnnotations(true);
        return xStream.toXML(response);
    }
}
