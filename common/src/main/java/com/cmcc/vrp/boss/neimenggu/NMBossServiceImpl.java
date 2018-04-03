package com.cmcc.vrp.boss.neimenggu;

import com.google.gson.GsonBuilder;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossService;
import com.cmcc.vrp.boss.neimenggu.model.PubInfo;
import com.cmcc.vrp.boss.neimenggu.model.query.ProductObject;
import com.cmcc.vrp.boss.neimenggu.model.query.ResponseQueryData;
import com.cmcc.vrp.boss.neimenggu.model.send.ChargeBusiParams;
import com.cmcc.vrp.boss.neimenggu.model.send.RequestChargeObject;
import com.cmcc.vrp.boss.neimenggu.model.send.ResponseSendData;
import com.cmcc.vrp.boss.neimenggu.model.send.SendChargeObject;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.cmcc.vrp.util.RSAUtil;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URLEncoder;
import java.security.interfaces.RSAPublicKey;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * <p>Title: </p> <p>Description: </p>
 *
 * @author lgk8023
 * @date 2017年1月22日 上午10:59:28
 */
public class NMBossServiceImpl implements BossService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NMBossServiceImpl.class);

    private static final String CHARSET_NAME = Charsets.UTF_8.name();
    @Autowired
    EnterprisesService enterprisesService;
    @Autowired
    SupplierProductService supplierProductService;
    @Autowired
    ProductService productService;
    @Autowired
    NMBossQueryServiceImpl nmBossQueryService;
    @Autowired
    private GlobalConfigService globalConfigService;

    @Override
    public BossOperationResult charge(Long entId, Long splPid, String mobile,
                                      String serialNum, Long prdSize) {
        Enterprise e = enterprisesService.selectByPrimaryKey(entId);
        SupplierProduct supplierProduct = supplierProductService.selectByPrimaryKey(splPid);

        String transactionId = UUID.randomUUID().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        String transactionTime = dateFormat.format(new Date());
        ResponseSendData datas = getRecvMsg(transactionId, transactionTime, serialNum, e.getCode(), mobile, supplierProduct.getCode());

        NMBossOperationResultImpl result = new NMBossOperationResultImpl(datas);

        return result;
    }

    @Override
    public String getFingerPrint() {
        return "nmg";
    }

    private List<Product> queryProductsByEntId(Long entId, String serialNum) {
        Enterprise enterprise = null;
        if (entId == null
                || StringUtils.isBlank(serialNum)
                || (enterprise = enterprisesService.selectByPrimaryKey(entId)) == null) {
            LOGGER.error("无效的参数，从BOSS侧查询企业帐户信息失败. EntId = {}, SerialNum = {}.", entId, serialNum);
            return null;
        }

        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS").format(new Date());
        ResponseQueryData rqd = nmBossQueryService.getRecvMsg(serialNum, datetime, enterprise.getCode());
        return convertToProducts(rqd);
    }

    private Account queryAccountByEntId(Long entId, Long prdId, String serialNum) {
        Enterprise enterprise = null;
        if (entId == null
                || prdId == null
                || StringUtils.isBlank(serialNum)
                || (enterprise = enterprisesService.selectByPrimaryKey(entId)) == null) {
            LOGGER.error("无效的参数，从BOSS侧查询企业帐户信息失败. EntId = {}, PrdId = {}, SerialNum = {}.", entId, prdId, serialNum);
            return null;
        }

        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS").format(new Date());
        ResponseQueryData rqd = nmBossQueryService.getRecvMsg(serialNum, datetime, enterprise.getCode());
        LOGGER.info("BOSS侧返回的查询响应数据为EntCode = {}, Balance = {}.", rqd.getGroupId(), rqd.getBalance());

        return convertToAccount(rqd, prdId);
    }

    @Override
    public boolean syncFromBoss(Long entId, Long prdId) {
        Account account = queryAccountByEntId(entId, prdId, SerialNumGenerator.buildSerialNum());
        if (account == null) {
            return false;
        }
        return true;
    }

    public ResponseSendData getRecvMsg(String transactionId, String transactionTime,
                                       String outSysSn, String groupId, String phoneId, String prodId) {

        //得到Http返回的信息
        String recvMsg = getRecvData(transactionId, transactionTime,
                outSysSn, groupId, phoneId, prodId);

        //得到解密后的数据
        String parseDataString = new NMBossUtils(getPubKeyStr()).getReturnMsg(recvMsg);
        LOGGER.info("返回后解析的数据为:" + parseDataString);

        // 将解析后的数据，转化为ResponseSendData对象
        ResponseSendData datas = getRecvObject(parseDataString);
        LOGGER.info("BOSS returns {}.", JSONObject.toJSONString(datas));
        return datas;
    }

    /**
     * transactionId: 公共信息中的流水号，可传任何数据 transactionTime：发送时间 outSysSn：外系统发起时流水号 groupId：集团Id prodId:
     * 产品Id
     */
    private String getRecvData(String transactionId, String transactionTime,
                               String outSysSn, String groupId, String phoneId, String prodId) {

        String json = getSendJson(transactionId, transactionTime,
                outSysSn, groupId, phoneId, prodId);

        LOGGER.info("发送到boss的json数据为：" + json);
        String url = getSendUrl(json);
        LOGGER.info("发送到boss的url为：" + url);

        return HttpUtils.post(url, json);

        //return "{\"respInfo\":{\"code\":\"0\",\"message\":\"Successful operation\"},\"respData\":{\"resp_key\":\"641E1D42161D2C67A3FDB68B7CDA7DCDD33D0CC197F71DD4AA23FBD4D12C7546C197D4BAC4009CCAB2830E601D4AB1E924998FC831CF4C382F5AF314D2A4D38243882CE447F3F951E8A99276BCA6252F5D7924B516EF28A09DEF7FEB354C822D748E685715D220BB70FD9ADF04F964791EF3182F36283FA4CC16CDEAB00FB07B\",\"resp_msg\":\"s8kvqaB4dlLbFER/a70IOesES22L7UnkT0PemnFbyz9VSlPfoHPl21bgygjerg5I1U2ocwTi7zlzCWfhh8+ypt64w9XGJyh6tO4NRA1/vkrcysQ4janjBWaZBlfP+8Rrvpd2rWoOO7/FJbMO121jntamNoAmSaRHIzG+/S3xJgK6gm7qcTIhgJ9GCcCVvXqM3aprfAh7zwO0W2BGS3PJKQ\u003d\u003d\"},\"sign\":{\"isSigned\":true,\"sign\":\"i6Z98o2Rz2wb8SztVvlhrQJEzGM\u003d\"}}";

    }


    /**
     * 得到发送到boss的String对象
     */
    public String getSendJson(String transactionId, String transactionTime,
                              String outSysSn, String groupId, String phoneId, String prodId) {
        RequestChargeObject request = constructRequestObject(outSysSn, groupId, phoneId, prodId);
        SendChargeObject send = getSendQueryObject(request, transactionId, transactionTime);
        GsonBuilder gb = new GsonBuilder();
        com.google.gson.Gson gson = gb.create();
        gb.setDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        String msg = gson.toJson(send);
        return msg;
    }


    /**
     * 得到最终查询发送的对象。 transactionId 外系统发起时流水 transactionTime 交易时间
     */
    public SendChargeObject getSendQueryObject(RequestChargeObject request, String transactionId, String transactionTime) {
        SendChargeObject send = new SendChargeObject();

        PubInfo pubInfo = new PubInfo();
        pubInfo.setAppId(getAppId());
        pubInfo.setClientIP(getClientIP());
        pubInfo.setCountyCode(getCountyCode());
        pubInfo.setRegionCode(getRegionCode());
        pubInfo.setTransactionId(transactionId);
        pubInfo.setTransactionTime(transactionTime);


        send.setPubInfo(pubInfo);
        send.setRequest(request);

        return send;
    }

    /**
     * 组装相应信息 <p> outSysSn  外系统发起时流水 groupId   企业编码 phoneId   电话号码 prodId    产品编码
     */
    public RequestChargeObject constructRequestObject(String outSysSn, String groupId, String phoneId, String prodId) {
        RequestChargeObject object = new RequestChargeObject();

        ChargeBusiParams params = new ChargeBusiParams();
        params.setActType(getActType());
        params.setGroupId(groupId);
        params.setOutSysSn(outSysSn);
        params.setPhoneId(phoneId);
        params.setProdId(prodId);

        object.setBusiParams(params);
        object.setBusiCode(getBusiCodeQuery());

        return object;
    }

    /**
     * 得到发送的地址 <p> json:json对象
     */
    public String getSendUrl(String json) {
        try {
            String pubKeyStr = getPubKeyStr();
            String appId = getAppId();
            String bossUrl = getBossUrl();

            //产生16位动态加密串
            String content = new String(RSAUtil.getRequestKey());

            RSAPublicKey pubKey = RSAUtil.getPubKeyForEncode(pubKeyStr);

            String contentEncrypted = RSAUtil.RSAEncryptForClient(content, pubKey);


            //对json对象进行加密

            byte[] contentEncode = RSAUtil.AESEncrypt(json.getBytes(CHARSET_NAME), content.getBytes(CHARSET_NAME));

            String msgEncode = Base64.encodeBase64String(contentEncode);


            //将signData，appId，contentEncrypted，msgEncode按照顺序填充到字符串中

            String signData = "POST&/odp/v1/trans&app_id={0}&req_key={1}&req_msg={2}";

            signData = MessageFormat.format(signData, appId, contentEncrypted, msgEncode);

            signData = URLEncoder.encode(signData, CHARSET_NAME);

            String signKey = pubKeyStr + "&";

            byte[] signBinary = RSAUtil.HmacSHA1Signature(signData.getBytes(),

                    signKey.getBytes(CHARSET_NAME));

            String sign = Base64.encodeBase64String(signBinary);

            sign = URLEncoder.encode(sign, CHARSET_NAME);

            String out = "app_id={0}&req_key={1}&req_msg={2}&sign={3}";

            String req_url = bossUrl + MessageFormat.format(out, appId,

                    URLEncoder.encode(contentEncrypted, CHARSET_NAME),

                    URLEncoder.encode(msgEncode, CHARSET_NAME),

                    URLEncoder.encode(sign, CHARSET_NAME));

            return req_url;
        } catch (Exception e) {
            LOGGER.error("GetSendUrl出错. 错误信息为{}.", e.toString());
            return "";
        } finally {

        }

    }


    /**
     * 将解析后的数据，转化为ResponseSendData对象 <p> decryStr:已解码后的数据
     */
    private ResponseSendData getRecvObject(String decryStr) {

        GsonBuilder gb = new GsonBuilder();

        com.google.gson.Gson gson = gb.create();

        return gson.fromJson(decryStr, ResponseSendData.class);
    }

    private Account convertToAccount(ResponseQueryData rqd, Long prdId) {
        Enterprise enterprise = enterprisesService.selectByCode(rqd.getGroupId());
        if (enterprise == null) {
            LOGGER.error("查询企业信息返回空值. EntCode = {}. ", rqd.getGroupId());
            return null;
        }

        Account account = new Account();
        account.setEnterId(enterprise.getId());
        account.setOwnerId(enterprise.getId());
        account.setProductId(prdId);
        account.setCount(NumberUtils.toDouble(rqd.getBalance()) / 100); //原因是BOSS侧返回的是以分为单位的金额

        return account;
    }

    //根据BOSS侧传回的产品编码，批量地获取产品信息
    private List<Product> convertToProducts(ResponseQueryData rqd) {
        List<ProductObject> bossProducts = rqd.getProdList();

        Set<String> prdCodes = new HashSet<String>();
        for (ProductObject bossProduct : bossProducts) {
            prdCodes.add(bossProduct.getProdId());
        }

        LOGGER.info("BOSS侧返回的产品列表为: {}.", JSONObject.toJSONString(prdCodes));
        return productService.batchSelectByCodes(prdCodes);
    }

    @Override
    public BossOperationResult mdrcCharge(String cardNumber, String mobile) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getAppId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_NM_APPID.getKey());
    }

    public String getPubKeyStr() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_NM_PUBKEY.getKey());
    }

    public String getBusiCodeQuery() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_NM_BUSICODE_QUERY.getKey());
    }

    public String getClientIP() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_NM_CLIENT_IP.getKey());
    }

    public String getCountyCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_NM_COUNTRY_CODE.getKey());
    }

    public String getRegionCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_NM_REGION_CODE.getKey());
    }

    public String getActType() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_NM_ACT_TYPE.getKey());
    }

    public String getBossUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_NM_BOSS_URL.getKey());
    }
}
