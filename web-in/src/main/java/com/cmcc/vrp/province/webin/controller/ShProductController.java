package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.boss.shanghai.model.querycount.InfoList;
import com.cmcc.vrp.boss.shanghai.model.querycount.QueryUsableBalanceOfFlow;
import com.cmcc.vrp.boss.shanghai.service.ShBossService;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Manager;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.ShOrderList;
import com.cmcc.vrp.province.model.ShOrderProductMap;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.ShOrderListService;
import com.cmcc.vrp.province.service.ShOrderProductMapService;
import com.cmcc.vrp.util.CSVUtil;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.SizeUnits;
import com.cmcc.vrp.util.StringUtils;

/**
 * @author lgk8023
 *
 */
@Controller
@RequestMapping("/sh/product")
public class ShProductController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShProductController.class);
    @Autowired
    private EnterprisesService enterprisesService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private ShOrderListService shOrderListService;

    @Autowired
    private ShOrderProductMapService shOrderProductMapService;

    @Autowired
    private ProductService productService;

    @Autowired
    @Qualifier("QueryUsableBalanceOfFlowServiceImpl")
    private ShBossService queryCountService;

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
        Manager manager = getCurrentUserManager();
        modelMap.addAttribute("managerId", manager.getId());
        return "shProduct/index.ftl";
    }

    /**
     * @param modelMap
     * @param queryObject
     * @return
     */
    @RequestMapping("entIndex")
    public String entIndex(ModelMap modelMap, QueryObject queryObject) {
        Manager manager = getCurrentUserManager();
        List<Enterprise> enterprises = enterprisesService.getAllEnterByManagerId(manager.getId());
        if (enterprises.isEmpty()) {
            modelMap.put("errorMsg", "根据当前用户未找到企业");
            return "error.ftl";
        }
        Enterprise enterprise = enterprises.get(0);
        modelMap.addAttribute("enterprise", enterprise);
        if (isEnterpriseContactor()) {
            modelMap.addAttribute("entManageFlag", 1);
        } else {
            modelMap.addAttribute("entManageFlag", 0);
        }
        return "shProduct/order_list.ftl";
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
            if (!isParentOf(managerId)) { // 当前用户不是指定用户或不是它的父节点，没有权限查看相应的节点信息
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

        // 页面查询参数中设定的管理员层级，如果设定了就会将上面的值覆盖
        setQueryParameter("managerId", queryObject);

        // 数据库查找符合查询条件的个数
        int enterpriseCount = enterprisesService.showForPageResultCount(queryObject);
        List<Enterprise> enterpriseList = enterprisesService.showEnterprisesForPageResult(queryObject);

        if (enterpriseList != null) {
            for (Enterprise enterprise : enterpriseList) {
                Manager districtName = entManagerService.getManagerForEnter(enterprise.getId());
                if (districtName != null) {
                    String fullname = "";
                    enterprise.setEnterpriseCity(managerService.getFullNameByCurrentManagerId(fullname, districtName.getParentId()));
                }

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
     * @param modelMap
     * @param entId
     * @return
     */
    @RequestMapping(value = "/entOrderList")
    public String entProductCodeList(ModelMap modelMap, String entId) {
        Enterprise enterprise = enterprisesService.selectById(NumberUtils.toLong(entId));
        if (enterprise == null) {
            modelMap.addAttribute("errorMsg", "该企业不存在");
            return "error.ftl";
        }

        modelMap.addAttribute("enterprise", enterprise);
        if (isEnterpriseContactor()) {
            modelMap.addAttribute("entManageFlag", 1);
        } else {
            modelMap.addAttribute("entManageFlag", 0);
        }
        return "shProduct/order_list.ftl";
    }

    /**
     * @param request
     * @param res
     */
    @RequestMapping("orderList")
    public void codeList(QueryObject queryObject, HttpServletRequest request, HttpServletResponse res) {

        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        setQueryParameter("orderName", queryObject);

        if (!StringUtils.isEmpty(getRequest().getParameter("entId"))) {
            queryObject.getQueryCriterias().put("entId", getRequest().getParameter("entId"));

        }
        if (!StringUtils.isEmpty(getRequest().getParameter("startTime"))) {
            queryObject.getQueryCriterias().put("beginTime", getRequest().getParameter("startTime") + " 00:00:00");
            queryObject.getQueryCriterias().put("endTime", getRequest().getParameter("endTime") + " 23:59:59");
        }
        int count = shOrderListService.showForPageResultCount(queryObject);
        List<ShOrderList> shOrderLists = shOrderListService.showForPageResultList(queryObject);
        for (ShOrderList shOrderList : shOrderLists) {
            if ("02".equals(shOrderList.getOrderType())) {
                Double totalFee = 0.0;
                QueryUsableBalanceOfFlow queryUsableBalanceOfFlow = new QueryUsableBalanceOfFlow();
                queryUsableBalanceOfFlow.setAccId(shOrderList.getAccId());
                queryUsableBalanceOfFlow.setMainBillId(shOrderList.getMainBillId());
                queryUsableBalanceOfFlow.setOfferId(shOrderList.getOfferId());
                List<InfoList> infoLists = queryCountService.queryUsableBalanceOfFlow(queryUsableBalanceOfFlow);
                if (infoLists != null) {
                    for (InfoList infoList : infoLists) {
                        totalFee = totalFee + NumberUtils.toDouble(infoList.getTotalFee()) * 100;
                    }
                    shOrderList.setCount(totalFee);
                    shOrderListService.updateCount(shOrderList);
                }
            }
        }
        shOrderLists = shOrderListService.showForPageResultList(queryObject);
        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", shOrderLists);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param modelMap
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/orderListInfo")
    public String orderListInfo(ModelMap modelMap, String orderId) {
        List<ShOrderProductMap> shOrderProductMaps = shOrderProductMapService.getByOrderListId(NumberUtils.toLong(orderId));
        if (shOrderProductMaps == null) {
            modelMap.addAttribute("errorMsg", "关联产品为空");
            return "error.ftl";
        }

        modelMap.addAttribute("orderId", orderId);
        return "shProduct/order_list_info.ftl";
    }

    /**
     * @param request
     * @param res
     */
    @RequestMapping("orderListInfoSearch")
    public void orderListInfo(QueryObject queryObject, HttpServletRequest request, HttpServletResponse res) {

        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        setQueryParameter("productName", queryObject);
        setQueryParameter("productCode", queryObject);
        setQueryParameter("productSize", queryObject);

        if (!StringUtils.isEmpty(getRequest().getParameter("orderId"))) {
            queryObject.getQueryCriterias().put("orderId", getRequest().getParameter("orderId"));

        }
        int count = productService.showForPageResultCount(queryObject);
        List<Product> shOrderProductMaps = productService.showForPageResultList(queryObject);

        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", shOrderProductMaps);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param request
     * @param res
     */
    @RequestMapping("orderListEdit")
    public String orderListEdit(HttpServletRequest request, ModelMap modelMap) {
        Long orderId = NumberUtils.toLong(request.getParameter("orderId"));
        Long entId = NumberUtils.toLong(request.getParameter("entId"));
        ShOrderList shOrderList = shOrderListService.getById(orderId);
        Enterprise enterprise = enterprisesService.selectById(entId);
        String createTime = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(shOrderList.getCreateTime());//shOrderList.getCreateTime().toLocaleString();
        modelMap.addAttribute("shOrderList", shOrderList);
        modelMap.addAttribute("enterprise", enterprise);
        modelMap.addAttribute("createTime", createTime);
        return "shProduct/edit.ftl";
    }

    /**
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping("update")
    public void update(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) {
        Long id = NumberUtils.toLong(request.getParameter("id"));
        Double alertCount = NumberUtils.toDouble(request.getParameter("alertCount"));
        Double stopCount = NumberUtils.toDouble(request.getParameter("stopCount"));
        ShOrderList shOrderList = new ShOrderList();
        shOrderList.setId(id);
        shOrderList.setAlertCount(alertCount);
        shOrderList.setStopCount(stopCount);

        try {
            if (shOrderListService.updateAlertSelective(shOrderList)) {
                modelMap.addAttribute("success", "更新成功");
                response.getWriter().write(JSON.toJSONString(modelMap));
                response.getWriter().flush();
                return;
            } else {
                modelMap.addAttribute("failure", "更新失败");
                response.getWriter().write(JSON.toJSONString(modelMap));
                response.getWriter().flush();
                return;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return;

    }

    /**
     * @param response
     * @param request
     * @param modelMap
     * @throws IOException
     */
    @RequestMapping("syncProduct")
    public void syncProduct(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) throws IOException {
        modelMap.addAttribute("success", "同步成功");
        response.getWriter().write(JSON.toJSONString(modelMap));
        response.getWriter().flush();
        return;
    }

    /**
     * @param response
     * @param request
     * @param modelMap
     * @throws IOException
     */
    @RequestMapping("getProducts")
    public void getProducts(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) throws IOException {
        Long orderListId = NumberUtils.toLong(request.getParameter("orderlistId"));

        // 全部产品
        List<Product> products = productService.getProductsByOrderListId(orderListId);
        modelMap.put("products", products);

        // 响应信息
        StreamUtils.copy(JSON.toJSONString(modelMap), Charsets.UTF_8, response.getOutputStream());
    }

    /**
     * 导出企业订购组
     * 
     * @param request
     * @param response
     * @param orderName
     */
    @RequestMapping("/creatOrderNameCSVfile")
    public void creatOrderNameCSVfile(HttpServletRequest request, HttpServletResponse response, String orderName, String entId,
            String startTime, String endTime) {

        Map map = new HashMap();

        /**
         * 查询参数: 企业名字、编码、效益
         */
        map.put("orderName", orderName);
        map.put("entId", NumberUtils.toLong(entId));

        if (!StringUtils.isEmpty(startTime)) {
            map.put("beginTime", startTime + " 00:00:00");
        }
        if (!StringUtils.isEmpty(endTime)) {
            map.put("endTime", endTime + " 23:59:59");
        }
        
        map.put("deleteFlag", "0");

        List<ShOrderList> list = shOrderListService.getOrderListByMap(map);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<String> title = new ArrayList<String>();
        title.add("订购组名称");
        title.add("订购组类型");
        title.add("订购组余额(元)");
        title.add("预警值(元)");
        title.add("暂停值(元)");
        title.add("创建时间");

        List<String> rowList = new ArrayList<String>();

        for (ShOrderList module : list) {
            rowList.add(module.getOrderName());

            if (module.getOrderType() != null) {
                String type = module.getOrderType();
                if ("01".equals(type)) {
                    rowList.add("全网订购");
                } else {
                    rowList.add("本地订购");
                }
            } else {
                rowList.add("");
            }

            if (module.getCount() != null) {
                String type = module.getOrderType();
                if ("01".equals(type)) {
                    rowList.add("-");
                } else {
                    Double count = module.getCount()/100.0;
                    rowList.add(count.toString());
                }
            } else {
                rowList.add("");
            }
            if (module.getAlertCount() != null) {
                String type = module.getOrderType();
                if ("01".equals(type)) {
                    rowList.add("-");
                } else {
                    Double count = module.getAlertCount()/100.0;
                    rowList.add(count.toString());
                }

            } else {
                rowList.add("");
            }
            if (module.getStopCount() != null) {
                String type = module.getOrderType();
                if ("01".equals(type)) {
                    rowList.add("-");
                } else {
                    Double count = module.getStopCount()/100.0;
                    rowList.add(count.toString());
                }

            } else {
                rowList.add("");
            }

            // 创建时间
            if (module.getCreateTime() != null) {
                rowList.add(sdf.format(module.getCreateTime()));
            } else {
                rowList.add("");
            }
        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "orderlist.csv");
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
     * 导出企业订购组
     * 
     * @param request
     * @param response
     * @param orderName
     */
    @RequestMapping("/creatOrderProductCSVfile")
    public void creatOrderProductCSVfile(HttpServletRequest request, HttpServletResponse response, 
            String productName, String productCode, String orderId) {

        Map map = new HashMap();

        /**
         * 查询参数: 企业名字、编码、效益
         */
        map.put("productName", productName);
        map.put("productCode", productCode);
        map.put("orderId", NumberUtils.toLong(orderId));
        map.put("deleteFlag", "0");

        List<Product> list = productService.getOrderProductByMap(map);

        List<String> title = new ArrayList<String>();
        title.add("产品名称");
        title.add("产品编码");
        title.add("产品大小");
        title.add("售出价格(元)");
        title.add("使用范围");
        title.add("漫游范围");

        List<String> rowList = new ArrayList<String>();

        for (Product module : list) {
            rowList.add(module.getName());

            if (module.getProductCode() != null) {
                rowList.add(module.getProductCode());
            } else {
                rowList.add("");
            }

            if (module.getProductSize() != null) {
                Long size = module.getProductSize();
                if (size < 1024) {
                    rowList.add(size.toString() + "KB");
                } else if (size < 1024 * 1024) {
                    rowList.add(String.valueOf(SizeUnits.KB.toMB(size)) + "MB");
                } else {
                    rowList.add(String.valueOf(SizeUnits.KB.toGB(size)) + "GB");
                } 
            } else {
                rowList.add("");
            }
            if (module.getPrice() != null) {
                Double price = module.getPrice() / 100.0;
                rowList.add(price.toString());
            } else {
                rowList.add("");
            }
            if (module.getOwnershipRegion() != null) {
                rowList.add(module.getOwnershipRegion());
            } else {
                rowList.add("");
            }
            if (module.getRoamingRegion() != null) {
                rowList.add(module.getRoamingRegion());
            } else {
                rowList.add("");
            }
        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, title));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "product.csv");
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
}
