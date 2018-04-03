/**
 *
 */
package com.cmcc.vrp.individual.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.IndividualProductMap;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.IndividualProductMapService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.webin.controller.BaseController;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;

import jxl.JXLException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title:FlowcoinPresentController
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author xujue
 * @date 2016年9月26日
 */
@Controller
@RequestMapping("/individual/flowcoinpresent")
public class FlowcoinPresentController extends BaseController {

    /*
     * 上传文件大小的限制
     */
    public static final int MAX_FILE_SIZE = 3;// 单位MB
    /*
     * 设定上传最大记录数量为100个
     */
    public static final int UPLOAD_MAX_RECORDS = 100;
    private static final Logger logger = LoggerFactory
        .getLogger(FlowcoinPresentController.class);
    @Autowired
    IndividualProductService individualProductService;
    @Autowired
    IndividualProductMapService individualProductMapService;
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    ActivityInfoService activityInfoService;
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    @Autowired
    ActivityPrizeService activityPrizeService;

    /**
     * @param map
     * @return
     */
    @RequestMapping("presentIndex")
    public String purchaseFlowcoinIndex(ModelMap map) {

        Manager manager = getCurrentUserManager();
        if (manager == null || manager.getId() == null) {
            return getLoginAddress();
        }

        map.addAttribute("managerId", manager.getId());
        return "individual/flowcoinPresent/flowCoin_present.ftl";
    }

    /**
     * 赠送流量币
     *
     * @param map
     * @param coins
     * @return
     * @Title: presentCoins
     * @Author: xujue
     */
    @RequestMapping(method = RequestMethod.POST, value = "presentCoins")
    public void presentCoins(HttpServletRequest request,
                             HttpServletResponse response, String activityName,
                             Integer coins, String presentTel) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        Administer admin = getCurrentUser();
        if (admin == null) {
            logger.info("无法获取当前用户信息");
            map.put("success", "fail");
            map.put("errorMsg", "无法获取当前用户信息");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }
        if (coins == null) {
            map.put("success", "fail");
            map.put("errorMsg", "未填写流量币数量");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }

        IndividualProduct product = individualProductService
            .getFlowcoinProduct();
        IndividualProductMap ipm = individualProductMapService
            .getByAdminIdAndProductId(admin.getId(), product.getId());

