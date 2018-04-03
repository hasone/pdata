package com.cmcc.vrp.async.controller;

import java.io.IOException;

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
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;

/**
 * 
 */
@Controller
@RequestMapping(value = "/users")
public class UserInterfaceController {

    private static final String SD_CLOUD_CODE = "99999";//山东云平台

    private static final int MAX_LENGTH_64 = 64;

    @Autowired
    private AdministerService administerService;

    @Autowired
    private EnterprisesService enterprisesService;

    private static final Logger logger = LoggerFactory.getLogger(UserInterfaceController.class);

    /**
     * 
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void memberOperation(final HttpServletRequest request, final HttpServletResponse response)
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

        String json = (String) request.getAttribute(Constants.BODY_XML_ATTR);//参数获取，JSON格式字符串
        logger.info("用户接口收到报文：" + json);
        int httpCode = HttpServletResponse.SC_BAD_REQUEST;//默认操作失败，参数缺失或者非法，返回400，
        try {
            //：订单业务处理
            if (StringUtils.isNotBlank(json) && handleAdminister(json)) {//操作成功，返回http状态码200
                httpCode = HttpServletResponse.SC_OK;
            }

        } catch (Exception e) {
            logger.error("参数错误或操作失败:" + json);
            e.printStackTrace();
        }
        //返回
        response.setStatus(httpCode);
        logger.info("用户接口响应状态码：" + httpCode);
        return;
    }

    private boolean handleAdminister(String json) {
        //参数校验
        JSONObject jo1 = JSONObject.parseObject(json);
        String type = jo1.getString("type");

        String info = jo1.getString("info");
        JSONObject jo2 = JSONObject.parseObject(info);
        String isAdmin = jo2.getString("isAdmin");

        //报文转换
        Administer administer = jsonToAdminister(json);
        if ("true".equals(isAdmin) && checkAdminister(administer)) {
            if ("NEW".equals(type)) {
                administer.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
                return administerService.createAdminister(administer);
            } else if ("UPDATE".equals(type)) {
                administer.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
                return administerService.updateAdminister(administer);
            } else if ("DELETE".equals(type)) {
                //逻辑删除
                administer.setDeleteFlag(DELETE_FLAG.DELETED.getValue());
                return administerService.updateAdminister(administer);
            }
        }
        return false;
    }

    /**
     * 
     * @Title: jsonToAdminister 
     * @Description: TODO
     * @param json
     * @return
     * @return: Administer
     */
    private Administer jsonToAdminister(String json) {
        Administer administer = new Administer();
        JSONObject jo = JSONObject.parseObject(json);
        String infoString = jo.getString("info");
        JSONObject info = JSONObject.parseObject(infoString);

        String phone = info.getString("phone");//电话,必填
        String email = info.getString("email");//邮箱,可选
        String name = info.getString("name");//姓名,必填
        String enterpriseCode = info.getString("enterpriseId");//所属企业编号，必填

        administer.setUserName(name);
        administer.setEmail(email);
        administer.setMobilePhone(phone);
        administer.setEnterpriseCode(enterpriseCode);
        return administer;
    }

    /**
     * 
     * @Title: checkAdminister 
     * @Description: 用户报文校验
     * @param administer
     * @return
     * @return: boolean
     */
    private boolean checkAdminister(Administer administer) {
        if (administer == null) {
            return false;
        } else if (org.apache.commons.lang.StringUtils.isBlank(administer.getUserName())
                || administer.getUserName().length() > MAX_LENGTH_64) {//用户手机号码，必填
            logger.error("用户信息报文校验失败：name非法，约定name必填且最大长度为" + MAX_LENGTH_64 + ",实际name =" + administer.getUserName());
            return false;
        } else if (org.apache.commons.lang.StringUtils.isBlank(administer.getMobilePhone())
                || !com.cmcc.vrp.util.StringUtils.isValidMobile(administer.getMobilePhone())) {//用户手机号码，必填
            logger.error("用户信息报文校验失败：phone非法，约定phone必填且必须为合法手机号码，实际phone =" + administer.getMobilePhone());
            return false;
        } else if (org.apache.commons.lang.StringUtils.isNotBlank(administer.getEmail())
                && (!com.cmcc.vrp.util.StringUtils.isValidEmail(administer.getEmail()) || administer.getEmail()
                        .length() > MAX_LENGTH_64)) {//用户邮箱可选，如果不为空，必须为合法邮箱且最大长度不能超过64
            logger.error("用户信息报文校验失败：email非法，约定email必须为合法邮箱且最大长度为：" + MAX_LENGTH_64 + ",实际为：email = "
                    + administer.getMobilePhone());
            return false;
        } else if (org.apache.commons.lang.StringUtils.isBlank(administer.getEnterpriseCode())) {
            logger.error("用户信息报文校验失败：enterpriseId非法，约定enterpriseId必填，实际enterpriseId = "
                    + administer.getEnterpriseCode());
            return false;
        }
        return true;
    }

}
