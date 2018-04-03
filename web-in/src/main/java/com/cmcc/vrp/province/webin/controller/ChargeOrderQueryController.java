package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.FinanceStatus;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qinqinyan on 2017/1/3.
 * 报表查询服务控制器
 */
@Controller
@RequestMapping("/manage/chargeOrder")
public class ChargeOrderQueryController extends BaseController{
    //private static final Logger logger = Logger.getLogger(ChargeOrderQueryController.class);

    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    ChargeRecordService chargeRecordService;
    @Autowired
    PhoneRegionService phoneRegionService;

    /**
     * 报表查询
     * @param modelMap
     * @author qinqinyan
     * */
    @RequestMapping("index")
    public String index(ModelMap modelMap, QueryObject queryObject){
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        return "entBill/orderQueryIndex.ftl";
    }

    /**
     * 报表查询
     * @param queryObject
     * @param res
     * @author qinqinyan
     * */
    @RequestMapping("search")
    public void search(QueryObject queryObject, HttpServletResponse res){
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        List<ChargeRecord> chargeRecords = null;
        Long count = 0L;

        /**
         * 查询参数
         */
        setQueryParameter("entName", queryObject);
        setQueryParameter("mobile", queryObject);

        Manager manager = getCurrentUserManager();
        if(manager!=null){
            List<Enterprise> enterprises = enterprisesService.getEnterByManagerId(manager.getId());
            if(enterprises!=null && enterprises.size()>0){
                queryObject.getQueryCriterias().put("enterprises", enterprises);

                //条件1：限定只能是不曾调账过的记录
                List<Integer> financeStatusList = new ArrayList<Integer>();
                financeStatusList.add(FinanceStatus.OUT.getCode());
                financeStatusList.add(FinanceStatus.IN.getCode());
                queryObject.getQueryCriterias().put("financeStatusList", financeStatusList);

                //条件2：限定不在调账中
                queryObject.getQueryCriterias().put("changeAccountStatus", 0);

                //条件3：限定已经发送充值请求
                queryObject.getQueryCriterias().put("status", ChargeRecordStatus.PROCESSING.getCode());

                /**
                 * 由于请求时长最终是转化成最早充值时间，所以这里有如下几种情况
                 * 1、时间范围未选择，请求时长未选择；——>直接默认当前时间倒退三个月
                 * 2、选择时间范围，未选择时长; ——> 搜索条件是选择时间范围
                 * 3、未选择时间范围，选择时长；——> 搜索条件是时长转化成时间范围
                 * 4、选择时间范围，选择时长；——> 搜索时间范围为二者交集
                 * */
                //条件4：限定请求数据的时间范围
                if(StringUtils.isEmpty(getRequest().getParameter("startTime"))
                    && StringUtils.isEmpty(getRequest().getParameter("requestTimeRange"))){

                    //时间范围未选择，请求时长未选择 ——> 直接默认当前时间倒退三个月
                    queryObject.getQueryCriterias().put("startTime",
                        DateUtil.dateToString(DateUtil.getCurrentDateOfFewMonths(new Date(), -3), "yyyy-MM-dd 00:00:00"));

                } else if(!StringUtils.isEmpty(getRequest().getParameter("startTime"))
                    && StringUtils.isEmpty(getRequest().getParameter("requestTimeRange"))){

                    //选择时间范围，未选择时长; ——> 搜索条件是选择时间范围
                    queryObject.getQueryCriterias().put("startTime", getRequest().getParameter("startTime"));
                    queryObject.getQueryCriterias().put("endTime", getRequest().getParameter("endTime") + " 23:59:59");

                } else if(StringUtils.isEmpty(getRequest().getParameter("startTime"))
                    && !StringUtils.isEmpty(getRequest().getParameter("requestTimeRange"))){

                    //未选择时间范围，选择时长；——> 搜索条件是时长转化的时间为endTime，默认搜索三个为的时间为startTime
                    queryObject.getQueryCriterias().put("startTime",
                        DateUtil.dateToString(DateUtil.getCurrentDateOfFewMonths(new Date(), -3), "yyyy-MM-dd 00:00:00"));
                    queryObject.getQueryCriterias().put("endTime",
                        DateUtil.dateToString(DateUtil.getDateByHours(new Date(), Integer.parseInt(getRequest().getParameter("requestTimeRange"))),
                            "yyyy-MM-dd HH:mm:ss"));

                } else {
                    //选择时间范围，选择时长；——> 搜索时间范围为二者交集,比较endTime
                    queryObject.getQueryCriterias().put("startTime", getRequest().getParameter("startTime"));

                    Date end = DateUtil.parse("yyyy-MM-dd HH:mm:ss", getRequest().getParameter("endTime") + " 23:59:59"); //时间选择器选择的结束时间
                    Date timeRange = DateUtil.getDateByHours(new Date(), Integer.parseInt(getRequest().getParameter("requestTimeRange"))); //时长计算出来的结束时间
                    if(end.getTime()>timeRange.getTime()){
                        queryObject.getQueryCriterias().put("endTime", DateUtil.dateToString(timeRange, "yyyy-MM-dd HH:mm:ss"));
                    }else{
                        queryObject.getQueryCriterias().put("endTime", DateUtil.dateToString(end, "yyyy-MM-dd HH:mm:ss"));
                    }
                }

                chargeRecords = chargeRecordService.queryChargeRecord(queryObject);
                count = chargeRecordService.countChargeRecord(queryObject);

                if(chargeRecords!=null && chargeRecords.size()>0){

                    for(ChargeRecord item : chargeRecords){
                        //查询手机号码归属地
                        /*PhoneRegion phoneRegion = phoneRegionService.query(item.getPhone());
                        if (phoneRegion == null) {
                            item.setDistrict("未查询到归属地");
                        }else{
                            item.setDistrict(phoneRegion.getProvince());
                        }
*/
                        //计算请求时长
                        item.setTimeRange(DateUtil.caculateTimeRange(item.getChargeTime(), new Date()));
                    }
                }
            }
        }
        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", chargeRecords);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
