package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.province.dao.HistoryEnterprisesMapper;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseFile;
import com.cmcc.vrp.province.model.HistoryEnterpriseFile;
import com.cmcc.vrp.province.model.HistoryEnterprises;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.EnterpriseFileService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.FileStoreService;
import com.cmcc.vrp.province.service.GiveMoneyEnterService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.HistoryEnterpriseFileService;
import com.cmcc.vrp.province.service.HistoryEnterprisesService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * Created by qinqinyan on 2016/10/24.
 *
 * @Description 历史企业服务测试类
 */
@RunWith(MockitoJUnitRunner.class)
public class HistoryEnterprisesServiceImplTest {

    @InjectMocks
    HistoryEnterprisesService historyEnterprisesService = new HistoryEnterprisesServiceImpl();
    @Mock
    HistoryEnterprisesMapper historyEnterprisesMapper;
    @Mock
    ApprovalRequestService approvalRequestService;

    @Mock
    ApprovalProcessDefinitionService approvalProcessDefinitionService;

    @Mock
    EnterpriseFileService enterpriseFileService;
    @Mock
    FileStoreService fileStoreService;
    @Mock
    HistoryEnterpriseFileService historyEnterpriseFileService;
    @Mock
    EnterprisesService enterprisesService;
    @Mock
    GlobalConfigService globalConfigService;
    @Mock
    GiveMoneyEnterService giveMoneyEnterService;

    /**
     * 插入
     */
    @Test
    public void testInsert() {
        //invalid
        assertFalse(historyEnterprisesService.insert(null));

        HistoryEnterprises invalidRecord = new HistoryEnterprises();
        assertFalse(historyEnterprisesService.insert(invalidRecord));

        //valid
        HistoryEnterprises validRecord = new HistoryEnterprises();
        validRecord.setEntId(1L);
        Mockito.when(historyEnterprisesMapper.insert(validRecord)).thenReturn(0).thenReturn(1);
        assertFalse(historyEnterprisesService.insert(validRecord));
        assertTrue(historyEnterprisesService.insert(validRecord));
        Mockito.verify(historyEnterprisesMapper, Mockito.times(2)).insert(validRecord);
    }

    @Test
    public void testInsertSelective() {
        //invalid
        assertFalse(historyEnterprisesService.insertSelective(null));

        //valid
        HistoryEnterprises historyEnterprises = new HistoryEnterprises();
        historyEnterprises.setEntId(1L);
        Mockito.when(historyEnterprisesMapper.insertSelective(Mockito.any(HistoryEnterprises.class))).thenReturn(0).thenReturn(1);
        assertFalse(historyEnterprisesService.insertSelective(historyEnterprises));
        assertTrue(historyEnterprisesService.insertSelective(historyEnterprises));
        Mockito.verify(historyEnterprisesMapper, Mockito.times(2)).insertSelective(Mockito.any(HistoryEnterprises.class));
    }

    /**
     * 根据requestId查找
     */
    @Test
    public void testSelectByRequestId() throws Exception {
        //invalid
        assertNull(historyEnterprisesService.selectByRequestId(null));

        //valid
        Long requestId = 1L;
        Mockito.when(historyEnterprisesMapper.selectByRequestId(requestId)).thenReturn(new HistoryEnterprises());
        assertNotNull(historyEnterprisesService.selectByRequestId(requestId));
        Mockito.verify(historyEnterprisesMapper).selectByRequestId(requestId);
    }

    /**
     * 根据主键更新状态
     */
    @Test
    public void testUpdateStatusByPrimaryKey() {
        //invalid
        assertFalse(historyEnterprisesService.updateStatusByPrimaryKey(null));

        //valid
        HistoryEnterprises record = new HistoryEnterprises();
        record.setEntId(1L);
        Mockito.when(historyEnterprisesMapper.updateStatusByRequestId(Mockito.any(HistoryEnterprises.class))).thenReturn(0).thenReturn(1);
        assertFalse(historyEnterprisesService.updateStatusByPrimaryKey(record));
        assertTrue(historyEnterprisesService.updateStatusByPrimaryKey(record));
        Mockito.verify(historyEnterprisesMapper, Mockito.times(2)).updateStatusByRequestId(Mockito.any(HistoryEnterprises.class));
    }

    /**
     * 根据请求id查找历史企业记录
     */
    @Test
    public void testSelectHistoryEnterpriseByRequestId() throws Exception {
        //invalid
        assertNull(historyEnterprisesService.selectHistoryEnterpriseByRequestId(null));

        //valid
        Long requestId = 1L;
        Mockito.when(historyEnterprisesMapper.selectHistoryEnterpriseByRequestId(requestId)).thenReturn(new HistoryEnterprises());
        assertNotNull(historyEnterprisesService.selectHistoryEnterpriseByRequestId(requestId));
        Mockito.verify(historyEnterprisesMapper).selectHistoryEnterpriseByRequestId(requestId);
    }

    @Test
    public void testHasApprovalRecord() {
        Long entId = 1L;
        List<HistoryEnterprises> list = new ArrayList();
        HistoryEnterprises he = new HistoryEnterprises();

        when(historyEnterprisesMapper.selectByEntIdAndStatus(Mockito.anyLong(), Mockito.anyInt())).thenReturn(null).thenReturn(list);
        assertFalse(historyEnterprisesService.hasApprovalRecord(entId));
        assertFalse(historyEnterprisesService.hasApprovalRecord(entId));
        list.add(he);
        assertTrue(historyEnterprisesService.hasApprovalRecord(entId));
        Mockito.verify(historyEnterprisesMapper, Mockito.times(3)).selectByEntIdAndStatus(Mockito.anyLong(), Mockito.anyInt());

    }

