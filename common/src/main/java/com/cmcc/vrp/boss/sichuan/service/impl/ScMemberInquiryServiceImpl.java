package com.cmcc.vrp.boss.sichuan.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.sichuan.model.ScMemberInquiryResponse;
import com.cmcc.vrp.boss.sichuan.service.ScMemberInquiryService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpConnection;
import com.google.gson.Gson;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年4月13日 下午4:27:17
*/
@Service("ScMemberInquiryService")
public class ScMemberInquiryServiceImpl implements ScMemberInquiryService {
    private static final Logger logger = LoggerFactory.getLogger(ScMemberInquiryServiceImpl.class);
    private static String TIME_FOMMAT = "yyyyMMddHHmmss";
    private static final String SYMBOL = "&";
    
    @Autowired
    GlobalConfigService globalConfigService;

    @Override
    public String generateRequestString(String mobile) {
        StringBuffer urlSb = new StringBuffer();
        urlSb.append("appKey=" + getAppKey());
        urlSb.append(SYMBOL);
        
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String requestTime = sdf.format(new Date());
        urlSb.append("timeStamp=" + requestTime);
        urlSb.append(SYMBOL);
        
        urlSb.append("userName=" + getUserName());
        urlSb.append(SYMBOL);
        
        urlSb.append("phone_no=" + mobile);       
             
        return urlSb.toString();
    }

    @Override
    public ScMemberInquiryResponse parseResponse(String responseStr) {
        ScMemberInquiryResponse scMemberInquiryResponse = null;
        if (responseStr != null) {
            Gson gson = new Gson();
            scMemberInquiryResponse = gson.fromJson(responseStr, ScMemberInquiryResponse.class);
        }
        return scMemberInquiryResponse;
    }

    @Override
    public String sendInquiryRequest(String mobile) {
        String mock = globalConfigService.get(GlobalConfigKeyEnum.DYNAMIC_PROXY_BOSS_FLAG.getKey());
        String vpmnTime = globalConfigService.get("MOCK_VPMNTIME");
        if(!StringUtils.isEmpty(mock) && "true".equals(mock)){            
            logger.info("四川V网成员查询mock结果"+vpmnTime);
            return StringUtils.isEmpty(vpmnTime)?null:vpmnTime;
        }
        logger.info("四川V网成员查询start{}", mobile);
        String retuestStr = generateRequestString(mobile);
        try {
            //String sign = Sign.sign(retuestStr, getPrivateKeyPath());
            String sign = com.cmcc.vrp.boss.jilin.utils.Sign.toSign(retuestStr, getPrivateKeyPath());
            retuestStr = retuestStr + "&sign=" + sign;
            logger.info("四川V网成员查询参数{}", retuestStr);
            String returnStr = HttpConnection.sendGetRequest(getMemberInquiryUrl(), retuestStr);
            logger.info("四川V网成员查询返回参数" + returnStr);
            if (!StringUtils.isEmpty(returnStr)) {
                ScMemberInquiryResponse response = parseResponse(returnStr);
                if (response == null
                        || !"0000000".equals(response.getResCode())
                        || response.getOutData() == null) {
                    return null;
                }
                return response.getOutData().getEFF_DATE();
            }
        } catch (Exception e) {
            logger.info("发送http请求错误:" + e.toString());
            return null;
        }
        return null;
    }
    @Override
    public Integer getdayRange(String mobile) {
        String effDate = null;
        if (mobile == null
                || (effDate = sendInquiryRequest(mobile)) == null) {
            logger.info("用户网龄查询失败!" + mobile);
            return null;
        }
        Integer dateRanage = null;
        try {
            dateRanage = DateUtil.getDayRange(effDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        if (dateRanage == null) {
            logger.info("计算网龄天数失败!" + mobile);
            return null;
        }
        return dateRanage + 1;
    }

    @Override
    public int[] getdateRange(String mobile) {
        String effDate = null;
        if (mobile == null
                || (effDate = sendInquiryRequest(mobile)) == null) {
            logger.info("用户网龄查询失败!" + mobile);
            return null;
        }
        int[] dateRanage = null;
        try {
            dateRanage = DateUtil.getDateRange(effDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return dateRanage;
    }
    
    /** 
     * 获取V网网龄的所有信息，包括入网时间，入网天数，入网至今的年月日
     * @Title: getVpmnYearInfo 
     * @param mobile
     * @return
     * @Author: wujiamin
     * @date 2017年5月10日
    */
    @Override
    public Map getVpmnYearInfo(String mobile) {
        String effDate = null;
        if (mobile == null
                || (effDate = sendInquiryRequest(mobile)) == null) {
            logger.info("用户网龄查询失败!" + mobile);
            return null;
        }
        int[] dateRanage = null;//年月日的数组
        try {
            dateRanage = DateUtil.getDateRange(effDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        Integer date = null;
        try {
            date = DateUtil.getDayRange(effDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        Map map = new HashMap();
        map.put("year", dateRanage[0]);//到当前的入网年
        map.put("month", dateRanage[1]);//到当前的入网月
        map.put("day", dateRanage[2]);//到当前的入网日
        map.put("vpmnYearStr", effDate);//入网时间的字符串
        map.put("date", date);//到当前的入网天数    
        return map;
    }
    
    
    public String getMemberInquiryUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_MEMBER_INQUIRY_URL.getKey());
    }

    public String getPrivateKeyPath() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_PRIVATE_KEY_PATH.getKey());
    }

    public String getAppKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_APP_KEY.getKey());
    }

    public String getUserName() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_USER_NAME.getKey());
    }

    public String getLoginNo() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_LOGIN_NO.getKey());
    }
}
