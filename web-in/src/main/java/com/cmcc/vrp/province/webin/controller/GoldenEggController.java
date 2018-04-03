package com.cmcc.vrp.province.webin.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.ActivityPrizeRank;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.enums.SchedulerType;
import com.cmcc.vrp.exception.ProductInitException;
import com.cmcc.vrp.province.activity.model.AutoPrizesPojo;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityTemplate;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.GetDataFromTemplateResp;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.ShOrderList;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityCreatorService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityTemplateService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.ShOrderListService;
import com.cmcc.vrp.province.service.VirtualProductService;
import com.cmcc.vrp.province.service.XssService;
import com.cmcc.vrp.province.service.impl.GoldenEggServiceImpl;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.cmcc.vrp.util.MatrixToImageWriter;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by qinqinyan on 2016/11/14. 砸金蛋控制器
 */
@Controller
@RequestMapping("/manage/goldenegg")
public class GoldenEggController extends BaseController {
    private static final Logger logger = LoggerFactory
            .getLogger(GoldenEggController.class);

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
    @Qualifier("goldenEggService")
    GoldenEggServiceImpl goldenEggService;
    @Autowired
    ActivityWinRecordService activityWinRecordService;
    @Autowired
    ProductService productService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    VirtualProductService virtualProductService;
    @Autowired
    private ShOrderListService shOrderListService;
    @Autowired
    ManagerService managerService;
    @Autowired
    XssService xssService;
    @Autowired
    ActivityCreatorService activityCreatorService;
    /**
     * 活动列表页
     *
     * @author qinqinyan
     */
    @RequestMapping(value = "/index")
    public String index(ModelMap map, QueryObject queryObject) {
        
        if(queryObject != null){
            map.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        //获取当前用户信息
        Manager manager = getCurrentUserManager();
        Administer administer = getCurrentUser();
        if (manager == null || manager.getId() == null || manager.getRoleId() == null ||
                administer == null || administer.getId() == null) {
            return getLoginAddress();
        }

        //判断是否需要审核
        Boolean approvalFlag = approvalProcessDefinitionService
                .wheatherNeedApproval(ApprovalType.Activity_Approval.getCode());
        map.addAttribute("approvalFlag", approvalFlag.toString());

        map.addAttribute("managerId", manager.getId());
        map.addAttribute("currUserId", administer.getId());
        map.addAttribute("roleId", manager.getRoleId());
        if(isSdEnvironment()){
            map.addAttribute("isShandong", "true");
        }
        return "goldenEgg/index.ftl";
    }

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
             * */
            setQueryParameter("name", queryObject);
            setQueryParameter("status", queryObject);
            setQueryParameter("entName", queryObject);
            setQueryParameter("startTime", queryObject);
            setQueryParameter("endTime", queryObject);

            if (queryObject.getQueryCriterias().get("endTime") != null) {
                queryObject.getQueryCriterias().put("startTime",
                        queryObject.getQueryCriterias().get("startTime").toString() + " 00:00:00");
                queryObject.getQueryCriterias().put("endTime",
                        queryObject.getQueryCriterias().get("endTime").toString() + " 23:59:59");
            }

            queryObject.getQueryCriterias().put("type", ActivityType.GOLDENBALL.getCode());
            queryObject.getQueryCriterias().put("managerId", manager.getId());

            list = activitiesService.selectByMap(queryObject.toMap());
            count = activitiesService.countByMap(queryObject.toMap());

            //校验活动状态,如果存在未上架又已经结束的活动，则在此更改活动状态
            List<Activities> updateActivities = new ArrayList<Activities>();
            for (Activities activities : list) {
                if (activities.getStatus().toString().equals(ActivityStatus.FINISH_APPROVAL.getCode().toString())
                        && activities.getEndTime().getTime() < System.currentTimeMillis()) {
                    activities.setStatus(ActivityStatus.END.getCode());

                    Activities item = new Activities();
                    item.setId(activities.getId());
                    updateActivities.add(item);
                }
                activities.setActivityCreator(activityCreatorService.getByActId(ActivityType.GOLDENBALL, activities.getId()));
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
            e.printStackTrace();
        }
    }

