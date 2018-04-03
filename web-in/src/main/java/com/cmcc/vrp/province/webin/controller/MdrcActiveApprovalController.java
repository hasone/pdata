package com.cmcc.vrp.province.webin.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.ApprovalRequestStatus;
import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.enums.MdrcBatchConfigStatus;
import com.cmcc.vrp.province.mdrc.enums.MdrcCardStatus;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigStatusRecordService;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.model.ApprovalRecord;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.MdrcActiveDetail;
import com.cmcc.vrp.province.model.MdrcActiveRequestConfig;
import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.model.MdrcBatchConfigStatusRecord;
import com.cmcc.vrp.province.model.MdrcCardInfo;
import com.cmcc.vrp.province.model.MdrcTemplate;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRecordService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.ApprovalRequestSmsService;
import com.cmcc.vrp.province.service.CardNumAndPwdService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.FileStoreService;
import com.cmcc.vrp.province.service.MdrcActiveDetailService;
import com.cmcc.vrp.province.service.MdrcActiveRequestConfigService;
import com.cmcc.vrp.province.service.MdrcTemplateService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.S3Service;
import com.cmcc.vrp.util.MD5;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;
import com.google.gson.Gson;

/**
 * Created by qinqinyan on 2016/11/17. 营销卡激活审核控制器
 */
