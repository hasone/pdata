package com.cmcc.webservice.hainan;

import com.cmcc.vrp.boss.BossService;
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
import com.cmcc.vrp.util.HttpUtils;
import com.cmcc.vrp.util.IpUtils;
import com.cmcc.vrp.util.MD5;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * <p>Title: </p> <p>Description: </p>
 *
 * @author lgk8023
 * @date 2017年1月22日 下午5:01:23
 */
@Path("/")
@Component
public class OrderFlowWebService {

    private static Logger logger = Logger.getLogger(OrderFlowWebService.class);
    private static String TIME_FOMMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";//时间格式
    private static Long TOKEN_VALIDATE = 7 * 24 * 60 * 60 * 1000L;//token有效期，默认为7天
    private static String TOKEN_VALIDATE_PREFIXX = "VALIDATE";//token有效期前缀
    @Autowired
    SupplierProductService supplierProductService;
    @Autowired
    ProductService productService;
    @Context
    HttpServletRequest request;

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

    public static void main(String[] args) throws DocumentException {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);

        //获取token
//        String baseURL = "http://hainantest.4ggogo.com";
        String baseURL = "http://localhost:8080";
        String authorizationURL = baseURL + "/web-in/open/authorizations";
        String authRequestTime = sdf.format(new Date());
        String appKey = "81c48d8575574dc8b7531cd0581d2216";
        String appSecret = "354a7b284dcc4294a542ad71ef85ffae";
        String sign = DigestUtils.sha256Hex(appKey + authRequestTime + appSecret);
        String requestXML = "<Request><DATETIME>" + authRequestTime
                + "</DATETIME><AUTHORIZATION><APP_KEY>" + appKey + "</APP_KEY><SIGN>"
                + sign + "</SIGN></AUTHORIZATION></Request>";
        System.out.println("认证接口调用：url = " + authorizationURL + ",param = " + requestXML);

        String response = HttpUtils.post(authorizationURL, requestXML);


        //解析报文
        Element rootElement = Dom4jXml.getRootElement(response);
        Element authorization = rootElement.element("AUTHORIZATION");
        String token = authorization.element("TOKEN").getStringValue();
        String hashedToken = authorization.element("HASHED_TOKEN").getStringValue();

        String orderURL = baseURL + "/web-in/open/BOSSOrder";

