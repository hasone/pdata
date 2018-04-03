package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.model.LabelConfig;
import com.cmcc.vrp.province.service.LabelConfigService;
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
 * @ClassName: LabelConfigController
 * @Description: 标签变量控制类
 * @author: panxin
 * @date:2016年9月7日 下午
 */
@Controller
@RequestMapping("/manage/labelConfig")
public class LabelConfigController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(LabelConfigController.class);

    @Autowired
    private LabelConfigService labelConfigService;

    /**
     * @param model
     * @param @return
     * @return String
     * @throws
     * @Title: index
     * @Description: 用于显示配置项主页
     */
    @RequestMapping(value = "/index")
    public String index(QueryObject queryObject, ModelMap modelMap) {
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        return "labelConfig/index.ftl";
    }

    /**
     * @Title: search
     */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse response) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        setQueryParameter("configName", queryObject);
        setQueryParameter("defaultValue", queryObject, null);

        int configCount = labelConfigService.countLabelConfig(queryObject.toMap());
        List<LabelConfig> list = labelConfigService.selectLabelConfigPage(queryObject.toMap());
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
    public String addOrEditLabelConfig(Model model, LabelConfig labelConfig) {
        // 参数校验
        String errorMsg = validate(labelConfig);
        if (errorMsg != null) {
            model.addAttribute("errorMsg", errorMsg);
            return showAddOrUpdateLabelConfig(model, labelConfig);
        }

        int type = labelConfig.getId() == null ? 0 : 1;// 0增加，1修改
        if (type == 0) {
            return create(labelConfig, model);
        } else {
            return update(labelConfig, model);
        }
    }

    // 新建操作
    private String create(LabelConfig labelConfig, Model model) {
        long userId = getCurrentUser().getId();

        labelConfig.setCreatorId(userId);
        labelConfig.setCreateTime(new Date());
        labelConfig.setUpdaterId(userId);
        labelConfig.setUpdateTime(new Date());
        labelConfig.setDeleteFlag(0);

        if (labelConfigService.insert(labelConfig)) {
            return "redirect:index.html";
        } else {
            model.addAttribute("errorMsg", "添加标签项" + labelConfig.getName() + "失败!");
            return showAddOrUpdateLabelConfig(model, labelConfig);
        }
    }

    // 更新操作
    private String update(LabelConfig labelConfig, Model model) {
        long userId = getCurrentUser().getId();
        labelConfig.setUpdaterId(userId);
        labelConfig.setUpdateTime(new Date());

        if (labelConfigService.updateByPrimaryKeySelective(labelConfig)) {
            return "redirect:index.html";
        } else {
            model.addAttribute("errorMsg", "更改标签项" + labelConfig.getName() + "失败!");
            return showAddOrUpdateLabelConfig(model, labelConfig);
        }
    }

    /**
     * 显示页面:添加或编辑配置项
     *
     * @param globalConfig
     * @param model
     */
    @RequestMapping(value = "/showAddOrUpdateLabelConfig", method = RequestMethod.GET)
    public String showAddOrUpdateLabelConfig(Model model, LabelConfig labelConfig) {
        if (labelConfig != null && labelConfig.getId() != null) {
            labelConfig = labelConfigService.get(labelConfig.getId());
            model.addAttribute("labelConfig", labelConfig);
        }

        return "labelConfig/add_or_edit.ftl";
    }

    /** 
     * @Title: delete 
    */
    @RequestMapping(value = "/delete")
    public void delete(long id, HttpServletResponse response) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        if (labelConfigService.delete(id)) {
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
        String szCurrentId = getStringFromRequest(request, "id");

        Boolean bFlag = true;
        List<LabelConfig> labels = labelConfigService.get();
        for (LabelConfig label : labels) {
            if (szCurrentId != null && label.getId().toString().equals(szCurrentId)) {
                continue;
            }

            if (label.getName().equalsIgnoreCase(szCurrentName)) {
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

    private String getStringFromRequest(HttpServletRequest request, String parameterName) {
        if (request == null || StringUtils.isBlank(parameterName)) {
            return null;
        }

        return request.getParameter(parameterName);
    }

    private String validate(LabelConfig labelConfig) {
        try {
            labelConfigService.validate(labelConfig);
            return null;
        } catch (InvalidParameterException e) {
            return e.getMessage();
        }
    }
}
