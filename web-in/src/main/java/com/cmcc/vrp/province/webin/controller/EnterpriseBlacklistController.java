/**
 * 
 */
package com.cmcc.vrp.province.webin.controller;


import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.EnterpriseBlacklist;
import com.cmcc.vrp.province.service.EnterpriseBlacklistService;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.SensitiveWordFilterUtil;
import com.cmcc.vrp.util.StringUtils;

/**
 *  @desc: 企业黑名单及关键词
 *  @author: wuguoping 
 *  @data: 2017年7月3日
 */
@Controller
@RequestMapping("/manage/blacklist")
public class EnterpriseBlacklistController extends BaseController {
    private static final Logger logger = Logger.getLogger(EnterpriseBlacklistController.class);
    
    @Autowired
    EnterpriseBlacklistService enterpriseBlacklistService;
    
    /**
     * 页面跳转至黑名单首页
     * title: index
     * desc: 
     * @return
     * wuguoping
     * 2017年7月4日
     */
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index(QueryObject queryObject, ModelMap modelMap) {
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        return "enterpriseBlacklist/index.ftl";
    } 
    
    /**
     * 企业黑名单列表
     * title: search
     * desc: 
     * @param queryObject
     * @param res
     * wuguoping
     * 2017年7月14日
     */
    @RequestMapping(value="search")
    public void search(QueryObject queryObject, HttpServletResponse res) throws Exception{
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        
        //这里是为了支持模糊搜索
        setQueryParameter("eName", queryObject);
        setQueryParameter("keyword", queryObject);
        
        Integer entBlacklistCount = enterpriseBlacklistService.getEntBlacklistCount(queryObject);
        if(entBlacklistCount == null){
            logger.error("entBlacklistCount = { NULL }");
          //  return;
        }
        List<EnterpriseBlacklist>  entBlacklists = enterpriseBlacklistService.getEntBlacklistForPage(queryObject);
        
        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", entBlacklists);
        json.put("total", entBlacklistCount);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            logger.error("写出出错  {}", e);
        }
    }
    
    /**
     * 页面跳转至添加黑名单页面
     * title: add
     * desc: 
     * @return
     * wuguoping
     * 2017年7月13日
     */
    @RequestMapping(value= "add" , method = RequestMethod.GET)
    public String add(){
        return "enterpriseBlacklist/add.ftl";
    }
    

    /**
     * 检查该企业名下关键词是否重复
     * title: check
     * desc: 
     * @param queryObject
     * @param response
     * wuguoping
     * 2017年7月13日
     */
    @RequestMapping(value = "check", method = RequestMethod.GET)
    @ResponseBody
    public void check(@RequestParam("eName") String entName,
            @RequestParam("keyword") String keyword,
            HttpServletResponse response) throws Exception{
        
        //1、检查是企业名称是否为空
        if(StringUtils.isEmpty(entName)){
            logger.error("无效的请求参数 entName = { NULL or ''}");
            return ;
        }
        //2、校验该企业名下关键词是否重复
        if(StringUtils.isEmpty(keyword)){
            logger.error("无效的请求参数 keyword = { NULL or ''}");
            return ;
        }
        JSONObject json = new JSONObject();
        if(enterpriseBlacklistService.isContainKeyword(entName, keyword)){
            json.put("status", "TRUE");
            response.getWriter().write(json.toJSONString());
        }else{
            json.put("status", "FALSE");
            response.getWriter().write(json.toJSONString());
        }
    }
    
    /**
     * 插入企业黑名单
     * title: save
     * desc: 
     * @param entName
     * @param keyword
     * @param response
     * wuguoping
     * 2017年7月14日
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public void save(@RequestParam("eName") String entName,
            @RequestParam("keyword") String keyword,
            HttpServletResponse response) throws Exception{
        if(StringUtils.isEmpty(entName) 
                || StringUtils.isEmpty(keyword)){
            logger.error("无效的请求参数 entName/keyword = { NULL or ''}");
            return;
        }
        
        Administer administer = getCurrentUser();
        EnterpriseBlacklist enterpriseBlacklist = new EnterpriseBlacklist();
        enterpriseBlacklist.setEnterpriseName(entName);
        enterpriseBlacklist.setKeyName(keyword);
        enterpriseBlacklist.setCreatorId(administer.getId());
        
        JSONObject json = new JSONObject();
        
        if(!enterpriseBlacklistService.insert(enterpriseBlacklist)){
            json.put("status", "ERROR");
            logger.error("企业黑名单插入失败 entName=" +entName +",  keyword = "+ keyword);
            response.getWriter().write(json.toJSONString());
            return;
        }
        json.put("status", "OK");
        response.getWriter().write(json.toJSONString());
    }
    
    /**
     * 跳转编辑页面
     * title: edit
     * desc: 
     * @return
     * wuguoping
     * 2017年7月17日
     */
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String edit(@RequestParam("id") int entId,
            HttpServletResponse response, ModelMap modelMap) {
        EnterpriseBlacklist enterpriseBlacklist = enterpriseBlacklistService.selectEntBlacklistById(entId);
        modelMap.addAttribute("entBlacklist", enterpriseBlacklist);
        return "enterpriseBlacklist/edit.ftl";
    } 
    
    
    /**
     * 编辑黑名单
     * title: update
     * desc: 
     * @param entName
     * @param keyword
     * @param id
     * @param response
     * wuguoping
     * 2017年7月17日
     */
    @RequestMapping(value = "update" , method = RequestMethod.POST)
    @ResponseBody
    public void update(@RequestParam("eName") String entName,
            @RequestParam("keyword") String keyword,
            @RequestParam("id") Long id,
            HttpServletResponse response) throws Exception{
        Administer administer = getCurrentUser();
        if(StringUtils.isEmpty(entName) 
                || StringUtils.isEmpty(keyword)
                || StringUtils.isEmpty(String.valueOf(id))
                || StringUtils.isEmpty(String.valueOf(administer.getId()))){
            logger.error("无效的请求参数 entName/keyword/id = { NULL or ''}");
            return;
        }

        JSONObject json = new JSONObject();
        if(!enterpriseBlacklistService.update(id, entName, keyword, administer.getId())){
            json.put("status", "ERROR");
            logger.error("企业黑名单插入失败 id= "+ id + "entName=" +entName +",  keyword = "+ keyword);
            response.getWriter().write(json.toJSONString());
            return;
        }
        json.put("status", "OK");
        response.getWriter().write(json.toJSONString());
    }
    
    /**
     * 删除黑名单
     * title: delete
     * desc: 
     * @param id
     * @param response
     * @throws Exception
     * wuguoping
     * 2017年7月17日
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public void delete(@RequestParam("id") String eId,
            HttpServletResponse response) throws Exception{
        if(StringUtils.isEmpty(eId)){
            logger.error("无效的请求参数 id = { NULL or ''}");
            return;
        }
        JSONObject json = new JSONObject();
        Long id = Long.parseLong(eId);
        if(!enterpriseBlacklistService.delete(id)){
            json.put("status", "ERROR");
            logger.error("企业黑名单插入失败 id= "+ id );
            response.getWriter().write(json.toJSONString());
            return;
        }
        json.put("status", "OK");
        response.getWriter().write(json.toJSONString());
    }
    
    /**
     * 企业开户时核查企业名称是否在黑名单中
     * title: checkEntName
     * desc: 
     * @param entName
     * @param response
     * wuguoping
     * 2017年7月18日
     */
    @RequestMapping(value = "checkEntName", method = RequestMethod.GET)
    @ResponseBody
    public void checkEntName(@RequestParam("entName") String entName,
            HttpServletResponse response) {
        if(StringUtils.isEmpty(entName)){
            logger.error("无效的请求参数 entName = { NULL or ''}");
            return;
        }
        
        List<String> entNameList = enterpriseBlacklistService.getBlacklistEntNameList(entName);
        JSONObject json = new JSONObject();
        json.put("blacklist", entNameList);
            
        try {
            response.getWriter().write(json.toJSONString());
        } catch (IOException e) {
            logger.error("写出流错误 .");
        }
    }
    
    /**
     * 模糊核查企业名称是否在企业黑名单中
     * title: indistinctCheckEntName
     * desc: 
     * @param entName
     * @param response
     * wuguoping
     * 2017年7月19日
     */
    @RequestMapping(value= "indistinctCheck", method = RequestMethod.GET)
    @ResponseBody
    public void indistinctCheckEntName(@RequestParam("entName") String entName,
            HttpServletResponse response){
        if(StringUtils.isEmpty(entName)){
            logger.error("无效的请求参数 entName = { NULL or ''}");
            return;
        }
        String newEntName = SensitiveWordFilterUtil.stringFilter(entName);
        Set<String>  entNames = enterpriseBlacklistService.indistinctCheckByEntName(newEntName);
       
        JSONObject json = new JSONObject();
        json.put("blacklist", entNames);
        try {
            response.getWriter().write(json.toJSONString());
        } catch (IOException e) {
            logger.error("写出流错误 .");
        }
    }
    
}

