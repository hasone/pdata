package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.UrlMap;

/**
 * Created by leelyn on 2017/1/13.
 */
public interface UrlMapService {

    /**
     * @param uuid
     * @return
     */
    UrlMap queryByUUID(String uuid);

    /**
     * @param urlMap
     * @return
     */
    boolean createUrlMap(UrlMap urlMap);
}
