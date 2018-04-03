package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.EnterpriseSmsTemplateMapper;
import com.cmcc.vrp.province.model.EnterpriseSmsTemplate;
import com.cmcc.vrp.province.model.SmsTemplate;
import com.cmcc.vrp.province.service.EnterpriseSmsTemplateService;
import com.cmcc.vrp.province.service.SmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service("enterpriseSmsTemplateService")
public class EnterpriseSmsTemplateServiceImpl implements EnterpriseSmsTemplateService {

    @Autowired
    EnterpriseSmsTemplateMapper enterpriseSmsTemplateMapper;
    @Autowired
    SmsTemplateService smsTemplateService;

    @Override
    public boolean insert(EnterpriseSmsTemplate record) {
        if (enterpriseSmsTemplateMapper.insert(record) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Long> selectByEnterId(Long enterId) {
        return enterpriseSmsTemplateMapper.selectByEnterId(enterId);
    }

    @Override
    public List<EnterpriseSmsTemplate> selectSmsTemplateByEnterId(Long enterId) {
        return enterpriseSmsTemplateMapper.selectSmsTemplateByEnterId(enterId);
    }

    @Override
    public void updateEnterpriseSmsTemplate(Long enterId,
                                            List<Long> smsTemplatesList) {
        //1、删除企业原模板
        enterpriseSmsTemplateMapper.deleteByEnterId(enterId);
        //2、插入企业原模板
        if (smsTemplatesList != null && smsTemplatesList.size() > 0) {
            for (Long st : smsTemplatesList) {
                EnterpriseSmsTemplate est = new EnterpriseSmsTemplate();
                est.setEnterId(enterId);
                est.setSmsTemplateId(st);
                est.setStatus(0);
                enterpriseSmsTemplateMapper.insert(est);
            }
        }
    }

    @Override
    public void setSmsTemplateForEnterprise(Long enterId, Long smsTemplateId) {
        List<EnterpriseSmsTemplate> lists = selectSmsTemplateByEnterId(enterId);
        if (lists != null && lists.size() > 0) {
            for (EnterpriseSmsTemplate template : lists) {
                if (template.getStatus() == 1) {
                    enterpriseSmsTemplateMapper.updateStatus(enterId, template.getSmsTemplateId(), 0);
                }
            }
        }
        if (!smsTemplateId.equals(0L)) {
            enterpriseSmsTemplateMapper.updateStatus(enterId, smsTemplateId, 1);
        }
    }

    @Override
    public EnterpriseSmsTemplate getChoosedSmsTemplate(Long enterId) {
        List<EnterpriseSmsTemplate> list = enterpriseSmsTemplateMapper.getChoosedSmsTemplate(enterId);
        if (list != null && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void insertDefaultSmsTemplate(Long id) {
        List<SmsTemplate> templates = smsTemplateService.showSmsTemplate(new HashMap());
        if (templates != null && templates.size() > 0) {
            for (SmsTemplate template : templates) {
                if (template.getDefaultHave() == 1) {
                    EnterpriseSmsTemplate est1 = new EnterpriseSmsTemplate();
                    est1.setEnterId(id);
                    est1.setSmsTemplateId(template.getId());
                    est1.setStatus(template.getDefaultUse());
                    if (enterpriseSmsTemplateMapper.insert(est1) < 0) {
                        throw new RuntimeException();
                    }
                }
            }
        }
    }

}
