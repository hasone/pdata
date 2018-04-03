package com.cmcc.vrp.province.webin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.dao.ProductMapper;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.ProductConverter;
import com.cmcc.vrp.province.model.json.PrdInterditPage;
import com.cmcc.vrp.province.service.ProductConverterService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.QueryObject;
import com.google.gson.Gson;

/**
 * 产品禁止转换关系Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/manage/productConverter")
public class ProductConverterController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductConverterController.class);
    
    @Autowired
    ProductConverterService converterService;
    
    @Autowired
    ProductService productService;
    
    @Autowired
    ProductMapper productMapper;

    /**
     * 编辑页面
     */
    @RequestMapping("/edit")
    public String edit(ModelMap model, QueryObject queryObject, HttpServletRequest request) {
        List<Integer> initPrdTypes = new ArrayList<Integer>();
        initPrdTypes.add((int)Constants.ProductType.FLOW_ACCOUNT.getValue());
        initPrdTypes.add((int)Constants.ProductType.FLOW_PACKAGE.getValue());
        initPrdTypes.add((int)Constants.ProductType.MOBILE_FEE.getValue());
        
        List<Product> products = productService.getPrdsByType(null, initPrdTypes , null);
        model.addAttribute("initSrcPrds", products);
        return "productConverter/edit.ftl";
    }
    
    /**
     * 删除页面
     */
    @RequestMapping(value = "delete")
    public String delete(ModelMap modelMap, Long id, QueryObject queryObject) {
        if (id == null) {
            return showProductList(modelMap, queryObject);
        }
        
        if (!converterService.delete(id)) {
            LOGGER.error("用户ID:" + getCurrentUser().getId() + " 删除产品转换关系Id:" + id + "失败");
        } else {
            LOGGER.error("用户ID:" + getCurrentUser().getId() + " 删除产品转换关系Id:" + id + "成功");
        }
        
        return showProductList(modelMap, queryObject);
    }
    
    /**
     * 列表页
     */
    @RequestMapping(value = "index")
    public String showProductList(ModelMap modelMap, QueryObject queryObject) {
        if(queryObject != null){
            modelMap.addAttribute("back",queryObject.getBack());//增加返回标识 
        }
        return "productConverter/index.ftl";
    }
    
    /**
     * 产品列表查找
     */
    @RequestMapping(value = "/search")
    public void search(QueryObject queryObject, HttpServletResponse res) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }

        setQueryParameter("srcProductName", queryObject);
        setQueryParameter("destProductName", queryObject);
        setQueryParameter("srcProductSize", queryObject);
        convertProductSize(queryObject);//检验是否正确，正确的话将大小从Mb转化为Kb

        // 数据库查找符合查询条件的个数
        List<ProductConverter> converterList = converterService.page(queryObject);

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", converterList);
        json.put("total", converterService.count(queryObject));
        json.put("queryObject", queryObject.toMap());        
        queryObject.toMap().clear();

        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void convertProductSize(QueryObject queryObject){
        String sizeStr =(String) queryObject.getQueryCriterias().get("srcProductSize");
        Integer sizeInt = 0;
        if(StringUtils.isBlank(sizeStr)){
            //nothing todo
        }
        else if((sizeInt = NumberUtils.toInt(sizeStr)) > 0){
            queryObject.getQueryCriterias().put("srcProductSize", sizeInt * 1024);
        }else{
            queryObject.getQueryCriterias().put("srcProductSize", -1);//为了保证对方输入非法字符时搜索为空
        }
    }
    
    
    /**
     * 编辑页面载入json
     */
    @RequestMapping("/initConverterData")
    public void initConverterData(HttpServletRequest request, HttpServletResponse resp) throws IOException{
        List<ProductConverter> converterList = converterService.get();
        resp.getWriter().write(new Gson().toJson(new PrdInterditPage(converterList)));
        return;
    }
    
    /**
     * 编辑页面载入可用的目标产品
     */
    @RequestMapping("/getAvailDestPrds")
    public void getAvailDestPrds(HttpServletRequest request, HttpServletResponse resp,Long sourcePrdId) throws IOException{
        Product product = productService.get(sourcePrdId);
        
        Map<String, Object> params =new HashMap<String, Object>();
        if(product == null){
            params.put("success", "false");
            resp.getWriter().write(JSON.toJSONString(params));
        }else{
            List<Integer> initPrdTypes = new ArrayList<Integer>();
            
            if (product.getType() == Constants.ProductType.FLOW_PACKAGE.getValue()) {
                initPrdTypes.add((int)Constants.ProductType.FLOW_ACCOUNT.getValue());
                initPrdTypes.add((int)Constants.ProductType.CURRENCY.getValue());
                
            }else if(product.getType() == Constants.ProductType.FLOW_ACCOUNT.getValue() ){
                initPrdTypes.add((int)Constants.ProductType.CURRENCY.getValue());
            }

            params.put("success", "true");
            params.put("result", productService.getPrdsByType(sourcePrdId, initPrdTypes , null));
            resp.getWriter().write(JSON.toJSONString(params));
            return;
        }

    }
    
    /**
     * 编辑页面提交
     */
    @RequestMapping("/editSubmitAjax")
    public void editSubmitAjax(
            HttpServletRequest request,HttpServletResponse resp,String converterPage) throws IOException{
        
        PrdInterditPage page = JSON.parseObject(converterPage, PrdInterditPage.class);
       
        //从页面和数据库中的数据，做相互的减集运算，决定了数据库需要删除的对象和需要添加的对象
        List<ProductConverter> pageList = page.getConvertLists();
        List<ProductConverter> dataBaseList = converterService.get();
        
        Map<String, Object> params = new HashMap<String, Object>();
        
        try{
            if(converterService.save(pageList, dataBaseList)){
                params.put("success", "true");
            }else{
                params.put("success", "false");
                params.put("result", "插入失败");
            }
            
        }catch(TransactionException e){
            LOGGER.error(e.getMessage());
            params.put("success", "false");
            params.put("result", "插入失败");
        }
        
        resp.getWriter().write(JSON.toJSONString(params));
    }
    
    
    /**
     * 获取产品列表
     *
     * @author qinqinyan
     */
    @RequestMapping("searchProduct")
    public void searchProduct(QueryObject queryObject, HttpServletRequest request, HttpServletResponse response) {
        if (queryObject == null) {
            queryObject = new QueryObject();
        }
        /**
         * 查询参数: 产品名称,产品编码
         */
        setQueryParameter("productName", queryObject);

        List<Integer> types = new ArrayList<Integer>();
        types.add((int)Constants.ProductType.FLOW_ACCOUNT.getValue());
        types.add((int)Constants.ProductType.FLOW_PACKAGE.getValue());
        types.add((int)Constants.ProductType.MOBILE_FEE.getValue());

        List<Product> products = productService.getPrdsByTypePageList(queryObject, types);
        int count = productService.getPrdsByTypePageCount(queryObject, types);

        JSONObject json = new JSONObject();
        json.put("pageNum", queryObject.getPageNum());
        json.put("pageSize", queryObject.getPageSize());
        json.put("data", products);
        json.put("total", count);
        json.put("queryObject", queryObject.toMap());
        try {
            response.getWriter().write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
}
