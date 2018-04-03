package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.RuleSmsTemplateMapper;
import com.cmcc.vrp.province.model.RuleSmsTemplate;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.RuleSmsTemplateService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.QueryObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("ruleSmsTemplateService")
public class RuleSmsTemplateServiceImpl implements RuleSmsTemplateService {

    @Autowired
    RuleSmsTemplateMapper mapper;

    @Autowired
    private AdminRoleService adminRoleService;

    @Override
    public boolean deleteByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        if (id == null) {
            return false;
        }
        RuleSmsTemplate ruleSmsTemplate = selectByPrimaryKey(id);
        ruleSmsTemplate.setDelete(Constants.DELETE_FLAG.DELETED.getValue());
        ruleSmsTemplate.setUpdateTime(new Date());
        return mapper.updateByPrimaryKeySelective(ruleSmsTemplate) == 1;
    }

    @Override
    public boolean insert(RuleSmsTemplate record) {
        // TODO Auto-generated method stub
        if (record == null) {
            return false;
        }

        record.setName(record.getName().replaceAll(" ", ""));
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setDelete(Constants.DELETE_FLAG.UNDELETED.getValue());

        return mapper.insert(record) == 1;
    }

    @Override
    public RuleSmsTemplate selectByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        if (id == null) {
            return null;
        }
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateByPrimaryKeySelective(RuleSmsTemplate record) {
        // TODO Auto-generated method stub
        if (record == null) {
            return false;
        }
        record.setUpdateTime(new Date());
        record.setName(record.getName().replaceAll(" ", ""));
        return mapper.updateByPrimaryKeySelective(record) == 1;
    }

    @Override
    public Long count(QueryObject queryObject) {
        // TODO Auto-generated method stub
        if (queryObject == null) {
            return 0L;
        }
        Long result = mapper.count(queryObject.toMap());
        return (result == null ? 0 : result);
    }

    @Override
    public List<RuleSmsTemplate> listRuleSmsTemplate(QueryObject queryObject) {
        // TODO Auto-generated method stub
        if (queryObject == null) {
            return null;
        }
        return mapper.listRuleSmsTemplate(queryObject.toMap());
    }

    /**
     * @param type 短信类型
     */
    @Override
    public List<RuleSmsTemplate> getRuleSmsTemplateByCreator(Long creatorId, String type) {
        // TODO Auto-generated method stub

        if (creatorId == null || StringUtils.isBlank(type)) {
            return null;
        }
        List<RuleSmsTemplate> list = null;
        Long roleId = adminRoleService.getRoleIdByAdminId(creatorId);
        QueryObject queryObject = new QueryObject();
        queryObject.getQueryCriterias().put("type", type);
        if (roleId.intValue() == 1) {
            list = mapper.getRuleTemplateByCreatorId(queryObject.toMap());
        } else {
            queryObject.getQueryCriterias().put("creatorId", creatorId);
            queryObject.getQueryCriterias().put("roleId", roleId);
            list = mapper.getRuleTemplateByCreatorId(queryObject.toMap());
        }
        return list;
    }

}
