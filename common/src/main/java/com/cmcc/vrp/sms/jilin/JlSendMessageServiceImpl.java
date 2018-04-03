package com.cmcc.vrp.sms.jilin;

import com.google.gson.Gson;

import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.enums.MessageType;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.sms.SendMessageService;
import com.cmcc.vrp.sms.jilin.model.Body;
import com.cmcc.vrp.sms.jilin.model.Header;
import com.cmcc.vrp.sms.jilin.model.Params;
import com.cmcc.vrp.sms.jilin.model.Req;
import com.cmcc.vrp.sms.jilin.model.Root;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.StringUtils;
import com.cmcc.vrp.util.HttpUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>Title: </p> <p>Description: </p>
 *
 * @author lgk8023
 * @date 2017年4月25日 上午8:36:41
 */
public class JlSendMessageServiceImpl implements SendMessageService {
    private static final Log logger = LogFactory.getLog(JlSendMessageServiceImpl.class);
    private static String TIME_FOMMAT = "yyyyMMddHHmmss";
    @Autowired
    GlobalConfigService globalConfigService;

    public static void main(String[] args) {
        JlSendMessageServiceImpl sendMessageServiceImpl = new JlSendMessageServiceImpl();
        SmsPojo smsPojo = new SmsPojo();
        smsPojo.setMobile("18867105766");
        smsPojo.setContent("123456");
        smsPojo.setType(MessageType.RANDOM_CODE.getCode());
        System.out.println(sendMessageServiceImpl.send(smsPojo));
    }

    @Override
    public boolean send(SmsPojo smsPojo) {
        logger.info("吉林发送短信消息！");
        String req = check(smsPojo);
        if (req != null) {
            logger.info("发送短信参数smsPojo对象异常:" + req);
        }
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FOMMAT);
        String requestTime = sdf.format(new Date());
        String traceId = SerialNumGenerator.genRandomNum(10);
        String parentCallId = "9000" + requestTime + SerialNumGenerator.genRandomNum(6);
        if (smsPojo.getType().equals(MessageType.RANDOM_CODE.getCode())) {
            String json = buildJson(smsPojo, traceId, parentCallId);
            logger.info("发送短信消息{}" + json);
            System.out.println("发送短信消息{}" + json);
            String url = getUrl();
            String response = HttpUtils.post(url, json, "text/plain");
            logger.info("短信消息返回" + response);
            System.out.println(response);
            try {
                if (!StringUtils.isEmpty(response)) {
                    Gson gson = new Gson();
                    Req resp = gson.fromJson(response, Req.class);
                    if ("0".equals(resp.getROOT().getBODY().getRETURN_CODE())) {
                        return true;
                    }
                }
            } catch (Exception e) {
                return false;
            }

        }
        return false;
    }

    private String buildJson(SmsPojo smsPojo, String traceId, String parentCallId) {
        Body body = new Body();
        Header header = new Header();
        Root root = new Root();
        Req req = new Req();
        body.setSYSID("9");
        body.setSEQ(parentCallId);
        body.setTEMPLATEID(getTemplateId());
        Params params = new Params();
        params.setMsg(smsPojo.getContent());
        body.setPARAMS(params);
        body.setSERVICENO("");
        body.setPHONENO(smsPojo.getMobile());
        body.setLOGINNO("mnp");
        body.setSERVNO("");
        body.setSERVNAME("");
        body.setSUBPHONESEQ("");
        body.setSENDTIME("");
        body.setHOLD1("0");
        body.setHOLD2("");
        body.setHOLD3("");
        body.setHOLD4("");
        body.setHOLD5("");

        header.setTRACE_ID(traceId);
        header.setPARENT_CALL_ID(parentCallId);
        header.setKEEP_LIVE("");
        root.setHEADER(header);
        root.setBODY(body);
        req.setROOT(root);
        Gson gson = new Gson();

        return gson.toJson(req);
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

    private String getTemplateId() {
        //return "97775643";
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_JILIN_TEMPLATEID.getKey());
    }

    private String getUrl() {
        //return "http://10.163.106.148:51000/esbWS/rest/com_sitech_miso_hsf_SmsService_send";
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_JILIN_MSG_URL.getKey());
    }
}
