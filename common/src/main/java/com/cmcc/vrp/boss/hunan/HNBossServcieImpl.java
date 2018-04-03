package com.cmcc.vrp.boss.hunan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.SyncAccountResult;
import com.cmcc.vrp.boss.hunan.model.HNBOSSAccount;
import com.cmcc.vrp.boss.hunan.model.HNBOSSCharge;
import com.cmcc.vrp.boss.hunan.model.HNBOSSProduct;
import com.cmcc.vrp.boss.hunan.model.HNBOSSQueryType;
import com.cmcc.vrp.boss.hunan.model.HNChargeResponse;
import com.cmcc.vrp.boss.hunan.model.HNQueryGroupAccountResponse;
import com.cmcc.vrp.boss.hunan.model.HNQueryGroupProductResponse;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpConnection;
import com.cmcc.vrp.util.HttpUtils;
import com.google.gson.Gson;

/**
 * @ClassName: HNBossServcieImpl
 * @Description: 湖南省BOSS接口实现类
 * @author: Rowe
 * @date: 2016年4月7日 下午4:57:30
 */
@Component("huNanBossService")
public class HNBossServcieImpl implements BossService {

    private static final Logger logger = LoggerFactory.getLogger(HNBossServcieImpl.class);
    @Autowired
    protected GlobalConfigService globalConfigService;
    @Autowired
    SupplierProductService supplierProductService;
    @Autowired
    ProductService productService;
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    private SerialNumService serialNumService;

    @Autowired
    private AccountService accountService;

