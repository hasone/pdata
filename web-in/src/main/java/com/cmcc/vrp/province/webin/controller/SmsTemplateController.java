package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.province.model.SmsTemplate;
import com.cmcc.vrp.province.service.SmsTemplateService;
import com.cmcc.vrp.util.PageResult;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.SmsTemplateEl;
import com.cmcc.vrp.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 短信模板Controller
 *
 * @author kok
 */
@Controller
@RequestMapping("/manage/sms")
public class SmsTemplateController extends BaseController {

    @Autowired
    SmsTemplateService smsTemplateService;
    private Logger logger = Logger.getLogger(SmsTemplateController.class);

    /**
     * 短信模板首页
     *
     * @return
     */
    @RequestMapping(value = "/index")
    public String index(ModelMap model, QueryObject queryObject) {

        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        model.addAttribute("back",queryObject.getBack());//增加返回标识 

        setQueryParameter("name", queryObject);
        int count = smsTemplateService.countSmsTemplate(queryObject
            .getQueryCriterias());

        List<SmsTemplate> list = smsTemplateService.showSmsTemplate(queryObject
            .toMap());
        queryObject = QueryObject.filterQueryObject(queryObject);
        PageResult pageResult = new PageResult<SmsTemplate>(queryObject, count,
            list, "index.html");

        model.addAttribute("pageResult", pageResult);

        return "/sms/index.ftl";
    }

    /**
     * 跳转到添加页面
     *
     * @return
     */

    @RequestMapping(value = "/add")
    public String add() {
        return "/sms/add.ftl";
    }

    /**
     * 保存短信模板
     *
     * @param map
     * @param smsTemplate
     * @param pageResult
     * @return
     */
    @RequestMapping(value = "/save")
    public String save(ModelMap map, SmsTemplate smsTemplate) {

        // 对数据进行简单的非空判断
        String msg = checkMsg(smsTemplate);
        if (msg == null) {

            if (isOnlyName(smsTemplate.getName())) {

                // if(SmsTemplateEl.virifySmsTemplate(smsTemplate.getContent()))
                // 执行添加操作
                if (smsTemplateService.insertSelective(smsTemplate)) {
                    // 添加成功

                    return "redirect:index.html";
                }

            } else {
                msg = "短信名称重复!";
            }
        }
        map.addAttribute("errorMsg", msg);
        // 失败
        return "/sms/add.ftl";
    }

    /**
     * 跳转到编辑界面
     *
     * @param map
     * @param smsTemplate
     * @return
     */
    @RequestMapping(value = "/edit")
    public String edit(ModelMap map, SmsTemplate smsTemplate) {

        // 简单判断
        if (smsTemplate != null && smsTemplate.getId() > 0) {

            // 根据ID查询
            smsTemplate = smsTemplateService.selectByPrimaryKey(smsTemplate.getId());

            // 判断是否存在该值 如果存在就跳转到编辑界面 否则回到首页
            if (smsTemplate != null) {
                map.addAttribute("smsTemplate", smsTemplate);
                return "/sms/edit.ftl";
            }
        }

        return index(map, null);
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
    public String update(ModelMap map, SmsTemplate smsTemplate) {

        String msg = checkMsg(smsTemplate);
        // 对数据进行简单的非空判断
        if (msg == null) {

            List<SmsTemplate> list = smsTemplateService.checkSms(smsTemplate.getName());
            if (list == null || list.get(0).getId() == smsTemplate.getId()) {
                // 执行修改操作
                if (smsTemplateService.updateByPrimaryKeySelective(smsTemplate)) {
                    // 修改成功

                    return "redirect:index.html";
                }
            } else {
                msg = "姓名已存在";
            }

        }
        map.addAttribute("errorMsg", msg);
        return "/sms/edit.ftl";
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
    public String delete(ModelMap map, SmsTemplate smsTemplate,
                         QueryObject queryObject) {

        if (smsTemplate != null && smsTemplate.getId() > 0) {
            smsTemplateService.deleteByPrimaryKey(smsTemplate.getId());
        }

        return index(map, queryObject);
    }

    /**
     * 查看短信模板
     *
     * @param map
     * @param smsTemplate
     * @return
     */
    @RequestMapping(value = "/showSmsTemplate")
    public String showSmsTemplate(ModelMap map, SmsTemplate smsTemplate) {

        if (smsTemplate != null && smsTemplate.getId() > 0) {
            smsTemplate = smsTemplateService.selectByPrimaryKey(smsTemplate
                .getId());
            if (smsTemplate != null) {
                map.addAttribute("smsTemplate", smsTemplate);
                return "/sms/showSmsTemplate.ftl";
            }
        }

        return index(map, null);

    }

    /**
     * 判断姓名是否唯一 true 为
     *
     * @param name
     * @return
     */
    Boolean isOnlyName(String name) {
        if (!StringUtils.isEmpty(name)) {
            List list = smsTemplateService.checkSms(name);
            if (list == null || list.size() == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * 内容校验
     *
     * @param smsTemplate
     * @return
     */
    String checkMsg(SmsTemplate smsTemplate) {
        String msg = "";
        if (smsTemplate != null) {
            if (!StringUtils.isEmpty(smsTemplate.getName())) {
                if (!StringUtils.isEmpty(smsTemplate.getContent())) {
                    return null;
                } else {
                    msg = "模板内容不能为空!";
                }

            } else {
                msg = "姓名不能为空!";
            }
        }

        return msg;
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
     * @Title: check 
    */
    @RequestMapping(value = "/check")
    public void check(HttpServletRequest request, HttpServletResponse resp) {
        String name = request.getParameter("name");

        try {
            resp.getWriter().write(isOnlyName(name).toString());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e);
        }
    }

    /** 
     * @Title: updateCheck
    */
    @RequestMapping(value = "/updateCheck")
    public void updateCheck(HttpServletRequest request, HttpServletResponse resp) {
        String name = request.getParameter("name");
        Long id = Long.parseLong(request.getParameter("id") != null ? request.getParameter("id") : "0");
        List<SmsTemplate> list = smsTemplateService.checkSms(name);


        try {
            if (list == null || list.get(0).getId() == id) {
                resp.getWriter().write("true");

            } else {
                resp.getWriter().write("false");
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e);
        }
    }

    /**
     * 查找短信模板内容
     *
     * @param map
     * @param smsTemplate
     * @return
     */
    @RequestMapping(value = "getSmsTemplateAjax")
    public void getProductsAjax(HttpServletRequest request, HttpServletResponse resp) throws IOException {

        String id = request.getParameter("smsTemplateId");//得到enterpriseId

        if (id == null) {
            return;
        }

        SmsTemplate sms = smsTemplateService.selectByPrimaryKey(Long.parseLong(id));

        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(JSON.toJSONString(sms));
    }
}
