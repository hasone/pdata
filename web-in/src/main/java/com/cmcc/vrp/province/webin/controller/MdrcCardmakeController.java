package com.cmcc.vrp.province.webin.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.ApprovalRequestStatus;
import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.mdrc.service.MdrcCardmakerService;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.MdrcBatchConfigInfo;
import com.cmcc.vrp.province.model.MdrcCardmakeDetail;
import com.cmcc.vrp.province.model.MdrcCardmaker;
import com.cmcc.vrp.province.model.MdrcTemplate;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.ApprovalRequestSmsService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.FileStoreService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.MdrcActiveDetailService;
import com.cmcc.vrp.province.service.MdrcBatchConfigInfoService;
import com.cmcc.vrp.province.service.MdrcCardmakeDetailService;
import com.cmcc.vrp.province.service.MdrcMakecardRequestConfigService;
import com.cmcc.vrp.province.service.MdrcTemplateService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.S3Service;
import com.cmcc.vrp.province.service.XssService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.MD5;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;

/**
 * Created by qinqinyan on 2016/12/1. 营销卡制卡控制器
 */
@Controller
@RequestMapping("manage/mdrc/cardmake")
public class MdrcCardmakeController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MdrcCardmakeController.class);

    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Autowired
    ApprovalRequestService approvalRequestService;
    @Autowired
    MdrcBatchConfigService mdrcBatchConfigService;
    @Autowired
    MdrcCardInfoService mdrcCardInfoService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    S3Service s3Service;
    @Autowired
    MdrcActiveDetailService mdrcActiveDetailService;
    @Autowired
    FileStoreService fileStoreService;
    @Autowired
    MdrcTemplateService mdrcTemplateService;
    @Autowired
    ProductService productService;
    @Autowired
    ApprovalRecordService approvalRecordService;
    @Autowired
    MdrcCardmakerService mdrcCardmakerService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    MdrcCardmakeDetailService mdrcCardmakeDetailService;
    @Autowired
    MdrcBatchConfigInfoService mdrcBatchConfigInfoService;
    @Autowired
    MdrcMakecardRequestConfigService mdrcMakecardRequestConfigService;
    @Autowired
    XssService xssService;
    @Autowired
    ManagerService managerService;
    @Autowired
    ApprovalRequestSmsService approvalRequestSmsService;

    /**
     * 制卡申请列表页
     * 
     * @param map
     * @author qinqinyan
     */
    @RequestMapping("index")
    public String index(HttpServletRequest request, ModelMap map, QueryObject queryObject) {
        
        if(queryObject != null){
            map.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        
        if (getCurrentUser() != null && getCurrentUserManager() != null) {
            return "mdrcMakeCardApproval/approvalRequestIndex.ftl";
        }
        return getLoginAddress();
    }

    /**
     * @param queryObject
     * @param res
     * @author qinqinyan
     * 
     *         edit by qinqinyan on 2017/07/28
     */
    @RequestMapping("search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        /**
         * 查询参数
         */
        setQueryParameter("result", queryObject); // 审批状态
        setQueryParameter("enterpriseName", queryObject); // 企业名称
        setQueryParameter("enterpriseCode", queryObject); // 企业集团编码

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
                queryObject.getQueryCriterias().put("creatorId", administer.getId());

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
     * @param modelMap
     * @return
     * @Title: addOrUpdate
     * @Description: 新建规则
     * @return: String
     * 
     *          edit by qinqinyan on 2017/07/28
     */
    @RequestMapping(value = "/create")
    public String create(ModelMap modelMap) {
        // 查询所有制卡商
        /*
         * List<MdrcCardmaker> cardmakerList = mdrcCardmakerService
         * .selectAllCardmaker();
         * 
         * modelMap.addAttribute("cardmakerList", cardmakerList);
         * 
         * Map<String, List<MdrcTemplate>> templateMap = mdrcTemplateService
         * .themeTemplates(); modelMap.addAttribute("templateMap", templateMap);
         */

        Manager manager = getCurrentUserManager();
        if (manager != null) {
            List<MdrcCardmaker> cardmakerList = mdrcCardmakerService.selectAllCardmaker();
            if (cardmakerList != null && cardmakerList.size() > 0) {
                // 获取企业
                List<Enterprise> enterprises = enterprisesService.getNomalEnterByManagerId(manager.getId());

                modelMap.addAttribute("enterprises", enterprises);

                modelMap.addAttribute("minCardNum", getMinCardNum());
                modelMap.addAttribute("maxCardNum", getMaxCardNum());
                modelMap.addAttribute("maxDays", Integer.parseInt(getMaxDay()));

                // v1.12.1的需求提出默认取第一个制卡商，因为说总共只有一个制卡商
                modelMap.addAttribute("cardmakerId", cardmakerList.get(0).getId());

                // 是否需要审核
                ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                        .selectByType(ApprovalType.MDRC_MakeCard_Approval.getCode());
                Boolean wheatherNeedApproval = true;
                if (approvalProcessDefinition.getStage().toString().equals("0")) {
                    wheatherNeedApproval = false;
                }
                modelMap.addAttribute("wheatherNeedApproval", wheatherNeedApproval.toString());
                return "mdrcMakeCardApproval/create.ftl";
            } else {
                modelMap.addAttribute("errorMsg", "未查找到制卡商，无法提交制卡申请。请联系制卡专员！");
                return "error.ftl";
            }
        } else {
            return getLoginAddress();
        }
    }

    /**
     * 编辑
     * 
     * @param modelMap
     * @param requestId
     * @author qinqinyan
     * @date 2017/08/04
     */
    @RequestMapping("edit")
    public String edit(ModelMap modelMap, Long requestId) {
        logger.info("requestId = {}", requestId);
        Manager manager = getCurrentUserManager();
        MdrcCardmakeDetail mdrcCardmakeDetail = mdrcCardmakeDetailService.selectByRequestId(requestId);
        if (mdrcCardmakeDetail != null && manager!=null) {
            //判断是否有权限编辑这个企业的制卡申请
            if(!managerService.managedByManageId(mdrcCardmakeDetail.getEnterpriseId(), manager.getId())){
                logger.info("manageId = {} 没有权限编辑企业 = {} 的制卡申请", manager.getId(), mdrcCardmakeDetail.getEnterpriseId());
                modelMap.addAttribute("errorMsg", "对不起，您无权限修改该企业的制卡申请记录!");
                return "error.ftl";
            }
            
            MdrcBatchConfigInfo mdrcBatchConfigInfo = mdrcBatchConfigInfoService
                    .selectByPrimaryKey(mdrcCardmakeDetail.getConfigInfoId());
            Product product = productService.selectProductById(mdrcCardmakeDetail.getProductId());
            MdrcTemplate mdrcTemplate = mdrcTemplateService.selectByPrimaryKey(mdrcCardmakeDetail.getTemplateId());
            if (mdrcBatchConfigInfo != null && product != null && mdrcTemplate != null) {
                
                // modelMap.addAttribute("enterprises", enterprises);

                Enterprise enterprise = enterprisesService.selectById(mdrcCardmakeDetail.getEnterpriseId());
                modelMap.addAttribute("enterprise", enterprise);
                // 根据企业id获取产品列表
                List<Product> products = productService.selectAllProductsByEnterId(mdrcCardmakeDetail
                        .getEnterpriseId());
                modelMap.addAttribute("products", products);

                // 获取营销卡短信模板列表
                Map<String, List<MdrcTemplate>> templateMap = mdrcTemplateService
                        .getThemeTemplatesByProdSize(product.getProductSize());
                modelMap.addAttribute("templateMap", templateMap);

                modelMap.addAttribute("mdrcCardmakeDetail", mdrcCardmakeDetail);
                modelMap.addAttribute("mdrcBatchConfigInfo", mdrcBatchConfigInfo);
                modelMap.addAttribute("product", product);
                modelMap.addAttribute("mdrcTemplate", mdrcTemplate);

                modelMap.addAttribute("minCardNum", getMinCardNum());
                modelMap.addAttribute("maxCardNum", getMaxCardNum());

                DecimalFormat df = new DecimalFormat("#.00");
                modelMap.put("totalPrice", df.format(mdrcCardmakeDetail.getCost() / 100.00));

                return "mdrcMakeCardApproval/edit.ftl";
            }
        }
        modelMap.addAttribute("errorMsg", "参数错误，为查找到该条记录!");
        return "error.ftl";
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
     * 获取制卡费用
     * 
     * @author qinqinyan
     * @param cardCount
     *            制卡张数
     * @date 2017/08/22
     */
    @RequestMapping("getMakeCardCostAjax")
    public void getMakeCardCostAjax(HttpServletRequest request, HttpServletResponse response, Long cardCount) {
        Map map = new HashMap<String, String>();
        logger.info("cardCount = {}", cardCount);
        String totalPrice = "0.00";
        if (cardCount != null) {
            Double price = caculateMakeCost(cardCount) / 100.00;
            logger.info("price ==== " + price);
            DecimalFormat df = new DecimalFormat("#.00");
            logger.info("totalPrice ==== " + df.format(price));
            totalPrice = df.format(price);
        }
        map.put("totalPrice", totalPrice);
        try {
            response.getWriter().write(JSON.toJSONString(map));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("就算制卡费用失败,失败原因：{}", e.getMessage());
        }
    }

    /**
     * 计算制卡费用
     * 
     * @author qinqinyan
     * @param cardCount
     *            制卡张数
     * @date 2017/08/22 费用计算规则 5,000 - 10,000 张: 5,000元/订单 10,001 - 20,000 张:
     *       6,500元/订单 20,001 - 50,000 张: 0.32元/张 50,001 - 100,000 张: 0.26元/张
     *       100,001- 200,000 张: 0.24元/张 200,001- 500,000 张: 0.22元/张
     *       500,001-1000,000 张: 0.20元/张
     */
    private Long caculateMakeCost(Long cardCount) {
        Long totalPrice = 0L;
        if (cardCount.longValue() <= 10000l) {
            totalPrice = 500000l;
        } else if (cardCount.longValue() > 10000l && cardCount.longValue() <= 20000l) {
            totalPrice = 650000l;
        } else {
            if (cardCount.longValue() > 20000l && cardCount.longValue() <= 50000l) {
                totalPrice = cardCount.longValue() * 32;
            } else if (cardCount.longValue() > 50000l && cardCount.longValue() <= 100000l) {
                totalPrice = cardCount.longValue() * 26;
            } else if (cardCount.longValue() > 100000l && cardCount.longValue() <= 200000l) {
                totalPrice = cardCount.longValue() * 24;
            } else if (cardCount.longValue() > 200000l && cardCount.longValue() <= 500000l) {
                totalPrice = cardCount.longValue() * 22;
            } else {
                totalPrice = cardCount.longValue() * 20;
            }
        }
        return totalPrice;
    }

    /**
     * 更据企业id获取产品
     * 
     * @author qinqinyan
     * @date 2017/07/28
     */
    @RequestMapping("getProductsByEntIdAjax")
    public void getProductsByEntIdAjax(HttpServletResponse response, Long enterpriseId) {
        Map map = new HashMap<String, String>();
        logger.info("enterpriseId = {}", enterpriseId);
        
        Manager manager = getCurrentUserManager();
        List<Product> products = null;
        //判断是否有权限编辑这个企业的制卡申请
        if(managerService.managedByManageId(enterpriseId, manager.getId())){
            products = productService.selectAllProductsByEnterId(enterpriseId);
        }else{
            logger.info("manageId = {} 没有权限查询企业 = {} 的产品列表", manager.getId(), enterpriseId);
        }
        map.put("products", JSON.toJSONString(products));
        try {
            response.getWriter().write(JSON.toJSONString(map));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("根据企业id = {} 获取产品失败,失败原因：{}", enterpriseId, e.getMessage());
        }
    }

    /**
     * 根据产品大小获取可用的卡面主题
     * 
     * @author qinqinyan
     * @date 2017/07/28
     */
    @RequestMapping("getTemplatesByProdSizeAjax")
    public void getTemplatesByProdSizeAjax(HttpServletResponse response, Long productSize) {
        Map map = new HashMap<String, String>();
        logger.info("productSize = {}", productSize);
        Map<String, List<MdrcTemplate>> templateMap = mdrcTemplateService.getThemeTemplatesByProdSize(productSize);
        map.put("templateMap", JSON.toJSONString(templateMap));
        try {
            response.getWriter().write(JSON.toJSONString(map));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("根据productSize = {} 获取卡面主题失败,失败原因：{} ", e.getMessage());
        }
    }

    /**
     * 获取制卡商列表
     * 
     * @param response
     * @author qinqinyan
     */
    @RequestMapping("getCardMakersAjax")
    public void getCardMakersAjax(HttpServletResponse response) {
        Map map = new HashMap<String, String>();
        List<MdrcCardmaker> cardmakerList = mdrcCardmakerService.selectAllCardmaker();
        map.put("cardmakerList", JSON.toJSONString(cardmakerList));
        try {
            response.getWriter().write(JSON.toJSONString(map));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("获取制卡商列表失败,失败原因：" + e.getMessage());
        }
    }

    /**
     * 校验营销卡详情对象
     * 
     * @author qinqinyan
     * @date 2017/08/01
     */
    private boolean verifyMdrcCardmakeDetail(MdrcCardmakeDetail mdrcCardmakeDetail) {
        if (mdrcCardmakeDetail == null || mdrcCardmakeDetail.getAmount() == null
                || mdrcCardmakeDetail.getStartTime() == null || mdrcCardmakeDetail.getEndTime() == null
                || mdrcCardmakeDetail.getEnterpriseId() == null || mdrcCardmakeDetail.getProductId() == null) {
            return false;
        }
        return true;
    }

    /**
     * 校验营销卡详情对象
     * 
     * @author qinqinyan
     * @date 2017/08/01
     */
    private boolean verifyMdrcBatchConfigInfo(MdrcBatchConfigInfo mdrcBatchConfigInfo) {
        if (mdrcBatchConfigInfo == null || mdrcBatchConfigInfo.getIsFree() == null
                || StringUtils.isEmpty(mdrcBatchConfigInfo.getName())
                || StringUtils.isEmpty(mdrcBatchConfigInfo.getMobile())
                || StringUtils.isEmpty(mdrcBatchConfigInfo.getAddress())
                || StringUtils.isEmpty(mdrcBatchConfigInfo.getPostcode())
                || StringUtils.isEmpty(mdrcBatchConfigInfo.getCustomerServicePhone())) {
            return false;
        }
        return true;
    }

    /**
     * 校验营销卡详情对象
     * 
     * @author qinqinyan
     * @date 2017/08/01
     */
    private boolean verifyImages(MultipartHttpServletRequest multipartRequest, MdrcCardmakeDetail mdrcCardmakeDetail) {
        try {
            Iterator<String> itr = multipartRequest.getFileNames();
            MultipartFile multipartFile = null;

            String qrcodeName = "";
            String certificate = "";
            String templateFront = "";
            String templateBack = "";

            while (itr != null && itr.hasNext()) {
                multipartFile = multipartRequest.getFile(itr.next());

                String originalFilename = multipartFile.getOriginalFilename();
                if (!StringUtils.isEmpty(originalFilename)) {
                    if (multipartFile.getName().equals("qrCode")) {
                        qrcodeName = originalFilename;
                    } else if (multipartFile.getName().equals("certificate")) {
                        certificate = originalFilename;
                    } else if (multipartFile.getName().equals("templateFront")) {
                        templateFront = originalFilename;
                    } else if (multipartFile.getName().equals("templateBack")) {
                        templateBack = originalFilename;
                    }
                }
            }

            if (StringUtils.isEmpty(qrcodeName) || StringUtils.isEmpty(certificate)
                    || mdrcCardmakeDetail.getTemplateId() == null && StringUtils.isEmpty(templateFront)
                    && StringUtils.isEmpty(templateBack)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("校验上传文件出错，错误原因" + e.getMessage());
            return false;
        }
    }

    /**
     * 上传文件到S3
     * 
     * @author qinqinyan
     * @date 2017/08/01
     */
    private boolean uploadFiles(MultipartHttpServletRequest multipartRequest, MdrcBatchConfigInfo mdrcBatchConfigInfo) {
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
                    if (multipartFile.getName().equals("qrCode")) {
                        if (!StringUtils.isEmpty(mdrcBatchConfigInfo.getQrcodeKey())) {
                            key = mdrcBatchConfigInfo.getQrcodeKey();
                        }
                        fileStoreService.save(key, file);
                        mdrcBatchConfigInfo.setQrcodeKey(key);
                        mdrcBatchConfigInfo.setQrcodeName(originalFilename);
                    } else if (multipartFile.getName().equals("certificate")) {
                        if (!StringUtils.isEmpty(mdrcBatchConfigInfo.getCertificateKey())) {
                            key = mdrcBatchConfigInfo.getCertificateKey();
                        }
                        fileStoreService.save(key, file);
                        mdrcBatchConfigInfo.setCertificateKey(key);
                        mdrcBatchConfigInfo.setCertificateName(originalFilename);
                    } else if (multipartFile.getName().equals("templateFront")) {
                        if (!StringUtils.isEmpty(mdrcBatchConfigInfo.getTemplateFrontKey())) {
                            key = mdrcBatchConfigInfo.getTemplateFrontKey();
                        }
                        fileStoreService.save(key, file);
                        mdrcBatchConfigInfo.setTemplateFrontKey(key);
                        mdrcBatchConfigInfo.setTemplateFrontName(originalFilename);
                    } else if (multipartFile.getName().equals("templateBack")) {
                        if (!StringUtils.isEmpty(mdrcBatchConfigInfo.getTemplateBackKey())) {
                            key = mdrcBatchConfigInfo.getTemplateBackKey();
                        }
                        fileStoreService.save(key, file);
                        mdrcBatchConfigInfo.setTemplateBackKey(key);
                        mdrcBatchConfigInfo.setTemplateBackName(originalFilename);
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
     * 由于XSS Filter捕获不到参数的值，所以在这里进行转化
     * */
    /*private String stripXss(String value) {
        if (!StringUtils.isEmpty(value)) {
            value = value.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;")
                    .replaceAll("'", "&apos;")
                    .replaceAll("\"", "&quot;");

        }
        return value;
    }*/

    /**
     * 提交编辑后的制卡申请
     */
    @RequestMapping("submitEditAjax")
    public void submitEditAjax(MultipartHttpServletRequest multipartRequest, HttpServletResponse response,
            HttpServletRequest request) throws IOException {
        Administer administer = getCurrentUser();
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = "参数缺失，提交失败！";
        String code = "fail";
        String failType = "1"; // 0：成功；1:参数异常，2：制卡商已经被删除；3内部错误

        String mdrcCardmakeDetailStr = request.getParameter("mdrcCardmakeDetail");
        String mdrcBatchConfigInfoStr = request.getParameter("mdrcBatchConfigInfo");

        if (administer != null && !StringUtils.isEmpty(mdrcCardmakeDetailStr)
                && !StringUtils.isEmpty(mdrcBatchConfigInfoStr)) {
            MdrcCardmakeDetail mdrcCardmakeDetail = JSON.parseObject(mdrcCardmakeDetailStr, MdrcCardmakeDetail.class);
            MdrcBatchConfigInfo mdrcBatchConfigInfo = JSON.parseObject(mdrcBatchConfigInfoStr,
                    MdrcBatchConfigInfo.class);
            if (mdrcCardmakeDetail != null && mdrcBatchConfigInfo != null) {
                //校验参数是否正常，防止被拦截篡改
                MdrcCardmakeDetail orgMdrcCardmakeDetail = mdrcCardmakeDetailService.selectByRequestId(mdrcCardmakeDetail.getRequestId());
                if(!managerService.managedByManageId(orgMdrcCardmakeDetail.getEnterpriseId(), getCurrentUserManager().getId())){
                    logger.info("用户 manageId  = {} 无权限修改企业 entId = {} 的制卡申请记录。");
                    map.put("failType", failType);
                    map.put("code", code);
                    map.put("msg", "对不起，您无权限修改该企业制卡申请记录！");
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }
                
                
                if (!s3Service.checkFile(multipartRequest)) {
                    logger.info("上传的图片只支持jpg、jpeg和png这三种格式");
                    map.put("failType", failType);
                    map.put("code", code);
                    map.put("msg", "上传的图片只支持jpg、jpeg和png这三种格式！");
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }

                if (s3Service.checkFileSize(multipartRequest)) {
                    logger.info("上传的图片大于1MB");
                    map.put("failType", failType);
                    map.put("code", code);
                    map.put("msg", "上传图片大小超过1MB!");
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }

                if (uploadFiles(multipartRequest, mdrcBatchConfigInfo)) {
                    ApprovalRequest approvalRequest = createApprovalRequest(administer.getId(),
                            mdrcCardmakeDetail.getEnterpriseId(), mdrcCardmakeDetail.getRequestId());
                    mdrcBatchConfigInfo.setId(orgMdrcCardmakeDetail.getConfigInfoId());
                    
                    mdrcCardmakeDetail.setEndTime(DateUtil.getEndTimeOfDate(mdrcCardmakeDetail.getEndTime()));
                    mdrcCardmakeDetail.setStartTime(DateUtil.getBeginOfDay(mdrcCardmakeDetail.getStartTime()));
                    List<MdrcCardmaker> cardmakerList = mdrcCardmakerService.selectAllCardmaker();
                    if (cardmakerList != null && cardmakerList.size() > 0) {
                        mdrcCardmakeDetail.setCardmakerId(cardmakerList.get(0).getId());
                    }
                    // 计算费用
                    mdrcCardmakeDetail.setCost(caculateMakeCost(mdrcCardmakeDetail.getAmount()));

                    mdrcCardmakeDetail.setConfigName(xssService.stripXss(mdrcCardmakeDetail.getConfigName()));
                    mdrcBatchConfigInfo.setAddress(xssService.stripXss(mdrcBatchConfigInfo.getAddress()));
                    mdrcBatchConfigInfo.setName(xssService.stripXss(mdrcBatchConfigInfo.getName()));
                    mdrcBatchConfigInfo.setCustomerServicePhone(xssService.stripXss(mdrcBatchConfigInfo
                            .getCustomerServicePhone()));
                    if (mdrcBatchConfigService.submitEditMdrcCardmakeApproval(approvalRequest, mdrcCardmakeDetail,
                            mdrcBatchConfigInfo)) {
                        logger.info("更新成功");
                        msg = "更新成功";
                        code = "success";
                        failType = "0";
                    } else {
                        logger.info("更新失败");
                        msg = "更新失败";
                        failType = "3";
                    }
                }
            }
        }
        map.put("code", code);
        map.put("msg", msg);
        map.put("failType", failType);
        response.getWriter().write(JSON.toJSONString(map));
        return;
    }

    /**
     * 提交审核
     * 
     * @param mdrcCardmakeDetail
     * @param request
     * @param response
     * @throws IOException
     * @author qinqinyan
     * 
     *         edit by qinqinyan on 2017/08/01
     */
    @RequestMapping("submitAjax")
    public void submitAjax(MultipartHttpServletRequest multipartRequest, HttpServletResponse response,
            HttpServletRequest request) throws IOException {
        Administer administer = getCurrentUser();
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = "参数缺失，提交失败！";
        String code = "fail";
        String failType = "1"; // 0：成功；1:参数异常，2：制卡商已经被删除；3内部错误

        String mdrcCardmakeDetailStr = request.getParameter("mdrcCardmakeDetail");
        String mdrcBatchConfigInfoStr = request.getParameter("mdrcBatchConfigInfo");

        if (!StringUtils.isEmpty(mdrcCardmakeDetailStr) && !StringUtils.isEmpty(mdrcBatchConfigInfoStr)) {

            MdrcCardmakeDetail mdrcCardmakeDetail = JSON.parseObject(mdrcCardmakeDetailStr, MdrcCardmakeDetail.class);
            MdrcBatchConfigInfo mdrcBatchConfigInfo = JSON.parseObject(mdrcBatchConfigInfoStr,
                    MdrcBatchConfigInfo.class);

            if (verifyMdrcCardmakeDetail(mdrcCardmakeDetail) && verifyMdrcBatchConfigInfo(mdrcBatchConfigInfo)
                    && verifyImages(multipartRequest, mdrcCardmakeDetail)) {
                if (!s3Service.checkFile(multipartRequest)) {
                    logger.info("上传的图片只支持jpg、jpeg和png这三种格式");
                    map.put("failType", failType);
                    map.put("code", code);
                    map.put("msg", "上传的图片只支持jpg、jpeg和png这三种格式！");
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }

                if (s3Service.checkFileSize(multipartRequest)) {
                    logger.info("上传的图片大于1MB");
                    map.put("failType", failType);
                    map.put("code", code);
                    map.put("msg", "上传图片大小超过1MB!");
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }

                if (uploadFiles(multipartRequest, mdrcBatchConfigInfo)) {
                    // 上传图片到s3成功
                    ApprovalRequest approvalRequest = createApprovalRequest(administer.getId(),
                            mdrcCardmakeDetail.getEnterpriseId(), null);
                    mdrcCardmakeDetail.setEndTime(DateUtil.getEndTimeOfDate(mdrcCardmakeDetail.getEndTime()));
                    mdrcCardmakeDetail.setStartTime(DateUtil.getBeginOfDay(mdrcCardmakeDetail.getStartTime()));

                    List<MdrcCardmaker> cardmakerList = mdrcCardmakerService.selectAllCardmaker();
                    if (cardmakerList != null && cardmakerList.size() > 0) {
                        mdrcCardmakeDetail.setCardmakerId(cardmakerList.get(0).getId());
                    }
                    // 计算费用
                    mdrcCardmakeDetail.setCost(caculateMakeCost(mdrcCardmakeDetail.getAmount()));

                    mdrcCardmakeDetail.setConfigName(xssService.stripXss(mdrcCardmakeDetail.getConfigName()));
                    mdrcBatchConfigInfo.setAddress(xssService.stripXss(mdrcBatchConfigInfo.getAddress()));
                    mdrcBatchConfigInfo.setName(xssService.stripXss(mdrcBatchConfigInfo.getName()));
                    mdrcBatchConfigInfo.setCustomerServicePhone(xssService.stripXss(mdrcBatchConfigInfo
                            .getCustomerServicePhone()));

                    if (mdrcBatchConfigService.submitMdrcCardmakeApproval(approvalRequest, mdrcCardmakeDetail,
                            mdrcBatchConfigInfo)) {
                        approvalRequestSmsService.sendNoticeSms(approvalRequest.getId());//增加发送制卡申请短信的功能
                        logger.info("提交成功");
                        msg = "提交成功";
                        code = "success";
                        failType = "0";
                    } else {
                        logger.info("提交失败");
                        msg = "提交失败";
                        failType = "3";
                    }
                }
            }
        }

        map.put("code", code);
        map.put("msg", msg);
        map.put("failType", failType);
        response.getWriter().write(JSON.toJSONString(map));
        return;
    }

    /**
     * 审批被驳回后重新提交审批申请
     * 
     * @param mdrcCardmakeDetail
     * @param request
     * @param response
     * @throws IOException
     * @author qinqinyan
     * @date 2017/08/01
     */
    /*
    * @RequestMapping("editSubmitAjax") public void
    * editSubmitAjax(MultipartHttpServletRequest multipartRequest,
    * MdrcCardmakeDetail mdrcCardmakeDetail, MdrcBatchConfigInfo
    * MdrcBatchConfigInfo, HttpServletResponse response) throws IOException{
    * Administer administer = getCurrentUser(); Map<String, Object> map = new
    * HashMap<String, Object>(); String msg = "参数缺失，提交失败！"; String code =
    * "fail"; String failType = "1"; //0：成功；1:参数异常，2：制卡商已经被删除；3内部错误
    * 
    * if(verifyMdrcCardmakeDetail(mdrcCardmakeDetail) &&
    * verifyMdrcBatchConfigInfo(MdrcBatchConfigInfo) &&
    * verifyImages(multipartRequest, mdrcCardmakeDetail)){
    * if(!s3Service.checkFile(multipartRequest)){
    * logger.info("上传的图片只支持jpg、jpeg和png这三种格式"); map.put("failType",
    * failType); map.put("code", code); map.put("msg",
    * "上传的图片只支持jpg、jpeg和png这三种格式！");
    * response.getWriter().write(JSON.toJSONString(map)); return; }
    * 
    * if(!s3Service.checkFileSize(multipartRequest)){
    * logger.info("上传的图片大于5MB"); map.put("failType", failType);
    * map.put("code", code); map.put("msg", "上传图片大小超过5MB!");
    * response.getWriter().write(JSON.toJSONString(map)); return; }
    * 
    * if(uploadFiles(multipartRequest, MdrcBatchConfigInfo)){ //上传图片到s3成功
    * ApprovalRequest approvalRequest =
    * createApprovalRequest(administer.getId(),
    * mdrcCardmakeDetail.getEnterpriseId(), null);
    * mdrcCardmakeDetail.setEndTime(DateUtil.getEndTimeOfDate(
    * mdrcCardmakeDetail.getEndTime()));
    * mdrcCardmakeDetail.setStartTime(DateUtil.getBeginOfDay(
    * mdrcCardmakeDetail.getStartTime()));
    * 
    * if(mdrcBatchConfigService.submitEditMdrcCardmakeApproval(
    * approvalRequest, mdrcCardmakeDetail, MdrcBatchConfigInfo)){
    * logger.info("提交成功"); msg = "提交成功"; code = "success"; failType = "0";
    * }else{ logger.info("提交失败"); msg = "提交失败"; failType = "3"; } } }
    * map.put("code", code); map.put("msg", msg); map.put("failType",
    * failType); response.getWriter().write(JSON.toJSONString(map)); return;
    * }
    */

    private ApprovalRequest createApprovalRequest(Long adminId, Long entId, Long requestId) {
        ApprovalRequest approvalRequest = new ApprovalRequest();
        ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                .selectByType(ApprovalType.MDRC_MakeCard_Approval.getCode());
        if (approvalProcessDefinition != null && approvalProcessDefinition.getStage() != null
                && approvalProcessDefinition.getId() != null) {
            approvalRequest.setProcessId(approvalProcessDefinition.getId());
            approvalRequest.setEntId(entId);
            approvalRequest.setCreatorId(adminId);
            approvalRequest.setStatus(0);

            if (approvalProcessDefinition.getStage().toString().equals("0")) {
                approvalRequest.setResult(ApprovalRequestStatus.APPROVED.getCode());
            } else {
                approvalRequest.setResult(ApprovalRequestStatus.APPROVING.getCode());
            }
            if (requestId != null) {
                // 编辑
                approvalRequest.setId(requestId);
            } else {
                approvalRequest.setCreateTime(new Date());
            }
            approvalRequest.setUpdateTime(new Date());
            approvalRequest.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        }
        return approvalRequest;
    }

    /**
     * 生成卡数据
     * <p>
     *
     * @param configName
     * @param cardmakerId
     * @param amount
     * @param templateId
     * @param response
     * @param mdrcCardmakeDetail
     * @param startTime
     * @param endTime
     * @throws IOException
     */
    //@RequestMapping("saveAjax")
    //@Transactional(propagation = Propagation.REQUIRED)
    public void saveAjax(String configName, long cardmakerId, long templateId, String startTime, String endTime,
            long amount, HttpServletResponse response, MdrcCardmakeDetail mdrcCardmakeDetail) throws IOException {
        // 获取当前登陆用户
        Administer administer = getCurrentUser();
        Map<String, Object> map = new HashMap<String, Object>();
        if (administer != null) {
            // 生成卡数据
            if (mdrcBatchConfigService.create(configName, cardmakerId, templateId, refineStartTime(startTime),
                    refineDeadline(endTime), amount, administer.getId())) {
                map.put("code", "ok");
            } else {
                map.put("code", "-1");
            }
        } else {
            map.put("code", "-1");
        }
        response.getWriter().write(JSON.toJSONString(map));
    }

    // 1、起始时间为今天则时间则精确到当前日期(yyyy-MM-dd hh:mm:ss)
    // 2、起始时间为今天以后的日期则精确到(yyyy-MM-dd 00:00:00)
    private Date refineStartTime(String startTime) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            String today = sdf1.format(new Date());
            if (today.equals(startTime)) {
                return sdf2.parse(sdf2.format(new Date()));
            } else {
                Date oldDate = sdf1.parse(startTime);
                String szDate = sdf2.format(oldDate);
                return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(szDate);
            }
        } catch (ParseException e) {
            logger.error(e.toString());
        }
        return null;
    }

    // 把截止日期更新成当天的最后一秒
    private Date refineDeadline(String oldDeadline) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        try {
            Date oldDate = sdf1.parse(oldDeadline);
            String szDate = sdf2.format(oldDate);
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(szDate);
        } catch (ParseException e) {
            logger.error(e.toString());
        }

        return null;
    }

    /**
     * 申请卡详情
     * 
     * @param requestId
     * @param map
     * @author qinqinyan
     * 
     *         edit by qinqinyan on 2017/08/01
     */
    @RequestMapping("requestDetail")
    public String requestDetail(ModelMap map, Long requestId) {
        MdrcCardmakeDetail mdrcCardmakeDetail = mdrcCardmakeDetailService.selectByRequestId(requestId);
        Manager manager = getCurrentUserManager();
        if (mdrcCardmakeDetail != null && manager!=null) {
            if(!managerService.managedByManageId(mdrcCardmakeDetail.getEnterpriseId(), manager.getId())){
                map.addAttribute("errorMsg", "对不起，您无权限查看该企业制卡申请记录!");
                return "error.ftl";
            }
            
            MdrcBatchConfigInfo mdrcBatchConfigInfo = mdrcBatchConfigInfoService
                    .selectByPrimaryKey(mdrcCardmakeDetail.getConfigInfoId());
            Enterprise enterprise = enterprisesService.selectById(mdrcCardmakeDetail.getEnterpriseId());
            Product product = productService.get(mdrcCardmakeDetail.getProductId());
            // 审批记录列表
            List<ApprovalRecord> approvalRecords = approvalRecordService.selectByRequestId(requestId);
            map.addAttribute("opinions", approvalRecords);

            MdrcTemplate mdrcTemplate = mdrcTemplateService.selectByPrimaryKey(mdrcCardmakeDetail.getTemplateId());
            // MdrcCardmaker mdrcCardmaker =
            // mdrcCardmakerService.selectByPrimaryKeyForshow(mdrcCardmakeDetail.getCardmakerId());

            String provinceFlag = getProvinceFlag();
            map.addAttribute("provinceFlag", provinceFlag);

            map.put("mdrcCardmakeDetail", mdrcCardmakeDetail);
            map.put("mdrcTemplate", mdrcTemplate);
            map.put("MdrcBatchConfigInfo", mdrcBatchConfigInfo);
            map.put("enterprise", enterprise);
            map.put("product", product);

            DecimalFormat df = new DecimalFormat("#.00");
            map.put("totalPrice", df.format(mdrcCardmakeDetail.getCost() / 100.00));

            // 是否需要审核
            ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                    .selectByType(ApprovalType.MDRC_MakeCard_Approval.getCode());
            Boolean wheatherNeedApproval = true;
            if (approvalProcessDefinition.getStage().toString().equals("0")) {
                wheatherNeedApproval = false;
            }
            map.addAttribute("wheatherNeedApproval", wheatherNeedApproval.toString());

            ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(requestId);
            map.put("approvalRequest", approvalRequest);

            return "mdrcMakeCardApproval/requestDetail.ftl";
        }
        map.addAttribute("errorMsg", "未查找到该激活申请!");
        return "error.ftl";
    }

    /**
     * 图片显示
     * 
     * @author qinqinyan
     */
    @RequestMapping(value = "/getImage")
    public void getImage(String type, String filename, HttpServletResponse response, HttpServletRequest request) {

        /*
         * if(StringUtils.isEmpty(type) || id == null){
         * logger.info("参数缺失，图片显示失败！"); return; } MdrcBatchConfigInfo
         * MdrcBatchConfigInfo =
         * MdrcBatchConfigInfoService.selectByPrimaryKey(id);
         * 
         * if (MdrcBatchConfigInfo == null) { logger.info(
         * "未查到到制卡申请id = {} 相应的扩展信息，图片显示失败！", id); return; }
         */
        if (!StringUtils.isEmpty(filename)) { // "qrCode".equals(type) &&
            getImageFromS3(response, filename, request);
        }
        /*
         * if ("certificate".equals(type) && !StringUtils.isEmpty(filename)) {
         * s3Service.getImageFromS3(response, filename, request); }
         */
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
}