        String orderRequestTime = sdf.format(new Date());
        String transIDO = "999999999";
        String groupId = "8989859421";
        String requestBody = "<Request><DATETIME>" + orderRequestTime + "</DATETIME><CONTENT>"
                + "<TRANS_IDO>" + transIDO + "</TRANS_IDO>"
                + "<GRP_ID>" + groupId + "</GRP_ID>"
                + "<ITEM><PAK_NUM>1000</PAK_NUM><PAK_MONEY>45</PAK_MONEY><PAK_GPRS>101</PAK_GPRS><PAK_END_DTAE>" + orderRequestTime + "</PAK_END_DTAE></ITEM>"
                + "<ITEM><PAK_NUM>2000</PAK_NUM><PAK_MONEY>46</PAK_MONEY><PAK_GPRS>201</PAK_GPRS><PAK_END_DTAE>" + orderRequestTime + "</PAK_END_DTAE></ITEM>"
                + "<ITEM><PAK_NUM>3000</PAK_NUM><PAK_MONEY>47</PAK_MONEY><PAK_GPRS>301</PAK_GPRS><PAK_END_DTAE>" + orderRequestTime + "</PAK_END_DTAE></ITEM>"
                + "</CONTENT></Request>";

//		String signatrue = MD5.sign(requestBody, token, "utf-8");
        String signatrue = MD5.HMACSign(requestBody, token);
        System.out.println("订购接口调用：url = " + orderURL + ",param = " + requestBody
                + ",token = " + token + ",hashedToken = " + hashedToken + ",signatrue = "
                + signatrue);
        String responseBody = doPost(orderURL, requestBody, hashedToken, signatrue, "utf-8", false);
        System.out.println(responseBody);

    }

    /**
     * @param url
     * @param reqStr
     * @param hashedToken
     * @param signatrue
     * @param charset
     * @param pretty
     * @return
     */
    public static String doPost(String url, String reqStr, String hashedToken, String signatrue, String charset, boolean pretty) {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();

        PostMethod method = new PostMethod(url);
        try {
            method.addRequestHeader("4GGOGO-Auth-Token", hashedToken);
            method.addRequestHeader("HTTP-X-4GGOGO-Signature", signatrue);
            method.setRequestEntity(new StringRequestEntity(reqStr, "text/plain", "utf-8"));

            client.executeMethod(method);
            System.out.println("返回的状态码为" + method.getStatusCode());
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(method.getResponseBodyAsStream(),
                                charset));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (pretty) {
                        response.append(line).append(
                                System.getProperty("line.separator"));
                    } else {
                        response.append(line);
                    }
                }
                System.out.println("返回的数据为" + response.toString());
                reader.close();
            }
        } catch (UnsupportedEncodingException e1) {
            System.out.println(e1.getMessage());
            return "";
        } catch (IOException e) {
            System.out.println("执行HTTP Post请求" + url + "时，发生异常！" + e.getMessage());

            return "";
        } finally {
            method.releaseConnection();
        }
        return response.toString();
    }

    /**
     * @Title: authorizations
     * @Description: 认证接口
     * @return: Response
     */
    @Path("authorizations")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response authorizations(String xmlStr) {

        try {
            //1、校验报文格式
            String errorMsg = validateAuthXML(xmlStr);
            if (errorMsg != null) {
                String returnXML = assembleOrderResponse(new Date(), "", "403", errorMsg);
                logger.info("认证接口返回报文：" + returnXML);
                return Response.status(200).entity(returnXML).build();
            }

            //2.生成token
            Element rootElement = Dom4jXml.getRootElement(xmlStr);
            Element authorizationElement = rootElement.element("AUTHORIZATION");//认证参数
            Element appKeyElement = authorizationElement.element("APP_KEY");//AppKey
            String appKey = appKeyElement.getStringValue();
            Enterprise enterprise = enterprisesService.selectByAppKey(appKey);

            String rawToken = enterprise.getAppSecret() + SerialNumGenerator.buildSerialNum();
            String encptyToken = MD5.sign(rawToken, enterprise.getAppSecret(), "utf-8");
            String hashedToken = String.valueOf(encptyToken.hashCode());

            //4.将token存入redis中，以供验证
            smsRedisListener.putMsgToRedis(hashedToken, encptyToken);

            //5.组织返回报文
            Long validateTime = System.currentTimeMillis();
            validateTime += TOKEN_VALIDATE;
            Calendar calendar = Calendar.getInstance();
            Date requestTime = calendar.getTime();
            calendar.setTimeInMillis(validateTime);
            Date expiredTime = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("TOKEN", encptyToken);
            map.put("HASHED_TOKEN", hashedToken);
            map.put("CREATE_AT", sdf.format(requestTime));
            map.put("UPDATE_AT", sdf.format(requestTime));
            map.put("EXPIRED_AT", sdf.format(expiredTime));
            smsRedisListener.putMsgToRedis(TOKEN_VALIDATE_PREFIXX + hashedToken, String.valueOf(validateTime));//存储有效期

            Map<String, Object> returnMap = new LinkedHashMap<String, Object>();
            returnMap.put("DATETIME", sdf.format(new Date()));//响应时间
            returnMap.put("AUTHORIZATION", map);
            String returnXml = AssembleXml.map2Xml(returnMap, "Response");
            logger.info("认证接口返回报文：" + returnXml);
            return Response.status(200).entity(returnXml).build();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.status(403).entity("Fobidden").build();
    }

    /**
     * @Title: validateAuthXML
     * @Description: 校验认证接口报文
     * @return: String
     */
    private String validateAuthXML(String xmlStr) {
        String returnMsg = null;
        try {
            //校验报文是否为空
            if (StringUtils.isBlank(xmlStr)) {
                return "请求报文为空";
            }

            //校验报文体
            Element rootElement = Dom4jXml.getRootElement(xmlStr);
            Element dateTimeElement = rootElement.element("DATETIME");//请求时间
            if (dateTimeElement == null || StringUtils.isBlank(dateTimeElement.getStringValue())) {
                return "缺少参数DATETIME";
            }

            Element authorizationElement = rootElement.element("AUTHORIZATION");//认证参数
            if (authorizationElement == null) {
                return "缺少参数AUTHORIZATION";
            }

            Element appKeyElement = authorizationElement.element("APP_KEY");//AppKey
            if (appKeyElement == null || StringUtils.isBlank(appKeyElement.getStringValue())) {
                return "缺少参数APP_KEY";
            }

            String appKey = appKeyElement.getStringValue();//
            Enterprise enterprise = enterprisesService.selectByAppKey(appKey);
            if (enterprise == null) {
                return "该省BOSS尚未在平台端注册";
            }

            Element signElement = authorizationElement.element("SIGN");//签名
            if (signElement == null || StringUtils.isBlank(signElement.getStringValue())) {
                return "缺少签名";

            }
            String sign = DigestUtils.sha256Hex(appKey + dateTimeElement.getStringValue() + enterprise.getAppSecret());
            if (!sign.equals(signElement.getStringValue())) {
                return "无效的签名";
            }
        } catch (DocumentException e) {
            returnMsg = "报文格式错误";
        }
        return returnMsg;
    }

    /**
     * @Title: order
     * @Description: 订购流量
     * @return: Response
     */
    @Path("BOSSOrder")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response order(String xmlStr) {

        logger.info("order:clientAddr:" + IpUtils.getRemoteAddr(request) + ",clientHost:" + request.getRemoteHost() + ",xml:" + xmlStr);

        String returnXML = null;

        //校验报文
        String errorMsg = validateOrderXml(xmlStr);
        if (errorMsg != null) {
            returnXML = assembleOrderResponse(new Date(), "", "403", errorMsg);
            logger.info("订购接口返回报文：" + returnXML);
            return Response.status(200).entity(returnXML).build();
        }

        //解析报文
        HaiNanOrder orderInfo = parseOrderXML(xmlStr);

        //校验企业是否存在
        String grpId = orderInfo.getGrpId();
        Enterprise enterprise = enterprisesService.selectByCode(grpId);
        if (enterprise == null) {
            returnXML = assembleOrderResponse(new Date(), "", "403", "企业【GRP_ID=" + grpId + "】尚未在流量平台端注册");
            logger.info("订购接口返回报文：" + returnXML);
            return Response.status(200).entity(returnXML).build();
        }

        //校验供应商是否存在
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        if (suppliers == null || suppliers.size() <= 0) {
            returnXML = assembleOrderResponse(new Date(), "", "403", "平台尚未存储任何供应商信息");
            logger.info("订购接口返回报文：" + returnXML);
            return Response.status(200).entity(returnXML).build();
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
            return Response.status(200).entity(returnXML).build();
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
        return Response.status(200).entity(returnXML).build();

    }

    /**
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
     * @Title: validateOrderRequestHeader
     * @Description: 校验订购接口请求头
     * @return: String
     */
    private String validateOrderRequestHeader(String xmlStr) {
        String returnMsg = null;
        //验证token的正确性
        String hashedToken = request.getHeader("4GGOGO-Auth-Token");
        if (StringUtils.isBlank(hashedToken)) {
            returnMsg = "请求头缺少4GGOGO-Auth-Token";
        }

        String redisToken = smsRedisListener.getMsgFromRedis(hashedToken);
        if (StringUtils.isBlank(redisToken)) {
            returnMsg = "Token错误";
        }

        String tokenValidate = smsRedisListener.getMsgFromRedis(TOKEN_VALIDATE_PREFIXX + hashedToken);
        if (StringUtils.isBlank(tokenValidate) || Long.valueOf(tokenValidate) < System.currentTimeMillis()) {
            returnMsg = "token已过期";
        }

        //验证签名的正确性
        String signature = request.getHeader("HTTP-X-4GGOGO-Signature");//获取签名
        if (StringUtils.isBlank(signature)) {
            returnMsg = "缺少签名";
        }
        String result = MD5.HMACSign(xmlStr, redisToken);
        if (!result.equalsIgnoreCase(signature)) {
            returnMsg = "无效的签名";
        }
        return returnMsg;
    }

    /**
     * @Title: validateOrderXml
     * @Description: 校验订购接口报文
     * @return: String
     */
    private String validateOrderXml(String xml) {
        try {

            if (StringUtils.isBlank(xml)) {
                return "请求报文为空";
            }

            //校验请求头
            String returnMsg = validateOrderRequestHeader(xml);
            if (returnMsg != null) {
                return returnMsg;
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
                            return new StringBuilder().append("PAK_END_DTAE字段数据格式错误，请求参照格式").append("\"").append(TIME_FOMMAT).append("\"").toString();
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
            return new StringBuilder().append("DATETIME字段数据格式错误，请求参照格式").append("\"")
                    .append(TIME_FOMMAT).append("\"").toString();
        }
        return null;
    }

    /**
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

            return AssembleXml.map2Xml(responseMap, "Response");
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
                        accountService.addCount(account.getEnterId(), account.getProductId(), AccountType.ENTERPRISE,
                                Double.valueOf(pakNum), sdf.format(new Date()), "海南BOSS推送企业账户信息");
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }
}