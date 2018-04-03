/**
 *
 */
package com.cmcc.vrp.sms.sichuan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.MessageType;
import com.cmcc.vrp.province.model.SCMessage.BusiParam;
import com.cmcc.vrp.province.model.SCMessage.SystemParam;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.sms.SendMessageService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpConnection;
import com.cmcc.vrp.util.MyRSA;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JamieWu
 */
public class SCSendMessageServiceImpl implements SendMessageService {

    private static final Log logger = LogFactory.getLog(SCSendMessageServiceImpl.class);

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    @Qualifier("MDSendMessageService")
    private SendMessageService MDsendMessageService;

    @Override
    public boolean send(SmsPojo smsPojo) {
        BusiParam busiParam = new BusiParam();
        SystemParam sysParam = new SystemParam();

        String req = check(smsPojo);
        if (req != null) {
            logger.info("发送短信参数smsPojo对象异常:" + req);
        }

        String templateId1 = getTemplateId1();
        String templateId2 = getTemplateId2();
        String templateId3 = getTemplateId3();

        //短信类型：充值提醒
        if (smsPojo.getType().equals(MessageType.CHARGE_NOTICE.getCode())) {            
            if (smsPojo.getTemplateName().equals(templateId1)) {
                busiParam.setTemplateId(templateId1);
                Map map = new HashMap();
                map.put("data", smsPojo.getPrdName());
                String params = JSON.toJSONString(map);
                busiParam.setParams(params);
            }
            if (smsPojo.getTemplateName().equals(templateId3)) {
                busiParam.setTemplateId(templateId3);
                Map map = new HashMap();
                map.put("data", smsPojo.getPrdName());
                String params = JSON.toJSONString(map);
                busiParam.setParams(params);
            }
            if (smsPojo.getTemplateName().equals(templateId2)) {
                busiParam.setTemplateId(templateId2);
                Map map = new HashMap();
                map.put("company", smsPojo.getEntName());
                map.put("data", smsPojo.getPrdName());
                String params = JSON.toJSONString(map);
                busiParam.setParams(params);
            }

            busiParam.setPhone_no(smsPojo.getMobile());

            sysParam.setApp_id(getApp_id());

            sysParam.setMethod(getMethod());
            sysParam.setVersion(getVersion());
            sysParam.setStatus(getStatus());

            SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
            String timestamp = date.format(new Date());
            sysParam.setTimestamp(timestamp);

            String appKey = getAppKey(getApp_key(), timestamp);

            sysParam.setApp_key(appKey);

            String sign = null;
            try {
                sign = getSign(busiParam, sysParam);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            sysParam.setSign(sign);


            //生成请求参数
            List<BasicNameValuePair> formParams;
            try {
                formParams = getFormParam(sysParam, busiParam);
                logger.info("发送短信参数form参数:" + JSON.toJSONString(formParams));
                //发送http请求
                HttpConnection connection = new HttpConnection();
                String response = null;
                try {
                    response = connection.sendPostByForm(getUrl(), formParams);
                    logger.info("发送短信参数form参数:" + JSON.toJSONString(formParams) + ",发送短信返回：" + response);
                    if (response != null) {
                        JSONObject json = (JSONObject) JSONObject.parse(response);
                        String resCode = json.getString("res_code");
                        if ("0000".equals(resCode)) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                    logger.info("发送短信-http请求异常，原因：" + e.toString());
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }
        else{//当前短信验证码用漫道发送
            return MDsendMessageService.send(smsPojo);
        }

        return false;
    }

    /**
     * 将app_key进行MD5加密
     *
     * @param appKey
     * @param timstamp
     * @return
     */
    private String getAppKey(String appKey, String timestamp) {
        String str = appKey + timestamp;
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }


    /**
     * 对所有参数进行签名
     *
     * @param busiParam
     * @param sysParam
     * @return
     * @throws UnsupportedEncodingException
     */
    private String getSign(BusiParam busiParam, SystemParam sysParam) throws UnsupportedEncodingException {
        //1、拼接
        String str = "secret" + "app_id" + sysParam.getApp_id() + "app_key" + sysParam.getApp_key()
            + "method" + sysParam.getMethod() + "params" + busiParam.getParams() + "phone_no" + busiParam.getPhone_no()
            + "status" + sysParam.getStatus() + "templateId" + busiParam.getTemplateId()
            + "timestamp" + sysParam.getTimestamp() + "version" + sysParam.getVersion() + "secret";

        //str = "secretappKey2a7373bb7dba028d557237576e43595fapp_id10000231methodsendInfoNTKparams{\"company\":\"哇哈哈\",\"data\":\"30MB\"}phone_no18867103685status2templateId20158150timeStamp20160324110027version1.0secret";
        logger.debug(str);
        //str = "secretaccess_token3a17928265d8812db09f5925a3ac6d5fapp_id10000007methodsFreeMinQryphone_no13981177888timestamp20141202103001version1.0secret";

        //2、字符串做urlencode
        str = URLEncoder.encode(str, "UTF-8");
        logger.debug("[urlencode]" + str);
        //3、对字符串做MD5加密
        String md5Str = DigestUtils.md5DigestAsHex(str.getBytes());

        //4、私钥加密
        String signStr = "";
        try {
            signStr = MyRSA.sign(md5Str, getPrivate_key());

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        //5、urlencode
        str = URLEncoder.encode(signStr, "UTF-8");

        return str;
    }

    private List<BasicNameValuePair> getFormParam(SystemParam sysParam, BusiParam busiParam) throws UnsupportedEncodingException {
        List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>();

        BasicNameValuePair appId = new BasicNameValuePair("app_id", sysParam.getApp_id());
        BasicNameValuePair appKey = new BasicNameValuePair("app_key", sysParam.getApp_key());
        BasicNameValuePair timestamp = new BasicNameValuePair("timestamp", sysParam.getTimestamp());
        BasicNameValuePair version = new BasicNameValuePair("version", sysParam.getVersion());
        BasicNameValuePair method = new BasicNameValuePair("method", sysParam.getMethod());
        BasicNameValuePair status = new BasicNameValuePair("status", sysParam.getStatus());
        BasicNameValuePair sign = new BasicNameValuePair("sign", sysParam.getSign());
        BasicNameValuePair phone_no = new BasicNameValuePair("phone_no", busiParam.getPhone_no());
        BasicNameValuePair templateId = new BasicNameValuePair("templateId", busiParam.getTemplateId());
        BasicNameValuePair params = new BasicNameValuePair("params", busiParam.getParams());
        formParams.add(appId);
        formParams.add(appKey);
        formParams.add(timestamp);
        formParams.add(version);
        formParams.add(method);
        formParams.add(status);
        formParams.add(sign);
        formParams.add(phone_no);
        formParams.add(templateId);
        formParams.add(params);

        return formParams;
    }

    private String check(SmsPojo smsPojo) {
        if (smsPojo == null) {
            return "smsPojo为空";
        }
        if (smsPojo.getType() == null) {
            return "smsPojo.getType为空";
        }
        return null;
    }

    public String getTemplateId1() {
        return globalConfigService.get(GlobalConfigKeyEnum.SC_MSG_TEMPLATE_ID1.getKey());
    }

    public String getTemplateId2() {
        return globalConfigService.get(GlobalConfigKeyEnum.SC_MSG_TEMPLATE_ID2.getKey());
    }

    public String getTemplateId3() {
        return globalConfigService.get(GlobalConfigKeyEnum.SC_MSG_TEMPLATE_ID3.getKey());
    }

    public String getUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.SC_MSG_SEND_URL.getKey());
    }

    public String getApp_id() {
        return globalConfigService.get(GlobalConfigKeyEnum.SC_MSG_APPID.getKey());
    }

    public String getApp_key() {
        return globalConfigService.get(GlobalConfigKeyEnum.SC_MSG_APPKEY.getKey());
    }

    public String getMethod() {
        return globalConfigService.get(GlobalConfigKeyEnum.SC_MSG_METHOD.getKey());
    }

    public String getVersion() {
        return globalConfigService.get(GlobalConfigKeyEnum.SC_MSG_VERSION.getKey());
    }

    public String getStatus() {
        return globalConfigService.get(GlobalConfigKeyEnum.SC_MSG_ENV.getKey());
    }

    public String getPrivate_key() {
        return globalConfigService.get(GlobalConfigKeyEnum.SC_MSG_PRIVATE_KEY.getKey());
    }
}
