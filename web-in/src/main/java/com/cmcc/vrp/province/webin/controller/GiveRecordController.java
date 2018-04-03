package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.PresentRecord;
import com.cmcc.vrp.province.model.PresentRecordResult;
import com.cmcc.vrp.province.model.PresentRule;
import com.cmcc.vrp.province.service.AdminEnterService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.PresentRecordService;
import com.cmcc.vrp.province.service.PresentRuleService;
import com.cmcc.vrp.util.PageResult;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;
import jxl.Cell;
import jxl.JXLException;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 赠送记录管理
 *
 * @author kok
 */

@Controller
@RequestMapping("/manage/giveRecordManager")
public class GiveRecordController extends BaseController {

    private static Logger logger = Logger.getLogger(GiveRecordController.class);

    @Autowired
    PresentRecordService presentRecordService;

    @Autowired
    PresentRuleService presentRuleService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    private AdminEnterService adminEnterService;

    /** 
     * @Title: judge 
    */
    public static Boolean judge(String str) {
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher((CharSequence) str);
        return matcher.matches();
    }

    /** 
     * @Title: giveList 
    */
    @RequestMapping(value = "/giveList")
    public String giveList(HttpServletRequest request, Model model,
                           QueryObject queryObject) {

        setQueryParameter("ruleId", queryObject);

        int recoCount = presentRecordService.queryCount(queryObject);
        List<PresentRecordResult> recoList = presentRecordService.queryRecord(queryObject);

        queryObject = QueryObject.filterQueryObject(queryObject);
        PageResult<PresentRecordResult> pageResult = new PageResult<PresentRecordResult>(
            queryObject, recoCount, recoList, "giveList.html");

        model.addAttribute("pageResult", pageResult);
        model.addAttribute("recordStatus", ChargeRecordStatus.toMap());

        return "giveManage/giveList.ftl";
    }

    /** 
     * @Title: giveResult 
    */
    @RequestMapping(value = "/giveResult")
    public String giveResult(ModelMap modelMap, QueryObject queryObject, Long ruleId, Integer pageNum) {

        if (ruleId == null) {
            modelMap.addAttribute("errorMsg", "对不起，缺少相关字段");
            return "error.ftl";
        }

        PresentRule rule = presentRuleService.selectByPrimaryKey(ruleId);
        if (rule == null) {
            modelMap.addAttribute("errorMsg", "对不起，没有查到相关赠送规则");
            return "error.ftl";
        }

        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }

        //判断查找到的普通赠送对应的企业是否归该用户所有
        List<Long> enters = enterprisesService.getEnterpriseIdByAdminId(administer);
        if (enters == null || enters.size() <= 0 || !enters.contains(rule.getEntId())) {
            modelMap.addAttribute("errorMsg", "对不起，您没有权限查看赠送记录");
            return "error.ftl";
        }

        modelMap.addAttribute("recordStatus", ChargeRecordStatus.toMap());
        modelMap.addAttribute("ruleId", rule.getId());
        modelMap.addAttribute("provinceFlag", getProvinceFlag());

