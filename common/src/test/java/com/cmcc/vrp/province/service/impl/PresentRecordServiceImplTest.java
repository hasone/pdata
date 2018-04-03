package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.dao.PresentRecordMapper;
import com.cmcc.vrp.province.model.PresentRecord;
import com.cmcc.vrp.province.model.PresentRecordResult;
import com.cmcc.vrp.province.model.PresentRule;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.json.post.PresentRecordJson;
import com.cmcc.vrp.province.service.PresentRecordService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.QueryObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author qqy
 */
@RunWith(MockitoJUnitRunner.class)
public class PresentRecordServiceImplTest {

    @InjectMocks
    PresentRecordService presentRecordService = new PresentRecordServiceImpl();

    @Mock
    ProductService productService;

    @Mock
    PresentRecordMapper presentRecordMapper;

    /**
     * 创建规则记录
     */
    @Test
    public void testCreate() {
        //return false
        assertFalse(presentRecordService.create(new PresentRule(), new LinkedList<PresentRecordJson>()));

        when(productService.selectProductById(initInvalidPresentRecordJsonList().get(0).getPrdId())).thenReturn(null);
        assertFalse(presentRecordService.create(initPresentRule(), initInvalidPresentRecordJsonList()));

        //return true
        when(productService.selectProductById(initPresentRecordJsonList().get(0).getPrdId())).thenReturn(initProduct());
        when(presentRecordMapper.batchInsert(Mockito.anyList())).thenReturn(2);
        assertTrue(presentRecordService.create(initPresentRule(), initPresentRecordJsonList()));

        Mockito.verify(productService, Mockito.times(2)).selectProductById(Mockito.anyLong());
        Mockito.verify(presentRecordMapper, Mockito.times(2)).batchInsert(Mockito.anyList());
    }

    @Test
    public void testCreate2() {
        Product p = initProduct();
        p.setType((int) ProductType.FLOW_ACCOUNT.getValue());
        when(productService.selectProductById(initPresentRecordJsonList().get(0).getPrdId())).thenReturn(p);
        when(presentRecordMapper.batchInsert(Mockito.anyList())).thenReturn(2);
        assertTrue(presentRecordService.create(initPresentRule(), initPresentRecordJsonList2()));

        Mockito.verify(presentRecordMapper, Mockito.times(1)).batchInsert(Mockito.anyList());
    }

    @Test
    public void testQueryCount() {
        when(presentRecordMapper.queryCount(Mockito.anyMap())).thenReturn(2);
        assertSame(2, presentRecordService.queryCount(new QueryObject()));
        Mockito.verify(presentRecordMapper).queryCount(Mockito.anyMap());
    }

    @Test
    public void testQueryCountPlus() {
        when(presentRecordMapper.queryCountPlus(Mockito.anyMap())).thenReturn(2);
        assertSame(2, presentRecordService.queryCountPlus(Mockito.anyMap()));
        Mockito.verify(presentRecordMapper).queryCountPlus(Mockito.anyMap());
    }

    @Test
    public void testQueryRecord() {
        List<PresentRecordResult> presentRecordResultList = new LinkedList<PresentRecordResult>();
        QueryObject queryObject = new QueryObject();
        when(presentRecordMapper.queryRecord(Mockito.anyMap())).thenReturn(presentRecordResultList);
        assertSame(presentRecordResultList.size(), presentRecordService.queryRecord(queryObject).size());
        Mockito.verify(presentRecordMapper).queryRecord(Mockito.anyMap());
    }

    @Test
    public void testQueryRecordPlus() {
        List<PresentRecordResult> presentRecordResultList = new LinkedList<PresentRecordResult>();
        when(presentRecordMapper.queryRecordPlus(Mockito.anyMap())).thenReturn(presentRecordResultList);
        assertSame(presentRecordResultList.size(), presentRecordService.queryRecordPlus((new QueryObject()).toMap()).size());
        Mockito.verify(presentRecordMapper).queryRecordPlus(Mockito.anyMap());
    }


    @Test
    public void testSelectByRuleId() {
        long ruleId = 1L;
        List<PresentRecord> presentRecordList = new LinkedList<PresentRecord>();
        when(presentRecordMapper.selectByRuleId(ruleId)).thenReturn(presentRecordList);
        assertSame(presentRecordList.size(), presentRecordService.selectByRuleId(ruleId).size());
        Mockito.verify(presentRecordMapper).selectByRuleId(ruleId);
    }

    @Test
    public void testSelectByRecordId() {
        when(presentRecordMapper.selectByPrimaryKey(initPresentRecord().getId())).thenReturn(initPresentRecord());
        assertSame(initPresentRecord().getId(), presentRecordService.selectByRecordId(initPresentRecord().getId()).getId());
        Mockito.verify(presentRecordMapper).selectByPrimaryKey(Mockito.anyLong());
    }

    @Test
    public void testCount() {
        long id = 1L;
        when(presentRecordMapper.count(id)).thenReturn(2);
        assertSame(2, presentRecordService.count(id));
        Mockito.verify(presentRecordMapper).count(Mockito.anyLong());
    }

