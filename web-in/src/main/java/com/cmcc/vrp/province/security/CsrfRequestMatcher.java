package com.cmcc.vrp.province.security;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sunyiwei on 2016/8/23.
 */
public class CsrfRequestMatcher implements RequestMatcher {
    private List<String> excludeUrls = new LinkedList<String>();

    @Override
    public boolean matches(HttpServletRequest request) {
        String url = request.getServletPath();

        for (String excludeUrl : excludeUrls) {
            if (excludeUrl.equals(url)) {
                return false;
            }
        }

        return true;
    }

    public List<String> getExcludeUrls() {
        return excludeUrls;
    }

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

}
