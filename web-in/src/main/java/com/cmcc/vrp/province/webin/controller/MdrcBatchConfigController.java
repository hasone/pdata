package com.cmcc.vrp.province.webin.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.ApprovalRequestStatus;
import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.enums.MdrcBatchConfigStatus;
import com.cmcc.vrp.enums.NoticeMsgStatus;
import com.cmcc.vrp.province.mdrc.enums.MdrcCardStatus;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigStatusRecordService;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.mdrc.service.MdrcCardmakerService;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.MdrcBatchConfigInfo;
import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.model.MdrcBatchConfigStatusRecord;
import com.cmcc.vrp.province.model.MdrcCardmakeDetail;
import com.cmcc.vrp.province.model.MdrcCardmaker;
import com.cmcc.vrp.province.model.MdrcMakecardRequestConfig;
import com.cmcc.vrp.province.model.MdrcTemplate;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.FileStoreService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.MdrcBatchConfigInfoService;
import com.cmcc.vrp.province.service.MdrcCardmakeDetailService;
import com.cmcc.vrp.province.service.MdrcMakecardRequestConfigService;
import com.cmcc.vrp.province.service.MdrcTemplateService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.S3Service;
import com.cmcc.vrp.province.service.XssService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.MD5;
import com.cmcc.vrp.util.QueryObject;

/**
 * @ClassName: MDRCController
 * @Description: 规则配置控制器
 * @author: luozuwu
 * @date: 2015年4月17日 上午10:06:08
 */
@Controller
@RequestMapping("/manage/mdrc/batchconfig")
public class MdrcBatchConfigController extends BaseController {
    private static Logger logger = Logger.getLogger(MdrcBatchConfigController.class);
    @Autowired
    MdrcBatchConfigService mdrcBatchConfigService;
    @Autowired
    MdrcCardmakerService mdrcCardmakerService;
    @Autowired
    ProductService productServcie;
    @Autowired
    MdrcTemplateService mdrcTemplateService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Autowired
    ApprovalRequestService approvalRequestService;
    @Autowired
    MdrcCardmakeDetailService mdrcCardmakeDetailService;
    @Autowired
    MdrcMakecardRequestConfigService mdrcMakecardRequestConfigService;
    @Autowired
    ApprovalRecordService approvalRecordService;
    @Autowired
    MdrcBatchConfigInfoService mdrcBatchConfigInfoService;
    @Autowired
    MdrcCardInfoService mdrcCardInfoService;
    @Autowired
    MdrcBatchConfigStatusRecordService mdrcBatchConfigStatusRecordService;
    @Autowired
    S3Service s3Service;
    @Autowired
    FileStoreService fileStoreService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    ManagerService managerService;
    @Autowired
    XssService xssService;

    /**
     * @param modelMap
     * @param queryObject
     * @return
     * @Title: index
     * @Description: 规则列表
     * @return: String
     * 
     *          edit by qinqinyan on 2017/08/02 for v1.21.1
     *          这里展示卡数据列表根据登录人员角色不同看到的范围不同
     */
    @RequestMapping(value = "/index")
    public String index(ModelMap modelMap, QueryObject queryObject) {
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        Manager manager = getCurrentUserManager();
        if (manager != null) {
            modelMap.addAttribute("mdrcBatchConfigStatus", MdrcBatchConfigStatus.toMap());// 传入配置规则的所有状态
            if (hasAuthority("ROLE_MDRC_ACTIVE_REQUEST")) {
                modelMap.addAttribute("MdrcActiveAuth", "true");// 当前用户是否有激活申请权限
            }
            if (hasAuthority("ROLE_MDRC_ACK_RECEIVE")) {
                modelMap.addAttribute("MdrcReceiveAuth", "true");// 当前用户是否有签收权限
            }
            return "mdrcBatchConfig/index.ftl";
        }
        return getLoginAddress();
    }

