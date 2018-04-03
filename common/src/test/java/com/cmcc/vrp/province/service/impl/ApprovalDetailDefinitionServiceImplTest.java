package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.ApprovalDetailDefinitionMapper;
import com.cmcc.vrp.province.model.ApprovalDetailDefinition;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.service.ApprovalDetailDefinitionService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
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
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;

/**
 * Created by qinqinyan on 2016/10/28.
 */
@RunWith(MockitoJUnitRunner.class)
public class ApprovalDetailDefinitionServiceImplTest {
    @InjectMocks
    ApprovalDetailDefinitionService approvalDetailDefinitionService =
            new ApprovalDetailDefinitionServiceImpl();
    @Mock
    ApprovalDetailDefinitionMapper approvalDetailDefinitionMapper;
    @Mock
    ApprovalProcessDefinitionService approvalProcessDefinitionService;

    @Test
    public void testUpdateByPrimaryKey() {
        assertFalse(approvalDetailDefinitionService.updateByPrimaryKey(null));

        ApprovalDetailDefinition record = new ApprovalDetailDefinition();
        assertFalse(approvalDetailDefinitionService.updateByPrimaryKey(record));

        record.setId(1L);
        assertFalse(approvalDetailDefinitionService.updateByPrimaryKey(record));

        record.setRoleId(1L);
        Mockito.when(approvalDetailDefinitionMapper.updateByPrimaryKey(any(ApprovalDetailDefinition.class))).thenReturn(1);
        assertTrue(approvalDetailDefinitionService.updateByPrimaryKey(record));
        Mockito.verify(approvalDetailDefinitionMapper).updateByPrimaryKey(any(ApprovalDetailDefinition.class));
    }

    @Test
    public void testGetByApprovalProcessId() {
        assertNull(approvalDetailDefinitionService.getByApprovalProcessId(null));

        Long id = 1L;
        Mockito.when(approvalDetailDefinitionMapper.getByApprovalProcessId(anyLong())).thenReturn(new ArrayList<ApprovalDetailDefinition>());
        assertNotNull(approvalDetailDefinitionService.getByApprovalProcessId(id));
        Mockito.verify(approvalDetailDefinitionMapper).getByApprovalProcessId(anyLong());
    }

    @Test
    public void testDeleteByApprovalProcess() {
        assertFalse(approvalDetailDefinitionService.deleteByApprovalProcess(null));

        Long id = 1L;
        Mockito.when(approvalDetailDefinitionMapper.deleteByApprovalProcess(anyLong())).thenReturn(1);
        assertTrue(approvalDetailDefinitionService.deleteByApprovalProcess(id));
        Mockito.verify(approvalDetailDefinitionMapper).deleteByApprovalProcess(anyLong());
    }

    @Test
    public void testBatchInsert() {
        assertTrue(approvalDetailDefinitionService.batchInsert(null));

        List<ApprovalDetailDefinition> records = new ArrayList<ApprovalDetailDefinition>();
        assertTrue(approvalDetailDefinitionService.batchInsert(records));

        ApprovalDetailDefinition approvalDetailDefinition = new ApprovalDetailDefinition();
        approvalDetailDefinition.setId(1L);
        records.add(approvalDetailDefinition);

        Mockito.when(approvalDetailDefinitionMapper.batchInsert(anyList())).thenReturn(1);
        assertTrue(approvalDetailDefinitionService.batchInsert(records));
        Mockito.verify(approvalDetailDefinitionMapper).batchInsert(anyList());
    }

    @Test
    public void testGetCurrentApprovalDetailByProccessId() {
        Long processId = 1L;
        Integer currentStatus = 0;
        List<ApprovalDetailDefinition> approvalDetailDefinitionList = new ArrayList<ApprovalDetailDefinition>();
        ApprovalDetailDefinition detailDefinition = new ApprovalDetailDefinition();
        detailDefinition.setPrecondition(0);
        approvalDetailDefinitionList.add(detailDefinition);

        assertNull(approvalDetailDefinitionService.getCurrentApprovalDetailByProccessId(null, currentStatus));

        Mockito.when(approvalDetailDefinitionMapper.getByApprovalProcessId(anyLong())).thenReturn(approvalDetailDefinitionList);
        assertNotNull(approvalDetailDefinitionService.getCurrentApprovalDetailByProccessId(processId, currentStatus));
        Mockito.verify(approvalDetailDefinitionMapper).getByApprovalProcessId(anyLong());
    }

