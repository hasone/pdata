package com.cmcc.vrp.province.service.impl;

import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.service.XssService;
import com.cmcc.vrp.util.StringUtils;

@Service("xssService")
public class XssServiceImpl implements XssService{

    @Override
    public String stripXss(String value) {
        // TODO Auto-generated method stub
        if (!StringUtils.isEmpty(value)) {
            value = value.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;")
                    .replaceAll("'", "&apos;")
                    .replaceAll("\"", "&quot;");

        }
        return value;
    }

    @Override
    public String stripQuot(String value) {
        // TODO Auto-generated method stub
        if (!StringUtils.isEmpty(value)) {
            value = value.replaceAll("\"", "&quot;");
        }
        return value;
    }

}
