package com.cmcc.vrp.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by sunyiwei on 2016/5/31.
 */
public class IpUtils {
    //获取真实的用户IP，而不是代理的IP
    public static String getRemoteAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (org.apache.commons.lang.StringUtils.isNotBlank(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }

        ip = request.getHeader("X-Real-IP");
        if (org.apache.commons.lang.StringUtils.isNotBlank(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}
