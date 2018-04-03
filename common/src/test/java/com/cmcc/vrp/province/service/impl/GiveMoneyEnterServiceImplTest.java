/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.GiveMoneyEnterMapper;
import com.cmcc.vrp.province.model.GiveMoneyEnter;
import com.cmcc.vrp.province.service.GiveMoneyEnterService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>Description: </p>
 *
 * @author xj
 * @date 2016年5月30日
 */

@RunWith(MockitoJUnitRunner.class)
public class GiveMoneyEnterServiceImplTest {

    @Mock
    GiveMoneyEnterMapper giveMoneyEnterMapper;

    @InjectMocks
    GiveMoneyEnterService giveMoneyEnterService = new GiveMoneyEnterServiceImpl();

    @Test
    public void testInsert() {

        Long enterId = 1L;
        Long giveMoneyId = 1L;


        when(giveMoneyEnterMapper.insert(Mockito.any(GiveMoneyEnter.class))).thenReturn(1);
        assertFalse(giveMoneyEnterService.insert(null, null));
        assertFalse(giveMoneyEnterService.insert(enterId, null));
        assertTrue(giveMoneyEnterService.insert(giveMoneyId, enterId));
        verify(giveMoneyEnterMapper).insert(Mockito.any(GiveMoneyEnter.class));

    }

  
    @Test
    public void testInsert2() {

        Long enterId = 1L;
        Long giveMoneyId = 1L;

        when(giveMoneyEnterMapper.insert(Mockito.any(GiveMoneyEnter.class))).thenReturn(0);
        try {
            giveMoneyEnterService.insert(giveMoneyId, enterId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    @Test
    public void testUpdateByEnterId() {

        Long enterId = 1L;
        Long giveMoneyId = 1L;
        GiveMoneyEnter gme = new GiveMoneyEnter();

        when(giveMoneyEnterMapper.updateByEnterId(Mockito.any(GiveMoneyEnter.class))).thenReturn(1);
        when(giveMoneyEnterMapper.selectByEnterId(enterId)).thenReturn(null).thenReturn(null).thenReturn(gme);
        when(giveMoneyEnterMapper.insert(Mockito.any(GiveMoneyEnter.class))).thenReturn(0).thenReturn(1);
        assertFalse(giveMoneyEnterService.updateByEnterId(null, null));
        assertFalse(giveMoneyEnterService.updateByEnterId(enterId, null));
        try{
            giveMoneyEnterService.updateByEnterId(giveMoneyId, enterId);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(giveMoneyEnterService.updateByEnterId(giveMoneyId, enterId));
        
        assertTrue(giveMoneyEnterService.updateByEnterId(giveMoneyId, enterId));
        when(giveMoneyEnterMapper.updateByEnterId(Mockito.any(GiveMoneyEnter.class))).thenReturn(0);
        try{
            giveMoneyEnterService.updateByEnterId(giveMoneyId, enterId);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
//        verify(giveMoneyEnterMapper).updateByEnterId(Mockito.any(GiveMoneyEnter.class));

    }

    
    @Test
    public void testSelectByEnterId(){
	Long enterId = 1L;
	GiveMoneyEnter gme = new GiveMoneyEnter();
	
	when(giveMoneyEnterMapper.selectByEnterId(enterId)).thenReturn(gme);
	assertSame(gme, giveMoneyEnterService.selectByEnterId(enterId));
    }
    
    @Test
    public void testDeleteGiveMoneyEnterByEnterId() {
	Long enterId = 1L;
	GiveMoneyEnter gme = new GiveMoneyEnter();
	when(giveMoneyEnterMapper.selectByEnterId(enterId)).thenReturn(null).thenReturn(gme);
	when(giveMoneyEnterMapper.deleteGiveMoneyEnterByEnterId(enterId)).thenReturn(0).thenReturn(1);
	assertTrue(giveMoneyEnterService.deleteGiveMoneyEnterByEnterId(enterId));
	assertFalse(giveMoneyEnterService.deleteGiveMoneyEnterByEnterId(enterId));
	assertTrue(giveMoneyEnterService.deleteGiveMoneyEnterByEnterId(enterId));
    }


}
