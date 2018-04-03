package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmcc.vrp.boss.chongqing.CQBossServiceImpl;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.dao.DiscountMapper;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Discount;
import com.cmcc.vrp.province.model.EntSyncList;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductAccount;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.EntSyncListService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductAccountService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;

/**
 * @author lgk8023
 *
 */
@Controller
@RequestMapping("/cq/product")
public class CqProductController extends BaseController{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(CqProductController.class);
    
    @Autowired
    ApplicationContext applicationContext;
	
    @Autowired
	EnterprisesService enterprisesService;
	
    @Autowired
	SupplierProductService supplierProductService;
	
    @Autowired 
	SupplierProductMapService supplierProductMapService;
	
    @Autowired
	SupplierProductAccountService supplierProductAccountService;
	
    @Autowired
	AccountService accountService;
	
    @Autowired
	ManagerService managerService;
	
    @Autowired
	ProductService productService;
    
    @Autowired
    EntSyncListService entSyncListService;
    
    @Autowired
    DiscountMapper discountMapper;
    
    
    /**
     * @param modelMap
     * @param queryObject
     * @return
     */
    @RequestMapping("index")
    public String showEnterpriseList(ModelMap modelMap, QueryObject queryObject) {
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
    	return "product/sync.ftl";
    }
    /**
     * 企业列表
     * 
     * @param model
     * @param res
     * @return
     */
    @RequestMapping("enterpriseList")
	public void enterpriseList(QueryObject queryObject, HttpServletResponse res) {
    	if (queryObject == null) {
    	    queryObject = new QueryObject();
    	}

    	String managerIdStr = getRequest().getParameter("managerId");
    	if (!StringUtils.isEmpty(managerIdStr)) {
    	    Long managerId = NumberUtils.toLong(managerIdStr);
    	    if (!isParentOf(managerId)) { //当前用户不是指定用户或不是它的父节点，没有权限查看相应的节点信息
    	    	res.setStatus(HttpStatus.SC_FORBIDDEN);
    	    	return;
    	    }
    	}

         /**
          * 查询参数: 企业名字、编码、效益
          */
    	setQueryParameter("name", queryObject);
    	setQueryParameter("code", queryObject);
    	setQueryParameter("deleteFlag", queryObject);

    	if (!StringUtils.isEmpty(getRequest().getParameter("startTime"))) {
    	    queryObject.getQueryCriterias().put("beginTime", getRequest().getParameter("startTime"));
    	    queryObject.getQueryCriterias().put("endTime", getRequest().getParameter("endTime"));
    	}

         /**
          * 当前登录用户的管理员层级
          */
    	Manager manager = getCurrentUserManager();
    	queryObject.getQueryCriterias().put("managerId", manager.getId());

         //页面查询参数中设定的管理员层级，如果设定了就会将上面的值覆盖
    	setQueryParameter("managerId", queryObject);

         // 数据库查找符合查询条件的个数
    	int enterpriseCount = enterprisesService.showForPageResultCount(queryObject);
    	List<Enterprise> enterpriseList = enterprisesService.showEnterprisesForPageResult(queryObject);

    	if (enterpriseList != null) {
    	    for (Enterprise enterprise : enterpriseList) {
    	    	Manager districtName = entManagerService.getManagerForEnter(enterprise.getId());
    	    	if (districtName != null) {
    	    	    String fullname = "";
    	    	    enterprise.setEnterpriseCity(managerService.getFullNameByCurrentManagerId(fullname,
                             districtName.getParentId()));
    	    	}
    	    	
    	    	//企业信息模糊化处理
    	    	enterprisesService.blurEnterpriseInfo(enterprise);
    	    
    	    }
    	}

    	com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
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
    
    
    /**
     * 获取企业的集团产品编码列表
     * 
     * @param modelMap
     * @param entId
     * @return
     */
    @RequestMapping(value = "/entProductCodeList")
	public String entProductCodeList(ModelMap modelMap, String entId) {
    	Enterprise enterprise = enterprisesService.selectById(NumberUtils.toLong(entId));
    	if(enterprise == null) {
    	    modelMap.addAttribute("errorMsg", "该企业不存在");
            return "error.ftl";
    	}
		
    	modelMap.addAttribute("enterprise", enterprise);
        return "product/code_list.ftl";
    }
       
    
    /**
     * @param request
     * @param res
     */
    @RequestMapping("codeList")
    public void codeList(HttpServletRequest request, HttpServletResponse res) {
    	Long enterId = NumberUtils.toLong(request.getParameter("entId"));
        
    	List<EntSyncList> entSyncLists = entSyncListService.getByEntId(enterId);
    	JSONObject json = new JSONObject();
        json.put("pageNum", 1);
        json.put("pageSize", 10);
        json.put("data", entSyncLists);
        json.put("total", 1)
        ;
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }	
    }
    