    private List<PresentRecordJson> initPresentRecordJsonList() {
        List<PresentRecordJson> presentRecordJsonList = new LinkedList<PresentRecordJson>();
        PresentRecordJson record1 = new PresentRecordJson();
        record1.setPrdId(1L);
        record1.setPrdName("30MB");
        record1.setPhones("18867103717,18867102222");
        record1.setGiveNum(2);
        presentRecordJsonList.add(record1);

        return presentRecordJsonList;
    }

    private List<PresentRecordJson> initPresentRecordJsonList2() {
        List<PresentRecordJson> presentRecordJsonList = new LinkedList<PresentRecordJson>();
        PresentRecordJson record1 = new PresentRecordJson();
        record1.setPrdId(1L);
        record1.setPhones("18867103717,18867102222");
        record1.setGiveNum(2);
        presentRecordJsonList.add(record1);

        return presentRecordJsonList;
    }

    private List<PresentRecordJson> initInvalidPresentRecordJsonList() {
        List<PresentRecordJson> presentRecordJsonList = new LinkedList<PresentRecordJson>();
        PresentRecordJson record2 = new PresentRecordJson();
        record2.setPrdId(2L);
        record2.setPrdName("100MB");
        record2.setPhones("18867101111,18867103333");
        record2.setGiveNum(2);
        presentRecordJsonList.add(record2);
        return presentRecordJsonList;
    }

    private Product initProduct() {
        Product record = new Product();
        record.setId(1L);
        record.setType(2);
        return record;
    }

    private PresentRule initPresentRule() {
        PresentRule record = new PresentRule();
        record.setId(1L);
        return record;
    }

    private PresentRecord initPresentRecord() {
        PresentRecord record = new PresentRecord();
        record.setId(1L);
        return record;
    }

    @Test
    public void testBatchUpdateChargeResult() {
        List<PresentRecord> records = new ArrayList();
        when(presentRecordMapper.batchUpdateChargeResult(records)).thenReturn(0);
        assertFalse(presentRecordService.batchUpdateChargeResult(null));
        assertFalse(presentRecordService.batchUpdateChargeResult(records));
        PresentRecord presentRecord = new PresentRecord();
        records.add(presentRecord);
        assertFalse(presentRecordService.batchUpdateChargeResult(records));
        when(presentRecordMapper.batchUpdateChargeResult(records)).thenReturn(records.size());
        assertTrue(presentRecordService.batchUpdateChargeResult(records));

        Mockito.verify(presentRecordMapper, Mockito.times(2)).batchUpdateChargeResult(records);
    }

    @Test
    public void testBatchUpdateStatus() throws Exception {
        List<Long> ids = new ArrayList();
        assertFalse(presentRecordService.batchUpdateStatus(null, null));
        assertFalse(presentRecordService.batchUpdateStatus(ids, ChargeRecordStatus.FAILED));
        ids.add(1L);
        when(presentRecordMapper.batchUpdateStatus(Mockito.anyList(), Mockito.anyInt())).thenReturn(0).thenReturn(ids.size());
        assertFalse(presentRecordService.batchUpdateStatus(ids, ChargeRecordStatus.FAILED));
        assertTrue(presentRecordService.batchUpdateStatus(ids, ChargeRecordStatus.FAILED));

        Mockito.verify(presentRecordMapper, Mockito.times(2)).batchUpdateStatus(Mockito.anyList(), Mockito.anyInt());
    }


    @Test
    public void testUpdateStatus() throws Exception {
        assertFalse(presentRecordService.updateStatus(null, ChargeRecordStatus.FAILED, ""));
        assertFalse(presentRecordService.updateStatus(324L, null, ""));

        when(presentRecordMapper.updateStatusAndStatusCode(anyLong(), anyInt(), anyString(), anyString()))
                .thenReturn(1)
                .thenReturn(0);

        assertTrue(presentRecordService.updateStatus(234L, ChargeRecordStatus.FAILED, ""));
        assertFalse(presentRecordService.updateStatus(234L, ChargeRecordStatus.FAILED, ""));

        verify(presentRecordMapper, times(2)).updateStatusAndStatusCode(anyLong(), anyInt(), anyString(), anyString());
    }

    @Test
    public void testBatchUpdateStatusCode() {
        List<Long> ids = new ArrayList();
        String statusCode = "204";
        ids.add(1L);
        ids.add(2L);
        when(presentRecordMapper.batchUpdateStatusCode(Mockito.anyList(), Mockito.anyString())).thenReturn(0).thenReturn(ids.size());

        assertFalse(presentRecordService.batchUpdateStatusCode(null, statusCode));
        assertFalse(presentRecordService.batchUpdateStatusCode(new ArrayList(), statusCode));
        assertFalse(presentRecordService.batchUpdateStatusCode(ids, statusCode));
        assertTrue(presentRecordService.batchUpdateStatusCode(ids, statusCode));

    }

    @Test
    public void testUpdateStatusCode() {
        Long recordId = 1L;
        String statusCode = "203";

        when(presentRecordMapper.updateStatusCode(Mockito.anyLong(), Mockito.anyString())).thenReturn(0).thenReturn(1);
        assertFalse(presentRecordService.updateStatusCode(null, statusCode));
        assertFalse(presentRecordService.updateStatusCode(recordId, statusCode));
        assertTrue(presentRecordService.updateStatusCode(recordId, statusCode));
    }

}
