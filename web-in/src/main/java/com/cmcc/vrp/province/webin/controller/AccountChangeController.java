package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.province.dao.AdminDistrictMapper;
import com.cmcc.vrp.province.dao.AdminManagerEnterMapper;
import com.cmcc.vrp.province.dao.DistrictMapper;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.AccountChangeDetail;
import com.cmcc.vrp.province.model.AccountChangeOperator;
import com.cmcc.vrp.province.model.AccountChangeRequest;
import com.cmcc.vrp.province.model.AdminDistrict;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SdAccApprovalRequest;
import com.cmcc.vrp.province.service.AccountChangeApprovalRecordService;
import com.cmcc.vrp.province.service.AccountChangeDetailService;
import com.cmcc.vrp.province.service.AccountChangeOperatorService;
import com.cmcc.vrp.province.service.AccountChangeRequestService;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.CSVUtil;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.SizeUnits;
import com.cmcc.vrp.util.StringUtils;

/**
 * Created by sunyiwei on 2016/4/19.
 */
@Controller
@RequestMapping("/manage/accountChange")
public class AccountChangeController extends BaseController {
    private static final Logger logger = Logger.getLogger(AccountChangeController.class);
    //元转化为分
    private static final int _BIT = 100;

    @Autowired
    AdminDistrictMapper adminDistrictMapper;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    ProductService productService;
    @Autowired
    AccountService accountService;
    @Autowired
    AdministerService administerService;
    @Autowired
    AdminManagerEnterMapper adminManagerEnterMapper;
    @Autowired
    DistrictMapper districtMapper;
    @Autowired
    AccountChangeRequestService accountChangeRequestService;
    @Autowired
    AccountChangeApprovalRecordService accountChangeApprovalRecordService;
    @Autowired
    AccountChangeOperatorService accountChangeOperatorService;
    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Autowired
    AdminManagerService adminManagerService;
    @Autowired
    ApprovalRequestService approvalRequestService;
    @Autowired
    ApprovalRecordService approvalRecordService;
    @Autowired
    AccountChangeDetailService accountChangeDetailService;
    @Autowired
    EntManagerService entManagerService;
    @Autowired
    ManagerService managerService;
    @Autowired
    GlobalConfigService globalConfigService;

    /**
     * 账户变更列表
     *
     * @param modelMap 模型对象
     * @return 视图
     */
    @RequestMapping("index")
    public String accountChangeIndex(QueryObject queryObject, ModelMap modelMap) {
        modelMap.addAttribute("back", queryObject.getBack());//增加返回标识
        if (isCustomManager()) {
            modelMap.put("flag", "1");
        } else {
            modelMap.put("flag", "0");
        }

        Administer admin = getCurrentUser();
        if (admin == null) {
            return getLoginAddress();
        }

        //管理员层级筛选
        //根节点为当前用户的管理员层级
        Manager manager = getCurrentUserManager();
        if (manager == null) {
            modelMap.put("errorMsg", "当前用户无管理员身份");
            return "error.ftl";
        }

        //山东省级管理员不显示操作列
        if (isShowOperator(manager.getRoleId().toString(), getProvinceFlag())) {
            modelMap.addAttribute("isShowOperator", true);
        }

        modelMap.addAttribute("managerId", manager.getId());

        //判断是否有提交审核权限
        Boolean submitApprovalAuth = approvalProcessDefinitionService.hasAuthToSubmitApproval(manager.getRoleId(),
                ApprovalType.Account_Change_Approval.getCode());
        modelMap.addAttribute("submitApprovalAuth", submitApprovalAuth.toString());

        //山东流量平台显示BOSS ID，对应数据库字段phone
        modelMap.addAttribute("isShowPhone", isShowPhone(getProvinceFlag()));

        //传入省份标识
        modelMap.addAttribute("provinceFlag", getProvinceFlag());
        return "account_change/change_list.ftl";
    }

    /**
     * 待提交审核的充值列表
     *
     * @author qinqinyan
     */
    @RequestMapping("submitIndex")
    public String submitIndex(ModelMap modelMap, Long entId, Integer approvalType) {

        Administer currentAdmin = getCurrentUser();
        Manager manager = getCurrentUserManager();

        modelMap.addAttribute("entId", entId);
        modelMap.addAttribute("currentId", currentAdmin.getId());
        modelMap.addAttribute("roleId", manager.getRoleId());
        modelMap.addAttribute("approvalType", approvalType);
        return "account_change/submitChangeIndex.ftl";
    }

    /**
     * 搜索账户变更列表
     *
     * @param queryObject 查询对象
     * @param res         响应对象
     * @param entId       企业ID
     */
    @RequestMapping(value = "searchSubmitIndex")
    public void searchSubmitIndex(QueryObject queryObject, HttpServletResponse res, Long entId, Integer approvalType) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 查询参数: 企业名字、编码
         */

        if (!StringUtils.isEmpty(getRequest().getParameter("startTime"))) {
            queryObject.getQueryCriterias().put("startTime", getRequest().getParameter("startTime"));
            queryObject.getQueryCriterias().put("endTime", getRequest().getParameter("endTime"));
        }

        Map map = queryObject.toMap();
        map.put("entId", entId);
        map.put("approvalType", approvalType);

