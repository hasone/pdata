package com.cmcc.vrp.province.webin.controller;

import com.cmcc.vrp.province.model.WhiteList;
import com.cmcc.vrp.province.service.WhiteListService;
import com.cmcc.vrp.util.PageResult;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * WhiteListController.java
 */
@Controller
@RequestMapping("/manage/whiteList")
public class WhiteListController extends BaseController {
    private static Logger logger = Logger.getLogger(WhiteListController.class);

    @Autowired
    private WhiteListService whiteListService;

    /*
     * 白名单列表
     */
    /** 
     * @Title: index 
     * @author wujiamin
    */
    @RequestMapping("/index")
    public String index(ModelMap modelMap, QueryObject queryObject) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 查询参数: 手机号码
         */
        setQueryParameter("mobile", queryObject);

        modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 

        // 数据库查找符合查询条件的个数
        Long count = whiteListService.count(queryObject);
        List<WhiteList> lists = whiteListService.query(queryObject);
        queryObject = QueryObject.filterQueryObject(queryObject);
        PageResult<WhiteList> pageResult = new PageResult<WhiteList>(
            queryObject, count, lists, "index.html");
        modelMap.addAttribute("pageResult", pageResult);

        return "whiteList/index.ftl";
    }

    /*
     * 删除用户
     */
    /** 
     * @Title: delete 
     * @author wujiamin
    */
    @RequestMapping("/delete")
    public String delete(ModelMap modelMap, QueryObject queryObject, Long id) {
        if (id == null) {
            modelMap.addAttribute("errorMsg", "无效的白名单ID！");
            return "error.ftl";
        }

        if (!whiteListService.delete(id)) {
            modelMap.addAttribute("errorMsg", "删除用户失败！");
            return "error.ftl";
        }

        return index(modelMap, queryObject);
    }

    /*
     * 保存
     */
    /** 
     * @Title: save 
     * @author wujiamin
    */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(ModelMap modelMap, String phones) {
        if (org.apache.commons.lang.StringUtils.isBlank(phones)) {
            modelMap.addAttribute("errorMsg", "缺少必要的数据!");
            return showAdd(modelMap, phones);
        }

        String[] phoneArray = null;
        phoneArray = phones.split(",");
        if (whiteListService.insertBatch(Arrays.asList(phoneArray))) {
            return "redirect:/manage/whiteList/index.html";
        }

        modelMap.addAttribute("phones", phones);
        modelMap.addAttribute("errorMsg", "批量插入白名单出错!");
        return showAdd(modelMap, phones);
    }

    /** 
     * @Title: showAdd 
     * @author wujiamin
    */
    @RequestMapping(value = "add")
    public String showAdd(ModelMap model, String phones) {
        List<String> list = new ArrayList<String>();
        changeStrToList(list, phones);
        model.addAttribute("phoneList", list);
        return "whiteList/add.ftl";
    }

    private void changeStrToList(List<String> list, String phones) {
        if (org.apache.commons.lang.StringUtils.isBlank(phones)) {
            return;
        }

        String[] array = phones.split(",");
        for (String temp : array) {
            list.add(temp.trim());
        }
    }

    /**
     * 上传被赠送人文件
     *
     * @param uploadContacts
     * @return
     */
    @RequestMapping(value = "/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             String phones, ModelMap model) {
        List<String> list = new ArrayList<String>();
        if (file == null || file.isEmpty()) {
            model.addAttribute("errorMsg", "请选择上传的文件");
        } else {// 处理上传的文件
            try {
                String fileSuffix = file.getOriginalFilename();
                if (fileSuffix != null) {
                    fileSuffix = fileSuffix.split("\\.")[fileSuffix
                        .split("\\.").length - 1];
                }
                if (fileSuffix != null && "txt".equals(fileSuffix)) {
                    readText(list, file.getInputStream());
                } else {
                    model.addAttribute("errorMsg", "只支持TXT文件");
                }
            } catch (IOException e) {
                logger.error("上传文件异常", e);
                model.addAttribute("errorMsg", "上传文件失败");
            }
        }
        changeStrToList(list, phones);
        model.addAttribute("phoneList", list);
        return "whiteList/add.ftl";
    }

    // 解析Txt文件
    private void readText(List<String> list, InputStream inputStream)
        throws IOException {
        BufferedReader buReader = new BufferedReader(new InputStreamReader(
            inputStream));
        String in = null;
        while ((in = buReader.readLine()) != null) {
            if (StringUtils.isValidMobile(in)) {
                list.add(in);
            }
        }
    }

}
