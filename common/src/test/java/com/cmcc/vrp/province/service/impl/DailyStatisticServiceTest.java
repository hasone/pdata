package com.cmcc.vrp.province.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.province.dao.DailyStatisticMapper;
import com.cmcc.vrp.province.model.DailyStatistic;
import com.cmcc.vrp.province.model.DailyStatisticResult;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.DailyStatisticService;
import com.cmcc.vrp.province.service.EnterprisesService;

/**
 * DailyStatisticServiceTest
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DailyStatisticServiceTest {
    @InjectMocks
    DailyStatisticService dailyStatisticService = new DailyStatisticServiceImpl();

    @Mock
    EnterprisesService enterpriseService;

    @Mock
    DailyStatisticMapper dailyStatisticMapper;

    /**
     * 测试所有企业分析
     */
    @Test
    public void testEntAnalyse() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("beginTime", "2017-01-01");
        map.put("endTime", "2017-10-12");
        Long managerId = 2L;
        List<Enterprise> listEnters = new ArrayList<Enterprise>();
        Enterprise enterprise = new Enterprise();
        enterprise.setDeleteFlag(0);
        enterprise.setName("AAAAAAA");
        enterprise.setCode("123456");
        listEnters.add(enterprise);
        map.put("enters", listEnters);
        
        Mockito.when(enterpriseService.getEnterByManagerId(managerId)).thenReturn(listEnters);

        List<DailyStatisticResult> results = new ArrayList<DailyStatisticResult>();
        Mockito.when(dailyStatisticMapper.getEntSumDailyResult(Mockito.anyMap())).thenReturn(results);
        Assert.assertEquals(dailyStatisticService.getEntSumDailyResult(2L, map), results);

        Mockito.when(dailyStatisticMapper.getEntSumSortResult(Mockito.anyMap())).thenReturn(results);
        Assert.assertEquals(dailyStatisticService.getEntSumSortResult(2L, map), results);

        Mockito.when(dailyStatisticMapper.getEntSumSoldeResult(Mockito.anyMap())).thenReturn(results);
        Assert.assertEquals(dailyStatisticService.getEntSumSoldeResult(2L, map), results);

        Mockito.when(dailyStatisticMapper.getEntPrdSizeDistribution(Mockito.anyMap())).thenReturn(results);
        Assert.assertEquals(dailyStatisticService.getEntPrdSizeDistribution(2L, map, ProductType.FLOW_PACKAGE), results);

        Mockito.when(dailyStatisticMapper.getEntChargeTypeResult(Mockito.anyMap())).thenReturn(results);
        Assert.assertEquals(dailyStatisticService.getEntChargeTypeResult(2L, map), results);

        map = new HashMap<String, Object>();
        Assert.assertEquals(dailyStatisticService.getEntSumDailyResult(2L, map), results);

    }

    @Test
    public void testCountSuccessCountByCity() {
        List<DailyStatistic> list = new ArrayList<DailyStatistic>();
        Mockito.when(
                dailyStatisticMapper.countSuccessCountByCity(Mockito.anyString(), Mockito.anyString(),
                        Mockito.any(List.class), Mockito.any(Long.class), Mockito.any(List.class),
                        Mockito.any(Date.class), Mockito.any(Date.class))).thenReturn(list);
        Assert.assertEquals(dailyStatisticService.countSuccessCountByCity("济南市", "EC", null, 1L, null, null, null),
                list);
    }

    @Test
    public void testCountSuccessMoneyByCity() {
        List<DailyStatistic> list = new ArrayList<DailyStatistic>();
        Mockito.when(
                dailyStatisticMapper.countSuccessCountByCity(Mockito.anyString(), Mockito.anyString(),
                        Mockito.any(List.class), Mockito.any(Long.class), Mockito.any(List.class),
                        Mockito.any(Date.class), Mockito.any(Date.class))).thenReturn(list);
        Assert.assertEquals(dailyStatisticService.countSuccessCountByCity("济南市", "EC", null, 1L, null, null, null),
                list);
    }

    @Test
    public void testCountSuccessCapacityByCity() {
        List<DailyStatistic> list = new ArrayList<DailyStatistic>();
        Mockito.when(
                dailyStatisticMapper.countSuccessCapacityByCity(Mockito.anyString(), Mockito.anyString(),
                        Mockito.any(List.class), Mockito.any(Long.class), Mockito.any(List.class),
                        Mockito.any(Date.class), Mockito.any(Date.class))).thenReturn(list);
        Assert.assertEquals(dailyStatisticService.countSuccessCapacityByCity("济南市", "EC", null, 1L, null, null, null),
                list);
    }

    @Test
    public void testGetDailyStatisticByCityAndSize() {
        List<DailyStatistic> list = new ArrayList<DailyStatistic>();
        Mockito.when(
                dailyStatisticMapper.getDailyStatisticByCityAndSize(Mockito.anyString(), Mockito.anyString(),
                        Mockito.any(List.class), Mockito.any(Long.class), Mockito.any(List.class),
                        Mockito.any(Date.class), Mockito.any(Date.class))).thenReturn(list);
        Assert.assertEquals(
                dailyStatisticService.getDailyStatisticByCityAndSize("济南市", "EC", null, 1L, null, null, null), list);
    }

    @Test
    public void testCountSuccessCountByDate() {
        List<DailyStatistic> list = new ArrayList<DailyStatistic>();
        Mockito.when(
                dailyStatisticMapper.countSuccessCountByDate(Mockito.anyString(), Mockito.anyString(),
                        Mockito.any(List.class), Mockito.any(Long.class), Mockito.any(List.class),
                        Mockito.any(Date.class), Mockito.any(Date.class))).thenReturn(list);
        Assert.assertEquals(dailyStatisticService.countSuccessCountByDate("济南市", "EC", null, 1L, null, null, null),
                list);
    }

    @Test
    public void testCountSuccessCapacityByDate() {
        List<DailyStatistic> list = new ArrayList<DailyStatistic>();
        Mockito.when(
                dailyStatisticMapper.countSuccessCapacityByDate(Mockito.anyString(), Mockito.anyString(),
                        Mockito.any(List.class), Mockito.any(Long.class), Mockito.any(List.class),
                        Mockito.any(Date.class), Mockito.any(Date.class))).thenReturn(list);
        Assert.assertEquals(dailyStatisticService.countSuccessCapacityByDate("济南市", "EC", null, 1L, null, null, null),
                list);
    }

    /**
     * 测试所有平台分析
     */
    @Test
    public void testPlatformAnalyse() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("beginTime", "2017-01-01");
        map.put("endTime", "2017-10-12");
        Long managerId = 2L;

        List<Enterprise> listEnters = new ArrayList<Enterprise>();
        Enterprise enterprise = new Enterprise();
        enterprise.setDeleteFlag(0);
        enterprise.setName("AAAAAAA");
        enterprise.setCode("123456");
        listEnters.add(enterprise);
        map.put("enters", listEnters);
        
        Mockito.when(enterpriseService.getEnterByManagerId(managerId)).thenReturn(listEnters);

        List<DailyStatisticResult> results = new ArrayList<DailyStatisticResult>();
        Mockito.when(dailyStatisticMapper.getPlatformChargeCountDate(Mockito.anyMap())).thenReturn(results);
        Assert.assertEquals(dailyStatisticService.getPlatformChargeCountDate(2L, map), results);

        Mockito.when(dailyStatisticMapper.getPlatformChargeCapacityDate(Mockito.anyMap())).thenReturn(results);
        Assert.assertEquals(dailyStatisticService.getPlatformChargeCapacityDate(2L, map), results);

        Mockito.when(dailyStatisticMapper.getPlatformChargeTypeDate(Mockito.anyMap())).thenReturn(results);
        Assert.assertEquals(dailyStatisticService.getPlatformChargeTypeDate(2L, map), results);

        Mockito.when(dailyStatisticMapper.getPlatformChargeSuccessDate(Mockito.anyMap())).thenReturn(results);
        Assert.assertEquals(dailyStatisticService.getPlatformChargeSuccessDate(2L, map), results);

        Mockito.when(dailyStatisticMapper.getPlatformChargeMoneyDate(Mockito.anyMap())).thenReturn(results);
        Assert.assertEquals(dailyStatisticService.getPlatformChargeMoneyDate(2L, map), results);

        List<DailyStatistic> results2 = new ArrayList<DailyStatistic>();
        Mockito.when(dailyStatisticMapper.getPlatformKeyDate(Mockito.anyMap())).thenReturn(results2);
        Assert.assertEquals(dailyStatisticService.getPlatformKeyDate(2L, map), results2);

        Mockito.when(dailyStatisticMapper.getPlatformData(Mockito.anyMap())).thenReturn(results2);
        Assert.assertEquals(dailyStatisticService.getPlatformData(2L, map), results2);

        Integer result3 = 0;
        Mockito.when(dailyStatisticMapper.countGetPlatformData(Mockito.anyMap())).thenReturn(result3);
        Assert.assertEquals(dailyStatisticService.countGetPlatformData(2L, map), result3);

        DailyStatistic result4 = new DailyStatistic();
        Mockito.when(dailyStatisticMapper.getSumPlatformData(Mockito.anyMap())).thenReturn(result4);
        Assert.assertEquals(dailyStatisticService.getSumPlatformData(2L, map), result4);
        
        Mockito.when(dailyStatisticMapper.getPlatformDataByMap(Mockito.anyMap())).thenReturn(results2);
        Assert.assertEquals(dailyStatisticService.getPlatformDataByMap(2L, map), results2);
        
        Mockito.when(dailyStatisticMapper.getSumPlatformDataByMap(Mockito.anyMap())).thenReturn(result4);
        Assert.assertEquals(dailyStatisticService.getSumPlatformDataByMap(2L, map), result4);
    }

    @Test
    public void testGetDailyStatisticGroupProCode() {
        List<DailyStatistic> list = new ArrayList<DailyStatistic>();
        Mockito.when(dailyStatisticMapper.getDailyStatisticGroupProCode(Mockito.anyMap())).thenReturn(list);
        Assert.assertEquals(dailyStatisticService.getDailyStatisticGroupProCode(null), list);
    }

    @Test
    public void testCountDailyStatisticGroupByCity() {
        Long result = 1L;
        Mockito.when(dailyStatisticMapper.countDailyStatisticGroupByCity(Mockito.anyMap())).thenReturn(result);
        Assert.assertEquals(dailyStatisticService.countDailyStatisticGroupByCity(new HashMap<String, Object>()), result);

    }

    @Test
    public void testCountDailyStatisticGroupProCode() {
        Long result = 1L;
        Mockito.when(dailyStatisticMapper.countDailyStatisticGroupProCode(Mockito.anyMap())).thenReturn(result);
        Assert.assertEquals(dailyStatisticService.countDailyStatisticGroupProCode(new HashMap<String, Object>()),
                result);
    }

    @Test
    public void testGetDailyStatisticGroupByCity() {
        List<DailyStatistic> list = new ArrayList<DailyStatistic>();
        Mockito.when(dailyStatisticMapper.getDailyStatisticGroupByCity(Mockito.anyMap())).thenReturn(list);
        Assert.assertEquals(dailyStatisticService.getDailyStatisticGroupByCity(null), list);
    }
    
    @Test
    public void testCountGetEnterformData(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("beginTime", "2017-01-01");
        map.put("endTime", "2017-10-12");
        Long managerId = 2L;

        List<Enterprise> listEnters = new ArrayList<Enterprise>();
        Enterprise enterprise = new Enterprise();
        enterprise.setDeleteFlag(0);
        enterprise.setName("AAAAAAA");
        enterprise.setCode("123456");
        listEnters.add(enterprise);
        map.put("enters", listEnters);
        
        Integer count = 0;       
        Mockito.when(dailyStatisticMapper.countGetEnterformData(Mockito.anyMap())).thenReturn(count);      
        Assert.assertEquals(dailyStatisticService.countGetEnterformData(managerId, map), count);

    }
}
