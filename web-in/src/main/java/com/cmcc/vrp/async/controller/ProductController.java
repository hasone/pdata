package com.cmcc.vrp.async.controller;

import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.bean.ProductItem;
import com.cmcc.vrp.ec.bean.ProductsResp;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.SizeUnits;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leelyn on 2016/5/17.
 */
@Controller
@RequestMapping(value = "products")
public class ProductController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    EnterprisesService enterprisesService;

    @Autowired
    ProductService productService;

    @Autowired
    EntProductService entProductService;
    
    @Autowired
    GlobalConfigService globalConfigService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public void getProducts(HttpServletRequest request, HttpServletResponse response) {
        List<Product> productList;
        Long enterpriseId;
        Enterprise enterprise;
        
        if (needCheck()) {
            String type = request.getContentType();
            if(StringUtils.isBlank(type)){
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                return; 
            }
            String contentType = request.getContentType();
            if (!(contentType.indexOf("application/xml") != -1 || contentType.indexOf("text/xml") != -1)) {
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                return;
            }
        }
        
        String appKey = (String) request.getAttribute(Constants.APP_KEY_ATTR);
        if (StringUtils.isBlank(appKey)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if ((enterprise = enterprisesService.selectByAppKey(appKey)) == null
        		|| enterprise.getDeleteFlag() != 0
        		|| enterprise.getInterfaceFlag() != 1) {
            LOGGER.error("企业不存在、企业暂停或接口关闭. appKey = {}", appKey);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if ((enterpriseId = enterprise.getId()) == null
        		|| (productList = entProductService.selectProductByEnterId(enterpriseId)) == null) {
            LOGGER.error("与该企业关联的产品不存在. appKey = {}", appKey);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        ProductsResp resp = buildProductsResp(productList, enterpriseId);
        XStream xStream = new XStream();
        xStream.alias("Response", ProductsResp.class);
        xStream.autodetectAnnotations(true);

        try {
            StreamUtils.copy(xStream.toXML(resp), Charsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("产品列表响应出错，错误信息为{}.", e.toString());
        }
    }

    private ProductsResp buildProductsResp(List<Product> productList, long enterpriseId) {
        ProductsResp productsResp = new ProductsResp();
        productsResp.setEnterpriseId(enterpriseId);
        productsResp.setTotalCount(productList.size());
        productsResp.setResponseTime(DateUtil.getRespTime());
        List<ProductItem> productItems = new ArrayList<ProductItem>();
        for (Product product : productList) {
            ProductItem item = new ProductItem();
            item.setProductName(product.getName());
            item.setProductCode(product.getProductCode());
            item.setCost(product.getPrice() / 100.);
            item.setSize(String.valueOf(SizeUnits.KB.toMB(product.getProductSize())));
            productItems.add(item);
        }
        productsResp.setProducts(productItems);
        return productsResp;
    }
    private boolean needCheck() {
        
        String checkFlag = globalConfigService.get(GlobalConfigKeyEnum.EC_NEED_CHECK.getKey());
        String finalFlag = org.apache.commons.lang.StringUtils.isBlank(checkFlag) ? "false" : checkFlag;
        return Boolean.parseBoolean(finalFlag);
    }
}
