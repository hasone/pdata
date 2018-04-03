package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.xinjiang.XjBossServiceImpl;
import com.cmcc.vrp.enums.ProductStatus;
import com.cmcc.vrp.province.dao.CustomerTypeMapper;
import com.cmcc.vrp.province.dao.DiscountMapper;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Discount;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.Supplier;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductMap;
import com.cmcc.vrp.province.service.AdminEnterService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.vrp.util.CSVUtil;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.SizeUnits;
import com.cmcc.vrp.util.StringUtils;

/**
 * Created by leelyn on 2016/6/14.
 */
@Controller
@RequestMapping(value = "/manage/platformProduct")
public class PlatformProductController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformProductController.class);
    @Autowired
    AdministerService administerService;
    @Autowired
    DiscountMapper discountMapper;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    AdminEnterService adminEnterService;
    @Autowired
    EntManagerService entManagerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SupplierProductService spService;
    @Autowired
    private SupplierProductMapService spmService;
    @Autowired
    GlobalConfigService getGlobalConfigService;

    @Autowired
    private XjBossServiceImpl xjBossService;
    
    @Autowired
    private CustomerTypeMapper customerTypeMapper;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierProductMapService supplierProductMapService;

    /**
     * @Title: showProductList
     */
    @RequestMapping(value = "index")
    public String showProductList(QueryObject queryObject, ModelMap modelMap) {
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        return "platformProduct/index.ftl";
    }

    /**
     * @Title: createProduct
     */
    @RequestMapping(value = "create")
    public String createProduct(ModelMap modelMap, Product product) {
        if (modelMap == null) {
            modelMap = new ModelMap();
        }
        modelMap.addAttribute("product", product);
        
        //产品分类，类型由customer_type表确定
        modelMap.addAttribute("productCustomerType", customerTypeMapper.selectAll());
        
        return "platformProduct/create.ftl";
    }
    
    /**
     * 校验平台产品唯一性
     * @throws IOException 
     * @param response
     * @param productCode
     * @param productId 编辑时会携带产品Id
     * @author qinqinyan
     * @Date 2017/3/9
     * */
    @RequestMapping("checkProductCodeUnique")
    public void checkProductCodeUnique(HttpServletResponse response, String productCode, Long productId) throws IOException{
        Product product = productService.selectByCode(productCode);
        Boolean bFlag = false;
        if(product == null || 
                productId!=null && product.getId().toString().equals(productId.toString())){
            bFlag = true;
        }
        response.getWriter().write(bFlag.toString());
    }
    
    
    /**
     * @Title: save
     */
    @RequestMapping(value = "save")//, method = RequestMethod.POST
    public String save(ModelMap modelMap, Product product, String unit, String priceStr, String sizeStr) {
        // 1.检验所有参数是否通过校验
        if (!StringUtils.isEmpty(product.getName())) {
            product.setName(product.getName().replaceAll(" ", ""));
        }
        if (!validateProduct(modelMap, product)) {
            return createProduct(modelMap, product);
        }
        
        //校验产品编码是否唯一
        if(productService.selectByCode(product.getProductCode())!=null){
            modelMap.addAttribute("errorMsg", "系统中已存在该产品编码的平台产品!");
            return createProduct(modelMap, product);
        }

        // 设置初始默认值
        product = setDefaultValue(product, unit, priceStr, sizeStr);

        if (!productService.insertProduct(product)) {
            LOGGER.info("用户ID:" + getCurrentUser().getId() + " 插入新产品名称:" + product.getName() + "失败");
            modelMap.addAttribute("errorMsg", "插入产品失败");
            return createProduct(modelMap, product);
        }
        LOGGER.info("用户ID:" + getCurrentUser().getId() + " 插入新产品名称:" + product.getName() + "成功"
                + " 产品名称 " + product.getName() + " 产品编码 " + product.getProductCode() + " 产品大小" + product.getProductSize());

        //return "redirect:index.html";
        return "redirect:relation/" + product.getId() + ".html";
    }

    private Product setDefaultValue(Product product, String unit, String priceStr, String sizeStr) {
        if (product == null) {
            return product;
        }
        product.setName(product.getName().replace(" ", ""));
        //转换价格
        Double money = Double.parseDouble(priceStr);
        money = money * 100;
        product.setPrice(money.intValue());

        //转换流量
        Double size = Double.parseDouble(sizeStr);
        product.setProductSize(transformFlow(size, unit));

        product.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());

        return product;
    }

    private boolean validateProduct(ModelMap model, Product product) {
        if (product == null) {
            model.addAttribute("errorMsg", "无效的产品对象!");
            return false;
        }

        try {
            product.selfCheck();
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
            return false;
        }

        /**
         * 唯一性校验
         */
        //平台产品名称不做校验
        /*if (!productService.checkNameUnique(product)) {
            model.addAttribute("errorMsg", "平台产品名称已存在!");
            return false;
        }*/
        if (!productService.checkProductCodeUnique(product)) {
            model.addAttribute("errorMsg", "平台产品编码已存在!");
            return false;
        }

        return true;
    }


    /**
     * 建立平台产品与BOSS产品的关联
     *
     * @date 2016年6月16日
     * @author wujiamin
     */
    @RequestMapping("relation/{productId}")
    public String pltProduct2BossProduct(@PathVariable Long productId, ModelMap map) {
        Product p = productService.get(productId);
        map.put("platformProductId", p.getId());

        map.put("size", p.getProductSize());

        return "platformProduct/relation.ftl";
    }

    /**
     * 平台产品和BOSS产品的关联列表获取
     *
     * @date 2016年6月17日
     * @author wujiamin
     */
    @RequestMapping("getRelation")
    public void getRelation(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        setQueryParameter("platformProductId", queryObject);

        // 数据库查找符合查询条件的个数
        int count = spService.queryPaginationSupplierProduct2PltProductCount(queryObject);
        List<SupplierProduct> list = spService.queryPaginationSupplierProduct2PltProduct(queryObject);

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

    @RequestMapping("getBossProduct")
    public void getBossProduct(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 查询参数: 供应商、产品大小、产品名称、产品编码、使用范围、漫游范围
         */

        setQueryParameter("supplierName", queryObject);//供应商
        setQueryParameter("platformProductId", queryObject);

        setQueryParameter("size", queryObject);
        setQueryParameter("isp", queryObject);
        setQueryParameter("status", queryObject);

        setQueryParameter("name", queryObject);
        setQueryParameter("productCode", queryObject);
        setQueryParameter("roamingRegion", queryObject);
        setQueryParameter("ownershipRegion", queryObject);

        // 数据库查找符合查询条件的个数
        int count = spService.querySupplierProductAvailableCount(queryObject);

        List<SupplierProduct> list = spService.querySupplierProductAvailable(queryObject);

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
     * 增加平台产品与BOSS产品的关联
     *
     * @date 2016年6月17日
     * @author wujiamin
     */
    @RequestMapping("addRelation")
    public void addRelation(Long spId, Long pltProductId, HttpServletResponse res) {
        SupplierProductMap map = initSupplierProductMap(spId, pltProductId);

        JSONObject json = new JSONObject();

        if (spmService.create(map)) {
            LOGGER.info("增加平台产品与BOSS产品的关联成功，平台产品ID：{}，供应商产品ID：{}", pltProductId, spId);
            json.put("success", true);
        } else {
            LOGGER.info("增加平台产品与BOSS产品的关联失败，平台产品ID：{}，供应商产品ID：{}", pltProductId, spId);
            json.put("success", false);
        }

        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private SupplierProductMap initSupplierProductMap(Long spId, Long pltProductId) {
        SupplierProductMap map = new SupplierProductMap();
        map.setCreateTime(new Date());
        map.setDeleteFlag(0);
        map.setPlatformProductId(pltProductId);
        map.setSupplierProductId(spId);
        map.setUpdateTime(new Date());
        return map;
    }

    /**
     * 删除平台产品与BOSS产品的关联
     *
     * @date 2016年6月17日
     * @author wujiamin
     */
    @RequestMapping("deleteRelation")
    public void deleteRelation(Long spId, Long pltProductId, HttpServletResponse res) {
        JSONObject json = new JSONObject();
        if (spmService.delete(pltProductId, spId)) {
            json.put("success", true);
            LOGGER.info("删除平台产品与BOSS产品的关联成功，平台产品ID：{}，供应商产品ID：{}", pltProductId, spId);
        } else {
            json.put("success", false);
            LOGGER.info("删除平台产品与BOSS产品的关联失败，平台产品ID：{}，供应商产品ID：{}", pltProductId, spId);
        }
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 产品列表的搜索
     *
     * @date 2016年6月17日
     * @author wujiamin
     */
    @RequestMapping(value = "search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        setQueryParameter("productName", queryObject);
        setQueryParameter("productCode", queryObject);
        setQueryParameter("productSize", queryObject);
        setQueryParameter("unit", queryObject);
        setQueryParameter("isp", queryObject);
        setQueryParameter("roamingRegion", queryObject);
        setQueryParameter("ownershipRegion", queryObject);

        queryObject.getQueryCriterias().put("flowAccountFlag", Constants.FLOW_ACCOUNT_FLAG.REAL_PRODUCT.getCode());
        setQueryParameter("status", queryObject);

        // 数据库查找符合查询条件的个数
        int count = productService.getProductCount(queryObject);
        List<Product> list = productService.list(queryObject);

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", list);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上下架产品
     */
    @RequestMapping(value = "status")
    public void changeProductStatus(Long id, HttpServletResponse resp) throws IOException {
        Product product = productService.get(id);

        Map returnMap = new HashMap();

        String operType = "";//记录是上架还是下架
        if (product.getStatus() == Constants.PRODUCT_STATUS.ON.getStatus()) {
            product.setStatus(Constants.PRODUCT_STATUS.OFF.getStatus());
            operType = "下架";
        } else {
            product.setStatus(Constants.PRODUCT_STATUS.ON.getStatus());
            operType = "上架";
        }

        if (productService.changeProductStatus(product)) {
            LOGGER.error("用户ID:" + getCurrentUser().getId() + operType + "产品Id:" + id + "成功");
            returnMap.put("result", operType + "成功");
            resp.getWriter().write(JSON.toJSONString(returnMap));
            return;
        } else {
            LOGGER.error("用户ID:" + getCurrentUser().getId() + operType + "产品Id:" + id + "失败");
            returnMap.put("result", operType + "失败");
            resp.getWriter().write(JSON.toJSONString(returnMap));
            return;
        }
    }

    /**
     * 编辑产品
     */
    @RequestMapping(value = "edit")
    public String edit(ModelMap modelMap, Long productId) {

        Product product = productService.get(productId);
        if (product == null) {
            return "redirect:index.html";
        }

//        product.setUnit(product.getSize().substring(product.getSize().length() - 2));
//        product.setSize(product.getSize().substring(0, product.getSize().length() - 2));

        modelMap.addAttribute("product", product);
        
        //产品分类，类型由customer_type表确定
        modelMap.addAttribute("productCustomerType", customerTypeMapper.selectAll());

        return "platformProduct/update.ftl";
    }

    /**
     * @Title: saveEdit
     */
    @RequestMapping(value = "saveEdit")
    public String saveEdit(ModelMap modelMap, Product product, String unit, String priceStr, String sizeStr) {
        //重新请求时product不是null，但是提交的表单都是null
        if (product.getId() == null) {
            return "redirect:/manage/platformProduct/index.html";
        }

        // 1.检验所有参数是否通过校验
        if (!StringUtils.isEmpty(product.getName())) {
            product.setName(product.getName().replaceAll(" ", ""));
        }
        //增加后台校验
        if (!validateProduct(modelMap, product)) {
            return edit(modelMap, product.getId());
        }
        
        //校验产品编码是否唯一
        Product productFromDb = productService.selectByCode(product.getProductCode());
        if(productFromDb!=null 
                && !productFromDb.getId().toString().equals(product.getId().toString())){
            modelMap.addAttribute("errorMsg", "系统中已存在该产品编码的平台产品!");
            return createProduct(modelMap, product);
        }

        // 设置初始默认值
        product = setDefaultValue(product, unit, priceStr, sizeStr);

        if (!productService.updateProduct(product)) {
            LOGGER.info("用户ID:" + getCurrentUser().getId() + " 更新产品,名称:" + product.getName() + "失败");
            modelMap.addAttribute("errorMsg", "更新产品失败");
            return edit(modelMap, product.getId());
        }
        LOGGER.info("用户ID:" + getCurrentUser().getId() + " 更新产品,名称:" + product.getName() + "成功"
                + " 产品名称 " + product.getName() + " 产品编码 " + product.getProductCode() + " 产品大小" + product.getProductSize());

        //return "redirect:index.html";
        //return pltProduct2BossProduct(product, modelMap);
        return "redirect:relation/" + product.getId() + ".html";
    }


    /**
     * 删除产品
     */
    @RequestMapping(value = "delete")
    public String delete(ModelMap modelMap, Long id, QueryObject queryObject) {
        if (id == null) {
            return showProductList(queryObject, modelMap);
        }

        try {
            if (!productService.deletePlatformProduct(id)) {
                LOGGER.info("用户ID:" + getCurrentUser().getId() + " 删除产品Id:" + id + "失败");
            } else {
                LOGGER.info("用户ID:" + getCurrentUser().getId() + " 删除产品Id:" + id + "成功");
            }
        } catch (RuntimeException e) {
            LOGGER.error("用户ID:" + getCurrentUser().getId() + " 删除产品Id:" + id + "失败,原因:" + e.toString());
        }

        return showProductList(queryObject, modelMap);
    }

    /**
     * 查看产品详情
     */
    @RequestMapping(value = "getDetail")
    public String getProductDetail(ModelMap modelMap, Long productId) {

        Product product = productService.get(productId);
        if (product == null) {
            return "redirect:index.html";
        }
        /*
        if(product.getStatus().intValue() != Constants.PRODUCT_STATUS.ON.getStatus()){
			product.setUnit(product.getSize().substring(product.getSize().length()-2));
			String productSize = product.getSize().substring(0, product.getSize().length()-2);

			modelMap.addAttribute("productSize", productSize);
		}*/

        if (isZiying()) {
            List<Supplier> suppliers = supplierService.getSupplierByPrdId(productId);
            modelMap.addAttribute("suppliers", suppliers);
            Supplier priorSupplier = supplierService.getPriorSupplierByPrdid(productId);
            if (priorSupplier != null) {
                modelMap.addAttribute("priorSupplier", priorSupplier);
            } else {
                priorSupplier = new Supplier();
                priorSupplier.setId(0l);
                modelMap.addAttribute("priorSupplier", priorSupplier);
            }
        }
        modelMap.addAttribute("product", product);
        modelMap.addAttribute("ziying", isZiying().toString());

        return "platformProduct/detail.ftl";
    }

    
    /**
     * plus优先选择供应商
     * @param request
     * @param resp
     * @param supplierId
     * @param prdId
     * @throws IOException
     */
    @RequestMapping("selectSupplierAjax")
    public void selectSupplierAjax(HttpServletRequest request, HttpServletResponse resp, Long supplierId, Long prdId) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        LOGGER.info("平台产品id-{}优先选择供应商-{}", prdId, supplierId);
        if (supplierProductMapService.updatePriorSupplier(prdId, supplierId)) {
            map.put("msg", "success");    
        } else {
            map.put("msg", "更新失败"); 
        }
        resp.getWriter().write(JSON.toJSONString(map));
        return;
    }
    
    
    /**
     * @Title: showOrderList
     */
    @RequestMapping(value = "orderIndex")
    public String showOrderList(ModelMap modelMap, QueryObject queryObject) {
        //判断是否是新疆
        boolean isXjProvince = "xj".equalsIgnoreCase(getProvinceFlag());

        //新疆的特殊处理
        if (isXjProvince) {
            return showXjOrderList(modelMap);
        }

        //自营平台不用显示流量包个数
        String countFlag = "true";
        String provinceFlag = getProvinceFlag();
        String enterpriseContractor = getENTERPRISE_CONTACTOR();

        if (provinceFlag != null && provinceFlag.equals(zyProvinceFlagValue)) {
            countFlag = "false";
        }
        modelMap.addAttribute("countFlag", countFlag);

        //获取当前客户的企业信息
        //当前用户需要是企业管理员
        Administer admin = getCurrentUser();

        if (admin.getRoleId().toString().equals(enterpriseContractor)) {
            List<Enterprise> enters = enterprisesService.getEnterpriseListByAdminId(admin);
            Enterprise e = null;
            if (enters != null
                    && enters.size() > 0
                    && (e = enters.get(0)) != null) {
                modelMap.addAttribute("enterprise", e);
                Discount d = discountMapper.selectByPrimaryKey(e.getDiscount());
                modelMap.addAttribute("discount", d);

                return "platformProduct/orderIndex.ftl";
            } else {
                modelMap.addAttribute("errorMsg", "企业管理员没有关联企业");
                return "error.ftl";
            }

        } else {
            modelMap.addAttribute("errorMsg", "该用户非企业管理员");
            return "error.ftl";
        }

    }

    //新疆特有的页面,不要问我为什么,我怕我会控制不了我自己...
    private String showXjOrderList(ModelMap modelMap) {
        //获取当前客户的企业信息
        //当前用户需要是企业管理员
        Administer admin = getCurrentUser();
        if (admin.getRoleId().toString().equals(getENTERPRISE_CONTACTOR())) {
            List<Enterprise> enterprises = enterprisesService.getEnterpriseListByAdminId(admin);
            if (enterprises != null) {
                Enterprise e = enterprises.get(0);
                modelMap.addAttribute("enterprise", e);
                Discount d = discountMapper.selectByPrimaryKey(e.getDiscount());
                modelMap.addAttribute("discount", d);

                String result = xjBossService.queryAccountByEntId(e.getId());
                modelMap.addAttribute("flowCount", result);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                modelMap.addAttribute("startTime", sdf.format(e.getStartTime()));
                modelMap.addAttribute("endTime", sdf.format(e.getEndTime()));
            }
        }

        //新疆要返回特有的产品余额页面...
        return "platformProduct/xjOrderIndex.ftl";
    }


    /**
     * 企业订购列表查找
     */
    @RequestMapping(value = "/searchOrderList")
    public void searchOrderList(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        setQueryParameter("productName", queryObject);
        setQueryParameter("productCode", queryObject);
        setQueryParameter("size", queryObject);
        setQueryParameter("status", queryObject);
        setQueryParameter("enterId", queryObject);

        // 数据库查找符合查询条件的个数
        int count = productService.getProductCountFromEnterAccount(queryObject);
        List<Product> list = productService.getProductListFromEnterAccount(queryObject);

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
     * 根据前端输入的单位转换流量
     *
     * @date 2016年6月15日
     * @author wujiamin
     */
    private Long transformFlow(Double size, String unit) {
        if ("KB".equals(unit)) {
            return SizeUnits.KB.toKB(size);
        }
        if ("MB".equals(unit)) {
            return SizeUnits.MB.toKB(size);
        }
        if ("GB".equals(unit)) {
            return SizeUnits.GB.toKB(size);
        }
        if ("TB".equals(unit)) {
            return SizeUnits.TB.toKB(size);
        }
        return null;
    }
    
    /**
     * 下载
     * @param response
     * @author qinqinyan
     * */
    @RequestMapping("downloadPlatformProducts")
    public void downloadPlatformProducts(HttpServletResponse response, String productName,
                                         String productCode, String productSize, String unit,
                                         String isp, String roamingRegion, String ownershipRegion,
                                         String status){

        QueryObject queryObject = new QueryObject();
        if(!StringUtils.isEmpty(productName)){
            queryObject.getQueryCriterias().put("productName", productName);
        }
        if(!StringUtils.isEmpty(productCode)){
            queryObject.getQueryCriterias().put("productCode", productCode);
        }
        if(!StringUtils.isEmpty(productSize)){
            queryObject.getQueryCriterias().put("productSize", productSize);
            queryObject.getQueryCriterias().put("unit", unit);
        }
        if(!StringUtils.isEmpty(isp)){
            queryObject.getQueryCriterias().put("isp", isp);
        }
        if(!StringUtils.isEmpty(roamingRegion)){
            queryObject.getQueryCriterias().put("roamingRegion", roamingRegion);
        }
        if(!StringUtils.isEmpty(ownershipRegion)){
            queryObject.getQueryCriterias().put("ownershipRegion", ownershipRegion);
        }
        if(!StringUtils.isEmpty(status)){
            queryObject.getQueryCriterias().put("status", status);
        }
        //查出来的是真是产品，很重要
        queryObject.getQueryCriterias().put("flowAccountFlag", Constants.FLOW_ACCOUNT_FLAG.REAL_PRODUCT.getCode());
        //去掉分页
        queryObject.setPageSize(-1);

        List<Product> products = productService.list(queryObject);

        List<String> titles = new ArrayList<String>();
        titles.add("序号");
        titles.add("运营商");
        titles.add("产品名称");
        titles.add("产品编码");
        titles.add("产品大小");
        titles.add("售出价格(元)");
        titles.add("状态");
        titles.add("使用范围");
        titles.add("漫游范围");

        List<String> rowList = new ArrayList<String>();
        for(int i =1; i<=products.size(); i++){
            rowList.add("" + i);
            rowList.add(getIspName(products.get(i-1).getIsp()));
            rowList.add(products.get(i-1).getName());
            rowList.add(products.get(i-1).getProductCode());
            rowList.add(getSizeStr(products.get(i-1).getProductSize()));
            rowList.add(""+products.get(i-1).getPrice().intValue()/100.0);

            if(products.get(i-1).getStatus().intValue()== ProductStatus.NORMAL.getCode().intValue()){
                rowList.add("上架");
            }else{
                rowList.add("下架");
            }
            rowList.add(products.get(i-1).getOwnershipRegion());
            rowList.add(products.get(i-1).getRoamingRegion());
        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, titles));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "platform_products.csv");
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

    private String getIspName(String isp){
        if(!StringUtils.isEmpty(isp)){
            if("M".equals(isp)){
                return "移动";
            }else if("T".equals(isp)){
                return "电信";
            }else if("U".equals(isp)){
                return "联通";
            }else {
                return "三网";
            }
        }
        return "-";
    }
    
    private String getSizeStr(Long size){
        if(size!=null){
            if(size.longValue()<1024){
                return size.toString() + "KB";
            }else if(size.longValue()>=1024 && size.longValue()<1024*1024){
                return size.longValue()/1024 + "MB";
            }else{
                return size.longValue()/1024/1024 + "GB";
            }
        }
        return "-";
    }

}
