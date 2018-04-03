package com.cmcc.vrp.sms.mandao;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.sms.SendMessageService;
import com.cmcc.vrp.sms.mandao.pojos.MessageConfig;
import com.cmcc.vrp.sms.mandao.pojos.ReturnCode;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;


/**
 * @ClassName: SendMessageServiceImpl
 * @Description: 发送短信服务实现类
 * @author: sunyiwei
 * @date: 2015年5月23日 上午11:15:32
 */
@Service("MDSendMessageService")
public class MDSendMessageServiceImpl implements SendMessageService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MDSendMessageServiceImpl.class);

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private GlobalConfigService globalConfigService;

    @Override
    public boolean send(SmsPojo smsPojo) {
        return smsPojo != null
            && send(buildConfig(smsPojo.getMobile(), smsPojo.getContent()));
    }

    private boolean send(MessageConfig mc) {
        if (validateMobiles(mc.getMobiles())
            && StringUtils.isNotBlank(mc.getContent())) {
            return sendInternal(mc);
        }

        return false;
    }

    private boolean sendInternal(MessageConfig mc) {
        HttpClient httpClient = HttpClients.createDefault();

        String requeryUrl = getRequeryUrl();

        try {
            //设置请求地址
            HttpPost httpPost = new HttpPost(requeryUrl);

            //设置请求参数
            httpPost.setEntity(buildResponseBody(mc));

            //发起请求
            HttpResponse response = httpClient.execute(httpPost);

            //解析响应
            String content = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
            int statusCode = response.getStatusLine().getStatusCode();
            ReturnCode returnCode = ReturnCode.fromCode(content);
            LOGGER.info("Send message to {} with requestBody = {} returns {}, and responseBody = {}.", requeryUrl, JSON.toJSON(mc),
                statusCode, returnCode == null ? content : returnCode.getDescription());

            return (statusCode == HttpResponseStatus.OK.code() && returnCode == null);
        } catch (IOException e) {
            LOGGER.error("Send message to {} with parameters {} throws a IOException.", requeryUrl, JSON.toJSON(mc));
            LOGGER.error("Exception stack is as follows: " + e);
        }

        return false;
    }

    private HttpEntity buildResponseBody(MessageConfig mc) throws IOException {
        List<NameValuePair> nvps = buildNameValuePairs(mc);

        return new UrlEncodedFormEntity(nvps, Charsets.UTF_8);
    }

    private List<NameValuePair> buildNameValuePairs(MessageConfig mc) {
        String sn = getSn();
        String pwd = getPwd();
        String sign = getSign();

        List<NameValuePair> nvps = new LinkedList<NameValuePair>();
        nvps.add(new BasicNameValuePair("sn", sn));
        nvps.add(new BasicNameValuePair("pwd", DigestUtils.md5Hex(sn + pwd).toUpperCase()));
        nvps.add(new BasicNameValuePair("mobile", StringUtils.join(mc.getMobiles(), ",")));
        nvps.add(new BasicNameValuePair("content", mc.getContent() + sign));
        nvps.add(new BasicNameValuePair("ext", mc.getExt()));
        nvps.add(new BasicNameValuePair("stime", mc.getStime() == null ? null : sdf.format(mc.getStime())));
        nvps.add(new BasicNameValuePair("rrid", mc.getRrid()));
        nvps.add(new BasicNameValuePair("msgfmt", mc.getMsgfmt() == null ? null : mc.getMsgfmt().getValue()));

        return nvps;
    }

    private boolean validateMobiles(List<String> mobiles) {
        if (mobiles == null
            || mobiles.isEmpty()) {
            return false;
        }

        for (String mobile : mobiles) {
            if (!isValidMobile(mobile)) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidMobile(String mobile) {
        return StringUtils.isNotBlank(mobile) && mobile.length() == 11;
    }

    private MessageConfig buildConfig(String mobile, String content) {
        List<String> mobiles = new LinkedList<String>();
        mobiles.add(mobile);

        return buildConfig(mobiles, content);
    }

    private MessageConfig buildConfig(List<String> mobiles, String content) {
        MessageConfig config = new MessageConfig();

        config.setMobiles(mobiles);
        config.setContent(content);

        return config;
    }

    public String getRequeryUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.MD_URL.getKey());
    }

    public String getSn() {
        return globalConfigService.get(GlobalConfigKeyEnum.MD_SN.getKey());
    }

    public String getPwd() {
        return globalConfigService.get(GlobalConfigKeyEnum.MD_PWD.getKey());
    }

    public String getSign() {
        return globalConfigService.get(GlobalConfigKeyEnum.MD_SIGN.getKey());
    }
}
