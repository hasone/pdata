package com.cmcc.vrp.province.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.ApprovalRecordMapper;
import com.cmcc.vrp.province.model.ActivityApprovalDetail;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.service.ActivityApprovalDetailService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;

/**
 * ApprovalRecordServiceImpl
 * */
@Service("approvalRecordService")
public class ApprovalRecordServiceImpl implements ApprovalRecordService {
    @Autowired
    ApprovalRecordMapper mapper;
    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Autowired
    ApprovalRequestService approvalRequestService;
    @Autowired
    ActivityApprovalDetailService activityApprovalDetailService;

    @Override
    public ApprovalRecord selectByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        if (id == null) {
            return null;
        }
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ApprovalRecord> selectByRequestId(Long requestId) {
        // TODO Auto-generated method stub
        if (requestId == null) {
            return null;
        }
        return mapper.selectByRequestId(requestId);
    }

    @Override
    public List<ApprovalRecord> selectByRequestIdAll(Long requestId) {
        if (requestId == null) {
            return null;
        }
        return mapper.selectByRequestIdAll(requestId);
    }

    @Override
    public boolean insertApprovalRecord(ApprovalRecord record) {
        // TODO Auto-generated method stub
        if (record == null) {
            return false;
        }
        //return mapper.insertApprovalRecord(record)==1;
        return mapper.insert(record) == 1;
    }

    @Override
    public boolean updateApprovalRecord(ApprovalRecord record) {
        // TODO Auto-generated method stub
        if (record == null) {
            return false;
        }
        return mapper.updateApprovalRecord(record) == 1;
    }

    @Override
    public List<ApprovalRecord> selectByEndIdAndProcessType(Long entId, Integer type) {
        // TODO Auto-generated method stub
        if (entId == null || type == null) {
            return null;
        }
        ApprovalProcessDefinition approvalProcess = approvalProcessDefinitionService.selectByType(type);
        List<ApprovalRequest> approvalRequests = approvalRequestService.selectByEntIdAndProcessId(entId,
                approvalProcess.getId());
        return selectByApprovalRequests(approvalRequests);
    }

    @Override
    public List<ApprovalRecord> selectByApprovalRequests(List<ApprovalRequest> approvalRequests) {
        // TODO Auto-generated method stub
        if (approvalRequests == null || approvalRequests.size() < 1) {
            return null;
        }
        QueryObject queryObject = new QueryObject();
        Map map = queryObject.toMap();
        map.put("approvalRequests", approvalRequests);
        List<ApprovalRecord> approvalRecords = mapper.selectByMap(map);
        return sortAscendingById(approvalRecords);
    }

    List<ApprovalRecord> sortAscendingById(List<ApprovalRecord> approvalRecords) {
        if (approvalRecords == null || approvalRecords.size() < 1) {
            return null;
        }
        if (approvalRecords.size() == 1) {
            return approvalRecords;
        }
        Collections.sort(approvalRecords, new Comparator<ApprovalRecord>() {
            public int compare(ApprovalRecord arg0, ApprovalRecord arg1) {
                long hits0 = arg0.getId().longValue();
                long hits1 = arg1.getId().longValue();
                if (hits1 < hits0) {
                    return 1;
                } else if (hits1 == hits0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        //职位=职位+角色
        for (ApprovalRecord item : approvalRecords) {
            if (item.getManagerName() != null && item.getRoleName() != null) {
                item.setManagerName(item.getManagerName() + item.getRoleName());
            }
        }
        return approvalRecords;
    }

    @Override
    public ApprovalRecord selectNewRecordByRequestId(Long requestId, Long currUserId) {
        if (requestId == null || currUserId == null) {
            return null;
        }
        return mapper.selectNewRecordByRequestId(requestId, currUserId);
    }

    @Override
    public List<ApprovalRecord> selectByActivityIdForActivity(String activityId) {
        if (!StringUtils.isEmpty(activityId)) {
            List<ActivityApprovalDetail> activityApprovalDetails = activityApprovalDetailService
                    .selectByActivityId(activityId);
            if (activityApprovalDetails != null && activityApprovalDetails.size() > 0) {
                Map<String, Object> map = new HashedMap();
                map.put("activityApprovalDetails", activityApprovalDetails);
                List<ApprovalRecord> approvalRecords = mapper.selectByMap(map);
                return sortAscendingById(approvalRecords);
            }
        }
        return null;
    }

    @Override
    public List<ApprovalRecord> getRecords(QueryObject queryObject) {
        // TODO Auto-generated method stub
        return mapper.getRecords(queryObject.toMap());
    }

    @Override
    public Long countRecords(QueryObject queryObject) {
        // TODO Auto-generated method stub
        return mapper.countRecords(queryObject.toMap());
    }

    @Override
    public Long getNewApprovalRecord(Long id) {
        // TODO Auto-generated method stub
        List<ApprovalRecord> approvalRecords = selectByRequestId(id);
        if(approvalRecords!=null && approvalRecords.size()>0){
            for(int i = approvalRecords.size() - 1; i>=0; i--){
                if(approvalRecords.get(i).getIsNew().toString().equals("1")){
                    return approvalRecords.get(i).getId();
                }
            }
        }
        return null;
    }
}
