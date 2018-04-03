package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.ApprovalDetailDefinitionMapper;
import com.cmcc.vrp.province.model.ApprovalDetailDefinition;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.service.ApprovalDetailDefinitionService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审批流程详情定义服务类
 * @author qinqinyan
 * */
@Service("approvalDetailDefinitionService")
public class ApprovalDetailDefinitionServiceImpl implements ApprovalDetailDefinitionService {
    @Autowired
    ApprovalDetailDefinitionMapper mapper;
    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;

    @Override
    public boolean updateByPrimaryKey(ApprovalDetailDefinition record) {
        // TODO Auto-generated method stub
        if (record == null || record.getId() == null || record.getRoleId() == null) {
            return false;
        }
        return mapper.updateByPrimaryKey(record) == 1;
    }

    @Override
    public List<ApprovalDetailDefinition> getByApprovalProcessId(Long id) {
        // TODO Auto-generated method stub
        if (id == null) {
            return null;
        }
        return mapper.getByApprovalProcessId(id);
    }

    @Override
    public boolean deleteByApprovalProcess(Long id) {
        // TODO Auto-generated method stub
        if (id == null) {
            return false;
        }
        return mapper.deleteByApprovalProcess(id) >= 0;
    }

    @Override
    public boolean batchInsert(List<ApprovalDetailDefinition> approvalDetails) {
        // TODO Auto-generated method stub
        if (approvalDetails == null || approvalDetails.size() < 1) {
            return true;
        }
        return mapper.batchInsert(approvalDetails) == approvalDetails.size();
    }

    @Override
    public ApprovalDetailDefinition getCurrentApprovalDetailByProccessId(Long processId, Integer currentStatus) {
        // TODO Auto-generated method stub
        if (processId == null) {
            return null;
        }
        List<ApprovalDetailDefinition> approvalDetails = getByApprovalProcessId(processId);

        for (ApprovalDetailDefinition approvalDetail : approvalDetails) {
            if (currentStatus.toString().equals(approvalDetail.getPrecondition().toString())) {
                return approvalDetail;
            }
        }
        return null;
    }

    @Override
    public ApprovalDetailDefinition getNextApprovalDetailByProccessId(Long processId, Integer currentStatus) {
        // TODO Auto-generated method stub
        if (processId == null) {
            return null;
        }
        ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService.selectByPrimaryKey(processId);
        List<ApprovalDetailDefinition> approvalDetails = getByApprovalProcessId(processId);
        for (int i = 0; i < approvalDetails.size(); i++) {
            if (currentStatus.toString().equals(approvalDetails.get(i).getPrecondition().toString())) {
                int nextStatus = approvalDetails.get(i).getPrecondition().intValue() + approvalDetails.get(i).getApprovalCode().intValue();
                if (approvalProcessDefinition.getTargetStatus().intValue() == nextStatus) {
                    //当前是最后一个层级
                    return null;
                }
                return approvalDetails.get(i + 1);
            }
        }
        return null;
    }

    @Override
    public ApprovalDetailDefinition getLastApprovalDetailByProccessId(Long processId) {
        // TODO Auto-generated method stub
        if (processId == null) {
            return null;
        }
        List<ApprovalDetailDefinition> approvalDetails = getByApprovalProcessId(processId);
        if (approvalDetails != null && approvalDetails.size() > 0) {
            return approvalDetails.get(approvalDetails.size() - 1);
        }
        return null;
    }

}
