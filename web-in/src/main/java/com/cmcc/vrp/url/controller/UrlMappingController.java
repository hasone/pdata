package com.cmcc.vrp.url.controller;

import com.cmcc.vrp.boss.RedisUtilService;
import com.cmcc.vrp.province.model.UrlMap;
import com.cmcc.vrp.province.service.UrlMapService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by leelyn on 2017/1/12.
 */
@Controller
@RequestMapping(value = "vi")
public class UrlMappingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlMappingController.class);

    @Autowired
    UrlMapService urlMapService;

    @Autowired
    RedisUtilService redisUtilService;

    /**
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public String urlMapping(@PathVariable("uuid") String uuid) {
        String realUrl;
        if (StringUtils.isBlank(uuid)) {
            LOGGER.error("uuid为空");
            return "redirect:error/404.html";
        }
        //从缓存中读取
        realUrl = redisUtilService.get(uuid);
        if (StringUtils.isNotBlank(realUrl)) {
            return "redirect:" + realUrl;
        }
        //从数据库中读取
        UrlMap urlMap;
        if ((urlMap = urlMapService.queryByUUID(uuid)) != null
                && StringUtils.isNotBlank(realUrl = urlMap.getRealUrl())) {
            return "redirect:" + realUrl;
        }
        LOGGER.error("根据uuid没有检索到URL");
        return "redirect:error/404.html";
    }
}
