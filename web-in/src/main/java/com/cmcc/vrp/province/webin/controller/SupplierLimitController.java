package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.SupplierLimitStatus;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.Supplier;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierReqUsePerDay;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.SupplierReqUsePerDayService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.StringUtils;

/**
 * 供应商限额控制类
 * 
 * @author qinqinyan
 */
@Controller
@RequestMapping("/manage/supplierLimit")
public class SupplierLimitController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SupplierLimitController.class);

    @Autowired
    SupplierService supplierService;
    @Autowired
    SupplierProductService supplierProductService;
    @Autowired
    SupplierReqUsePerDayService supplierReqUsePerDayService;

    /**
     * @Title: index
     * @author qinqinyan
     */
    @RequestMapping("index")
    public String index(QueryObject queryObject, ModelMap modelMap) {
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        return "supplierLimit/index.ftl";
    }

    /**
     * @Title: search
     * @author qinqinyan
     */
    @RequestMapping("search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        setQueryParameter("supplierName", queryObject);

        int count = supplierService.queryPaginationSupplierCount(queryObject);
        List<Supplier> list = supplierService.queryPaginationSupplier(queryObject);

        // 查找是否有产品限额控制
        Map searchMap = new HashMap<String, String>();
        searchMap.put("status", "on");
        searchMap.put("limitMoneyFlag", SupplierLimitStatus.ON.getCode());
        for (Supplier supplier : list) {
            searchMap.put("supplierId", supplier.getId());

            List<SupplierProduct> records = supplierProductService.selectByMap(searchMap);
            if (records != null && records.size() > 0) {
                supplier.setProductLimitMoneyFlag(SupplierLimitStatus.ON.getCode());
            }
            searchMap.remove("supplierId");
            
            //获取已经使用的总金额
            SupplierReqUsePerDay supplierReqUsePerDay = supplierReqUsePerDayService.getTodayRecord(supplier.getId());
            if(supplierReqUsePerDay!=null){
                supplier.setNowUsedMoney(supplierReqUsePerDay.getUsedMoney());
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

    /**
     * 查看供应商限额详情
     * 
     * @param id
     *            供应商id
     * @author qinqinyan
     */
    @RequestMapping("showDetail")
    public String showDetail(ModelMap map, Long id) {
        if (id != null) {
            map.addAttribute("supplierId", id);
            return "supplierLimit/limitDetial.ftl";
        }
        map.addAttribute("errorMsg", "未查找到该供应商的限额信息");
        return "error.ftl";
    }

    /**
     * 获取供应商记录
     * 
     * @author qinqinyan
     */
    @RequestMapping("getSupplier")
    public void getSupplier(HttpServletResponse response, Long supplierId) {
        Supplier supplier = supplierService.get(supplierId);
        List<Supplier> suppliers = new ArrayList<Supplier>();
        suppliers.add(supplier);
        JSONObject json = new JSONObject();
        json.put("suppliers", suppliers);
        try {
            response.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取供应商产品限额记录
     * 
     * @author qinqinyan
     */
    @RequestMapping("getSupplierProducts")
    public void getPayRecords(HttpServletResponse response, Long supplierId) {
        Map map = new HashMap<String, String>();
        map.put("supplierId", supplierId);
        map.put("status", "on");
        List<SupplierProduct> supplierProducts = supplierProductService.selectByMap(map);

        JSONObject json = new JSONObject();
        json.put("data", supplierProducts);
        try {
            response.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置供应商全量限额
     * 
     * @author qinqinyan
     */
    @RequestMapping("saveSupplierLimitAjax")
    public void saveSupplierLimitAjax(HttpServletResponse response, HttpServletRequest request) throws IOException {
        Map returnMap = new HashMap<String, String>();
        Boolean result = false;
        String msg = "参数异常,保存失败";
        Administer administer = getCurrentUser();

        String supplierStr = request.getParameter("supplier");

        if (administer != null && !StringUtils.isEmpty(supplierStr)) {
            Supplier supplier = JSON.parseObject(supplierStr, Supplier.class);
            String resultMsg = validate(supplier);
            if ("success".equals(resultMsg)) {
                try {
                    if (supplierService.setSupplierLimit(supplier, administer.getId())) {
                        result = true;
                        msg = "保存成功";
                    } else {
                        msg = "保存失败";
                    }
                } catch (RuntimeException e) {
                    logger.info(e.getMessage());
                    msg = "保存失败";
                }
            }
        }
        returnMap.put("result", result ? "supplierLimit" : result.toString());
        returnMap.put("msg", msg);
        response.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }

    private String validate(Supplier supplier) {
        if (supplier.getLimitMoneyFlag() != null
                && supplier.getLimitMoney() != null) {
            return "success";
        }
        return "fail";
    }

    private String validate1(SupplierProduct supplierProduct) {
        if (supplierProduct.getLimitMoneyFlag() != null
                && supplierProduct.getLimitMoney() != null) {
            return "success";
        }
        return "fail";
    }

    /**
     * 设置供应商产品限额
     * 
     * @author qinqinyan
     */
    @RequestMapping("saveProductSupplierLimitAjax")
    public void saveProductSupplierLimitAjax(HttpServletResponse response, HttpServletRequest request)
            throws IOException {
        Map returnMap = new HashMap<String, String>();
        Boolean result = false;
        String msg = "参数异常,保存失败";
        Administer administer = getCurrentUser();

        String supplierProductStr = request.getParameter("supplierProduct");

        if (administer != null && !StringUtils.isEmpty(supplierProductStr)) {
            SupplierProduct supplierProduct = JSON.parseObject(supplierProductStr, SupplierProduct.class);
            String resultMsg = validate1(supplierProduct);
            if ("success".equals(resultMsg)) {
                try {
                    if (supplierProductService.setProductSupplierLimit(supplierProduct, administer.getId())) {
                        result = true;
                        msg = "保存成功";
                    } else {
                        msg = "保存失败";
                    }
                } catch (RuntimeException e) {
                    logger.info(e.getMessage());
                    msg = "保存失败";
                }
            }
        }
        returnMap.put("result", result ? "productLimit" : result.toString());
        returnMap.put("msg", msg);
        response.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }

}
