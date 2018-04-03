package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.SmsTemplateMapper;
import com.cmcc.vrp.province.model.SmsTemplate;
import com.cmcc.vrp.province.service.SmsTemplateService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 短信模板service
 *
 * @author kok
 */
@Service("smsTemplateService")
public class SmsTemplateServiceImpl implements SmsTemplateService {

    @Autowired
    private SmsTemplateMapper smsTemplateMapper;

    public boolean deleteByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        if (id > 0) {
            if (smsTemplateMapper.deleteByPrimaryKey(id) > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean insertSelective(SmsTemplate record) {
        // TODO Auto-generated method stub
        if (record != null && record.getName() != null
            && record.getContent() != null) {
            record.setCreateTime(new Date());
            if (smsTemplateMapper.insertSelective(record) > 0) {
                return true;
            }

        }
        return false;
    }

    public SmsTemplate selectByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        return smsTemplateMapper.selectByPrimaryKey(id);
    }

    public boolean updateByPrimaryKeySelective(SmsTemplate record) {
        // TODO Auto-generated method stub
        if (record != null && record.getId() > 0) {
            if (smsTemplateMapper.updateByPrimaryKeySelective(record) > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int countSmsTemplate(Map<String, Object> map) {
        // TODO Auto-generated method stub

        return smsTemplateMapper.countSmsTemplate(map);
    }

    @Override
    public List<SmsTemplate> showSmsTemplate(Map<String, Object> map) {

        return smsTemplateMapper.showSmsTemplate(map);
    }

    @Override
    public List<SmsTemplate> checkSms(String name) {
        return smsTemplateMapper.checkSms(name);
    }

    /**
     * @param name
     * @return
     * @Title: get
     * @Description: TODO
     * @see com.cmcc.vrp.province.service.SmsTemplateService#get(java.lang.String)
     */
    @Override
    public SmsTemplate get(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }

        return smsTemplateMapper.get(name);
    }
}