    /**
     * 活动详情
     *
     * @author qinqinyan
     */
    @RequestMapping(value = "/detail")
    public String detail(ModelMap map, String activityId, HttpServletResponse resp) {
        Manager manager = getCurrentUserManager();
        if (manager!=null && !StringUtils.isEmpty(activityId)) {
            Activities activities = activitiesService.selectByActivityId(activityId);
            List<ActivityPrize> activityPrizes = activityPrizeService.selectByActivityId(activityId);
            ActivityTemplate activityTemplate = activityTemplateService.selectByActivityId(activityId);
            ActivityInfo activityInfo = activityInfoService.selectByActivityId(activityId);
            if (activities != null && activityPrizes != null && activityPrizes.size() > 0
                    && activityTemplate != null && activityInfo != null) {
                if(!managerService.managedByManageId(activities.getEntId(), manager.getId())){
                    map.addAttribute("errorMsg", "对不起，您无权限查看该活动详情!");
                    return "error.ftl";
                }
                
                //活动信息
                map.addAttribute("activities", activities);
                //map.addAttribute("activityPrizes", activityPrizes);
                map.put("activityPrizes", JSONObject.toJSONString(goldenEggService.sortActivityPrizes(activityPrizes)));

                map.addAttribute("activityTemplate", activityTemplate);
                
                //向营销模板请求获取用户参与中奖人次等
                if(activities.getStatus().toString().equals(ActivityStatus.PROCESSING.getCode().toString())){
                    //进行中的活动才去发起请求
                    GetDataFromTemplateResp result = activityTemplateService.getDataFromTemplate(activityInfo.getUrl());
                    
                    activityInfo.setPlayCount(result.getPlayCount());
                    activityInfo.setVisitCount(result.getVisitCount());
                    activityInfo.setGivedUserCount(result.getWinCount());
                    
                    ActivityInfo updateActivityInfo = new ActivityInfo();
                    updateActivityInfo.setActivityId(activityInfo.getActivityId());
                    updateActivityInfo.setId(activityInfo.getId());
                    
                    updateActivityInfo.setPlayCount(result.getPlayCount());
                    updateActivityInfo.setVisitCount(result.getVisitCount());
                    updateActivityInfo.setGivedUserCount(result.getWinCount());
                    
                    if(!activityInfoService.updateByPrimaryKeySelective(updateActivityInfo)){
                        logger.info("更新活动  = {} 访问游戏次数  = {}, 参与人次 = {}, 中奖人次 = {}",
                                updateActivityInfo.getActivityId(), updateActivityInfo.getVisitCount(),
                                updateActivityInfo.getPlayCount(), updateActivityInfo.getGivedUserCount());
                    }
                }
                map.addAttribute("activityInfo", activityInfo);

                Enterprise enterprise = enterprisesService.selectById(activities.getEntId());
                map.addAttribute("enterprise", enterprise);

                //审核记录
                //历史审核记录
                List<ApprovalRecord> approvalRecords = approvalRecordService.selectByActivityIdForActivity(activityId);
                map.put("opinions", approvalRecords);
                
                map.addAttribute("activityId", activityId);
                map.addAttribute("authorizeUrl", getAuthorizeUrl() + getActivityPlatformName());

                return "goldenEgg/detail.ftl";
            }
        }
        map.addAttribute("errorMsg", "未查找到该活动!");
        return "error.ftl";
    }
    
