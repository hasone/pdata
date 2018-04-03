package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.MonthlyPresentRecord;
import com.cmcc.vrp.province.service.MonthlyPresentRecordService;
import com.cmcc.vrp.util.QueryObject;
/**
 * 
 * @ClassName: MonthlyPresentRuleController 
 * @Description: 包月赠送记录
 * @author: Rowe
 * @date: 2017年7月4日 下午2:50:09
 */
@Controller
@RequestMapping("/manage/monthRecord")
public class MonthlyPresentRecordController extends BaseController {

    private static Logger logger = Logger.getLogger(MonthlyPresentRecordController.class);
    
    @Autowired
    MonthlyPresentRecordService monthlyPresentRecordService;

    /**
     * 
     * @Title: index 
     * @Description: 首页
     * @param modelMap
     * @param queryObject
     * @return
     * @return: String
     */
    @RequestMapping(value = "/index")
    public String index(ModelMap modelMap, QueryObject queryObject, Long ruleId) {
        
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        //充值状态列表
        modelMap.addAttribute("chargeRecordStatus", ChargeRecordStatus.toMap());
        modelMap.addAttribute("ruleId", ruleId);
        modelMap.addAttribute("provinceFlag", getProvinceFlag());
        return "monthRecord/index.ftl";

    }
  
    /**
     * 
     * @Title: search 
     * @Description: 搜索
     * @param queryObject
     * @param response
     * @return: void
     */
    @RequestMapping("search")
    public void search(QueryObject queryObject, HttpServletResponse response) {
        //查询条件
        setQueryParameter("ruleId", queryObject);//活动ID
        setQueryParameter("status", queryObject);//充值结果
        setQueryParameter("mobile", queryObject);//充值手机号码
        setQueryParameter("giveMonth", queryObject);//充值手机号码
        
        Long count = monthlyPresentRecordService.countRecords(queryObject);//统计记录条数
        List<MonthlyPresentRecord> list = monthlyPresentRecordService.getRecords(queryObject);//详细活动记录
        
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
}