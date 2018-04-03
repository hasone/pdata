package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.UrlMapMapper;
import com.cmcc.vrp.province.model.UrlMap;
import com.cmcc.vrp.province.service.UrlMapService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by leelyn on 2017/1/13.
 */
@Service
public class UrlMapServiceImpl implements UrlMapService {

    @Autowired
    private UrlMapMapper urlMapMapper;

    @Override
    public UrlMap queryByUUID(String uuid) {
        return StringUtils.isBlank(uuid) ? null : urlMapMapper.selectByUUID(uuid);
    }

    @Override
    public boolean createUrlMap(UrlMap urlMap) {
        if (urlMap == null) {
            return false;
        }
        return urlMapMapper.insertSelective(urlMap) == 1;
    }
}
