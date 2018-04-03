package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.SmsTemplate;

import java.util.List;
import java.util.Map;

/**
 * SmsTemplateService.java
 * @author wujiamin
 * @date 2016年11月11日
 */
public interface SmsTemplateService {
    /** 
     * @Title: deleteByPrimaryKey 
    */
    boolean deleteByPrimaryKey(Long id);

    /** 
     * @Title: insertSelective 
    */
    boolean insertSelective(SmsTemplate record);

    /** 
     * @Title: selectByPrimaryKey 
    */
    SmsTemplate selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKeySelective 
    */
    boolean updateByPrimaryKeySelective(SmsTemplate record);

    /** 
     * @Title: countSmsTemplate 
    */
    int countSmsTemplate(Map<String, Object> map);

    /** 
     * @Title: showSmsTemplate 
    */
    List<SmsTemplate> showSmsTemplate(Map<String, Object> map);

    /** 
     * @Title: checkSms 
    */
    List<SmsTemplate> checkSms(String name);

    /*
     * 根据模板名称获取模板对象
     */
    /** 
     * @Title: get 
    */
    SmsTemplate get(String name);
}
