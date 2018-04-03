package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.ApprovalProcessDefinitionMapper;
import com.cmcc.vrp.province.model.ApprovalDetailDefinition;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.service.ApprovalDetailDefinitionService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.util.QueryObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * ApprovalProcessDefinitionServiceImpl
 * */
@Service("approvalProcessDefinitionService")
public class ApprovalProcessDefinitionServiceImpl implements ApprovalProcessDefinitionService {

    /*@Value("#{settings['first.approval']}")
    public String FIRST_APPROVAL;//一级审核
    @Value("#{settings['second.approval']}")
    public String SECOND_APPROVAL;//二级审核
    @Value("#{settings['third.approval']}")
    public String THIRD_APPROVAL;//三级审核
    @Value("#{settings['forth.approval']}")
    public String FORTH_APPROVAL;//四级审核
    @Value("#{settings['fifth.approval']}")
    public String FIFTH_APPROVAL;//五级审核*/

    //一级审核
    public String firstApproval = "1";
    //二级审核
    public String secondApproval = "2";
    //三级审核
    public String thirdApproval = "4";
    //四级审核
    public String forthApproval = "8";
    //五级审核
    public String fifthApproval = "16";

    @Autowired
    private ApprovalProcessDefinitionMapper mapper;
    @Autowired
    private ApprovalDetailDefinitionService approvalDetailDefinitionService;

