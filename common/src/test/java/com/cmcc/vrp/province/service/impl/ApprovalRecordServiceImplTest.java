package com.cmcc.vrp.province.service.impl;

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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;

/**
 * Created by qinqinyan on 2016/10/31.
 */
@RunWith(MockitoJUnitRunner.class)
public class ApprovalRecordServiceImplTest {

    @InjectMocks
    ApprovalRecordService approvalRecordService = new ApprovalRecordServiceImpl();
    @Mock
    ApprovalRecordMapper approvalRecordMapper;
    @Mock
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Mock
    ApprovalRequestService approvalRequestService;
    @Mock
    ActivityApprovalDetailService activityApprovalDetailService;

    @Test
    public void testSelectByPrimaryKey(){
        assertNull(approvalRecordService.selectByPrimaryKey(null));

        Long id = 1L;
        Mockito.when(approvalRecordMapper.selectByPrimaryKey(anyLong())).thenReturn(new ApprovalRecord());
        assertNotNull(approvalRecordService.selectByPrimaryKey(id));
        Mockito.verify(approvalRecordMapper).selectByPrimaryKey(anyLong());
    }

    @Test
    public void testSelectByRequestId(){
        assertNull(approvalRecordService.selectByRequestId(null));

        Long requestId = 1L;
        Mockito.when(approvalRecordMapper.selectByRequestId(anyLong())).thenReturn(new ArrayList<ApprovalRecord>());
        assertNotNull(approvalRecordService.selectByRequestId(requestId));
        Mockito.verify(approvalRecordMapper).selectByRequestId(anyLong());
    }

    @Test
    public void testSelectByRequestIdAll(){
        assertNull(approvalRecordService.selectByRequestIdAll(null));

        Long requestId = 1L;
        Mockito.when(approvalRecordMapper.selectByRequestIdAll(anyLong())).thenReturn(new ArrayList<ApprovalRecord>());
        assertNotNull(approvalRecordService.selectByRequestIdAll(requestId));
        Mockito.verify(approvalRecordMapper).selectByRequestIdAll(anyLong());
    }

    @Test
    public void testInsertApprovalRecord(){
        assertFalse(approvalRecordService.insertApprovalRecord(null));

        Mockito.when(approvalRecordMapper.insert(any(ApprovalRecord.class))).thenReturn(1);
        assertTrue(approvalRecordService.insertApprovalRecord(new ApprovalRecord()));
        Mockito.verify(approvalRecordMapper).insert(any(ApprovalRecord.class));
    }

    @Test
    public void testUpdateApprovalRecord(){
        assertFalse(approvalRecordService.updateApprovalRecord(null));

        Mockito.when(approvalRecordMapper.updateApprovalRecord(any(ApprovalRecord.class))).thenReturn(1);
        assertTrue(approvalRecordService.updateApprovalRecord(new ApprovalRecord()));
        Mockito.verify(approvalRecordMapper).updateApprovalRecord(any(ApprovalRecord.class));
    }

    @Test
    public void testSelectByEndIdAndProcessType(){
        //init
        Long entId = 1L;
        Integer type = 1;

        ApprovalProcessDefinition approvalProcess = new ApprovalProcessDefinition();
        approvalProcess.setId(1L);

        List<ApprovalRequest> approvalRequests = new ArrayList<ApprovalRequest>();
        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setId(1L);
        approvalRequests.add(approvalRequest);

        List<ApprovalRecord> approvalRecords = new ArrayList<ApprovalRecord>();
        ApprovalRecord approvalRecord1 = initApprovalRecord(7L);
        ApprovalRecord approvalRecord2 = initApprovalRecord(1L);
        ApprovalRecord approvalRecord3 = initApprovalRecord(8L);
        approvalRecords.add(approvalRecord1);
        approvalRecords.add(approvalRecord2);
        approvalRecords.add(approvalRecord3);

        assertNull(approvalRecordService.selectByEndIdAndProcessType(null, type));
        assertNull(approvalRecordService.selectByEndIdAndProcessType(entId, null));

        Mockito.when(approvalProcessDefinitionService.selectByType(anyInt())).thenReturn(approvalProcess);
        Mockito.when(approvalRequestService.selectByEntIdAndProcessId(anyLong(), anyLong())).thenReturn(approvalRequests);
        Mockito.when(approvalRecordMapper.selectByMap(anyMap())).thenReturn(approvalRecords);

        assertNotNull(approvalRecordService.selectByEndIdAndProcessType(entId, type));

        Mockito.verify(approvalProcessDefinitionService).selectByType(anyInt());
        Mockito.verify(approvalRequestService).selectByEntIdAndProcessId(anyLong(), anyLong());
        Mockito.verify(approvalRecordMapper).selectByMap(anyMap());
    }

