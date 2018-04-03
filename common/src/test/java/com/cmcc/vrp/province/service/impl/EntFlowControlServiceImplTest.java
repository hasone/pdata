/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.cmcc.vrp.province.dao.EntFlowControlMapper;
import com.cmcc.vrp.province.dao.EntFlowControlRecordMapper;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.EntFlowControl;
import com.cmcc.vrp.province.model.EntFlowControlRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.EntFlowControlService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.util.QueryObject;

/**
 * <p>
 * Title:EntFlowControlServiceImplTest
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author xujue
 * @date 2016年9月22日
 */
@RunWith(MockitoJUnitRunner.class)
public class EntFlowControlServiceImplTest {

    // 缓存用的hash的key值
    private final String HASH_KEY = "amountUpper";
    private final String HASH_KEY2 = "amountNow";
    private final String HASH_KEY3 = "amountAddition";
    @InjectMocks
    EntFlowControlService efcs = new EntFlowControlServiceImpl();
    @Mock
    EntFlowControlMapper entFlowControlMapper;
    @Mock
    ManagerService managerService;
    @Mock
    EntFlowControlRecordMapper entFlowControlRecordMapper;
    @Mock
    Jedis jedis;
    @Mock
    JedisPool jedisPool;
    @Mock
    TaskProducer taskProducer;
    @Mock
    EnterprisesService enterprisesService;
    @Mock
    AdminManagerService adminManagerService;
    @Mock
    GlobalConfigService globalConfigService;

    @Test
    public void showForPageResultCountTest() {

        QueryObject queryObject = new QueryObject();
        Map<String, Object> map = queryObject.toMap();
        map.put("managerId", 2);
        queryObject.setQueryCriterias(map);

        when(entFlowControlMapper.showForPageResultCount(map)).thenReturn(1);
        List<Long> nodes = new ArrayList<Long>();
        nodes.add(1L);
        when(managerService.getChildNode(Mockito.anyLong())).thenReturn(nodes);
        map.put("managerIds", nodes);
        queryObject.setQueryCriterias(map);
        assertEquals(1, efcs.showForPageResultCount(queryObject));
        assertEquals(0, efcs.showForPageResultCount(null));
    }

