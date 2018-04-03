package com.cmcc.vrp.async.controller;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.enums.InterfaceApprovalStatus;
import com.cmcc.vrp.enums.InterfaceStatus;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.cmcc.vrp.util.DateUtil;

/**
 * 
 */
@Controller
@RequestMapping(value = "/enterprises")
public class EnterpriseInterfaceController {

    private static final Logger logger = LoggerFactory.getLogger(EnterpriseInterfaceController.class);

    private static final int MAX_LENGTH_255 = 255;
    private static final int MAX_LENGTH_64 = 64;

    private static final String SD_CLOUD_CODE = "99999";//山东云平台

    @Autowired
    private AdministerService administerService;

    @Autowired
    private EnterprisesService enterprisesService;

    /**
     * 
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void enterpriseOperation(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {

        String appKey = null;//企业APPKEY
        String systemSerialNum = null;//系统流水号

        //校验认证返回的参数,返回appKey和 systemSerialNum,否则认为认证失败，返回403
        if (StringUtils.isBlank(appKey = (String) request.getAttribute(Constants.APP_KEY_ATTR))
                || StringUtils.isBlank(systemSerialNum = (String) request.getAttribute(Constants.SYSTEM_NUM_ATTR))) {
            logger.error("认证未通过, AppKey = {}, SystemSerialNum = {}.", appKey, systemSerialNum);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        //校验是否是山东云平台发送的请求
        Enterprise enterprise = enterprisesService.selectByAppKey(appKey);
        if (enterprise == null || DELETE_FLAG.UNDELETED.getValue() != enterprise.getDeleteFlag().intValue()
                || !SD_CLOUD_CODE.equalsIgnoreCase(enterprise.getCode())) {
            logger.error("非山东云平台推送消息,约定只能使用山东云平台【code = " + SD_CLOUD_CODE + "】推送消息.");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String json = (String) request.getAttribute(Constants.BODY_XML_ATTR);//请求报文参数获取，JSON格式字符串
        logger.info("企业接口收到报文：" + json);
        int httpCode = HttpServletResponse.SC_BAD_REQUEST;//默认操作失败，参数缺失或者非法，返回400
        try {
            //:企业信息业务处理
            if (StringUtils.isNotBlank(json) && handleEnterprise(json)) {//操作成功，返回http状态码200
                httpCode = HttpServletResponse.SC_OK;//http状态码200
            }
        } catch (Exception e) {
            logger.error("参数错误或操作失败:" + json);
            e.printStackTrace();
        }
        response.setStatus(httpCode);//设置http状态码
        logger.info("企业接口响应状态码：" + httpCode);
        return;
    }

    /**
     * 
     * @Title: jsonToEnterpise 
     * @Description: 接口报文转为Model类型
     * @param json
     * @return
     * @return: Enterprise
     */
    private Enterprise jsonToEnterpise(String json) {
        Enterprise enterprise = new Enterprise();
        JSONObject jo = JSONObject.parseObject(json);
        String entInfo = jo.getString("info");
        JSONObject info = JSONObject.parseObject(entInfo);

        enterprise.setPhone(info.getString("ecId"));//山东流量平台，企业BOSSID，存储在phone字段
        enterprise.setCode(info.getString("id"));//山东流量平台，云平台企业编码
        enterprise.setName(info.getString("name"));//企业名称
        enterprise.setEntName(info.getString("name"));//企业扩展名
        enterprise.setEnterpriseCity(info.getString("city"));//城市	
        return enterprise;
    }

    /**
     * 
     * @Title: checkEnterprise 
     * @Description: 企业报文校验
     * @param enterprise
     * @return
     * @return: boolean
     */
    private boolean checkEnterprise(Enterprise enterprise) {
        if (enterprise == null) {
            return false;
        } else if (StringUtils.isBlank(enterprise.getName()) || enterprise.getName().length() > MAX_LENGTH_255) {//企业名称，必填
            logger.error("企业信息报文校验失败：name非法，约定name非空且最大长度为" + MAX_LENGTH_255 + ",实际name=" + enterprise.getName());
            return false;
        } else if (StringUtils.isBlank(enterprise.getCode()) || enterprise.getCode().length() > MAX_LENGTH_64) {//企业编码，必填
            logger.error("企业信息报文校验失败：id非法，约定id非空且最大长度为" + MAX_LENGTH_64 + ",实际id=" + enterprise.getCode());
            return false;
        } else if (StringUtils.isBlank(enterprise.getPhone()) || enterprise.getPhone().length() > MAX_LENGTH_64) {//企业BOSSID，必填
            logger.error("企业信息报文校验失败：ecId非法，约定ecId非空且最大长度为" + MAX_LENGTH_64 + ",实际ecId=" + enterprise.getPhone());
            return false;
        } else if (StringUtils.isBlank(enterprise.getEnterpriseCity())) {//企业地市信息必填
            logger.error("企业信息报文校验失败：city非法，约定city非空,实际city=" + enterprise.getEnterpriseCity());
            return false;
        }
        return true;
    }

    /**
     * 
     * @Title: initEnterprise 
     * @Description: 初始化默认参数
     * @param enterprise
     * @return: void
     */
    private void initEnterprise(Enterprise enterprise) {
        if (enterprise == null) {
            return;
        }
        //初始化默认参数
        //设置企业状态
        enterprise.setStatus((byte) 3);

        //设置企业appSecret
        enterprise.setAppSecret(UUID.randomUUID().toString().replace("-", ""));

        //企业默认无折扣
        enterprise.setDiscount(1L);

        //设置EC接口开关，默认关闭
        enterprise.setInterfaceFlag(InterfaceStatus.CLOSE.getCode());

        Date startTime = new Date();
        Date endTime = DateUtil.getDateAfter(startTime, 365 * 100);//有效期100年

        //设置接口有效期
        enterprise.setInterfaceExpireTime(endTime);

        //设置EC接口默认审核通过
        enterprise.setInterfaceApprovalStatus(InterfaceApprovalStatus.APPROVED.getCode());

        //设置合作开始时间和结束时间
        enterprise.setStartTime(startTime);
        enterprise.setEndTime(endTime);

        enterprise.setAppKey(UUID.randomUUID().toString().replace("-", ""));

        //设置工商营业执有效期
        enterprise.setLicenceStartTime(startTime);
        enterprise.setLicenceEndTime(endTime);
    }

    private boolean handleEnterprise(String json) {
        //报文转换
        Enterprise enterprise = jsonToEnterpise(json);

        //参数校验
        if (!checkEnterprise(enterprise)) {
            return false;
        }

        JSONObject jo = JSONObject.parseObject(json);
        if ("DELETE".equals(jo.getString("type"))) {
            enterprise.setDeleteFlag(DELETE_FLAG.DELETED.getValue());
        } else {
            initEnterprise(enterprise);
        }
        return enterprisesService.createOrUpdateEnterprise(enterprise);
    }
}