@Controller
@RequestMapping("manage/mdrc/active")
public class MdrcActiveApprovalController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MdrcActiveApprovalController.class);

    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;
    @Autowired
    ApprovalRequestService approvalRequestService;
    @Autowired
    MdrcBatchConfigService mdrcBatchConfigService;
    @Autowired
    MdrcCardInfoService mdrcCardInfoService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    S3Service s3Service;
    @Autowired
    FileStoreService fileStoreService;
    @Autowired
    MdrcActiveDetailService mdrcActiveDetailService;
    @Autowired
    MdrcTemplateService mdrcTemplateService;
    @Autowired
    ProductService productService;
    @Autowired
    ApprovalRecordService approvalRecordService;
    @Autowired
    MdrcActiveRequestConfigService mdrcActiveRequestConfigService;
    @Autowired
    CardNumAndPwdService mdrcCardNumAndPwdService;
    @Autowired
    MdrcBatchConfigStatusRecordService mdrcBatchConfigStatusRecordService;
    @Autowired
    private ApprovalRequestSmsService approvalRequestSmsService;

    /**
     * 激活审批请求列表页
     *
     * @author qinqinyan
     */
    @RequestMapping("index")
    public String index(ModelMap map, QueryObject queryObject) {
        if(queryObject != null){
            map.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        
        if (getCurrentUser() != null && getCurrentUserManager() != null) {
            return "mdrcActiveApproval/approvalRequestIndex.ftl";
        }
        return getLoginAddress();
    }

    /**
     * @author qinqinyan
     */
    @RequestMapping("search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        /**
         * 查询参数
         * */
        setQueryParameter("name", queryObject);//企业名称
        setQueryParameter("serialNumber", queryObject);//批次号
        setQueryParameter("result", queryObject);//状态
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
                queryObject.getQueryCriterias().put("creatorId", administer.getId());

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
     * 创建营销卡激活申请
     *
     * @author qinqinyan
     */
    @RequestMapping("create")
    public String createActiveRequest(ModelMap modelMap,Long configId) {
        Manager manager = getCurrentUserManager();
        if (manager != null && manager.getId() != null && configId != null) {
            //获取可激活的卡信息
            MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.getConfigDetailsByIdAndStatus(configId,
                    MdrcCardStatus.STORED.getCode());           
            modelMap.addAttribute("mdrcBatchConfig", mdrcBatchConfig);
            return "mdrcActiveApproval/activeApproval.ftl";
        }
        modelMap.addAttribute("errorMsg", "参数缺失，无法激活该批次卡!");
        return "error.ftl";
    }

    /**
     * 根据卡面主题获取卡模板
     *
     * @author qinqinyan
     */
    @RequestMapping("getProductsAjax")
    public void getProductsAjax(HttpServletResponse response, Long templateId, Long entId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();// 返回给Ajax请求的值
        if (templateId != null && entId != null) {
            MdrcTemplate mdrcTemplate = mdrcTemplateService.selectByPrimaryKey(templateId);
            if (mdrcTemplate != null && !StringUtils.isEmpty(mdrcTemplate.getProductSize())) {
                List<Product> products = productService
                        .getProListByProSizeEnterId(mdrcTemplate.getProductSize(), entId);
                map.put("products", products);
                map.put("result", "success");
                response.getWriter().write(JSON.toJSONString(map));
                return;
            }
        }
        map.put("result", "fail");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 保存提交信息
     *
     * @author qinqinyan
     */
    @RequestMapping("save")
    public void save(HttpServletRequest request, HttpServletResponse response, Long configId, String startCardNumber, Integer count) {
        Map<String, Object> map = new HashMap<String, Object>();// 返回给Ajax请求的值    
        try {
            Administer administer = getCurrentUser();
                       
            //参数校验
            String errorMsg = validateActiveInfo(configId, startCardNumber, count);
            if(org.apache.commons.lang.StringUtils.isNotBlank(errorMsg)){//校验不通过
                map.put("result", "fail");
                map.put("errorMsg", errorMsg);
                response.getWriter().write(JSON.toJSONString(map));
                return;
            }
            
            //获取配置信息
            MdrcBatchConfig config = mdrcBatchConfigService.select(configId);
            ApprovalRequest approvalRequest = createApprovalRequest(administer.getId());
            approvalRequest.setEntId(config.getEnterpriseId());
            
            //构建激活申请记录信息
            MdrcActiveDetail mdrcActiveDetail = new MdrcActiveDetail();
            mdrcActiveDetail.setConfigId(configId);
            mdrcActiveDetail.setStartCardNumber(startCardNumber);
            String endCardNumber = mdrcCardNumAndPwdService.cal(startCardNumber, count - 1);
            mdrcActiveDetail.setEndCardNumber(endCardNumber);//根据起始卡号和激活数量，计算结束卡号，由于每个省份生成卡的规则不一样，需要单独配置
            mdrcActiveDetail.setCount(count);
            mdrcActiveDetail.setEntId(config.getEnterpriseId());
            mdrcActiveDetail.setTemplateId(config.getTemplateId());
            mdrcActiveDetail.setProductId(config.getProductId());
            try {
                if (approvalRequestService.submitApprovalForMdrcActive(approvalRequest, mdrcActiveDetail)) {
                    map.put("result", "success");
                    approvalRequestSmsService.sendNoticeSms(approvalRequest.getId());
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }
            } catch (RuntimeException e) {
                logger.error(e.getMessage());
            }
            map.put("result", "fail");
            map.put("errorMsg", "系统繁忙，请稍后再试");
            response.getWriter().write(JSON.toJSONString(map));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 激活详情
     *
     * @author qinqinyan
     */
    @RequestMapping("detail")
    public String detail(ModelMap map, Long requestId) {
        if (requestId != null) {
            MdrcActiveDetail mdrcActiveDetail = mdrcActiveDetailService.selectByRequestId(requestId);
            if (mdrcActiveDetail.getTemplateId() != null && mdrcActiveDetail.getProductId() != null
                    && mdrcActiveDetail.getEntId() != null) {
                MdrcTemplate mdrcTemplate = mdrcTemplateService.selectByPrimaryKey(mdrcActiveDetail.getTemplateId());
                Product product = productService.selectProductById(mdrcActiveDetail.getProductId());
                Enterprise enterprise = enterprisesService.selectById(mdrcActiveDetail.getEntId());

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

                //查找激活详情
                List<MdrcActiveRequestConfig> mdrcActiveRequestConfigs = mdrcActiveRequestConfigService
                        .selectByRequestId(requestId);
                MdrcBatchConfig mdrcBatchConfig = null;
                if (mdrcActiveRequestConfigs != null && mdrcActiveRequestConfigs.size() > 0) {
                    Long configId = mdrcActiveRequestConfigs.get(0).getConfigId();
                    mdrcBatchConfig = mdrcBatchConfigService.selectByPrimaryKey(configId);
                    
                    //批次状态变更记录
                    List<Integer> statusList = new ArrayList<Integer>();
                    statusList.add(MdrcBatchConfigStatus.NOT_DOWNLOAD.getCode());
                    statusList.add(MdrcBatchConfigStatus.DOWNLOADED.getCode());
                    statusList.add(MdrcBatchConfigStatus.STORED.getCode());
                    statusList.add(MdrcBatchConfigStatus.POST.getCode());
                    statusList.add(MdrcBatchConfigStatus.USELESS.getCode());
                    //statusList.add(MdrcBatchConfigStatus.EXPIRED.getCode());                 
                    List<MdrcBatchConfigStatusRecord> records = mdrcBatchConfigStatusRecordService.selectByConfigId(configId, statusList);
                    map.addAttribute("records", records);
                }

                ApprovalRequest approvalRequest = approvalRequestService.selectByPrimaryKey(requestId);
                map.put("approvalRequest", approvalRequest);

                map.put("mdrcActiveRequestConfigs", mdrcActiveRequestConfigs);
                map.put("mdrcBatchConfig", mdrcBatchConfig);

                map.put("mdrcActiveDetail", mdrcActiveDetail);
                map.put("mdrcTemplate", mdrcTemplate);
                map.put("product", product);
                map.put("enterprise", enterprise);
                               
                return "mdrcActiveApproval/detail.ftl";
            }
        }
        map.addAttribute("errorMsg", "未查找到该激活申请!");
        return "error.ftl";
    }

    /**
     * 创建营销卡激活申请
     *
     * @author qinqinyan
     */
    @RequestMapping("preActiveRequest")
    public String preActiveRequest(ModelMap modelMap, Long configId) {
        if (configId == null) {
            modelMap.addAttribute("errorMsg", "参数缺失，无法激活该批次卡!");
            return "error.ftl";
        }
        Administer currentUser = getCurrentUser();

        MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.select(configId);

        if (currentUser != null && mdrcBatchConfig != null) {

            //该批次卡卡数量
            Long totalCount = mdrcBatchConfig.getAmount();

            //还未激活卡列表
            List<MdrcCardInfo> mdrcCardInfoList = mdrcCardInfoService.getByMdrcBatchConfigAndStatus(mdrcBatchConfig,
                    MdrcCardStatus.STORED.getCode());

            String year = mdrcBatchConfig.getThisYear().substring(2, 4);
            Integer currentCardStatus = mdrcBatchConfig.getStatus();

            modelMap.addAttribute("year", year);
            modelMap.addAttribute("currentCardStatus", currentCardStatus);

            modelMap.addAttribute("mdrcBatchConfig", mdrcBatchConfig);

            modelMap.addAttribute("configId", configId);

            modelMap.addAttribute("totalCount", totalCount);
            modelMap.addAttribute("notActiveCount", mdrcCardInfoList == null ? 0 : mdrcCardInfoList.size());
            modelMap.addAttribute("startSerial",
                    (mdrcCardInfoList != null && mdrcCardInfoList.size() > 0) ? mdrcCardInfoList.get(0).getCardNumber()
                            : "");

            return "mdrcActiveApproval/activeApproval.ftl";
        }

        modelMap.addAttribute("errorMsg", "参数缺失，无法激活该批次卡!");
        return "error.ftl";
    }

    /**
     * 编辑营销卡激活申请
     *
     * @author qinqinyan
     */
    @RequestMapping("edit")
    public String edit(ModelMap modelMap, Long requestId, Long configId) {

        if (requestId != null && configId != null) {
            MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.select(configId);
            MdrcActiveDetail mdrcActiveDetail = mdrcActiveDetailService.selectByRequestId(requestId);

            if (mdrcBatchConfig != null && mdrcActiveDetail != null) {

                //该批次卡卡数量
                Long totalCount = mdrcBatchConfig.getAmount();
                //还未激活卡列表
                List<MdrcCardInfo> mdrcCardInfoList = mdrcCardInfoService.getByMdrcBatchConfigAndStatus(
                        mdrcBatchConfig, MdrcCardStatus.STORED.getCode());

                modelMap.addAttribute("mdrcActiveDetail", mdrcActiveDetail);
                modelMap.addAttribute("configId", configId);
                modelMap.addAttribute("totalCount", totalCount);
                modelMap.addAttribute("notActiveCount", mdrcCardInfoList == null ? 0 : mdrcCardInfoList.size());

                return "mdrcActiveApproval/editActiveApproval.ftl";
            }
        }
        modelMap.addAttribute("errorMsg", "对不起,未查到该激活审批请求!");
        return "error.ftl";
    }

    /**
     * 保存提交信息
     *
     * @author qinqinyan
     */
    @RequestMapping("save_old")
    public String saveOld(ModelMap modelMap, MdrcActiveDetail mdrcActiveDetail, MultipartHttpServletRequest request) {
        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }

        //检查文件大小
        if (s3Service.checkFileSize(request)) {
            modelMap.addAttribute("errorMsg", "文件大小超过限制！");
            return preActiveRequest(modelMap, mdrcActiveDetail.getConfigId());
        }
        //检查上传的文件格式
        if (!s3Service.checkFile(request)) {
            modelMap.addAttribute("errorMsg", "图片格式不正确，只支持jpg，jpeg，png");
            return preActiveRequest(modelMap, mdrcActiveDetail.getConfigId());
        }

        ApprovalRequest approvalRequest = null;

        Iterator<String> itr = request.getFileNames();
        MultipartFile multipartFile = null;
        File file = new File("dest");

        while (itr != null && itr.hasNext()) {
            //获得文件实例
            multipartFile = request.getFile(itr.next());
            try {
                multipartFile.transferTo(file);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            approvalRequest = createApprovalRequest(administer.getId());

            String originalFilename = multipartFile.getOriginalFilename();
            String fileName = multipartFile.getName();
            if (!StringUtils.isEmpty(originalFilename) && !StringUtils.isEmpty(fileName) && "entImage".equals(fileName)) {
                mdrcActiveDetail.setImage(originalFilename);
                mdrcActiveDetail.setImageKey(MD5.md5(UUID.randomUUID().toString()));
                //上传文件
                fileStoreService.save(mdrcActiveDetail.getImageKey(), file);
            }
        }

        try {
            if (approvalRequestService.submitApprovalForMdrcActive(approvalRequest, mdrcActiveDetail)) {
                logger.info("提交成功!");
                approvalRequestSmsService.sendNoticeSms(approvalRequest.getId());
                return "redirect:index.html?configId=" + mdrcActiveDetail.getConfigId();
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        modelMap.addAttribute("errorMsg", "提交失败!");
        return preActiveRequest(modelMap, mdrcActiveDetail.getConfigId());
    }

    private ApprovalRequest createApprovalRequest(Long adminId) {
        ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                .selectByType(ApprovalType.MDRC_Active_Approval.getCode());
        ApprovalRequest approvalRequest = new ApprovalRequest();
        if (approvalProcessDefinition != null && approvalProcessDefinition.getStage() != null) {
            approvalRequest.setProcessId(approvalProcessDefinition.getId());
            approvalRequest.setEntId(adminId);
            approvalRequest.setCreatorId(adminId);
            approvalRequest.setStatus(0);
            if (approvalProcessDefinition.getStage().toString().equals("0")) {
                //不需要审核
                approvalRequest.setResult(ApprovalRequestStatus.APPROVED.getCode());
            } else {
                approvalRequest.setResult(ApprovalRequestStatus.APPROVING.getCode());
            }
            approvalRequest.setCreateTime(new Date());
            approvalRequest.setUpdateTime(new Date());
            approvalRequest.setDeleteFlag(0);
        }
        return approvalRequest;
    }

    /**
     * 保存编辑信息
     *
     * @author qinqinyan
     */
    @RequestMapping("saveEdit")
    public String saveEdit(ModelMap modelMap, MdrcActiveDetail mdrcActiveDetail, MultipartHttpServletRequest request) {

        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }

        //检查文件大小
        if (s3Service.checkFileSize(request)) {
            modelMap.addAttribute("errorMsg", "文件大小超过限制！");
            return preActiveRequest(modelMap, mdrcActiveDetail.getConfigId());
        }
        //检查上传的文件格式
        if (!s3Service.checkFile(request)) {
            modelMap.addAttribute("errorMsg", "图片格式不正确，只支持jpg，jpeg，png");
            return preActiveRequest(modelMap, mdrcActiveDetail.getConfigId());
        }

        Iterator<String> itr = request.getFileNames();
        MultipartFile multipartFile = null;
        File file = new File("dest");

        while (itr != null && itr.hasNext()) {
            //获得文件实例
            multipartFile = request.getFile(itr.next());
            try {
                multipartFile.transferTo(file);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //替换文件
            String originalFilename = multipartFile.getOriginalFilename();
            String fileName = multipartFile.getName();
            if (!StringUtils.isEmpty(originalFilename) && !StringUtils.isEmpty(fileName) && "entImage".equals(fileName)) {
                mdrcActiveDetail.setImage(originalFilename);
                mdrcActiveDetail.setImageKey(MD5.md5(UUID.randomUUID().toString()));
                //上传文件
                fileStoreService.save(mdrcActiveDetail.getImageKey(), file);
            }
        }

        ApprovalRequest approvalRequest = editApprovalRequest(mdrcActiveDetail.getRequestId());

        try {
            if (approvalRequestService.editApprovalForMdrcActive(approvalRequest, mdrcActiveDetail)) {
                approvalRequestSmsService.sendNoticeSms(approvalRequest.getId());
                logger.info("提交成功!");
                return "redirect:index.html?configId=" + mdrcActiveDetail.getConfigId();
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        modelMap.addAttribute("errorMsg", "提交失败!");
        return preActiveRequest(modelMap, mdrcActiveDetail.getConfigId());
    }

    private ApprovalRequest editApprovalRequest(Long requestId) {
        ApprovalRequest oldApprovalRequest = approvalRequestService.selectByPrimaryKey(requestId);
        oldApprovalRequest.setId(requestId);
        oldApprovalRequest.setResult(ApprovalRequestStatus.APPROVING.getCode());
        oldApprovalRequest.setStatus(0);
        oldApprovalRequest.setUpdateTime(new Date());
        return oldApprovalRequest;
    }

    /**
     * 下载文件
     *
     * @author qinqinyan
     */
    @RequestMapping(value = "/downloadFile")
    public void downloadFile(String type, Long requestId, HttpServletResponse response, HttpServletRequest request) {
        MdrcActiveDetail mdrcActiveDetail = mdrcActiveDetailService.selectByRequestId(requestId);

        if (mdrcActiveDetail == null || StringUtils.isEmpty(mdrcActiveDetail.getImage())
                || StringUtils.isEmpty(mdrcActiveDetail.getImageKey())) {
            return;
        }
        if ("entImage".equals(type)) {
            downloadFromS3(response, mdrcActiveDetail.getImageKey(), mdrcActiveDetail.getImage(), request);
        }
    }

    /**
     * 获取文件
     *
     * @author qinqinyan
     **/
    @RequestMapping(value = "/getImageFile")
    public void getImageFile(Long requestId, HttpServletResponse response, HttpServletRequest request) {
        MdrcActiveDetail mdrcActiveDetail = mdrcActiveDetailService.selectByRequestId(requestId);

        if (!StringUtils.isEmpty(mdrcActiveDetail.getImage()) && !StringUtils.isEmpty(mdrcActiveDetail.getImageKey())) {
            downloadFromS3(response, mdrcActiveDetail.getImageKey(), mdrcActiveDetail.getImage(), request);
        }
    }

    /**
     * 获取图片
     *
     * @param requestId 审核请求id
     * @author qinqinyan
     */
    @RequestMapping(value = "/getImage")
    public void getImage(String type, Long requestId, HttpServletResponse response) {

        if (requestId != null) {
            MdrcActiveDetail mdrcActiveDetail = mdrcActiveDetailService.selectByRequestId(requestId);
            if (mdrcActiveDetail != null && !StringUtils.isEmpty(mdrcActiveDetail.getImageKey())) {
                if ("entImage".equals(type)) {
                    s3Service.getImageFromS3(response, mdrcActiveDetail.getImageKey());
                }
            }
        }
    }

    /**
     * 从S3中下载
     *
     * @date 2016年5月31日
     * @author wujiamin
     */
    private void downloadFromS3(HttpServletResponse response, String key, String fileName, HttpServletRequest request) {
        // S3Until download = new S3Until();
        InputStream bis = null;
        java.io.BufferedOutputStream bos = null;
        try {
            // 客户使用保存文件的对话框：
            response.setContentType("application/x-msdownload");
            response.setCharacterEncoding("utf-8");

            String userAgent = request.getHeader("USER-AGENT").toLowerCase();
            if (userAgent != null && userAgent.indexOf("firefox") >= 0) {
                response.setHeader("Content-disposition",
                        "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
            } else {
                response.setHeader("Content-disposition",
                        "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
            }
            // 通知客户文件的MIME类型：
            bis = fileStoreService.get(key);
            bos = new java.io.BufferedOutputStream(response.getOutputStream());
            StreamUtils.copy(bis, bos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bis = null;
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bos = null;
            }
        }
    }

    /**
     * 
     * @Title: getConfigsAjax 
     * @Description: 根据企业ID获取批次信息
     * @param request
     * @param response
     * @param entId
     * @return: void
     */
    @RequestMapping("getConfigsAjax")
    @ResponseBody
    public void getConfigsAjax(HttpServletRequest request, HttpServletResponse response, Long entId) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();// 返回给Ajax请求的值
            //获取企业营销卡批次信息
            List<MdrcBatchConfig> storedMdrcBatchConfigList = mdrcBatchConfigService.selectByEntIdAndStatus(entId,
                    MdrcBatchConfigStatus.STORED);
            if (storedMdrcBatchConfigList != null) {
                map.put("configs", storedMdrcBatchConfigList);
                map.put("result", "success");
                response.getWriter().write(JSON.toJSONString(map));
            } else {
                map.put("result", "fail");
                response.getWriter().write(JSON.toJSONString(map));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 
     * @Title: getConfigDetailsAjax 
     * @Description: 根据批次信息获取卡号信息
     * @param request
     * @param response
     * @param configId
     * @return: void
     */
    @RequestMapping("getConfigDetailsAjax")
    @ResponseBody
    public void getConfigDetailsAjax(HttpServletRequest request, HttpServletResponse response, Long configId) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();// 返回给Ajax请求的值
            //是否横向越权
            if(mdrcBatchConfigService.isOverAuth(getCurrentAdminID(), configId)){
                logger.info("你没有权限访问,configId = " , configId);
                map.put("result", "fail");
                map.put("errorMsg", "你没有权限访问!");
                response.getWriter().write(JSON.toJSONString(map));
                return; 
            }
            
            
            //参数校验：是否存在未审核的激活请求
            List<MdrcActiveDetail> approvingList = mdrcActiveDetailService.selectByconfigIdAndStatus(configId, ApprovalRequestStatus.APPROVING.getCode());
            if(approvingList != null && approvingList.size() > 0){
                logger.info("激活申请信息校验失败:存在审核中的激活申请,approvingList = {}。", new Gson().toJson(approvingList));
                map.put("result", "fail");
                map.put("errorMsg", "存在审批中的数据，请先处理审批中数据!");
                response.getWriter().write(JSON.toJSONString(map));
                return;
            }
          
            //获取可激活的卡信息
            MdrcBatchConfig mdrcBatchConfig = mdrcBatchConfigService.getConfigDetailsByIdAndStatus(configId,
                    MdrcCardStatus.STORED.getCode());           
            if (mdrcBatchConfig != null) {
                map.put("result", "success");
                map.put("mdrcBatchConfig", mdrcBatchConfig);
                response.getWriter().write(JSON.toJSONString(map));
            } else {
                map.put("result", "fail");
                map.put("errorMsg", "该批次号没有可激活的卡号!");
                response.getWriter().write(JSON.toJSONString(map));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @Title: calEndCardNumberAjax 
     * @Description: 根据起始卡号和激活数量计算终止卡号
     * @param request
     * @param response
     * @param startCardNumber
     * @param count
     * @return: void
     */
    @RequestMapping("calEndCardNumberAjax")
    @ResponseBody
    public void calEndCardNumberAjax(HttpServletRequest request, HttpServletResponse response, String startCardNumber, int count) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();// 返回给Ajax请求的值
            
            //根据起始卡号计算终止卡号
            if(org.apache.commons.lang.StringUtils.isBlank(startCardNumber) || count <= 0){
                map.put("result", "fail");
                map.put("errorMsg", "该批次号没有可激活的卡号!");
                response.getWriter().write(JSON.toJSONString(map));
            }else {
                String endCardNumber = mdrcCardNumAndPwdService.cal(startCardNumber, count - 1);
                map.put("result", "success");
                map.put("endCardNumber", endCardNumber);
                response.getWriter().write(JSON.toJSONString(map));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private String validateActiveInfo(Long configId,String startCardNumber, Integer count){
        if (configId == null || count == null || count <= 0 || org.apache.commons.lang.StringUtils.isBlank(startCardNumber)) {
            logger.info("激活申请信息校验失败,configId = {}, count={}.", configId, count);
            return "参数缺失,提交失败!";
        }

        //参数校验：合法性校验
        MdrcBatchConfig config = mdrcBatchConfigService.select(configId);
        if (config == null || config.getEnterpriseId() == null || config.getProductId() == null
                || config.getTemplateId() == null) {
            logger.info("激活申请信息校验失败,config = {}, configId = {}", new Gson().toJson(config), configId);
            return "参数非法,提交失败!";
        }

        //参数校验：激活数量校验
        Long canBeActivate = mdrcCardInfoService.countByConfigIdAndStatus(configId, MdrcCardStatus.STORED);
        if (count <= 0 || count > canBeActivate) {
            logger.info("激活申请信息校验失败:激活数量异常,激活数量不能超过可激活总数,configId = {},激活数量 = {}, 可激活总数={}。", configId, count,
                    canBeActivate);
            return "激活数量不可超过可激活总数!";
        }

        //参数校验：是否存在未审核的激活请求
        List<MdrcActiveDetail> approvingList = mdrcActiveDetailService.selectByconfigIdAndStatus(configId, ApprovalRequestStatus.APPROVING.getCode());
        if(approvingList != null && approvingList.size() > 0){
            logger.info("激活申请信息校验失败:存在审核中的激活申请,approvingList = {}。", new Gson().toJson(approvingList));
            return "已存在审批中数据，请勿重复提交!";
        }
        
        //校验通过：没有错误信息，返回null
        return null;
    }
}
