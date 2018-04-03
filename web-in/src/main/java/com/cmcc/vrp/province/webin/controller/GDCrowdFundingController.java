package com.cmcc.vrp.province.webin.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.PaymentStatus;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.CrowdfundingActivityDetail;
import com.cmcc.vrp.province.model.CrowdfundingQueryUrl;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterprisesExtInfo;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.Masking;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityBlackAndWhiteService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityTemplateService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.CrowdFundingService;
import com.cmcc.vrp.province.service.CrowdfundingActivityDetailService;
import com.cmcc.vrp.province.service.CrowdfundingQueryUrlService;
import com.cmcc.vrp.province.service.EnterprisesExtInfoService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.FileStoreService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.MaskingService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.S3Service;
import com.cmcc.vrp.province.service.VirtualProductService;
import com.cmcc.vrp.province.service.XssService;
import com.cmcc.vrp.province.service.impl.LotteryServiceImpl;
import com.cmcc.vrp.util.CSVUtil;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.MD5;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.webservice.crowdfunding.CrowdfundingCallbackService;

/**
 * Created by qinqinyan on 2017/1/9. 广东流量众筹控制器
 */
@Controller
@RequestMapping("/manage/crowdFunding")
public class GDCrowdFundingController extends BaseController {
    /**
     * 上传文件大小的限制
     */
    public static final int MAX_FILE_SIZE = 1;// 单位MB

    /**
     * 黑白名单文件名
     */
    public static final String PHONELIST_TXT = "phonelist.txt";

    /**
     * 报名用户文件名
     */
    public static final String JOINUSER_TXT = "joinUsers.txt";

    private static final Logger logger = 
    		LoggerFactory.getLogger(GDCrowdFundingController.class);

    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    ActivityPrizeService activityPrizeService;
    @Autowired
    ActivityInfoService activityInfoService;
    @Autowired
    ActivityTemplateService activityTemplateService;
    @Autowired
    ApprovalRecordService approvalRecordService;
    @Autowired
    @Qualifier("lotteryService")
    LotteryServiceImpl lotteryService;
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    @Autowired
    ManagerService managerService;
    @Autowired
    ProductService productService;
    @Autowired
    AccountService accountService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    VirtualProductService virtualProductService;
    @Autowired
    CrowdfundingActivityDetailService crowdfundingActivityDetailService;
    @Autowired
    GlobalConfigService getGlobalConfigService;
    @Autowired
    CrowdFundingService crowdFundingService;
    @Autowired
    ActivityBlackAndWhiteService activityBlackAndWhiteService;
    @Autowired
    CrowdfundingCallbackService crowdfundingCallbackService;
    @Autowired
    S3Service s3Service;
    @Autowired
    FileStoreService fileStoreService;
    @Autowired
    EnterprisesExtInfoService enterprisesExtInfoService;
    @Autowired
    CrowdfundingQueryUrlService crowdfundingQueryUrlService;
    @Autowired
    MaskingService maskingService;
    @Autowired
    XssService xssService;

    /**
     * @Title: index
     * @Description: 活动列表页
     * @return: String
     * @author qinqinyan
     */
    @RequestMapping(value = "/index")
    public String index(ModelMap modelMap, QueryObject queryObject, String needmask) {
        
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        
        // 获取当前用户信息
        Manager manager = getCurrentUserManager();
        Administer administer = getCurrentUser();
        if (manager == null || manager.getId() == null || manager.getRoleId() == null || administer == null
                || administer.getId() == null) {
            return getLoginAddress();
        }

        // 判断是否需要审核
        Boolean approvalFlag = approvalProcessDefinitionService
                .wheatherNeedApproval(ApprovalType.Activity_Approval.getCode());
        modelMap.addAttribute("approvalFlag", approvalFlag.toString());

        modelMap.addAttribute("managerId", manager.getId());
        modelMap.addAttribute("currUserId", administer.getId());
        modelMap.addAttribute("roleId", manager.getRoleId());

//        Masking masking = maskingService.getByAdminId(administer.getId());
//        
//        if (!StringUtils.isEmpty(needmask)
//                || (masking != null
//                && masking.getCrowdfundingMask() == 0)) {
//            return "crowd-funding/index.ftl";
//        }
//        return "crowd-funding/createCrowdFunding.ftl";
        return "crowd-funding/index.ftl";
    }
    
    /**
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/createCrowdFunding")
    public String createCrowdFunding(ModelMap modelMap) {

        return "crowd-funding/createCrowdFunding.ftl";
    }
    /**
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/approvalCrowdFunding")
    public String approvalCrowdFunding(ModelMap modelMap) {

        return "crowd-funding/approvalCrowdFunding.ftl";
    }
    
    /**
     * @param modelMap
     * @param request
     * @return
     * @throws IOException 
     */
    @RequestMapping(value = "/maskingAjax")
    public void maskingAjax(HttpServletRequest request, HttpServletResponse response,
            Integer flag) throws IOException {
        
        Administer administer = getCurrentUser();
        Masking masking = new Masking();
        masking.setAdminId(administer.getId());
        masking.setCrowdfundingMask(flag);
        
        Boolean result = maskingService.insertOrUpdate(masking);
        
        Map<String, String> returnMap = new HashMap();
        returnMap.put("result", result.toString());
        response.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }

