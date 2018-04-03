/**
 * 
 */
package com.cmcc.vrp.province.webin.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.MdrcBatchConfigStatus;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigService;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.MdrcBatchConfig;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;

/**
 * <p>Title:MdrcPurchaseDownloadController </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年8月19日
 */
@Controller
@RequestMapping("/manage/mdrc/purchaseDownload")
public class MdrcPurchaseDownloadController extends BaseController {	
    private static Logger logger = Logger.getLogger(MdrcPurchaseDownloadController.class);
    @Autowired
    MdrcBatchConfigService mdrcBatchConfigService;
    @Autowired
    private GlobalConfigService globalConfigService;
    
    /**
    * @Title: useStaticPackage
    * @Description: 
    */ 
    public static TemplateHashModel useStaticPackage(String packageName) {
        try {
            @SuppressWarnings("deprecation")
            BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
            TemplateHashModel staticModels = wrapper.getStaticModels();
            TemplateHashModel fileStatics = (TemplateHashModel) staticModels
                    .get(packageName);
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

        //规则名称
        setQueryParameter("configName", queryObject);
        //规则状态
        setQueryParameter("status", queryObject);
        modelMap.addAttribute("configName", queryObject.getQueryCriterias().get("configName"));
//        modelMap.addAttribute("status", queryObject.getQueryCriterias().get("status"));
        modelMap.addAttribute("mdrcBatchConfigStatus", MdrcBatchConfigStatus.toMap());//规则状态
        modelMap.put("currentUserID", getCurrentUser().getId());//当前用户ID
        return "mdrcPurchaseDownload/index.ftl";
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
        //规则名称
        setQueryParameter("configName", queryObject);
        //规则状态
//        setQueryParameter("status", queryObject);

        int count = mdrcBatchConfigService.countAllConfigByPagination(queryObject);
        List<MdrcBatchConfig> list = mdrcBatchConfigService.selectAllConfigByPagination(queryObject);

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);

        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @param configId:规则ID
     * @param modelMap
     * @return
     * @Title: listFile
     * @Description: 获取文件列表，若状态为未生成，则生成文件
     * @return: String
     */
    @RequestMapping("listFile")
    public String listFile(long configId,long templateId, ModelMap modelMap) {
        // 取得当前登录用户
        Administer administer = getCurrentUser();
        if (administer == null) {
            return getLoginAddress();
        }

        List<File> files = mdrcBatchConfigService.listPurchaseFile(configId);
        logger.info("Files has been retrieved...");

        modelMap.addAttribute("configId", configId);
        modelMap.addAttribute("templateId", templateId);
        modelMap.addAttribute("files", files);
        modelMap.addAttribute("encoder",
                useStaticPackage("java.net.URLEncoder"));

        return "mdrcPurchaseDownload/listPurchaseFile.ftl";
    }
    
    /**
     * 下载指定文件
     * <p>
     *
     * @param request
     * @param response
     * @param configId
     * @throws IOException
     */
    @RequestMapping("file")
    public void getFile(HttpServletRequest request,
                        HttpServletResponse response, long configId, String fileName,long templateId)
            throws IOException {
        logger.info("fileName:" + fileName);
        Administer administer = getCurrentUser();
        URLDecoder.decode(fileName,"UTF-8");
//        fileName   = new String( fileName.getBytes("ISO-8859-1") , "utf-8");
        logger.info("decode,fileName:" + fileName);
        if (administer != null
                && mdrcBatchConfigService.downloadPurchaseFile(request, response,
                configId, fileName, templateId)) {

            //下载成功后，删除源文件
            String path = getPurchaseDataFilePath() + File.separator + configId + File.separator + fileName;
            FileUtils.deleteQuietly(new File(path));
        } else {
            response.sendError(403);
        }
    }

    public String getPurchaseDataFilePath() {
        return globalConfigService.get(GlobalConfigKeyEnum.MDRC_PURCHASE_DATA_FILE_PATH.getKey());
    }
}
