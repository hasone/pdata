package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.enums.AuthStatus;
import com.cmcc.vrp.province.model.AuthResponse;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.HttpUtils;
import com.cmcc.vrp.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by qinqinyan on 2017/2/21. 微信鉴权控制器
 *
 * 用code向微信获取用户信息，从而跳转到应用页面
 */
@Controller
@RequestMapping("/manage/wechat")
public class WechatAuthController extends BaseController {
    private static final Logger logger =
            LoggerFactory.getLogger(WechatAuthController.class);

    @Autowired
    GlobalConfigService globalConfigService;

    /**
     * 微信鉴权
     *
     * @author qinqinyan
     */
    @RequestMapping("auth")
    public String auth(HttpServletRequest request, String code, String urlKey) {
        
        /*if (StringUtils.isEmpty(code)) {
            //无code，未登录过，回授权页
            String appID = getWxAppID();
            logger.info("code is null, appID = {}", appID);
            return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid="+getWxAppID()
            +"&redirect_uri=https%3A%2F%2Fgdzc.4ggogo.com%2Fweb-in%2Fmanage%2FcrowdFunding%2Fauth.html&&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        }*/
        String sessionMobile = getMobile();
        logger.info("get mobile from session: " + sessionMobile);
        if (!StringUtils.isEmpty(sessionMobile)) {
            logger.info("session exist, mobile = {}, go to {}", sessionMobile, getFlowURL(urlKey));
            return "redirect:" + getFlowURL(urlKey);
        }

        logger.info("-----start to auth---- code = {}, urlKey ={}", code, urlKey);
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(urlKey)) {
            logger.info("参数缺失，直接返回错误页，code = {}, urlKey ={}", code, urlKey);
            return "gdzc/404.ftl";
        }


        logger.info("有 code,但是没有mobile. code = {}", code);
        String responseBody = getUserInfoWithCode(code);
        logger.info("auth response===" + responseBody);

        AuthResponse authResponse = null;
        try {
            authResponse = JSON.parseObject(responseBody, AuthResponse.class);
            if (authResponse != null) {
                if (authResponse.getStatus().toString()
                        .equals(AuthStatus.NO_FOCUS.getCode().toString())) {
                    logger.info("no focus. " + JSON.toJSONString(authResponse));
                    logger.info("go to {}", getFocusURL());
                    return "redirect:" + getFocusURL();
                } else if (authResponse.getStatus().toString()
                        .equals(AuthStatus.FOCUS_BUT_NOT_BINDED.getCode().toString())) {
                    logger.info("focus, but no binded. " + JSON.toJSONString(authResponse));
                    logger.info("go to {}", getBindedURL());
                    return "redirect:" + getBindedURL();
                } else if (authResponse.getStatus().toString()
                        .equals(AuthStatus.FOCUS_AND_BINDED.getCode().toString())
                        && StringUtils.isValidMobile(authResponse.getMobile())
                        && StringUtils.isValidOpenId(authResponse.getWxOpenId())) {
                    /**
                     * 1、将mobile和wxOpenId存于session
                     * 2、跳转到领取页面
                     * */
                    if (setMobile(authResponse.getMobile(), authResponse.getWxOpenId())
                            && setWxOpenid(authResponse.getWxOpenId())) {
                        logger.info("succeed to set mobile = {} and wxOpenId = {} in session."
                                + "go to {}", authResponse.getMobile(), authResponse.getWxOpenId(), getFlowURL(urlKey));
                        return "redirect:" + getFlowURL(urlKey);
                    } else {
                        logger.info("fail to set mobile = {} and wxOpenId = {} in session.",
                                authResponse.getMobile(), authResponse.getWxOpenId());
                        return "gdzc/404.ftl";
                    }
                }
            }
            logger.error("解析对象为空");
        } catch (Exception e) {
            logger.error("解析失败,失败原因:" + e.getMessage());
        }
        logger.info("go to {}", getFocusURL());
        return "redirect:" + getFocusURL();
    }

    /**
     * 向公众号获取用户信息
     *
     * @author qinqinyan
     */
    public String getUserInfoWithCode(String code) {
        String authURl = getAuthURL();
        Long start = System.currentTimeMillis();
        logger.info("auth request===" + start);
        String resBody = HttpUtils.get(authURl + "?code=" + code);
        Long end = System.currentTimeMillis();
        logger.info("auth response===" + end);
        logger.info("请求时间===" + (end - start) / 1000);
        logger.info("auth response===" + resBody);
        return resBody;
    }

    /**
     * 广东众筹鉴权URL，code向公众号换用户信息的URL
     *
     * @author qinqinyan
     */
    private String getAuthURL() {
        return globalConfigService.get("CROWD_FUNDING_AUTH_URL");
    }

    /**
     * 获取流量领取页面URL
     *
     * @author qinqinyan
     */
    private String getFlowURL(String urlKey) {
        //return globalConfigService.get("CROWD_FUNDING_GETFLOW_PAGE");
        return globalConfigService.get(urlKey);
    }

    /**
     * 引导关注公众号URL
     *
     * @author qinqinyan
     */
    private String getFocusURL() {
        return globalConfigService.get("CROWD_FUNDING_FOCUS_URL");
    }

    /**
     * 广东众筹公众号绑定手机号URL
     *
     * @author qinqinyan
     */
    private String getBindedURL() {
        return globalConfigService.get("CROWD_FUNDING_BINDED_URL");
    }

}
