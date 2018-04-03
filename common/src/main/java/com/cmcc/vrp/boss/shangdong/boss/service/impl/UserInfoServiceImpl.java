package com.cmcc.vrp.boss.shangdong.boss.service.impl;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.shangdong.boss.model.Customer;
import com.cmcc.vrp.boss.shangdong.boss.model.Manager;
import com.cmcc.vrp.boss.shangdong.boss.service.SdCloudWebserviceImpl;
import com.cmcc.vrp.boss.shangdong.boss.service.SdUserInfoService;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.service.AdministerService;

/**
 * @ClassName UserInfoServiceImpl
 * @Description: 山东云平台登录跳转时获取用户信息
 * @author JamieWu
 * @Date 2016年1月4日上午10:47:34
 */
@Service("sdkUserInfo")
public class UserInfoServiceImpl implements SdUserInfoService {

    @Autowired
    AdministerService administerService;

    @Autowired
    private SdCloudWebserviceImpl platFormService;

    private static final Logger logger = Logger.getLogger(UserInfoServiceImpl.class);

    @Override
    public Administer getUserInfo(String userToken, Boolean isManager) {
        Administer administer = null;

        String jsonStr = null;
        //登录的是客户经理
        if (isManager) {
//            jsonStr = "{\"code\":\"200\",\"manager\":{\"username\":\"panxin\",\"phone\":\"18867105828\","
//                    + "\"registerIp\":\"1092\",\"email\":\"panxin@163.com\",\"gender\":\"1\",\"newsLetter\":\"true\","
//                    + "\"lastUpdateDate\":\"20161112\",\"createDate\":\"20161112\","
//                    + "\"lastLoginDate\":\"20161112\",\"isAdmin\":\"true\","
//                    + "\"name\": \"panxin\",\"id\": \"123\",\"province\": \"山东\",\"city\": \"济南市\",\"county\": \"区县\"}, \"msg\": \"ok\"}";
            jsonStr = platFormService.getCurrentMgrInfo(userToken);
            logger.info("客户经理信息查询,json:" + jsonStr);

            try {
                JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                if (jsonObject == null) {//预防jsonObject为空引起的后续错误, 返回null给后端处理
                    logger.error("jsonObject为空");
                    return null;
                }

                String code = jsonObject.getString("code");//预防code为空引起的后续错误, 返回null给后端处理
                if (code == null) {
                    logger.error("code为空");
                    return null;
                } else if ("200".equals(code) || NumberUtils.toInt(code) == 200) {
                    String userJsonStr = jsonObject.getString("manager");
                    if (userJsonStr == null) {
                        return null;
                    }

                    Manager managerInfo = new Manager();
                    managerInfo = JSON.parseObject(userJsonStr, Manager.class);

                    administer = administerService.selectByMobilePhone(managerInfo.getPhone());

                    return administer;
                }
            } catch (JSONException e) {
                logger.error("企业关键人登录异常：" + e);
                return null;
            } catch (NullPointerException e) {
                logger.error("企业关键人登录异常：" + e);
                return null;
            }

        } else {
            logger.info("企业管理员登录");
            try {
//                jsonStr = "{\"code\":\"200\",\"customer\":{\"username\":\"panxin\",\"phone\":\"18867105827\","
//                        + "\"registerIp\":\"1092\",\"enterpriseId\":\"01\",\"email\":\"panxin@163.com\",\"gender\":\"1\",\"newsLetter\":\"true\","
//                        + "\"credentialNumber\":\"2614\",\"credentialType\":\"shenfenzheng\",\"lastUpdateDate\":\"20161112\",\"createDate\":\"20161112\","
//                        + "\"lastLoginDate\":\"20161112\",\"isAdmin\":\"true\",\"customerGroupId\":\"\",\"customerGroupName\":\"\","
//                        + "\"enterpriseName\": \"test\",\"name\": \"panxin\",\"id\": \"123\",\"status\": \"状态\"}, \"msg\": \"ok\"}";
                jsonStr = platFormService.getCurrentUserInfo(userToken);

                JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                String code = jsonObject.getString("code");//预防code为空引起的后续错误, 返回null给后端处理
                logger.info("code:" + code);
                if (code == null) {
                    logger.error("code为空");
                    return null;
                } else if ("200".equals(code) || NumberUtils.toInt(code) == 200) {
                    String userJsonStr = jsonObject.getString("customer");
                    if (userJsonStr == null) {
                        return null;
                    }

                    Customer customerInfo = new Customer();
                    customerInfo = JSON.parseObject(userJsonStr, Customer.class);

                    administer = administerService.selectByMobilePhone(customerInfo.getPhone());

                    return administer;
                }
            } catch (JSONException e) {
                logger.error("企业关键人登录异常：" + e);
                return null;
            } catch (NullPointerException e) {
                logger.error("企业关键人登录异常：" + e);
                return null;
            }
        }
        return null;
    }

}
