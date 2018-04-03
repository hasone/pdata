package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.enums.ChargeStatisticType;
import com.cmcc.vrp.province.model.DailyStatistic;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.DailyStatisticService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.util.CSVUtil;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.SizeUnits;

/**
 * ChargeAnalyseController
 *
 */
@Controller
@RequestMapping("/manage/chargeStatisticsForm")
public class ChargeStatisticsFormController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChargeStatisticsFormController.class);
    @Autowired
    DailyStatisticService service;

    @Autowired
    EnterprisesService enterprisesService;

    /**
     * @param modelMap
     * @return
     */
    @RequestMapping("index")
    public String accountChangeIndex(ModelMap modelMap) {
        //填充角色可访问类型
        Manager manager = getCurrentUserManager();
        if (manager != null && manager.getRoleId() != null) {
            Set<Integer> types = ChargeStatisticType.getTypesByRoleId(isCustomManager(), isCityManager(),
                    isProvinceManager() || isSuperAdmin());
            modelMap.addAttribute("permitTypes", types);
        }

        Date date = DateUtil.getDateBefore(new Date(), 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        modelMap.addAttribute("startTime", sdf.format(new DateTime(date).minusDays(6).toDate()));
        modelMap.addAttribute("endTime", sdf.format(date));
        modelMap.addAttribute("provinceFlag", getProvinceFlag());//省份标识
        return "chargeStatistic/statisticsForm.ftl";
    }

    /**
     * getPlatformData
     */
    @RequestMapping("getPlatformData")
    public void getPlatformData(HttpServletRequest request, HttpServletResponse response, String startTime,
            String endTime, QueryObject queryObject) throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(startTime)) {
            setQueryParameter("startTime", queryObject);
        }
        if (StringUtils.isNotBlank(endTime)) {
            setQueryParameter("endTime", queryObject);
        }
        //统计
        Manager manager = getCurrentUserManager();
        List<DailyStatistic> results = service.getPlatformData(manager.getId(), queryObject.toMap());
        DailyStatistic sumResults = service.getSumPlatformData(manager.getId(), queryObject.toMap());
        if (sumResults == null) {
            sumResults = new DailyStatistic();
        }
        Integer count = service.countGetPlatformData(manager.getId(), queryObject.toMap());
        resultMap.put("data", results);
        resultMap.put("pageNum", queryObject.getPageNum());
        resultMap.put("pageSize", queryObject.getPageSize());
        resultMap.put("total", count);
        resultMap.put("sumSuccessCount", sumResults.getTotalSuccessCount());
        resultMap.put("sumSuccessCapacity", sumResults.getTotalSuccessCapacity());
        resultMap.put("sumSuccessMoney", sumResults.getTotalSuccessMoney());
        resultMap.put("queryObject", queryObject.toMap());
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    /**
     * getEnter
     */
    @RequestMapping("getEnterData")
    public void getEnterData(HttpServletRequest request, HttpServletResponse response, String startTime,
            String endTime, QueryObject queryObject) throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(startTime)) {
            setQueryParameter("startTime", queryObject);
        }
        if (StringUtils.isNotBlank(endTime)) {
            setQueryParameter("endTime", queryObject);
        }
        //统计
        Manager manager = getCurrentUserManager();
        List<DailyStatistic> results = service.getEnterformData(manager.getId(), queryObject.toMap());
        DailyStatistic sumResults = service.getSumPlatformData(manager.getId(), queryObject.toMap());
        if (sumResults == null) {
            sumResults = new DailyStatistic();
        }
        Integer count = service.countGetEnterformData(manager.getId(), queryObject.toMap());
        resultMap.put("data", results);
        resultMap.put("pageNum", queryObject.getPageNum());
        resultMap.put("pageSize", queryObject.getPageSize());
        resultMap.put("total", count);
        resultMap.put("sumSuccessCount", sumResults.getTotalSuccessCount());
        resultMap.put("sumSuccessCapacity", sumResults.getTotalSuccessCapacity());
        resultMap.put("sumSuccessMoney", sumResults.getTotalSuccessMoney());
        resultMap.put("queryObject", queryObject.toMap());
        response.getWriter().write(JSON.toJSONString(resultMap));
    }

    /**   
     * 
     * @Title: getRegionData 
     * @Description: 平台报表之地区报表
     * @param queryObject
     * @param res
     * @throws IOException
     * @return: void
     */
    @RequestMapping("getRegionData")
    public void getRegionData(QueryObject queryObject, HttpServletResponse res) throws IOException {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        setQueryParameter("startTime", queryObject);
        setQueryParameter("endTime", queryObject);

        //统计
        Manager manager = getCurrentUserManager();
        List<Enterprise> listEnters = enterprisesService.getEnterByManagerId(manager.getId());
        queryObject.getQueryCriterias().put("listEnters", listEnters);
        List<DailyStatistic> list = service.getDailyStatisticGroupByCity(queryObject.toMap());
        Long count = service.countDailyStatisticGroupByCity(queryObject.toMap());
        DailyStatistic sumResults = service.getSumPlatformData(manager.getId(), queryObject.toMap());
        if (sumResults == null) {
            sumResults = new DailyStatistic();
        }
        JSONObject json = new JSONObject();
        json.put("data", list);
        json.put("total", count);
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("sumSuccessCount", sumResults.getTotalSuccessCount());
        json.put("sumSuccessCapacity", sumResults.getTotalSuccessCapacity());
        json.put("sumSuccessMoney", sumResults.getTotalSuccessMoney());
        json.put("queryObject", queryObject.toMap());
        res.getWriter().write(json.toString());
    }

    /**
     * 
     * @Title: getProductData 
     * @Description: 平台报表之产品报表
     * @param queryObject
     * @param res
     * @throws IOException
     * @return: void
     */
    @RequestMapping("getProductData")
    public void getProductData(QueryObject queryObject, HttpServletResponse res) throws IOException {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        setQueryParameter("startTime", queryObject);
        setQueryParameter("endTime", queryObject);

        //统计
        Manager manager = getCurrentUserManager();
        List<Enterprise> listEnters = enterprisesService.getEnterByManagerId(manager.getId());
        queryObject.getQueryCriterias().put("listEnters", listEnters);
        List<DailyStatistic> list = service.getDailyStatisticGroupProCode(queryObject.toMap());
        Long count = service.countDailyStatisticGroupProCode(queryObject.toMap());

        DailyStatistic sumResults = service.getSumPlatformData(manager.getId(), queryObject.toMap());
        if (sumResults == null) {
            sumResults = new DailyStatistic();
        }

        JSONObject json = new JSONObject();
        json.put("data", list);
        json.put("total", count);
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("sumSuccessCount", sumResults.getTotalSuccessCount());
        json.put("sumSuccessCapacity", sumResults.getTotalSuccessCapacity());
        json.put("sumSuccessMoney", sumResults.getTotalSuccessMoney());
        json.put("queryObject", queryObject.toMap());
        res.getWriter().write(json.toString());
    }

    /**
     * 导出CSV
     */
    @RequestMapping("/creatCSVfile")
    public void creatPackageCSVfile(HttpServletRequest request, HttpServletResponse response, String startTime,
            String endTime, String type) {
        //统计
        Manager manager = getCurrentUserManager();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(startTime)) {
            paramMap.put("startTime", startTime);
        }
        if (StringUtils.isNotBlank(startTime)) {
            paramMap.put("endTime", endTime);
        }
        paramMap.put("managerId", manager.getId());
        if ("enter".equals(type)) {//企业报表报表导出
            List<DailyStatistic> results = service.getEnterformData(manager.getId(), paramMap);
            export(response, type, results, paramMap);
        }
        if ("product".equals(type)) {//产品分析报表导出            
            List<Enterprise> listEnters = enterprisesService.getEnterByManagerId(manager.getId());
            paramMap.put("listEnters", listEnters);
            List<DailyStatistic> results = service.getDailyStatisticGroupProCode(paramMap);
            export(response, type, results, paramMap);
        }
        if ("region".equals(type)) {//地区报表报表导出
            List<Enterprise> listEnters = enterprisesService.getEnterByManagerId(manager.getId());
            paramMap.put("listEnters", listEnters);
            List<DailyStatistic> results = service.getDailyStatisticGroupByCity(paramMap);
            export(response, type, results, paramMap);
        }

    }

    /**
     * 导出CSV
     */
    private void export(HttpServletResponse response, String type, List<DailyStatistic> results,
            Map<String, Object> queryMap) {
        List<String> title = new ArrayList<String>();
        List<String> rowList = new ArrayList<String>();
        String fileName = "report.csv";
        if ("enter".equals(type)) {
            fileName = "qiyechongzhitongjibiao.csv";
            fillingEnterDatasToList(title, rowList, results, queryMap);
        }
        if ("product".equals(type)) {
            fileName = "chanpinchongzhitongjibiao.csv";
            fillingProductDatasToList(title, rowList, results, queryMap);
        }
        if ("region".equals(type)) {
            fileName = "diquchognzhitongjibiao.csv";
            fillingRegionDatasToList(title, rowList, results, queryMap);
        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            byte[] b = new byte[100];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inputStream.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 填充企业数据
     */
    private void fillingEnterDatasToList(List<String> title, List<String> rowList, List<DailyStatistic> results,
            Map<String, Object> queryMap) {
        DailyStatistic sumResults = service.getSumPlatformDataByMap((Long) queryMap.get("managerId"), queryMap);
        String startTime = (String) queryMap.get("startTime");
        String endTime = (String) queryMap.get("endTime");
        title.add("充值时间");
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            title.add(startTime.substring(0, 10) + "~" + endTime.substring(0, 10));
        } else {
            title.add("");
        }
        title.add("");
        title.add("");
        title.add("");

        rowList.add("总计");
        if (sumResults == null) {
            rowList.add("-");
            rowList.add("-");
            rowList.add("-");
            rowList.add("-");
        } else {
            rowList.add("-");
            rowList.add(String.valueOf(sumResults.getTotalSuccessCount()));
            rowList.add(String.valueOf(sumResults.getTotalSuccessCapacity()));
            rowList.add(String.valueOf(sumResults.getTotalSuccessMoney()));
        }

        rowList.add("企业名称");
        rowList.add("集团编码");
        rowList.add("产品充值个数");
        rowList.add("产品充值总量（M)");
        rowList.add("折前价值（元）");

        for (DailyStatistic dailyStatistic : results) {
            if (StringUtils.isNotBlank(dailyStatistic.getEnterName())) {
                rowList.add(dailyStatistic.getEnterName());
            } else {
                rowList.add("-");
            }
            if (StringUtils.isNotBlank(dailyStatistic.getEnterCode())) {
                rowList.add(dailyStatistic.getEnterCode());
            } else {
                rowList.add("-");
            }

            if (dailyStatistic.getTotalSuccessCount() != null) {
                rowList.add(String.valueOf(dailyStatistic.getTotalSuccessCount()));
            } else {
                rowList.add("-");
            }

            if (dailyStatistic.getTotalSuccessCapacity() != null) {
                rowList.add(String.valueOf(dailyStatistic.getTotalSuccessCapacity()));
            } else {
                rowList.add("-");
            }

            if (dailyStatistic.getTotalSuccessMoney() != null) {
                rowList.add(String.valueOf(dailyStatistic.getTotalSuccessMoney()));
            } else {
                rowList.add("-");
            }

        }
    }

    /**
     * 
     * @Title: fillingProductDatasToList 
     * @Description: 填充产品报表导出字段
     * @param title
     * @param rowList
     * @param results
     * @return: void
     */
    private void fillingProductDatasToList(List<String> title, List<String> rowList, List<DailyStatistic> results,
            Map<String, Object> queryMap) {

        DailyStatistic sumResults = service.getSumPlatformDataByMap((Long) queryMap.get("managerId"), queryMap);
        String startTime = (String) queryMap.get("startTime");
        String endTime = (String) queryMap.get("endTime");
        title.add("充值时间");
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            title.add(startTime.substring(0, 10) + "~" + endTime.substring(0, 10));
        } else {
            title.add("");
        }
        title.add("");
        title.add("");
        title.add("");
        title.add("");

        rowList.add("总计");
        if (sumResults == null) {
            rowList.add("-");
            rowList.add("-");
            rowList.add("-");
            rowList.add("-");
            rowList.add("-");
        } else {
            rowList.add("-");
            rowList.add("-");
            rowList.add(String.valueOf(sumResults.getTotalSuccessCount()));
            rowList.add(String.valueOf(sumResults.getTotalSuccessCapacity()));
            rowList.add(String.valueOf(sumResults.getTotalSuccessMoney()));
        }

        rowList.add("产品名称");
        rowList.add("产品类型");
        rowList.add("产品大小");
        rowList.add("产品充值个数");
        rowList.add("产品充值总量（M)");
        rowList.add("折前价值（元）");

        for (DailyStatistic dailyStatistic : results) {
            if (StringUtils.isNotBlank(dailyStatistic.getProductName())) {
                rowList.add(dailyStatistic.getProductName());
            } else {
                rowList.add("-");
            }
            ProductType productType = ProductType.fromValue(dailyStatistic.getProductType().byteValue());
            if (productType != null) {
                rowList.add(productType.getDesc());
            } else {
                rowList.add("-");
            }

            if (dailyStatistic.getProductSize() != null) {
                rowList.add(String.valueOf(SizeUnits.KB.toMB(dailyStatistic.getProductSize())));
            } else {
                rowList.add("-");
            }

            if (dailyStatistic.getTotalSuccessCount() != null) {
                rowList.add(String.valueOf(dailyStatistic.getTotalSuccessCount()));
            } else {
                rowList.add("-");
            }

            if (dailyStatistic.getTotalSuccessCapacity() != null) {
                rowList.add(String.valueOf(SizeUnits.KB.toMB(dailyStatistic.getTotalSuccessCapacity())));//KB转为MB
            } else {
                rowList.add("-");
            }

            if (dailyStatistic.getTotalSuccessMoney() != null) {
                rowList.add(String.valueOf(dailyStatistic.getTotalSuccessMoney() / 100));//分转换为元
            } else {
                rowList.add("-");
            }
        }
    }

    /**
     * 
     * @Title: fillingRegionDatasToList 
     * @Description: 地区报表导出字段
     * @param title
     * @param rowList
     * @param results
     * @return: void
     */
    private void fillingRegionDatasToList(List<String> title, List<String> rowList, List<DailyStatistic> results,
            Map<String, Object> queryMap) {

        DailyStatistic sumResults = service.getSumPlatformDataByMap((Long) queryMap.get("managerId"), queryMap);
        String startTime = (String) queryMap.get("startTime");
        String endTime = (String) queryMap.get("endTime");
        title.add("充值时间");
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            title.add(startTime.substring(0, 10) + "~" + endTime.substring(0, 10));
        } else {
            title.add("");
        }
        title.add("");
        title.add("");

        rowList.add("总计");
        if (sumResults == null) {
            rowList.add("-");
            rowList.add("-");
            rowList.add("-");
        } else {
            rowList.add(String.valueOf(sumResults.getTotalSuccessCount()));
            rowList.add(String.valueOf(sumResults.getTotalSuccessCapacity()));
            rowList.add(String.valueOf(sumResults.getTotalSuccessMoney()));
        }

        rowList.add("地区");
        rowList.add("产品充值个数");
        rowList.add("产品充值总量（M)");
        rowList.add("折前价值（元）");

        for (DailyStatistic dailyStatistic : results) {
            if (StringUtils.isNotBlank(dailyStatistic.getCity())) {
                rowList.add(dailyStatistic.getCity());
            } else {
                rowList.add("-");
            }

            if (dailyStatistic.getTotalSuccessCount() != null) {
                rowList.add(String.valueOf(dailyStatistic.getTotalSuccessCount()));
            } else {
                rowList.add("-");
            }

            if (dailyStatistic.getTotalSuccessCapacity() != null) {
                rowList.add(String.valueOf(SizeUnits.KB.toMB(dailyStatistic.getTotalSuccessCapacity())));//KB转为MB
            } else {
                rowList.add("-");
            }

            if (dailyStatistic.getTotalSuccessMoney() != null) {
                rowList.add(String.valueOf(dailyStatistic.getTotalSuccessMoney() / 100));//分转换为元
            } else {
                rowList.add("-");
            }
        }
    }

    /**
     * 导出平台分析CSV
     */
    @RequestMapping("/creatPlatformCSVfile")
    public void creatPlatformCSVfile(HttpServletRequest request, HttpServletResponse response, String startTime,
            String endTime) {

        Map map = new HashMap();
        map.put("startTime", startTime);
        map.put("endTime", endTime);

        Manager manager = getCurrentUserManager();
        if (manager == null) {
            LOGGER.error("当前用户无管理员身份");
            return;
        }
        //统计
        List<DailyStatistic> results = service.getPlatformDataByMap(manager.getId(), map);
        DailyStatistic sumResults = service.getSumPlatformDataByMap(manager.getId(), map);

        List<String> title = new ArrayList<String>();
        title.add("充值时间");
        title.add(startTime + "~" + endTime);
        title.add("");
        title.add("");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> rowList = new ArrayList<String>();
        rowList.add("总计");
        if (sumResults == null) {
            rowList.add("-");
            rowList.add("-");
            rowList.add("-");
        } else {
            rowList.add(String.valueOf(sumResults.getTotalSuccessCount()));
            rowList.add(String.valueOf(sumResults.getTotalSuccessCapacity()));
            rowList.add(String.valueOf(sumResults.getTotalSuccessMoney()));
        }
        rowList.add("日期");
        rowList.add("产品充值个数");
        rowList.add("产品充值总量（M)");
        rowList.add("折前价值（元）");
        if (results != null) {
            for (DailyStatistic module : results) {
                if (module.getDate() != null) {
                    rowList.add(sdf.format(module.getDate()));
                } else {
                    rowList.add("");
                }

                if (module.getTotalSuccessCount() != null) {
                    rowList.add(String.valueOf(module.getTotalSuccessCount()));
                } else {
                    rowList.add("");
                }

                if (module.getTotalSuccessCapacity() != null) {
                    rowList.add(String.valueOf(module.getTotalSuccessCapacity()));
                } else {
                    rowList.add("");
                }

                if (module.getTotalSuccessMoney() != null) {
                    rowList.add(String.valueOf(module.getTotalSuccessMoney()));
                } else {
                    rowList.add("");
                }
            }
        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "pingtaichongzhitongjibiao.csv");
            byte[] b = new byte[100];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inputStream.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
