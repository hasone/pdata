package com.cmcc.vrp.ec.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.vrp.ec.bean.TimeResp;
import com.cmcc.vrp.ec.controller.aop.LimitIPRequest;
import com.thoughtworks.xstream.XStream;

/**
 * @desc: 限制某个IP在某个时间段内请求某个方法的次数
 * @author: wuguoping
 * @data: 2017年5月9日
 */
@Controller
@RequestMapping(value = "validate")
public class TimesGetController {
    private static final Logger logger = LoggerFactory.getLogger(AuthValidateController.class);
    private static String TIME_FOMMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    /**
     * 
     * title: timeGet
     * desc: 
     * @param response
     * @throws Exception
     * wuguoping
     * 2017年5月31日
     */
    @LimitIPRequest(limitCounts = 10, limitTime = 60)
    @ResponseBody
    @RequestMapping(value = "time", method = RequestMethod.GET)
    public void timeGet(HttpServletResponse response) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String currentTime = sdf.format(new Date());
        logger.info("currentTime = {}", currentTime);

        TimeResp time = new TimeResp();
        time.setResponseTime(currentTime);

        XStream xStream = new XStream();
        xStream.alias("Response", TimeResp.class);
        xStream.autodetectAnnotations(true);

        try {
            StreamUtils.copy(xStream.toXML(time), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            logger.error("获取服务器时间出错，错误信息为{}.", e.toString());
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
