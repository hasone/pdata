package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.PresentStatus;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.MonthlyPresentRule;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.ActivityCreatorService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.MonthlyPresentRuleService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;

/**
 * 
 * @ClassName: MonthlyPresentRuleController 
 * @Description: 包月赠送
 * @author: Rowe
 * @date: 2017年7月4日 下午2:50:09
 */
@Controller
@RequestMapping("/manage/monthRule")
public class MonthlyPresentRuleController extends BaseController {

    private static Logger logger = Logger.getLogger(MonthlyPresentRuleController.class);

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    ProductService productService;

    @Autowired
    MonthlyPresentRuleService monthlyPresentRuleService;

    @Autowired
    EntProductService entProductService;
    
    @Autowired
    ActivityCreatorService activityCreatorService;
    
    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * 
     * @Title: index 
     * @Description: 包月赠送活动列表
     * @param modelMap
     * @param queryObject
     * @return
     * @return: String
     */
    @RequestMapping(value = "/index")
    public String index(ModelMap modelMap, QueryObject queryObject) {
        //充值状态列表
        modelMap.addAttribute("presentStatus", PresentStatus.toMap());
        
        if(isSdEnvironment()){
            modelMap.addAttribute("isShandong", "true");
        }
        return "monthRule/index.ftl";
    }

