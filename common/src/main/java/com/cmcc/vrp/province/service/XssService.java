package com.cmcc.vrp.province.service;

/**
 * 转义特殊字符
 * */
public interface XssService {
    
    /**
     * 
     * */
    String stripXss(String value);
    
    /**
     * 仅转义双引号
     * */
    String stripQuot(String value);

}
