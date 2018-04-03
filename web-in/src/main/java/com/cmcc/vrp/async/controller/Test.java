package com.cmcc.vrp.async.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.vrp.ec.service.AuthService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ChargeQueryPojo;
import com.cmcc.vrp.util.DateUtil;

/**
 * Created by leelyn on 2016/5/17.
 */
@Controller
public class Test {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    @Autowired
    AuthService authService;
    @Autowired
    TaskProducer taskProducer;
    @Autowired
    GlobalConfigService globalConfigService;

    /** 
     * @Title: auth 
     * @param request
     * @param response
     * @throws IOException
     * @Author: wujiamin
     * @date 2016年10月17日下午4:55:18
    */
    @RequestMapping(value = "test", method = RequestMethod.POST)
    @ResponseBody
    public void auth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ChargeQueryPojo pojo = new ChargeQueryPojo();
        pojo.setSystemNum("6290761462679359488");
        pojo.setFingerPrint("neimenggu123456789");
        pojo.setEntId(623l);
        pojo.setBeginTime(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        pojo.setTimes(0);
        taskProducer.produceAsynDeliverQueryMsg(pojo);
        
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
