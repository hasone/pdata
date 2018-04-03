package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ApprovalDetailDefinition;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.model.Role;
import com.cmcc.vrp.province.service.ApprovalDetailDefinitionService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.RoleService;
import com.cmcc.vrp.util.QueryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ApprovalDefinitionController
 */
@RequestMapping("/manage/approvalDefinition")
@Controller
public class ApprovalDefinitionController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApprovalDefinitionController.class);

    @Autowired
    private ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Autowired
    private ApprovalDetailDefinitionService approvalDetailDefinitionService;
    @Autowired
    private RoleService roleService;

    /** 
     * @Title: index 
     * @param modelmap
     * @return
    */
    @RequestMapping("index")
    public String index(ModelMap modelmap, QueryObject queryObject) {
        Administer currentAdmin = getCurrentUser();
        if (currentAdmin == null) {
            return getLoginAddress();
        }
        
        if(queryObject != null){
            modelmap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        return "approvalDefinition/index.ftl";
    }

    /** 
     * @Title: search 
     * @param queryObject
     * @param res
    */
    @RequestMapping(value = "search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        // 设置前端筛选字段
        //setQueryParameter("name", queryObject);
        //setQueryParameter("deleteFlag", queryObject);

        try {
            int count = approvalProcessDefinitionService.countApprovalProcess(queryObject);
            List<ApprovalProcessDefinition> approvalProcessList = approvalProcessDefinitionService.queryApprovalProcessList(queryObject);

            JSONObject json = new JSONObject();
            json.put("pageNum", queryObject.getPageNum());
            json.put("pageSize", queryObject.getPageSize());
            json.put("data", approvalProcessList);
            json.put("total", count);
            json.put("queryObject", queryObject.toMap());
            try {
                res.getWriter().write(json.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /** 
     * @Title: detial 
     * @param modelmap
     * @param id
     * @return
    */
    @RequestMapping(value = "detail")
    public String detial(ModelMap modelmap, Long id) {

        if (id == null) {
            return "error.ftl";
        }

        ApprovalProcessDefinition approvalProcess = approvalProcessDefinitionService.getApprovalProcessById(id);

        List<ApprovalDetailDefinition> approvalDetailList = approvalDetailDefinitionService.getByApprovalProcessId(id);

        modelmap.addAttribute("approvalProcess", approvalProcess);
        modelmap.addAttribute("approvalDetailList", approvalDetailList);

        return "approvalDefinition/detail.ftl";
    }

    /**
     * 删除产品
     *
     * @param modelMap
     * @param id
     * @return
     */
    @RequestMapping(value = "delete")
    public String delete(ModelMap modelMap, Long id) {
        if (id == null) {
            return index(modelMap, null);
        }
        try {
            if (!approvalProcessDefinitionService.deleteApprovalProcess(id)) {
                LOGGER.info("用户ID:" + getCurrentUser().getId() + " 删除审批流程ID:" + id + "失败");
            }
        } catch (RuntimeException e) {
            LOGGER.error("用户ID:" + getCurrentUser().getId() + " 删除审批流程ID:" + id + "失败,原因:" + e.getMessage());
        }
        return "redirect:index.html";
    }

    /** 
     * @Title: create 
     * @param modelMap
     * @param approvalProcess
     * @return
    */
    @RequestMapping(value = "create")
    public String create(ModelMap modelMap, ApprovalProcessDefinition approvalProcess) {
        List<Role> roleList = roleService.getAvailableRoles();
        List<Role> approvalRoles = new ArrayList<Role>();
        for (Role role : roleList) {
            if (!Long.toString(role.getRoleId()).equals(getENTERPRISE_CONTACTOR())) {
                approvalRoles.add(role);
            }
        }
        modelMap.addAttribute("roles", roleList);
        modelMap.addAttribute("approvalRoles", approvalRoles);
        return "approvalDefinition/create.ftl";
    }

    /** 
     * @Title: saveApprovalProcess 
     * @param modelMap
     * @param approvalProcess
     * @return
    */
    @RequestMapping(value = "saveApprovalProcess")
    public String saveApprovalProcess(ModelMap modelMap, ApprovalProcessDefinition approvalProcess) {

        if (approvalProcess != null && approvalProcess.getStage() != null && approvalProcess.getOriginRoleId() != null) {
            //判断是否有已经存在同类型审批流程
            ApprovalProcessDefinition approvalProcessHasExit = approvalProcessDefinitionService.selectByType(approvalProcess.getType());
            if (approvalProcessHasExit != null) {
                modelMap.addAttribute("errorMsg", "已经存在该类型的审批流程，不可重复创建!");
                return "error.ftl";
            }
            try {
                if (approvalProcessDefinitionService.insertApprovalProcess(approvalProcess)) {
                    LOGGER.error("用户ID:" + getCurrentUser().getId() + " 新建审批流程ID" + approvalProcess.getId() + "成功");
                    return "redirect:index.html";
                } else {
                    LOGGER.error("用户ID:" + getCurrentUser().getId() + " 新建审批流程失败");
                    return create(modelMap, approvalProcess);
                }
            } catch (RuntimeException e) {
                LOGGER.error("用户ID:" + getCurrentUser().getId() + " 新建审批流程失败,原因:" + e.getMessage());
                return create(modelMap, approvalProcess);
            }
        } else {
            return create(modelMap, approvalProcess);
        }
    }

    /** 
     * @Title: edit 
     * @param modelMap
     * @param id
     * @return
    */
    @RequestMapping(value = "edit")
    public String edit(ModelMap modelMap, Long id) {
        if (id == null) {
            return "redirect:index.html";
        }

        ApprovalProcessDefinition approvalProcess = approvalProcessDefinitionService.getApprovalProcessById(id);
        List<ApprovalDetailDefinition> approvalDetailList = approvalDetailDefinitionService.getByApprovalProcessId(id);

        if (approvalProcess == null) {
            return "redirect:index.html";
        }
        if (approvalProcess.getStage().toString().equals("1")) {
            approvalProcess.setFirstRoleId(approvalDetailList.get(0).getRoleId());
        } else if (approvalProcess.getStage().toString().equals("2")) {
            approvalProcess.setFirstRoleId(approvalDetailList.get(0).getRoleId());
            approvalProcess.setSecondRoleId(approvalDetailList.get(1).getRoleId());
        } else if (approvalProcess.getStage().toString().equals("3")) {
            approvalProcess.setFirstRoleId(approvalDetailList.get(0).getRoleId());
            approvalProcess.setSecondRoleId(approvalDetailList.get(1).getRoleId());
            approvalProcess.setThirdRoleId(approvalDetailList.get(2).getRoleId());
        } else if (approvalProcess.getStage().toString().equals("4")) {
            approvalProcess.setFirstRoleId(approvalDetailList.get(0).getRoleId());
            approvalProcess.setSecondRoleId(approvalDetailList.get(1).getRoleId());
            approvalProcess.setThirdRoleId(approvalDetailList.get(2).getRoleId());
            approvalProcess.setForthRoleId(approvalDetailList.get(3).getRoleId());
        } else if (approvalProcess.getStage().toString().equals("5")) {
            approvalProcess.setFirstRoleId(approvalDetailList.get(0).getRoleId());
            approvalProcess.setSecondRoleId(approvalDetailList.get(1).getRoleId());
            approvalProcess.setThirdRoleId(approvalDetailList.get(2).getRoleId());
            approvalProcess.setForthRoleId(approvalDetailList.get(3).getRoleId());
            approvalProcess.setFifthRoleId(approvalDetailList.get(4).getRoleId());
        }

        List<Role> roleList = roleService.getAvailableRoles();
        List<Role> approvalRoles = new ArrayList<Role>();
        for (Role role : roleList) {
            if (!Long.toString(role.getRoleId()).equals(getENTERPRISE_CONTACTOR())) {
                approvalRoles.add(role);
            }
        }
        modelMap.addAttribute("roles", roleList);
        modelMap.addAttribute("approvalRoles", approvalRoles);
        modelMap.addAttribute("approvalProcess", approvalProcess);
        return "approvalDefinition/edit.ftl";
    }

    /** 
     * @Title: saveEdit 
     * @param modelMap
     * @param approvalProcess
     * @return
    */
    @RequestMapping(value = "saveEdit")
    public String saveEdit(ModelMap modelMap, ApprovalProcessDefinition approvalProcess) {

        if (approvalProcess != null) {
            if (approvalProcess.getId() == null) {
                return "redirect:index.html";
            }

            if (approvalProcess.getStage() != null && approvalProcess.getOriginRoleId() != null) {
                try {
                    if (approvalProcessDefinitionService.updateApprovalProcess(approvalProcess)) {
                        LOGGER.error("用户ID:" + getCurrentUser().getId() + " 更新审批流程ID" + approvalProcess.getId() + "成功");
                        return "redirect:index.html";
                    } else {
                        LOGGER.error("用户ID:" + getCurrentUser().getId() + " 更新审批流程失败");
                        return edit(modelMap, approvalProcess.getId());
                    }
                } catch (RuntimeException e) {
                    LOGGER.error("用户ID:" + getCurrentUser().getId() + " 更新审批流程失败,原因:" + e.getMessage());
                    return edit(modelMap, approvalProcess.getId());
                }
            } else {
                return edit(modelMap, approvalProcess.getId());
            }
        } else {
            return "redirect:index.html";
        }
    }

}