    @Test
    public void testSelectByEntIdAndStatus() {
        Long entId = 1L;
        Integer status = 1;
        List<HistoryEnterprises> list = new ArrayList();

        when(historyEnterprisesMapper.selectByEntIdAndStatus(entId, status)).thenReturn(list);
        assertNull(historyEnterprisesService.selectByEntIdAndStatus(null, null));
        assertNull(historyEnterprisesService.selectByEntIdAndStatus(entId, null));
        assertSame(list, historyEnterprisesService.selectByEntIdAndStatus(entId, status));

        Mockito.verify(historyEnterprisesMapper, Mockito.times(1)).selectByEntIdAndStatus(entId, status);
    }

    @Test
    public void testSaveEdit() throws IOException {
        //invalid
        assertFalse(historyEnterprisesService.saveEdit(null, null, null));
        assertFalse(historyEnterprisesService.saveEdit(new HistoryEnterprises(), null, null));

        HistoryEnterprises historyEnterprises = new HistoryEnterprises();
        Long adminId = 1L;
        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
        ApprovalProcessDefinition approvalProcessDefinition = new ApprovalProcessDefinition();
        historyEnterprises.setEntId(1L);
        approvalProcessDefinition.setStage(0);
        when(approvalRequestService.submitEnterpriseChange(historyEnterprises.getEntId(), adminId)).thenReturn(null).thenReturn(1L);
        when(approvalProcessDefinitionService.selectByType(ApprovalType.Ent_Information_Change_Approval.getCode())).thenReturn(approvalProcessDefinition);
        when(historyEnterprisesMapper.insertSelective(historyEnterprises)).thenReturn(0);
        assertFalse(historyEnterprisesService.saveEdit(historyEnterprises, adminId, request));
        assertFalse(historyEnterprisesService.saveEdit(historyEnterprises, adminId, request));
        when(historyEnterprisesMapper.insertSelective(historyEnterprises)).thenReturn(1);
        EnterpriseFile efOrigin = new EnterpriseFile();
        efOrigin.setLicenceKey("aa");
        efOrigin.setAuthorizationKey("bb");
        efOrigin.setIdentificationKey("cc");
        efOrigin.setIdentificationBackKey("dd");
        efOrigin.setCustomerfileKey("ee");
        efOrigin.setContractKey("ff");
        efOrigin.setImageKey("gg");
        when(enterpriseFileService.selectByEntId(historyEnterprises.getEntId())).thenReturn(efOrigin);
        assertFalse(historyEnterprisesService.saveEdit(historyEnterprises, adminId, request));

        File tempFile = File.createTempFile("back", "xml");
        MockMultipartFile multipartFile = new MockMultipartFile("aaa", new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile0 = new MockMultipartFile("bbb", "bbb", "", new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile1 = new MockMultipartFile("licenceImage", "licenceImage", "", new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile2 = new MockMultipartFile("authorization", "authorization", "", new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile3 = new MockMultipartFile("identification", "identification", "", new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile4 = new MockMultipartFile("identificationBack", "identificationBack", "", new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile5 = new MockMultipartFile("customerFile", "customerFile", "", new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile6 = new MockMultipartFile("contract", "contract", "", new BufferedInputStream(new FileInputStream(tempFile)));
        MockMultipartFile multipartFile7 = new MockMultipartFile("image", "image", "", new BufferedInputStream(new FileInputStream(tempFile)));
        request.addFile(multipartFile);
        request.addFile(multipartFile0);
        request.addFile(multipartFile1);
        request.addFile(multipartFile2);
        request.addFile(multipartFile3);
        request.addFile(multipartFile4);
        request.addFile(multipartFile5);
        request.addFile(multipartFile6);
        request.addFile(multipartFile7);

//	when(enterpriseFileService.selectByEntId(enterprise.getId())).thenReturn(efOrigin);
        doReturn(true).when(fileStoreService).save(Mockito.anyString(), Mockito.any(File.class));
//	when(enterpriseFileService.update(Mockito.any(EnterpriseFile.class))).thenReturn(false).thenReturn(true);
        when(historyEnterprisesMapper.insertSelective(historyEnterprises)).thenReturn(1).thenReturn(0);
        assertFalse(historyEnterprisesService.saveEdit(historyEnterprises, adminId, request));

//	when(historyEnterpriseFileService.insertSelective(hef)).thenReturn(false).thenReturn(true);
        assertFalse(historyEnterprisesService.saveEdit(historyEnterprises, adminId, request));

        when(enterprisesService.updateByPrimaryKeySelective(Mockito.any(Enterprise.class))).thenReturn(false).thenReturn(true);
        when(historyEnterprisesMapper.insertSelective(historyEnterprises)).thenReturn(1);
        when(historyEnterpriseFileService.insertSelective(Mockito.any(HistoryEnterpriseFile.class))).thenReturn(true);
        assertFalse(historyEnterprisesService.saveEdit(historyEnterprises, adminId, request));

        when(enterpriseFileService.update(Mockito.any(EnterpriseFile.class))).thenReturn(false).thenReturn(true);
        assertFalse(historyEnterprisesService.saveEdit(historyEnterprises, adminId, request));

        when(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey())).thenReturn("ziying").thenReturn("sc");
        assertTrue(historyEnterprisesService.saveEdit(historyEnterprises, adminId, request));

        when(giveMoneyEnterService.updateByEnterId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false).thenReturn(true);
        assertFalse(historyEnterprisesService.saveEdit(historyEnterprises, adminId, request));
        assertTrue(historyEnterprisesService.saveEdit(historyEnterprises, adminId, request));

        approvalProcessDefinition.setStage(1);
        assertFalse(historyEnterprisesService.saveEdit(historyEnterprises, adminId, request));
    }


}