    @Test
    public void showForPageResultListTest() {

        QueryObject queryObject = new QueryObject();
        Map<String, Object> map = queryObject.toMap();
        map.put("managerId", 2);
        queryObject.setQueryCriterias(map);

        List<EntFlowControl> entFlowControlList = new ArrayList<EntFlowControl>();
        EntFlowControl entFlowControl = new EntFlowControl();
        entFlowControl.setEnterId(1L);
        entFlowControl.setCountUpper(100L);
        entFlowControlList.add(entFlowControl);
        when(entFlowControlMapper.showForPageResultList(map)).thenReturn(
            entFlowControlList);
        List<Long> nodes = new ArrayList<Long>();
        nodes.add(1L);
        when(managerService.getChildNode(Mockito.anyLong())).thenReturn(nodes);
        map.put("managerIds", nodes);
        queryObject.setQueryCriterias(map);

        when(jedisPool.getResource()).thenReturn(jedis);
        when(jedis.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        EntFlowControl efc = new EntFlowControl();
        efc.setCountNow(100L);
        efc.setCountAddition(100L);
        when(entFlowControlMapper.getFlowControlRecordByEntId(entFlowControl.getEnterId())).thenReturn(efc);

        assertNull(efcs.showForPageResultList(null));
        assertSame(entFlowControlList, efcs.showForPageResultList(queryObject));
        assertSame(entFlowControlList, efcs.showForPageResultList(queryObject));

        when(jedis.hget(Mockito.anyString(), Mockito.anyString())).thenReturn("100");
        entFlowControl.setCountUpper(-1L);
        assertSame(entFlowControlList, efcs.showForPageResultList(queryObject));
        when(jedisPool.getResource()).thenThrow(new RuntimeException());
        assertSame(entFlowControlList, efcs.showForPageResultList(queryObject));
    }

    @Test
    public void getFlowControlRecordByEntIdTest() {
        EntFlowControl erc = new EntFlowControl();
        when(entFlowControlMapper.getFlowControlRecordByEntId(1L)).thenReturn(
            erc);

        assertNotNull(efcs.getFlowControlRecordByEntId(1L));
    }

    @Test
    public void testUpdateSmsFlagByEntId() {
        Long entId = 1L;
        Integer fcSmsFlag = 1;
        when(entFlowControlMapper.updateSmsFlagByEntId(Mockito.anyMap())).thenReturn(0).thenReturn(1);

        assertFalse(efcs.updateSmsFlagByEntId(entId, fcSmsFlag));
        assertTrue(efcs.updateSmsFlagByEntId(entId, fcSmsFlag));
        Mockito.verify(entFlowControlMapper, Mockito.times(2)).updateSmsFlagByEntId(Mockito.anyMap());
    }

    @Test
    public void updateEntFlowControlTest() {
        Long entId = 1L;
        Long countUpper = -1L;
        Long updatorId = 1L;
        int type = 1;

        EntFlowControl entFlowControl = new EntFlowControl();
        entFlowControl.setEnterId(1L);
        entFlowControl.setCountUpper(100L);
        entFlowControl.setUpdatorId(1L);
        entFlowControl.setCreatorId(1L);
        entFlowControl.setDeleteFlag(0);

        when(entFlowControlMapper.getFlowControlUpperByEntId(1L)).thenReturn(null);
        when(entFlowControlMapper.insertEntFlowControlUpper(Mockito.any(EntFlowControl.class))).thenReturn(0).thenReturn(1);
        when(entFlowControlRecordMapper.insertEntFlowControlRecord(Mockito.any(EntFlowControlRecord.class))).thenReturn(0).thenReturn(1);

        assertFalse(efcs.updateEntFlowControl(entId, countUpper, updatorId, type));
        assertFalse(efcs.updateEntFlowControl(entId, countUpper, updatorId, type));
        assertFalse(efcs.updateEntFlowControl(entId, countUpper, updatorId, type));

        countUpper = 100L;
        when(entFlowControlMapper.getFlowControlUpperByEntId(1L)).thenReturn(entFlowControl);
        when(entFlowControlMapper.updateEntFlowControlUpper(Mockito.any(EntFlowControl.class))).thenReturn(0).thenReturn(1);
        when(entFlowControlRecordMapper.insertEntFlowControlRecord(Mockito.any(EntFlowControlRecord.class))).thenReturn(0).thenReturn(1);

        assertFalse(efcs.updateEntFlowControl(entId, countUpper, updatorId, type));
        assertFalse(efcs.updateEntFlowControl(entId, countUpper, updatorId, type));
        assertFalse(efcs.updateEntFlowControl(entId, countUpper, updatorId, type));

        when(jedisPool.getResource()).thenReturn(jedis);
        when(jedis.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1L);
        assertTrue(efcs.updateEntFlowControl(entId, countUpper, updatorId, type));

        when(jedisPool.getResource()).thenThrow(new RuntimeException());
        assertFalse(efcs.updateEntFlowControl(entId, countUpper, updatorId, type));

    }

    @Test
    public void testShowHistoryForPageResultCount() {
        QueryObject queryObject = new QueryObject();
        int count = 1;

        when(entFlowControlRecordMapper.showHistoryForPageResultCount(queryObject.toMap())).thenReturn(count);
        assertSame(0, efcs.showHistoryForPageResultCount(null));
        assertSame(1, efcs.showHistoryForPageResultCount(queryObject));
        Mockito.verify(entFlowControlRecordMapper, Mockito.times(1)).showHistoryForPageResultCount(queryObject.toMap());
    }

    @Test
    public void testShowHistoryForPageResultList() {
        QueryObject queryObject = new QueryObject();
        List<EntFlowControlRecord> list = new ArrayList();

        when(entFlowControlRecordMapper.showHistoryForPageResultList(queryObject.toMap())).thenReturn(list);
        assertNull(efcs.showHistoryForPageResultList(null));
        assertSame(list, efcs.showHistoryForPageResultList(queryObject));
        Mockito.verify(entFlowControlRecordMapper, Mockito.times(1)).showHistoryForPageResultList(queryObject.toMap());
    }

    @Test
    public void testUpdateEntFlowControlAddition() {
        Long entId = 1L;
        Long countAddition = 1L;

        when(entFlowControlRecordMapper.insertEntFlowControlRecord(Mockito.any(EntFlowControlRecord.class))).thenReturn(0).thenReturn(1);
        when(jedisPool.getResource()).thenReturn(jedis);
        when(jedis.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(null).thenReturn("100");
        when(jedis.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1L);
        when(jedis.expireAt(Mockito.anyString(), Mockito.anyLong())).thenReturn(1L);
        List<EntFlowControlRecord> efcrList = new ArrayList();
        EntFlowControlRecord efcr = new EntFlowControlRecord();
        efcr.setCount(100L);
        efcrList.add(efcr);
        when(entFlowControlRecordMapper.selectEntFlowControlRecordByMap(Mockito.anyMap())).thenReturn(efcrList);

        assertFalse(efcs.updateEntFlowControlAddition(entId, countAddition));
        assertTrue(efcs.updateEntFlowControlAddition(entId, countAddition));
        assertTrue(efcs.updateEntFlowControlAddition(entId, countAddition));
        when(jedisPool.getResource()).thenThrow(new RuntimeException());
        assertFalse(efcs.updateEntFlowControlAddition(entId, countAddition));
    }
    
    @Test
    public void testIsFlowControl() {
	Double deltaAmount = 1000.0;
	Long entId = 1L;
	boolean isUpdate = true;
	
	EntFlowControl ef = new EntFlowControl();
	ef.setCountUpper(100L);
	ef.setCountAddition(10L);
	
	when(jedisPool.getResource()).thenReturn(jedis);
	when(jedis.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
	when(entFlowControlMapper.getFlowControlRecordByEntId(Mockito.anyLong())).thenReturn(ef);
	when(jedis.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1L);
	
	assertFalse(efcs.isFlowControl(deltaAmount, entId, isUpdate));
	
	verify(jedis, Mockito.times(3)).hget(Mockito.anyString(), Mockito.anyString());
	verify(entFlowControlMapper, Mockito.times(1)).getFlowControlRecordByEntId(Mockito.anyLong());
	verify(jedis, Mockito.times(2)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }
    
    @Test
    public void testIsFlowControl2() {
	Double deltaAmount = 1000.0;
	Long entId = 1L;
	boolean isUpdate = true;
	
	EntFlowControl ef = new EntFlowControl();
	ef.setCountUpper(100L);
	ef.setCountAddition(100L);
	
	when(jedisPool.getResource()).thenReturn(jedis);
	when(jedis.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
	when(entFlowControlMapper.getFlowControlRecordByEntId(Mockito.anyLong())).thenReturn(null);
	when(jedis.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1L);
	
	assertTrue(efcs.isFlowControl(deltaAmount, entId, isUpdate));
	
	verify(jedis, Mockito.times(3)).hget(Mockito.anyString(), Mockito.anyString());
	verify(entFlowControlMapper, Mockito.times(2)).getFlowControlRecordByEntId(Mockito.anyLong());
	verify(jedis, Mockito.times(3)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }
    
    @Test
    public void testIsFlowControl3() {
	Double deltaAmount = 910.0;
	Long entId = 1L;
	boolean isUpdate = true;
	
	EntFlowControl ef = new EntFlowControl();
	ef.setCountUpper(1000L);
	ef.setCountAddition(0L);
	ef.setFcSmsFlag(1);
	
	Enterprise ent = new Enterprise();
	ent.setCmPhone("18867101111");
	
	List<Administer> adminList = new ArrayList();
	Administer admin = new Administer();
	admin.setMobilePhone("18867102222");
	adminList.add(admin);
	
	when(jedisPool.getResource()).thenReturn(jedis);
	when(jedis.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
	when(entFlowControlMapper.getFlowControlRecordByEntId(Mockito.anyLong())).thenReturn(ef);
	when(jedis.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1L);
	when(jedis.expireAt(Mockito.anyString(), Mockito.anyLong())).thenReturn(1L);
	when(enterprisesService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(ent);
	when(adminManagerService.getAdminForEnter(Mockito.anyLong())).thenReturn(adminList);
	when(taskProducer.produceDeliverNoticeSmsMsg(Mockito.any(SmsPojo.class))).thenReturn(false);
	when(globalConfigService.get(Mockito.anyString())).thenReturn("OK");
	
	assertTrue(efcs.isFlowControl(deltaAmount, entId, isUpdate));
	
	verify(jedis, Mockito.times(3)).hget(Mockito.anyString(), Mockito.anyString());
	verify(entFlowControlMapper, Mockito.times(1)).getFlowControlRecordByEntId(Mockito.anyLong());
	verify(jedis, Mockito.times(3)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	verify(jedis, Mockito.times(2)).expireAt(Mockito.anyString(), Mockito.anyLong());
	verify(enterprisesService, Mockito.times(1)).selectByPrimaryKey(Mockito.anyLong());
	verify(adminManagerService, Mockito.times(1)).getAdminForEnter(Mockito.anyLong());
	verify(taskProducer, Mockito.times(2)).produceDeliverNoticeSmsMsg(Mockito.any(SmsPojo.class));
	verify(globalConfigService, Mockito.times(2)).get(Mockito.anyString());
    }

}
