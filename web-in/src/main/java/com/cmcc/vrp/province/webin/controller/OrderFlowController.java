package com.cmcc.vrp.province.webin.controller;

import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.Supplier;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.vrp.province.sms.login.SmsRedisListener;
import com.cmcc.vrp.util.AssembleXml;
import com.cmcc.vrp.util.Dom4jXml;
import com.cmcc.vrp.util.IpUtils;
import com.cmcc.webservice.hainan.HaiNanBOSSProduct;
import com.cmcc.webservice.hainan.HaiNanOrder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * OrderFlowController.java
 */
@RequestMapping("/order")
@Controller
public class OrderFlowController {

    private static Logger logger = Logger.getLogger(OrderFlowController.class);
    private static String TIME_FOMMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";//时间格式

    @Autowired
    SupplierProductService supplierProductService;

    @Autowired
    ProductService productService;

    @Autowired
    SmsRedisListener smsRedisListener;

    @Autowired
    @Qualifier("haiNanBossService")
    BossService bossService;

    @Autowired
    SupplierService supplierService;

    @Autowired
    SupplierProductMapService supplierProductMapService;

    @Autowired
    private EnterprisesService enterprisesService;

    @Autowired
    private AccountService accountService;


    /** 
     * @Title: order 
    */
    @RequestMapping(value = "BOSSOrder", method = RequestMethod.POST,
        consumes = org.springframework.http.MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public void order(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/xml;charset=utf-8");
        String xmlStr = (String) request.getAttribute(Constants.BODY_XML_ATTR);
        logger.info("order:clientAddr:" + IpUtils.getRemoteAddr(request) + ",clientHost:" + request.getRemoteHost() + ",xml:" + xmlStr);
        String returnXML = null;

        //校验报文
        String errorMsg = validateOrderXml(xmlStr);
        if (errorMsg != null) {
            returnXML = assembleOrderResponse(new Date(), "", "403", errorMsg);
            logger.info("订购接口返回报文：" + returnXML);
            response.getWriter().write(returnXML);
            return;
        }

        //解析报文
        HaiNanOrder orderInfo = parseOrderXML(xmlStr);

        //校验企业是否存在
        String grpId = orderInfo.getGrpId();
        Enterprise enterprise = enterprisesService.selectByCode(grpId);
        if (enterprise == null) {
            returnXML = assembleOrderResponse(new Date(), "", "403", "企业【GRP_ID=" + grpId + "】尚未在流量平台端注册");
            logger.info("订购接口返回报文：" + returnXML);
            response.getWriter().write(returnXML);
            return;
        }

        //校验供应商是否存在
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        if (suppliers == null || suppliers.size() <= 0) {
            returnXML = assembleOrderResponse(new Date(), "", "403", "平台尚未存储任何供应商信息");
            logger.info("订购接口返回报文：" + returnXML);

            response.getWriter().write(returnXML);
            return;
        }

        Supplier supplier = null;
        for (Supplier s : suppliers) {
            if (s.getFingerprint().equals(bossService.getFingerPrint())) {
                supplier = s;
                break;
            }
        }
        if (supplier == null) {
            returnXML = assembleOrderResponse(new Date(), "", "403", "供应商【fingerprint=" + bossService.getFingerPrint() + "】尚未在平台注册");
            logger.info("订购接口返回报文：" + returnXML);

            response.getWriter().write(returnXML);
            return;
        }

        //存储企业账户信息
        List<HaiNanBOSSProduct> proList = orderInfo.getItem();
        for (HaiNanBOSSProduct pInfo : proList) {
            storeEntAccountInfo(grpId, pInfo.getPakNum(), pInfo.getPakMoney(), pInfo.getPakGPRS(), supplier.getId());
        }

        //组织响应报文
        String transIDD = SerialNumGenerator.buildSerialNum();
        returnXML = assembleOrderResponse(new Date(), transIDD, "200", "OK");
        logger.info("订购接口返回报文：" + returnXML);
        response.getWriter().write(returnXML);
        return;
    }

    /**
     * @param xml
     * @return
     * @Title: parseXML
     * @Description: 解析订购接口报文
     * @return: List<OrderInfo>
     */
    private HaiNanOrder parseOrderXML(String xml) {
        HaiNanOrder orderInfo = new HaiNanOrder();
        List<HaiNanBOSSProduct> items = new ArrayList<HaiNanBOSSProduct>();
        try {
            Element rootElement = Dom4jXml.getRootElement(xml);

            //请求时间DATETIME
            Element dateTimeElement = rootElement.element("DATETIME");
            orderInfo.setRequestTime(new SimpleDateFormat(TIME_FOMMAT).parse(dateTimeElement.getStringValue().trim()));

            //CONTENT
            Element contentElement = rootElement.element("CONTENT");
            orderInfo.setGrpId(contentElement.element("GRP_ID").getStringValue().trim());
            orderInfo.setRequestSerialNumber(contentElement.element("TRANS_IDO").getStringValue().trim());
            List<Element> elements = contentElement.elements("ITEM");
            for (Element element : elements) {
                HaiNanBOSSProduct pInfo = new HaiNanBOSSProduct();
                pInfo.setPakNum(element.element("PAK_NUM").getStringValue().trim());
                pInfo.setPakMoney(element.element("PAK_MONEY").getStringValue().trim());
                pInfo.setPakGPRS(element.element("PAK_GPRS").getStringValue().trim());
                pInfo.setPakEndDate(element.element("PAK_END_DTAE").getStringValue().trim());
                items.add(pInfo);
            }
            orderInfo.setItem(items);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return orderInfo;
    }

    /**
     * @param xml
     * @return
     * @Title: validateOrderXml
     * @Description: 校验订购接口报文
     * @return: String
     */
    private String validateOrderXml(String xml) {
        try {

            if (StringUtils.isBlank(xml)) {
                return "请求报文为空";
            }

            //校验请求报文
            Element rootElement = Dom4jXml.getRootElement(xml);

            //校验DATETIME字段
            Element dateTimeElement = rootElement.element("DATETIME");
            if (dateTimeElement == null) {
                return "缺少DATETIME字段";
            } else if (StringUtils.isBlank(dateTimeElement.getStringValue().trim())) {
                return "DATETIME字段数据为空";
            } else {
                new SimpleDateFormat(TIME_FOMMAT).parse(dateTimeElement.getStringValue().trim());
            }

            //校验CONTENT字段
            Element contentElement = rootElement.element("CONTENT");
            if (contentElement == null) {
                return "缺少CONTENT字段";
            } else if (StringUtils.isBlank(dateTimeElement.getStringValue().trim())) {
                return "CONTENT字段数据为空";
            }

            //校验TRANS_IDO字段
            Element transIDOElement = contentElement.element("TRANS_IDO");
            if (transIDOElement == null) {
                return "缺少TRANS_IDO字段";
            } else if (StringUtils.isBlank(transIDOElement.getStringValue().trim())) {
                return "TRANS_IDO字段数据为空";
            }

            //校验GRP_ID字段
            Element grpElement = contentElement.element("GRP_ID");
            if (grpElement == null) {
                return "缺少GRP_ID字段";
            } else if (StringUtils.isBlank(grpElement.getStringValue().trim())) {
                return "GRP_ID字段数据为空";
            }

            //校验ITME字段
            List<Element> elements = contentElement.elements("ITEM");
            if (elements == null || elements.size() <= 0) {
                return "缺少ITEM字段";
            } else {
                for (Element element : elements) {
                    if (element.element("PAK_NUM") == null) {
                        return "缺少PAK_NUM";
                    } else if (StringUtils.isBlank(element.element("PAK_NUM").getStringValue().trim())) {
                        return "PAK_NUM字段数据为空";
                    }

                    if (element.element("PAK_MONEY") == null) {
                        return "缺少PAK_MONEY";
                    } else if (StringUtils.isBlank(element.element("PAK_MONEY").getStringValue().trim())) {
                        return "PAK_MONEY字段数据为空";
                    }

                    if (element.element("PAK_GPRS") == null) {
                        return "缺少PAK_GPRS";
                    } else if (StringUtils.isBlank(element.element("PAK_GPRS").getStringValue().trim())) {
                        return "PAK_GPRS字段数据为空";
                    }

                    if (element.element("PAK_END_DTAE") == null) {
                        return "缺少PAK_END_DTAE";
                    } else if (StringUtils.isBlank(element.element("PAK_END_DTAE").getStringValue().trim())) {
                        return "PAK_END_DTAE字段数据为空";
                    } else {
                        String dateString = element.element("PAK_END_DTAE").getStringValue().trim();
                        try {
                            new SimpleDateFormat(TIME_FOMMAT).parse(dateString);
                        } catch (ParseException e) {
                            return new StringBuilder().append("PAK_END_DTAE字段数据格式错误，请求参照格式\'").append(TIME_FOMMAT).append("\'").toString();
                        }
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
            return "报文格式错误，非XML格式报文";
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new StringBuilder().append("DATETIME字段数据格式错误，请求参照格式'")
                .append(TIME_FOMMAT).append("'").toString();
        }
        return null;
    }

    /**
     * @param reponseTime
     * @param transIDD
     * @param code
     * @param codeInfo
     * @return
     * @Title: assembleOrderResponse
     * @Description: 组织订购接口响应报文
     * @return: String
     */
    private String assembleOrderResponse(Date reponseTime, String transIDD, String code, String codeInfo) {
        try {
            //构建CONTENT节点
            Map<String, Object> contentMap = new LinkedHashMap<String, Object>();
            contentMap.put("TRANS_IDD", transIDD);//响应序列号
            contentMap.put("RESULTE_CODE", code);//状态码
            contentMap.put("RESULTE_INFO", codeInfo);//操作结果说明

            //构建Response节点
            Map<String, Object> responseMap = new LinkedHashMap<String, Object>();
            responseMap.put("DATETIME", new SimpleDateFormat(TIME_FOMMAT).format(reponseTime));//响应时间
            responseMap.put("CONTENT", contentMap);

            return AssembleXml.map2Xml(responseMap, "Response", "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean storeEntAccountInfo(String groupId, String pakNum, String pakMoney,
                                        String pakGPRS, Long ispId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        //1.校验参数
        if (StringUtils.isBlank(pakNum) || StringUtils.isBlank(pakMoney) || StringUtils.isBlank(pakGPRS)) {
            return false;
        }

        //2.校验产品信息是否存在，如果不存在则新建产品
        //通过GRP_ID,pakMoney,pakGPRS,pakEndDate唯一确定一个产品{"GRP_ID":"XXXXXX","PAK_MONEY":"XXXXXX","PAK_GPRS":"XXXXXX"}
        String feature = "{\"GRP_ID\":\"" + groupId + "\",\"PAK_MONEY\":\"" + pakMoney + "\",\"PAK_GPRS\":\"" + pakGPRS + "\"}";

        Enterprise enterprise = enterprisesService.selectByCode(groupId);//获取企业信息

        SupplierProduct supplierProduct = supplierProductService.selectByFeature(feature);//获取供应商产品信息

        Map<Long, Double> productAmountMap = new HashMap<Long, Double>();
        if (supplierProduct == null) {//供应商产品不存在,则新增供应商产品、新增平台产品、新增账户，并将其关联起来
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("GRP_ID", groupId);
            map.put("PAK_MONEY", pakMoney);
            map.put("PAK_GPRS", pakGPRS);
            map.put("ispId", ispId);
            if (productService.createNewProduct(map)) {//创建账户
                supplierProduct = supplierProductService.selectByFeature(feature);//获取供应商产品信息
                List<Product> products = supplierProductMapService.getBySplPid(supplierProduct.getId());//获取平台产品信息
                if (products != null && products.size() > 0) {//创建账户
                    for (Product product : products) {
                        productAmountMap.clear();
                        productAmountMap.put(product.getId(), Double.valueOf(pakNum));
                        accountService.createEnterAccount(enterprise.getId(), productAmountMap, sdf.format(new Date()));
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {//产品存在,则更新企业账户余额
            List<Product> products = supplierProductMapService.getBySplPid(supplierProduct.getId());//获取平台产品信息
            if (products != null && products.size() > 0) {
                for (Product product : products) {
                    productAmountMap.clear();
                    productAmountMap.put(product.getId(), Double.valueOf(pakNum));
                    Account account = accountService.get(enterprise.getId(), product.getId(), AccountType.ENTERPRISE.getValue());
                    if (account == null) {//账户不存在，则新增账户
                        accountService.createEnterAccount(enterprise.getId(), productAmountMap, sdf.format(new Date()));
                    } else {//账户存在，更新账户
                        accountService.addCount(account.getEnterId(), account.getProductId(),
                                AccountType.ENTERPRISE, Double.valueOf(pakNum), sdf.format(new Date()), "海南BOSS推送企业账户信息");
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }
}