        List<AccountChangeOperator> accountChangeOperators = accountChangeOperatorService.selectByMap(map);
        Long count = accountChangeOperatorService.countByMap(map);

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", accountChangeOperators);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param entId
     * @param modelMap
     * @param accountChangeRequestId
     * @return
     * @Title: accountChange
     */
    @RequestMapping("change")
    public String accountChange(Long entId, Long prdId, ModelMap modelMap, Long accountChangeRequestId,
            Integer approvalType) {

        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        enterprise.setProductId(prdId);
        //获取地区名称
        /*String districtName = districtMapper.selectFullDistrictNameById(enterprise.getDistrictId());
        enterprise.setDistrictName(districtName);*/

        //获取余额
        Account account = accountService.get(entId, prdId, AccountType.ENTERPRISE.getValue());
        if (account != null) {
            Product product = productService.get(account.getProductId());
            if (ProductType.FLOW_ACCOUNT.getValue() == product.getType()) {//流量池产品，单位为KB，转换为MB，便于前端显示
                enterprise.setCurrencyCount(SizeUnits.KB.toMB(account.getCount().longValue()) * 1.0);
            } else {//其他产品，单位为分，转换为元
                enterprise.setCurrencyCount(account.getCount() / 100);
            }
            enterprise.setProductType(product.getType());//产品类型
            enterprise.setProductTypeName(ProductType.fromValue(product.getType().byteValue()).getDesc());
            enterprise.setProductName(product.getName());
        } else {
            enterprise.setCurrencyCount(0.0D);
        }

        modelMap.put("enterprise", enterprise);
        modelMap.put("provinceFlag", getProvinceFlag());
        modelMap.put("isAllowMinusCharge", isAllowNegetiveCharge());
        modelMap.put("approvalType", approvalType);

        if (ApprovalType.Account_Change_Approval.getCode().equals(approvalType)) {//账户余额充值
            return "account_change/change.ftl";
        } else if (ApprovalType.Account_Stop_Change_Approval.getCode().equals(approvalType)) {//账户暂停值重置
            return "account_change/stopChange.ftl";
        } else {
            return "error.ftl";
        }
    }

    /**
     * @param entId
     * @param modelMap
     * @return
     * @Title: editAlert
     */
    @RequestMapping("editAlert")
    public String editAlert(Long entId, Long prdId, ModelMap modelMap) {
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        enterprise.setProductId(prdId);
        //获取余额
        Account account = accountService.get(entId, prdId, AccountType.ENTERPRISE.getValue());
        if (account != null) {
            Double count = 0.0;
            Double minCount = 0.0;
            Double alertCount = 0.0;
            Double stopCount = 0.0;
            Product product = productService.get(prdId);
            //单位换算
            if (ProductType.FLOW_ACCOUNT.getValue() == product.getType().intValue()) {//流量池产品，KB转MB
                count = SizeUnits.KB.toMB(account.getCount().longValue()) * 1.0;
                minCount = SizeUnits.KB.toMB(account.getMinCount().longValue()) * 1.0;
                alertCount = SizeUnits.KB.toMB(account.getAlertCount().longValue()) * 1.0;
                stopCount = SizeUnits.KB.toMB(account.getStopCount().longValue()) * 1.0;
            } else {//资金产品、话单产品，分转元
                count = account.getCount() / 100.00;
                minCount = account.getMinCount() / 100.00;
                alertCount = account.getAlertCount() / 100.00;
                stopCount = account.getStopCount() / 100.00;
            }
            enterprise.setProductType(product.getType());
            enterprise.setProductTypeName(ProductType.fromValue(product.getType().byteValue()).getDesc());
            enterprise.setProductName(product.getName());
            enterprise.setCurrencyCount(count);
            enterprise.setMinCount(minCount);
            enterprise.setAlertCount(alertCount);
            enterprise.setStopCount(stopCount);
        } else {
            enterprise.setCurrencyCount(0.0D);
            enterprise.setMinCount(0.0D);
            enterprise.setAlertCount(0.0D);
            enterprise.setStopCount(0.0D);
        }

        modelMap.put("enterprise", enterprise);
        modelMap.put("provinceFlag", getProvinceFlag());
        return "account_change/editAlert.ftl";
    }

    /**
     * 添加或编辑配置项的数据库操作
     *
     * @param modelMap
     * @param enterprise
     */
    @RequestMapping(value = "saveAlert")
    public String saveAlert(ModelMap modelMap, Enterprise enterprise, Long prdId) {
        //获取余额
        Account account = accountService.get(enterprise.getId(), prdId, AccountType.ENTERPRISE.getValue());
        Product product = productService.get(prdId);
        //单位换算
        Double alert = enterprise.getAlertCount() == null ? 0.0 : enterprise.getAlertCount();//预警值
        Double stopCount = enterprise.getStopCount() == null ? 0.0 : enterprise.getStopCount();//暂停值
        if (ProductType.FLOW_ACCOUNT.getValue() == product.getType().intValue()) {//流量池产品，MB转KB
            alert = SizeUnits.MB.toKB(alert) * 1.0;
            stopCount = SizeUnits.MB.toKB(stopCount) * 1.0;
        } else {//资金产品、话单产品，元转分
            alert = alert * 100;
            stopCount = stopCount * 100;
        }
        account.setAlertCount(alert);
        account.setStopCount(stopCount);

        //审批列表

        if (accountService.updateAlertSelective(account)) {
            accountService.recoverAlert(enterprise.getId(), prdId);
            return "redirect:index.html";
        } else {
            if ("hun".equals(getProvinceFlag())) {//湖南省份显示是暂停值
                modelMap.addAttribute("errorMsg", "设置暂停值失败!");
            } else {
                modelMap.addAttribute("errorMsg", "设置预警值失败!");
            }
            return editAlert(enterprise.getId(), prdId, modelMap);
        }
    }