    /**
     * 同步成功详情
     * 
     * @param modelMap
     * @param request
     * @param queryObject
     * @return
     */
    @RequestMapping("syncDetails")
	public String syncDetails(ModelMap modelMap, HttpServletRequest request) {
    	String entProCode = request.getParameter("entProductCode");
    	Long entId = NumberUtils.toLong(request.getParameter("entId"));
    	modelMap.addAttribute("entProCode", entProCode);
    	modelMap.addAttribute("entId", entId);
    	return "product/product_details.ftl";
    }
    
	/**
	 * 余额更新
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
    @RequestMapping(value = "updateBalance1")
	public String updateBalance1(HttpServletRequest request, ModelMap modelMap) {
    	CQBossServiceImpl bossService = applicationContext.getBean("cqBossService", CQBossServiceImpl.class);
    	String entProCode = request.getParameter("entProCode");
    	Long entId = NumberUtils.toLong(request.getParameter("entId"));
    	Enterprise enterprise = enterprisesService.selectById(entId);
    	EntSyncList entSyncList = entSyncListService.getByEntIdAndEntProCode(entId, entProCode);
    	if (entSyncList.getStatus() == 1) {
    	    List<String> prdsList = bossService.getProductsOrderByEnterCode(entProCode);
		     	
    	    if (prdsList == null || prdsList.isEmpty()) {
    	        modelMap.addAttribute("errorMsg", "企业没有产品更新");
    	        return "error.ftl";
    	    }
    	    for (String prd:prdsList) {  
    	        //Double currentBossNumber = NumberUtils.toDouble("50");
    	    	Double currentBossNumber = NumberUtils.toDouble(bossService.getEnterPrdSolde(entProCode, prd));
    	        JSONObject jsonObject = new JSONObject();
    	        jsonObject.put("productCode", prd);
    	        jsonObject.put("enterProCode", entSyncList.getEntProductCode());
    	        jsonObject.put("enterCode", enterprise.getCode());
    	        SupplierProduct supplierProduct = supplierProductService.selectByFeature(jsonObject.toString());			
    	        if (supplierProduct == null) {
    	            productService.insertSupplierProduct(prd, currentBossNumber, enterprise, entSyncList.getEntProductCode());
    	            continue;
    	        } else if ((supplierProductAccountService.getInfoBySupplierProductId(supplierProduct.getId()).getCount() - currentBossNumber) == 0) {
    	            continue;
    	        }
    	        supplierProductAccountService.updateSupplierProductAccount(supplierProduct.getId(), currentBossNumber, entId);		        	
    	    }
    	}
    	modelMap.addAttribute("entProCode", entProCode);
    	modelMap.addAttribute("entId", entId);
    	return "product/product_details.ftl";
    }
    
    /**
     * 同步失败详情
     * 
     * @param modelMap
     * @param response
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("errorInfo")
    public void errorInfo(ModelMap modelMap,HttpServletResponse response, HttpServletRequest request) throws IOException {
    	String entProCode = request.getParameter("entProductCode");
    	Long entId = NumberUtils.toLong(request.getParameter("entId"));
    	EntSyncList entSyncList = entSyncListService.getByEntIdAndEntProCode(entId, entProCode);
    	response.setContentType("text/html; charset=UTF-8"); //转码
    	PrintWriter out = response.getWriter();
    	out.println(entSyncList.getSyncInfo());
    	out.close();
		
    }
    
    /**
     * 添加企业集团编码
     * 
     * @param modelMap
     * @param request
     * @return
     */
    @RequestMapping("productAdd")
    public String productAdd(ModelMap modelMap, HttpServletRequest request) {
    	Long entId = NumberUtils.toLong(request.getParameter("entId"));
    	Enterprise enterprise = enterprisesService.selectById(entId);
    	if(enterprise == null) {
    	    modelMap.addAttribute("errorMsg", "该企业不存在");
            return "error.ftl";
    	}
    	modelMap.addAttribute("enterprise", enterprise);
    	return "product/product_add.ftl";
    }
    
