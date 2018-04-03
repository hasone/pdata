/**
 * @Title: NMBossQueryServiceImpl.java
 * @Package com.cmcc.vrp.neimenggu.boss.service
 * @author: qihang
 * @date: 2016年2月2日 下午3:52:00
 * @version V1.0
 */
package com.cmcc.vrp.boss.neimenggu;

import com.cmcc.vrp.boss.neimenggu.model.PubInfo;
import com.cmcc.vrp.boss.neimenggu.model.query.QueryBusiParams;
import com.cmcc.vrp.boss.neimenggu.model.query.RequestQueryObject;
import com.cmcc.vrp.boss.neimenggu.model.query.ResponseQueryData;
import com.cmcc.vrp.boss.neimenggu.model.query.SendQueryObject;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.cmcc.vrp.util.RSAUtil;
import com.google.gson.GsonBuilder;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.security.interfaces.RSAPublicKey;
import java.text.MessageFormat;

/**
 * @ClassName: NMBossQueryServiceImpl
 * @Description: Boss查询服务类
 * @author: qihang
 * @date: 2016年2月2日 下午3:52:00
 */
@Service
public class NMBossQueryServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(NMBossQueryServiceImpl.class);

    private static final String CHARSET_NAME = Charsets.UTF_8.name();

    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * 从boss得到json数据，并解析放到对象中
     * <p>
     * transactionId:客户端流水号
     * transactionTime：发起时间，由于boss端没有确定暂时写成String型
     * groupId：企业编码
     */
    public ResponseQueryData getRecvMsg(String transactionId, String transactionTime, String groupId) {
        String json = getQueryJson(transactionId, transactionTime, groupId);
        String url = getQueryUrl(json);

        LOGGER.info("查询接口发送的json数据为：" + json);
        LOGGER.info("查询接口的url地址为：" + url);

        String returnData = HttpUtils.post(url, json);
        String returnMsg = new NMBossUtils(getPubKeyStr()).getReturnMsg(returnData);

        LOGGER.info("得到的解析后的数据为：" + returnMsg);

        return getRecvObject(returnMsg);
    }

    /**
     * 得到json数据
     * <p>
     * transactionId:客户端流水号
     * transactionTime：发起时间，由于boss端没有确定暂时写成String型
     * groupId：企业编码
     */
    public String getQueryJson(String transactionId, String transactionTime, String groupId) {
        SendQueryObject sendObject = getSendQueryObject(transactionId, transactionTime, groupId);
        GsonBuilder gb = new GsonBuilder();
        com.google.gson.Gson gson = gb.create();
        gb.setDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        String msg = gson.toJson(sendObject);
        return msg;
    }


    /**
     * 得到发送的json对象
     * <p>
     * transactionId:客户端流水号
     * transactionTime：发起时间，由于boss端没有确定暂时写成String型
     * groupId：企业编码
     */
    private SendQueryObject getSendQueryObject(String transactionId, String transactionTime, String groupId) {
        RequestQueryObject request = new RequestQueryObject();

        QueryBusiParams params = new QueryBusiParams();
        params.setGroupId(groupId);

        request.setBusiParams(params);
        request.setBusiCode(getBusiCodeQuery());

        PubInfo pubInfo = new PubInfo();
        pubInfo.setAppId(getAppId());
        pubInfo.setClientIP(getClientIP());
        pubInfo.setCountyCode(getCountyCode());
        pubInfo.setRegionCode(getRegionCode());
        pubInfo.setTransactionId(transactionId);
        pubInfo.setTransactionTime(transactionTime);


        SendQueryObject object = new SendQueryObject();
        object.setRequest(request);
        object.setPubInfo(pubInfo);

        return object;
    }

    /**
     * 得到发送的地址
     * <p>
     * json:json对象
     */
    public String getQueryUrl(String json) {
        try {
            String pubKeyStr = getPubKeyStr();
            String appId = getAppId();
            String bossUrl = getBossUrl();

            //产生16位动态加密串
            String content = RSAUtil.getRequestKey();

            RSAPublicKey pubKey = RSAUtil.getPubKeyForEncode(pubKeyStr);

            String contentEncrypted = RSAUtil.RSAEncryptForClient(content, pubKey);


            //对json对象进行加密

            byte[] contentEncode = RSAUtil.AESEncrypt(json.getBytes(CHARSET_NAME), content.getBytes(CHARSET_NAME));

            String msgEncode = Base64.encodeBase64String(contentEncode);


            //将signData，appId，contentEncrypted，msgEncode按照顺序填充到字符串中

            String signData = "POST&/odp/v1/trans&app_id={0}&req_key={1}&req_msg={2}";

            signData = MessageFormat.format(signData, getAppId(), contentEncrypted, msgEncode);

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
        } catch (RuntimeException e) {
            return "";
        } catch (Exception e) {
            return "";
        } finally {

        }
    }

    /**
     * 得到返回的对象
     * <p>
     * decryStr:已解码后的数据
     */
    public ResponseQueryData getRecvObject(String decryStr) {

        if (StringUtils.isEmpty(decryStr)) {
            return new ResponseQueryData();
        }

        GsonBuilder gb = new GsonBuilder();

        com.google.gson.Gson gson = gb.create();

        return gson.fromJson(decryStr, ResponseQueryData.class);
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
