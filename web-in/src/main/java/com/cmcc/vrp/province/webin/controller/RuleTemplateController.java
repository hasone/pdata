package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.RuleTemplate;
import com.cmcc.vrp.province.service.AdminRoleService;
import com.cmcc.vrp.province.service.RuleTemplateService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.PageResult;
import com.cmcc.vrp.util.QueryObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/manage/rule_template")
public class RuleTemplateController extends BaseController {

    private static final long IMAGESIZE = 2;
    private Logger logger = Logger.getLogger(RuleTemplateController.class);
    @Autowired
    private AdminRoleService adminRoleService;
    @Autowired
    private RuleTemplateService ruleTemplateService;

    /**
     * 活动短信模板首页
     */
    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request, ModelMap modelMap, QueryObject queryObject, Integer pageNum) {

        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 

        String queryStatus = request.getParameter("status");
        String name = request.getParameter("name");

        if (pageNum != null && pageNum.intValue() > 1) {
            queryObject.setPageNum(pageNum);
        }

        /**
         * 查询参数: 企业名称，状态
         */
        setQueryParameter("name", queryObject);
        setQueryParameter("status", queryObject);
        /**
         * 设置当前用户ID
         */
        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }
        modelMap.addAttribute("adminId", administer.getId());

        Long roleId = adminRoleService.getRoleIdByAdminId(administer.getId());
        // 不是超级管理员只能查看自己创建的记录和超级管理员创建的记录
        if (!roleId.toString().equals(getSuperAdminRoleId())) {
            queryObject.getQueryCriterias().put("roleId", roleId);
            queryObject.getQueryCriterias().put("creatorId", administer.getId());
        }

        // 数据库查找符合查询条件的个数
        Long count = ruleTemplateService.count(queryObject);

        List<RuleTemplate> list = ruleTemplateService.listRuleTemplate(queryObject);
        queryObject = QueryObject.filterQueryObject(queryObject);
        PageResult<RuleTemplate> pageResult = new PageResult<RuleTemplate>(queryObject, count, list, "index.html");
        modelMap.addAttribute("pageResult", pageResult);

        modelMap.addAttribute("queryStatus", queryStatus);
        modelMap.addAttribute("taskTemplateName", name);
        modelMap.addAttribute("pageNum", queryObject.getPageNum());

        return "ruleTemplate/index.ftl";
    }

    /**
     * @Title:add
     * @author:
     */
    @RequestMapping("/create")
    public String create(ModelMap modelMap, RuleTemplate ruleTemplate) {
        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }

        if (!adminRoleService.getRoleIdByAdminId(administer.getId()).toString().equals(getSuperAdminRoleId())
                && !adminRoleService.getRoleIdByAdminId(administer.getId()).toString().equals(getCustomManager())
                && !adminRoleService.getRoleIdByAdminId(administer.getId()).toString().equals(getENTERPRISE_CONTACTOR())) {
            modelMap.addAttribute("errorMsg", "对不起，只有超级管理员、客户经理和企业关键人才能创建红包模板！");
            return "error.ftl";
        }
        return "ruleTemplate/create.ftl";
    }

    /**
     * 校验卡序列号
     */
    @RequestMapping("/checkNameAjax")
    public void checkNameAjax(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();// ajax传输的值

        String name = request.getParameter("name").replaceAll(" ", "");

        if (StringUtils.isBlank(name)) {
            map.put("failure", "missingParam");
            resp.getWriter().write(JSON.toJSONString(map));
            return;
        }

        /**
         * 设置当前用户ID
         */
        Administer administer = getCurrentUser();

        List<RuleTemplate> list = ruleTemplateService.getRuleTemplateByCreator(administer.getId());

        for (RuleTemplate item : list) {
            if (name.equals(item.getName())) {
                map.put("failure", "same");
                resp.getWriter().write(JSON.toJSONString(map));
                return;
            }
        }

        map.put("msg", "ok");
        resp.getWriter().write(JSON.toJSONString(map));
        return;

    }

    /**
     * @Title: checkName
     */
    @RequestMapping(value = "/checkName")
    public void checkName(String name, Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String szName = name.replaceAll(" ", "");

        /**
         * 设置当前用户ID
         */
        Administer administer = getCurrentUser();

        List<RuleTemplate> list = ruleTemplateService.getRuleTemplateByCreator(administer.getId());

        Boolean bFlag = true;
        for (RuleTemplate item : list) {
            if (szName.equals(item.getName())) {
                bFlag = false;
                break;
            }
        }
        response.getWriter().write(bFlag.toString());
    }

    /**
     * @Title:save
     * @Description: 保存
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(ModelMap modelMap, RuleTemplate ruleTemplate, QueryObject queryObject) {
        /**
         * 校验
         */
        Administer currentAdmin = getCurrentUser();
        if (currentAdmin == null) {
            return getLoginAddress();
        }

        if (ruleTemplate == null || StringUtils.isBlank(ruleTemplate.getActivityDes()) || StringUtils.isBlank(ruleTemplate.getDescription())
                || StringUtils.isBlank(ruleTemplate.getName()) || StringUtils.isBlank(ruleTemplate.getPeople()) || StringUtils.isBlank(ruleTemplate.getTitle())) {
            String msg = "存在参数为空，请填写完整！";
            modelMap.addAttribute("errorMessage", msg);
            return create(modelMap, ruleTemplate);
        }
        ruleTemplate.setName(ruleTemplate.getName().replaceAll(" ", ""));
        ruleTemplate.setCreatorId(currentAdmin.getId());
        ruleTemplate.setRoleId(currentAdmin.getRoleId());

        if (!ruleTemplateService.insert(ruleTemplate)) {
            String msg = "新建短信模板失败，请检查输入数据是否有误！";
            modelMap.addAttribute("errorMessage", msg);
            return create(modelMap, ruleTemplate);
        }
        return "redirect:index.html";
    }

    /**
     * @param modelMap
     * @param id
     * @param queryObject
     * @return
     * @throws
     * @Title:changeStatus
     * @Description: 更新上下架状态
     * @author:
     */
    @RequestMapping("changeStatus")
    public String changeStatus(HttpServletRequest request, ModelMap modelMap, Long id, QueryObject queryObject) {

        if (id != null) {

            RuleTemplate ruleTemplate = ruleTemplateService.selectByPrimaryKey(id);

            if (ruleTemplate != null) {

                Administer administer = getCurrentUser();
                if (administer == null) {
                    return getLoginAddress();
                }

                // 判断该条记录是否由当前用户创建
                if (!administer.getRoleId().toString().equals(getSuperAdminRoleId())) {
                    if (administer.getId().intValue() != ruleTemplate.getCreatorId().intValue()) {
                        modelMap.addAttribute("errorMsg", "对不起，您没有权限修改该条记录！");
                        return "error.ftl";
                    }
                }

                if (ruleTemplate.getStatus().intValue() == Constants.ENTREDPACKET_STATUS.ON.getValue()) {
                    ruleTemplate.setStatus(Constants.ENTREDPACKET_STATUS.OFF.getValue());
                } else {
                    ruleTemplate.setStatus(Constants.ENTREDPACKET_STATUS.ON.getValue());
                }
                ruleTemplateService.updateByPrimaryKey(ruleTemplate);
            }
        }
        return index(request, modelMap, queryObject, null);
    }

    /**
     * @Title: uploadImage
     */
    @RequestMapping("uploadImage")
    public String uploadImage(ModelMap modelMap, Long id, Integer pageNum) {

        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }
        RuleTemplate ruleTemplate = ruleTemplateService.selectByPrimaryKey(id);
        // 判断该条记录是否由当前用户创建

        if (ruleTemplate == null || administer.getId().intValue() != ruleTemplate.getCreatorId().intValue()) {
            modelMap.addAttribute("errorMsg", "对不起，您没有权限修改该条记录！");
            return "error.ftl";
        }

        List<File> files = ruleTemplateService.listFiles(id);
        String creatorName = adminRoleService.getRoleNameByAdminId(ruleTemplate.getCreatorId());
        modelMap.addAttribute("ruleTemplate", ruleTemplate);
        modelMap.addAttribute("creatorName", creatorName);
        modelMap.put("userId", getCurrentUser().getId());
        modelMap.put("files", files);
        modelMap.put("pageNum", pageNum);

        return "ruleTemplate/uploadImage.ftl";
    }

    /** 
     * @Title: detail 
    */
    @RequestMapping("detail")
    public String detail(ModelMap modelMap, Long id, Integer pageNum) {

        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }
        RuleTemplate ruleTemplate = ruleTemplateService.selectByPrimaryKey(id);
        // 判断该条记录是否由当前用户创建
        if (!administer.getRoleId().toString().equals(getSuperAdminRoleId())) {
            if (administer.getId().intValue() != ruleTemplate.getCreatorId().intValue()) {
                modelMap.addAttribute("errorMsg", "对不起，您没有权限查看该条记录！");
                return "error.ftl";
            }
        }
        List<File> files = null;
        if (ruleTemplate != null) {
            files = ruleTemplateService.listFiles(id);
        }

        String creatorName = ruleTemplate == null ? null : adminRoleService.getRoleNameByAdminId(ruleTemplate.getCreatorId());
        modelMap.addAttribute("ruleTemplate", ruleTemplate);
        modelMap.addAttribute("creatorName", creatorName);
        modelMap.put("userId", getCurrentUser().getId());
        modelMap.put("files", files);
        modelMap.put("pageNum", pageNum);

        return "ruleTemplate/detail.ftl";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public @ResponseBody String upload() {
        return "ok";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public void upload(MultipartHttpServletRequest request, HttpServletResponse response, long id) throws IOException {
        // Long userId = Long.parseLong(request.getParameter("userId"));
        // RuleTemplate ruleTemplate = ruleTemplateService.selectByPrimaryKey(id);

        int flag = 1;
        // if (ruleTemplate == null || userId == null
        // || !ruleTemplate.getCreatorId().equals(userId)) {
        // response.sendError(403);
        // return "forbit";
        // }

        JSONObject json = new JSONObject();

        try {
            Iterator<String> itr = request.getFileNames();
            MultipartFile multipartFile = null;

            // 迭代处理
            while (itr != null && itr.hasNext()) {

                // 获得文件实例
                multipartFile = request.getFile(itr.next());
                BufferedImage img = ImageIO.read(multipartFile.getInputStream());
                if (multipartFile.getSize() > IMAGESIZE * 1024 * 1024) {
                    json.put("success", false);
                    json.put("msg", "图片大小不符合要求，不能大于" + IMAGESIZE + "M");
                } else {
                    json.put("success", true);
                    // 保存到磁盘
                    flag = ruleTemplateService.writeFile(id, multipartFile.getOriginalFilename(), multipartFile.getBytes());

                    logger.info("用户Id：" + getCurrentUser().getId() + "上传文件\"" + multipartFile.getOriginalFilename() + "\"，大小" + multipartFile.getSize());
                }
            }
            // if(flag==0){
            // return "ok";
            // }else{
            // response.sendError(412);
            // return "repeat";
            // }

        } catch (Exception e) {
            logger.error("用户Id：" + getCurrentUser().getId() + "上传文件出现异常\n " + e);
        }

        response.getWriter().write(json.toString());
    }

    @RequestMapping(value = "/getFile")
    public void getFile(long id, String filename, HttpServletResponse response) {

        byte[] data = null;
        try {
            data = ruleTemplateService.getFile(id, filename);
            if (data != null) {
                String encoded = URLEncoder.encode(filename, "utf-8");
                response.addHeader("Content-Disposition", "attachment;filename=" + encoded);
                response.addHeader("Content-Length", data.length + "");

                response.getOutputStream().write(data);
                response.getOutputStream().flush();
            }
        } catch (Exception e) {
            logger.error("用户Id：" + getCurrentUser().getId() + " 读取文件 " + filename + " 出现异常 " + e);
        }

    }

    @RequestMapping(value = "/setCover")
    public String setCover(ModelMap map, long id, String front) {

        if (StringUtils.isNotBlank(front)) {
            ruleTemplateService.updateFrontAndRearImage(id, front);
        }
        logger.info("用户Id：" + getCurrentUser().getId() + "设置模板背景图片，id：" + id + ",图片：" + front);
        return uploadImage(map, id, 1);
    }

    @RequestMapping(value = "/delete")
    public String delete(ModelMap map, long id, String filename) {

        RuleTemplate ruleTemplate = ruleTemplateService.selectByPrimaryKey(id);

        if (ruleTemplate == null || getCurrentUser() == null || !ruleTemplate.getCreatorId().equals(getCurrentUser().getId())) {
            return uploadImage(map, id, 1);
        }

        ruleTemplateService.deleteFile(id, filename);

        return uploadImage(map, id, 1);
    }

    @RequestMapping("/deleteRecord")
    public String delete(HttpServletRequest request, ModelMap modelMap, Long id, QueryObject queryObject) {

        if (id != null) {
            RuleTemplate ruleTemplate = ruleTemplateService.selectByPrimaryKey(id);

            if (ruleTemplate != null) {

                Administer administer = getCurrentUser();
                if (administer == null) {
                    return getLoginAddress();
                }

                // 判断该条记录是否由当前用户创建
                if (!administer.getId().equals(ruleTemplate.getCreatorId())) {
                    modelMap.addAttribute("errorMsg", "对不起，您没有权限修改该条记录！");
                    return "error.ftl";
                }

                ruleTemplateService.deleteByPrimaryKey(id);
            }
        }
        return index(request, modelMap, queryObject, null);
    }

    @RequestMapping(value = "getTemplateAjax")
    public void getTemplateAjax(HttpServletRequest request, HttpServletResponse resp) throws IOException {

        Map<String, Object> map = new HashMap<String, Object>();// ajax传输的值
        Administer administer = getCurrentUser();
        if (administer == null) {
            map.put("fail", "fail");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(JSON.toJSONString(map));
            return;

        }

        // List<RuleTemplate> templates= ruleTemplateService.getRuleTemplateByCreator(administer.getId());

        List<RuleTemplate> templates = ruleTemplateService.getTemplateToCreateRedpacket(administer.getId());

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

    @RequestMapping(value = "getPictureAjax")
    public void getPictureAjax(HttpServletRequest request, HttpServletResponse resp) throws IOException {

        String templateId = getRequest().getParameter("templateId");
        Map<String, Object> map = new HashMap<String, Object>();// ajax传输的值

        if (StringUtils.isBlank(templateId)) {
            map.put("fail", "notExist");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(JSON.toJSONString(map));
            return;
        }

        RuleTemplate ruleTemplate = ruleTemplateService.selectByPrimaryKey(Long.valueOf(templateId));

        if (ruleTemplate == null || StringUtils.isBlank(ruleTemplate.getImage())) {
            map.put("fail", "notExist");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(JSON.toJSONString(map));
            return;
        }

        map.put("filename", ruleTemplate.getImage());
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(JSON.toJSONString(map));

    }

}