    /**
     * 企业充值编辑及企业充值详情
     *
     * @param entId
     * @param modelMap
     * @param accountChangeRequestId
     * @param edit
     * @return
     */
    @RequestMapping("changeEdit")
    public String accountChangeEdit(Long entId, Long prdId, ModelMap modelMap, Long accountChangeRequestId,
            boolean edit, Integer approvalType) {
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        enterprise.setProductId(prdId);
        //获取地区名称
        /*String districtName = districtMapper.selectFullDistrictNameById(enterprise.getDistrictId());
        enterprise.setDistrictName(districtName);*/

        //获取余额
        Account account = accountService.get(entId, prdId, AccountType.ENTERPRISE.getValue());
        enterprise.setCurrencyCount(account.getCount());

        Product product = productService.get(account.getProductId());
        if (product != null) {
            enterprise.setProductType(product.getType());
            enterprise.setProductTypeName(ProductType.fromValue(product.getType().byteValue()).getDesc());
            enterprise.setProductName(product.getName());
        }

        //地区全称
        Manager entManager = entManagerService.getManagerForEnter(enterprise.getId());
        if (entManager != null) {
            String fullname = "";
            enterprise
                    .setCmManagerName(managerService.getFullNameByCurrentManagerId(fullname, entManager.getParentId()));
        }

        modelMap.put("enterprise", enterprise);

        if (accountChangeRequestId != null) {
            AccountChangeOperator aco = accountChangeOperatorService.selectByPrimaryKey(accountChangeRequestId);
            if (aco == null) {
                modelMap.put("errorMsg", "无效的充值请求ID:" + accountChangeRequestId);
                return "error.ftl";
            }

            modelMap.put("money", aco.getCount());
            modelMap.put("discountType", aco.getDiscountType());
            modelMap.put("discountValue", aco.getDiscountValue());
            modelMap.put("provinceFlag", getProvinceFlag());
            modelMap.put("accountChangeRequestId", accountChangeRequestId);
        }

        //显示历史审批意见
        //        List<AccountChangeApprovalRecord> approvalRecords = accountChangeApprovalRecordService.getByRequestId(accountChangeRequestId);
        //        modelMap.addAttribute("approvalRecords", approvalRecords);
        modelMap.addAttribute("approvalType", approvalType);
        if (edit) {
            return "account_change/change_edit.ftl";
        } else {
            return "account_change/change_detail.ftl";
        }
    }

    /**
     * 显示记录详情
     *
     * @param modelMap     模型对象
     * @param requestId    请求ID
     * @param approvalType 审批类型
     * @return 视图
     */
    @RequestMapping("recordDetial")
    public String recordDetial(ModelMap modelMap, Long requestId, Integer approvalType) {

        if (requestId != null && approvalType != null) {
            ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(requestId);

            if (approvalRequest != null && approvalRequest.getEntId() != null) {
                //充值请求记录
                //modelMap.addAttribute("approvalRequest", approvalRequest);
                //请求充值额度
                AccountChangeDetail accountChangeDetail = accountChangeDetailService.selectByRequestId(approvalRequest
                        .getId());
                modelMap.addAttribute("requestCount", accountChangeDetail.getCount());
                if (accountChangeDetail.getDiscountType() != null) {
                    switch (accountChangeDetail.getDiscountType()) {
                        case 0:
                            modelMap.addAttribute("discountType", "无");
                            break;
                        case 1:
                            modelMap.addAttribute("discountType", "存送比例");
                            break;
                        default:
                            modelMap.addAttribute("discountType", "无");
                            break;
                        }
                } else {
                    modelMap.addAttribute("discountType", "无");
                }
                if (accountChangeDetail.getDiscountValue() != null) {
                    modelMap.addAttribute("discountValue", accountChangeDetail.getDiscountValue().toString() + "%");
                } else {
                    modelMap.addAttribute("discountValue", "0");
                }

                modelMap.addAttribute("provinceFlag", getProvinceFlag());
                //企业信息
                Enterprise enterprise = enterprisesService.selectByPrimaryKey(approvalRequest.getEntId());
                if (enterprise == null) {
                    modelMap.addAttribute("errorMsg", "数据库中没有相关企业Id: " + approvalRequest.getEntId());
                    return "error.ftl";
                }

                //地区全称
                Manager entManager = entManagerService.getManagerForEnter(enterprise.getId());
                if (entManager != null) {
                    String fullname = "";
                    enterprise.setCmManagerName(managerService.getFullNameByCurrentManagerId(fullname,
                            entManager.getParentId()));
                }

                //获得当前企业余额
                Account account = accountService.get(enterprise.getId(), accountChangeDetail.getProductId(),
                        AccountType.ENTERPRISE.getValue());
                if (account != null) {
                    Product product = productService.get(accountChangeDetail.getProductId());
                    enterprise.setProductType(product.getType());
                    enterprise.setProductTypeName(ProductType.fromValue(product.getType().byteValue()).getDesc());
                    enterprise.setProductName(product.getName());
                    modelMap.addAttribute("account", account.getCount());
                }

                modelMap.addAttribute("enterprise", enterprise);
                //显示历史审批意见
                //审批记录列表
                List<ApprovalRecord> approvalRecords = approvalRecordService.selectByRequestIdAll(requestId);
                //用户名
                if (approvalRecords != null && approvalRecords.size() > 0) {
                    for (ApprovalRecord item : approvalRecords) {
                        if (item.getOperatorId() != null) {
                            Administer administer = administerService.selectAdministerById(item.getOperatorId());
                            item.setUserName(administer.getUserName());
                        }
                    }
                }

                modelMap.addAttribute("isShowPhone", isShowPhone(getProvinceFlag()));
                modelMap.addAttribute("approvalRecords", approvalRecords);
                modelMap.addAttribute("approvalType", approvalType);
                //modelMap.addAttribute("approvalRecordId", approvalRecords.get(approvalRecords.size()-1).getId());
                //审核请求记录id
                //modelMap.addAttribute("requestId", requestId);
                //审批流程id
                //modelMap.addAttribute("processId", approvalRequest.getProcessId());

                return "account_change/change_detail.ftl";
            }
            logger.info("数据库中没有相关审批记录Id: " + requestId);
        }
        return "redirect:record.html";
    }

