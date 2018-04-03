package com.cmcc.vrp.province.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.province.dao.DailyStatisticMapper;
import com.cmcc.vrp.province.model.DailyStatistic;
import com.cmcc.vrp.province.model.DailyStatisticResult;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.DailyStatisticService;
import com.cmcc.vrp.province.service.EnterprisesService;

/**
 * @author lgk8023
 *
 */
@Service
public class DailyStatisticServiceImpl implements DailyStatisticService {

    @Autowired
    EnterprisesService enterpriseService;

    @Autowired
    DailyStatisticMapper dailyStatisticMapper;

    @Override
    public List<DailyStatisticResult> getEntSumDailyResult(Long managerId, Map<String, Object> map) {
        fillingEntsTimeToMap(map, managerId);
        return dailyStatisticMapper.getEntSumDailyResult(map);
    }

    @Override
    public List<DailyStatisticResult> getEntSumSortResult(Long managerId, Map<String, Object> map) {
        fillingEntsTimeToMap(map, managerId);
        return dailyStatisticMapper.getEntSumSortResult(map);
    }

    @Override
    public List<DailyStatisticResult> getEntSumSoldeResult(Long managerId, Map<String, Object> map) {
        fillingEntsTimeToMap(map, managerId);
        return dailyStatisticMapper.getEntSumSoldeResult(map);
    }

    @Override
    public List<DailyStatisticResult> getEntPrdSizeDistribution(Long managerId, Map<String, Object> map,
            ProductType prdType) {
        fillingEntsTimeToMap(map, managerId);
        map.put("prdType", prdType.getValue());
        List<DailyStatisticResult> list = dailyStatisticMapper.getEntPrdSizeDistribution(map);

        //将name /1024 + M
        /*for(DailyStatisticResult result : list){
            Long size = NumberUtils.toLong(result.getName());
            if(size > 0){
                result.setName(size/1024 + "M"); 
            }
        }*/

        return list;
    }

    @Override
    public List<DailyStatisticResult> getEntChargeTypeResult(Long managerId, Map<String, Object> map) {
        fillingEntsTimeToMap(map, managerId);
        return dailyStatisticMapper.getEntChargeTypeResult(map);
    }

    /**
     *  将企业和时间填充到搜索map中
     *  qihang
     */
    private void fillingEntsTimeToMap(Map<String, Object> map, Long managerId) {
        if (map.get("startTime") != null) {
            map.put("startTime", map.get("startTime") + " 00:00:00");
        }
        if (map.get("endTime") != null) {
            map.put("endTime", map.get("endTime") + " 00:00:00");
        }
        List<Enterprise> listEnters = enterpriseService.getEnterByManagerIdEnterName(managerId,
                (String) map.get("eName"));
        if(listEnters != null && listEnters.size() > 0){
            map.put("enters", listEnters);
        }
    }

    @Override
    public List<DailyStatistic> countSuccessCountByCity(String city, String chargeType, List<Enterprise> listEnters,
            Long prdId, List<Byte> productTypeList, Date startTime, Date endTime) {
        return dailyStatisticMapper.countSuccessCountByCity(city, chargeType, listEnters, prdId, productTypeList,
                startTime, endTime);
    }

    @Override
    public List<DailyStatistic> countSuccessMoneyByCity(String city, String chargeType, List<Enterprise> listEnters,
            Long prdId, List<Byte> productTypeList, Date startTime, Date endTime) {
        return dailyStatisticMapper.countSuccessMoneyByCity(city, chargeType, listEnters, prdId, productTypeList,
                startTime, endTime);
    }

    @Override
    public List<DailyStatistic> countSuccessCapacityByCity(String city, String chargeType, List<Enterprise> listEnters,
            Long prdId, List<Byte> productTypeList, Date startTime, Date endTime) {
        return dailyStatisticMapper.countSuccessCapacityByCity(city, chargeType, listEnters, prdId, productTypeList,
                startTime, endTime);
    }

    @Override
    public List<DailyStatistic> getDailyStatisticByCityAndSize(String city, String chargeType,
            List<Enterprise> listEnters, Long prdId, List<Byte> productTypeList, Date startTime, Date endTime) {
        return dailyStatisticMapper.getDailyStatisticByCityAndSize(city, chargeType, listEnters, prdId,
                productTypeList, startTime, endTime);
    }

