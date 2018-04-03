package com.cmcc.vrp.province.webin.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.JXLException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.MinusCountReturnType;
import com.cmcc.vrp.exception.ProductInitException;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.AccountPrizeMap;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.PresentRecord;
import com.cmcc.vrp.province.model.PresentRule;
import com.cmcc.vrp.province.model.PrizeInfo;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.ProductPrizeMap;
import com.cmcc.vrp.province.model.RuleSmsTemplate;
import com.cmcc.vrp.province.model.ShOrderList;
import com.cmcc.vrp.province.model.SmsTemplate;
import com.cmcc.vrp.province.model.json.post.PresentRecordJson;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivityCreatorService;
import com.cmcc.vrp.province.service.AdminEnterService;
import com.cmcc.vrp.province.service.EntFlowControlService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.PresentRecordService;
import com.cmcc.vrp.province.service.PresentRuleService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.RuleSmsTemplateService;
import com.cmcc.vrp.province.service.ShOrderListService;
import com.cmcc.vrp.province.service.SmsTemplateService;
import com.cmcc.vrp.province.service.VirtualProductService;
import com.cmcc.vrp.province.service.impl.PresentRateLimitServiceImpl;
import com.cmcc.vrp.province.service.impl.PresentSingleRateLimitServiceImpl;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.SizeUnits;

/**
 * 赠送规则管理
 *
 * @author kok
 */
@Controller
@RequestMapping("/manage/giveRuleManager")
public class GiveRuleController extends BaseController {
    public static final int MAX_FILE_SIZE = 500;//单位KB
    //普通赠送图形验证码的KEY
    public final String PRESENT_IMG_CHECK_CODE_KEY = "presentImgCheckCodeKey";
    @Autowired
    PresentRuleService presentRuleService;
    @Autowired
    PresentRecordService presentRecordService;
    @Autowired
    EntProductService entProductService;
    @Autowired
    EntFlowControlService entFlowControlService;
    @Autowired
    VirtualProductService virtualProductService;
    private Logger logger = Logger.getLogger(GiveRuleController.class);
    @Autowired
    private ProductService productService;
    @Autowired
    private AdminEnterService adminEnterService;
    @Autowired
    private EnterprisesService enterprisesService;
    @Autowired
    private RuleSmsTemplateService ruleSmsTemplateService;
    @Autowired
    private SmsTemplateService smsTemplateService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    private PresentRateLimitServiceImpl presentRateLimitService;
    @Autowired
    private PresentSingleRateLimitServiceImpl presentSingleRateLimitService;
    @Autowired
    private ShOrderListService shOrderListService;
    @Autowired
    ActivityCreatorService activityCreatorService;

    /**
     * 首页
     */
    @RequestMapping(value = "/index")
    public String index(Model model, QueryObject queryObject) {

        if(queryObject != null){
            model.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        setQueryParameter("aName", queryObject);
        setQueryParameter("entName", queryObject);
        setQueryParameter("status", queryObject);

        /**
         * 设置当前登录用户
         */
        Administer administer = getCurrentUser();
        queryObject.getQueryCriterias().put("adminId", getCurrentUser().getId());

        model.addAttribute("adminId", administer.getId());

        /*根据管理员id查询相应企业,实际查找到得只有一个企业*/
        List<Enterprise> enterprises = enterprisesService.getEnterpriseListByAdminId(administer);
        model.addAttribute("enterprises", enterprises);

        /**
         * 获取当前用户角色
         */
        long roleId = administer.getRoleId();
        if (roleId == 3) {
            model.addAttribute("role", "ENTERPRISE_CONTACTOR");
        } else {
            model.addAttribute("role", "CUSTOM_MANAGER");
        }

        String status = String.valueOf(queryObject.getQueryCriterias().get("status"));
        if ("3".equals(queryObject.getQueryCriterias().get("status"))) {
            status = "3";
            queryObject.getQueryCriterias().put("status", "-1");
            queryObject.getQueryCriterias().put("deleteFlag", "1");
        }

        model.addAttribute("status", status);
        
        if(isSdEnvironment()){
            model.addAttribute("isShandong", "true");
        }

        return "giveManage/index.ftl";
    }

    /**
     * 删除规则
     */
    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request, Model model, QueryObject queryObject) {
        String ruleId = request.getParameter("ruleId");
        Administer administer = getCurrentUser();

        if (ruleId != null) {
            PresentRule presentRule = presentRuleService.selectByPrimaryKey(NumberUtils.toLong(ruleId));
            if (presentRule != null && presentRule.getCreatorId() != null) {

                //判断是否是同一用户创建的规则
                if (!presentRuleService.isSameAdminCreated(administer, NumberUtils.toLong(ruleId))) {
                    model.addAttribute("errorMsg", "对不起，该条规则不是由您创建，您无权修改该规则");
                    return "error.ftl";
                }

                if (!(presentRuleService.deleteDraft(NumberUtils.toLong(ruleId)) > 0)) {
                    // 删除失败 返回错误页面 修改点不直接返回error页面
                    model.addAttribute("errorMsg", "删除失败!");
                }
            } else {
                model.addAttribute("errorMsg", "删除失败!");

            }
        }

        logger.info("用户ID:" + getCurrentUser().getId() + " 开始批量规则赠送" + "ruleId:" + ruleId);

        return index(model, queryObject);
    }