    /**
     * 保存变更申请
     *
     * @param model                  模型对象
     * @param entCode                企业编码
     * @param deltaCount             变更数量
     * @param accountChangeRequestId 变更请求ID
     * @return 视图
     */
    @RequestMapping("saveChange")
    public String saveAccountChange(ModelMap model, String entCode, Long prdId, Double deltaCount,
            Long accountChangeRequestId, Integer discountType, Integer discountValue, Integer approvalType) {
        //该方法会出现如果企业突然在非正常状态了，就无法通过编码筛选出来，不符合需求，因此修改
        //Enterprise enterprise = enterprisesService.selectByCode(entCode);
        Map map = new HashMap();
        map.put("uniqueCode", entCode);
        List<Enterprise> enterprises = enterprisesService.selectEnterpriseByMap(map);
        if (enterprises == null || enterprises.size() <= 0) {
            model.put("errorMsg", "企业信息异常，无法修改充值申请！");
            return "error.ftl";
        }

        Enterprise enterprise = enterprises.get(0);
        model.put("entId", enterprise.getId());
        //x
        Product product = productService.selectProductById(prdId);
        if (product == null) {
            model.put("errorMsg", "账户不存在！");
            return accountChangeEdit(enterprise.getId(), prdId, model, accountChangeRequestId, true, approvalType);
        }

        //前端传过来的充值额度，单位说明，流量池单位为MB，钱单位为元，包单位为个数
        //        Double deltaCountIn = ProductType.transferMiniUnit(product.getType().byteValue(), deltaCount);

        if (deltaCount == 0d && accountChangeRequestId != null) {
            model.put("errorMsg", "充值额度为0");
            return accountChangeEdit(enterprise.getId(), prdId, model, accountChangeRequestId, true, approvalType);
        }

        if (accountChangeRequestId == null) {
            String serialNum = DigestUtils.md5Hex(UUID.randomUUID().toString());

            //变更现金产品，改为插入临时操作表
            AccountChangeOperator accountChangeOperator = createAccountChangeOperator(enterprise.getId(), prdId,
                    AccountType.ENTERPRISE, deltaCount, serialNum, discountType, discountValue, approvalType);
            if (accountChangeOperatorService.insert(accountChangeOperator)) {
                return "redirect:submitIndex.html?approvalType=" + approvalType;
            }
        } else {
            AccountChangeOperator accountChangeOperator = new AccountChangeOperator();
            accountChangeOperator.setId(accountChangeRequestId);
            accountChangeOperator.setCount(deltaCount);
            accountChangeOperator.setUpdateTime(new Date());
            accountChangeOperator.setDiscountType(discountType);
            accountChangeOperator.setDiscountValue(discountValue);

            if (accountChangeOperatorService.updateByPrimaryKeySelective(accountChangeOperator)) {
                return "redirect:submitIndex.html?approvalType=" + approvalType;
            }
        }

        //accountChangeRequestId不为空，是编辑操作
        /*if (accountChangeRequestId != null) {
            AccountChangeRequest acr = build(enterprise.getId(), product.getId(), AccountType.ENTERPRISE, administer.getId(), deltaCount, serialNum);
            if (accountChangeRequestService.updateCount(accountChangeRequestId, deltaCount, getCurrentUser().getId())) {
                return "redirect:record.html";
            } else {
                model.put("errorMsg", "编辑充值记录失败！");
                return accountChangeEdit(enterprise.getId(), model, accountChangeRequestId, true);
            }
        } else {
            AccountChangeRequest acr = build(enterprise.getId(), product.getId(), AccountType.ENTERPRISE, administer.getId(), deltaCount, serialNum);
            accountChangeRequestService.insert(acr);
        }*/

        return "redirect:submitIndex.html?approvalType=" + approvalType;
    }

    /**
     * 
     * @Title: createAccountChangeOperator 
     * @Description: TODO
     * @param entId
     * @param prdId
     * @param accountType
     * @param deltaCount
     * @param serialNum
     * @param discountValue 
     * @param discountType 
     * @return
     * @return: AccountChangeOperator
     */
    private AccountChangeOperator createAccountChangeOperator(Long entId, Long prdId, AccountType accountType,
            Double deltaCount, String serialNum, Integer discountType, Integer discountValue, Integer changeType) {
        AccountChangeOperator accountChangeOperator = new AccountChangeOperator();

        Account account = accountService.get(entId, prdId, accountType.getValue());
        accountChangeOperator.setCount(deltaCount);
        accountChangeOperator.setEntId(entId);
        accountChangeOperator.setPrdId(prdId);
        accountChangeOperator.setAccountId(account.getId());
        accountChangeOperator.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        accountChangeOperator.setSerialNum(serialNum);
        accountChangeOperator.setCreateTime(new Date());
        accountChangeOperator.setUpdateTime(new Date());
        accountChangeOperator.setDiscountType(discountType);
        accountChangeOperator.setDiscountValue(discountValue);
        accountChangeOperator.setChangeType(changeType);//账户变更类型
        return accountChangeOperator;
    }

