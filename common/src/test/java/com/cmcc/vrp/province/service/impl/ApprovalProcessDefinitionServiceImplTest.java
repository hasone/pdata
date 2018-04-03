package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.ApprovalProcessDefinitionMapper;
import com.cmcc.vrp.province.model.ApprovalDetailDefinition;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.service.ApprovalDetailDefinitionService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;

/**
 * Created by qinqinyan on 2016/11/7.
 */
@RunWith(MockitoJUnitRunner.class)
public class ApprovalProcessDefinitionServiceImplTest {
    @InjectMocks
    ApprovalProcessDefinitionService approvalProcessDefinitionService
            = new ApprovalProcessDefinitionServiceImpl();
    @Mock
    ApprovalProcessDefinitionMapper approvalProcessDefinitionMapper;
    @Mock
    ApprovalDetailDefinitionService approvalDetailDefinitionService;

    @Test
    public void testDeleteByPrimaryKey(){
        assertFalse(approvalProcessDefinitionService.deleteByPrimaryKey(null));
        Long id = 1L;
        Mockito.when(approvalProcessDefinitionMapper.deleteByPrimaryKey(anyLong())).thenReturn(1);
        assertTrue(approvalProcessDefinitionService.deleteByPrimaryKey(id));
        Mockito.verify(approvalProcessDefinitionMapper).deleteByPrimaryKey(anyLong());
    }

    @Test
    public void testInsert(){
        assertFalse(approvalProcessDefinitionService.insert(null));
        Mockito.when(approvalProcessDefinitionMapper.insert(any(ApprovalProcessDefinition.class))).thenReturn(1);
        assertTrue(approvalProcessDefinitionService.insert(new ApprovalProcessDefinition()));
        Mockito.verify(approvalProcessDefinitionMapper).insert(any(ApprovalProcessDefinition.class));
    }

    @Test
    public void testSelectByPrimaryKey(){
        assertNull(approvalProcessDefinitionService.selectByPrimaryKey(null));
        Long id = 1L;
        Mockito.when(approvalProcessDefinitionMapper.selectByPrimaryKey(anyLong())).thenReturn(new ApprovalProcessDefinition());
        assertNotNull(approvalProcessDefinitionService.selectByPrimaryKey(id));
        Mockito.verify(approvalProcessDefinitionMapper).selectByPrimaryKey(anyLong());
    }

    @Test
    public void testUpdateByPrimaryKey(){
        ApprovalProcessDefinition record = new ApprovalProcessDefinition();
        assertFalse(approvalProcessDefinitionService.updateByPrimaryKey(record));
        record.setOriginRoleId(1L);
        Mockito.when(approvalProcessDefinitionMapper.updateByPrimaryKey(any(ApprovalProcessDefinition.class))).thenReturn(1);
        assertTrue(approvalProcessDefinitionService.updateByPrimaryKey(record));
        Mockito.verify(approvalProcessDefinitionMapper).updateByPrimaryKey(any(ApprovalProcessDefinition.class));
    }

    @Test
    public void testCountApprovalProcess(){
        QueryObject queryObject = new QueryObject();
        Mockito.when(approvalProcessDefinitionMapper.countApprovalProcess(anyMap())).thenReturn(1);
        assertSame(1,approvalProcessDefinitionService.countApprovalProcess(queryObject));
        Mockito.verify(approvalProcessDefinitionMapper).countApprovalProcess(anyMap());
    }

    @Test
    public void testQueryApprovalProcessList(){
        QueryObject queryObject = new QueryObject();
        Mockito.when(approvalProcessDefinitionMapper.queryApprovalProcessList(anyMap())).thenReturn(new ArrayList<ApprovalProcessDefinition>());
        assertNotNull(approvalProcessDefinitionService.queryApprovalProcessList(queryObject));
        Mockito.verify(approvalProcessDefinitionMapper).queryApprovalProcessList(anyMap());
    }

    @Test
    public void testGetApprovalProcessById(){
        assertNull(approvalProcessDefinitionService.getApprovalProcessById(null));
        Long id = 1L;
        Mockito.when(approvalProcessDefinitionMapper.getApprovalProcessById(anyLong())).thenReturn(new ApprovalProcessDefinition());
        assertNotNull(approvalProcessDefinitionService.getApprovalProcessById(id));
        Mockito.verify(approvalProcessDefinitionMapper).getApprovalProcessById(anyLong());
    }

