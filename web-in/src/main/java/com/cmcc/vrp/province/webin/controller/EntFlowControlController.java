/**
 *
 */
package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.FlowControlType;
import com.cmcc.vrp.province.model.EntFlowControl;
import com.cmcc.vrp.province.model.EntFlowControlRecord;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.EntFlowControlService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.util.QueryObject;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title:EntFlowControlController Description:
 * </p>
 *
 * @author xujue
 * @date 2016年9月20日
 */

@Controller
@RequestMapping("manage/entFlowControl")
public class EntFlowControlController extends BaseController {

    private static final Logger logger = LoggerFactory
        .getLogger(EntFlowControlController.class);

    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    ManagerService managerService;
    @Autowired
    EntFlowControlService entFlowControlService;

    /** 
     * @Title: entFlowControlIndex 
    */
    @RequestMapping("index")
    public String entFlowControlIndex(ModelMap model, QueryObject queryObject) {
//	if (queryObject == null) {
//	    queryObject = new QueryObject();
//	}

        // 管理员层级筛选
        // 根节点为当前用户的管理员层级
        if(queryObject != null){
            model.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        Manager manager = getCurrentUserManager();
        if (manager == null) {
            model.put("errorMsg", "当前用户无管理员身份");
            return "error.ftl";
        }

        model.addAttribute("managerId", manager.getId());

        model.addAttribute("currentUserRoleId", getCurrentUser().getRoleId());

        return "entFlowControl/index.ftl";
    }

    /**
     * 企业列表查找
     */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        String managerIdStr = getRequest().getParameter("managerId");
        if (StringUtils.isNotBlank(managerIdStr)) {
            Long managerId = NumberUtils.toLong(managerIdStr);
            if (!isParentOf(managerId)) { // 当前用户不是指定用户或不是它的父节点，没有权限查看相应的节点信息
                res.setStatus(HttpStatus.SC_FORBIDDEN);
                return;
            }
        }

        /**
         * 查询参数: 企业名字、编码、效益
         */
        setQueryParameter("name", queryObject);
        setQueryParameter("code", queryObject);
        setQueryParameter("deleteFlag", queryObject);

        /**
         * 当前登录用户的管理员层级
         */
        Manager manager = getCurrentUserManager();
        queryObject.getQueryCriterias().put("managerId", manager.getId());

        // 页面查询参数中设定的管理员层级，如果设定了就会将上面的值覆盖
        setQueryParameter("managerId", queryObject);

        // 数据库查找符合查询条件的个数
        int entFlowControlCount = entFlowControlService
            .showForPageResultCount(queryObject);
        List<EntFlowControl> entFlowControlList = entFlowControlService
            .showForPageResultList(queryObject);

        if (entFlowControlList != null) {
            for (EntFlowControl entFlowControl : entFlowControlList) {
                // 地区全称
                Manager entManager = entManagerService
                    .getManagerForEnter(entFlowControl.getEnterId());
                if (entManager != null) {
                    String fullname = "";
                    entFlowControl.setCmManagerName(managerService
                        .getFullNameByCurrentManagerId(fullname,
                                entManager.getParentId()));
                }
            }
        }

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", entFlowControlList);
        json.put("total", entFlowControlCount);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启或关闭企业流控短信提醒
     */
    @RequestMapping("updateSmsAlertAjax")
    public void updateSmsAlertAjax(HttpServletRequest request,
                                   HttpServletResponse resp, Long entId, Integer fcSmsFlag)
        throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();

        if (entId == null || fcSmsFlag == null) {
            map.put("res", "fail");
            resp.getWriter().write(JSON.toJSONString(map));
            return;
        } else {

            if (entFlowControlService.updateSmsFlagByEntId(entId, fcSmsFlag)) {
                map.put("res", "success");
                resp.getWriter().write(JSON.toJSONString(map));
                return;
            } else {
                map.put("res", "fail");
                resp.getWriter().write(JSON.toJSONString(map));
                return;
            }
        }
    }

    /**
     * 显示页面:设置日上线金额
     */
    @RequestMapping(value = "/setEntFlowControl")
    public String setEntFlowControl(Model model, Long enterId) {

        model.addAttribute("enterId", enterId);

        return "entFlowControl/setEntFlowControl.ftl";
    }

    /**
     * 保存日上线金额
     */
    @RequestMapping(value = "saveEntFlowControl", method = RequestMethod.POST)
    public String saveSmsTemplate(ModelMap model, Long enterId, Long countUpper) {

        if (enterId == null) {
            model.addAttribute("errorMsg", "无效的企业ID");
            return "error.ftl";
        }

        entFlowControlService.updateEntFlowControl(enterId, countUpper,
            getCurrentUser().getId(), FlowControlType.setCountUpper.getCode());

        return "redirect:index.html";
    }

    /**
     * 显示页面:查看历史记录
     */
    @RequestMapping(value = "/queryEntFlowControlHistory")
    public String queryEntFlowControlHistory(Model model, Long enterId) {

        model.addAttribute("enterId", enterId);

        return "entFlowControl/history.ftl";
    }

    /**
     * 企业列表查找
     */
    @RequestMapping(value = "/searchHistory")
    public void searchHistory(QueryObject queryObject, HttpServletResponse res,
                              Long enterId) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        setQueryParameter("enterId", queryObject);
        // queryObject.getQueryCriterias().put("enterId", enterId.toString());

        // 数据库查找符合查询条件的个数
        int entFlowControlRecordCount = entFlowControlService
            .showHistoryForPageResultCount(queryObject);
        List<EntFlowControlRecord> entFlowControlRecordList = entFlowControlService
            .showHistoryForPageResultList(queryObject);

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", entFlowControlRecordList);
        json.put("total", entFlowControlRecordCount);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示页面:设置日追加额度
     */
    @RequestMapping(value = "/setEntFlowContorlAddition")
    public String setEntFlowContorlAddition(Model model, Long enterId) {

        model.addAttribute("enterId", enterId);

        return "entFlowControl/setEntFlowContorlAddition.ftl";
    }

    /**
     * 保存日追加额度
     */
    @RequestMapping(value = "/saveEntFlowControlAddition", method = RequestMethod.POST)
    public String saveEntFlowControlAddition(Model model, Long enterId,
                                             Long countAddition) {

        if (enterId == null) {
            model.addAttribute("errorMsg", "无效的企业ID");
            return "error.ftl";
        }

        entFlowControlService.updateEntFlowControlAddition(enterId,
            countAddition);

        return "redirect:index.html";
    }

}