    @Override
    public boolean deleteByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        if (id == null) {
            return false;
        }
        return mapper.deleteByPrimaryKey(id) == 1;
    }

    @Override
    public boolean insert(ApprovalProcessDefinition record) {
        // TODO Auto-generated method stub
        if (record == null) {
            return false;
        }
        return mapper.insert(record) == 1;
    }

    @Override
    public ApprovalProcessDefinition selectByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        if (id == null) {
            return null;
        }
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateByPrimaryKey(ApprovalProcessDefinition record) {
        // TODO Auto-generated method stub
        if (record == null || record.getOriginRoleId() == null) {
            return false;
        }
        return mapper.updateByPrimaryKey(record) == 1;
    }

    @Override
    public int countApprovalProcess(QueryObject queryObject) {
        // TODO Auto-generated method stub
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        return mapper.countApprovalProcess(queryObject.toMap());
    }

    @Override
    public List<ApprovalProcessDefinition> queryApprovalProcessList(QueryObject queryObject) {
        // TODO Auto-generated method stub
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        return mapper.queryApprovalProcessList(queryObject.toMap());
    }

    @Override
    public ApprovalProcessDefinition getApprovalProcessById(Long id) {
        // TODO Auto-generated method stub
        if (id == null) {
            return null;
        }
        return mapper.getApprovalProcessById(id);
    }


    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean deleteApprovalProcess(Long id) {
        // TODO Auto-generated method stub
        if (id == null) {
            return false;
        }
        //删除审批流程记录
        if (!deleteByPrimaryKey(id)) {
            return false;
        }
        //删除审批流程详情
        if (!approvalDetailDefinitionService.deleteByApprovalProcess(id)) {
            throw new RuntimeException();
        }
        return true;
    }

    @Override
    public boolean insertApprovalProcess(ApprovalProcessDefinition approvalProcess) {
        // TODO Auto-generated method stub
        if (approvalProcess == null || approvalProcess.getOriginRoleId() == null || approvalProcess.getStage() == null) {
            return false;
        }
        if (!insertProcessAndDetails(initApprovalProcess(approvalProcess), initApprovalDetails(approvalProcess))) {
            return false;
        }
        return true;
    }

    private ApprovalProcessDefinition initApprovalProcess(ApprovalProcessDefinition approvalProcess) {

        switch (approvalProcess.getStage()) {
            case 1:
                approvalProcess.setTargetStatus(Integer.parseInt(firstApproval));
                break;
            case 2:
                approvalProcess.setTargetStatus(Integer.parseInt(firstApproval) + Integer.parseInt(secondApproval));
                break;
            case 3:
                approvalProcess.setTargetStatus(Integer.parseInt(firstApproval) + Integer.parseInt(secondApproval)
                    + Integer.parseInt(thirdApproval));
                break;
            case 4:
                approvalProcess.setTargetStatus(Integer.parseInt(firstApproval) + Integer.parseInt(secondApproval)
                    + Integer.parseInt(thirdApproval) + Integer.parseInt(forthApproval));
                break;
            case 5:
                approvalProcess.setTargetStatus(Integer.parseInt(firstApproval) + Integer.parseInt(secondApproval)
                    + Integer.parseInt(thirdApproval) + Integer.parseInt(forthApproval) + Integer.parseInt(fifthApproval));
                break;
            default:
                approvalProcess.setTargetStatus(0);
                break;
        }
        approvalProcess.setCreateTime(new Date());
        approvalProcess.setUpdateTime(new Date());
        approvalProcess.setDeleteFlag(0);
        return approvalProcess;
    }

    private List<ApprovalDetailDefinition> initApprovalDetails(ApprovalProcessDefinition approvalProcess) {
        if (approvalProcess.getStage().toString().equals("0")) {
            return null;
        }
        List<ApprovalDetailDefinition> approvalDetails = new ArrayList<ApprovalDetailDefinition>();
        //一级审批
        if (approvalProcess.getFirstRoleId() != null) {
            ApprovalDetailDefinition record = new ApprovalDetailDefinition();
            record.setRoleId(approvalProcess.getFirstRoleId());
            record.setApprovalCode(Integer.parseInt(firstApproval));
            record.setPrecondition(0);
            record.setDeleteFlag(0);
            approvalDetails.add(record);
        }
        //二级审批
        if (approvalProcess.getSecondRoleId() != null) {
            ApprovalDetailDefinition record = new ApprovalDetailDefinition();
            record.setRoleId(approvalProcess.getSecondRoleId());
            record.setApprovalCode(Integer.parseInt(secondApproval));
            record.setPrecondition(Integer.parseInt(firstApproval));
            record.setDeleteFlag(0);
            approvalDetails.add(record);
        }
        //三级审批
        if (approvalProcess.getThirdRoleId() != null) {
            ApprovalDetailDefinition record = new ApprovalDetailDefinition();
            record.setRoleId(approvalProcess.getThirdRoleId());
            record.setApprovalCode(Integer.parseInt(thirdApproval));
            record.setPrecondition(Integer.parseInt(firstApproval) + Integer.parseInt(secondApproval));
            record.setDeleteFlag(0);
            approvalDetails.add(record);
        }
        //四级审批
        if (approvalProcess.getForthRoleId() != null) {
            ApprovalDetailDefinition record = new ApprovalDetailDefinition();
            record.setRoleId(approvalProcess.getForthRoleId());
            record.setApprovalCode(Integer.parseInt(forthApproval));
            record.setPrecondition(Integer.parseInt(firstApproval) + Integer.parseInt(secondApproval)
                + Integer.parseInt(thirdApproval));
            record.setDeleteFlag(0);
            approvalDetails.add(record);
        }
        //五级审批
        if (approvalProcess.getFifthRoleId() != null) {
            ApprovalDetailDefinition record = new ApprovalDetailDefinition();
            record.setRoleId(approvalProcess.getFifthRoleId());
            record.setApprovalCode(Integer.parseInt(fifthApproval));
            record.setPrecondition(Integer.parseInt(firstApproval) + Integer.parseInt(secondApproval)
                + Integer.parseInt(thirdApproval) + Integer.parseInt(forthApproval));
            record.setDeleteFlag(0);
            approvalDetails.add(record);
        }
        return approvalDetails;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean insertProcessAndDetails(ApprovalProcessDefinition approvalProcess,
                                           List<ApprovalDetailDefinition> approvalDetails) {
        // TODO Auto-generated method stub
        if (!insert(approvalProcess)) {
            return false;
        }
        if (approvalDetails != null && approvalDetails.size() > 0) {
            for (ApprovalDetailDefinition item : approvalDetails) {
                item.setProcessId(approvalProcess.getId());
            }
            if (!approvalDetailDefinitionService.batchInsert(approvalDetails)) {
                throw new RuntimeException();
            }
        }
        return true;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean updateApprovalProcess(ApprovalProcessDefinition approvalProcess) {
        // TODO Auto-generated method stub
        if (!updateByPrimaryKey(approvalProcess)) {
            return false;
        }
        if (!approvalProcess.getStage().toString().equals("0")) {
            List<ApprovalDetailDefinition> approvalDetails = approvalDetailDefinitionService
                    .getByApprovalProcessId(approvalProcess.getId());

            if (approvalProcess.getStage().toString().equals("1")) {
                approvalDetails.get(0).setRoleId(approvalProcess.getFirstRoleId());
            } else if (approvalProcess.getStage().toString().equals("2")) {
                approvalDetails.get(0).setRoleId(approvalProcess.getFirstRoleId());
                approvalDetails.get(1).setRoleId(approvalProcess.getSecondRoleId());
            } else if (approvalProcess.getStage().toString().equals("3")) {
                approvalDetails.get(0).setRoleId(approvalProcess.getFirstRoleId());
                approvalDetails.get(1).setRoleId(approvalProcess.getSecondRoleId());
                approvalDetails.get(2).setRoleId(approvalProcess.getThirdRoleId());
            } else if (approvalProcess.getStage().toString().equals("4")) {
                approvalDetails.get(0).setRoleId(approvalProcess.getFirstRoleId());
                approvalDetails.get(1).setRoleId(approvalProcess.getSecondRoleId());
                approvalDetails.get(2).setRoleId(approvalProcess.getThirdRoleId());
                approvalDetails.get(3).setRoleId(approvalProcess.getForthRoleId());
            } else if (approvalProcess.getStage().toString().equals("5")) {
                approvalDetails.get(0).setRoleId(approvalProcess.getFirstRoleId());
                approvalDetails.get(1).setRoleId(approvalProcess.getSecondRoleId());
                approvalDetails.get(2).setRoleId(approvalProcess.getThirdRoleId());
                approvalDetails.get(3).setRoleId(approvalProcess.getForthRoleId());
                approvalDetails.get(4).setRoleId(approvalProcess.getFifthRoleId());
            }

            for (ApprovalDetailDefinition approvalDetail : approvalDetails) {
                if (!approvalDetailDefinitionService.updateByPrimaryKey(approvalDetail)) {
                    throw new RuntimeException();
                }
            }
        }
        return true;
    }

    @Override
    public ApprovalProcessDefinition selectByType(Integer type) {
        // TODO Auto-generated method stub
        if (type == null) {
            return null;
        }
        return mapper.selectByType(type);
    }

    @Override
    public List<ApprovalProcessDefinition> selectApprovalProcessesByType(Integer type) {
        if(type==null){
            return null;
        }
        return mapper.selectApprovalProcessesByType(type);
    }

    @Override
    public Boolean hasAuthToSubmitApproval(Long roleId, Integer type) {
        // TODO Auto-generated method stub
        if (roleId == null || type == null) {
            return false;
        }
        ApprovalProcessDefinition approvalProcess = selectByType(type);
        if (approvalProcess != null && approvalProcess.getOriginRoleId().toString().equals(roleId.toString())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean wheatherNeedApproval(Integer approvalType) {
        if(approvalType!=null){
            ApprovalProcessDefinition approvalProcess = selectByType(approvalType);
            if(approvalProcess!=null){
                if(!approvalProcess.getStage().toString().equals("0")){
                    return true;
                }
            }
        }
        return false;
    }
}
