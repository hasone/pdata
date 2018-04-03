package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.province.dao.CustomerTypeMapper;
import com.cmcc.vrp.province.dao.DiscountMapper;
import com.cmcc.vrp.province.dao.GiveMoneyMapper;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.CustomerType;
import com.cmcc.vrp.province.model.Discount;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseFile;
import com.cmcc.vrp.province.model.EnterprisesExtInfo;
import com.cmcc.vrp.province.model.GiveMoney;
import com.cmcc.vrp.province.model.GiveMoneyEnter;
import com.cmcc.vrp.province.model.HistoryEnterpriseFile;
import com.cmcc.vrp.province.model.HistoryEnterprises;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.DiscountService;
import com.cmcc.vrp.province.service.EnterpriseFileService;
import com.cmcc.vrp.province.service.EnterprisesExtInfoService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.FileStoreService;
import com.cmcc.vrp.province.service.GiveMoneyEnterService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.HistoryEnterpriseFileService;
import com.cmcc.vrp.province.service.HistoryEnterprisesService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.S3Service;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;

import org.apache.commons.httpclient.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by qinqinyan on 2016/10/12.
 */
@Controller
@RequestMapping("manage/historyEnterprise")
public class HistoryEnterpriseController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(HistoryEnterpriseController.class);

    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Autowired
    ApprovalRequestService approvalRequestService;
    @Autowired
    HistoryEnterprisesService historyEnterprisesService;
    @Autowired
    HistoryEnterpriseFileService historyEnterpriseFileService;
    @Autowired
    ManagerService managerService;
    @Autowired
    ProductService productService;
    @Autowired
    AccountService accountService;
    @Autowired
    AdminManagerService adminManagerService;
    @Autowired
    AdministerService administerService;
    @Autowired
    S3Service s3Service;
    @Autowired
    DiscountService discountService;
    @Autowired
    ApprovalRecordService approvalRecordService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    EnterpriseFileService enterpriseFileService;
    @Autowired
    CustomerTypeMapper customerTypeMapper;
    @Autowired
    GiveMoneyMapper giveMoneyMapper;
    @Autowired
    DiscountMapper discountMapper;
    @Autowired
    GiveMoneyEnterService giveMoneyEnterService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    FileStoreService fileStoreService;
    @Autowired
    EnterprisesExtInfoService enterprisesExtInfoService;

    /**
     * @Title: enterpriseIndex
     * @Description:
     */
    @RequestMapping("index")
    public String enterpriseIndex(ModelMap model, QueryObject queryObject, Long entId) {
        if(queryObject != null){
            model.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        //管理员层级筛选
        //根节点为当前用户的管理员层级
        Manager manager = getCurrentUserManager();
        if (manager == null) {
            model.put("errorMsg", "当前用户无管理员身份");
            return "error.ftl";
        }

        model.addAttribute("managerId", manager.getId());
        model.addAttribute("currUserId", getCurrentUser().getRoleId());
        model.addAttribute("entId", entId);

        return "historyEnter/index.ftl";
    }

    /**
     * @Title: search
     * @Description:
     */
    @RequestMapping("search")
    public void search(QueryObject queryObject, HttpServletResponse res, Long entId) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        List<ApprovalRequest> approvalRequests = null;
        Long count = 0L;
        if (entId != null) {
            queryObject.getQueryCriterias().put("entId", entId);
            //queryObject.getQueryCriterias().put("approvalProcessList", approvalProcessList);

            approvalRequests = approvalRequestService.queryForEntChange(queryObject);
            count = approvalRequestService.countForEntChange(queryObject);
        }
        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", approvalRequests);
        json.put("total", count);
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Title: historyDetail
     * @Description:
     */
    @RequestMapping("historyDetail")
    public String historyDetail(ModelMap map, Long requestId) {

        if (requestId != null) {
            /**
             * 1、requestId对应企业历史修改记录
             * 2、历史对应审核记录
             * 3、企业对应文件
             * */
            HistoryEnterprises historyEnterprises = historyEnterprisesService.selectByRequestId(requestId);
            if (historyEnterprises != null && historyEnterprises.getEntId() != null) {

                /**
                 * 企业信息
                 * */
                map.addAttribute("historyEnterprises", historyEnterprises);

                /**
                 * 企业附件信息
                 * */
                HistoryEnterpriseFile historyEnterpriseFile = historyEnterpriseFileService.selectByRequestId(requestId);
                if (historyEnterpriseFile != null) {
                    map.addAttribute("businessLicence", historyEnterpriseFile.getBusinessLicence());
                    map.addAttribute("authorization", historyEnterpriseFile.getAuthorizationCertificate());
                    map.addAttribute("identification", historyEnterpriseFile.getIdentificationCard());
                    map.addAttribute("identificationBack", historyEnterpriseFile.getIdentificationBack());

                    map.addAttribute("customerfile", historyEnterpriseFile.getCustomerfileName());
                    map.addAttribute("contract", historyEnterpriseFile.getContractName());
                    map.addAttribute("image", historyEnterpriseFile.getImageName());
                }
                map.addAttribute("historyEnterpriseFile", historyEnterpriseFile);

                if (historyEnterpriseFile != null) {
                    if (!StringUtils.isEmpty(historyEnterpriseFile.getCustomerfileName()) || !StringUtils.isEmpty(historyEnterpriseFile.getContractName())
                            || !StringUtils.isEmpty(historyEnterpriseFile.getImageName())) {
                        //只要有一个文件不问空就需要翻页
                        map.addAttribute("nextPage", "true");
                    }else{
                        map.addAttribute("nextPage", "false");
                    }
                } else {
                    map.addAttribute("nextPage", "false");
                }

                /**
                 * 地区全称
                 */
                Manager manager = entManagerService.getManagerForEnter(historyEnterprises.getEntId());
                if (manager != null) {
                    String fullname = "";
                    map.addAttribute("fullDistrictName", managerService.getFullNameByCurrentManagerId(fullname, manager.getParentId()));
                }

                /**
                 * 获取企业管理员
                 */
                List<Administer> enterpriseManagers = adminManagerService.getAdminForEnter(historyEnterprises.getEntId());
                if (null != enterpriseManagers && enterpriseManagers.size() == 1) {
                    map.addAttribute("enterpriseManager", enterpriseManagers.get(0));
                }

                /**
                 * 获取客户经理
                 */
                Administer customerManager = administerService.selectByMobilePhone(historyEnterprises.getCmPhone());
                if (customerManager == null) {
                    customerManager = new Administer();
                }
                map.addAttribute("customerManager", customerManager);

                /**
                 * 显示企业余额
                 */
                Account account = accountService.get(historyEnterprises.getEntId(), productService.getCurrencyProduct().getId(),
                        AccountType.ENTERPRISE.getValue());

                if (account != null) {
                    Double minCount = account.getMinCount() / (-100.0);
                    if (account.getMinCount() == 0) {
                        minCount = 0D;
                    }
                    map.addAttribute("minCount", minCount);
                }

                /**
                 * 传递标识：是否是自营的企业
                 */
                if (getProvinceFlag().equals(zyProvinceFlagValue)) {
                    map.addAttribute("flag", 1);
                } else {
                    map.addAttribute("flag", 0);

                    /**
                     * 获取折扣信息
                     */
                    Discount discount = discountService.selectByPrimaryKey(historyEnterprises.getDiscount());
                    map.addAttribute("discountName", discount.getName());

                    /**
                     * 获取存送信息
                     */
                    GiveMoneyEnter giveMoney = giveMoneyEnterService.selectByEnterId(historyEnterprises.getEntId());
                    map.addAttribute("giveMoneyName", giveMoney.getGiveMoneyName());
                }
                /**
                 * 获取历史审批记录
                 * */
                List<ApprovalRecord> approvalRecords = approvalRecordService.selectByRequestId(requestId);
                map.addAttribute("opinions", approvalRecords);
                if (approvalRecords != null && approvalRecords.size() > 0) {
                    map.addAttribute("hasApproval", "true");
                } else {
                    map.addAttribute("hasApproval", "false");
                }

                CustomerType customerType = customerTypeMapper.selectById(historyEnterprises.getCustomerTypeId());
                map.addAttribute("customerName", customerType.getName());

                if ("gd_mdrc".equals(getProvinceFlag())) { //广东流量卡
                    EnterprisesExtInfo enterprisesExtInfo = enterprisesExtInfoService.get(historyEnterprises.getEntId());
                    map.put("extEntInfo", enterprisesExtInfo);
                    return "historyEnter/historyDetail_gd.ftl";
                } else {
                    return "historyEnter/historyDetail.ftl";
                }
            }
        }
        map.addAttribute("errorMsg", "为查找到相关记录!");
        return "error.ftl";
    }

    /**
     * 下载文件
     *
     * @param requestId 审核请求id
     * @author qinqinyan
     */
    @RequestMapping(value = "downloadFile")
    public void downloadFile(String type, Long requestId, HttpServletResponse response, HttpServletRequest request) {
        HistoryEnterpriseFile historyEnterpriseFile = historyEnterpriseFileService.selectByRequestId(requestId);

        if (historyEnterpriseFile == null) {
            return;
        }

        if (!hasAuthority(historyEnterpriseFile.getEntId())) {
            logger.error("当前用户没有下载该企业文件的权限！");
            response.setStatus(HttpStatus.SC_FORBIDDEN);
            return;
        }

        if ("contract".equals(type)) {
            s3Service.downloadFromS3(response, historyEnterpriseFile.getContractKey(), historyEnterpriseFile.getContractName(), request);
        }
        if ("customerFile".equals(type) || "customerfile".equals(type)) {
            s3Service.downloadFromS3(response, historyEnterpriseFile.getCustomerfileKey(), historyEnterpriseFile.getCustomerfileName(), request);
        }
        if ("image".equals(type)) {
            s3Service.downloadFromS3(response, historyEnterpriseFile.getImageKey(), historyEnterpriseFile.getImageName(), request);
        }
        if ("businessLicence".equals(type)) {
            s3Service.downloadFromS3(response, historyEnterpriseFile.getLicenceKey(), historyEnterpriseFile.getBusinessLicence(), request);
        }
        if ("authorization".equals(type)) {
            s3Service.downloadFromS3(response, historyEnterpriseFile.getAuthorizationKey(), historyEnterpriseFile.getAuthorizationCertificate(), request);
        }
        if ("identification".equals(type)) {
            s3Service.downloadFromS3(response, historyEnterpriseFile.getIdentificationKey(), historyEnterpriseFile.getIdentificationCard(), request);
        }
        if ("identificationBack".equals(type)) {
            s3Service.downloadFromS3(response, historyEnterpriseFile.getIdentificationBackKey(), historyEnterpriseFile.getIdentificationBack(), request);
        }
    }

    /**
     * 获取图片
     *
     * @param requestId 审核请求id
     * @author qinqinyan
     */
    @RequestMapping(value = "/getImage")
    public void getImage(String type, Long requestId, HttpServletResponse response, HttpServletRequest request) {

        HistoryEnterpriseFile historyEnterpriseFile = historyEnterpriseFileService.selectByRequestId(requestId);
        if (historyEnterpriseFile == null || historyEnterpriseFile.getEntId() == null) {
            return;
        }
        if (!hasAuthority(historyEnterpriseFile.getEntId())) {
            response.setStatus(HttpStatus.SC_FORBIDDEN);
            return;
        }

        if ("businessLicence".equals(type)) {
            s3Service.getImageFromS3(response, historyEnterpriseFile.getLicenceKey());
        }
        if ("identification".equals(type)) {
            s3Service.getImageFromS3(response, historyEnterpriseFile.getIdentificationKey());
        }
        if ("identificationBack".equals(type)) {
            s3Service.getImageFromS3(response, historyEnterpriseFile.getIdentificationBackKey());
        }
        if ("image".equals(type)) {
            s3Service.getImageFromS3(response, historyEnterpriseFile.getImageKey());
        }
    }

    /**
     * 修改企业信息
     *
     * @author qinqinyan
     */
    @RequestMapping("modifyEnterprise")
    public String modifyEnterprise(ModelMap modelMap, HttpServletResponse response, HttpServletRequest request, Long entId) {
        if (getProvinceFlag().equals("chongqing")) {
            modelMap.addAttribute("cqFlag", 1);
        } else {
            modelMap.addAttribute("cqFlag", 0);
        }

        if (entId != null) {
            Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
            if (enterprise != null) {
                modelMap.addAttribute("enterprises", enterprise);

                //获取客户经理
                Administer customerManager = administerService.selectByMobilePhone(enterprise.getCmPhone());
                modelMap.addAttribute("customerManager", customerManager);

                //获取企业管理员
                List<Administer> enterpriseManagers = adminManagerService.getAdminForEnter(enterprise.getId());
                modelMap.addAttribute("enterpriseManagers", enterpriseManagers);

                //地区全称
                Manager manager = entManagerService.getManagerForEnter(enterprise.getId());
                if (manager != null) {
                    String fullname = "";
                    modelMap.addAttribute("fullDistrictName", managerService.getFullNameByCurrentManagerId(fullname, manager.getParentId()));
                }

                //显示附件文件名称
                EnterpriseFile enterpriseFile = enterpriseFileService.selectByEntId(enterprise.getId());
                modelMap.addAttribute("enterpriseFile", enterpriseFile);

                //显示企业余额
                Product product = productService.getCurrencyProduct();
                if (product != null) {
                    Account account = accountService.get(entId, product.getId(), AccountType.ENTERPRISE.getValue());
                    modelMap.addAttribute("account", account);
                }

                //传递标识：是否是自营的企业
                if (getProvinceFlag().equals(zyProvinceFlagValue)) {
                    modelMap.addAttribute("flag", 1);
                } else {
                    modelMap.addAttribute("flag", 0);
                }

                //获取信用额度值
                Account account = accountService.get(enterprise.getId(), productService.getCurrencyProduct().getId(), AccountType.ENTERPRISE.getValue());
                if (account != null && account.getMinCount() != null) {
                    modelMap.addAttribute("minCount", account.getMinCount() == 0 ? 0 : account.getMinCount() / (-100.0));
                }

                //合作信息填写需在页面提供的内容
                //客户分类选项
                /*List<CustomerType> customerType = customerTypeMapper.selectAll();
                modelMap.addAttribute("customerType", customerType);*/
                CustomerType customerType = customerTypeMapper.selectById(enterprise.getCustomerTypeId());
                if (customerType != null) {
                    modelMap.addAttribute("customerName", customerType.getName());
                }

                //存送比
                GiveMoneyEnter giveMoneyEnter = giveMoneyEnterService.selectByEnterId(entId);
                modelMap.addAttribute("giveMoneyEnter", giveMoneyEnter);

                List<GiveMoney> giveMoneyList = giveMoneyMapper.selectNormalRecord();
                modelMap.addAttribute("giveMoneyList", giveMoneyList);

                //折扣
                List<Discount> discountList = discountMapper.selectAllDiscount();
                modelMap.addAttribute("discountList", discountList);

                if ("gd_mdrc".equals(getProvinceFlag())) { //广东流量卡
                    EnterprisesExtInfo enterprisesExtInfo = enterprisesExtInfoService.get(enterprise.getId());
                    modelMap.put("extEntInfo", enterprisesExtInfo);

                    return "historyEnter/detail-edit_gd.ftl";
                } else {
                    if(zyProvinceFlagValue.equals(getProvinceFlag())){//自营平台可以修改企业的折扣
                        modelMap.put("discountModify", true);
                    }
                    return "historyEnter/detail-edit.ftl";
                }
            }
        }
        modelMap.addAttribute("errorMsg", "参数有误，查找不到相关企业!");
        return "error.ftl";
    }

    /**
     * 1、提交审核 2、保存变更记录
     */
    @RequestMapping("saveChangeEnterprise")
    public String saveChangeEnterprise(ModelMap modelMap, HistoryEnterprises historyEnterprises, EnterprisesExtInfo enterprisesExtInfo,
                                       HttpServletResponse response, MultipartHttpServletRequest request) {

        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }

        if (historyEnterprises == null) {
            modelMap.addAttribute("errorMsg", "提交审核失败!");
            return "error.ftl";
        }

        //检查文件大小
        if (s3Service.checkFileSize(request)) {
            modelMap.addAttribute("errorMsg", "文件大小超过限制！");
            return modifyEnterprise(modelMap, response, request, historyEnterprises.getEntId());
        }

        //检查上传文件格式
        if (!s3Service.checkFile(request)) {
            modelMap.addAttribute("errorMsg", "图片格式不正确，只支持jpg，jpeg，png");
            return modifyEnterprise(modelMap, response, request, historyEnterprises.getEntId());
        }
        
        //广东众筹、广东流量卡，校验企业编码和集团产品编码是否有重复
        if (needExtEntInfo()) { //需要企业扩展信息的时候，才需要保存
            List<EnterprisesExtInfo> extInfos = enterprisesExtInfoService.selectByEcCodeAndEcPrdCode(historyEnterprises.getCode(), 
                    enterprisesExtInfo.getEcPrdCode());
            if(extInfos!=null && extInfos.size()>0){
                for(EnterprisesExtInfo extInfo : extInfos){
                    if(!extInfo.getEnterId().equals(historyEnterprises.getId())){
                        modelMap.addAttribute("errorMsg", "企业编码和集团产品号码已存在！");
                        return modifyEnterprise(modelMap, response, request, historyEnterprises.getEntId());
                    }
                }                
            }
        }

        try {
            historyEnterprises.setLicenceEndTime(DateUtil.getEndTimeOfDate(historyEnterprises.getLicenceEndTime()));
            if (historyEnterprises.getEndTime() != null) {
                historyEnterprises.setEndTime(DateUtil.getEndTimeOfDate(historyEnterprises.getEndTime()));
            }
            if (historyEnterprisesService.saveEdit(historyEnterprises, administer.getId(), request)) {
                logger.info("提交企业变更信息成功！企业ID-" + historyEnterprises.getEntId());
                return "redirect:/manage/enterprise/index.html";
            }
        } catch (RuntimeException e) {
            logger.info("提交企业变更信息失败！企业ID-" + historyEnterprises.getEntId());
        }
        return modifyEnterprise(modelMap, response, request, historyEnterprises.getEntId());
    }

    /**
     * 判断企业是否有信息变更审核记录
     *
     * @author qinqinyan
     */
    @RequestMapping("judgeApprovalAjax")
    public void judgeApprovalAjax(HttpServletRequest request, HttpServletResponse response,
                                  Long entId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        if (entId != null) {
            boolean hasApprovalRecordFlag = historyEnterprisesService.hasApprovalRecord(entId);
            if (!hasApprovalRecordFlag) {
                map.put("success", "success");
                response.getWriter().write(JSON.toJSONString(map));
                return;
            }
        }
        map.put("success", "fail");
        response.getWriter().write(JSON.toJSONString(map));
        return;
    }

    /**
     * 检查企业编码唯一性
     *
     * @author qinqinyan
     */
    @RequestMapping("checkUnique")
    public void checkUnique(HttpServletResponse response, Long entId, Enterprise enterprise) throws IOException {
        Boolean bFlag = false;
        if (entId != null && enterprise != null && enterprise.getCode() != null) {
            Enterprise enter = enterprisesService.queryEnterpriseByCode(enterprise.getCode());
            if (enter == null || enter.getId().toString().equals(entId.toString())) {
                bFlag = true;
            }
        }
        response.getWriter().write(bFlag.toString());
    }

}