    /**
     * 获取集团编码详情
     * 
     * @param request
     * @param res
     */
    @RequestMapping("productAddDetails")
    public void productAddDetails(HttpServletRequest request, HttpServletResponse res) {
    	CQBossServiceImpl bossService = applicationContext.getBean("cqBossService", CQBossServiceImpl.class);
    	Long entId = NumberUtils.toLong(request.getParameter("entId"));
    	String entProCode = request.getParameter("entProCode");
    	Enterprise enterprise = null;
    	if (StringUtils.isEmpty(entProCode)
    			|| (enterprise = enterprisesService.selectById(entId)) == null) {
    	    LOGGER.error("无效的请求参数. entProCode = {}, enterprise = {}", entProCode, enterprise);
    	    return; 
    	}
    	
    	List<String> prdsList = bossService.getProductsOrderByEnterCode(entProCode);
		
        if (prdsList == null || prdsList.isEmpty()) {
            LOGGER.info("企业没有产品更新");
            return;
        }
        Long discount = enterprise.getDiscount();
        List<JSONObject> list = new ArrayList<JSONObject>();
    	for (String prd:prdsList) {  
    	    JSONObject jsonObject = new JSONObject();
    	    Double currentBossNumber = NumberUtils.toDouble(bossService.getEnterPrdSolde(entProCode, prd));
    	    jsonObject.put("proName", prd);
    	    jsonObject.put("discount", discount);
    	    jsonObject.put("amount", currentBossNumber);
    	    list.add(jsonObject);  	    	    
    	}
    	JSONObject json = new JSONObject();
    	json.put("data", list);
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 提交并做关联
     * 
     * @param modelMap
     * @param request
     * @return
     */
    @RequestMapping("submitProductAdd")
    public String submitProductAdd(ModelMap modelMap, HttpServletRequest request) {
    	Long entId = NumberUtils.toLong(request.getParameter("entId"));
    	String entProCode = request.getParameter("entProCode");
    	Enterprise enterprise = null;
    	if (StringUtils.isEmpty(entProCode)
    			|| (enterprise = enterprisesService.selectById(entId)) == null) {
    	    modelMap.addAttribute("errorMsg", "该企业不存在");
            return "error.ftl";
    	}
    	
    	if (entSyncListService.getByEntIdAndEntProCode(entId, entProCode) != null
    			&& entSyncListService.getByEntIdAndEntProCode(entId, entProCode).getStatus() == 1) {
    	    modelMap.addAttribute("errorMsg", entProCode + "已存在，不可重复提交");
            return "error.ftl";
    	}
    	
    	try {
    	    EntSyncList entSyncList = null;
    	    if ((entSyncList = entSyncListService.getByEntIdAndEntProCode(entId, entProCode)) == null) {
    	        entSyncList = new EntSyncList();
    	        entSyncList.setEntId(entId);
    	        entSyncList.setEntProductCode(entProCode);
    	        entSyncList.setStatus(1);
    	        entSyncList.setSyncInfo("同步成功");
    	        entSyncList.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
    	        entSyncListService.insert(entSyncList);
    	    } else {
    	    	Long endSyncLsitId = entSyncList.getId();
    	    	EntSyncList updateEntSyncList = new EntSyncList();
    	    	updateEntSyncList.setId(endSyncLsitId);
    	    	updateEntSyncList.setStatus(1);
    	    	updateEntSyncList.setSyncInfo("同步成功");
    	    	entSyncListService.updateSelective(updateEntSyncList);
    	    }   		
    	    productService.synPrdsWithSupplierPro(enterprise.getCode(), entProCode);
    	} catch (TransactionException e) {
    	    EntSyncList entSyncList = null;
    	    if ((entSyncList = entSyncListService.getByEntIdAndEntProCode(entId, entProCode)) != null) {
    	        Long endSyncLsitId = entSyncList.getId();
    	        EntSyncList updateEntSyncList = new EntSyncList();
    	        updateEntSyncList.setId(endSyncLsitId);
    	        updateEntSyncList.setStatus(0);
    	        updateEntSyncList.setSyncInfo(e.getMessage());
    	        entSyncListService.updateSelective(updateEntSyncList);
    	    } else {
    	    	entSyncList = new EntSyncList();
    	    	entSyncList.setEntId(entId);
    	    	entSyncList.setEntProductCode(entProCode);
    	    	entSyncList.setStatus(0);
    	    	entSyncList.setSyncInfo(e.getMessage());
    	    	entSyncList.setDeleteFlag(Constants.DELETE_FLAG.UNDELETED.getValue());
    	    	entSyncListService.insert(entSyncList);
    	    }
    	    modelMap.addAttribute("errorMsg", "同步失败");
            return "error.ftl";
    	}   	
    	modelMap.addAttribute("enterprise", enterprise);
    	return "product/code_list.ftl";
    }
	
	/**
	 * 查询平台产品余额
	 * 
	 * @param modelMap
	 * @param queryObject
	 * @return
	 */
    @RequestMapping(value = "productAccount")
	public String productAccount(ModelMap modelMap, QueryObject queryObject) {
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

                return "product/product_account.ftl";
            } else {
                modelMap.addAttribute("errorMsg", "企业管理员没有关联企业");
                return "error.ftl";
            }

        } else {
            modelMap.addAttribute("errorMsg", "该用户非企业管理员");
            return "error.ftl";
        }
    }
	
    /**
     * @param queryObject
     * @param res
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
	 * 平台产品余额明细（集团产品编码）
	 * 
	 * @param modelMap
	 * @param productCode
	 * @param entId
	 * @return
	 */
    @RequestMapping(value = "/productAccountDetail")
	public String productAccountDetail(ModelMap modelMap, String productCode, String entId) {
    	Enterprise enterprise = enterprisesService.selectById(NumberUtils.toLong(entId));
    	if(enterprise == null) {
    	    modelMap.addAttribute("errorMsg", "该企业不存在");
            return "error.ftl";
    	}
		
    	modelMap.addAttribute("enterprise", enterprise);
    	Product product = productService.selectByProductCode(productCode);
    	modelMap.addAttribute("product", product);
    	return "product/product_account_detail.ftl";
    }
    
	/**
	 * @param request
	 * @param res
	 */
    @RequestMapping(value = "entProCodeList")
	public void entProCodeList(HttpServletRequest request, HttpServletResponse res) {
    	String productCode = request.getParameter("productCode");
    	Long entId = NumberUtils.toLong(request.getParameter("entId"));		
    	List<EntSyncList> entSyncLists = entSyncListService.getByEntId(entId);
    	int total = 0;
    	List<JSONObject> list = new ArrayList<JSONObject>();
    	for (EntSyncList entSyncList:entSyncLists) {
    	    if (entSyncList.getStatus() == 1) {
    	        List<SupplierProductAccount> supplierProductAccounts = supplierProductAccountService.getInfoByEntSyncListId(entSyncList.getId());
    	        for (SupplierProductAccount supplierProductAccount:supplierProductAccounts) {
    	            Double count = supplierProductAccount.getCount();
    	            List<Product> products = supplierProductMapService.getBySplPid(supplierProductAccount.getSupplierProductId());
    	            for (Product product:products) {
    	                if (product.getProductCode().equals(productCode)) {
    	                    total++;
    	                    JSONObject jsonObject = new JSONObject();
    	                    jsonObject.put("entProCode", entSyncList.getEntProductCode());
    	                    jsonObject.put("count", count);
    	                    list.add(jsonObject);
    	                }
    	            }
    	        }
    	    }			
    	}
		
        JSONObject json = new JSONObject();
        json.put("pageNum", (total/10) + 1);
        json.put("pageSize", 10);
        json.put("data", list);
        json.put("total", total);
        
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * 订单余额查询（集团产品编码）
	 * 
	 * @param modelMap
	 * @param queryObject
	 * @return
	 */
    @RequestMapping(value = "orderSearch")
	public String orderSearch(ModelMap modelMap, QueryObject queryObject) {
        String enterpriseContractor = getENTERPRISE_CONTACTOR();
        Administer admin = getCurrentUser();
        if (admin.getRoleId().toString().equals(enterpriseContractor)) {
            List<Enterprise> enters = enterprisesService.getEnterpriseListByAdminId(admin);
            Enterprise e = null;
            if (enters != null
                && enters.size() > 0
                && (e = enters.get(0)) != null) {
                modelMap.addAttribute("enterprise", e);
                return "product/order_search.ftl";
            } else {
                modelMap.addAttribute("errorMsg", "企业管理员没有关联企业");
                return "error.ftl";
            }

        } else {
            modelMap.addAttribute("errorMsg", "该用户非企业管理员");
            return "error.ftl";
        }
    }
	
	/**
	 * @param request
	 * @param res
	 */
    @RequestMapping(value = "orderSearchList")
	public void orderSearchList(HttpServletRequest request, HttpServletResponse res) {
    	Long entId = NumberUtils.toLong(request.getParameter("entId"));
		
    	List<EntSyncList> entSyncLists = entSyncListService.getByEntId(entId);
    	List<JSONObject> list = new ArrayList<JSONObject>();
    	for (EntSyncList entSyncList:entSyncLists) {
    	    if (entSyncList.getStatus() ==1) {
    	        JSONObject jsonObject = new JSONObject();
    	        jsonObject.put("entProCode", entSyncList.getEntProductCode());
    	        list.add(jsonObject);
    	    }
    	}
		
        JSONObject json = new JSONObject();
        json.put("pageNum", (entSyncLists.size()/10) + 1);
        json.put("pageSize", 10);
        json.put("data", list);
        json.put("total", entSyncLists.size());
        
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }	
    }
	/**
	 * 订单余额明细
	 * 
	 * @param modelMap
	 * @param request
	 * @param queryObject
	 * @return
	 */
    @RequestMapping(value = "orderDetails")
	public String orderDetails(ModelMap modelMap, HttpServletRequest request) {
        String entProCode = request.getParameter("entProCode");
        Long entId = NumberUtils.toLong(request.getParameter("entId"));
        modelMap.addAttribute("entProCode", entProCode);
        modelMap.addAttribute("entId", entId);
        return "product/order_details.ftl";
    }
	
	/**
	 * @param request
	 * @param res
	 */
    @RequestMapping(value = "orderDetailsList")
	public void orderDetailsList(HttpServletRequest request, HttpServletResponse res) {
        String entProCode = request.getParameter("entProCode");
        Long entId = NumberUtils.toLong(request.getParameter("entId"));
		
        Long discount = enterprisesService.selectById(entId).getDiscount();
        EntSyncList entSyncList = entSyncListService.getByEntIdAndEntProCode(entId, entProCode);
        List<JSONObject> list = new ArrayList<JSONObject>();
        int total = 0;
        List<SupplierProductAccount> supplierProductAccounts = supplierProductAccountService.getInfoByEntSyncListId(entSyncList.getId());			
        for (SupplierProductAccount supplierProductAccount:supplierProductAccounts) {
            total++;
            JSONObject jsonObject = new JSONObject();
            Double count = supplierProductAccount.getCount();
            String proName = supplierProductService.selectById(supplierProductAccount.getSupplierProductId()).getName();
            jsonObject.put("proName", proName);
            jsonObject.put("discount", discount);
            jsonObject.put("count", count);
            list.add(jsonObject);
        }
		
        JSONObject json = new JSONObject();
        json.put("pageNum", (Integer)(total/10) + 1);
        json.put("pageSize", 10);
        json.put("data", list);
        json.put("total", total);
        
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }	
		
    }
	/**
	 * 余额更新
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
    @RequestMapping(value = "updateBalance")
	public String updateBalance(HttpServletRequest request, ModelMap modelMap) {
    	CQBossServiceImpl bossService = applicationContext.getBean("cqBossService", CQBossServiceImpl.class);
    	String entProCode = request.getParameter("entProCode");
    	Long entId = NumberUtils.toLong(request.getParameter("entId"));
    	Enterprise enterprise = enterprisesService.selectById(entId);
    	EntSyncList entSyncList = entSyncListService.getByEntIdAndEntProCode(entId, entProCode);
    	if (entSyncList.getStatus() == 1) {
    	    List<String> prdsList = bossService.getProductsOrderByEnterCode(entProCode);
		     	
    	    if (prdsList == null || prdsList.isEmpty()) {
    	        modelMap.addAttribute("errorMsg", "企业没有产品更新");
    	        return "error.ftl";
    	    }
    	    for (String prd:prdsList) {  
    	    	Double currentBossNumber = NumberUtils.toDouble(bossService.getEnterPrdSolde(entProCode, prd));
    	        JSONObject jsonObject = new JSONObject();
    	        jsonObject.put("productCode", prd);
    	        jsonObject.put("enterProCode", entSyncList.getEntProductCode());
    	        jsonObject.put("enterCode", enterprise.getCode());
    	        SupplierProduct supplierProduct = supplierProductService.selectByFeature(jsonObject.toString());			
    	        if (supplierProduct == null) {
    	            productService.insertSupplierProduct(prd, currentBossNumber, enterprise, entSyncList.getEntProductCode());
    	            continue;
    	        } else if ((supplierProductAccountService.getInfoBySupplierProductId(supplierProduct.getId()).getCount() - currentBossNumber) == 0) {
    	            continue;
    	        }
    	        supplierProductAccountService.updateSupplierProductAccount(supplierProduct.getId(), currentBossNumber, entId);		        	
    	    }
    	}
    	modelMap.addAttribute("entProCode", entProCode);
    	modelMap.addAttribute("entId", entId);
    	return "product/order_details.ftl";
    }
}