    /**
     * @param queryObject
     * @param res
     * @Title: search
     * @Description: 搜索
     * @return: void
     */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        // 企业名称
        setQueryParameter("enterpriseName", queryObject);
        // 批次号
        setQueryParameter("serialNumber", queryObject);

        // 状态
        setQueryParameter("status", queryObject);

        List<String> statusList = new ArrayList<String>();
        String status = (String) queryObject.getQueryCriterias().get("status");
        if (StringUtils.isNotBlank(status)) {
            statusList.add(status);// 前端传入的规则状态
        }

        queryObject.getQueryCriterias().put("statusList", statusList);

        int count = 0;
        List<MdrcBatchConfig> list = null;

        List<Enterprise> enterprises = enterprisesService.getEnterByManagerId(getCurrentUserManager().getId());
        if (enterprises != null && enterprises.size() > 0) {
            // enterprises = enterprisesService.getNormalEnterpriseList();
            queryObject.getQueryCriterias().put("enterprises", enterprises);
            // 数据库查找符合查询条件的个数
            count = mdrcBatchConfigService.queryCounts(queryObject);
            list = mdrcBatchConfigService.queryPagination(queryObject);
        }

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
     * @param modelMap
     * @return
     * @Title: addOrUpdate
     * @Description: 新建规则
     * @return: String
     */
    @RequestMapping(value = "/addOrEdit")
    public String addOrUpdate(ModelMap modelMap) {
        // 查询所有制卡商
        List<MdrcCardmaker> cardmakerList = mdrcCardmakerService.selectAllCardmaker();

        modelMap.addAttribute("cardmakerList", cardmakerList);

        // 查询所有模板列表
        // List<MdrcTemplate> templateList = mdrcTemplateService.selectAll();
        // modelMap.addAttribute("templateList", templateList);

        Map<String, List<MdrcTemplate>> templateMap = mdrcTemplateService.themeTemplates();
        modelMap.addAttribute("templateMap", templateMap);
        modelMap.addAttribute("minCardNum", getMinCardNum());
        modelMap.addAttribute("maxCardNum", getMaxCardNum());
        modelMap.addAttribute("maxDays", Integer.parseInt(getMaxDay()));

        // 是否需要审核
        ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                .selectByType(ApprovalType.MDRC_MakeCard_Approval.getCode());
        Boolean wheatherNeedApproval = true;
        if (approvalProcessDefinition.getStage().toString().equals("0")) {
            wheatherNeedApproval = false;
        }
        modelMap.addAttribute("wheatherNeedApproval", wheatherNeedApproval.toString());

        return "mdrcBatchConfig/addOrEdit.ftl";
    }

    /**
     * 获取审批通过的制卡申请列表
     * 
     * @param modelMap
     * @author qinqinyan
     */
    @RequestMapping("cardmakeIndex")
    public String cardmakeIndex(ModelMap modelMap) {
        if (getCurrentUser() != null && getCurrentUserManager() != null) {
            return "mdrcMakeCardApproval/cardmakeIndex.ftl";
        }
        return getLoginAddress();
    }

