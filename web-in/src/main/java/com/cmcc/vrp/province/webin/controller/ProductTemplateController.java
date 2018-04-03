package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.PlatformProductTemplateMap;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.ProductTemplate;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.PlatformProductTemplateMapService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.ProductTemplateEnterpriseMapService;
import com.cmcc.vrp.province.service.ProductTemplateService;
import com.cmcc.vrp.province.service.XssService;
import com.cmcc.vrp.util.CSVUtil;
import com.cmcc.vrp.util.QueryObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qinqinyan on 2017/3/10. 产品模板
 */
@Controller
@RequestMapping("manage/productTemplate")
public class ProductTemplateController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ProductTemplateController.class);

    @Autowired
    ProductTemplateService productTemplateService;
    @Autowired
    GlobalConfigService getGlobalConfigService;
    @Autowired
    ProductService productService;
    @Autowired
    PlatformProductTemplateMapService platformProductTemplateMapService;
    @Autowired
    ProductTemplateEnterpriseMapService productTemplateEnterpriseMapService;
    @Autowired
    ManagerService managerService;
    @Autowired
    XssService xssService;

    /**
     * 产品模板列表
     * 
     * @author qinqinyan
     */
    @RequestMapping("index")
    public String index(QueryObject queryObject, ModelMap modelMap) {
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        if (getCurrentUserManager() == null) {
            return getLoginAddress();
        }
        return "productTemplate/index.ftl";
    }

    /**
     * 查询产品模板列表
     * 
     * @author qinqinyan
     */
    @RequestMapping("search")
    public void search(HttpServletResponse response, QueryObject queryObject) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        setQueryParameter("name", queryObject);

        List<ProductTemplate> list = productTemplateService.selectByMap(queryObject.toMap());
        long count = productTemplateService.countByMap(queryObject.toMap());

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            response.getWriter().write(json.toString());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 创建产品模板
     * 
     * @param map
     * @param id
     * @author qinqinyan
     */
    @RequestMapping("create")
    public String create(ModelMap map, Long id) {
        ProductTemplate productTemplate = null;
        if (id != null) {
            // 编辑
            productTemplate = productTemplateService.selectByPrimaryKey(id);
        }
        map.put("templateId", id);
        map.put("productTemplate", productTemplate);
        return "productTemplate/create.ftl";
    }

    /**
     * 编辑产品模板
     */
    @RequestMapping("edit")
    public String edit(ModelMap modelMap, Long id) {
        if (id != null) {
            ProductTemplate productTemplate = productTemplateService.selectByPrimaryKey(id);
            Map map = new HashMap<String, String>();
            map.put("id", id);
            List<Product> products = productService.listProductsByTemplateId(map);

            modelMap.addAttribute("productTemplate", productTemplate);
            modelMap.put("products", JSON.toJSONString(products));
            return "productTemplate/edit.ftl";
        }
        modelMap.addAttribute("errorMsg", "参数异常，未查找相应产品模板!");
        return "error.ftl";
    }

    /**
     * 获取产品模板关联的所有产品
     * 
     * @author qinqinyan
     */
    @RequestMapping("getRelation")
    public void getRelation(HttpServletResponse response, QueryObject queryObject) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        // 模板id
        setQueryParameter("id", queryObject);
        List<Product> list = productService.listProductsByTemplateId(queryObject.toMap());
        int count = productService.countProductsByTemplate(queryObject.toMap());

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            response.getWriter().write(json.toString());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 获取平台产品列表
     * 
     * @author qinqinyan
     */
    @RequestMapping("getProducts")
    public void getProducts(HttpServletResponse response, QueryObject queryObject) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        /**
         * 查询参数:产品名称，产品编码，产品大小，运营商，使用范围，漫游范围，状态
         */
        setQueryParameter("productName", queryObject);// 供应商
        setQueryParameter("productCode", queryObject);
        setQueryParameter("size", queryObject);
        setQueryParameter("unit", queryObject);
        setQueryParameter("isp", queryObject);
        setQueryParameter("status", queryObject);
        setQueryParameter("roamingRegion", queryObject);
        setQueryParameter("ownershipRegion", queryObject);
        setQueryParameter("productIds", queryObject); // 已经选择的产品id
        // setQueryParameter("productTemplateId", queryObject); //已经选择的产品id

        List<Product> list = productService.getProductListAvailable(queryObject);
        int count = productService.countProductListAvailable(queryObject);

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            response.getWriter().write(json.toString());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 保存新建或者编辑
     * 
     * @param response
     * @param request
     * @author qinqinyan
     */
    @RequestMapping("saveAjax")
    public void saveAjax(HttpServletResponse response, HttpServletRequest request) {
        Map map = new HashMap<String, String>();
        Boolean flag = false;
        String msg = "模板参数为空,保存失败!";
        String productTemplateStr = request.getParameter("productTemplate");
        String platformProductTemplateMaps = request.getParameter("platformProductTemplateMaps");

        if (!StringUtils.isBlank(productTemplateStr) && !StringUtils.isBlank(platformProductTemplateMaps)) {
            try {
                ProductTemplate productTemplate = JSON.parseObject(productTemplateStr, ProductTemplate.class);
                List<PlatformProductTemplateMap> maps = JSON.parseArray(platformProductTemplateMaps,
                        PlatformProductTemplateMap.class);
                
                productTemplate.setName(xssService.stripQuot(productTemplate.getName()));
                if (productTemplate.getId() != null) {
                    // 编辑产品模板
                    if (productTemplateService.editProductTemplate(productTemplate, maps)) {
                        logger.info("编辑成功！");
                        msg = "保存成功！";
                        flag = true;
                    } else {
                        logger.info("编辑失败！" + JSON.toJSONString(productTemplate));
                        msg = "保存失败！";
                    }
                } else {
                    // 新建产品模板
                    if (productTemplateService.createProductTemplate(productTemplate, maps)) {
                        logger.info("保存成功！");
                        msg = "保存成功！";
                        flag = true;
                    } else {
                        logger.info("保存失败！" + JSON.toJSONString(productTemplate));
                        msg = "保存失败！";
                    }
                }
            } catch (RuntimeException e) {
                logger.error("保存失败！" + e.getMessage());
                msg = "保存失败!";
            } catch (Exception e) {
                logger.error(e.getMessage());
                msg = "参数异常,保存失败!";
            }
        } else {
            logger.info("模板参数为空,保存失败!");
        }
        map.put("result", flag.toString());
        map.put("msg", msg);
        try {
            response.getWriter().write(JSON.toJSONString(map));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
        }
    }

    /**
     * 下载
     * @param response
     * @param name
     * @author qinqinyan
     */
    @RequestMapping("downloadProductTemplates")
    public void downloadProductTemplates(HttpServletResponse response, String name) {

        Map map = new HashMap();
        /**
         * 查询参数: 模板名称
         */
        map.put("name", name);
        List<ProductTemplate> productTemplates = productTemplateService.selectByMap(map);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> titles = new ArrayList<String>();
        titles.add("序号");
        titles.add("产品模板名称");
        titles.add("创建时间");
        titles.add("修改时间");

        List<String> rowList = new ArrayList<String>();

        for (int i = 1; i <= productTemplates.size(); i++) {
            rowList.add("" + i);
            rowList.add(productTemplates.get(i - 1).getName());
            rowList.add(sdf.format(productTemplates.get(i - 1).getCreateTime()));
            rowList.add(sdf.format(productTemplates.get(i - 1).getUpdateTime()));
        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, titles));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "product_template.csv");
            byte[] b = new byte[100];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
            inputStream.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    /**
     * 查看产品模板详情
     * 
     * @param modelMap
     * @param id
     * @author qinqinyan
     */
    @RequestMapping("showDetail")
    public String showDetail(ModelMap modelMap, Long id) {
        if (id != null) {
            ProductTemplate productTemplate = productTemplateService.selectByPrimaryKey(id);
            Map map = new HashMap<String, String>();
            map.put("id", id);
            List<Product> products = productService.listProductsByTemplateId(map);

            modelMap.addAttribute("productTemplate", productTemplate);
            modelMap.addAttribute("platformProducts", products);
            return "productTemplate/detail.ftl";
        }
        modelMap.addAttribute("errorMsg", "参数异常,未查找到该条记录相关信息!");
        return "error.ftl";
    }

    /**
     * 查看关联企业
     * 
     * @param modelMap
     * @param templateId
     * @author qinqinyan
     */
    @RequestMapping("relatedEnterprises")
    public String relatedEnterprises(ModelMap modelMap, Long templateId) {
        if (getCurrentUserManager() == null) {
            return getLoginAddress();
        }
        modelMap.addAttribute("templateId", templateId);
        return "productTemplate/relatedEnterprises.ftl";
    }

    /**
     * 查找模板关联企业ajax
     */
    @RequestMapping("searchRelatedEnterprises")
    public void searchRelatedEnterprises(HttpServletRequest request, HttpServletResponse response,
            QueryObject queryObject) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        /**
         * 查询参数: 产品模板id
         */
        setQueryParameter("templateId", queryObject);

        List<Enterprise> enterprises = productTemplateEnterpriseMapService
                .selectRelatedEnterprises(queryObject.toMap());
        int count = productTemplateEnterpriseMapService.countRelatedEnterprises(queryObject.toMap());

        if (enterprises != null) {
            for (Enterprise enterprise : enterprises) {
                // 地区全称
                Manager districtName = entManagerService.getManagerForEnter(enterprise.getId());
                if (districtName != null) {
                    String fullname = "";
                    enterprise.setCmManagerName(
                            managerService.getFullNameByCurrentManagerId(fullname, districtName.getParentId()));
                }
            }
        }

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", enterprises);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            response.getWriter().write(json.toString());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 判断该模板是否正在被企业使用
     * 
     * @param response
     * @param id
     * @author qinqinyan
     */
    @RequestMapping("whetherUseProdTemplateAjax")
    public void whetherUseProdTemplateAjax(HttpServletResponse response, Long id) {
        Map returnMap = new HashMap<String, String>();
        Boolean flag = false;
        String msg = "";
        if (productTemplateService.whetherUseProdTemplate(id)) {
            flag = true;
            msg = "该模版已有企业关联，删除后会导致这些企业无可用产品，是否删除该模版？";
        }
        returnMap.put("flag", flag.toString());
        returnMap.put("msg", msg);
        try {
            response.getWriter().write(JSON.toJSONString(returnMap));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 删除产品模板
     * 
     * @param response
     * @param id
     * @author qinqinyan
     */
    @RequestMapping("delProductTemplateAjax")
    public void delProductTemplateAjax(HttpServletResponse response, Long id) {
        Map returnMap = new HashMap<String, String>();
        String msg = "参数异常，删除模板失败";

        Administer administer = getCurrentUser();
        if (administer != null) {
            if (id != null) {
                try {
                    if (productTemplateService.deleteProductTemplate(id)) {
                        logger.info("删除产品模板成功。模板id = {}, 操作用户id = {}", id, administer.getId());
                        msg = "删除成功";
                    } else {
                        msg = "删除模板失败";
                    }
                } catch (RuntimeException e) {
                    logger.error("删除产品模板失败，失败原因：" + e.getMessage());
                    msg = "删除模板失败";
                }
            }
        }
        returnMap.put("msg", msg);
        try {
            response.getWriter().write(JSON.toJSONString(returnMap));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 校验产品模板唯一性
     * 
     * @throws IOException
     * @param response
     * @param name
     * @param productTemplateId
     * @author qinqinyan
     * @Date 2017/5/9
     */
    @RequestMapping("checkNameUnique")
    public void checkProductCodeUnique(HttpServletResponse response, String name, Long productTemplateId)
            throws IOException {
        Map<String, String> returnMap = new HashMap<String, String>();
        Boolean result = true;
        List<ProductTemplate> list = productTemplateService.selectByName(name);
        if (list != null && list.size() > 0) {
            if (productTemplateId != null) {
                // 编辑
                for (ProductTemplate item : list) {
                    if (!item.getId().toString().equals(productTemplateId.toString())) {
                        result = false;
                        break;
                    }
                }
            } else {
                // 新建
                result = false;
            }
        }
        returnMap.put("result", result.toString());
        response.getWriter().write(JSON.toJSONString(returnMap));
    }

}
