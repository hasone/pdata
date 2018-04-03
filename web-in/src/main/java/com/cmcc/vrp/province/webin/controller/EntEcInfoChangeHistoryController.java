/**
 * 
 */
package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.EcApprovalDetail;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.EcApprovalDetailService;
import com.cmcc.vrp.util.QueryObject;

/**
 * <p>Title:EntEcInfoChangeHistoryController </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年10月24日
 */
@Controller
@RequestMapping("manage/entecinfochangehistory")
public class EntEcInfoChangeHistoryController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(EntEcInfoChangeHistoryController.class);
    
    @Autowired
    ApprovalRequestService approvalRequestService;
    @Autowired
    EcApprovalDetailService ecApprovalDetailService;
    @Autowired
    ApprovalRecordService approvalRecordService;
    
    
    /**
     * 
     * @Title: index
     * @Description: 
     */
    @RequestMapping("index")
    public String index(ModelMap model, QueryObject queryObject, Long entId) {
        if(queryObject != null){
            model.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        model.put("entId", entId);
        return "ecChangeHistory/index.ftl";
    }

    /**
     * @param queryObject
     * @param res
     * @Title: 企业列表查找
     */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 查询参数: 企业名字、编码、效益
         */
        setQueryParameter("entId", queryObject);

        // 数据库查找符合查询条件的个数
        List<ApprovalRequest> list = approvalRequestService.queryApprovalRequestsForEcChange(queryObject);
        Long count = approvalRequestService.countApprovalRequestsForEcChange(queryObject);

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @param id 审核请求id
     */
    @RequestMapping("detail")
    public String detail(ModelMap modelmap, HttpServletRequest request, Long id) {
        if (id != null) {
            ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(id);
            if (approvalRequest != null && approvalRequest.getEntId() != null) {
                modelmap.addAttribute("approvalRequest", approvalRequest);
                //EC变更信息
                EcApprovalDetail ecApprovalDetail = ecApprovalDetailService.selectByRequestId(approvalRequest.getId());
              
                if (ecApprovalDetail == null) {
                    modelmap.addAttribute("errorMsg", "数据库中没有相关EC变更信息，企业Id: " + approvalRequest.getEntId());
                    return "error.ftl";
                }
                modelmap.addAttribute("ecApprovalDetail", ecApprovalDetail);

                //显示历史审批意见
                //审批记录列表
                List<ApprovalRecord> approvalRecords = approvalRecordService.selectByRequestIdAll(id);

                modelmap.addAttribute("approvalRecords", approvalRecords);

                //审核请求记录id
                modelmap.addAttribute("requestId", id);
                //审批流程id
                modelmap.addAttribute("processId", approvalRequest.getProcessId());
              
                //type=0时页面不展示审核意见
                return "ecChangeHistory/detail.ftl";
            }
            logger.info("数据库中没有相关审批记录Id: " + id);
        }
        return "redirect:approvalIndex.html?approvalType=" +
          	  ApprovalType.Ec_Approval.getCode().toString();
    }
    
    /**
    * 编辑页面载入可用的目标产品
    */
    @RequestMapping("/detailAjax")
    public void detailAjax(HttpServletRequest request, HttpServletResponse resp,Long id) throws IOException{
        Map<String, Object> params =new HashMap<String, Object>();
        ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(id);
        if (approvalRequest != null && approvalRequest.getEntId() != null) {//审批记录列表
            List<ApprovalRecord> approvalRecords = approvalRecordService.selectByRequestIdAll(id);    
            params.put("success", "true");
            params.put("datas", approvalRecords);
            resp.getWriter().write(JSON.toJSONString(params));
        }
    }

}