    @Test
    public void testSelectByApprovalRequests(){
        List<ApprovalRequest> approvalRequests = new ArrayList<ApprovalRequest>();
        List<ApprovalRecord> approvalRecords = new ArrayList<ApprovalRecord>();

        assertNull(approvalRecordService.selectByApprovalRequests(approvalRequests));

        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setId(1L);
        approvalRequests.add(approvalRequest);

        Mockito.when(approvalRecordMapper.selectByMap(anyMap())).thenReturn(approvalRecords);
        assertNull(approvalRecordService.selectByApprovalRequests(approvalRequests));

        Mockito.verify(approvalRecordMapper).selectByMap(anyMap());
    }

    private ApprovalRecord initApprovalRecord(Long id){
        ApprovalRecord record = new ApprovalRecord();
        record.setId(id);
        record.setManagerName("测试省");
        record.setRoleName("测试管理员");
        return record;
    }

    @Test
    public void testSelectNewRecordByRequestId(){
        Long requestId = 1L;
        Long currUserId = 1L;
        assertNull(approvalRecordService.selectNewRecordByRequestId(null, currUserId));
        assertNull(approvalRecordService.selectNewRecordByRequestId(requestId, null));

        Mockito.when(approvalRecordMapper.selectNewRecordByRequestId(anyLong(), anyLong())).thenReturn(new ApprovalRecord());
        assertNotNull(approvalRecordService.selectNewRecordByRequestId(requestId, currUserId));
        Mockito.verify(approvalRecordMapper).selectNewRecordByRequestId(anyLong(), anyLong());
    }

    @Test
    public void testSelectByActivityIdForActivity(){
        assertNull(approvalRecordService.selectByActivityIdForActivity(null));

        //init
        String activityId = "test";
        List<ActivityApprovalDetail> activityApprovalDetails = new ArrayList<ActivityApprovalDetail>();
        ActivityApprovalDetail activityApprovalDetail = new ActivityApprovalDetail();
        activityApprovalDetail.setId(1L);
        activityApprovalDetails.add(activityApprovalDetail);

        Mockito.when(activityApprovalDetailService.selectByActivityId(anyString())).thenReturn(activityApprovalDetails);
        Mockito.when(approvalRecordMapper.selectByMap(anyMap())).thenReturn(new ArrayList<ApprovalRecord>());
        assertNull(approvalRecordService.selectByActivityIdForActivity(activityId));
        Mockito.verify(activityApprovalDetailService).selectByActivityId(anyString());
        Mockito.verify(approvalRecordMapper).selectByMap(anyMap());
    }
    
    @Test
    public void testGetRecords(){
        List<ApprovalRecord> list = new ArrayList<ApprovalRecord>();     
        QueryObject queryObject = new QueryObject();       
        Mockito.when(approvalRecordMapper.getRecords(anyMap())).thenReturn(list);
        assertNotNull(approvalRecordService.getRecords(queryObject));
    }
    
    @Test
    public void testCountRecords(){       
        QueryObject queryObject = new QueryObject();     
        Long result = 10L;     
        Mockito.when(approvalRecordMapper.countRecords(anyMap())).thenReturn(result);
        assertNotNull(approvalRecordService.countRecords(queryObject));
    }
}