    @Override
    public List<DailyStatistic> countSuccessCountByDate(String city, String chargeType, List<Enterprise> listEnters,
            Long prdId, List<Byte> productTypeList, Date startTime, Date endTime) {
        return dailyStatisticMapper.countSuccessCountByDate(city, chargeType, listEnters, prdId, productTypeList,
                startTime, endTime);
    }

    @Override
    public List<DailyStatistic> countSuccessCapacityByDate(String city, String chargeType, List<Enterprise> listEnters,
            Long prdId, List<Byte> productTypeList, Date startTime, Date endTime) {
        return dailyStatisticMapper.countSuccessCapacityByDate(city, chargeType, listEnters, prdId, productTypeList,
                startTime, endTime);
    }

    @Override
    public List<DailyStatisticResult> getPlatformChargeCountDate(Long managerId, Map<String, Object> map) {
        fillingEntsTimeToMap(map, managerId);
        //判断搜索条件是否存在
        List<Enterprise> listEnters = (List<Enterprise>) map.get("enters");
        if (listEnters == null || listEnters.size() <= 0) {
            return new ArrayList<DailyStatisticResult>();
        }
        return dailyStatisticMapper.getPlatformChargeCountDate(map);
    }

    @Override
    public List<DailyStatisticResult> getPlatformChargeCapacityDate(Long managerId, Map<String, Object> map) {
        fillingEntsTimeToMap(map, managerId);
        //判断搜索条件是否存在
        List<Enterprise> listEnters = (List<Enterprise>) map.get("enters");
        if (listEnters == null || listEnters.size() <= 0) {
            return new ArrayList<DailyStatisticResult>();
        }
        return dailyStatisticMapper.getPlatformChargeCapacityDate(map);
    }

    @Override
    public List<DailyStatisticResult> getPlatformChargeTypeDate(Long managerId, Map<String, Object> map) {
        fillingEntsTimeToMap(map, managerId);
        //判断搜索条件是否存在
        List<Enterprise> listEnters = (List<Enterprise>) map.get("enters");
        if (listEnters == null || listEnters.size() <= 0) {
            return new ArrayList<DailyStatisticResult>();
        }
        return dailyStatisticMapper.getPlatformChargeTypeDate(map);
    }

    @Override
    public List<DailyStatisticResult> getPlatformChargeSuccessDate(Long managerId, Map<String, Object> map) {
        fillingEntsTimeToMap(map, managerId);
        //判断搜索条件是否存在
        List<Enterprise> listEnters = (List<Enterprise>) map.get("enters");
        if (listEnters == null || listEnters.size() <= 0) {
            return new ArrayList<DailyStatisticResult>();
        }
        return dailyStatisticMapper.getPlatformChargeSuccessDate(map);
    }

    @Override
    public List<DailyStatisticResult> getPlatformChargeMoneyDate(Long managerId, Map<String, Object> map) {
        fillingEntsTimeToMap(map, managerId);
        //判断搜索条件是否存在
        List<Enterprise> listEnters = (List<Enterprise>) map.get("enters");
        if (listEnters == null || listEnters.size() <= 0) {
            return new ArrayList<DailyStatisticResult>();
        }
        return dailyStatisticMapper.getPlatformChargeMoneyDate(map);
    }

    @Override
    public List<DailyStatistic> getPlatformKeyDate(Long managerId, Map<String, Object> map) {
        fillingEntsTimeToMap(map, managerId);
        //判断搜索条件是否存在
        List<Enterprise> listEnters = (List<Enterprise>) map.get("enters");
        if (listEnters == null || listEnters.size() <= 0) {
            return new ArrayList<DailyStatistic>();
        }
        return dailyStatisticMapper.getPlatformKeyDate(map);
    }

    @Override
    public List<DailyStatistic> getPlatformData(Long managerId, Map<String, Object> map) {
        fillingEntsTimeToMap(map, managerId);
        //判断搜索条件是否存在
        List<Enterprise> listEnters = (List<Enterprise>) map.get("enters");
        if (listEnters == null || listEnters.size() <= 0) {
            return new ArrayList<DailyStatistic>();
        }
        return dailyStatisticMapper.getPlatformData(map);
    }

