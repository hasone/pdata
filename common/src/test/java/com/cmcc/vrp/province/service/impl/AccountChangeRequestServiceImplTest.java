/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.enums.AccountChangeRequestStatus;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.province.dao.AccountChangeRequestMapper;
import com.cmcc.vrp.province.dao.DistrictMapper;
import com.cmcc.vrp.province.model.AccountChangeApprovalRecord;
import com.cmcc.vrp.province.model.AccountChangeRequest;
import com.cmcc.vrp.province.service.AccountChangeApprovalRecordService;
import com.cmcc.vrp.province.service.AccountChangeRequestService;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.QueryObject;

/**
 * <p>Description: 账户充值请求服务单元测试类</p>
 *
 * @author xj
 * @date 2016年5月19日
 */

@RunWith(MockitoJUnitRunner.class)
public class AccountChangeRequestServiceImplTest {

    @InjectMocks
    AccountChangeRequestService accrService = new AccountChangeRequestServiceImpl();

    @Mock
    AccountChangeRequestMapper acrMapper;

    @Mock
    AccountChangeApprovalRecordService approvalRecordService;

    @Mock
    DistrictMapper districtMapper;

    @Mock
    AccountService accountService;
    
    @Mock
    ProductService productService;


    @Test
    public void testInsert() {

        AccountChangeRequest acr = initObject();
        when(acrMapper.insert(acr)).thenReturn(1);

        assertFalse(accrService.insert(null));
        assertTrue(accrService.insert(acr));
        verify(acrMapper).insert(acr);

    }

    //插入审批记录
//	@Test
//	public void testInsert2() {
//		
//		AccountChangeRequest acrCommit = initObject(AccountChangeRequestStatus.COMMIT.getValue());
////		AccountChangeApprovalRecord acar = build(acrCommit.getId(), acrCommit.getCreatorId(),
////				acrCommit.getDesc(), acrCommit.getSerialNum(), AccountChangeApprovalOperationResult.COMMIT);	
////		
//				
////		AccountChangeApprovalRecord acar = new AccountChangeApprovalRecord();
//		when(acrMapper.insert(acrCommit)).thenReturn(1);
//		when(approvalRecordService.insert(Mockito.argThat(new IsParameterClass()))).thenReturn(true);
////		when(acarm.insert(acar)).thenReturn(1);
//		
//		assertTrue(accrService.insert(acrCommit));
//		verify(acrMapper).insert(acrCommit);
//		verify(approvalRecordService).insert(Mockito.argThat(new IsParameterClass()));
//
//	}

    @Test
    public void testCommit() {

        Long id = 1L;
        Long operatorId = 1L;
        String comment = "aaa";
        String serialNum = "aaa";

        when(acrMapper.updateStatus(id, AccountChangeRequestStatus.APPROVING_ONE.getValue(),
            AccountChangeRequestStatus.APPROVING_ONE.getDesc(), operatorId)).thenReturn(0).thenReturn(1);
        when(approvalRecordService.insert(Mockito.argThat(new IsParameterClass()))).thenReturn(false).thenReturn(true);

        assertFalse(accrService.commit(null, null, null, null));
        assertFalse(accrService.commit(id, null, null, null));
        assertFalse(accrService.commit(id, operatorId, null, null));
        assertFalse(accrService.commit(id, operatorId, null, null));
        assertFalse(accrService.commit(id, operatorId, comment, null));
        assertFalse(accrService.commit(id, operatorId, comment, serialNum));
        assertFalse(accrService.commit(id, operatorId, comment, serialNum));
        assertTrue(accrService.commit(id, operatorId, comment, serialNum));

        verify(acrMapper, Mockito.times(3)).updateStatus(id, AccountChangeRequestStatus.APPROVING_ONE.getValue(),
            AccountChangeRequestStatus.APPROVING_ONE.getDesc(), operatorId);
        verify(approvalRecordService, Mockito.times(2)).insert(Mockito.argThat(new IsParameterClass()));

    }

    @Test
    public void testQuery() {

        QueryObject queryObject = new QueryObject();
        Map<String, Object> queryMap = new HashMap();
//		Object obj = (Object) new String("aaa");
//		
//		queryMap.put("districtId", "1");
        queryObject.setQueryCriterias(queryMap);
        Map map = queryObject.toMap();
        List<Long> districtIds = new ArrayList();
        List<AccountChangeRequest> list = new ArrayList();

//		when(districtMapper.selectNodeById(Long.parseLong((String) map.get("districtId")))).thenReturn(districtIds);
        when(acrMapper.query(map)).thenReturn(list);

        assertNotNull(accrService.query(queryObject));

//		verify(districtMapper).selectNodeById(Long.parseLong((String) map.get("districtId")));
        verify(acrMapper).query(map);

    }

