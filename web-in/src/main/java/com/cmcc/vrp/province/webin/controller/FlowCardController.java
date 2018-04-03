package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.enums.BlackAndWhiteListType;
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
import com.cmcc.vrp.province.service.ActivityBlackAndWhiteService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.JudgeIspService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.ShOrderListService;
import com.cmcc.vrp.province.service.VirtualProductService;
import com.cmcc.vrp.province.service.XssService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.util.Constants.FLOW_ACCOUNT_FLAG;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: FlowCardController
 * @Description: 流量卡管理
 */
@Controller
@RequestMapping("/manage/flowcard")
public class FlowCardController extends BaseController {
    /*
     * 上传文件大小的限制
     */
    public static final int MAX_FILE_SIZE = 1;// 单位MB
    /*
     * 设定上传最大记录数量为100个
     */
    public static final int UPLOAD_MAX_RECORDS = 100;
    private static final Logger logger = LoggerFactory.getLogger(FlowCardController.class);
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    AccountService accountService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    EntProductService entProductService;
    @Autowired
    ActivityBlackAndWhiteService activityBlackAndWhiteService;
    @Autowired
    JudgeIspService judgeIspService;
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
    ApprovalRecordService approvalRecordService;
    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Autowired
    VirtualProductService virtualProductService;
    @Autowired
    private ShOrderListService shOrderListService;
    @Autowired
    ManagerService managerService;
    @Autowired
    XssService xssService;

    /**
     * 流量卡管理首页
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
        Boolean approvalFlag = approvalProcessDefinitionService
                .wheatherNeedApproval(ApprovalType.Activity_Approval.getCode());
        modelMap.addAttribute("approvalFlag", approvalFlag.toString());

        modelMap.addAttribute("managerId", manager.getId());
        modelMap.addAttribute("currUserId", administer.getId());
        modelMap.addAttribute("roleId", manager.getRoleId());

        return "flowcard/index.ftl";
    }

    /**
     * 流量卡活动查询
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
        //setQueryParameter("manageId", queryObject);
        if (managerId != null) {
            queryObject.getQueryCriterias().put("managerId", managerId.toString());
        }
        queryObject.getQueryCriterias().put("type", ActivityType.FLOWCARD.getCode().toString());

        if (!StringUtils.isEmpty(getRequest().getParameter("startTime"))) {
            queryObject.getQueryCriterias().put("startTime", getRequest().getParameter("startTime"));
            queryObject.getQueryCriterias().put("endTime", getRequest().getParameter("endTime") + " 23:59:59");
        }


        List<Activities> records = activitiesService.selectByMap(queryObject.toMap());
        Long count = activitiesService.countByMap(queryObject.toMap());

        //校验活动状态,如果存在未上架又已经结束的活动，则在此更改活动状态
        List<Activities> updateActivities = new ArrayList<Activities>();
        for(Activities activities : records){
            if(activities.getStatus().toString().equals(ActivityStatus.FINISH_APPROVAL.getCode().toString())
                && activities.getEndTime().getTime()<System.currentTimeMillis()){
                activities.setStatus(ActivityStatus.END.getCode());

                Activities item = new Activities();
                item.setId(activities.getId());
                updateActivities.add(item);
            }
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
     * 获取产品列表
     *
     * @author qinqinyan
     */
    @RequestMapping("searchProduct")
    public void searchProduct(QueryObject queryObject, HttpServletRequest request, HttpServletResponse response,
                              Long entId, String type, Long orderId) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        /**
         * 查询参数: 产品名称,产品编码
         */
        setQueryParameter("productName", queryObject);
        setQueryParameter("productCode", queryObject);

        if (entId != null && !org.apache.commons.lang.StringUtils.isBlank(type)) {
            queryObject.getQueryCriterias().put("enterId", entId.toString());
            queryObject.getQueryCriterias().put("isp", type);
        }
        if (orderId != null) {
            queryObject.getQueryCriterias().put("orderId", orderId);
        }

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
     * 获取企业余额ajax
     *
     * @author qinqinyan
     */
    @RequestMapping("getBalanceAjax")
    public void getBalanceAjax(HttpServletRequest request, HttpServletResponse response,
                               Long entId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        if (entId != null) {
            Product product = productService.getCurrencyProduct();
            if (product != null) {
                Account account = accountService.get(entId, product.getId(), AccountType.ENTERPRISE.getValue());
                if (account != null && account.getCount() != null) {
                    map.put("success", "success");
                    java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    map.put("balance", nf.format((account.getCount() / 100.0)) + "元");
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }
            }
        }
        map.put("success", "fail");
        response.getWriter().write(JSON.toJSONString(map));
        return;
    }

    /**
     * 新建流量券
     *
     * @author qinqniyan
     */
    @RequestMapping("create")
    public String create(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Manager manager = getCurrentUserManager();
        if (manager == null || manager.getId() == null) {
            return getLoginAddress();
        }
        if (getProvinceFlag().equals("chongqing")) {
            map.addAttribute("cqFlag", 1);
        } else {
            map.addAttribute("cqFlag", 0);
        }
        

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

        return "flowcard/create.ftl";
    }