    /**
     * 
     * @Title: search 
     * @Description: 包月赠送搜素
     * @param queryObject
     * @param response
     * @return: void
     */
    @RequestMapping("search")
    public void search(QueryObject queryObject, HttpServletResponse response) {
        //查询条件
        setQueryParameter("status", queryObject);//活动状态

        String startTime = getRequest().getParameter("startTime");//活动创建时间之起始时间
        String endTime = getRequest().getParameter("endTime");//活动创建时间之终止时间
        if (StringUtils.isNotBlank(startTime)) {//格式化时间，起始时间默认当天凌晨
            startTime += " 00:00:00";
            queryObject.getQueryCriterias().put("startTime", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {//格式化时间，终止时间默认为当晚最晚
            endTime += " 23:59:59";
            queryObject.getQueryCriterias().put("endTime", endTime);
        }

        //只能查看自己创建的活动
        queryObject.getQueryCriterias().put("creatorId", getCurrentAdminID());

        Long count = monthlyPresentRuleService.countRules(queryObject);//统计记录条数
        List<MonthlyPresentRule> list = monthlyPresentRuleService.getRules(queryObject);//详细活动记录
        
        for(MonthlyPresentRule rule : list){
            rule.setActivityCreator(activityCreatorService.getByActId(ActivityType.MONTHLY_PRESENT, rule.getId()));
        }

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            response.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @Title: addOrEdit 
     * @Description: TODO
     * @param modelMap
     * @param presentRule
     * @return
     * @return: String
     */
    @RequestMapping("addOrEdit")
    public String addOrEdit(Model modelMap, MonthlyPresentRule rule) {
        Administer currentUser = getCurrentUser();

        //省份标识
        String provinceFlag = getProvinceFlag();
        modelMap.addAttribute("provinceFlag", provinceFlag);

        if (getEffectType()) {
            modelMap.addAttribute("effectType", 1);
        } else {
            modelMap.addAttribute("effectType", 0);
        }
        modelMap.addAttribute("productSizeLimit", getProductSizeLimit());

        //根据当前用户查询出对应的企业列表
        List<Enterprise> list = enterprisesService.getEnterpriseListByAdminId(currentUser);

        // 默认取出第一个企业的产品和余额 后续企业产品通过AJAX查询
        List<Product> products = new ArrayList<Product>();
        if (list != null && list.size() > 0) {
            Long entId = list.get(0).getId();
            //传入产品信息：山东只能选择产品编码为***1092***的产品,山东平台产品编码后六位以1092、1099、1087开头
            List<Product> productList = productService.selectAllProductsByEnterId(entId);
            
            if ("sd".equals(provinceFlag) && productList != null) {
                List<Product> sdProducts = productList;
                productList = new LinkedList<Product>();
                Iterator<Product> it = sdProducts.iterator();
                while (it.hasNext()) {
                    Product p = it.next();
                    String productCode = p.getProductCode();
                    if (productCode.length() > 6
                            && productCode.substring(productCode.length() - 6, productCode.length()).startsWith("1092")) {
                        productList.add(p);
                    }
                }
            }
            productService.sdPrdsSort(productList);
            if(productList != null){
                products.addAll(productList);
            }
        }
        //设置参数
        modelMap.addAttribute("enterprises", list);//企业列表
        modelMap.addAttribute("products", products);//产品列表
        modelMap.addAttribute("rule", rule);//规则信息
        modelMap.addAttribute("activityType", rule.getActivityType());//活动类型 0：单个赠送；1批量赠送
        return "monthRule/addOrEdit.ftl";
    }

    /**
     * 
     * @Title: save 
     * @Description: 保存变更
     * @param presentRule
     * @return
     * @return: String
     */
    @RequestMapping("save")
    public String save(Model modelMap, MonthlyPresentRule monthlyPresentRule) {

        //校验活动信息
        String errMsg = validateRule(monthlyPresentRule);

        if (StringUtils.isNotBlank(errMsg)) {//校验不通过，有错误信息，返回编辑页面
            modelMap.addAttribute("rule", monthlyPresentRule);
            modelMap.addAttribute("errMsg", errMsg);
            return addOrEdit(modelMap, monthlyPresentRule);
        }

        //创建活动
        logger.info("用户ID：" + getCurrentAdminID() + " 创建包月赠送。。。。");
        if ("sd".equals(getProvinceFlag())) {
            errMsg = monthlyPresentRuleService.create(initMonthlyPresentRule(monthlyPresentRule));
        } else {
            errMsg = monthlyPresentRuleService.unSdcreate(initMonthlyPresentRule(monthlyPresentRule));
        }
        
        if (StringUtils.isNotBlank(errMsg)) {//操作失败，返回编辑页面
            modelMap.addAttribute("rule", monthlyPresentRule);
            modelMap.addAttribute("errMsg", errMsg);
            return addOrEdit(modelMap, monthlyPresentRule);
        }

        //返回首页
        return "redirect:index.html";
    }

    /**
     * 
     * @Title: detail 
     * @Description: 包月赠送详情
     * @param modelMap
     * @param id
     * @return
     * @return: String
     */
    @RequestMapping("detail")
    public String detail(Model modelMap, Long id) {
        MonthlyPresentRule monthlyPresentRule = monthlyPresentRuleService.getDetailByRuleId(id);
        modelMap.addAttribute("rule", monthlyPresentRule);
        modelMap.addAttribute("provinceFlag", getProvinceFlag());
        return "monthRule/detail.ftl";
    }

    /**
     * 
     * @Title: initMonthlyPresentRule 
     * @Description: 初始化包月赠送规则
     * @param monthlyPresentRule
     * @return
     * @return: MonthlyPresentRule
     */
    private MonthlyPresentRule initMonthlyPresentRule(MonthlyPresentRule monthlyPresentRule) {
        if (monthlyPresentRule == null) {
            return null;
        }
        monthlyPresentRule.setCreatorId(getCurrentAdminID());//活动创建者ID
        monthlyPresentRule.setUpdaterId(getCurrentAdminID());//活动更新者ID        
        monthlyPresentRule.setStatus(PresentStatus.PROCESSING.getCode());//活动进行中
        Date now = new Date();
        if (monthlyPresentRule.getStartTime() == null) {
            monthlyPresentRule.setStartTime(now);//活动开始时间   
        }       
        monthlyPresentRule.setEndTime(DateUtil.getAfterFewMonths(now, monthlyPresentRule.getMonthCount()));//活动结束时间        
        monthlyPresentRule.setUpdateTime(now);//活动创建时间
        monthlyPresentRule.setCreateTime(now);//活动更新时间        
        monthlyPresentRule.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());//默认未删除状态
        monthlyPresentRule.setVersion(1);//版本号
        monthlyPresentRule.setUseSms(0);//不使用短信模板
        return monthlyPresentRule;
    }

    /**
     * 
     * @Title: validateRule 
     * @Description: 活动信息校验
     * @param rule
     * @return
     * @return: String
     */
    private String validateRule(MonthlyPresentRule rule) {
        //校验参数
        if (rule == null) {
            return "参数错误";
        } else if (StringUtils.isBlank(rule.getActivityName())) {
            return "参数缺失：请输入活动名称";
        } else if (rule.getEntId() == null) {
            return "参数缺失：请选择企业";
        } else if (rule.getPrdId() == null) {
            return "参数缺失：请选择产品";
        } else if (rule.getMonthCount() == null) {
            return "参数缺失：请输入赠送月数";
        } else if (rule.getMonthCount() > 12 || rule.getMonthCount() < 1) {
            return "参数错误：请输入合法的赠送月数";
        } else if (StringUtils.isBlank(rule.getPhones())) {
            return "参数错误：请输入手机号码";
        } else if (StringUtils.isNotBlank(rule.getPhones())) {
            String[] array = rule.getPhones().split(",");
            List<String> phoneList = Arrays.asList(array);
            for (String phone : phoneList) {
                if (!com.cmcc.vrp.util.StringUtils.isValidMobile(phone)) {
                    return "参数错误：存在非法手机号码：" + phone;
                }
            }
            rule.setPhonesList(phoneList);
            rule.setTotal(phoneList.size());
        }

        return null;
    }
    
    /**
     * 判断是否山东环境
     */
    private boolean isSdEnvironment(){
        return "sd".equalsIgnoreCase(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey()));
    }

}