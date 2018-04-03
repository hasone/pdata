package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.SyncAccountResult;
import com.cmcc.vrp.boss.gd.model.Response;
import com.cmcc.vrp.boss.gd.service.QryECSyncInfoService;
import com.cmcc.vrp.boss.sichuan.model.SCCancelRequest;
import com.cmcc.vrp.boss.sichuan.service.SCBalanceService;
import com.cmcc.vrp.boss.sichuan.service.SCCancelService;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityStatus;
import com.cmcc.vrp.enums.ApprovalRequestStatus;
import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.enums.EnterpriseStatus;
import com.cmcc.vrp.enums.InterfaceStatus;
import com.cmcc.vrp.enums.MessageType;
import com.cmcc.vrp.enums.SmsType;
import com.cmcc.vrp.province.dao.AdminManagerEnterMapper;
import com.cmcc.vrp.province.dao.CustomerTypeMapper;
import com.cmcc.vrp.province.dao.DiscountMapper;
import com.cmcc.vrp.province.dao.DistrictMapper;
import com.cmcc.vrp.province.dao.EnterpriseFileMapper;
import com.cmcc.vrp.province.dao.GiveMoneyMapper;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.CustomerType;
import com.cmcc.vrp.province.model.Discount;
import com.cmcc.vrp.province.model.District;
import com.cmcc.vrp.province.model.ECSyncInfo;
import com.cmcc.vrp.province.model.EcApprovalDetail;
import com.cmcc.vrp.province.model.EntCallbackAddr;
import com.cmcc.vrp.province.model.EntECRecord;
import com.cmcc.vrp.province.model.EntStatusRecord;
import com.cmcc.vrp.province.model.EntWhiteList;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterpriseFile;
import com.cmcc.vrp.province.model.EnterpriseSmsTemplate;
import com.cmcc.vrp.province.model.EnterprisesExtInfo;
import com.cmcc.vrp.province.model.GiveMoney;
import com.cmcc.vrp.province.model.GiveMoneyEnter;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SmsTemplate;
import com.cmcc.vrp.province.security.MySessionListener;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.AdminEnterService;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.AuthorityService;
import com.cmcc.vrp.province.service.DistrictService;
import com.cmcc.vrp.province.service.ECSyncInfoService;
import com.cmcc.vrp.province.service.EcApprovalDetailService;
import com.cmcc.vrp.province.service.EntCallbackAddrService;
import com.cmcc.vrp.province.service.EntECRecordService;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EntStatusRecordService;
import com.cmcc.vrp.province.service.EntWhiteListService;
import com.cmcc.vrp.province.service.EnterpriseFileService;
import com.cmcc.vrp.province.service.EnterpriseSmsTemplateService;
import com.cmcc.vrp.province.service.EnterpriseUserIdService;
import com.cmcc.vrp.province.service.EnterprisesExtInfoService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.FileStoreService;
import com.cmcc.vrp.province.service.GiveMoneyEnterService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.HistoryEnterprisesService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.province.service.PotentialCusterService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.RoleService;
import com.cmcc.vrp.province.service.S3Service;
import com.cmcc.vrp.province.service.SendMsgService;
import com.cmcc.vrp.province.service.SmsTemplateService;
import com.cmcc.vrp.province.sms.login.LoginSmsPojo;
import com.cmcc.vrp.province.sms.login.SmsRedisListener;
import com.cmcc.vrp.util.CSVUtil;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.PropertyValidator;
import com.cmcc.vrp.util.Provinces;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.wx.impl.GdZcBossServiceImpl;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

/**
 * @author wujiamin
 * @date 2016年10月17日下午5:19:54
 */
