package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.FinanceStatus;
import com.cmcc.vrp.province.model.EntBillRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.EntBillService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.util.QueryObject;

/**
 * EntBillController.java
 * @author wujiamin
 * @date 2016年12月27日
 */
@Controller
@RequestMapping("/manage/entBill")
public class EntBillController extends BaseController {
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    EntBillService entBillService;
    
    /** 
     * 企业账单首页(客户经理)
     * @Title: index 
     */
    @RequestMapping("index")
    public String index(ModelMap map, Long enterId, String chargeTime, QueryObject queryObject){
        if(queryObject != null){
            map.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        Manager currentManager = getCurrentUserManager();
        if(currentManager == null || currentManager.getId() == null){
            return getLoginAddress();
        }
        Long managerId = currentManager.getId();
        //1、查询该用户下属的企业列表
        List<Enterprise> enters = enterprisesService.getAllEnterByManagerId(managerId);
        map.put("enters", enters);
        if(enterId != null){
            Enterprise enter = enterprisesService.selectByPrimaryKey(enterId);
            map.put("enterName", enter.getName());
            map.put("enterId", enterId);
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        if(StringUtils.isEmpty(chargeTime)){
            chargeTime = dateFormat.format(new Date());
        }
        
        map.put("chargeTime", chargeTime);
    	
        return "entBill/index.ftl";
    }
    
    /** 
     * 企业账单搜索
     * @Title: search 
     */
    @RequestMapping("search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 查询参数: 企业Id， 日期， 类型
         */
        setQueryParameter("enterId", queryObject);
        setQueryParameter("chargeTime", queryObject);
        setQueryParameter("type", queryObject);
        setQueryParameter("phone", queryObject);

        String entId = (String) queryObject.toMap().get("enterId");
        String type = (String) queryObject.toMap().get("type");
        queryObject.getQueryCriterias().put("financeStatus", FinanceStatus.fromType(type));            
        
        // 数据库查找符合查询条件的个数
        int count = 0;
        List<EntBillRecord> list = new ArrayList<EntBillRecord>();
        if(!StringUtils.isEmpty(entId)){
            // 数据库查找符合查询条件的个数
            count = entBillService.showPageCount(queryObject);
            list = entBillService.showPageList(queryObject);
        }

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("price", entBillService.sumEntBillPriceByMap(queryObject.toMap()));
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /** 
     * 调账确认页面
     * @Title: changeConfirm 
     */
    @RequestMapping("changeConfirm")
    public String changeConfirm(String recordSystemNums, Long enterId, String chargeTime, ModelMap map) {
    	//1、获取企业信息
    	Enterprise enterprise = enterprisesService.selectByPrimaryKey(enterId);
    	map.put("enterprise", enterprise);
    	//2、获取充值记录
    	map.put("recordSystemNums", recordSystemNums);
    	//3、获取账单日期
    	map.put("chargeTime", chargeTime);
    	return "entBill/changeConfirm.ftl";
    }
    
    @RequestMapping("confirmSearch")
    public void confirmSearch(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 查询参数
         */
        setQueryParameter("enterId", queryObject);
        setQueryParameter("recordSystemNums", queryObject);

        // 数据库查找符合查询条件的个数
        int count = entBillService.showPageCount(queryObject);;
        List<EntBillRecord> list = entBillService.showPageList(queryObject);

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("price", entBillService.sumEntBillPriceByMap(queryObject.toMap()));
        json.put("queryObject", queryObject.toMap());
        if(list!=null && list.size()>0){
            if(list.get(0).getFinanceStatus().equals(FinanceStatus.OUT.getCode())
        			|| list.get(0).getFinanceStatus().equals(FinanceStatus.ACCOUNT_CHANGE_OUT.getCode())){
                json.put("type", "调账未出账");
            }
            if(list.get(0).getFinanceStatus().equals(FinanceStatus.IN.getCode())
        			|| list.get(0).getFinanceStatus().equals(FinanceStatus.ACCOUNT_CHANGE_IN.getCode())){
                json.put("type", "调账出账");
            }
        }

        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @RequestMapping("submitChangeApproval")
    public String submitChangeApproval(String recordSystemNums, Long enterId, String chargeTime){
    	ModelMap map = new ModelMap();
    	map.put("enterId", enterId);
    	
    	return "redirect:index.html?enterId=" + enterId + "&&chargeTime=" + chargeTime;
    }
}