    @Test
    public void testQuery2() {

        QueryObject queryObject = new QueryObject();
        Map<String, Object> queryMap = new HashMap();

        queryMap.put("districtId", "1");
        queryMap.put("endTime", "2016-05-30");
        queryObject.setQueryCriterias(queryMap);
        Map map = queryObject.toMap();
        List<Long> districtIds = new ArrayList();
        List<AccountChangeRequest> list = new ArrayList();
        map.put("districtIds", list);
        map.put("endTime", "2016-05-30 23:59:59");

        when(districtMapper.selectNodeById(Long.parseLong((String) map.get("districtId")))).thenReturn(districtIds);
        when(acrMapper.query(map)).thenReturn(list);

        assertNotNull(accrService.query(queryObject));

//		verify(districtMapper).selectNodeById(Long.parseLong((String) map.get("districtId")));
        verify(acrMapper).query(map);

    }


    @Test
    public void testQueryCount() {

        QueryObject queryObject = new QueryObject();
        Map<String, Object> queryMap = new HashMap();

        queryObject.setQueryCriterias(queryMap);
        Map map = queryObject.toMap();
        List<Long> districtIds = new ArrayList();
        List<AccountChangeRequest> list = new ArrayList();

        when(acrMapper.queryCount(map)).thenReturn(1);

        assertEquals(1, accrService.queryCount(queryObject));

        verify(acrMapper).queryCount(map);

    }

    @Test
    public void testQueryCount2() {

        QueryObject queryObject = new QueryObject();
        Map<String, Object> queryMap = new HashMap();

        queryMap.put("districtId", "1");
        queryMap.put("endTime", "2016-05-30");
        queryObject.setQueryCriterias(queryMap);
        Map map = queryObject.toMap();
        List<Long> districtIds = new ArrayList();
        List<AccountChangeRequest> list = new ArrayList();
        map.put("districtIds", list);
        map.put("endTime", "2016-05-30 23:59:59");

        when(districtMapper.selectNodeById(Long.parseLong((String) map.get("districtId")))).thenReturn(districtIds);
        when(acrMapper.queryCount(map)).thenReturn(1);

        assertEquals(1, accrService.queryCount(queryObject));

        verify(districtMapper).selectNodeById(Long.parseLong((String) map.get("districtId")));
        verify(acrMapper).queryCount(map);

    }

    @Test
    public void testAccountChangeReques() {

        AccountChangeRequest accountChangeRequest = new AccountChangeRequest();
        Long id = 1L;
        when(acrMapper.get(id)).thenReturn(accountChangeRequest);
        assertNotNull(accrService.get(id));
        verify(acrMapper).get(id);

    }

    /* 一级审批  */
    @Test
    public void testApproval() {

        AccountChangeRequest acr = new AccountChangeRequest();
        acr.setStatus(1);
        Long id = 1L;
        Long ownerId = 1L;
        Long prdId = 1L;
        Double delta = 1.0d;
        String serialNum = "aaa";
        String comment = "aaa";
        Long operatorId = 1L;
        acr.setEntId(ownerId);
        acr.setPrdId(prdId);
        acr.setCount(delta);

        when(acrMapper.get(id)).thenReturn(acr);
        when(acrMapper.updateStatus(id, AccountChangeRequestStatus.APPROVING_TWO.getValue(),
            AccountChangeRequestStatus.APPROVING_TWO.getDesc(), operatorId)).thenReturn(1);
        when(approvalRecordService.insert(Mockito.argThat(new IsParameterClass()))).thenReturn(true);

        assertTrue(accrService.approval(prdId, operatorId, comment, serialNum));
        verify(acrMapper).get(id);
        verify(acrMapper).updateStatus(id, AccountChangeRequestStatus.APPROVING_TWO.getValue(),
            AccountChangeRequestStatus.APPROVING_TWO.getDesc(), operatorId);
        verify(approvalRecordService).insert(Mockito.argThat(new IsParameterClass()));

    }

