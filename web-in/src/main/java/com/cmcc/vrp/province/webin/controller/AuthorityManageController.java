package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.model.Authority;
import com.cmcc.vrp.province.service.AuthorityService;
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

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Title:AuthorityManagerController </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年8月18日
 */
@Controller
@RequestMapping("/manage/authorityManage")
public class AuthorityManageController extends BaseController {
    @Autowired
    AuthorityService authorityService;
    private Logger logger = LoggerFactory.getLogger(AuthorityManageController.class);

    /**
     * @param model
     * @return String
     * @throws
     * @Title: index
     * @Description: 用于显示权限列表
     */
    @RequestMapping(value = "/index")
    public String index(QueryObject queryObject, ModelMap modelmap) {
        if(queryObject != null){
            modelmap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        return "authorityManage/index.ftl";
    }

    /** 
     * @Title: search 
     * @param queryObject
     * @param response
    */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse response) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        setQueryParameter("name", queryObject);
        setQueryParameter("authorityName", queryObject);

        int configCount = authorityService.countAuthority(queryObject.toMap());
        List<Authority> list = authorityService.selectAuthorityPage(queryObject.toMap());
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
     * 显示页面:添加或编辑权限
     *
     * @param globalConfig
     * @param model
     */
    @RequestMapping(value = "/AddOrUpdateAuthority")
    public String addOrUpdateAuthority(Model model, Authority authority) {
        if (authority != null && authority.getAuthorityId() != null) {
            authority = authorityService.get(authority.getAuthorityId());
            model.addAttribute("authority", authority);
        }

        return "authorityManage/addOrEdit.ftl";
    }

    /**
     * 添加或编辑配置项的数据库操作
     *
     * @param globalConfig
     * @param model
     */
    @RequestMapping(value = "/saveAuthority", method = RequestMethod.POST)
    public String saveAuthority(Model model, Authority authority) {
        // 参数校验
        if (validate(authority)) {
            model.addAttribute("errorMsg", "必填项不能为空");
            return addOrUpdateAuthority(model, authority);
        }

        if (!authority.getAuthorityName().substring(0, 5).equalsIgnoreCase("Role_")) {
            model.addAttribute("errorMsg", "权限access必须以ROLE_开头");
            return addOrUpdateAuthority(model, authority);
        }
        //唯一性校验
        if (!authorityService.uniqueAuthority(authority)) {
            model.addAttribute("errorMsg", "已存在相同的权限access或编码");
            return addOrUpdateAuthority(model, authority);
        }

        authority.setAuthorityName(authority.getAuthorityName().toUpperCase());
        int type = authority.getAuthorityId() == null ? 0 : 1;//0增加，1修改
        if (type == 0) {
            return create(authority, model);
        } else {
            return update(authority, model);
        }
    }

    /** 
     * @Title: delete 
    */
    @RequestMapping(value = "/delete")
    public void delete(long authorityId, HttpServletResponse response) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        if (authorityService.deleteAuthorityById(authorityId)) {
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

    //新建操作
    /** 
     * @Title: create 
    */
    private String create(Authority authority, Model model) {
        Long userId = getCurrentUser().getId();

        authority.setCreator(userId.toString());
        authority.setCreateTime(new Date());
        authority.setUpdateUser(userId.toString());
        authority.setUpdateTime(new Date());
        authority.setDeleteFlag(0);

        if (authorityService.insert(authority)) {
            return "redirect:index.html";
        } else {
            model.addAttribute("errorMsg", "添加权限" + authority.getName() + "失败!");
            return addOrUpdateAuthority(model, authority);
        }
    }

    //更新操作
    /** 
     * @Title: update 
    */
    private String update(Authority authority, Model model) {
        Long userId = getCurrentUser().getId();
        authority.setUpdateUser(userId.toString());
        ;
        authority.setUpdateTime(new Date());

        if (authorityService.updateByPrimaryKeySelective(authority)) {
            return "redirect:index.html";
        } else {
            model.addAttribute("errorMsg", "更改权限" + authority.getName() + "失败!");
            return addOrUpdateAuthority(model, authority);
        }
    }

    private boolean validate(Authority authority) {
        return authority != null && StringUtils.isBlank(authority.getName())
            && StringUtils.isBlank(authority.getAuthorityName()) && StringUtils.isBlank(authority.getCode());            
    }

}
