package com.cmcc.vrp.individual.controller;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.model.CallbackPojo;
import com.cmcc.vrp.province.service.CallbackForIndividualService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by qinqinyan on 2016/10/9.
 */
@Controller
@RequestMapping("/api/individual/")
public class CallbackForIndividualController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackForIndividualController.class);

    @Autowired
    CallbackForIndividualService callbackForIndividualService;

    /**
     * @param request
     * @param response
     */
    @RequestMapping(value = "charge", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE)
    public void callback(HttpServletRequest request,
                         HttpServletResponse response) {
        try {
            String content = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
            CallbackPojo pojo = JSONObject.parseObject(content, CallbackPojo.class);

            //return serial num, now it's fake
            String serialNum = DigestUtils.md5Hex(UUID.randomUUID().toString());

            LOGGER.info("Receive callback with pojo = {}.", JSONObject.toJSON(pojo));
            boolean bFlag = callbackForIndividualService.callback(pojo, serialNum);
                       
            Map<String, String> map = new LinkedHashMap<String, String>();
            LOGGER.info("Callback returns " + bFlag);
            if(bFlag){                
                LOGGER.info("Now return serialNum.");
                LOGGER.info("Response to callback.");
                map.put("serialNum", serialNum);
            }
            response.getWriter().write(JSONObject.toJSONString(map));
        } catch (RuntimeException e) {
            LOGGER.error("Callback failed.", e);
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
        } catch (IOException e) {
            LOGGER.error("Response failed.", e);
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
        }
    }
}
