/**
 * @Title: NMSendMessageImpl.java
 * @Package com.cmcc.vrp.neimenggu.boss.json.service
 * @author: qihang
 * @date: 2016年2月22日 上午11:33:07
 * @version V1.0
 */
package com.cmcc.vrp.sms.neimenggu;

import com.cmcc.vrp.boss.neimenggu.NMBossUtils;
import com.cmcc.vrp.boss.neimenggu.model.PubInfo;
import com.cmcc.vrp.boss.neimenggu.model.resp.RespInfoObject;
import com.cmcc.vrp.boss.neimenggu.model.sms.RequestSmsObject;
import com.cmcc.vrp.boss.neimenggu.model.sms.SendSmsObject;
import com.cmcc.vrp.boss.neimenggu.model.sms.SmsBusiParams;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.sms.SendMessageService;
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

import java.net.URLEncoder;
import java.security.interfaces.RSAPublicKey;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: NMSendMessageImpl
 * @Description: 发短信服务
 * @author: qihang
 * @date: 2016年2月22日 上午11:33:07
 */
public class NMSendMessageImpl implements SendMessageService {
    public static final String flag = "WSZF";
    private static final String CHARSET_NAME = Charsets.UTF_8.name();
    private static Logger LOGGER = LoggerFactory.getLogger(NMSendMessageImpl.class);
    @Autowired
    private GlobalConfigService globalConfigService;

    @Override
    public boolean send(SmsPojo smsPojo) {
        String mobile = null;
        String content = null;

        if (smsPojo == null
            || (StringUtils.isBlank(mobile = smsPojo.getMobile()))
            || (StringUtils.isBlank(content = smsPojo.getContent()))) {
            return false;
        }

        String transactionId = "1";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        String transactionTime = dateFormat.format(new Date());

        //得到json参数和url地址
        String json = getSendJson(transactionId, transactionTime, mobile, content);
        String url = getSendUrl(json);

        LOGGER.info("短信接口发送的json数据为：" + json);
        LOGGER.info("短信接口的url地址为：" + url);

        //调用http，返回数据
        String returnData = HttpUtils.post(url, json);

        //解析返回的密文数据，得到对象
        RespInfoObject object = new NMBossUtils(getPubKeyStr()).getRespInfo(returnData);

        //判断是否成功
        if (object.isSussess()) {
            LOGGER.info("向手机号" + mobile + "发送短信:" + content + " 成功.");
            return true;
        } else {
            LOGGER.error("向手机号" + mobile + "发送短信:" + content + " 失败.");
            return false;
        }
    }


    /**
     * 得到发送到boss的String对象
     */
    public String getSendJson(String transactionId, String transactionTime, String phone_id, String message) {
        RequestSmsObject request = constructRequestObject(phone_id, message);
        SendSmsObject send = getSendSmsObject(request, transactionId, transactionTime);
        GsonBuilder gb = new GsonBuilder();
        com.google.gson.Gson gson = gb.create();
        gb.setDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        return gson.toJson(send);
    }

    /**
     * 得到最终查询发送的对象。
     * transactionId 外系统发起时流水
     * transactionTime 交易时间
     */
    public SendSmsObject getSendSmsObject(RequestSmsObject request, String transactionId, String transactionTime) {
        SendSmsObject send = new SendSmsObject();

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

    public RequestSmsObject constructRequestObject(String phone_id, String message) {
        SmsBusiParams params = new SmsBusiParams();
        params.setPhone_id(phone_id);
        params.setMessage(message);
        params.setFlag(flag);

        RequestSmsObject sendObject = new RequestSmsObject();
        sendObject.setBusiCode(getBusiCodeSms());
        sendObject.setBusiParams(params);

        return sendObject;
    }


    /**
     * 得到发送的地址
     * <p>
     * json:json对象
     */
    public String getSendUrl(String json) {
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
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            return "";
        } finally {

        }

    }

    public String getAppId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_NM_APPID.getKey());
    }

    public String getPubKeyStr() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_NM_PUBKEY.getKey());
    }

    public String getBusiCodeSms() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_NM_BUSICODE_SMS.getKey());
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
