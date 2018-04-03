package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.MdrcBatchConfigInfoService;
import com.cmcc.vrp.province.service.MdrcCardmakeDetailService;
import com.cmcc.vrp.province.service.MdrcMakecardRequestConfigService;
import com.cmcc.vrp.province.service.MdrcTemplateService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.QueryObject;

/**
 * @ClassName: MdrcChargeSearchController
 * @Description: 营销卡充值查询控制器
 * @author: qinqinyan
 * @date: 2017/08/14
 */
@Controller
@RequestMapping("/manage/mdrc/charge")
public class MdrcChargeSearchController extends BaseController {
    @Autowired
    MdrcBatchConfigService mdrcBatchConfigService;
    @Autowired
    MdrcMakecardRequestConfigService mdrcMakecardRequestConfigService;
    @Autowired
    MdrcCardmakeDetailService mdrcCardmakeDetailService;
    @Autowired
    ProductService productService;
    @Autowired
    MdrcBatchConfigInfoService mdrcBatchConfigInfoService;
    @Autowired
    MdrcTemplateService mdrcTemplateService;
    @Autowired
    MdrcCardInfoService mdrcCardInfoService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    ChargeRecordService chargeRecordService;

    /**
     * @param modelMap
     * @param queryObject
     * @return
     * @Title: listRecord
     * @Description: 
     * @return: String
     */
    @RequestMapping("index")
    public String index(ModelMap modelMap, QueryObject queryObject) {
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        return "mdrcCardInfo/chargeIndex.ftl";
    }

    /**
     * @param queryObject
     * @param res
     * @Title: search
     * @Description: 搜索
     * @return: void
     */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse res, HttpServletRequest req) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        //年份
        String year = (String) req.getParameter("year");
        if (StringUtils.isBlank(year)) {//默认当前年份
            year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        }
        //状态
        String status = (String) req.getParameter("status");
        if (StringUtils.isNotBlank(status)) {
            List<Integer> statusList = new ArrayList<Integer>();
            statusList.add(Integer.valueOf(status));
            queryObject.getQueryCriterias().put("statusList", statusList);
        }

        //卡号
        setQueryParameter("cardNumber", queryObject);
        //充值手机号
        setQueryParameter("mobile", queryObject);

        long count = 0;
        List<ChargeRecord> list = null;

        List<Enterprise> enterprises = enterprisesService.getNormalEnterByManagerId(Long
                .valueOf(getCurrentUserManager().getId()));
        if (enterprises != null && enterprises.size() > 0) {
            queryObject.getQueryCriterias().put("enterprises", enterprises);
            list = chargeRecordService.getMdrcChargeRecords(year, queryObject.toMap());
            count = chargeRecordService.countMdrcChargeRecords(year, queryObject.toMap());
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

}
