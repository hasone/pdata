package com.cmcc.vrp.province.webin.controller;

import com.cmcc.vrp.util.QueryObject;
import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by sunyiwei on 2016/6/30.
 */
@Controller
@RequestMapping("/manage/query")
public class VerifyCodeQueryController {
    @Autowired
    JedisPool jedisPool;

    /** 
     * @Title: queryVerifyCodeIndex 
     * @author wujiamin
    */
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String queryVerifyCodeIndex(QueryObject queryObject, ModelMap modelMap) {
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        return "query/info.ftl";
    }

    /** 
     * @Title: queryLoginVerifyCode 
     * @author wujiamin
    */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    @ResponseBody
    public String queryLoginVerifyCode(String mobile) {
        final String LOGIN_SUFFIX = "SmsLogin";
        return query(mobile + LOGIN_SUFFIX);
    }

    /** 
     * @Title: queryUpdatePwdVerifyCode 
     * @author wujiamin
    */
    @RequestMapping(value = "updatePwd", method = RequestMethod.GET)
    @ResponseBody
    public String queryUpdatePwdVerifyCode(String mobile) {
        final String UPDATE_PWD_SUFFIX = "updateSmsLogin";
        return query(mobile + UPDATE_PWD_SUFFIX);
    }

    private String query(String key) {
        Jedis jedis = null;
        final String NO_EXIST = "No Exist";

        try {
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            Result result = null;

            if (StringUtils.isBlank(value)) {
                result = buildResult(NO_EXIST);
            } else {
                VerifyCode verifyCode = new Gson().fromJson(value, VerifyCode.class);
                result = buildResult(verifyCode == null ? NO_EXIST : verifyCode.getPassword());
            }

            return new Gson().toJson(result);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    private Result buildResult(String code) {
        Result result = new Result();
        result.setCode(code);

        return result;
    }

    private class Result {
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    private class VerifyCode {
        private String lastTime;
        private String mobile;
        private String password;
        private String todayFrequency;

        public String getLastTime() {
            return lastTime;
        }

        public void setLastTime(String lastTime) {
            this.lastTime = lastTime;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getTodayFrequency() {
            return todayFrequency;
        }

        public void setTodayFrequency(String todayFrequency) {
            this.todayFrequency = todayFrequency;
        }
    }

}
