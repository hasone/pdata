package com.cmcc.vrp.wx.impl;

import com.google.gson.Gson;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.AuthStatus;
import com.cmcc.vrp.province.cache.AbstractCacheSupport;
import com.cmcc.vrp.province.model.WxAdminister;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.WxAdministerService;
import com.cmcc.vrp.util.AES;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpConnection;
import com.cmcc.vrp.util.HttpUtils;
import com.cmcc.vrp.wx.WetchatService;
import com.cmcc.vrp.wx.beans.InviteQrcodeReq;
import com.cmcc.vrp.wx.beans.InviteQrcodeReqActionInfo;
import com.cmcc.vrp.wx.beans.InviteQrcodeReqScene;
import com.cmcc.vrp.wx.beans.InviteQrcodeResp;
import com.cmcc.vrp.wx.beans.SendWxMsgReq;
import com.cmcc.vrp.wx.beans.SendWxMsgResp;
import com.cmcc.vrp.wx.beans.WxUserInfo;
import com.cmcc.vrp.wx.beans.WxUserTypeInterfacePojo;
import com.cmcc.vrp.wx.enums.SendMsgStatus;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

/**
 * Created by leelyn on 2017/1/3.
 */
@Service
public class WetchatServiceImpl extends AbstractCacheSupport implements WetchatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WetchatServiceImpl.class);
    //活动标识
    private static final String ACTIVE_ID = "gdzc_";
    public static final String SESSION_KEY = ACTIVE_ID + "wxOpenId";
    public static final String SESSION_MOBILE_KEY = ACTIVE_ID + "mobile";
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    Gson gson;
    @Autowired
    WxAdministerService wxAdministerService;
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    IndividualAccountService individualAccountService;

    @Override
    public String getPhoneByOpenId(String openId) {
        if (StringUtils.isBlank(openId)) {
            LOGGER.error("openId为空");
            return null;
        }
        String result = HttpUtils.get(getQueryMobileUrl() + "/" + openId);
        if (StringUtils.isBlank(result)) {
            LOGGER.error("请求返回为空");
            return null;
        }
        String mobile = null;
        try {
            JSONObject json = (JSONObject) JSONObject.parse(result);
            mobile = json.getString("mobile");
            if (StringUtils.isBlank(mobile)) {
                LOGGER.error("解析字段为空");
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("解析字段抛出异常:{}", e.getMessage());
        }
        return mobile;
    }


    @Override
    public boolean sendWetchatMsg(String mobile, Integer templateMsgType, Long activityId, Map<String, String> params) {
        if (StringUtils.isBlank(mobile)
                || templateMsgType == null
                || activityId == null
                || params == null) {
            LOGGER.error("参数为空");
            return false;
        }
        String reqStr = getMsgReq(mobile, templateMsgType, activityId, params);
        String result = HttpUtils.post(getSendWxMsgUrl(), reqStr, "application/xml");
        if (StringUtils.isBlank(result)) {
            LOGGER.error("返回包体为空");
            return false;
        }
        SendWxMsgResp resp = gson.fromJson(result, SendWxMsgResp.class);
        if (resp == null
                || !resp.getCode().equals(SendMsgStatus.OK.getCode())) {
            LOGGER.error("发送消息失败,code:{},message:{}", resp == null ? "resp为空"
                    : resp.getCode(), resp == null ? "resp为空" : resp.getMessage());
            return false;
        }
        return true;
    }

    private String getMsgReq(String mobile, Integer templateMsgType, Long activityId, Map<String, String> params) {
        SendWxMsgReq req = new SendWxMsgReq();
        req.setMobile(mobile);
        req.setTemplateMsgType(templateMsgType);
        req.setActivityId(activityId);
        req.setParams(params);
        req.setAuthentication(getDigest(mobile));
        return gson.toJson(req);
    }

    private String getDigest(String mobile) {
        StringBuffer sb = new StringBuffer();
        sb.append(mobile);
        sb.append(",");
        sb.append(getSendWxMsgAppkey());
        String content = sb.toString();
        byte[] encryptResult = AES.encrypt(content.getBytes(), getSendWxMsgAppsecret().getBytes());
        return DatatypeConverter.printHexBinary(encryptResult);
    }

    private String getQueryMobileUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.GUANGDONG_QUERY_MOBILE_URL.getKey());
    }

    private String getSendWxMsgUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.GUANGDONG_SEND_WX_MESSAGE_URL.getKey());
    }

    private String getSendWxMsgAppkey() {
        return globalConfigService.get(GlobalConfigKeyEnum.GUANGDONG_SEND_WX_APPKEY.getKey());
    }

    private String getSendWxMsgAppsecret() {
        return globalConfigService.get(GlobalConfigKeyEnum.GUANGDONG_SEND_WX_APPSECRET.getKey());
    }

    @Override
    public String getCode(String redirectURI) {
        //向微信公众号发请求code方法
        String platformUrl = globalConfigService.get(GlobalConfigKeyEnum.PLATFORM_URL.getKey());
        try {
            redirectURI = platformUrl + new URLCodec("utf-8").encode(redirectURI);
        } catch (EncoderException e) {
            LOGGER.error("URLEncode error! URL = {}", redirectURI);
            return null;
        }
        String getWxCodeUrl = globalConfigService.get(GlobalConfigKeyEnum.WEIXIN_CODE_URL.getKey());
        String authURl = MessageFormat.format(getWxCodeUrl, redirectURI);
        LOGGER.info("向微信公众号发请求code链接-" + "redirect:" + authURl);
        return "redirect:" + authURl;

    }

    @Override
    public String getUserType(HttpServletRequest request) {
        //向微信公众号发请求获取用户类型
        String getWxUserTypeUrl = globalConfigService.get(GlobalConfigKeyEnum.WEIXIN_USERTYPE_URL.getKey());
        String authURl = MessageFormat.format(getWxUserTypeUrl, request.getParameter("code"), request.getParameter("state"));
        Long start = System.currentTimeMillis();
        LOGGER.info("向微信公众号发请求getUserType链接 request请求时间===" + start);
        String resBody = null;
        try {
            resBody = HttpConnection.sendGetRequest(authURl, null);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Long end = System.currentTimeMillis();
        LOGGER.info("auth response响应时间===" + end);
        LOGGER.info("请求时间===" + (end - start) / 1000);
        LOGGER.info("auth response响应内容===" + resBody);

        String returnUrl = "gdzc/404.ftl";
        try {
            WxUserTypeInterfacePojo pojo = com.alibaba.fastjson.JSONObject.parseObject(resBody, WxUserTypeInterfacePojo.class);
            if (AuthStatus.FOCUS_BUT_NOT_BINDED.getCode().equals(pojo.getUserType())
                    || AuthStatus.NO_FOCUS.getCode().equals(pojo.getUserType())) {
                returnUrl = "redirect:" + pojo.getRedirectUrl();
            } else if (AuthStatus.FOCUS_AND_BINDED.getCode().equals(pojo.getUserType())
                    && !StringUtils.isEmpty(pojo.getMobile())
                    && !StringUtils.isEmpty(pojo.getOpenid())) {
                LOGGER.info("从微信公众号获取到用户手机号{}，openid{}", pojo.getMobile(), pojo.getOpenid());
                request.getSession().setAttribute(SESSION_KEY, pojo.getOpenid());//将openid放到session中
                request.getSession().setAttribute(SESSION_MOBILE_KEY, pojo.getMobile());//将手机号放到session中
                WxAdminister admin = wxAdministerService.selectByMobilePhone(pojo.getMobile());
                if (admin == null) {
                    LOGGER.info("从微信公众号获取到用户手机号{}，平台中不存在该手机号用户，新建用户", pojo.getMobile());
                    if (wxAdministerService.insertForWx(pojo.getMobile(), pojo.getOpenid())) {
                        admin = wxAdministerService.selectByMobilePhone(pojo.getMobile());
                        LOGGER.info("session中存储，mobile={}，openid{}，平台查找到用户adminId={}", pojo.getMobile(), pojo.getOpenid(), admin.getId());
                        request.getSession().setAttribute("currentUserId", admin.getId());//将adminId放到session中
                        returnUrl = "busiPage";//业务页面的标记
                    } else {
                        LOGGER.info("新建用户mobile={},openid={}失败", pojo.getMobile(), pojo.getOpenid());
                    }
                } else {
                    //已存在的用户要检查是否存在个人账户
                    if (!individualAccountService.checkAndInsertAccountForWx(admin.getId(), pojo.getOpenid())) {
                        LOGGER.info("用户检查并创建个人账户失败mobile={},openid={}失败", pojo.getMobile(), pojo.getOpenid());
                    } else {
                        LOGGER.info("session中存储，mobile={}，openid{}，平台查找到用户adminId={}", pojo.getMobile(), pojo.getOpenid(), admin.getId());
                        request.getSession().setAttribute("currentUserId", admin.getId());//将adminId放到session中
                        returnUrl = "busiPage";//业务页面的标记
                    }
                }

            } else {
                returnUrl = "gdzc/404.ftl";
            }
        } catch (Exception e) {
            LOGGER.error("出现异常，" + e.getMessage());
            returnUrl = "gdzc/404.ftl";
        }

        LOGGER.info("返回链接，{}", returnUrl);
        return returnUrl;
    }
    
    

    @Override
    public InviteQrcodeResp getInviteQrcode(String sceneStr) {
        // TODO Auto-generated method stub
        InviteQrcodeReq req = new InviteQrcodeReq();
        InviteQrcodeReqScene scene = new InviteQrcodeReqScene();
        InviteQrcodeReqActionInfo actionInfo = new InviteQrcodeReqActionInfo();
        scene.setSceneStr(sceneStr);
        actionInfo.setScene(scene);
        req.setActionInfo(actionInfo);
        req.setExpireSecond(2592000);
        String request = JSONObject.toJSONString(req);
        
        String getInviteQrcodeUrl = globalConfigService.get(GlobalConfigKeyEnum.WEIXIN_INVITE_QRCODE_URL.getKey());
        LOGGER.info("向微信公众号发请求getInviteQrcode请求:{}", request);
        String resBody = null;
        try {
            resBody = HttpConnection.doPost(getInviteQrcodeUrl, request, "application/json", "utf-8", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("向微信公众号发请求getInviteQrcode请求，响应内容===" + resBody);
        try {
            InviteQrcodeResp response = JSONObject.parseObject(resBody, InviteQrcodeResp.class);
            return response;
        } catch (Exception e) {
            LOGGER.info("解析JSON出错，{}", e.getMessage());
        }
        return null;
    }


    /*@Override
    public InviteQrcodeResp getInviteQrcode(Integer sceneId) {
        InviteQrcodeReq req = new InviteQrcodeReq();
        InviteQrcodeReqScene scene = new InviteQrcodeReqScene();
        InviteQrcodeReqActionInfo actionInfo = new InviteQrcodeReqActionInfo();
        scene.setSceneId(sceneId);
        actionInfo.setScene(scene);
        req.setActionInfo(actionInfo);
        req.setExpireSecond(2592000);
        String request = JSONObject.toJSONString(req);

        String getInviteQrcodeUrl = globalConfigService.get(GlobalConfigKeyEnum.WEIXIN_INVITE_QRCODE_URL.getKey());
        LOGGER.info("向微信公众号发请求getInviteQrcode请求:{}", request);
        String resBody = null;
        try {
            resBody = HttpConnection.doPost(getInviteQrcodeUrl, request, "application/json", "utf-8", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("向微信公众号发请求getInviteQrcode请求，响应内容===" + resBody);
        try {
            InviteQrcodeResp response = JSONObject.parseObject(resBody, InviteQrcodeResp.class);
            return response;
        } catch (Exception e) {
            LOGGER.info("解析JSON出错，{}", e.getMessage());
        }
        return null;
    }*/

    @Override
    public WxUserInfo getWxUserInfo(String mobile) {
        String resBody = null;
        if (!StringUtils.isEmpty(cacheService.get(mobile))) {
            resBody = cacheService.get(mobile);
        } else {
            String getWxUserInfoUrl = globalConfigService.get(GlobalConfigKeyEnum.WEIXIN_USERINFO_URL.getKey());
            String url = MessageFormat.format(getWxUserInfoUrl, mobile);
            LOGGER.info("向微信公众号发请求getWxUserInfo请求", url);

            try {
                resBody = HttpConnection.sendGetRequest(url, null);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            LOGGER.info("向微信公众号发请求getWxUserInfo请求，响应内容===" + resBody);
            cacheService.add(mobile, resBody);
        }
        try {
            WxUserInfo response = JSONObject.parseObject(resBody, WxUserInfo.class);
            return response;
        } catch (Exception e) {
            LOGGER.info("解析JSON出错，{}", e.getMessage());
        }
        return null;
    }


    @Override
    public String sendTemplateMag(String param) {
        String getInviteQrcodeUrl = globalConfigService.get(GlobalConfigKeyEnum.WEIXIN_SEND_TEMPLATE_URL.getKey());
        LOGGER.info("向微信公众号发请求sendTemplateMag请求:{}", param);
        String resBody = null;
        try {
            resBody = HttpConnection.doPost(getInviteQrcodeUrl, param, "application/json", "utf-8", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("向微信公众号发请求getInviteQrcode请求，响应内容===" + resBody);

        return resBody;
    }


    @Override
    protected String getPrefix() {
        return "wx.getWxUserInfo.";
    }

}
