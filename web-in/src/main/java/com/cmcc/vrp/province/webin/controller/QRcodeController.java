package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.enums.IspType;
import com.cmcc.vrp.exception.ProductInitException;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.ShOrderList;
import com.cmcc.vrp.province.module.CurrentActivityInfo;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityCreatorService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.QRCodeService;
import com.cmcc.vrp.province.service.ShOrderListService;
import com.cmcc.vrp.province.service.VirtualProductService;
import com.cmcc.vrp.province.service.XssService;
import com.cmcc.vrp.util.Constants.FLOW_ACCOUNT_FLAG;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.SizeUnits;
import com.cmcc.vrp.util.StringUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: QRcodeController
 * @Description: 二维码管理
 */
@Controller
@RequestMapping("/manage/qrcode")
public class QRcodeController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(QRcodeController.class);
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    AccountService accountService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    EntProductService entProductService;
    @Autowired
    QRCodeService qRCodeService;
    @Autowired
    ApprovalRecordService approvalRecordService;
    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Autowired
    VirtualProductService virtualProductService;
    @Autowired
    private ProductService productService;
    @Autowired
    private EnterprisesService enterprisesService;
    @Autowired
    private ActivityPrizeService activityPrizeService;
    @Autowired
    private ActivityInfoService activityInfoService;
    @Autowired
    private ActivityWinRecordService activityWinRecordService;
    @Autowired
    private ShOrderListService shOrderListService;
    @Autowired
    XssService xssService;
    @Autowired
    ManagerService managerService;
    @Autowired
    ActivityCreatorService activityCreatorService;

    /**
     * 二维码首页
     *
     * @author qinqinyan
     */
    @RequestMapping(value = "/index")
    public String index(ModelMap modelMap, QueryObject queryObject) {
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }

        Manager manager = getCurrentUserManager();
        if (manager == null || manager.getId() == null) {
            return getLoginAddress();
        }

        //判断是否需要审核
        Boolean approvalFlag = approvalProcessDefinitionService.wheatherNeedApproval(ApprovalType.Activity_Approval
                .getCode());
        modelMap.addAttribute("approvalFlag", approvalFlag.toString());

        modelMap.addAttribute("managerId", manager.getId());
        modelMap.addAttribute("currUserId", administer.getId());
        modelMap.addAttribute("roleId", manager.getRoleId());
        
        if(isSdEnvironment()){
            modelMap.addAttribute("isShandong", "true");
        }


        return "qrcode/index.ftl";
    }

    /**
     * 二维码查询
     *
     * @author qinqinyan
     */
    @RequestMapping("search")
    public void searchSubmitIndex(QueryObject queryObject, HttpServletResponse res, Long managerId) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 查询参数: 用户手机,状态,创建时间
         */
        setQueryParameter("name", queryObject);
        setQueryParameter("status", queryObject);
        // setQueryParameter("manageId", queryObject);
        if (managerId != null) {
            queryObject.getQueryCriterias().put("managerId", managerId.toString());
        }
        queryObject.getQueryCriterias().put("type", ActivityType.QRCODE.getCode().toString());

        if (!StringUtils.isEmpty(getRequest().getParameter("startTime"))) {
            queryObject.getQueryCriterias().put("startTime", getRequest().getParameter("startTime"));
            queryObject.getQueryCriterias().put("endTime", getRequest().getParameter("endTime") + " 23:59:59");
        }

        List<Activities> records = activitiesService.selectByMap(queryObject.toMap());
        Long count = activitiesService.countByMap(queryObject.toMap());

        //查询是否下载
        for (Activities item : records) {
            ActivityInfo activityInfo = activityInfoService.selectByActivityId(item.getActivityId());
            item.setDownload(activityInfo.getDownload());
        }

        //校验活动状态,如果存在未上架又已经结束的活动，则在此更改活动状态
        List<Activities> updateActivities = new ArrayList<Activities>();
        for (Activities activities : records) {
            if (activities.getStatus().toString().equals(ActivityStatus.FINISH_APPROVAL.getCode().toString())
                    && activities.getEndTime().getTime() < System.currentTimeMillis()) {
                activities.setStatus(ActivityStatus.END.getCode());

                Activities item = new Activities();
                item.setId(activities.getId());
                updateActivities.add(item);
            }
            activities.setActivityCreator(activityCreatorService.getByActId(ActivityType.QRCODE, activities.getId()));
        }
        activitiesService.batchChangeStatus(updateActivities, ActivityStatus.END.getCode());

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
     * 新建二维码
     *
     * @author qinqniyan
     */
    @RequestMapping("create")
    public String create(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Manager manager = getCurrentUserManager();
        if (manager == null || manager.getId() == null) {
            return getLoginAddress();
        }

        //根据省份标识设置参数
        setCqFlag(map);

        List<Enterprise> enterprises = enterprisesService.getEnterByManagerId(manager.getId());
        map.addAttribute("enterprises", enterprises);
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

        //产品的最大size
        map.addAttribute("maxProductSize", virtualProductService.getMaxSizeOfVirtualFlowProduct());

        return "qrcode/create.ftl";
    }

    /**
     * @Title: edit
     */
    @RequestMapping("edit")
    public String edit(ModelMap map, HttpServletRequest request, HttpServletResponse response, String activityId)
            throws IOException {
        if (StringUtils.isEmpty(activityId)) {
            map.addAttribute("errorMsg", "这条活动记录不存在!");
            return "error.ftl";
        }
        Administer administer = getCurrentUser();
        Manager manager = getCurrentUserManager();
        if (administer == null || administer.getId() == null || manager == null || manager.getId() == null) {
            return getLoginAddress();
        }
        Activities activities = activitiesService.selectByActivityId(activityId);
        ActivityInfo activityInfo = activityInfoService.selectByActivityId(activityId);
        
        if (activities != null && activities.getEntId() != null) {
            if (!activities.getCreatorId().toString().equals(administer.getId().toString())) {
                map.addAttribute("errorMsg", "对不起，您没有权限修改该条记录!");
                return "error.ftl";
            }
            
            if(activityInfo!=null && activityInfo.getDownload()!=null && activityInfo.getDownload().intValue()==1){
                map.addAttribute("errorMsg", "对不起，二维码已经下载过，不允许进行编辑操作!");
                return "error.ftl";
            }
            Enterprise enterprise = enterprisesService.selectByPrimaryKey(activities.getEntId());
            List<ActivityPrize> activityPrizes = activityPrizeService.selectByActivityId(activityId);
            //ActivityInfo activityInfo = activityInfoService.selectByActivityId(activityId);

            map.addAttribute("activities", activities);
            map.addAttribute("enterprise", enterprise);
            map.addAttribute("activityInfo", activityInfo);

            List<Enterprise> enterpriseList = enterprisesService.getEnterByManagerId(manager.getId());
            map.addAttribute("enterpriseList", enterpriseList);

            for (ActivityPrize item : activityPrizes) {
                if (item.getIsp().equals(IspType.CMCC.getValue())) {
                    map.addAttribute("cmProductId", item.getProductId());
                }
                if (item.getIsp().equals(IspType.UNICOM.getValue())) {
                    map.addAttribute("cuProductId", item.getProductId());
                }
                if (item.getIsp().equals(IspType.TELECOM.getValue())) {
                    map.addAttribute("ctProductId", item.getProductId());
                }
            }
            if ("shanghai".equals(getProvinceFlag())) {
                map.addAttribute("showOrderList", 1);
                List<ShOrderList> shOrderLists = shOrderListService.getByEnterId(enterpriseList.get(0).getId());
                map.addAttribute("orderList", shOrderLists);
            } else {
                map.addAttribute("showOrderList", 0);
            }
            //是否允许使用三网产品
            if (allowAllPlatformProduct()) {
                map.addAttribute("allowAllPlatformProduct", true);
            }

            //产品的最大size
            map.addAttribute("maxProductSize", virtualProductService.getMaxSizeOfVirtualFlowProduct());

            //根据省份标识设置参数
            setCqFlag(map);

            return "qrcode/edit.ftl";
        }
        map.addAttribute("errorMsg", "这条活动记录不存在!");
        return "error.ftl";
    }

    /**
     * 二维码预览页
     *
     * @author qinqinyan
     */
    @RequestMapping("preView")
    public String preSave(ModelMap map, HttpServletRequest request, HttpServletResponse response,
            Activities activities, ActivityInfo activityInfo, Long cmProductId, Long cuProductId, Long ctProductId,
            String cmMobileList, String cuMobileList, String ctMobileList, String invalidMobiles, String cmPrdSize,
            String cuPrdSize, String ctPrdSize) {

        activities.setName(xssService.stripXss(activities.getName()));
        //1、流量池、话费产品转化 2、流量包不转化
        //移动产品转换
        Product cmProduct = productService.get(cmProductId);
        try {
            //流量池、话费产品进行转换，如果是流量池、话费产品，对应的大小必须不能为空    
            if (cmProduct != null
                    && ((cmProduct.getType().byteValue() == ProductType.FLOW_ACCOUNT.getValue()) || cmProduct.getType()
                            .byteValue() == ProductType.MOBILE_FEE.getValue())) {
                cmProduct = virtualProductService
                        .initProcess(
                                (cmProduct.getFlowAccountProductId() != null)
                                        && cmProduct.getFlowAccountFlag().equals(
                                                FLOW_ACCOUNT_FLAG.REAL_PRODUCT.getCode()) ? cmProduct
                                        .getFlowAccountProductId() : cmProductId, cmPrdSize);
                if (cmProduct == null) {
                    map.addAttribute("errorMsg", "移动产品转化失败");
                    return "error.ftl";
                }
                cmProductId = cmProduct.getId();
            }
        } catch (ProductInitException e) {
            map.addAttribute("errorMsg", e.getMessage());
            return "error.ftl";
        }

        map.addAttribute("cmProductId", cmProductId);
        map.addAttribute("cmProduct", cmProduct);
        map.addAttribute("cmProductSize",
                (cmProduct != null && cmProduct.getProductSize() != null) ? sizeFun(cmProduct.getProductSize()) : "-");
        map.addAttribute(
                "cmPrice",
                (cmProduct != null && cmProduct.getFlowAccountFlag().equals(FLOW_ACCOUNT_FLAG.REAL_PRODUCT.getCode())) ? priceFun(cmProduct
                        .getPrice()) : "-");

        // 联通产品转换
        Product cuProduct = productService.get(cuProductId);
        try {
            //流量池、话费产品进行转换，如果是流量池、话费产品，对应的大小必须不能为空
            if (cuProduct != null
                    && (cuProduct.getType().byteValue() == ProductType.FLOW_ACCOUNT.getValue() || cuProduct.getType()
                            .byteValue() == ProductType.MOBILE_FEE.getValue())) {
                cuProduct = virtualProductService.initProcess(
                        (cuProduct.getFlowAccountProductId() != null) ? cuProduct.getFlowAccountProductId()
                                : cuProductId, cuPrdSize);
                if (cuProduct == null) {
                    map.addAttribute("errorMsg", "联通产品转化失败");
                    return "error.ftl";
                }
                cuProductId = cuProduct.getId();
            }
        } catch (ProductInitException e) {
            map.addAttribute("errorMsg", e.getMessage());
            return "error.ftl";
        }
        map.addAttribute("cuProductId", cuProductId);
        map.addAttribute("cuProduct", cuProduct);
        map.addAttribute("cuProductSize",
                (cuProduct != null && cuProduct.getProductSize() != null) ? sizeFun(cuProduct.getProductSize()) : "-");
        map.addAttribute(
                "cuPrice",
                (cuProduct != null && cuProduct.getPrice() != null && cuProduct.getFlowAccountFlag().equals(
                        FLOW_ACCOUNT_FLAG.REAL_PRODUCT.getCode())) ? priceFun(cuProduct.getPrice()) : "-");

        //电信产品转换
        Product ctProduct = productService.get(ctProductId);
        try {
            //流量池、话费产品进行转换，如果是流量池、话费产品，对应的大小必须不能为空
            if (ctProduct != null
                    && (ctProduct.getType().byteValue() == ProductType.FLOW_ACCOUNT.getValue() || ctProduct.getType()
                            .byteValue() == ProductType.MOBILE_FEE.getValue())) {
                ctProduct = virtualProductService.initProcess(
                        (ctProduct.getFlowAccountProductId() != null) ? ctProduct.getFlowAccountProductId()
                                : ctProductId, ctPrdSize);
                if (ctProduct == null) {
                    map.addAttribute("errorMsg", "联通产品转化失败");
                    return "error.ftl";
                }
                ctProductId = ctProduct.getId();
            }
        } catch (ProductInitException e) {
            map.addAttribute("errorMsg", e.getMessage());
            return "error.ftl";
        }
        map.addAttribute("ctProductId", ctProductId);
        map.addAttribute("ctProduct", ctProduct);
        map.addAttribute("ctProductSize",
                (ctProduct != null && ctProduct.getProductSize() != null) ? sizeFun(ctProduct.getProductSize()) : "-");
        map.addAttribute(
                "ctPrice",
                (ctProduct != null && ctProduct.getPrice() != null && ctProduct.getFlowAccountFlag().equals(
                        FLOW_ACCOUNT_FLAG.REAL_PRODUCT.getCode())) ? priceFun(ctProduct.getPrice()) : "-");

        Enterprise enterprise = enterprisesService.selectByPrimaryKeyForActivity(activities.getEntId());
        map.addAttribute("enterprises", enterprise);

        // 余额
        Product product = productService.getCurrencyProduct();
        if (product != null && product.getId() != null) {
            Account account = accountService.get(activities.getEntId(), product.getId(),
                    AccountType.ENTERPRISE.getValue());
            if (account != null && account.getCount() != null) {
                map.addAttribute("balance", account.getCount() * 1.00 / 100.00 + account.getCount() * 1.00 % 100.00);
            } else {
                map.addAttribute("balance", 0.00);
            }
        } else {
            map.addAttribute("balance", 0.00);
        }

        if (StringUtils.isEmpty(invalidMobiles)) {
            map.addAttribute("hasInvalidMobiles", "false");
        } else {
            map.addAttribute("hasInvalidMobiles", "true");
        }
        map.addAttribute("invalidMobiles", invalidMobiles);
        map.addAttribute("correctMobiles", getCorrectMobiles(cmMobileList, cuMobileList, ctMobileList));
        setCqFlag(map);
        return "qrcode/preview.ftl";
    }

    private String getCorrectMobiles(String cmMobileList, String cuMobileList, String ctMobileList) {
        String correctMobiles = "";
        if (!StringUtils.isEmpty(cmMobileList)) {
            if (StringUtils.isEmpty(correctMobiles)) {
                correctMobiles += cmMobileList;
            } else {
                correctMobiles += "," + cmMobileList;
            }
        }

        if (!StringUtils.isEmpty(cuMobileList)) {
            if (StringUtils.isEmpty(correctMobiles)) {
                correctMobiles += cuMobileList;
            } else {
                correctMobiles += "," + cuMobileList;
            }
        }

        if (!StringUtils.isEmpty(ctMobileList)) {
            if (StringUtils.isEmpty(correctMobiles)) {
                correctMobiles += ctMobileList;
            } else {
                correctMobiles += "," + ctMobileList;
            }
        }
        return correctMobiles;
    }

    /**
     * 实时获取活动规模信息
     *
     * @author qinqinyan
     */
    @RequestMapping("getActivityInfoAjax")
    public void getActivityInfoAjax(HttpServletRequest request, HttpServletResponse response, Long cmProductId,
            Long cuProductId, Long ctProductId, String activityId, Long prizeCount, Long entId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();

        Product cmProduct = productService.selectProductById(cmProductId);
        Product cuProduct = productService.selectProductById(cuProductId);
        Product ctProduct = productService.selectProductById(ctProductId);

        EntProduct cmEntProduct = entProductService.selectByProductIDAndEnterprizeID(cmProductId, entId);
        EntProduct cuEntProduct = entProductService.selectByProductIDAndEnterprizeID(cuProductId, entId);
        EntProduct ctEntProduct = entProductService.selectByProductIDAndEnterprizeID(ctProductId, entId);

        //流量池产品，cmEntProduct替换为流量池的对应关系
        /*if (cmProduct != null && cmProduct.getFlowAccountFlag() == 1) {
            Product flowAccountProduct = productService.getFlowProduct();
            cmEntProduct = entProductService.selectByProductIDAndEnterprizeID(flowAccountProduct.getId(), entId);
        }*/

        if (cmProduct != null && cmProduct.getFlowAccountFlag() != null
                && cmProduct.getFlowAccountFlag().intValue() == FLOW_ACCOUNT_FLAG.VIRTUAL_PRODUCT.getCode().intValue()) {
            Product flowAccountProduct = productService.get(cmProduct.getId());
            if (flowAccountProduct != null) {
                cmEntProduct = entProductService.selectByProductIDAndEnterprizeID(
                        flowAccountProduct.getFlowAccountProductId(), entId);
            } else {
                cmEntProduct = null;
            }
        }

        Integer totalPrice = 0;
        Long totalSize = 0L;
        Long userCount = prizeCount.longValue();

        if (cmProductId != null && cuProductId != null && ctProductId != null && prizeCount >= 10L) {
            // 三种产品都存在，则按照移动:联通:电信=7:2:1
            Long cmCount = prizeCount.longValue() * 7 / 10;
            Long cuCount = prizeCount.longValue() * 2 / 10;
            Long ctCount = prizeCount.longValue() / 10;

            if (prizeCount.longValue() % 10 != 0) {
                cmCount += 1;
                cuCount += 1;
                ctCount += 1;
            }

            totalSize = cmCount.longValue() * cmProduct.getProductSize().longValue() + cuCount.longValue()
                    * cuProduct.getProductSize().longValue() + ctCount.longValue()
                    * ctProduct.getProductSize().longValue();

            if (cmProduct.getPrice() != null && cmEntProduct != null && cmEntProduct.getDiscount() != null) {
                totalPrice += cmCount.intValue() * cmProduct.getPrice().intValue()
                        * cmEntProduct.getDiscount().intValue() / 100;
            }

            if (cuProduct.getPrice() != null && cuEntProduct != null && cuEntProduct.getDiscount() != null) {
                totalPrice += cuCount.intValue() * cuProduct.getPrice().intValue()
                        * cuEntProduct.getDiscount().intValue() / 100;
            }

            if (ctProduct.getPrice() != null && ctEntProduct != null && ctEntProduct.getDiscount() != null) {
                totalPrice += ctCount.intValue() * ctProduct.getPrice().intValue()
                        * ctEntProduct.getDiscount().intValue() / 100;
            }
            map.put("totalPrice", priceFun(totalPrice));
            map.put("totalSize", sizeFun(totalSize));
        } else if (cmProductId == null && cuProductId == null && ctProductId == null) {
            map.put("totalPrice", 0);
            map.put("totalSize", 0);
        } else {
            Integer avePrice = 0;
            Long aveSize = 0L;
            Double aveDiscount = 0D;

            if (cmProduct != null && cmProduct.getProductSize() != null && cmProduct.getPrice() != null) {
                avePrice += cmProduct.getPrice();
                aveSize += cmProduct.getProductSize();

                if (cmEntProduct != null && cmEntProduct.getDiscount() != null) {
                    aveDiscount += cmEntProduct.getDiscount();
                }
            }

            if (cuProduct != null && cuProduct.getProductSize() != null && cuProduct.getPrice() != null) {
                avePrice += cuProduct.getPrice();
                aveSize += cuProduct.getProductSize();

                if (cuEntProduct != null && cuEntProduct.getDiscount() != null) {
                    aveDiscount += cuEntProduct.getDiscount();
                }
            }

            if (ctProduct != null && ctProduct.getProductSize() != null && ctProduct.getPrice() != null) {
                avePrice += ctProduct.getPrice();
                aveSize += ctProduct.getProductSize();

                if (ctEntProduct != null && ctEntProduct.getDiscount() != null) {
                    aveDiscount += ctEntProduct.getDiscount();
                }
            }

            if (cmProduct != null && cuProduct != null && ctProduct != null) {
                avePrice = new BigDecimal(avePrice / 3).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                aveSize = new BigDecimal(aveSize / 3).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
                aveDiscount = aveDiscount / 3;
            } else if (cmProduct != null && cuProduct != null && ctProduct == null || cmProduct != null
                    && cuProduct == null && ctProduct != null || cmProduct == null && cuProduct != null
                    && ctProduct != null) {
                avePrice = avePrice / 2;
                aveSize = aveSize / 2;
                aveDiscount = aveDiscount / 2;
            }

            totalPrice = new BigDecimal(avePrice.intValue() * prizeCount.intValue() * aveDiscount.doubleValue() / 100)
                    .setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            totalSize = aveSize.longValue() * prizeCount.longValue();

            String totalSizeFun = sizeFun(totalSize);

            map.put("totalPrice", (double) totalPrice / 100);
            map.put("totalSize", totalSizeFun.substring(0, totalSizeFun.indexOf('.') + 3));
        }

        map.put("prizeCount", prizeCount);
        map.put("userCount", userCount);
        map.put("success", "success");
        response.getWriter().write(JSON.toJSONString(map));
        return;
    }

    private String sizeFun(Long size) {
        if (size == null) {
            return "-";
        }

        if (size < 1024) {
            return size + "KB";
        } else if (size >= 1024 && size < 1024 * 1024) {
            return (size * 1.0 / 1024) + "MB";
        } else {
            return (size * 1.0 / 1024 / 1024) + "GB";
        }

    }

    private String priceFun(Integer price) {
        if (price == null) {
            return "0";
        }
        Double p = price / 100.00;
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(p);
    }


    /**
     * 保存流量券活动记录
     */
    @RequestMapping("saveAjax")
    public void saveAjax(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Map map = new HashedMap();
        String msg = "";
        Boolean result = false;
        Administer admin = getCurrentUser();
        Manager manager = getCurrentUserManager();
        if (admin == null || admin.getId() == null || manager == null) {
            logger.info("用户异常,保存失败!");
            msg = "用户异常,保存失败!";
        }

        String activityParams = request.getParameter("activity");
        String activityInfoParams = request.getParameter("activityInfo");
        String cmProductIdStr = request.getParameter("cmProductId");
        String cuProductIdStr = request.getParameter("cuProductId");
        String ctProductIdStr = request.getParameter("ctProductId");
        String correctMobiles = request.getParameter("correctMobiles");
        String cmPrdSize = request.getParameter("cmPrdSize");
        String cuPrdSize = request.getParameter("cuPrdSize");
        String ctPrdSize = request.getParameter("ctPrdSize");

        logger.info("activityParams:"+activityParams);
        if(StringUtils.isEmpty(activityParams) || (StringUtils.isEmpty(cmProductIdStr)
            && StringUtils.isEmpty(cuProductIdStr) && StringUtils.isEmpty(ctProductIdStr))){//
            msg = "参数异常,保存失败!";
        }else{
            Activities activities = null;
            ActivityInfo activityInfo = null;
            try{
                activities = JSON.parseObject(activityParams, Activities.class);
                activityInfo = JSON.parseObject(activityInfoParams, ActivityInfo.class);
            }catch(Exception e){
                logger.info(e.getMessage());
            }

            if(activities != null){
                //检查activity的enterId是否是该账号有权操作的企业
                if(!enterprisesService.isParentManage(activities.getEntId(), manager.getId())){
                    logger.info("活动参数中的企业id={}，当前用户managerId={}，不存在上下级管理关系!", activities.getEntId(), manager.getId());
                    msg = "对不起，您无权限进行该操作";
                    map.put("msg", msg);
                    map.put("result", result.toString());
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }

                activities.setEndTime(DateUtil.getEndTimeOfDate(activities.getEndTime()));
                activities.setName(xssService.stripXss(activities.getName()));

                //移动的产品转化,针对流量池、话费
                Product cmProduct;
                Long cmProductId = null;
                if(!StringUtils.isEmpty(cmProductIdStr)){
                    if(!StringUtils.isEmpty(cmPrdSize)){
                        try {
                            cmProduct = virtualProductService.initProcess(Long.valueOf(cmProductIdStr), cmPrdSize);
                            if (cmProduct == null) {
                                logger.info("产品转化失败");
                            }else{
                                cmProductId = cmProduct.getId();
                            }
                        } catch (ProductInitException e) {
                            logger.info("产品转化失败,失败原因："+e.getMessage());
                        }
                    }else{
                        cmProductId = Long.valueOf(cmProductIdStr);
                    }
                }

                Product cuProduct;
                Long cuProductId = null;
                if(!StringUtils.isEmpty(cuProductIdStr)){
                    if(!StringUtils.isEmpty(cuPrdSize)){
                        try {
                            cuProduct = virtualProductService.initProcess(Long.valueOf(cuProductIdStr), cuPrdSize);
                            if (cuProduct == null) {
                                logger.info("产品转化失败");
                            }else{
                                cuProductId = cuProduct.getId();
                            }
                        } catch (ProductInitException e) {
                            logger.info("产品转化失败,失败原因："+e.getMessage());
                        }
                    }else{
                        cuProductId = Long.valueOf(cuProductIdStr);
                    }
                }


                Product ctProduct;
                Long ctProductId = null;
                if(!StringUtils.isEmpty(ctProductIdStr)){
                    if(!StringUtils.isEmpty(ctPrdSize)){
                        try {
                            ctProduct = virtualProductService.initProcess(Long.valueOf(ctProductIdStr), ctPrdSize);
                            if (ctProduct == null) {
                                logger.info("产品转化失败");
                            }else{
                                ctProductId = ctProduct.getId();
                            }
                        } catch (ProductInitException e) {
                            logger.info("产品转化失败,失败原因："+e.getMessage());
                        }
                    }else{
                        ctProductId = Long.valueOf(ctProductIdStr);
                    }
                }

                if (org.apache.commons.lang.StringUtils.isBlank(activities.getActivityId())) {
                    // 新建
                    activities.setType(ActivityType.QRCODE.getCode());//活动类型二维码
                    activities.setCreatorId(admin.getId());

                    try{
                        if (activitiesService.insertQRcodeActivity(activities, activityInfo, cmProductId, cuProductId, ctProductId,
                            correctMobiles)) {
                            logger.info("插入活动记录成功：" + JSONArray.toJSONString(activities));
                            msg = "保存成功!";
                            result = true;
                        } else {
                            msg = "保存失败!";
                        }
                    }catch (RuntimeException e){
                        logger.error("保存失败，失败原因：" + e.getMessage());
                        msg = "保存失败!";
                    }

                } else {
                    Activities orgActivities = activitiesService.selectByActivityId(activities.getActivityId());
                    ActivityInfo orgActivityInfo = activityInfoService.selectByActivityId(activities.getActivityId());
                    if(orgActivities==null || orgActivityInfo==null){
                        logger.info("更据活动id = "+activities.getActivityId()+"未从数据库里面查找到活动信息");
                        msg = "信息错误，保存失败!";
                        map.put("msg", msg);
                        map.put("result", result.toString());
                        response.getWriter().write(JSON.toJSONString(map));
                        return;
                    }

                    //对根据前端传过来的活动id从数据库查找出来的活动的状态进行判断，只要以保存和已驳回的可进行编辑保存
                    if(!(orgActivities.getStatus().toString().equals(ActivityStatus.SAVED.getCode().toString())
                        || orgActivities.getStatus().toString().equals(ActivityStatus.REJECT.getCode().toString()))){
                        logger.info("该活动id = " + activities.getActivityId() + "状态下不可进行编辑操作");
                        msg = "该活动状态下不可进行编辑操作!";
                        map.put("msg", msg);
                        map.put("result", result.toString());
                        response.getWriter().write(JSON.toJSONString(map));
                        return;
                    }

                    if(orgActivityInfo.getDownload()!=null && orgActivityInfo.getDownload().intValue()==1){
                        logger.info("该活动id = "+activities.getActivityId()+"二维码已经被下载过，不允许进行编辑操作");
                        msg = "二维码已经被下载过，不允许进行编辑操作!";
                        map.put("msg", msg);
                        map.put("result", result.toString());
                        response.getWriter().write(JSON.toJSONString(map));
                        return;
                    }

                    if(!enterprisesService.isParentManage(orgActivities.getEntId(), manager.getId())){
                        logger.info("编辑活动记录失败，活动参数中的企业id={}，当前用户managerId={}，不存在上下级管理关系!", activities.getEntId(), manager.getId());
                        msg = "对不起，您无权限进行该操作";
                        map.put("msg", msg);
                        map.put("result", result.toString());
                        response.getWriter().write(JSON.toJSONString(map));
                        return;
                    }

                    //检查数据库查出来  -- 检查activity的enterId是否是该账号有权操作的企业
                    if(!enterprisesService.isParentManage(orgActivities.getEntId(), manager.getId())){
                        logger.info("用户managerId = "+manager.getId()+"无权限操作该活动");
                        msg = "对不起，您无权限进行该操作";
                        map.put("msg", msg);
                        map.put("result", result.toString());
                        response.getWriter().write(JSON.toJSONString(map));
                        return;
                    }
                    // 编辑
                    try{
                        if (activitiesService.editQRcodeActivity(activities, activityInfo, cmProductId, cuProductId, ctProductId,
                            correctMobiles)) {
                            logger.info("编辑活动记录成功：" + JSONArray.toJSONString(activities));
                            msg = "编辑成功!";
                            result = true;
                        }else{
                            logger.info("编辑失败：" + JSONArray.toJSONString(activities));
                            msg = "编辑失败!";
                        }
                    }catch (RuntimeException e){
                        logger.error("编辑失败，失败原因：" + e.getMessage());
                        msg = "编辑失败!";
                    }
                }
            }
        }
        map.put("msg", msg);
        map.put("result", result.toString());
        response.getWriter().write(JSON.toJSONString(map));
        return;
    }

    @RequestMapping("getProductAjax")
    public void getProductAjax(HttpServletRequest request, HttpServletResponse response, Long productId)
            throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        if (productId != null) {
            Product product = productService.selectProductById(productId);
            if (product != null) {
                map.put("success", "success");
                map.put("productId", product.getId());
                map.put("productName", product.getName());
                map.put("productCode", product.getProductCode());
                map.put("productPrice", priceFun(product.getPrice()));
                map.put("ownership", product.getOwnershipRegion());
                map.put("roaming", product.getRoamingRegion());
                map.put("type", product.getType());
                map.put("isp", product.getIsp());

                if (product.getType().byteValue() == ProductType.CURRENCY.getValue()
                        || product.getType().byteValue() == ProductType.MOBILE_FEE.getValue()) {//话费产品，产品大小单位为分
                    map.put("productSize", product.getProductSize() == null ? "0元" : product.getProductSize() / 100.00
                            + "元");
                    map.put("size", product.getProductSize() == null ? "0" : product.getProductSize() / 100.00);//不带单位的产品大小，默认单位元
                } else {//流量包产品、流量池产品单位为KB
                    map.put("productSize", sizeFun(product.getProductSize()));//带单位的产品大小
                    map.put("size", product.getProductSize() == null ? 0 : SizeUnits.KB.toMB(product.getProductSize()));//不带单位的产品大小，默认单位MB
                }
                response.getWriter().write(JSON.toJSONString(map));
                return;
            }
        }
        map.put("success", "fail");
        response.getWriter().write(JSON.toJSONString(map));
        return;
    }

    /**
     * 获取大小为MB单位的产品大小
     *
     * @Title: getMbProductAjax
     */
    @RequestMapping("getMbProductAjax")
    public void getMbProductAjax(HttpServletRequest request, HttpServletResponse response, Long productId)
            throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        if (productId != null) {
            Product product = productService.selectProductById(productId);
            if (product != null) {
                map.put("success", "success");
                map.put("productId", product.getId());
                map.put("productName", product.getName());
                map.put("productCode", product.getProductCode());
                map.put("productSize", SizeUnits.KB.toMB(product.getProductSize()));
                map.put("productPrice", priceFun(product.getPrice()));
                map.put("ownership", product.getOwnershipRegion());
                map.put("roaming", product.getRoamingRegion());

                map.put("isp", product.getIsp());

                response.getWriter().write(JSON.toJSONString(map));
                return;
            }
        }
        map.put("success", "fail");
        response.getWriter().write(JSON.toJSONString(map));
        return;
    }

    /**
     * 下架
     *
     * @author qinqinyan
     */
    @RequestMapping("offShelf")
    public String offShelf(ModelMap map, String activityId) {
        Administer admin = getCurrentUser();
        Activities activities = activitiesService.selectByActivityId(activityId);
        if (!admin.getId().toString().equals(activities.getCreatorId().toString())) {
            map.addAttribute("errorMsg", "对不起，您没有权限操作该条记录!");
            return "error.ftl";
        }
        if (activitiesService.changeStatus(activityId, ActivityStatus.DOWN.getCode())) {
            return "redirect:index.html";
        }
        map.addAttribute("errorMsg", "下架失败!");
        return "error.ftl";
    }

    /**
     * @Title: onShelf
     */
    @RequestMapping("onShelf")
    public void onShelf(String activityId, HttpServletResponse resp) throws IOException {
        Administer admin = getCurrentUser();
        Activities activities = activitiesService.selectByActivityId(activityId);
        Map returnMap = new HashMap();
        if (!admin.getId().toString().equals(activities.getCreatorId().toString())) {
            returnMap.put("message", "对不起，您没有权限操作该条记录!");
            resp.getWriter().write(JSONObject.toJSONString(returnMap));
            logger.error("对不起，您没有权限操作该条记录!");
            return;
        }

        // 活动上架
        String result = activitiesService.onShelf(activityId);
        if ("success".equals(result)) {
            returnMap.put("result", "true");
            resp.getWriter().write(JSON.toJSONString(returnMap));
            return;
        } else {
            returnMap.put("message", result);
            resp.getWriter().write(JSON.toJSONString(returnMap));
            return;
        }
    }

    /**
     * 流量券记录详情
     *
     * @Title: showDetail
     * @Description: TODO
     * @Author: wujiamin
     * @date 2016年8月18日上午11:40:01
     */
    @RequestMapping("detail")
    public String showDetail(ModelMap modelMap, String activityId) {
        Activities activity = null;
        ActivityInfo activityInfo = null;
        if (StringUtils.isEmpty(activityId) || (activity = activitiesService.selectByActivityId(activityId)) == null
                || (activityInfo = activityInfoService.selectByActivityId(activity.getActivityId())) == null) {
            logger.error("流量券记录不存在，activityId={}", activityId);
            modelMap.put("errorMsg", "流量券记录不存在");
            return "error.ftl";
        }
      
        //检查activity的enterId是否是该账号有权操作的企业           
        if(!enterprisesService.isParentManage(activity.getEntId(), getCurrentUserManager().getId())){
            logger.info("活动参数中的企业id={}，当前用户managerId={}，不存在上下级管理关系!", activity.getEntId(), getCurrentUserManager().getId());
            modelMap.addAttribute("errorMsg", "没有查看该活动的权限!");
            return "error.ftl";
        }        

        Enterprise enterprise = enterprisesService.selectByPrimaryKeyForActivity(activity.getEntId());
        if (enterprise == null) {
            logger.error("创建流量券的企业不存在，enterId={}", activity.getEntId());
            modelMap.put("errorMsg", "流量券记录不存在");
            return "error.ftl";
        }

        //历史审核记录
        List<ApprovalRecord> approvalRecords = approvalRecordService.selectByActivityIdForActivity(activityId);
        modelMap.put("opinions", approvalRecords);

        modelMap.put("enterprise", enterprise);
        modelMap.put("activity", activity);
        // 活动规模（估计）
        modelMap.put("activityInfo", activityInfo);

        // 活动规模（当前）
        CurrentActivityInfo currentActivityInfo = activityWinRecordService.getCurrentActivityInfo(activity
                .getActivityId());
        modelMap.put("currentActivityInfo", currentActivityInfo);

        // 奖品
        List<ActivityPrize> prizes = activityPrizeService.getDetailByActivityId(activity.getActivityId());

        ActivityPrize prizeM = null;
        ActivityPrize prizeU = null;
        ActivityPrize prizeT = null;
        ActivityPrize prizeA = null;
        //奖品按照移动M、联通U、电信T、三网A排序
        for (ActivityPrize prize : prizes) {
            if (IspType.CMCC.getValue().equalsIgnoreCase(prize.getIsp())) {
                prizeM = prize;
            }
            if (IspType.UNICOM.getValue().equalsIgnoreCase(prize.getIsp())) {
                prizeU = prize;
            }
            if (IspType.TELECOM.getValue().equalsIgnoreCase(prize.getIsp())) {
                prizeT = prize;
            }
            if (IspType.ALL.getValue().equalsIgnoreCase(prize.getIsp())) {
                prizeA = prize;
            }
        }
        List<ActivityPrize> orderPrizes = new ArrayList<ActivityPrize>();
        if (prizeM != null) {
            orderPrizes.add(prizeM);
        }
        if (prizeU != null) {
            orderPrizes.add(prizeU);
        }
        if (prizeT != null) {
            orderPrizes.add(prizeT);
        }
        if (prizeA != null) {
            orderPrizes.add(prizeA);
        }

        modelMap.put("prizes", orderPrizes);
        setCqFlag(modelMap);

        return "qrcode/detail.ftl";
    }

    /**
     * @Title: checkCQMobile
     */
    @RequestMapping(value = "/check")
    public void checkCQMobile(String mobile, HttpServletResponse resp) throws IOException {
        if (StringUtils.isValidMobile(mobile)) {
            resp.getWriter().write("true");
        } else {
            resp.getWriter().write("false");
        }
    }

    /**
     * 赠送用户详情
     *
     * @Title: recordsIndex
     * @Description: TODO
     * @Author: xujue
     * @date 2016年8月25日下午3:49:07
     */
    @RequestMapping("records")
    public String recordsIndex(ModelMap map, String activityId) {
        Manager manager = getCurrentUserManager();
        if(manager==null){
            return getLoginAddress();
        }
        Activities activities = activitiesService.selectByActivityId(activityId);
        if(activities!=null){
            if(!managerService.managedByManageId(activities.getEntId(), manager.getId())){
                map.put("errorMsg", "对不起，您无权限查看活动详情");
                return "error.ftl";
            }else{
                map.put("activityId", activityId);
                return "qrcode/records.ftl";
            }
        }else{
            map.put("errorMsg", "参数异常");
            return "error.ftl";
        }
    }

    /**
     * 赠送用户的筛选
     *
     * @Title: searchRecords
     * @Description: TODO
     * @Author: xujue
     * @date 2016年8月25日下午3:49:24
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
        setQueryParameter("status", queryObject);
        setQueryParameter("isp", queryObject);
        setQueryParameter("activityId", queryObject);

        // 数据库查找符合查询条件的个数
        int enterpriseCount = activityWinRecordService.showForPageResultCount(queryObject);
        List<ActivityWinRecord> enterpriseList = activityWinRecordService.showForPageResult(queryObject);

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", enterpriseList);
        json.put("total", enterpriseCount);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载二维码
     *
     * @Title: downloadQrCode
     * @Description: TODO
     * @Author: xujue
     * @date 2016年8月26日下午3:49:24
     */
    @RequestMapping(value = "/downloadQrCode")
    public void downloadQrCode(String activitiesId, ModelMap modelMap, QueryObject queryObject, HttpServletRequest res,
            HttpServletResponse resp) throws IOException {

        ActivityInfo activityInfo = activityInfoService.selectByActivityId(activitiesId);
        Activities activities = activitiesService.selectByActivityId(activitiesId);

        String fileName = activities.getName() + "_二维码" + "_"
                + new SimpleDateFormat("yyyyMMddHHmmss").format(activityInfo.getCreateTime());
        // fileName = URLDecoder.decode(fileName,"UTF-8");
        logger.info("decode,fileName:" + fileName);

        if (qRCodeService.qRCodeCommon(activityInfo, fileName, res, resp)) {
            FileUtils.deleteQuietly(new File(getQRCodeDownloadPath() + File.separator + activityInfo.getId()
                    + File.separator + fileName + ".zip"));
        } else {
            resp.sendError(403);
        }

    }

    public String getQRCodeDownloadPath() {
        return globalConfigService.get(GlobalConfigKeyEnum.QRCODE_DOWNLOAD_PATH.getKey());
    }

    /**
     * 营销活动提交审批申请
     *
     * @author qinqinyan
     */
    @RequestMapping("submitActivityApprovalAjax")
    public void submitActivityApprovalAjax(HttpServletResponse resp, String activityId)
            throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        Manager manager = getCurrentUserManager();
        Administer administer = getCurrentUser();
        
        if(manager==null || administer==null){
            map.put("submitRes", "fail");
            map.put("errorMsg", "用户信息异常");
        }else{
            Long currentId = administer.getId();
            Long roleId = manager.getId();
            
            Activities activities = activitiesService.selectByActivityId(activityId);
            if(!managerService.managedByManageId(activities.getEntId(), manager.getId())){
                map.put("submitRes", "fail");
                map.put("errorMsg", "对不起，您无权限提交审核记录");
            }else{
                map = activitiesService.submitApproval(activityId, currentId, roleId);
            }
        }
        resp.getWriter().write(JSON.toJSONString(map));
        return;
    }
    
    /**
     * 判断是否山东环境
     */
    private boolean isSdEnvironment(){
        return "sd".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()));
    }

}
