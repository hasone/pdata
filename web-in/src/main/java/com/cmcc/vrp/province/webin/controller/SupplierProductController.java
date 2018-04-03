package com.cmcc.vrp.province.webin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.SupplierStatus;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.Supplier;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.vrp.util.CSVUtil;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.SizeUnits;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/***
 * 供應商產品服務器
 * */
@Controller
@RequestMapping("/manage/supplierProduct")
public class SupplierProductController extends BaseController {
    private static final org.slf4j.Logger LOGGER =
            LoggerFactory.getLogger(SupplierProductController.class);

    @Autowired
    SupplierProductService supplierProductService;

    @Autowired
    SupplierService supplierService;
    
    @Autowired
    GlobalConfigService getGlobalConfigService;
    
    @Autowired
    ProductService productService;
    
    /**
     * BOSS产品管理首页
     *
     * @date 2016年6月13日
     * @author wujiamin
     */
    @RequestMapping("index")
    public String index(QueryObject queryObject, ModelMap modelMap) {
        if(queryObject == null){
            queryObject = new QueryObject();
        }
        modelMap.addAttribute("back",queryObject.getBack());//增加返回标识
        return "supplierProduct/index.ftl";
    }

    /**
     * BOSS产品管理搜索
     *
     * @date 2016年6月13日
     * @author wujiamin
     */
    @RequestMapping("search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        /**
         * 查询参数: 供应商、上下架状态、运营商类型、产品大小、产品名称、产品编码、使用范围、漫游范围
         */

        setQueryParameter("supplierName", queryObject);
        setQueryParameter("status", queryObject);
        setQueryParameter("isp", queryObject);

        setQueryParameter("size", queryObject);
        setQueryParameter("unit", queryObject);

        setQueryParameter("name", queryObject);
        setQueryParameter("code", queryObject);
        setQueryParameter("roamingRegion", queryObject);
        setQueryParameter("ownershipRegion", queryObject);

        // 数据库查找符合查询条件的个数
        int count = supplierProductService.queryPaginationSupplierProductCount(queryObject);

        List<SupplierProduct> list = supplierProductService.queryPaginationSupplierProduct(queryObject);

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
     * 展示新建BOSS产品页面
     *
     * @date 2016年6月14日
     * @author wujiamin
     * 
     * edit by qinqinyan 2017/3/7
     * 在最新产品需求里无sync（是否有同步接口）字段，所以这里获取全部上架供应商
     */
    @RequestMapping("createBossProduct")
    public String createBossProduct(SupplierProduct product, String unit, String priceStr, String sizeStr, ModelMap modelMap) {
        Map map = new HashMap<String, String>();
        map.put("status", SupplierStatus.ON.getCode());
        List<Supplier> suppliers = supplierService.selectByMap(map);
        modelMap.put("suppliers", suppliers);

        modelMap.put("supplierProduct", product);
        modelMap.put("unit", unit);
        modelMap.put("priceStr", priceStr);
        modelMap.put("sizeStr", sizeStr);
        return "supplierProduct/createBossProduct.ftl";
    }

    /**
     * 保存新建的BOSS产品
     *
     * @date 2016年6月14日
     * @author wujiamin
     */
    @RequestMapping("saveBossProduct")
    public String saveBossProduct(SupplierProduct product, String unit, String priceStr, String sizeStr, ModelMap map) {
        //转换价格
        Double money = NumberUtils.toDouble(priceStr);
        money = money * 100;
        product.setPrice((int) Math.round(money));

        //转换流量
        Double size = Double.parseDouble(sizeStr);
        product.setSize(transformFlow(size, unit));

        //校验
        String result = validateBossProduct(product);
        if (!"success".equals(result)) {
            LOGGER.info("插入失败，失败原因：{}", result);
            map.put("errorMsg", result);
            return createBossProduct(product, unit, priceStr, sizeStr, map);
        }

        try{
            if (supplierProductService.insert(product)) {
                LOGGER.info("成功插入产品信息");
                return "redirect:index.html";
            };
        }catch(Exception e){
            LOGGER.error("新建失败，失败原因: {}", e.getMessage());
        }
        map.put("errorMsg", "新建失败");
        return createBossProduct(product, unit, priceStr, sizeStr, map);
    }
    
    /**
     * 编辑BOSS产品
     * @param map
     * @param id
     * @author qinqinyan
     * */
    @RequestMapping("edit")
    public String edit(ModelMap map, Long id) {
        if(id!=null){
            SupplierProduct supplierProduct = supplierProductService.selectById(id);
            if(supplierProduct!=null){
                //价格换算成元
                Double money = supplierProduct.getPrice()/100.0;
                map.addAttribute("money", money);
                map.addAttribute("supplierProduct", supplierProduct);
                return "supplierProduct/edit.ftl";
            }
        }
        map.addAttribute("errorMsg", "未找到该条BOSS产品的相关信息！");
        return "error.ftl";
    }
    
    /**
     * 编辑BOSS产品
     * @param product
     * @param priceStr
     * @param map
     * @author qinqinyan
     * */
    @RequestMapping("saveEdit")
    public String saveEdit(SupplierProduct product, String priceStr, ModelMap map) {
        if(product!=null){
            //转换价格
            product.setPrice(new BigDecimal(priceStr).multiply(new BigDecimal(100)).intValue());
            
            product.setUpdateTime(new Date());
            
            //校验产品编码是否唯一
            SupplierProduct validateObject = supplierProductService.selectByCodeAndSupplierId(product.getCode(), 
                    product.getSupplierId());
            if (validateObject!= null 
                    && !validateObject.getId().toString().equals(product.getId().toString())) {
                map.put("errorMsg", "该供应商下挂产品编码已存在");
                return edit(map, product.getId());
            }
            
            try{
                if(supplierProductService.updateByPrimaryKey(product)){
                    LOGGER.info("编辑成功。id = {}", product.getId());
                    return "redirect:index.html";
                }
            }catch(Exception e){
                LOGGER.error("编辑失败，失败原因: {}", e.getMessage());
            }
            map.put("errorMsg", "编辑失败");
            return edit(map, product.getId());
        }
        map.put("errorMsg", "未找到该条BOSS产品信息!");
        return "error.ftl";
    }
    
    /**
     * 上下架操作
     * @param response
     * @param id
     * @param status
     * @author qinqinyan
     * */
    @RequestMapping("changeStatus")
    public void changeStatus(HttpServletResponse response, Long id, Integer status)
            throws IOException{
        Map returnMap = new HashMap<String, String>();
        
        if(getCurrentUserManager()==null){
            LOGGER.info("获取用户信息失败。id = {}, status = {}", id, status);
            response.getWriter().write(JSON.toJSONString(returnMap));
            return;
        }
        if(id==null || status==null){
            LOGGER.info("存在参数为空。id = {}, status = {}", id, status);
            returnMap.put("msg", "参数错误");
            response.getWriter().write(JSON.toJSONString(returnMap));
            return;
        }
        
        String msg = "上架";
        if(status.intValue()==SupplierStatus.OFF.getCode()){
            msg = "下架";
        }else{
            //校验供应商上下架情况
            SupplierProduct supplierProduct = supplierProductService.selectById(id);
            if(supplierProduct.getSupplierStatus().intValue()==SupplierStatus.OFF.getCode()){
                LOGGER.info("BOSS产品id = {} 对应的供应商 supplierId = {} 是下架状态，因此不能上架该BOSS产品", 
                        id, supplierProduct.getSupplierId());
                returnMap.put("msg", 
                        "供应商【"+supplierProduct.getSupplierName()+"】处于下架状态,因此不能上架产品【"+supplierProduct.getName()+"】");
                response.getWriter().write(JSON.toJSONString(returnMap));
                return;
            }
        }
        
        SupplierProduct record = new SupplierProduct();
        record.setId(id);
        record.setStatus(status);
        record.setUpdateTime(new Date());
        if(supplierProductService.updateByPrimaryKey(record)){
            LOGGER.info(msg + "成功。id = {}, status = {}", id, status);
            msg = msg + "成功！";
        }else{
            LOGGER.info(msg + "失败。id = {}, status = {}", id, status);
            msg = msg + "失败！";
        }
        returnMap.put("msg", msg);
        response.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }

    /**
     * 自动同步BOSS产品
     *
     * @date 2016年6月14日
     * @author wujiamin
     */
    @RequestMapping("synchronizeBossProduct")
    public String synchronizeBossProduct(ModelMap map) {
        //TODO：自动同步BOSS产品
        return "redirect:index.html";
    }

    /**
     * 检验产品对象
     *
     * @date 2016年6月14日
     * @author wujiamin
     */
    private String validateBossProduct(SupplierProduct product) {
        if (product == null) {
            return "对象为空";
        }
        if (!StringUtils.isNotBlank(product.getIsp())) {
            return "请选择运营商";
        }
        if (product.getSupplierId() == null) {
            return "请选择供应商";
        }
        if (!StringUtils.isNotBlank(product.getCode())) {
            return "请输入产品编码";
        }
        if (product.getSize() == null) {
            return "请输入流量包大小";
        }
        if (!StringUtils.isNotBlank(product.getOwnershipRegion())) {
            return "请选择使用范围";
        }
        if (!StringUtils.isNotBlank(product.getRoamingRegion())) {
            return "请选择漫游范围";
        }
        if (!StringUtils.isNotBlank(product.getName())) {
            return "请输入产品名称";
        }

        //产品名称不做校验
        /*if (!(supplierProductService.selectByName(product.getName())).isEmpty()) {
            return "产品名称已存在";
        }*/

        if (supplierProductService.selectByCodeAndSupplierId(product.getCode(), product.getSupplierId()) != null) {
            return "该供应商下挂产品编码已存在";
        }

        return "success";
    }


    /**
     * 删除BOSS产品
     *
     * @date 2016年6月14日
     * @author wujiamin
     * @throws IOException 
     */
    @RequestMapping("deleteBossProduct")
    public void deleteBossProduct(Long id, HttpServletResponse response) throws IOException {
        Map returnMap = new HashMap<String, String>();
        if(getCurrentUserManager()==null){
            LOGGER.info("获取用户信息失败。id = {}", id);
            response.getWriter().write(JSON.toJSONString(returnMap));
            return;
        }
        String msg = "";
        if(id!=null){
            try{
                if(supplierProductService.deleteSupplierProduct(id)){
                    LOGGER.info("删除BOSS产品成功, id = {}", id);
                    msg = "成功删除BOSS产品！";
                }
            }catch(RuntimeException e){
                LOGGER.info("删除BOSS产品失败, id = {}, 失败原因  : {}", id, e.getMessage());
                msg = "删除BOSS产品失败！";
            }
        }else{
            msg = "参数异常,删除BOSS产品失败！";
        }
        returnMap.put("msg", msg);
        response.getWriter().write(JSON.toJSONString(returnMap));
        return;
    }

    /**
     * 显示BOSS产品详情
     *
     * @date 2016年6月15日
     * @author wujiamin
     * 
     * edit by qinqinyan 2017/3/9
     */
    @RequestMapping("showDetail")
    public String showDetail(Long id, ModelMap map) {
        SupplierProduct product = supplierProductService.selectByPrimaryKey(id);
        if (product == null) {
            map.put("errorMsg", "不存在该BOSS产品id：" + id);
            return "error.ftl";
        }
        //product.setSize(SizeUnits.KB.toMB(product.getSize()));
        map.put("product", product);
        
        List<Product>  platformProducts = productService.getPlatFormBySupplierId(id);
        map.put("platformProducts", platformProducts);
        return "supplierProduct/bossProductDetail.ftl";
    }
    
    @RequestMapping("platFormProductDetail")
    public String platFormProductDetail(ModelMap modelMap, Long productId){
        Product product = productService.get(productId);
        if (product == null) {
            return "redirect:index.html";
        }
        modelMap.addAttribute("product", product);
        modelMap.addAttribute("ziying", isZiying().toString());
        
        return "platformProduct/detail.ftl";
    }


    /**
     * 根据前端输入的单位转换流量
     *
     * @date 2016年6月15日
     * @author wujiamin
     */
    private Long transformFlow(Double size, String unit) {
        if ("GB".equals(unit)) {
            return SizeUnits.GB.toKB(size);
        }
        if ("MB".equals(unit)) {
            return SizeUnits.MB.toKB(size);
        }
        if ("KB".equals(unit)) {
            return SizeUnits.KB.toKB(size);
        }
        return null;
    }
    
    /**
     * 下载
     * @param response
     * @author qinqinyan
     * */
    @RequestMapping("downloadBossProducts")
    public void downloadBossProducts(HttpServletResponse response, String supplierName,
                                     String size, String unit, String name, String code,
                                     String isp, String ownershipRegion, String roamingRegion,
                                     String status){

        QueryObject queryObject = new QueryObject();
        if(!StringUtils.isEmpty(supplierName)){
            queryObject.getQueryCriterias().put("supplierName", supplierName);
        }
        if(!StringUtils.isEmpty(size)){
            queryObject.getQueryCriterias().put("size", size);
            queryObject.getQueryCriterias().put("unit", unit);
        }
        if(!StringUtils.isEmpty(name)){
            queryObject.getQueryCriterias().put("name", name);
        }
        if(!StringUtils.isEmpty(code)){
            queryObject.getQueryCriterias().put("code", code);
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

        //去掉分页
        queryObject.setPageSize(-1);

        List<SupplierProduct> list = supplierProductService.queryPaginationSupplierProduct(queryObject);
        List<String> titles = new ArrayList<String>();
        titles.add("序号");
        titles.add("供应商");
        titles.add("运营商");
        titles.add("产品名称");
        titles.add("产品编码");
        titles.add("产品大小");
        titles.add("采购价格(元)");
        titles.add("状态");
        titles.add("使用范围");
        titles.add("漫游范围");

        List<String> rowList = new ArrayList<String>();
        for(int i =1; i<=list.size(); i++){
            rowList.add("" + i);
            rowList.add(list.get(i-1).getSupplierName());
            rowList.add(getIspName(list.get(i-1).getIsp()));
            rowList.add(list.get(i-1).getName());
            rowList.add(list.get(i-1).getCode());
            rowList.add(getSizeStr(list.get(i-1).getSize()));
            rowList.add(""+list.get(i-1).getPrice().intValue()/100.0);
            if(list.get(i-1).getStatus().intValue()==SupplierStatus.ON.getCode()
                && list.get(i-1).getSupplierStatus().intValue() == SupplierStatus.ON.getCode()){
                rowList.add("上架");
            }else{
                rowList.add("下架");
            }
            rowList.add(list.get(i-1).getOwnershipRegion());
            rowList.add(list.get(i-1).getRoamingRegion());
        }

        InputStream inputStream;
        try {
            inputStream = CSVUtil.StringTOInputStream(CSVUtil.listToString(rowList, titles));
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + "Boss_Products.csv");
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
        if(StringUtils.isNotBlank(isp)){
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
