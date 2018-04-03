package com.cmcc.vrp.weixin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.enums.IndividualProductType;
import com.cmcc.vrp.enums.IspType;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualAccountRecord;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.model.WxAdminister;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountRecordService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.province.service.WxAdministerService;
import com.cmcc.vrp.province.webin.controller.BaseController;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.wx.ExchangeService;

/**
 * ExchangeController.java
 * @author wujiamin
 * @date 2017年3月17日
 */
@Controller
@RequestMapping("/wx/exchange")
public class ExchangeController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeController.class);
    @Autowired
    IndividualProductService individualProductService;
    @Autowired
    IndividualAccountService accountService;
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    PhoneRegionService phoneRegionService;
    @Autowired
    ExchangeService exchangeService;
    @Autowired
    IndividualAccountRecordService accountRecordService;

    @Autowired
    WxAdministerService wxAdministerService;
    
    /** 
     * @Title: index 
     */
    @RequestMapping("index")
    public String index(HttpServletRequest request, ModelMap model){
        //获取用户信息        
        String wxAspect = (String) request.getAttribute("wxAspect");
        if(StringUtils.isEmpty(wxAspect)){
            return "gdzc/404.ftl";
        }else if("done".equals(wxAspect)){
            WxAdminister admin = getWxCurrentUser(); 
            if(admin == null){
                LOGGER.info("session中adminId为空");
                return "gdzc/404.ftl";
            }else{
                model.put("currentMobilePhone", admin.getMobilePhone());
                IndividualProduct product = individualProductService.getIndivialPointProduct();
                if(product == null || product.getId() == null){
                    LOGGER.info("平台未配置个人积分产品");
                    return "gdzc/404.ftl";
                }
                IndividualAccount account = accountService.getAccountByOwnerIdAndProductId(admin.getId(), product.getId(), 
                        IndividualAccountType.INDIVIDUAL_BOSS.getValue());
                if(account!=null){
                    model.put("score", account.getCount());
                    String rule = globalConfigService.get(GlobalConfigKeyEnum.WEIXIN_SCORE_EXCHANGE_RULE.getKey());
                    if(!StringUtils.isEmpty(rule)){
                        model.put("rule", rule);                        
                        model.put("size", account.getCount().longValue()*Integer.parseInt(rule));//规则是1个积分可以兑换几M流量，所以可兑换的流量=规则*账户中积分数
                    }                    
                }else{
                    model.put("score", 0);
                    model.put("size", 0);                    
                }                
                
                List<IndividualProduct> products = individualProductService.selectByType(IndividualProductType.FLOW_PACKAGE.getValue());
                model.put("products", products);
                
                return "weixin/exchange/exchangeFlow.ftl";
            }
        }else{
            return wxAspect;
        }
   
    }
    
    
    /** 
     * 检查手机号
     * @Title: operate 
     */
    @RequestMapping("checkMobile")
    public void checkMobile(String mobile, HttpServletRequest request, HttpServletResponse response){      
        JSONObject json = new JSONObject();
        boolean result = false;
        PhoneRegion phoneRegion = phoneRegionService.query(mobile);
        if(phoneRegion!=null && "广东".equals(phoneRegion.getProvince()) && IspType.CMCC.getValue().equals(phoneRegion.getSupplier())){
            result = true;
        }
        json.put("result", result);            
        try {
            response.getWriter().write(json.toJSONString());
        } catch (IOException e) {                
            e.printStackTrace();
        }
    }
    
    /** 
     * 执行兑换操作
     * @Title: operate 
     */
    @RequestMapping("operate")
    public void operate(String mobile, String prdCode, HttpServletRequest request, HttpServletResponse response){
        JSONObject json = new JSONObject();
        json.put("result", "success");
        
        WxAdminister admin = getWxCurrentUser();
        if(admin == null){
            LOGGER.info("当前用户为空");
            json.put("result", "fail");
        }else{
            if(!exchangeService.operateExchange(mobile, admin.getId(), prdCode, json)){
                LOGGER.info("流量兑换失败，兑换人adminId={}，兑换手机号mobile={}，prdCode={}, json={}", admin.getId(), mobile, prdCode, json.toJSONString());
            }
        }
            
        try {
            response.getWriter().write(json.toJSONString());
        } catch (IOException e) {                
            e.printStackTrace();
        }
    }
    

    /** 
     * 显示兑换记录页面
     * @Title: record 
     */
    @RequestMapping("record")
    public String record(HttpServletRequest request, HttpServletResponse response){
        return "weixin/exchange/exchangeRecord.ftl";
    }
    
    /** 
     * 查询兑换记录列表
     * @Title: queryRecord 
     */
    @RequestMapping("queryRecord")
    public void queryRecord(QueryObject queryObject, HttpServletRequest request, int pageSize, int pageNo, HttpServletResponse response){
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        JSONObject json = new JSONObject();
        json.put("result", "success");
        
        WxAdminister admin = getWxCurrentUser();
        /**
         * 分页转换
         */
        queryObject.setPageNum(pageNo);
        queryObject.setPageSize(pageSize);
        
        queryObject.getQueryCriterias().put("activityType", ActivityType.POINTS_EXCHANGR.getCode());
        queryObject.getQueryCriterias().put("adminId", admin.getId());
        List<IndividualAccountRecord> records = accountRecordService.selectByMap(queryObject.toMap());
        
        try {
            Map result = new HashMap();
            result.put("data", records);
            response.getWriter().write(JSONObject.toJSONString(result));            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
