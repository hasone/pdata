package com.cmcc.vrp.async.controller;

import com.cmcc.vrp.ec.bean.PhoneRegionResp;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.util.Provinces;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by sunyiwei on 2016/6/27.
 */
@RequestMapping
@Controller
public class PhoneRegionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneRegionController.class);

    @Autowired
    PhoneRegionService phoneRegionService;

    /**
     * @param mobile
     * @param outputStream
     */
    @RequestMapping(value = "/hlr/{mobile}", method = RequestMethod.GET)
    public void query(@PathVariable("mobile") String mobile, OutputStream outputStream) {
        PhoneRegion phoneRegion = phoneRegionService.query(mobile);

        String provinceCode = (phoneRegion == null)
                ? Provinces.Unknown.getCode()
                : Provinces.fromName(phoneRegion.getProvince()).getCode();

        //响应
        writeResp(mobile, provinceCode, outputStream);
    }

    private void writeResp(String mobile, String code, OutputStream outputStream) {
        PhoneRegionResp phoneRegionResp = new PhoneRegionResp(mobile, code);

        XStream xStream = new XStream();
        xStream.processAnnotations(PhoneRegionResp.class);
        try {
            StreamUtils.copy(xStream.toXML(phoneRegionResp), Charsets.UTF_8, outputStream);
        } catch (IOException e) {
            LOGGER.error("查询号码归属地响应失败.");
        }
    }
}
