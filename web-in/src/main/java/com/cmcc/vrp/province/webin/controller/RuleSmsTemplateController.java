package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.RuleSmsTemplate;
import com.cmcc.vrp.province.model.RuleType;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.RuleSmsTemplateService;
import com.cmcc.vrp.province.service.RuleTypeService;
import com.cmcc.vrp.util.PageResult;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.SmsTemplateEl;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RuleSmsTemplateController.java
 */
@Controller
@RequestMapping("/manage/rule_sms_template")
public class RuleSmsTemplateController extends BaseController {

    private Logger logger = Logger.getLogger(RuleSmsTemplateController.class);

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private RuleSmsTemplateService ruleSmsTemplateService;

    @Autowired
    private RuleTypeService ruleTypeService;

    /**
     * 活动短信模板首页
     */
    @RequestMapping(value = "/index")
    public String index(ModelMap modelMap, QueryObject queryObject, Integer pageNum) {

        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 

        /**
         * 查询参数: 企业名称，状态
         */
        String name = getRequest().getParameter("name");

        setQueryParameter("name", queryObject);
        //	setQueryParameter("status", queryObject);
        /*if(queryObject.getQueryCriterias().get("name")!=null){
            queryObject.getQueryCriterias().put("name", transferQuery(name.replace(" ", "")));
		}*/
        if (pageNum != null && pageNum.intValue() > 1) {
            queryObject.setPageNum(pageNum);
        }
        /**
         * 设置当前用户ID
         */
        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }

        Long roleId = adminRoleService.getRoleIdByAdminId(administer.getId());
        //不是超级管理员只能查看自己创建的记录和超级管理员创建的记录
        if (!roleId.toString().equals(getSuperAdminRoleId())) {
            queryObject.getQueryCriterias().put("roleId", roleId);
            queryObject.getQueryCriterias().put("creatorId", administer.getId());
        }

        // 数据库查找符合查询条件的个数
        Long count = ruleSmsTemplateService.count(queryObject);

		/*根据管理员id查询相应企业,实际查找到得只有一个企业*/
        List<RuleSmsTemplate> list = ruleSmsTemplateService.listRuleSmsTemplate(queryObject);

        queryObject = QueryObject.filterQueryObject(queryObject);
        PageResult<RuleSmsTemplate> pageResult = new PageResult<RuleSmsTemplate>(
            queryObject, count, list, "index.html");
        modelMap.addAttribute("pageResult", pageResult);

        modelMap.addAttribute("templateName", name);

        modelMap.addAttribute("pageNum", queryObject.getPageNum());

