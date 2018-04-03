package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.SmsTemplate;

import java.util.List;
import java.util.Map;


/**
 * @Title:SmsTemplateMapper
 * @Description:
 * */
public interface SmsTemplateMapper {

    /**
     * @Title:deleteByPrimaryKey
     * @Description:
     * */
    int deleteByPrimaryKey(Long id);


    /**
     * @Title:insertSelective
     * @Description:
     * */
    int insertSelective(SmsTemplate record);


    /**
     * @Title:selectByPrimaryKey
     * @Description:
     * */
    SmsTemplate selectByPrimaryKey(Long id);


    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:
     * */
    int updateByPrimaryKeySelective(SmsTemplate record);


    /**
     * @Title:countSmsTemplate
     * @Description:
     * */
    int countSmsTemplate(Map<String, Object> map);

    /**
     * @Title:showSmsTemplate
     * @Description:
     * */

    List<SmsTemplate> showSmsTemplate(Map<String, Object> map);

    /**
     * @Title:checkSms
     * @Description:
     * */
    List<SmsTemplate> checkSms(String name);


    /**
     * @Title:get
     * @Description:
     * */
    SmsTemplate get(String name);

}