    /**
     * 新增流量券预览页
     *
     * @param activities     活动信息
     * @param cmProductId    移动产品id
     * @param cuProductId    联通产品id
     * @param ctProductId    电信产品id
     * @param invalidMobiles 错误手机号码json
     * @author qinqinyan
     */
    //@RequestMapping("preView")
    public String preSave(ModelMap map, HttpServletRequest request, HttpServletResponse response,
                          Activities activities,
                          Long cmProductId, Long cuProductId, Long ctProductId,
                          String invalidMobiles,
                          String cmMobileList, String cuMobileList, String ctMobileList,
                          String cmUserSet, String cuUserSet, String ctUserSet, String prdSize) {
        logger.info("invalidMobiles:" + invalidMobiles);

        //移动的产品转化,针对流量池、话费
        Product cmProduct;
        try {
            cmProduct = virtualProductService.initProcess(cmProductId, prdSize);
            if (cmProduct == null) {
                map.addAttribute("errorMsg", "产品转化失败");
                return "error.ftl";
            }
            cmProductId = cmProduct.getId();
        } catch (ProductInitException e) {
            map.addAttribute("errorMsg", e.getMessage());
            return "error.ftl";
        }

        map.addAttribute("cmProductId", cmProductId);
        map.addAttribute("cmProduct", cmProduct);
        if (cmProduct.getType() == 3) {
            map.addAttribute("cmProductSize", (cmProduct.getProductSize() != null) ? cmProduct.getProductSize() : "-");
        } else {
            map.addAttribute("cmProductSize", (cmProduct.getProductSize() != null) ? sizeFun(cmProduct.getProductSize()) : "-");
        }       
        map.addAttribute("cmPrice", (cmProduct.getPrice() != null) ? priceFun(cmProduct.getPrice()) : "-");
        //联通
        Product cuProduct = productService.selectProductById(cuProductId);
        map.addAttribute("cuProductId", cuProductId);
        map.addAttribute("cuProduct", cuProduct);
        map.addAttribute("cuProductSize", (cuProduct != null && cuProduct.getProductSize() != null) ? sizeFun(cuProduct.getProductSize()) : "-");
        map.addAttribute("cuPrice", (cuProduct != null && cuProduct.getPrice() != null) ? priceFun(cuProduct.getPrice()) : "-");
        //电信
        Product ctProduct = productService.selectProductById(ctProductId);
        map.addAttribute("ctProduct", ctProduct);
        map.addAttribute("ctProductId", ctProductId);
        map.addAttribute("ctProductSize", (ctProduct != null && ctProduct.getProductSize() != null) ? sizeFun(ctProduct.getProductSize()) : "-");
        map.addAttribute("ctPrice", (ctProduct != null && ctProduct.getPrice() != null) ? priceFun(ctProduct.getPrice()) : "-");

        logger.info("activities：" + JSON.toJSONString(activities));
        Enterprise enterprise = enterprisesService.selectByPrimaryKeyForActivity(activities.getEntId());
        map.addAttribute("enterprises", enterprise);

        //余额
        Product product = productService.getCurrencyProduct();
        if (product != null && product.getId() != null) {
            Account account = accountService.get(activities.getEntId(), product.getId(), AccountType.ENTERPRISE.getValue());
            if (account != null && account.getCount() != null) {
                map.addAttribute("balance", account.getCount() * 1.00 / 100.00 + account.getCount() * 1.00 % 100.00);
            } else {
                map.addAttribute("balance", 0.00);
            }
        } else {
            map.addAttribute("balance", 0.00);
        }

        map.addAttribute("activities", activities);

        String correctMobiles = getCorrectMobiles(cmProductId, cuProductId, ctProductId,
                cmMobileList, cuMobileList, ctMobileList);
        String newInvalidMobiles = getInvalidMobiles(cmProductId, cuProductId, ctProductId,
                cmMobileList, cuMobileList, ctMobileList, invalidMobiles);

        if (StringUtils.isEmpty(newInvalidMobiles)) {
            map.addAttribute("hasInvalidMobiles", "false");
        } else {
            map.addAttribute("hasInvalidMobiles", "true");
        }
        map.addAttribute("invalidMobiles", newInvalidMobiles);
        map.addAttribute("correctMobiles", correctMobiles);

        map.addAttribute("cmMobileList", cmProductId != null ? cmMobileList : "");
        map.addAttribute("cuMobileList", cuProductId != null ? cuMobileList : "");
        map.addAttribute("ctMobileList", ctProductId != null ? ctMobileList : "");

        map.addAttribute("cmUserSet", cmProductId != null ? cmUserSet : "");
        map.addAttribute("cuUserSet", cuProductId != null ? cuUserSet : "");
        map.addAttribute("ctUserSet", ctProductId != null ? ctUserSet : "");

        return "flowcard/preview.ftl";
    }

    /**
     * 获取正确手机号
     */
    private String getCorrectMobiles(Long cmProductId, Long cuProductId, Long ctProductId,
                                     String cmMobileList, String cuMobileList, String ctMobileList) {
        String correctMobiles = "";
        //移动
        if (!(cmProductId != null && StringUtils.isEmpty(cmMobileList))) {
            if (!(cmProductId == null && (!StringUtils.isEmpty(cmMobileList)))) {
                if (StringUtils.isEmpty(correctMobiles)) {
                    correctMobiles += cmMobileList;
                } else {
                    correctMobiles += "," + cmMobileList;
                }
            }
        }
        //联通
        if (!(cuProductId != null && StringUtils.isEmpty(cuMobileList))) {
            if (!(cuProductId == null && (!StringUtils.isEmpty(cuMobileList)))) {
                if (StringUtils.isEmpty(correctMobiles)) {
                    correctMobiles += cuMobileList;
                } else {
                    correctMobiles += "," + cuMobileList;
                }
            }
        }

        //电信
        if (!(ctProductId != null && StringUtils.isEmpty(ctMobileList))) {
            if (!(ctProductId == null && (!StringUtils.isEmpty(ctMobileList)))) {
                if (StringUtils.isEmpty(correctMobiles)) {
                    correctMobiles += ctMobileList;
                } else {
                    correctMobiles += "," + ctMobileList;
                }
            }
        }

        return correctMobiles;
    }

