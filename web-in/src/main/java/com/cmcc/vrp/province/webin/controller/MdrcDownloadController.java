package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.MdrcBatchConfigStatus;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.mdrc.service.MdrcCardmakerService;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.MdrcBatchConfigInfo;
import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.model.MdrcCardInfo;
import com.cmcc.vrp.province.model.MdrcCardmakeDetail;
import com.cmcc.vrp.province.model.MdrcCardmaker;
import com.cmcc.vrp.province.model.MdrcMakecardRequestConfig;
import com.cmcc.vrp.province.model.MdrcTemplate;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.FileStoreService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.MdrcBatchConfigInfoService;
import com.cmcc.vrp.province.service.MdrcCardmakeDetailService;
import com.cmcc.vrp.province.service.MdrcMakecardRequestConfigService;
import com.cmcc.vrp.province.service.MdrcTemplateService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.S3Service;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: MdrcDownloadController
 * @Description: 营销卡数据下载控制器
 * @author: Rowe
 * @date: 2016年5月20日 下午3:26:55
 */
@Controller
@RequestMapping("/manage/mdrc/download")
public class MdrcDownloadController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MdrcDownloadController.class);
    @Autowired
    MdrcBatchConfigService mdrcBatchConfigService;
    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    MdrcMakecardRequestConfigService mdrcMakecardRequestConfigService;
    @Autowired
    MdrcCardmakeDetailService mdrcCardmakeDetailService;
    @Autowired
    ProductService productService;
    @Autowired
    MdrcBatchConfigInfoService MdrcBatchConfigInfoService;
    @Autowired
    MdrcTemplateService mdrcTemplateService;
    @Autowired
    MdrcCardInfoService mdrcCardInfoService;
    @Autowired
    S3Service s3Service;
    @Autowired
    FileStoreService fileStoreService;
    @Autowired
    ManagerService managerService;
    @Autowired
    MdrcCardmakerService mdrcCardmakerService;

    /**
     * @Title: useStaticPackage
     */
    public static TemplateHashModel useStaticPackage(String packageName) {
        try {
            @SuppressWarnings("deprecation")
            BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
            TemplateHashModel staticModels = wrapper.getStaticModels();
            TemplateHashModel fileStatics = (TemplateHashModel) staticModels.get(packageName);
            return fileStatics;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param modelMap
     * @param queryObject
     * @return
     * @Title: listRecord
     * @Description: 下载列表
     * @return: String
     */
    @RequestMapping("index")
    public String index(ModelMap modelMap, QueryObject queryObject) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
      
        // 规则名称
        setQueryParameter("configName", queryObject);
        // 规则状态
        setQueryParameter("status", queryObject);
        modelMap.addAttribute("configName", queryObject.getQueryCriterias().get("configName"));
        modelMap.addAttribute("status", queryObject.getQueryCriterias().get("status"));
        modelMap.addAttribute("mdrcBatchConfigStatus", MdrcBatchConfigStatus.toMap());// 规则状态
        modelMap.put("currentUserID", getCurrentUser().getId());// 当前用户ID
        return "mdrcDownload/index.ftl";
    }

    /**
     * @param queryObject
     * @param res
     * @Title: search
     * @Description: 搜索
     * @return: void
     */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        // 规则名称
        setQueryParameter("serialNumber", queryObject);
        // 规则状态
        setQueryParameter("status", queryObject);

        // 取得当前制卡专员用户标识
        setQueryParameter("operatorId", queryObject, getCurrentUser().getId());
        // 根据手机号码查询对应记录
        int count = mdrcBatchConfigService.countByCardmaker(queryObject);
        List<MdrcBatchConfig> list = mdrcBatchConfigService.selectByCardmaker(queryObject);

        for (MdrcBatchConfig item : list) {
            MdrcMakecardRequestConfig requestConfigMap = mdrcMakecardRequestConfigService
                    .selectByConfigId(item.getId());
            if(requestConfigMap != null){
                item.setCardmakeStatus(requestConfigMap.getCardmakeStatus());
            }
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
    
    @RequestMapping("getConfigDetail")
    public String getConfigDetail(ModelMap modelMap, Long configId){
        Administer currentAdmin = getCurrentUser();
        LOGGER.info("configId = {}", configId);
        if(configId!=null && currentAdmin!=null){
            MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.selectByPrimaryKey(configId);
            MdrcMakecardRequestConfig requestConfigMap = mdrcMakecardRequestConfigService.selectByConfigId(configId);
            if (mdrcBatchConfig != null && requestConfigMap != null) {
                
               //判断是否有操作权限
                MdrcCardmaker mdrcCardmaker = mdrcCardmakerService.selectByPrimaryKey(mdrcBatchConfig.getCardmakerId());
                if(!mdrcCardmaker.getOperatorId().toString().equals(currentAdmin.getId().toString())){
                    LOGGER.info("制卡商 cardmakerId= {}, 制卡商的操作用户 = {}, 当前登录用户  = {}",
                            mdrcBatchConfig.getCardmakerId(), mdrcCardmaker.getOperatorId(), currentAdmin.getId());
                    modelMap.addAttribute("errorMsg", "对不起，您无权限查看该条记录!");
                    return "error.ftl";
                }
                
                Product product = productService.get(requestConfigMap.getProductId());
                MdrcBatchConfigInfo bacthConfigInfo = MdrcBatchConfigInfoService
                        .selectByPrimaryKey(requestConfigMap.getConfigInfoId());
                MdrcTemplate mdrcTemplate = mdrcTemplateService.selectByPrimaryKey(requestConfigMap.getTemplateId());
                
                MdrcCardmakeDetail mdrcCardmakeDetail = mdrcCardmakeDetailService.selectByRequestId(requestConfigMap.getRequestId());
                modelMap.addAttribute("mdrcBatchConfig", mdrcBatchConfig);
                modelMap.addAttribute("requestConfigMap", requestConfigMap);
                modelMap.addAttribute("bacthConfigInfo", bacthConfigInfo);
                modelMap.addAttribute("product", product);
                modelMap.addAttribute("mdrcTemplate", mdrcTemplate);
                modelMap.addAttribute("mdrcCardmakeDetail", mdrcCardmakeDetail);
                
                List<MdrcCardInfo> mdrcCardInfos = mdrcCardInfoService.listByConfig(mdrcBatchConfig);
                modelMap.addAttribute("startCardNum", mdrcCardInfos.get(0).getCardNumber());
                modelMap.addAttribute("endCardNum", mdrcCardInfos.get(mdrcCardInfos.size()-1).getCardNumber());
                return "mdrcDownload/configDetail.ftl";
            }
        }
        modelMap.addAttribute("errorMsg", "未查找到该条记录的详细信息!");
        return "error.ftl";
    }

    /**
     * @param configId:规则ID
     * @param modelMap
     * @return
     * @Title: listFile
     * @Description: 获取文件列表，若状态为未生成，则生成文件
     * @return: String
     */
    /*@RequestMapping("listFile")
    public String listFile(long configId, long templateId, ModelMap modelMap) {
        // 取得当前登录用户
        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }

        List<File> files = mdrcBatchConfigService.listFile(configId, administer.getId());
        LOGGER.info("Files has been retrieved...");

        modelMap.addAttribute("configId", configId);
        modelMap.addAttribute("templateId", templateId);
        modelMap.addAttribute("files", files);
        modelMap.addAttribute("encoder", useStaticPackage("java.net.URLEncoder"));

        return "mdrcDownload/listFile.ftl";
    }*/

    /**
     * 填写物流单号
     * 
     * @param configId
     *            营销卡规则id
     * @author qinqinyan
     * @date 2017/08/10
     */
    @RequestMapping("fillInTrackingNumber")
    public String fillInTrackingNumber(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response,
            Long configId) {
        Administer currentAdmin = getCurrentUser();
        LOGGER.info("configId = {}", configId);
        if (configId != null && currentAdmin!=null) {
            MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.select(configId);
            //判断这条记录是否已经被处理
            MdrcMakecardRequestConfig requestConfigMap = mdrcMakecardRequestConfigService.selectByConfigId(configId);
            if (mdrcBatchConfig != null && requestConfigMap != null) {
                
                //判断是否有操作权限
                MdrcCardmaker mdrcCardmaker = mdrcCardmakerService.selectByPrimaryKey(mdrcBatchConfig.getCardmakerId());
                if(!mdrcCardmaker.getOperatorId().toString().equals(currentAdmin.getId().toString())){
                    LOGGER.info("制卡商 cardmakerId= {}, 制卡商的操作用户 = {}, 当前登录用户  = {}",
                            mdrcBatchConfig.getCardmakerId(), mdrcCardmaker.getOperatorId(), currentAdmin.getId());
                    modelMap.addAttribute("errorMsg", "对不起，您无权限操作该条记录!");
                    return "error.ftl";
                }
                
                if(mdrcBatchConfig.getStatus().toString().equals(MdrcBatchConfigStatus.DOWNLOADED.getCode().toString())){
                    Product product = productService.get(requestConfigMap.getProductId());
                    MdrcBatchConfigInfo bacthConfigInfo = MdrcBatchConfigInfoService
                            .selectByPrimaryKey(requestConfigMap.getConfigInfoId());
                    MdrcTemplate mdrcTemplate = mdrcTemplateService.selectByPrimaryKey(requestConfigMap.getTemplateId());
                    
                    MdrcCardmakeDetail mdrcCardmakeDetail = mdrcCardmakeDetailService.selectByRequestId(requestConfigMap.getRequestId());
                    modelMap.addAttribute("mdrcBatchConfig", mdrcBatchConfig);
                    modelMap.addAttribute("requestConfigMap", requestConfigMap);
                    modelMap.addAttribute("bacthConfigInfo", bacthConfigInfo);
                    modelMap.addAttribute("product", product);
                    modelMap.addAttribute("mdrcTemplate", mdrcTemplate);
                    modelMap.addAttribute("mdrcCardmakeDetail", mdrcCardmakeDetail);
                    
                    List<MdrcCardInfo> mdrcCardInfos = mdrcCardInfoService.listByConfig(mdrcBatchConfig);
                    modelMap.addAttribute("startCardNum", mdrcCardInfos.get(0).getCardNumber());
                    modelMap.addAttribute("endCardNum", mdrcCardInfos.get(mdrcCardInfos.size()-1).getCardNumber());
                    
                    return "mdrcDownload/fillInTrackingNumber.ftl";
                }else{
                    modelMap.addAttribute("errorMsg", "该条数据状态不是制卡中，无法填写快递单号。");
                    return "error.ftl";
                }
            }
        }
        modelMap.addAttribute("errorMsg", "未查找到相关营销卡数据");
        return "error.ftl";
    }

    /**
     * 下载失败处理
     * 
     * @author qinqinyan
     * @date 2017/08/10
     * @throws IOException
     */
    @RequestMapping("downloadFailAjax")
    public void downloadFailAjax(HttpServletRequest request, HttpServletResponse response, Long configId)
            throws IOException {
        Map<String, String> returnMap = new HashMap<String, String>();
        LOGGER.info("configId = {}", configId);
        Boolean result = false;
        
        Administer currentAdmin = getCurrentUser();
        if(currentAdmin!=null){
            MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.select(configId);
            if(mdrcBatchConfig!=null){
                //获取制卡商
                MdrcCardmaker mdrcCardmaker = mdrcCardmakerService.selectByPrimaryKey(mdrcBatchConfig.getCardmakerId());
                if(mdrcCardmaker==null){
                    LOGGER.info("该批次卡  configId = {} 的制卡信息有误", configId);
                }else{
                    if(!mdrcCardmaker.getOperatorId().toString().equals(currentAdmin.getId().toString())){
                        LOGGER.info("制卡商 cardmakerId= {} 无权限设置卡 configId = {} 失效。制卡商的操作用户 = {}, 当前登录用户  = {}",
                                mdrcBatchConfig.getCardmakerId(), configId, mdrcCardmaker.getOperatorId(), currentAdmin.getId());
                    }else{
                        try {
                            if (mdrcBatchConfigService.handleDownloadFail(configId)) {
                                LOGGER.info("设置卡 configId = {} 失效成功。", configId);
                                result = true;
                            } else {
                                LOGGER.info("设置卡 configId = {} 失效失败。", configId);
                            }
                        } catch (RuntimeException e) {
                            LOGGER.info("设置卡 configId = {}失效失败，失败原因： {}", configId, e.getMessage());
                        }
                    }
                }
            }else{
                LOGGER.info("未查找到卡 configId = {} 的信息。", configId);
            }
        }else{
            LOGGER.info("未查找用户信息。configId = {}", configId);
        }
        returnMap.put("result", result.toString());
        response.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }

    /**
     * 获取物流单号
     * 
     * @param configId
     * @author qinqinyan
     * @date 2017/08/10
     */
    @RequestMapping("getTrackingNumberAjax")
    public void getTrackingNumberAjax(HttpServletRequest request, HttpServletResponse response, Long configId)
            throws IOException {
        Map<String, String> returnMap = new HashMap<String, String>();
        Administer currentAdmin = getCurrentUser();
        
        LOGGER.info("configId = {}", configId);
        Boolean result = false;
        String trackingNumber = "";
        if (configId != null && currentAdmin!=null) {
            MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.select(configId);
            if(mdrcBatchConfig!=null){
                MdrcCardmaker mdrcCardmaker = mdrcCardmakerService.selectByPrimaryKey(mdrcBatchConfig.getCardmakerId());
                if(mdrcCardmaker!=null){
                    if(mdrcCardmaker.getOperatorId().toString().equals(currentAdmin.getId().toString())){
                        MdrcMakecardRequestConfig makecardRequestConfig = mdrcMakecardRequestConfigService
                                .selectByConfigId(configId);
                        if (makecardRequestConfig != null && !StringUtils.isEmpty(makecardRequestConfig.getTrackingNumber())) {
                            result = true;
                            trackingNumber = makecardRequestConfig.getTrackingNumber();
                            LOGGER.info("获取到的物流单号： configId = {}, trackingNumber = {}", configId, trackingNumber);
                        }
                    }
                }
            }
        }
        returnMap.put("result", result.toString());
        returnMap.put("trackingNumber", trackingNumber);
        response.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }

    /**
     * 填写快递单号
     * 
     * @author qinqinyan
     * @date 2017/08/10
     */
    @RequestMapping("fillInAjax")
    public void fillInAjax(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> returnMap = new HashMap<String, String>();
        Administer currentAdmin = getCurrentUser();
        Boolean result = false;
        String mdrcCardmakeDetailStr = request.getParameter("mdrcCardmakeDetail");
        //String mdrcBatchConfigStr = request.getParameter("mdrcBatchConfig");
        LOGGER.info("获取到的参数 mdrcCardmakeDetailStr = {}", mdrcCardmakeDetailStr);
        if (!StringUtils.isEmpty(mdrcCardmakeDetailStr) && currentAdmin!=null) {
            try {
                MdrcCardmakeDetail mdrcCardmakeDetail = JSON.parseObject(mdrcCardmakeDetailStr,
                        MdrcCardmakeDetail.class);
                MdrcMakecardRequestConfig requestConfigMap = mdrcMakecardRequestConfigService.selectByRequestId(mdrcCardmakeDetail.getRequestId());
                if(requestConfigMap!=null){
                    MdrcBatchConfig orgMdrcBatchConfig = mdrcBatchConfigService.select(requestConfigMap.getConfigId());
                    if(orgMdrcBatchConfig!=null){
                        MdrcCardmaker mdrcCardmaker = mdrcCardmakerService.selectByPrimaryKey(orgMdrcBatchConfig.getCardmakerId());
                        if(mdrcCardmaker!=null 
                                && mdrcCardmaker.getOperatorId().toString().equals(currentAdmin.getId().toString())){
                            MdrcBatchConfig mdrcBatchConfig = new MdrcBatchConfig();
                            mdrcBatchConfig.setId(orgMdrcBatchConfig.getId());
                            if (mdrcCardmakeDetailService.updateTrackingNumber(mdrcCardmakeDetail, mdrcBatchConfig)) {
                                LOGGER.info("更新快递单号成功 mdrcCardmakeDetailStr = {}", mdrcCardmakeDetailStr);
                                result = true;
                            } else {
                                LOGGER.info("更新快递单号失败 mdrcCardmakeDetailStr = {}， mdrcBatchConfigStr = {}", mdrcCardmakeDetailStr);
                            }
                        }else{
                            LOGGER.info("登录用户 = {}与改批次卡的制卡商 不符合", currentAdmin.getId(), JSONObject.toJSONString(mdrcCardmaker));
                        }
                    }else{
                        LOGGER.info("根据 config = {} 未查找相应卡批次信息", requestConfigMap.getConfigId());
                    }
                }
            } catch (Exception e) {
                LOGGER.info("填写物流单号失败，失败原因：{}", e.getMessage());
            }
        }
        returnMap.put("result", result.toString());
        response.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }

    /**
     * 下载指定文件
     * <p>
     *
     * @param request
     * @param response
     * @param configId
     * @throws IOException
     * 
     *             edit by qinqinyan on 2017/08/09 , String fileName, long
     *             templateId
     */
    @RequestMapping("file")
    public void getFile(HttpServletRequest request, HttpServletResponse response, Long configId) throws IOException {
        //Map<String, String> returnMap = new HashMap<String, String>();
        //Boolean result = false;

        LOGGER.info("configId:" + configId);
        Administer administer = getCurrentUser();
        if (configId != null && administer != null) {
            MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.select(configId);
            if (mdrcBatchConfig != null 
                    && MdrcBatchConfigStatus.NOT_DOWNLOAD.getCode().toString().equals(mdrcBatchConfig.getStatus().toString())) {
                
                MdrcCardmaker mdrcCardmaker = mdrcCardmakerService.selectByPrimaryKey(mdrcBatchConfig.getCardmakerId());
                if(mdrcCardmaker==null){
                    LOGGER.info("根据 = {}未查找到该批次卡关联的制卡商信息", mdrcBatchConfig.getCardmakerId());
                    return;
                }
                if(!mdrcCardmaker.getOperatorId().toString().equals(administer.getId().toString())){
                    LOGGER.info("登录用户 = {}不具备下载文件权限。该批次制卡商 = {} 的操作用户为 = {}", 
                            administer.getId(), mdrcBatchConfig.getCardmakerId(), mdrcCardmaker.getOperatorId());
                    return;
                }
                
                MdrcMakecardRequestConfig requestConfigMap = mdrcMakecardRequestConfigService
                        .selectByConfigId(configId);
                if (requestConfigMap != null && !StringUtils.isEmpty(requestConfigMap.getFileName())) {
                    //存储S3的key值
                    String key = requestConfigMap.getFileName();
                    LOGGER.info("key = {}, templateId = {}", key, mdrcBatchConfig.getTemplateId());
                    
                    if (mdrcBatchConfigService.downloadFile(request, response, configId, administer.getId(), key,
                            mdrcBatchConfig.getTemplateId())) {
                        // 下载成功后，自动下发通知短信给客户经理
                        // v1.12.1版本 跟产品确认后，由于流程变更，卡商下载成功给客户经理下发短信功能暂时去掉 by
                        // qinqinyan on 2017/08/09
                        // mdrcBatchConfigService.notifyManager(configId);

                        //测试下载和删除
                        try{
                            fileStoreService.deleteKeysStartWith(key);
                        }catch(Exception e){
                            LOGGER.error("删除S3文件 .{}", e.getMessage());
                        }
                        return;
                    }
                } else {
                    LOGGER.info("未查到营销卡规则和制卡申请关联关系或者待下载文件名称为空 configId = {}, requestConfigMap", configId,
                            JSON.toJSONString(requestConfigMap));
                }
            } else {
                LOGGER.info("未查到营销卡规则 configId = {}", configId);
            }
        }
        return;
    }
    
    
    public String getDataFilePath() {
        return globalConfigService.get(GlobalConfigKeyEnum.MDRC_DATA_FILE_PATH.getKey());
    }

}
