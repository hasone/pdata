package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.model.CallbackPojo;
import com.cmcc.vrp.province.service.CallbackService;
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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by sunyiwei on 16-3-29.
 */
@Controller
@RequestMapping("/api")
public class CallbackController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackController.class);

    @Autowired
    CallbackService callbackService;

    /**
     * 营销模板的回调
     * @param request  请求
     * @param response 响应
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
            boolean bFlag = callbackService.callback(pojo, serialNum);
            LOGGER.info("Callback returns " + bFlag);
            LOGGER.info("Now return serialNum.");
            LOGGER.info("Response to callback.");

            Map<String, String> map = new LinkedHashMap<String, String>();
            if(bFlag){
                //如果一切没有问题，则返回序列号，否则不返回，让其不中奖
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
    
    
    /**
     * 用于回调测试，勿删
     * @author qinqinyan
     * */
    @RequestMapping("testCallback")
    public void testCallback(){
        CallbackPojo pojo = createCallbackPojo();
        
        String serialNum = DigestUtils.md5Hex(UUID.randomUUID().toString());

        LOGGER.info("Receive callback with pojo = {}.", JSONObject.toJSON(pojo));
        boolean bFlag = callbackService.callback(pojo, serialNum);
        LOGGER.info("Callback returns " + bFlag);
        LOGGER.info("Now return serialNum.");
        LOGGER.info("Response to callback.");

        if(bFlag){
            //如果一切没有问题，则返回序列号，否则不返回，让其不中奖
            LOGGER.info("everything is ok");
        }
    }
    
    private CallbackPojo createCallbackPojo(){
        CallbackPojo pojo = new CallbackPojo();

        pojo.setActiveId("6286768482188410880");
        pojo.setCatName("移动");
        pojo.setCreateTime(new Date());
        pojo.setDeleteFlag(0);
        pojo.setDescription("测试随机红包");
        pojo.setEnterId("432");
        pojo.setGameType(4);
        pojo.setId(104681);
        pojo.setMobile("18867103717");
        pojo.setPlatName("sgs_sctestdev");
        pojo.setPrizeCount(27);
        pojo.setPrizeId("594");
        pojo.setPrizeName("27个");
        pojo.setPrizeResponse(1);
        pojo.setPrizeSource("432");
        pojo.setPrizeType(0);
        pojo.setRankName("一等奖");
        return pojo;
    }
}