    /**
     * 新建规则
     */
    @RequestMapping(value = "/add")
    public String add(Model modelMap, PresentRule presentRule) {
        Administer currentUser = getCurrentUser();
        if (isCreatorUser()) {
            modelMap.addAttribute("errorMsg", "当前用户没有权限!");
            return "error.ftl";
        }

        if (getEffectType()) {
            modelMap.addAttribute("effectType", 1);
        } else {
            modelMap.addAttribute("effectType", 0);
        }

        if ("chongqing".equals(getProvinceFlag()) || "xj".equals(getProvinceFlag())) { //新疆也不需要显示余额
            modelMap.addAttribute("cqFlag", 1);
        } else {
            modelMap.addAttribute("cqFlag", 0);
        }

        //选择流量池产品时是否显示账户余额，山东显示，其他省份暂时不变
        if ("sd".equals(getProvinceFlag())) { //新疆也不需要显示余额
            modelMap.addAttribute("showFlowAccount", 1);
        } else {
            modelMap.addAttribute("showFlowAccount", 0);
        }

        //根据当前用户查询出对应的企业列表
        List<Enterprise> list = enterprisesService.getEnterpriseListByAdminId(currentUser);

        // 默认取出第一个企业的产品和余额 后续企业产品通过AJAX查询
        List<Product> products = new ArrayList<Product>();
        if (list != null && list.size() > 0) {
            products.addAll(productService.selectAllProductsByEnterId(list.get(0).getId()));
            setEntAccount(modelMap, list.get(0).getId()); //设置企业余额
            productService.sdPrdsSort(products);
        }
        if ("shanghai".equals(getProvinceFlag())) {
            modelMap.addAttribute("showOrderList", 1);
            List<ShOrderList> shOrderLists = shOrderListService.getByEnterId(list.get(0).getId());
            modelMap.addAttribute("orderList", shOrderLists);
        } else {
            modelMap.addAttribute("showOrderList", 0);
        }

        //设置参数
        modelMap.addAttribute("enterprises", list);
        modelMap.addAttribute("products", products);
        modelMap.addAttribute("smsTemplates", smsTemplateService.showSmsTemplate(null));
        modelMap.addAttribute("rule", presentRule);

        modelMap.addAttribute("maxFlowSize",
                globalConfigService.get(GlobalConfigKeyEnum.MAX_SIZE_VIRTRUAL_FLOW_PRODUCT.getKey()));

        modelMap.addAttribute("maxCoinSize",
                globalConfigService.get(GlobalConfigKeyEnum.GIVE_VIRTUAL_COIN_MAX.getKey()));

        //是否需要图形验证码, 需要就设置属性，否则不设置
        if (needImageCodeCheck()) {
            modelMap.addAttribute("needImgCheckCode", "on");
        }

        return "giveManage/add.ftl";
    }

    //设置企业账户现金余额
    private void setEntAccount(Model modelMap, Long entId) {
        Account account = null;

        if ((account = accountService.getCurrencyAccount(entId)) != null) {
            modelMap.addAttribute("account", account.getCount());
            return;
        }

        modelMap.addAttribute("account", null);
    }