    /**
     * 获取不合法手机号
     * 如果上传的手机号不在产品所属运营商之列，则认为该手机号不合法
     */
    private String getInvalidMobiles(Long cmProductId, Long cuProductId, Long ctProductId,
                                     String cmMobileList, String cuMobileList, String ctMobileList,
                                     String invalidMobiles) {
        String newInvalidMobiles = invalidMobiles;
        //移动
        if (cmProductId == null && (!StringUtils.isEmpty(cmMobileList))) {
            if (StringUtils.isEmpty(newInvalidMobiles)) {
                newInvalidMobiles += cmMobileList;
            } else {
                newInvalidMobiles += "," + cmMobileList;
            }
        }
        //联通
        if (cuProductId == null && (!StringUtils.isEmpty(cuMobileList))) {
            if (StringUtils.isEmpty(newInvalidMobiles)) {
                newInvalidMobiles += cuMobileList;
            } else {
                newInvalidMobiles += "," + cuMobileList;
            }
        }
        //电信
        if (ctProductId == null && (!StringUtils.isEmpty(ctMobileList))) {
            if (StringUtils.isEmpty(newInvalidMobiles)) {
                newInvalidMobiles += ctMobileList;
            } else {
                newInvalidMobiles += "," + ctMobileList;
            }
        }
        return newInvalidMobiles;
    }

    /**
     * 实时获取活动规模信息
     *
     * @author qinqinyan
     */
    @RequestMapping("getActivityInfoAjax")
    public void getActivityInfoAjax(HttpServletRequest request, HttpServletResponse response,
                                    Long cmProdId, Long cuProdId, Long ctProdId,
                                    String correctMobiles,
                                    String activityId, Long entId
    ) throws IOException {

        Map<String, Object> map = new HashMap<String, Object>();
        Product cmProduct = productService.selectProductById(cmProdId);
        Product cuProduct = productService.selectProductById(cuProdId);
        Product ctProduct = productService.selectProductById(ctProdId);

        List<String> cmMobileList = new ArrayList<String>();
        List<String> cuMobileList = new ArrayList<String>();
        List<String> ctMobileList = new ArrayList<String>();

        Set<String> cmMobileSet = new HashSet<String>();
        Set<String> cuMobileSet = new HashSet<String>();
        Set<String> ctMobileSet = new HashSet<String>();

        String[] cmMobile = getCMCCMobileFlag().split(",");
        String[] cuMobile = getUNICOMMobileFlag().split(",");
        String[] ctMobile = getTELECOMMobileFlag().split(",");

        if (!StringUtils.isEmpty(correctMobiles)) {
            //新建，编辑重新上传手机号
            String[] mobileArray = correctMobiles.split(",");
            for (int i = 0; i < mobileArray.length; i++) {
                String isp = judgeIspService.judgeIsp(mobileArray[i], cmMobile, cuMobile, ctMobile);
                if (isp.equals(IspType.CMCC.getValue())) {
                    cmMobileList.add(mobileArray[i]);
                    cmMobileSet.add(mobileArray[i]);
                }
                if (isp.equals(IspType.UNICOM.getValue())) {
                    cuMobileList.add(mobileArray[i]);
                    cuMobileSet.add(mobileArray[i]);
                }
                if (isp.equals(IspType.TELECOM.getValue())) {
                    ctMobileList.add(mobileArray[i]);
                    ctMobileSet.add(mobileArray[i]);
                }
            }
        } else {
            List<ActivityWinRecord> activityWinRecords = activityWinRecordService.selectByActivityId(activityId);
            if (activityWinRecords != null) {
                for (ActivityWinRecord item : activityWinRecords) {
                    String isp = judgeIspService.judgeIsp(item.getOwnMobile(), cmMobile, cuMobile, ctMobile);
                    if (isp.equals(IspType.CMCC.getValue())) {
                        cmMobileList.add(item.getOwnMobile());
                        cmMobileSet.add(item.getOwnMobile());
                    }

                    if (isp.equals(IspType.UNICOM.getValue())) {
                        cuMobileList.add(item.getOwnMobile());
                        cuMobileSet.add(item.getOwnMobile());
                    }

                    if (isp.equals(IspType.TELECOM.getValue())) {
                        cuMobileList.add(item.getOwnMobile());
                        cuMobileSet.add(item.getOwnMobile());
                    }
                }
            }

        }

        //奖品个数
        int prizeCount = getTotalCount(cmMobileList, cuMobileList, ctMobileList);
        map.put("prizeCount", prizeCount);
        //用户个数
        int userCount = getTotalUser(cmMobileSet, cuMobileSet, ctMobileSet);
        map.put("userCount", userCount);
        //总价格
        String totalPrice = getTotalPrice(cmMobileList, cuMobileList, ctMobileList,
                cmProduct, cuProduct, ctProduct, entId);
        //总流量
        String totalSize = getTotalProductSize(cmMobileList, cuMobileList, ctMobileList,
                cmProduct, cuProduct, ctProduct);

        map.put("totalSize", totalSize);
        map.put("totalPrice", totalPrice);
        map.put("success", "success");
        response.getWriter().write(JSON.toJSONString(map));
        return;
    }

