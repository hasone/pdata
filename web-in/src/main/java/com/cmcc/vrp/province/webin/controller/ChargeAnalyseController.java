package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.enums.ChargeAnalyseType;
import com.cmcc.vrp.province.model.DailyStatistic;
import com.cmcc.vrp.province.model.DailyStatisticResult;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.DailyStatisticService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.SizeUnits;

/**
 * ChargeAnalyseController
 *
 */
@Controller
@RequestMapping("/manage/chargeAnalyse")
public class ChargeAnalyseController extends BaseController {
    @Autowired
    DailyStatisticService service;

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    DailyStatisticService dailyStatisticService;

    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * @param modelMap
     * @return
     */
    @RequestMapping("index")
    public String accountChangeIndex(ModelMap modelMap) {
        //填充角色可访问类型
        Manager manager = getCurrentUserManager();
        if (manager != null && manager.getRoleId() != null) {
            Set<Integer> types = ChargeAnalyseType.getTypesByRoleId(isCustomManager(), isCityManager(),
                    isProvinceManager() || isSuperAdmin());
            modelMap.addAttribute("permitTypes", types);
        }

        Date date = DateUtil.getDateBefore(new Date(), 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        modelMap.addAttribute("startTime", sdf.format(new DateTime(date).minusDays(6).toDate()));
        modelMap.addAttribute("endTime", sdf.format(date));
        modelMap.addAttribute("isShanDong", String.valueOf(isSdEnvironment()));
        modelMap.addAttribute("isXinJiang", String.valueOf(isXJEnvironment()));
        modelMap.addAttribute("isPlus", String.valueOf(isPlusEnvironment()));
        return "chargeStatistic/chargeAnalysis.ftl";
    }

    /**
     * 为了防止404，完成所有页面后之后请删除
     */
    @RequestMapping(value = "/getLineChartData")
    public void getLineChartData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> returnMap = new HashMap<String, String>();
        response.getWriter().write(JSON.toJSONString(returnMap));
    }