        if (ipm != null) {
            // todo:查询boss侧流量币数量
            if (!activitiesService.insertIndividualPresentActivity(
                activityName, presentTel, coins, admin.getId(), product.getId(), admin.getMobilePhone())) {
                map.put("success", "fail");
                map.put("errorMsg", "赠送流量币失败！");
                response.getWriter().write(JSON.toJSONString(map));
                return;
            } else {
                map.put("success", "success");
                map.put("errorMsg", "赠送流量币成功！");
                response.getWriter().write(JSON.toJSONString(map));
                return;
            }

        } else {
            map.put("success", "fail");
            map.put("errorMsg", "用户没有流量币产品！");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }

    }

    /**
     * 上传手机号ajax
     *
     * @author xujue
     */
    @RequestMapping(method = RequestMethod.POST, value = "/uploadPhones")
    public void uploadPhones(HttpServletRequest request,
                             @RequestParam("file") MultipartFile file,
                             HttpServletResponse response) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        // 设置响应给前台内容的数据格式
        response.setContentType("text/plain; charset=UTF-8");
        List<String> phonesList = new ArrayList<String>(); // 正确的手机号
        InputStream is = null;
        if (file == null || file.isEmpty()) {
            map.put("errorMsg", "上传文件不能为空");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        } else if (file.getSize() > MAX_FILE_SIZE * 1024 * 1024) {
            map.put("errorMsg", "超过文件大小限制（" + MAX_FILE_SIZE + "MB）");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        } else {
            try {
                // PrintWriter out = response.getWriter();
                String fileSuffix = file.getOriginalFilename();
                if (fileSuffix != null) {
                    fileSuffix = fileSuffix.split("\\.")[fileSuffix
                        .split("\\.").length - 1];
                }
                File tempFile = File.createTempFile(new SimpleDateFormat(
                    "yyyyMMddHHmmss").format(new Date()), fileSuffix);
                file.transferTo(tempFile);
                is = new FileInputStream(tempFile);

                // int updatePhonesNum = 0;// 上传的电话数
                // // int validPhonesNum = 0;//合法的电话数
                //
                int phonesNum = 0;
                if (fileSuffix != null && "txt".equals(fileSuffix)) {
                    phonesNum = readText(phonesList, is);

                } else if (fileSuffix != null && "xlsx".equals(fileSuffix)) {
                    phonesNum = readXlsx(phonesList, is);
                } else if (fileSuffix != null && "xls".equals(fileSuffix)) {
                    phonesNum = readXls(phonesList, is);
                } else {
                    // 只支持文件
                    map.put("errorMsg", "请上传正确的文件格式!");
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }
                
                if(phonesNum>1000){
                    // 号码数量超过限制
                    map.put("errorMsg", "上传手机号码超过1000条，请重新上传!");
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }

                FileUtils.deleteQuietly(tempFile);
                // validPhonesNum = list.size();

                // String correctMobiles = toPhones(list);
                // String invalidMobiles = toPhones(invalidList);
                if ((phonesList.size()) > 0) {
                    map.put("successMsg", "success");
                    map.put("correctMobiles", phonesList);
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                } else {
                    map.put("successMsg", "fail");
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }

            } catch (IOException e) {
                logger.error("上传文件异常", e);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                logger.error("上传文件格式异常", e);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
    }

    /**
     * 流量币赠送列表
     *
     * @author xujue
     */
    @RequestMapping("search")
    public void search(QueryObject queryObject,
                       HttpServletResponse res, Long managerId) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 查询参数: 用户手机,状态,创建时间
         */
//	setQueryParameter("startDate", queryObject);
//	setQueryParameter("endDate", queryObject);
        // setQueryParameter("manageId", queryObject);

        queryObject.getQueryCriterias().put("managerId",
            getCurrentUser().getId().toString());
        queryObject.getQueryCriterias().put("type",
            ActivityType.FLOWCOIN_PRESENT.getCode().toString());

        if (!StringUtils.isEmpty(getRequest().getParameter("startDate"))) {
            queryObject.getQueryCriterias().put("startTime",
                getRequest().getParameter("startDate"));
            queryObject.getQueryCriterias().put("endTime",
                getRequest().getParameter("endDate") + " 23:59:59");
        }

        queryObject.getQueryCriterias().put("creatorId", getCurrentUser().getId());
        
        List<Activities> records = activitiesService.selectByMap(queryObject
            .toMap());
        //为记录注入赠送的流量币个数
        if(records!=null && records.size()>1){
            for(Activities record : records){
                List<ActivityPrize> prizes = activityPrizeService.selectByActivityIdForIndividual(record.getActivityId());
                if(prizes!=null && prizes.size() == 1){
                    record.setProductSize(prizes.get(0).getSize());
                }
            }
        }
        Long count = activitiesService.countByMap(queryObject.toMap());

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", records);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param map
     * @param activityId
     * @return
     */
    @RequestMapping("detail")
    public String detail(ModelMap map, String activityId) {

        Activities activities = activitiesService.selectByActivityId(activityId);
        ActivityInfo activityInfo = activityInfoService.selectByActivityId(activityId);
        ActivityPrize activityPrize = activityPrizeService.selectByActivityIdForIndividual(activityId).get(0);

        map.addAttribute("activities", activities);
        map.addAttribute("activityInfo", activityInfo);
        map.addAttribute("activityId", activityId);
        map.addAttribute("activityPrize", activityPrize);
        return "individual/flowcoinPresent/flowCoin_present_detail.ftl";
    }

    /**
     * 流量币赠送记录列表
     *
     * @author xujue
     */
    @RequestMapping("searchRecords")
    public void searchRecords(QueryObject queryObject,
                              HttpServletResponse res, String activityId) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 查询参数: 充值手机号
         */
        setQueryParameter("chargeMobile", queryObject);
        queryObject.getQueryCriterias().put("activityId", activityId);

        List<ActivityWinRecord> activityWinRecords = activityWinRecordService.showForPageResultIndividualPresent(queryObject);
        int count = activityWinRecordService.showForPageResultCountIndividualPresent(queryObject);

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", activityWinRecords);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 解析Txt文件,返回成功总行数
     *
     * @param list 正确的手机号
     */
    public int readText(List<String> list, InputStream inputStream)
        throws IOException {
        BufferedReader buReader = new BufferedReader(new InputStreamReader(
            inputStream));
        String in = null;
        // int rowNums = 0;
        while ((in = buReader.readLine()) != null) {
            in = in.trim();
            // rowNums++;
            if (judge(in)) {
                list.add(in);
            }
        }
        return list != null && list.size() > 0 ? list.size() : 0;
    }

    @SuppressWarnings("resource")
    private int readXlsx(List<String> list, InputStream in) throws Exception,
        JXLException {
        org.apache.poi.ss.usermodel.Workbook wb = null;// 获取Excel文件对象
        wb = new XSSFWorkbook(in); // Excel 2007
        // 读取第一张表格内容
        XSSFSheet sheet = (XSSFSheet) wb.getSheetAt(0);
        XSSFRow row = null;
        int realAddCnt = 0;
        for (int i = 0; i <= sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row != null) {
        	org.apache.poi.ss.usermodel.Cell cell1 = row.getCell(0);
                if (cell1.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC) {
                    // 使用DecimalFormat类对科学计数法格式的数字进行格式化
                    BigDecimal bd = new BigDecimal(cell1.getNumericCellValue());
                    String phone = bd.toPlainString();
                    if (judge(phone)) {
                        list.add(phone);
                        realAddCnt++;
                    }
                }
            }
        }
        return realAddCnt;
    }

    @SuppressWarnings("resource")
    private int readXls(List<String> list, InputStream in) throws Exception,
        JXLException {
        org.apache.poi.ss.usermodel.Workbook wb = null;// 获取Excel文件对象
        wb = (org.apache.poi.ss.usermodel.Workbook) new HSSFWorkbook(in);
        // 读取第一张表格内容
        org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheetAt(0);
        Row row = null;
        int realAddCnt = 0;
        for (int i = 0; i <= sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row != null) {
        	org.apache.poi.ss.usermodel.Cell cell1 = row.getCell(0);
                if (cell1.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC) {
                    // 使用DecimalFormat类对科学计数法格式的数字进行格式化
                    BigDecimal bd = new BigDecimal(cell1.getNumericCellValue());
                    String phone = bd.toPlainString();
                    if (judge(phone)) {
                        list.add(phone);
                        realAddCnt++;
                    }
                }
            }
            
        }
        return realAddCnt;
    }

    private boolean judge(String str) {
        return com.cmcc.vrp.util.StringUtils.isValidMobile(str);
    }

}
