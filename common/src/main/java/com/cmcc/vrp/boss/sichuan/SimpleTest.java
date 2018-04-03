///**
// * 
// */
//package com.cmcc.vrp.boss.sichuan;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
//
//import java.awt.List;
//import java.util.Iterator;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
///**
// * <p>Description: </p>
// * @author xj
// * @date 2016年5月11日*/
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:conf/applicationContext.xml" })
//public class SimpleTest {
//
////	@Test
////	public void Test() {
////		//arrange
////		Iterator i = mock(Iterator.class);
////		when(i.next()).thenReturn("Hello").thenReturn("world");
////		//act
////		String result = i.next() + " " + i.next();
////		//verify
////		verify(i,times(2)).next();
////		//assert
////		assertEquals("Hello world", result);
////		
////	}
//	@Test
//	public void verifyTest(){
//		
//		List<String> mock1 = mock(List.class);
//		List<String> mock2 = mock(List.class);
//		
//		when(mock1.get(0)).thenReturn("hello");
//		
//		mock1.get(0);
//		mock1.get(1);
//		mock1.get(2);
//		
//		mock2.get(0);
//		
//		verify(mock1).get(0);
//		verify(mock1, never()).get(3);
//		verifyNoMoreInteractions(mock1);
//		verifyNoMoreInteractions(mock2);
// 	}
//
//}
