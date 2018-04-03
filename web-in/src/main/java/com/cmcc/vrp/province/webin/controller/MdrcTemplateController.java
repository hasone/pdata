package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.exception.SelfCheckException;
import com.cmcc.vrp.province.mdrc.model.MdrcFileInfo;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.MdrcTemplate;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.AdminEnterService;
import com.cmcc.vrp.province.service.FileStoreService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.MdrcTemplateService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.S3Service;
import com.cmcc.vrp.province.service.XssService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.MD5;
import com.cmcc.vrp.util.PropertyValidator;
import com.cmcc.vrp.util.QueryObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * MdrcTemplateController.java
 */
@Controller
@RequestMapping("/manage/mdrc/template")
public class MdrcTemplateController extends BaseController {
    private static String[] SURPPORTED_PIC_TYPE = {".gif", ".jpg", ".jpeg", ".png", ".bmp"};// 支持的图片格式
    private static Long SUPPORTED_PIC_MAX_SIZE = 5 * 1024 * 1024L;// 支持的图片最大大小
    @Autowired
    MdrcTemplateService mdrcTemplateService;

    @Autowired
    AdminEnterService adminEnterService;

    @Autowired
    ProductService productService;
    
    @Autowired
    S3Service s3Service;
    @Autowired
    XssService xssService;

    @Autowired
    private FileStoreService fileStoreService;

    private Logger logger = Logger.getLogger(MdrcTemplateController.class);
    @Autowired
    private GlobalConfigService globalConfigService;

    @RequestMapping("detail")
    public String detail(ModelMap modelMap, Long id) {
        MdrcTemplate record = mdrcTemplateService.selectByPrimaryKey(id);

        List<MdrcFileInfo> files = new ArrayList<MdrcFileInfo>();

        if (record != null) {
            files = mdrcTemplateService.listFilesS3(id);
        }

        logger.info(files.size());
        modelMap.put("record", record);

        modelMap.put("files", files);
        modelMap.put("userId", getCurrentUser().getId());

        return "mdrcTemplate/detailS3.ftl";
    }

    /**
     * @Title: index
     */
    @RequestMapping(value = "/index")
    public String index(ModelMap modelMap, QueryObject queryObject) {
        if(queryObject == null){
            queryObject = new QueryObject();
        }
        modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        setQueryParameter("name", queryObject);
        modelMap.addAttribute("name", queryObject.getQueryCriterias().get("name"));
        return "mdrcTemplate/index.ftl";
    }

