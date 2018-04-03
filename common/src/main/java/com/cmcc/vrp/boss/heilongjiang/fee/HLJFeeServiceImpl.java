package com.cmcc.vrp.boss.heilongjiang.fee;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.sf.json.JSONArray;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpConnection;
import com.cmcc.vrp.util.MyRSA;
/**
 * 
 * @ClassName: HLJFeeServiceImpl 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年10月17日 上午10:49:57
 */
@Service
public class HLJFeeServiceImpl implements HLJFeeService {

    private static final String DATE_FORMAT = "yyyyMMddHHmmss";
    private static Logger LOG = LoggerFactory.getLogger(HLJFeeServiceImpl.class);

    private GlobalConfigService globalConfigService;

    @Override
    @Autowired
    public void setGlobalConfigService(GlobalConfigService globalConfigService) {
        this.globalConfigService = globalConfigService;
    }

    /**
     * An api that queries whether phone charge red packet that could be
     * transferred into prepay account Huafei hongbao zhanghu kezhuan yucun
     * chaxun API
     */
    @Override
    public PrepayQueryResponse prepayQuery(PrepayQueryRequest request) {
        HLJFeeServiceImpl.checkValid(request);
        Map<String, Object> map = new TreeMap<String, Object>();
        map.put("appKey", request.getAppKey());
        map.put("timeStamp", this.getDateString(request.getTimestamp(), HLJFeeServiceImpl.DATE_FORMAT));
        map.put("group_acc_no", request.getGroupAccNo());
        String url = this.getPrepayQuery();
        LOG.info("黑龙江话费红包账户可转预存查询接口,请求url:{},请求参数：{}。", url, map);
        String result = this.execute("GET", url, map);
        LOG.info("黑龙江话费红包账户可转预存查询接口响应报文：{}", result);
        if (result != null) {
            PrepayQueryResponse resp = (PrepayQueryResponse) JsonUtil.fromJson(result, PrepayQueryResponse.class);
            LOG.info(resp.toString());
            return resp;
        } else {
            return new PrepayQueryResponse();
        }
    }

    /**
     * Group corporation products are used for the personal charge service. Jituan chanpin wei geren chongzhi API
     * Group corporation products are used for the personal charge service.
     * Jituan chanpin wei geren chongzhi API
     */
    @Override
    public GroupPersonResponse groupPerson(GroupPersonRequest request) {
        HLJFeeServiceImpl.checkValid(request);
        GroupPersonResponse resp = null;
        Map<String, Object> map = new TreeMap<String, Object>();
        map.put("appKey", request.getAppKey());
        map.put("timeStamp", this.getDateString(request.getTimestamp(), HLJFeeServiceImpl.DATE_FORMAT));
        map.put("order_no", request.getOrderNo());
        map.put("order_date", request.getOrderDate());
        map.put("group_acc_no", request.getGroupAccNo());
        map.put("acc_nbr", request.getAccNbr());
        map.put("total_fee", request.getTotalFee());
        map.put("group_no", request.getGroupNo());
        String url = this.getGroupPerson();
        LOG.info("开始调用黑龙江话费充值接口,请求url:{},请求参数：{}。", url, map);
        String result = this.execute("GET", url, map);
        LOG.info("响应报文：{}", result);
        if (result != null) {
            resp = (GroupPersonResponse) JsonUtil.fromJson(result, GroupPersonResponse.class);
            LOG.info(resp.toString());
        }
        return resp;
    }

    /**
     * account balance query Huafei xinxi chaxun
     */
    @Override
    public BalanceQueryResponse balanceQuery(BalanceQueryRequest request) {
        HLJFeeServiceImpl.checkValid(request);
        Map<String, Object> map = new TreeMap<String, Object>();
        map.put("appKey", request.getAppKey());
        map.put("timeStamp", this.getDateString(request.getTimestamp(), HLJFeeServiceImpl.DATE_FORMAT));
        map.put("phone_no", request.getPhoneNo());
        String url = this.getBalanceQuery();
        LOG.info("黑龙江话费信息查询接口,请求url:{},请求参数：{}。", url, map);
        String result = this.execute("GET", url, map);
        LOG.info("黑龙江话费信息查询接口响应报文：{}", result);
        if (result != null) {
            BalanceQueryResponse resp = (BalanceQueryResponse) JsonUtil.fromJson(result, BalanceQueryResponse.class);
            LOG.info(resp.toString());
            return resp;
        }
        return new BalanceQueryResponse();
    }

