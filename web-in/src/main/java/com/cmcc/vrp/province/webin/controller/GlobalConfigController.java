package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.model.GlobalConfig;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.QueryObject;
import com.google.gson.Gson;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: GlobalConfigController
 * @Description: 全局变量控制类
 * @author: hexinxu
 * @date:2015年4月20日 下午
 */
@Controller
@RequestMapping("/manage/globalConfig")
public class GlobalConfigController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(GlobalConfigController.class);

    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * @param model
     * @param @return
     * @return String
     * @throws
     * @Title: index
     * @Description: 用于显示配置项主页
     */
    @RequestMapping(value = "/index")
    public String index(ModelMap modelMap, QueryObject queryObject) {
        
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        return "globalConfig/index.ftl";
    }

    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse response) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        setQueryParameter("configKey", queryObject);
        setQueryParameter("configName", queryObject);

        int configCount = globalConfigService.countGlobalConfig(queryObject.toMap());
        List<GlobalConfig> list = globalConfigService.selectGlobalConfigPage(queryObject.toMap());
        queryObject = QueryObject.filterQueryObject(queryObject);

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", configCount);
        json.put("queryObject", queryObject.toMap());
        try {
            StreamUtils.copy(json.toString(), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    /**
     * 添加或编辑配置项的数据库操作
     *
     * @param globalConfig
     * @param model
     */
    @RequestMapping(value = "addOrEditProduct", method = RequestMethod.POST)
    public String addOrEditGlobalConfig(Model model, GlobalConfig globalConfig) {
        // 参数校验
        String errorMsg = validate(globalConfig);
        if (errorMsg != null) {
            model.addAttribute("errorMsg", errorMsg);
            return showAddOrUpdateGlobalConfig(model, globalConfig);
        }

        int type = globalConfig.getId() == null ? 0 : 1;//0增加，1修改
        if (type == 0) {
            return create(globalConfig, model);
        } else {
            return update(globalConfig, model);
        }
    }

    //新建操作
    private String create(GlobalConfig globalConfig, Model model) {
        long userId = getCurrentUser().getId();

        globalConfig.setCreatorId(userId);
        globalConfig.setCreateTime(new Date());
        globalConfig.setUpdaterId(userId);
        globalConfig.setUpdateTime(new Date());
        globalConfig.setDeleteFlag(0);

        if (globalConfigService.insert(globalConfig)) {
            return "redirect:index.html";
        } else {
            model.addAttribute("errorMsg", "添加配置项" + globalConfig.getName() + "失败!");
            return showAddOrUpdateGlobalConfig(model, globalConfig);
        }
    }

    //更新操作
    private String update(GlobalConfig globalConfig, Model model) {
        long userId = getCurrentUser().getId();
        globalConfig.setUpdaterId(userId);
        globalConfig.setUpdateTime(new Date());

        if (globalConfigService.updateByPrimaryKeySelective(globalConfig)) {
            return "redirect:index.html";
        } else {
            model.addAttribute("errorMsg", "更改配置项" + globalConfig.getName() + "失败!");
            return showAddOrUpdateGlobalConfig(model, globalConfig);
        }
    }

    /**
     * 显示页面:添加或编辑配置项
     *
     * @param globalConfig
     * @param model
     */
    @RequestMapping(value = "/showAddOrUpdateGlobalConfig", method = RequestMethod.GET)
    public String showAddOrUpdateGlobalConfig(Model model, GlobalConfig globalConfig) {
        if (globalConfig != null && globalConfig.getId() != null) {
            globalConfig = globalConfigService.get(globalConfig.getId());
            model.addAttribute("globalConfig", globalConfig);
        }

        return "globalConfig/add_or_edit.ftl";
    }

    @RequestMapping(value = "/delete")
    public void delete(long id, HttpServletResponse response) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        if (globalConfigService.delete(id)) {
            map.put("result", true);
        } else {
            map.put("result", false);
        }

        try {
            StreamUtils.copy(new Gson().toJson(map), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    /**
     * 验证增加或修改配置项时验证每一项是否正确
     */
    @RequestMapping(value = "/check")
    public void check(HttpServletRequest request, HttpServletResponse resp) {
        String szCurrentName = getStringFromRequest(request, "name");
        String szCurrentCode = getStringFromRequest(request, "configKey");
        String szCurrentId = getStringFromRequest(request, "id");

        Boolean bFlag = true;
        List<GlobalConfig> globals = globalConfigService.get();
        for (GlobalConfig global : globals) {
            if (global.getDeleteFlag() == Constants.DELETE_FLAG.DELETED.getValue() //配置已删除
                || (szCurrentId != null && global.getId().toString().equals(szCurrentId))) {  //或者是当前正在修改的配置
                continue;
            }

            if (global.getConfigKey().equalsIgnoreCase(szCurrentCode)
                || global.getName().equalsIgnoreCase(szCurrentName)) {
                bFlag = false;
                break;
            }
        }

        try {
            resp.getWriter().write(bFlag.toString());
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    private String getStringFromRequest(HttpServletRequest request,
                                        String parameterName) {
        if (request == null || StringUtils.isBlank(parameterName)) {
            return null;
        }

        return request.getParameter(parameterName);
    }

    private String validate(GlobalConfig globalConfig) {
        try {
            globalConfigService.validate(globalConfig);
            return null;
        } catch (InvalidParameterException e) {
            return e.getMessage();
        }
    }
}
