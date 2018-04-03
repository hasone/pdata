package com.cmcc.vrp.sms.opt;

import com.google.gson.Gson;

import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.sms.SendMessageService;
import com.cmcc.vrp.sms.opt.enums.OptMsgNeedReceiptEnum;
import com.cmcc.vrp.sms.opt.enums.OptMsgRespEnum;
import com.cmcc.vrp.sms.opt.pojos.OptMsgSendRequestPojo;
import com.cmcc.vrp.sms.opt.pojos.OptMsgSendResponsePojo;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.cmcc.vrp.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 开放平台短信接入服务
 *
 * 文档地址: http://www.openservice.com.cn/msg.action
 *
 * 登录账号: sunyiwei@cmhi.chinamobile.com 登录密码: MjIyYTg5OGM1N2FhMzlk
 *
 * Created by sunyiwei on 2017/2/13.
 */
public class OptSendMessageServiceImpl implements SendMessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OptSendMessageServiceImpl.class);

    @Autowired
    private GlobalConfigService globalConfigService;

    @Override
    public boolean send(SmsPojo smsPojo) {
        Gson gson = new Gson();

        //1. 校验
        if (!validate(smsPojo)) {
            LOGGER.error("校验平台短信对象时不通过, SmsPojo = {}.", gson.toJson(smsPojo));
            return false;
        }

        //2. 将平台短信对象转化成开放平台短信对象
        OptMsgSendRequestPojo optMsgSendRequestPojo = buildReq(smsPojo);

        //3. 发送请求
        String responseBody = sendInternal(optMsgSendRequestPojo);
        LOGGER.info("开放平台短信通道请求地址为{}, 请求内容为{}, 响应内容为{}.", getSendAddr(), gson.toJson(optMsgSendRequestPojo), responseBody);

        //4. 解析应答
        OptMsgSendResponsePojo optMsgSendResponsePojo = buildResp(responseBody);

        //不为空,且结果码为OK
        return optMsgSendResponsePojo != null
                && OptMsgRespEnum.OK.getCode().equalsIgnoreCase(optMsgSendResponsePojo.getResultCode());
    }

    //向开放平台侧发送短信请求, 返回响应内容
    private String sendInternal(OptMsgSendRequestPojo pojo) {
        String requestUrl = getSendAddr();
        return HttpUtils.post(requestUrl, new Gson().toJson(pojo), "application/json");
    }

    //校验短信对象
    private boolean validate(SmsPojo smsPojo) {
        return smsPojo != null //短信对象不为空
                && StringUtils.isValidMobile(smsPojo.getMobile()) //有效的手机号码
                && org.apache.commons.lang.StringUtils.isNotBlank(smsPojo.getContent()); //短信内容不为空
    }

    //将应答消息转化成响应对象
    private OptMsgSendResponsePojo buildResp(String responseBody) {
        try {
            return new Gson().fromJson(responseBody, OptMsgSendResponsePojo.class);
        } catch (Exception e) {
            LOGGER.error("短信发送应答内容转化成应答 对象时出错, 错误信息为{}, 错误堆栈为{}.", e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    //转换成开放平台指定的格式
    private OptMsgSendRequestPojo buildReq(SmsPojo smsPojo) {
        OptMsgSendRequestPojo omsrp = new OptMsgSendRequestPojo();
        omsrp.setApiKey(getAppKey());
        omsrp.setSecretKey(getSecretKey());
        omsrp.setDestAddr(smsPojo.getMobile());
        omsrp.setMessage(smsPojo.getContent());
        omsrp.setNeedReceipt(OptMsgNeedReceiptEnum.NOT_NEED.getValue());
        omsrp.setReceiptNotificationURL("");

        return omsrp;
    }



    public String getAppKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.OPT_MSG_APP_KEY.getKey());
    }

    public String getSecretKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.OPT_MSG_SECRET_KEY.getKey());
    }


    public String getSendAddr() {
        return globalConfigService.get(GlobalConfigKeyEnum.OPT_MSG_REQ_ADDR.getKey());
    }
}
