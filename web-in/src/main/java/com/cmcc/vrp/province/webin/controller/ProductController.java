package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.dao.DiscountMapper;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Discount;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.EnterprisesExtInfo;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.EnterprisesExtInfoService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;

/**
 * ProductController.java
 */
@Controller("manage.product.productController")
@RequestMapping("/manage/product")
public class ProductController extends BaseController {

    private Logger logger = Logger.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private EnterprisesService enterprisesService;

    @Autowired
    private DiscountMapper discountMapper;

    @Autowired
    private AccountService accountService;
    @Autowired
    EnterprisesExtInfoService enterprisesExtInfoService;

    /**
     * @Title:showProductList
     * @Description: 后台列出产品列表
     * @author: xuwanlin
     */
    @RequestMapping(value = "index")
    public String showProductList(ModelMap modelMap, QueryObject queryObject) {
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        return "product/index.ftl";
    }

    /**
     * 产品列表查找
     */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        setQueryParameter("productName", queryObject);
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
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    @RequestMapping(value = "orderIndex")
    public String showOrderList(ModelMap modelMap, QueryObject queryObject) {
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
            }
        }

        return "platformProduct/orderIndex.ftl";
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
        setQueryParameter("status", queryObject);
        setQueryParameter("enterId", queryObject);

        // 数据库查找符合查询条件的个数
        int count = productService.getProductCountForEnter(queryObject);
        List<Product> list = productService.getProductListForEnter(queryObject);

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
     * 添加或编辑产品
     */
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String create(ModelMap modelMap, Product product) {
        if (modelMap == null) {
            modelMap = new ModelMap();
        }
        modelMap.addAttribute("product", product);

        return "product/create.ftl";
    }

    /**
     *
     * @Title:update
     * @Description: 更新产品
     * @param modelMap
     * @param id
     * @param name
     * @param code
     * @param size
     * @param unit 单位
     * @return
     * @throws
     * @author: xuwanlin
     */
    /*@RequestMapping("update")
    public String update(ModelMap modelMap, Long id, String name, String productCode,
			String size) {
		if (id == null) {
			modelMap.addAttribute("errorMsg", "无效的产品ID!");
			return "error.ftl";
		}
		if (name == null) {
			modelMap.addAttribute("errorMsg", "产品名称必需存在!");
			getProductDetail(modelMap, id);
		}
		if(productCode == null){
			modelMap.addAttribute("errorMsg", "产品名称必需存在!");
			getProductDetail(modelMap, id);
		}
		if (size == null) {
			modelMap.addAttribute("errorMsg", "流量包大小不能为空!");
			getProductDetail(modelMap, id);
		}
		Product product = productService.get(id);
		product.setName(name);
		product.setProductCode(productCode);
		product.setSize(size);

		if (!productService.updateProduct(product)) {
			logger.error("用户ID:"+getCurrentUser().getId()+" 更新产品Id:"+id+" 失败 "); 
			modelMap.addAttribute("errorMsg", "更新产品出错!");
			return "error.ftl";
		}

		logger.error("用户ID:"+getCurrentUser().getId()+" 更新产品Id:"+id+" 成功 " 
				+ " 产品名称 " + name + " 产品编码 " + productCode + " 产品大小" +size);
		
		return "redirect:index.html";
	}*/

    /**
     * @Title:update
     * @Description: 更新产品重构
     * @author: qihang
     */
    @RequestMapping("update")
    public String update(ModelMap modelMap, Product product) {
        // 1.检验所有参数是否通过校验
        if (product.getId() == null || !validateProduct(modelMap, product)) {
            return getProductDetail(modelMap, product.getId());
        }

        if (!productService.updateProduct(product)) {
            logger.error("用户ID:" + getCurrentUser().getId() + " 更新新产品Id:" + product.getId() + "失败");
            modelMap.addAttribute("errorMsg", "更新产品出错!");
            return getProductDetail(modelMap, product.getId());
        }
        logger.error("用户ID:" + getCurrentUser().getId() + " 更新产品名称:" + product.getName() + "成功"
                + " 产品ID " + product.getId());

        return "redirect:index.html";
    }

    /**
     * @Title: saveEdit
     */
    @RequestMapping(value = "saveEdit")
    public String saveEdit(ModelMap modelMap, Product product) {

        if (product == null) {
            return "redirect:index.html";
        }

        product.setName(product.getName().replace(" ", ""));
        product.setUpdateTime(new Date());

        productService.updateProduct(product);
        return "redirect:index.html";
    }

    /**
     * @Title:save
     * @Description: 保存产品对象
     * @author: xunwanlin
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(ModelMap modelMap, Product product) {
        // 1.检验所有参数是否通过校验
        if (!StringUtils.isEmpty(product.getName())) {
            product.setName(product.getName().replaceAll(" ", ""));
        }
        if (!validateProduct(modelMap, product)) {
            return create(modelMap, product);
        }
        // 设置初始默认值
        setDefaultValue(product);
        if (!productService.insertProduct(product)) {
            logger.error("用户ID:" + getCurrentUser().getId() + " 插入新产品名称:" + product.getName() + "失败");
            modelMap.addAttribute("errorMsg", "插入产品失败");
            return create(modelMap, product);
        }
        logger.error("用户ID:" + getCurrentUser().getId() + " 插入新产品名称:" + product.getName() + "成功"
                + " 产品名称 " + product.getName() + " 产品编码 " + product.getProductCode() + " 产品大小" + product.getProductSize());

        return "redirect:index.html";
    }

    /**
     * 上下架产品
     */
    @RequestMapping(value = "changeProductStatus")
    public String changeProductStatus(ModelMap modelMap, Long id,
                                      QueryObject queryObject) {

        Product product = productService.get(id);
        if (product == null) {
            return showProductList(modelMap, queryObject);
        }

        String operType = "";//记录是上架还是下架
        if (product.getStatus() == Constants.PRODUCT_STATUS.ON.getStatus()) {
            product.setStatus(Constants.PRODUCT_STATUS.OFF.getStatus());
            operType = "下架";
        } else {
            product.setStatus(Constants.PRODUCT_STATUS.ON.getStatus());
            operType = "上架";
        }

        if (productService.changeProductStatus(product)) {
            logger.error("用户ID:" + getCurrentUser().getId() + operType + "产品Id:" + id + "成功");

            //检查当前审核状态中的产品变更记录是否有该下架产品
            if (product.getStatus() == Constants.PRODUCT_STATUS.OFF.getStatus()) {
                //pcrService.updateWhenProductDown(product.getId());


            }
        } else {
            logger.error("用户ID:" + getCurrentUser().getId() + operType + "产品Id:" + id + "失败");
        }

        return showProductList(modelMap, queryObject);
    }

    /**
     * 删除产品
     */
    @RequestMapping(value = "delete")
    public String delete(ModelMap modelMap, Long id, QueryObject queryObject) {
        if (id == null) {
            return showProductList(modelMap, queryObject);
        }

        if (!productService.delete(id)) {
            logger.error("用户ID:" + getCurrentUser().getId() + " 删除产品Id:" + id + "失败");
        } else {
            logger.error("用户ID:" + getCurrentUser().getId() + " 删除产品Id:" + id + "成功");
        }

        return showProductList(modelMap, queryObject);
    }

    /**
     * @Title:getProductDetail
     * @Description: TODO
     * @author: xuwanlin
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

        modelMap.addAttribute("product", product);

        return "product/detail.ftl";
    }

    /**
     * @Title: edit
     */
    @RequestMapping(value = "edit")
    public String edit(ModelMap modelMap, Long productId) {

        Product product = productService.get(productId);
        if (product == null) {
            return "redirect:index.html";
        }

        modelMap.addAttribute("product", product);

        return "product/update.ftl";
    }

    /**
     * @Title:check
     * @Description: 创建或修改产品时对产品名称检验
     * @author: xuwanlin
     */
    @RequestMapping(value = "checkName")
    public void checkName(HttpServletRequest request,
                          HttpServletResponse response, Product product) throws IOException {
        Boolean bFlag = true;
        //过滤空格
        if (!StringUtils.isEmpty(product.getName())) {
            product.setName(product.getName().replaceAll(" ", ""));
        }

        if (!productService.checkNameUnique(product)) {
            bFlag = false;
        }

        response.getWriter().write(bFlag.toString());
    }

    /**
     * @Title:checkProductCode
     * @Description: TODO
     * @author: xuwanlin
     */
    @RequestMapping(value = "checkProductCode")
    public void checkProductCode(HttpServletRequest request,
                                 HttpServletResponse response, Product product) throws IOException {
        Boolean bFlag = true;

        if (!productService.checkProductCodeUnique(product)) {
            bFlag = false;
        }

        response.getWriter().write(bFlag.toString());
    }


    /**
     * 根据企业ID异步获取产品列表
     *
     * @param enterpriseId 企业ID
     * @param resp         响应对象
     */
    @RequestMapping(value = "getProductsAjax")
    public void getProductsAjax(@RequestParam(value = "enterpriseId", required = false) Long enterpriseId,
                                ModelMap modelMap, HttpServletResponse resp) throws IOException {
        if (enterpriseId == null) {
            return;
        }

            
        
        //全部产品
        List<Product> products = productService.selectAllProductsByEnterId(enterpriseId);
        
        //传入产品信息：山东只能选择产品编码为***1092***的产品
        if ("sd".equals(getProvinceFlag()) && products != null) {
            List<Product> sdProducts = products;
            products = new LinkedList<Product>();           
            Iterator<Product> it = sdProducts.iterator();
            while (it.hasNext()) {
                Product p = it.next();
                String productCode = p.getProductCode();
                if (productCode.length() > 6
                        && productCode.substring(productCode.length() - 6, productCode.length()).startsWith("1092")) {
                    products.add(p);
                }
            }
        }                        
        modelMap.put("products", products);

        //显示企业余额
        Account account = accountService.getCurrencyAccount(enterpriseId);
        if (account != null) {
            modelMap.put("account", account.getCount());
        }

        //响应信息
        StreamUtils.copy(JSON.toJSONString(modelMap), Charsets.UTF_8, resp.getOutputStream());
    }


    /**
     * @Title:getProductsAjaxBySizeEnterName
     * @Description: 根据流量包大小和企业名称查询相应的产品列表
     * @author: caohaibing
     */
    @RequestMapping(value = "getProductsAjaxBySizeEnterId")
    public void getProductsAjaxBySizeEnterId(HttpServletRequest request, HttpServletResponse resp) throws IOException {

        String proSize = request.getParameter("proSize");
        String enterId = request.getParameter("enterId");

        if (enterId == null || proSize == null) {
            return;
        }
        List<Product> products = productService.getProListByProSizeEnterId(proSize, NumberUtils.toLong(enterId));

        String json = JSON.toJSONString(products);

        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

    /**
     * @Title: validateProduct
     * @Description: 全量校验产品对象
     * @author: xuwanlin
     */
    private boolean validateProduct(ModelMap model, Product product) {
        if (product == null) {
            model.addAttribute("errorMsg", "无效的产品对象!");
            return false;
        }

        try {
            product.selfCheck();
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.toString());
            return false;
        }

        /**
         * 唯一性校验
         */
        if (!productService.checkUnique(product)) {
            model.addAttribute("errorMsg", "产品名称或产品编码或资费代码已存在!");
            return false;
        }

        return true;
    }

    /**
     * @Title:setDefaultValue
     * @Description: TODO
     * @author: xuwanlin
     */
    private void setDefaultValue(Product product) {
        if (product == null) {
            return;
        }
        product.setName(product.getName().replace(" ", ""));
        if (product.getProductSize() == null) {
            return;
        }

        product.setCreateTime(new Date());
        product.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
        product.setUpdateTime(new Date());

        return;
    }
    
    /**
     * @param model
     * @param enterpriseId
     */
    @RequestMapping(value = "/gd/qryProduct")
    public String qryProduct(ModelMap model, String entCode, Long entId, String entPrdCode) {

        EnterprisesExtInfo enterprisesExtInfo = enterprisesExtInfoService.get(entId);
        if (enterprisesExtInfo == null
                || !enterprisesExtInfo.getEcCode().equals(entCode)
                || !enterprisesExtInfo.getEcPrdCode().equals(entPrdCode)
                || !enterprisesService.isParentManage(entId, getCurrentUserManager().getId())) {
            model.addAttribute("errorMsg", "请求异常");
            return "error.ftl";
        }
        List<Product> products = productService.selectAllProductsByEnterId(entId);
        
        if (products == null) {
            model.addAttribute("errorMsg", "订购套餐为空");
            return "error.ftl";
        }

        model.addAttribute("entCode", entCode);
        model.addAttribute("entId", entId);
        model.addAttribute("entPrdCode", entPrdCode);
        return "product/gd_order_info.ftl";
    }
    /**
     * @param request
     * @param res
     */
    @RequestMapping("/gd/searchProduct")
    public void orderListInfo(HttpServletRequest request, HttpServletResponse res, Long entId) {
        List<Product> products = productService.selectProductsByEnterId(entId);
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        Long discount = enterprise.getDiscount();
        for(Product product:products) {
            product.setDiscount(discount.intValue());
        }
        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
        json.put("data", products);
        json.put("disCount", enterprise.getDiscount());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
