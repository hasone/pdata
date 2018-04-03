package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.RedisUtilService;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ApprovalRequestStatus;
import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.MdrcBatchConfigStatus;
import com.cmcc.vrp.province.mdrc.enums.MdrcCardStatus;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.MdrcActiveDetail;
import com.cmcc.vrp.province.model.MdrcActiveRequestConfig;
import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.model.MdrcCardInfo;
import com.cmcc.vrp.province.model.MdrcTemplate;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.MdrcActiveDetailService;
import com.cmcc.vrp.province.service.MdrcActiveRequestConfigService;
import com.cmcc.vrp.province.service.MdrcTemplateService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.MdrcChargePojo;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.IpUtils;
import com.cmcc.vrp.util.QueryObject;

/**
 * @ClassName: MdrcCardInfoController
 * @Description: 营销卡管理
 * @author: Rowe 
 * @date: 2016年5月20日 上午11:11:22
 */
@Controller
@RequestMapping("/manage/mdrc/cardinfo")
public class MdrcCardInfoController extends BaseController {

    @Autowired
    MdrcCardInfoService mdrcCardInfoService;
    @Autowired
    MdrcBatchConfigService mdrcBatchConfigService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    ProductService productService;
    @Autowired
    MdrcTemplateService mdrcTemplateService;
    @Autowired
    GlobalConfigService globalConfigService;

    @Autowired
    TaskProducer taskProducer;
    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Autowired
    ApprovalRequestService approvalRequestService;
    @Autowired
    MdrcActiveDetailService mdrcActiveDetailService;
    @Autowired
    MdrcActiveRequestConfigService mdrcActiveRequestConfigService;
    @Autowired
    ApprovalRecordService approvalRecordService;
    @Autowired
    RedisUtilService redisUtilService;
    @Autowired
    PhoneRegionService phoneRegionService;
    @Autowired
    ChargeRecordService chargeRecordService;
    @Autowired
    ManagerService managerService;

    private static String chargeTimeLimitPerMonthPrefix = "MDRC_MAX_SIZE_PER_MONTH_";