    /**
     * 参数说明：<p>
     * mobileNums：手机号码,赠送的号码，多个号码以|隔开，最多不超过100个
     * groupId：集团ID
     * productId：产品编码
     * discnt：流量包编码，多个以|隔开，与号码一一对应
     * tradeId：流量平台生成的流水号
     */
    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile, String serialNum, Long prdSize) {
        //0 校验参数
        if (entId == null || splPid == null || StringUtils.isBlank(mobile) || StringUtils.isBlank(serialNum)) {
            logger.info("调用湖南省BOSS充值接口失败：缺少参数");
            return null;
        }

        //1.获取参数
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        if (enterprise == null || StringUtils.isBlank(enterprise.getCode())) {
            logger.info("调用湖南省BOSS充值接口失败：企业entId =" + entId + "不存在");
            return null;
        }

        SupplierProduct supplierProduct = supplierProductService.selectByPrimaryKey(splPid);
        if (supplierProduct == null || StringUtils.isBlank(supplierProduct.getCode())) {
            logger.info("调用湖南省BOSS充值接口失败：供应商产品pId =" + splPid + "不存在");
            return null;
        }

        String groupId = enterprise.getCode();//企业编码
        String discnt = supplierProduct.getCode();//产品编码变为资费编码
        String tradeId = SerialNumGenerator.buildNormalBossReqNum("hunan", 25);
        String bossProductId = getBossProductId();

        //2.校验参数
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(groupId) || StringUtils.isBlank(bossProductId)
                || StringUtils.isBlank(discnt) || StringUtils.isBlank(tradeId)) {
            logger.info("调用湖南省BOSS充值接口失败：缺少参数");
            return null;
        }

        //3.发送请求
        return newCharge(mobile, groupId, bossProductId, discnt, tradeId, serialNum);
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        SyncAccountResult sar = accountService.syncFromBoss(entId, prdId);
        logger.info("企业{}，产品{}，同步余额结果{}", entId, prdId, JSONObject.toJSONString(sar));
        return sar != null && SyncAccountResult.isSuccess(sar);
    }

    @Override
    public String getFingerPrint() {
        //md5hex("hunanbossservice");
        return "c681821e08db27039cf0c6223eaf8fbf";
    }

    private List<Product> queryProductsByEntId(Long entId, String serialNum) {

        //获取集团编码
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        if (enterprise == null || StringUtils.isBlank(enterprise.getCode())) {
            logger.info("调用湖南省BOSS查询企业产品列表失败：企业entId =" + entId + "不存在");
            return new LinkedList<Product>();
        }

        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("queryType", HNBOSSQueryType.QUERY_GROUP_PRODUCT.getCode());//查询类型
        queryMap.put("groupId", enterprise.getCode());//集团ID
        queryMap.put("type", getBossQueryType());//产品类型:00 通用流量统付业务

        HNQueryGroupProductResponse productResponse = (HNQueryGroupProductResponse) query(queryMap);
        return convertToProducts(productResponse);
    }

    //根据BOSS侧传回的产品编码，批量地获取产品信息
    private List<Product> convertToProducts(HNQueryGroupProductResponse response) {
        if (response != null && response.isSuccess()) {
            List<HNBOSSProduct> bossProducts = response.getDISCNT_DATA();

            Set<String> prdCodes = new HashSet<String>();
            for (HNBOSSProduct bossProduct : bossProducts) {
                prdCodes.add(bossProduct.getDISCNT_CODE());//资费编码当做产品编码
            }
            logger.info("BOSS侧返回的产品列表为: {}.", JSONObject.toJSONString(prdCodes));
            return productService.batchSelectByCodes(prdCodes);
        } else {
            return new LinkedList<Product>();
        }
    }

    /**
     * @param entId：企业ID
     * @param prdId：平台资金产品ID
     * @param serialNum
     * @return
     * @Title: queryAccountByEntId
     * @Description: 查询企业资金账户余额
     * @return: Account
     */
    public Account queryAccountByEntId(Long entId, Long prdId, String serialNum) {

        //获取集团编码
        Enterprise enterprise = enterprisesService.selectByPrimaryKey(entId);
        if (enterprise == null || StringUtils.isBlank(enterprise.getCode())) {
            logger.info("调用湖南省BOSS查询企业账户失败：企业entId =" + entId + "不存在");
            return null;
        }

        //查询集团产品余额
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("queryType", HNBOSSQueryType.QUERY_GROUP_ACCOUNT.getCode());//查询类型
        queryMap.put("groupId", enterprise.getCode());//集团ID
        queryMap.put("productId", getBossProductId());//产品ID
        HNQueryGroupAccountResponse accountResponse = (HNQueryGroupAccountResponse) query(queryMap);
        return convertToAccount(entId, prdId, accountResponse);

    }

    /**
     * @param prdId
     * @return
     * @Title: convertToAccount
     * @Description: 类型转换
     * @return: Account
     */
    private Account convertToAccount(Long entId, Long prdId, HNQueryGroupAccountResponse accountResponse) {
        if (accountResponse != null && accountResponse.isSuccess()) {
            Account account = new Account();
            account.setEnterId(entId);
            account.setOwnerId(entId);
            account.setProductId(prdId);
            account.setCount((double) (NumberUtils.toDouble(accountResponse.getBossAccount().getACCT_FEE())));
            logger.info("转为为平台账户信息：account = " + new Gson().toJson(account));
            return account;
        }
        return null;
    }

    /**
     * 参数说明：<p>
     * queryType：查询类型：查询集团产品列表；查询集团产品余额；
     * <p>查询集团产品列表需要的参数：
     * groupId：集团ID
     * type：产品类型:00 通用流量统付业务
     * <p>查询集团产品余额需要的参数：
     * groupId：集团ID
     * productId：产品编码
     */
    public BossOperationResult query(Map<String, Object> params) {
        //1.获取参数
        Integer queryType = (Integer) params.get("queryType");

        if (queryType == null) {
            logger.info("调用湖南省BOSS查询接口失败：缺少参数queryType");
            return null;
        }

        if (HNBOSSQueryType.QUERY_GROUP_PRODUCT.getCode().equals(queryType)) {//查询企业产品列表
            return newQueryGroupProduct((String) params.get("groupId"), (String) params.get("type"));

        } else if (HNBOSSQueryType.QUERY_GROUP_ACCOUNT.getCode().equals(queryType)) {//查询产品余额
            return newQueryGroupAccount((String) params.get("groupId"), (String) params.get("productId"));

        } else {
            logger.info("调用湖南省BOSS查询接口失败：未知的查询类型");
            return null;
        }
    }

    /**
     * 
     * @Title: updateRecord 
     * @Description: 更新
     * @param systemNum
     * @param bossRespNum
     * @param bossReqNum
     * @return
     * @return: boolean
     */
    public boolean updateRecord(String systemNum, String bossRespNum, String bossReqNum) {
        if (StringUtils.isBlank(systemNum) || serialNumService.getByPltSerialNum(systemNum) == null) {
            return false;
        }
        SerialNum serialNum = new SerialNum();
        serialNum.setBossReqSerialNum(bossReqNum);
        serialNum.setBossRespSerialNum(bossRespNum);
        serialNum.setPlatformSerialNum(systemNum);
        return serialNumService.updateSerial(serialNum);
    }

    /**
     * @param groupId
     * @return
     * @Title: queryGroupProduct
     * @Description: 根据集团ID查询产品列表
     * @return: HNQueryGroupProductResponse
     */
    private HNQueryGroupProductResponse queryGroupProduct(String groupId, String type) {
        //1.校验参数
        if (StringUtils.isBlank(groupId) || StringUtils.isBlank(type)) {
            logger.info("调用湖南省BOSS查询集团产品列表接口失败:请求参数不完整！GROUP_ID = " + groupId + ",TYPE=" + type);
            return null;
        }
        //2.组装请求参数
        String requestParam = assembleQueryProductParam(groupId, type);

        //3.发送请求
        String url = getBossUrl();
        logger.info("调用湖南省BOSS查询集团产品列表接口,请求url:{},请求报文：{}。", url, requestParam);
        String response = sendHttpRequest(url, requestParam);
        logger.info("调用湖南省BOSS查询集团产品列表接口,响应报文：{}。", response);
        //4.解析报文
        return JSON.parseObject(response, HNQueryGroupProductResponse.class);

    }

    /**
     * 
     * @Title: newQueryGroupProduct 
     * @Description: 湖南BOSS集团客户产品查询
     * @param groupId 企业编码
     * @param type  查询类型
     * @return
     * @return: HNQueryGroupProductResponse
     */
    public HNQueryGroupProductResponse newQueryGroupProduct(String groupId, String type) {
        try {
            //1.校验参数
            if (StringUtils.isBlank(groupId) || StringUtils.isBlank(type)) {
                logger.info("调用湖南省BOSS查询集团产品列表接口失败:请求参数不完整！");
                return new HNQueryGroupProductResponse();
            }

            //2.组装请求参数
            //通用参数
            //        String url = assembleCommon("DQ_HQ_HNAN_QryProductPreOrder");
            String url = assembleCommon(getBossQueryGroupProductMethod());

            //业务参数
            StringBuffer busiData = new StringBuffer();
            busiData.append("{\"GROUP_ID\":\"").append(groupId).append("\",\"TYPE\":\"").append(type).append("\"}");
            logger.info("湖南省BOSS查询集团产品列表接口:请求url=" + url + ",请求参数=" + busiData.toString());
            //3.发送请求
            String response = HttpConnection.doPost(url, busiData.toString(), "application/json", "utf-8", false);
            logger.info("湖南省BOSS查询集团产品列表接口响应报文：" + response);
            //4.解析报文
            JSONObject jsonObject = JSONObject.parseObject(response);

            String result = jsonObject.getString("result");
            if (StringUtils.isEmpty(result) || result.length() <= 2) {
                logger.info("湖南BOSS集团客户产品查询口响应报文解析失败！响应报文：" + response);
                logger.info("湖南BOSS集团客户产品查询口响应报文约定的格式示例：{\"respCode\":\"0\",\"respDesc\":\"ok\",\"result\":[{\"DISCNT_DATA\""
                        + ":[{\"DISCNT_CODE\":\"77410031\",\"DISCNT_NAME\":\"通用流量统付流量加油包3元/月\"}],\"PRODUCT_ID\":\"22000774\",\""
                        + "PRODUCT_NAME\":\"通用流量统付业务(产品)\",\"X_PAGINCOUNT\":\"0\",\"X_PAGINCURRENT\":\"0\",\"X_PAGINSIZE\":\"0\",\""
                        + "X_RESULTCODE\":\"0\",\"X_RESULTCOUNT\":\"0\",\"X_RESULTINFO\":\"ok\",\"X_RESULTSIZE\":\"1\",\"X_RSPCODE\":\""
                        + "0000\",\"X_RSPDESC\":\"ok\",\"X_RSPTYPE\":\"0\",\"aop-srv\":\"aopesb1\"}]}");
                HNQueryGroupProductResponse hNQueryGroupProductResponse = new HNQueryGroupProductResponse();
                hNQueryGroupProductResponse.setX_RESULTINFO("集团客户产品查询口响应报文解析失败：报文格式发生变更。");
                return hNQueryGroupProductResponse;
            }
            result = result.substring(1, result.length() - 1);//截取其中的产品列表信息
            String productId = JSONObject.parseObject(result).getString("PRODUCT_ID");//产品ID        
            String productName = JSONObject.parseObject(result).getString("PRODUCT_NAME");//产品名称

            HNQueryGroupProductResponse queryGroupProductResponse = JSON.parseObject(result,
                    HNQueryGroupProductResponse.class);

            //查询成功
            if (queryGroupProductResponse != null && queryGroupProductResponse.getDISCNT_DATA() != null
                    && !queryGroupProductResponse.getDISCNT_DATA().isEmpty()) {
                List<HNBOSSProduct> list = queryGroupProductResponse.getDISCNT_DATA();
                Iterator<HNBOSSProduct> it = list.iterator();
                while (it.hasNext()) {//回填产品ID和产品名称
                    HNBOSSProduct temp = it.next();
                    temp.setPRODUCT_ID(productId);
                    temp.setPRODUCT_NAME(productName);
                }
            }

            return queryGroupProductResponse;
        } catch (Exception e) {
            logger.info("湖南BOSS集团客户产品查询口调用失败：" + e.getMessage());
        }
        return new HNQueryGroupProductResponse();

    }

    private HNQueryGroupAccountResponse queryGroupAccount(String groupId, String productId) {
        //1.校验参数
        if (StringUtils.isBlank(groupId) || StringUtils.isBlank(productId)) {
            logger.info("调用湖南省BOSS查询集团产品余额接口失败:请求参数不完整！");
            return null;
        }
        //2.组装请求参数
        String requestParam = assembleQueryGroupAccountParam(groupId, productId);

        //3.发送请求
        String response = sendHttpRequest(getBossUrl(), requestParam);

        //4.解析报文
        HNQueryGroupAccountResponse account = JSON.parseObject(response, HNQueryGroupAccountResponse.class);
        HNBOSSAccount bossAccount = JSON.parseObject(response, HNBOSSAccount.class);
        account.setBossAccount(bossAccount);

        return account;
    }

    /**
     * 
     * @Title: newQueryGroupAccount 
     * @Description: 集团客户流量账户信息查询
     * @param groupId 企业编码
     * @param productId 产品编码
     * @return
     * @return: HNQueryGroupAccountResponse
     */
    public HNQueryGroupAccountResponse newQueryGroupAccount(String groupId, String productId) {

        try {
            //1.校验参数
            if (StringUtils.isBlank(groupId) || StringUtils.isBlank(productId)) {
                logger.info("调用湖南省BOSS集团客户流量账户信息查询接口失败:请求参数不完整！");
                return new HNQueryGroupAccountResponse();
            }
            //2.组装请求参数
            //通用参数
            String url = assembleCommon(getBossQueryGroupAccountMethod());
            //        String url = assembleCommon("DQ_HQ_HNAN_QryGrpAccoutInfo");

            //业务参数
            StringBuffer busiData = new StringBuffer();
            busiData.append("{\"GROUP_ID\":\"").append(groupId).append("\",\"PRODUCT_ID\":\"").append(productId)
                    .append("\"}");
            logger.info("湖南省BOSS集团客户流量账户信息查询接口:请求url=" + url + ",请求参数=" + busiData.toString());
            //3.发送请求
            String response = HttpConnection.doPost(url, busiData.toString(), "application/json", "utf-8", false);
            logger.info("湖南省BOSS集团客户流量账户信息查询接口响应报文:" + response);
            //4.解析报文
            HNQueryGroupAccountResponse queryGroupAccountResponse = new HNQueryGroupAccountResponse();
            JSONObject jsonObject = JSONObject.parseObject(response);

            String result = jsonObject.getString("result");
            if (StringUtils.isEmpty(result) || result.length() <= 2) {
                logger.info("湖南BOSS集团账户信息查询接口响应报文解析失败！响应报文：" + response);
                logger.info("湖南BOSS集团账户信息查询接口约定的响应报文格式示例：{\"respCode\":\"0\",\"respDesc\":\"ok\",\"result\":"
                        + "[{\"ACCT_FEE\":\"0\",\"GROUP_ADDR\":\"********\",\"GROUP_ID\":\"7313100109\","
                        + "\"GROUP_MEMO\":\"公司简介\",\"GROUP_NAME\":\"示**团1\",\"X_PAGINCOUNT\":\"0\","
                        + "\"X_PAGINCURRENT\":\"0\",\"X_PAGINSIZE\":\"0\",\"X_RESULTCODE\":\"0\","
                        + "\"X_RESULTCOUNT\":\"0\",\"X_RESULTINFO\":\"ok\",\"X_RESULTSIZE\":\"1\","
                        + "\"X_RSPCODE\":\"0000\",\"X_RSPDESC\":\"ok\",\"X_RSPTYPE\":\"0\",\"aop-srv\":\"aopesb1\"}]}");
                queryGroupAccountResponse.setX_RESULTINFO("集团账户信息查询接口响应报文解析失败：报文格式发生变更。");
                return queryGroupAccountResponse;
            }
            result = result.substring(1, result.length() - 1);//截取其中的账户信息
            HNBOSSAccount bossAccount = JSON.parseObject(result, HNBOSSAccount.class);
            if (bossAccount != null) {
                queryGroupAccountResponse.setBossAccount(bossAccount);
                queryGroupAccountResponse.setX_RESULTCODE(bossAccount.getX_RESULTCODE());
                queryGroupAccountResponse.setX_RESULTINFO(bossAccount.getX_RESULTINFO());
            }

            return queryGroupAccountResponse;
        } catch (Exception e) {
            logger.info("湖南省BOSS集团客户流量账户信息查询接口调用失败：" + e.getMessage());
        }
        return new HNQueryGroupAccountResponse();

    }

    /**
     * @return
     * @Title: assembleCommonParam
     * @Description: 组装通用的参数信息, 其中的参数按字典排序
     * @return: Map
     */
    private TreeMap<String, String> assembleCommonParam() {
        TreeMap<String, String> paramMap = new TreeMap<String, String>();
        paramMap.put("format", getBossFormat());
        paramMap.put("appid", getBossAppid());
        paramMap.put("version", getBossVersion());
        paramMap.put("status", getBossStatus());
        paramMap.put("locale", getBossLocale());
        paramMap.put("timestamp", new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
        return paramMap;
    }

    /**
     * @param groupId
     * @return
     * @Title: assembleQueryProductParam
     * @Description: 组装集团产品列表查询接口参数
     * @return: String
     */
    private String assembleQueryProductParam(String groupId, String type) {
        //0.通用参数
        TreeMap<String, String> common = assembleCommonParam();

        //1.拼装参数
        StringBuffer params = new StringBuffer(getBossSecret());
        common.put("method", getBossQueryGroupProductMethod());
        common.put("groupId", groupId);
        common.put("type", type);
        Map<String, String> map = new TreeMap<String, String>();
        map.putAll(common);
        Iterator<String> iter = map.keySet().iterator();
        StringBuffer result = null;
        while (iter.hasNext()) {
            String name = (String) iter.next();
            result = params.append(name).append(map.get(name));
        }
        result.append(getBossSecret());

        // 2.MD5签名
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            common.put("sign", byte2hex(md.digest(params.toString().getBytes("utf-8"))));
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        // 3.拼装URL参数
        StringBuilder param = new StringBuilder();
        for (Iterator<Map.Entry<String, String>> it = common.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> e = it.next();
            param.append("&").append(e.getKey()).append("=").append(e.getValue());
        }

        return param.toString().substring(1);
    }

    /**
     * @param groupId
     * @param productId
     * @return
     * @Title: assembleQueryUserAccountParam
     * @Description: 组装集团产品余额查询接口请求参数
     * @return: String
     */
    private String assembleQueryGroupAccountParam(String groupId, String productId) {
        //0.通用参数
        TreeMap<String, String> common = assembleCommonParam();

        // 1.拼装参数
        String bossSecret = getBossSecret();
        StringBuffer params = new StringBuffer(bossSecret);
        common.put("method", getBossQueryGroupAccountMethod());
        common.put("groupId", groupId);
        common.put("productId", productId);
        Map<String, String> map = new TreeMap<String, String>();
        map.putAll(common);
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            params.append(name).append(map.get(name));
        }
        params.append(bossSecret);

        // 2.MD5签名
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            common.put("sign", byte2hex(md.digest(params.toString().getBytes("utf-8"))));
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        // 3.拼装URL参数
        StringBuilder param = new StringBuilder();
        for (Iterator<Map.Entry<String, String>> it = common.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> e = it.next();
            param.append("&").append(e.getKey()).append("=").append(e.getValue());
        }
        return param.toString().substring(1);
    }

    /**
     * @param mobileNums
     * @param groupId
     * @param productId
     * @param discnt
     * @param tradeId
     * @return
     * @Title: assembleChargeParam
     * @Description: 组装充值接口请求参数
     * @return: String
     */
    private String assembleChargeParam(String mobileNums, String groupId, String productId, String discnt,
            String tradeId) {
        //0.通用参数
        TreeMap<String, String> common = assembleCommonParam();

        // 1.拼装参数
        String bossSecret = getBossSecret();
        StringBuffer params = new StringBuffer(bossSecret);
        common.put("method", getBossChargeMethod());
        common.put("mobileNums", mobileNums);
        common.put("groupId", groupId);
        common.put("productId", productId);
        common.put("discnt", discnt);
        common.put("tradeId", tradeId);
        Map<String, String> map = new TreeMap<String, String>();
        map.putAll(common);
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            params.append(name).append(map.get(name));
        }
        params.append(bossSecret);

        // 2.MD5签名
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            common.put("sign", byte2hex(md.digest(params.toString().getBytes("utf-8"))));
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        // 3.拼装URL参数
        StringBuilder param = new StringBuilder();
        for (Iterator<Map.Entry<String, String>> it = common.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> e = it.next();
            param.append("&").append(e.getKey()).append("=").append(e.getValue());
        }
        return param.toString().substring(1);
    }

    /**
     * @param b
     * @return
     * @Title: byte2hex
     * @Description: 二进制转字符串
     * @return: String
     */
    private String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString().toUpperCase();
    }

    /**
     * @param urlStr
     * @param content
     * @return
     * @Title: sendHttpRequest
     * @Description: 发送http请求
     * @return: String
     */
    private String sendHttpRequest(String urlStr, String content) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr + "?" + content);
            logger.info("请求地址：" + url);
            if ("https".equalsIgnoreCase(url.getProtocol())) {
                try {
                    SslUtils.ignoreSsl();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            String result = buffer.toString();
            logger.info("返回结果：" + result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("连接BOSS失败！");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        return null;
    }

    public String getBossUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_URL.getKey());
    }

    public String getBossFormat() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_FORMAT.getKey());
    }

    public String getBossAppid() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_APPID.getKey());
    }

    public String getBossStatus() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_ENV.getKey());
    }

    public String getBossVersion() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_VERSION.getKey());
    }

    public String getBossProvinceCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_PROVINCE_CODE.getKey());
    }

    public String getBossInModeCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_IN_MODE_CODE.getKey());
    }

    public String getBossTradeEparchyCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_TRADE_EPARCHY_CODE.getKey());
    }

    public String getBossTradeCityCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_TRADE_CITY_CODE.getKey());
    }

    public String getBossTradeDepartId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_TRADE_DEPART_ID.getKey());
    }

    public String getBossTradeStaffId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_TRADE_STAFF_ID.getKey());
    }

    public String getBossTradeDepartPasswd() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_TRADE_DEPART_PASSWD.getKey());
    }

    public String getBossTradeTerminalId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_TRADE_TERMINAL_ID.getKey());
    }

    public String getBossRouteEparchyCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_ROUTE_EPARCHY_CODE.getKey());
    }

    public String getBossProductId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_PRODUCT_ID.getKey());
    }

    public String getBossChargeMethod() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_CHARGE_METHOD.getKey());
    }

    public String getBossQueryGroupAccountMethod() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_QUERY_GROUP_ACCOUNT_METHOD.getKey());
    }

    public String getBossQueryType() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_QUERY_TYPE.getKey());
    }

    public String getBossQueryGroupProductMethod() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_QUERY_GROUP_PRODUCT_METHOD.getKey());
    }

    public String getBossSecret() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_SECRET.getKey());
    }

    public String getBossLocale() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HUNAN_LOCALE.getKey());
    }

    /**
     * 
     * @Title: assembleCommon 
     * @Description: 构建通用参数
     * @param method
     * @return
     * @return: String
     */
    public String assembleCommon(String method) {

        //        String requestUrl = "http://111.8.20.250:19001/oppf";
        //        String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());//时间戳
        //        String format = "json";//数据格式
        //        String appId = "505811";//appId,正式接入需要更换
        //        String status = "1";
        //        String flowId = timestamp;//渠道唯一流水
        //        String provinceCode = "HNAN";
        //        String inModeCode = "9";
        //        String tradeEparchyCode = "0731";
        //        String tradeCityCode = "XXXG";
        //        String tradeDepartId = "C0ZZC";
        //        String tradeStaffId = "ITFHYPT1";//正式接入需要更换
        //        String tradeDepartPasswd = "348688";//正式接入需要更换
        //        String tradeTerminalId = "120.76.211.39";//接入IP
        //        String routeEparchyCode = "0731";

        String requestUrl = getBossUrl();
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());//时间戳
        String format = getBossFormat();//数据格式
        String appId = getBossAppid();//appId,正式接入需要更换
        String status = getBossStatus();
        String flowId = timestamp;//渠道唯一流水
        String provinceCode = getBossProvinceCode();
        String inModeCode = getBossInModeCode();
        String tradeEparchyCode = getBossTradeEparchyCode();
        String tradeCityCode = getBossTradeCityCode();
        String tradeDepartId = getBossTradeDepartId();
        String tradeStaffId = getBossTradeStaffId();//正式接入需要更换
        String tradeDepartPasswd = getBossTradeDepartPasswd();//正式接入需要更换
        String tradeTerminalId = getBossTradeTerminalId();//接入IP
        String routeEparchyCode = getBossRouteEparchyCode();

        StringBuffer commonParam = new StringBuffer();
        commonParam.append(requestUrl).append("?method=").append(method).append("&timestamp=").append(timestamp)
                .append("&format=").append(format).append("&appId=").append(appId).append("&status=").append(status)
                .append("&flowId=").append(flowId).append("&PROVINCE_CODE=").append(provinceCode)
                .append("&IN_MODE_CODE=").append(inModeCode).append("&TRADE_EPARCHY_CODE=").append(tradeEparchyCode)
                .append("&TRADE_CITY_CODE=").append(tradeCityCode).append("&TRADE_DEPART_ID=").append(tradeDepartId)
                .append("&TRADE_STAFF_ID=").append(tradeStaffId).append("&TRADE_DEPART_PASSWD=")
                .append(tradeDepartPasswd).append("&TRADE_TERMINAL_ID=").append(tradeTerminalId)
                .append("&ROUTE_EPARCHY_CODE=").append(routeEparchyCode);

        return commonParam.toString();
    }

    /**
     * 
     * @Title: newCharge 
     * @Description: 集团客户流量赠送
     * @param mobile 手机号码
     * @param groupId 企业ID
     * @param productId 产品ID
     * @param discntCode 资费编码
     * @param bossReqNum 请求序列号
     * @param systemNum 平台生成是序列号
     * @return
     * @return: HNChargeResponse
     */
    public HNChargeResponse newCharge(String mobile, String groupId, String productId, String discntCode,
            String bossReqNum, String systemNum) {

        //组织公用参数
        String url = assembleCommon(getBossChargeMethod());

        //        String url = assembleCommon("DQ_HT_HNAN_CreateGroupMebOrder");

        //组织业务参数
        StringBuffer busiData = new StringBuffer();
        busiData.append("{\"SERIAL_NUMBERS\":\"").append(mobile).append("\",\"GROUP_ID\":\"").append(groupId)
                .append("\",\"PRODUCT_ID\":\"").append(productId).append("\",\"DISCNT_CODE\":\"").append(discntCode)
                .append("\",\"TRADE_ID\":\"").append(bossReqNum).append("\"}");

        logger.info("湖南BOSS集团客户流量赠送接口，充值请求url ： " + url + ",请求参数 ：" + busiData.toString());
        //发送充值请求
        String response = HttpUtils.post(url, busiData.toString(), "application/json");
        //        String response = HttpConnection.doPost(url, busiData.toString(), "application/json", "utf-8", false);
        logger.info("湖南BOSS集团客户流量赠送接口响应报文：" + response);

        //解析应答报文：充值状态解析、boss返回的额外信息解析
        HNChargeResponse chargeResponse = new HNChargeResponse();
        JSONObject jsonObject = JSONObject.parseObject(response);
        String result = jsonObject.getString("result");
        if (StringUtils.isEmpty(result) || result.length() <= 2) {
            logger.info("湖南BOSS集团客户流量赠送接口响应报文解析失败！响应报文：" + response);
            logger.info("湖南BOSS集团客户流量赠送接口约定的响应报文格式示例： {\"respCode\":\"0\",\"respDesc\":\"ok\",\"result\""
                    + ":[{\"SERIAL_NUMBERS\":\"\",\"X_PAGINCOUNT\":\"0\",\"X_PAGINCURRENT\":\"0\",\"X_PAGINSIZE\":\""
                    + "0\",\"X_RESULTCODE\":\"0\",\"X_RESULTCOUNT\":\"0\",\"X_RESULTINFO\":\"ok\",\"X_RESULTSIZE\":\""
                    + "1\",\"X_RSPCODE\":\"0000\",\"X_RSPDESC\":\"ok\",\"X_RSPTYPE\":\"0\",\"X_TRADE_ID\":\""
                    + "3117030295351395\",\"aop-srv\":\"aopesb1\"}]}");
            chargeResponse.setX_RESULTINFO("集团客户流量赠送接口响应报文解析失败：报文格式发生变更。");
            chargeResponse.setResultDesc("集团客户流量赠送接口响应报文解析失败：报文格式发生变更。");
            return chargeResponse;
        }
        result = result.substring(1, result.length() - 1);//截取其中的充值结果信息
        HNBOSSCharge charge = JSON.parseObject(result, HNBOSSCharge.class);
        //外层封装
        if (charge != null) {
            chargeResponse.setCharge(charge);
            chargeResponse.setX_RESULTCODE(charge.getX_RESULTCODE());
            chargeResponse.setX_RESULTINFO(charge.getX_RESULTINFO());
            chargeResponse.setResultCode(charge.getX_RESULTCODE());
            chargeResponse.setResultDesc(charge.getX_RESULTINFO());
        }

        //充值更新流水号
        if (updateRecord(systemNum, charge == null ? null : charge.getX_TRADE_ID(), bossReqNum)) {
            logger.info("湖南充值更新流水号成功");
        } else {
            logger.info("湖南充值更新流水号失败");
        }

        return chargeResponse;
    }

    //    public static void main(String args[]) {
    //        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
    //                "classpath:conf/applicationContext.xml");
    //        HNBossServcieImpl bossServcie = (HNBossServcieImpl) applicationContext.getBean("huNanBossService");
    //        HNQueryGroupAccountResponse hNQueryGroupAccountResponse = bossServcie.newQueryGroupAccount("7313100109",
    //                "22000774");
    //        System.out.print("集团账户查询接口：" + hNQueryGroupAccountResponse.isSuccess());
    //        HNQueryGroupProductResponse hNQueryGroupProductResponse = bossServcie.newQueryGroupProduct("7313100109", "00");
    //        System.out.print("集团产品查询接口：" + hNQueryGroupProductResponse.isSuccess());
    //        HNChargeResponse hNChargeResponse = bossServcie.newCharge("13507310001", "7313100109", "22000774", "77410031",
    //                "77410031", "77410031");
    //        System.out.print("流量充值接口：" + hNChargeResponse.isSuccess());
    //
    //    }
}
