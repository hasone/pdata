package com.cmcc.vrp.individual.controller;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.service.IndividualBossService;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.IndividualFlowcoinRecord;
import com.cmcc.vrp.province.service.IndividualFlowcoinRecordService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.webin.controller.BaseController;
import com.cmcc.vrp.util.QueryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * 业务查询controller
 *
 * @author wujiamin
 * @date 2016年9月27日下午5:04:09
 */
@Controller
@RequestMapping("/individual/query")
public class QueryController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(QueryController.class);

    @Autowired
    IndividualFlowcoinRecordService individualFlowcoinRecordService;

    @Autowired
    IndividualProductService individualProductService;

    @Autowired
    IndividualBossService individualBossService;

    /**
     * 显示流量余额
     *
     * @param request
     * @return
     * @Title: flowQuery
     * @Author: wujiamin
     * @date 2016年10月9日上午11:47:00
     */
    @RequestMapping("flow")
    public String flowQuery(HttpServletRequest request) {
        String mobile = (String) request.getSession().getAttribute("mobile");
        if (StringUtils.isEmpty(mobile)) {
            return getLoginAddress();
        }
        /*SCFreeMinQryResponse result = new SCFreeMinQryResponse();
    	individualBossService.queryFlow(mobile, result);
    	
    	SCFreeMinQryResponseFeeList list = result.getOutData().getFeeList();
    	if(list!=null){
    		
    	}*/


        return "individual/search/flow_balance.ftl";
    }

    /**
     * 显示流量币余额
     *
     * @return
     * @Title: flowcoinQuery
     * @Author: wujiamin
     * @date 2016年10月9日上午11:52:11
     */
    @RequestMapping("flowcoin")
    public String flowcoinQuery() {
        return "individual/search/flowCoin_balance.ftl";
    }

    /**
     * 流量币收入
     *
     * @param queryObject
     * @param res
     * @Title: searchInRecord
     * @Author: wujiamin
     * @date 2016年10月9日上午11:52:24
     */
    @RequestMapping("inRecord")
    public void searchInRecord(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        Administer admin = getCurrentUser();
        if (admin == null) {
            logger.info("无法获取当前用户信息");
            return;
        }

        setQueryParameter("startDate", queryObject);
        setQueryParameter("endDate", queryObject);

        queryObject.getQueryCriterias().put("adminId", admin.getId());
        queryObject.getQueryCriterias().put("type", AccountRecordType.INCOME.getValue());

        // 数据库查找符合查询条件的个数
        int count = individualFlowcoinRecordService.countByMap(queryObject.toMap());
        List<IndividualFlowcoinRecord> list = individualFlowcoinRecordService.selectByMap(queryObject.toMap());


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
     * 流量币支出
     *
     * @param queryObject
     * @param res
     * @Title: searchOutRecord
     * @Author: wujiamin
     * @date 2016年10月9日上午11:52:35
     */
    @RequestMapping("outRecord")
    public void searchOutRecord(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        Administer admin = getCurrentUser();
        if (admin == null) {
            logger.info("无法获取当前用户信息");
            return;
        }

        setQueryParameter("mobile", queryObject);
        setQueryParameter("startDate", queryObject);
        setQueryParameter("endDate", queryObject);

        queryObject.getQueryCriterias().put("adminId", admin.getId());
        queryObject.getQueryCriterias().put("type", AccountRecordType.OUTGO.getValue());

        // 数据库查找符合查询条件的个数
        int count = individualFlowcoinRecordService.countByMap(queryObject.toMap());
        List<IndividualFlowcoinRecord> list = individualFlowcoinRecordService.selectByMap(queryObject.toMap());

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