    /**
     * 获取审批通过的制卡申请列表
     * 
     * @param queryObject
     * @param res
     * @author qinqinyan
     */
    @RequestMapping("cardmakeIndexSearch")
    public void cardmakeIndexSearch(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        if (!StringUtils.isEmpty(getRequest().getParameter("startTime"))) {
            queryObject.getQueryCriterias().put("startTime", getRequest().getParameter("startTime"));
            queryObject.getQueryCriterias().put("endTime", getRequest().getParameter("endTime") + " 23:59:59");
        }

        ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                .selectByType(ApprovalType.MDRC_MakeCard_Approval.getCode());

        List<ApprovalRequest> totalApprovalRequests = null;
        Long totalCount = 0L;

        Administer administer = getCurrentUser();
        if (administer != null && administer.getId() != null) {
            if (approvalProcessDefinition != null && approvalProcessDefinition.getId() != null) {
                queryObject.getQueryCriterias().put("processId", approvalProcessDefinition.getId());
                queryObject.getQueryCriterias().put("result", ApprovalRequestStatus.APPROVED.getCode().toString());

                totalApprovalRequests = approvalRequestService.getApprovalRequestForMdrcCardmake(queryObject);
                totalCount = approvalRequestService.countApprovalRequestForMdrcCardmake(queryObject);
            }
        }
        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", totalApprovalRequests);
        json.put("total", totalCount);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @titile:preMakeCard
     */
    @RequestMapping("preMakeCard")
    public String preMakeCard(ModelMap map, Long requestId) {
        if (!approvalRequestService.isOverAuth(getCurrentAdminID(), requestId)) {
            MdrcCardmakeDetail mdrcCardmakeDetail = mdrcCardmakeDetailService.selectByRequestId(requestId);
            if (mdrcCardmakeDetail != null && mdrcCardmakeDetail.getCardmakerId() != null
                    && mdrcCardmakeDetail.getTemplateId() != null) {
                if (mdrcCardmakeDetail.getCardmakeStatus().toString()
                        .equals(Constants.MAKE_CARD_STATUS.MAKE_CARD.getResult())) {
                    map.addAttribute("errorMsg", "该条记录已经被处理！");
                    return "error.ftl";
                }

                MdrcTemplate mdrcTemplate = mdrcTemplateService.selectByPrimaryKey(mdrcCardmakeDetail.getTemplateId());
                MdrcCardmaker mdrcCardmaker = mdrcCardmakerService
                        .selectByPrimaryKeyForshow(mdrcCardmakeDetail.getCardmakerId());

                map.addAttribute("mdrcCardmakeDetail", mdrcCardmakeDetail);
                map.addAttribute("mdrcTemplate", mdrcTemplate);
                map.addAttribute("mdrcCardmaker", mdrcCardmaker);

                // 审批记录列表
                List<ApprovalRecord> approvalRecords = approvalRecordService.selectByRequestId(requestId);
                map.addAttribute("opinions", approvalRecords);

                // 是否需要审核
                ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                        .selectByType(ApprovalType.MDRC_MakeCard_Approval.getCode());
                Boolean wheatherNeedApproval = true;
                if (approvalProcessDefinition.getStage().toString().equals("0")) {
                    wheatherNeedApproval = false;
                }
                map.addAttribute("wheatherNeedApproval", wheatherNeedApproval.toString());

                return "mdrcMakeCardApproval/makecard.ftl";
            }
        }
        map.addAttribute("errorMsg", "参数缺失,未查找该条记录");
        return "error.ftl";
    }

    /**
     * 生成卡数据
     * <p>
     *
     * @param response
     * @param requestId
     * @throws IOException
     */
    @RequestMapping("saveAjax")
    public void saveAjax(Long requestId, HttpServletResponse response) throws IOException {       
        // 获取当前登陆用户
        Administer administer = getCurrentUser();
        Map<String, Object> map = new HashMap<String, Object>();

        if(approvalRequestService.isOverAuth(administer.getId(), requestId)){
            map.put("code", "2");
            map.put("msg", "你无权访问");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }
        
        MdrcCardmakeDetail mdrcCardmakeDetail = mdrcCardmakeDetailService.selectByRequestId(requestId);

        if (mdrcCardmakeDetail.getCardmakeStatus().toString()
                .equals(Constants.MAKE_CARD_STATUS.MAKE_CARD.getResult())) {
            // 这条记录已经被处理
            map.put("code", "2");
            map.put("msg", "该条记录已经被处理,生成卡数据失败!");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }
        // 校验制卡商
        MdrcCardmaker mdrcCardmaker = mdrcCardmakerService.selectByPrimaryKey(mdrcCardmakeDetail.getCardmakerId());
        if (mdrcCardmaker == null) {
            map.put("code", "1");
            map.put("msg", "该制卡商已被删除,生成卡数据失败!");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }

        // 生成卡数据
        try {
            if (mdrcCardmakeDetailService.makecard(requestId, administer.getId())) {
                map.put("code", "ok");
            } else {
                map.put("code", "-1");
                map.put("msg", "生成卡数据失败！");
            }
        } catch (RuntimeException e) {
            logger.info("生成卡数据失败，失败原因：" + e.getMessage());
            map.put("code", "-1");
            map.put("msg", "生成卡数据失败！");
        }
        response.getWriter().write(JSON.toJSONString(map));
        return;
    }

    /**
     * 制卡详情
     * 
     * @param requestId
     * @param map
     * @return
     * @author qinqinyan
     */
    @RequestMapping("makecardDetail")
    public String makecardDetail(ModelMap map, Long requestId) {
        if (!approvalRequestService.isOverAuth(getCurrentAdminID(), requestId)) {
            MdrcMakecardRequestConfig mdrcMakecardRequestConfig = mdrcMakecardRequestConfigService
                    .selectByRequestId(requestId);
            MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService
                    .selectByPrimaryKey(mdrcMakecardRequestConfig.getConfigId());

            map.put("mdrcMakecardRequestConfig", mdrcMakecardRequestConfig);
            map.put("mdrcBatchConfig", mdrcBatchConfig);

            // 审批记录列表
            List<ApprovalRecord> approvalRecords = approvalRecordService.selectByRequestId(requestId);
            map.addAttribute("opinions", approvalRecords);

            String provinceFlag = getProvinceFlag();
            map.addAttribute("provinceFlag", provinceFlag);

            MdrcCardmakeDetail mdrcCardmakeDetail = mdrcCardmakeDetailService.selectByRequestId(requestId);
            map.put("mdrcCardmakeDetail", mdrcCardmakeDetail);

            MdrcTemplate mdrcTemplate = mdrcTemplateService.selectByPrimaryKey(mdrcCardmakeDetail.getTemplateId());
            MdrcCardmaker mdrcCardmaker = mdrcCardmakerService
                    .selectByPrimaryKeyForshow(mdrcCardmakeDetail.getCardmakerId());
            map.put("mdrcTemplate", mdrcTemplate);
            map.put("mdrcCardmaker", mdrcCardmaker);
            // 是否需要审核
            ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                    .selectByType(ApprovalType.MDRC_MakeCard_Approval.getCode());
            Boolean wheatherNeedApproval = true;
            if (approvalProcessDefinition.getStage().toString().equals("0")) {
                wheatherNeedApproval = false;
            }
            map.addAttribute("wheatherNeedApproval", wheatherNeedApproval.toString());

            return "mdrcMakeCardApproval/makecardDetail.ftl";
        }
        map.addAttribute("errorMsg", "未查询到该条记录详情!");
        return "error.ftl";
    }

    /**
     * @Title: showDetail
     */
    @RequestMapping("detail")
    public String showDetail(ModelMap modelMap, Long id) {
        Manager manager = getCurrentUserManager();
        if(manager == null){
            return getLoginAddress();
        }
        /*if (mdrcBatchConfigService.isOverAuth(getCurrentAdminID(), id)) {
            modelMap.addAttribute("errorMsg", "必要参数缺失");
            return "error.ftl";
        }*/
        MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.getConfigDetailsById(id);
        if(mdrcBatchConfig==null){
            modelMap.addAttribute("errorMsg", "必要参数缺失");
            return "error.ftl";
        }
        if(!managerService.managedByManageId(mdrcBatchConfig.getEnterpriseId(), manager.getId())){
            modelMap.addAttribute("errorMsg", "对不起，您无权限查看该批次卡详情。");
            return "error.ftl";
        }
        modelMap.addAttribute("mdrcBatchConfig", mdrcBatchConfig);// 配置详细信息
        modelMap.addAttribute("mdrcBatchConfigStatus", MdrcBatchConfigStatus.toMap());// 传入配置规则的所有状态
        modelMap.addAttribute("noticeMsgStatus", NoticeMsgStatus.toMap());// 传入短信的所有状态
        // 批次状态变更记录,只显示物流信息：新制卡、制卡中、已邮寄、已失效
        List<Integer> statusList = new ArrayList<Integer>();
        statusList.add(MdrcBatchConfigStatus.NOT_DOWNLOAD.getCode());
        statusList.add(MdrcBatchConfigStatus.DOWNLOADED.getCode());
        statusList.add(MdrcBatchConfigStatus.STORED.getCode());
        statusList.add(MdrcBatchConfigStatus.POST.getCode());
        statusList.add(MdrcBatchConfigStatus.USELESS.getCode());
        // statusList.add(MdrcBatchConfigStatus.EXPIRED.getCode());
        List<MdrcBatchConfigStatusRecord> records = mdrcBatchConfigStatusRecordService.selectByConfigId(id, statusList);
        modelMap.addAttribute("records", records);

        return "mdrcBatchConfig/detail.ftl";
    }

    /**
     * 
     * @Title: receive
     * @Description: 签收页面
     * @param modelMap
     * @param id
     * @return
     * @return: String
     * 
     */
    @RequestMapping("receive")
    public String receive(ModelMap modelMap, Long id) {
        if (mdrcBatchConfigService.isOverAuth(getCurrentAdminID(), id)) {
            modelMap.addAttribute("errorMsg", "必要参数缺失");
            return "error.ftl";
        }

        MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.getConfigDetailsById(id);
        modelMap.addAttribute("mdrcBatchConfig", mdrcBatchConfig);// 配置详细信息

        // 批次状态变更记录,只显示物流信息：新制卡、制卡中、已邮寄、已失效
        List<Integer> statusList = new ArrayList<Integer>();
        statusList.add(MdrcBatchConfigStatus.NOT_DOWNLOAD.getCode());
        statusList.add(MdrcBatchConfigStatus.DOWNLOADED.getCode());
        statusList.add(MdrcBatchConfigStatus.STORED.getCode());
        statusList.add(MdrcBatchConfigStatus.POST.getCode());
        statusList.add(MdrcBatchConfigStatus.USELESS.getCode());
        // statusList.add(MdrcBatchConfigStatus.EXPIRED.getCode());
        List<MdrcBatchConfigStatusRecord> records = mdrcBatchConfigStatusRecordService.selectByConfigId(id, statusList);
        modelMap.addAttribute("records", records);

        return "mdrcBatchConfig/receive.ftl";
    }

    /**
     * 
     * @Title: ackReceive
     * @Description: TODO
     * @param modelMap
     * @param id
     * @return
     * @return: String
     */
    @RequestMapping("ackReceive")
    public String ackReceive(ModelMap modelMap, Long id, String name, String mobile,
            MultipartHttpServletRequest multipartRequest) {
        // 参数校验
        if (id == null || StringUtils.isBlank(name) || StringUtils.isBlank(mobile) || mdrcBatchConfigService.isOverAuth(getCurrentAdminID(), id)) {
            logger.info("参数缺失，id = " + id + ", name = " + name + ", mobile = " + mobile);
            modelMap.addAttribute("errorMsg", "必要参数缺失");
            return "error.ftl";
        }
              
        // 合法性校验
        MdrcBatchConfig config = mdrcBatchConfigService.select(id);
        if (config == null || config.getStatus().equals(MdrcBatchConfigStatus.STORED.getCode())) {// 是否已被签收过
            logger.info("无效参数，id = " + id + ", name = " + name + ", mobile = " + mobile);
            modelMap.addAttribute("errorMsg", "该批次号不存在");
            return "error.ftl";
        }

        // 合法性校验
        MdrcBatchConfigInfo mdrcBatchConfigInfo = mdrcBatchConfigInfoService
                .selectByPrimaryKey(config.getConfigInfoId());
        if (mdrcBatchConfigInfo == null) {
            logger.info("批次号不存在，id = " + id + ", name = " + name + ", mobile = " + mobile);
            modelMap.addAttribute("errorMsg", "该批次号不存在");
            return "error.ftl";
        }

        // 校验签收凭证
        String errMsg = checkSignVoucher(multipartRequest);
        if (StringUtils.isBlank(errMsg)) {// 校验通过，开始上传文件
            if (!uploadFile(multipartRequest, mdrcBatchConfigInfo)) {
                logger.info("签收文件上传失败！");
                modelMap.addAttribute("errorMsg", "签收凭证上传失败！");
                return "error.ftl";
            }
        } else {
            logger.info("未检测到签收凭证文件！");
            modelMap.addAttribute("errorMsg", "请上传签收凭证！");
            return "error.ftl";
        }

        // 插入更新记录
        MdrcBatchConfigStatusRecord record = new MdrcBatchConfigStatusRecord();
        record.setConfigId(id);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setPreStatus(config.getStatus());
        record.setNowStatus(MdrcBatchConfigStatus.STORED.getCode());
        record.setUpdatorId(getCurrentAdminID());
        record.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        if (!mdrcBatchConfigStatusRecordService.insertSelective(record)) {
            logger.info("生成更新记录失败，id = " + id + ", name = " + name + ", mobile = " + mobile);
            modelMap.addAttribute("errorMsg", "该批次号不存在");
            return "error.ftl";
        }

        config.setStatus(MdrcBatchConfigStatus.STORED.getCode());// 已签收
        if (!mdrcBatchConfigService.update(config)) {
            logger.info("更新失败，id = " + id + ", name = " + name + ", mobile = " + mobile);
            modelMap.addAttribute("errorMsg", "系统内部错误！");
            return "error.ftl";
        }

        // 保存签收信息
        mdrcBatchConfigInfo.setExpressEntName("顺丰快运");
        mdrcBatchConfigInfo.setReceiverMobile(mobile);
        mdrcBatchConfigInfo.setReceiverName(xssService.stripXss(name));
        if (!mdrcBatchConfigInfoService.updateByPrimaryKeySelective(mdrcBatchConfigInfo)) {
            logger.info("更新签收信息失败，id = " + id + ", name = " + name + ", mobile = " + mobile);
            modelMap.addAttribute("errorMsg", "系统内部错误！");
            return "error.ftl";
        }

        // 批量入库卡号：新制卡 --》 已入库
        if (!mdrcCardInfoService.batchUpdateByConfigId(id, MdrcCardStatus.NEW.getCode(),
                MdrcCardStatus.STORED.getCode())) {
            logger.info("入库失败，id = " + id + ", name = " + name + ", mobile = " + mobile);
            modelMap.addAttribute("errorMsg", "系统内部错误！");
            return "error.ftl";
        }

        return index(modelMap, new QueryObject());
    }

    /**
     * 给制卡专员下发通知短信，告知已生成卡数据，可下载
     * <p>
     *
     * @param request
     * @param response
     * @param id
     * @throws IOException
     */
    @RequestMapping("sendMessage")
    @ResponseBody
    public void sendMessage(HttpServletRequest request, HttpServletResponse response, Long id) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();// 返回给Ajax请求的值
        if (mdrcBatchConfigService.isOverAuth(getCurrentAdminID(), id)) {
            map.put("message", "failure");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }
        if (!mdrcBatchConfigService.notifyCardmaker(id)) {
            map.put("message", "failure");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }
        map.put("message", "success");
        response.getWriter().write(JSON.toJSONString(map));
    }