    /**
     * @param queryObject
     * @param res
     */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        Long count = 0L;
        List<Activities> list = new ArrayList<Activities>();
        Manager manager = getCurrentUserManager();
        if (manager != null && manager.getId() != null) {
            /**
             * 查询参数：企业名称，活动名称，活动状态
             */
            setQueryParameter("name", queryObject);
            setQueryParameter("status", queryObject);
            //setQueryParameter("entName", queryObject);
            setQueryParameter("startTime", queryObject);
            setQueryParameter("endTime", queryObject);

            if (queryObject.getQueryCriterias().get("endTime") != null) {
                queryObject.getQueryCriterias().put("startTime",
                        formatTime(queryObject.getQueryCriterias().get("startTime").toString()));
                queryObject.getQueryCriterias().put("endTime",
                        formatTime(queryObject.getQueryCriterias().get("endTime").toString()));
            }

            queryObject.getQueryCriterias().put("type", ActivityType.CROWD_FUNDING.getCode());
            queryObject.getQueryCriterias().put("managerId", manager.getId());

            list = activitiesService.selectByMapForGDCrowdFunding(queryObject.toMap());
            count = activitiesService.countByMapGDCrowdFunding(queryObject.toMap());

            // 校验活动状态,如果存在未上架又已经结束的活动，则在此更改活动状态
            List<Activities> updateActivities = new ArrayList<Activities>();
            for (Activities activities : list) {
                if (activities.getStatus().toString().equals(ActivityStatus.FINISH_APPROVAL.getCode().toString())
                        && activities.getEndTime().getTime() < System.currentTimeMillis()) {
                    activities.setStatus(ActivityStatus.END.getCode());

                    Activities item = new Activities();
                    item.setId(activities.getId());
                    updateActivities.add(item);
                }
            }
            activitiesService.batchChangeStatus(updateActivities, ActivityStatus.END.getCode());
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
            logger.error(e.getMessage());
        }
    }

    /**
     * 校正时间格式
     */
    private String formatTime(String time) {
        String[] timeArray = time.split("-");
        String str = timeArray[timeArray.length - 1];
        String str0 = str.substring(0, 2);
        String str1 = str.substring(2);
        String formatTime = timeArray[0] + "-" + timeArray[1] + "-" + str0
                + " " + str1;
        //System.out.println(formatTime);    
        return formatTime;
    }

    /**
     * 新建页面
     *
     * @author qinqinyan
     */
    @RequestMapping("create")
    public String create(ModelMap modelMap) {
        Administer currentAdmin = getCurrentUser();
        if (currentAdmin == null) {
            return getLoginAddress();
        }
        Manager manager = getCurrentUserManager();
        if (manager != null) {
            List<Enterprise> enterprises = enterprisesService.getNomalEnterByManagerId(manager.getId());
            modelMap.addAttribute("enterprises", enterprises);
            if (enterprises != null
                    && enterprises.size() != 0) {
                EnterprisesExtInfo enterprisesExtInfo = enterprisesExtInfoService.get(enterprises.get(0).getId());
                modelMap.addAttribute("enterprisesExtInfo", enterprisesExtInfo);
            }
        }
        return "crowd-funding/create.ftl";
    }

    /**
     * 新建页面
     *
     * @author qinqinyan
     */
    @RequestMapping("edit")
    public String edit(ModelMap modelMap, String activityId) {
        Administer currentAdmin = getCurrentUser();
        Manager manager = getCurrentUserManager();
        if (currentAdmin == null || manager == null) {
            return getLoginAddress();
        }

        if (StringUtils.isEmpty(activityId)) {
            modelMap.addAttribute("errorMsg", "参数缺失，查找不到该活动!");
            return "error.ftl";
        }

        Activities activities = activitiesService.selectByActivityId(activityId);
        List<ActivityPrize> activityPrizeList = activityPrizeService.selectByActivityId(activityId);
        CrowdfundingActivityDetail crowdfundingActivityDetail = crowdfundingActivityDetailService
                .selectByActivityId(activityId);

        modelMap.addAttribute("activities", activities);
        // modelMap.addAttribute("activityPrizeList", activityPrizeList);
        modelMap.addAttribute("crowdfundingActivityDetail", crowdfundingActivityDetail);
        
        List<Enterprise> enterprises = enterprisesService.getNomalEnterByManagerId(manager.getId());
        modelMap.addAttribute("enterprises", enterprises);
        if (enterprises != null
                && enterprises.size() != 0) {
            EnterprisesExtInfo enterprisesExtInfo = enterprisesExtInfoService.get(enterprises.get(0).getId());
            modelMap.addAttribute("enterprisesExtInfo", enterprisesExtInfo);
            /**
             * 校验该用户所管理的企业是否包含该活动对应的企业
             * */
            int i = 0;
            for (Enterprise item : enterprises) {
                if (item.getId().toString().equals(activities.getEntId().toString())) {
                    break;
                }
                i++;
                if (i == enterprises.size()) {
                    modelMap.addAttribute("errorMsg", "对不起,您无权限编辑该活动!");
                    return "error.ftl";
                }
            }
        }
        
        
        Enterprise enter = enterprisesService.selectById(activities.getEntId());
        modelMap.addAttribute("enter", enter);

        modelMap.put("activityPrizeList", JSON.toJSONString(activityPrizeList));
        if (crowdfundingActivityDetail.getUserList() != null
                && crowdfundingActivityDetail.getUserList() == 3) {
            CrowdfundingQueryUrl crowdfundingQueryUrl = 
                    crowdfundingQueryUrlService.getByCrowdfundingActivityDetailId(crowdfundingActivityDetail.getId());
            String url = crowdfundingQueryUrl.getQueryUrl().split("//")[1];
            String prefix= crowdfundingQueryUrl.getQueryUrl().split(":")[0];

            modelMap.addAttribute("crowdfundingQueryUrl", url);
            modelMap.addAttribute("prefix", prefix);
        }

        return "crowd-funding/edit.ftl";
    }

    /**
     * 获取产品列表
     *
     * @param enterpriseId 产品类型 M:移动包，T:电信包，U：联通包
     * @author qinqinyan
     */
    @RequestMapping("searchProduct")
    public void searchProduct(QueryObject queryObject, HttpServletResponse response, Long enterpriseId) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        queryObject.getQueryCriterias().put("isp", "M");
        if (enterpriseId != null) {
            queryObject.getQueryCriterias().put("enterId", enterpriseId.toString());
        }
        queryObject.setPageSize(-1);
        List<Product> products = productService.getProductByEntIdAndIsp(queryObject);
        int count = productService.countProductByEntIdAndIsp(queryObject);

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", products);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            response.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 流量众筹活动详情页面
     */
    @RequestMapping("detail")
    public String detail(ModelMap modelMap, String activityId) {
        Administer currentAdmin = getCurrentUser();
        if (currentAdmin == null) {
            return getLoginAddress();
        }
        Activities activity = null;
        CrowdfundingActivityDetail crowdfundingActivityDetail = null;
        if (StringUtils.isEmpty(activityId) || (activity = activitiesService.selectByActivityId(activityId)) == null
                || (crowdfundingActivityDetail = crowdfundingActivityDetailService
                .selectByActivityId(activityId)) == null) {
            logger.error("活动不存在，activityId={}", activityId);
            modelMap.put("errorMsg", "活动记录不存在");
            return "error.ftl";
        }

        Enterprise enterprise = enterprisesService.selectByPrimaryKeyForActivity(activity.getEntId());
        if (enterprise == null) {
            logger.error("活动activityId={}对应的企业信息enterId={}查找不到.", activityId, activity.getEntId());
            modelMap.put("errorMsg", "活动对应的企业信息不存在");
            return "error.ftl";
        }
        modelMap.addAttribute("enterprise", enterprise);

        // 历史审核记录
        List<ApprovalRecord> approvalRecords = approvalRecordService.selectByActivityIdForActivity(activityId);
        modelMap.put("opinions", approvalRecords);

        modelMap.addAttribute("activity", activity);
        modelMap.addAttribute("crowdfundingActivityDetail", crowdfundingActivityDetail);

        // 奖品
        List<ActivityPrize> prizes = activityPrizeService.getDetailByActivityId(activity.getActivityId());
        modelMap.put("prizes", prizes);

        String encryptActivityId = activitiesService.encryptActivityId(activityId);
        modelMap.put("encryptActivityId", encryptActivityId);
        if (crowdfundingActivityDetail.getUserList() != null
                && crowdfundingActivityDetail.getUserList() == 3) {
            CrowdfundingQueryUrl crowdfundingQueryUrl = 
                    crowdfundingQueryUrlService.getByCrowdfundingActivityDetailId(crowdfundingActivityDetail.getId());
            
            modelMap.put("crowdfundingQueryUrl", crowdfundingQueryUrl);
        }

        return "crowd-funding/detail.ftl";
    }

    /**
     * 营销活动提交审批申请
     *
     * @author qinqinyan
     */
    @RequestMapping("submitActivityApprovalAjax")
    public void submitActivityApprovalAjax(HttpServletResponse resp, String activityId, Long currentId, Long roleId)
            throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();

        if(StringUtils.isEmpty(activityId) || currentId==null || roleId==null){
            map.put("submitRes", "fail");
            map.put("errorMsg", "参数异常，查找不到该活动");
        }else{
            Activities activity = activitiesService.selectByActivityId(activityId);

            //众筹活动要求：只要存在一个活动在审核中或者上架或者进行中，则不能提交审核
            if(activity.getType().toString().equals(ActivityType.CROWD_FUNDING.getCode().toString())){
                Map<String, Object> searchMap = new HashMap<String, Object>();
                searchMap.put("entId", activity.getEntId());

                List<Integer> statusList = new ArrayList<Integer>();
                statusList.add(ActivityStatus.ON.getCode());
                //statusList.add(ActivityStatus.FINISH_APPROVAL.getCode());
                statusList.add(ActivityStatus.SUBMIT_APPROVAL.getCode());
                statusList.add(ActivityStatus.PROCESSING.getCode());
                searchMap.put("status", statusList);
                searchMap.put("type", ActivityType.CROWD_FUNDING.getCode());

                List<Activities> activityList = activitiesService.selectByEntId(searchMap);
                if(activityList!=null && activityList.size()>0){
                    map.put("submitRes", "fail");
                    map.put("errorMsg", "企业同一时间只能上架一个众筹活动");
                    resp.getWriter().write(JSON.toJSONString(map));
                    return;
                }
            }
            map = activitiesService.submitApproval(activityId, currentId, roleId);
        }
        resp.getWriter().write(JSON.toJSONString(map));
        return;
    }

    /**
     * 保存编辑活动
     *
     * @author qinqinyan
     */
    @RequestMapping(method = RequestMethod.POST, value = "/saveEditAjax")
    public void saveEditAjax(MultipartHttpServletRequest multipartRequest, HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        Manager manager = getCurrentUserManager();
        Administer administer = getCurrentUser();
        if (administer == null || manager == null || manager.getId() == null) {
            logger.info("用户异常,保存失败!");
            map.put("fail", "用户异常,保存失败!");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }

        String activityParams = request.getParameter("activity");
        String crowdfundingActivityDetailParams = request.getParameter("crowdfundingActivityDetail");
        String activityPrizesParams = request.getParameter("activityPrizes");
        String phones = request.getParameter("phones");
        String queryurl = request.getParameter("queryurl");
        if (StringUtils.isEmpty(activityParams) || StringUtils.isEmpty(crowdfundingActivityDetailParams)
                || StringUtils.isEmpty(activityPrizesParams)) {
            logger.info("存在活动参数为空,保存失败!");
            map.put("fail", "存在参数为空,保存失败!");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }

        Activities activities = JSON.parseObject(activityParams, Activities.class);
        
        //检查activity的enterId是否是该账号有权操作的企业
        if(!enterprisesService.isParentManage(activities.getEntId(), getCurrentUserManager().getId())){
            logger.info("活动参数中的企业id={}，当前用户managerId={}，不存在上下级管理关系!", activities.getEntId(), getCurrentUserManager().getId());
            map.put("fail", "参数异常，无操作权限!");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }

        CrowdfundingActivityDetail crowdfundingActivityDetail = JSON.parseObject(crowdfundingActivityDetailParams,
                CrowdfundingActivityDetail.class);

        //校验活动时间
        if (activities.getStartTime() == null || activities.getEndTime() == null
                || activities.getStartTime().getTime() >= activities.getEndTime().getTime()) {
            logger.info("活动开始时间不能在活动结束时间之后,保存失败!");
            map.put("fail", "活动开始时间不能晚于活动结束时间,请修改活动时间!");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }

        if (activities.getEndTime().getTime() < System.currentTimeMillis()) {
            logger.info("活动时间已过期");
            map.put("fail", "活动时间已过期,请修改活动时间!");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }

        //校验图片格式
        if (!s3Service.checkFile(multipartRequest)) {
            logger.info("上传的图片只支持jpg、jpeg和png这三种格式");
            map.put("fail", "上传的图片只支持jpg、jpeg和png这三种格式！");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }

        //校验图片大小
        if (s3Service.checkFileSize(multipartRequest)) {
            logger.info("上传的图片大于100KB或者上传的附件大于5MB");
            map.put("fail", "图片大小或者附件大小超出规定值！");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }

        List<ActivityPrize> activityPrizeList = JSON.parseArray(activityPrizesParams, ActivityPrize.class);

        // 上传图片：先备份图片，上传，当更新数据成功后，删除备份图片和文件，否则删除上传文件，将恢复备份文件

        if (uploadFilesToS3(multipartRequest, activities.getActivityId(), crowdfundingActivityDetail)) {
            
            crowdfundingActivityDetail.setRules(xssService.stripXss(crowdfundingActivityDetail.getRules()));
            activities.setName(xssService.stripXss(activities.getName()));
            
            try {
                if (crowdFundingService.updateActivity(activities, crowdfundingActivityDetail, activityPrizeList,
                        phones, queryurl)) {
                    logger.info("编辑活动成功!");
                    map.put("success", "true");
                    response.getWriter().write(JSON.toJSONString(map));
                    // 删除备份文件
                    //deleteBackupFiles(activities.getActivityId(), multipartRequest);
                    return;
                } else {
                    logger.info("编辑活动失败!");
                    map.put("fail", "编辑失败!");
                    // 如果插入活动信息失败，则删除上传的图片,这里未实现
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }
            } catch (RuntimeException e) {
                logger.error("更新活动数据失败，失败原因：" + e.getMessage());
                // deleteBackupFiles(activities.getActivityId(),
                // multipartRequest);
                map.put("fail", "编辑失败!");
                response.getWriter().write(JSON.toJSONString(map));
                return;
            }
        } else {
            logger.info("替换文件失败!");
            map.put("fail", "编辑失败!");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }
    }

    /**
     * 保存新建活动
     *
     * @author qinqinyan
     */
    @RequestMapping(method = RequestMethod.POST, value = "/saveAjax")
    public void saveAjax(MultipartHttpServletRequest multipartRequest, HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        Map<String, String> map = new HashMap<String, String>();

        Manager manager = getCurrentUserManager();
        Administer administer = getCurrentUser();
        if (administer == null || manager == null || manager.getId() == null) {
            logger.info("用户异常,保存失败!");
            map.put("fail", "用户异常,保存失败!");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }

        String activityParams = request.getParameter("activity");
        String crowdfundingActivityDetailParams = request.getParameter("crowdfundingActivityDetail");
        String activityPrizesParams = request.getParameter("activityPrizes");
        String phones = request.getParameter("phones");
        String queryurl = request.getParameter("queryurl");

        //校验参数是否为空
        if (StringUtils.isEmpty(activityParams) || StringUtils.isEmpty(crowdfundingActivityDetailParams)
                || StringUtils.isEmpty(activityPrizesParams)) {
            logger.info("存在活动参数为空,保存失败!");
            map.put("fail", "存在参数为空,保存失败!");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }

        Activities activities = JSON.parseObject(activityParams, Activities.class);
        activities.setType(ActivityType.CROWD_FUNDING.getCode());
        activities.setCreatorId(administer.getId());

        //校验是否上传了手机号
        CrowdfundingActivityDetail crowdfundingActivityDetail = JSON.parseObject(crowdfundingActivityDetailParams,
                CrowdfundingActivityDetail.class);
        // 如果是选择黑白名单，校验上传手机号
        // if (!crowdfundingActivityDetail.getHasWhiteOrBlack().toString()
        // .equals(BlackAndWhiteListType.NOLIST.getCode().toString())) {
        if (crowdfundingActivityDetail.getUserList() == 1
                && StringUtils.isEmpty(phones)) {
            logger.info("上传的手机号为空,保存失败!");
            map.put("fail", "上传的手机号为空,保存失败!");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }
        
        if (crowdfundingActivityDetail.getUserList() == 3
                && StringUtils.isEmpty(queryurl.split("//")[1])) {
            logger.info("企业输入查询地址为空,保存失败!");
            map.put("fail", "企业输入查询地址为空,保存失败!");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }
        // }

        //校验活动时间
        if (activities.getStartTime() == null || activities.getEndTime() == null
                || activities.getStartTime().getTime() >= activities.getEndTime().getTime()) {
            logger.info("活动开始时间晚于活动结束时间");
            map.put("fail", "活动开始时间不能晚于活动结束时间,请修改活动时间!");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }

        if (activities.getEndTime().getTime() < System.currentTimeMillis()) {
            logger.info("活动时间已过期");
            map.put("fail", "活动时间已过期,请修改活动时间!");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }

        //校验图片格式
        if (!s3Service.checkFile(multipartRequest)) {
            logger.info("上传的图片只支持jpg、jpeg和png这三种格式");
            map.put("fail", "上传的图片只支持jpg、jpeg和png这三种格式！");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }

        //校验图片大小
        if (s3Service.checkFileSize(multipartRequest)) {
            logger.info("上传的图片大于100KB或者上传的附件大于5MB");
            map.put("fail", "图片大小或者附件大小超出规定值！");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }

        List<ActivityPrize> activityPrizeList = JSON.parseArray(activityPrizesParams, ActivityPrize.class);

        String activityId = SerialNumGenerator.buildSerialNum();
        // 上传文件成功，才插入其他活动信息
        if (uploadFilesToS3(multipartRequest, activityId, crowdfundingActivityDetail)) {
            activities.setActivityId(activityId);

            EnterprisesExtInfo enterprisesExtInfo = enterprisesExtInfoService.get(activities.getEntId());
            crowdfundingActivityDetail.setJoinType(enterprisesExtInfo.getJoinType());
            crowdfundingActivityDetail.setRules(xssService.stripXss(crowdfundingActivityDetail.getRules()));
            
            activities.setName(xssService.stripXss(activities.getName()));
            try {
                if (crowdFundingService.insertActivity(activities, crowdfundingActivityDetail, activityPrizeList,
                        phones, queryurl)) {
                    logger.info("保存活动成功!");
                    map.put("success", "true");
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }
            } catch (RuntimeException e) {
                logger.error("插入活动信息失败，失败原因：" + e.getMessage());
                map.put("fail", "保存失败!");
                // 如果插入活动信息失败，则删除上传的图片
                //deleteFiles(activityId);
                response.getWriter().write(JSON.toJSONString(map));
                return;
            }
        } else {
            logger.info("上传文件失败!");
            map.put("fail", "上传文件失败!");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        }
    }

    /**
     * 下载文件
     */
    @RequestMapping(value = "/downloadFile")
    public void downloadFile(String type, String activityId, HttpServletResponse response, HttpServletRequest request) {
        CrowdfundingActivityDetail detail =
                crowdfundingActivityDetailService.selectByActivityId(activityId);
        if (detail == null) {
            logger.info("未查询到活动明细，附件下载失败！");
            return;
        }
        if ("appendix".equals(type)) {
            s3Service.downloadFromS3(response, detail.getAppendixKey(), detail.getAppendix(), request);
        }

    }


    /**
     * 图片显示
     *
     * @author qinqinyan
     */
    @RequestMapping(value = "/getImage")
    public void getImage(String type, String activityId, HttpServletResponse response, HttpServletRequest request) {

        CrowdfundingActivityDetail detail =
                crowdfundingActivityDetailService.selectByActivityId(activityId);
        if (detail == null) {
            logger.info("未查询到活动明细，图片显示失败！");
            return;
        }
        if ("logo".equals(type)) {
            getImageFromS3(response, detail.getLogoKey(), request);
        }
        if ("banner".equals(type)) {
            getImageFromS3(response, detail.getBannerKey(), request);
        }
    }

    private void getImageFromS3(HttpServletResponse response, String key, HttpServletRequest request) {
        InputStream inputStream = fileStoreService.get(key);
        if (inputStream != null) {
            try {
                StreamUtils.copy(inputStream, response.getOutputStream());
            } catch (IOException e) {
                logger.error("输出结果流时抛出异常,错误信息为{}, 错误堆栈为{}.", e.getMessage(), e.getStackTrace());
            }
        }
    }


    /**
     * 删除文件
     */
    /*private boolean deleteFiles(String activityId) {
        String filePath = getGlobalConfigService.get(GlobalConfigKeyEnum.CROWD_FUNDING_PATH.getKey()) + File.separator
                + activityId;
        File delFile = new File(filePath);
        if (deleteDir(delFile)) {
            return true;
        }
        return false;
    }

    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }*/

    /**
     * 替换文件
     */
    /*private boolean replaceFiles(MultipartHttpServletRequest multipartRequest, String activityId,
            CrowdfundingActivityDetail crowdfundingActivityDetail) {
        try {
            Iterator<String> itr = multipartRequest.getFileNames();

            String filePath = getGlobalConfigService.get(GlobalConfigKeyEnum.CROWD_FUNDING_PATH.getKey())
                    + File.separator + activityId + File.separator;

            while (itr != null && itr.hasNext()) {
                // 获得文件实例
                MultipartFile multipartFile = multipartRequest.getFile(itr.next());

                String filename = multipartFile.getOriginalFilename();
                if (!StringUtils.isEmpty(filename)) {
                    if (multipartFile.getName().equals("logo")) {
                        // 备份文件
                        String oldPath = filePath + "logo";
                        String newPath = filePath + "logoBackup";
                        backupFolder(oldPath, newPath);

                        File file = new File(oldPath);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        File newFile = new File(file, multipartFile.getOriginalFilename());
                        multipartFile.transferTo(newFile);

                        crowdfundingActivityDetail.setLogo(multipartFile.getOriginalFilename());

                    } else if (multipartFile.getName().equals("banner")) {
                        // 备份文件
                        String oldPath = filePath + "banner";
                        String newPath = filePath + "bannerBackup";
                        backupFolder(oldPath, newPath);

                        File file = new File(oldPath);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        File newFile = new File(file, multipartFile.getOriginalFilename());
                        multipartFile.transferTo(newFile);

                        crowdfundingActivityDetail.setBanner(multipartFile.getOriginalFilename());

                    } else if (multipartFile.getName().equals("appendix")) {
                        // 备份文件
                        String oldPath = filePath + "appendix";
                        String newPath = filePath + "appendixBackup";
                        backupFolder(oldPath, newPath);

                        File file = new File(oldPath);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        File newFile = new File(file, multipartFile.getOriginalFilename());
                        multipartFile.transferTo(newFile);

                        crowdfundingActivityDetail.setAppendix(multipartFile.getOriginalFilename());
                    }
                }
            }
            return true;
        } catch (Exception e) {
            logger.error("替换文件出错，错误原因" + e.getMessage());
            return false;
        }
    }*/

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath
     *            String 原文件路径
     * @param newPath
     *            String 复制后路径
     * @return boolean
     */
    /*public void copyFolder(String oldPath, String newPath) {
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            (new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            if (file != null) {
                for (int i = 0; i < file.length; i++) {
                    if (oldPath.endsWith(File.separator)) {
                        temp = new File(oldPath + file[i]);
                    } else {
                        temp = new File(oldPath + File.separator + file[i]);
                    }

                    if (temp.isFile()) {
                        input = new FileInputStream(temp);
                        output = new FileOutputStream(newPath + File.separator + (temp.getName()).toString());
                        byte[] b = new byte[1024 * 5];
                        int len;
                        while ((len = input.read(b)) != -1) {
                            output.write(b, 0, len);
                        }
                    }
                    if (temp.isDirectory()) {// 如果是子文件夹
                        copyFolder(oldPath + File.separator + file[i], newPath + File.separator + file[i]);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("复制整个文件夹内容操作出错,错误原因：" + e.getMessage());
        } finally {
            if (output != null) {
                try {
                    output.flush();
                    output.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    logger.error(e.getMessage());
                }
            }

            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    logger.error(e.getMessage());
                }
            }
        }
    }*/

    /**
     * 移动文件到指定目录
     *
     * @param oldPath
     *            String
     * @param newPath
     *            String
     */
    /*private void backupFolder(String oldPath, String newPath) {
        copyFolder(oldPath, newPath);
        deleteDir(new File(oldPath));
    }*/

    /**
     * S3上传文件
     */
    private boolean uploadFilesToS3(MultipartHttpServletRequest multipartRequest, String activityId,
                                    CrowdfundingActivityDetail crowdfundingActivityDetail) {
        try {
            Iterator<String> itr = multipartRequest.getFileNames();
            File file = new File("dest");

            //1、上传文件
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
                CrowdfundingActivityDetail orgDetail =
                        crowdfundingActivityDetailService.selectByActivityId(activityId);
                if (!StringUtils.isEmpty(originalFilename)) {
                    if (multipartFile.getName().equals("logo")) {
                        if (orgDetail != null && !StringUtils.isEmpty(orgDetail.getLogoKey())) {
                            key = orgDetail.getLogoKey();
                        }
                        fileStoreService.save(key, file);
                        crowdfundingActivityDetail.setLogo(originalFilename);
                        crowdfundingActivityDetail.setLogoKey(key);

                    } else if (multipartFile.getName().equals("banner")) {
                        if (orgDetail != null && !StringUtils.isEmpty(orgDetail.getBannerKey())) {
                            key = orgDetail.getBannerKey();
                        }
                        fileStoreService.save(key, file);
                        crowdfundingActivityDetail.setBannerKey(key);
                        crowdfundingActivityDetail.setBanner(originalFilename);
                    } else if (multipartFile.getName().equals("appendix")) {
                        if (orgDetail != null && !StringUtils.isEmpty(orgDetail.getAppendixKey())) {
                            key = orgDetail.getAppendixKey();
                        }
                        fileStoreService.save(key, file);
                        crowdfundingActivityDetail.setAppendixKey(key);
                        crowdfundingActivityDetail.setAppendix(originalFilename);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            logger.error("上传文件是出错，错误原因" + e.getMessage());
            return false;
        }
    }

    /**
     * 上传文件
     */
    /*private boolean uploadFiles(MultipartHttpServletRequest multipartRequest, String activityId,
            CrowdfundingActivityDetail crowdfundingActivityDetail) {
        try {
            Iterator<String> itr = multipartRequest.getFileNames();

            String filePath = getGlobalConfigService.get(GlobalConfigKeyEnum.CROWD_FUNDING_PATH.getKey())
                    + File.separator + activityId + File.separator;

            while (itr != null && itr.hasNext()) {
                // 获得文件实例
                MultipartFile multipartFile = multipartRequest.getFile(itr.next());

                if (multipartFile.getName().equals("logo")) {
                    String logoFilePath = filePath + File.separator + "logo";
                    File file = new File(logoFilePath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File newFile = new File(file, multipartFile.getOriginalFilename());
                    multipartFile.transferTo(newFile);
                    crowdfundingActivityDetail.setLogo(multipartFile.getOriginalFilename());
                } else if (multipartFile.getName().equals("banner")) {
                    String bannerFilePath = filePath + File.separator + "banner";
                    File file = new File(bannerFilePath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File newFile = new File(file, multipartFile.getOriginalFilename());
                    multipartFile.transferTo(newFile);

                    crowdfundingActivityDetail.setBanner(multipartFile.getOriginalFilename());
                } else if (multipartFile.getName().equals("appendix")) {
                    String appendixFilePath = filePath + File.separator + "appendix";
                    File file = new File(appendixFilePath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File newFile = new File(file, multipartFile.getOriginalFilename());
                    multipartFile.transferTo(newFile);

                    crowdfundingActivityDetail.setAppendix(multipartFile.getOriginalFilename());
                }
            }
            return true;
        } catch (Exception e) {
            logger.error("上传文件是出错，错误原因" + e.getMessage());
            return false;
        }
    }*/

    /**
     * 上传黑白名单手机号ajax
     *
     * @author qinqinyan
     */
    @RequestMapping(method = RequestMethod.POST, value = "/uploadPhones")
    public void uploadFileAjax(@RequestParam("file") MultipartFile file, HttpServletResponse response)
            throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();

        // 设置响应给前台内容的数据格式
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain; charset=UTF-8");

        Set<String> correctPhones = new HashSet<String>(); // 正确的手机号
        Set<String> invalidPhones = new HashSet<String>(); // 不合法的手机号

        InputStream is = null;
        if (file == null || file.isEmpty()) {
            map.put("errorMsg", "上传文件不能为空");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        } else if (file.getSize() > MAX_FILE_SIZE * 1024 * 1024) {
            map.put("errorMsg", "文件格式限TXT，最大不超过" + MAX_FILE_SIZE + "MB");
            response.getWriter().write(JSON.toJSONString(map));
            return;
        } else {
            try {
                String fileSuffix = file.getOriginalFilename();
                if (fileSuffix != null) {
                    String[] fileNameArray = fileSuffix.split("\\.");
                    if(!"txt".equals(fileNameArray[fileSuffix.split("\\.").length - 1])){
                        map.put("errorMsg", "文件格式错误");
                        response.getWriter().write(JSON.toJSONString(map));
                        return;
                    }
                    fileSuffix = fileSuffix.split("\\.")[fileSuffix.split("\\.").length - 1];
                }
                File tempFile = File.createTempFile(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()),
                        fileSuffix);
                file.transferTo(tempFile);
                is = new FileInputStream(tempFile);

                int updatePhonesNum = 0;// 上传的电话数

                if (fileSuffix != null && "txt".equals(fileSuffix)) {
                    updatePhonesNum = readText(correctPhones, invalidPhones, is);
                } else {
                    // 只支持文件
                    map.put("errorMsg", "文件格式限TXT，最大不超过" + MAX_FILE_SIZE + "MB");
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }

                FileUtils.deleteQuietly(tempFile);

                String correctMobiles = toPhones(correctPhones);
                String invalidMobiles = toPhones(invalidPhones);
                if (updatePhonesNum > 0) {
                    map.put("successMsg", "success");
                    map.put("correctMobiles", correctMobiles);
                    map.put("invalidMobiles", invalidMobiles);

                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                } else {
                    map.put("successMsg", "fail");
                    map.put("invalidMobiles", invalidMobiles);
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }

            } catch (IOException e) {
                logger.error("上传文件异常", e);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                logger.error("上传文件格式异常", e);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
    }

    private String toPhones(Set<String> phones) throws UnsupportedEncodingException {
        // return org.apache.commons.lang.StringUtils.join(list.toArray(),",");
        StringBuffer buffer = new StringBuffer();
        if (phones == null || phones.size() == 0) {
            return "";
        } else {
            for (String phone : phones) {
                buffer.append(phone + ",");
            }
        }
        String phoneStr = new String(buffer.toString());
        phoneStr = phoneStr.substring(0, phoneStr.length() - 1);
        return phoneStr;
    }

    /**
     * 解析Txt文件,返回成功总行数
     *
     * @param correctPhones 正确的手机号
     * @param invalidPhones 错误的手机号
     */
    public int readText(Set<String> correctPhones, Set<String> invalidPhones, InputStream inputStream)
            throws IOException {
        BufferedReader buReader = new BufferedReader(new InputStreamReader(inputStream, "GB2312"));
        String in = null;
        // int rowNums = 0;
        while ((in = buReader.readLine()) != null) {
            in = in.trim();
            // rowNums++;
            if (judge(in)) {
                // 判断运营商
                correctPhones.add(in);
            } else {
                invalidPhones.add(in);
            }
        }
        return correctPhones != null && correctPhones.size() > 0 ? correctPhones.size() : 0;
    }

    private boolean judge(String str) {
        return com.cmcc.vrp.util.StringUtils.isValidMobile(str);
    }

    /**
     * 获取图片
     */
    @RequestMapping(value = "/getImageOrg")
    public void getImageOrg(String type, String activityId, HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        if (StringUtils.isEmpty(activityId) || StringUtils.isEmpty(type)) {
            response.setStatus(HttpStatus.SC_FORBIDDEN);
            return;
        }
        Activities activities = activitiesService.selectByActivityId(activityId);
        CrowdfundingActivityDetail crowdfundingActivityDetail = crowdfundingActivityDetailService
                .selectByActivityId(activityId);
        if (activities == null || crowdfundingActivityDetail == null) {
            response.setStatus(HttpStatus.SC_FORBIDDEN);
            return;
        }

        String JPG = "image/jpeg;charset=UTF-8";
        String filePath = getGlobalConfigService.get(GlobalConfigKeyEnum.CROWD_FUNDING_PATH.getKey()) + File.separator
                + activityId + File.separator;
        String path = "";
        if ("banner".equals(type)) {
            // 文件路径
            path = filePath + "banner" + File.separator + crowdfundingActivityDetail.getBanner();
        } else if ("logo".equals(type)) {
            // 文件路径
            path = filePath + "logo" + File.separator + crowdfundingActivityDetail.getLogo();
        }

        File file = new File(path);
        // 获取输出流
        OutputStream outputStream = null;
        FileInputStream fileInputStream = null;

        try {
            outputStream = response.getOutputStream();
            fileInputStream = new FileInputStream(file);
            // 读数据
            byte[] data = new byte[fileInputStream.available()];
            fileInputStream.read(data);
            // fileInputStream.close();
            // 回写
            response.setCharacterEncoding("UTF-8");
            response.setContentType(JPG);
            outputStream.write(data);

        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }

    /**
     * 下载手机号
     */
    @RequestMapping(value = "/downloadPhoneList")
    public void downloadPhoneList(String activityId, HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        Manager manager = getCurrentUserManager();
        if (manager == null || StringUtils.isEmpty(activityId)) {
            response.setStatus(HttpStatus.SC_FORBIDDEN);
            return;
        }
        Activities activities = activitiesService.selectByActivityId(activityId);
        CrowdfundingActivityDetail crowdfundingActivityDetail = crowdfundingActivityDetailService
                .selectByActivityId(activityId);
        if (activities == null || crowdfundingActivityDetail == null) {
            response.setStatus(HttpStatus.SC_FORBIDDEN);
            return;
        }

        // 设置文件存放路径
        String filePath = getGlobalConfigService.get(GlobalConfigKeyEnum.CROWD_FUNDING_PATH.getKey()) + File.separator
                + activityId + File.separator + "phoneList" + File.separator;
        File file = new File(filePath);
        try {
            if (file.exists()) {
                // 移除文件夹下已存在的文件
                FileUtils.cleanDirectory(file);
            } else {
                file.mkdirs();
            }
            // 生成相关文件
            makePhoneList(activityId, filePath);

            // 取得文件名。
            // String filename = file.getName();
            File fileFolder = new File(filePath + PHONELIST_TXT);
            String encoded = URLEncoder.encode(fileFolder.getName(), "utf-8");
            // 清空response
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename*=utf-8'zh_cn'" + encoded);
            response.addHeader("Content-Length", "" + fileFolder.length());
            // 以流的形式下载文件。
            byte[] fileData = FileUtils.readFileToByteArray(fileFolder);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(fileData);
            outputStream.flush();

        } catch (Exception e) {
            logger.error("下载黑白名单失败，失败原因" + e.getMessage());
        }
    }

    private void makePhoneList(String activityId, String filePath) throws Exception {
        // 查询黑白名单
        Map map = new HashMap<String, String>();
        map.put("activityId", activityId);
        List<String> phoneList = activityBlackAndWhiteService.selectPhonesByMap(map);
        if (phoneList != null && phoneList.size() > 0) {
            // txt文件路径
            String txtFilePath = filePath + PHONELIST_TXT;
            // 生成txt文件
            toTxtFile(txtFilePath, phoneList);
        }
    }

    private void toTxtFile(String filePath, List<String> list) {
        if (StringUtils.isEmpty(filePath)) {
            return;
        }
        OutputStream os = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            os = new FileOutputStream(file);

            //构造用户名单列表
            if (list != null) {
                for (String phone : list) {
                    byte[] content = (phone + "\r\n").getBytes();
                    os.write(content);
                }
            }

        } catch (IOException e) {
            logger.error("生成报名用户列表文件失败，失败原因：" + e.getMessage());
            return;
        } catch (Exception e) {
            logger.error("生成报名用户列表文件失败，失败原因：" + e.getMessage());
            return;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * 下载中奖纪录
     */
    @RequestMapping("downloadWinRecords")
    public void downloadWinRecords(String activityId, String status, String mobile, String productName, String payResult,
            HttpServletResponse response, HttpServletRequest request) {
        Manager manager = getCurrentUserManager();
        if (manager == null || StringUtils.isEmpty(activityId)) {
            response.setStatus(HttpStatus.SC_FORBIDDEN);
            return;
        }
        Map map = new HashMap<String, String>();
        map.put("activityId", activityId);
        map.put("status", status);
        map.put("mobile", mobile);
        map.put("productName", productName);
        map.put("payResult", payResult);
 
        List<ActivityWinRecord> list = activityWinRecordService.selectAllWinRecords(map);
        
        List<String> title = new ArrayList<String>();
        title.add("企业名称");
        title.add("手机号");
        title.add("产品名称");
        title.add("参加时间");
        title.add("支付时间");
        title.add("支付状态");
        title.add("充值时间");
        title.add("充值状态");

        List<String> rowList = new ArrayList<String>();

        for (ActivityWinRecord module : list) {
            String timeType = "yyyy-MM-dd HH:mm:ss";
            rowList.add(module.getEntName());
            rowList.add(module.getOwnMobile());
            rowList.add(module.getProductName());
            rowList.add(DateUtil.dateToString(module.getWinTime(), timeType));
            String payTime = "-";
            if (module.getPaymentTime() != null) {
                payTime = DateUtil.dateToString(module.getPaymentTime(), timeType);
            }
            rowList.add(payTime);
            PaymentStatus payStatus = PaymentStatus.fromValue(module.getPayResult());
            if(payStatus == null){
                rowList.add("-");
            }else{
                rowList.add(payStatus.getMessage());
            }
                        
            String chargeTime = "-";
            if (module.getChargeTime() != null) {
                chargeTime = DateUtil.dateToString(module.getChargeTime(), timeType);
            }
            rowList.add(chargeTime);

            ChargeRecordStatus chargeStatus = ChargeRecordStatus.fromValue(module.getStatus());
            if(chargeStatus == null){
                rowList.add("-");
            }else if(chargeStatus==ChargeRecordStatus.WAIT){
                rowList.add("待领取");
            }else{
                rowList.add(chargeStatus.getMessage());
            }
        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "joinUsers.csv");
            byte[] b = new byte[100];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inputStream.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 获取中奖纪录
     *
     * @author qinqinyan
     */
    @RequestMapping("records")
    public String records(ModelMap map, String activityId) {
        Manager manager = getCurrentUserManager();
        if (manager == null) {
            return getLoginAddress();
        }
        map.put("activityId", activityId);
        return "crowd-funding/records.ftl";
    }

    /**
     * 获取中奖纪录
     *
     * @author qinqinyan
     */
    @RequestMapping(value = "/searchRecords")
    public void searchRecords(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 查询参数:手机号，产品名称，状态，运营商
         */
        setQueryParameter("mobile", queryObject);
        setQueryParameter("productName", queryObject);
        // setQueryParameter("status", queryObject);
        // setQueryParameter("isp", queryObject);
        setQueryParameter("activityId", queryObject);
        setQueryParameter("status", queryObject);
        setQueryParameter("payResult", queryObject);

        // 数据库查找符合查询条件的个数
        int count = activityWinRecordService.countWinRecords(queryObject.toMap());
        List<ActivityWinRecord> list = activityWinRecordService.showWinRecords(queryObject.toMap());
        
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
     * 活动下架
     *
     * @author qinqinyan
     */
    @RequestMapping("offshelf")
    public void offshelf(HttpServletResponse resp, String activityId) throws IOException {
        Boolean flag = false;
        Map<String, String> returnMap = new HashMap();
        if (!StringUtils.isEmpty(activityId)) {
            try {
                flag = crowdFundingService.offShelf(activityId);
            } catch (RuntimeException e) {
                logger.error("下架失败，失败原因" + e.getMessage());
            }
        }
        returnMap.put("message", flag.toString());
        resp.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }

    /**
     * 活动下架
     *
     * @author qinqinyan
     */
    @RequestMapping("onshelf")
    public void onshelf(HttpServletResponse resp, String activityId)
            throws IOException {
        Boolean flag = false;
        String message = "";
        Map<String, String> returnMap = new HashMap();
        Manager manager = getCurrentUserManager();
        if (manager != null) {
            if (!StringUtils.isEmpty(activityId)) {
                try {
                    //flag = crowdFundingService.onShelf(activityId);
                    //众筹活动要求：只要存在一个活动在审核中或者上架或者进行中，则不能提交审核
                    Activities activity = activitiesService.selectByActivityId(activityId);
                    if(activity.getType().toString().equals(ActivityType.CROWD_FUNDING.getCode().toString())){
                        Map<String, Object> searchMap = new HashMap<String, Object>();
                        searchMap.put("entId", activity.getEntId());

                        List<Integer> statusList = new ArrayList<Integer>();
                        statusList.add(ActivityStatus.ON.getCode());
                        //statusList.add(ActivityStatus.FINISH_APPROVAL.getCode());
                        statusList.add(ActivityStatus.SUBMIT_APPROVAL.getCode());
                        statusList.add(ActivityStatus.PROCESSING.getCode());
                        searchMap.put("type", ActivityType.CROWD_FUNDING.getCode());
                        searchMap.put("status", statusList);

                        List<Activities> activityList = activitiesService.selectByEntId(searchMap);
                        if(activityList!=null && activityList.size()>0){
                            returnMap.put("result", "false");
                            returnMap.put("errorMsg", "企业同一时间只能上架一个众筹活动");
                            resp.getWriter().write(JSON.toJSONString(returnMap));
                            return;
                        }
                    }
                    String result = activitiesService.onShelf(activityId);
                    if ("success".equals(result)) {
                        flag = true;
                        message = "上架成功";
                    } else {
                        message = result;
                    }
                } catch (RuntimeException e) {
                    logger.error("下架失败，失败原因" + e.getMessage());
                    message = "上架失败";
                }
            }
        }
        returnMap.put("result", flag.toString());
        returnMap.put("message", message);
        resp.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }

    /**
     * 流量领取页面
     *
     * @author qinqinyan
     */
    @RequestMapping("getFlow")
    public String getFlow() {
        //test
        String mobile = getMobile();
        if (!StringUtils.isEmpty(mobile)) {
            logger.info("mobile = {} in session.", mobile);
            return "crowd-funding/charge.ftl";
        } else {
            logger.info("mobile is null in session.");
            return "gdzc/404.ftl";
        }
    }

    /**
     * 流量领取页面的中奖列表
     * 活动用户所有中奖列表
     */
    @RequestMapping("getWinRecords")
    public void getWinRecords(HttpServletRequest request, HttpServletResponse response,
                              Integer pageSize, Integer pageNo)
            throws IOException {
        //从缓存里取手机号
        String mobile = getMobile();

        List<ActivityWinRecord> records = null;
        if (!StringUtils.isEmpty(mobile)) {
            logger.info("mobile = {} from session.", mobile);

            QueryObject queryObject = new QueryObject();
            queryObject.getQueryCriterias().put("activityType", ActivityType.CROWD_FUNDING.getCode().toString());
            queryObject.getQueryCriterias().put("mobile", mobile);
            queryObject.setPageNum(pageNo);
            queryObject.setPageSize(pageSize);
            queryObject.getQueryCriterias().put("joinType", 1);//参加方式，企业报名，此处只筛选企业报名的记录

            //Map<String, String> map = new HashMap<String, String>();
            //map.put("activityType", ActivityType.Crowd_Funding.getCode().toString());
            //map.put("mobile", mobile);
            records = activityWinRecordService.getWinRecordsForCrowdFunding(queryObject.toMap());                       
        } else {
            logger.info("mobile is null in session.");
        }
        //returnMap.put("data", JSONObject.toJSONString(records));
        //response.getWriter().write(JSON.toJSONString(returnMap));
        JSONObject json = new JSONObject();
        json.put("data", records);

        try {
            response.getWriter().write(json.toString());
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return;
    }

    /**
     * 领取中奖页面的充值请求
     */
    @RequestMapping("chargeAjax")
    public void chargeAjax(HttpServletRequest request, HttpServletResponse response,
                           Long id) throws IOException {
        Boolean flag = false;
        String message = "参数异常,领取失败";
        Map<String, String> returnMap = new HashMap();
        String mobile = getMobile();
        if (!StringUtils.isEmpty(mobile)) {
            logger.info("mobile = {} from session", mobile);
            if (id != null) {
                ActivityWinRecord activityWinRecord =
                        activityWinRecordService.selectByPrimaryKey(id);
                if (activityWinRecord != null) {

                    //将wxOpenID插入到中奖纪录
                    String wxOpenId = getWxOpenid();
                    if (!StringUtils.isEmpty(wxOpenId)) {
                        ActivityWinRecord item = new ActivityWinRecord();
                        item.setWxOpenid(wxOpenId);
                        item.setRecordId(activityWinRecord.getRecordId());
                        if (!activityWinRecordService.updateByPrimaryKeySelective(item)) {
                            logger.info("更新纪录 recordId = {} 中wxOpenId = {} 失败。",
                                    activityWinRecord.getRecordId(), wxOpenId);
                        }
                    }
                    logger.info("开始进入充值服务...");
                    if (crowdFundingService.charge(activityWinRecord.getRecordId())) {
                        logger.info("领取成功，中奖纪录ID - {}", activityWinRecord.getRecordId());
                        flag = true;
                        message = "领取成功";
                    } else {
                        logger.info("领取失败，中奖纪录ID - {}", activityWinRecord.getRecordId());
                        message = "领取失败";
                    }
                }
            }
        } else {
            logger.info("未从session中取到手机号。");
        }
        returnMap.put("result", flag.toString());
        returnMap.put("message", message);
        response.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }

    
    /**
     * @param modelMap
     * @return
     */
    @RequestMapping("abilityConfig")
    public String abilityConfig(ModelMap modelMap) {
        return "crowd-funding/abilityConfig.ftl";
    }
    /**
     * @param modelMap
     * @param id
     * @return
     */
    @RequestMapping("config")
    public String config(ModelMap modelMap, Long id) {
        System.out.println(id);
        
        EnterprisesExtInfo enterprisesExtInfo = enterprisesExtInfoService.get(id);
        modelMap.put("enterprisesExtInfo", enterprisesExtInfo);
        modelMap.put("entId", id);
        return "crowd-funding/config.ftl";
    }
    /**
     * @param request
     * @param response
     * @param id
     * @throws IOException
     */
    @RequestMapping("saveConfig")
    public void saveConfig(HttpServletRequest request, HttpServletResponse response,
                           Long entId, String query) throws IOException {
        Map<String, String> returnMap = new HashMap();
        Boolean flag = false;
        if (StringUtils.isEmpty(query)) {
            flag = true;
        } else {
            Integer abilityConfig = Integer.valueOf(query);
            EnterprisesExtInfo enterprisesExtInfo = new EnterprisesExtInfo();
            enterprisesExtInfo.setEnterId(entId);
            enterprisesExtInfo.setAbilityConfig(abilityConfig);
            enterprisesExtInfo.setDeleteFlag(0);
            if (enterprisesExtInfoService.update(enterprisesExtInfo)) {
                flag = true;
            }
        }
        
        returnMap.put("result", flag.toString());
        response.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }
    
}
