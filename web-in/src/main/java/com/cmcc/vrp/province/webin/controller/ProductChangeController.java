package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.ApprovalType;
import com.cmcc.vrp.enums.EnterpriseStatus;
import com.cmcc.vrp.enums.ProductChangeOperation;
import com.cmcc.vrp.enums.ProductChangeRequestStatus;
import com.cmcc.vrp.province.dao.AdminDistrictMapper;
import com.cmcc.vrp.province.model.AdminDistrict;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ApprovalProcessDefinition;
import com.cmcc.vrp.province.model.ApprovalRequest;
import com.cmcc.vrp.province.model.Discount;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.ProductChangeOperator;
import com.cmcc.vrp.province.model.ProductTemplate;
import com.cmcc.vrp.province.model.ProductTemplateEnterpriseMap;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.ApprovalProcessDefinitionService;
import com.cmcc.vrp.province.service.ApprovalRequestService;
import com.cmcc.vrp.province.service.DiscountService;
import com.cmcc.vrp.province.service.EntManagerService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.ProductChangeOperatorService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.ProductTemplateEnterpriseMapService;
import com.cmcc.vrp.province.service.ProductTemplateService;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;

/**
 * ProductChangeController.java
 */
@Controller
@RequestMapping("/manage/enterProductChange")
public class ProductChangeController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ProductChangeController.class);

    @Autowired
    AdminDistrictMapper adminDistrictMapper;

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    EntProductService entProductService;

    @Autowired
    ProductService productService;

    @Autowired
    AdministerService administerService;

    @Autowired
    ProductChangeOperatorService productChangeOperatorService;
    @Autowired
    ApprovalProcessDefinitionService approvalProcessDefinitionService;

    @Autowired
    DiscountService discountService;

    @Autowired
    EntManagerService entManagerService;

    @Autowired
    ManagerService managerService;
    @Autowired
    ApprovalRequestService approvalRequestService;

    @Autowired
    ProductTemplateEnterpriseMapService productTemplateEnterpriseMapService;
    @Autowired
    ProductTemplateService productTemplateService;

    /**
     * @Title: productChangeListIndex
     */
    @RequestMapping("/index")
    public String productChangeListIndex(ModelMap model, QueryObject queryObject, HttpServletRequest request) {
        if(queryObject != null){
            model.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        Administer admin = getCurrentUser();
        if (admin == null) {
            return getLoginAddress();
        }

        // 管理员层级筛选
        // 根节点为当前用户的管理员层级
        Manager manager = getCurrentUserManager();
        if (manager == null) {
            model.put("errorMsg", "当前用户无管理员身份");
            return "error.ftl";
        }

        // 判断是否有提交审核的权限
        if (manager.getRoleId() != null) {
            Boolean authFlag = approvalProcessDefinitionService.hasAuthToSubmitApproval(manager.getRoleId(),
                    ApprovalType.Product_Change_Approval.getCode());
            model.addAttribute("authFlag", authFlag.toString());
            model.addAttribute("managerId", manager.getId());
        } else {
            model.addAttribute("authFlag", "false");
        }

        model.addAttribute("roleId", manager.getRoleId());
        model.addAttribute("currUserId", admin.getId());

        // 是否使用产品模板
        if (getUseProductTemplate()) {
            return "productChange/templateIndex.ftl";
        }
        return "productChange/index.ftl";
    }

    /**
     * 企业列表查找
     */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 查询参数: 企业名字、编码、效益
         */
        setQueryParameter("name", queryObject);
        setQueryParameter("code", queryObject);

        setQueryParameter("productTemplateName", queryObject);

        if (!StringUtils.isEmpty(getRequest().getParameter("startTime"))) {
            queryObject.getQueryCriterias().put("beginTime", getRequest().getParameter("startTime"));
            queryObject.getQueryCriterias().put("endTime", getRequest().getParameter("endTime"));
        }

        /**
         * 当前登录用户的管理员身份
         */
        Manager manager = getCurrentUserManager();
        queryObject.getQueryCriterias().put("managerId", manager.getId());

        // 页面查询参数中设定的管理员层级，如果设定了就会将上面的值覆盖
        setQueryParameter("managerId", queryObject);

        List<Integer> deleteFlags = new ArrayList<Integer>();
        deleteFlags.add(EnterpriseStatus.NORMAL.getCode());
        String deleteFlag = org.apache.commons.lang.StringUtils.join(deleteFlags, ",");
        queryObject.getQueryCriterias().put("deleteFlag", deleteFlag);

        // 数据库查找符合查询条件的个数
        int enterpriseCount = enterprisesService.showForPageResultCount(queryObject);
        List<Enterprise> enterpriseList = enterprisesService.showEnterprisesForPageResult(queryObject);

        if (enterpriseList != null && enterpriseList.size() > 0) {

            for (Enterprise e : enterpriseList) {
                // 判断是否存在保存更改记录
                List<ProductChangeOperator> productChangeOperators = productChangeOperatorService
                        .getProductChangeRecordByEntId(e.getId());
                if (productChangeOperators != null && productChangeOperators.size() > 0) {
                    e.setProductChangeStatus(ProductChangeRequestStatus.SAVED.getValue());
                    e.setDescription("已保存变更记录");
                    continue;
                }

                // 是否正在审核的记录
                ApprovalProcessDefinition approvalProcessDefinition = approvalProcessDefinitionService
                        .selectByType(ApprovalType.Product_Change_Approval.getCode());
                List<ApprovalRequest> approvalRequests = approvalRequestService.selectByEntIdAndProcessId(e.getId(),
                        approvalProcessDefinition.getId());
                if (approvalRequests != null && approvalRequests.size() > 0) {
                    if (approvalRequests.get(approvalRequests.size() - 1).getDeleteFlag().toString().equals("1")) {
                        // 最近的一条记录已经被驳回，即不存在审核中记录
                        e.setProductChangeStatus(ProductChangeRequestStatus.NOCHANGE.getValue());
                        e.setDescription("已驳回");
                        continue;
                    } else if (approvalRequests.get(approvalRequests.size() - 1).getStatus().toString()
                            .equals(approvalProcessDefinition.getTargetStatus().toString())) {
                        // 最近的一条记录审批完成，即不存在审批记录
                        e.setProductChangeStatus(ProductChangeRequestStatus.REJECT.getValue());
                        e.setDescription("");
                        continue;
                    } else {
                        // 存在审批中的记录
                        e.setProductChangeStatus(ProductChangeRequestStatus.APPROVING.getValue());
                        String statusDes = approvalRequestService
                                .getCurrentStatus(approvalRequests.get(approvalRequests.size() - 1));
                        e.setDescription(statusDes);
                        continue;
                    }
                } else {
                    e.setProductChangeStatus(ProductChangeRequestStatus.NOCHANGE.getValue());
                    e.setDescription("");
                }
            }

            for (Enterprise e : enterpriseList) {
                //// 设置地域全称 地区全称
                Manager entManager = entManagerService.getManagerForEnter(e.getId());
                if (entManager != null) {
                    String fullname = "";
                    e.setCmManagerName(
                            managerService.getFullNameByCurrentManagerId(fullname, entManager.getParentId()));
                }

                // 是否使用产品模板
                if (getUseProductTemplate()) {

                    ProductTemplateEnterpriseMap productTemplateEnterpriseMap = productTemplateEnterpriseMapService
                            .selectByEntId(e.getId());
                    if (productTemplateEnterpriseMap != null) {
                        e.setProductTemplateId(productTemplateEnterpriseMap.getProductTemplateId());
                        e.setProductTemplateName(productTemplateEnterpriseMap.getProductTemplateName());
                    } else {
                        e.setProductTemplateName("无产品模版");
                    }
                }
            }

        }

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", enterpriseList);
        json.put("total", enterpriseCount);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 查找折扣变更记录
    private List<ProductChangeOperator> searchDiscountChangeRecords(Long enterpriseId, List<Product> products,
            List<Product> originProducts, Long oldProductTemplateId, Long newProductTemplateId) {
        List<ProductChangeOperator> list = new ArrayList<ProductChangeOperator>();
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            int j = 0;
            for (; j < originProducts.size(); j++) {
                if (product.getId().toString().equals(originProducts.get(j).getId().toString())
                        && !product.getDiscount().toString().equals(originProducts.get(j).getDiscount().toString())) {
                    ProductChangeOperator record = createProductChangeOperator(enterpriseId, product,
                            oldProductTemplateId, newProductTemplateId,
                            ProductChangeOperation.CHANGE_DISCOUNT.getValue());
                    list.add(record);
                    break;
                }
            }
        }
        return list;
    }

    // 构造对象
    private ProductChangeOperator createProductChangeOperator(Long enterpriseId, Product product,
            Long oldProductTemplateId, Long newProductTemplateId, int operate) {
        ProductChangeOperator record = new ProductChangeOperator();
        record.setEnterId(enterpriseId);
        record.setPrdId(product.getId());
        record.setOperator(operate);
        if (product.getDiscount() != null) {
            record.setDiscount(product.getDiscount());
        } else {
            record.setDiscount(100);
            //假设企业有折扣
            Enterprise enter = enterprisesService.selectById(enterpriseId);
            if(enter!=null && enter.getDiscount()!=null){
                Discount discount = discountService.selectByPrimaryKey(enter.getDiscount());
                if(discount!=null){                    
                    record.setDiscount(new BigDecimal(String.valueOf(discount.getDiscount())).multiply(new BigDecimal(100)).intValue());
                }                
            }            
        }
        record.setDeleteFlag(0);
        record.setOldProductTemplateId(oldProductTemplateId);
        record.setNewProductTemplateId(newProductTemplateId);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        return record;
    }

    // 查找增加产品记录
    private List<ProductChangeOperator> searchAddRecords(Long enterpriseId, List<Product> products,
            List<Product> originProducts, Long oldProductTemplateId, Long newProductTemplateId) {
        List<ProductChangeOperator> list = new ArrayList<ProductChangeOperator>();
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            int j = 0;
            for (; j < originProducts.size(); j++) {
                if (product.getId().toString().equals(originProducts.get(j).getId().toString())) {
                    break;
                }
            }
            if (j == originProducts.size()) {
                ProductChangeOperator record = createProductChangeOperator(enterpriseId, product, oldProductTemplateId,
                        newProductTemplateId, ProductChangeOperation.ADD.getValue());
                list.add(record);
            }
        }
        return list;
    }

    // 查找要删除的产品
    private List<ProductChangeOperator> searchDeleteRecords(Long enterpriseId, List<Product> products,
            List<Product> originProducts, Long oldProductTemplateId, Long newProductTemplateId) {
        List<ProductChangeOperator> list = new ArrayList<ProductChangeOperator>();
        for (int i = 0; i < originProducts.size(); i++) {
            Product product = originProducts.get(i);
            int j = 0;
            for (; j < products.size(); j++) {
                if (product.getId().toString().equals(products.get(j).getId().toString())) {
                    break;
                }
            }
            if (j == products.size()) {
                ProductChangeOperator record = createProductChangeOperator(enterpriseId, product, oldProductTemplateId,
                        newProductTemplateId, ProductChangeOperation.DELETE.getValue());
                list.add(record);
            }
        }
        return list;
    }

    /**
     * 保存企业产品变更模板
     */
    @RequestMapping(value = "/saveProductChange")
    public void saveProductChange(Long enterpriseId, String products, Long productTemplateId, HttpServletResponse res) {

        Map returnMap = new HashMap<String, String>();
        String msg = "参数缺失，保存失败";
        Boolean result = false;

        List<ProductChangeOperator> changeRecords = new ArrayList<ProductChangeOperator>();
        List<ProductChangeOperator> addRecords = null;
        List<ProductChangeOperator> deleteRecords = null;
        List<ProductChangeOperator> discountChangeRecords = null;
        Long oldProductTemplateId = null; // 企业原产品模板

        if (enterpriseId != null) {
            
            //将前端discount转换为preDiscount
            products = products.replace("discount", "preDiscount");
            
            // 获取已有的企业产品列表
            List<Product> originProductList = entProductService.selectAllProductByEnterId(enterpriseId);
            List<Product> productsList = JSON.parseArray(products, Product.class);           
            
            if ((originProductList == null || originProductList.size() == 0)
                    && (productsList == null || productsList.size() == 0)) {
                msg = "产品无变更";
            } else {
                //将前端preDiscount字段值赋值给discount
                for(Product product : productsList){
                    if (product.getPreDiscount() != null) {
                        product.setDiscount((int)(product.getPreDiscount() * 10));
                    }                   
                }
                
                // 使用产品模板
                if (getUseProductTemplate()) {
                    ProductTemplateEnterpriseMap originMap = productTemplateEnterpriseMapService
                            .selectByEntId(enterpriseId);
                    if (originMap != null) {
                        oldProductTemplateId = originMap.getProductTemplateId();
                    }
                }

                addRecords = searchAddRecords(enterpriseId, productsList, originProductList, oldProductTemplateId,
                        productTemplateId);
                deleteRecords = searchDeleteRecords(enterpriseId, productsList, originProductList, oldProductTemplateId,
                        productTemplateId);
                if (isZiying()) {
                    // 自营平台要判断折扣
                    discountChangeRecords = searchDiscountChangeRecords(enterpriseId, productsList, originProductList,
                            oldProductTemplateId, productTemplateId);
                    if ((addRecords == null || addRecords.size() == 0)
                            && (deleteRecords == null || deleteRecords.size() == 0)
                            && (discountChangeRecords == null || discountChangeRecords.size() == 0)) {
                        msg = "产品无变更";
                    }
                } else {
                    if ((addRecords == null || addRecords.size() == 0)
                            && (deleteRecords == null || deleteRecords.size() == 0)) {
                        msg = "产品无变更";
                    }
                }
                if(!"产品无变更".equals(msg)){
                    if(addRecords.size()>0){
                        changeRecords.addAll(addRecords);
                    }
                    if(deleteRecords!=null && deleteRecords.size()>0){
                        changeRecords.addAll(deleteRecords);
                    }
                    if(discountChangeRecords!=null && discountChangeRecords.size()>0){
                        changeRecords.addAll(discountChangeRecords);
                    }

                    try {
                        if (productChangeOperatorService.batchInsert(changeRecords)) {
                            result = true;
                            msg = "保存成功";
                        } else {
                            msg = "保存失败";
                        }
                    } catch (RuntimeException e) {
                        logger.error("保存失败，失败原因："+e.getMessage());
                        msg = "保存失败";
                    }
                }
            }
        }
        returnMap.put("result", result.toString());
        returnMap.put("msg", msg);

        try {
            res.getWriter().write(JSON.toJSONString(returnMap));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 企业产品变更申请页面显示
     */
    @RequestMapping("productChangeIndex")
    public String productChangeIndex(ModelMap model, Long id) {
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(id);
        if (enterprise == null) {
            model.addAttribute("errorMsg", "没有选定企业");
            return "error.ftl";
        }

        /*
         * List<ProductChangeOperator> productChangeOperators =
         * productChangeOperatorService .getProductChangeRecordByEntId(id);
         */

        /*
         * if (productChangeOperators != null && productChangeOperators.size() >
         * 0) { model.addAttribute("errorMsg", "已提交产品变更！"); return "error.ftl";
         * }
         */
        model.put("enterprise", enterprise);

        List<Discount> discounts = discountService.selectAllDiscount();
        model.put("discounts", discounts);
        // List<Product> products =
        // entProductService.selectProductByEnterId(id);
        // model.put("nowProductList", products);
        //
        // List<Product> allProducts =
        // entProductService.productAvailableByEnterId(id);
        // model.put("allProducts", allProducts);

        // 区别自营还是省公司
        model.addAttribute("provinceFlag", getProvinceFlag());

        // 是否使用产品模板

        List<Product> products = productService.selectProductsByEnterId(id);
        if (getUseProductTemplate()) {
            ProductTemplateEnterpriseMap productTemplateEnterpriseMap = productTemplateEnterpriseMapService
                    .selectByEntId(id);
            model.addAttribute("productTemplateEnterpriseMap", productTemplateEnterpriseMap);

            // 该企业绑定产品模板
            /*
             * if(productTemplateEnterpriseMap!=null){ products =
             * productService.selectByProductTemplateId(
             * productTemplateEnterpriseMap.getProductTemplateId()); }else{
             * products = productService.selectProductsByEnterId(id); }
             */

            List<ProductTemplate> productTemplates = productTemplateService.selectAllProductTemplates();
            model.addAttribute("productTemplates", productTemplates);

            model.addAttribute("products", JSON.toJSONString(products));
            return "productChange/templateChange.ftl";
        } else {
            // 该企业未绑定产品模板
            model.addAttribute("productTemplateId", "-1");
            model.addAttribute("products", JSON.toJSONString(products));
            return "productChange/change.ftl";
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
        setQueryParameter("productName", queryObject);
        setQueryParameter("productCode", queryObject);
        setQueryParameter("size", queryObject);
        setQueryParameter("unit", queryObject);
        setQueryParameter("isp", queryObject);
        setQueryParameter("status", queryObject);
        setQueryParameter("roamingRegion", queryObject);
        setQueryParameter("ownershipRegion", queryObject);
        setQueryParameter("productIds", queryObject); // 已经选择的产品id

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
     * 查询企业产品
     */
    @RequestMapping("getEntProduct")
    public void getEntProduct(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        setQueryParameter("enterId", queryObject);

        String enterId = (String) queryObject.getQueryCriterias().get("enterId");

        List<Product> products = entProductService.selectProductByEnterId(Long.parseLong(enterId));

        JSONObject json = new JSONObject();
        json.put("data", products);

        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 区别自营还是省公司
        // model.addAttribute("provinceFlag", provinceFlag);
        // model.addAttribute("defaultDiscount",
        // (products!=null&&products.size()>0)?products.get(0).getDiscount():"");

    }

    /**
     * 根据模板id获取产品模板关联的产品
     */
    @RequestMapping("getTemplateProductsAjax")
    public void getTemplateProductsAjax(HttpServletResponse response, Long productTemplateId) {
        List<Product> products = productService.selectByProductTemplateId(productTemplateId);
        JSONObject json = new JSONObject();
        json.put("products", products);
        try {
            response.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询企业可选产品
     */
    @RequestMapping("getAvailableProduct")
    public void getAvailableProduct(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        setQueryParameter("enterId", queryObject);
        String enterId = (String) queryObject.getQueryCriterias().get("enterId");
        List<Product> allProducts = entProductService.productAvailableByEnterId(Long.parseLong(enterId));

        // 将现金产品去除
        Iterator<Product> it = allProducts.iterator();
        while (it.hasNext()) {
            Product prd = it.next();
            if (prd.getType().toString().equals("0")) {
                it.remove();
            }
        }

        JSONObject json = new JSONObject();
        json.put("data", allProducts);

        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 区别自营还是省公司
        // model.addAttribute("provinceFlag", provinceFlag);
        // model.addAttribute("defaultDiscount",
        // (products!=null&&products.size()>0)?products.get(0).getDiscount():"");

    }

    /**
     * 企业产品变更审核列表
     */

    @RequestMapping("/approvalList")
    public String approvalList(ModelMap model, QueryObject queryObject, HttpServletRequest request) {

        Administer admin = getCurrentUser();
        if (admin == null) {
            return getLoginAddress();
        }
        // 有地区关联的管理员只能获取其同级及下级的地区
        List<AdminDistrict> ad1 = adminDistrictMapper.selectAdminDistrictByAdminId(getCurrentUser().getId());
        if (ad1 == null || ad1.size() != 1) {
            model.addAttribute("districtId", "1");
        } else {
            model.addAttribute("districtId", ad1.get(0).getDistrictId().toString());
        }

        return "productChange/product_change_subject_list.ftl";
    }

    /**
     * 查看产品变更详情
     *
     * @param id
     *            企业id
     * @qinqinyan
     */
    @RequestMapping("productChangeDetail")
    public String productChangeDetail(ModelMap map, HttpServletRequest request, HttpServletResponse response, Long id) {
        if (id != null) {
            List<ProductChangeOperator> productChangeOperators = productChangeOperatorService
                    .getProductChangeRecordByEntId(id);

            Boolean productTemplateFlag = getUseProductTemplate();
            if (productTemplateFlag) {
                // 采用模板
                ProductTemplateEnterpriseMap nowMap = productTemplateEnterpriseMapService.selectByEntId(id);
                map.addAttribute("nowProductTemplateName",
                        nowMap != null ? nowMap.getProductTemplateName() : "未采用产品模板");

                Long productTemplateId = productChangeOperators.get(0).getNewProductTemplateId();
                if (productTemplateId != null) {
                    ProductTemplate productTemplate = productTemplateService.selectByPrimaryKey(productTemplateId);
                    map.addAttribute("newProductTemplateName", productTemplate.getName());
                } else {
                    map.addAttribute("newProductTemplateName", "未采用产品模板");
                }
            }

            map.addAttribute("opinions", productChangeOperators);
            map.addAttribute("enterId", id);
            map.addAttribute("productTemplateFlag", productTemplateFlag.toString());

            Enterprise enterprise = enterprisesService.selectByPrimaryKey(id);

            String provinceFlag = getProvinceFlag();
            if (provinceFlag.equals(zyProvinceFlagValue)) {
                map.addAttribute("flag", 1);
            } else {
                map.addAttribute("flag", 0);
                map.addAttribute("discount", enterprise.getDiscount());
            }

            return "productChange/productChangeDetail.ftl";
        }
        return "redirect:index.html";
    }

    /**
     * 撤销产品变更请求
     *
     * @qinqinyan
     */
    @RequestMapping("cancelProductChange")
    public String cancelProductChange(ModelMap map, HttpServletRequest request, HttpServletResponse response,
            Long enterId) {
        if (enterId != null) {
            if (!productChangeOperatorService.deleteProductChangeRecordByEntId(enterId)) {
                return "redirect:productChangeDetail.html";
            }
        }
        return "redirect:index.html";
    }
}