    private String getMinCardNum() {
        return globalConfigService.get(GlobalConfigKeyEnum.MDRC_MINIMUM_NUM.getKey());
    }

    private String getMaxCardNum() {
        return globalConfigService.get(GlobalConfigKeyEnum.MDRC_MAXIMUM_NUM.getKey());
    }

    private String getMaxDay() {
        return globalConfigService.get(GlobalConfigKeyEnum.MDRC_VALID_MAX_DAYS.getKey());
    }

    /**
     * 
     * @Title: checkSignVoucher
     * @Description: 签收文件校验
     * @param multipartRequest
     * @param MdrcBatchConfigInfo
     * @return
     * @return: String
     */
    private String checkSignVoucher(MultipartHttpServletRequest multipartRequest) {
        Iterator<String> itr = multipartRequest.getFileNames();
        MultipartFile multipartFile = null;

        String fileName = "";
        while (itr != null && itr.hasNext()) {
            multipartFile = multipartRequest.getFile(itr.next());
            String originalFilename = multipartFile.getOriginalFilename();
            if (!StringUtils.isEmpty(originalFilename)) {
                if (multipartFile.getName().equals("signVoucher")) {
                    fileName = originalFilename;
                    break;
                }
            }
        }
        if (StringUtils.isBlank(fileName)) {
            return "请上传文件";
        }

        if (!s3Service.checkFile(multipartRequest)) {
            logger.info("上传的图片只支持jpg、jpeg和png这三种格式！");
            return "上传的图片只支持jpg、jpeg和png这三种格式！";
        }

        if (s3Service.checkFileSize(multipartRequest)) {
            logger.info("上传的图片大于5MB");
            return "上传的图片大于5MB";
        }

        return null;
    }