    @RequestMapping(value = "/getBalanceAjax")
    public void getBalanceAjax(HttpServletResponse response, HttpServletRequest request) {

        Long proId = NumberUtils.toLong(request.getParameter("proId"));
        Long entId = NumberUtils.toLong(request.getParameter("entId"));
        Account account = accountService.get(entId, proId, AccountType.ENTERPRISE.getValue());

        try {
            PrintWriter out;
            out = response.getWriter();
            if (account != null) {
                out.print(account.getCount().intValue());
                out.flush();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return;
    }

    /*
     * 跳转到修改界面
     */
    @RequestMapping(value = "/editRule")
    public String editRule(Model model, String phones, Long ruleId) {
        Administer currentUser = getCurrentUser();

        if (isCreatorUser()) {
            model.addAttribute("errorMsg", "对不起，您没有权限修改该规则");
            return index(model, null);
        }

        PresentRule presentRule = presentRuleService.selectByPrimaryKey(ruleId != null ? ruleId : 0);

        if (presentRule != null && presentRule.getUseSms().intValue() == 1) {
            List<RuleSmsTemplate> smsTemplates = ruleSmsTemplateService.getRuleSmsTemplateByCreator(
                    currentUser.getId(), "CG");
            model.addAttribute("smsTemplates", smsTemplates);
        }

        // 显示当前登录用户的企业及所有产品
        if (presentRule != null) {
            List<Enterprise> enterprises = adminEnterService.selecteEntByAdminId(currentUser.getId());

            List<Product> products = productService.selectAllProductsByEnterId(presentRule.getEntId());

            List<PresentRecord> list = presentRecordService.selectByRuleId(presentRule.getId());

            model.addAttribute("presentRule", presentRule);
            model.addAttribute("enterprises", enterprises);
            model.addAttribute("products", products);

            // by qihang,根据前台传输是否为空判断是取数据库，还是前台数据
            if (StringUtils.isEmpty(phones)) {
                model.addAttribute("phones", toPhones(list));
                model.addAttribute("phoneNumber", list.size());
            } else {
                String[] phoneNums = split(phones);
                model.addAttribute("phones", toPhones(list));
                model.addAttribute("phoneNumber", phoneNums.length);
            }

            List<String> phoneList = toPhonesList(list);
            model.addAttribute("phoneList", phoneList);
        }

        return "giveManage/edit.ftl";
    }

    /**
     * 查看规则详细
     */
    @RequestMapping(value = "/detail")
    public String ruleDetails(long id, Model model) {        
        if (id > 0) {
            PresentRule presentRule = presentRuleService.selectRuleDetails(id);
            Manager manager = getCurrentUserManager();
            if (presentRule == null
                    || manager == null
                    || !enterprisesService.isParentManage(presentRule.getEntId(), manager.getId())) {
                model.addAttribute("errorMsg", "请求异常");
                return "error.ftl";
            }
            int total = presentRecordService.count(id);
            model.addAttribute("record", presentRule);
            model.addAttribute("total", total);

            SmsTemplate smsTemplate = smsTemplateService.selectByPrimaryKey(presentRule.getSmsTemplateId());
            model.addAttribute("smsContent", smsTemplate);

            return "giveManage/detail.ftl";
        }
        return "redirect:index.html";

    }

    /**
     * 保存规则（批量赠送）
     */
    @RequestMapping(value = "/addRule")
    public void addRule(String batchImgCheckCode, HttpServletRequest request, Model model, PresentRule presentRule,
            HttpServletResponse response) throws IOException {
        logger.info("用户ID:" + getCurrentUser().getId() + "点击批量赠送");
        Map<String, String> returnMap = new LinkedHashMap<String, String>();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain; charset=UTF-8");

        //校验图形验证码
        if (needImageCodeCheck()) {
            if (!checkImgCode(batchImgCheckCode, request)) {
                model.addAttribute("errorMsg", "图形验证码不正确！");
                returnMap.put("result", "fail");
                returnMap.put("msg", "图形验证码不正确！");
                response.getWriter().write(JSON.toJSONString(returnMap));
                return;
            }
        }

        String entId = request.getParameter("entId");

        //校验频率限制
        try {
            presentRateLimitService.allowToContinue(entId);
        } catch (RuntimeException e) {
            model.addAttribute("errorMsg", e.getMessage());
            returnMap.put("result", "fail");
            returnMap.put("msg", e.getMessage());
            response.getWriter().write(JSON.toJSONString(returnMap));
            return;
        }

        String aName = request.getParameter("aName");
        if (aName.length() > 100) {
            model.addAttribute("errorMsg", "活动名称过长!");
            returnMap.put("result", "fail");
            returnMap.put("msg", "活动名称过长!");
            response.getWriter().write(JSON.toJSONString(returnMap));
            return;
        }

        if (!StringUtils.isNotBlank(entId) || !StringUtils.isNotBlank(aName)) {
            model.addAttribute("errorMsg", "赠送活动基本信息不完整!");
            returnMap.put("result", "fail");
            returnMap.put("msg", "赠送活动基本信息不完整");
            response.getWriter().write(JSON.toJSONString(returnMap));
            return;
        }

        String json = request.getParameter("records");
        List<PresentRecordJson> prs = JSON.parseArray(json, PresentRecordJson.class);
        if (prs.size() <= 0) {
            model.addAttribute("errorMsg", "赠送对象为空!");
            returnMap.put("msg", "赠送对象为空!");
            returnMap.put("result", "fail");
            response.getWriter().write(JSON.toJSONString(returnMap));
            return;
        }

        Long enterId = NumberUtils.toLong(entId);
        Enterprise enterprise2 = enterprisesService.selectByPrimaryKey(enterId);
        if (enterprise2 == null || enterprise2.getDeleteFlag() != 0
                || (enterprise2.getStartTime() != null && enterprise2.getStartTime().after(new Date()))
                || (enterprise2.getEndTime() != null && (new Date()).after(enterprise2.getEndTime()))) {

            model.addAttribute("errorMsg", "企业处于非正常状态！");
            returnMap.put("msg", "企业处于非正常状态！");
            returnMap.put("result", "fail");
            response.getWriter().write(JSON.toJSONString(returnMap));
            return;
        }

        //检查产品是否与企业关联
        for (PresentRecordJson recordJson : prs) {
            EntProduct entProduct = entProductService.selectByProductIDAndEnterprizeID(recordJson.getPrdId(), enterId);
            if (entProduct == null) {
                returnMap.put("msg", "产品不存在");
                returnMap.put("result", "fail");
                response.getWriter().write(JSON.toJSONString(returnMap));
                return;
            }
        }

        //1、流量池、话费产品转化 2、流量包不转化
        try {
            prs = virtualProductService.batchInitProcess(prs);
        } catch (ProductInitException e) {
            model.addAttribute("errorMsg", e.getMessage());
            returnMap.put("msg", e.getMessage());
            returnMap.put("result", "fail");
            response.getWriter().write(JSON.toJSONString(returnMap));
            return;
        }

        //检查账户余额
        //获得奖品的具体列表（重复的奖品需将数量累加）
        //List<PrizeInfo> list = new ArrayList<PrizeInfo>();
        String batchPresentSn = SerialNumGenerator.buildSerialNum();
        for (PresentRecordJson recordJson : prs) {

            MinusCountReturnType checkType = accountService.minusCount(NumberUtils.toLong(entId),
                    recordJson.getPrdId(), AccountType.ENTERPRISE, (double) recordJson.getGiveNum(), batchPresentSn,
                    "批量赠送", false);

            if (!checkType.equals(MinusCountReturnType.OK)) {
                returnMap.put("msg", checkType.getMsg());
                returnMap.put("result", "fail");
                response.getWriter().write(JSON.toJSONString(returnMap));
                return;
            }

        }

        //1、保存规则
        presentRule.setaName(aName);
        presentRule.setEntId(Long.parseLong(entId));

        try {
            presentRuleService.addRule(getCurrentUser(), presentRule, prs);
        } catch (Exception e) {
            returnMap.put("msg", e.getMessage());
            returnMap.put("result", "fail");
            response.getWriter().write(JSON.toJSONString(returnMap));
            return;
        }

        logger.info("用户ID:" + getCurrentUser().getId() + " 成功添加赠送记录" + "ruleId:" + presentRule.getId() + " 企业Id："
                + presentRule.getEntId());

        //检查企业是否是正常状态
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(Long.parseLong(entId));
        if (enterprise != null && enterprise.getDeleteFlag() != 0) {
            model.addAttribute("errorMsg", "企业不处于开通状态，无法进行赠送操作!");
            returnMap.put("msg", "企业不处于开通状态，无法进行赠送操作!");
            returnMap.put("result", "fail");
            response.getWriter().write(JSON.toJSONString(returnMap));
            return;
        }

        // 开始赠送
        logger.info("用户ID:" + getCurrentUser().getId() + "点击了直接赠送按钮," + "ruleId:" + presentRule.getId() + "已开始赠送");

        if (give(presentRule.getId(), model, presentRule, batchPresentSn)) {
            //增加频率限制
            presentRateLimitService.add(entId);

            returnMap.put("result", "success");
            returnMap.put("msg", "赠送成功!");
            response.getWriter().write(JSON.toJSONString(returnMap));
            return;
        }

        returnMap.put("result", "success");
        response.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }

    /**
     * 计算总的奖品所花费的总流量数
     */
    private Double calculateSumCost(List<ProductPrizeMap> ppMaps) {
        Double sum = 0d;
        for (ProductPrizeMap map : ppMaps) {
            Long size = map.getPrizeSize();
            Long count = map.getPrizeCount();
            sum += (size * count);
        }
        return sum;
    }

    private List<ProductPrizeMap> generatePPMap(List<Product> products, List<PrizeInfo> prizes) {
        if (products == null || products.isEmpty() || prizes == null || prizes.isEmpty()
                || products.size() != prizes.size()) {
            return null;
        }
        List<ProductPrizeMap> list = new ArrayList<ProductPrizeMap>();
        for (Product product : products) {
            for (PrizeInfo prize : prizes) {
                if (product.getId().equals(prize.getProductId())) {
                    ProductPrizeMap map = new ProductPrizeMap();
                    map.setPrizeCount(prize.getCount());
                    map.setPrizeSize(product.getProductSize());
                    list.add(map);
                }
            }
        }
        return list;
    }

    private Double calculTotalSum(List<Product> products, List<PrizeInfo> prizes, Long entId) {
        if (products == null || products.isEmpty() || prizes == null || prizes.isEmpty()
                || products.size() != prizes.size()) {
            return 0d;
        }

        Double totalSum = 0d;
        for (Product product : products) {
            EntProduct entProduct = entProductService.selectByProductIDAndEnterprizeID(product.getId(), entId);
            int discount = 0;

            if (entProduct != null && entProduct.getDiscount() != null) {
                discount = entProduct.getDiscount();
            }

            for (PrizeInfo prize : prizes) {
                if (product.getId().equals(prize.getProductId())) {
                    totalSum = totalSum + prize.getCount() * product.getPrice() * discount / 100d;
                }
            }
        }
        return totalSum;
    }

    //校验入参
    private String validate(Model model, Long entId, String aName, Long prdId, String phone, PresentRule presentRule,
            String userInputImgCheckCode, HttpServletRequest request) {
        //基本校验
        if (entId == null || StringUtils.isBlank(aName) || prdId == null) {
            model.addAttribute("errorMsgSingle", "赠送活动基本信息不完整!");

            return add(model, presentRule);
        }

        //校验活动名称长度
        if (aName.length() > 100) {
            model.addAttribute("errorMsgSingle", "活动名称过长!");
            return add(model, presentRule);
        }

        //校验手机号码
        if (!com.cmcc.vrp.util.StringUtils.isValidMobile(phone)) {
            model.addAttribute("errorMsgSingle", "非法手机号码!");
            return add(model, presentRule);
        }

        //校验图形验证码
        if (needImageCodeCheck()) {
            if (!checkImgCode(userInputImgCheckCode, request)) {
                model.addAttribute("errorMsgSingle", "图形验证码不正确！");
                return add(model, presentRule);
            }
        }

        //校验频率限制
        try {
            presentSingleRateLimitService.allowToContinue(String.valueOf(entId));
        } catch (RuntimeException e) {
            model.addAttribute("errorMsgSingle", e.getMessage());
            return add(model, presentRule);
        }

        //校验企业信息
        Enterprise enterprise2 = enterprisesService.selectByPrimaryKey(entId);
        if (enterprise2 == null || enterprise2.getDeleteFlag() != 0
                || (enterprise2.getStartTime() != null && enterprise2.getStartTime().after(new Date()))
                || (enterprise2.getEndTime() != null && (new Date()).after(enterprise2.getEndTime()))) {
            model.addAttribute("errorMsgSingle", "企业状态异常！");
            logger.info("企业状态异常或企业合作时间已过期，企业对象：" + JSON.toJSONString(enterprise2));

            return add(model, presentRule);
        }

        //恭喜！全部校验通过！
        return null;
    }

    //校验余额是否足够
    private String validateAccount(Long prdId, Model model, Long entId, PresentRule presentRule) {
        //检查账户余额
        List<PrizeInfo> list = buildPrizeInfos(prdId);
        List<Account> accounts = accountService.getByEntIdAndProId(entId, list);
        List<AccountPrizeMap> maps = generateMap(accounts, list);
        if (maps == null) {
            // 产品账户为空时候，检查资金账户是否负债
            List<Product> products = productService.getProductsByPrizes(list);
            List<ProductPrizeMap> ppMaps = generatePPMap(products, list);
            Double sum = calculateSumCost(ppMaps);
            Account account = accountService.getCurrencyAccount(entId);
            if (account != null && !accountService.isEnough2Debt(account, sum)) {
                model.addAttribute("errorMsgSingle", "企业账户余额不足!");
                return add(model, presentRule);
            }

            //检测是否达到预警值或暂停值
            /*String checkValue = accountService.checkAlertStopValue(entId, calculTotalSum(products,list,entId));
            if(checkValue != null){
                model.addAttribute("errorMsgSingle", checkValue);
                return add(model, presentRule);
            }*/

        } else {
            // 产品账户不为空时候 检查产品和资金账户是否负债
            if (accountService.isDebt2Account(maps, entId)) {
                model.addAttribute("errorMsgSingle", "企业账户余额不足!");
                return add(model, presentRule);
            }
        }

        //恭喜！账户余额测试通过！
        return null;
    }

    private List<PrizeInfo> buildPrizeInfos(Long prdId) {
        List<PrizeInfo> list = new LinkedList<PrizeInfo>();

        PrizeInfo prizeInfo = new PrizeInfo();
        prizeInfo.setProductId(prdId);
        prizeInfo.setCount(1L);
        list.add(prizeInfo);

        return list;
    }

    private List<PresentRecordJson> buildPresentRecords(String phone, Long prdId, Integer effectType) {
        List<PresentRecordJson> prs = new LinkedList<PresentRecordJson>();

        PresentRecordJson pr = new PresentRecordJson();
        pr.setGiveNum(1);
        pr.setPhones(phone);
        pr.setPrdId(prdId);
        pr.setEffectType(effectType);
        prs.add(pr);

        return prs;
    }

    /**
     * 保存规则（单独赠送）
     */
    @RequestMapping(value = "/addRuleSingle")
    public String addRuleSingle(HttpServletRequest request, Long entId, String aName, Long prdId, String prdSize,
            String phone, PresentRule presentRule, String imgCheckCode, Model model, String effect) throws IOException {
        //1. 校验参数
        String validateResult = null;

        if (StringUtils.isNotBlank(validateResult = validate(model, entId, aName, prdId, phone, presentRule,
                imgCheckCode, request))) {
            return validateResult;
        }
        Integer effectType;
        if (effect == null) {
            effectType = 1;
        } else {
            effectType = NumberUtils.toInt(effect);
        }

        //2. 校验产品
        EntProduct entProduct = entProductService.selectByProductIDAndEnterprizeID(prdId, entId);
        if (entProduct == null) {
            model.addAttribute("errorMsgSingle", "产品不存在！");
            return add(model, presentRule);
        }

        //3、流量池、话费产品转化 2、流量包不转化
        Product product = productService.get(prdId);
        if (product == null) {
            model.addAttribute("errorMsgSingle", "产品不存在！");
            return add(model, presentRule);
        }
        try {
            if (!StringUtils.isBlank(prdSize) && product.getType().byteValue() == ProductType.FLOW_ACCOUNT.getValue()) {//前端传过来的产品大小单位为MB
                Long productSize = Long.valueOf(prdSize);
                //MB转KB
                productSize = SizeUnits.MB.toKB(productSize);
                prdSize = String.valueOf(productSize);
            }
            product = virtualProductService.initProcess(prdId, prdSize);
            if (product == null) {
                model.addAttribute("errorMsgSingle", "产品不存在！");
                return add(model, presentRule);
            }
        } catch (ProductInitException e) {
            model.addAttribute("errorMsgSingle", e.getMessage());
            return add(model, presentRule);
        } catch (NumberFormatException e) {
            model.addAttribute("errorMsgSingle", "产品大小格式错误！");
            return add(model, presentRule);
        }
        prdId = product.getId();
        String batchPresentSn = SerialNumGenerator.buildSerialNum();
        MinusCountReturnType checkType = accountService.minusCount(entId, prdId, AccountType.ENTERPRISE, 1.0,
                batchPresentSn, "二维码", false);

        if (!checkType.equals(MinusCountReturnType.OK)) {
            model.addAttribute("errorMsgSingle", checkType.getMsg());
            return add(model, presentRule);
        }
        //企业余额、暂停值、预警值校验，TODO
        //        //4. 校验企业预警值，暂停值
        //        Product cash = productService.getCurrencyProduct();
        //        Account account = accountService.get(entId, cash.getId(), AccountType.ENTERPRISE.getValue());
        //
        //        Integer type = product.getType();
        //        Double price = 0d;
        //        if (type == Constants.ProductType.MOBILE_FEE.getValue()) {
        //            price = (double) product.getPrice();
        //        } else if (type == Constants.ProductType.FLOW_ACCOUNT.getValue()) {
        //            price = (double) product.getPrice();
        //        } else if (type == ProductType.FLOW_PACKAGE.getValue()) {
        //            price = product.getPrice() * entProduct.getDiscount() / 100.0;
        //        }
        //        if (account == null || account.getStopCount().longValue() != 0
        //                && (account.getCount().longValue() - price.longValue() < account.getStopCount().longValue())) {
        //            model.addAttribute("errorMsgSingle", "消费后余额低于暂停值,赠送失败！");
        //            return add(model, presentRule);
        //        }
        //
        //        //4. 校验余额
        //        if (StringUtils.isNotBlank(validateResult = validateAccount(prdId, model, entId, presentRule))) {
        //            return validateResult;
        //        }

//        //5. 检查企业流控
//        if ("OK".equalsIgnoreCase(getEntFlowControlFlag())
//                && StringUtils.isNotBlank(validateResult = isFlowControl(prdId, model, entId, presentRule))) {
//            return validateResult;
//        }

        List<PresentRecordJson> prs = buildPresentRecords(phone, product.getId(), effectType);

        //6. 校验通过后增加赠送规则
        if (!presentRuleService.addRule(getCurrentUser(), presentRule, prs)) {
            logger.error("增加普通赠送记录时出错.");
            model.addAttribute("errorMsgSingle", "增加普通赠送记录时出错！");
            return add(model, presentRule);
        } else {
            logger.info("用户ID:" + getCurrentUser().getId() + " 成功添加赠送记录" + "ruleId:" + presentRule.getId() + " 企业Id："
                    + presentRule.getEntId());
        }

        //5. 开始赠送
        logger.info("用户ID:" + getCurrentUser().getId() + "点击了直接赠送按钮," + "ruleId:" + presentRule.getId() + "已开始赠送");
        if (!give(presentRule.getId(), model, presentRule, batchPresentSn)) {
            model.addAttribute("errorMsgSingle", "赠送失败!");
            return add(model, presentRule);
        }

        //6. 增加频率
        presentSingleRateLimitService.add(String.valueOf(entId));

        return "redirect:index.html";
    }

    /**
     * 保存修改的规则
     */
    @RequestMapping(value = "/updateRule")
    public String updateRule(String phones, Integer type, String ruleId, HttpServletRequest request, Model model,
            PresentRule presentRule) {
        // 显示当前登录用户的企业及所有产品

        PresentRule modifyRule = presentRuleService.selectByPrimaryKey(NumberUtils.toLong(ruleId));

        Administer administer = getCurrentUser();

        // 判断查找到的红包对应的企业是否归该用户所有
        if (adminEnterService.selectCountByAdminIdEntId(administer.getId(), modifyRule.getEntId()) == 0) {
            model.addAttribute("errorMsg", "对不起，您没有权限修改该规则");
            return editRule(model, phones, NumberUtils.toLong(ruleId));
        }

        //判断是否是同一用户创建的规则
        if (!presentRuleService.isSameAdminCreated(administer, NumberUtils.toLong(ruleId))) {
            model.addAttribute("errorMsg", "对不起，该条规则不是由您创建，您无权修改该规则");
            return editRule(model, phones, NumberUtils.toLong(ruleId));
        }

        String[] phoneArray = split(phones);
        if (phoneArray == null || phoneArray.length == 0) {
            model.addAttribute("errorMsg", "对不起，提交的电话号码不能为空");
            return editRule(model, phones, NumberUtils.toLong(ruleId));
        }

        // 从boss端判断添加的电话号码的个数是否超过boss的存量
        /*String msg = entProductService.checkPhoneNumsValid(
                presentRule.getEntId(), presentRule.getPrdId(),
        		phoneArray.length);
        if (StringUtils.isNotBlank(msg)) {
        	model.addAttribute("phones", phones);
        	model.addAttribute("errorMsg", msg);
        	return editRule(model, phones, NumberUtils.toLong(ruleId));
        }
        */
        setPresentRule(ruleId, presentRule);

        if (presentRule.getUseSms().intValue() == 0) {
            presentRule.setSmsTemplateId(null);
        }

        if (!presentRuleService.updateRule(getCurrentUser(), presentRule, split(phones), type)) {
            model.addAttribute("errorMsg", "修改失败");
            return editRule(model, phones, NumberUtils.toLong(ruleId));
        }

        logger.info("用户ID:" + getCurrentUser().getId() + " 成功更改批量赠送" + "ruleId:" + presentRule.getId() + " 企业Id："
                + presentRule.getEntId() + "电话号码为：" + phones);

        return "redirect:index.html";
    }

    /**
     * 上传号码
     */
    @RequestMapping(method = RequestMethod.POST, value = "/uploadPhones")
    public void uploadFileAjax(@RequestParam("file") MultipartFile file, HttpServletResponse response) {

        //设置响应给前台内容的数据格式
        response.setContentType("text/plain; charset=UTF-8");
        List<String> list = new ArrayList<String>();
        InputStream is = null;
        if (file == null || file.isEmpty()) {
            try {
                PrintWriter out = response.getWriter();

                String returnMsg = "上传文件不能为空";
                out.print("1;" + returnMsg);
                out.flush();
                return;

            } catch (IOException e) {

            }
        }
        //fix bug PDATA-1609 : 对号码个数进行限制，不限制文件大小，号码最多为20000 罗祖武 20170710
        //        else if (file.getSize() > MAX_FILE_SIZE * 1024) {
        //            try {
        //                PrintWriter out = response.getWriter();
        //
        //                String returnMsg = "超过文件大小限制（" + MAX_FILE_SIZE + "KB）";
        //                out.print("1;" + returnMsg);
        //                out.flush();
        //                return;
        //
        //            } catch (IOException e) {
        //
        //            }
        //
        //        } 
//        else if (file.getSize() > MAX_FILE_SIZE * 1024) {
//            try {
//                PrintWriter out = response.getWriter();
//
//                String returnMsg = "超过文件大小限制（" + MAX_FILE_SIZE + "KB）";
//                out.print("1;" + returnMsg);
//                out.flush();
//                return;
//
//            } catch (IOException e) {
//
//            }
//
//        } 
        else {
            try {

                PrintWriter out = response.getWriter();
                String fileSuffix = file.getOriginalFilename();

                if (fileSuffix != null) {
                    fileSuffix = fileSuffix.split("\\.")[fileSuffix.split("\\.").length - 1];
                }

                File tempFile = File.createTempFile(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()),
                        fileSuffix);

                file.transferTo(tempFile);

                is = new FileInputStream(tempFile);

                int validPhonesNum = 0;//合法的电话数

                if (fileSuffix != null && "txt".equals(fileSuffix)) {
                    readText(list, is);
                } else if (fileSuffix != null && "xlsx".equals(fileSuffix)) {
                    readXlsx(list, is);
                } else if (fileSuffix != null && "xls".equals(fileSuffix)) {
                    readXls(list, is);
                } else {
                    //只支持文件
                    String returnMsg = "只支持上传txt、xls和xlsx文件";
                    is.close();
                    FileUtils.deleteQuietly(tempFile);
                    out.print("1;" + returnMsg);
                    out.flush();
                    return;

                }

                FileUtils.deleteQuietly(tempFile);
                validPhonesNum = list.size();                          
                String p = concatPhones(list);
                if (validPhonesNum > 0 && validPhonesNum <= 20000) {
                    out.print("0;" + validPhonesNum + ";" + p);
                    out.flush();
                } else if(validPhonesNum <= 0){
                    out.print("1;" + "对不起，您本次提交的文件中没有符合电话格式规范的数据");
                    out.flush();
                } else{
                    out.print("1;" + "对不起，已超出20000个手机号码，请重新上传");
                    out.flush();                  
                }
            } catch (IOException e) {
                logger.error("上传文件异常", e);
            } catch (JXLException e) {
                logger.error("解析excel错误", e);
            } catch (Exception e) {
                logger.error("上传文件格式异常", e);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 赠送
     */

    public Boolean give(Long ruleId, Model model, PresentRule presentRule, String batchPresentSn) {
        boolean fla = false;

        Administer administer = getCurrentUser();

        if (presentRule != null && ruleId > 0) {
            presentRule.setId(ruleId);

            //判断是否是同一用户创建的规则
            if (!presentRuleService.isSameAdminCreated(administer, ruleId)) {
                model.addAttribute("errorMsg", "对不起，该条规则不是由您创建，您无权修改该规则");
                return false;
            }

            // ruleId!=null and ruleId>0 直接赠送
            fla = presentRuleService.give(getCurrentUser(), presentRule, batchPresentSn);
        } else if (presentRule != null && presentRule.geteName() != null) {
            // 新增界面 赠送
            fla = addOnGive(presentRule, batchPresentSn);
        }

        if (fla) {

            //            PresentRule record = presentRuleService.selectByPrimaryKey(ruleId);

            //            List<PresentRecord> presentRecords = presentRecordService.selectByRuleId(ruleId);

            /*if(record.getUseSms().intValue()==1 && presentRecords != null && presentRecords.size() != 0){
                usmsgService.SCsendSmsToUsers(record, presentRecords);
            }*/

            logger.info("用户ID:" + getCurrentUser().getId() + " 开始批量规则赠送" + "ruleId:" + presentRule.getId());
            return true;
        }
        model.addAttribute("errorMsg", "赠送失败!");

        return false;
    }

    /**
     * 先保存再赠送
     */
    boolean addOnGive(PresentRule presentRule, String batchPresentSn) {
        if (presentRule != null) {
            // 赠送
            return presentRuleService.give(getCurrentUser(), presentRule, batchPresentSn);
        }
        return false;
    }

    /**
     * 判断是否是创建人
     *
     * @Title:isCreatorUser
     * @Description: TODO
     * @author: hexinxu
     */
    private boolean isCreatorUser() {
        if (!isCustomManager() && !isEnterpriseContactor()) {
            return true;
        }
        return false;
    }

    /**
     * 分割手机号码 同时去除不规则手机号码
     *
     * @Title:split
     * @Description: TODO
     * @author: hexinxu
     */
    private String[] split(String phones) {

        String[] phoneArray = com.cmcc.vrp.util.StringUtils.split(phones, ",");
        List<String> list = new ArrayList<String>();
        for (String s : phoneArray) {

            // if (com.cmcc.vrp.util.StringUtils.isValidMobile(s)) {
            list.add(s);
            // }
        }
        return listToArray(list);
    }

    /**
     * 字符串转List
     *
     * @Title:toList
     * @author: hexinxu
     */
    public List<String> toList(String str) {
        if (str != null) {
            String[] st = split(str);
            if (st.length >= 1) {
                return new ArrayList<String>(Arrays.asList(st));
            }

        }
        return null;
    }

    /**
     * list转手机字符串
     *
     * @Title:toPhones
     * @author: hexinxu
     */
    public String toPhones(List<PresentRecord> list) {
        StringBuffer buffer = new StringBuffer();
        if (list != null) {
            for (PresentRecord p : list) {
                buffer.append(p.getMobile() + ",");
            }
        }
        if (buffer.toString().trim().length() > 1) {
            return buffer.toString().substring(0, buffer.toString().length() - 1);
        }
        return buffer.toString();
    }

    /**
     * 规则记录list转字符串list
     *
     * @Title:toPhonesList
     * @author: hexinxu
     */
    public List<String> toPhonesList(List<PresentRecord> list) {
        List<String> returnList = new ArrayList<String>();
        if (list != null) {
            for (PresentRecord p : list) {
                returnList.add(p.getMobile());
            }
        }
        return returnList;
    }

    /**
     * 规则记录list转字符串数组
     *
     * @Title:toPhonesList
     * @author: hexinxu
     */
    public String[] listToArray(List<String> list) {
        String[] phones = null;
        if (list != null) {
            phones = new String[list.size()];
            list.toArray(phones);
            return phones;
        }
        return phones = new String[0];
    }

    /**
     * 设置规则参数
     *
     * @Title:setPresentRule
     * @Description: TODO
     * @author: hexinxu
     */
    public void setPresentRule(String ruleId, PresentRule presentRule) {
        if (presentRule != null) {
            if (ruleId != null && ruleId.trim().length() > 0) {
                presentRule.setId(NumberUtils.toLong(ruleId));
            }
            if (presentRule.geteName() != null && presentRule.geteName().trim().length() > 2) {
                presentRule.setEntId(NumberUtils.toLong(toList(presentRule.geteName()).get(0)));
            }
        }
    }

    /**
     * 赠送记录的搜索
     */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 设置当前用户ID
         */
        //        Administer administer = getCurrentUser();
        queryObject.getQueryCriterias().put("adminId", getCurrentUser().getId());
        /**
         * 查询参数: 企业名字、红包状态
         */
        setQueryParameter("aName", queryObject);
        setQueryParameter("entName", queryObject);
        setQueryParameter("status", queryObject);
        setQueryParameter("startTime", queryObject);
        setQueryParameter("endTime", queryObject);

        int presentRulecount = presentRuleService.queryCount(queryObject);
        List<PresentRule> prList = presentRuleService.queryPage(queryObject);
        
        /**
         * 填充活动创建者
         */
        for(PresentRule rule:prList){
            rule.setActivityCreator(activityCreatorService.getByActId(ActivityType.GIVE, rule.getId()));
        }

        JSONObject json = new JSONObject();
        JSONArray jsonarray = new JSONArray();

        for (PresentRule pr : prList) {
            JSONObject jo = new JSONObject();
            jo.put("aName", pr.getaName());
            jo.put("eName", pr.geteName());
            jo.put("total", pr.getTotal());
            jo.put("status", pr.getStatus());
            jo.put("createTime", pr.getCreateTime());
            jo.put("updateTime", pr.getUpdateTime());
            jo.put("id", pr.getId());
            jo.put("activityCreator", pr.getActivityCreator());
            jsonarray.add(jo);
        }

        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", jsonarray);
        json.put("total", presentRulecount);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<AccountPrizeMap> generateMap(List<Account> accounts, List<PrizeInfo> prizes) {
        if (accounts == null || accounts.isEmpty() || prizes == null || prizes.isEmpty()
                || accounts.size() != prizes.size()) {
            return null;
        }
        List<AccountPrizeMap> list = new ArrayList<AccountPrizeMap>();
        for (Account account : accounts) {
            for (PrizeInfo prize : prizes) {
                if (account.getProductId().longValue() == prize.getProductId().longValue()) {
                    AccountPrizeMap map = new AccountPrizeMap();
                    map.setAccountCount(account.getCount());
                    map.setPrizeCount(prize.getCount());
                    map.setPrizeSize(account.getProductSize());
                    map.setPrdId(prize.getProductId());
                    list.add(map);
                }
            }
        }
        return list;
    }

    //解析Txt文件,返回成功总行数
    public int readText(List<String> list, InputStream inputStream) throws IOException {
        BufferedReader buReader = new BufferedReader(new InputStreamReader(inputStream));
        String in = null;
        int rowNums = 0;
        while ((in = buReader.readLine()) != null) {
            in = in.trim();
            rowNums++;
            if (judge(in)) {
                list.add(in);
            }
        }
        return rowNums;
    }

    @SuppressWarnings("resource")
    private int readXlsx(List<String> list, InputStream in) throws Exception {
        Workbook wb = new XSSFWorkbook(in); //Excel 2007

        //读取第一张表格内容
        XSSFSheet sheet = (XSSFSheet) wb.getSheetAt(0);

        XSSFRow row = null;

        int realAddCnt = 0;

        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {

            row = sheet.getRow(i);

            Cell cell1 = row.getCell(0);

            if (cell1.getCellType() == Cell.CELL_TYPE_NUMERIC) {

                //使用DecimalFormat类对科学计数法格式的数字进行格式化
                BigDecimal bd = new BigDecimal(cell1.getNumericCellValue());
                String phone = bd.toPlainString();

                if (judge(phone)) {
                    list.add(phone);
                    realAddCnt++;
                }
            }
        }
        return realAddCnt;
    }

    @SuppressWarnings("resource")
    private int readXls(List<String> list, InputStream in) throws Exception {
        Workbook wb = new HSSFWorkbook(in);

        //读取第一张表格内容
        Sheet sheet = wb.getSheetAt(0);

        Row row = null;

        int realAddCnt = 0;

        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {

            row = sheet.getRow(i);

            Cell cell1 = row.getCell(0);

            if (cell1.getCellType() == Cell.CELL_TYPE_NUMERIC) {

                //使用DecimalFormat类对科学计数法格式的数字进行格式化
                BigDecimal bd = new BigDecimal(cell1.getNumericCellValue());
                String phone = bd.toPlainString();

                if (judge(phone)) {
                    list.add(phone);
                    realAddCnt++;
                }
            }
        }
        return realAddCnt;
    }

    private String concatPhones(List<String> list) {
        StringBuffer buffer = new StringBuffer();
        if (list == null || list.size() == 0) {
            return "";
        } else {
            for (String phone : list) {
                buffer.append(phone + ",");
            }
        }

        String phones = buffer.toString();
        phones = phones.substring(0, phones.length() - 1);

        return phones;
    }

    //判断号码是否为正常的号码
    private boolean judge(String mobile) {
        return Pattern.compile("^1[3-8][0-9]{9}$").matcher(mobile).matches();
    }

    //是否已经打开图形验证码验证
    private boolean needImageCodeCheck() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.PRESENT_SWITCH.getKey());
        return StringUtils.isNotBlank(value) && "ON".equalsIgnoreCase(value);
    }

    //校验图形验证码
    private boolean checkImgCode(String userInputImgCode, HttpServletRequest request) {
        String expectedValue = (String) request.getSession().getAttribute(PRESENT_IMG_CHECK_CODE_KEY);
        return StringUtils.isNotBlank(expectedValue) && expectedValue.equalsIgnoreCase(userInputImgCode);
    }

    private boolean isFlowControl(List<AccountPrizeMap> maps, Long enterId) {

        Double money = 0.0;
        for (AccountPrizeMap ap : maps) {
            Product p = productService.selectProductById(ap.getPrdId());
            EntProduct entProduct = entProductService.selectByProductIDAndEnterprizeID(ap.getPrdId(), enterId);
            if (p != null && entProduct != null) {
                money += ap.getPrizeCount() * p.getPrice() * entProduct.getDiscount() / 100.0;
            }
        }
        return entFlowControlService.isFlowControl(money, enterId, false);
    }

    private String isFlowControl(Long prdId, Model model, Long entId, PresentRule presentRule) {
        Double money = 0.0;
        Product p = productService.selectProductById(prdId);
        if (p == null) {
            model.addAttribute("errorMsgSingle", "企业流控异常!");
            return add(model, presentRule);
        }
        Integer type = p.getType();
        if (type == Constants.ProductType.MOBILE_FEE.getValue()) {
            money = p.getPrice() / 100.0;
        } else if (type == Constants.ProductType.FLOW_ACCOUNT.getValue()) {
            money = p.getPrice() / 100.0;
        } else if (type == ProductType.FLOW_PACKAGE.getValue()) {
            EntProduct entProduct = entProductService.selectByProductIDAndEnterprizeID(prdId, entId);
            money = p.getPrice() * entProduct.getDiscount() / 100.0;
        }

        if (!entFlowControlService.isFlowControl(money, entId, false)) {
            model.addAttribute("errorMsgSingle", "企业流控异常!");
            return add(model, presentRule);
        }

        return null;
    }

    public String getEntFlowControlFlag() {
        return globalConfigService.get(GlobalConfigKeyEnum.ENTERPRISE_FLOW_CONTROL.getKey());
    }

    /**
     * 
     * @Title: getAccountInfo 
     * @Description: 显示账户详情
     * @param entId
     * @param prdId
     * @param response
     * @return: void
     * @throws IOException 
     */
    @RequestMapping(value = "/getAccountInfo")
    public void getAccountInfo(Long entId, Long prdId, HttpServletResponse response) throws IOException {
        Account account = null;
        String result = "";
        DecimalFormat df = new DecimalFormat("#########.##");
        Map<String, String> returnMap = new LinkedHashMap<String, String>();
        Product product = productService.selectProductById(prdId);
        if (product == null) {
            result = "账户信息不存在";
        } else if (ProductType.FLOW_PACKAGE.getValue() == product.getType().byteValue()) {//流量包账户显示资金余额
            account = accountService.getCurrencyAccount(entId);
        } else if(ProductType.PRE_PAY_PRODUCT.getValue() == product.getType().byteValue()){//预付费产品账户显示该账户资金余额
            account = accountService.getPaypreAccByType(entId, ProductType.PRE_PAY_CURRENCY);
        } else {//其他账户显示具体详情
            account = accountService.get(entId, prdId, AccountType.ENTERPRISE.getValue());
        }

        if (account == null) {
            result = "账户信息不存在";
        } else if (ProductType.FLOW_ACCOUNT.getValue() == product.getType().byteValue()) {//流量池账户，单位为MB           
            result = df.format(account.getCount() / 1024) + " MB";
        } else {//其他账户显示钱
            result = df.format(account.getCount() / 100) + " 元";
        }
        returnMap.put("result", result);
        response.getWriter().write(JSON.toJSONString(returnMap));
    }
    
    /**
     * 判断是否山东环境
     */
    private boolean isSdEnvironment(){
        return "sd".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()));
    }
}