    @Override
    public Integer countGetPlatformData(Long managerId, Map<String, Object> map) {
        fillingEntsTimeToMap(map, managerId);
        //判断搜索条件是否存在
        List<Enterprise> listEnters = (List<Enterprise>) map.get("enters");
        if (listEnters == null || listEnters.size() <= 0) {
            return 0;
        }
        return dailyStatisticMapper.countGetPlatformData(map);
    }

    @Override
    public DailyStatistic getSumPlatformData(Long managerId, Map<String, Object> map) {
        fillingEntsTimeToMap(map, managerId);
        //判断搜索条件是否存在
        List<Enterprise> listEnters = (List<Enterprise>) map.get("enters");
        if (listEnters == null || listEnters.size() <= 0) {
            return new DailyStatistic();
        }
        return dailyStatisticMapper.getSumPlatformData(map);
    }

    @Override
    public Integer countGetEnterformData(Long managerId, Map<String, Object> map) {
        fillingEntsTimeToMap(map, managerId);
        //判断搜索条件是否存在
        List<Enterprise> listEnters = (List<Enterprise>) map.get("enters");
        if (listEnters == null || listEnters.size() <= 0) {
            return 0;
        }
        return dailyStatisticMapper.countGetEnterformData(map);
    }

    @Override
    public List<DailyStatistic> getEnterformData(Long managerId, Map<String, Object> map) {
        fillingEntsTimeToMap(map, managerId);
        //判断搜索条件是否存在
        List<Enterprise> listEnters = (List<Enterprise>) map.get("enters");
        if (listEnters == null || listEnters.size() <= 0) {
            return new ArrayList<DailyStatistic>();
        }
        return dailyStatisticMapper.getEnterformData(map);
    }

    @Override
    public List<DailyStatistic> getPlatformDataByMap(Long managerId, Map<String, Object> map) {
        fillingEntsTimeToMap(map, managerId);
        //判断搜索条件是否存在
        List<Enterprise> listEnters = (List<Enterprise>) map.get("enters");
        if (listEnters == null || listEnters.size() <= 0) {
            return new ArrayList<DailyStatistic>();
        }
        return dailyStatisticMapper.getPlatformDataByMap(map);
    }

    @Override
    public DailyStatistic getSumPlatformDataByMap(Long managerId, Map<String, Object> map) {
        fillingEntsTimeToMap(map, managerId);
        //判断搜索条件是否存在
        List<Enterprise> listEnters = (List<Enterprise>) map.get("enters");
        if (listEnters == null || listEnters.size() <= 0) {
            return new DailyStatistic();
        }
        return dailyStatisticMapper.getSumPlatformDataByMap(map);
    }

    @Override
    public int insertDailyStatistic(String beginTime, String endTime) {
        return dailyStatisticMapper.insertDailyStatistic(beginTime, endTime);
    }

    @Override
    public List<DailyStatistic> getDailyStatisticBySize(String city, String chargeType, List<Enterprise> listEnters,
            Long prdId, List<Byte> productTypeList, Date startTime, Date endTime) {
        return dailyStatisticMapper.getDailyStatisticBySize(city, chargeType, listEnters, prdId, productTypeList,
                startTime, endTime);

    }

    @Override
    public List<DailyStatistic> getDailyStatisticGroupProCode(Map<String, Object> map) {
        return dailyStatisticMapper.getDailyStatisticGroupProCode(map);
    }

    @Override
    public Long countDailyStatisticGroupProCode(Map<String, Object> map) {
        return dailyStatisticMapper.countDailyStatisticGroupProCode(map);
    }

    @Override
    public List<DailyStatistic> getDailyStatisticGroupByCity(Map<String, Object> map) {
        return dailyStatisticMapper.getDailyStatisticGroupByCity(map);
    }

    @Override
    public Long countDailyStatisticGroupByCity(Map<String, Object> map) {
        return dailyStatisticMapper.countDailyStatisticGroupByCity(map);
    }

}