    @Test
    public void testDeleteApprovalProcess(){
        assertFalse(approvalProcessDefinitionService.deleteApprovalProcess(null));

        Long id = 1L;
        Mockito.when(approvalProcessDefinitionMapper.deleteByPrimaryKey(id)).thenReturn(0);
        assertFalse(approvalProcessDefinitionService.deleteApprovalProcess(id));
        Mockito.verify(approvalProcessDefinitionMapper).deleteByPrimaryKey(anyLong());
    }

    /**
     * 正常流程
     * */
    @Test
    public void testDeleteApprovalProcess1(){
        Long id = 1L;
        Mockito.when(approvalProcessDefinitionMapper.deleteByPrimaryKey(id)).thenReturn(1);
        Mockito.when(approvalDetailDefinitionService.deleteByApprovalProcess(id)).thenReturn(true);
        assertTrue(approvalProcessDefinitionService.deleteApprovalProcess(id));
        Mockito.verify(approvalProcessDefinitionMapper).deleteByPrimaryKey(anyLong());
        Mockito.verify(approvalDetailDefinitionService).deleteByApprovalProcess(anyLong());
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteApprovalProcess2(){
        Long id = 1L;
        Mockito.when(approvalProcessDefinitionMapper.deleteByPrimaryKey(id)).thenReturn(1);
        Mockito.when(approvalDetailDefinitionService.deleteByApprovalProcess(id)).thenReturn(false);
        assertFalse(approvalProcessDefinitionService.deleteApprovalProcess(id));
    }

    /**
     * 一级审核
     * */
    @Test
    public void testInsertApprovalProcess(){
        ApprovalProcessDefinition approvalProcess =
                new ApprovalProcessDefinition();
        approvalProcess.setId(1L);
        approvalProcess.setOriginRoleId(1L);
        approvalProcess.setFirstRoleId(1L);
        assertFalse(approvalProcessDefinitionService.insertApprovalProcess(approvalProcess));

        Mockito.when(approvalProcessDefinitionMapper.insert(any(ApprovalProcessDefinition.class))).thenReturn(1);
        approvalProcess.setStage(1);
        Mockito.when(approvalDetailDefinitionService.batchInsert(anyList())).thenReturn(true);
        assertTrue(approvalProcessDefinitionService.insertApprovalProcess(approvalProcess));
        Mockito.verify(approvalProcessDefinitionMapper).insert(any(ApprovalProcessDefinition.class));
        Mockito.verify(approvalDetailDefinitionService).batchInsert(anyList());
    }

    /**
     * 非正常流程
     * */
    @Test
    public void testInsertApprovalProcess1(){
        ApprovalProcessDefinition approvalProcess =
                new ApprovalProcessDefinition();
        approvalProcess.setId(1L);
        approvalProcess.setOriginRoleId(1L);
        assertFalse(approvalProcessDefinitionService.insertApprovalProcess(approvalProcess));

        Mockito.when(approvalProcessDefinitionMapper.insert(any(ApprovalProcessDefinition.class))).thenReturn(0);
        approvalProcess.setStage(1);
        assertFalse(approvalProcessDefinitionService.insertApprovalProcess(approvalProcess));
        Mockito.verify(approvalProcessDefinitionMapper).insert(any(ApprovalProcessDefinition.class));
    }

    @Test
    public void testInsertApprovalProcess2(){
        ApprovalProcessDefinition approvalProcess =
                new ApprovalProcessDefinition();
        approvalProcess.setId(1L);
        approvalProcess.setOriginRoleId(1L);
        approvalProcess.setFirstRoleId(1L);
        approvalProcess.setSecondRoleId(2L);
        assertFalse(approvalProcessDefinitionService.insertApprovalProcess(approvalProcess));

        Mockito.when(approvalProcessDefinitionMapper.insert(any(ApprovalProcessDefinition.class))).thenReturn(1);
        approvalProcess.setStage(2);
        Mockito.when(approvalDetailDefinitionService.batchInsert(anyList())).thenReturn(true);
        assertTrue(approvalProcessDefinitionService.insertApprovalProcess(approvalProcess));
        Mockito.verify(approvalProcessDefinitionMapper).insert(any(ApprovalProcessDefinition.class));
        Mockito.verify(approvalDetailDefinitionService).batchInsert(anyList());
    }

    @Test
    public void testInsertApprovalProcess3(){
        ApprovalProcessDefinition approvalProcess =
                new ApprovalProcessDefinition();
        approvalProcess.setId(1L);
        approvalProcess.setOriginRoleId(1L);
        approvalProcess.setFirstRoleId(1L);
        approvalProcess.setSecondRoleId(2L);
        approvalProcess.setThirdRoleId(3L);
        assertFalse(approvalProcessDefinitionService.insertApprovalProcess(approvalProcess));

        Mockito.when(approvalProcessDefinitionMapper.insert(any(ApprovalProcessDefinition.class))).thenReturn(1);
        approvalProcess.setStage(3);
        Mockito.when(approvalDetailDefinitionService.batchInsert(anyList())).thenReturn(true);
        assertTrue(approvalProcessDefinitionService.insertApprovalProcess(approvalProcess));
        Mockito.verify(approvalProcessDefinitionMapper).insert(any(ApprovalProcessDefinition.class));
        Mockito.verify(approvalDetailDefinitionService).batchInsert(anyList());
    }

    @Test
    public void testInsertApprovalProcess4(){
        ApprovalProcessDefinition approvalProcess =
                new ApprovalProcessDefinition();
        approvalProcess.setId(1L);
        approvalProcess.setOriginRoleId(1L);
        approvalProcess.setFirstRoleId(1L);
        approvalProcess.setSecondRoleId(2L);
        approvalProcess.setThirdRoleId(3L);
        approvalProcess.setForthRoleId(4L);
        assertFalse(approvalProcessDefinitionService.insertApprovalProcess(approvalProcess));

        Mockito.when(approvalProcessDefinitionMapper.insert(any(ApprovalProcessDefinition.class))).thenReturn(1);
        approvalProcess.setStage(4);
        Mockito.when(approvalDetailDefinitionService.batchInsert(anyList())).thenReturn(true);
        assertTrue(approvalProcessDefinitionService.insertApprovalProcess(approvalProcess));
        Mockito.verify(approvalProcessDefinitionMapper).insert(any(ApprovalProcessDefinition.class));
        Mockito.verify(approvalDetailDefinitionService).batchInsert(anyList());
    }

    @Test
    public void testInsertApprovalProcess5(){
        ApprovalProcessDefinition approvalProcess =
                new ApprovalProcessDefinition();
        approvalProcess.setId(1L);
        approvalProcess.setOriginRoleId(1L);
        approvalProcess.setFirstRoleId(1L);
        approvalProcess.setSecondRoleId(2L);
        approvalProcess.setThirdRoleId(3L);
        approvalProcess.setForthRoleId(4L);
        approvalProcess.setFifthRoleId(5L);
        assertFalse(approvalProcessDefinitionService.insertApprovalProcess(approvalProcess));

        Mockito.when(approvalProcessDefinitionMapper.insert(any(ApprovalProcessDefinition.class))).thenReturn(1);
        approvalProcess.setStage(5);
        Mockito.when(approvalDetailDefinitionService.batchInsert(anyList())).thenReturn(true);
        assertTrue(approvalProcessDefinitionService.insertApprovalProcess(approvalProcess));
        Mockito.verify(approvalProcessDefinitionMapper).insert(any(ApprovalProcessDefinition.class));
        Mockito.verify(approvalDetailDefinitionService).batchInsert(anyList());
    }

    @Test
    public void testInsertApprovalProcess0(){
        ApprovalProcessDefinition approvalProcess =
                new ApprovalProcessDefinition();
        approvalProcess.setId(1L);
        approvalProcess.setOriginRoleId(1L);
        assertFalse(approvalProcessDefinitionService.insertApprovalProcess(approvalProcess));

        Mockito.when(approvalProcessDefinitionMapper.insert(any(ApprovalProcessDefinition.class))).thenReturn(1);
        approvalProcess.setStage(0);
        assertTrue(approvalProcessDefinitionService.insertApprovalProcess(approvalProcess));
        Mockito.verify(approvalProcessDefinitionMapper).insert(any(ApprovalProcessDefinition.class));
    }

    @Test
    public void testInsertProcessAndDetails(){
        assertFalse(approvalProcessDefinitionService.insertProcessAndDetails(new ApprovalProcessDefinition(),
                new ArrayList<ApprovalDetailDefinition>()));

        ApprovalProcessDefinition approvalProcessDefinition = new ApprovalProcessDefinition();
        approvalProcessDefinition.setId(1L);

        List<ApprovalDetailDefinition> approvalDetails = new ArrayList<ApprovalDetailDefinition>();
        ApprovalDetailDefinition record = new ApprovalDetailDefinition();
        record.setId(1L);
        approvalDetails.add(record);

        Mockito.when(approvalProcessDefinitionMapper.insert(any(ApprovalProcessDefinition.class))).thenReturn(1);
        Mockito.when(approvalDetailDefinitionService.batchInsert(anyList())).thenReturn(true);
        assertTrue(approvalProcessDefinitionService.insertProcessAndDetails(approvalProcessDefinition,
                approvalDetails));
        Mockito.verify(approvalProcessDefinitionMapper, Mockito.times(2)).insert(any(ApprovalProcessDefinition.class));
        Mockito.verify(approvalDetailDefinitionService).batchInsert(anyList());
    }

    @Test(expected = RuntimeException.class)
    public void testInsertProcessAndDetails1(){
        ApprovalProcessDefinition approvalProcessDefinition = new ApprovalProcessDefinition();
        approvalProcessDefinition.setId(1L);

        List<ApprovalDetailDefinition> approvalDetails = new ArrayList<ApprovalDetailDefinition>();
        ApprovalDetailDefinition record = new ApprovalDetailDefinition();
        record.setId(1L);
        approvalDetails.add(record);

        Mockito.when(approvalProcessDefinitionMapper.insert(any(ApprovalProcessDefinition.class))).thenReturn(1);
        Mockito.when(approvalDetailDefinitionService.batchInsert(anyList())).thenReturn(false);
        assertTrue(approvalProcessDefinitionService.insertProcessAndDetails(approvalProcessDefinition,
                approvalDetails));
        Mockito.verify(approvalProcessDefinitionMapper).insert(any(ApprovalProcessDefinition.class));
        Mockito.verify(approvalDetailDefinitionService).batchInsert(anyList());
    }

    @Test
    public void testUpdateApprovalProcess(){
        assertFalse(approvalProcessDefinitionService.updateApprovalProcess(new ApprovalProcessDefinition()));

        ApprovalProcessDefinition approvalProcess = new ApprovalProcessDefinition();
        approvalProcess.setId(1L);
        approvalProcess.setOriginRoleId(1L);
        approvalProcess.setStage(1);
        approvalProcess.setFirstRoleId(1L);

        List<ApprovalDetailDefinition> approvalDetails = new ArrayList<ApprovalDetailDefinition>();
        ApprovalDetailDefinition record = new ApprovalDetailDefinition();
        record.setId(1L);
        approvalDetails.add(record);

        Mockito.when(approvalProcessDefinitionMapper.updateByPrimaryKey(any(ApprovalProcessDefinition.class)))
                .thenReturn(1);
        Mockito.when(approvalDetailDefinitionService.getByApprovalProcessId(anyLong())).thenReturn(approvalDetails);
        Mockito.when(approvalDetailDefinitionService.updateByPrimaryKey(any(ApprovalDetailDefinition.class))).thenReturn(true);

        assertTrue(approvalProcessDefinitionService.updateApprovalProcess(approvalProcess));

        Mockito.verify(approvalProcessDefinitionMapper).updateByPrimaryKey(any(ApprovalProcessDefinition.class));
        Mockito.verify(approvalDetailDefinitionService).getByApprovalProcessId(anyLong());
        Mockito.verify(approvalDetailDefinitionService).updateByPrimaryKey(any(ApprovalDetailDefinition.class));
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateApprovalProcess1(){
        assertFalse(approvalProcessDefinitionService.updateApprovalProcess(new ApprovalProcessDefinition()));

        ApprovalProcessDefinition approvalProcess = new ApprovalProcessDefinition();
        approvalProcess.setId(1L);
        approvalProcess.setOriginRoleId(1L);
        approvalProcess.setStage(1);
        approvalProcess.setFirstRoleId(1L);

        List<ApprovalDetailDefinition> approvalDetails = new ArrayList<ApprovalDetailDefinition>();
        ApprovalDetailDefinition record = new ApprovalDetailDefinition();
        record.setId(1L);
        approvalDetails.add(record);

        Mockito.when(approvalProcessDefinitionMapper.updateByPrimaryKey(any(ApprovalProcessDefinition.class)))
                .thenReturn(1);
        Mockito.when(approvalDetailDefinitionService.getByApprovalProcessId(anyLong())).thenReturn(approvalDetails);
        Mockito.when(approvalDetailDefinitionService.updateByPrimaryKey(any(ApprovalDetailDefinition.class))).thenReturn(false);

        assertTrue(approvalProcessDefinitionService.updateApprovalProcess(approvalProcess));

        Mockito.verify(approvalProcessDefinitionMapper).updateByPrimaryKey(any(ApprovalProcessDefinition.class));
        Mockito.verify(approvalDetailDefinitionService).getByApprovalProcessId(anyLong());
        Mockito.verify(approvalDetailDefinitionService).updateByPrimaryKey(any(ApprovalDetailDefinition.class));
    }

    @Test
    public void testUpdateApprovalProcess2(){
        ApprovalProcessDefinition approvalProcess = new ApprovalProcessDefinition();
        approvalProcess.setId(1L);
        approvalProcess.setOriginRoleId(1L);
        approvalProcess.setStage(2);
        approvalProcess.setFirstRoleId(1L);
        approvalProcess.setSecondRoleId(2L);

        List<ApprovalDetailDefinition> approvalDetails = new ArrayList<ApprovalDetailDefinition>();

        ApprovalDetailDefinition record1 = new ApprovalDetailDefinition();
        record1.setId(1L);
        ApprovalDetailDefinition record2 = new ApprovalDetailDefinition();
        record2.setId(1L);

        approvalDetails.add(record1);
        approvalDetails.add(record2);

        Mockito.when(approvalProcessDefinitionMapper.updateByPrimaryKey(any(ApprovalProcessDefinition.class)))
                .thenReturn(1);
        Mockito.when(approvalDetailDefinitionService.getByApprovalProcessId(anyLong())).thenReturn(approvalDetails);
        Mockito.when(approvalDetailDefinitionService.updateByPrimaryKey(any(ApprovalDetailDefinition.class))).thenReturn(true);

        assertTrue(approvalProcessDefinitionService.updateApprovalProcess(approvalProcess));

        Mockito.verify(approvalProcessDefinitionMapper).updateByPrimaryKey(any(ApprovalProcessDefinition.class));
        Mockito.verify(approvalDetailDefinitionService).getByApprovalProcessId(anyLong());
        Mockito.verify(approvalDetailDefinitionService, Mockito.times(2)).updateByPrimaryKey(any(ApprovalDetailDefinition.class));
    }

    @Test
    public void testUpdateApprovalProcess3(){
        ApprovalProcessDefinition approvalProcess = new ApprovalProcessDefinition();
        approvalProcess.setId(1L);
        approvalProcess.setOriginRoleId(1L);
        approvalProcess.setStage(3);
        approvalProcess.setFirstRoleId(1L);
        approvalProcess.setSecondRoleId(2L);
        approvalProcess.setThirdRoleId(3L);

        List<ApprovalDetailDefinition> approvalDetails = new ArrayList<ApprovalDetailDefinition>();

        ApprovalDetailDefinition record1 = new ApprovalDetailDefinition();
        record1.setId(1L);
        ApprovalDetailDefinition record2 = new ApprovalDetailDefinition();
        record2.setId(1L);
        ApprovalDetailDefinition record3 = new ApprovalDetailDefinition();
        record3.setId(1L);

        approvalDetails.add(record1);
        approvalDetails.add(record2);
        approvalDetails.add(record3);

        Mockito.when(approvalProcessDefinitionMapper.updateByPrimaryKey(any(ApprovalProcessDefinition.class)))
                .thenReturn(1);
        Mockito.when(approvalDetailDefinitionService.getByApprovalProcessId(anyLong())).thenReturn(approvalDetails);
        Mockito.when(approvalDetailDefinitionService.updateByPrimaryKey(any(ApprovalDetailDefinition.class))).thenReturn(true);

        assertTrue(approvalProcessDefinitionService.updateApprovalProcess(approvalProcess));

        Mockito.verify(approvalProcessDefinitionMapper).updateByPrimaryKey(any(ApprovalProcessDefinition.class));
        Mockito.verify(approvalDetailDefinitionService).getByApprovalProcessId(anyLong());
        Mockito.verify(approvalDetailDefinitionService, Mockito.times(3)).updateByPrimaryKey(any(ApprovalDetailDefinition.class));
    }

    @Test
    public void testUpdateApprovalProcess4(){
        ApprovalProcessDefinition approvalProcess = new ApprovalProcessDefinition();
        approvalProcess.setId(1L);
        approvalProcess.setOriginRoleId(1L);
        approvalProcess.setStage(4);
        approvalProcess.setFirstRoleId(1L);
        approvalProcess.setSecondRoleId(2L);
        approvalProcess.setThirdRoleId(3L);
        approvalProcess.setForthRoleId(4L);

        List<ApprovalDetailDefinition> approvalDetails = new ArrayList<ApprovalDetailDefinition>();

        ApprovalDetailDefinition record1 = new ApprovalDetailDefinition();
        record1.setId(1L);
        ApprovalDetailDefinition record2 = new ApprovalDetailDefinition();
        record2.setId(1L);
        ApprovalDetailDefinition record3 = new ApprovalDetailDefinition();
        record3.setId(1L);
        ApprovalDetailDefinition record4 = new ApprovalDetailDefinition();
        record4.setId(1L);

        approvalDetails.add(record1);
        approvalDetails.add(record2);
        approvalDetails.add(record3);
        approvalDetails.add(record4);

        Mockito.when(approvalProcessDefinitionMapper.updateByPrimaryKey(any(ApprovalProcessDefinition.class)))
                .thenReturn(1);
        Mockito.when(approvalDetailDefinitionService.getByApprovalProcessId(anyLong())).thenReturn(approvalDetails);
        Mockito.when(approvalDetailDefinitionService.updateByPrimaryKey(any(ApprovalDetailDefinition.class))).thenReturn(true);

        assertTrue(approvalProcessDefinitionService.updateApprovalProcess(approvalProcess));

        Mockito.verify(approvalProcessDefinitionMapper).updateByPrimaryKey(any(ApprovalProcessDefinition.class));
        Mockito.verify(approvalDetailDefinitionService).getByApprovalProcessId(anyLong());
        Mockito.verify(approvalDetailDefinitionService, Mockito.times(4)).updateByPrimaryKey(any(ApprovalDetailDefinition.class));
    }

    @Test
    public void testUpdateApprovalProcess5(){
        ApprovalProcessDefinition approvalProcess = new ApprovalProcessDefinition();
        approvalProcess.setId(1L);
        approvalProcess.setOriginRoleId(1L);
        approvalProcess.setStage(5);
        approvalProcess.setFirstRoleId(1L);
        approvalProcess.setSecondRoleId(2L);
        approvalProcess.setThirdRoleId(3L);
        approvalProcess.setForthRoleId(4L);
        approvalProcess.setFifthRoleId(5L);

        List<ApprovalDetailDefinition> approvalDetails = new ArrayList<ApprovalDetailDefinition>();

        ApprovalDetailDefinition record1 = new ApprovalDetailDefinition();
        record1.setId(1L);
        ApprovalDetailDefinition record2 = new ApprovalDetailDefinition();
        record2.setId(1L);
        ApprovalDetailDefinition record3 = new ApprovalDetailDefinition();
        record3.setId(1L);
        ApprovalDetailDefinition record4 = new ApprovalDetailDefinition();
        record4.setId(1L);
        ApprovalDetailDefinition record5 = new ApprovalDetailDefinition();
        record5.setId(1L);

        approvalDetails.add(record1);
        approvalDetails.add(record2);
        approvalDetails.add(record3);
        approvalDetails.add(record4);
        approvalDetails.add(record5);

        Mockito.when(approvalProcessDefinitionMapper.updateByPrimaryKey(any(ApprovalProcessDefinition.class)))
                .thenReturn(1);
        Mockito.when(approvalDetailDefinitionService.getByApprovalProcessId(anyLong())).thenReturn(approvalDetails);
        Mockito.when(approvalDetailDefinitionService.updateByPrimaryKey(any(ApprovalDetailDefinition.class))).thenReturn(true);

        assertTrue(approvalProcessDefinitionService.updateApprovalProcess(approvalProcess));

        Mockito.verify(approvalProcessDefinitionMapper).updateByPrimaryKey(any(ApprovalProcessDefinition.class));
        Mockito.verify(approvalDetailDefinitionService).getByApprovalProcessId(anyLong());
        Mockito.verify(approvalDetailDefinitionService, Mockito.times(5)).updateByPrimaryKey(any(ApprovalDetailDefinition.class));
    }

    @Test
    public void testSelectByType(){
        assertNull(approvalProcessDefinitionService.selectByType(null));
        Integer type = 1;
        Mockito.when(approvalProcessDefinitionMapper.selectByType(anyInt())).thenReturn(new ApprovalProcessDefinition());
        assertNotNull(approvalProcessDefinitionService.selectByType(type));
        Mockito.verify(approvalProcessDefinitionMapper).selectByType(anyInt());
    }

    @Test
    public void testSelectApprovalProcessesByType(){
        assertNull(approvalProcessDefinitionService.selectApprovalProcessesByType(null));
        Integer type = 1;
        Mockito.when(approvalProcessDefinitionMapper.selectApprovalProcessesByType(type))
                .thenReturn(new ArrayList<ApprovalProcessDefinition>());
        assertNotNull(approvalProcessDefinitionService.selectApprovalProcessesByType(type));
        Mockito.verify(approvalProcessDefinitionMapper).selectApprovalProcessesByType(anyInt());
    }

    @Test
    public void testHasAuthToSubmitApproval(){
        Long roleId = 1L;
        Integer type = 1;
        assertFalse(approvalProcessDefinitionService.hasAuthToSubmitApproval(roleId, null));

        ApprovalProcessDefinition approvalProcess = new ApprovalProcessDefinition();
        approvalProcess.setOriginRoleId(1L);
        Mockito.when(approvalProcessDefinitionMapper.selectByType(anyInt())).thenReturn(approvalProcess);
        assertTrue(approvalProcessDefinitionService.hasAuthToSubmitApproval(roleId, type));
        Mockito.verify(approvalProcessDefinitionMapper).selectByType(anyInt());
    }

    @Test
    public void testHasAuthToSubmitApproval1(){
        Long roleId = 1L;
        Integer type = 1;

        ApprovalProcessDefinition approvalProcess = new ApprovalProcessDefinition();
        approvalProcess.setOriginRoleId(2L);
        Mockito.when(approvalProcessDefinitionMapper.selectByType(anyInt())).thenReturn(approvalProcess);
        assertFalse(approvalProcessDefinitionService.hasAuthToSubmitApproval(roleId, type));
        Mockito.verify(approvalProcessDefinitionMapper).selectByType(anyInt());
    }

    @Test
    public void testWheatherNeedApproval(){
        assertFalse(approvalProcessDefinitionService.wheatherNeedApproval(null));

        Integer type = 1;
        ApprovalProcessDefinition approvalProcess = new ApprovalProcessDefinition();
        approvalProcess.setStage(1);
        Mockito.when(approvalProcessDefinitionMapper.selectByType(anyInt())).thenReturn(approvalProcess);
        assertTrue(approvalProcessDefinitionService.wheatherNeedApproval(type));
        Mockito.verify(approvalProcessDefinitionMapper).selectByType(anyInt());
    }











}