    /**
     * Don't actually know what this Chinese means. Hongbao dianziquan yewu chongzheng
     * Don't actually know what this Chinese means. Hongbao dianziquan yewu
     * chongzheng
     */
    @Override
    public ECouponResponse eCoupon(ECouponRequest request) {
        HLJFeeServiceImpl.checkValid(request);
        Map<String, Object> map = new TreeMap<String, Object>();
        map.put("appKey", request.getAppKey());
        map.put("timeStamp", this.getDateString(request.getTimestamp(), HLJFeeServiceImpl.DATE_FORMAT));
        map.put("order_no", request.getOrderNo());
        map.put("order_time", request.getOrderTime());
        map.put("acc_nbr", request.getAccNbr());
        map.put("boss_order_no", request.getBossOrderNo());
        map.put("order_date", request.getOrderDate());
        map.put("group_acc_no", request.getGroupAccNo());
        map.put("group_no", request.getGroupNo());
        map.put("phone_no", request.getPhoneNo());
        String url = this.getECoupon();
        LOG.info("黑龙江话费红包电子券业务冲正接口,请求url:{},请求参数：{}。", url, map);
        String result = this.execute("GET", url, map);
        LOG.info("黑龙江话费红包电子券业务冲正接口响应报文：{}", result);
        if (result != null) {
            ECouponResponse resp = (ECouponResponse) JsonUtil.fromJson(result, ECouponResponse.class);
            LOG.info(resp.toString());
            return resp;
        }
        return new ECouponResponse();
    }

    /**
     * 
     * @Title: getDateString 
     * @Description: TODO
     * @param d
     * @param p
     * @return
     * @return: String
     */
    private String getDateString(Date d, String p) {
        SimpleDateFormat f = new SimpleDateFormat(p);
        return f.format(d);
    }