    private int getTotalCount(List<String> cmMobileList, List<String> cuMobileList, List<String> ctMobileList) {
        int count = 0;
        if (cmMobileList != null) {
            count += cmMobileList.size();
        }
        if (cuMobileList != null) {
            count += cuMobileList.size();
        }
        if (ctMobileList != null) {
            count += ctMobileList.size();
        }
        return count;
    }

    private int getTotalUser(Set<String> cmMobileSet, Set<String> cuMobileSet, Set<String> ctMobileSet) {
        int count = 0;
        if (cmMobileSet != null) {
            count += cmMobileSet.size();
        }
        if (cuMobileSet != null) {
            count += cuMobileSet.size();
        }
        if (ctMobileSet != null) {
            count += ctMobileSet.size();
        }
        return count;
    }

    private String getTotalPrice(List<String> cmMobileList, List<String> cuMobileList, List<String> ctMobileList,
                                 Product cmProduct, Product cuProduct, Product ctProduct,
                                 Long entId) {
        Integer price = 0;
        if (entId != null) {
            if (cmMobileList != null && cmProduct != null && cmProduct.getId() != null) {
                EntProduct cmEntProduct = entProductService.selectByProductIDAndEnterprizeID(cmProduct.getId(), entId);
                //流量池产品，cmEntProduct替换为流量池的对应关系
                if (cmProduct.getFlowAccountFlag().intValue() ==FLOW_ACCOUNT_FLAG.VIRTUAL_PRODUCT.getCode().intValue()) {
                    Product flowAccountProduct = productService.get(cmProduct.getId());
                    if(flowAccountProduct != null){
                        cmEntProduct = entProductService.selectByProductIDAndEnterprizeID(flowAccountProduct.getFlowAccountProductId(), entId);
                    }else{
                        cmEntProduct = null;
                    }
                }
                if (cmEntProduct != null && cmEntProduct.getDiscount() != null) {
                    price += cmMobileList.size() * cmProduct.getPrice() * cmEntProduct.getDiscount() / 100;
                }
            }

            if (cuMobileList != null && cuProduct != null) {
                EntProduct cuEntProduct = entProductService.selectByProductIDAndEnterprizeID(cuProduct.getId(), entId);
                if (cuEntProduct != null && cuEntProduct.getDiscount() != null) {
                    price += cuMobileList.size() * cuProduct.getPrice() * cuEntProduct.getDiscount() / 100;
                }
            }

            if (ctMobileList != null && ctProduct != null) {
                EntProduct ctEntProduct = entProductService.selectByProductIDAndEnterprizeID(ctProduct.getId(), entId);
                if (ctEntProduct != null && ctEntProduct.getDiscount() != null) {
                    price += ctMobileList.size() * ctProduct.getPrice() * ctEntProduct.getDiscount() / 100;
                }
            }
        }
        return priceFun(price);
    }

    private String getTotalProductSize(List<String> cmMobileList, List<String> cuMobileList, List<String> ctMobileList,
                                       Product cmProduct, Product cuProduct, Product ctProduct) {
        Long size = 0L;
        if (cmMobileList != null && cmProduct != null && cmProduct.getProductSize() != null) {
            size += cmProduct.getProductSize() * cmMobileList.size();
        }
        if (cuMobileList != null && cuProduct != null && cuProduct.getProductSize() != null) {
            size += cuProduct.getProductSize() * cuMobileList.size();
        }
        if (ctMobileList != null && ctProduct != null && ctProduct.getProductSize() != null) {
            size += ctProduct.getProductSize() * ctMobileList.size();
        }
        return sizeFun(size);
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
            return "0.00";
        }
        Double p = price / 100.00;
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(p);
    }

    /**
     * 上传手机号ajax
     *
     * @author qinqinyan
     */
    @RequestMapping(method = RequestMethod.POST, value = "/uploadPhones")
    public void uploadFileAjax(HttpServletRequest request, @RequestParam("file") MultipartFile file, HttpServletResponse response)
            throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();

        //设置响应给前台内容的数据格式       
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain; charset=UTF-8");

        List<String> list = new ArrayList<String>();  //正确的手机号
        List<String> invalidList = new ArrayList<String>(); //错误的手机号

        List<String> cmMobileList = new ArrayList<String>(); //移动手机号
        List<String> cuMobileList = new ArrayList<String>(); //联通手机号
        List<String> ctMobileList = new ArrayList<String>(); //电信手机号

        Set<String> cmUserSet = new HashSet<String>(); //赠送移动用户，cmMobileList去重
        Set<String> cuUserSet = new HashSet<String>(); //赠送联通用户，cuMobileList去重
        Set<String> ctUserSet = new HashSet<String>(); //赠送电信用户，ctMobileList去重

//        String[] fileType = file.getOriginalFilename().split("\\.");
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
                //PrintWriter out = response.getWriter();
                String fileSuffix = file.getOriginalFilename();
                if (fileSuffix != null) {
                    fileSuffix = fileSuffix.split("\\.")[fileSuffix.split("\\.").length - 1];
                }
                File tempFile = File.createTempFile(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), fileSuffix);
                file.transferTo(tempFile);
                is = new FileInputStream(tempFile);

                int updatePhonesNum = 0;//上传的电话数

                if (fileSuffix != null && "txt".equals(fileSuffix)) {
                    updatePhonesNum = readText(list, invalidList,
                            cmMobileList, cuMobileList, ctMobileList,
                            cmUserSet, cuUserSet, ctUserSet,
                            is);
                    System.out.println(updatePhonesNum);
                } else {
                    //只支持文件
                    map.put("errorMsg", "文件格式限TXT，最大不超过" + MAX_FILE_SIZE + "MB");
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }

                FileUtils.deleteQuietly(tempFile);