    /**
     * 帐户充值列表
     */
    @RequestMapping(value = "search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 查询参数: 企业名字、编码
         */
        setQueryParameter("name", queryObject);
        setQueryParameter("code", queryObject);
        setQueryParameter("phone", queryObject);

        if (!StringUtils.isEmpty(getRequest().getParameter("startTime"))) {
            queryObject.getQueryCriterias().put("beginTime", getRequest().getParameter("startTime"));
            queryObject.getQueryCriterias().put("endTime", getRequest().getParameter("endTime"));
        }

        List<Byte> productTypeList = null;
        String requestProductType = getRequest().getParameter("productType");//产品类型
        if (org.apache.commons.lang.StringUtils.isNotBlank(requestProductType)
                && org.apache.commons.lang.StringUtils.isNumeric(requestProductType)) {//前端产品选择
            productTypeList = new ArrayList<Byte>();
            productTypeList.add(Byte.valueOf(requestProductType));
        } else {//默认显示全部产品：资金产品、流量池产品
            productTypeList = new ArrayList<Byte>();
            productTypeList.add(ProductType.CURRENCY.getValue());//资金账户类型
            productTypeList.add(ProductType.FLOW_ACCOUNT.getValue());//流量池账户
            productTypeList.add(ProductType.PRE_PAY_CURRENCY.getValue());//预付费产品账户
        }

        queryObject.getQueryCriterias().put("productTypeList", productTypeList);

        /**
         * 当前登录用户的管理员身份
         */
        Manager manager = getCurrentUserManager();
        queryObject.getQueryCriterias().put("managerId", manager.getId());

        //设置企业状态，必须是正常的
        queryObject.getQueryCriterias().put("deleteFlag", "0");

        // 数据库查找符合查询条件的个数

        List<Enterprise> enterpriseList = enterprisesService.showEnterprisesAccountsForPageResult(queryObject);
        int count = enterprisesService.showEnterprisesAccountsCount(queryObject);
        Iterator<Enterprise> it = enterpriseList.iterator();
        //充填账户类型
        while (it.hasNext()) {
            Enterprise temp = it.next();
            temp.setProductTypeName(ProductType.fromValue(temp.getProductType().byteValue()).getDesc());

            //填充企业地区信息 fix bug PDATA-1653 : 20170710 luozuwu added
            Manager districtName = entManagerService.getManagerForEnter(temp.getId());
            if (districtName != null) {
                String fullname = "";
                temp.setCmManagerName(managerService.getFullNameByCurrentManagerId(fullname, districtName.getParentId()));
            }

        }
        //        List<Enterprise> enterprises = new ArrayList<Enterprise>();
        //
        //        if (enterpriseList != null) {
        //            for (Enterprise enterprise : enterpriseList) {
        //                //地区全称
        //                Manager entManager = entManagerService.getManagerForEnter(enterprise.getId());
        //                List<Account> accountList = accountService.getByEntId(enterprise.getId());//获取企业所有账户信息
        //                if (accountList != null && accountList.size() > 0) {
        //                    for (Account account : accountList) {      
        //                            Product product = productService.get(account.getProductId());
        //                            if(product == null){
        //                                continue;
        //                            }
        //                            
        //                            if (productType != null && productType != product.getType().byteValue()) {
        //                                continue;
        //                            }
        //                            Enterprise temp = new Enterprise();
        //                            temp.setId(enterprise.getId());
        //                            temp.setName(enterprise.getName());
        //                            temp.setCode(enterprise.getCode());
        //                            temp.setCreateTime(enterprise.getCreateTime());
        //                            temp.setStartTime(enterprise.getStartTime());
        //                            temp.setEndTime(enterprise.getEndTime());
        //                            temp.setProductType(ProductType.fromValue(product.getType().byteValue()).getDesc());
        //                            temp.setProductName(product.getName());
        //                            temp.setProductId(account.getProductId());
        //                            temp.setCurrencyCount(account.getCount());
        //                            temp.setAlertCount(account.getAlertCount());
        //                            temp.setStopCount(account.getStopCount());
        //
        //                            if (entManager != null) {
        //                                String fullname = "";
        //                                temp.setCmManagerName(managerService.getFullNameByCurrentManagerId(fullname,
        //                                        entManager.getParentId()));
        //                            }
        //                            enterprises.add(temp);
        //                        }
        //
        //                }
        //            }

        //分页问题
        //            int pageNum = queryObject.getPageNum() - 1 ;
        //            int pageSize = queryObject.getPageSize();
        //            int totalSize = enterprises.size();
        //            List<Enterprise> subEnterprises = new ArrayList<Enterprise>();
        //            if(pageSize * pageNum < totalSize) {
        //                subEnterprises = enterprises.subList(pageNum * pageSize, pageSize);
        //            }else{
        //                subEnterprises = enterprises.subList(pageNum * pageSize, totalSize);
        //            }

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", enterpriseList);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //    private AccountChangeRequest build(Long entId, Long prdId, AccountType accountType, Long creatorId,
    //            Double deltaCount, String serialNum) {
    //        AccountChangeRequest acr = new AccountChangeRequest();
    //
    //        Account account = accountService.get(entId, prdId, accountType.getValue());
    //        acr.setCount(deltaCount);
    //        acr.setAccountId(account.getId());
    //        acr.setEntId(entId);
    //        acr.setPrdId(prdId);
    //        acr.setCreatorId(creatorId);
    //        acr.setDesc(AccountChangeRequestStatus.APPROVING_ONE.getDesc());
    //        acr.setSerialNum(serialNum);
    //        acr.setStatus(AccountChangeRequestStatus.APPROVING_ONE.getValue());
    //        acr.setCreateTime(new Date());
    //        acr.setUpdateTime(new Date());
    //        acr.setDeleteFlag((byte) Constants.DELETE_FLAG.UNDELETED.getValue());
    //
    //        return acr;
    //    }

    /**
     * 
     * @Title: subjectList 
     * @Description: TODO
     * @return
     * @return: String
     */
    @RequestMapping("subjectList")
    public String subjectList() {
        return "account_change/subject_list.ftl";
    }

    /**
     * 企业充值审核列表查询
     *
     * @param queryObject
     * @param res
     */
    @RequestMapping("subjectSearch")
    public void subjectSearch(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 查询参数: 企业名字、编码
         */
        setQueryParameter("name", queryObject);
        setQueryParameter("code", queryObject);

        if (!StringUtils.isEmpty(getRequest().getParameter("startTime"))) {
            queryObject.getQueryCriterias().put("startTime", getRequest().getParameter("startTime"));
            queryObject.getQueryCriterias().put("endTime", getRequest().getParameter("endTime"));
        }

        /**
         * 当前登录用户
         */
        Administer administer = getCurrentUser();
        Long adminId = administer.getId();
        Long roleId = adminRoleService.getRoleIdByAdminId(adminId);
        String superAdminRoleId = getSuperAdminRoleId();
        final String ENTERPRISE_CONTACTOR = getENTERPRISE_CONTACTOR();

        //超级管理员，获取所有的企业，其他管理员获取该地区所有企业
        if (!roleId.toString().equals(superAdminRoleId) && !roleId.toString().equals(ENTERPRISE_CONTACTOR)) {
            //queryObject.getQueryCriterias().put("adminId", adminId);
            //获取当前用户districtId
            List<AdminDistrict> ad = adminDistrictMapper.selectAdminDistrictByAdminId(adminId);
            if (ad != null && ad.size() == 1) {
                queryObject.getQueryCriterias().put("districtId", ad.get(0).getDistrictId().toString());
            }
        }
        //企业管理员获取所管理的所有企业
        if (roleId.toString().equals(ENTERPRISE_CONTACTOR)) {
            queryObject.getQueryCriterias().put("adminId", adminId);
        }

        //设置查询参数，地区
        setQueryParameter("districtId", queryObject);

        //根据权限可以传递当前用户可展示的企业充值请求状态,状态为一个逗号分隔的字符串
        if (roleId.toString().equals(getProvinceManager())) {
            //省级管理员
            queryObject.getQueryCriterias().put("status", "2,3");
        }
        if (roleId.toString().equals(getCityManager())) {
            //市级管理员
            queryObject.getQueryCriterias().put("status", "1");
        }

        //审核列表中不出现上一个操作者是自己的记录
        queryObject.getQueryCriterias().put("lastOperatorId", adminId);

        // 数据库查找符合查询条件的个数
        int count = accountChangeRequestService.queryCount(queryObject);
        List<AccountChangeRequest> list = accountChangeRequestService.query(queryObject);

        //插入客户经理及客户经理手机号
        for (AccountChangeRequest acr : list) {
            /**
             * 显示当前该企业的客户经理信息
             */
            List<Administer> currentCustomerManager = adminManagerEnterMapper.selectAdminByEnterId(acr.getEntId());
            if (currentCustomerManager != null && currentCustomerManager.size() > 0) {
                acr.setManagerName(currentCustomerManager.get(0).getUserName());
                acr.setManagerPhone(currentCustomerManager.get(0).getMobilePhone());
            }
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
     * 显示充值记录
     *
     * @return
     */
    @RequestMapping("record")
    public String record(ModelMap modelMap, Integer approvalType) {
        Administer administer = getCurrentUser();
        Manager manager = getCurrentUserManager();
        modelMap.addAttribute("managerId", manager.getId());
        modelMap.addAttribute("adminId", administer.getId());

        //山东流量平台显示BOSS ID，对应数据库字段phone
        modelMap.addAttribute("isShowPhone", isShowPhone(getProvinceFlag()));

        //甘肃显示优惠类型优惠值
        modelMap.addAttribute("provinceFlag", getProvinceFlag());

        //不同类型
        modelMap.addAttribute("approvalType", approvalType);
        return "account_change/record.ftl";
    }

    /**
     * 
     * @Title: searchRecord 
     * @Description: TODO
     * @param queryObject
     * @param res
     * @param managerId
     * @param adminId
     * @return: void
     */
    @RequestMapping("searchRecord")
    public void searchRecord(QueryObject queryObject, HttpServletResponse res, Long managerId, Long adminId,
            Integer approvalType) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        List<ApprovalRequest> approvalRequests = null;
        Long count = 0L;

        //当前用户节点只能查看其子节点企业信息
        if (managerService.isParentManage(managerId, getCurrentUserManager().getId())) {
            /**
             * 查询参数: 企业名字、编码
             */
            setQueryParameter("name", queryObject);
            setQueryParameter("code", queryObject);
            setQueryParameter("phone", queryObject);
            if (!StringUtils.isEmpty(getRequest().getParameter("startTime"))) {
                queryObject.getQueryCriterias().put("startTime", getRequest().getParameter("startTime"));
                queryObject.getQueryCriterias().put("endTime", getRequest().getParameter("endTime"));
            }

            //   String name = queryObject.getQueryCriterias().get("name").toString();
            //   String code = queryObject.getQueryCriterias().get("code").toString();

            /**
             * 1、获取manageId所管理的合法企业列表
             * 2、根据给定的entIds范围筛选审核请求记录
             * */
            approvalRequests = approvalRequestService.queryApprovalRequestsForAccountChange(queryObject, adminId,
                    approvalType);

            if (approvalRequests != null && approvalRequests.size() > 0) {
                for (ApprovalRequest approvalRequest : approvalRequests) {
                    String description = approvalRequestService.getCurrentStatus(approvalRequest);
                    approvalRequest.setDescription(description);

                    //地区全称
                    Manager entManager = entManagerService.getManagerForEnter(approvalRequest.getEntId());
                    if (entManager != null) {
                        String fullname = "";
                        approvalRequest.setDistrictName(managerService.getFullNameByCurrentManagerId(fullname,
                                entManager.getParentId()));
                    }
                }
            }

            count = approvalRequestService.countApprovalRequestsForAccountChange(queryObject, adminId, approvalType);
        }

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", approvalRequests);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @Title: isShowPhone 
     * @Description: 是否显示集团编码：山东需要显示
     * @param provinceFlag
     * @return
     * @return: boolean
     */
    private boolean isShowPhone(String provinceFlag) {
        if (!org.apache.commons.lang.StringUtils.isBlank(provinceFlag) && "sd".equalsIgnoreCase(provinceFlag)) {
            return true;
        }
        return false;
    }

    /**
     * 导出企业CSV
     */
    @RequestMapping("/exportSdChargeRecords")
    public void exportSdChargeRecords(HttpServletRequest request, HttpServletResponse response, Long managerId,
            Long adminId) {
        //Map<String, Object> paramsMap = new HashMap<String, Object>();
        QueryObject queryObject = new QueryObject();

        setQueryParameter("name", queryObject);
        setQueryParameter("code", queryObject);
        setQueryParameter("phone", queryObject);

        if (!StringUtils.isEmpty(getRequest().getParameter("startTime"))) {
            queryObject.getQueryCriterias().put("startTime", getRequest().getParameter("startTime"));
            queryObject.getQueryCriterias().put("endTime", getRequest().getParameter("endTime"));
        }
        List<SdAccApprovalRequest> list = approvalRequestService.querySdAccountChangeRecord(queryObject, adminId,
                ApprovalType.Account_Change_Approval.getCode());

        //填充地区，状态
        for (SdAccApprovalRequest sdApprovalRequest : list) {
            ApprovalRequest approvalRequest = sdApprovalRequest.convertToApprovalRequest();
            String description;

            if (ProductType.PRE_PAY_CURRENCY.getValue() == sdApprovalRequest.getProductType().byteValue()) {
                description = "上游同步完成";
            } else {
                description = approvalRequestService.getCurrentStatus(approvalRequest);
            }
            sdApprovalRequest.setDescription(description);

            //地区全称
            Manager entManager = entManagerService.getManagerForEnter(approvalRequest.getEntId());
            if (entManager != null) {
                String fullname = "";
                sdApprovalRequest.setDistrictName(managerService.getFullNameByCurrentManagerId(fullname,
                        entManager.getParentId()));
            }
        }

        creatSDCSVfile(request, response, list);
    }

    /**
     * 导出山东充值CSV
     */
    @RequestMapping("/creatCSVfile")
    public void creatSDCSVfile(HttpServletRequest request, HttpServletResponse response, List<SdAccApprovalRequest> list) {
        List<String> title = new ArrayList<String>();
        List<String> rowList = new ArrayList<String>();

        title.add("企业名称");
        title.add("集团编码");
        title.add("所属地区");
        title.add("账户名称");
        title.add("充值时间");
        title.add("充值额度");
        title.add("状态");
        title.add("Pkgseq");
        title.add("AcctSeq");
        title.add("AcctID");

        DecimalFormat df = new DecimalFormat("0.00");
        for (SdAccApprovalRequest req : list) {
            rowList.add(org.apache.commons.lang.StringUtils.trimToEmpty(req.getEntName()));
            rowList.add(org.apache.commons.lang.StringUtils.trimToEmpty(req.getEntCode()));
            rowList.add(org.apache.commons.lang.StringUtils.trimToEmpty(req.getDistrictName()));
            if (ProductType.PRE_PAY_CURRENCY.getValue() == req.getProductType().byteValue()) {
                rowList.add("预付费资金账户");
            } else if (ProductType.CURRENCY.getValue() == req.getProductType().byteValue()) {
                rowList.add("资金账户");
            } else if (ProductType.FLOW_ACCOUNT.getValue() == req.getProductType().byteValue()) {
                rowList.add("流量池账户");
            } else {
                rowList.add("");
            }

            if (req.getCreateTime() != null) {
                rowList.add(new DateTime(req.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));
            } else {
                rowList.add("");
            }

            if (req.getCount() != null) {
                rowList.add(df.format(req.getCount()) + "元");
            } else {
                rowList.add("");
            }
            rowList.add(org.apache.commons.lang.StringUtils.trimToEmpty(req.getDescription()));
            rowList.add(org.apache.commons.lang.StringUtils.trimToEmpty(req.getPkgSeq()));
            rowList.add(org.apache.commons.lang.StringUtils.trimToEmpty(req.getAcctSeq()));
            rowList.add(org.apache.commons.lang.StringUtils.trimToEmpty(req.getAcctId()));

        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "sdChargeRecord.csv");
            byte[] b = new byte[100];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inputStream.close();
        } catch (Exception e1) {
            logger.error(e1.getMessage());
        }
    }

    /**
     * 导出企业CSV
     */
    @RequestMapping("/exportChargeRecords")
    public void exportChargeRecords(HttpServletRequest request, HttpServletResponse response, Long managerId,
            Long adminId) {
        //Map<String, Object> paramsMap = new HashMap<String, Object>();
        QueryObject queryObject = new QueryObject();

        setQueryParameter("name", queryObject);
        if (!StringUtils.isEmpty(getRequest().getParameter("phone"))) {
            queryObject.getQueryCriterias().put("code", getRequest().getParameter("phone"));
        }

        if (!StringUtils.isEmpty(getRequest().getParameter("startTime"))) {
            queryObject.getQueryCriterias().put("startTime", getRequest().getParameter("startTime"));
            queryObject.getQueryCriterias().put("endTime", getRequest().getParameter("endTime"));
        }
        List<ApprovalRequest> list = approvalRequestService.queryRecordForAccountChange(queryObject, adminId,
                ApprovalType.Account_Change_Approval.getCode());

        //填充地区，状态
        for (ApprovalRequest approvalRequest : list) {
            String description = approvalRequestService.getCurrentStatus(approvalRequest);
            approvalRequest.setDescription(description);

            //地区全称
            Manager entManager = entManagerService.getManagerForEnter(approvalRequest.getEntId());
            if (entManager != null) {
                String fullname = "";
                approvalRequest.setDistrictName(managerService.getFullNameByCurrentManagerId(fullname,
                        entManager.getParentId()));
            }
        }

        creatCSVfile(request, response, list);
    }

    /**
     * 导出充值CSV
     */
    @RequestMapping("/exportCSVfile")
    public void creatCSVfile(HttpServletRequest request, HttpServletResponse response, List<ApprovalRequest> list) {
        List<String> title = new ArrayList<String>();
        List<String> rowList = new ArrayList<String>();

        if (getProvinceFlag().equals("gansu")) {
            title.add("优惠类型");
            title.add("优惠值");
        }
        title.add("企业名称");
        title.add("集团编码");
        title.add("所属地区");
        title.add("账户名称");
        title.add("充值时间");
        title.add("充值额度");
        title.add("状态");

        DecimalFormat df = new DecimalFormat("0.00");
        for (ApprovalRequest req : list) {
            if (getProvinceFlag().equals("gansu")) {
                if (req.getDiscountType() != null) {
                    if (req.getDiscountType() == 1) {
                        rowList.add("存送比例");
                        rowList.add(org.apache.commons.lang.StringUtils.trimToEmpty(String.valueOf(req
                                .getDiscountValue())) + "%");
                    } else {
                        rowList.add("无");
                        rowList.add("无");
                    }
                } else {
                    rowList.add("");
                    rowList.add("");
                }
            }
            rowList.add(org.apache.commons.lang.StringUtils.trimToEmpty(req.getEntName()));
            rowList.add(org.apache.commons.lang.StringUtils.trimToEmpty(req.getEntCode()));
            rowList.add(org.apache.commons.lang.StringUtils.trimToEmpty(req.getDistrictName()));
            if (ProductType.PRE_PAY_CURRENCY.getValue() == req.getProductType().byteValue()) {
                rowList.add("预付费资金账户");
            } else if (ProductType.CURRENCY.getValue() == req.getProductType().byteValue()) {
                rowList.add("资金账户");
            } else if (ProductType.FLOW_ACCOUNT.getValue() == req.getProductType().byteValue()) {
                rowList.add("流量池账户");
            } else {
                rowList.add("");
            }

            if (req.getCreateTime() != null) {
                rowList.add(new DateTime(req.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));
            } else {
                rowList.add("");
            }

            if (req.getCount() != null) {
                rowList.add(df.format(req.getCount()) + "元");
            } else {
                rowList.add("");
            }
            rowList.add(req.getDescription());

        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "ChargeRecord.csv");
            byte[] b = new byte[100];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inputStream.close();
        } catch (Exception e1) {
            logger.error(e1.getMessage());
        }
    }

    /**
     * 
     * @Title: isShowOperator 
     * @Description: 是否显示操作列：山东省级管理员不显示操作列
     * @param roleId
     * @param provinceFlag
     * @return
     * @return: boolean
     */
    private boolean isShowOperator(String roleId, String provinceFlag) {
        if (org.apache.commons.lang.StringUtils.isNotBlank(roleId)
                && org.apache.commons.lang.StringUtils.isNotBlank(provinceFlag)
                && roleId.equalsIgnoreCase(getProvinceManager()) && "sd".equalsIgnoreCase(provinceFlag)) {
            return false;
        }
        return true;
    }

    /**
     * 是否允许负数充值
     */
    private boolean isAllowNegetiveCharge() {
        return "true".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_CHARGE_ALLOW_NEGATIVE
                .getKey()));
    }

}
