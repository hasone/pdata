package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.dao.AdminDistrictMapper;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.module.ChargeStatisticLineModule;
import com.cmcc.vrp.province.module.ChargeStatisticListModule;
import com.cmcc.vrp.province.service.AdminEnterService;
import com.cmcc.vrp.province.service.ChargeRecordQueryService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.InterfaceRecordService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.util.CSVUtil;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;

/**
 * 充值记录统计
 *
 * @author JamieWu
 */
@Controller
@RequestMapping("/manage/statisticCharge")
public class StatisticChargeController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(StatisticChargeController.class);
    @Autowired
    AdminDistrictMapper adminDistrictMapper;
    @Autowired
    ChargeRecordService chargeRecordService;
    @Autowired
    AdminEnterService adminEnterService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    InterfaceRecordService interfaceRecordService;
    @Autowired
    private ManagerService managerService;

    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    private ChargeRecordQueryService chargeRecordQueryService;

    /**
     * (四川全省运营人员用)充值记录详情列表
     */
    @RequestMapping("/provinceChargeListIndex")
    public String getProvinceChargeListIndex(ModelMap model, QueryObject queryObject) {
        Administer admin = getCurrentUser();
        if (admin == null) {
            return "redirect:/manage/user/login.html";
        }

        List<Manager> managers = managerService.selectByParentId(1L);
        if (managers == null) {
            return "redirect:/manage/user/login.html";
        }
        Long managerId = null;
        for (Manager manager : managers) {
            if (getProvinceManager().equals(manager.getRoleId().toString())) {
                managerId = manager.getId();
            }
        }

        model.addAttribute("managerId", managerId);

        return "statistic/chargeStatisticList.ftl";

    }

    /**
     * 充值记录详情列表
     */
    @RequestMapping("/chargeListIndex")
    public String getChargeListStatistics(ModelMap model, QueryObject queryObject) {
        Administer admin = getCurrentUser();
        if (admin == null) {
            return "redirect:/manage/user/login.html";
        }

        Long managerId;
        Manager manager = managerService.getManagerByAdminId(admin.getId());
        if (manager == null || (managerId = manager.getId()) == null) {
            return "redirect:/manage/user/login.html";
        }

        model.addAttribute("managerId", managerId);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        model.addAttribute("startTime", sdf.format(new DateTime(date).minusDays(6).toDate()));
        model.addAttribute("endTime", sdf.format(date));
        model.addAttribute("showStatus", showChargeRecordStatus());

        model.addAttribute("crowdfunding", getIsCrowdfundingPlatform());

        if (isSdEnvironment()) {
            model.addAttribute("isSdEnvironment", "true");
        }

        return "statistic/chargeStatisticList.ftl";

    }

    /**
     * 搜索充值记录列表
     */
    @RequestMapping("/listSearch")
    public void listSearch(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        setQueryParameter("startTime", queryObject);
        setQueryParameter("endTime", queryObject);
        setQueryParameter("mobile", queryObject);
        setQueryParameter("eName", queryObject);
        setQueryParameter("type", queryObject);
        setQueryParameter("managerId", queryObject, String.valueOf(getCurrentUserManager().getId()));
        setQueryParameter("status", queryObject);
        setQueryParameter("sdPrdType", queryObject);

        int count = 0;
        List<ChargeStatisticListModule> list = new ArrayList();
        String managerId = (String) queryObject.toMap().get("managerId");
        //判断当前用户是否有传入managerId的查看权限
        if (managerId != null
                && managerService.isParentManage(Long.parseLong(managerId), getCurrentUserManager().getId())) {
            count = chargeRecordService.statisticChargeListCount(queryObject);
            list = chargeRecordService.statisticChargeList(queryObject);
        }

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json");
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出充值记录列表CSV
     * @throws IOException 
     */
    @RequestMapping("/creatCSVfile")
    public void creatCSVfile(HttpServletRequest request, HttpServletResponse response, String mobile, String eName,
            String managerId, String startTime, String endTime, String type, String status, String sdPrdType)
            throws IOException {

        QueryObject queryObject = new QueryObject();

        queryObject.setPageNum(0);
        queryObject.setPageSize(0);
        queryObject.getQueryCriterias().put("needPage", false); //导出报表不添加页码限制

        if (StringUtils.isEmpty(managerId)) {
            Manager manager = managerService.getManagerByAdminId(getCurrentUser().getId());
            managerId = String.valueOf(manager.getId());
        }

        //判断当前用户是否有传入managerId的查看权限
        if (!managerService.isParentManage(Long.parseLong(managerId), getCurrentUserManager().getId())) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("请求异常");
            return;
        }

        queryObject.getQueryCriterias().put("managerId", managerId);
        queryObject.getQueryCriterias().put("startTime", startTime);
        queryObject.getQueryCriterias().put("endTime", endTime);
        queryObject.getQueryCriterias().put("mobile", mobile);
        queryObject.getQueryCriterias().put("eName", eName);
        queryObject.getQueryCriterias().put("type", type);
        queryObject.getQueryCriterias().put("status", status);
        queryObject.getQueryCriterias().put("sdPrdType", sdPrdType);

        //不选定日期，就只能导出最近六个月的数据
        if (endTime == null || startTime == null) {
            Date endDay = new Date();
            Date startDay = DateUtil.getAfterMonths(endDay, -6);
            queryObject.getQueryCriterias().put("startTime", startDay);
            queryObject.getQueryCriterias().put("endTime", endDay);
        }

        //是否需要显示充值失败信息
        boolean showStatus = showChargeRecordStatus();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        logger.info("导出文件功能：数据库搜索开始{}，搜索条件{}", format.format(new Date()),
                JSON.toJSONString(queryObject.getQueryCriterias()));

        List<ChargeStatisticListModule> list = chargeRecordService.statisticChargeList(queryObject);

        logger.info("导出文件功能：数据库搜索结束" + format.format(new Date()));

        List<String> title = new ArrayList<String>();
        //山东增增加“pkgseq”、“systemnum”两个字段，删除手机号码值
        String provinceFlag = getProvinceFlag();
        boolean isSd = false;
        if (!org.apache.commons.lang.StringUtils.isBlank(provinceFlag) && "sd".equalsIgnoreCase(provinceFlag)) {
            isSd = true;
            title.add("pkgseq");
            title.add("systemnum");
        }

        title.add("EC接口订单编码");
        title.add("企业名称");
        title.add("企业编码");
        title.add("地区");
        title.add("活动名称");
        title.add("活动类型");
        title.add("产品名称");

        //是否显示充值产品编码，重庆流量平台显示，其他省份默认不显示20171009 luozuwu
        boolean isShowProductCode = false;
        if (!org.apache.commons.lang.StringUtils.isBlank(provinceFlag) && "chongqing".equalsIgnoreCase(provinceFlag)) {
            isShowProductCode = true;
            title.add("集团产品号码");
        }
        if (includePriceInReport()) {
            title.add("产品价格");
        }
        if (!isSd) {//山东不显示手机号码
            title.add("手机号码");
        }
        title.add("充值时间");
        title.add("充值状态");

        if (showStatus) {
            title.add("充值信息");
        }

        List<String> rowList = new ArrayList<String>();

        for (ChargeStatisticListModule module : list) {
            if (isSd) {
                //显示pkgseq
                if (StringUtils.isEmpty(module.getBossReqSerialNum())) {
                    rowList.add("-");
                } else {
                    rowList.add("'" + module.getBossReqSerialNum());
                }
                //显示systemnum
                if (StringUtils.isEmpty(module.getSystemNum())) {
                    rowList.add("-");
                } else {
                    rowList.add("'" + module.getSystemNum());
                }
            }

            if (StringUtils.isEmpty(module.getSerialNum())) {
                rowList.add("-");
            } else {
                rowList.add("'" + module.getSerialNum());
            }
            rowList.add(module.getEName());
            rowList.add("'" + module.getECode());
            rowList.add(module.getFullDistrictName());
            rowList.add(module.getAName());
            rowList.add(module.getChargeType());
            rowList.add(module.getPName());

            if (isShowProductCode) {
                //重庆显示集团产品号码enterProCode，特征参数样例：{"productCode":"gl_mwsq_10M","enterProCode":"123456789","enterCode":"123000004444"}
                try {
                    String feature = module.getFeature();
                    JSONObject jo = JSONObject.parseObject(feature);
                    String enterProCode = jo.getString("enterProCode");
                    if (org.apache.commons.lang.StringUtils.isBlank(enterProCode)) {
                        rowList.add("-");
                    } else {
                        rowList.add(enterProCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    rowList.add("-");
                }
            }

            if (includePriceInReport()) {
                Double price = 0d;
                if (module.getPrice() != null) {
                    price = module.getPrice() / 100.0;
                }
                rowList.add(price.toString());
            }

            if (!isSd) {//山东不显示手机号码
                rowList.add(module.getMobile());
            }
            rowList.add("'" + module.getChargeTime());
            if (module.getChargeStatus() == 1) {
                rowList.add("待充值");
            }
            if (module.getChargeStatus() == 2) {
                rowList.add("已发送充值请求");
            }
            if (module.getChargeStatus() == 3) {
                rowList.add("充值成功");
            }
            if (module.getChargeStatus() == 4) {
                rowList.add("充值失败");
            }

            if (showStatus) {
                rowList.add(module.getChargeMsg());
            }

        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "chargeInfo.csv");
            byte[] b = new byte[100];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inputStream.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        logger.info("导出文件功能：传输到前台" + format.format(new Date()));
    }

    /**
     * 充值折线图统计
     */
    @RequestMapping("chargeLineIndex")
    public String getChargeLineIndex(ModelMap model, QueryObject queryObject) {
        Administer admin = getCurrentUser();
        if (admin == null) {
            return "redirect:/manage/user/login.html";
        }

        Manager manager = managerService.getManagerByAdminId(admin.getId());

        Long managerId;
        if (manager == null || (managerId = manager.getId()) == null) {
            return "redirect:/manage/user/login.html";
        }
        model.addAttribute("managerId", managerId);

        //根据省份标识设置参数
        setCqFlag(model);

        return "statistic/chargeLineStatistic.ftl";

    }

    /**
     * 根据条件获取充值统计的折线图数据
     */
    @RequestMapping(value = "/getChargeLineData")
    public void getEnterpriseLineData(HttpServletResponse res, QueryObject queryObject) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        setQueryParameter("startTime", queryObject);
        setQueryParameter("endTime", queryObject);
        setQueryParameter("mobile", queryObject);
        setQueryParameter("eName", queryObject);
        setQueryParameter("managerId", queryObject);

        List<ChargeStatisticLineModule> list = new ArrayList();
        //判断当前用户是否有传入managerId的查看权限
        String managerId = (String) queryObject.toMap().get("managerId");
        if (managerService.isParentManage(Long.parseLong(managerId), getCurrentUserManager().getId())) {
            list = chargeRecordService.statistictByChargeDay(queryObject);
        }

        try {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json");
            JSONObject json = new JSONObject();
            List<String> categories1 = new ArrayList<String>();
            List<Long> series1 = new ArrayList<Long>();
            Long chargeNum = parseEnterpriseLineDataNum(list, categories1, series1);
            json.put("categories1", categories1);
            json.put("series1", series1);

            List<String> categories2 = new ArrayList<String>();
            List<Double> series2 = new ArrayList<Double>();
            Double chargeM = parseEnterpriseLineDataCapacity(list, categories2, series2);
            json.put("categories2", categories2);
            json.put("series2", series2);

            List<String> categories3 = new ArrayList<String>();
            List<Double> series3 = new ArrayList<Double>();
            Double chargeMoney = parseEnterpriseLineDataMoney(list, categories3, series3);
            json.put("categories3", categories3);
            json.put("series3", series3);

            json.put("chargeNum", chargeNum);

            json.put("chargeM", chargeM);

            json.put("chargeMoney", chargeMoney);

            res.getWriter().write(json.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param res
     * @param queryObject
     * @throws IOException 
     */
    @RequestMapping(value = "/queryChargeResult")
    public void queryChargeResult(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap)
            throws IOException {
        String systemNum = request.getParameter("systemNum");
        String fingerprint = request.getParameter("fingerprint");

        logger.info("系统流水号-{}, fingerprint-{}", systemNum, fingerprint);
        ChargeRecord record = chargeRecordService.getRecordBySN(systemNum);
        if (record != null && record.getQueryTime() != null
                && DateUtil.getDateBeforeSeconds(new Date(), -1800).before(record.getQueryTime())) {

            modelMap.addAttribute("time", DateUtil.caculateSecond(record.getQueryTime(), new Date()) + "后再次查询");
            response.getWriter().write(JSON.toJSONString(modelMap));
            response.getWriter().flush();
            return;
        }
        try {
            chargeRecordService.updateQueryTime(systemNum, new Date());
            ChargeRecord chargeRecord = chargeRecordQueryService.queryStatusBySystemNun(systemNum);
            if (chargeRecord != null) {
                if (ChargeRecordStatus.COMPLETE.getCode().equals(chargeRecord.getStatus())) {
                    modelMap.addAttribute("success", "充值成功");
                } else if (ChargeRecordStatus.FAILED.getCode().equals(chargeRecord.getStatus())) {
                    modelMap.addAttribute("failed", "充值失败");
                } else {
                    modelMap.addAttribute("process", "请稍后再查");
                }
            }

            response.getWriter().write(JSON.toJSONString(modelMap));
            response.getWriter().flush();
            return;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 将数据库搜索的数据组装成能适应企业统计折线图的数据
     */
    Long parseEnterpriseLineDataNum(List<ChargeStatisticLineModule> list, List<String> categories, List<Long> series) {
        Long number = 0L;
        if (list != null) {
            for (ChargeStatisticLineModule m : list) {
                categories.add(m.getDate());
                series.add(m.getNumber());
                number = number + m.getNumber();
            }
        }
        return number;
    }

    Double parseEnterpriseLineDataCapacity(List<ChargeStatisticLineModule> list, List<String> categories,
            List<Double> series) {
        Double capacity = 0.0;
        if (list != null) {
            for (ChargeStatisticLineModule m : list) {
                categories.add(m.getDate());
                series.add(m.getCapacity());
                capacity = capacity + m.getCapacity();
            }
        }
        return capacity;
    }

    Double parseEnterpriseLineDataMoney(List<ChargeStatisticLineModule> list, List<String> categories,
            List<Double> series) {
        Double money = 0.0;
        if (list != null) {
            for (ChargeStatisticLineModule m : list) {
                categories.add(m.getDate());
                if (m.getMoney() == null) {
                    m.setMoney(0d);
                }
                series.add(m.getMoney());
                money = money + m.getMoney();
            }
        }
        BigDecimal bg = new BigDecimal(money);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    //根据当前的省份标识判断导出的报表中是否需要包含价格字段
    private boolean includePriceInReport() {
        //目前新疆是不需要的,可按需要继续添加
        return !"xj".equals(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()));
    }

    private boolean getShowFailReasonToEntManager() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.SHOW_FAIL_REASON_TO_ENT_MANAGER.getKey());
        return org.apache.commons.lang.StringUtils.isNotBlank(value) && "YES".equalsIgnoreCase(value);
    }

    //是否要展示充值状态信息
    private boolean showChargeRecordStatus() {
        return getShowFailReasonToEntManager() || !isEnterpriseContactor(); //除了企业关键人,其它角色都要展示充值失败信息
    }

    /**
     * 判断是否山东环境
     */
    private boolean isSdEnvironment() {
        return "sd".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()));
    }
}