    /* 二级审批  */
    @Test
    public void testApproval2() {

        AccountChangeRequest acr = new AccountChangeRequest();
        acr.setStatus(2);
        Long id = 1L;
        Long ownerId = 1L;
        Long prdId = 1L;
        Double delta = 1.0d;
        String serialNum = "aaa";
        String comment = "aaa";
        Long operatorId = 1L;
        acr.setEntId(ownerId);
        acr.setPrdId(prdId);
        acr.setCount(delta);
        ;

        when(acrMapper.get(id)).thenReturn(acr);
        when(acrMapper.updateStatus(id, AccountChangeRequestStatus.APPROVING_THREE.getValue(),
            AccountChangeRequestStatus.APPROVING_THREE.getDesc(), operatorId)).thenReturn(1);
        when(approvalRecordService.insert(Mockito.argThat(new IsParameterClass()))).thenReturn(true);

        assertTrue(accrService.approval(prdId, operatorId, comment, serialNum));
        verify(acrMapper).get(id);
        verify(acrMapper).updateStatus(id, AccountChangeRequestStatus.APPROVING_THREE.getValue(),
            AccountChangeRequestStatus.APPROVING_THREE.getDesc(), operatorId);
        verify(approvalRecordService).insert(Mockito.argThat(new IsParameterClass()));

    }

    /* 三级审批  */
    @Test
    public void testApproval3() {

        AccountChangeRequest acr = new AccountChangeRequest();
        acr.setStatus(3);
        Long id = 1L;
        Long ownerId = 1L;
        Long prdId = 1L;
        Double delta = 1.0d;
        String serialNum = "aaa";
        String comment = "aaa";
        Long operatorId = 1L;
        acr.setEntId(ownerId);
        acr.setPrdId(prdId);
        acr.setCount(delta);

        when(acrMapper.get(id)).thenReturn(acr);
        when(acrMapper.updateStatus(id, AccountChangeRequestStatus.APPROVED.getValue(),
            AccountChangeRequestStatus.APPROVED.getDesc(), operatorId)).thenReturn(1);
        when(approvalRecordService.insert(Mockito.argThat(new IsParameterClass()))).thenReturn(true);
        when(accountService.addCount(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AccountType.class), Mockito.anyDouble(),
            Mockito.anyString(), Mockito.anyString())).thenReturn(false).thenReturn(true);
        when(productService.get(Mockito.anyLong())).thenReturn(null);

        assertFalse(accrService.approval(prdId, operatorId, comment, serialNum));
        assertTrue(accrService.approval(prdId, operatorId, comment, serialNum));
        verify(acrMapper, Mockito.times(2)).get(id);
        verify(acrMapper, Mockito.times(2)).updateStatus(id, AccountChangeRequestStatus.APPROVED.getValue(),
            AccountChangeRequestStatus.APPROVED.getDesc(), operatorId);
        verify(approvalRecordService, Mockito.times(2)).insert(Mockito.argThat(new IsParameterClass()));

    }


    @Test
    public void testReject() {

        Long id = 1L;
        String serialNum = "aaa";
        String comment = "aaa";
        Long operatorId = 1L;

        when(acrMapper.updateStatus(id, AccountChangeRequestStatus.REJECT.getValue(),
            AccountChangeRequestStatus.REJECT.getDesc(), operatorId)).thenReturn(1);
        when(approvalRecordService.insert(Mockito.argThat(new IsParameterClass()))).thenReturn(true);

        assertTrue(accrService.reject(id, operatorId, comment, serialNum));
        verify(acrMapper).updateStatus(id, AccountChangeRequestStatus.REJECT.getValue(),
            AccountChangeRequestStatus.REJECT.getDesc(), operatorId);
        verify(approvalRecordService).insert(Mockito.argThat(new IsParameterClass()));

    }

    @Test
    public void testupdateCount() {
        Long accountChangeRequestId = 1L;
        Double deltaCount = 1.0;
        Long operatorId = 1L;
        when(acrMapper.updateCount(Mockito.anyLong(), Mockito.anyDouble(), Mockito.anyInt(),
            Mockito.anyString(), Mockito.anyLong())).thenReturn(0).thenReturn(1);

        assertFalse(accrService.updateCount(accountChangeRequestId, deltaCount, operatorId));
        assertTrue(accrService.updateCount(accountChangeRequestId, deltaCount, operatorId));
    }

    public AccountChangeRequest initObject() {
        AccountChangeRequest acr = new AccountChangeRequest();
        acr.setCount(1.0D);
        acr.setAccountId(1L);
        acr.setEntId(1L);
        acr.setPrdId(1L);
        acr.setCreatorId(1L);
        acr.setSerialNum("aaa");
        acr.setStatus(1);

        return acr;
    }


    //自定义一个与之匹配的matcher类
    class IsParameterClass extends ArgumentMatcher<AccountChangeApprovalRecord> {
        public boolean matches(Object para) {
            return para.getClass() == AccountChangeApprovalRecord.class;
        }
    }

}