    private static String mdrcChargePrefix = "MDRC_CHARGE_";
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @param modelMap
     * @param queryObject
     * @return
     * @Title: index
     * @Description: 卡数据列表
     * @return: String
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request, ModelMap modelMap, QueryObject queryObject) {

        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        String currCardStatus = request.getParameter("currentCardStatus");

        //规则ID
        setQueryParameter("configId", queryObject);
        //年份
        setQueryParameter("year", queryObject);
        //卡号
        setQueryParameter("cardNumber", queryObject);
        //卡状态
        setQueryParameter("status", queryObject);

        modelMap.addAttribute("configId", queryObject.getQueryCriterias().get("configId"));
        modelMap.addAttribute("year", queryObject.getQueryCriterias().get("year"));
        modelMap.addAttribute("cardNumber", queryObject.getQueryCriterias().get("cardNumber"));
        modelMap.addAttribute("status", queryObject.getQueryCriterias().get("status"));

        modelMap.addAttribute("cardStatus", MdrcCardStatus.toMap());//营销卡状态

        //获取当前卡状态
        String configId = request.getParameter("configId");
        MdrcBatchConfig config = null;
        if (!StringUtils.isEmpty(configId)
                && (config = mdrcBatchConfigService.select(Long.parseLong(configId))) != null) {
            currCardStatus = config.getStatus().toString();
        }

        modelMap.addAttribute("currCardStatus", currCardStatus);

        //充值失败时， 是否需要重新充值的开关
        modelMap.addAttribute("needRecharge", needRecharge());
        /**
         * 客户经理进入另外一个页面
         * */
        /*if(isCustomManager()){
            return "mdrcActiveApproval/index.ftl";
        }*/

        return "mdrcCardInfo/index.ftl";
    }

    /**
     * @param queryObject
     * @param request
     * @param res
     * @Title: search
     * @Description: 卡数据搜索
     * @return: void
     */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletRequest request, HttpServletResponse res) {

        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        //查询条件：规则ID、年份、卡号、状态
        setQueryParameter("configId", queryObject);

        setQueryParameter("year", queryObject);

        setQueryParameter("cardNumber", queryObject);

        setQueryParameter("status", queryObject);

        //        queryObject.getQueryCriterias().put("manageId", getCurrentUser().getId());

        Long count = 0L;
        List<MdrcCardInfo> list = new ArrayList<MdrcCardInfo>();
        
        String configId = (String)queryObject.getQueryCriterias().get("configId");

        if (StringUtils.isNotBlank(configId) && !mdrcBatchConfigService.isOverAuth(getCurrentAdminID(), Long.valueOf(configId))) {
            count = mdrcCardInfoService.queryMdrcCount(queryObject);
            list = mdrcCardInfoService.queryMdrcList(queryObject);
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
     * @param modelMap
     * @param year
     * @param id
     * @return
     * @Title: detail
     * @Description: 卡数据详情
     * @return: String
     */
    @RequestMapping("/detail")
    public String detail(ModelMap modelMap, int year, Long id, HttpServletRequest request) {
        
        Manager manager = getCurrentUserManager();
        if(manager==null){
            return getLoginAddress();
        }

        modelMap.addAttribute("year", year);
        if (id != null && year > 0) {
            MdrcCardInfo record = mdrcCardInfoService.selectByPrimaryKey(year, id);
            if (record != null) {
                MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.select(record.getConfigId());
                if(mdrcBatchConfig!=null){
                    if(!managerService.managedByManageId(mdrcBatchConfig.getEnterpriseId(), manager.getId())){
                        modelMap.addAttribute("errorMsg", "对不起，您无权限查看该条记录。");
                        return "error.ftl";
                    }
                    
                    modelMap.addAttribute("record", record);
                    modelMap.addAttribute("cardStatus", MdrcCardStatus.toMap());//营销卡状态
                    return "mdrcCardInfo/detail.ftl";
                }
            }
        }
        return index(request, modelMap, null);
    }

    /**
     * @param modelMap
     * @param configId
     * @param year
     * @return
     * @Title: pieStatistics
     * @Description: 饼状统计图
     * @return: String
     */
    @RequestMapping("/pieStatistics")
    public String pieStatistics(ModelMap modelMap, Long configId, int year) {

        Map<String, Long> map = mdrcCardInfoService.pieStatistics(year, configId);

        String[] rdata = toData(map, "pie");
        toModel(modelMap, map);
        modelMap.addAttribute("nameData", rdata[0]);
        modelMap.addAttribute("valueData", rdata[1]);
        modelMap.addAttribute("configId", configId);
        modelMap.addAttribute("year", year);

        return "mdrcCardInfo/pieStatistics.ftl";
    }

    /**
     * @titile:toModel
     * */
    void toModel(ModelMap modelMap, Map<String, Long> map) {

        Long count = 0L;
        for (String key : map.keySet()) {
            if (map.get(key) != null) {
                count += map.get(key);
            }
        }
        modelMap.put("cardStatus", MdrcCardStatus.toMap());
        modelMap.put("statusStatistics", map);
        modelMap.addAttribute("count", count);
    }

    /**
     * @titile:toData
     * */
    String[] toData(Map<String, Long> map, String type) {

        // 需要保证按一定顺序排列
        Map<String, String> rmap = new HashMap<String, String>();
        for (String key : map.keySet()) {
            String value = MdrcCardStatus.fromValue(Integer.valueOf(key)).getMessage();
            rmap.put(key, value + "," + map.get(key).toString());
        }
        if ("pie".equals(type)) {
            return pieToData(rmap);
        } else {
            return barToDate(rmap);
        }

    }

    /**
     * @titile:barToDate
     * */
    String[] barToDate(Map<String, String> rmap) {

        String[] rdata = new String[2];
        StringBuffer nameData = new StringBuffer();
        StringBuffer valueData = new StringBuffer();
        nameData.append("[");
        valueData.append("[");
        for (String key : rmap.keySet()) {
            String[] data = rmap.get(key).replaceAll("_", "").split(",");
            if (data[0].length() > 6) {
                nameData.append("'" + data[0].substring(data[0].length() - 2, data[0].length()) + "',");
            } else {
                nameData.append("'" + data[0] + "',");
            }

            valueData.append(rmap.get(key).split(",")[1] + ",");

        }
        rdata[0] = nameData.toString().substring(0, nameData.toString().length() - 1) + "]";
        rdata[1] = valueData.toString().substring(0, valueData.toString().length() - 1) + "]";

        return rdata;
    }

    /**
     * 转换成饼图需要的格式
     * @titile:barToDate
     * */
    String[] pieToData(Map<String, String> rmap) {
        String[] rdata = new String[2];
        StringBuffer nameData = new StringBuffer();
        StringBuffer valueData = new StringBuffer();
        nameData.append("data:[");
        valueData.append("data:[");
        for (String key : rmap.keySet()) {
            String temp = rmap.get(key);
            if (temp == null) {
                continue;
            }
            String[] data = temp.split(",");
            nameData.append("'" + data[0] + "',");
            valueData.append("{value:" + data[1] + ",name:'" + data[0] + "'},");

        }
        rdata[0] = nameData.toString().substring(0, nameData.toString().length() - 1) + "]";
        rdata[1] = valueData.toString().substring(0, valueData.toString().length() - 1) + "]";

        return rdata;
    }

    /**
     * @param map
     * @param configId
     * @param response
     * @throws IOException
     * @Title: storeAll
     * @Description: 全部入库
     * @return: void
     */
    @RequestMapping("/storeAll")
    public void storeAll(ModelMap map, String configId, HttpServletResponse response) throws IOException {
        MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.selectByPrimaryKey(NumberUtils.toLong(configId, 0));
        // 仅当卡配置状态为已下载时可进行入库操作
        if (mdrcBatchConfig != null && mdrcBatchConfig.getStatus().equals(MdrcBatchConfigStatus.DOWNLOADED.getCode())) {
            mdrcCardInfoService.batchUpdateStatus(NumberUtils.toLong(configId), MdrcCardStatus.NEW,
                    MdrcCardStatus.STORED);
            mdrcBatchConfig.setStatus(MdrcBatchConfigStatus.STORED.getCode());//设置状态为已入库
            if (mdrcBatchConfigService.update(mdrcBatchConfig)) {//更新规则状态
                response.getWriter().write("true");
            } else {
                response.getWriter().write("false");
            }

        } else {
            //如果当前状态已经是已入库了，不做任何操作直接返回正确
            if (mdrcBatchConfig != null && mdrcBatchConfig.getStatus().equals(MdrcBatchConfigStatus.STORED.getCode())) {
                response.getWriter().write("true");
            } else {
                response.getWriter().write("false");
            }
        }
    }

    /**
     * 进入激活页面
     * 1.获取第一个序列号
     * 2.获取该批次卡总量
     * 3.获取剩余可激活数量
     * 4.获取企业列表
     */
    @RequestMapping("/preActiveRange")
    public String preActiveRange(ModelMap map, String configId, HttpServletResponse response) throws IOException {

        Administer currentUser = getCurrentUser();

        MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.select(NumberUtils.toLong(configId, 0));

        if (currentUser != null && mdrcBatchConfig != null) {

            //该批次卡卡数量
            Long totalCount = mdrcBatchConfig.getAmount();

            //还未激活卡列表
            List<MdrcCardInfo> mdrcCardInfoList = mdrcCardInfoService.getByMdrcBatchConfigAndStatus(mdrcBatchConfig,
                    MdrcCardStatus.STORED.getCode());

            //获取企业列表（获取平台所有正常状态下企业的列表）
            List<Enterprise> enterprises = enterprisesService.getNormalEnterpriseList();

            if (enterprises != null && enterprises.size() > 0) {
                map.addAttribute("enterprises", enterprises);
            }

            String year = mdrcBatchConfig.getThisYear().substring(2, 4);
            Integer currentCardStatus = mdrcBatchConfig.getStatus();

            map.addAttribute("year", year);
            map.addAttribute("currentCardStatus", currentCardStatus);

            map.addAttribute("mdrcBatchConfig", mdrcBatchConfig);

            map.addAttribute("configId", configId);

            map.addAttribute("totalCount", totalCount);
            map.addAttribute("notActiveCount", mdrcCardInfoList == null ? 0 : mdrcCardInfoList.size());
            map.addAttribute("startSerial",
                    (mdrcCardInfoList != null && mdrcCardInfoList.size() > 0) ? mdrcCardInfoList.get(0).getCardNumber()
                            : "");
        }
        return "mdrcCardInfo/active_range.ftl";
    }

    /**
     * 区间激活
     */
    @RequestMapping("/activeRange")
    public void activeRange(ModelMap map, HttpServletResponse response, HttpServletRequest request) throws IOException {
        String configId = request.getParameter("configId");
        String serialNumber = request.getParameter("serialNumber");
        String startSerial = request.getParameter("startSerial");
        String endSerial = request.getParameter("endSerial");
        String enterpriseId = request.getParameter("enterpriseId");
        String productId = request.getParameter("product");

        JSONObject json = new JSONObject();

        if (StringUtils.isBlank(configId) || StringUtils.isBlank(serialNumber) || StringUtils.isBlank(startSerial)
                || StringUtils.isBlank(endSerial) || StringUtils.isBlank(enterpriseId)
                || StringUtils.isBlank(productId)) {
            json.put("success", false);
        } else {
            if (mdrcCardInfoService.checkCardNum(NumberUtils.toLong(configId), startSerial, endSerial)) {
                //校验是否存在已入库但未激活的卡数据
                //更新相应卡状态
                if (mdrcCardInfoService.activeRange(NumberUtils.toLong(configId), startSerial, endSerial,
                        NumberUtils.toLong(enterpriseId), NumberUtils.toLong(productId))) {
                    json.put("success", true);
                } else {
                    json.put("success", false);
                }
            } else {
                json.put("success", false);
            }
        }
        response.getWriter().write(json.toString());
    }

    /**
     * @param map
     * @param configId
     * @param response
     * @throws IOException
     * @Title: activateAllCheck
     * @Description: 激活前的校验
     * @return: void
     */
    @RequestMapping("/activateCheck")
    public void activateAllCheck(ModelMap map, String configId, HttpServletResponse response) throws IOException {

        MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.selectByPrimaryKey(NumberUtils.toLong(configId));
        // 校验营销卡配置是否存在，校验操作者身份
        if (mdrcBatchConfig == null || !getCurrentUser().getId().equals(mdrcBatchConfig.getCreatorId())) {
            response.getWriter().write("false");
            return;
        }
        //校验是否存在已入库但未激活的卡数据
        Long count = mdrcCardInfoService.countByConfigIdAndStatus(NumberUtils.toLong(configId), MdrcCardStatus.STORED);
        if (count != null && count > 0) {
            response.getWriter().write("true");
        } else {
            response.getWriter().write("false");
        }
    }

    /**
     * @param map
     * @param configId
     * @return
     * @Title: internalActivationPage
     * @Description: 进入激活页面
     * @return: String
     */
    @RequestMapping("/internalActivationPage")
    public String internalActivationPage(ModelMap map, long configId) {

        // 校验营销卡配置是否存在，校验操作者身份
        MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.selectByPrimaryKey(configId);
        if (mdrcBatchConfig == null || !getCurrentUser().getId().equals(mdrcBatchConfig.getCreatorId())) {
            map.addAttribute("errorMessage", "没有找到符合条件的配置记录");
        } else {
            //企业列表
            List<Enterprise> enterpriseList = enterprisesService.getNormalEnterpriseListByAdminId(getCurrentUser());
            map.addAttribute("enterpriseList", enterpriseList);

            //第一个企业关联的产品列表
            if (enterpriseList != null && enterpriseList.size() > 0) {

                //此处根据模板获取产品
                MdrcTemplate mdrcTemplate = mdrcTemplateService.selectByPrimaryKey(mdrcBatchConfig.getTemplateId());
                map.addAttribute("mdrcTemplate", mdrcTemplate);

                List<Product> productList = productService.getProListByProSizeEnterId(mdrcTemplate.getProductSize(),
                        enterpriseList.get(0).getId());

                map.addAttribute("productList", productList);
            }

            map.addAttribute("config", mdrcBatchConfig);

            //可激活的卡数量
            map.addAttribute("activableCardNums",
                    mdrcCardInfoService.countByConfigIdAndStatus(configId, MdrcCardStatus.STORED));//可激活的数量
        }
        return "mdrcCardInfo/internalActivationPage.ftl";
    }

    /**
     * 获取审核通过的激活申请列表
     * @param modelMap
     * @author qinqinyan
     * */
    @RequestMapping("activeIndex")
    public String activeIndex(ModelMap modelMap) {
        if (getCurrentUser() != null && getCurrentUserManager() != null) {
            return "mdrcActiveApproval/activeIndex.ftl";
        }
        return getLoginAddress();
    }

    /**
     * 获取审核通过的激活申请列表
     * @param queryObject
     * @param res
     * @author qinqinyan
     * */
    @RequestMapping("activeIndexSearch")
    public void activeIndexSearch(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        if (!StringUtils.isEmpty(getRequest().getParameter("startTime"))) {
            queryObject.getQueryCriterias().put("startTime", getRequest().getParameter("startTime"));
            queryObject.getQueryCriterias().put("endTime", getRequest().getParameter("endTime") + " 23:59:59");
        }

        ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                .selectByType(ApprovalType.MDRC_Active_Approval.getCode());

        List<ApprovalRequest> totalApprovalRequests = null;
        Long totalCount = 0L;

        Administer administer = getCurrentUser();
        if (administer != null && administer.getId() != null) {
            if (approvalProcessDefinition != null && approvalProcessDefinition.getId() != null) {
                queryObject.getQueryCriterias().put("processId", approvalProcessDefinition.getId());
                queryObject.getQueryCriterias().put("result", ApprovalRequestStatus.APPROVED.getCode().toString());

                totalApprovalRequests = approvalRequestService.getApprovalRequestForMdrcActive(queryObject);
                totalCount = approvalRequestService.countApprovalRequestForMdrcActive(queryObject);
            }
        }
        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", totalApprovalRequests);
        json.put("total", totalCount);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进入激活页面
     * @param requestId
     * @param modelMap
     * @return
     * @author qinqinyan
     * */
    @RequestMapping("preActive")
    public String preActive(ModelMap modelMap, Long requestId) {
        if (requestId != null) {
            MdrcActiveDetail mdrcActiveDetail = mdrcActiveDetailService.selectByRequestId(requestId);

            if (mdrcActiveDetail.getActiveStatus().toString().equals(Constants.MDRC_ACTIVE_STATUS.ACTIVE.getResult())) {
                modelMap.addAttribute("errorMsg", "该条记录已经被处理!");
                return "error.ftl";
            }
            //查询可以使用的批次
            Map map = new HashMap();
            map.put("templateId", mdrcActiveDetail.getTemplateId());
            map.put("status", MdrcBatchConfigStatus.STORED.getCode());
            List<MdrcBatchConfig> mdrcBatchConfigs = mdrcBatchConfigService.selectByMap(map);

            modelMap.addAttribute("mdrcActiveDetail", mdrcActiveDetail);
            modelMap.addAttribute("mdrcBatchConfigs", mdrcBatchConfigs);

            return "mdrcActiveApproval/active.ftl";
        }
        modelMap.addAttribute("errorMsg", "参数缺失，未查找到该条记录!");
        return "error.ftl";
    }

    /**
     * 校验该批次卡号是否有足够卡数量用于激活
     * @param activeCount
     * @param configId
     * @param response
     * @author qinqinyan
     * */
    @RequestMapping("verifyLeftCountAjax")
    public void verifyLeftCountAjax(HttpServletResponse response, Long configId, Long activeCount) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();// 返回给Ajax请求的值
        if (configId != null && activeCount != null) {

            MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.selectByPrimaryKey(configId);
            //还未激活卡列表
            List<MdrcCardInfo> mdrcCardInfoList = mdrcCardInfoService.getByMdrcBatchConfigAndStatus(mdrcBatchConfig,
                    MdrcCardStatus.STORED.getCode());

            if (mdrcCardInfoList != null && mdrcCardInfoList.size() >= activeCount.intValue()) {
                //计算起始序列号
                String startSerial = "";
                if (mdrcCardInfoList.size() > 0) {
                    startSerial = mdrcCardInfoList.get(0).getCardNumber();
                }
                Integer count = mdrcCardInfoList.size() - activeCount.intValue();
                map.put("message", "该批次卡剩余" + mdrcCardInfoList.size() + "张,此次激活后还剩余" + count.toString() + "张!");
                map.put("leftCount", mdrcCardInfoList.size());
                map.put("startSerial", startSerial);
                map.put("result", "success");
                response.getWriter().write(JSON.toJSONString(map));
                return;
            }
            if (mdrcCardInfoList != null) {
                map.put("message", "该批次卡仅剩余" + mdrcCardInfoList.size() + "张,请选择其他批次!");
            } else {
                map.put("message", "该批次卡异常,请选择其他批次!");
            }
            map.put("result", "fail");
            response.getWriter().write(JSON.toJSONString(map));
            return;

        }
        map.put("message", "未查询到该批次卡信息,请选择其他批次!");
        map.put("result", "fail");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 激活操作
     * @param response
     * @param request
     * @author qinqinyan
     */
    @RequestMapping("/activeAjax")
    public void activeAjax(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String configId = request.getParameter("configId");
        String startSerial = request.getParameter("startSerial");
        String endSerial = request.getParameter("endSerial");
        String requestId = request.getParameter("requestId");

        Map<String, Object> map = new HashMap<String, Object>();// 返回给Ajax请求的值

        if (StringUtils.isNotBlank(configId) && StringUtils.isNotBlank(startSerial)
                && StringUtils.isNotBlank(requestId) && StringUtils.isNotBlank(endSerial)) {
            if (mdrcCardInfoService.checkCardNum(NumberUtils.toLong(configId), startSerial, endSerial)) {
                //校验是否存在已入库但未激活的卡数据
                //更新相应卡状态
                MdrcActiveRequestConfig mdrcActiveRequestConfig = createMdrcActiveRequestConfig(
                        NumberUtils.toLong(requestId), NumberUtils.toLong(configId), startSerial, endSerial);

                MdrcActiveDetail mdrcActiveDetail = mdrcActiveDetailService.selectByRequestId(mdrcActiveRequestConfig
                        .getRequestId());
                if (mdrcActiveDetail.getActiveStatus().toString()
                        .equals(Constants.MDRC_ACTIVE_STATUS.ACTIVE.getResult())) {
                    //该条记录已经激活
                    logger.info("该条记录已经被处理，激活失败!ID=" + mdrcActiveRequestConfig.getRequestId());
                    map.put("result", "fail");
                    map.put("code", "2");
                    map.put("msg", "该条记录已经被处理，激活失败!");
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }
                try {
                    if (mdrcActiveRequestConfigService.active(mdrcActiveRequestConfig)) {
                        logger.info("激活成功!ID=" + mdrcActiveRequestConfig.getRequestId());
                        map.put("result", "success");
                        response.getWriter().write(JSON.toJSONString(map));
                        return;
                    } else {
                        logger.info("激活失败");
                        map.put("result", "fail");
                        map.put("code", "1");
                        map.put("msg", "激活失败!");
                        response.getWriter().write(JSON.toJSONString(map));
                        return;
                    }
                } catch (RuntimeException e) {
                    logger.info("激活失败,失败原因：" + e.getMessage());
                    map.put("result", "fail");
                    map.put("code", "1");
                    map.put("msg", "激活失败!");
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }
            } else {
                logger.info("该批卡不存在已入库但未激活的卡!");
                map.put("code", "2");
                map.put("msg", "该条记录已经被处理，激活失败!");
                map.put("result", "fail");
                response.getWriter().write(JSON.toJSONString(map));
                return;
            }
        }
        logger.info("参数异常,激活失败!");
        map.put("code", "-1");
        map.put("msg", "参数异常,激活失败!");
        map.put("result", "fail");
        response.getWriter().write(JSON.toJSONString(map));
        return;
    }

    /**
     * @titile:createMdrcActiveRequestConfig
     * */
    private MdrcActiveRequestConfig createMdrcActiveRequestConfig(Long requestId, Long configId, String startSerial,
            String endSerial) {
        MdrcActiveRequestConfig mdrcActiveRequestConfig = new MdrcActiveRequestConfig();
        mdrcActiveRequestConfig.setRequestId(requestId);
        mdrcActiveRequestConfig.setConfigId(configId);
        mdrcActiveRequestConfig.setStartSerial(startSerial);
        mdrcActiveRequestConfig.setEndSerial(endSerial);
        mdrcActiveRequestConfig.setCreateTime(new Date());
        mdrcActiveRequestConfig.setDeleteFlag(0);
        return mdrcActiveRequestConfig;
    }

    /**
     * 激活详情
     * @param requestId
     * @param map
     * @author qinqinyan
     */
    @RequestMapping("activeDetail")
    public String activeDetail(ModelMap map, Long requestId) {
        if (requestId != null) {
            MdrcActiveDetail mdrcActiveDetail = mdrcActiveDetailService.selectByRequestId(requestId);
            if (mdrcActiveDetail.getTemplateId() != null && mdrcActiveDetail.getProductId() != null
                    && mdrcActiveDetail.getEntId() != null) {
                MdrcTemplate mdrcTemplate = mdrcTemplateService.selectByPrimaryKey(mdrcActiveDetail.getTemplateId());
                Product product = productService.selectProductById(mdrcActiveDetail.getProductId());
                Enterprise enterprise = enterprisesService.selectById(mdrcActiveDetail.getEntId());

                List<MdrcActiveRequestConfig> mdrcActiveRequestConfigs = mdrcActiveRequestConfigService
                        .selectByRequestId(requestId);

                MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.selectByPrimaryKey(mdrcActiveRequestConfigs
                        .get(0).getConfigId());

                // 审批记录列表
                List<ApprovalRecord> approvalRecords = approvalRecordService.selectByRequestId(requestId);
                map.addAttribute("opinions", approvalRecords);

                String provinceFlag = getProvinceFlag();
                map.addAttribute("provinceFlag", provinceFlag);

                //是否需要审核
                ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                        .selectByType(ApprovalType.MDRC_MakeCard_Approval.getCode());
                Boolean wheatherNeedApproval = true;
                if (approvalProcessDefinition.getStage().toString().equals("0")) {
                    wheatherNeedApproval = false;
                }
                map.addAttribute("wheatherNeedApproval", wheatherNeedApproval.toString());

                map.put("mdrcActiveDetail", mdrcActiveDetail);
                map.put("mdrcTemplate", mdrcTemplate);
                map.put("product", product);
                map.put("enterprise", enterprise);
                map.put("mdrcActiveRequestConfigs", mdrcActiveRequestConfigs);
                map.put("mdrcBatchConfig", mdrcBatchConfig);
                return "mdrcActiveApproval/activeDetail.ftl";
            }
        }
        map.addAttribute("errorMsg", "未查找到该激活申请!");
        return "error.ftl";
    }

    /**
     * @titile:internalActivate
     * */
    @RequestMapping("/internalActivate")
    public String internalActivate(ModelMap map, long configId, String beginCardNum, String endCardNum, long enterId,
            long proId) throws IOException {

        MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.selectByPrimaryKey(configId);
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(enterId);

        // 取得当前用户
        Administer administer = getCurrentUser();

        // 仅当卡配置状态为已入库时可进行激活操作
        if (mdrcBatchConfig == null || enterprise == null
                || !mdrcBatchConfig.getStatus().equals(MdrcBatchConfigStatus.STORED.getCode())
                || !mdrcBatchConfig.getCreatorId().equals(administer.getId())) {
            map.addAttribute("errorMsg", "参数或卡记录状态有误，请检查后重新提交");
            return internalActivationPage(map, configId);
        }

        mdrcCardInfoService.internalActivate(configId, beginCardNum, endCardNum, enterId, proId);

        return "redirect:index.html?configId=" + configId + "&status=" + MdrcCardStatus.ACTIVATED.getCode();
    }

    /**
     * 流量卡充值页面
     */
    @RequestMapping("/chargeIndex")
    public String chargeIndex(ModelMap map, String chargeMobile, String cardnumber, String errMsg)
            throws UnsupportedEncodingException {

        chargeMobile = process(chargeMobile);
        cardnumber = process(cardnumber);

        map.addAttribute("chargeMobile", chargeMobile);
        map.addAttribute("cardnumber", cardnumber);
        map.addAttribute("errMsg", errMsg);

        String province = getProvinceFlag();
        if (StringUtils.isNotBlank(province) && "gd_mdrc".equals(province)) {
            return "mdrcCardInfo/mdrcCardInfo-gd.ftl";
        } else {
            return "mdrcCardInfo/mdrcCardInfo.ftl";
        }
    }

    @RequestMapping("qustions")
    public String qustions(ModelMap modelMap) {
        return "mdrcCardInfo/qustions.ftl";
    }

    /**
     * @Title: submit
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public String submit(ModelMap modelMap, String chargeMobile, String cardnumber, String cardpsw,
            HttpServletRequest request) throws UnsupportedEncodingException {
        //预处理
        chargeMobile = process(chargeMobile);
        cardnumber = process(cardnumber);
        cardpsw = process(cardpsw);
        String ip = IpUtils.getRemoteAddr(request);

        //校验
        String checkResult = null;
        String errMsg = null;
        //错误类型很多，前端只显示三类错误信息：企业状态异常，请联系企业；该卡已使用；卡号或密码错误；
        if (StringUtils.isBlank(chargeMobile) || !com.cmcc.vrp.util.StringUtils.isValidMobile(chargeMobile)) {//手机号码校验
            errMsg = "请输入手机号码";
        } else if (StringUtils.isNotBlank((errMsg = phoneRegionService.checkPhoneRegionService(chargeMobile)))) {
            modelMap.addAttribute("errMsg", errMsg);
            modelMap.addAttribute("chargeMobile", chargeMobile);
            modelMap.addAttribute("cardnumber", cardnumber);
            return "redirect:chargeIndex.html";
        } else if (StringUtils.isBlank(checkResult = mdrcCardInfoService.validate(cardnumber, cardpsw))) {//卡号、密码校验通过
            //是否限制每个用户充值成功次数
            String maxTimesPerMonthStr = globalConfigService.get(GlobalConfigKeyEnum.MAX_SIZE_CHARGE_SUCCESS_PER_MONTH
                    .getKey());
            String chargedSuccessTimesStr = null;
            String key = chargeTimeLimitPerMonthPrefix + chargeMobile;
            if (StringUtils.isNotBlank(maxTimesPerMonthStr)
                    && StringUtils.isNotBlank(chargedSuccessTimesStr = redisUtilService.get(key))) {
                long maxTimesPerMonth = Long.valueOf(maxTimesPerMonthStr); //每月充值成功的次数限制          
                long chargedSuccessTimes = Long.valueOf(chargedSuccessTimesStr);//当前月份内已经充值成功的次数
                if (maxTimesPerMonth <= chargedSuccessTimes) {
                    errMsg = "对不起，您已达到每月充值次数上限！";
                    modelMap.addAttribute("errMsg", errMsg);
                    modelMap.addAttribute("chargeMobile", chargeMobile);
                    modelMap.addAttribute("cardnumber", cardnumber);
                    return "redirect:chargeIndex.html";
                }
            }
            //控制并发
            String mdrcChargeKey = mdrcChargePrefix + cardnumber;
            if(StringUtils.isNotBlank(redisUtilService.get(mdrcChargeKey))){//该卡号已发起过充值
                errMsg = "该卡已使用！";
            }else {
                String result = redisUtilService.set(mdrcChargeKey, cardnumber, 5);//默认失效时间5分钟
                logger.info("MDRC充值并发控制，入reids, key = {}, result = {}", mdrcChargeKey, result);
                if(StringUtils.isNotBlank(result) && "success".equalsIgnoreCase(result)){                   
                    //平台流水号systemNum对应MDRC充值请求流水号
                    String systemNum = SerialNumGenerator.buildSerialNum();
                    if (chargeRecordService.create(bulidChargeRecord(chargeMobile, cardnumber, systemNum))
                            && taskProducer.produceMdrcChargeMsg(build(chargeMobile, cardnumber, cardpsw, ip, systemNum))) {
                        return "redirect:success.html";
                    }
                }else {
                    errMsg = "系统繁忙，请稍后再试！";
                }
            }
        } else {
            errMsg = checkResult;
        }
        modelMap.addAttribute("errMsg", errMsg);
        modelMap.addAttribute("chargeMobile", chargeMobile);
        modelMap.addAttribute("cardnumber", cardnumber);
        return "redirect:chargeIndex.html";
    }

    /**
     * @Title: success
     */
    @RequestMapping("/success")
    public String success(ModelMap modelMap, String mobile) {
        modelMap.addAttribute("chargeMobile", mobile);
        String province = getProvinceFlag();
        if (StringUtils.isNotBlank(province) && "gd_mdrc".equals(province)) {//广东单独页面
            return "mdrcCardInfo/success-gd.ftl";
        }else {
            return "mdrcCardInfo/success.ftl";
        }  
        
    }

    /**
     * 充值失败时需要重新充值
     *
     * @param id
     * @param year
     * @return
     */
    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    public void rechargeAjax(HttpServletResponse response, Long id, int year) throws IOException {
        Boolean rechargeResult = true;
        if (id == null) {
            logger.error("重新充值失败, 参数缺失");
            rechargeResult = false;
        } else {
            MdrcCardInfo mdrcCardInfo = mdrcCardInfoService.selectByPrimaryKey(year, id);
            if (mdrcCardInfo == null || StringUtils.isBlank(mdrcCardInfo.getCardNumber())) {
                logger.error("重新充值失败, id = {}.", id);
                rechargeResult = false;
            } else {
                if (!mdrcCardInfoService.recharge(mdrcCardInfo.getCardNumber())) {
                    logger.error("重新充值失败, MdrcCardNum = {}.", mdrcCardInfo.getCardNumber());
                    rechargeResult = false;
                }
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("result", rechargeResult.toString());
        response.getWriter().write(JSON.toJSONString(map));
        //Map<String, String> result = new LinkedHashMap<String, String>();
        //result.put("result", String.valueOf(rechargeResult));
        /*try {
            StreamUtils.copy(new Gson().toJson(result), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            logger.error("输出响应时抛出异常，异常信息为{}, 异常堆栈为{}.", e.getMessage(), e.getStackTrace());
        }*/
    }

    private List<Long> getIdList(String ids) {
        List<Long> idList = new ArrayList<Long>();
        if (StringUtils.isNotBlank(ids)) {
            String[] ary = ids.split("-");
            for (String str : ary) {
                long id = NumberUtils.toLong(str, 0);
                if (id > 0) {
                    idList.add(new Long(id));
                }
            }
        }
        return idList;
    }

    /**
     * @titile:build
     * */
    private MdrcChargePojo build(String mobile, String cardNum, String cardPwd, String ip, String serialNum) {
        MdrcChargePojo chargePojo = new MdrcChargePojo();

        chargePojo.setMobile(mobile);
        chargePojo.setCardNum(cardNum);
        chargePojo.setPassword(cardPwd);
        chargePojo.setIp(ip);
        chargePojo.setSerialNum(serialNum);

        return chargePojo;
    }

    /**
     * @titile:process
     * */
    private String process(String ori) throws UnsupportedEncodingException {
        ori = StringEscapeUtils.escapeHtml(ori);

        if (!StringUtils.isEmpty(ori)) {
            ori = URLEncoder.encode(ori, "UTF-8");
        }

        return ori;
    }

    private boolean needRecharge() {
        String value = globalConfigService.get(GlobalConfigKeyEnum.MDRC_RECHARGE_WHEN_FAILED.getKey());
        return StringUtils.isNotBlank(value) && "ON".equalsIgnoreCase(value);
    }

    /**
     * 
     * @Title: checkMobile 
     * @Description: 号码归属地校验
     * @param modelMap
     * @param mobile
     * @return
     * @return: String
     * @throws IOException 
     */
    @RequestMapping("/checkMobile")
    public void checkMobile(HttpServletResponse response, ModelMap modelMap, String mobile) throws IOException {
        
        response.setContentType("text/xml;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = response.getWriter();
        //号码归属地校验
        if (StringUtils.isNotBlank(phoneRegionService.checkPhoneRegionService(mobile))) {
            pw.print("false");
        } else {
            pw.print("true");
        }
    }

    /**
     * 
     * @Title: bulidChargeRecord 
     * @Description: 构建充值记录
     * @param mobile
     * @param cardNumber
     * @param bossReqNum
     * @return
     * @return: ChargeRecord
     */
    private ChargeRecord bulidChargeRecord(String mobile, String cardNumber, String systemNum) {
        MdrcCardInfo mdrcCardInfo = mdrcCardInfoService.get(cardNumber);
        ChargeRecord cr = new ChargeRecord();
        cr.setEnterId(mdrcCardInfo.getEnterId());
        cr.setPrdId(mdrcCardInfo.getProductId());
        cr.setStatus(ChargeRecordStatus.WAIT.getCode());
        cr.setType(ActivityType.MDRC_CHARGE.getname());
        cr.setaName(ActivityType.MDRC_CHARGE.getname());
        cr.setTypeCode(ActivityType.MDRC_CHARGE.getCode());
        cr.setPhone(mobile);
        cr.setRecordId(mdrcCardInfo.getId());
        cr.setSystemNum(systemNum);
        cr.setChargeTime(new Date());
        cr.setEffectType(0);
        cr.setCount(1);
        return cr;
    }
}