    /**
     * 
     * @Title: uploadFile
     * @Description: 上传签收凭证文件
     * @param multipartRequest
     * @param MdrcBatchConfigInfo
     * @return
     * @return: boolean
     */
    private boolean uploadFile(MultipartHttpServletRequest multipartRequest, MdrcBatchConfigInfo mdrcBatchConfigInfo) {
        try {
            Iterator<String> itr = multipartRequest.getFileNames();
            File file = new File("dest");
            // 1、上传文件
            while (itr != null && itr.hasNext()) {
                // 获得文件实例
                MultipartFile multipartFile = multipartRequest.getFile(itr.next());

                try {
                    multipartFile.transferTo(file);
                } catch (IllegalStateException e) {
                    logger.error(e.getMessage());
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }

                String originalFilename = multipartFile.getOriginalFilename();
                String key = MD5.md5(UUID.randomUUID().toString());
                if (!StringUtils.isEmpty(originalFilename) && mdrcBatchConfigInfo != null) {
                    if (multipartFile.getName().equals("signVoucher")) {
                        if (!StringUtils.isEmpty(mdrcBatchConfigInfo.getReceiveKey())) {
                            key = mdrcBatchConfigInfo.getReceiveKey();
                        }
                        fileStoreService.save(key, file);
                        mdrcBatchConfigInfo.setReceiveKey(key);
                        mdrcBatchConfigInfo.setReceiveFileName(originalFilename);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            logger.error("上传文件时出错，错误原因" + e.getMessage());
            return false;
        }
    }

    /**
     * 
     * @Title: getImage
     * @Description: 获取文件
     * @param filename
     * @param response
     * @param request
     * @return: void
     */
    @RequestMapping(value = "/getImage")
    public void getImage(String filename, HttpServletResponse response, HttpServletRequest request) {
        if (!StringUtils.isEmpty(filename)) { // "qrCode".equals(type) &&
            getImageFromS3(response, filename, request);
        }
    }

    private void getImageFromS3(HttpServletResponse response, String key, HttpServletRequest request) {
        InputStream inputStream = fileStoreService.get(key);
        if (inputStream != null) {
            try {
                StreamUtils.copy(inputStream, response.getOutputStream());
            } catch (IOException e) {
                logger.info("输出结果流时抛出异常,错误信息为:" + e.getMessage());
            }
        }
    }
}