//                validPhonesNum = list.size();

                int cmPhoneCount = 0;
                int cuPhoneCount = 0;
                int ctPhoneCount = 0;
                if (cmMobileList != null && cmMobileList.size() > 0) {
                    cmPhoneCount = cmMobileList.size();
                }
                if (cuMobileList != null && cuMobileList.size() > 0) {
                    cuPhoneCount = cuMobileList.size();
                }
                if (ctMobileList != null && ctMobileList.size() > 0) {
                    ctPhoneCount = ctMobileList.size();
                }

                String correctMobiles = toPhones(list);
                String invalidMobiles = toPhones(invalidList);
                if ((cmPhoneCount + cuPhoneCount + ctPhoneCount) > 0) {
                    map.put("successMsg", "success");
                    map.put("correctMobiles", correctMobiles);
                    map.put("invalidMobiles", invalidMobiles);

                    map.put("cmMobileList", toPhones(cmMobileList));
                    map.put("cuMobileList", toPhones(cuMobileList));
                    map.put("ctMobileList", toPhones(ctMobileList));

                    map.put("cmPhoneCount", cmPhoneCount);
                    map.put("cuPhoneCount", cuPhoneCount);
                    map.put("ctPhoneCount", ctPhoneCount);

                    map.put("cmUserSet", cmUserSet);
                    map.put("cuUserSet", cuUserSet);
                    map.put("ctUserSet", ctUserSet);
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

    private String toPhones(List<String> list) throws UnsupportedEncodingException {
        //return org.apache.commons.lang.StringUtils.join(list.toArray(),",");
        StringBuffer buffer = new StringBuffer();
        if (list == null || list.size() == 0) {
            return "";
        } else {
            for (String phone : list) {
                buffer.append(phone + ",");
            }
        }
        //str.equals(new String(str.getBytes(charEncode),charEncode)
        String phones = new String(buffer.toString());
        phones = phones.substring(0, phones.length() - 1);

        return phones;
    }

    /**
     * 解析Txt文件,返回成功总行数
     *
     * @param list        正确的手机号
     * @param invalidList 错误的手机号
     */
    public int readText(List<String> list, List<String> invalidList,
                        List<String> cmMobileList, List<String> cuMobileList, List<String> ctMobileList,
                        Set<String> cmUserSet, Set<String> cuUserSet, Set<String> ctUserSet,
                        InputStream inputStream) throws IOException {
        String[] cmMobile = getCMCCMobileFlag().split(",");
        String[] cuMobile = getUNICOMMobileFlag().split(",");
        String[] ctMobile = getTELECOMMobileFlag().split(",");

        BufferedReader buReader = new BufferedReader(new InputStreamReader(inputStream, "GB2312"));
        String in = null;
        //int rowNums = 0;
        while ((in = buReader.readLine()) != null) {
            in = in.trim();
            //rowNums++;          
            if (judge(in)) {
                //判断运营商
                String isp = judgeIspService.judgeIsp(in, cmMobile, cuMobile, ctMobile);

                if (StringUtils.isEmpty(isp)) {
                    invalidList.add(in);
                } else {
                    if (isp.equals(IspType.CMCC.getValue())) {
                        cmMobileList.add(in);
                        cmUserSet.add(in);
                    } else if (isp.equals(IspType.UNICOM.getValue())) {
                        cuMobileList.add(in);
                        cuUserSet.add(in);
                    } else {
                        ctMobileList.add(in);
                        ctUserSet.add(in);
                    }
                    list.add(in);
                }
            } else {
                invalidList.add(in);
            }
        }
        return list != null && list.size() > 0 ? list.size() : 0;
    }

    private boolean judge(String str) {
        return com.cmcc.vrp.util.StringUtils.isValidMobile(str);
    }

    /**
     * @param modelMap
     * @param request
     * @param response
     * @param phones
     * @Title: downloadPhones
     * @Description: 手机号下载
     * @return: void
     */
    @RequestMapping("downloadPhones")
    @ResponseBody
    public void downloadPhones(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response,
                               String phones, String activitiesId, String activityType) throws IOException {
        Long creatorId = getCurrentUser().getId();

        String filename = "手机号_";
        List<String> phoneList = new ArrayList<String>();
        if (StringUtils.isEmpty(phones)) {

            Activities activities = activitiesService.selectByActivityId(activitiesId);
            filename = activities.getName() + "_";

            if (StringUtils.isEmpty(activityType)) {
                //流量券，下载的是赠送手机号
                //从db查找出来手机号
                List<ActivityWinRecord> activityWinRecords = activityWinRecordService.selectByActivityId(activitiesId);
                for (ActivityWinRecord item : activityWinRecords) {
                    phoneList.add(item.getOwnMobile());
                }
            } else {
                //二维码，下载的是黑白名单
                //从db查找出来
                ActivityInfo activityInfo = activityInfoService.selectByActivityId(activitiesId);
                if (activityInfo.getHasWhiteOrBlack().intValue() == BlackAndWhiteListType.WHITELIST.getCode().intValue()) {
                    filename += "白名单_";
                } else if (activityInfo.getHasWhiteOrBlack().intValue() == BlackAndWhiteListType.BLACKLIST.getCode().intValue()) {
                    filename += "黑名单_";
                }
                Map map = new HashedMap();
                map.put("activityId", activitiesId);
                List<String> blackOrWhitePhones = activityBlackAndWhiteService.selectPhonesByMap(map);
                phoneList.addAll(blackOrWhitePhones);
            }

        } else {
            phoneList.addAll(Arrays.asList(phones.split(",")));
        }
        filename += creatorId.toString() + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".txt";

        URLDecoder.decode(filename, "UTF-8");
        logger.info("decode,fileName:" + filename);

        if (activityWinRecordService.downLoadPhones(request, response, creatorId, phoneList, filename, activityType)) {
            if (StringUtils.isEmpty(activityType)) {
                FileUtils.deleteQuietly(new File(getFlowcardFilePath() + File.separator + creatorId + File.separator + filename));
            } else {
                FileUtils.deleteQuietly(new File(getQRcodeFilePath() + File.separator + creatorId + File.separator + filename));
            }
        } else {
            response.sendError(403);
        }
    }


    /**
     * 错误手机号码下载
     *
     * @param modelMap
     * @param request
     * @param response
     * @throws IOException
     * @Title: downloadPhones
     * @Author: wujiamin
     * @date 2016年9月14日下午4:41:49
     */
    @RequestMapping("downloadErrorPhones")
    @ResponseBody
    public void downloadErrorPhones(ModelMap modelMap, HttpServletRequest request, String errorPhones, HttpServletResponse response) throws IOException {
        Long creatorId = getCurrentUser().getId();

        String filename = "手机号_";
        List<String> phoneList = new ArrayList<String>();

        phoneList.addAll(Arrays.asList(errorPhones.split(",")));

        filename += creatorId.toString() + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".txt";

        URLDecoder.decode(filename, "UTF-8");
        logger.info("decode,fileName:" + filename);

        if (activityWinRecordService.downLoadPhones(request, response, creatorId, phoneList, filename, null)) {
            FileUtils.deleteQuietly(new File(getFlowcardFilePath() + File.separator + creatorId + File.separator + filename));
        } else {
            response.sendError(403);
        }
    }

    /**
     * 流量券下载临时目录
     */
    public String getFlowcardFilePath() {
        return globalConfigService
                .get(GlobalConfigKeyEnum.LOTTERY_FLOWCARD_PATH.getKey());
    }

    /**
     * 二维码下载临时目录
     */
    public String getQRcodeFilePath() {
        return globalConfigService
                .get(GlobalConfigKeyEnum.LOTTERY_FLOWCARD_PATH.getKey());
    }
    
    /**
     * 保存流量券活动记录
     * @author qinqinyan 
     * @throws IOException 
     * @date 2017-06-27
     * */
    @RequestMapping("saveAjax")
    public void saveAjax(HttpServletRequest request, HttpServletResponse response,
            Long cmProductId, Long cuProductId, Long ctProductId,
            String cmMobileList, String cuMobileList, String ctMobileList,
            String cmUserSet, String cuUserSet, String ctUserSet, String prdSize) throws IOException{
        
        Map map = new HashedMap();
        String msg = "";
        Boolean result = false;
        Administer administer = getCurrentUser();
        Manager manager = getCurrentUserManager();
        if (administer == null || administer.getId() == null || manager == null) {
            logger.info("用户异常,保存失败!");
            msg = "用户异常,保存失败!";
        }
        String activityParams = request.getParameter("activity");
        logger.info("activityParams:"+activityParams);
        if(StringUtils.isEmpty(activityParams)){//
            msg = "用户异常,保存失败!";
        }else{
            Activities activities = null;
            try{
                activities = JSON.parseObject(activityParams, Activities.class);
            }catch(Exception e){
                logger.info(e.getMessage());
            }
            
            if(activities!=null){
                
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
                activities.setName(xssService.stripQuot(activities.getName()));
                if(!StringUtils.isEmpty(prdSize)){
                    //移动的产品转化,针对流量池、话费
                    Product cmProduct;
                    try {
                        cmProduct = virtualProductService.initProcess(cmProductId, prdSize);
                        if (cmProduct == null) {
                            logger.info("产品转化失败");
                        }else{
                            cmProductId = cmProduct.getId();
                        }
                    } catch (ProductInitException e) {
                        logger.info("产品转化失败,失败原因："+e.getMessage());
                    }
                }
                
                try{
                    if(StringUtils.isEmpty(activities.getActivityId())){
                        //新建
                        activities.setCreatorId(administer.getId());
                        if (activitiesService.insertFlowcardActivity(activities,
                                cmProductId, cuProductId, ctProductId,
                                cmMobileList, cuMobileList, ctMobileList,
                                cmUserSet, cuUserSet, ctUserSet)) {
                            logger.info("插入活动记录成功：" + JSONArray.toJSONString(activities));
                            msg = "保存成功";
                            result = true;
                        }else{
                            logger.info("插入活动记录失败：" + JSONArray.toJSONString(activities));
                            msg = "保存失败";
                        }
                    }else{
                        //校验前端传过来的活动id是否有效
                        Activities orgActivities = activitiesService.selectByActivityId(activities.getActivityId());
                        if(orgActivities==null){
                            msg = "对不起，您无权限进行该操作";
                            map.put("msg", msg);
                            map.put("result", result.toString());
                            response.getWriter().write(JSON.toJSONString(map));
                            return;
                        }
                        
                        //对根据前端传过来的活动id从数据库查找出来的活动的状态进行判断，只要以保存和已驳回的可进行编辑保存
                        if(!(orgActivities.getStatus().toString().equals(ActivityStatus.SAVED.getCode().toString())
                                || orgActivities.getStatus().toString().equals(ActivityStatus.REJECT.getCode().toString()))){
                            msg = "该活动状态下不可进行编辑操作";
                            map.put("msg", msg);
                            map.put("result", result.toString());
                            response.getWriter().write(JSON.toJSONString(map));
                            return;
                        }
                        
                        //检查数据库查出来  -- 检查activity的enterId是否是该账号有权操作的企业
                        if(!enterprisesService.isParentManage(orgActivities.getEntId(), manager.getId())){
                            msg = "对不起，您无权限进行该操作";
                            map.put("msg", msg);
                            map.put("result", result.toString());
                            response.getWriter().write(JSON.toJSONString(map));
                            return;
                        }
                        
                        //编辑
                        if (activitiesService.editFlowcardActivity(activities,
                                cmProductId, cuProductId, ctProductId,
                                cmMobileList, cuMobileList, ctMobileList,
                                cmUserSet, cuUserSet, ctUserSet)) {
                            logger.info("编辑活动记录成功：" + JSONArray.toJSONString(activities));
                            msg = "保存成功";
                            result = true;
                        }else{
                            logger.info("更新活动记录失败：" + JSONArray.toJSONString(activities));
                            msg = "保存失败";
                        }
                    }
                }catch(RuntimeException e){
                    logger.info("插入或者更新活动记录失败,失败原因：" + e.getMessage()+", 如果是编辑，则活动id = "+activities.getId());
                    msg = "保存失败";
                }
            }
        }
        
        map.put("msg", msg);
        map.put("result", result.toString());
        response.getWriter().write(JSON.toJSONString(map));
        return;
    }
    
    

    /**
     * 保存流量券活动记录
     */
    //@RequestMapping("save")
    public String save(ModelMap map, Activities activities,
                       Long cmProductId, Long cuProductId, Long ctProductId,
                       String cmMobileList, String cuMobileList, String ctMobileList,
                       String cmUserSet, String cuUserSet, String ctUserSet) {
        Administer admin = getCurrentUser();
//         Manager manager = getCurrentUserManager();
        activities.setEndTime(DateUtil.getEndTimeOfDate(activities.getEndTime()));
        if (org.apache.commons.lang.StringUtils.isBlank(activities.getActivityId())) {
            //新建
            activities.setCreatorId(admin.getId());
            if (activitiesService.insertFlowcardActivity(activities,
                    cmProductId, cuProductId, ctProductId,
                    cmMobileList, cuMobileList, ctMobileList,
                    cmUserSet, cuUserSet, ctUserSet)) {
                logger.info("插入活动记录成功：" + JSONArray.toJSONString(activities));
                return "redirect:index.html";
            }
        } else {
            //编辑
            activities.setEndTime(DateUtil.getEndTimeOfDate(activities.getEndTime()));
            if (activitiesService.editFlowcardActivity(activities,
                    cmProductId, cuProductId, ctProductId,
                    cmMobileList, cuMobileList, ctMobileList,
                    cmUserSet, cuUserSet, ctUserSet)) {
                logger.info("编辑活动记录成功：" + JSONArray.toJSONString(activities));
                return "redirect:index.html";
            }
        }
        return "redirect:index.html";
    }

    /**
     * @Title: edit
     */
    @RequestMapping("edit")
    public String edit(HttpServletResponse response, HttpServletRequest request, ModelMap map, Long id) {
        Administer administer = getCurrentUser();
        Manager manager = getCurrentUserManager();
        if (manager == null || administer == null) {
            return getLoginAddress();
        }
        if (id == null) {
            map.addAttribute("errorMsg", "该条记录不存在!");
            return "error.ftl";
        }

        Activities activities = activitiesService.selectByPrimaryKey(id);

        if (activities == null || StringUtils.isEmpty(activities.getActivityId())) {
            map.addAttribute("errorMsg", "该条记录不存在!");
            return "error.ftl";
        }
        if (!administer.getId().toString().equals(activities.getCreatorId().toString())) {
            map.addAttribute("errorMsg", "对不起，您无权限修改该条记录!");
            return "error.ftl";
        }

        Enterprise enterprise = enterprisesService.selectByPrimaryKey(activities.getEntId());
        ActivityInfo activityInfo = activityInfoService.selectByActivityId(activities.getActivityId());

        //产品信息
        Long cmProductId = null;
        Long cuProductId = null;
        Long ctProductId = null;

        List<ActivityPrize> activityPrizes = activityPrizeService.selectByActivityId(activities.getActivityId());
        for (ActivityPrize item : activityPrizes) {
            Product product = productService.selectProductById(item.getProductId());

            if (product.getIsp().equals(IspType.CMCC.getValue())) {
                cmProductId = item.getProductId();
            }
            if (product.getIsp().equals(IspType.UNICOM.getValue())) {
                cuProductId = item.getProductId();
            }
            if (product.getIsp().equals(IspType.TELECOM.getValue())) {
                ctProductId = item.getProductId();
            }
        }
        map.addAttribute("cmProductId", cmProductId);
        map.addAttribute("cuProductId", cuProductId);
        map.addAttribute("ctProductId", ctProductId);

        List<Enterprise> enterpriseList = enterprisesService.getEnterByManagerId(manager.getId());

        map.addAttribute("activityInfo", activityInfo);
        map.addAttribute("activityPrizes", activityPrizes);

        map.addAttribute("enterprise", enterprise);
        map.addAttribute("enterpriseList", enterpriseList);
        map.addAttribute("activities", activities);
        if ("shanghai".equals(getProvinceFlag())) {
            map.addAttribute("showOrderList", 1);
            List<ShOrderList> shOrderLists = shOrderListService.getByEnterId(enterprise.getId());
            map.addAttribute("orderList", shOrderLists);
        } else {
            map.addAttribute("showOrderList", 0);
        }

        //是否允许使用三网产品
        if (allowAllPlatformProduct()) {
            map.addAttribute("allowAllPlatformProduct", true);
        }

        return "flowcard/edit.ftl";
    }

    /**
     * @title:getProductAjax
     */
    @RequestMapping("getProductAjax")
    public void getProductAjax(HttpServletRequest request, HttpServletResponse response, Long productId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        if (productId != null) {
            Product product = productService.selectProductById(productId);
            if (product != null) {
                map.put("success", "success");
                map.put("productId", product.getId());
                map.put("productName", product.getName());
                map.put("productCode", product.getProductCode());
                if(product.getType() == 3) {
                    map.put("productSize", (product.getProductSize()));
                } else {
                    map.put("productSize", sizeFun(product.getProductSize()));
                }
                map.put("productPrice", priceFun(product.getPrice()));
                map.put("ownership", product.getOwnershipRegion());
                map.put("roaming", product.getRoamingRegion());
                map.put("type", product.getType());
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
     * @param activityId
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

        //活动上架
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
     * @param modelMap
     * @return
     * @Title: showDetail
     * @Description: TODO
     * @Author: wujiamin
     * @date 2016年8月18日上午11:40:01
     */
    @RequestMapping("detail")
    public String showDetail(ModelMap modelMap, String activityId) {
        Manager manager = getCurrentUserManager();
        if(manager == null){
            return getLoginAddress();
        }
        
        Activities activity = null;
        ActivityInfo activityInfo = null;
        if (StringUtils.isEmpty(activityId) || (activity = activitiesService.selectByActivityId(activityId)) == null
                || (activityInfo = activityInfoService.selectByActivityId(activity.getActivityId())) == null) {
            logger.error("流量券记录不存在，activityId={}", activityId);
            modelMap.put("errorMsg", "流量券记录不存在");
            return "error.ftl";
        }
        
        if(!managerService.managedByManageId(activity.getEntId(), manager.getId())){
            modelMap.put("errorMsg", "对不起，您无权限查看活动详情");
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
        //活动规模（估计）
        modelMap.put("activityInfo", activityInfo);

        //活动规模（当前）
        CurrentActivityInfo currentActivityInfo = activityWinRecordService.getCurrentActivityInfo(activity.getActivityId());
        modelMap.put("currentActivityInfo", currentActivityInfo);

        //奖品
        List<ActivityPrize> prizes = activityPrizeService.getDetailByActivityId(activity.getActivityId());
        modelMap.put("prizes", prizes);

        return "flowcard/detail.ftl";
    }


    /**
     * title:check
     */
    @RequestMapping(value = "/check")
    public void checkCQMobile(String mobile, HttpServletResponse resp)
            throws IOException {
        if (StringUtils.isValidMobile(mobile)) {
            resp.getWriter().write("true");
        } else {
            resp.getWriter().write("false");
        }
    }

    /**
     * 赠送用户详情
     *
     * @param map
     * @param activityId
     * @return
     * @Title: recordsIndex
     * @Description: TODO
     * @Author: wujiamin
     * @date 2016年8月19日下午3:49:07
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
                map.put("errorMsg", "对不起，您无权限查看赠送用户详情");
                return "error.ftl";
            }else{
                map.put("activityId", activityId);
                return "flowcard/records.ftl";
            }
        }else{
            map.put("errorMsg", "活动参数异常");
            return "error.ftl";
        }
    }

    /**
     * 赠送用户的筛选
     *
     * @param queryObject
     * @param res
     * @Title: searchRecords
     * @Description: TODO
     * @Author: wujiamin
     * @date 2016年8月19日下午3:49:24
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
        int enterpriseCount = activityWinRecordService
                .showForPageResultCount(queryObject);
        List<ActivityWinRecord> enterpriseList = activityWinRecordService
                .showForPageResult(queryObject);

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
     * 营销活动提交审批申请
     *
     * @author qinqinyan
     */
    @RequestMapping("submitActivityApprovalAjax")
    public void submitActivityApprovalAjax(HttpServletResponse resp,
                                           String activityId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        Manager manager = getCurrentUserManager();
        Administer administer = getCurrentUser();
        if(manager==null || administer==null){
            map.put("submitRes", "fail");
            map.put("errorMsg", "用户信息异常");
        }else{
            Long currentId = administer.getId();
            Long roleId = manager.getRoleId();
            Activities activities = activitiesService.selectByActivityId(activityId);
            if(activities!=null){
                if(!managerService.managedByManageId(activities.getEntId(), manager.getId())){
                    map.put("submitRes", "fail");
                    map.put("errorMsg", "对不起，您无权限提交审核记录");
                }else{
                    map = activitiesService.submitApproval(activityId,
                            currentId, roleId);
                }
            }else{
                map.put("submitRes", "fail");
                map.put("errorMsg", "活动信息异常");
            }
        }
        resp.getWriter().write(JSON.toJSONString(map));
        return;
    }

    /**
     * 获取移动手机号段（前三位）
     */
    private String getCMCCMobileFlag() {
        return globalConfigService.get(GlobalConfigKeyEnum.CMCC_MOBILE_FLAG.getKey());
    }

    /**
     * 获取联通手机号段（前三位）
     */
    private String getUNICOMMobileFlag() {
        return globalConfigService.get(GlobalConfigKeyEnum.UNICOM_MOBILE_FLAG.getKey());
    }

    /**
     * 获取电信手机号段（前三位）
     */
    private String getTELECOMMobileFlag() {
        return globalConfigService.get(GlobalConfigKeyEnum.TELECOM_MOBILE_FLAG.getKey());
    }


}