    /**
     * @Title: search
     */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse res) {

        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 设置当前用户ID
         */
        Administer administer = getCurrentUser();
        if (administer == null) {

        }
        // queryObject.getQueryCriterias().put("creatorId", administer.getId());

        setQueryParameter("name", queryObject);

        int count = mdrcTemplateService.count(queryObject);
        List<MdrcTemplate> list = mdrcTemplateService.list(queryObject);

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
     * @Title: create
     */
    @RequestMapping("create")
    public String create(ModelMap modelMap) {

        // List<Product> products = productService.selectAllProducts();
        List<Product> proSizeList = productService.selectDistinctProSize();

        List<String> themes = mdrcTemplateService.listThemes();

        modelMap.addAttribute("products", proSizeList);
        modelMap.addAttribute("themes", themes);

        return "mdrcTemplate/create.ftl";
    }
    
    /**
     * 校验营销模板参数
     * */
    private boolean verifyTemplate(MdrcTemplate mdrcTemplate){
        if(mdrcTemplate==null 
                || StringUtils.isBlank(mdrcTemplate.getTheme()) 
                || !templateNameCheck(mdrcTemplate.getName())
                || StringUtils.isBlank(mdrcTemplate.getProductSize())){
            logger.info("参数异常，校验不同过。"+JSON.toJSONString(mdrcTemplate));
            return true;
        }
        return true;
    }
    
    private boolean verifyMdrcTemplateImages(MultipartHttpServletRequest multipartRequest){
        try {
            Iterator<String> itr = multipartRequest.getFileNames();
            MultipartFile multipartFile = null;
            String mdrcCommonTemplateFront = "";
            String mdrcCommonTemplateBack = "";

            while (itr != null && itr.hasNext()) {
                multipartFile = multipartRequest.getFile(itr.next());

                String originalFilename = multipartFile.getOriginalFilename();
                if (!StringUtils.isEmpty(originalFilename)) {
                    if (multipartFile.getName().equals("mdrcCommonTemplateFront")) {
                        mdrcCommonTemplateFront = originalFilename;
                    } else if (multipartFile.getName().equals("mdrcCommonTemplateBack")) {
                        mdrcCommonTemplateBack = originalFilename;
                    } 
                }
            }
            if(StringUtils.isEmpty(mdrcCommonTemplateFront) || StringUtils.isEmpty(mdrcCommonTemplateBack)){
                logger.info("上传的图片为空。mdrcCommonTemplateFront = " + mdrcCommonTemplateFront +
                        " ; mdrcCommonTemplateBack = "+mdrcCommonTemplateBack); 
                return false;
            }
            return true;
        }catch(Exception e){
            logger.error("校验上传文件出错，错误原因" + e.getMessage());
            return false;
        }
    }
    
    /**
     * 上传文件到S3
     * @author qinqinyan
     * @date 2017/08/16
     * */
    private boolean uploadFiles(MultipartHttpServletRequest multipartRequest, MdrcTemplate mdrcTemplate){
        try {
            Iterator<String> itr = multipartRequest.getFileNames();
            File file = new File("dest");

            //1、上传文件
            while (itr != null && itr.hasNext()) {
                // 获得文件实例
                MultipartFile multipartFile = multipartRequest.getFile(itr.next());

                try {
                    multipartFile.transferTo(file);
                } catch (IllegalStateException e) {
                    logger.error(e.getMessage());
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }

                String originalFilename = multipartFile.getOriginalFilename();
                String key = MD5.md5(UUID.randomUUID().toString());
                if (!StringUtils.isEmpty(originalFilename) && mdrcTemplate!=null) {
                    if (multipartFile.getName().equals("mdrcCommonTemplateFront")) {
                        if (!StringUtils.isEmpty(mdrcTemplate.getFrontImage())) {
                            key = mdrcTemplate.getFrontImage();
                        }
                        fileStoreService.save(key, file);
                        mdrcTemplate.setFrontImage(key);
                        mdrcTemplate.setFrontImageName(originalFilename);
                    } else if (multipartFile.getName().equals("mdrcCommonTemplateBack")) {
                        if (!StringUtils.isEmpty(mdrcTemplate.getRearImage())) {
                            key = mdrcTemplate.getRearImage();
                        }
                        fileStoreService.save(key, file);
                        mdrcTemplate.setRearImage(key);
                        mdrcTemplate.setRearImageName(originalFilename);
                    } 
                }
            }
            return true;
        } catch (Exception e) {
            logger.error("上传文件时出错，错误原因" + e.getMessage());
            return false;
        }
    }
    
    /**
     * 营销模板保存
     * @author qinqinyan
     * @throws IOException 
     * @date 2017/08/16
     * */
    @RequestMapping("saveAjax")
    public void saveAjax(MultipartHttpServletRequest multipartRequest,
            HttpServletResponse response, HttpServletRequest request) throws IOException{
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = "参数缺失，提交失败！";
        String code = "fail";
        
        String mdrcTemplateStr = request.getParameter("mdrcTemplate");
        if(!StringUtils.isEmpty(mdrcTemplateStr)){
            MdrcTemplate mdrcTemplate = JSON.parseObject(mdrcTemplateStr, MdrcTemplate.class);
            if(verifyTemplate(mdrcTemplate) && verifyMdrcTemplateImages(multipartRequest)){
                if(!s3Service.checkFile(multipartRequest)){
                    logger.info("上传的图片只支持jpg、jpeg和png这三种格式");
                    map.put("code", code);
                    map.put("msg", "上传的图片只支持jpg、jpeg和png这三种格式！");
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }
                
                if(s3Service.checkFileSize(multipartRequest)){
                    logger.info("上传的图片大于1MB");
                    map.put("code", code);
                    map.put("msg", "上传图片大小超过1MB!");
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }
                
                if(uploadFiles(multipartRequest, mdrcTemplate)){
                    
                    mdrcTemplate.setCreateTime(new Date());
                    mdrcTemplate.setCreatorId(getCurrentUser().getId());
                    mdrcTemplate.setDeleteFlag(0);
                    mdrcTemplate.setDeleteTime(null);
                    mdrcTemplate.setResourcesCount(0);
                    mdrcTemplate.setName(xssService.stripXss(mdrcTemplate.getName()));
                    mdrcTemplate.setTheme(xssService.stripXss(mdrcTemplate.getTheme()));
                    if(mdrcTemplateService.insert(mdrcTemplate)){
                        logger.info("用户Id：" + getCurrentUser().getId() + "成功添加模板,模板名称:" + mdrcTemplate.getName() + ",模板主题:" + mdrcTemplate.getTheme());
                        msg = "插入模板成功";
                        code = "success";
                    }else{
                        logger.info("用户Id：" + getCurrentUser().getId() + "添加模板失败,模板名称:" + mdrcTemplate.getName() + ",模板主题:" + mdrcTemplate.getTheme());
                        msg = "插入模板失败";
                    }
                }
            }
        }
        map.put("code", code);
        map.put("msg", msg);
        response.getWriter().write(JSON.toJSONString(map));
        return;
    }
    
    /**
     * 编辑页面
     * @author qinqinyan
     * @throws IOException 
     * @date 2017/08/16
     * */
    @RequestMapping("edit")
    public String edit(ModelMap modelMap, Long id){
        if(id!=null){
            MdrcTemplate mdrcTemplate = mdrcTemplateService.selectByPrimaryKey(id);
            if(mdrcTemplate!=null){
                modelMap.addAttribute("productSize", getProductSize(mdrcTemplate.getProductSize()));
                modelMap.addAttribute("mdrcTemplate", mdrcTemplate);
                return "mdrcTemplate/edit.ftl";
            }
        }
        modelMap.addAttribute("errorMsg", "未查找到相关记录");
        return "error.ftl";
    }
    
    private String getProductSize(String productSize){
        if(!StringUtils.isEmpty(productSize)){
            try{
                Integer prodSize = Integer.parseInt(productSize);
                if(prodSize.intValue()<1024){
                    return prodSize.toString() + "KB";
                }else if(prodSize.intValue()>=1024 && prodSize.intValue()<1024*1024){
                    return prodSize.intValue()/1024 + "MB";
                }else{
                    return prodSize.intValue()/1024/1024 + "GB";
                }
            }catch(Exception e){
                
            }
        }
        return "-";
    }
    
    /**
     * 保存编辑模板，只能更改图片
     * @author qinqinyan
     * @throws IOException 
     * @date 2017/08/16
     * */
    @RequestMapping("saveEditAjax")
    public void saveEditAjax(MultipartHttpServletRequest multipartRequest,
            HttpServletResponse response, HttpServletRequest request) throws IOException{
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = "参数缺失，提交失败！";
        String code = "fail";
        
        String mdrcTemplateStr = request.getParameter("mdrcTemplate");
        if(!StringUtils.isEmpty(mdrcTemplateStr)){
            MdrcTemplate mdrcTemplate = JSON.parseObject(mdrcTemplateStr, MdrcTemplate.class);
            if(mdrcTemplate!=null && mdrcTemplate.getId()!=null){
                if(!s3Service.checkFile(multipartRequest)){
                    logger.info("上传的图片只支持jpg、jpeg和png这三种格式");
                    map.put("code", code);
                    map.put("msg", "上传的图片只支持jpg、jpeg和png这三种格式！");
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }
                
                if(s3Service.checkFileSize(multipartRequest)){
                    logger.info("上传的图片大于5MB");
                    map.put("code", code);
                    map.put("msg", "上传图片大小超过5MB!");
                    response.getWriter().write(JSON.toJSONString(map));
                    return;
                }
                MdrcTemplate orgMdrcTemplate = mdrcTemplateService.selectByPrimaryKey(mdrcTemplate.getId());
                if(orgMdrcTemplate!=null && uploadFiles(multipartRequest, orgMdrcTemplate)){
                    mdrcTemplate.setRearImage(orgMdrcTemplate.getRearImage());
                    mdrcTemplate.setFrontImage(orgMdrcTemplate.getFrontImage());
                    mdrcTemplateService.updateByIdSeletive(mdrcTemplate);
                    logger.info("用户Id：" + getCurrentUser().getId() + "更新模板图片成功");
                    msg = "更新模板图片成功";
                    code = "success";
                    
                }else{
                    logger.info("用户Id：" + getCurrentUser().getId() + "添加模板失败,模板名称:" + mdrcTemplate.getName() + ",模板主题:" + mdrcTemplate.getTheme());
                    msg = "更新模板图片失败";
                }
            }
        }
        map.put("code", code);
        map.put("msg", msg);
        response.getWriter().write(JSON.toJSONString(map));
        return;
    }

    /**
     * @Title: save
     */
    @RequestMapping("save")
    public String save(ModelMap modelMap, String name, String theme, String proSize) {
        // String name = templateName.replaceAll(" ", "");

        if (StringUtils.isBlank(name) || StringUtils.isBlank(theme)) {
            modelMap.addAttribute("errorMsg", "请输入模板名称和主题。");
            modelMap.addAttribute("name", name);
            return create(modelMap);
        }
        QueryObject queryObject = new QueryObject();
        setQueryParameter("name", queryObject);
        int count = mdrcTemplateService.isExist(queryObject);

        if (count > 0) {
            modelMap.addAttribute("name", name);
            modelMap.addAttribute("errorMsg", "模板名称已存在。");
            return create(modelMap);
        }

        try {
            templateNameCheck(name);
        } catch (Exception e) {
            modelMap.addAttribute("name", name);
            modelMap.addAttribute("errorMsg", e.getMessage());
            return create(modelMap);
        }

        MdrcTemplate mdrcTemplate = new MdrcTemplate();
        mdrcTemplate.setCreateTime(new Date());
        mdrcTemplate.setCreatorId(getCurrentUser().getId());
        mdrcTemplate.setDeleteFlag(0);
        mdrcTemplate.setDeleteTime(null);
        mdrcTemplate.setName(name.replaceAll(" ", ""));
        mdrcTemplate.setFrontImage(null);
        mdrcTemplate.setRearImage(null);

        mdrcTemplate.setProductSize(proSize);

        mdrcTemplate.setResourcesCount(0);
        mdrcTemplate.setTheme(theme.replaceAll(" ", ""));

        mdrcTemplateService.insert(mdrcTemplate);

        logger.info("用户Id：" + getCurrentUser().getId() + "成功添加模板,模板名称:" + mdrcTemplate.getName() + ",模板主题:" + mdrcTemplate.getTheme());

        return "redirect:/manage/mdrc/template/index.html";
    }

    /**
     * 校验模板名称
     *
     * @date 2016年7月29日
     * @author wujiamin
     */
    private boolean templateNameCheck(String name) {
        try{
            PropertyValidator.isChineseLowerNumberUnderline(name, "模板名称");
            PropertyValidator.maxLengthCheck(name, 64, "模板名称");
            // 检验通过返回true
            return true;
        }catch(SelfCheckException e){
            return false;
        }
    }

    /**
     * @Title: upload
     */
    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public
    @ResponseBody
    String upload() {
        return "ok";
    }

    /**
     * @Title: upload
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public
    @ResponseBody
    String upload(MultipartHttpServletRequest request, HttpServletResponse response, long id) throws IOException {
        Long userId = Long.parseLong(request.getParameter("userId"));
        MdrcTemplate mdrcTemplate = mdrcTemplateService.selectByPrimaryKey(id);

        int flag = 1;
        if (mdrcTemplate == null || userId == null) {
            logger.info("当前用户为空或当前模板对象为空");
            response.sendError(403);

            return "failed";
        }

        try {
            Iterator<String> itr = request.getFileNames();
            MultipartFile multipartFile = null;

            // 迭代处理
            while (itr != null && itr.hasNext()) {
                // S3使用类，上传文件
                // 获得文件实例
                multipartFile = request.getFile(itr.next());

                // S3上传的文件的名称为模板Id_原上传文件名
                String fileName = multipartFile.getOriginalFilename();

                // 校验图片的格式
                if (!checkPicType(fileName)) {
                    continue;
                }

                // 校验图片大小
                Long size = multipartFile.getSize();
                if (size > SUPPORTED_PIC_MAX_SIZE) {
                    continue;
                }

                // 存到S3文件的该文件的Name
                String s3FileKeyName = "mdrc" + "_" + id + "_" + size + "_" + fileName;
                String s3MdrcKeyName = "mdrc" + "_" + id;

                String filePath = getTemplateFilePath() + File.separator + id + File.separator;

                // S3找到同名文件，现在不处理，之后看是否替换
                if (fileStoreService.exist(s3FileKeyName)) {
                    logger.info("S3存在同名文件" + s3FileKeyName);
                    continue;
                }

                // 保存到磁盘
                flag = mdrcTemplateService.writeFile(id, multipartFile.getOriginalFilename(), multipartFile.getBytes());

                logger.info("用户Id：" + userId + "上传文件\"" + multipartFile.getOriginalFilename() + "\"，大小" + multipartFile.getSize());

                File tempFile = new File(filePath + fileName);
                if (tempFile.exists()) {
                    logger.info("文件存在：" + filePath + fileName + ",向s3上传文件");
                    fileStoreService.save(s3FileKeyName, tempFile);
                    tempFile.delete();
                }

                // 更新资源个数
                List<String> s3ExistFileList = fileStoreService.getKeysStartWith(s3MdrcKeyName);
                logger.info("资源个数：" + s3MdrcKeyName + ",count:" + s3ExistFileList.size());
                mdrcTemplateService.updateResourcesCount(id, s3ExistFileList.size());

            }
            if (flag == 0) {
                return "ok";
            } else {
                response.sendError(412);
                return "failed";
            }

        } catch (Exception e) {
            logger.error("用户Id：" + userId + "上传文件出现异常\n " + e);
            return "failed";
        }
    }

    /**
     * @Title: delete
     */
    @RequestMapping(value = "/delete")
    public String delete(ModelMap map, long id, String filename) {

        MdrcTemplate mdrcTemplate = mdrcTemplateService.selectByPrimaryKey(id);
        //System.out.println(getCurrentUser().getId());
        if (mdrcTemplate == null || getCurrentUser() == null || !mdrcTemplate.getCreatorId().equals(getCurrentUser().getId())) {
            return detail(map, id);
        }
        //删除之前要判断这个图片是否已经被使用
        if (filename.equals(mdrcTemplate.getRearImage()) || filename.equals(mdrcTemplate.getFrontImage())) {
            logger.info(filename + "已经是模板id" + id + "使用的图片，不能删除");
            map.addAttribute("errorMsg", "图片已经被设置为模板正反面，禁止删除");
            return detail(map, id);
        }

        mdrcTemplateService.minusResourceCount(id);
        fileStoreService.deleteKeysStartWith(filename);

        return detail(map, id);
    }

    @RequestMapping(value = "/setCover")
    public String setCover(ModelMap map, long id, String front, String rear) {

        //设置图片前先判断
        MdrcTemplate mdrcTemplate = mdrcTemplateService.selectByPrimaryKey(id);
        if (mdrcTemplate != null) {
            if (StringUtils.isNotBlank(front)
                    && (StringUtils.isNotBlank(mdrcTemplate.getFrontImage())
                    && front.equals(mdrcTemplate.getFrontImage())
                    || StringUtils.isNotBlank(mdrcTemplate.getRearImage())
                    && front.equals(mdrcTemplate.getRearImage()))) {
                logger.info("用户Id：" + getCurrentUser().getId() + "设置模板背景图片，id：" + id + ",正面图片：" + front + "失败");
                map.addAttribute("errorMsg", "该图片已设置");
                return detail(map, id);
            }

            if (StringUtils.isNotBlank(rear)
                    && (StringUtils.isNotBlank(mdrcTemplate.getFrontImage())
                    && rear.equals(mdrcTemplate.getFrontImage())
                    || StringUtils.isNotBlank(mdrcTemplate.getRearImage())
                    && rear.equals(mdrcTemplate.getRearImage()))) {
                logger.info("用户Id：" + getCurrentUser().getId() + "设置模板背景图片，id：" + id + ",反面图片" + rear + "失败");
                map.addAttribute("errorMsg", "该图片已设置");
                return detail(map, id);
            }

            mdrcTemplateService.updateFrontAndRearImage(id, front, rear);
            logger.info("用户Id：" + getCurrentUser().getId() + "设置模板背景图片，id：" + id + ",正面图片：" + front + ",反面图片" + rear);
            return detail(map, id);
        } else {
            map.addAttribute("errorMsg", "为查询到该模板信息");
            return detail(map, id);
        }
    }

    @RequestMapping(value = "/getFile")
    public void getFile(long id, String filename, HttpServletResponse response) {

        byte[] data = null;
        try {
            data = mdrcTemplateService.getFile(id, filename);
            if (data != null) {
                String encoded = URLEncoder.encode(filename, "utf-8");
                response.addHeader("Content-Disposition", "attachment;filename=" + encoded);
                response.addHeader("Content-Length", data.length + "");

                response.getOutputStream().write(data);
                response.getOutputStream().flush();
            }
        } catch (Exception e) {
            logger.error("用户Id：" + getCurrentUser().getId() + " 读取文件 " + filename + " 出现异常 " + e);
        }

    }

    @RequestMapping(value = "/getFileS3")
    public void getFileS3(long id, String filename, HttpServletResponse response) {

        try {
            String encoded = URLEncoder.encode(filename, "utf-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + encoded);

            InputStream inputStream = fileStoreService.get(filename);
            StreamUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            logger.error("用户Id：" + getCurrentUser().getId() + " 读取文件 " + filename + " 出现异常 " + e);
        }

    }

    /**
     * @Title: check
     */
    @RequestMapping(value = "check")
    public void check(String name, HttpServletResponse response) throws IOException {
        String szname = name.toLowerCase().replaceAll(" ", "");
        Boolean bFlag = mdrcTemplateService.checkUnique(szname);
        response.getWriter().write(bFlag.toString());
    }

    public String getTemplateFilePath() {
        return globalConfigService.get(GlobalConfigKeyEnum.MDRC_TEMPLATE_FILE_PATH.getKey());
    }

    private boolean checkPicType(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return false;
        }
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // if (suffix == null) {
        // return false;
        // }
        for (String type : SURPPORTED_PIC_TYPE) {
            if (StringUtils.endsWithIgnoreCase(type, suffix)) {
                return true;
            }
        }
        return false;
    }
}