    /**
     * 平台标示
     */
    public String getPlateName() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_PLATFORM_NAME.getKey());
    }

    /**
     * 查看用户中奖信息
     *
     * @author qinqinyan
     */
    @RequestMapping("records")
    public String records(ModelMap map, String activityId) {
        Manager manager = getCurrentUserManager();
        if (manager == null) {
            return getLoginAddress();
        }
        
        Activities activities = activitiesService.selectByActivityId(activityId);
        if(activities!=null){
            if(!managerService.managedByManageId(activities.getEntId(), manager.getId())){
                map.addAttribute("errorMsg", "对不起，您无权限查看该活动详情!");
                return "error.ftl";
            }
            
            map.addAttribute("activityId", activityId);
            return "goldenEgg/records.ftl";
        }
        map.addAttribute("errorMsg", "参数异常");
        return "error.ftl";
    }

    @RequestMapping("searchRecord")
    public void searchRecord(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 查询参数: 企业名字、红包状态
         */
        setQueryParameter("mobile", queryObject);
        setQueryParameter("status", queryObject);
        setQueryParameter("activityId", queryObject);

        int count = activityWinRecordService.showForPageResultCount(queryObject);
        List<ActivityWinRecord> records = activityWinRecordService
                .showForPageResult(queryObject);

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", records);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 活动上架
     *
     * @author qinqinyan
     * 
     * edit by qinqinyan 增加鉴权
     */
    @RequestMapping(value = "onshelf")
    public void onshelf(HttpServletResponse resp, String activityId) throws IOException {
        Administer administer = getCurrentUser();
        Manager manager = getCurrentUserManager();
        boolean flag = true;
        Map returnMap = new HashMap();
        if (administer == null || manager == null) {
            returnMap.put("message", "对不起，获取不到该用户信息!");
            resp.getWriter().write(JSONObject.toJSONString(returnMap));
            return;
        }
        Activities activities = activitiesService.selectByActivityId(activityId);
        //直接上架，如果是微信活动，要判断活动是否通过鉴权
        ActivityTemplate activityTemplate = activityTemplateService.selectByActivityId(activityId);
        
        if(activities==null || activityTemplate==null){
            returnMap.put("message", "参数异常，上架失败");
        }else{
            if(!managerService.managedByManageId(activities.getEntId(), manager.getId())){
                returnMap.put("message", "对不起，您无权限上架活动");
            }else{
                if(activityTemplate.getUserType()!=null 
                        && activityTemplate.getUserType().intValue() == 1){
                    //需要微信鉴权
                    resp.reset();
                    resp.setContentType("text/plain;charset=utf-8");
                    resp.setHeader("Cache-Control", "no-cache");

                    String url = getIsAuthorizedUrl() + getActivityPlatformName() + activityId;
                    logger.info("判断活动是否授权url:" + url);
                    String result = HttpUtils.get(url);
                    logger.info("判断活动是否授权结果:" + result);
                    
                    if(!(!StringUtils.isEmpty(result) && "true".equals(result))){
                        //未授权
                        returnMap.put("message", "活动没有微信授权请到详情中授权");
                        flag = false;
                    }
                }
                
                if(flag){
                    //活动上架
                    String result = goldenEggService.onShelf(activityId, SchedulerType.GOLDENEGG_START.getCode(),
                            SchedulerType.GOLDENEGG_END.getCode());
                    if ("success".equals(result)) {
                        returnMap.put("result", "true");
                    } else {
                        returnMap.put("message", result);
                    }
                }
            }
        }
        resp.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }

    /**
     * 活动下架
     *
     * @author qinqinyan
     */
    @RequestMapping("offShelf")
    public void offShelf(HttpServletResponse resp, String activityId) throws IOException {
        Manager manager = getCurrentUserManager();
        Boolean flag = false;
        Map returnMap = new HashMap();
        Activities activities = activitiesService.selectByActivityId(activityId);
        if(manager!=null && activities!=null){
            if(!managerService.managedByManageId(activities.getEntId(), manager.getId())){
                logger.info("用户manageId = {} 无权限下载活动 activityId = {}",  manager.getId(), activityId);
            }else{
                try {
                    flag = goldenEggService.offShelf(activityId);
                } catch (RuntimeException e) {
                    logger.info("下架活动失败，失败原因： {}", e.getMessage());
                }
            }
        }
        returnMap.put("message", flag.toString());
        resp.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }

    /**
     * 营销活动提交审批申请
     *
     * @author qinqinyan
     */
    @RequestMapping("submitActivityApprovalAjax")
    public void submitActivityApprovalAjax(HttpServletResponse resp,
                                           String activityId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean flag = true;
        
        Administer administer = getCurrentUser();
        Manager manager = getCurrentUserManager();
        if(administer==null || manager==null){
            map.put("submitRes", "fail");
            map.put("errorMsg", "用户信息异常，提交申请失败");
        }else{
            Long currentId = administer.getId();
            Long roleId = manager.getRoleId();
            
            Activities activities = activitiesService.selectByActivityId(activityId);
            ActivityTemplate activityTemplate = activityTemplateService.selectByActivityId(activityId);
            if(activities==null || activityTemplate==null){
                map.put("submitRes", "fail");
                map.put("errorMsg", "活动信息异常，提交申请失败");
            }else{
                if(!managerService.managedByManageId(activities.getEntId(), manager.getId())){
                    map.put("submitRes", "fail");
                    map.put("errorMsg", "对不起，您无权限提交审核");
                }else{
                    if(activityTemplate.getUserType()!=null 
                            && activityTemplate.getUserType().intValue() == 1){
                        //需要微信鉴权
                        resp.reset();
                        resp.setContentType("text/plain;charset=utf-8");
                        resp.setHeader("Cache-Control", "no-cache");

                        String url = getIsAuthorizedUrl() + getActivityPlatformName() + activityId;
                        logger.info("判断活动是否授权url:" + url);
                        String result = HttpUtils.get(url);
                        logger.info("判断活动是否授权结果:" + result);
                        
                        if(!(!StringUtils.isEmpty(result) && "true".equals(result))){
                            //未授权
                            map.put("submitRes", "fail");
                            map.put("errorMsg", "活动没有微信授权请到详情中授权");
                            flag = false;
                        }
                    }
                    
                    if(flag){
                        //提交审核
                        map = activitiesService.submitApproval(activityId,
                                currentId, roleId);
                    }
                }
            }
        }
        resp.getWriter().write(JSON.toJSONString(map));
        return;
    }

    /**
     * 新建砸金蛋
     *
     * @author qinqinyan
     */
    @RequestMapping(value = "/create")
    public String create(ModelMap map) {
        Manager manager = getCurrentUserManager();
        if (manager == null || manager.getId() == null) {
            return getLoginAddress();
        }

        //根据省份标识设置参数
        setCqFlag(map);

        List<Enterprise> enterprises = enterprisesService.getEnterByManagerId(manager.getId());
        map.put("enterprises", enterprises);
        logger.info("authorizeUrl = {}", getAuthorizeUrl()+getActivityPlatformName());
        map.addAttribute("authorizeUrl", getAuthorizeUrl()+getActivityPlatformName());
        if ("shanghai".equals(getProvinceFlag())) {
            map.addAttribute("showOrderList", 1);
            List<ShOrderList> shOrderLists = shOrderListService.getByEnterId(enterprises.get(0).getId());
            map.addAttribute("orderList", shOrderLists);
        } else {
            map.addAttribute("showOrderList", 0);
        }

        //是否允许使用三网产品
        if (allowAllPlatformProduct()) {
            map.addAttribute("allowAllPlatformProduct", true);
        }

        return "goldenEgg/create.ftl";
    }

    public String getAuthorizeUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_AUTHORIZED_URL.getKey());
    }
    
    public String getActivityPlatformName(){
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_PLATFORM_NAME.getKey());
    }


    /**
     * 编辑砸金蛋
     *
     * @author qinqinyan
     */
    @RequestMapping("edit")
    public String edit(ModelMap map, String activityId) {
        Manager manager = getCurrentUserManager();
        if (manager == null || manager.getId() == null) {
            return getLoginAddress();
        }

        if (StringUtils.isEmpty(activityId)) {
            map.addAttribute("errorMsg", "参数缺失，查找不到该活动!");
            return "error.ftl";
        }

        //根据省份标识设置参数
        setCqFlag(map);

        Activities activities = activitiesService.selectByActivityId(activityId);
        List<ActivityPrize> activityPrizeList = activityPrizeService.selectByActivityId(activityId);
        ActivityInfo activityInfo = activityInfoService.selectByActivityId(activityId);
        ActivityTemplate activityTemplate = activityTemplateService.selectByActivityId(activityId);

        if (activities == null || activities.getEntId() == null || activityPrizeList == null
                || activityPrizeList.size() < 1 || activityInfo == null || activityTemplate == null) {
            map.addAttribute("errorMsg", "活动参数缺失，查找不到该活动!");
            return "error.ftl";
        }

        List<Enterprise> enterprises = enterprisesService.getEnterByManagerId(manager.getId());
        if (enterprises == null) {
            map.addAttribute("errorMsg", "参数缺失，查找不到该活动对应的企业信息!");
            return "error.ftl";
        }

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
                map.addAttribute("errorMsg", "对不起,您无权限编辑该活动!");
                return "error.ftl";
            }
        }

        Enterprise enter = enterprisesService.selectById(activities.getEntId());
        map.addAttribute("enter", enter);

        if (activityPrizeList.get(0).getFlowType().toString().equals("2")) {
            //流量包
            map.addAttribute("flowType", 1);
        } else {
            //流量池
            map.addAttribute("flowType", 2);
        }

        //构造适合前端显示的奖品
        AutoPrizesPojo firstRankPrize = goldenEggService.getAutoPrizesPojo(activityPrizeList,
                ActivityPrizeRank.FirstRankPrize.getIdPrefix());
        AutoPrizesPojo secondRankPrize = goldenEggService.getAutoPrizesPojo(activityPrizeList,
                ActivityPrizeRank.SecondRankPrize.getIdPrefix());
        AutoPrizesPojo thirdRankPrize = goldenEggService.getAutoPrizesPojo(activityPrizeList,
                ActivityPrizeRank.ThirdRankPrize.getIdPrefix());
        AutoPrizesPojo forthRankPrize = goldenEggService.getAutoPrizesPojo(activityPrizeList,
                ActivityPrizeRank.ForthRankPrize.getIdPrefix());
        AutoPrizesPojo fiveRankPrize = goldenEggService.getAutoPrizesPojo(activityPrizeList,
                ActivityPrizeRank.FifthRankPrize.getIdPrefix());
        AutoPrizesPojo sixRankPrize = goldenEggService.getAutoPrizesPojo(activityPrizeList,
                ActivityPrizeRank.SixthRankPrize.getIdPrefix());

        map.addAttribute("activities", activities);
        map.addAttribute("activityPrizeList", activityPrizeList);
        map.addAttribute("activityInfo", activityInfo);
        map.addAttribute("activityTemplate", activityTemplate);
        map.addAttribute("enterprises", enterprises);

        map.put("firstRankPrize", JSON.toJSONString(firstRankPrize));
        map.put("secondRankPrize", JSON.toJSONString(secondRankPrize));
        map.put("thirdRankPrize", JSON.toJSONString(thirdRankPrize));
        map.put("forthRankPrize", JSON.toJSONString(forthRankPrize));
        map.put("fiveRankPrize", JSON.toJSONString(fiveRankPrize));
        map.put("sixRankPrize", JSON.toJSONString(sixRankPrize));

        map.addAttribute("authorizeUrl", getAuthorizeUrl() + getActivityPlatformName());
        if ("shanghai".equals(getProvinceFlag())) {
            map.addAttribute("showOrderList", 1);
            List<ShOrderList> shOrderLists = shOrderListService.getByEnterId(enter.getId());
            map.addAttribute("orderList", shOrderLists);
        } else {
            map.addAttribute("showOrderList", 0);
        }
        //是否允许使用三网产品
        if (allowAllPlatformProduct()) {
            map.addAttribute("allowAllPlatformProduct", true);
        }

        return "goldenEgg/edit.ftl";
    }

    /**
     * 获取产品列表
     *
     * @param searchType 产品类型 M:移动包，T:电信包，U：联通包
     * @author qinqinyan
     */
    @RequestMapping("searchProduct")
    public void searchProduct(QueryObject queryObject, HttpServletResponse response,
                              Long enterpriseId, String searchType, Long orderId) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        if (enterpriseId != null && !org.apache.commons.lang.StringUtils.isBlank(searchType)) {
            queryObject.getQueryCriterias().put("enterId", enterpriseId.toString());
            queryObject.getQueryCriterias().put("isp", searchType);
        }
        if (orderId != null) {
            queryObject.getQueryCriterias().put("orderId", orderId);
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
     * 保存砸金蛋
     *
     * @author qinqinyan
     */
    @RequestMapping("saveAjax")
    public void saveAjax(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Manager manager = getCurrentUserManager();
        Administer administer = getCurrentUser();
        if (manager == null || manager.getId() == null) {
            logger.info("用户异常,保存失败!");
            modelMap.addAttribute("fail", "用户异常,保存失败!");
            response.getWriter().write(JSON.toJSONString(modelMap));
            return;
        }

        String activityParams = request.getParameter("activity");
        String activityPrizesParams = request.getParameter("activityPrizes");
        String activityTemplateParams = request.getParameter("activityTemplate");
        String activityInfoParams = request.getParameter("activityInfo");

        if (StringUtils.isEmpty(activityParams) || StringUtils.isEmpty(activityPrizesParams)
                || StringUtils.isEmpty(activityTemplateParams) || StringUtils.isEmpty(activityInfoParams)) {
            logger.info("参数缺失,保存失败!");
            modelMap.addAttribute("fail", "参数缺失,保存失败!");
            response.getWriter().write(JSON.toJSONString(modelMap));
            return;
        }

        Activities activities = JSON.parseObject(activityParams, Activities.class);
        
        //检查activity的enterId是否是该账号有权操作的企业
        if(!enterprisesService.isParentManage(activities.getEntId(), manager.getId())){
            logger.info("活动参数中的企业id={}，当前用户managerId={}，不存在上下级管理关系!", activities.getEntId(), manager.getId());
            modelMap.addAttribute("fail", "参数异常，无操作权限!");
            response.getWriter().write(JSON.toJSONString(modelMap));
            return;
        }
        
        activities.setType(ActivityType.GOLDENBALL.getCode());
        activities.setEndTime(DateUtil.getEndTimeOfDate(activities.getEndTime()));
        activities.setCreatorId(administer.getId());
        ActivityTemplate activityTemplate = JSON.parseObject(activityTemplateParams, ActivityTemplate.class);
        ActivityInfo activityInfo = JSON.parseObject(activityInfoParams, ActivityInfo.class);

        //1、流量池、话费产品转化 2、流量包不转化
        List<ActivityPrize> activityPrizeList;
        try {
            activityPrizeList = virtualProductService.activityInitProcess(activityPrizesParams);
        } catch (ProductInitException e) {
            logger.error("保存失败,原因:{}", e.getMessage());
            modelMap.addAttribute("fail", e.getMessage() + ",保存失败!");
            response.getWriter().write(JSON.toJSONString(modelMap));
            return;
        }
        try {
            activities.setName(xssService.stripQuot(activities.getName()));
            activityTemplate.setRules(xssService.stripQuot(activityTemplate.getRules()));
            
            if (goldenEggService.insertActivity(activities, activityPrizeList, activityTemplate, activityInfo)) {
                logger.info("保存成功,activities-{}", JSON.toJSONString(activities));
                modelMap.addAttribute("success", "保存成功!");
                modelMap.addAttribute("activityId", activities.getActivityId());
                response.getWriter().write(JSON.toJSONString(modelMap));
                return;
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        logger.info("插入活动失败,activities-{}, activityPrizeList-{}, activityTemplate-{}, activityInfo-{}",
                JSON.toJSONString(activities), JSON.toJSONString(activityPrizeList), JSON.toJSONString(activityTemplate),
                JSON.toJSONString(activityInfo));
        modelMap.addAttribute("fail", "保存失败!");
        response.getWriter().write(JSON.toJSONString(modelMap));
        return;
    }

    /**
     * 保存编辑信息
     *
     * @author qinqinyan
     */
    @RequestMapping("saveEditAjax")
    public void saveEditAjax(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Manager manager = getCurrentUserManager();
        if (manager == null || manager.getId() == null) {
            logger.info("用户异常,保存失败!");
            modelMap.addAttribute("failed", "用户异常,保存失败!");
            response.getWriter().write(JSON.toJSONString(modelMap));
            return;
        }

        String activityParams = request.getParameter("activity");
        String activityPrizesParams = request.getParameter("activityPrizes");
        String activityTemplateParams = request.getParameter("activityTemplate");
        String activityInfoParams = request.getParameter("activityInfo");

        if (StringUtils.isEmpty(activityParams) || StringUtils.isEmpty(activityPrizesParams)
                || StringUtils.isEmpty(activityTemplateParams) || StringUtils.isEmpty(activityInfoParams)) {
            logger.info("参数缺失,保存失败!");
            modelMap.addAttribute("failed", "参数缺失,保存失败!");
            response.getWriter().write(JSON.toJSONString(modelMap));
            return;
        }

        Activities activities = JSON.parseObject(activityParams, Activities.class);
        
        //校验前端传过来的活动id是否有效
        Activities orgActivities = activitiesService.selectByActivityId(activities.getActivityId());
        if(orgActivities==null){
            logger.info("参数缺失,保存失败!");
            modelMap.addAttribute("failed", "参数缺失,保存失败!");
            response.getWriter().write(JSON.toJSONString(modelMap));
            return;
        }
        
        //对根据前端传过来的活动id从数据库查找出来的活动的状态进行判断，只要以保存和已驳回的可进行编辑保存
        if(!(orgActivities.getStatus().toString().equals(ActivityStatus.SAVED.getCode().toString())
                || orgActivities.getStatus().toString().equals(ActivityStatus.REJECT.getCode().toString()))){
            logger.info("该活动的状态不允许编辑活动!");
            modelMap.addAttribute("failed", "该活动的状态不允许编辑活动!");
            response.getWriter().write(JSON.toJSONString(modelMap));
            return;
        }
        
        //检查数据库查出来  -- 检查activity的enterId是否是该账号有权操作的企业
        if(!enterprisesService.isParentManage(orgActivities.getEntId(), manager.getId())){
            logger.info("活动参数中的企业id={}，当前用户managerId={}，不存在上下级管理关系!", orgActivities.getEntId(), manager.getId());
            modelMap.addAttribute("fail", "参数异常，无操作权限!");
            response.getWriter().write(JSON.toJSONString(modelMap));
            return;
        }
        
        //检查activity的enterId是否是该账号有权操作的企业
        if(!enterprisesService.isParentManage(activities.getEntId(), manager.getId())){
            logger.info("活动参数中的企业id={}，当前用户managerId={}，不存在上下级管理关系!", activities.getEntId(), manager.getId());
            modelMap.addAttribute("fail", "参数异常，无操作权限!");
            response.getWriter().write(JSON.toJSONString(modelMap));
            return;
        }
        
        activities.setEndTime(DateUtil.getEndTimeOfDate(activities.getEndTime()));
        ActivityTemplate activityTemplate = JSON.parseObject(activityTemplateParams, ActivityTemplate.class);
        ActivityInfo activityInfo = JSON.parseObject(activityInfoParams, ActivityInfo.class);

        List<ActivityPrize> activityPrizeList = null;
        try {
            activityPrizeList = virtualProductService.activityInitProcess(activityPrizesParams);
        } catch (ProductInitException e) {
            logger.error("保存失败,原因:{}", e.getMessage());
            modelMap.addAttribute("fail", e.getMessage() + ",保存失败!");
            response.getWriter().write(JSON.toJSONString(modelMap));
            return;
        }

        try {
            //采用后台赋值，避免篡改
            activityInfo.setActivityId(activities.getActivityId());
            activityTemplate.setActivityId(activities.getActivityId());
            for(ActivityPrize item : activityPrizeList){
                item.setActivityId(activities.getActivityId());
            }
            
            activities.setName(xssService.stripQuot(activities.getName()));
            activityTemplate.setRules(xssService.stripQuot(activityTemplate.getRules()));
            
            if (goldenEggService.updateActivity(activities, activityPrizeList, activityTemplate, activityInfo)) {
                logger.info("保存成功,activities-{}", JSON.toJSONString(activities));
                modelMap.addAttribute("success", "保存成功!");
                modelMap.addAttribute("activityId", activities.getActivityId());
                response.getWriter().write(JSON.toJSONString(modelMap));
                return;
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        logger.info("更新活动失败,activities-{}, activityPrizeList-{}, activityTemplate-{}, activityInfo-{}",
                JSON.toJSONString(activities), JSON.toJSONString(activityPrizeList), JSON.toJSONString(activityTemplate),
                JSON.toJSONString(activityInfo));
        modelMap.addAttribute("fail", "保存失败!");
        response.getWriter().write(JSON.toJSONString(modelMap));
        return;
    }

    @RequestMapping("qrCode")
    public void getQRCode(HttpServletResponse resp, String activityId) {
        try {
            int nWidth = 256;

            ActivityInfo activityInfo = activityInfoService.selectByActivityId(activityId);
            String url = activityInfo.getUrl();
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            int nMargin = 1; //定义边框大小
            BitMatrix bitMatrix = multiFormatWriter.encode(url,
                    BarcodeFormat.QR_CODE, nWidth, nWidth, hints);
            bitMatrix = activityTemplateService.updateBit(bitMatrix, nMargin);

            MatrixToImageWriter.writeToStream(bitMatrix, "png",
                    resp.getOutputStream());
        } catch (WriterException e) {
        } catch (IOException e) {
        }
    }

    /**
     * 企业授权
     * 修改成用活动uuid来校验授权
     * edit by qinqinyan on 2017/5/24
     */
    @RequestMapping(method = RequestMethod.POST, value = "/isAuthorized")
    @ResponseBody
    public void isAuthorized(HttpServletResponse response, String activityId) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();//ajax传输的值
            if (StringUtils.isEmpty(activityId)) {
                map.put("result", "false");
                response.getWriter().write(JSON.toJSONString(map));
                return;
            }
            response.reset();
            response.setContentType("text/plain;charset=utf-8");
            response.setHeader("Cache-Control", "no-cache");

            String url = getIsAuthorizedUrl() + getActivityPlatformName() + activityId;
            logger.info("判断活动是否授权url:" + url);
            String result = HttpUtils.get(url);
            logger.info("判断活动是否授权结果:" + result);
            
            if (!StringUtils.isEmpty(result) && "true".equals(result)) {
                //授权成功
                map.put("result", "true");
            } else {//未授权
                map.put("result", "false");
            }
            response.getWriter().write(JSON.toJSONString(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getIsAuthorizedUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.ACTIVITY_IS_AUTHORIZED_URL.getKey());
    }

    /**
     * 判断是否山东环境
     */
    private boolean isSdEnvironment(){
        return "sd".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()));
    }
}