@Controller
@RequestMapping("manage/enterprise")
public class EnterpriseController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(EnterpriseController.class);
    private final String configKey = "RANDOMPASS_CHECK";//数据库中globalconfig的随机验证码验证key值
    private final String checkPassKey = "OK";//globalconfig需要检验随机密码的value值

    protected String customerFilePath;//客户说明文件目录
    protected String contractPath;//合同文件目录
    protected String imagePath;//截图文件目录 

    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    AdminEnterService adminEnterService;
    @Autowired
    PhoneRegionService phoneRegionService;
    @Autowired
    EntProductService entProductService;
    @Autowired
    AccountService accountService;
    @Autowired
    GiveMoneyMapper giveMoneyMapper;
    @Autowired
    GiveMoneyEnterService giveMoneyEnterService;
    @Autowired
    SmsRedisListener smsRedisListener;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    DistrictMapper districtMapper;
    @Autowired
    CustomerTypeMapper customerTypeMapper;
    @Autowired
    AdministerService administerService;
    @Autowired
    EnterpriseFileService enterpriseFileService;
    @Autowired
    EnterpriseUserIdService enterpriseUserIdService;
    @Autowired
    AdminManagerEnterMapper adminManagerEnterMapper;
    @Autowired
    DiscountMapper discountMapper;
    @Autowired
    SCCancelService scCancelService;
    @Autowired
    EnterpriseSmsTemplateService enterpriseSmsTemplateService;
    @Autowired
    SmsTemplateService smsTemplateService;
    @Autowired
    ProductService productService;
    @Autowired
    DistrictService districtService;
    @Autowired
    EnterpriseFileMapper enterpriseFileMapper;
    @Autowired
    ManagerService managerService;
    @Autowired
    AdminManagerService adminManagerService;
    @Autowired
    EntManagerService entManagerService;
    @Autowired
    ApprovalRecordService approvalRecordService;

    @Autowired
    FileStoreService fileStoreService;

    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Autowired
    AuthorityService authorityService;
    @Autowired
    PotentialCusterService potentialCusterService;
    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    S3Service s3Service;
    @Autowired
    EntCallbackAddrService entCallbackAddrService;
    @Autowired
    EntWhiteListService entWhiteListService;
    @Autowired
    ApprovalRequestService approvalRequestService;
    @Autowired
    EcApprovalDetailService ecApprovalDetailService;
    @Autowired
    HistoryEnterprisesService historyEnterprisesService;

    @Autowired
    SCBalanceService scBalanceService;
    @Autowired
    EnterprisesExtInfoService enterprisesExtInfoService;
    @Autowired
    @Qualifier("huNanBossService")
    private BossService bossService;

    @Autowired
    EntECRecordService entECRecordService;

    @Autowired
    EntStatusRecordService entStatusRecordService;

    @Autowired
    RoleService roleService;
    
    @Autowired
    ECSyncInfoService ecSyncInfoService;

    @Autowired
    QryECSyncInfoService qryECSyncInfoService;
    
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    SendMsgService sendMsgService;

    /**
     * @param queryObject 前台产来的参数，包括PageNum和pageSize
     * @return PageResult<Enterprise>
     * @Title: generatePageResult
     * @Description: 产生经过数据库查询操作之后的pageResult对象
     * @author qihang
     */
    @RequestMapping("index")
    public String enterpriseIndex(ModelMap model, QueryObject queryObject) {

        //管理员层级筛选
        //根节点为当前用户的管理员层级
        Manager manager = getCurrentUserManager();
        if (manager == null) {
            model.put("errorMsg", "当前用户无管理员身份");
            return "error.ftl";
        }

        //重庆、新疆、山东企业列表不显示合作开始时间、合作结束时间、折扣信息
        if ("chongqing".equals(getProvinceFlag()) || "xj".equals(getProvinceFlag()) || "sd".equals(getProvinceFlag())) {
            model.addAttribute("cqFlag", 1);
        } else {
            model.addAttribute("cqFlag", 0);
        }

        //平台账户是否为空账户的标识
        if (accountService.isEmptyAccount()) {
            model.addAttribute("isEmptyAccount", true);
        }

        model.addAttribute("managerId", manager.getId());

        model.addAttribute("currentUserRoleId", getCurrentUser().getRoleId());

        String provinceFlag = globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey());
        model.addAttribute("provinceFlag", provinceFlag);

        //是否是过期的企业
        if (getSession().getAttribute("expire") != null) {
            String expire = (String) getSession().getAttribute("expire");
            if ("true".equals(expire)) {
                model.addAttribute("expire", expire);
                //session中删除expire
                getSession().removeAttribute("expire");
            }
        }

        //使用企业关键人企业列表中的更新，还是用户更改中的修改和提交审核
        String isUseEnterList = globalConfigService.get(GlobalConfigKeyEnum.ADMIN_CHANGE_USE_ENTERLIST.getKey());
        if ("true".equals(isUseEnterList)) {
            model.addAttribute("isUseEnterList", "true");
        } else {
            model.addAttribute("isUseEnterList", "false");
        }

        //四川有一键同步按钮
        if ("sc".equals(getProvinceFlag())) {
            model.addAttribute("synchronizeAccount", "1");
        }

        model.addAttribute("back",queryObject.getBack());//增加返回标识
        if ("gd_mdrc".equals(getProvinceFlag())) { //广东流量卡
            if ("YES".equals(globalConfigService.get(GlobalConfigKeyEnum.IS_CROWDFUNDING_PLATFORM.getKey()))) {
                model.addAttribute("gdzcFlag", "true");
            }
            return "enterprises/index_gd.ftl";
        } else {
            return "enterprises/index.ftl";
        }
    }

    /**
     * 异步请求企业的余额
     *
     * @param entIds 企业ID列表,以逗号分隔
     */
    @RequestMapping(value = "accountAjax", method = RequestMethod.GET)
    public void queryAccountAjax(String entIds, HttpServletResponse httpServletResponse) {
        if (StringUtils.isBlank(entIds)) {
            return;
        }

        //获取企业现金账户余额
        List<Long> ids = parse(entIds);
        Map<Long, Double> accounts = enterprisesService.queryAccounts(ids);
        if (accounts == null || accounts.isEmpty()) {
            return;
        }

        try {
            StreamUtils.copy(new Gson().toJson(accounts), Charsets.UTF_8, httpServletResponse.getOutputStream());
        } catch (IOException e) {
            logger.error("输出企业余额响应时出错,错误信息为{}, 错误堆栈为{}.", e.getMessage(), e.getStackTrace());
        }
    }

    private List<Long> parse(String idsStr) {
        String[] ids = idsStr.split(",");
        List<Long> idList = new LinkedList<Long>();

        for (String id : ids) {
            idList.add(NumberUtils.toLong(id, -1));
        }

        return idList;
    }

    /**
     * @Title: 企业列表查找
     * @Author: wujiamin
     * @date 2016年10月17日下午5:20:02
     */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        String managerIdStr = getRequest().getParameter("managerId");
        if (StringUtils.isNotBlank(managerIdStr)) {
            Long managerId = NumberUtils.toLong(managerIdStr);
            if (!isParentOf(managerId)) { //当前用户不是指定用户或不是它的父节点，没有权限查看相应的节点信息
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
        setQueryParameter("beginTime", queryObject);
        setQueryParameter("endTime", queryObject);

        if (needExtEntInfo()) {
            queryObject.getQueryCriterias().put("extEntInfoFlag", "true");
        }

        /**
         * 当前登录用户的管理员层级
         */
        Manager manager = getCurrentUserManager();
        queryObject.getQueryCriterias().put("managerId", manager.getId());

        //页面查询参数中设定的管理员层级，如果设定了就会将上面的值覆盖
        setQueryParameter("managerId", queryObject);

        // 数据库查找符合查询条件的个数
        int enterpriseCount = enterprisesService.showForPageResultCount(queryObject);
        List<Enterprise> enterpriseList = enterprisesService.showEnterprisesForPageResult(queryObject);

        if (enterpriseList != null) {
            for (Enterprise enterprise : enterpriseList) {            
                //地区全称
                Manager districtName = entManagerService.getManagerForEnter(enterprise.getId());
                if (districtName != null) {
                    String fullname = "";
                    enterprise.setCmManagerName(managerService.getFullNameByCurrentManagerId(fullname,
                            districtName.getParentId()));
                }
                
                //四川获取企业的202
                if("sc".equals(getProvinceFlag())){
                    String userId = enterpriseUserIdService.getUserIdByEnterpriseCode(enterprise.getCode());
                    enterprise.setScUserId(userId==null?"":userId);
                }
                
                //企业信息模糊处理
                enterprisesService.blurEnterpriseInfo(enterprise);
            }
        }

        //是否展示余额字段
        addCurrentCountIfNecessary(enterpriseList);

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

    //为所有的企业对象增加现金账户余额
    private void addCurrentCountIfNecessary(List<Enterprise> ents) {
        //只有平台账户不是空账户的时候,才需要查询平台的现金账户
        if (!accountService.isEmptyAccount()) {
            //获取企业id
            List<Long> entIds = new LinkedList<Long>();
            for (Enterprise ent : ents) {
                entIds.add(ent.getId());
            }

            //获取企业现金账户
            Map<Long, Double> accounts = enterprisesService.queryAccounts(entIds);
            if (accounts == null || accounts.isEmpty()) {
                return;
            }

            //回填
            for (Enterprise ent : ents) {
                ent.setCurrencyCount(accounts.get(ent.getId()));
            }
        }
    }

    /**
     * 查看企业具体信息
     */
    @RequestMapping(value = "/showEnterpriseDetail", method = RequestMethod.GET)
    public String showEnterpriseDetail(ModelMap map, Integer changeTag, Integer cqSync, Enterprise enterprise) {
        // Id不为空，认为是编辑功能
        if (enterprise.getId() != null) {
            Long enterpriseId = enterprise.getId();
            enterprise = enterprisesService.selectByPrimaryKey(enterpriseId);
            if (enterprise == null) {
                map.addAttribute("errorMsg", "数据库中没有相关企业Id: " + enterpriseId);
                return "error.ftl";
            }
        }

        if (getProvinceFlag().equals("chongqing")) {
            map.addAttribute("cqFlag", 1);
        } else {
            map.addAttribute("cqFlag", 0);
        }

        //用来标记该详情页是否有企业信息变更的按钮
        if (changeTag != null && changeTag == 1) {
            map.addAttribute("changeTag", 1);
        }
        if (cqSync != null && cqSync == 1) {
            map.addAttribute("cqSync", 1);
        } else {
            map.addAttribute("cqSync", 0);
        }
        if (!hasAuthority(enterprise.getId())) {
            map.addAttribute("errorMsg", "当前用户无查看权限");
            return "error.ftl";
        }

        // 业务分类选项

        // 流量类型选项

        // 产品类型选项

        // 折扣选项

        // EC开通选项
        
        //企业信息模糊处理
        enterprisesService.blurEnterpriseInfo(enterprise);
        
        map.addAttribute("Enterprises", enterprise);
        //获取企业管理员
        List<Administer> enterpriseManagers = adminManagerService.getAdminForEnter(enterprise.getId());
        
        //企业管理员信息模糊处理
        if(enterpriseManagers != null && enterpriseManagers.size() > 0){
            for(Administer administer : enterpriseManagers){
                administerService.blurAdministerInfo(administer);
            }
        }
        map.addAttribute("enterpriseManagers", enterpriseManagers);

        //获取客户经理
        /*        List<Administer> customerManagers = adminManagerService.getCustomerManagerByEntId(enterprise.getId());
                map.addAttribute("customerManagers", customerManagers);*/
        //        Administer customerManager = administerService.selectByMobilePhone(enterprise.getCmPhone());
        Administer customerManager = null;
        String customManagerID = globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey());
        if (org.apache.commons.lang.StringUtils.isNotEmpty(customManagerID)) {
            Long customManagerId = Long.parseLong(customManagerID);
            List<Manager> managers = managerService.selectEntParentNodeByEnterIdOrRoleId(enterprise.getId(),
                    customManagerId);
            if (managers != null && !managers.isEmpty()) {
                List<Administer> administers = adminManagerService.getAdminByManageId(managers.get(0).getId());
                if (administers != null && !administers.isEmpty()) {
                    customerManager = administers.get(0);
                }
            }
        }
        if (customerManager == null) {
            customerManager = new Administer();
        }
        //模糊化处理
        administerService.blurAdministerInfo(customerManager);
        map.addAttribute("customerManager", customerManager);

        //地区全称
        Manager manager = entManagerService.getManagerForEnter(enterprise.getId());
        if (manager != null) {
            String fullname = "";
            map.addAttribute("fullDistrictName",
                    managerService.getFullNameByCurrentManagerId(fullname, manager.getParentId()));
        }

        /**
         * 显示附件文件名称
         */
        map.addAttribute("enterpriseFile", enterpriseFileService.selectByEntId(enterprise.getId()));

        /**
         * 显示企业余额
         */
        Product product = productService.getCurrencyProduct();
        if (enterprise != null && product != null) {
            Account account = accountService
                    .get(enterprise.getId(), product.getId(), AccountType.ENTERPRISE.getValue());
            map.addAttribute("account", account);
        } else {
            map.addAttribute("account", null);
        }

        //传递标识：是否是自营的企业
        if (getProvinceFlag().equals(zyProvinceFlagValue)) {
            map.addAttribute("flag", 1);
        } else {
            map.addAttribute("flag", 0);
        }

        //获取信用额度值
        Account account = accountService.get(enterprise.getId(), productService.getCurrencyProduct().getId(),
                AccountType.ENTERPRISE.getValue());
        if (account != null) {
            Double minCount = account.getMinCount() / (-100.0);
            if (account.getMinCount() == 0) {
                minCount = 0D;
            }
            map.addAttribute("minCount", minCount);
        }

        //合作时间 是否过期
        if (enterprise.getEndTime() != null && enterprise.getEndTime().before(new Date())) {
            map.addAttribute("contractExpire", "true");
        }
        //营业执照是否过期
        if (enterprise.getLicenceEndTime() != null && enterprise.getLicenceEndTime().before(new Date())) {
            map.addAttribute("licenceExpire", "true");
        }

        //判断是否有企业信息变更审核权限
        Boolean authFlag = approvalProcessDefinitionService.hasAuthToSubmitApproval(
                getCurrentUserManager().getRoleId(), ApprovalType.Ent_Information_Change_Approval.getCode());
        Boolean hasApprovalRecordFlag = historyEnterprisesService.hasApprovalRecord(enterprise.getId());
        map.addAttribute("hasApprovalRecordFlag", hasApprovalRecordFlag.toString());
        map.addAttribute("authFlag", authFlag.toString());
        //下线企业不能修改企业信息
        Boolean enterStatus = true;
        if (enterprise.getDeleteFlag().toString().equals(EnterpriseStatus.OFFLINE.getCode().toString())) {
            enterStatus = false;
        }
        map.addAttribute("enterStatus", enterStatus.toString());

        if ("gd_mdrc".equals(getProvinceFlag())) { //广东流量卡
            map.addAttribute("crowdfundingFlag", getIsCrowdfundingPlatform());
            EnterprisesExtInfo enterprisesExtInfo = enterprisesExtInfoService.get(enterprise.getId());
            map.put("extEntInfo", enterprisesExtInfo);

            return "enterprises/detail_gd.ftl";
        } else {
            return "enterprises/detail.ftl";
        }
    }

    /**
     * 变更企业状态
     *
     * @Title: changeEnterpriseStatusAjax
     * @Author: wujiaminl
     * @date 2016年10月17日下午5:22:05
     */
    @RequestMapping(value = "/changeEnterpriseStatusAjax")
    public void changeEnterpriseStatusAjax(Long id, int status, String opDesc, String reason, HttpServletResponse response) {
        Map result = new HashMap();
        if (id == null || StringUtils.isBlank(opDesc)) {
            try {
                logger.info("参数缺失，操作失败！");
                result.put("msg", "参数缺失，操作失败！");
                response.getWriter().write(JSONObject.toJSONString(result));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            return;
        }

        String msg = "fail";
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(id);
        if (enterprise == null) {
            try {
                logger.info("更新企业状态失败,企业不存在,id=" + id);
                result.put("msg", "fail");
                response.getWriter().write(JSONObject.toJSONString(result));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            return;
        }

        //生成企业状态变更记录
        EntStatusRecord entStatusRecord = new EntStatusRecord();
        entStatusRecord.setOperatorId(getCurrentAdminID());//操作者
        entStatusRecord.setOperatorMobile(getCurrentUser().getMobilePhone());//手机号码
        entStatusRecord.setOperatorName(getCurrentUser().getUserName());//操作者姓名
        entStatusRecord.setOperatorRole(roleService.getRoleById(
                managerService.selectByAdminId(getCurrentAdminID()).getRoleId()).getName());//操作者角色
        entStatusRecord.setEntId(id);//企业
        entStatusRecord.setCreateTime(new Date());
        entStatusRecord.setUpdateTime(new Date());
        entStatusRecord.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        entStatusRecord.setPreStatus(enterprise.getDeleteFlag());//企业先前状态，注：enterprise中deleteFlag表企业状态

        //生成企业EC接口变更记录
        EntECRecord entECRecord = new EntECRecord();
        entECRecord.setOperatorId(getCurrentAdminID());
        entECRecord.setOperatorMobile(getCurrentUser().getMobilePhone());//手机号码
        entECRecord.setOperatorName(getCurrentUser().getUserName());//操作者姓名
        entECRecord.setOperatorRole(roleService.getRoleById(
                managerService.selectByAdminId(getCurrentAdminID()).getRoleId()).getName());//操作者角色
        entECRecord.setEntId(id);
        entECRecord.setPreStatus(enterprise.getInterfaceFlag());
        entECRecord.setCreateTime(new Date());
        entECRecord.setUpdateTime(new Date());
        entECRecord.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        boolean ecRecordFlag = false;

        String operType = "";//记录是暂停还是开通
        if (status == 2) {//暂停操作,前提：企业处于开通状态
            if (!EnterpriseStatus.NORMAL.getCode().equals(enterprise.getDeleteFlag())) {
                String tipMsg = "";
                if (EnterpriseStatus.PAUSE.getCode().equals(enterprise.getDeleteFlag())) {//企业已暂停
                    tipMsg = "企业已暂停，操作失败！";
                } else if (EnterpriseStatus.OFFLINE.getCode().equals(enterprise.getDeleteFlag())) {//企业已下线
                    tipMsg = "企业已下线，操作失败！";
                } else {
                    tipMsg = "企业状态异常，操作失败！";
                }
                try {
                    result.put("msg", tipMsg);
                    response.getWriter().write(JSONObject.toJSONString(result));
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
                return;
            }

            enterprise.setDeleteFlag(EnterpriseStatus.PAUSE.getCode());//暂停
            if (enterprise.getInterfaceFlag().equals(InterfaceStatus.OPEN.getCode())) {//企业暂停时，如果企业EC接口开通，则关闭企业EC接口，生成EC操作记录               
                entECRecord.setOpType(InterfaceStatus.CLOSE.getCode());
                entECRecord.setOpDesc("企业暂停，EC同步关闭");
                enterprise.setInterfaceFlag(InterfaceStatus.CLOSE.getCode());//关闭EC
                entECRecord.setNowStatus(enterprise.getInterfaceFlag());
                ecRecordFlag = true;
            }
            operType = "暂停";
        }
        if (status == 0) {//开通操作，前提：企业处于暂停状态
            if (!EnterpriseStatus.PAUSE.getCode().equals(enterprise.getDeleteFlag())) {
                String tipMsg = "";
                if (EnterpriseStatus.NORMAL.getCode().equals(enterprise.getDeleteFlag())) {//企业已暂停
                    tipMsg = "企业已开通，操作失败！";
                } else if (EnterpriseStatus.OFFLINE.getCode().equals(enterprise.getDeleteFlag())) {//企业已下线
                    tipMsg = "企业已下线，操作失败！";
                } else {
                    tipMsg = "企业状态异常，操作失败！";
                }
                try {
                    result.put("msg", tipMsg);
                    response.getWriter().write(JSONObject.toJSONString(result));
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
                return;
            }

            enterprise.setDeleteFlag(EnterpriseStatus.NORMAL.getCode());//开通，企业状态恢复正常
            operType = "开通";
            if (enterprise.getEndTime() != null && new Date().after(enterprise.getEndTime())) {
                logger.info("企业合同已结束，无法上线开通,用户ID:" + getCurrentUser().getId() + operType + "企业Id:" + id + "失败");
                try {
                    result.put("msg", "fail");
                    response.getWriter().write(JSONObject.toJSONString(result));
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
                return;
            }
            if (enterprise.getLicenceEndTime() != null && new Date().after(enterprise.getLicenceEndTime())) {
                logger.info("企业营业执照到期时间早于当前时间，无法上线开通,用户ID:" + getCurrentUser().getId() + operType + "企业Id:" + id + "失败");
                try {
                    result.put("msg", "fail");
                    response.getWriter().write(JSONObject.toJSONString(result));
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
                return;
            }
        }
        if (status == 3) {//下线操作，前提：企业未下线            
            if (EnterpriseStatus.OFFLINE.getCode().equals(enterprise.getDeleteFlag())) {//企业已下线
                try {
                    result.put("msg", "企业已下线，操作失败！");
                    response.getWriter().write(JSONObject.toJSONString(result));
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
                return;
            }

            if ("sc".equals(getProvinceFlag())) {
                SCCancelRequest request = new SCCancelRequest();
                String eUserId = enterpriseUserIdService.getUserIdByEnterpriseCode(enterprise.getCode());
                request.setPhoneNo(eUserId);
                if (scCancelService.sendCancelRequest(request)) {
                    enterprise.setDeleteFlag(EnterpriseStatus.OFFLINE.getCode());
                    enterprise.setAppKey("");
                    enterprise.setAppSecret("");
                    operType = "下线";

                    if (InterfaceStatus.OPEN.getCode() == enterprise.getInterfaceFlag().intValue()) {//企业暂停时，如果企业EC接口开通，则关闭企业EC接口，生成EC操作记录
                        entECRecord.setOpType(InterfaceStatus.CLOSE.getCode());//关闭EC
                        enterprise.setInterfaceFlag(InterfaceStatus.CLOSE.getCode());//关闭EC
                        entECRecord.setOpDesc("企业下线，EC同步关闭");//关闭EC
                        entECRecord.setNowStatus(enterprise.getInterfaceFlag());//关闭EC
                        ecRecordFlag = true;
                    }
                } else {
                    logger.info("下线失败！");
                    try {
                        result.put("msg", "fail");
                        response.getWriter().write(JSONObject.toJSONString(result));
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                    }
                    return;
                }
            } else {
                enterprise.setDeleteFlag(EnterpriseStatus.OFFLINE.getCode());
                enterprise.setAppKey("");
                enterprise.setAppSecret("");
                operType = "下线";

                if (InterfaceStatus.OPEN.getCode() == enterprise.getInterfaceFlag().intValue()) {////企业暂停时，如果企业EC接口开通，则关闭企业EC接口，生成EC操作记录
                    entECRecord.setOpType(InterfaceStatus.CLOSE.getCode());//关闭EC
                    enterprise.setInterfaceFlag(InterfaceStatus.CLOSE.getCode());//关闭EC
                    entECRecord.setOpDesc("企业下线，EC同步关闭");//关闭EC
                    entECRecord.setNowStatus(enterprise.getInterfaceFlag());//关闭EC
                    ecRecordFlag = true;
                }
            }
        }

        entStatusRecord.setNowStatus(enterprise.getDeleteFlag());
        entStatusRecord.setOpType(enterprise.getDeleteFlag());
        entStatusRecord.setOpDesc(opDesc);
        if (!StringUtils.isEmpty(reason)) {
            if ("1".equals(reason)) {
                reason = "集团通报";
            } else if ("2".equals(reason)) {
                reason = "省公司通报";
            } else if ("3".equals(reason)) {
                reason = "合同到期";               
            } else if ("4".equals(reason)) {
                reason = "客户要求";
            } else if ("5".equals(reason)) {
                reason = "其他";
            } else {
                reason = "-";
            }
            entStatusRecord.setReason(reason);
        }

        if (!entStatusRecordService.insert(entStatusRecord)) {
            logger.info("企业状态变更记录生成失败：" + new Gson().toJson(entStatusRecord));
        }

        if (ecRecordFlag) {//生成EC操作记录
            if (!entECRecordService.insert(entECRecord)) {
                logger.info("企业EC变更记录生成失败：" + new Gson().toJson(entStatusRecord));
            }
        }

        if (enterprisesService.updateByPrimaryKeySelective(enterprise)) {
            logger.info("用户ID:" + getCurrentUser().getId() + operType + "企业Id:" + id + "成功");

            //如果企业是下线，则下架所有活动
            if (enterprise.getDeleteFlag().toString().equals(EnterpriseStatus.OFFLINE.getCode().toString())) {
                Map map = queryMap(enterprise.getId());
                List<Activities> activities = activitiesService.selectByEntId(map);
                if (activitiesService.batchChangeStatus(activities, ActivityStatus.DOWN.getCode())) {
                    //将所有活动下架
                    activitiesService.batchDownShelf(activities);
                    logger.info("企业-" + enterprise.getId() + "活动下架成功");
                } else {
                    logger.info("企业-" + enterprise.getId() + "活动下架失败");
                }
            }
            msg = "操作成功";
            if ("gd_mdrc".equals(getProvinceFlag())) {
                List<Administer> enAdministers = adminManagerService.getAdminForEnter(id);
                List<Administer> cmAdministers = adminManagerService.getCustomerManagerByEntId(id);
                
                String content = getContent(enterprise, status);
                if (enAdministers != null
                        && enAdministers.size() != 0) {
                    for(Administer administer:enAdministers) {
                        String mobile = administer.getMobilePhone();
                        String email = administer.getEmail();
                        if (!StringUtils.isEmpty(mobile)) {
                            if (!sendMsgService.sendMessage(mobile, content,
                                    MessageType.ENTERPRISE_OPER.getCode())) {
                                logger.info("企业状态变更发送短信失败;手机号-" + mobile);
                            }
                        }
                        if (!StringUtils.isEmpty(email)) {
                            sendMailByLinuxCommand(email, content);
                        }
                    }
                }
                if (cmAdministers != null
                        && cmAdministers.size() != 0) {
                    for(Administer administer:cmAdministers) {
                        String mobile = administer.getMobilePhone();
                        String email = administer.getEmail();
                        if (!StringUtils.isEmpty(mobile)) {
                            if (!sendMsgService.sendMessage(mobile, content,
                                    MessageType.ENTERPRISE_OPER.getCode())) {
                                logger.info("企业状态变更发送短信失败;手机号-" + mobile);
                            }
                        }
                        if (!StringUtils.isEmpty(email)) {
                            sendMailByLinuxCommand(email, content);
                        }
                    }
                }
            }
        } else {
            logger.info("用户ID:" + getCurrentUser().getId() + operType + "企业Id:" + id + "失败");
        }

        try {
            result.put("msg", msg);
            response.getWriter().write(JSONObject.toJSONString(result));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return;
    }
    /**
     * @param mailAddress
     * @param content
     */
    public void sendMailByLinuxCommand(String mailAddress, String content) {
        logger.info("开始发送邮件：邮箱地址：{}, 内容：{}", mailAddress, content);
        String command = "echo \"" + content + "\" | mail -s \"" + content + "\" -r \"" + getMailHost() + "\" " + mailAddress;
        try {
            Runtime r = Runtime.getRuntime();
            System.out.println("command:" + command);
            Process p = r.exec(new String[]{"/usr/bin/bash", "-c", command});
            p.waitFor();
            System.out.println("result:" + p.getOutputStream());
        } catch (InterruptedException ex) {
            System.out.println("InterruptedException:" + ex.getMessage());
           
        } catch (IOException ex) {
            System.out.println("IOException:" + ex.getMessage());
        }

    }
    private String getMailHost(){
        return globalConfigService.get(GlobalConfigKeyEnum.MAIL_ADDRESS.getKey());
    }
    private String getContent(Enterprise enterprise, int status) {
        String platform = null;
        String oper = null;
        if (getIsCrowdfundingPlatform()) {
            platform = "众筹";
        } else {
            platform = "流量卡";
        }
        if (status == 0) {
            oper = "恢复";
        } else if (status == 2) {
            oper = "暂停";
        } else {
            oper = "关闭";
        }
        return "【广东流量平台】您好，" + enterprise.getName() +"的" + platform +"产品已" + oper + "。";
        
    }

    private Map queryMap(Long entId) {
        Map map = new HashedMap();
        map.put("entId", entId);
        List<Integer> status = new ArrayList<Integer>();
        status.add(ActivityStatus.ON.getCode());
        status.add(ActivityStatus.PROCESSING.getCode());
        map.put("status", status);
        return map;
    }

    /**
     * @Title: changeECStatus
     * @Author: wujiamin
     * @date 2016年10月17日下午5:23:16
     */
    @RequestMapping(value = "/changeECStatus")
    public void changeECStatus(Long id, String opDesc, Integer status, HttpServletResponse response,
            HttpServletRequest request) {
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(id);
        Map result = new HashMap();
        if (enterprise == null) {
            result.put("msg", "fail");
            try {
                response.getWriter().write(JSONObject.toJSONString(result));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            return;
        }
        if (enterprise.getDeleteFlag() != 0) {
            result.put("msg", "企业状态异常，无法操作EC!");
            try {
                response.getWriter().write(JSONObject.toJSONString(result));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            return;
        }
        if (StringUtils.isBlank(opDesc) || status == null) {
            result.put("msg", "参数缺失，EC操作失败!");
            try {
                response.getWriter().write(JSONObject.toJSONString(result));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            return;
        }

        //校验操作
        if (enterprise.getInterfaceFlag().intValue() == status) {
            String tipMsg = "";
            if (enterprise.getInterfaceFlag().intValue() == InterfaceStatus.OPEN.getCode()) {
                tipMsg = "EC已开通，操作失败！";
            } else if (enterprise.getInterfaceFlag().intValue() == InterfaceStatus.CLOSE.getCode()) {
                tipMsg = "EC已关闭，操作失败！";
            } else {
                tipMsg = "EC状态异常，操作失败！";
            }
            result.put("msg", tipMsg);
            try {
                response.getWriter().write(JSONObject.toJSONString(result));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            return;
        }

        //生成EC操作记录
        EntECRecord entECRecord = new EntECRecord();
        entECRecord.setOperatorId(getCurrentAdminID());//操作者ID
        entECRecord.setOperatorMobile(getCurrentUser().getMobilePhone());//手机号码
        entECRecord.setOperatorName(getCurrentUser().getUserName());//操作者姓名
        entECRecord.setOperatorRole(roleService.getRoleById(
                managerService.selectByAdminId(getCurrentAdminID()).getRoleId()).getName());//操作者角色
        entECRecord.setEntId(enterprise.getId());//企业ID
        entECRecord.setPreStatus(enterprise.getInterfaceFlag());//操作之前EC状态

        String operType = "";//记录是暂停还是开通
        if (enterprise.getInterfaceFlag().intValue() == InterfaceStatus.OPEN.getCode()) {
            enterprise.setInterfaceFlag(InterfaceStatus.CLOSE.getCode());
            operType = "关闭EC";
        } else if (enterprise.getInterfaceFlag() == InterfaceStatus.CLOSE.getCode()) {
            enterprise.setInterfaceFlag(InterfaceStatus.OPEN.getCode());
            operType = "开通EC";
        } else {
            logger.info("企业Id:" + id + ",interface=" + enterprise.getInterfaceFlag() + ",EC接口状态异常，无法开通或关闭");
            result.put("msg", "EC状态异常，无法操作EC!");
            try {
                response.getWriter().write(JSONObject.toJSONString(result));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            return;
        }

        //生成企业EC操作记录
        entECRecord.setNowStatus(enterprise.getInterfaceFlag());//操作后状态
        entECRecord.setCreateTime(new Date());//创建时间
        entECRecord.setUpdateTime(new Date());//操作时间
        entECRecord.setOpType(enterprise.getInterfaceFlag());
        entECRecord.setOpDesc(opDesc);//描述信息
        entECRecord.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());

        if (enterprisesService.updateByPrimaryKeySelective(enterprise)) {
            result.put("msg", operType + "成功!");
        } else {
            result.put("msg", operType + "失败!");
        }

        if (!entECRecordService.insert(entECRecord)) {
            logger.error("生成EC操作记录失败：" + new Gson().toJson(entECRecord));
        }

        try {
            response.getWriter().write(JSONObject.toJSONString(result));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return;
    }

    /**
     * @Title: check
     * @Author: wujiamin
     * @date 2016年10月17日下午5:23:22
     */
    @RequestMapping(value = "/check")
    public void check(HttpServletResponse response, Enterprise enterprise) throws IOException {
        Boolean bFlag = checkUnique(enterprise);
        response.getWriter().write(bFlag.toString());
    }

    /**
     * @Title: checkNameValid
     * @Author: wujiamin
     * @date 2016年10月17日下午5:23:27
     */
    @RequestMapping(value = "/checkNameValid")
    public void checkNameValid(HttpServletResponse resp, Enterprise enterprise) throws IOException {
        String name = enterprise.getName().replaceAll(" ", "");
        PrintWriter pw = resp.getWriter();

        resp.setContentType("text/xml;charset=utf-8");
        resp.setHeader("Cache-Control", "no-cache");

        if (!Pattern.compile(PropertyValidator.CHINESE_LOWER_UPPER_UNDERLINE_NUMBER_REGEX).matcher(name).matches()) {
            resp.getWriter().write("企业名称只能由汉字、英文字符、下划线或数字组成!");
        } else {
            pw.print("true");
        }
    }

    /**
     * @Title: checkMobileValid
     * @Author: wujiamin
     * @date 2016年10月17日下午5:23:33
     */
    @RequestMapping(value = "/checkMobileValid")
    public void checkMobileValid(HttpServletResponse resp, Enterprise enterprise) throws IOException {

        String mobile = enterprise.getPhone();

        resp.setContentType("text/xml;charset=utf-8");
        resp.setHeader("Cache-Control", "no-cache");

        Boolean bFlag = checkUnique(enterprise);
        PrintWriter pw = resp.getWriter();

        String str = "true";

        if (!bFlag) {
            str = "该手机号码已存在，请重新输入";
            resp.getWriter().write(str);
        } else if (!com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)) {
            str = "非移动手机号码，请重新输入";
            resp.getWriter().write(str);
        } else {
            pw.print("true");
        }
    }

    /**
     * @Title:validateEnterprise
     * @Description: 全量校验企业对象
     * @author: sunyiwei
     */
    private boolean validateEnterprise(ModelMap model, Enterprise enterprise) {
        if (enterprise == null) {
            model.addAttribute("errorMsg", "无效的企业对象!");
            return false;
        }

        try {
            enterprise.selfCheck();
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.toString());
            return false;
        }

        /**
         * 唯一性校验
         */
        if (!checkUnique(enterprise)) {
            model.addAttribute("errorMsg", "企业名称或编码已存在!");
            return false;
        }

        if (enterprise.getStartTime() == null || enterprise.getEndTime() == null) {
            model.addAttribute("errorMsg", "请填写合作开始时间及结束时间!");
            return false;
        }

        if (enterprise.getStartTime().after(enterprise.getEndTime())) {
            model.addAttribute("errorMsg", "合作起始时间晚于结束时间!");
            return false;
        }
        if (enterprise.getEndTime().before(new Date())) {
            model.addAttribute("errorMsg", "合作结束时间早于当前时间!");
            return false;
        }
        if (enterprise.getStartTime().after(new Date())) {
            model.addAttribute("errorMsg", "合作开始时间晚于当前时间!");
            return false;
        }
        return true;
    }

    /**
     * @Title: uniqueCheck
     * @Description: 校验新建或编辑的企业编码与企业名称是否已存在
     */
    private boolean checkUnique(Enterprise enterprise) {
        if (enterprise == null) {
            return true;
        }

        return !enterprisesService.countExists(enterprise.getId(), enterprise.getName(), enterprise.getCode(),
                enterprise.getPhone());
    }

    /**
     * 导出企业CSV
     */
    @RequestMapping("/creatCSVfile")
    public void creatPackageCSVfile(HttpServletRequest request, HttpServletResponse response, String name, String code,
            String managerId, String startTime, String endTime) {

        Map map = new HashMap();

        //        Long roleId = getCurrentUser().getRoleId();
        //        String superAdminRoleId = getSuperAdminRoleId();
        //        String ENTERPRISE_CONTACTOR = getENTERPRISE_CONTACTOR();

        /**
         * 查询参数: 企业名字、编码、效益
         */
        map.put("name", name);
        map.put("code", code);
        map.put("beginTime", startTime);
        map.put("endTime", endTime);

        List<String> deleteFlags = new ArrayList<String>();
        deleteFlags.add(EnterpriseStatus.NORMAL.getCode().toString());
        deleteFlags.add(EnterpriseStatus.PAUSE.getCode().toString());
        deleteFlags.add(EnterpriseStatus.OFFLINE.getCode().toString());
        map.put("deleteFlags", org.apache.commons.lang.StringUtils.join(deleteFlags, ","));

        //管理员层级筛选
        //根节点为当前用户的管理员层级
        Manager manager = getCurrentUserManager();
        if (manager == null) {
            logger.error("当前用户无管理员身份");
            return;
        }
        if (!StringUtils.isEmpty(managerId)) {
            map.put("managerId", managerId);
        } else {
            map.put("managerId", manager.getId().toString());
        }

        List<Enterprise> list = enterprisesService.selectEnterpriseByMap(map);
        if (list != null) {
            for (Enterprise enterprise : list) {
                //地区全称
                Manager districtName = entManagerService.getManagerForEnter(enterprise.getId());
                if (districtName != null) {
                    String fullname = "";
                    enterprise.setCmManagerName(managerService.getFullNameByCurrentManagerId(fullname,
                            districtName.getParentId()));
                }
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        boolean flag = "xj".equals(getProvinceFlag());

        List<String> title = new ArrayList<String>();
        title.add("企业名称");
        title.add("企业编码");
        title.add("所属地区");//13.0新增
        title.add("企业联系邮箱");
        title.add("企业EC");
        title.add("企业创建时间");
        title.add("企业状态");

        if (!flag) {
            title.add("企业合作开始时间");
            title.add("企业合作结束时间");
            title.add("企业余额(元)");
            title.add("企业折扣");
            title.add("企业存送");
        }
        
        //四川导出时增加202编码
        if("sc".equals(getProvinceFlag())){
            title.add("企业202编码");
            title.add("产品类型");
        }

        List<String> rowList = new ArrayList<String>();
        if(list !=null){
            for (Enterprise module : list) {
                rowList.add(module.getName());
    
                if (module.getCode() != null) {
                    rowList.add("'" + module.getCode());
                } else {
                    rowList.add("");
                }
                
                if(module.getCmManagerName() != null){
                    rowList.add(module.getCmManagerName());
                } else {
                    rowList.add("");
                }
    
                if (module.getEmail() != null) {
                    rowList.add(module.getEmail());
                } else {
                    rowList.add("");
                }
    
                if (module.getInterfaceFlag() == InterfaceStatus.OPEN.getCode()) {
                    rowList.add("已开通");
                } else if (module.getInterfaceFlag() == InterfaceStatus.CLOSE.getCode()) {
                    rowList.add("已关闭");
                } else if (module.getInterfaceFlag() == InterfaceStatus.UNAPPROVAL.getCode()) {
                    rowList.add("未申请");
                } else if (module.getInterfaceFlag() == InterfaceStatus.APPROVING.getCode()) {
                    rowList.add("申请中");
                } else if (module.getInterfaceFlag() == InterfaceStatus.REJECT.getCode()) {
                    rowList.add("已驳回");
                } else {
                    rowList.add("-");
                }
    
                //创建时间
                if (module.getCreateTime() != null) {
                    rowList.add(sdf.format(module.getCreateTime()));
                } else {
                    rowList.add("");
                }
    
                //状态
                rowList.add(EnterpriseStatus.getNameByCode(module.getDeleteFlag()));
    
                //不是新疆平台的,有更多的属性
                if (!flag) {
                    //合作开始时间
                    if (module.getStartTime() != null) {
                        rowList.add(sdf.format(module.getStartTime()));
                    } else {
                        rowList.add("");
                    }
    
                    //合作结束时间
                    if (module.getEndTime() != null) {
                        rowList.add(sdf.format(module.getEndTime()));
                    } else {
                        rowList.add("");
                    }
    
                    //企业报表导出时增加余额
                    Account account = accountService.getCurrencyAccount(module.getId());
                    double accountBalance = 0d;
                    if (account != null) {
                        accountBalance = account.getCount() / 100.0;
                    }
                    java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    rowList.add(nf.format(accountBalance));
    
                    //折扣
                    if (module.getDiscountName() != null) {
                        rowList.add(module.getDiscountName());
                    } else {
                        rowList.add("");
                    }
    
                    //存送
                    if (module.getGiveMoneyName() != null) {
                        rowList.add(module.getGiveMoneyName());
                    } else {
                        rowList.add("");
                    }
                }
                
                //四川导出时增加202编码
                if("sc".equals(getProvinceFlag())){
                    String userId = enterpriseUserIdService.getUserIdByEnterpriseCode(module.getCode());
                    rowList.add(userId==null?"":userId);
                    CustomerType ct = customerTypeMapper.selectById(module.getCustomerTypeId());
                    rowList.add(ct==null?"":ct.getName());
                }
            }
        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "enterprise.csv");
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

    @RequestMapping(value = "/getImageFile")
    public void getImageFile(long id, HttpServletResponse response, HttpServletRequest request) {
        EnterpriseFile enterpriseFile = enterpriseFileService.selectByEntId(id);

        //当前用户无权获取该企业的文件，403
        if (!hasAuthority(id)) {
            response.setStatus(HttpStatus.SC_FORBIDDEN);
            return;
        }

        if (enterpriseFile != null && enterpriseFile.getImageName() != null) {
            downloadFromS3(response, enterpriseFile.getImageKey(), enterpriseFile.getImageName(), request);
        }
    }

    /**
     * 获取身份证正面预览
     */
    @RequestMapping(value = "/getIdentificationFile")
    public void getIdentificationFile(long id, HttpServletResponse response, HttpServletRequest request) {

        EnterpriseFile enterpriseFile = enterpriseFileService.selectByEntId(id);

        if (enterpriseFile.getIdentificationKey() != null) {
            downloadFromS3(response, enterpriseFile.getIdentificationKey(), enterpriseFile.getIdentificationCard(),
                    request);
        }
    }

    /**
     * 获取管理员层级
     */
    @RequestMapping(value = "getManagerAjax")
    public void getManagerAjax(HttpServletRequest request, HttpServletResponse resp) throws IOException {

        String managerId = getRequest().getParameter("managerId");
        String parentId = getRequest().getParameter("parentId");

        List<Manager> manager = new ArrayList<Manager>();
        if (managerId != null) {
            manager.add(managerService.selectByPrimaryKey(Long.parseLong(managerId)));
        }
        if (parentId != null) {
            manager = managerService.selectByParentId(Long.parseLong(parentId));
        }

        List<Map<String, Object>> itemsList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < manager.size(); i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", manager.get(i).getId());
            item.put("text", manager.get(i).getName());
            item.put("state", "closed");
            // item.put("children", itemsList2);
            itemsList.add(item);
        }

        String data = JSON.toJSONString(itemsList);

        resp.getWriter().write(data);

    }

    /**
     * 获取市县节点
     */
    @RequestMapping(value = "getDistrictAjax")
    public void getDistrictAjax(HttpServletRequest request, HttpServletResponse resp) throws IOException {

        Map<String, Object> map = new HashMap<String, Object>();
        String districtId = getRequest().getParameter("districtId");
        String parentId = getRequest().getParameter("parentId");

        List<District> district = new ArrayList<District>();
        if (districtId != null) {
            map.put("id", districtId);
            district = districtMapper.selectByMap(map);
        }
        if (parentId != null) {
            map.put("parentId", parentId);
            district = districtMapper.selectByMap(map);
        }

        List<Map<String, Object>> itemsList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < district.size(); i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", district.get(i).getId());
            item.put("text", district.get(i).getName());
            item.put("state", "closed");
            // item.put("children", itemsList2);
            itemsList.add(item);
        }

        String data = JSON.toJSONString(itemsList);

        resp.getWriter().write(data);

    }

    /**
     * 给企业选择短信模板可选范围
     */
    @RequestMapping(value = "smsTemplateIndex")
    public String smsTemplateIndex(ModelMap model, QueryObject queryObject) {
        Manager manager = getCurrentUserManager();
        if (manager == null) {
            model.put("errorMsg", "当前用户无管理员身份");
            return "error.ftl";
        }

        model.addAttribute("managerId", manager.getId());

        model.addAttribute("currentUserRoleId", getCurrentUser().getRoleId());

        return "enterprises/smsTemplateIndex.ftl";
    }

    /**
     * 企业确定唯一的使用模板
     */
    @RequestMapping(value = "setSmsTemplateIndex")
    public String setSmsTemplateIndex(ModelMap model, QueryObject queryObject) {
        Manager manager = getCurrentUserManager();
        if (manager == null) {
            model.put("errorMsg", "当前用户无管理员身份");
            return "error.ftl";
        }

        model.addAttribute("managerId", manager.getId());

        model.addAttribute("currentUserRoleId", getCurrentUser().getRoleId());

        return "enterprises/setSmsTemplateIndex.ftl";
    }

    /**
     * 企业可选模板范围，页面展示
     */
    @RequestMapping(value = "smsTemplateShow")
    public String smsTemplateShow(ModelMap model, Long enterId) {
        Map map = new HashMap();
        List<SmsTemplate> smsTemplates = smsTemplateService.showSmsTemplate(map);

        List<Long> nowTemplateIds = enterpriseSmsTemplateService.selectByEnterId(enterId);

        if (nowTemplateIds != null && nowTemplateIds.size() > 0) {
            model.addAttribute("nowTemplateIds", nowTemplateIds);
        }

        model.addAttribute("smsTemplates", smsTemplates);

        model.addAttribute("enterId", enterId);

        return "enterprises/editSmsTemplate.ftl";
    }

    /**
     * 保存选定模板范围
     */
    @RequestMapping(value = "saveSmsTemplate", method = RequestMethod.POST)
    public String saveSmsTemplate(ModelMap model, Long enterId, Long[] smsTemplateIds) {

        List<Long> smsTemplatesList = (smsTemplateIds == null) ? null : Arrays.asList(smsTemplateIds);

        enterpriseSmsTemplateService.updateEnterpriseSmsTemplate(enterId, smsTemplatesList);

        return "redirect:smsTemplateIndex.html";
    }

    /**
     * 设置企业唯一模板，页面展示
     */
    @RequestMapping(value = "setSmsTemplateShow")
    public String setSmsTemplateShow(ModelMap model, Long enterId) {

        List<EnterpriseSmsTemplate> nowTemplates = enterpriseSmsTemplateService.selectSmsTemplateByEnterId(enterId);

        if (nowTemplates != null && nowTemplates.size() > 0) {
            //判断是否已关闭短信
            for (EnterpriseSmsTemplate template : nowTemplates) {
                if (template.getStatus() == 1) {
                    model.addAttribute("openSms", "1");
                }
            }
            model.addAttribute("nowTemplates", nowTemplates);
        }

        model.addAttribute("enterId", enterId);

        return "enterprises/editSetSmsTemplate.ftl";
    }

    /**
     * 保存选定的唯一模板
     */
    @RequestMapping(value = "saveSetSmsTemplate", method = RequestMethod.POST)
    public String saveSetSmsTemplate(ModelMap model, Long enterId, String sms) {

        if (StringUtils.isEmpty(sms)) {
            model.addAttribute("errorMsg", "选定短信模板为空");
            return setSmsTemplateShow(model, enterId);
        }

        enterpriseSmsTemplateService.setSmsTemplateForEnterprise(enterId, Long.parseLong(sms));

        return "redirect:setSmsTemplateIndex.html";
    }

    /**
     * 更新企业管理员页面显示
     */
    @RequestMapping(value = "updateEnterManager")
    public String updateEnterManager(Long enterpriseId, ModelMap modelMap) {
        Administer currentUser = getCurrentUser();
        if (currentUser == null || currentUser.getId() == null) {
            return getLoginAddress();
        }
        /**
         * 显示当前该企业管理员信息
         */
        List<Administer> enterpriseManagers = adminManagerService.getAdminForEnter(enterpriseId);
        //目前设计为一个企业只可能有一个管理的客户经理
        if (null != enterpriseManagers && enterpriseManagers.size() > 0) {
            Administer administer = enterpriseManagers.get(0);
            if (administer == null) {
                modelMap.addAttribute("errorMsg", "企业管理员不存在");
                return "error.ftl";
            }

            Enterprise e = enterprisesService.selectByPrimaryKey(enterpriseId);
            modelMap.addAttribute("enterprise", e);
            modelMap.addAttribute("administer", administer);
            modelMap.addAttribute("currentMobilePhone", currentUser.getMobilePhone());

            return "enterprises/updateEnterManager.ftl";
        } else {
            modelMap.addAttribute("errorMsg", "企业管理员不存在");
            return "error.ftl";
        }
    }

    /**
     * 更新用户手机号
     */
    @RequestMapping("/saveEnterManager")
    public String saveEnterManager(ModelMap model, HttpServletRequest request) {

        String enterIdStr = request.getParameter("enterId");
        if (StringUtils.isEmpty(enterIdStr)) {
            model.addAttribute("errorMsg", "无效的企业ID");
            return "error.ftl";
        }
        //获取各参数
        String mobilePhone = request.getParameter("mobilePhone");
        String userName = request.getParameter("userName");
        String originMobilePhone = request.getParameter("originMobilePhone");
        String email = request.getParameter("email");
        Long enterId = Long.parseLong(enterIdStr);

        String responseMsg = checkParameters(request);
        if (responseMsg != null) {
            model.addAttribute("errorMsg", responseMsg);
            return updateEnterManager(enterId, model);
        }

        Administer newAdmin = new Administer();
        newAdmin.setUserName(userName);
        newAdmin.setMobilePhone(mobilePhone);
        newAdmin.setEmail(email);

        Administer admin = administerService.selectByMobilePhone(mobilePhone);

        //先检查该用户是否已分配了管理员身份，如果分配了管理员身份则不能操作
        if (admin != null) {
            Manager manager = managerService.selectByAdminId(admin.getId());
            if (null != manager && !manager.getId().equals(-1L)) {
                model.addAttribute("errorMsg", "该用户已有管理员身份，不能设置成企业管理员！");
                return updateEnterManager(enterId, model);
            }
        }

        try {
            if (enterprisesService.changeEnterManager(newAdmin, enterId, getCurrentUser().getId())) {
                logger.info("用户ID:" + getCurrentUser().getId() + "修改企业ID：" + enterId + "管理员,原手机号为：" + originMobilePhone
                        + ",现修改为：" + mobilePhone);

                //原用户session退出
                Administer oldAdmin = administerService.selectByMobilePhone(originMobilePhone);
                if (oldAdmin != null && oldAdmin.getId() != null) {
                    Boolean hasLogin = MySessionListener.checkIfHasLogin(oldAdmin.getId());
                    if (hasLogin) {
                        MySessionListener.removeUserSession(oldAdmin.getId());
                        logger.info("修改企业管理员时原手机号用户：" + originMobilePhone + "在登录状态，已强制退出session");
                    }
                }
                return "redirect:index.html";
            } else {
                responseMsg = "手机号修改失败！";
            }
        } catch (Exception e) {
            responseMsg = "内部系统错误，手机号修改失败";
        }

        model.addAttribute("errorMsg", responseMsg);
        return updateEnterManager(enterId, model);
    }

    /**
     * 参数验证
     */
    private String checkParameters(HttpServletRequest request) {
        //获取各参数
        String currentMobilePhone = request.getParameter("currentMobilePhone");
        String mobilePhone = request.getParameter("mobilePhone");
        String userName = request.getParameter("userName");
        String verifyCode2 = request.getParameter("verifyCode2");
        String verifyCode1 = request.getParameter("verifyCode1");
        String originMobilePhone = request.getParameter("originMobilePhone");
        if (StringUtils.isEmpty(currentMobilePhone) || StringUtils.isEmpty(userName)
                || StringUtils.isEmpty(mobilePhone) || StringUtils.isEmpty(verifyCode2)
                || StringUtils.isEmpty(verifyCode1) || StringUtils.isEmpty(originMobilePhone)) {
            return "请求参数缺失！";
        }

        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }
        if (!currentMobilePhone.equals(administer.getMobilePhone())) {
            return "操作者非账户拥有者";

        }
        String checkResult = saveEnterManagerCheck(currentMobilePhone, verifyCode1, mobilePhone, verifyCode2);
        if (!"success".equals(checkResult)) {
            return checkResult;
        }

        return null;
    }

    /**
     * 验证码匹配验证
     */
    private String saveEnterManagerCheck(String mobile1, String verifyCode1, String mobile2, String verifyCode2) {

        //从数据库的global_config表中得到config_key对应的value值，如果不存在或者值不为"OK"则不进行检测验证码，返回
        String configValue = globalConfigService.get(configKey);
        if (configValue == null || !checkPassKey.equals(configValue)) {
            return "success";
        }

        LoginSmsPojo pojo1 = smsRedisListener.getLoginInfo(mobile1, SmsType.UPDATEPHONE_SMS);
        LoginSmsPojo pojo2 = smsRedisListener.getLoginInfo(mobile2, SmsType.UPDATEPHONE_SMS);
        if (pojo1 == null || pojo2 == null) {
            return "请先获取验证码!";
        }

        if (!verifyCode1.equals(pojo1.getPassword()) || !verifyCode2.equals(pojo2.getPassword())) {
            return "验证码不匹配!";
        }

        //判断是否已超过5分钟
        Date after1 = DateUtil.getDateAfterMinutes(pojo1.getLastTime(), 5);
        Date after2 = DateUtil.getDateAfterMinutes(pojo2.getLastTime(), 5);
        if (after1.getTime() < (new Date()).getTime() || after2.getTime() < (new Date()).getTime()) {
            return "验证码已超时!";
        }

        return "success";
    }

    /**
     * @Title: checkRoleExtist
     * @Author: wujiamin
     * @date 2016年10月17日下午5:23:59
     */
    @RequestMapping("/checkRoleExtist")
    public void checkRoleExtist(String mobilePhone, HttpServletResponse resp) throws IOException {

        resp.setContentType("text/xml;charset=utf-8");
        resp.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = resp.getWriter();

        Administer admin = administerService.selectByMobilePhone(mobilePhone);
        //先检查该用户是否已分配了角色，如果已分配角色则不能修改
        if (admin != null) {
            Long roleId = adminRoleService.getRoleIdByAdminId(admin.getId());
            if (null != roleId && !roleId.equals(-1L)) {
                pw.print("该用户已分配了角色！");
            } else {
                pw.print("true");
            }
        } else {
            pw.print("true");
        }
    }

    /**
     * 跳转到企业认证页面
     *
     * @date 2016年5月30日
     * @author wujiamin
     */
    @RequestMapping("/qualification")
    public String qualification(Long entId, ModelMap model) {
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        if (enterprise == null) {
            model.addAttribute("errorMsg", "数据库中没有相关企业Id: " + entId);
            return "error.ftl";
        }

        model.addAttribute("enterprise", enterprise);

        //获取企业管理员
        List<Administer> enterpriseManagers = adminManagerService.getAdminForEnter(enterprise.getId());
        //目前设计为一个企业只可能有一个企业管理员
        if (null != enterpriseManagers && enterpriseManagers.size() > 0) {
            model.addAttribute("enterpriseManager", enterpriseManagers.get(0));
        }
        //获取客户经理
        /*List<Administer> customerManagers = adminManagerService.getCustomerManagerByEntId(enterprise.getId());
        if (customerManagers != null && customerManagers.size()>0) {
            model.addAttribute("customerManager", customerManagers.get(0));
        }*/
        Administer customerManager = administerService.selectByMobilePhone(enterprise.getCmPhone());
        if (customerManager == null) {
            customerManager = new Administer();
        }
        model.addAttribute("customerManager", customerManager);

        EnterpriseFile ef = enterpriseFileService.selectByEntId(enterprise.getId());
        if (ef == null) {
            return "enterprises/qualification.ftl";
        } else {

            //获取历史审批记录
            List<ApprovalRecord> approvalRecords = approvalRecordService.selectByEndIdAndProcessType(entId,
                    ApprovalType.Enterprise_Approval.getCode());
            model.addAttribute("opinions", approvalRecords);
            if (approvalRecords != null && approvalRecords.size() > 0) {
                model.addAttribute("hasApproval", "true");
            } else {
                model.addAttribute("hasApproval", "false");
            }

            model.addAttribute("enterpriseFile", ef);
            return "enterprises/editQualification.ftl";
        }
    }

    /**
     * @decription 编辑认证页面
     * @author qinqinyan
     */
    @RequestMapping("/editQualification")
    public String editQualification(Long entId, ModelMap modelmap) {
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        if (enterprise == null) {
            modelmap.addAttribute("errorMsg", "数据库中没有相关企业Id: " + entId);
            return "error.ftl";
        }
        modelmap.addAttribute("enterprise", enterprise);
        EnterpriseFile ef = enterpriseFileService.selectByEntId(enterprise.getId());
        if (ef != null) {

            modelmap.addAttribute("enterpriseFile", ef);
        }

        //获取历史审批记录
        List<ApprovalRecord> approvalRecords = approvalRecordService.selectByEndIdAndProcessType(entId,
                ApprovalType.Enterprise_Approval.getCode());
        modelmap.addAttribute("opinions", approvalRecords);
        if (approvalRecords != null && approvalRecords.size() > 0) {
            modelmap.addAttribute("hasApproval", "true");
        } else {
            modelmap.addAttribute("hasApproval", "false");
        }

        return "enterprises/editQualification.ftl";
    }

    /**
     * @decription 认证合作页面
     * @author xujue
     */
    @RequestMapping("/addQualificationCooperation")
    public String addQualificationCooperation(Long entId, ModelMap modelmap) {
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        if (enterprise == null) {
            modelmap.addAttribute("errorMsg", "数据库中没有相关企业Id: " + entId);
            return "error.ftl";
        }
        modelmap.addAttribute("enterprise", enterprise);
        //传递标识：是否是自营的企业
        if (getProvinceFlag().equals(zyProvinceFlagValue)) {
            modelmap.addAttribute("flag", 1);
        } else {
            modelmap.addAttribute("flag", 0);
        }
        //获取企业管理员
        List<Administer> enterpriseManagers = adminManagerService.getAdminForEnter(enterprise.getId());
        //目前设计为一个企业只可能有一个企业管理员
        if (null != enterpriseManagers && enterpriseManagers.size() > 0) {
            modelmap.addAttribute("enterpriseManager", enterpriseManagers.get(0));
        }

        //是否需要企业扩展信息字段
        if (needExtEntInfo()) {
            EnterprisesExtInfo enterprisesExtInfo = enterprisesExtInfoService.get(entId);
            modelmap.addAttribute("needExtEntInfo", true);
            modelmap.put("extEntInfo", enterprisesExtInfo);
        }

        Administer customerManager = administerService.selectByMobilePhone(enterprise.getCmPhone());
        if (customerManager == null) {
            customerManager = new Administer();
        }
        modelmap.addAttribute("customerManager", customerManager);

        //合作信息填写需在页面提供的内容
        //客户分类选项
        List<CustomerType> customerType = getCustomerTypeList();
        modelmap.addAttribute("customerType", customerType);

        //存送比
        List<GiveMoney> giveMoneyList = giveMoneyMapper.selectNormalRecord();
        modelmap.addAttribute("giveMoneyList", giveMoneyList);
        //获取当前企业的存送比
        GiveMoneyEnter giveMoneyEnter = giveMoneyEnterService.selectByEnterId(enterprise.getId());
        modelmap.addAttribute("giveMoneyEnter", giveMoneyEnter);

        //折扣
        List<Discount> discountList = discountMapper.selectAllDiscount();
        modelmap.addAttribute("discountList", discountList);

        //文件下载内容
        EnterpriseFile ef = enterpriseFileService.selectByEntId(entId);
        if (ef == null) {
            return "enterprises/qualification_cooperation_add.ftl";
        } else {

            modelmap.addAttribute("enterpriseFile", ef);
            //获取信用额度值
            Account account = accountService.get(enterprise.getId(), productService.getCurrencyProduct().getId(),
                    AccountType.ENTERPRISE.getValue());
            if (account != null && account.getId() != null) {
                Double minCount = account.getMinCount() / (-100.0);
                if (account.getMinCount() == 0) {
                    minCount = 0D;
                }
                modelmap.addAttribute("minCount", minCount);
            }

            //获取历史审批记录
            List<ApprovalRecord> approvalRecords = approvalRecordService.selectByEndIdAndProcessType(entId,
                    ApprovalType.Enterprise_Approval.getCode());
            modelmap.addAttribute("opinions", approvalRecords);
            if (approvalRecords != null && approvalRecords.size() > 0) {
                modelmap.addAttribute("hasApproval", "true");
            } else {
                modelmap.addAttribute("hasApproval", "false");
            }

            return "enterprises/editEntInfo.ftl";
        }
    }

    /**
     * 提交认证信息
     *
     * @date 2016年7月18日
     * @author xujue
     */
    @RequestMapping("/saveQualificationCooperation")
    public String saveQualificationCooperation(ModelMap model, Enterprise enterprise, EnterprisesExtInfo eei,
            MultipartHttpServletRequest request, HttpServletResponse response) {
        if (enterprise == null || enterprise.getId() == null) {
            model.addAttribute("errorMsg", "企业异常！ ");
            return addQualificationCooperation(null, model);
        }

        //0. 得到当前登录用户
        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }
        //检查文件大小
        if (s3Service.checkFileSize(request)) {
            model.addAttribute("errorMsg", "文件大小超过限制！");
            return addQualificationCooperation(enterprise.getId(), model);
        }

        try {
            if (enterprisesService.saveQualificationCooperation(getCurrentUser().getId(), enterprise, request)) {
                logger.info("用户ID{}提交企业ID：{}认证成功！", getCurrentUser().getId(), enterprise.getId());
            }

            if (needExtEntInfo()) { //需要企业扩展信息的时候，才需要保存
                eei.setEnterId(enterprise.getId());
                eei.setEcCode(enterprise.getCode());
                eei.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
                if (enterprisesExtInfoService.insert(eei)) {
                    logger.info("增加企业拓展信息成功. ExtEntInfo = {}.", new Gson().toJson(eei));
                }
            }
        } catch (Exception e) {
            model.addAttribute("errorMsg", "认证失败！");
            return addQualificationCooperation(enterprise.getId(), model);
            //return "error.ftl";
        }

        return "redirect:/manage/potentialCustomer/indexPotential.html";
    }

    /**
     * 提交认证信息
     *
     * @date 2016年5月30日
     * @author wujiamin
     */
    @RequestMapping("/saveQualification")
    public String saveQualification(ModelMap model, Enterprise enterprise, MultipartHttpServletRequest request,
            HttpServletResponse response) {
        if (enterprise == null || enterprise.getId() == null) {
            model.addAttribute("errorMsg", "企业异常！ ");
            return "error.ftl";
        }
        //检查文件大小
        if (s3Service.checkFileSize(request)) {
            model.addAttribute("errorMsg", "文件大小超过限制！");
            return qualification(enterprise.getId(), model);
        }

        enterprise.setLicenceEndTime(DateUtil.getEndTimeOfDate(enterprise.getLicenceEndTime()));

        if (enterprise.getLicenceEndTime().before(new Date())) {
            model.addAttribute("errorMsg", "营业执照已过期");
            return "error.ftl";
        }

        if (enterprise.getLicenceStartTime().after(new Date())) {
            model.addAttribute("errorMsg", "还未到营业执照生效日期");
            return "error.ftl";
        }

        //检查上传的文件格式
        if (!s3Service.checkFile(request)) {
            model.addAttribute("errorMsg", "图片格式不正确，只支持jpg，jpeg，png");
            return "error.ftl";
        }

        if (enterprisesService.saveQualification(getCurrentUser().getId(), enterprise, request)) {
            logger.info("用户ID{}提交企业ID：{}认证成功！", getCurrentUser().getId(), enterprise.getId());
        }

        return "redirect:/manage/potentialCustomer/indexPotential.html";
    }

    /**
     * 保存编辑页面（包括企业认证和合作信息）
     *
     * @author qinqinyan
     */
    @RequestMapping("saveEditEntInfo")
    public String saveEditEntInfo(ModelMap model, Enterprise enterprise, EnterprisesExtInfo eei,
            MultipartHttpServletRequest request) {

        if (enterprise == null || enterprise.getId() == null) {
            model.addAttribute("errorMsg", "企业异常！ ");
            return "error.ftl";
        }

        //检查文件大小
        if (s3Service.checkFileSize(request)) {
            model.addAttribute("errorMsg", "文件大小超过限制！");
            return addQualificationCooperation(enterprise.getId(), model);
        }

        enterprise.setLicenceEndTime(DateUtil.getEndTimeOfDate(enterprise.getLicenceEndTime()));
        if (enterprise.getLicenceEndTime().before(new Date())) {
            model.addAttribute("errorMsg", "营业执照已过期");
            return "error.ftl";
        }

        if (enterprise.getLicenceStartTime().after(new Date())) {
            model.addAttribute("errorMsg", "还未到营业执照生效日期");
            return "error.ftl";
        }

        //转换企业合同结束日期为23:59:59秒
        enterprise.setEndTime(DateUtil.getEndTimeOfDate(enterprise.getEndTime()));

        if (enterprise.getStartTime().after(new Date())) {
            model.addAttribute("errorMsg", "还未到合同开始时间");
            return "error.ftl";
        }

        //检查上传的文件格式
        if (!s3Service.checkFile(request)) {
            model.addAttribute("errorMsg", "图片格式不正确，只支持jpg，jpeg，png");
            return "error.ftl";
        }

        try {
            enterprise.setDeleteFlag(EnterpriseStatus.COOPERATE_INFO.getCode());
            if (enterprisesService.saveEditEntInfo(enterprise, getCurrentUser().getId(), request)) {
                logger.info("用户ID{}保存企业信息ID：{}成功！", getCurrentUser().getId(), enterprise.getId());

                if (needExtEntInfo()) { //需要企业扩展信息的时候，才需要保存
                    eei.setEnterId(enterprise.getId());
                    eei.setEcCode(enterprise.getCode());
                    eei.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
                    if (enterprisesExtInfoService.update(eei)) {
                        logger.info("增加企业拓展信息成功. ExtEntInfo = {}.", new Gson().toJson(eei));
                    }
                }

                return "redirect:/manage/potentialCustomer/indexPotential.html";
            }
        } catch (RuntimeException e) {
            logger.error("用户ID{}保存认证信息ID：{}失败，原因" + e.getMessage(), getCurrentUser().getId(), enterprise.getId());
        }

        return addQualificationCooperation(enterprise.getId(), model);
    }

    /**
     * 保存编辑页面
     *
     * @author qinqinyan
     */
    @RequestMapping("saveEditQualification")
    public String saveEditQualification(ModelMap model, Enterprise enterprise, MultipartHttpServletRequest request,
            HttpServletResponse response) {
        if (enterprise == null || enterprise.getId() == null) {
            model.addAttribute("errorMsg", "企业异常！ ");
            return "error.ftl";
        }
        //检查文件大小
        if (s3Service.checkFileSize(request)) {
            model.addAttribute("errorMsg", "文件大小超过限制！");
            return editQualification(enterprise.getId(), model);
        }
        enterprise.setLicenceEndTime(DateUtil.getEndTimeOfDate(enterprise.getLicenceEndTime()));
        if (enterprise.getLicenceEndTime().before(new Date())) {
            model.addAttribute("errorMsg", "营业执照已过期");
            return "error.ftl";
        }

        if (enterprise.getLicenceStartTime().after(new Date())) {
            model.addAttribute("errorMsg", "还未到营业执照生效日期");
            return "error.ftl";
        }

        //检查上传的文件格式
        if (!s3Service.checkFile(request)) {
            model.addAttribute("errorMsg", "图片格式不正确，只支持jpg，jpeg，png");
            return "error.ftl";
        }

        try {
            enterprise.setDeleteFlag(EnterpriseStatus.QUALIFICATION_APPROVAL.getCode());
            if (enterprisesService.saveEditQualification(getCurrentUser().getId(), enterprise, request)) {
                logger.info("用户ID{}保存认证信息ID：{}成功！", getCurrentUser().getId(), enterprise.getId());
                return "redirect:/manage/potentialCustomer/indexPotential.html";
            }
        } catch (RuntimeException e) {
            logger.error("用户ID{}保存认证信息ID：{}失败，原因" + e.getMessage(), getCurrentUser().getId(), enterprise.getId());
        }

        return saveEditQualification(model, enterprise, request, response);
    }

    /**
     * 保存和作信息
     *
     * @Title: saveEditCooperation
     * @Author: wujiamin
     * @date 2016年10月17日下午5:24:10
     */
    @RequestMapping("saveEditCooperation")
    public String saveEditCooperation(ModelMap map, Enterprise enterprise, MultipartHttpServletRequest request,
            HttpServletResponse response) {
        //转换企业合同结束日期为23:59:59秒
        enterprise.setEndTime(DateUtil.getEndTimeOfDate(enterprise.getEndTime()));

        if (enterprise.getStartTime().after(new Date())) {
            map.addAttribute("errorMsg", "还未到合同开始时间");
            return "error.ftl";
        }
        //检查文件大小
        if (s3Service.checkFileSize(request)) {
            map.addAttribute("errorMsg", "文件大小超过限制！");
            return qualificationApproval(enterprise.getId(), map);
        }

        //检查上传的文件格式
        if (!s3Service.checkFile(request)) {
            map.addAttribute("errorMsg", "图片格式不正确，只支持jpg，jpeg，png");
            return "error.ftl";
        }

        //0. 得到当前登录用户
        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }
        try {
            enterprise.setDeleteFlag(EnterpriseStatus.COOPERATE_INFO.getCode());
            if (!enterprisesService.saveEditCooperation(enterprise, getCurrentUser().getId(), request)) {
                map.addAttribute("errorMsg", "保存合作信息失败！");
                return qualificationApproval(enterprise.getId(), map);
            }
        } catch (Exception e) {
            map.addAttribute("errorMsg", "保存合作信息失败！");
            return qualificationApproval(enterprise.getId(), map);
        }

        return "redirect:/manage/potentialCustomer/indexPotential.html";

    }

    /**
     * 跳转到企业认证审核页面
     *
     * @date 2016年5月31日
     * @author wujiamin
     */
    @RequestMapping("/qualificationApproval")
    public String qualificationApproval(Long entId, ModelMap model) {
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        if (enterprise == null) {
            model.addAttribute("errorMsg", "数据库中没有相关企业Id: " + entId);
            return "error.ftl";
        }

        model.addAttribute("enterprise", enterprise);

        String provinceFlag = getProvinceFlag();
        model.addAttribute("provinceFlag", provinceFlag);

        //model.addAttribute("fullDistrictName", districtMapper.selectFullDistrictNameById(enterprise.getDistrictId()));

        //传递标识：是否是自营的企业
        if (getProvinceFlag().equals(zyProvinceFlagValue)) {
            model.addAttribute("flag", 1);
        } else {
            model.addAttribute("flag", 0);
        }

        //获取企业管理员
        List<Administer> enterpriseManagers = adminManagerService.getAdminForEnter(enterprise.getId());
        //目前设计为一个企业只可能有一个企业管理员
        if (null != enterpriseManagers && enterpriseManagers.size() > 0) {
            model.addAttribute("enterpriseManager", enterpriseManagers.get(0));
        }
        //获取客户经理
        /*List<Administer> customerManagers = adminManagerService.getCustomerManagerByEntId(enterprise.getId());
        if (customerManagers != null && customerManagers.size()>0) {
            model.addAttribute("customerManager", customerManagers.get(0));
        }*/
        Administer customerManager = administerService.selectByMobilePhone(enterprise.getCmPhone());
        if (customerManager == null) {
            customerManager = new Administer();
        }
        model.addAttribute("customerManager", customerManager);

        //文件下载内容
        EnterpriseFile ef = enterpriseFileService.selectByEntId(entId);
        if (ef != null) {
            model.addAttribute("businessLicence", ef.getBusinessLicence());
            model.addAttribute("authorization", ef.getAuthorizationCertificate());
            model.addAttribute("identification", ef.getIdentificationCard());
            model.addAttribute("identificationBack", ef.getIdentificationBack());

            model.addAttribute("enterpriseFile", ef);
        }

        //合作信息填写需在页面提供的内容
        //客户分类选项
        List<CustomerType> customerType = getCustomerTypeList();
        model.addAttribute("customerType", customerType);

        //存送比
        List<GiveMoney> giveMoneyList = giveMoneyMapper.selectNormalRecord();
        model.addAttribute("giveMoneyList", giveMoneyList);

        //折扣
        List<Discount> discountList = discountMapper.selectAllDiscount();
        model.addAttribute("discountList", discountList);

        //获取信用额度值
        Account account = accountService.get(enterprise.getId(), productService.getCurrencyProduct().getId(),
                AccountType.ENTERPRISE.getValue());
        if (account != null) {
            Double minCount = account.getMinCount() / (-100.0);
            if (account.getMinCount() == 0) {
                minCount = 0D;
            }
            model.addAttribute("minCount", minCount);
        }

        if (ef != null
                && (!StringUtils.isEmpty(ef.getContractName()) || !StringUtils.isEmpty(ef.getImageName()) || !StringUtils
                        .isEmpty(ef.getCustomerfileName()))) {
            //跳转到编辑页面

            //获取历史审批记录
            List<ApprovalRecord> approvalRecords = approvalRecordService.selectByEndIdAndProcessType(entId,
                    ApprovalType.Enterprise_Approval.getCode());
            model.addAttribute("opinions", approvalRecords);
            if (approvalRecords != null && approvalRecords.size() > 0) {
                model.addAttribute("hasApproval", "true");
            } else {
                model.addAttribute("hasApproval", "false");
            }

            return "enterprises/editCooperation.ftl";
        }
        return "enterprises/qualification_approval.ftl";
    }

    /**
     * 保存合作信息
     *
     * @date 2016年6月1日
     * @author wujiamin
     */
    @RequestMapping("saveCooperation")
    public String saveCooperation(ModelMap map, Enterprise enterprise, MultipartHttpServletRequest request,
            HttpServletResponse response) {
        //转换企业合同结束日期为23:59:59秒
        enterprise.setEndTime(DateUtil.getEndTimeOfDate(enterprise.getEndTime()));

        if (enterprise.getStartTime().after(new Date())) {
            map.addAttribute("errorMsg", "还未到合同开始时间");
            return qualificationApproval(enterprise.getId(), map);
        }

        //检查文件大小
        if (s3Service.checkFileSize(request)) {
            map.addAttribute("errorMsg", "文件大小超过限制！");
            return qualificationApproval(enterprise.getId(), map);
        }
        //检查上传的文件格式
        if (!s3Service.checkFile(request)) {
            map.addAttribute("errorMsg", "图片格式不正确，只支持jpg，jpeg，png");
            return qualificationApproval(enterprise.getId(), map);
        }

        //0. 得到当前登录用户
        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }
        try {
            if (enterprisesService.saveCooperation(enterprise, getCurrentUser().getId(), request)) {
                return "redirect:/manage/potentialCustomer/indexPotential.html";
            }
        } catch (Exception e) {
            map.addAttribute("errorMsg", "保存合作信息失败！");
            return qualificationApproval(enterprise.getId(), map);
            //return "error.ftl";
        }
        map.addAttribute("errorMsg", "保存合作信息失败！");
        return qualificationApproval(enterprise.getId(), map);
        //return "error.ftl";
    }

    /**
     * 申请认证的模板下载
     *
     * @date 2016年6月22日
     * @author wujiamin
     */
    @RequestMapping(value = "/exportTemplate")
    public void exportTemplate(HttpServletResponse response, HttpServletRequest request) {
        //        try {
        //            String templateFileName = getTemplateFileName();
        String templateFileKey = getTemplateFileKey();

        //            templateFileName = new String(templateFileName.getBytes("ISO-8859-1"), "utf-8");
        String templateFileName = "企业管理员授权证明.doc";
        downloadFromS3(response, templateFileKey, templateFileName, request);
        //        } catch (UnsupportedEncodingException e) {
        //            e.printStackTrace();
        //        }
    }

    /**
     * 获取图片：用于编辑页面
     *
     * @author qinqinyan
     */
    @RequestMapping(value = "/getImage")
    public void getImage(String type, Long enterpriseId, HttpServletResponse response, HttpServletRequest request) {
        if (!hasAuthority(enterpriseId)) {
            response.setStatus(HttpStatus.SC_FORBIDDEN);
            return;
        }

        EnterpriseFile enterpriseFile = enterpriseFileMapper.selectByEntId(enterpriseId);
        if ("businessLicence".equals(type)) {
            getImageFromS3(response, enterpriseFile.getLicenceKey(), request);
        }
        if ("identification".equals(type)) {
            getImageFromS3(response, enterpriseFile.getIdentificationKey(), request);
        }
        if ("identificationBack".equals(type)) {
            getImageFromS3(response, enterpriseFile.getIdentificationBackKey(), request);
        }
        if ("image".equals(type)) {
            getImageFromS3(response, enterpriseFile.getImageKey(), request);
        }

        if ("contract".equals(type)) {
            getImageFromS3(response, enterpriseFile.getContractKey(), request);
        }
    }

    /**
     * 下载文件
     *
     * @date 2016年5月31日
     * @author wujiamin
     */
    @RequestMapping(value = "/downloadFile")
    public void downloadFile(String type, Long enterpriseId, HttpServletResponse response, HttpServletRequest request) {
        EnterpriseFile enterpriseFile = enterpriseFileMapper.selectByEntId(enterpriseId);

        if (!hasAuthority(enterpriseId)) {
            logger.error("当前用户没有下载该企业文件的权限！");
            response.setStatus(HttpStatus.SC_FORBIDDEN);
            return;
        }

        if (enterpriseFile == null) {
            return;
        }
        if ("contract".equals(type)) {
            downloadFromS3(response, enterpriseFile.getContractKey(), enterpriseFile.getContractName(), request);
        }
        if ("customerFile".equals(type) || "customerfile".equals(type)) {
            downloadFromS3(response, enterpriseFile.getCustomerfileKey(), enterpriseFile.getCustomerfileName(), request);
        }
        if ("image".equals(type)) {
            downloadFromS3(response, enterpriseFile.getImageKey(), enterpriseFile.getImageName(), request);
        }
        if ("businessLicence".equals(type)) {
            downloadFromS3(response, enterpriseFile.getLicenceKey(), enterpriseFile.getBusinessLicence(), request);
        }
        if ("authorization".equals(type)) {
            downloadFromS3(response, enterpriseFile.getAuthorizationKey(),
                    enterpriseFile.getAuthorizationCertificate(), request);
        }
        if ("identification".equals(type)) {
            downloadFromS3(response, enterpriseFile.getIdentificationKey(), enterpriseFile.getIdentificationCard(),
                    request);
        }
        if ("identificationBack".equals(type)) {
            downloadFromS3(response, enterpriseFile.getIdentificationBackKey(), enterpriseFile.getIdentificationBack(),
                    request);
        }
    }

    /**
     * 从S3中下载
     *
     * @date 2016年5月31日
     * @author wujiamin
     */
    private void downloadFromS3(HttpServletResponse response, String key, String fileName, HttpServletRequest request) {
        // S3Until download = new S3Until();

        InputStream bis = null;
        java.io.BufferedOutputStream bos = null;
        try {
            // 客户使用保存文件的对话框：
            response.setContentType("application/x-msdownload");
            response.setCharacterEncoding("utf-8");

            String userAgent = request.getHeader("USER-AGENT").toLowerCase();
            if (userAgent != null && userAgent.indexOf("firefox") >= 0) {
                response.setHeader("Content-disposition",
                        "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
            } else {
                response.setHeader("Content-disposition",
                        "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
            }
            // 通知客户文件的MIME类型：
            bis = fileStoreService.get(key);
            bos = new java.io.BufferedOutputStream(response.getOutputStream());

            if (bis != null) {
                StreamUtils.copy(bis, bos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bis = null;
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bos = null;
            }
        }

    }

    public String getTemplateFileName() {
        return globalConfigService.get(GlobalConfigKeyEnum.TEMPLATE_FILE_NAME.getKey());
    }

    public String getTemplateFileKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.TEMPLATE_FILE_KEY.getKey());
    }

    private void getImageFromS3(HttpServletResponse response, String key, HttpServletRequest request) {
        InputStream inputStream = fileStoreService.get(key);
        if (inputStream != null) {
            try {
                StreamUtils.copy(inputStream, response.getOutputStream());
            } catch (IOException e) {
                logger.error("输出结果流时错误,错误信息为{}, 错误堆栈为{}.", e.getMessage(), e.getStackTrace());
            }
        }
    }

    /**
     * @param id       企业ID
     * @param response 响应对象
     * @Description: 从BOSS处同步资金余额至平台，此处为湖南流量平台特有功能，20160715，luozuwu
     * @Description: 原来的实现有问题，现已更新，20161010, sunyiwei
     * @return: void
     */
    @RequestMapping("queryAccountFromBoss")
    public void queryAccountFromBoss(Long id, HttpServletResponse response) throws IOException {
        Account platAccount = accountService.getCurrencyAccount(id);
        if (platAccount == null) {
            writeResponse(response, "平台现金账户不存在");
        } else {
            SyncAccountResult sar = accountService.syncFromBoss(id, platAccount.getProductId());
            writeResponse(response, sar.getResult());
        }
    }

    /**
     * 同步所有企业账户
     *
     * @Title: queryAllAccountFromBoss
     */
    @RequestMapping("queryAllAccountFromBoss")
    public void queryAllAccountFromBoss(HttpServletResponse response) throws IOException {
        Manager currentManager = getCurrentUserManager();
        if (currentManager == null || currentManager.getId() == null) {
            writeResponse(response, "用户无职位");
            return;
        }
        List<Enterprise> enters = enterprisesService.getAllEnterByManagerId(currentManager.getId());
        if (enters != null && enters.size() > 0) {
            for (Enterprise e : enters) {
                Account platAccount = accountService.getCurrencyAccount(e.getId());
                if (platAccount == null) {
                    logger.info("企业Id：" + e.getId() + "不存在平台现金账户");
                } else {
                    accountService.syncFromBoss(e.getId(), platAccount.getProductId());
                }
            }
        }
        writeResponse(response, "同步完成");

    }

    //响应内容
    private void writeResponse(HttpServletResponse response, String msg) {
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> map = new HashMap<String, Object>();//ajax传输的值
        map.put("msg", msg);

        try {
            StreamUtils.copy(new Gson().toJson(map), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            logger.error("响应时出错， 错误信息为{}.", e.getMessage());
        }
    }

    /**
     * 显示创建企业的页面
     *
     * @param modelmap modelMap
     * @return 页面内容
     */
    @RequestMapping("createEnterpriseIndex")
    public String createEnterpriseIndex(ModelMap modelmap) {
        Administer currentAdmin = getCurrentUser();
        Manager manager = getCurrentUserManager();
        if (currentAdmin == null) {
            return getLoginAddress();
        }
        Boolean submitApprovalFlag = false;
        Boolean hasAuthToFillInQuafilication = false;
        Boolean hasAuthToFillInCooperation = false;
        Boolean hasAuthToFillInForProvince = false;
        Boolean hasAuthToEdit = false;

        if (manager != null) {
            //判断是否有发起审核流程的权限
            submitApprovalFlag = approvalProcessDefinitionService.hasAuthToSubmitApproval(manager.getRoleId(),
                    ApprovalType.Enterprise_Approval.getCode());

            //传递标识：是否是自营的企业
            if (getProvinceFlag().equals(zyProvinceFlagValue)) {
                modelmap.addAttribute("flag", 1);

                hasAuthToFillInQuafilication = enterprisesService.hasAuthToFillInQuafilication(manager.getRoleId());
                hasAuthToFillInCooperation = enterprisesService.hasAuthToFillInCooperation(manager.getRoleId());

            } else {
                modelmap.addAttribute("flag", 0);

                hasAuthToFillInForProvince = enterprisesService.hasAuthToFillInForProvince(manager.getRoleId());
            }

            hasAuthToEdit = enterprisesService.hasAuthToEdit(manager.getRoleId());
            modelmap.addAttribute("managerId", manager.getId());
        }
        modelmap.addAttribute("submitApprovalFlag", submitApprovalFlag.toString());
        modelmap.addAttribute("quafilicationFlag", hasAuthToFillInQuafilication.toString());
        modelmap.addAttribute("cooperationFlag", hasAuthToFillInCooperation.toString());
        modelmap.addAttribute("entInfoFlag", hasAuthToFillInForProvince.toString());
        modelmap.addAttribute("editFlag", hasAuthToEdit.toString());


        modelmap.addAttribute("currentUserRoleId", getCurrentUser().getRoleId());

        String provinceFlag = globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey());
        modelmap.addAttribute("provinceFlag", provinceFlag);

        //使用企业关键人企业列表中的更新，还是用户更改中的修改和提交审核
        String isUseEnterList = globalConfigService.get(GlobalConfigKeyEnum.ADMIN_CHANGE_USE_ENTERLIST.getKey());
        if ("true".equals(isUseEnterList)) {
            modelmap.addAttribute("isUseEnterList", "true");
        } else {
            modelmap.addAttribute("isUseEnterList", "false");
        }
        if ("gd_mdrc".equals(getProvinceFlag())) { //广东流量卡
            modelmap.addAttribute("crowdfundingFlag", getIsCrowdfundingPlatform().toString());
            modelmap.addAttribute("gdzcFlag", "true");
            return "enterprises/create_enterprise_index_gd.ftl";
        } else {
            return "enterprises/create_enterprise_index.ftl";
        }
        
    }

    /**
     * 企业创建全流程页面
     *
     * @Title: createEnterprise
     * @Author: wujiamin
     * @date 2016年9月7日上午10:13:20
     */
    @RequestMapping("/createEnterprise")
    public String createEnterprise(ModelMap modelmap) {
        Administer currentAdmin = getCurrentUser();
        Manager manager = getCurrentUserManager();
        if (currentAdmin == null) {
            return getLoginAddress();
        }
        Boolean submitApprovalFlag = false;
        Boolean hasAuthToFillInQuafilication = false;
        Boolean hasAuthToFillInCooperation = false;
        Boolean hasAuthToFillInForProvince = false;
        Boolean hasAuthToEdit = false;

        if (manager != null) {
            //判断是否有发起审核流程的权限
            submitApprovalFlag = approvalProcessDefinitionService.hasAuthToSubmitApproval(manager.getRoleId(),
                    ApprovalType.Enterprise_Approval.getCode());

            if (getProvinceFlag().equals("chongqing")) {
                modelmap.addAttribute("cqFlag", 1);
            } else {
                modelmap.addAttribute("cqFlag", 0);
            }

            //传递标识：是否是自营的企业
            if (getProvinceFlag().equals(zyProvinceFlagValue)) {
                modelmap.addAttribute("flag", 1);

                hasAuthToFillInQuafilication = enterprisesService.hasAuthToFillInQuafilication(manager.getRoleId());
                hasAuthToFillInCooperation = enterprisesService.hasAuthToFillInCooperation(manager.getRoleId());

            } else {
                modelmap.addAttribute("flag", 0);

                hasAuthToFillInForProvince = enterprisesService.hasAuthToFillInForProvince(manager.getRoleId());
            }

            hasAuthToEdit = enterprisesService.hasAuthToEdit(manager.getRoleId());
        }
        modelmap.addAttribute("submitApprovalFlag", submitApprovalFlag.toString());
        modelmap.addAttribute("quafilicationFlag", hasAuthToFillInQuafilication.toString());
        modelmap.addAttribute("cooperationFlag", hasAuthToFillInCooperation.toString());
        modelmap.addAttribute("entInfoFlag", hasAuthToFillInForProvince.toString());
        modelmap.addAttribute("editFlag", hasAuthToEdit.toString());

        //合作信息填写需在页面提供的内容
        //客户分类选项
        List<CustomerType> customerType = getCustomerTypeList();
        modelmap.addAttribute("customerType", customerType);

        //存送比
        List<GiveMoney> giveMoneyList = giveMoneyMapper.selectNormalRecord();
        modelmap.addAttribute("giveMoneyList", giveMoneyList);

        //折扣
        List<Discount> discountList = discountMapper.selectAllDiscount();
        modelmap.addAttribute("discountList", discountList);

        //默认的客户经理
        //如果当前用户有作为企业管理员父节点的权限，则将该用户号码填写至页面
        boolean roleFlag = false;
        if (manager != null) {
            roleFlag = authorityService.ifHaveAuthority(manager.getId(), "ROLE_ENTERPRISE_MANAGER_PARENT");
            //获取地区全称
            String fullname = "";
            modelmap.addAttribute("fullDistrictName",
                    managerService.getFullNameByCurrentManagerId(fullname, manager.getId()));
        }

        if (roleFlag) {
            modelmap.addAttribute("cmPhone", currentAdmin.getMobilePhone());
            modelmap.addAttribute("cmEmial", currentAdmin.getEmail());
            modelmap.addAttribute("cmName", currentAdmin.getUserName());
        }
        if (needCheckPhoneRegion()) {
            String phoneRegion = Provinces.fromCode(getPhoneRegion()).getName();
            modelmap.addAttribute("phoneRegion", phoneRegion);
        }

        if ("gd_mdrc".equals(getProvinceFlag())) { //广东流量卡
            modelmap.addAttribute("crowdfundingFlag", getIsCrowdfundingPlatform().toString());
            return "enterprises/create_enterprise_add_gd.ftl";
        } else {
            return "enterprises/create_enterprise_add.ftl";
        }

    }

    /**
     * 编辑全流程企业信息
     *
     * @Title: editEnterprise
     * @Author: wujiamin
     * @date 2016年9月7日下午4:06:26
     */
    @RequestMapping("/editEnterprise")
    public String editEnterprise(Long entId, ModelMap modelmap) {
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        if (enterprise == null) {
            modelmap.addAttribute("errorMsg", "数据库中没有相关企业Id: " + entId);
            return "error.ftl";
        }
        if (getProvinceFlag().equals("chongqing")) {
            modelmap.addAttribute("cqFlag", 1);
        } else {
            modelmap.addAttribute("cqFlag", 0);
        }

        //获取地区全称
        Manager manager = entManagerService.getManagerForEnter(enterprise.getId());
        if (manager != null) {
            String fullname = "";
            modelmap.addAttribute("fullDistrictName",
                    managerService.getFullNameByCurrentManagerId(fullname, manager.getParentId()));
        }

        modelmap.addAttribute("enterprise", enterprise);
        //传递标识：是否是自营的企业
        if (getProvinceFlag().equals(zyProvinceFlagValue)) {
            modelmap.addAttribute("flag", 1);
        } else {
            modelmap.addAttribute("flag", 0);
        }
        //获取企业管理员
        List<Administer> enterpriseManagers = adminManagerService.getAdminForEnter(enterprise.getId());
        //目前设计为一个企业只可能有一个企业管理员
        if (null != enterpriseManagers && enterpriseManagers.size() > 0) {
            modelmap.addAttribute("enterpriseManager", enterpriseManagers.get(0));
        }

        Administer customerManager = administerService.selectByMobilePhone(enterprise.getCmPhone());
        if (customerManager == null) {
            customerManager = new Administer();
        }
        modelmap.addAttribute("customerManager", customerManager);

        //合作信息填写需在页面提供的内容
        //客户分类选项
        List<CustomerType> customerType = getCustomerTypeList();
        modelmap.addAttribute("customerType", customerType);

        //存送比
        List<GiveMoney> giveMoneyList = giveMoneyMapper.selectNormalRecord();
        modelmap.addAttribute("giveMoneyList", giveMoneyList);
        //获取当前企业的存送比
        GiveMoneyEnter giveMoneyEnter = giveMoneyEnterService.selectByEnterId(enterprise.getId());
        modelmap.addAttribute("giveMoneyEnter", giveMoneyEnter);

        //折扣
        List<Discount> discountList = discountMapper.selectAllDiscount();
        modelmap.addAttribute("discountList", discountList);

        //文件下载内容
        EnterpriseFile ef = enterpriseFileService.selectByEntId(entId);
        if (ef == null) {
            return "enterprises/create_enterprise_add.ftl";
        } else {

            modelmap.addAttribute("enterpriseFile", ef);
            //获取信用额度值
            Account account = accountService.get(enterprise.getId(), productService.getCurrencyProduct().getId(),
                    AccountType.ENTERPRISE.getValue());
            if (account != null) {
                Double minCount = account.getMinCount() / (-100.0);
                if (account.getMinCount() == 0) {
                    minCount = 0D;
                }
                modelmap.addAttribute("minCount", minCount);
            }

            //获取历史审批记录
            List<ApprovalRecord> approvalRecords = approvalRecordService.selectByEndIdAndProcessType(entId,
                    ApprovalType.Enterprise_Approval.getCode());
            modelmap.addAttribute("opinions", approvalRecords);
            if (approvalRecords != null && approvalRecords.size() > 0) {
                modelmap.addAttribute("hasApproval", "true");
            } else {
                modelmap.addAttribute("hasApproval", "false");
            }
            if (needCheckPhoneRegion()) {
                String phoneRegion = Provinces.fromCode(getPhoneRegion()).getName();
                modelmap.addAttribute("phoneRegion", phoneRegion);
            }

            //判断是否是四川的平台
            String provinceFlag = globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey());
            if ("sc".equalsIgnoreCase(provinceFlag)) {
                modelmap.addAttribute("sc", "true");
            }

            if ("gd_mdrc".equals(getProvinceFlag())) { //广东流量卡
                EnterprisesExtInfo enterprisesExtInfo = enterprisesExtInfoService.get(entId);
                modelmap.put("extEntInfo", enterprisesExtInfo);
                modelmap.addAttribute("crowdfundingFlag", getIsCrowdfundingPlatform().toString());
                return "enterprises/create_enterprise_edit_gd.ftl";
            } else {
                return "enterprises/create_enterprise_edit.ftl";
            }
        }

    }

    /**
     * @Title: checkEnterpriseManagerValid
     */
    @RequestMapping(value = "/checkEnterpriseManagerValid")
    public void checkEnterpriseManagerValid(HttpServletResponse resp, String enterpriseManagerPhone, Long enterId)
            throws IOException {
        String str = "true";
        if (!com.cmcc.vrp.util.StringUtils.isValidMobile(enterpriseManagerPhone)) {
            str = "false";
            resp.getWriter().write(str);
            return;
        }

        //判断该用户身份
        Administer admin = administerService.selectByMobilePhone(enterpriseManagerPhone);

        //该号码已经存在
        if (admin != null) {
            Long managerId = adminManagerService.selectManagerIdByAdminId(admin.getId());
            if (managerId != null && !managerId.equals(-1L)) {
                str = "false";
            }
            Long roleId = adminRoleService.getRoleIdByAdminId(admin.getId());
            if (roleId != null && !roleId.equals(-1L)) {
                str = "false";
            }
            //如果是编辑，判断当前企业的管理员是否是该用户
            if (enterId != null) {
                List<Administer> admins = adminManagerService.getAdminForEnter(enterId);
                if (admins != null && admins.size() > 0) {
                    for (Administer a : admins) {
                        if (a.getMobilePhone().equals(enterpriseManagerPhone)) {
                            str = "true";
                            break;
                        }
                    }
                }
            }

        }
        resp.getWriter().write(str);
    }

    /**
     * @param resp
     * @param enterpriseManagerPhone
     * @param enterId
     * @throws IOException
     */
    @RequestMapping(value = "/checkEnterManagerPhoneRegion")
    public void checkEnterManagerPhoneRegion(HttpServletResponse resp, String enterpriseManagerPhone, Long enterId)
            throws IOException {
        String str = "true";
        if (needCheckPhoneRegion()) {
            PhoneRegion phoneRegion = phoneRegionService.query(enterpriseManagerPhone);

            String provinceCode = (phoneRegion == null) ? Provinces.Unknown.getCode() : Provinces.fromName(
                    phoneRegion.getProvince()).getCode();
            String supplier = (phoneRegion == null) ? Provinces.Unknown.getCode() : phoneRegion.getSupplier();
            if (!provinceCode.equals(getPhoneRegion()) || !"M".equals(supplier)) {
                str = "false";
                resp.getWriter().write(str);
                return;
            }
        }
        resp.getWriter().write(str);
    }

    /**
     * @Title: checkCustomerManagerValid
     */
    @RequestMapping(value = "/checkCustomerManagerValid")
    public void checkCustomerManagerValid(ModelMap model, HttpServletResponse resp, String customerManagerPhone)
            throws IOException {
        String str = "true";
        Administer admin = administerService.selectByMobilePhone(customerManagerPhone);

        if (admin == null) {
            str = "false";
            resp.getWriter().write(str);
            return;
        } else {
            Manager manager = managerService.selectByAdminId(admin.getId());
            if (manager == null) {
                model.addAttribute("errorMsg", "客户经理手机号码的用户无客户经理身份");
                str = "false";
                resp.getWriter().write(str);
                return;
            } else {
                //查找该用户的管理员身份所对应的角色，是否具有成为企业管理员父节点的权限
                boolean roleFlag = authorityService.ifHaveAuthority(manager.getId(), "ROLE_ENTERPRISE_MANAGER_PARENT");

                if (!roleFlag) {
                    str = "false";
                    resp.getWriter().write(str);
                    return;
                } else {
                    resp.getWriter().write(str);
                    return;
                }

            }
        }
    }

    /**
     * 根据企业名称查询相应的企业， 支持模糊匹配
     *
     * @param entName 企业名称
     */
    @RequestMapping(value = "/query")
    public void queryEntInfos(@RequestParam("keyword") String entName, HttpServletResponse response) {
        if (StringUtils.isBlank(entName)) {
            return;
        }

        Manager manager = getCurrentUserManager();
        QueryObject queryObject = new QueryObject();
        Map<String, Object> queryCriteria = new LinkedHashMap<String, Object>();
        queryCriteria.put("name", transferQuery(entName));
        queryCriteria.put("managerId", manager.getId());
        queryObject.setQueryCriterias(queryCriteria);

        List<Enterprise> enterpriseList = enterprisesService.showEnterprisesForPageResult(queryObject);
        List<EntInfo> infos = convert(enterpriseList);
        try {
            StreamUtils.copy(new Gson().toJson(infos), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            logger.error("响应请求时出错，错误信息为{}, 错误堆栈为{}.", e.getMessage(), e.getStackTrace());
        }
    }

    private List<EntInfo> convert(List<Enterprise> ents) {
        List<EntInfo> infos = new LinkedList<EntInfo>();
        for (Enterprise ent : ents) {
            infos.add(new EntInfo(ent.getName(), ent.getName()));
        }

        return infos;
    }

    /**
     * 保存全流程企业开户
     *
     * @Title: saveCreateEnterprise
     * @Author: wujiamin
     * @date 2016年9月7日下午4:10:14
     */
    @RequestMapping("/saveCreateEnterprise")
    public String saveCreateEnterprise(ModelMap model, Enterprise enterprise, EnterprisesExtInfo eei,
            MultipartHttpServletRequest request, HttpServletResponse response) {

        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }
        String cmPhone = request.getParameter("customerManagerPhone");
        String emPhone = request.getParameter("enterpriseManagerPhone");
        String emName = request.getParameter("enterpriseManagerName");

        // 1.检验所有参数是否通过校验
        if (!validatePotEnterprise(model, enterprise, cmPhone, emPhone, emName)) {
            return createEnterprise(model);
        }
        
        //广东众筹、广东流量卡，校验企业编码和集团产品编码是否有重复
        if (needExtEntInfo()) { //需要企业扩展信息的时候，才需要保存
            List<EnterprisesExtInfo> extInfos = enterprisesExtInfoService.selectByEcCodeAndEcPrdCode(enterprise.getCode(), 
                    eei.getEcPrdCode());
            if(extInfos!=null && extInfos.size()>0){
                model.addAttribute("errorMsg", "企业编码和集团产品号码已存在！");
                return createEnterprise(model);
            }
        }
        // 2.检查文件大小
        if (s3Service.checkFileSize(request)) {
            model.addAttribute("errorMsg", "文件大小超过限制！");
            return "error.ftl";
        }

        enterprise.setCmPhone(cmPhone);
        // 3.初始化潜在企业的信息
        initPotentialEnt(enterprise, administer);

        // 4.创建潜在企业
        try {
            if (potentialCusterService
                    .savePotentialEnterprise(enterprise, cmPhone, emPhone, emName, administer.getId())
                    && enterprisesService.saveQualificationCooperation(getCurrentUser().getId(), enterprise, request)) {
                logger.info("用户ID:" + getCurrentUser().getId() + " 创建新企业成功" + " 企业名称：" + enterprise.getName()
                        + " 企业编码：" + enterprise.getCode() + "企业品牌名：" + enterprise.getEntName());

                //5. 保存企业扩展信息
                //这里只有广东流量卡和广东众筹用到
                if (needExtEntInfo()) { //需要企业扩展信息的时候，才需要保存
                    eei.setEnterId(enterprise.getId());
                    eei.setEcCode(enterprise.getCode());
                    eei.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
                    eei.setCreateTime(new Date());
                    eei.setUpdateTime(new Date());
                    if (enterprisesExtInfoService.insert(eei)) {
                        logger.info("增加企业拓展信息成功. ExtEntInfo = {}.", new Gson().toJson(eei));
                    }

                    //默认接口开通
                    Enterprise enter = new Enterprise();
                    enter.setId(enterprise.getId());
                    enter.setAppKey(UUID.randomUUID().toString().replace("-", ""));
                    enter.setAppSecret(UUID.randomUUID().toString().replace("-", ""));
                    enter.setInterfaceFlag(InterfaceStatus.OPEN.getCode());
                    enter.setInterfaceExpireTime(DateUtil.converStrYMDToDate("2099-01-01 00:00:00"));
                    if (!enterprisesService.updateByPrimaryKeySelective(enter)) {
                        logger.info("更新企业appkey、appsecret、开通EC接口失败 - {}" + JSON.toJSONString(enter));
                    }
                }

                return "redirect:createEnterpriseIndex.html";
            } else {
                model.addAttribute("errorMsg", "企业开户失败！");
                return createEnterprise(model);
            }
        } catch (Exception e) {
            model.addAttribute("errorMsg", "企业开户失败！" + e.getMessage());
            return createEnterprise(model);
        }
    }

    /**
     * 保存编辑全流程企业开户
     *
     * @Title: saveEditEntInfo
     * @Author: wujiamin
     * @date 2016年9月7日下午4:10:31
     */
    @RequestMapping("saveEditEnterprise")
    public String saveEditEnterprise(ModelMap model, Enterprise enterprise, EnterprisesExtInfo eei,
            MultipartHttpServletRequest request, HttpServletResponse response) {

        //1. 得到当前登录用户
        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }

        if (enterprise == null || enterprise.getId() == null) {
            model.addAttribute("errorMsg", "企业异常！ ");
            return "error.ftl";
        }

        String cmPhone = request.getParameter("customerManagerPhone");
        String emPhone = request.getParameter("enterpriseManagerPhone");
        String emName = request.getParameter("enterpriseManagerName");

        // 2.检验所有参数是否通过校验
        if (!validatePotEnterprise(model, enterprise, cmPhone, emPhone, emName)) {
            return editEnterprise(enterprise.getId(), model);
        }
      
        //广东众筹、广东流量卡，校验企业编码和集团产品编码是否有重复
        if (needExtEntInfo()) { //需要企业扩展信息的时候，才需要保存
            List<EnterprisesExtInfo> extInfos = enterprisesExtInfoService.selectByEcCodeAndEcPrdCode(enterprise.getCode(), 
                    eei.getEcPrdCode());
            if(extInfos!=null && extInfos.size()>0){
                for(EnterprisesExtInfo extInfo : extInfos){
                    if(!extInfo.getEnterId().equals(enterprise.getId())){
                        model.addAttribute("errorMsg", "企业编码和集团产品号码已存在！");
                        return editEnterprise(enterprise.getId(), model);
                    }
                }                
            }
        }

        // 3.更新潜在企业、企业管理员、客户经理的信息
        Enterprise enterprised = enterprisesService.selectByPrimaryKey(enterprise.getId());
        if (enterprised == null || enterprised.getId() == null) {
            model.addAttribute("errorMsg", "企业异常！ ");
            return "error.ftl";
        }
        //校验企业是否已在审核状态
        if (EnterpriseStatus.NORMAL.getCode().equals(enterprised.getDeleteFlag())
                || EnterpriseStatus.SUBMIT_APPROVAL.getCode().equals(enterprised.getDeleteFlag())) {//判断是否重复提交
            model.addAttribute("errorMsg1", "该记录已提交审核！");//前端用一个隐藏的审核标识
            return editEnterprise(enterprise.getId(), model);
        }

        String name;
        String entName;
        String phone;
        String email;
        Long adminId;
        if (StringUtils.isNotBlank(name = enterprise.getName())) {
            enterprised.setName(name);
        }
        if (StringUtils.isNotBlank(entName = enterprise.getEntName())) {
            enterprised.setEntName(entName);
        }
        if (StringUtils.isNotBlank(phone = enterprise.getPhone())) {
            enterprised.setPhone(phone);
        }
        if (StringUtils.isNotBlank(email = enterprise.getEmail())) {
            enterprised.setEmail(email);
        }
        if ((adminId = administer.getId()) != null) {
            enterprised.setCreatorId(adminId);
        }

        enterprised.setCmPhone(cmPhone);

        //认证及合作信息
        //检查文件大小
        if (s3Service.checkFileSize(request)) {
            model.addAttribute("errorMsg", "文件大小超过限制！");
            return editEnterprise(enterprise.getId(), model);
        }
        if (!getProvinceFlag().equals("chongqing")) {
            enterprise.setLicenceEndTime(DateUtil.getEndTimeOfDate(enterprise.getLicenceEndTime()));

            if (enterprise.getLicenceEndTime().before(new Date())) {
                model.addAttribute("errorMsg", "营业执照已过期");
                return editEnterprise(enterprise.getId(), model);
            }

            if (enterprise.getLicenceStartTime().after(new Date())) {
                model.addAttribute("errorMsg", "还未到营业执照生效日期");
                return editEnterprise(enterprise.getId(), model);
            }

            //转换企业合同结束日期为23:59:59秒
            enterprise.setEndTime(DateUtil.getEndTimeOfDate(enterprise.getEndTime()));

            if (enterprise.getStartTime().after(new Date())) {
                model.addAttribute("errorMsg", "还未到合同开始时间");
                return editEnterprise(enterprise.getId(), model);
            }
        }
        //检查上传的文件格式
        if (!s3Service.checkFile(request)) {
            model.addAttribute("errorMsg", "图片格式不正确，只支持jpg，jpeg，png");
            return editEnterprise(enterprise.getId(), model);
        }

        try {
            enterprise.setDeleteFlag(EnterpriseStatus.COOPERATE_INFO.getCode());
            if (potentialCusterService.saveEditPotential(enterprised, cmPhone, emPhone, emName, administer.getId())
                    && enterprisesService.saveEditEntInfo(enterprise, getCurrentUser().getId(), request)) {

                //5. 保存企业扩展信息
                if (needExtEntInfo()) { //需要企业扩展信息的时候，才需要保存
                    eei.setEnterId(enterprise.getId());
                    eei.setEcCode(enterprise.getCode());
                    eei.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
                    if (enterprisesExtInfoService.update(eei)) {
                        logger.info("增加企业拓展信息成功. ExtEntInfo = {}.", new Gson().toJson(eei));
                    }
                }

                logger.info("用户ID:{}编辑潜在用户成功，enterpriseId={}，cmPhone={}，emPhone={}，emName={}", administer.getId(),
                        enterprised.getId(), cmPhone, emPhone, emName);
                logger.info("用户ID{}保存企业信息ID：{}成功！", getCurrentUser().getId(), enterprise.getId());
                return "redirect:createEnterpriseIndex.html";
            }
        } catch (RuntimeException e) {
            logger.error("用户ID{}保存认证信息ID：{}失败，原因" + e.getMessage(), getCurrentUser().getId(), enterprise.getId());
        }

        return editEnterprise(enterprise.getId(), model);
    }

    private boolean validatePotEnterprise(ModelMap model, Enterprise enterprise, String cmPhone, String emPhone,
            String emName) {
        if (enterprise == null) {
            model.addAttribute("errorMsg", "无效的企业对象!");
            return false;
        }

        try {
            enterprise.selfCheck();
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
            return false;
        }

        return checkCM2Ent(model, cmPhone) && checkEM2Ent(model, enterprise, emPhone, emName);
    }

    private boolean checkCM2Ent(ModelMap model, String cmPhone) {
        //判断用户是否已经有managerId
        Administer admin = administerService.selectByMobilePhone(cmPhone);
        if (admin == null) {
            model.addAttribute("errorMsg", "客户经理的手机号码不存在");
            logger.info("该号码的客户经理不存在phone={}", cmPhone);
            return false;
        } else {
            Manager manager = managerService.selectByAdminId(admin.getId());
            if (manager == null) {
                model.addAttribute("errorMsg", "客户经理手机号码的用户无客户经理身份");
                logger.info("该号码的用户没有管理员身份phone={},adminId={}", cmPhone, admin.getId());
                return false;
            } else {
                //查找该用户的管理员身份所对应的角色，是否具有成为企业管理员父节点的权限
                boolean roleFlag = authorityService.ifHaveAuthority(manager.getId(), "ROLE_ENTERPRISE_MANAGER_PARENT");
                if (!roleFlag) {
                    model.addAttribute("errorMsg", "客户经理手机号码对应的用户非客户经理身份");
                    logger.info("客户经理手机号码对应的用户的角色，不能成为企业管理员父节点");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 检查企业与企业管理员之间的关系
     */
    private boolean checkEM2Ent(ModelMap model, Enterprise enterprise, String EMPhone, String EMName) {

        //判断用户是否已经有managerId
        Administer admin = administerService.selectByMobilePhone(EMPhone);
        if (admin == null) {
            return true;
        } else {
            //企业ID已存在，说明是编辑
            if (enterprise.getId() != null) {
                //如果企业管理员的用户已经是该企业的企业管理员对应的用户，就不报错
                List<Long> adminIds = adminManagerService.getAdminIdForEnter(enterprise.getId());
                if (adminIds != null && adminIds.size() > 0 && adminIds.contains(admin.getId())) {
                    return true;
                }
            }

            Manager manager = managerService.selectByAdminId(admin.getId());
            if (manager != null && !manager.getId().equals(-1L)) {
                model.addAttribute("errorMsg", "企业管理员手机号码的用户已有其他职位");
                return false;
            }
        }

        return true;
    }

    private void initPotentialEnt(Enterprise enterprise, Administer administer) {
        enterprise.setStatus((byte) 1);
        enterprise.setCreateTime(new Date());
        enterprise.setUpdateTime(new Date());
        enterprise.setDeleteFlag(EnterpriseStatus.PROBATION.getCode());
        enterprise.setCreatorId(administer.getId());
    }

    /**
     * 审批意见列表
     *
     * @Title: showApprovalDetail
     * @Author: wujiamin
     * @date 2016年9月8日上午10:13:53
     */
    @RequestMapping("/showApprovalDetail")
    public String showApprovalDetail(Long entId, ModelMap modelmap) {
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        if (enterprise == null) {
            modelmap.addAttribute("errorMsg", "数据库中没有相关企业Id: " + entId);
            return "error.ftl";
        }
        //获取历史审批记录
        List<ApprovalRecord> approvalRecords = approvalRecordService.selectByEndIdAndProcessType(entId,
                ApprovalType.Enterprise_Approval.getCode());
        modelmap.addAttribute("opinions", approvalRecords);
        if (approvalRecords != null && approvalRecords.size() > 0) {
            modelmap.addAttribute("hasApproval", "true");
        } else {
            modelmap.addAttribute("hasApproval", "false");
        }

        //判断是否是四川的平台
        String provinceFlag = globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey());
        if ("sc".equalsIgnoreCase(provinceFlag)) {
            modelmap.addAttribute("sc", "true");
        }

        return "entApproval/approvalDetailList.ftl";

    }

    /**
     * 查看企业EC信息
     *
     * @Title: showEcInfo
     * @Author: wujiamin
     * @date 2016年10月18日
     */
    @RequestMapping("/showEcInfo")
    public String showEcInfo(Long entId, ModelMap modelmap) {
        Manager manager = getCurrentUserManager();
        if(manager == null){
            return getLoginAddress();
        }
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        if (enterprise == null) {
            modelmap.addAttribute("errorMsg", "数据库中没有相关企业Id: " + entId);
            return "error.ftl";
        }
        
        if(!managerService.managedByManageId(entId, manager.getId())){
            logger.info("用户managerId = " +  manager.getId() + "没有权限操作企业 entId = " + entId);
            modelmap.put("errorMsg", "对不起，您没有权限操作该条记录");
            return "error.ftl";
        }

        modelmap.addAttribute("enterprise", enterprise);
        EntCallbackAddr addr = entCallbackAddrService.get(entId);
        if (addr != null) {
            modelmap.addAttribute("callbackAddr", addr.getCallbackAddr());
        }
        List<EntWhiteList> entWhiteList = entWhiteListService.selectByEntId(entId);
        modelmap.addAttribute("entWhiteLists", entWhiteList);
        modelmap.addAttribute("currentUserRoleId", getCurrentUser().getRoleId());

        //appkey及appsecret状态
        if (enterprise.getDeleteFlag() == 3) {
            modelmap.addAttribute("expire", "已失效");
        } else if (enterprise.getInterfaceExpireTime() != null && new Date().after(enterprise.getInterfaceExpireTime())) {
            modelmap.addAttribute("expire", "已过期");
        } else {
            modelmap.addAttribute("expire", "生效中");
        }
        if (getENTERPRISE_CONTACTOR().equals(getCurrentUser().getRoleId().toString())) {
            return "ec/ecInfoEnterManager.ftl";
        }
        //山东流量平台，市级管理显示关闭和操作记录按钮;上海流量平台，市级管理显示关闭和操作记录按钮;
        String proviceFlag = getProvinceFlag();
        if ("sd".equalsIgnoreCase(proviceFlag) || "shanghai".equalsIgnoreCase(proviceFlag)) {
            modelmap.addAttribute("closeAndRecordButton", "true");
        }
        return "ec/ecInfoSuperiorManager.ftl";

    }

    /**
     * 更新appkey和appsecret
     *
     * @Title: changeAppkey
     * @Author: wujiamin
     * @date 2016年10月19日
     */
    @RequestMapping(value = "/changeAppkey")
    public void changeAppkey(Long id, HttpServletResponse response, HttpServletRequest request) {
        logger.info("用户id：{}，进行更新企业id：{}的appkey、appsecret操作", getCurrentUser().getId(), id);
        Map result = new HashMap();
        if (enterprisesService.changeAppkey(id)) {
            result.put("msg", "success");
        } else {
            result.put("msg", "fail");
        }
        try {
            response.getWriter().write(JSONObject.toJSONString(result));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return;
    }

    /**
     * EC信息编辑
     *
     * @Title: ecInfoEdit
     * @Title: ecInfoEdit
     * @Author: wujiamin
     * @date 2016年10月20日
     */
    @RequestMapping(value = "/ecInfoEdit")
    public String ecInfoEdit(Long entId, ModelMap model) {
        Manager manager = getCurrentUserManager();
        if(manager == null){
            return getLoginAddress();
        }
        
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        if (enterprise == null) {
            model.put("errorMsg", "企业不存在，企业Id=" + entId);
            return "error.ftl";
        }
        
        if(!managerService.managedByManageId(entId, manager.getId())){
            logger.info("用户managerId = " +  manager.getId() + "没有权限操作企业 entId = " + entId);
            model.put("errorMsg", "对不起，您没有权限操作该条记录");
            return "error.ftl";
        }

        model.put("enterprise", enterprise);

        //修改信息是回填当前生效的信息
        if (enterprise.getInterfaceFlag() == InterfaceStatus.OPEN.getCode()
                || enterprise.getInterfaceFlag() == InterfaceStatus.CLOSE.getCode()) {
            EntCallbackAddr addr = entCallbackAddrService.get(entId);
            if (addr != null) {
                if (!StringUtils.isEmpty(addr.getCallbackAddr())) {
                    parseCallbackUrl(addr.getCallbackAddr(), model);
                }
            }
            List<EntWhiteList> ipList = entWhiteListService.selectByEntId(entId);
            for (int i = 0; i < ipList.size(); i++) {
                if (!StringUtils.isEmpty(ipList.get(i).getIpAddress())) {
                    if (i == 0) {
                        model.put("ip1", ipList.get(i).getIpAddress());
                        continue;
                    }
                    if (i == 1) {
                        model.put("ip2", ipList.get(i).getIpAddress());
                        continue;
                    }
                    if (i == 2) {
                        model.put("ip3", ipList.get(i).getIpAddress());
                        continue;
                    }
                }
            }

        } else if (enterprise.getInterfaceFlag() == InterfaceStatus.REJECT.getCode()) {//初始化已驳回回填上次填写的信息
            List<ApprovalRequest> requests = approvalRequestService.selectByEntIdAndProcessType(entId,
                    ApprovalType.Ec_Approval.getCode());
            if (requests != null && requests.size() > 0) {
                if (requests.get(0).getResult().equals(ApprovalRequestStatus.REJECT.getCode())) {
                    EcApprovalDetail detail = ecApprovalDetailService.selectByRequestId(requests.get(0).getId());
                    if (!StringUtils.isEmpty(detail.getCallbackUrl())) {
                        parseCallbackUrl(detail.getCallbackUrl(), model);
                    }
                    if (!StringUtils.isEmpty(detail.getIp1())) {
                        model.put("ip1", detail.getIp1());
                    }
                    if (!StringUtils.isEmpty(detail.getIp2())) {
                        model.put("ip2", detail.getIp2());
                    }
                    if (!StringUtils.isEmpty(detail.getIp3())) {
                        model.put("ip3", detail.getIp3());
                    }
                } else {
                    logger.info("最新记录是不驳回状态，entId={},requestId={}", entId, requests.get(0).getId());
                }
            }
        }

        return "ec/ecInfoEdit.ftl";
    }

    /**
     * 分解url地址
     *
     * @Title: parseCallbackUrl
     * @Author: wujiamin
     * @date 2016年10月25日
     */
    private void parseCallbackUrl(String url, ModelMap model) {
        int index = url.indexOf("://");
        if (index > 0) {
            String header = url.substring(0, index);
            String body = url.substring((index + 3), url.length());
            model.put("urlHead", header);
            model.put("urlBody", body);
        }
    }

    /**
     * 获取企业余额ajax
     *
     * @author qinqinyan
     */
    @RequestMapping("getBalanceAjax")
    public void getBalanceAjax(HttpServletResponse response, Long entId) throws IOException {
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
     * @Title: ecRecord
     * @Description: 企业EC操作记录
     * @return: String
     */
    @RequestMapping(value = "/ecRecord")
    public String ecRecord(ModelMap model, QueryObject queryObject, Long entId) {
        model.addAttribute("entId", entId);
        return "ec/ecRecord.ftl";
    }

    /**
     * 
     * @Title: ecSearch 
     * @Description:EC操作记录
     * @param queryObject
     * @param res
     * @return: void
     */
    @RequestMapping(value = "/ecSearch")
    public void ecSearch(QueryObject queryObject, HttpServletResponse res, Long entId) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        setQueryParameter("status", queryObject);
        setQueryParameter("startTime", queryObject);
        setQueryParameter("endTime", queryObject);
        String startTime = (String) queryObject.getQueryCriterias().get("startTime");
        if (StringUtils.isNotBlank(startTime)) {
            startTime += " 00:00:00";
            queryObject.getQueryCriterias().put("startTime", startTime);
        }

        String endTime = (String) queryObject.getQueryCriterias().get("endTime");
        if (StringUtils.isNotBlank(endTime)) {
            endTime += " 23:59:59";
            queryObject.getQueryCriterias().put("endTime", endTime);
        }
        queryObject.getQueryCriterias().put("entId", entId);

        // 数据库查找符合查询条件的个数
        long count = entECRecordService.showEntEcRecordCount(queryObject);
        List<EntECRecord> entECRecordList = entECRecordService.showEntEcRecordForPageResult(queryObject);

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", entECRecordList);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Title: statusRecord
     * @Description: TODO
     * @return: String
     */
    @RequestMapping(value = "/statusRecord")
    public String statusRecord(ModelMap model, QueryObject queryObject, Long entId) {
        model.addAttribute("entId", entId);
        if ("gd_mdrc".equals(getProvinceFlag())) {
            return "enterprises/statusRecord_gd.ftl";
        }
        return "enterprises/statusRecord.ftl";

    }

    /**
     * 
     * @Title: statusSearch 
     * @Description: TODO
     * @param queryObject
     * @param res
     * @param entId
     * @return: void
     */
    @RequestMapping(value = "/statusSearch")
    public void statusSearch(QueryObject queryObject, HttpServletResponse res, Long entId) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        setQueryParameter("startTime", queryObject);
        setQueryParameter("endTime", queryObject);

        String startTime = (String) queryObject.getQueryCriterias().get("startTime");
        if (StringUtils.isNotBlank(startTime)) {
            startTime += " 00:00:00";
            queryObject.getQueryCriterias().put("startTime", startTime);
        }

        String endTime = (String) queryObject.getQueryCriterias().get("endTime");
        if (StringUtils.isNotBlank(endTime)) {
            endTime += " 23:59:59";
            queryObject.getQueryCriterias().put("endTime", endTime);
        }

        queryObject.getQueryCriterias().put("entId", entId);

        // 数据库查找符合查询条件的个数
        long count = entStatusRecordService.showEntStatusRecordCount(queryObject);
        List<EntStatusRecord> entStatusRecordList = entStatusRecordService
                .showEntStatusRecordForPageResult(queryObject);

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", entStatusRecordList);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取折扣信息
     *
     * @Title: getDiscount
     */
    @RequestMapping(value = "/getDiscount")
    @ResponseBody
    public void getDiscount(HttpServletResponse response, Long customerTypeId) {
        Map result = new HashMap();
        List<Discount> discounts = discountMapper.selectAllDiscount();
        result.put("discounts", discounts);
        try {
            response.getWriter().write(JSONObject.toJSONString(result));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return;

    }

    private class EntInfo {
        private String id;
        private String text;

        public EntInfo(String id, String text) {
            this.id = id;
            this.text = text;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    /** 
     * 企业开户列表点击编辑时，检查该企业信息是否可以编辑
     * @Title: checkEnterpriseEdit 
     */
    @RequestMapping(value = "/checkEnterpriseEdit")
    public void checkEnterpriseEdit(HttpServletResponse resp, Long entId) throws IOException {
        //判断该企业是否已经提交审核
        Map map = new HashMap();
        Enterprise enter = enterprisesService.selectByPrimaryKey(entId);
        if (enter != null
                && (EnterpriseStatus.NORMAL.getCode().equals(enter.getDeleteFlag()) || EnterpriseStatus.SUBMIT_APPROVAL
                        .getCode().equals(enter.getDeleteFlag()))) {//判断是否重复提交
            map.put("msg", "fail");

        } else {
            map.put("msg", "success");
        }
        try {
            resp.getWriter().write(JSON.toJSONString(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * 
     * @Title: showStatusChangeDescAjax 
     * @Description: TODO
     * @param response
     * @param id
     * @return: void
     */
    @RequestMapping(value = "/showStatusChangeDescAjax")
    @ResponseBody
    public void showStatusChangeDescAjax(HttpServletResponse response, Long id) {
        EntStatusRecord record = null;
        Map result = new HashMap();
        if (id != null && (record = entStatusRecordService.selectByPrimaryKey(id)) != null) {
            result.put("msg", record.getOpDesc());
        } else {
            result.put("msg", "fail");
        }
        try {
            response.getWriter().write(JSONObject.toJSONString(result));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return;

    }

    /**
     * 
     * @Title: showStatusChangeDescAjax 
     * @Description: TODO
     * @param response
     * @param id
     * @return: void
     */
    @RequestMapping(value = "/enterConfirm")
    public String enterConfirm(ModelMap model, Long enterpriseId, String ecCode, String ecPrdCode, String needConfirm, String erroeMsg) {

        if (!enterprisesService.isParentManage(enterpriseId, getCurrentUserManager().getId())) {
            model.put("errorMsg", "请求异常");
            return "error.ftl";
        }
        List<EnterprisesExtInfo> enterprisesExtInfos = enterprisesExtInfoService.selectByEcCodeAndEcPrdCode(ecCode, ecPrdCode);
        if (enterprisesExtInfos == null
                || enterprisesExtInfos.size() ==0) {
            
            model.put("errorMsg", "查询不到订购信息");
            return "error.ftl";
        }
        if (!com.cmcc.vrp.util.StringUtils.isEmpty(erroeMsg)) {
            model.put("errorMsg", erroeMsg);
        }
        EnterprisesExtInfo enterprisesExtInfo = enterprisesExtInfos.get(0);
        ECSyncInfo ecSyncInfo = ecSyncInfoService.selectByECCode(ecCode);
        String innetDate = null;
        String vipTypeStateDate = null;
        if (ecSyncInfo != null) {
            innetDate = DateUtil.dateToString(ecSyncInfo.getInnetDate(),"yyyy-MM-dd HH:mm:SS");
            vipTypeStateDate = DateUtil.dateToString(ecSyncInfo.getVipTypeStateDate(),"yyyy-MM-dd HH:mm:SS");
        }

        Enterprise enterprise = enterprisesService.selectByPrimaryKeyForActivity(enterpriseId);  
        Manager districtName = entManagerService.getManagerForEnter(enterprise.getId());
        if (districtName != null) {
            String fullname = "";
            enterprise.setCmManagerName(managerService.getFullNameByCurrentManagerId(fullname,
                    districtName.getParentId()));
        }     
        
        //List<Administer> entAdministers = administerService.selectEMByEnterpriseId(enterpriseId);
        List<Administer> entAdministers = adminManagerService.getAdminForEnter(enterprise.getId());
        
        Administer entAdminister = null;
        if (entAdministers != null && !entAdministers.isEmpty()) {
            entAdminister = entAdministers.get(0);
        }
        if (entAdminister == null) {
            entAdminister = new Administer();
        }
        Administer cmAdminister = null;

        String customManagerID = globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_MANAGER_ROLE_ID.getKey());
        if (org.apache.commons.lang.StringUtils.isNotEmpty(customManagerID)) {
            Long customManagerId = Long.parseLong(customManagerID);
            List<Manager> managers = managerService.selectEntParentNodeByEnterIdOrRoleId(enterprise.getId(),
                    customManagerId);
            if (managers != null && !managers.isEmpty()) {
                List<Administer> administers = adminManagerService.getAdminByManageId(managers.get(0).getId());
                if (administers != null && !administers.isEmpty()) {
                    cmAdminister = administers.get(0);
                }
            }
        }
        if (cmAdminister == null) {
            cmAdminister = new Administer();
        }
         
        model.put("cmAdminister", cmAdminister);
        model.put("ecSyncInfo", ecSyncInfo);
        model.put("enterprise", enterprise);
        model.put("enterprisesExtInfo", enterprisesExtInfo);

        model.put("innetDate", innetDate);
        model.put("vipTypeStateDate", vipTypeStateDate);
        model.put("needConfirm", needConfirm);
        model.put("entAdminister", entAdminister);
        return "enterprises/confirm_gd.ftl";

    }
    /**
     * @param model
     * @param enterpriseId
     */
    @RequestMapping(value = "/syncEcInfo")
    public String syncEcInfo(ModelMap model, String entCode, Long entId, String entPrdCode, String needConfirm) {
        System.out.println(entCode);
        String result = qryECSyncInfoService.updateECInfo(entCode);
        XStream xStream = new XStream();
        xStream.alias("Response", Response.class);
        xStream.autodetectAnnotations(true);
        Response response = (Response) xStream.fromXML(result);
        if (!"0".equals(response.getResultCode())) {
            model.put("errorMsg", response.getResultMsg());
            return "error.ftl";
        }
        return enterConfirm(model, entId, entCode, entPrdCode, needConfirm, null);
    }
    /**
     * @param model
     * @param enterprise
     * @param eei
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping("/confirmCreateEnterprise")
    @Transactional
    public String confirmCreateEnterprise(ModelMap model, Long entId, String ecCode, String ecPrdCode,
            HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        if (StringUtils.isEmpty(name)
                || StringUtils.isEmpty(phone)) {
            return "redirect:enterConfirm.html?enterpriseId=" + entId + "&ecPrdCode=" + ecPrdCode + "&ecCode=" + ecCode +
                    "&needConfirm=true&erroeMsg=" + URLEncoder.encode("企业管理员信息不能为空！", "UTF-8");
        }
        if ((enterprisesService.selectByPrimaryKey(entId)) == null) {
            model.put("errorMsg", "获取企业信息失败");
            return "error.ftl";
        }
        Boolean flag = true;
        Administer admin = administerService.selectByMobilePhone(phone);
        List<Administer> emAdministers = adminManagerService.getAdminForEnter(entId);
//        if (emAdministers == null
//                || emAdministers.size() ==0) {
//            model.put("errorMsg", "产品管理员不存在");
//            return "error.ftl";
//        }
        if (emAdministers != null && !emAdministers.isEmpty()) {
            for (Administer administer:emAdministers) {
                if (phone.equals(administer.getMobilePhone())) {
                    flag = false;
                }
            }
        }


        //先检查该用户是否已分配了管理员身份，如果分配了管理员身份则不能操作
        if (flag && admin != null) {
            Manager manager = managerService.selectByAdminId(admin.getId());
            if (null != manager && !manager.getId().equals(-1L)) {
                model.addAttribute("errorMsg", "该用户已有管理员身份，不能设置成企业管理员！");
                //return enterConfirm(model, entId, ecCode, ecPrdCode, "true");
                return "redirect:enterConfirm.html?enterpriseId=" + entId + "&ecPrdCode=" + ecPrdCode + "&ecCode=" + ecCode +
                        "&needConfirm=true&erroeMsg=" + URLEncoder.encode("该用户已有管理员身份，不能设置成企业管理员！", "UTF-8");

            }
        }
        if (emAdministers != null && !emAdministers.isEmpty()) {
            for (Administer administer:emAdministers) {
                Administer newAdminister = new Administer();
                newAdminister.setId(administer.getId());
                newAdminister.setUserName(name);
                newAdminister.setMobilePhone(phone);
                newAdminister.setEmail(email);
                if (!administerService.updateSelective(newAdminister)) {
                    model.put("errorMsg", "产品管理员不存在");
                    return "error.ftl";
                }
            }
        } else {
            Administer cmUser = getCurrentUser();
            Administer emUser = administerService.selectByMobilePhone(phone);
            Administer newAdmin = new Administer();
            newAdmin.setMobilePhone(phone);
            newAdmin.setUserName(name);
            if (!StringUtils.isEmpty(email)) {
                newAdmin.setEmail(email);
            }
            Manager manager = entManagerService.getManagerForEnter(entId);
            if (!administerService.createAdminister(manager.getId(), newAdmin, emUser, cmUser.getId())) {
                logger.error("操作失败，用户ID-" + cmUser.getId() + "将手机号" + phone + "用户设置为ManagerId:" + manager.getId());
                throw new RuntimeException("将用户设置为企业管理员失败");
            }
        }
       
        GdZcBossServiceImpl bossService = applicationContext.getBean("gdZcBossService",
                GdZcBossServiceImpl.class);

        if (getIsCrowdfundingPlatform()) {
            EnterprisesExtInfo enterprisesExtInfo = enterprisesExtInfoService.get(entId);
            if (enterprisesExtInfo.getJoinType() != null
                    && "1".equals(enterprisesExtInfo.getJoinType().toString())
                    && !bossService.orderRelation(entId, "0")) {
                model.put("errorMsg", "大企业消息通知失败");
                return "error.ftl";
            }   
        }
        Enterprise newEnterprise = new Enterprise();
        newEnterprise.setId(entId);
        newEnterprise.setDeleteFlag(EnterpriseStatus.NORMAL.getCode());
        if (!enterprisesService.updateByPrimaryKeySelective(newEnterprise)) {
            model.put("errorMsg", "开户失败");
            return "error.ftl";
        }
       
        
        return "redirect:createEnterpriseIndex.html";
    }
    

    /**
     * @Title: 企业列表查找
     * @Author: wujiamin
     * @date 2016年10月17日下午5:20:02
     */
    @RequestMapping(value = "/searchConfirm")
    public void searchConfirm(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        String managerIdStr = getRequest().getParameter("managerId");
        if (StringUtils.isNotBlank(managerIdStr)) {
            Long managerId = NumberUtils.toLong(managerIdStr);
            if (!isParentOf(managerId)) { //当前用户不是指定用户或不是它的父节点，没有权限查看相应的节点信息
                res.setStatus(HttpStatus.SC_FORBIDDEN);
                return;
            }
        }

        /**
         * 查询参数: 企业名字、编码、效益
         */
        setQueryParameter("name", queryObject);
        setQueryParameter("code", queryObject);
        //setQueryParameter("deleteFlag", queryObject);
        queryObject.getQueryCriterias().put("deleteFlag", 14);

        if (!StringUtils.isEmpty(getRequest().getParameter("startTime"))) {
            queryObject.getQueryCriterias().put("beginTime", getRequest().getParameter("startTime"));
            queryObject.getQueryCriterias().put("endTime", getRequest().getParameter("endTime"));
        }
        if (needExtEntInfo()) {
            queryObject.getQueryCriterias().put("extEntInfoFlag", "true");
        }

        /**
         * 当前登录用户的管理员层级
         */
        Manager manager = getCurrentUserManager();
        queryObject.getQueryCriterias().put("managerId", manager.getId());

        //页面查询参数中设定的管理员层级，如果设定了就会将上面的值覆盖
        setQueryParameter("managerId", queryObject);

        // 数据库查找符合查询条件的个数
        int enterpriseCount = enterprisesService.showForPageResultCount(queryObject);
        List<Enterprise> enterpriseList = enterprisesService.showEnterprisesForPageResult(queryObject);

        if (enterpriseList != null) {
            for (Enterprise enterprise : enterpriseList) {
                //地区全称
                Manager districtName = entManagerService.getManagerForEnter(enterprise.getId());
                if (districtName != null) {
                    String fullname = "";
                    enterprise.setCmManagerName(managerService.getFullNameByCurrentManagerId(fullname,
                            districtName.getParentId()));
                }
            }
        }

        //是否展示余额字段
        addCurrentCountIfNecessary(enterpriseList);

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
    
    private List<CustomerType> getCustomerTypeList(){
        List<CustomerType> cts = customerTypeMapper.selectAll();
        List<CustomerType> out = new ArrayList();
        if("sc".equals(getProvinceFlag())){
            for(CustomerType ct : cts){
                if(!"无".equals(ct.getName())){
                    out.add(ct);
                }
            }
        }
        return out;
    }
}