    @Test
    public void testGetNextApprovalDetailByProccessId() {
        Long processId = 1L;
        Integer currentStatus = 0;
        List<ApprovalDetailDefinition> approvalDetailDefinitionList = new ArrayList<ApprovalDetailDefinition>();
        ApprovalDetailDefinition detailDefinition1 = new ApprovalDetailDefinition();
        detailDefinition1.setPrecondition(0);
        detailDefinition1.setApprovalCode(1);
        approvalDetailDefinitionList.add(detailDefinition1);

        ApprovalDetailDefinition detailDefinition2 = new ApprovalDetailDefinition();
        detailDefinition2.setPrecondition(1);
        detailDefinition2.setApprovalCode(2);
        approvalDetailDefinitionList.add(detailDefinition2);

        ApprovalProcessDefinition approvalProcessDefinition = new ApprovalProcessDefinition();
        approvalProcessDefinition.setTargetStatus(3);

        assertNull(approvalDetailDefinitionService.getNextApprovalDetailByProccessId(null, currentStatus));

        Mockito.when(approvalProcessDefinitionService.selectByPrimaryKey(anyLong())).thenReturn(approvalProcessDefinition);
        Mockito.when(approvalDetailDefinitionMapper.getByApprovalProcessId(anyLong())).thenReturn(approvalDetailDefinitionList);

        //存在下一级审批
        assertNotNull(approvalDetailDefinitionService.getNextApprovalDetailByProccessId(processId, currentStatus));

        //已经到最后一个级审批
        approvalProcessDefinition.setTargetStatus(1);
        assertNull(approvalDetailDefinitionService.getNextApprovalDetailByProccessId(processId, currentStatus));

        Mockito.verify(approvalProcessDefinitionService, Mockito.times(2)).selectByPrimaryKey(anyLong());
        Mockito.verify(approvalDetailDefinitionMapper, Mockito.times(2)).getByApprovalProcessId(anyLong());
    }

    @Test
    public void testGetNextApprovalDetailByProccessId1() {
        Long processId = 1L;
        Integer currentStatus = 0;

        ApprovalProcessDefinition approvalProcessDefinition = new ApprovalProcessDefinition();
        List<ApprovalDetailDefinition> approvalDetailDefinitionList = new ArrayList<ApprovalDetailDefinition>();

        Mockito.when(approvalProcessDefinitionService.selectByPrimaryKey(anyLong())).thenReturn(approvalProcessDefinition);
        Mockito.when(approvalDetailDefinitionMapper.getByApprovalProcessId(anyLong())).thenReturn(approvalDetailDefinitionList);

        assertNull(approvalDetailDefinitionService.getNextApprovalDetailByProccessId(processId, currentStatus));

        Mockito.verify(approvalProcessDefinitionService).selectByPrimaryKey(anyLong());
        Mockito.verify(approvalDetailDefinitionMapper).getByApprovalProcessId(anyLong());
    }

    @Test
    public void testGetLastApprovalDetailByProccessId() {
        Long processId = 1L;

        List<ApprovalDetailDefinition> approvalDetailDefinitionList = new ArrayList<ApprovalDetailDefinition>();

        assertNull(approvalDetailDefinitionService.getLastApprovalDetailByProccessId(null));

        Mockito.when(approvalDetailDefinitionMapper.getByApprovalProcessId(anyLong())).thenReturn(approvalDetailDefinitionList);
        assertNull(approvalDetailDefinitionService.getLastApprovalDetailByProccessId(processId));

        ApprovalDetailDefinition detailDefinition = new ApprovalDetailDefinition();
        detailDefinition.setApprovalCode(0);
        approvalDetailDefinitionList.add(detailDefinition);

        assertNotNull(approvalDetailDefinitionService.getLastApprovalDetailByProccessId(processId));

        Mockito.verify(approvalDetailDefinitionMapper, Mockito.times(2)).getByApprovalProcessId(anyLong());
    }
}