        return "ruleSmsTemplate/index.ftl";
    }
	
	/*private String transferStr(String str){
		if(StringUtils.isBlank(str)){
			return str;
		}
		System.out.println(str.replaceAll("\\", ""));
		System.out.println(str);
		return str;
	}*/

    /**
     * @Title:add
     * @author:
     */
    @RequestMapping("/add")
    public String add(ModelMap modelMap, RuleSmsTemplate ruleSmsTemplate) {

        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }

        modelMap.addAttribute("ruleSmsTemplate", ruleSmsTemplate);

        List<RuleType> ruleTypes = ruleTypeService.listRuleType();

        modelMap.addAttribute("ruleTypes", ruleTypes);

        return "ruleSmsTemplate/add.ftl";
    }

    /**
     * 示例演示
     */
    /** 
     * @Title: test 
    */
    @RequestMapping("/test")
    public String test(ModelMap modelMap, String content_test) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        java.util.Date date = new java.util.Date();
        String dateStr = sdf.format(date);
        //String test = "您好，感谢您参与{1}发起的{2}活动,恭喜您于{4}抽到{7}流量包!之后还将陆续推出更多精彩活动!敬请期待!";
        String[] str = {dateStr, "中国移动公司", "周末大放送", "70MB", dateStr, "中国移动公司", "周末大放送", "70MB", dateStr, "中国移动公司", "周末大放送", "70MB"};
        String test = "";
        test = MessageFormat.format(content_test, str);
        modelMap.addAttribute("test", test);
        return "redirect:add.html";
    }

    /**
     * @Title:save
     * @Description: 保存
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(ModelMap modelMap, RuleSmsTemplate ruleSmsTemplate, QueryObject queryObject) {
        /**
         * 校验
         */
        Administer currentAdmin = getCurrentUser();
        if (currentAdmin == null) {
            return getLoginAddress();
        }

        String msg = addValidate(ruleSmsTemplate, queryObject);
        if (StringUtils.isNotBlank(msg)) {
            modelMap.addAttribute("errorMsg", msg);
            return add(modelMap, ruleSmsTemplate);
        }

        ruleSmsTemplate.setCreatorId(currentAdmin.getId());
        ruleSmsTemplate.setRoleId(currentAdmin.getRoleId());

        //System.out.println(ruleSmsTemplate.getType());
        if (ruleSmsTemplate.getType().equals("RP")) {
            ruleSmsTemplate.setTypeName("红包短信模板");
        } else if (ruleSmsTemplate.getType().equals("MG")) {
            ruleSmsTemplate.setTypeName("包月赠送短信模板");
        } else if (ruleSmsTemplate.getType().equals("CG")) {
            ruleSmsTemplate.setTypeName("普通赠送短信模板");
        } else if (ruleSmsTemplate.getType().equals("FT")) {
            ruleSmsTemplate.setTypeName("流量券充值短信模板");
        } else if (ruleSmsTemplate.getType().equals("FC")) {
            ruleSmsTemplate.setTypeName("营销卡充值短信模板");
        }

        if (!ruleSmsTemplateService.insert(ruleSmsTemplate)) {
            msg = "新建短信模板失败，请检查输入数据是否有误！";
            modelMap.addAttribute("errorMsg", msg);
            return add(modelMap, ruleSmsTemplate);
        }
        return "redirect:index.html";
    }

    private String addValidate(RuleSmsTemplate ruleSmsTemplate, QueryObject queryObject) {

        if (ruleSmsTemplate == null || ruleSmsTemplate.getName() == null
            || ruleSmsTemplate.getContent() == null) {
            return "传入参数不完整";
        }

        queryObject.getQueryCriterias().put("type", ruleSmsTemplate.getType());

        List<RuleSmsTemplate> list = ruleSmsTemplateService.listRuleSmsTemplate(queryObject);

        for (int i = 0; i < list.size(); ++i) {
            if (ruleSmsTemplate.getId() != null) {
                if (ruleSmsTemplate.getName().replaceAll(" ", "").equals(list.get(i).getName())
                    && ruleSmsTemplate.getId().intValue() != list.get(i).getId().intValue()) {
                    return ruleSmsTemplate.getName() + "规则已经存在该名称的短信模板！";
                }
            } else {
                if (ruleSmsTemplate.getName().replaceAll(" ", "").equals(list.get(i).getName())) {
                    return ruleSmsTemplate.getName() + "规则已经存在该名称的短信模板！";
                }
            }
        }
        return null;
    }

    /**
     * 检查添加和编辑时模板内容是否符合EL表达式逻辑，不符合返回具体错误内容，符合时返回true
     *
     * @param smsTemplate
     * @param model
     */
    @RequestMapping(value = "/checkSmsContent")
    public void checkSmsContent(HttpServletRequest request,
                                HttpServletResponse resp) {
        String smsTemplateContent = request.getParameter("content");

        String message = "true";// 返回true时认为正确，其它为错误

		/*
		 * 开始EL检测
		 */
        message = SmsTemplateEl.virifySmsTemplate(smsTemplateContent);
        try {

            resp.setContentType("text/xml;charset=utf-8");
            resp.setHeader("Cache-Control", "no-cache");

            PrintWriter pw = resp.getWriter();

            pw.print(message);
			/*
			 * message=new String(message.getBytes("ISO-8859-1"),"UTF-8");
			 * resp.getWriter().write(message);
			 */
            pw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            logger.error(e);
        }

    }

    /**
     * 跳转到编辑界面
     *
     * @param map
     * @param smsTemplate
     * @return
     */
    @RequestMapping(value = "/edit")
    public String edit(ModelMap map, Long id, Integer pageNum) {

        /**
         * 校验
         */
        Administer currentAdmin = getCurrentUser();
        if (currentAdmin == null) {
            return getLoginAddress();
        }

        // 简单判断
        if (id != null && id.intValue() > 0) {

            // 根据ID查询
            RuleSmsTemplate ruleSmsTemplate = ruleSmsTemplateService.selectByPrimaryKey(id);

            //超级管理员可以修改所有人创建的记录，其余人只能修改自己创建的记录
            Long roleId = adminRoleService.getRoleIdByAdminId(currentAdmin.getId());
            if (roleId == null || !roleId.toString().equals(getSuperAdminRoleId())
                && currentAdmin.getId().intValue() != ruleSmsTemplate.getCreatorId().intValue()) {
                map.addAttribute("errorMsg", "该条短信模板不是由当前用户创建，不具有修改权限!");
                return "error.ftl";
            }

            List<RuleType> ruleTypes = ruleTypeService.listRuleType();
            map.addAttribute("ruleTypes", ruleTypes);

            // 判断是否存在该值 如果存在就跳转到编辑界面 否则回到首页
            if (ruleSmsTemplate != null) {
                map.addAttribute("ruleSmsTemplate", ruleSmsTemplate);
                map.addAttribute("pageNum", pageNum);
                return "/ruleSmsTemplate/edit.ftl";
            }
        }
        return index(map, null, pageNum);
    }

    /**
     * 保存修改的操作
     *
     * @param map
     * @param smsTemplate
     * @param pageResult
     * @return
     */
    @RequestMapping(value = "/update")
    public String update(ModelMap map, RuleSmsTemplate ruleSmsTemplate, QueryObject queryObject, Integer pageNum) {
        /**
         * 参数校验
         * */
        String msg = addValidate(ruleSmsTemplate, queryObject);
        if (StringUtils.isNotBlank(msg)) {
            map.addAttribute("errorMsg", msg);
            return edit(map, ruleSmsTemplate.getId(), pageNum);
        }

        // 执行修改操作
        if (!ruleSmsTemplateService.updateByPrimaryKeySelective(ruleSmsTemplate)) {
            msg = "修改失败，请检查输入参数是否有误！";
            map.addAttribute("errorMsg", msg);
            map.addAttribute("pageNum", pageNum);
            return "/ruleSmsTemplate/edit.ftl";
        }


        return "redirect:index.html";
    }

    /**
     * 显示短信详情
     *
     * @param map
     * @param id
     * @return
     */
    @RequestMapping(value = "/detail")
    public String detail(ModelMap map, Long id, Integer pageNum) {

        /**
         * 校验
         */
        Administer currentAdmin = getCurrentUser();
        if (currentAdmin == null) {
            return getLoginAddress();
        }

        if (id != null && id.intValue() > 0) {
            // 根据ID查询
            RuleSmsTemplate ruleSmsTemplate = ruleSmsTemplateService.selectByPrimaryKey(id);

            //超级管理员可以修改所有人创建的记录，其余人只能修改自己创建的记录
            Long roleId = adminRoleService.getRoleIdByAdminId(currentAdmin.getId());
            if (roleId == null || !roleId.toString().equals(getSuperAdminRoleId())
                && ruleSmsTemplate != null && currentAdmin.getId().intValue() != ruleSmsTemplate.getCreatorId().intValue()) {
                map.addAttribute("errorMsg", "该条短信模板不是由当前用户创建，不具有查看权限!");
                return "error.ftl";
            }


            if (ruleSmsTemplate != null) {
                String creatorName = adminRoleService.getRoleNameByAdminId(ruleSmsTemplate.getCreatorId());
                map.addAttribute("creatorName", creatorName);
                map.addAttribute("ruleSmsTemplate", ruleSmsTemplate);
                map.addAttribute("pageNum", pageNum);
                return "/ruleSmsTemplate/detail.ftl";
            }
        }
        return index(map, null, pageNum);
    }

    /**
     * 删除记录
     *
     * @param map
     * @param smsTemplate
     * @param pageResult
     * @return
     */
    @RequestMapping(value = "/delete")
    public String delete(ModelMap map, Long id, QueryObject queryObject) {

        /**
         * 校验
         */
        Administer currentAdmin = getCurrentUser();
        if (currentAdmin == null) {
            return getLoginAddress();
        }

        if (id != null && id.intValue() > 0) {

            //根据ID查询
            RuleSmsTemplate ruleSmsTemplate = ruleSmsTemplateService.selectByPrimaryKey(id);
            //超级管理员可以修改所有人创建的记录，其余人只能修改自己创建的记录
            Long roleId = adminRoleService.getRoleIdByAdminId(currentAdmin.getId());
            if (roleId == null || !roleId.toString().equals(getSuperAdminRoleId())
                && currentAdmin.getId().intValue() != ruleSmsTemplate.getCreatorId().intValue()) {
                map.addAttribute("errorMsg", "该条短信模板不是由当前用户创建，不具有删除权限!");
                return "error.ftl";
            }
            //删除记录
            ruleSmsTemplateService.deleteByPrimaryKey(id);
        }
        return index(map, queryObject, 1);
    }

    /**
     * 获取短信模板
     **/
    @RequestMapping(value = "getSmsTemplateAjax")
    public void getSmsTemplateAjax(HttpServletRequest request, HttpServletResponse resp, String type) throws IOException {

        Map<String, Object> map = new HashMap<String, Object>();//ajax传输的值
        Administer administer = getCurrentUser();
        if (administer == null) {
            map.put("fail", "fail");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(JSON.toJSONString(map));
            return;

        }


        //	List<RuleTemplate> templates= ruleTemplateService.getRuleTemplateByCreator(administer.getId());

        List<RuleSmsTemplate> templates = ruleSmsTemplateService.
            getRuleSmsTemplateByCreator(administer.getId(), type);

        if (templates == null || templates.size() == 0) {
            map.put("fail", "notExist");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(JSON.toJSONString(map));
            return;
        }

        map.put("templates", templates);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(JSON.toJSONString(map));
    }


}
