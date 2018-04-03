package com.cmcc.vrp.boss.shangdong.boss.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.shangdong.boss.service.SdCloudWebserviceImpl;
import com.cmcc.vrp.boss.shangdong.boss.service.SdManagerService;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.StringUtils;

/**
* 
* <p>Description: 客户经理操作类</p>
* @author panxin
* @date 2016年11月12日 上午11:27:38
 */
@Service("sdManagerService")
public class SdManagerServiceImpl implements SdManagerService {

    @Autowired
    SdAdministerServiceImpl sdAdministerService;

    @Autowired
    private SdCloudWebserviceImpl platFormService;

    @Autowired
    AdministerService administerService;

    @Autowired
    private GlobalConfigService globalConfigService;

    private static final Logger logger = Logger.getLogger(SdManagerServiceImpl.class);

    @Override
    public String createManager(String userToken) {
        String jsonString = null;
        try {
            jsonString = platFormService.getCurrentMgrInfo(userToken);
//            jsonString = "{\"code\":\"200\",\"manager\":{\"username\":\"panxin\",\"phone\":\"18867105828\","
//                    + "\"registerIp\":\"1092\",\"email\":\"panxin@163.com\",\"gender\":\"1\",\"newsLetter\":\"true\","
//                    + "\"lastUpdateDate\":\"20161112\",\"createDate\":\"20161112\","
//                    + "\"lastLoginDate\":\"20161112\",\"isAdmin\":\"true\","
//                    + "\"name\": \"panxin\",\"id\": \"123\",\"province\": \"山东\",\"city\": \"济南市\",\"county\": \"区县\"}, \"msg\": \"ok\"}";
            logger.error("getCurrentMgrInfo" + jsonString);

            JSONObject jo = JSONObject.parseObject(jsonString);
            String code = jo.getString("code");

            Administer administer = new Administer();

            logger.info("得到管理员，code=" + code);
            if ("200".equals(code) || NumberUtils.toInt(code) == 200) {
                logger.info("得到管理员，code=" + code);
                String infoString = jo.getString("manager");

                JSONObject info = JSONObject.parseObject(infoString);
                String managerUsername = info.getString("username");//用户名
                String phone = info.getString("phone");//电话
                String email = info.getString("email");
                String id = info.getString("id");
                administer.setUserName(managerUsername);
                administer.setEmail(email);
                administer.setMobilePhone(phone);
                administer.setCreateTime(new Date());
                administer.setUpdateTime(new Date());
                administer.setCitys(info.getString("city"));
                //                administer.setManagerCode(id);
                logger.info("得到管理员，email=" + email);
                logger.info("得到管理员，managerUsername=" + managerUsername);
                logger.info("得到管理员，phone=" + phone);
                logger.info("得到管理员，id=" + id);
            }

            //1028 在platFormService的内部解析
            List<String> enterpriseId = platFormService.getEnterprisesInfoList(userToken);
            
            for (String s : enterpriseId) {
                logger.info("得到企业Id" + s);
            }

            if (!StringUtils.isEmpty(administer.getMobilePhone())) {
                String customManager = globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey());
                String cityManager = globalConfigService.get(GlobalConfigKeyEnum.CITY_MANAGER_ROLE_ID.getKey());

                sdAdministerService.insertManager(administer, Long.parseLong(customManager), enterpriseId,
                        Long.parseLong(cityManager));
                return administer.getMobilePhone();
            } else {
                return "-1";
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("云平台SDK环境异常");
        }
        return null;
    }

    @Override
    public boolean updateManager(String userToken) {
        String jsonString = null;
        try {
//            jsonString = "{\"code\":\"200\",\"manager\":{\"username\":\"panxin\",\"phone\":\"18867105829\","
//                    + "\"registerIp\":\"1092\",\"email\":\"panxin@163.com\",\"gender\":\"1\",\"newsLetter\":\"true\","
//                    + "\"lastUpdateDate\":\"20161112\",\"createDate\":\"20161112\","
//                    + "\"lastLoginDate\":\"20161112\",\"isAdmin\":\"true\","
//                    + "\"name\": \"panxin\",\"id\": \"123\",\"province\": \"山东\",\"city\": \"济南市\",\"county\": \"区县\"}, \"msg\": \"ok\"}";
            
            jsonString = platFormService.getCurrentMgrInfo(userToken);

            logger.info("更新客户经理信息：" + jsonString);
            JSONObject jo = JSONObject.parseObject(jsonString);
            String code = jo.getString("code");

            Administer administer = new Administer();

            if ("200".equals(code) || NumberUtils.toInt(code) == 200) {
                String infoString = jo.getString("manager");

                JSONObject info = JSONObject.parseObject(infoString);
                String managerUsername = info.getString("username");//用户名
                String phone = info.getString("phone");//电话
                String email = info.getString("email");
                //                String id = info.getString("id");

                administer = administerService.selectByMobilePhone(phone);
                administer.setUserName(managerUsername);
                administer.setEmail(email);
                administer.setMobilePhone(phone);
                administer.setCreateTime(new Date());
                administer.setUpdateTime(new Date());
                administer.setCitys(info.getString("city"));
            }
            //1028 在platFormService的内部解析
            List<String> enterpriseId = platFormService.getEnterprisesInfoList(userToken);
            
            String customManager = globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey());
            String cityManager = globalConfigService.get(GlobalConfigKeyEnum.CITY_MANAGER_ROLE_ID.getKey());
            return sdAdministerService.updateManager(administer, enterpriseId, Long.parseLong(customManager),
                    Long.parseLong(cityManager));
        } catch (Exception e) {
            e.printStackTrace();            
            logger.error("云平台SDK环境异常:" + e.getMessage());
        }
        return false;
    }
}
