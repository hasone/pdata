/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.BlackAndWhiteListMapper;
import com.cmcc.vrp.province.model.BlackAndWhiteList;
import com.cmcc.vrp.province.service.BlackAndWhiteListService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>Description: </p>
 *
 * @author xj
 * @date 2016年5月20日
 */
@RunWith(MockitoJUnitRunner.class)
public class BlackAndWhiteListServiceImplTest {

    @InjectMocks
    @Autowired
    BlackAndWhiteListService blackAndWhiteListService = new BlackAndWhiteListServiceImpl();

    @Mock
    BlackAndWhiteListMapper mapper;


    @Test
    public void testGetPhonesByActivityId() {

        List<BlackAndWhiteList> bawList = new ArrayList();
        BlackAndWhiteList baw = new BlackAndWhiteList();
        baw.setPhone("18867105219");
        bawList.add(baw);
        Long activityId = 1L;
        when(mapper.getPhonesByActivityId(activityId)).thenReturn(bawList);
        assertNotNull(blackAndWhiteListService.getPhonesByActivityId(activityId));
        verify(mapper).getPhonesByActivityId(activityId);

    }

    @Test
    public void testGetPhonesByActivityId2() {

        Long activityId = 1L;
        when(mapper.getPhonesByActivityId(activityId)).thenReturn(null);

        assertNull(blackAndWhiteListService.getPhonesByActivityId(null));
        assertNull(blackAndWhiteListService.getPhonesByActivityId(activityId));
        verify(mapper).getPhonesByActivityId(activityId);

    }


    @Test
    public void testDelete() {

        List<String> phoneList = new ArrayList();
        String phone = "13929939292";
        phoneList.add(phone);
        Long activityId = 1L;
        when(mapper.batchDelete(phoneList, activityId)).thenReturn(0).thenReturn(phoneList.size());

        assertFalse(blackAndWhiteListService.delete(null, activityId));
        assertFalse(blackAndWhiteListService.delete(phoneList, null));
        assertFalse(blackAndWhiteListService.delete(phoneList, activityId));
        assertTrue(blackAndWhiteListService.delete(phoneList, activityId));
        verify(mapper, Mockito.times(2)).batchDelete(phoneList, activityId);

    }

    @Test
    public void testDeleteByActivityId() {

        Long activityId = 1L;
        when(mapper.deleteByActivityId(activityId)).thenReturn(1);

        assertEquals(0, blackAndWhiteListService.deleteByActivityId(null));
        assertEquals(1, blackAndWhiteListService.deleteByActivityId(activityId));
        verify(mapper).deleteByActivityId(activityId);

    }
	
	@Test
	public void testInsertBatch() {

		List<String> phoneList = new ArrayList();
		phoneList.add("13929939292");
		phoneList.add("13929939293");
		String activityType = "aaa";
		Long activityId = 1L;
		Integer isWhiteFlag = 1;

		when(mapper.insertBatch(Mockito.anyList())).thenReturn(0).thenReturn(phoneList.size());
		assertFalse(blackAndWhiteListService.insertBatch(null, null, null, null));
		assertFalse(blackAndWhiteListService.insertBatch(phoneList, null, null, null));
		assertFalse(blackAndWhiteListService.insertBatch(phoneList, activityType, null, null));
		assertFalse(blackAndWhiteListService.insertBatch(phoneList, activityType, activityId, null));
		try{
		    blackAndWhiteListService.insertBatch(phoneList, activityType, activityId, isWhiteFlag);
		} catch(Exception e) {
		    System.out.println(e.getMessage());
		}
		assertTrue(blackAndWhiteListService.insertBatch(phoneList, activityType, activityId, isWhiteFlag));
		assertTrue(blackAndWhiteListService.insertBatch(new ArrayList(), activityType, activityId, isWhiteFlag));
		verify(mapper, Mockito.times(2)).insertBatch(Mockito.anyList());
		
	}


}
