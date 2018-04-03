package com.cmcc.vrp.province.webin.controller;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.cmcc.vrp.province.model.GlobalConfig;
import com.cmcc.vrp.province.model.json.EntPropsInfoConfig;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取企业开户所需字段信息的接口, 列出的字段均为需要显示的字段, 字段信息为以JSON格式表示, 多个字段以JSON数组形式表达, 字段信息包括:
 *
 * 1. 字段ID(需与前端页面保持一致)
 *
 * 2. 字段名称(用于前端显示使用)
 *
 * Created by sunyiwei on 2017/2/8.
 */
@Controller
@RequestMapping("/manage/entPropsInfo")
public class EntPropsInfoController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntPropsInfoController.class);

    //配置的KEY
    private final String CONFIG_KEY = GlobalConfigKeyEnum.ENT_PROPS_INFO.getKey();
    @Autowired
    private GlobalConfigService globalConfigService;

    @RequestMapping(value = "query", method = RequestMethod.GET)
    public void query(HttpServletResponse response) {
        String resp = globalConfigService.get(CONFIG_KEY);
        try {
            StreamUtils.copy(resp, Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("输出相应对象时出错， 错误信息为{}， 错误堆栈为{}.", e.getMessage(), e.getStackTrace());
        }
    }

    /**
     * 显示字段信息的编辑页面
     *
     * @return 编辑页面
     */
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String showEdit(ModelMap modelMap, QueryObject queryObject) {
        
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        
        //获取当前的配置值
        EntPropsInfoConfig entPropsInfo = getConfig();
        modelMap.put("config", entPropsInfo);
        modelMap.put("configStr", toJson(entPropsInfo));

        return "entPropsInfo/index.ftl";
    }

    /**
     * 保存企业开户时的字段信息
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public void saveEdit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        //获取旧配置
        String oldValue = globalConfigService.get(CONFIG_KEY);
        EntPropsInfoConfig oldConfig = getConfig();

        //新的配置
        try {
            //这里先转换成对象再转换回字符串,是为了保证json字符串中的字段属性顺序是一致的
            String newConfigStr = StreamUtils.copyToString(httpServletRequest.getInputStream(), Charsets.UTF_8);

            //没有配置的情况下新建,有的情况下更新
            boolean isSuccess = (oldConfig == null)
                    ? globalConfigService.insert(build(newConfigStr))
                    : globalConfigService.updateValue(CONFIG_KEY, newConfigStr, oldValue);

            LOGGER.info("{}企业开户配置信息时返回{}, 修改后的配置信息为{}.", oldConfig == null ? "新增" : "更新", isSuccess ? "成功" : "失败", newConfigStr);

            if (isSuccess) {
                Map<String, String> map = new LinkedHashMap<String, String>();
                map.put("success", String.valueOf(true));
                StreamUtils.copy(new Gson().toJson(map), Charsets.UTF_8, httpServletResponse.getOutputStream());
            }
        } catch (IOException e) {
            LOGGER.error("解析企业开户配置字段信息时出错", e);
        }
    }

    private GlobalConfig build(String value) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setName("企业开户需要的字段信息");
        globalConfig.setDescription("企业开户需要的字段信息");
        globalConfig.setConfigKey(CONFIG_KEY);
        globalConfig.setConfigValue(value);
        globalConfig.setCreateTime(new Date());
        globalConfig.setUpdateTime(new Date());

        Long userId = getCurrentAdminID();
        globalConfig.setCreatorId(userId);
        globalConfig.setUpdaterId(userId);
        globalConfig.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());

        return globalConfig;
    }

    private EntPropsInfoConfig getConfig() {
        return fromJson(globalConfigService.get(CONFIG_KEY));
    }

    private String toJson(EntPropsInfoConfig entPropsInfo) {
        return entPropsInfo == null ? "" : new Gson().toJson(entPropsInfo);
    }

    private EntPropsInfoConfig fromJson(String jsonStr) {
        return StringUtils.isBlank(jsonStr) ? null : new Gson().fromJson(jsonStr, EntPropsInfoConfig.class);
    }
}