    /**
     * @param modelMap
     * @param request
     * @return
     * @throws IOException 
     */
    @RequestMapping(value = "/getEnterChargeCountData")
    public void getEnterChargeCountData(HttpServletRequest request, HttpServletResponse response, String startTime,
            String endTime, String eName) throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(startTime)) {
            params.put("startTime", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            params.put("endTime", endTime);
        }
        if (StringUtils.isNotBlank(eName)) {
            params.put("eName", eName);
        }

        //统计
        Manager manager = getCurrentUserManager();
        List<DailyStatisticResult> results = service.getEntSumDailyResult(manager.getId(), params);

        putResultsToMap(results, resultMap);
        resultMap.put("unit", "个");
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    /**
     * getEnterTypeData
     */
    @RequestMapping(value = "/getEnterChargeRankData")
    public void getEnterChargeRankData(HttpServletRequest request, HttpServletResponse response, String startTime,
            String endTime, String eName) throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(startTime)) {
            params.put("startTime", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            params.put("endTime", endTime);
        }
        if (StringUtils.isNotBlank(eName)) {
            params.put("eName", eName);
        }

        //统计
        Manager manager = getCurrentUserManager();
        List<DailyStatisticResult> results = service.getEntSumSortResult(manager.getId(), params);
        if (results.size() > 5) {
            results = results.subList(0, 5);
        }

        putResultsToMap(results, resultMap);
        resultMap.put("unit", "个");
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    /**
     * getEnterTypeData
     */
    @RequestMapping(value = "/getEnterChargeSoldeData")
    public void getEnterChargeSoldeData(HttpServletRequest request, HttpServletResponse response, String startTime,
            String endTime, String eName) throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(startTime)) {
            params.put("startTime", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            params.put("endTime", endTime);
        }
        if (StringUtils.isNotBlank(eName)) {
            params.put("eName", eName);
        }

        //统计
        Manager manager = getCurrentUserManager();
        List<DailyStatisticResult> results = service.getEntSumSoldeResult(manager.getId(), params);

        putResultsToMap(results, resultMap);
        resultMap.put("unit", "元");
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    /**
     * getEnterTypeData
     */
    @RequestMapping(value = "/getEntPrdDistribuerData")
    public void getEntPrdDistribuerData(HttpServletRequest request, HttpServletResponse response, String startTime,
            String endTime, String eName) throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(startTime)) {
            params.put("startTime", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            params.put("endTime", endTime);
        }
        if (StringUtils.isNotBlank(eName)) {
            params.put("eName", eName);
        }
        //统计
        Manager manager = getCurrentUserManager();
        List<DailyStatisticResult> results = service.getEntPrdSizeDistribution(manager.getId(), params,
                ProductType.FLOW_PACKAGE);

        for (DailyStatisticResult result : results) {
            Long size = NumberUtils.toLong(result.getName());
            if (size > 0) {
                result.setName(size / 1024 + "M");
            }
        }

        resultMap.put("datas", results);
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    /**
     * getEnterTypeData
     */
    @RequestMapping(value = "/getEntPrdFlowData")
    public void getEntPrdFlowData(HttpServletRequest request, HttpServletResponse response, String startTime,
            String endTime, String eName) throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(startTime)) {
            params.put("startTime", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            params.put("endTime", endTime);
        }
        if (StringUtils.isNotBlank(eName)) {
            params.put("eName", eName);
        }

        //统计
        Manager manager = getCurrentUserManager();
        List<DailyStatisticResult> flowResults = service.getEntPrdSizeDistribution(manager.getId(), params,
                ProductType.FLOW_ACCOUNT);

        Map<String, DailyStatisticResult> dataMap = new HashMap<String, DailyStatisticResult>();

        for (DailyStatisticResult dailyStatistic : flowResults) {
            String key = partitinon(dailyStatistic.getProductSize());

            if (dataMap.containsKey(key)) {
                DailyStatisticResult result = dataMap.get(key);
                result.setValue(result.getValue() + dailyStatistic.getValue());
            } else {
                DailyStatisticResult initDailyStatistic = new DailyStatisticResult();
                initDailyStatistic.setName(key);
                initDailyStatistic.setValue(dailyStatistic.getValue());
                dataMap.put(key, initDailyStatistic);
            }

        }

        List<DailyStatisticResult> finalResults = new ArrayList<DailyStatisticResult>();
        for (DailyStatisticResult result : flowResults) {
            finalResults.add(result);
        }

        resultMap.put("datas", finalResults);
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    /**
     * getEnterTypeData
     */
    @RequestMapping(value = "/getEnterTypeData")
    public void getEnterTypeData(HttpServletRequest request, HttpServletResponse response, String startTime,
            String endTime, String eName) throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(startTime)) {
            params.put("startTime", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            params.put("endTime", endTime);
        }
        if (StringUtils.isNotBlank(eName)) {
            params.put("eName", eName);
        }

        //统计
        Manager manager = getCurrentUserManager();
        List<DailyStatisticResult> results = service.getEntChargeTypeResult(manager.getId(), params);

        if (results.size() > 5) {
            results = results.subList(0, 5);
        }

        putResultsToMap(results, resultMap);
        resultMap.put("unit", "个");
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    /**
     * putResultsToMap
     */
    private void putResultsToMap(List<DailyStatisticResult> results, Map<String, Object> resultMap) {
        List<String> listNames = new LinkedList<String>();
        List<Long> listValues = new LinkedList<Long>();

        for (DailyStatisticResult result : results) {
            listNames.add(result.getName());
            listValues.add(result.getValue());
        }

        resultMap.put("xAxisNames", listNames);
        resultMap.put("xAxisValues", listValues);
    }

    /**
     * 平台产品充值个数
     * @param request
     * @param response
     * @param startTime
     * @param endTime
     * @throws IOException 
     */
    @RequestMapping(value = "/getPlatformChargeCountDate")
    public void getPlatformChargeCountDate(HttpServletRequest request, HttpServletResponse response, String startTime,
            String endTime) throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(startTime)) {
            params.put("startTime", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            params.put("endTime", endTime);
        }

        //统计
        Manager manager = getCurrentUserManager();
        List<DailyStatisticResult> results = service.getPlatformChargeCountDate(manager.getId(), params);
        putResultsToMap(results, resultMap);
        resultMap.put("unit", "个");
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    /**
     * 平台产品充值总量
     * @param request
     * @param response
     * @param startTime
     * @param endTime
     * @throws IOException 
     */
    @RequestMapping(value = "/getPlatformChargeCapacityDate")
    public void getPlatformChargeCapacityDate(HttpServletRequest request, HttpServletResponse response,
            String startTime, String endTime) throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(startTime)) {
            params.put("startTime", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            params.put("endTime", endTime);
        }

        //统计
        Manager manager = getCurrentUserManager();
        List<DailyStatisticResult> results = service.getPlatformChargeCapacityDate(manager.getId(), params);
        putResultsToMap(results, resultMap);
        resultMap.put("unit", "M");
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    /**
     * 平台产品充值类型
     * @param request
     * @param response
     * @param startTime
     * @param endTime
     * @throws IOException 
     */
    @RequestMapping(value = "/getPlatformChargeTypeDate")
    public void getPlatformChargeTypeDate(HttpServletRequest request, HttpServletResponse response, String startTime,
            String endTime) throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(startTime)) {
            params.put("startTime", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            params.put("endTime", endTime);
        }

        //统计
        Manager manager = getCurrentUserManager();
        List<DailyStatisticResult> results = service.getPlatformChargeTypeDate(manager.getId(), params);
        putResultsToMap(results, resultMap);
        resultMap.put("unit", "个");
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    /**
     * 平台产品充值成功率
     * @param request
     * @param response
     * @param startTime
     * @param endTime
     * @throws IOException
     */
    @RequestMapping(value = "/getPlatformChargeSuccessDate")
    public void getPlatformChargeSuccessDate(HttpServletRequest request, HttpServletResponse response,
            String startTime, String endTime) throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(startTime)) {
            params.put("startTime", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            params.put("endTime", endTime);
        }

        //统计
        Manager manager = getCurrentUserManager();
        List<DailyStatisticResult> results = service.getPlatformChargeSuccessDate(manager.getId(), params);
        //putResultsToMap(results, resultMap);
        List<String> listNames = new LinkedList<String>();
        List<Double> listValues = new LinkedList<Double>();

        for (DailyStatisticResult result : results) {
            listNames.add(result.getName());
            Double value = (double) result.getValue();
            listValues.add(value / 100.0);
        }

        resultMap.put("xAxisNames", listNames);
        resultMap.put("xAxisValues", listValues);
        resultMap.put("unit", "%");
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    /**
     * 产品创收金额
     * @param request
     * @param response
     * @param startTime
     * @param endTime
     * @throws IOException
     */
    @RequestMapping(value = "/getPlatformChargeMoneyDate")
    public void getPlatformChargeMoneyDate(HttpServletRequest request, HttpServletResponse response, String startTime,
            String endTime) throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(startTime)) {
            params.put("startTime", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            params.put("endTime", endTime);
        }

        //统计
        Manager manager = getCurrentUserManager();
        List<DailyStatisticResult> results = service.getPlatformChargeMoneyDate(manager.getId(), params);
        putResultsToMap(results, resultMap);
        resultMap.put("unit", "元");
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    @RequestMapping(value = "/getPlatformKeyDate")
    public void getPlatformKeyDate(HttpServletRequest request, HttpServletResponse response, String startTime,
            String endTime, Long managerId, Byte productType, String eName) throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(startTime)) {
            params.put("startTime", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            params.put("endTime", endTime);
        }

        //统计
        if (managerId == null) {
            Manager manager = getCurrentUserManager();
            managerId = manager.getId();
        }

        //搜索条件：产品类型
        List<Byte> productTypeList = null;
        if (productType != null) {
            productTypeList = new ArrayList<Byte>();
            if (ProductType.FLOW_PACKAGE.getValue() == productType) {//流量包产品，包括普通流量包产品和预付流量包产品
                productTypeList.add(ProductType.FLOW_PACKAGE.getValue());
                productTypeList.add(ProductType.PRE_PAY_PRODUCT.getValue());
            } else {
                productTypeList.add(productType);
            }
            params.put("productTypeList", productTypeList);
        }

        //企业名称
        params.put("eName", eName);

        List<DailyStatistic> results = service.getPlatformKeyDate(managerId, params);
        resultMap.put("data", results);
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    /**
     * 
     * @Title: getProductKey 
     * @Description: 产品分析之关键指标
     * @param request
     * @param response
     * @param startTime
     * @param endTime
     * @throws IOException
     * @return: void
     */
    @RequestMapping(value = "/getProductKey")
    public void getProductKey(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime)
            throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(startTime)) {
            params.put("startTime", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            params.put("endTime", endTime);
        }

        //统计
        Manager manager = getCurrentUserManager();
        List<DailyStatisticResult> results = service.getPlatformChargeCapacityDate(manager.getId(), params);
        putResultsToMap(results, resultMap);
        resultMap.put("unit", "M");
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    /**
     * 
     * @Title: getProductCountLine 
     * @Description: 产品分析之充值个数
     * @param request
     * @param response
     * @param startTime
     * @param endTime
     * @throws IOException
     * @return: void
     * @throws ParseException 
     */
    @RequestMapping(value = "/getProductCountLine")
    public void getProductCountLine(HttpServletRequest request, HttpServletResponse response, String startTime,
            String endTime, Byte productType) throws IOException, ParseException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Date startTimeDate = null;
        Date entTimeDate = null;

        //搜索条件：起止时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(startTime)) {
            startTimeDate = sdf.parse(startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            entTimeDate = sdf.parse(endTime);
        }
        //搜索条件：产品类型
        List<Byte> productTypeList = null;
        if (productType != null) {
            productTypeList = new ArrayList<Byte>();
            if (ProductType.FLOW_PACKAGE.getValue() == productType) {//流量包产品，包括普通流量包产品和预付流量包产品
                productTypeList.add(ProductType.FLOW_PACKAGE.getValue());
                productTypeList.add(ProductType.PRE_PAY_PRODUCT.getValue());
            } else {
                productTypeList.add(productType);
            }
        }

        //统计
        Manager manager = getCurrentUserManager();
        List<Enterprise> listEnters = enterprisesService.getEnterByManagerId(manager.getId());

        List<String> listNames = new LinkedList<String>();//横轴：时间维度
        List<Long> listValues = new LinkedList<Long>();//纵轴：数量维度
        if (listEnters != null && listEnters.size() > 0) {
            List<DailyStatistic> results = dailyStatisticService.countSuccessCountByDate(null, null, listEnters, null,
                    productTypeList, startTimeDate, entTimeDate);
            for (DailyStatistic dailyStatistic : results) {
                listNames.add(sdf.format(dailyStatistic.getDate()));//充值时间，以天为单位
                listValues.add(dailyStatistic.getTotalSuccessCount());//充值额度,个
            }
        }

        resultMap.put("xAxisNames", listNames);
        resultMap.put("xAxisValues", listValues);
        resultMap.put("unit", "个");
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    /**
     * 
     * @Title: getProductCapacityLine 
     * @Description: 产品分析之充值总量
     * @param request
     * @param response
     * @param startTime
     * @param endTime
     * @throws IOException
     * @return: void
     * @throws ParseException 
     */
    @RequestMapping(value = "/getProductCapacityLine")
    public void getProductCapacityLine(HttpServletRequest request, HttpServletResponse response, String startTime,
            String endTime, Byte productType) throws IOException, ParseException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Date startTimeDate = null;
        Date entTimeDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(startTime)) {
            startTimeDate = sdf.parse(startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            entTimeDate = sdf.parse(endTime);
        }

        //搜索条件：产品类型
        List<Byte> productTypeList = null;
        if (productType != null) {
            productTypeList = new ArrayList<Byte>();
            if (ProductType.FLOW_PACKAGE.getValue() == productType) {//流量包产品，包括普通流量包产品和预付流量包产品
                productTypeList.add(ProductType.FLOW_PACKAGE.getValue());
                productTypeList.add(ProductType.PRE_PAY_PRODUCT.getValue());
            } else {
                productTypeList.add(productType);
            }
        }

        //统计
        Manager manager = getCurrentUserManager();
        List<String> listNames = new LinkedList<String>();//横轴：时间维度
        List<Long> listValues = new LinkedList<Long>();//纵轴：额度维度
        List<Enterprise> listEnters = enterprisesService.getEnterByManagerId(manager.getId());
        if (listEnters != null && listEnters.size() > 0) {
            List<DailyStatistic> results = dailyStatisticService.countSuccessCapacityByDate(null, null, listEnters,
                    null, productTypeList, startTimeDate, entTimeDate);

            for (DailyStatistic dailyStatistic : results) {
                listNames.add(sdf.format(dailyStatistic.getDate()));//充值时间，以天为单位
                listValues.add(SizeUnits.KB.toMB(dailyStatistic.getTotalSuccessCapacity()));//充值额度,单位
            }
        }
        resultMap.put("xAxisNames", listNames);
        resultMap.put("xAxisValues", listValues);
        resultMap.put("unit", "M");
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    /**
     * 
     * @Title: getProductMultiDistribution 
     * @Description: 产品分析之产品分布
     * @param request
     * @param response
     * @param startTime
     * @param endTime
     * @throws IOException
     * @throws ParseException
     * @return: void
     */
    @RequestMapping(value = "/getProductMultiDistribution")
    public void getProductMultiDistribution(HttpServletRequest request, HttpServletResponse response, String startTime,
            String endTime, Byte productType) throws IOException, ParseException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<String> titles = new LinkedList<String>();
        List<List<DailyStatisticResult>> datas = new LinkedList<List<DailyStatisticResult>>();
        //搜索条件
        Date startTimeDate = null;
        Date endTimeDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(startTime)) {
            startTimeDate = sdf.parse(startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            endTimeDate = sdf.parse(endTime);
        }

        //统计
        Manager manager = getCurrentUserManager();
        List<Enterprise> listEnters = enterprisesService.getEnterByManagerId(manager.getId());

        if (listEnters != null && listEnters.size() > 0) {
            //搜索条件：产品类型
            List<Byte> productTypeList = null;
            //产品分布图：山东显示流量池产品分布和流量包产品分布，新疆只显示流量池产品分布图、其他省份只显示流量包产品分布
            if (!isXJEnvironment() && (productType == null || ProductType.FLOW_PACKAGE.getValue() == productType)) {
                //流量包充值统计
                productTypeList = new ArrayList<Byte>();
                productTypeList.add(ProductType.FLOW_PACKAGE.getValue());
                productTypeList.add(ProductType.PRE_PAY_PRODUCT.getValue());
                List<DailyStatistic> packageResults = dailyStatisticService.getDailyStatisticBySize(null, null,
                        listEnters, null, productTypeList, startTimeDate, endTimeDate);
                List<DailyStatisticResult> packageDataList = new LinkedList<DailyStatisticResult>();
                //转换
                for (DailyStatistic dailyStatistic : packageResults) {
                    DailyStatisticResult data = new DailyStatisticResult();
                    data.setName(String.valueOf(SizeUnits.KB.toMB(dailyStatistic.getProductSize())) + "MB");
                    data.setValue(dailyStatistic.getTotalSuccessCount());
                    packageDataList.add(data);
                }

                if (packageDataList.size() > 0) {
                    titles.add("流量包产品分布情况");
                    datas.add(packageDataList);
                }

            }

            //流量池统计
            if ((isSdEnvironment() || isXJEnvironment())
                    && (productType == null || ProductType.FLOW_ACCOUNT.getValue() == productType)) {
                productTypeList = new ArrayList<Byte>();
                productTypeList.add(ProductType.FLOW_ACCOUNT.getValue());
                List<DailyStatistic> flowResults = dailyStatisticService.getDailyStatisticBySize(null, null,
                        listEnters, null, productTypeList, startTimeDate, endTimeDate);
                List<DailyStatisticResult> flowDataList = new LinkedList<DailyStatisticResult>();

                //划分区间
                HashMap<String, Long> dataMap = new HashMap<String, Long>();
                for (DailyStatistic dailyStatistic : flowResults) {
                    String key = partitinon(dailyStatistic.getProductSize());
                    Long value = dailyStatistic.getProductSize() * dailyStatistic.getTotalSuccessCount();
                    if (dataMap.containsKey(key)) {
                        value += dailyStatistic.getProductSize();
                    }
                    dataMap.put(key, value);
                }

                //格式转换
                for (Map.Entry<String, Long> entry : dataMap.entrySet()) {
                    DailyStatisticResult data = new DailyStatisticResult();
                    data.setName(entry.getKey());
                    data.setValue(entry.getValue());
                    flowDataList.add(data);
                }

                if (flowDataList.size() > 0) {
                    titles.add("流量池产品分布情况");
                    datas.add(flowDataList);
                }
            }
        }
        resultMap.put("titles", titles);
        resultMap.put("datas", datas);
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    /**
     * 
     * @Title: getRegionCountLine 
     * @Description: 地区分析之地区充值排行榜
     * @param request
     * @param response
     * @param startTime
     * @param endTime
     * @throws IOException
     * @return: void
     * @throws ParseException 
     */
    @RequestMapping(value = "/getRegionCountLine")
    public void getRegionCountLine(HttpServletRequest request, HttpServletResponse response, String startTime,
            String endTime, Long managerId) throws IOException, ParseException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Date startTimeDate = null;
        Date entTimeDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(startTime)) {
            startTimeDate = sdf.parse(startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            entTimeDate = sdf.parse(endTime);
        }

        //搜索条件：地区
        if (managerId == null) {
            Manager manager = getCurrentUserManager();
            managerId = manager.getId();
        }

        List<String> listNames = new LinkedList<String>();//横轴：地市信息
        List<Long> listValues = new LinkedList<Long>();//纵轴：个数

        List<Enterprise> listEnters = enterprisesService.getEnterByManagerId(managerId);
        if (listEnters != null && listEnters.size() > 0) {
            List<DailyStatistic> results = dailyStatisticService.countSuccessCountByCity(null, null, listEnters, null,
                    null, startTimeDate, entTimeDate);

            List<DailyStatisticResult> dataList = new LinkedList<DailyStatisticResult>();
            for (DailyStatistic dailyStatistic : results) {
                DailyStatisticResult data = new DailyStatisticResult();
                data.setName(dailyStatistic.getCity());//地市信息
                data.setValue(dailyStatistic.getTotalSuccessCount());//个数
                dataList.add(data);
            }

            java.util.Collections.sort(dataList);
            List<DailyStatisticResult> top5List = getTopDataFromResult(dataList, 5);

            for (DailyStatisticResult dailyStatisticResult : top5List) {
                listNames.add(dailyStatisticResult.getName());
                listValues.add(dailyStatisticResult.getValue());
            }
        }

        resultMap.put("xAxisNames", listNames);
        resultMap.put("xAxisValues", listValues);
        resultMap.put("unit", "个");
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    /**
     * 
     * @Title: getRegionMoneyLine 
     * @Description: 地区分析之地区充值折前价值排行榜（top5）
     * @param request
     * @param response
     * @param startTime
     * @param endTime
     * @throws IOException
     * @return: void
     * @throws ParseException 
     */
    @RequestMapping(value = "/getRegionMoneyLine")
    public void getRegionMoneyLine(HttpServletRequest request, HttpServletResponse response, String startTime,
            String endTime, Long managerId) throws IOException, ParseException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Date startTimeDate = null;
        Date entTimeDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(startTime)) {
            startTimeDate = sdf.parse(startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            entTimeDate = sdf.parse(endTime);
        }

        //搜索条件：地区
        if (managerId == null) {
            Manager manager = getCurrentUserManager();
            managerId = manager.getId();
        }

        List<String> listNames = new LinkedList<String>();//横轴
        List<Long> listValues = new LinkedList<Long>();//纵轴

        List<Enterprise> listEnters = enterprisesService.getEnterByManagerId(managerId);
        if (listEnters != null && listEnters.size() > 0) {
            List<DailyStatistic> results = dailyStatisticService.countSuccessMoneyByCity(null, null, listEnters, null,
                    null, startTimeDate, entTimeDate);
            List<DailyStatisticResult> dataList = new LinkedList<DailyStatisticResult>();
            for (DailyStatistic dailyStatistic : results) {
                DailyStatisticResult data = new DailyStatisticResult();
                data.setName(dailyStatistic.getCity());//地市信息
                data.setValue(dailyStatistic.getTotalSuccessMoney() / 100);//充值额度,单位分，转换为元
                dataList.add(data);
            }

            java.util.Collections.sort(dataList);
            List<DailyStatisticResult> top5List = getTopDataFromResult(dataList, 5);

            for (DailyStatisticResult dailyStatisticResult : top5List) {
                listNames.add(dailyStatisticResult.getName());
                listValues.add(dailyStatisticResult.getValue());
            }
        }

        resultMap.put("xAxisNames", listNames);
        resultMap.put("xAxisValues", listValues);
        resultMap.put("unit", "元");
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    /**
     * 
     * @Title: getRegionDistributionData 
     * @Description: 地区分析之产品分布数据
     * @param request
     * @param response
     * @param startTime
     * @param endTime
     * @throws IOException
     * @return: void
     * @throws ParseException 
     */
    @RequestMapping(value = "/getRegionDistributionData")
    public void getRegionDistributionData(HttpServletRequest request, HttpServletResponse response, String startTime,
            String endTime, Byte type, Long managerId) throws IOException, ParseException {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        //搜索条件
        Date startTimeDate = null;
        Date endTimeDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(startTime)) {
            startTimeDate = sdf.parse(startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            endTimeDate = sdf.parse(endTime);
        }

        //搜索条件：地区
        if (managerId == null) {
            Manager manager = getCurrentUserManager();
            managerId = manager.getId();
        }

        //搜索条件：产品类型
        List<Byte> productTypeList = null;
        if (type != null) {
            productTypeList = new ArrayList<Byte>();
            if (ProductType.FLOW_PACKAGE.getValue() == type) {//流量包产品，包括普通流量包产品和预付流量包产品
                productTypeList.add(ProductType.FLOW_PACKAGE.getValue());
                productTypeList.add(ProductType.PRE_PAY_PRODUCT.getValue());
            } else {
                productTypeList.add(type);
            }
        }

        List<Enterprise> listEnters = enterprisesService.getEnterByManagerId(managerId);
        Map<String, List<DailyStatisticResult>> dataMap = new HashMap<String, List<DailyStatisticResult>>();
        if (listEnters != null && listEnters.size() > 0) {
            List<DailyStatistic> results = dailyStatisticService.getDailyStatisticByCityAndSize(null, null, listEnters,
                    null, productTypeList, startTimeDate, endTimeDate);

            //汇总    
            if (type != null && ProductType.FLOW_ACCOUNT.getValue() == type) {
                Map<String, Long> partitonMap = new HashMap<String, Long>();
                for (DailyStatistic dailyStatistic : results) {
                    String key = dailyStatistic.getCity() + "#" + partitinon(dailyStatistic.getProductSize());
                    Long value = dailyStatistic.getTotalSuccessCount();
                    if (dataMap.containsKey(key)) {
                        value += dailyStatistic.getTotalSuccessCount();
                    }
                    partitonMap.put(key, value);
                }

                //格式转换
                for (Map.Entry<String, Long> entry : partitonMap.entrySet()) {

                    String city = entry.getKey().split("#")[0];
                    String partitionName = entry.getKey().split("#")[1];
                    Long value = entry.getValue();

                    List<DailyStatisticResult> dataList = null;
                    if (dataMap.containsKey(city)) {
                        dataList = dataMap.get(city);
                    } else {
                        dataList = new LinkedList<DailyStatisticResult>();
                    }
                    DailyStatisticResult data = new DailyStatisticResult();

                    data.setName(partitionName);
                    data.setValue(value);

                    dataList.add(data);
                    dataMap.put(city, dataList);
                }
            } else {
                for (DailyStatistic dailyStatistic : results) {
                    //地市信息
                    List<DailyStatisticResult> dataList = null;
                    if (dataMap.containsKey(dailyStatistic.getCity())) {
                        dataList = dataMap.get(dailyStatistic.getCity());
                    } else {
                        dataList = new LinkedList<DailyStatisticResult>();
                    }
                    DailyStatisticResult data = new DailyStatisticResult();

                    data.setName(String.valueOf(SizeUnits.KB.toMB(dailyStatistic.getProductSize())) + "MB");
                    data.setValue(dailyStatistic.getTotalSuccessCount());

                    dataList.add(data);
                    dataMap.put(dailyStatistic.getCity(), dataList);
                }
            }
        }

        //输出
        List<String> titles = new LinkedList<String>();
        List<List<DailyStatisticResult>> datas = new LinkedList<List<DailyStatisticResult>>();
        Map<String, List<DailyStatisticResult>> resultDataMap = getTopData(dataMap, 5);
        for (Map.Entry<String, List<DailyStatisticResult>> entry : resultDataMap.entrySet()) {
            titles.add(entry.getKey());
            datas.add(getTopDataFromResult(entry.getValue(), 5));
        }
        resultMap.put("titles", titles);
        resultMap.put("datas", datas);
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    /**
     * 
     * @Title: partitinon 
     * @Description: 根据大小进行分区
     * @param size
     * @return
     * @return: String
     */
    private String partitinon(Long size) {
        Long sizeMB = SizeUnits.KB.toMB(size);
        if (sizeMB >= 1024) {
            return "[1024M,+∞)";
        } else if (sizeMB >= 500 && sizeMB < 1024) {
            return "[500M,1024M)";
        } else if (sizeMB >= 300 && sizeMB < 500) {
            return "[300M,500M)";
        } else if (sizeMB >= 100 && sizeMB < 300) {
            return "[100M,300M)";
        } else {
            return "(0,100M)";
        }
    }

    /**
     * 
     * @Title: getTopDataFromResult 
     * @Description: 后并后续多个元素
     * @param dataList
     * @param size
     * @return
     * @return: List<DailyStatisticResult>
     */
    private List<DailyStatisticResult> getTopDataFromResult(List<DailyStatisticResult> dataList, int size) {
        if (dataList == null || dataList.size() <= size) {
            return dataList;
        }

        //按从大到小排序
        java.util.Collections.sort(dataList);

        //前n-1个不变，后续的合并为一个
        List<DailyStatisticResult> dailyStatisticResults = dataList.subList(0, size - 1);

        Long value = 0L;
        for (int i = size - 1; i < dataList.size(); i++) {
            value += dataList.get(i).getValue();
        }
        DailyStatisticResult lastData = new DailyStatisticResult();
        lastData.setName("其他");
        lastData.setValue(value);
        dailyStatisticResults.add(lastData);
        return dailyStatisticResults;
    }

    /**
     * 
     * @Title: getTopData 
     * @Description: 降序排序
     * @param dataMap
     * @param size
     * @return
     * @return: Map<String,List<DailyStatisticResult>>
     */
    private Map<String, List<DailyStatisticResult>> getTopData(Map<String, List<DailyStatisticResult>> dataMap, int size) {

        Map<String, List<DailyStatisticResult>> resultMap = new LinkedHashMap<String, List<DailyStatisticResult>>();

        //1、按地市汇总
        Map<String, Long> totalMap = new TreeMap<String, Long>();
        for (Map.Entry<String, List<DailyStatisticResult>> entry : dataMap.entrySet()) {
            Long sum = 0L;
            List<DailyStatisticResult> resultList = entry.getValue();
            if (resultList != null && resultList.size() > 0) {
                for (DailyStatisticResult d : resultList) {
                    sum += d.getValue();
                }
            }
            totalMap.put(entry.getKey(), sum);
        }

        //2、按地市充值个数排序
        List<Map.Entry<String, Long>> list = new ArrayList<Map.Entry<String, Long>>(totalMap.entrySet());
        //然后通过比较器来实现排序
        java.util.Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
            //降序排序
            public int compare(Entry<String, Long> o1, Entry<String, Long> o2) {
                long result = o1.getValue().longValue() - o2.getValue().longValue();
                if (result > 0) {
                    return -1;
                } else if (result == 0) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        //3、取排名
        int i = 0;
        for (Map.Entry<String, Long> entry : list) {
            i++;
            if (i <= size) {//取值
                resultMap.put(entry.getKey(), dataMap.get(entry.getKey()));
            }
        }
        return resultMap;
    }

    
    /**
     * 判断是否山东环境
     */
    private boolean isSdEnvironment() {
        return "sd".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()));
    }

    /**
     * 判断是否新疆环境
     */
    private boolean isXJEnvironment() {
        return "xinjiang".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()));
    }

    /**
     * 判断是否新疆环境
     */
    private boolean isPlusEnvironment() {
        return "ziying".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()));
    }
}