        return "giveManage/giveResult.ftl";
    }

    /** 
     * @Title: search 
    */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 获取并设置查询参数
         */
        setQueryParameter("ruleId", queryObject);
        setQueryParameter("mobile", queryObject);
        setQueryParameter("status", queryObject);


        int recoCount = presentRecordService.queryCountPlus(queryObject.toMap());

        Map map = queryObject.toMap();
        List<PresentRecordResult> recoList = presentRecordService.queryRecordPlus(map);

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", recoList);
        json.put("total", recoCount);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 解析Txt文件
    /** 
     * @Title: readText 
    */
    public void readText(List<String> list, InputStream inputStream)
        throws IOException {
        BufferedReader buReader = new BufferedReader(new InputStreamReader(
            inputStream));
        String in = null;
        while ((in = buReader.readLine()) != null) {
            for (String s : in.split(" ")) {
                if (judge(s)) {
                    list.add(s);
                }
            }
        }

    }

    // 解析xls文件
    /** 
     * @Title: readXls 
    */
    public void readXls(List<String> list, InputStream inputStream)
        throws IOException, JXLException {


        Workbook wb = Workbook.getWorkbook(inputStream); // 得到工作薄

        Sheet[] sts = wb.getSheets(); // 获得所有的工作表

        if (sts.length > 0) {
            Sheet sheet = sts[0];//只取第一个sheet
            int rsRows = sheet.getRows(); // 得到excel的总行数
            for (int i = 0; i < rsRows; i++) {
                Cell cell = sheet.getCell(0, i);// 得到工作表的第一个单元格,即A1
                //Cell cell1 = st.getCell(1, i);// 得到工作表的第二个单元格,即B1
                String mobile = cell.getContents();

                if (StringUtils.isValidMobile(mobile)) {
                    list.add(mobile);
                }

            }

        }



/*		BufferedReader buReader = new BufferedReader(new InputStreamReader(
                inputStream));
		String in = null;
		while ((in = buReader.readLine()) != null) {
			for (String s : in.split(" "))
				if (judge(s))
					list.add(s);
		}*/

    }

    /**
     * 上传被赠送人文件
     *
     * @return
     */
    @RequestMapping(value = "/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, String phones, Model model) {

        List<String> list = new ArrayList<String>();
        if (file == null || file.isEmpty()) {
            model.addAttribute("returnMsg", "请选择上传的文件");
        } else {// 处理上传的文件
            try {
                String fileSuffix = file.getOriginalFilename();
                if (fileSuffix != null) {
                    fileSuffix = fileSuffix.split("\\.")[fileSuffix
                        .split("\\.").length - 1];
                }
                if (fileSuffix != null && "txt".equals(fileSuffix)) {
                    readText(list, file.getInputStream());
                } else if (fileSuffix != null && "xls".equals(fileSuffix)) {
                    readXls(list, file.getInputStream());

                } else {
                    model.addAttribute("returnMsg", "只支持TXT和XLS文件");
                }
            } catch (IOException e) {
                logger.error("上传文件异常", e);
                model.addAttribute("returnMsg", "上传文件失败");
            } catch (JXLException e) {
                logger.error("解析xls错误", e);
                model.addAttribute("returnMsg", "上传的xls文件解析失败");
            }
        }
        // changeStrToList(list, phones);
        model.addAttribute("phoneList", list);
        return "manage/giveManage/phones";
    }

    /**
     * 再次充值，重新提交到队列中
     *
     * @param modelMap
     * @param id
     * @param ruleId
     * @return
     * @throws
     * @Title:chargeAgain
     * @Description: 再次充值，重新提交到队列中
     * @author: qihang
     */
    @RequestMapping(value = "/chargeAgain")
    public String chargeAgain(ModelMap modelMap, Long id, Long ruleId, Integer pageNum) {
        if (ruleId == null || id == null) {
            modelMap.addAttribute("errorMsg", "规则Id不能为空");
            return "error.ftl";
        }

        //判断是否是同一用户创建的规则
        if (!presentRuleService.isSameAdminCreated(getCurrentUser(), ruleId)) {
            modelMap.addAttribute("errorMsg", "对不起，该条规则不是由您创建，您无权进行再次充值操作");
            return "error.ftl";
        }

        if (!presentRuleService.chargeAgain(id)) {//提交到队列失败
            modelMap.addAttribute("errMsg", "对不起，本次重新充值请求失败，请再次尝试");
        }

        logger.info("用户ID:" + getCurrentUser().getId() + " 开始批量批量赠送重新充值" + "ruleId:" + ruleId + "recordId:" + id);

        return giveResult(modelMap, null, ruleId, pageNum);
        //return "redirect:/manage/giveRecordManager/giveResult.html?ruleId="+ruleId;
    }

    /** 
     * @Title: phones 
    */
    @RequestMapping(value = "/phones")
    public String phones(String phones, Model model) {
        List<String> list = new ArrayList<String>();
        changeStrToList(list, phones);
        model.addAttribute("phoneList", list);
        return "manage/giveManage/phones";
    }

    //查找某条抢红包记录的错误原因
    @RequestMapping("/getRecordInfoAjax")
    public void getRecordInfoAjax(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        if (id == null) {
            resp.getWriter().write("对不起，您传输的参数错误，请刷新后重新尝试");
        }

        PresentRecord record = presentRecordService.selectByRecordId(NumberUtils.toLong(id));
        if (record == null || record.getStatus() != 4) {
            resp.getWriter().write("对不起，系统没有找到相关记录，请刷新后重新尝试");
        } else {

            if (record.getErrorMessage().trim().length() == 0) {
                resp.getWriter().write("未知错误原因");
            } else {
                resp.getWriter().write(record.getErrorMessage().trim());
            }

        }
    }

    private void changeStrToList(List<String> list, String phones) {

        if (StringUtils.isEmpty(phones)) {
            return;
        }
        String[] array = phones.split(",");
        for (String temp : array) {
            list.add(temp);
        }
    }
}