    /**
     * 
     * @Title: checkValid 
     * @Description: TODO
     * @param o
     * @return: void
     */
    private static void checkValid(Object o) {
        if (o == null) {
            throw new RuntimeException("parameter could not be null");
        }
        for (Field f : o.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
                if (f.get(o) == null) {
                    StringBuffer s = new StringBuffer("Heilongjiang fee charge service: ");
                    s.append(o.getClass()).append(".").append(f.getName()).append(" could not be null");
                    throw new RuntimeException(s.toString());
                }
            } catch (IllegalArgumentException e) {
                LOG.error(e.getMessage());
            } catch (IllegalAccessException e) {
                LOG.error(e.getMessage());
            }
        }
    }

    /**
     * To meet the specific argument requirements in doc.
     * 
     * @param resource
     * @param arguments
     * @return
     * @throws URISyntaxException
     */
    private java.net.URI getURI(String resource, Map<String, ?> arguments) throws URISyntaxException {
        if (arguments == null || arguments.size() == 0) {
            return null;
        }
        java.net.URI uri = null;
        String param = null;
        URIBuilder builder = new URIBuilder();
        Map<String, Object> treeMap = new TreeMap<String, Object>();
        treeMap.putAll(arguments);

        for (Iterator<?> iter = treeMap.entrySet().iterator(); iter.hasNext();) {
            Entry<?, ?> entry = (Entry<?, ?>) iter.next();
            builder.setParameter(entry.getKey().toString(), entry.getValue().toString());
        }
        param = builder.build().toString();
        param = param.charAt(0) == '?' ? param.substring(1) : param;
        String sign = this.getSignature(param, this.getPrivateKeyFile());
        builder.setParameter("sign", sign);
        builder.setPath(resource);
        uri = builder.build();

        return uri;
    }

    /**
     * Sending request to the server.
     * 
     * @param HTTPMethod
     *            Could not be null. one of PUT/POST/GET/DELETE string.
     * @param resource
     *            URL address. http://www.a.com/b/c
     * @param arguments
     *            a map, URL?param1=d&param2=e. param1 and param2 are keys, and
     *            d and 3 are values.
     * @return
     */
    private String execute(String HTTPMethod, String resource, Map<String, ?> arguments) {
        if (arguments == null) {
            throw new RuntimeException("arguments is null");
        }
        CheckParamNotNull.check(HTTPMethod, resource);
        HttpRequestBase httpRequest;
        CloseableHttpClient httpclient;
        java.net.URI uri;
        CloseableHttpResponse httpResponse = null;
        String content = null;
        try {
            uri = this.getURI(resource, arguments);
            if ("GET".equalsIgnoreCase(HTTPMethod)) {
                LOG.info("The HTTP GET request URL ：" + uri);
                httpRequest = new HttpGet(uri);
            } else {
                LOG.error("The HTTP Method must be GET.");
                throw new IOException();
            }
            httpclient = HttpClients.createDefault();
            httpResponse = httpclient.execute(httpRequest);
            InputStream in = httpResponse.getEntity().getContent();
            content = IOUtils.toString(in, "UTF-8");
            LOG.info("The repsone : " + content);
        } catch (URISyntaxException e) {
            LOG.error("The URI is not formatted properly:{}", e.getMessage());
        } catch (IOException e) {
            LOG.error("There was an error making the request:{}", e.getMessage());
        }

        return content;
    }

    /**
     * 
     * @Title: getSignature 
     * @Description: TODO
     * @param str
     * @param keyfile
     * @return
     * @return: String
     */
    public String getSignature(String str, String keyfile) {
        LOG.debug("签名字符串{}", str);
        String sign = null;
        try {
            // 1、字典排序
            // 2、请求参数做urlEncode
            str = URLEncoder.encode(str, "UTF-8");
            LOG.debug("一次URL编码后{}", str);

            // 3、对字符串做MD5加密
            String md5Str = DigestUtils.md5DigestAsHex(str.getBytes());
            LOG.debug("M5得到{}", md5Str);

            // 4、密钥加密
            sign = MyRSA.signWithFile(md5Str, keyfile);
            LOG.debug("私钥签名后{}", sign);

        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return sign;
    }

    /**
     * http://www.a.com/b/c
     * 
     * @return
     */
    private String getPrivateKeyFile() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_FEE_PRIVATE_KEY.getKey());
    };

   /**
    * 
    * @Title: getPrepayQuery 
    * @Description: TODO
    * @return
    * @return: String
    */
    private String getPrepayQuery() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_FEE_PREPAY_QUERY.getKey());
    }

    /**
     * 
     * @Title: getGroupPerson 
     * @Description: TODO
     * @return
     * @return: String
     */
    private String getGroupPerson() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_FEE_GROUP_PERSON.getKey());
    }

    /**
     * 
     * @Title: getBalanceQuery 
     * @Description: TODO
     * @return
     * @return: String
     */
    private String getBalanceQuery() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_FEE_BALANCE_QUERY.getKey());
    }

    /**
     * 
     * @Title: getECoupon 
     * @Description: TODO
     * @return
     * @return: String
     */
    private String getECoupon() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_FEE_ELECTRONIC_COUPON.getKey());
    }

    /**
     * 
     * @Title: redPacketCharge 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String redPacketCharge() {
        String url = "http://10.149.85.32:51000/esbWS/rest/com_sitech_acctmgr_inter_pay_I8014Svc_grpcfm";
        String requestParam = assemblePara();
        LOG.info("黑龙江红包充值，请求url = {}, 请求参数 = {}.", url, requestParam);
        String repsone = HttpConnection.doPost(url, requestParam, "application/json", "uft-8", false);
        LOG.info("黑龙江红包充值，请求url = {}, 请求参数 = {}, 应答报文 = {}.", url, requestParam, repsone);
        return repsone;
    }

    /**
     * 
     * @Title: assemblePara 
     * @Description: TODO
     * @return
     * @return: String
     */
    public static String assemblePara() {
        //约定的报文格式
        //        {
        //            "ROOT": {
        //                "HEADER": {
        //                    "DB_ID": "",
        //                    "ENV_ID": "",
        //                    "ROUTING": {
        //                        "ROUTE_KEY": "",
        //                        "ROUTE_VALUE": ""
        //                    }          
        //                },
        //                "BODY": {
        //                    "OPR_INFO": {
        //                        "OP_CODE": "8014",
        //                        "LOGIN_NO": "aan70s",
        //                        "GROUP_ID":"13436",
        //                        "PROVINCE_ID":"230000"
        //                    },
        //                    "BUSI_INFO": {
        //                        "JT_CONTRACT_NO":"230340003021379483",
        //                        "PHONE_NO": "13514526763",
        //                        "FOREIGN_SN":"1234567890",
        //                        "TRANS_COUNT":"4",
        //                        "ONE_TRANS_FEE":"111",
        //                        "UNIT_ID":"4515143266",
        //                        "PAY_PATH":"S9",
        //                        "PAY_METHOD":"A",
        //                        "OP_TYPE":"HBCZTrans",
        //                        "OP_NOTE":""
        //                    }
        //                }
        //            }
        //        }

        String dbId = "";
        String envId = "";

        String routeKey = "";//路由类型
        String routeValue = "";//路由值

        String opCode = "8014";//操作代码
        String loginNo = "aan70s";//工号
        String groupId = "13436";//工号归属,传入“8014”
        String provinceId = "230000";//省份代码,黑龙江填写：230000

        String jtContractNo = "230340003021379483";//集团账户
        String phoneNo = "13514526763";//用户号码
        String foreignSn = "1234567890";//外部流水
        String transCount = "4";//红包个数
        String oneTransFee = "111";//每次转账金额
        String unitId = "4515143266";//集团编码
        String payPath = "S9";//缴费渠道,建行：S8；杭研：S9；米粒：SA；
        String payMethod = "A";//缴费方式,写死传入"A"
        String opType = "HBCZTrans";//转账类型,写死传入“HBCZTrans”
        String opNote = "";//备注,可空       

        //构建ROUTING
        Map<String, Object> routingMap = new HashMap<String, Object>();
        routingMap.put("ROUTE_KEY", routeKey);//路由类型
        routingMap.put("ROUTE_VALUE", routeValue);//路由值

        //构建HEADER
        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("DB_ID", dbId);
        headerMap.put("ENV_ID", envId);
        headerMap.put("ROUTING", routingMap);

        //构建OPR_INFO
        Map<String, Object> oprInfoMap = new HashMap<String, Object>();
        oprInfoMap.put("OP_CODE", opCode);//操作代码
        oprInfoMap.put("LOGIN_NO", loginNo);//工号
        oprInfoMap.put("GROUP_ID", groupId);//工号归属,传入“8014”
        oprInfoMap.put("PROVINCE_ID", provinceId);//省份代码,黑龙江填写：230000

        //构建BUSI_INFO
        Map<String, Object> busiMap = new HashMap<String, Object>();
        busiMap.put("JT_CONTRACT_NO", jtContractNo);//集团账户
        busiMap.put("PHONE_NO", phoneNo);//用户号码
        busiMap.put("FOREIGN_SN", foreignSn);//外部流水
        busiMap.put("TRANS_COUNT", transCount);//红包个数
        busiMap.put("ONE_TRANS_FEE", oneTransFee);//每次转账金额
        busiMap.put("UNIT_ID", unitId);//集团编码
        busiMap.put("PAY_PATH", payPath);//缴费渠道,建行：S8；杭研：S9；米粒：SA；
        busiMap.put("PAY_METHOD", payMethod);//缴费方式,写死传入"A"
        busiMap.put("OP_TYPE", opType);//转账类型,写死传入“HBCZTrans”
        busiMap.put("OP_NOTE", opNote);//备注,可空

        //构建BODY
        Map<String, Object> bodyMap = new HashMap<String, Object>();
        bodyMap.put("OPR_INFO", oprInfoMap);
        bodyMap.put("BUSI_INFO", busiMap);

        //
        Map<String, Object> allMap = new HashMap<String, Object>();
        allMap.put("HEADER", headerMap);
        allMap.put("BODY", bodyMap);

        //构建ROOT
        Map<String, Object> rootMap = new HashMap<String, Object>();
        rootMap.put("ROOT", allMap);

        //去掉外层的[]，黑龙江BOSS约定，奇葩
        String result = JSONArray.fromObject(rootMap).toString();
        return result.substring(1, result.length() - 1);
    }

}
