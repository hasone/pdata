package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.EnterpriseSmsTemplate;

import java.util.List;

public interface EnterpriseSmsTemplateService {

    boolean insert(EnterpriseSmsTemplate record);

    List<Long> selectByEnterId(Long enterId);

    void updateEnterpriseSmsTemplate(Long enterId,
                                     List<Long> smsTemplatesList);

    List<EnterpriseSmsTemplate> selectSmsTemplateByEnterId(Long enterId);

    void setSmsTemplateForEnterprise(Long enterId, Long smsTemplateId);

    EnterpriseSmsTemplate getChoosedSmsTemplate(Long enterId);

    void insertDefaultSmsTemplate(Long id);

}
